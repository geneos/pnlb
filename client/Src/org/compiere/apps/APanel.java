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
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import org.compiere.apps.search.*;
import org.compiere.grid.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Main Application Panel.
 *  <pre>
 *  Structure:
 *      (MenuBar) -> to be added to owning window
 *		northPanel  (ToolBar)
 *		tabPanel
 *		southPanel  (StatusBar)
 *  </pre>
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: APanel.java,v 1.2 2006/10/09 13:41:00 SIGArg-01 Exp $
 */
public final class APanel extends CPanel
	implements DataStatusListener, ChangeListener, ActionListener, ASyncProcess
{
	/**
	 * Constructs a new instance.
	 * Need to call initPanel for dynamic initialization
	 */
	public APanel()
	{
		super();
		m_ctx = Env.getCtx();
		//
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		createMenu();
	}	//	APanel

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(APanel.class);
	
	/**
	 *	Dispose
	 */
	public void dispose()
	{
	//	log.config("");
		//  ignore changes
		m_disposing = true;
		//
		if (m_curAPanelTab != null)
		{
			m_curAPanelTab.unregisterPanel();
			m_curAPanelTab = null;
		}
		//  close panels
		tabPanel.dispose(this);
		tabPanel = null;
		//  All Workbenches
		for (int i = 0; i < m_mWorkbench.getWindowCount(); i++)
		{
			m_curWindowNo = m_mWorkbench.getWindowNo(i);
			log.info("#" + m_curWindowNo);
			Env.setAutoCommit(m_ctx, m_curWindowNo, false);
			m_mWorkbench.dispose(i);
			Env.clearWinContext(m_ctx, m_curWindowNo);
		}   //  all Workbenchens

		//  Get rid of remaining model
		if (m_mWorkbench != null)
			m_mWorkbench.dispose();
		m_mWorkbench = null;
		//  MenuBar
		if (menuBar != null)
			menuBar.removeAll();
		menuBar = null;
		//  ToolBar
		if (toolBar != null)
			toolBar.removeAll();
		toolBar = null;
		//  Prepare GC
		this.removeAll();
	}	//	dispose

	/**
	 * The Layout.
	 */
	private BorderLayout mainLayout = new BorderLayout();
	private VTabbedPane tabPanel = new VTabbedPane(true);
	private StatusBar statusBar = new StatusBar();
	private CPanel northPanel = new CPanel();
	private JToolBar toolBar = new JToolBar();
	private JMenuBar menuBar = new JMenuBar();
	private FlowLayout northLayout = new FlowLayout();

	/**
	 * Initializes the state of this instance.
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLocale(Language.getLoginLanguage().getLocale());
		this.setLayout(mainLayout);

		//	tabPanel
		mainLayout.setHgap(2);
		mainLayout.setVgap(2);
		this.add(tabPanel, BorderLayout.CENTER);
		//	southPanel
		this.add(statusBar, BorderLayout.SOUTH);
		//	northPanel
		this.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(northLayout);
		northLayout.setAlignment(FlowLayout.LEFT);
		northPanel.add(toolBar, null);
	}	//	jbInit

	private AppsAction 		aPrevious, aNext, aParent, aDetail, aFirst, aLast,
							aNew, aCopy, aDelete, aIgnore, aPrint,
							aRefresh, aHistory, aAttachment, aMulti, aFind,
							aWorkflow, aZoomAcross, aRequest, aWinSize, aArchive;
	public AppsAction		aSave, aLock;
	//	Local (added to toolbar)
	private AppsAction	    aReport, aEnd, aHome, aHelp, aProduct,
							aAccount, aCalculator, aCalendar, aEditor, aPreference, aScript,
							aOnline, aMailSupport, aAbout, aPrintScr, aScrShot, aExit, aBPartner;

	
	/**************************************************************************
	 *	Create Menu and Toolbar and registers keyboard actions.
	 *  - started from constructor
	 */
	private void createMenu()
	{
		/**
		 *	Menu
		 */
	//	menuBar.setHelpMenu();
		//								File
		JMenu mFile = AEnv.getMenu("File");
		menuBar.add(mFile);
		aPrintScr =	addAction("PrintScreen",	mFile,	KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, 0), 	false);
		aScrShot =	addAction("ScreenShot",		mFile,	KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, Event.SHIFT_MASK), 	false);
		aReport = 	addAction("Report",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0),	false);
		aPrint = 	addAction("Print",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0),	false);
		mFile.addSeparator();
		aEnd =	 	addAction("End",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK),	false);
		aExit =		addAction("Exit",			mFile, 	KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.SHIFT_MASK+Event.ALT_MASK),	false);
		//								Edit
		JMenu mEdit = AEnv.getMenu("Edit");
		menuBar.add(mEdit);
		aNew = 		addAction("New", 			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), false);
		aSave = 	addAction("Save",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0),	false);
		mEdit.addSeparator();
		aCopy =		addAction("Copy", 			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.SHIFT_MASK),	false);
		aDelete = 	addAction("Delete",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0),	false);
		aIgnore = 	addAction("Ignore",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),	false);
		aRefresh = 	addAction("Refresh",		mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),	false);
		mEdit.addSeparator();
		aFind = 	addAction("Find",			mEdit, 	KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), true);	//	toggle
		if (m_isPersonalLock)			
			aLock = addAction("Lock",			mEdit, 	null,	true);		//	toggle
		//								View
		JMenu mView = AEnv.getMenu("View");
		menuBar.add(mView);
		aProduct =	addAction("InfoProduct",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.ALT_MASK),	false);
		aBPartner =	addAction("InfoBPartner",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.SHIFT_MASK+Event.ALT_MASK),	false);
		if (MRole.getDefault().isShowAcct())
			aAccount =  addAction("InfoAccount",mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.ALT_MASK+Event.CTRL_MASK),	false);
		AEnv.addMenuItem("InfoSchedule", null, null, mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoOrder", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInvoice", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInOut", "Info", null, mView, this);
		AEnv.addMenuItem("InfoPayment", "Info", null, mView, this);
		AEnv.addMenuItem("InfoCashLine", "Info", null, mView, this);
		AEnv.addMenuItem("InfoAssignment", "Info", null, mView, this);
		AEnv.addMenuItem("InfoAsset", "Info", null, mView, this);
		mView.addSeparator();
		aAttachment = addAction("Attachment",	mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0),	true);		//	toggle
		aHistory = 	addAction("History",		mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0),	true);		//	toggle
		mView.addSeparator();
		aMulti =	addAction("Multi",			mView, 	KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0),	true);		//	toggle
		//								Go
		JMenu mGo = AEnv.getMenu("Go");
		menuBar.add(mGo);
		aFirst =	addAction("First", 			mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, Event.ALT_MASK),	false);
		aPrevious = addAction("Previous", 		mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_UP, Event.ALT_MASK),	false);
		aNext = 	addAction("Next", 			mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Event.ALT_MASK),	false);
		aLast =		addAction("Last",	 		mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, Event.ALT_MASK),	false);
		mGo.addSeparator();
		aParent =	addAction("Parent", 		mGo, 	KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Event.ALT_MASK),	false);
		aDetail =	addAction("Detail", 		mGo,	KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Event.ALT_MASK),	false);
		mGo.addSeparator();
		aZoomAcross = addAction("ZoomAcross",	mGo, 	null,	false);
		aRequest =  addAction("Request",		mGo, 	null,	false);
		aArchive =  addAction("Archive",		mGo, 	null,	false);
		aHome =		addAction("Home", 			mGo,	null,	false);
		//								Tools
		JMenu mTools = AEnv.getMenu("Tools");
		menuBar.add(mTools);
		aCalculator = addAction("Calculator",	mTools, 	null,	false);
		aCalendar = addAction("Calendar",		mTools, 	null,	false);
		aEditor =	addAction("Editor",			mTools, 	null,	false);
		aScript = addAction("Script",	        mTools, 	null,	false);
		if ("Y".equals(Env.getContext(m_ctx, "#SysAdmin")))	//	set in DB.loginDB
			aWinSize = addAction("WinSize",     mTools, 	null,	false);
		if (AEnv.isWorkflowProcess())
			aWorkflow = addAction("WorkFlow",	mTools,		null,	false);
		if (MRole.getDefault().isShowPreference())
		{
			mTools.addSeparator();
			aPreference = addAction("Preference",	mTools, 	null,	false);
		}
		//								Help
		JMenu mHelp = AEnv.getMenu("Help");
		menuBar.add(mHelp);
		aHelp = 	addAction("Help",			mHelp, 	KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),	false);
		aOnline =	addAction("Online",			mHelp, 	null,	false);
		aMailSupport = addAction("EMailSupport",	mHelp,	null,	false);
		aAbout = 	addAction("About",			mHelp, 	null,	false);

		/**
		 *	ToolBar
		 */
		toolBar.add(aIgnore.getButton());		//	ESC
		toolBar.addSeparator();
		toolBar.add(aHelp.getButton());			//	F1
		toolBar.add(aNew.getButton());
		toolBar.add(aDelete.getButton());
		toolBar.add(aSave.getButton());
		toolBar.addSeparator();
		toolBar.add(aRefresh.getButton());      //  F5
		toolBar.add(aFind.getButton());
		toolBar.add(aAttachment.getButton());
		toolBar.add(aMulti.getButton());
		toolBar.addSeparator();
		toolBar.add(aHistory.getButton());		//	F9
		toolBar.add(aHome.getButton());			//	F10 is Windows Menu Key
		toolBar.add(aParent.getButton());
		toolBar.add(aDetail.getButton());
		toolBar.addSeparator();
		toolBar.add(aFirst.getButton());
		toolBar.add(aPrevious.getButton());
		toolBar.add(aNext.getButton());
		toolBar.add(aLast.getButton());
		toolBar.addSeparator();
		toolBar.add(aReport.getButton());
		toolBar.add(aArchive.getButton());
		toolBar.add(aPrint.getButton());
		toolBar.addSeparator();
		if (m_isPersonalLock)
			toolBar.add(aLock.getButton());
		toolBar.add(aZoomAcross.getButton());
		if (aWorkflow != null)
			toolBar.add(aWorkflow.getButton());
		toolBar.add(aRequest.getButton());
		toolBar.add(aProduct.getButton());
		toolBar.addSeparator();
		toolBar.add(aEnd.getButton());
		//
		if (CLogMgt.isLevelAll())
			Util.printActionInputMap(this);
	}	//	createMenu


	/**
	 *	Add (Toggle) Action to Toolbar and Menu
	 *  @param actionName action name
	 *  @param menu manu
	 *  @param accelerator accelerator
	 *  @param toggle toggle button
	 *  @return AppsAction
	 */
	private AppsAction addAction (String actionName, JMenu menu, KeyStroke accelerator, boolean toggle)
	{
		AppsAction action = new AppsAction(actionName, accelerator, toggle);
		if (menu != null)
			menu.add(action.getMenuItem());
		action.setDelegate(this);
		AbstractButton b = action.getButton();
		String s = null;
		if (b != null)
			s = b.getToolTipText();
		
		//	Key Strokes
		if (accelerator != null)
		{
			getInputMap(WHEN_IN_FOCUSED_WINDOW).put(accelerator, actionName);
			getActionMap().put(actionName, action);
		}
		//
		return action;
	}	//	addAction

	/**
	 *	Return MenuBar
	 *  @return JMenuBar
	 */
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}	//	getMenuBar

	/**
	 *	Get Title of Window
	 *  @return String with Title
	 */
	public String getTitle()
	{
		if (m_mWorkbench.getWindowCount() > 1)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(m_mWorkbench.getName()).append("  ")
				.append(Env.getContext(m_ctx, "#AD_User_Name")).append("@")
				.append(Env.getContext(m_ctx, "#AD_Client_Name")).append(".")
				.append(Env.getContext(m_ctx, "#AD_Org_Name")).append(" [")
				.append(Env.getContext(m_ctx, "#DB_UID")).append("]");
			return sb.toString();
		}
		return Env.getHeader(m_ctx, m_curWindowNo);
	}	//	getTitle


	private Properties      m_ctx;

	/** Workbench Model                                 */
	private MWorkbench		m_mWorkbench;
	/** Current MTab                                    */
	private MTab			m_curTab;
	/** Current GridController                          */
	private GridController  m_curGC;
	/** Current Window Panel                            */
	private JTabbedPane     m_curWinTab = null;
	/** Current Window No                               */
	private int				m_curWindowNo;
	/** Current Window Panel Index                      */
	private int				m_curTabIndex = -1;
	/**	Current Tab Order								*/
	private APanelTab		m_curAPanelTab = null;

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	/** Save Error Message indicator                    */
	private boolean         m_errorDisplayed = false;
	/** Only current row flag                           */
	private boolean			m_onlyCurrentRows = true;
	/** Number of days to show	0=all					*/
	private int				m_onlyCurrentDays = 0;
	/** Process Info                                    */
	private boolean         m_isLocked = false;
	/** Show Personal Lock								*/
	private boolean 		m_isPersonalLock = MRole.getDefault().isPersonalLock();
	/**	Last Modifier of Action Event					*/
	private int 			m_lastModifiers;

	
	/**************************************************************************
	 *	Dynamic Panel Initialization - either single window or workbench.
	 *  <pre>
	 *  either
	 *  - Workbench tabPanel    (VTabbedPane)
	 *      - Tab               (GridController)
	 *  or
	 *  - Workbench tabPanel    (VTabbedPane)
	 *      - Window            (VTabbedPane)
	 *          - Tab           (GridController)
	 *  </pre>
	 *  tabPanel
	 *  @param AD_Workbench_ID  if > 0 this is a workbench, AD_Window_ID ignored
	 *  @param AD_Window_ID     if not a workbench, Window ID
	 *  @param query			if not a Workbench, Zoom Query - additional SQL where clause
	 *  @return true if Panel is initialized successfully
	 */
	public boolean initPanel (int AD_Workbench_ID, int AD_Window_ID, MQuery query)
	{
		log.info("WB=" + AD_Workbench_ID + ", Win=" + AD_Window_ID + ", Query=" + query);
		this.setName("APanel" + AD_Window_ID);

		//  Single Window
		if (AD_Workbench_ID == 0)
			m_mWorkbench = new MWorkbench(m_ctx, AD_Window_ID);
		else
		//  Workbench
		{
		//	m_mWorkbench = new MWorkbench(m_ctx);
		//	if (!m_mWorkbench.initWorkbench (AD_Workbench_ID))
		//	{
		//		log.log(Level.SEVERE, "APanel.initWindow - No Workbench Model");
		//		return false;
		//	}
		//	tabPanel.setWorkbench(true);
		//	tabPanel.addChangeListener(this);
			ADialog.warn(0, this, "","Not implemented yet");
			return false;
		}

		Dimension windowSize = m_mWorkbench.getWindowSize();
		
		/**
		 *  WorkBench Loop
		 */
		for (int wb = 0; wb < m_mWorkbench.getWindowCount(); wb++)
		{
			//  Get/set WindowNo
			m_curWindowNo = Env.createWindowNo (this);			                //  Timing: ca. 1.5 sec
			m_mWorkbench.setWindowNo(wb, m_curWindowNo);
			//  Set AutoCommit for this Window
			Env.setAutoCommit(m_ctx, m_curWindowNo, Env.isAutoCommit(m_ctx));
			boolean autoNew = Env.isAutoNew(m_ctx);
			Env.setAutoNew(m_ctx, m_curWindowNo, autoNew);

			//  Workbench Window
			VTabbedPane window = null;
			//  just one window
			if (m_mWorkbench.getWindowCount() == 1)
			{
				window = tabPanel;
				window.setWorkbench(false);
			}
			else
			{
				VTabbedPane tp = new VTabbedPane(false);
				window = tp;
			}
			//  Window Init
			window.addChangeListener(this);

			/**
			 *  Init Model
			 */
			int wbType = m_mWorkbench.getWindowType(wb);

			/**
			 *  Window
			 */
			if (wbType == MWorkbench.TYPE_WINDOW)
			{
				HashMap<Integer,GridController> includedMap = new HashMap<Integer,GridController>(4);
				//
				MWindowVO wVO = AEnv.getMWindowVO(m_curWindowNo, m_mWorkbench.getWindowID(wb), 0);
				if (wVO == null)
				{
					ADialog.error(0, null, "AccessTableNoView", "(No Window Model Info)");
					return false;
				}
				MWindow mWindow = new MWindow (wVO);			                //  Timing: ca. 0.3-1 sec
				//	Set SO/AutoNew for Window
				Env.setContext(m_ctx, m_curWindowNo, "IsSOTrx", mWindow.isSOTrx());
				
				/*
				 *10-01-2011 Camarzana Mariano
				 *Se setea IsReceipt en base a IsSOTrx para poder manejar adecuadamente 
				 *las ventanas Cobranzas y Pagos 
				 *
				 */
				Env.setContext(m_ctx, m_curWindowNo, "IsReceipt", mWindow.isSOTrx() ? "Y" : "N");
				
				// Fin modificacion 10-01-2011
				
				if (!autoNew && mWindow.isTransaction())
					Env.setAutoNew(m_ctx, m_curWindowNo, true);
				m_mWorkbench.setMWindow(wb, mWindow);
				if (wb == 0)
					m_onlyCurrentRows = mWindow.isTransaction();	//	default = only current
				if (windowSize == null)
					windowSize = mWindow.getWindowSize();

				/**
				 *  Window Tabs
				 */
				int tabSize = mWindow.getTabCount();
				boolean goSingleRow = query != null;	//	Zoom Query
				for (int tab = 0; tab < tabSize; tab++)
				{
					boolean included = false;
					//  MTab
					MTab mTab = m_mWorkbench.getMWindow(wb).getTab(tab);
					//  Query first tab
					if (tab == 0)
					{
						//  initial user query for single workbench tab
						if (m_mWorkbench.getWindowCount() == 1)
						{
							query = initialQuery (query, mTab);
							if (query != null && query.getRecordCount() <= 1)
								goSingleRow = true;
						}
						else if (wb != 0)
						//  workbench dynamic query for dependent windows
						{
							query = m_mWorkbench.getQuery();
						}
						//	Set initial Query on first tab
						if (query != null)
						{
							m_onlyCurrentRows = false;  //  Query might involve history
							mTab.setQuery(query);
						}
						if (wb == 0)
							m_curTab = mTab;
					}	//	query on first tab

					Component tabElement = null;
					//  GridController
					if (mTab.isSortTab())
					{
						VSortTab st = new VSortTab(m_curWindowNo, mTab.getAD_Table_ID(),
							mTab.getAD_ColumnSortOrder_ID(), mTab.getAD_ColumnSortYesNo_ID());
						st.setTabLevel(mTab.getTabLevel());
						tabElement = st;
					}
					else	//	normal tab
					{
						GridController gc = new GridController();			        //  Timing: ca. .1 sec
						CompiereColor cc = mWindow.getColor();
						if (cc != null)
							gc.setBackgroundColor(cc);                  //  set color on Window level
						gc.initGrid(mTab, false, m_curWindowNo, this, mWindow);  //  will set color on Tab level
																		//  Timing: ca. 6-7 sec for first .2 for next
						gc.addDataStatusListener(this);
						gc.registerESCAction(aIgnore);      //  register Escape Key
						//	Set First Tab
						if (wb == 0 && tab == 0)
						{
							m_curGC = gc;
							Dimension size = gc.getPreferredSize();     //  Screen Sizing
							size.width += 4;
							size.height += 4;
							gc.setPreferredSize(size);
						}
						tabElement = gc;
						//	If we have a zoom query, switch to single row
						if (tab == 0 && goSingleRow)
							gc.switchSingleRow();

						
                                                //begin vpj-cd e-evolution 08/10/2005
												//	Store GC if it has a included Tab
												//if (mTab.getIncluded_Tab_ID() != 0)
												//	includedMap.put(new Integer(mTab.getIncluded_Tab_ID()), gc);    
                                                MField[] fields = gc.getMTab().getFields();
                                                int m_tab_id = 0;
                                                for(int f =0 ; f < fields.length ; f ++)
                                                {
                                                    m_tab_id = fields[f].getIncluded_Tab_ID();
                                                    //System.out.println("mtab_id" + mtab_id);
                                                    if ( m_tab_id != 0)
                                                    {
                                                    includedMap.put(m_tab_id, gc);                                                                                                                                                    
                                                    }    
                                                }
                                                
                                        		
												
                                                //end e-evolution vpj-cd e-evolution 08/10/2005						

						//	Is this tab included?
						if (includedMap.size() > 0)
						{
							GridController parent = (GridController)includedMap.get(new Integer(mTab.getAD_Tab_ID()));
							if (parent != null)
							{
								//begin vpj-cd e-evolution 24.08.2005
                                //included = parent.includeTab(gc);
                                	included = parent.includeTab(gc,this);
                        			TabSwitcher ts = new TabSwitcher(parent, this);
                        			Component[] comp = parent.getvPanel().getComponents();
                        			for (int i = 0; i < comp.length; i++)
                        			{			                                        			   
                        				ts.addTabSwitchingSupport((JComponent)comp[i]);
                        			}
                        			ts = new TabSwitcher(gc, this);
                        			comp = gc.getvPanel().getComponents();
                        			for (int i = 0; i < comp.length; i++)
                        			{			                                        			   
                        				ts.addTabSwitchingSupport((JComponent)comp[i]);
                        			}
                        			ts = new TabSwitcher(gc, this);
                        			ts.addTabSwitchingSupport((JComponent)gc.getTable());
                        			//end vpj-cd e-evolution 14 Feb 2005
                        		
                                //end vpj-cd e-evolution 24.08.2005
								if (!included)
									log.log(Level.SEVERE, "Not Included = " + gc);
							}
						}
					}	//	normal tab

					if (!included)	//  Add to TabbedPane
					{
						StringBuffer tabName = new StringBuffer ();
						tabName.append ("<html>");
						if (mTab.isReadOnly())
							tabName.append("<i>");
						int pos = mTab.getName ().indexOf (" ");
						if (pos == -1)
							tabName.append (mTab.getName ()).append ("<br>&nbsp;");
						else
						{
							tabName.append (mTab.getName().substring (0, pos))
							  .append ("<br>")
							  .append (mTab.getName().substring(pos + 1));
						}
						if (mTab.isReadOnly())
							tabName.append("</i>");
						tabName.append ("</html>");
						//	Add Tab - sets ALT-<number> and Shift-ALT-<x>
						window.addTab (tabName.toString(), mTab.getIcon(), 
							tabElement, mTab.getDescription());
					}
				}   //  Tab Loop
			//  Tab background
			//	window.setBackgroundColor(new CompiereColor(Color.magenta, Color.green));
			}   //  Type-MWindow

			//  Single Workbench Window Tab
			if (m_mWorkbench.getWindowCount() == 1)
			{
				window.setToolTipText(m_mWorkbench.getDescription(wb));
			}
			else
			//  Add Workbench Window Tab
			{
				tabPanel.addTab(m_mWorkbench.getName(wb), m_mWorkbench.getIcon(wb), window, m_mWorkbench.getDescription(wb));
			}
			//  Used for Env.getHeader
			Env.setContext(m_ctx, m_curWindowNo, "WindowName", m_mWorkbench.getName(wb));
                        Env.setContext(m_ctx, m_curWindowNo, "WindowID", AD_Window_ID);

                        System.out.println("AD_WF_Process_ID " + Env.getContext(m_ctx, m_curWindowNo,"AD_WF_Process_ID"));

                        
		}   //  Workbench Loop

		//  stateChanged (<->) triggered
		toolBar.setName(getTitle());
		m_curTab.getTableModel().setChanged(false);
		//	Set Detail Button
		aDetail.setEnabled(0 != m_curWinTab.getTabCount()-1);

		
		if (windowSize != null)
			setPreferredSize(windowSize);
		Dimension size = getPreferredSize();
		log.info( "fini - " + size);
		m_curWinTab.requestFocusInWindow();
		return true;
	}	//	initPanel

	/**
	 * 	Get Current Window No
	 *	@return win no
	 */
	public int getWindowNo()
	{
		return m_curWindowNo;
	}	//	getWindowNo
	
	/**
	 * 	Initial Query
	 *	@param query initial query
	 *	@param mTab tab
	 *	@return query or null
	 */
	private MQuery initialQuery (MQuery query, MTab mTab)
	{
		//	We have a (Zoom) query
		if (query != null && query.isActive() && query.getRecordCount() < 10)
			return query;
		//
		StringBuffer where = new StringBuffer();
		//	Query automatically if high volume and no query
		boolean require = mTab.isHighVolume();
		if (!require && !m_onlyCurrentRows)				//	No Trx Window
		{
			String wh1 = mTab.getWhereExtended();
			if (wh1 == null || wh1.length() == 0)
				wh1 = mTab.getWhereClause();
			if (wh1 != null && wh1.length() > 0)
				where.append(wh1);
			//
			if (query != null)
			{
				String wh2 = query.getWhereClause();
				if (wh2.length() > 0)
				{
					if (where.length() > 0)
						where.append (" AND ");
					where.append(wh2);
				}
			}
			//
			StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ")
				.append(mTab.getTableName());
			if (where.length() > 0)
				sql.append(" WHERE ").append(where);
			//	Does not consider security
			int no = DB.getSQLValue(null, sql.toString());
			//
			require = MRole.getDefault().isQueryRequire(no);
		}
		//	Show Query
		if (require)
		{
			MField[] findFields = mTab.getFields();
			Find find = new Find (Env.getFrame(this), m_curWindowNo, mTab.getName(),
				mTab.getAD_Table_ID(), mTab.getTableName(), 
				where.toString(), findFields, 10);	//	no query below 10
			query = find.getQuery();
			find = null;
		}
		return query;
	}	//	initialQuery
	
	
	/**
	 *  Get Window Index
	 *  @return Window Index
	 */
	private int getWindowIndex()
	{
		//  only one window
		if (m_mWorkbench.getWindowCount() == 1)
			return 0;
		//  workbench
		return tabPanel.getSelectedIndex();
	}   //  getWindowIndex

	/**
	 *  Is first Tab (on Window)
	 *  @return true if the panel displays the first tab
	 */
	private boolean isFirstTab()
	{
		return m_curWinTab.getSelectedIndex() == 0;
	}   //  isFirstTab

	/**
	 * 	Get Window Image
	 *	@return image or null
	 */
	public Image getImage()
	{
		return m_mWorkbench.getImage(getWindowIndex());
	}	//	getImage
	
	
	/**************************************************************************
	 *	Data Status Listener (row change)			^ | v
	 *  @param e event
	 */
	public void dataStatusChanged (DataStatusEvent e)
	{
		if (m_disposing)
			return;
		log.info(e.getMessage());
		String dbInfo = e.getMessage();
		if (m_curTab != null && m_curTab.isQueryActive())
			dbInfo = "[ " + dbInfo + " ]";
		statusBar.setStatusDB(dbInfo, e);

		//	Set Message / Info
		if (e.getAD_Message() != null || e.getInfo() != null)
		{
			StringBuffer sb = new StringBuffer();
			String msg = e.getMessage();
			if (msg != null && msg.length() > 0)
				sb.append(Msg.getMsg(m_ctx, e.getAD_Message()));
			String info = e.getInfo();
			if (info != null && info.length() > 0)
			{
				if (sb.length() > 0 && !sb.toString().trim().endsWith(":"))
					sb.append(": ");
				sb.append(info);
			}
			if (sb.length() > 0)
			{
				int pos = sb.indexOf("\n");
				if (pos != -1)  // replace CR/NL
					sb.replace(pos, pos+1, " - ");
				setStatusLine (sb.toString (), e.isError ());
			}
		}

		//  Confirm Error
		if (e.isError() && !e.isConfirmed())
		{
			ADialog.error(m_curWindowNo, this, e.getAD_Message(), e.getInfo());
			e.setConfirmed(true);   //  show just once - if MTable.setCurrentRow is involved the status event is re-issued
			m_errorDisplayed = true;
		}
		//  Confirm Warning
		else if (e.isWarning() && !e.isConfirmed())
		{
			ADialog.warn(m_curWindowNo, this, e.getAD_Message(), e.getInfo());
			e.setConfirmed(true);   //  show just once - if MTable.setCurrentRow is involved the status event is re-issued
		}

		//	update Navigation
		boolean firstRow = e.isFirstRow();
		aFirst.setEnabled(!firstRow);
		aPrevious.setEnabled(!firstRow);
		boolean lastRow = e.isLastRow();
		aNext.setEnabled(!lastRow);
		aLast.setEnabled(!lastRow);

		//	update Change
		boolean changed = e.isChanged() || e.isInserting();
		boolean readOnly = m_curTab.isReadOnly();
		boolean insertRecord = !readOnly;
		if (insertRecord)
			insertRecord = m_curTab.isInsertRecord();
		aNew.setEnabled(!changed && insertRecord);
		aCopy.setEnabled(!changed && insertRecord);
		aRefresh.setEnabled(!changed);
		aDelete.setEnabled(!changed && !readOnly);
		//
		if (readOnly && m_curTab.isAlwaysUpdateField())
			readOnly = false;
		aIgnore.setEnabled(changed && !readOnly);
		aSave.setEnabled(changed && !readOnly);
		//
		//	No Rows
		if (e.getTotalRows() == 0 && insertRecord)
		{
			aNew.setEnabled(true);
			aDelete.setEnabled(false);
		}

		//	Single-Multi
		aMulti.setPressed(!m_curGC.isSingleRow());

		//	History	(on first Tab only)
		if (isFirstTab())
			aHistory.setPressed(!m_curTab.isOnlyCurrentRows());

		//	Transaction info
		String trxInfo = m_curTab.getTrxInfo();
		if (trxInfo != null)
			statusBar.setInfo(trxInfo);

		//	Check Attachment
		boolean canHaveAttachment = m_curTab.canHaveAttachment();		//	not single _ID column
		//
		if (canHaveAttachment && e.isLoading() && m_curTab.getCurrentRow() > e.getLoadedRows())
			canHaveAttachment = false;
		if (canHaveAttachment && m_curTab.getRecord_ID() == -1)    //	No Key
			canHaveAttachment = false;
		if (canHaveAttachment)
		{
			aAttachment.setEnabled(true);
			aAttachment.setPressed(m_curTab.hasAttachment());
		}
		else
			aAttachment.setEnabled(false);
		//	Lock Indicator
		if (m_isPersonalLock)
			aLock.setPressed(m_curTab.isLocked());

	//	log.info( "APanel.dataStatusChanged - fini", e.getMessage());
	}	//	dataStatusChanged

	/**
	 *	Set Status Line to text
	 *  @param text clear text
	 *  @param error error flag
	 */
	public void setStatusLine (String text, boolean error)
	{
		log.fine(text);
		statusBar.setStatusLine(text, error);
	}	//	setStatusLine

	/**
	 *	Indicate Busy
	 *  @param busy busy
	 */
	private void setBusy (boolean busy)
	{
		m_isLocked = busy;
		//
		JFrame frame = Env.getFrame(this);
		if (frame == null)  //  during init
			return;
		if (frame instanceof AWindow)
			((AWindow)frame).setBusy(busy);
	//	String processing = Msg.getMsg(m_ctx, "Processing");
		if (busy)
		{
	//		setStatusLine(processing);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else
		{
			this.setCursor(Cursor.getDefaultCursor());
			frame.setCursor(Cursor.getDefaultCursor());
			m_curGC.requestFocus();
	//		if (statusBar.getStatusLine().equals(processing))
	//			statusBar.setStatusLine("");
		}
	}	//	set Busy

	
	/**************************************************************************
	 *	Change Listener - (tab change)			<->
	 *  @param e event
	 */
	public void stateChanged (ChangeEvent e)
	{
		if (m_disposing)
			return;
		log.info(e.toString());
		setBusy(true);

		VTabbedPane tp = (VTabbedPane)e.getSource();
		boolean back = false;
		boolean isAPanelTab = false;

		//  Workbench Tab Change
		if (tp.isWorkbench())
		{
			int WBIndex = tabPanel.getSelectedIndex();
			m_curWindowNo = m_mWorkbench.getWindowNo(WBIndex);
			//  Window Change
			log.info("curWin=" + m_curWindowNo + " - Win=" + tp);
			if (tp.getSelectedComponent() instanceof JTabbedPane)
				m_curWinTab = (JTabbedPane)tp.getSelectedComponent();
			else
				throw new java.lang.IllegalArgumentException("Window does not contain Tabs");
			if (m_curWinTab.getSelectedComponent() instanceof GridController)
				m_curGC = (GridController)m_curWinTab.getSelectedComponent();
		//	else if (m_curWinTab.getSelectedComponent() instanceof APanelTab)
		//		isAPanelTab = true;
			else
				throw new java.lang.IllegalArgumentException("Window-Tab does not contain GridControler");
			//  change pointers
			m_curTabIndex = m_curWinTab.getSelectedIndex();
		}
		else
		{
			//  Just a Tab Change
			log.info("Tab=" + tp);
			m_curWinTab = tp;
			int tpIndex = m_curWinTab.getSelectedIndex();
			back = tpIndex < m_curTabIndex;
			GridController gc = null;
			if (m_curWinTab.getSelectedComponent() instanceof GridController)
				gc = (GridController)m_curWinTab.getSelectedComponent();
			else if (m_curWinTab.getSelectedComponent() instanceof APanelTab)
				isAPanelTab = true;
			else
				throw new java.lang.IllegalArgumentException("Tab does not contain GridControler");
			//  Save old Tab
			if (m_curGC != null)
			{
				m_curGC.stopEditor(true);
				//  has anything changed?
				if (m_curTab.needSave(true, false))
				{   //  do we have real change
					if (m_curTab.needSave(true, true))
					{
						//	Automatic Save
						if (Env.isAutoCommit(m_ctx, m_curWindowNo)
							& !m_curTab.dataSave(true))
						{   //  there is a problem, so we go back
							m_curWinTab.setSelectedIndex(m_curTabIndex);
							setBusy(false);
							return;
						}
						//  explicitly ask when changing tabs
						else if (ADialog.ask(m_curWindowNo, this, "SaveChanges?", m_curTab.getCommitWarning()))
						{   //  yes we want to save
							if (!m_curTab.dataSave(true))
							{   //  there is a problem, so we go back
								m_curWinTab.setSelectedIndex(m_curTabIndex);
								setBusy(false);
								return;
							}
						}
						else    //  Don't save
							m_curTab.dataIgnore();
					}
					else    //  new record, but nothing changed
						m_curTab.dataIgnore();
				}   //  there is a change
			}
			if (m_curAPanelTab != null)
			{
				m_curAPanelTab.saveData();
				m_curAPanelTab.unregisterPanel();
				m_curAPanelTab = null;
			}

			//	new tab
		//	if (m_curTabIndex >= 0)
		//		m_curWinTab.setForegroundAt(m_curTabIndex, CompierePLAF.getTextColor_Normal());
		//	m_curWinTab.setForegroundAt(tpIndex, CompierePLAF.getTextColor_OK());
			m_curTabIndex = tpIndex;
			if (!isAPanelTab)
				m_curGC = gc;
		}

		//	Sort Tab Handling
		if (isAPanelTab)
		{
			m_curAPanelTab = (APanelTab)m_curWinTab.getSelectedComponent();
			m_curAPanelTab.registerAPanel(this);
			m_curAPanelTab.loadData();
		}
		else	//	Cur Tab Setting
		{
			m_curGC.activate();
			m_curTab = m_curGC.getMTab();

			//	Refresh only current row when tab is current
			if (back && m_curTab.isCurrent())
				m_curTab.dataRefresh();
			else	//	Requery & autoSize
				m_curGC.query (m_onlyCurrentRows, m_onlyCurrentDays);

			//  Set initial record
			if (m_curTab.getRowCount() == 0)
			{
				//	Automatically create New Record, if none & tab not RO
				if (!m_curTab.isReadOnly() 
					&& (Env.isAutoNew(m_ctx, m_curWindowNo) || m_curTab.isQueryNewRecord()))
				{
					log.config("No record - creating new");
					m_curTab.dataNew(false);
				}
				else	//	No Records found
				{
					aSave.setEnabled(false);
					aDelete.setEnabled(false);
				}
				m_curTab.navigateCurrent();     //  updates counter
				m_curGC.dynamicDisplay(0);
			}
		//	else		##CHANGE
		//		m_curTab.navigateCurrent();
		}

		//	Update <-> Navigation
		aDetail.setEnabled(m_curTabIndex != m_curWinTab.getTabCount()-1);
		aParent.setEnabled(m_curTabIndex != 0 && m_curWinTab.getTabCount() > 1);

		//	History (on first tab only)
		if (m_mWorkbench.getMWindow(getWindowIndex()).isTransaction())
			aHistory.setEnabled(isFirstTab());
		else
		{
			aHistory.setPressed(false);
			aHistory.setEnabled(false);
		}
		//	Document Print
		aPrint.setEnabled(m_curTab.isPrinted());
		//	Query
		aFind.setPressed(m_curTab.isQueryActive());

		//	Order Tab
		if (isAPanelTab)
		{
			aMulti.setPressed(false);
			aMulti.setEnabled(false);
			aNew.setEnabled(false);
			aDelete.setEnabled(false);
			aFind.setEnabled(false);
			aRefresh.setEnabled(false);
			aAttachment.setEnabled(false);
		}
		else	//	Grid Tab
		{
			aMulti.setEnabled(true);
			aMulti.setPressed(!m_curGC.isSingleRow());
			aFind.setEnabled(true);
			aRefresh.setEnabled(true);
			aAttachment.setEnabled(true);
		}
		//
		m_curWinTab.requestFocusInWindow();
		setBusy(false);
		log.config( "fini");
	}	//	stateChanged

	/**
	 *	Navigate to Detail Tab			->
	 */
	private void cmd_detail()
	{
		int index = m_curWinTab.getSelectedIndex();
		if (index == m_curWinTab.getTabCount()-1)
			return;
		m_curGC.getTable().removeEditor();
		m_curWinTab.setSelectedIndex(index+1);
	}	//	navigateDetail

	/**
	 *	Navigate to Parent Tab			<-
	 */
	private void cmd_parent()
	{
		int index = m_curWinTab.getSelectedIndex();
		if (index == 0)
			return;
		m_curGC.getTable().removeEditor();
		m_curWinTab.setSelectedIndex(index-1);
	}	//	navigateParent

	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.info(e.getActionCommand() + " - " + e.getModifiers());
		//	+ " - " + new Timestamp(e.getWhen()) + " " + isUILocked());
		if (m_disposing || isUILocked())
			return;
			
		m_lastModifiers = e.getModifiers();
		String cmd = e.getActionCommand();
		//	Do ScreenShot w/o busy
		if (cmd.equals("ScreenShot"))
		{
			AEnv.actionPerformed (e.getActionCommand(), m_curWindowNo, this);
			return;
		}

		//  Problem: doubleClick detection - can't disable button as clicking button may change button status
		setBusy (true);
		//  Command Buttons
		if (e.getSource() instanceof VButton)
		{
			actionButton((VButton)e.getSource());
			setBusy(false);
			return;
		}

		try
		{
			//	File
			if (cmd.equals(aReport.getName()))
				cmd_report();
			else if (cmd.equals(aPrint.getName()))
				cmd_print();
			else if (cmd.equals(aEnd.getName()))
				cmd_end(false);
			else if (cmd.equals(aExit.getName()))
				cmd_end(true);
			//	Edit
			else if (cmd.equals(aNew.getName()))
				cmd_new(false);
			else if (cmd.equals(aSave.getName()))
				cmd_save(true);
			else if (cmd.equals(aCopy.getName()))
				cmd_new(true);
			else if (cmd.equals(aDelete.getName()))
				cmd_delete();
			else if (cmd.equals(aIgnore.getName()))
				cmd_ignore();
			else if (cmd.equals(aRefresh.getName()))
				cmd_refresh();
			else if (cmd.equals(aFind.getName()))
				cmd_find();
			else if (m_isPersonalLock && cmd.equals(aLock.getName()))
				cmd_lock();
			//	View
			else if (cmd.equals(aAttachment.getName()))
				cmd_attachment();
			else if (cmd.equals(aHistory.getName()))
				cmd_history();
			else if (cmd.equals(aMulti.getName()))
				m_curGC.switchRowPresentation();
			//	Go
			else if (cmd.equals(aFirst.getName()))
			{	/*cmd_save(false);*/
				m_curGC.getTable().removeEditor();
				m_curTab.navigate(0);
			}
			else if (cmd.equals(aPrevious.getName()))
			{	/*cmd_save(false);*/
				m_curGC.getTable().removeEditor();
				m_curTab.navigateRelative(-1);
			}
			else if (cmd.equals(aNext.getName()))
			{	/*cmd_save(false); */
				m_curGC.getTable().removeEditor();
				m_curTab.navigateRelative(+1);
			}
			else if (cmd.equals(aLast.getName()))
			{	/*cmd_save(false);*/
				m_curGC.getTable().removeEditor();
				m_curTab.navigate(m_curTab.getRowCount()-1);
			}
			else if (cmd.equals(aParent.getName()))
				cmd_parent();
			else if (cmd.equals(aDetail.getName()))
				cmd_detail();
			else if (cmd.equals(aZoomAcross.getName()))
				cmd_zoomAcross();
			else if (cmd.equals(aRequest.getName()))
				cmd_request();
			else if (cmd.equals(aArchive.getName()))
				cmd_archive();
			//	Tools
			else if (aWorkflow != null && cmd.equals(aWorkflow.getName()))
			{
				if (m_curTab.getRecord_ID() <= 0)
					;
				else if (m_curTab.getTabNo() == 0 && m_mWorkbench.getMWindow(getWindowIndex()).isTransaction())
					AEnv.startWorkflowProcess(m_curTab.getAD_Table_ID(), m_curTab.getRecord_ID());
				else
					AEnv.startWorkflowProcess(m_curTab.getAD_Table_ID(), m_curTab.getRecord_ID());
			}
			else if (aWinSize != null && cmd.equals(aWinSize.getName()))
				cmd_winSize();
			//	Help
			else if (cmd.equals(aHelp.getName()))
				cmd_help();
			//  General Commands (Environment)
			else if (!AEnv.actionPerformed (e.getActionCommand(), m_curWindowNo, this))
				log.log(Level.SEVERE, "No action for: " + cmd);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, cmd, ex);
			String msg = ex.getMessage();
			if (msg == null || msg.length() == 0)
				msg = ex.toString();
			msg = Msg.parseTranslation(m_ctx, msg);
			ADialog.error(m_curWindowNo, this, "Error", msg);
		}
		//
		m_curWinTab.requestFocusInWindow();
		setBusy(false);
	}	//	actionPerformed

	/**
	 *  Create New Record
	 *  @param copy true if current record is to be copied
	 */
	private void cmd_new (boolean copy)
	{
		log.config("copy=" + copy);
		if (!m_curTab.isInsertRecord())
		{
			log.warning("Insert Record disabled for Tab");
			return;
		}
		cmd_save(false);
		m_curTab.dataNew (copy);
		m_curGC.dynamicDisplay(0);
	//	m_curTab.getTableModel().setChanged(false);
	}   //  cmd_new

	/**
	 *  Confirm & delete record
	 */
	private void cmd_delete()
	{
		if (m_curTab.isReadOnly())
			return;
		int keyID = m_curTab.getRecord_ID();
		if (ADialog.ask(m_curWindowNo, this, "DeleteRecord?"))
			if (m_curTab.dataDelete())
				m_curGC.rowChanged(false, keyID);
		m_curGC.dynamicDisplay(0);
	}   //  cmd_delete

	/**
	 *  If required ask if you want to save and save it
	 *  @param manualCmd true if invoked manually (i.e. force)
	 *  @return true if saved
	 */
	private boolean cmd_save (boolean manualCmd)
	{
		if (m_curAPanelTab != null)
			manualCmd = false;
		log.config("Manual=" + manualCmd);
		m_errorDisplayed = false;
		m_curGC.stopEditor(true);
		boolean saveOK = true;

		if (m_curAPanelTab != null)
		{
			m_curAPanelTab.saveData();
			aSave.setEnabled(false);	//	set explicitly
		}

		if (m_curTab.getCommitWarning().length() > 0 && m_curTab.needSave(true, false))
			if (!ADialog.ask(m_curWindowNo, this, "SaveChanges?", m_curTab.getCommitWarning()))
				return false;

		//  manually initiated
		boolean retValue = m_curTab.dataSave(manualCmd);
		//   if there is no previous error
		if (manualCmd && !retValue && !m_errorDisplayed)
		{
			ADialog.error(m_curWindowNo, this, "SaveIgnored");
			setStatusLine(Msg.getMsg(m_ctx, "SaveIgnored"), true);
		}
		m_curGC.rowChanged(true, m_curTab.getRecord_ID());
		if (manualCmd)
			m_curGC.dynamicDisplay(0);
		return retValue;
	}   //  cmd_save

	/**
	 *  Ignore
	 */
	private void cmd_ignore()
	{
		m_curGC.stopEditor(false);
		m_curTab.dataIgnore();
		m_curGC.dynamicDisplay(0);
	}   //  cmd_ignore

	/**
	 *  Refresh
	 */
	private void cmd_refresh()
	{
		cmd_save(false);
		m_curTab.dataRefreshAll();
		m_curGC.dynamicDisplay(0);
	}   //  cmd_refresh

	/**
	 *	Print standard Report
	 */
	private void cmd_report ()
	{
		log.info("");
		if (!MRole.getDefault().isCanReport(m_curTab.getAD_Table_ID()))
		{
			ADialog.error(m_curWindowNo, this, "AccessCannotReport");
			return;
		}
		
		cmd_save(false);

		//	Query
		MQuery query = new MQuery(m_curTab.getTableName());
		//	Link for detail records
		String queryColumn = m_curTab.getLinkColumnName();
		//	Current row otherwise
		if (queryColumn.length() == 0)
			queryColumn = m_curTab.getKeyColumnName();
		//	Find display
		String infoName = null;
		String infoDisplay = null;
		for (int i = 0; i < m_curTab.getFieldCount(); i++)
		{
			MField field = m_curTab.getField(i);
			if (field.isKey())
				infoName = field.getHeader();
			if ((field.getColumnName().equals("Name") || field.getColumnName().equals("DocumentNo") )
				&& field.getValue() != null)
				infoDisplay = field.getValue().toString();
			if (infoName != null && infoDisplay != null)
				break;
		}
		if (queryColumn.length() != 0)
		{
			if (queryColumn.endsWith("_ID"))
				query.addRestriction(queryColumn, MQuery.EQUAL,
					new Integer(Env.getContextAsInt(m_ctx, m_curWindowNo, queryColumn)),
					infoName, infoDisplay);
			else
				query.addRestriction(queryColumn, MQuery.EQUAL,
					Env.getContext(m_ctx, m_curWindowNo, queryColumn),
					infoName, infoDisplay);
		}

		new AReport (m_curTab.getAD_Table_ID(), aReport.getButton(), query);
	}	//	cmd_report

	
	/**
	 * 	Zoom Across Menu
	 */
	private void cmd_zoomAcross()
	{
		int record_ID = m_curTab.getRecord_ID();
		log.info("ID=" + record_ID);
		if (record_ID <= 0)
			return;

		//	Query
		MQuery query = new MQuery();
		//	Current row
		String link = m_curTab.getKeyColumnName();
		//	Link for detail records
		if (link.length() == 0)
			link = m_curTab.getLinkColumnName();
		if (link.length() != 0)
		{
			if (link.endsWith("_ID"))
				query.addRestriction(link, MQuery.EQUAL,
					new Integer(Env.getContextAsInt(m_ctx, m_curWindowNo, link)));
			else
				query.addRestriction(link, MQuery.EQUAL,
					Env.getContext(m_ctx, m_curWindowNo, link));
		}
		new AZoomAcross (aZoomAcross.getButton(), 
			m_curTab.getTableName(), query);
	}	//	cmd_zoom
	
	/**
	 * 	Open/View Request
	 */
	private void cmd_request()
	{
		int record_ID = m_curTab.getRecord_ID();
		log.info("ID=" + record_ID);
		if (record_ID <= 0)
			return;

		int AD_Table_ID = m_curTab.getAD_Table_ID();
		int C_BPartner_ID = 0;
		Object BPartner_ID = m_curTab.getValue("C_BPartner_ID");
		if (BPartner_ID != null)
			C_BPartner_ID = ((Integer)BPartner_ID).intValue();
		new ARequest (aRequest.getButton(), AD_Table_ID, record_ID, C_BPartner_ID);
	}	//	cmd_request

	/**
	 * 	Open/View Archive
	 */
	private void cmd_archive()
	{
		int record_ID = m_curTab.getRecord_ID();
		log.info("ID=" + record_ID);
		if (record_ID <= 0)
			return;

		int AD_Table_ID = m_curTab.getAD_Table_ID();
		new AArchive (aArchive.getButton(), AD_Table_ID, record_ID);
	}	//	cmd_archive
	
	/**
	 *	Print specific Report - or start default Report
	 */
	private void cmd_print()
	{
		//	Get process defined for this tab
		int AD_Process_ID = m_curTab.getAD_Process_ID();
		log.info("ID=" + AD_Process_ID);

		//	No report defined
		if (AD_Process_ID == 0)
		{
			cmd_report();
			return;
		}

		cmd_save(false);
		//
		int table_ID = m_curTab.getAD_Table_ID();
		int record_ID = m_curTab.getRecord_ID();
		ProcessInfo pi = new ProcessInfo (getTitle(), AD_Process_ID, table_ID, record_ID);

		ProcessCtl.process(this, m_curWindowNo, pi, null); //  calls lockUI, unlockUI
	}   //  cmd_print

	/**
	 *	Find - Set Query
	 */
	private void cmd_find()
	{
		if (m_curTab == null)
			return;
		cmd_save(false);
		//	Gets Fields from AD_Field_v
		MField[] findFields = MField.createFields(m_ctx, m_curWindowNo, 0, m_curTab.getAD_Tab_ID());
		Find find = new Find (Env.getFrame(this), m_curWindowNo, m_curTab.getName(),
			m_curTab.getAD_Table_ID(), m_curTab.getTableName(), 
			m_curTab.getWhereExtended(), findFields, 1);
		MQuery query = find.getQuery();
		find = null;

		//	Confirmed query
		if (query != null)
		{
			m_onlyCurrentRows = false;      	//  search history too
			m_curTab.setQuery(query);
			m_curGC.query(m_onlyCurrentRows, m_onlyCurrentDays);   //  autoSize
		}
		aFind.setPressed(m_curTab.isQueryActive());
	}	//	cmd_find

	/**
	 *	Attachment
	 */
	private void cmd_attachment()
	{
		log.info("");
		int record_ID = m_curTab.getRecord_ID();
		if (record_ID == -1)	//	No Key
		{
			aAttachment.setEnabled(false);
			return;
		}

		Attachment va = new Attachment (Env.getFrame(this), m_curWindowNo,
			m_curTab.getAD_AttachmentID(), m_curTab.getAD_Table_ID(), record_ID, null);
		//
		m_curTab.loadAttachments();				//	reload
		aAttachment.setPressed(m_curTab.hasAttachment());
	}	//	attachment

	/**
	 *	Lock
	 */
	private void cmd_lock()
	{
		log.info("Modifiers=" + m_lastModifiers);
		if (!m_isPersonalLock)
			return;
		int record_ID = m_curTab.getRecord_ID();
		if (record_ID == -1)	//	No Key
			return;
		//	Control Pressed
		if ((m_lastModifiers & InputEvent.CTRL_MASK) != 0)
		{
			new RecordAccessDialog(Env.getFrame(this), m_curTab.getAD_Table_ID(), record_ID);
		}
		else
		{
			m_curTab.lock (Env.getCtx(), record_ID, aLock.getButton().isSelected());
			m_curTab.loadAttachments();			//	reload
		}
		aLock.setPressed(m_curTab.isLocked());
	}	//	lock

	/**
	 *	Toggle History
	 */
	private void cmd_history()
	{
		log.info("");
		if (m_mWorkbench.getMWindow(getWindowIndex()).isTransaction())
		{
			if (m_curTab.needSave(true, true) && !cmd_save(false))
				return;

			Point pt = new Point (0, aHistory.getButton().getBounds().height);
			SwingUtilities.convertPointToScreen(pt, aHistory.getButton());
			VOnlyCurrentDays ocd = new VOnlyCurrentDays(Env.getFrame(this), pt);
			m_onlyCurrentDays = ocd.getCurrentDays();
			if (m_onlyCurrentDays == 1)	//	Day
			{
				m_onlyCurrentRows = true;
				m_onlyCurrentDays = 0; 	//	no Created restriction
			}
			else
				m_onlyCurrentRows = false;
			log.config("OnlyCurrent=" + m_onlyCurrentRows + ", Days=" + m_onlyCurrentDays);
			//	No check for max query records
			m_curGC.query(m_onlyCurrentRows, m_onlyCurrentDays);   //  autoSize
		}
	}	//	cmd_history

	/**
	 *	Help
	 */
	private void cmd_help()
	{
		log.info("");
		Help hlp = new Help (Env.getFrame(this), this.getTitle(), m_mWorkbench.getMWindow(getWindowIndex()));
		hlp.setVisible(true);
	}	//	cmd_help

	/**
	 *  Close this screen - after save
	 *  @param exit ask if user wants to exit application
	 */
	private void cmd_end (boolean exit)
	{
		boolean exitSystem = false;
		if (!cmd_save(false))
			return;
		if (exit && ADialog.ask(m_curWindowNo, this, "ExitApplication?"))
			exitSystem = true;

		Env.getFrame(this).dispose();		//	calls this dispose

		if (exitSystem)
			AEnv.exit(0);
	}   //  cmd_end

	/**
	 * 	Set Window Size
	 */
	private void cmd_winSize()
	{
		Dimension size = getSize();
		if (!ADialog.ask(m_curWindowNo, this, "WinSizeSet", 
			"x=" + size.width + " - y=" + size.height))
		{
			setPreferredSize(null);
			SwingUtilities.getWindowAncestor(this).pack();
			size = new Dimension (0,0);
		}
		//
		M_Window win = new M_Window(m_ctx, m_curTab.getAD_Window_ID(), null);
		win.setWindowSize(size);
		win.save();
	}	//	cmdWinSize

	
	
	/**************************************************************************
	 *	Start Button Process
	 *  @param vButton button
	 */
	private void actionButton (VButton vButton)
	{
		log.info(vButton.toString());

		boolean startWOasking = false;
		String col = vButton.getColumnName();

		//  Zoom
		if (col.equals("Record_ID"))
		{
			int AD_Table_ID = Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Table_ID");
			int Record_ID = Env.getContextAsInt (m_ctx, m_curWindowNo, "Record_ID");
			AEnv.zoom(AD_Table_ID, Record_ID);
			return;
		}   //  Zoom

		//  save first	---------------
		if (m_curTab.needSave(true, false))
			if (!cmd_save(true))
				return;
		//
		int table_ID = m_curTab.getAD_Table_ID();
		//	Record_ID
		int record_ID = m_curTab.getRecord_ID();
		//	Record_ID - Language Handling
		if (record_ID == -1 && m_curTab.getKeyColumnName().equals("AD_Language"))
			record_ID = Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Language_ID");
		//	Record_ID - Change Log ID
		if (record_ID == -1 
			&& (vButton.getProcess_ID() == 306 || vButton.getProcess_ID() == 307))
		{
			Integer id = (Integer)m_curTab.getValue("AD_ChangeLog_ID");
			record_ID = id.intValue();
		}
		//	Ensure it's saved
		if (record_ID == -1 && m_curTab.getKeyColumnName().endsWith("_ID"))
		{
			ADialog.error(m_curWindowNo, this, "SaveErrorRowNotFound");
			return;
		}

		//	Pop up Payment Rules
		if (col.equals("PaymentRule"))
		{
			VPayment vp = new VPayment(m_curWindowNo, m_curTab, vButton);
			if (vp.isInitOK())		//	may not be allowed
				vp.setVisible(true);
			vp.dispose();
			if (vp.needSave())
			{
				cmd_save(false);
				cmd_refresh();
			}
		}	//	PaymentRule

		//	Pop up Document Action (Workflow)
		else if (col.equals("DocAction"))
		{
			VDocAction vda = new VDocAction(m_curWindowNo, m_curTab, vButton, record_ID);
			//	Something to select from?
			if (vda.getNumberOfOptions() == 0)
			{
				vda.dispose ();
				log.info("DocAction - No Options");
				return;
			}
			else
			{
				vda.setVisible(true);
				if (!vda.getStartProcess())
					return;
				startWOasking = true;
				vda.dispose();
			}
		}	//	DocAction

		//  Pop up Create From
		else if (col.equals("CreateFrom"))
		{
			//  m_curWindowNo
			VCreateFrom vcf = VCreateFrom.create (m_curTab);
			if (vcf != null)
			{
				if (vcf.isInitOK())
				{
					vcf.setVisible(true);
					vcf.dispose();
					m_curTab.dataRefresh();
				}
				else
					vcf.dispose();
				return;
			}
			//	else may start process
		}	//	CreateFrom

		//  Posting -----
		else if (col.equals("Posted") && MRole.getDefault().isShowAcct())
		{
			//  Check Doc Status
			String processed = Env.getContext(m_ctx, m_curWindowNo, "Processed");
			if (!processed.equals("Y"))
			{
				String docStatus = Env.getContext(m_ctx, m_curWindowNo, "DocStatus");
				if (DocAction.STATUS_Completed.equals(docStatus)
					|| DocAction.STATUS_Closed.equals(docStatus)
					|| DocAction.STATUS_Reversed.equals(docStatus)
					|| DocAction.STATUS_Voided.equals(docStatus))
					;
				else
				{
					ADialog.error(m_curWindowNo, this, "PostDocNotComplete");
					return;
				}
			}

			//  Check Post Status
			Object ps = m_curTab.getValue("Posted");
			if (ps != null && ps.equals("Y"))
			{
				new org.compiere.acct.AcctViewer (Env.getContextAsInt (m_ctx, m_curWindowNo, "AD_Client_ID"),
					m_curTab.getAD_Table_ID(), m_curTab.getRecord_ID());
			}
			else
			{
				if (ADialog.ask(m_curWindowNo, this, "PostImmediate?"))
				{
					String error = AEnv.postImmediate (m_curWindowNo, Env.getAD_Client_ID(m_ctx),
						m_curTab.getAD_Table_ID(), m_curTab.getRecord_ID(), false);
					m_curTab.dataRefresh();
					if (error != null)
						ADialog.error(m_curWindowNo, this, "PostingError-N", error);
				}
			}
			return;
		}   //  Posted

		/**
		 *  Start Process ----
		 */

		log.config("Process_ID=" + vButton.getProcess_ID() + ", Record_ID=" + record_ID);
		if (vButton.getProcess_ID() == 0)
			return;
		//	Save item changed
		if (m_curTab.needSave(true, false))
			if (!cmd_save(true))
				return;

		//	Ask user to start process, if Description and Help is not empty
		if (!startWOasking && !(vButton.getDescription().equals("") && vButton.getHelp().equals("")))
			if (!ADialog.ask(m_curWindowNo, this, "StartProcess?", 
				//	"<b><i>" + vButton.getText() + "</i></b><br>" +
				vButton.getDescription() + "\n" + vButton.getHelp()))
				return;
		//
		String title = vButton.getDescription();
		if (title == null || title.length() == 0)
			title = vButton.getName();
		ProcessInfo pi = new ProcessInfo (title, vButton.getProcess_ID(), table_ID, record_ID);
		pi.setAD_User_ID (Env.getAD_User_ID(m_ctx));
		pi.setAD_Client_ID (Env.getAD_Client_ID(m_ctx));

	//	Trx trx = Trx.get(Trx.createTrxName("AppsPanel"), true);
		ProcessCtl.process(this, m_curWindowNo, pi, null); //  calls lockUI, unlockUI
	}	//	actionButton

	
	/**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
	//	log.fine("" + pi);
		setBusy(true);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
	//	log.fine("" + pi);
		setBusy(false);
		//  Process Result
		if (pi != null		//	refresh if not print 
			&& pi.getAD_Process_ID() != m_curTab.getAD_Process_ID())
		{
			//	Refresh data
			m_curTab.dataRefresh();
			m_curGC.dynamicDisplay(0);
			//	Update Status Line
			setStatusLine(pi.getSummary(), pi.isError());
			//	Get Log Info
			ProcessInfoUtil.setLogFromDB(pi);
			String log = pi.getLogInfo();
			if (log.length() > 0)
				ADialog.info(m_curWindowNo, this, Env.getHeader(m_ctx, m_curWindowNo),
					pi.getTitle(), log);	//	 clear text
		}
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked

	/**
	 *  Method to be executed async.
	 *  Called from the ASyncProcess worker
	 *  @param pi process info
	 */
	public void executeASync (ProcessInfo pi)
	{
		log.config("-");
	}   //  executeASync

	/**
	 * 	Get Current Tab
	 *	@return current tab
	 */
	protected MTab getCurrentTab()
	{
		return m_curTab;
	}	//	getCurrentTab
	
	/**
	 *  String representation
	 *  @return String representation
	 */
	public String toString()
	{
		String s = "APanel[curWindowNo=" + m_curWindowNo;
		if (m_mWorkbench != null)
			s += ",WB=" + m_mWorkbench.toString();
		s += "]";
		return s;
	}   //  toString
	
	//begin e-evolution vpj-cd 03 Feb 2006
	public void dispatchTabSwitch(GridController gc) 
	{
		
	if(gc == null || gc.equals(m_curGC)) {
			
			return;	
	}
		
	if(m_curTab.getRecord_ID() == -1 ) 
	{	
		gc.getMTab().navigateCurrent();
        gc.dynamicDisplay(0);
        gc.getMTab().dataRefresh();
        return;
	}	
		
    //System.out.println("Entro al grid controller gc" + gc.toString());   	

	
	/*
        if (m_curGC != null) {

		//m_curGC.stopEditor(true);

		if (m_curTab.needSave(true, false)) {

			if (m_curTab.needSave(true, true)) {

				if (ADialog.ask(m_curWindowNo, this, "SaveChanges?", m_curTab.getCommitWarning())) {

					if (!m_curTab.dataSave(true)) {
						
						m_curWinTab.setSelectedIndex(m_curTabIndex);
						setBusy(false);
						return;
					}
				}
				else {
					m_curTab.dataIgnore();
				}
			}
			else {
				m_curTab.dataIgnore();
			}
		}
	}
	if (m_curAPanelTab != null) {
		
		m_curAPanelTab.saveData();
		m_curAPanelTab.unregisterPanel();
		m_curAPanelTab = null;
	}

	boolean back = m_curGC.equals(gc);
	
            
    if (!m_curTab.dataSave(true)) 
    {
						
						m_curWinTab.setSelectedIndex(m_curTabIndex);
						setBusy(false);
						return;
	}*/            
    
    //gc.stopEditor(true);
    //m_curTab.dataSave(true);
    //m_curTab.dataSave(false);
    //
	//System.out.println("Identificador de registro :" + m_curTab.getRecord_ID());
	//System.out.println("isQueryNewRecord() :" + m_curTab.isQueryNewRecord());
	//System.out.println("isInsertRecord() :" + m_curTab.isInsertRecord());
	//if(m_curTab.needSave(true, true))
	//{	
	gc.getMTab().dataSave(true);
	//m_curGC.stopEditor(true);
	//}
	
	m_curGC = gc;
	m_curGC.activate();

	m_curTab = gc.getMTab();
	//m_curTab.navigateCurrent();
	// here only data is refreshed
	//m_curTab.dataRefresh();
	
	// first init of included tab needs to be done otherwhere
	//m_curGC.query (m_onlyCurrentRows, m_onlyCurrentDays);
    //m_curTab.navigateCurrent();
    //m_curGC.dynamicDisplay(0);
	
	aDetail.setEnabled(m_curTabIndex != m_curWinTab.getTabCount()-1);
	aParent.setEnabled(m_curTabIndex != 0 && m_curWinTab.getTabCount() > 1);

	if (m_mWorkbench.getMWindow(getWindowIndex()).isTransaction()) {
	
		aHistory.setEnabled(isFirstTab());
	}
	else {
		
		aHistory.setPressed(false);
		aHistory.setEnabled(false);
	}

	aPrint.setEnabled(m_curTab.isPrinted());
	aFind.setPressed(m_curTab.isQueryActive());

	aMulti.setEnabled(true);
	aMulti.setPressed(!m_curGC.isSingleRow());
	aFind.setEnabled(true);
	aRefresh.setEnabled(true);
	aAttachment.setEnabled(true);

	m_curWinTab.requestFocusInWindow();
	setBusy(false);
	
	log.config( "fini");
    }
//end e-evolution vpj-cd 03 Feb 2006
}	//	APanel