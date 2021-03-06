/*
 * Class .
 */
package org.compiere.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.compiere.db.CConnection;
import org.compiere.interfaces.MD5;
import org.compiere.interfaces.MD5Home;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Trx;
import org.compiere.utils.DBUtils;
import org.compiere.utils.DigestOfFile;

/**
 * @author rlemeill
 * originaly coming from an application note from compiere.co.uk
*/
public class ReportStarter implements ProcessCall {
//logger
	private static CLogger log = CLogger.getCLogger(ReportStarter.class);
	private static File REPORT_HOME = null;

    static {
        System.setProperty( "javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty( "org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");

        String reportPath = System.getProperty("org.compiere.report.path");
        if (reportPath == null) {
            REPORT_HOME = new File( System.getProperty("COMPIERE_HOME")+"/reports");
        } else {
            REPORT_HOME = new File( reportPath);
        }
    }

    
    /**
     * @param requestURL
     * @return true if the report is on the same ip address than Application Server
     */
    private boolean isRequestedonAS(URL requestURL)
    {
    	boolean tBool = false;
    	try{
    		InetAddress[] request_iaddrs = InetAddress.getAllByName(requestURL.getHost());
    		InetAddress as_iaddr = InetAddress.getByName(CConnection.get().getAppsHost());
    		for(int i=0;i<request_iaddrs.length;i++)
    		{
    			log.info("Got "+request_iaddrs[i].toString()+" for "+requestURL+" as address #"+i);
    			if(request_iaddrs[i].equals(as_iaddr))
    			{
    				log.info("Requested report is on application server host");
    				tBool = true;
    				break;
    			}
    		}
    	}
    	catch (UnknownHostException e) {
    		log.severe("Unknown dns lookup error");
    		return false;
    	}
    	return tBool;
    	
    }
    
    /**
     * @return true if the class org.compiere.interfaces.MD5Home is present
     */
    private boolean isMD5HomeInterfaceAvailable()
    {
    	try
		{
    		Class md5HomeClass = Class.forName("org.compiere.interfaces.MD5Home");
    		log.info("EJB client for MD5 remote hashing is present");
    		return true;
		}
    	catch (ClassNotFoundException e)
		{
    		log.warning("EJB Client for MD5 remote hashing absent\nyou need the class org.compiere.interfaces.MD5Home - from webEJB-client.jar - in classpath");
    		return false;	
		}
    }
    
    /**
     * @param requestedURLString
     * @return md5 hash of remote file computed directly on application server
     * 			null if problem or if report doesn't seem to be on AS (different IP or 404)
     */
    private String ejbGetRemoteMD5(String requestedURLString)
    {
		InitialContext context = null;
		String md5Hash = null;
    	try {
    		URL requestURL = new URL(requestedURLString);
    		String requestURLHost = requestURL.getHost();
    		Hashtable env = new Hashtable();
    		env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    		env.put(InitialContext.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    		env.put(InitialContext.PROVIDER_URL, requestURL.getHost() );
    		context = new InitialContext(env);
    		if (isRequestedonAS(requestURL) && isMD5HomeInterfaceAvailable())
    		{
    			MD5Home home = (MD5Home)context.lookup(MD5Home.JNDI_NAME);
    			MD5 md5 = home.create();
    			md5Hash = md5.getFileMD5(requestedURLString);
    			log.info("MD5 for " + requestedURLString + " is " + md5Hash);
    			md5.remove();
    		}
  
    	}
    	catch (MalformedURLException e) {
    		log.severe("URL is invalid: "+ e.getMessage());
    		return null;
    	}
    	catch (NamingException e){
    		log.warning("Unable to create jndi context did you deployed webApp.ear package?\nRemote hashing is impossible");
    		return null;
    	}
    	catch (RemoteException e){
    		log.warning("Unknown remote error exception");
    		return null;
    	}
    	catch(CreateException e){
    		log.warning("Error in RemoteInterface creation");
			return null;
    	}
    	catch(RemoveException e){
    		log.warning("Error in RemoteInterface removing");
    		return null;
    	}
    	return md5Hash;
    }
    
    /**
     * @author rlemeill
     * @param reportLocation http://applicationserver/webApp/standalone.jrxml for exemple
     * @param localPath Where to put the http downloadede file
     * @return abstract File wich represent the downloaded file
     */
    private File getRemoteFile(String reportLocation, String localPath)
    {
    	try{
    		URL reportURL = new URL(reportLocation); 
    		InputStream in = reportURL.openStream();
    		
    		String[] tmps = reportURL.getFile().split("/");
    		
    		String cleanFile = tmps[tmps.length-1];
    		
    		File downloadedFile = new File(localPath);

    		if (downloadedFile.exists())
    		{
    			downloadedFile.delete();
    		}
    		
    		FileOutputStream fout = new FileOutputStream(downloadedFile);
    		
    		int c;
    		while ((c = in.read()) != -1)
    		{
    			fout.write(c);
    		}
    		in.close();
    		fout.flush();
    		fout.close();
    		return downloadedFile;
    	}
    	catch (FileNotFoundException e) {
    		log.severe("404 not found: Report cannot be found on server "+ e.getMessage());
    		return null;
    	}    
    	catch (IOException e) {
    		log.severe("IO error when trying to download report from server "+ e.getMessage());
    		return null;
    	}	
    }
    
    /**
     * @author rlemeill
     * @param reportLocation http string url ex: http://compiereserver.domain.com/webApp/standalone.jrxml
     * @return downloaded File (or already existing one)
     */
    private File httpDownloadedReport(String reportLocation)
    {
    	File reportFile = null;
    	File downloadedFile = null;
    	log.info(" report deployed to " + reportLocation);
    	try {
    		
    		
    		String[] tmps = reportLocation.split("/");
    		String cleanFile = tmps[tmps.length-1];
    		String localFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + cleanFile;
    		String downloadedLocalFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator")+"TMP" + cleanFile;
    		
    		reportFile = new File(localFile);
    		
    		
    		if (reportFile.exists())
    		{
    			String localMD5hash = DigestOfFile.GetLocalMD5Hash(reportFile);
    			String remoteMD5Hash = ejbGetRemoteMD5(reportLocation);
    			log.info("MD5 for local file is "+localMD5hash );
    			if ( remoteMD5Hash != null)
    			{
    				if (localMD5hash.equals(remoteMD5Hash))
    				{
    					log.info(" no need to download: local report is up-to-date");
    				}
    				else
    				{
    					log.info(" report on server is different that local one, download and replace");
    					downloadedFile = getRemoteFile(reportLocation, downloadedLocalFile);
    					reportFile.delete();
    					downloadedFile.renameTo(reportFile);
    				}
    			}
    			else
    			{
    				log.warning("Remote hashing is not available did you deployed webApp.ear?");
    				downloadedFile = getRemoteFile(reportLocation, downloadedLocalFile);
    				//    				compare hash of existing and downloaded
    				if ( DigestOfFile.md5localHashCompare(reportFile,downloadedFile) )
    				{
    					//nothing file are identic
    					log.info(" no need to replace your existing report");
    				}
    				else
    				{
    					log.info(" report on server is different that local one, replacing");
    					reportFile.delete();
    					downloadedFile.renameTo(reportFile);
    				}
    			}
    		}
    		else
    		{
    			reportFile = getRemoteFile(reportLocation,localFile);
    		}
    		
    	}
    	catch (Exception e) {
    		log.severe("Unknown exception: "+ e.getMessage());
    		return null;
    	}
    	return reportFile;
    }
    
    /**
	 *  Start the process.
	 *  Called then pressing the Process button in R_Request.
	 *  It should only return false, if the function could not be performed
	 *  as this causes the process to abort.
	 *  @author rlemeill
	 *  @param ctx  context
	 *  @param pi            Compiere standard process info
	 *  @param trx
	 *  @return true if success
	 */
    public boolean startProcess(Properties ctx, ProcessInfo pi, Trx trx) {
		
		String Name=pi.getTitle();
        int AD_PInstance_ID=pi.getAD_PInstance_ID();
        int Record_ID=pi.getRecord_ID();
        log.info( "ReportStarter.startProcess Name="+Name+"  AD_PInstance_ID="+AD_PInstance_ID+" Record_ID="+Record_ID);

        ReportData reportData = getReportData( AD_PInstance_ID);
        if (reportData==null) {
            reportResult( AD_PInstance_ID, "Can not find report data");
            return false;
        }

        String reportPath =  reportData.getReportFilePath();
        if ((reportPath==null)||(reportPath.length()==0)) {
            reportResult( AD_PInstance_ID, "Can not find report");
            return false;
        }
        if (Record_ID!=-1) {
			JasperData data = null;
			File reportFile = null;
			HashMap params = new HashMap( ctx);
			
			addProcessParameters( AD_PInstance_ID, params);
			
			reportFile = getReportFile(reportPath, (String)params.get("ReportType"));
			
			if (reportFile == null || reportFile.exists() == false) 
			{
				log.warning("No report file found for given type, falling back to " + reportPath);
				reportFile = getReportFile(reportPath);
			}
			
			if (reportFile.exists() == false) {
				String tmp = "Can not find report file -" + reportFile.getAbsolutePath();
				log.severe(tmp);
				reportResult(AD_PInstance_ID, tmp);
			}

			if (reportFile != null)
				data = processReport(reportFile);
			else
				return false;
			
			JasperReport jasperReport = data.getJasperReport();
            String jasperName = data.getJasperName();
            File reportDir = data.getReportDir();

            if (jasperReport != null) {

                // Subreports
                File[] subreports = reportDir.listFiles( new FileFilter( jasperName+"Subreport", reportDir, ".xml"));
                for( int i=0; i<subreports.length; i++) {
                    JasperData subData = processReport( subreports[i]);
                    if (subData.getJasperReport()!=null) {
                        params.put( subData.getJasperName(), subData.getJasperFile().getAbsolutePath());
                    }
                }

                params.put("RECORD_ID", new Integer( Record_ID));

                Language currLang = Env.getLanguage(Env.getCtx());
                params.put("CURRENT_LANG", currLang.getAD_Language());
                // Resources				
                File[] resources = reportDir.listFiles( new FileFilter( jasperName, reportDir, ".properties"));
                File resFile = null;
                // try baseName + "_" + language
                for( int i=0; i<resources.length; i++) {
                    if ( resources[i].getName().equals( jasperName+currLang.getLocale().getLanguage()+".properties")) {
                        resFile=resources[i];
                        break;
                    }
                }
                if (resFile==null) {
                    // try baseName only
                    for( int i=0; i<resources.length; i++) {
                        if ( resources[i].getName().equals( jasperName+".properties")) {
                            resFile=resources[i];
                            break;
                        }
                    }
                }
                if (resFile!=null) {
                    try {
                        PropertyResourceBundle res = new PropertyResourceBundle( new FileInputStream(resFile));
                        params.put("RESOURCE", res);
                    } catch (IOException e) {
                        ;
                    }
                }

                try {
                    JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, params, DB.getConnectionRW());
                    if (reportData.isDirectPrint()) {
                        log.info( "ReportStarter.startProcess print report -"+jasperPrint.getName());
                        JasperPrintManager.printReport( jasperPrint, false);
                    } else {
                        log.info( "ReportStarter.startProcess run report -"+jasperPrint.getName());
                        JasperViewer jasperViewer = new JasperViewer( jasperPrint, pi.getTitle()+" - " + reportPath);
                        jasperViewer.show();
                    }
                } catch (JRException e) {
                    log.severe("ReportStarter.startProcess: Can not run report - "+ e.getMessage());
                }
            }

        }

        reportResult( AD_PInstance_ID, null);
        return true;
    }

    /**
     * @author alinv
     * @param reportPath
     * @param reportType
     * @return the abstract file corresponding to typed report
     */
	private File getReportFile(String reportPath, String reportType) {
		
		if (reportType != null)
		{
			int cpos = reportPath.lastIndexOf('.');
			reportPath = reportPath.substring(0, cpos) + "_" + reportType + reportPath.substring(cpos, reportPath.length());
		}
		
		return getReportFile(reportPath);
	}
	
	/**
	 * @author alinv
	 * @param reportPath
	 * @return the abstract file corresponding to report
	 */
	private File getReportFile(String reportPath) {
		File reportFile = null;
		
		// Reports deployement on web server Thanks to Alin Vaida
		if (reportPath.startsWith("http://")) {
			
			reportFile = httpDownloadedReport(reportPath);
			
		} else {
			reportFile = new File(REPORT_HOME, reportPath);
		}
		return reportFile;
	}

	/**
     * @author rlemeill
     * @param AD_PInstance_ID
     * @param errMsg
     */
    private void reportResult( int AD_PInstance_ID, String errMsg) {
        int result = (errMsg==null)?1:0;
        errMsg = (errMsg==null)?"":errMsg;
        String sql = "update AD_PInstance set result="+result+", errormsg='"+errMsg+"' "+
                     "where AD_PInstance_ID="+AD_PInstance_ID;
        Statement pstmt = null;
        try {
            pstmt = DB.createStatement();
            pstmt.executeUpdate(sql);
            pstmt.close();
        } catch (SQLException e) {
            log.severe(sql+e.getMessage());
        } finally {
            DBUtils.close( pstmt);
        }
    }

    /**
     * @author rlemeill
     * @param reportFile
     * @return
     */
    private JasperData processReport( File reportFile) {
        log.info( "ReportStarter.processReport - "+reportFile.getAbsolutePath());
        JasperReport jasperReport = null;

        String jasperName = reportFile.getName();
        int pos = jasperName.indexOf('.');
        if (pos!=-1) jasperName = jasperName.substring(0, pos);
        File reportDir = reportFile.getParentFile();
        
        //test if the compiled report exists
        File jasperFile = new File( reportDir.getAbsolutePath(), jasperName+".jasper");
        if (jasperFile.exists()) { // test time
            if (reportFile.lastModified() == jasperFile.lastModified()) {
            	log.info(" no need to compile use "+jasperFile.getAbsolutePath());
                try {
                    jasperReport = (JasperReport)JRLoader.loadObject(jasperFile.getAbsolutePath());
                } catch (JRException e) {
                    jasperReport = null;
                    log.severe("ReportStarter.processReport: Can not load report - "+ e.getMessage());
                }
            } else {
                jasperReport = compileReport( reportFile, jasperFile);
            }
        } else { // create new jasper file
            jasperReport = compileReport( reportFile, jasperFile);
        }

        return new JasperData( jasperReport, reportDir, jasperName, jasperFile);
    }


    private void addProcessParameters( int AD_PInstance_ID, Map params) {
        log.info(  "ReportStarter.addProcessParameters");
        String sql = "select ParameterName, "+
                        "P_String, "+
                        "P_String_To, "+
                        "P_Number, "+
                        "P_Number_To, "+
                        "P_Date, "+
                        "P_Date_To "+
                    "from AD_PInstance_Para where AD_PInstance_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            pstmt.setInt(1, AD_PInstance_ID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String pStr = rs.getString(2);
                String pStrTo = rs.getString(3);
                Double pNum = new Double( rs.getDouble(4));
                Double pNumTo = new Double( rs.getDouble(5));
                Timestamp pDate = rs.getTimestamp(6);
                Timestamp pDateTo = rs.getTimestamp(7);
                if (pStr != null) {
                    if (pStrTo!=null) {
                        params.put( name+"1", pStr);
                        params.put( name+"2", pStrTo);
                    } else {
                        params.put( name, pStr);
                    }
                } else if (pDate != null) {
                    if (pDateTo!=null) {
                        params.put( name+"1", pDate);
                        params.put( name+"2", pDateTo);
                    } else {
                        params.put( name, pDate);
                    }
                } else if (pNum != null) {
                    if (rs.getBigDecimal(5)!=null) {
                        params.put( name+"1", pNum);
                        params.put( name+"2", pNumTo);
                    } else {
                        params.put( name, pNum);
                    }
                }
            }
        } catch (SQLException e) {
            log.severe("ReportStarter.addProcessParameters - "+sql+" " +e.getMessage());
        } finally {
            DBUtils.close( rs);
            DBUtils.close( pstmt);
        }
    }

    /**
     * @author rlemeill
     * Correct the class path if loaded from java web start
     */
    private void JWScorrectClassPath()
    {
    	try
		{
    		Class jnlpClass = Class.forName("javax.jnlp.BasicService");
    		URL jasperreportsAbsoluteURL = Thread.currentThread().getContextClassLoader().getResource("net/sf/jasperreports/engine");
    		URL compiereJasperAbsoluteURL = Thread.currentThread().getContextClassLoader().getResource("org/compiere/utils");
    		String jasperreportsAbsolutePath = jasperreportsAbsoluteURL.toString().split("!")[0].split("file:")[1];
    		String compiereJasperAbsolutePath = compiereJasperAbsoluteURL.toString().split("!")[0].split("file:")[1];
    		compiereJasperAbsolutePath = compiereJasperAbsolutePath.replaceAll("%20"," ");
    		jasperreportsAbsolutePath = jasperreportsAbsolutePath.replaceAll("%20"," ");
    		String newClassPath = jasperreportsAbsolutePath + System.getProperty("path.separator") + compiereJasperAbsolutePath;
    		log.warning("JasperReports compilation is probably started from JavaWebStart");
    		log.info("classpath is corrected to "+newClassPath);
    		System.setProperty("java.class.path",newClassPath) ;

		}
    	catch (ClassNotFoundException e)
		{
		}
    }
    /**
     * @author rlemeill
     * @param reportFile
     * @param jasperFile
     * @return compiled JasperReport
     */
    private JasperReport compileReport( File reportFile, File jasperFile) {
    	JWScorrectClassPath();
        JasperReport compiledJasperReport = null;
        try {
          	JasperCompileManager.compileReportToFile ( reportFile.getAbsolutePath(), jasperFile.getAbsolutePath() );
            jasperFile.setLastModified( reportFile.lastModified()); //Synchronize Dates
            compiledJasperReport =  (JasperReport)JRLoader.loadObject(jasperFile);
        } catch (JRException e) {
            log.severe("ReportStarter.compileReport- "+ e.getMessage());
        }
        return compiledJasperReport;
    }

    /**
     * @author rlemeill
     * @param AD_PInstance_ID
     * @return ReportData
     */
    public ReportData getReportData( int AD_PInstance_ID) {
    	log.info( "ReportStarter.getReportFileName");
        String sql = "SELECT pr.JasperReport, pr.IsDirectPrint from AD_Process pr, AD_PInstance pi "+
                        "WHERE pr.AD_Process_Id=pi.AD_Process_Id "+
                        "AND pi.AD_PInstance_ID=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,"JasperReport");
            pstmt.setInt(1, AD_PInstance_ID);
            rs = pstmt.executeQuery();
            String path = null;
            String tmp = null;
            boolean directPrint;
            if (rs.next()) {
                path = rs.getString(1);
                tmp = rs.getString(2);
            } else {
                log.info("ReportStarter.getReportFileName data not found -"+sql);
            }
            return new ReportData( path, ((tmp.equalsIgnoreCase("y"))?true:false));
        } catch (SQLException e) {
            log.severe("ReportStarter.getReportFileName - "+sql+" "+ e.getMessage());
            return null;
        } finally {
            DBUtils.close( rs);
            DBUtils.close( pstmt);
        }
    }

    class ReportData {
        private String reportFilePath;
        private boolean directPrint;

        public ReportData(String reportFilePath, boolean directPrint) {
            this.reportFilePath = reportFilePath;
            this.directPrint = directPrint;
        }

        public String getReportFilePath() {
            return reportFilePath;
        }

        public boolean isDirectPrint() {
            return directPrint;
        }
    }

    class JasperData {
        private JasperReport jasperReport;
        private File reportDir;
        private String jasperName;
        private File jasperFile;

        public JasperData(JasperReport jasperReport, File reportDir, String jasperName, File jasperFile) {
            this.jasperReport = jasperReport;
            this.reportDir = reportDir;
            this.jasperName = jasperName;
            this.jasperFile = jasperFile;
        }

        public JasperReport getJasperReport() {
            return jasperReport;
        }

        public File getReportDir() {
            return reportDir;
        }

        public String getJasperName() {
            return jasperName;
        }

        public File getJasperFile() {
            return jasperFile;
        }
    }

    class FileFilter implements FilenameFilter {
        private String reportStart;
        private File directory;
        private String extension;

        public FileFilter(String reportStart, File directory, String extension) {
            this.reportStart = reportStart;
            this.directory = directory;
            this.extension = extension;
        }

        public boolean accept(File file, String name) {
            if (file.equals( directory)) {
                if (name.startsWith( reportStart)) {
                    int pos = name.lastIndexOf( extension);
                    if ( (pos!=-1) && (pos==(name.length()-extension.length()))) return true;
                }
            }
            return false;
        }
    }

}
