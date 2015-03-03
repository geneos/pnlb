/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.logging.Level;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.compiere.util.CLogger;

/**
 *
 * @author alejandro
 */
public class HttpRequester {

	private final String hostURL;

	private final HashMap<String, String> httpHeaders;

	private final HashMap<String, List<String>> parameters;

	private final String nameService;

	private String resultFromPost;

	public static final String HTTP_REQUESTER_ERROR = "HTTP_REQUESTER_ERROR";
    private static CLogger logger = CLogger.getCLogger(HttpRequester.class);

	public HttpRequester(String url) {
		this(url, null);
	}

	public HttpRequester(String url, String service) {
		hostURL = url;
		nameService = service;
		parameters = new HashMap<String, List<String>>();
		httpHeaders = new HashMap<String, String>();
	}

	public String getHostURL() {
		return hostURL;
	}

	public void addParameter(String key, String value) {
		try {
			List<String> values;
			if (parameters.containsKey(key)) {
				values = parameters.get(key);
			} else {
				values = new ArrayList<String>();
			}

			values.add(URLEncoder.encode(value, "UTF-8"));
			parameters.put(key, values);
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Error at add parameter for HttpRequest ", e);
		} catch (NullPointerException e) {
			logger.log(Level.SEVERE, "Http Parameter is null:" + key + ";value");
		}
	}

	public void addHeader(String key, String value) {
		this.httpHeaders.put(key, value);
	}

	public boolean containsParameter(String key) {
		return parameters.containsKey(key);
	}

	public String sendGetRequest() {
		String result = null;
		if (hostURL.startsWith("http://")) {
			// Send a GET request to the servlet
			try {
				URL url = new URL(hostURL);
				URLConnection conn = url.openConnection();

				for (String key : this.httpHeaders.keySet()) {
					conn.setRequestProperty(key, this.httpHeaders.get(key));
				}

				// if (!this.httpHeaders.containsKey("Content-Type")) {
				// conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
				// }

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error sending HTTP request to" + hostURL, e);
			}
		}
		return result;
	}

	public void sendPostMessage() {
		BufferedReader in = null;
		try {

			URL dataURL = new URL(getHostURL());
			String charset = "UTF-8";

    		// Open the connection and prepare to POST
			URLConnection connection = dataURL.openConnection();

			// Make sure browser doesn't cache this URL.
			connection.setUseCaches(false);

			// Tell browser to allow me to send data to server.
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);

			// Grows if necessary Stream that writes into buffer
			final int sizeBuffer = 512;
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(sizeBuffer);
			PrintWriter out = new PrintWriter(byteStream, true);

			StringBuilder postData = new StringBuilder();

			if (nameService != null) {
				postData.append("service=");
				postData.append(nameService);
			}

			// for (String param : parameters.keySet()) {
			// String value = parameters.get(param);
			// postData.append("&").append(param).append("=").append(value);
			// }

			for (String param : parameters.keySet()) {
				List<String> values = parameters.get(param);
				for (String value : values) {
					postData.append("&").append(param).append("=").append(value);
				}
			}

			// Write POST data into local buffer
			out.print(postData.toString());
			out.flush(); // Flush since above used print, not println

			for (String key : this.httpHeaders.keySet()) {
				connection.setRequestProperty(key, this.httpHeaders.get(key));
			}

			// POST requests are required to have Content-Length
			String lengthString = String.valueOf(byteStream.size());
			connection.setRequestProperty("Content-Length", lengthString);

			connection.setConnectTimeout(30 * 1000);
			connection.setReadTimeout(30 * 1000);

			// Netscape sets the Content-Type to multipart/form-data
			// by default. So, if you want to send regular form data,
			// you need to set it to
			// application/x-www-form-urlencoded, which is the
			// default for Internet Explorer. If you send
			// serialized POST data with an ObjectOutputStream,
			// the Content-Type is irrelevant, so you could
			// omit this step.
			connection.setRequestProperty("Accept-Charset", charset);

			// TODO: Fix this in openfire plugin for accept charset in Content-Type
			// THIS IS HORRIBLE PLEASE URGENT FIX!!!!!!
			if (getHostURL().contains("plugins/zyncroextension/serverzyncro")) {
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			} else {
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			}

			// Write POST data to real output stream
			byteStream.writeTo(connection.getOutputStream());

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(charset)));

			String line;
			StringBuilder resultsArea = new StringBuilder("");
			while ((line = in.readLine()) != null) {
				resultsArea.append(line);
			}

			resultFromPost = resultsArea.toString();
			// logger.debug("Result of HTTP service " + resultFromPost);

		} catch (IOException ioe) {
			// Print debug info in Java Console
			logger.log(Level.SEVERE, "Error at HttpRequester" + ioe + " " + hostURL);
			resultFromPost = HTTP_REQUESTER_ERROR;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Can't close input stream", e);
				}
			}
		}
	}

	/**
	 * Upload files to url
	 *
	 * @param listFiles
	 */
	public void uploadFilesToUrl(ArrayList<File> listFiles) {
		PostMethod filePost = new PostMethod(getHostURL());
		// Disable USE_EXPECT_CONTINUE for problems with servers not HTTP/1.1
		filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);

		try {

			int totalFiles = listFiles.size();
			if (totalFiles <= 0) {
				logger.warning("Warning not upload files to " + hostURL + ", list files empty.");
				return;
			}

			Part[] parts = new FilePart[totalFiles];
			for (int idx = 0; idx < totalFiles; idx++) {
				File tgtFile = listFiles.get(idx);
				logger.log(Level.INFO, "Add File " + tgtFile.getName() + " for upload.");
				parts[idx] = new FilePart("uploadFile-" + idx, tgtFile);
			}

			// Set multipart content
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

			// Create Http client
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			// Upload files and get result
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				logger.info("Upload complete, response=" + filePost.getResponseBodyAsString());
			} else {
				logger.info("Upload failed, response=" + HttpStatus.getStatusText(status));
				resultFromPost = HTTP_REQUESTER_ERROR;
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error uploading files to " + getHostURL(), ex);
			resultFromPost = HTTP_REQUESTER_ERROR;
		} finally {
			filePost.releaseConnection();
		}
	}

	public String getResultFromPost() {
		return resultFromPost;
	}

}
