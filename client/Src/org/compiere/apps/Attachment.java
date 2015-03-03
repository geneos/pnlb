/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import com.qoppa.pdf.*;
import com.qoppa.pdfViewer.*;

/**
 *  Attachment Viewer
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Attachment.java,v 1.34 2005/12/31 06:32:06 jjanke Exp $
 */
public final class Attachment extends CDialog
	implements ActionListener
{
	/**
	 *	Constructor.
	 *	loads Attachment, if ID <> 0
	 *  @param frame frame
	 *  @param WindowNo window no
	 *  @param AD_Attachment_ID attachment
	 *  @param AD_Table_ID table
	 *  @param Record_ID record key
	 */
	public Attachment(Frame frame, int WindowNo, int AD_Attachment_ID,
		int AD_Table_ID, int Record_ID, String trxName)
	{
		super (frame, Msg.getMsg(Env.getCtx(), "Attachment"), true);
		//	needs to be modal otherwise APanel does not recongize change.
		log.config( "Attachment - ID=" + AD_Attachment_ID
			+ ", Table=" + AD_Table_ID + ", Record=" + Record_ID);
		//
		m_WindowNo = WindowNo;
		//
		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "Attachment", ex);
		}
		//	Create Model
		if (AD_Attachment_ID == 0)
			m_attachment = new MAttachment (Env.getCtx(), AD_Table_ID, Record_ID, trxName);
		else
			m_attachment = new MAttachment (Env.getCtx(), AD_Attachment_ID, trxName);
		loadAttachments();
		//
		try
		{
			AEnv.showCenterWindow(frame, this);
		}
		catch (Exception e)
		{
		}
		text.requestFocus();
	}	//	Attachment

	private int				m_WindowNo;
	private MAttachment		m_attachment;
	private boolean			m_change = false;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Attachment.class);
	
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CTextArea text = new CTextArea();
	private CButton bOpen = new CButton();
	private CButton bSave = new CButton();
	private CPanel northPanel = new CPanel();
	private CButton bLoad = new CButton();
	private BorderLayout northLayout = new BorderLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private CPanel toolBar = new CPanel(new FlowLayout(FlowLayout.LEADING, 5,5));
	private CButton bDelete = new CButton();
	private CButton bDeleteAll = null;
	private CComboBox cbContent = new CComboBox();
	private JSplitPane centerPane = new JSplitPane();
	//
	private CPanel graphPanel = new CPanel(new BorderLayout());
	private GImage gifPanel = new GImage();
	private JScrollPane gifScroll = new JScrollPane (gifPanel);
	private PDFViewerBean pdfViewer = Document.getViewer();
	private CTextArea info = new CTextArea();

	/**
	 *	Static setup.
	 *  <pre>
	 *  - northPanel
	 *      - toolBar
	 *      - title
	 *  - centerPane [split]
	 * 		- graphPanel (left)
	 *		  	- gifScroll - gifPanel
	 *			- pdfViewer
	 *  	- text (right)
	 *  - confirmPanel
	 *  </pre>
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		mainLayout.setHgap(5);
		mainLayout.setVgap(5);
		this.getContentPane().add(mainPanel);
		
		northPanel.setLayout(northLayout);
		northPanel.add(toolBar, BorderLayout.CENTER);
		toolBar.add(bLoad);
		toolBar.add(bDelete);
		toolBar.add(bSave);
		toolBar.add(bOpen);
		toolBar.add(cbContent);
		mainPanel.add(northPanel, BorderLayout.NORTH);

		//
		bOpen.setEnabled(false);
		bOpen.setIcon(Env.getImageIcon("Editor24.gif"));
		bOpen.setMargin(new Insets(0, 2, 0, 2));
		bOpen.setToolTipText(Msg.getMsg(Env.getCtx(), "Open"));
		bOpen.addActionListener(this);
		//
		bSave.setEnabled(false);
		bSave.setIcon(Env.getImageIcon("Export24.gif"));
		bSave.setMargin(new Insets(0, 2, 0, 2));
		bSave.setToolTipText(Msg.getMsg(Env.getCtx(), "AttachmentSave"));
		bSave.addActionListener(this);
		//
		bLoad.setIcon(Env.getImageIcon("Import24.gif"));
		bLoad.setMargin(new Insets(0, 2, 0, 2));
		bLoad.setToolTipText(Msg.getMsg(Env.getCtx(), "Load"));
		bLoad.addActionListener(this);
		//
		bDelete.setIcon(Env.getImageIcon("Delete24.gif"));
		bDelete.setMargin(new Insets(0, 2, 0, 2));
		bDelete.setToolTipText(Msg.getMsg(Env.getCtx(), "Delete"));
		bDelete.addActionListener(this);
		//
		Dimension size = cbContent.getPreferredSize();
		size.width = 200;
		cbContent.setPreferredSize(size);
	//	cbContent.setToolTipText(text);
		cbContent.addActionListener(this);
		cbContent.setLightWeightPopupEnabled(false);	//	Acrobat Panel is heavy
		//
		text.setBackground(CompierePLAF.getInfoBackground());
		text.setPreferredSize(new Dimension(200, 200));
		//
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);
		bDeleteAll = ConfirmPanel.createDeleteButton(true);
		confirmPanel.addButton(bDeleteAll);
		bDeleteAll.addActionListener(this);
		//
		info.setText("-");
		info.setReadWrite(false);
		graphPanel.add(info, BorderLayout.CENTER);
		//
		mainPanel.add(centerPane, BorderLayout.CENTER);
		centerPane.add(graphPanel, JSplitPane.LEFT);
		centerPane.add(text, JSplitPane.RIGHT);
		centerPane.setResizeWeight(.75);	//	more to graph
	}	//	jbInit

	
	/**
	 * 	Dispose
	 */
	public void dispose ()
	{
		pdfViewer = null;
		super.dispose ();
	}	//	dispose
	
	/**
	 *	Load Attachments
	 */
	private void loadAttachments()
	{
		log.config("");
		//	Set Text/Description
		String sText = m_attachment.getTextMsg();
		if (sText == null)
			text.setText("");
		else
			text.setText(sText);

		//	Set Combo
		int size = m_attachment.getEntryCount();
		for (int i = 0; i < size; i++)
			cbContent.addItem(m_attachment.getEntryName(i));
		if (size > 0)
			cbContent.setSelectedIndex(0);
		else
			displayData(0);
	}	//	loadAttachment

	/**
	 *  Display gif or jpg in gifPanel
	 * 	@param index index
	 */
	private void displayData (int index)
	{
		MAttachmentEntry entry = m_attachment.getEntry(index); 
		log.config("Index=" + index + " - " + entry);
		
		//	Reset UI
		gifPanel.setImage(null);
		graphPanel.removeAll();
		//
		bDelete.setEnabled(false);
		bOpen.setEnabled(false);
		bSave.setEnabled(false);

		Dimension size = null;
		//	no attachment
		if (entry == null || entry.getData() == null)
		{
			info.setText("-");
		}
		else
		{
			bOpen.setEnabled(true);
			bSave.setEnabled(true);
			bDelete.setEnabled(true);
			log.config(entry.toStringX());
			//
			info.setText(entry.toStringX());
			if (entry.isPDF() && pdfViewer != null)
			{
				try
				{
					pdfViewer.loadPDF(entry.getInputStream());
					pdfViewer.setScale(50);
					size = pdfViewer.getPreferredSize();
				//	size.width = Math.min(size.width, 400);
				//	size.height = Math.min(size.height, 400);
					//
					graphPanel.add(pdfViewer, BorderLayout.CENTER);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "(pdf)", e);
				}
			}
			else if (entry.isGraphic())
			{
				//  Can we display it
				Image image = Toolkit.getDefaultToolkit().createImage(entry.getData());
				if (image != null)
				{
					gifPanel.setImage(image);
					size = gifPanel.getPreferredSize();
					if (size.width == -1 && size.height == -1)
					{
						log.log(Level.SEVERE, "Invalid Image");
					}
					else
					{
						//	size.width += 40;
						//	size.height += 40;
						graphPanel.add(gifScroll, BorderLayout.CENTER);
					}
				}
				else
					log.log(Level.SEVERE, "Could not create image");
			}
		}
		if (graphPanel.getComponentCount() == 0)
		{
			graphPanel.add(info, BorderLayout.CENTER);
		}

		log.config("Size=" + size);
	//	graphPanel.setPreferredSize(size);
	//	centerPane.setDividerLocation(size.width+30);
	//	size.width += 100;
	//	size.height += 100;
	//	centerPane.setPreferredSize(size);
		pack();
	}   //  displayData


	/**
	 * 	Get File Name with index
	 *	@param index index
	 *	@return file name or null
	 */
	private String getFileName (int index)
	{
		String fileName = null;
		if (cbContent.getItemCount() > index)
			fileName = (String)cbContent.getItemAt(index);
		return fileName;
	}	//	getFileName

	/**
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
	//	log.config( "Attachment.actionPerformed - " + e.getActionCommand());
		//	Save and Close
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			String newText = text.getText();
			if (newText == null)
				newText = "";
			String oldText = m_attachment.getTextMsg();
			if (oldText == null)
				oldText = "";
			if (!m_change)
				m_change = !newText.equals(oldText);
			if (newText.length() > 0 || m_attachment.getEntryCount() > 0)
			{
				if (m_change)
				{
					m_attachment.setTextMsg(text.getText());
					m_attachment.save();
				}
			}
			else
				m_attachment.delete(true);
			dispose();
		}
		//	Cancel
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}
		//	Delete Attachment
		else if (e.getSource() == bDeleteAll)
		{
			deleteAttachment();
			dispose();
		}
		//	Delete individual entry and Return
		else if (e.getSource() == bDelete)
			deleteAttachmentEntry();
		//	Show Data
		else if (e.getSource() == cbContent)
			displayData (cbContent.getSelectedIndex());
		//	Load Attachment
		else if (e.getSource() == bLoad)
			loadFile();
		//	Open Attachment
		else if (e.getSource() == bSave)
			saveAttachmentToFile();
		//	Open Attachment
		else if (e.getSource() == bOpen)
		{
			if (!openAttachment())
				saveAttachmentToFile();
		}
	}	//	actionPerformed

	
	/**************************************************************************
	 *	Load file for attachment
	 */
	private void loadFile()
	{
		log.info("");
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "AttachmentNew"));
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		//
		String fileName = chooser.getSelectedFile().getName();
		log.config(fileName);
		File file = chooser.getSelectedFile();
		if (m_attachment.addEntry(file))
		{
			cbContent.addItem(fileName);
			cbContent.setSelectedIndex(cbContent.getItemCount()-1);
			m_change = true;
		}
	}	//	getFileName

	/**
	 *	Delete entire Attachment
	 */
	private void deleteAttachment()
	{
		log.info("");
		if (ADialog.ask(m_WindowNo, this, "AttachmentDelete?"))
			m_attachment.delete(true);
	}	//	deleteAttachment

	/**
	 *	Delete Attachment Entry
	 */
	private void deleteAttachmentEntry()
	{
		log.info("");
		int index = cbContent.getSelectedIndex();
		String fileName = getFileName(index);
		if (fileName == null)
			return;
		//
		if (ADialog.ask(m_WindowNo, this, "AttachmentDeleteEntry?", fileName))
		{
			if (m_attachment.deleteEntry(index))
				cbContent.removeItemAt(index);
			m_change = true;
		}
	}	//	deleteAttachment


	/**
	 *	Save Attachment to File
	 */
	private void saveAttachmentToFile()
	{
		int index = cbContent.getSelectedIndex();
		log.info("index=" + index);
		if (m_attachment.getEntryCount() < index)
			return;

		String fileName = getFileName(index);
		String ext = fileName.substring (fileName.lastIndexOf("."));
		log.config( "Ext=" + ext);

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "AttachmentSave"));
		File f = new File(fileName);
		chooser.setSelectedFile(f);
		//	Show dialog
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		File saveFile = chooser.getSelectedFile();
		if (saveFile == null)
			return;

		log.config("Save to " + saveFile.getAbsolutePath());
		m_attachment.getEntryFile(index, saveFile);
	}	//	saveAttachmentToFile

	/**
	 *	Open the temporary file with the application associated with the extension in the file name
	 *	@return true if file was opened with third party application
	 */
	private boolean openAttachment ()
    {
        int index = cbContent.getSelectedIndex();
        byte[] data = m_attachment.getEntryData(index);
        if (data == null)
            return false;
        
        try 
        {
            String fileName = System.getProperty("java.io.tmpdir") + m_attachment.getEntryName(index);
            File tempFile = new File(fileName);
            m_attachment.getEntryFile(index, tempFile);
        
            if (Env.isWindows())
            {
            //	Runtime.getRuntime().exec ("rundll32 url.dll,FileProtocolHandler " + url);
                Process p = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tempFile + "\"");
            //	p.waitFor();
                return true;
            }
            else if (Env.isMac())
            {
            	String [] cmdArray = new String [] {"open", tempFile.getAbsolutePath()};
            	Process p = Runtime.getRuntime ().exec (cmdArray);
            //	p.waitFor();
                return true;
            }
            else	//	other OS
            {
            }
        } 
        catch (Exception e) 
        {
        	log.log(Level.SEVERE, "", e);
        }
        return false;
    }    //    openFile

	
	/**************************************************************************
	 *  Graphic Image Panel
	 */
	class GImage extends JPanel
	{
		/**
		 *  Graphic Image
		 */
		public GImage()
		{
			super();
		}   //  GImage

		/** The Image           */
		private Image 			m_image = null;

		/**
		 *  Set Image
		 *  @param image image
		 */
		public void setImage (Image image)
		{
			m_image = image;
			if (m_image == null)
				return;

			MediaTracker mt = new MediaTracker(this);
			mt.addImage(m_image, 0);
			try {
				mt.waitForID(0);
			} catch (Exception e) {}
			Dimension dim = new Dimension(m_image.getWidth(this), m_image.getHeight(this));
			this.setPreferredSize(dim);
		}   //  setImage

		/**
		 *  Paint
		 *  @param g graphics
		 */
		public void paint (Graphics g)
		{
			Insets in = getInsets();
			if (m_image != null)
				g.drawImage(m_image, in.left, in.top, this);
		}   //  paint

		/**
		 *  Update
		 *  @param g graphics
		 */
		public void update (Graphics g)
		{
			paint(g);
		}   //  update
	}	//	GImage

	
	/**
	 * 	Test PDF Viewer
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
	}	//	main
	
}	//	Attachment
