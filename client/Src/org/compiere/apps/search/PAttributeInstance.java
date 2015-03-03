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
package org.compiere.apps.search;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VPAttributeDialog;
import org.compiere.grid.ed.VString;
import org.compiere.minigrid.*;
import org.compiere.model.MAttribute;
import org.compiere.model.MAttributeInstance;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MDocType;
import org.compiere.model.MLocator;
import org.compiere.model.MRole;
import org.compiere.swing.*;
import org.compiere.util.*;



/**
 *	Display Product Attribute Instance Info
 *
 *  @author     Jorg Janke
 *  @version    $Id: PAttributeInstance.java,v 1.3 2006/11/21 18:54:45 SIGArg-01 Exp $
 */
public class PAttributeInstance extends CDialog 
	implements ListSelectionListener
{
	/**
	 * 	Constructor
	 * 	@param parent frame parent
	 * 	@param title title
	 * 	@param M_Warehouse_ID warehouse key name pair
	 * 	@param M_Locator_ID locator
	 * 	@param M_Product_ID product key name pair
	 * 	@param C_BPartner_ID bp
	 */
	public PAttributeInstance(JFrame parent, String title,
		int M_Warehouse_ID, int M_Locator_ID, int M_Product_ID, int C_BPartner_ID)
	{
		super (parent, Msg.getMsg(Env.getCtx(), "PAttributeInstance") + title, true);
		init (M_Warehouse_ID, M_Locator_ID, M_Product_ID, C_BPartner_ID);
		AEnv.showCenterWindow(parent, this);
	}	//	PAttributeInstance
	
	
	private int m_WindowNoParent = 0;
	private boolean esWorkflow;
	
	/***02-11-2010 Camarzana Mariano
	 * 	Constructor agregado para permitir manejar las partidas desde factura
	 * 	@param parent dialog parent
	 * 	@param M_Warehouse_ID warehouse key name pair
	 * 	@param M_Locator_ID locator
	 * 	@param M_Product_ID product key name pair
	 * 	@param C_BPartner_ID bp
	 */
	public PAttributeInstance(JDialog parent, String title,
		int M_Warehouse_ID, int M_Locator_ID, int M_Product_ID, int C_BPartner_ID,boolean esWorkflow,int nroVentana)
	{
		super (parent, Msg.getMsg(Env.getCtx(), "PAttributeInstance") + title, true);
		m_WindowNoParent = nroVentana;
		this.esWorkflow = esWorkflow;
		init (M_Warehouse_ID, M_Locator_ID, M_Product_ID, C_BPartner_ID);
		AEnv.showCenterWindow(parent, this);		
	}	//	PAttributeInstance

	/**
	 *  Se agrego un nuevo constructor para permitir manjar mejor las partidas en las ordenes de venta
	 * 	Constructor
	 * 	@param parent dialog parent
	 * 	@param M_Warehouse_ID warehouse key name pair
	 * 	@param M_Locator_ID locator
	 * 	@param M_Product_ID product key name pair
	 * 	@param C_BPartner_ID bp
	 */
		
	public PAttributeInstance(JDialog parent, String title,
		int M_Warehouse_ID, int M_Locator_ID, int M_Product_ID, int C_BPartner_ID)
	{
		super (parent, Msg.getMsg(Env.getCtx(), "PAttributeInstance") + title, true);
		this.esWorkflow = esWorkflow;
		init (M_Warehouse_ID, M_Locator_ID, M_Product_ID, C_BPartner_ID);
		AEnv.showCenterWindow(parent, this);		
	}	//	PAttributeInstance	
	
	
	/**
	 * 	Initialization
	 *	@param M_Warehouse_ID wh
	 *	@param M_Locator_ID loc
	 *	@param M_Product_ID product
	 *	@param C_BPartner_ID partner
	 */
	private void init (int M_Warehouse_ID, int M_Locator_ID, int M_Product_ID, int C_BPartner_ID)
	{
		log.info("M_Warehouse_ID=" + M_Warehouse_ID 
			+ ", M_Locator_ID=" + M_Locator_ID
			+ ", M_Product_ID=" + M_Product_ID);
		m_M_Warehouse_ID = M_Warehouse_ID;
		m_M_Locator_ID = M_Locator_ID;
		m_M_Product_ID = M_Product_ID;
		try
		{
			jbInit();
			dynInit(C_BPartner_ID);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	// init	
        
        private BorderLayout mainLayout = new BorderLayout();
	
	private CPanel mainPanel = new CPanel();
	private CPanel northPanel = new CPanel();
        private CPanel mediumPanel = new CPanel();
        private CPanel genericNorthPanel = new CPanel();
	
        private BorderLayout northLayout = new BorderLayout();
	private BorderLayout mediumLayout = new BorderLayout();
        private BorderLayout genericNorthLayout = new BorderLayout();
	
        private JScrollPane centerScrollPane = new JScrollPane();
	private ConfirmPanel confirmPanel = new ConfirmPanel (true);
	
        private CCheckBox showAll = new CCheckBox (Msg.getMsg(Env.getCtx(), "ShowAll"));
        private VCheckBox CheckAnalisis = new VCheckBox ("", false, false, true, Msg.translate(Env.getCtx(), "New"), "", false);
        
        private int m_row = 0;
        
        /*
         *  Vit4B - Anexo para manejar el movimiento por aprobacion y rechazo
         *
         */
        
        private VString analisis = new VString ("Lot", false, false, true, 20, 20, null, null);
                
        /*
         *  Vit4B - Fin
         *
         */
        
	private MiniTable 			m_table = new MiniTable();
	private DefaultTableModel 	m_model;
	//	Parameter
	private int			 		m_M_Warehouse_ID;
	private int			 		m_M_Locator_ID;
	private int			 		m_M_Product_ID;
	//
	private int					m_M_AttributeSetInstance_ID = -1;
	private String				m_M_AttributeSetInstanceName = null;
	private String				m_sql;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(PAttributeInstance.class);

	/**
	 * 	Static Init
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		mainPanel.setLayout(mainLayout);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
                //	North
		mediumPanel.setLayout(mediumLayout);
                northPanel.setLayout(northLayout);
                genericNorthPanel.setLayout(genericNorthLayout);
               
                int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
                MRole rol = new MRole(Env.getCtx(),AD_Role_ID,null);
                
                String AD_Process_Workflow_ID= null;
                String AD_Window_ID = null;
                
                AD_Process_Workflow_ID = !esWorkflow ? new Integer(m_WindowNoParent).toString() :
                	Env.getContext(Env.getCtx(), 0,"AD_Process_Workflow_ID");
                                
                
                /**
                 * HAY QUE SETEAR BIEN LA VARIABLE DE CONTEXTO AD_Process_Workflow_ID
                 */
                System.out.println(" En Ventana de IATTR AD_WF_Process_ID: " + AD_Process_Workflow_ID);
				String parametro = (AD_Process_Workflow_ID == null) ? "AD_WINDOW_ID" : "AD_WORKFLOW_ID";
                //String id = (AD_Process_Workflow_ID == null) ? AD_Window_ID : AD_Process_Workflow_ID;
                if(existPermission(AD_Role_ID, parametro,AD_Process_Workflow_ID, 10)) 
                {
                    CLabel label = new CLabel (Msg.translate(Env.getCtx(), "ProductAnalysis"));
                    mediumPanel.add(label, BorderLayout.WEST);
                    mediumPanel.add(analisis, BorderLayout.CENTER);
                    mediumPanel.add(CheckAnalisis, BorderLayout.EAST);
                }
             
                
		northPanel.add(showAll, BorderLayout.EAST);
                
                showAll.addActionListener(this);
                
		genericNorthPanel.add(northPanel, BorderLayout.CENTER);
                genericNorthPanel.add(mediumPanel, BorderLayout.NORTH);
                
                mainPanel.add(genericNorthPanel, BorderLayout.NORTH);

                //	Center
		mainPanel.add(centerScrollPane, BorderLayout.CENTER);
		centerScrollPane.getViewport().add(m_table, null);
		//	South
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**	Table Column Layout Info			*/
	private static ColumnInfo[] s_layout = new ColumnInfo[] 
	{
		new ColumnInfo(" ", "s.M_AttributeSetInstance_ID", IDColumn.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), "asi.Description", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Lot"), "asi.Lot", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "SerNo"), "asi.SerNo", String.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "GuaranteeDate"), "asi.GuaranteeDate", Timestamp.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "M_Locator_ID"), "l.Value", KeyNamePair.class, "s.M_Locator_ID"),
		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "s.QtyOnHand", Double.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "s.QtyReserved", Double.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOrdered"), "s.QtyOrdered", Double.class),
		//	See RV_Storage
		new ColumnInfo(Msg.translate(Env.getCtx(), "GoodForDays"), "(TRUNC(asi.GuaranteeDate)-TRUNC(SysDate))-p.GuaranteeDaysMin", Integer.class, true, true, null),
		new ColumnInfo(Msg.translate(Env.getCtx(), "ShelfLifeDays"), "TRUNC(asi.GuaranteeDate)-TRUNC(SysDate)", Integer.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "ShelfLifeRemainingPct"), "CASE WHEN p.GuaranteeDays > 0 THEN TRUNC(((TRUNC(asi.GuaranteeDate)-TRUNC(SysDate))/p.GuaranteeDays)*100) ELSE 0 END", Integer.class),
	};
	/**	From Clause							*/
	private static String s_sqlFrom = "M_Storage s"
		+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)"
		+ " INNER JOIN M_Product p ON (s.M_Product_ID=p.M_Product_ID)"
		+ " LEFT OUTER JOIN M_AttributeSetInstance asi ON (s.M_AttributeSetInstance_ID=asi.M_AttributeSetInstance_ID)";
	/** Where Clause						*/
	private static String s_sqlWhere = "l.M_Warehouse_ID=? AND s.M_Product_ID=?"; 

	private String	m_sqlNonZero = " AND (s.QtyOnHand<>0 OR s.QtyReserved<>0 OR s.QtyOrdered<>0)";
	private String	m_sqlMinLife = "";
        
        /*
         *  VIT4B - ADD 
         *
         **/        
        
        private boolean flagWharehouse = true;
        
        /*
         *  VIT4B - END
         *
         **/            
        

	/**
	 * 	Dynamic Init
	 */
	private void dynInit(int C_BPartner_ID)
	{
		log.config("C_BPartner_ID=" + C_BPartner_ID);
		if (C_BPartner_ID != 0)
		{
			int ShelfLifeMinPct = 0;
			int ShelfLifeMinDays = 0;
			String sql = "SELECT bp.ShelfLifeMinPct, bpp.ShelfLifeMinPct, bpp.ShelfLifeMinDays "
				+ "FROM C_BPartner bp "
				+ " LEFT OUTER JOIN C_BPartner_Product bpp"
				+	" ON (bp.C_BPartner_ID=bpp.C_BPartner_ID AND bpp.M_Product_ID=?) "
				+ "WHERE bp.C_BPartner_ID=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, m_M_Product_ID);
				pstmt.setInt(2, C_BPartner_ID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					ShelfLifeMinPct = rs.getInt(1);		//	BP
					int pct = rs.getInt(2);				//	BP_P
					if (pct > 0)	//	overwrite
						ShelfLifeMinDays = pct;
					ShelfLifeMinDays = rs.getInt(3);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
			if (ShelfLifeMinPct > 0)
			{
				m_sqlMinLife = " AND COALESCE(TRUNC(((TRUNC(asi.GuaranteeDate)-TRUNC(SysDate))/p.GuaranteeDays)*100),0)>=" + ShelfLifeMinPct;
				log.config( "PAttributeInstance.dynInit - ShelfLifeMinPct=" + ShelfLifeMinPct);
			}
			if (ShelfLifeMinDays > 0)
			{
				m_sqlMinLife += " AND COALESCE((TRUNC(asi.GuaranteeDate)-TRUNC(SysDate)),0)>=" + ShelfLifeMinDays;
				log.config( "PAttributeInstance.dynInit - ShelfLifeMinDays=" + ShelfLifeMinDays);
			}
		}	//	BPartner != 0


                
                
                /*
                 *  VIT4B - Cambio para que la elecci�n de la instancia de atributos se realice del deposito
                 *  correpondiente y no del deposito de inicio que esta en el contexto. 
                 *
                 **/
                
                
                int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
                MRole rol = new MRole(Env.getCtx(),AD_Role_ID,null);
                
                String title = "";
                int M_Warehouse_ID = 0;
                VPAttributeDialog parent = (VPAttributeDialog) this.getParent();
                boolean flagWharehouse = true;
                   
                	/**
                    * 02-11-2010 Camarzana Mariano Comentado por problemas en el query por el almacen en la orden venta
                    */ 
                /*if (rol.getProcessAccess(1000122) == null)
                {
                    s_sqlWhere = "s.M_Product_ID=? AND l.M_Warehouse_ID=?";
                }
                else*/
               // {
                    //m_M_Warehouse_ID = Env.getContextAsInt(Env.getCtx(), parent.getWindowNoParent(), "M_Locator_ID");
                    
                    int M_Locator_ID = Env.getContextAsInt(Env.getCtx(), parent.getWindowNoParent(), "M_Locator_ID");
                    MLocator MLocatorInstance = MLocator.getDefault(Env.getCtx(),M_Locator_ID);
                    m_M_Warehouse_ID = MLocatorInstance.getM_Warehouse_ID();
                    
                    
                    int DocTypeLocal = Env.getContextAsInt(Env.getCtx(), parent.getWindowNoParent(), "C_DocType_ID");
                    
                    /**
                     * BISion - 28/07/2009 - Santiago Ibañez
                     * Modificacion realizada para tomar los docbasetype que agrupan a todos los Movimientos
                     */
                     MDocType docType = new MDocType(Env.getCtx(), DocTypeLocal, null);
                    //if(DocTypeLocal == 1000149 || DocTypeLocal == 1000167 || DocTypeLocal == 1000168)
                    if (docType.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialMovement))
                    {
                        s_sqlWhere = "s.M_Product_ID=? AND l.M_Warehouse_ID=?"; 
                    }
                    else
                    {
                       s_sqlWhere = "s.M_Product_ID=?";
                       flagWharehouse = false;
                    }
                    

                //}
                
                /*
                 *  VIT4B - Agregado para no listar la instancia ---
                 *
                 **/                
                

                s_sqlWhere += " AND s.M_AttributeSetInstance_ID <> 0";
                
                /*
                 *  VIT4B - FIN
                 *
                 **/                
                
                
                m_sql = m_table.prepareTable (s_layout, s_sqlFrom, 
			s_sqlWhere, false, "s")
			+ " ORDER BY asi.GuaranteeDate, s.QtyOnHand";	//	oldest, smallest first
                
		m_table.setRowSelectionAllowed(true);
		m_table.setMultiSelection(false);
		//m_table.addMouseListener(this);
		m_table.getSelectionModel().addListSelectionListener(this);
		//
		refresh();
	}	//	dynInit

	/**
	 * 	Refresh Query
	 */
	private void refresh()
	{
		String sql = m_sql;
		int pos = m_sql.lastIndexOf(" ORDER BY ");
		if (!showAll.isSelected())
		{
			sql = m_sql.substring(0, pos) 
				+ m_sqlNonZero;
			if (m_sqlMinLife.length() > 0)
				sql += m_sqlMinLife;
			sql += m_sql.substring(pos);
		}
		//
		log.finest(sql);
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
                        /*
                         *  VIT4B - FIN
                         *
                         **/
                        VPAttributeDialog parent = (VPAttributeDialog) this.getParent();
                        int DocTypeLocal = Env.getContextAsInt(Env.getCtx(), parent.getWindowNoParent(), "C_DocType_ID");
			
                        /*
                         *  Si se registra el tipo de documento se agrega el warehouse de lo contrario no ...
                         *
                         */
                        
                        
                        if(flagWharehouse)
                        {
                            /** BISIon - 28/07/2009 - Santiago Iba�ez
                             * Modificacion realizada para considerar todos los movimientos de acuerdo a
                             * su docbasetype.
                             */
                             MDocType docType = new MDocType(Env.getCtx(), DocTypeLocal, null);
                            //if(DocTypeLocal == 1000149 || DocTypeLocal == 1000167 || DocTypeLocal == 1000168)
                            if (docType.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialMovement))
                            // fin modificacion BISion
                            {
                                pstmt.setInt(2, m_M_Warehouse_ID);
                            }
                        }

                        pstmt.setInt(1, m_M_Product_ID);
			
                        /*
                         *  VIT4B - FIN
                         *
                         **/ 
                        ResultSet rs = pstmt.executeQuery();
                        
                        m_table.loadTable(rs);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		enableButtons();
	}	//	refresh

	/**
	 * 	Action Listener
	 *	@param e event 
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
                {
                    int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
                    MRole rol = new MRole(Env.getCtx(),AD_Role_ID,null);

                    String AD_Process_Workflow_ID;
                    Integer AD_Window_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNoParent,"WindowID");
                    AD_Process_Workflow_ID  = !esWorkflow ? AD_Window_ID.toString():Env.getContext(Env.getCtx(), 0,"AD_Process_Workflow_ID");
                    /**
                     * HAY QUE SETEAR BIEN LA VARIABLE DE CONTEXTO AD_Process_Workflow_ID
                     */
                    System.out.println(" En Ventana de IATTR AD_WF_Process_ID: " + AD_Process_Workflow_ID);
    				
                    //07-06-2011 Camarzana Mariano
                    //Estaba seteando mal el parametro
                    //String parametro = esWorkflow ? "AD_WINDOW_ID" : "AD_WORKFLOW_ID";
                    String parametro = !esWorkflow ? "AD_WINDOW_ID" : "AD_WORKFLOW_ID";
                    
                    
                    if(existPermission(AD_Role_ID, parametro,AD_Process_Workflow_ID, 10)) 
                    {  
                        
                        boolean CheckValue = ((Boolean)CheckAnalisis.getValue()).booleanValue();
                        
                        MAttributeSetInstance m_masi = MAttributeSetInstance.get(Env.getCtx(), m_M_AttributeSetInstance_ID, m_M_Product_ID);
                        MAttributeSet as = m_masi.getMAttributeSet();
                  
                        /*
                        **  Ojo que deberia de ver que paso con la instancia --- que no usa � 
                        **  VIT4B
                        */


                        int iniStr = m_M_AttributeSetInstanceName.indexOf("_");
                        String sufStr = "";
                        if(iniStr != -1)
                        {
                            sufStr = m_M_AttributeSetInstanceName.substring(iniStr+1, m_M_AttributeSetInstanceName.length());

                        }
                        else
                        {
                            sufStr = m_M_AttributeSetInstanceName;
                        }
                        
                        
                        if(this.analisis.getText().equals("") && !CheckValue && (iniStr == -1))
                        {
                            JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Debe ingresar un analisis"), "Info" , JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        else
                        {
                        
                            /*
                             *  Vit4B - ac� debo de ingresar o actualizar en su defecto la referencia al an�lisis
                             *
                             */
                            
                                                     
                            
                            

                            
                            MAttribute[] attributes = as.getMAttributes(true);
                            
                            
                            
                            for (int i = 0; i < attributes.length; i++)
                            {                                
                                if(CheckValue)
                                {
                                    
                                    String sql = "SELECT CURRENTNEXT FROM AD_SEQUENCE WHERE AD_SEQUENCE_ID = 1000716";
                                    
                                    
                                    int valueNew = 0;
                                    PreparedStatement pstmt = null;
                                    try
                                    {
                                            pstmt = DB.prepareStatement(sql, null);
                                            ResultSet rs = pstmt.executeQuery();
                                            while (rs.next())
                                            {
                                                valueNew = Integer.parseInt(rs.getString(1).toString());
                                            }
                                            rs.close();
                                            pstmt.close();
                                            pstmt = null;
                                    }
                                    catch (SQLException ex)
                                    {
                                            log.log(Level.SEVERE, sql, ex);
                                    }
                                    
                                    int valueNext = valueNew+1;
                                    
                                    sql = "UPDATE AD_SEQUENCE SET CURRENTNEXT = " + valueNext + " WHERE AD_SEQUENCE_ID = 1000716";
                                    

                                    try
                                    {
                                            pstmt = DB.prepareStatement(sql, null);
                                            ResultSet rs = pstmt.executeQuery();
                                            rs.close();
                                            pstmt.close();
                                            pstmt = null;
                                    }
                                    catch (SQLException ex)
                                    {
                                            log.log(Level.SEVERE, sql, ex);
                                    }
                                    
                                    String valueNewStr = String.valueOf(valueNew);

                                    attributes[i].setMAttributeInstance(m_M_AttributeSetInstance_ID, valueNewStr);
                                    //m_M_AttributeSetInstanceName = valueNewStr + "_" + sufStr;
                                    m_M_AttributeSetInstanceName = valueNewStr;
                                    m_M_AttributeSetInstanceName = m_M_AttributeSetInstanceName.length()==0 ? "«"+m_masi.getLot()+"»" : m_M_AttributeSetInstanceName+"_«"+m_masi.getLot()+"»";
                                    if (m_masi.getGuaranteeDate()!=null){
                                        Calendar fecha = Calendar.getInstance();
                                        fecha.setTimeInMillis(m_masi.getGuaranteeDate().getTime());
                                        String dia = fecha.get(Calendar.DAY_OF_MONTH) <= 9 ? "0"+fecha.get(Calendar.DAY_OF_MONTH): ""+fecha.get(Calendar.DAY_OF_MONTH);
                                        String mes = fecha.get(Calendar.MONTH) <= 9 ? "0"+fecha.get(Calendar.MONTH): ""+fecha.get(Calendar.MONTH);
                                        String anio = ""+fecha.get(Calendar.YEAR);
                                        m_M_AttributeSetInstanceName = m_M_AttributeSetInstanceName+"_"+dia+"/"+mes+"/"+anio;
                                    }
                                }
                                else
                                {
                                    
                                    /*
                                    **  Agregado para verificar que si la instancia es la generica no se asigne analisis
                                    **  VIT4B
                                    */

                                    if(m_M_AttributeSetInstance_ID == 0)
                                        attributes[i].setMAttributeInstance(m_M_AttributeSetInstance_ID, "");
                                    else
                                        //attributes[i].setMAttributeInstance(m_M_AttributeSetInstance_ID, this.analisis.getText());
                                        attributes[i].setMAttributeInstance(m_M_AttributeSetInstance_ID, this.analisis.getText());
                                    m_M_AttributeSetInstanceName = this.analisis.getText();
                                    m_M_AttributeSetInstanceName = m_M_AttributeSetInstanceName.length()==0 ? "«"+m_masi.getLot()+"»" : m_M_AttributeSetInstanceName+"_«"+m_masi.getLot()+"»";
                                    if (m_masi.getGuaranteeDate()!=null){
                                        Calendar fecha = Calendar.getInstance();
                                        fecha.setTimeInMillis(m_masi.getGuaranteeDate().getTime());
                                        String dia = fecha.get(Calendar.DAY_OF_MONTH) <= 9 ? "0"+fecha.get(Calendar.DAY_OF_MONTH): ""+fecha.get(Calendar.DAY_OF_MONTH);
                                        String mes = fecha.get(Calendar.MONTH) <= 9 ? "0"+fecha.get(Calendar.MONTH): ""+fecha.get(Calendar.MONTH);
                                        String anio = ""+fecha.get(Calendar.YEAR);
                                        m_M_AttributeSetInstanceName = m_M_AttributeSetInstanceName+"_"+dia+"/"+mes+"/"+anio;
                                    }
                                    //m_M_AttributeSetInstanceName = this.analisis.getText() + "_" + sufStr;
                                }
                            
                            }
                            
                            m_masi.setDescription(m_M_AttributeSetInstanceName);

                            //	Save Model
                            m_masi.save ();
                           
                                                       
                        }
                    }
                    
                    dispose();
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
			m_M_AttributeSetInstance_ID = -1;
			m_M_AttributeSetInstanceName = null;
		}
		else if (e.getSource() == showAll)
		{
			refresh();
		}
	}	//	actionPerformed
 
	/**
	 * 	Table selection changed
	 *	@param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		enableButtons();
	}	//	valueChanged

	/**
	 * 	Enable/Set Buttons and set ID
	 */
	private void enableButtons()
	{
		m_M_AttributeSetInstance_ID = -1;
		m_M_AttributeSetInstanceName = null;
		int row = m_table.getSelectedRow();
		boolean enabled = row != -1;
		if (enabled)
		{
			Integer ID = m_table.getSelectedRowKey();
			if (ID != null)
			{
				m_M_AttributeSetInstance_ID = ID.intValue();
				m_M_AttributeSetInstanceName = (String)m_table.getValueAt(row, 1);
                                
                                /*
                                 *  Agregado para setear el analisis cuando se selecciona una partida.
                                 *
                                 *
                                 */
                                
                                /** BISion - 05/02/2010 - Santiago Ibañez
                                 * COM-PAN-BUG-09.003.01
                                 */
                                int iniStr = m_M_AttributeSetInstanceName.indexOf("_");
                                String sufStr = getNroAnalisis();
                                analisis.setText(sufStr);
                                /*if(iniStr != -1)
                                {
                                    sufStr = m_M_AttributeSetInstanceName.substring(0,iniStr);
                                    analisis.setText(sufStr);   
                                }*/

                                
                                
			}
		}
		confirmPanel.getOKButton().setEnabled(enabled);
		log.fine("M_AttributeSetInstance_ID=" + m_M_AttributeSetInstance_ID 
			+ " - " + m_M_AttributeSetInstanceName);
	}	//	enableButtons

	/**
	 *  Mouse Clicked
	 *  @param e event
	 */
	public void mouseClicked(MouseEvent e)
	{
		//  Double click with selected row => exit
		if (e.getClickCount() > 1 && m_table.getSelectedRow() != -1)
		{
			enableButtons();
			dispose();
		}
	}   //  mouseClicked


	/**
	 * 	Get Attribute Set Instance
	 *	@return M_AttributeSetInstance_ID or -1
	 */
	public int getM_AttributeSetInstance_ID()
	{
		return m_M_AttributeSetInstance_ID;
	}	//	getM_AttributeSetInstance_ID

	/**
	 * 	Get Instance Name
	 * 	@return Instance Name
	 */
	public String getM_AttributeSetInstanceName()
	{
		return m_M_AttributeSetInstanceName;
	}	//	getM_AttributeSetInstanceName

        
	private boolean existPermission(int AD_Role_ID, String NombreEvento,
			String Identificador, int AD_Action_ID) {

		String sql = "SELECT ACTIVE FROM AD_PROCESS_PERMISSION WHERE "
				+ NombreEvento + "= " + Identificador + " AND AD_ROLE_ID= "
				+ AD_Role_ID + " AND AD_ACTION_ID= " + AD_Action_ID;
		boolean result = false;

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals("Y"))
					result = true;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (SQLException ex) {
			log.log(Level.SEVERE, sql, ex);
			return false;
		}

		return result;

        }

        private String getNroAnalisis(){
            String nroAnalisis = "";
            try {
                String sql = "select value from m_attributeinstance where m_attributesetinstance_id = " + m_M_AttributeSetInstance_ID;
                PreparedStatement ps = DB.prepareStatement(sql, null);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nroAnalisis = rs.getString(1);
                }
            } catch (SQLException sQLException) {
                System.out.println("No se pudo obtener el Nro. de Analisis");
            }
            return nroAnalisis;
        }


}	//	PAttributeInstance
