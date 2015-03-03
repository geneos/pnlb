/******************************************************************************
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
package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.apps.search.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * Product Attribute Set Product/Instance Dialog Editor. Called from
 * VPAttribute.actionPerformed
 * 
 * @author Jorg Janke
 * @version $Id: VPAttributeDialog.java,v 1.8 2006/10/09 15:29:34 SIGArg-01 Exp $
 */
public class VPAttributeDialog extends CDialog implements ActionListener {
	/**
	 * Product Attribute Instance Dialog
	 * 
	 * @param frame
	 *            parent frame
	 * @param M_AttributeSetInstance_ID
	 *            Product Attribute Set Instance id
	 * @param M_Product_ID
	 *            Product id
	 * @param productWindow
	 *            this is the product window (define Product Instance)
	 * @param AD_Column_ID
	 *            column
	 */
	public VPAttributeDialog(Frame frame, int M_AttributeSetInstance_ID,
			int M_Product_ID, int C_BPartner_ID, boolean productWindow,
			int AD_Column_ID, int WindowNo) {
		super(frame, Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"),
				true);
		log.config("M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID
				+ ", M_Product_ID=" + M_Product_ID + ", C_BPartner_ID="
				+ C_BPartner_ID + ", ProductW=" + productWindow + ", Column="
				+ AD_Column_ID);
		m_WindowNo = Env.createWindowNo(this);
		m_M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
		m_M_Product_ID = M_Product_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		m_productWindow = productWindow;
		m_AD_Column_ID = AD_Column_ID;
		m_WindowNoParent = WindowNo;

		try {
			jbInit();
		} catch (Exception ex) {
			log.log(Level.SEVERE, "VPAttributeDialog" + ex);
		}
		// Dynamic Init
		if (!initAttributes()) {
			dispose();
			return;
		}
		AEnv.showCenterWindow(frame, this);
	} // VPAttributeDialog

	private boolean esWorkflow;
	
	private String AD_Process_Workflow_ID;
	
	private String workflow = "AD_WORKFLOW_ID";

	private final String window = "AD_WINDOW_ID";

	private int m_WindowNo;

	private MAttributeSetInstance m_masi;

	private int m_M_AttributeSetInstance_ID;

	private String m_M_AttributeSetInstanceName;

	private int m_M_Product_ID;

	private int m_C_BPartner_ID;

	private int m_AD_Column_ID;

	private int m_WindowNoParent;

	
	
	/** Enter Product Attributes */
	private boolean m_productWindow = false;

	/** Change */
	private boolean m_changed = false;

	private CLogger log = CLogger.getCLogger(getClass());

	/** Row Counter */
	private int m_row = 0;

	/** List of Editors */
	private ArrayList<CEditor> m_editors = new ArrayList<CEditor>();

	/** Length of Instance value (40) */
	private static final int INSTANCE_VALUE_LENGTH = 40;

	private CCheckBox cbNewEdit = new CCheckBox();

	private CButton bSelect = new CButton(Env.getImageIcon("PAttribute16.gif"));

	// VIT4B - Agregado para el control autom�tico de analisis
	private CButton bAnalisis = new CButton(Msg.getMsg(Env.getCtx(), "New"));

	private VCheckBox CheckAnalisis = new VCheckBox("", false, false, true, Msg
			.translate(Env.getCtx(), "New"), "", false);

	// VIT4B - FIN

	// Lot
	private VString fieldLotString = new VString("Lot", false, false, true, 20,
			20, null, null);

	private VString sufixLotString = new VString("sufixLot", false, false,
			true, 20, 20, null, null);

	private CComboBox fieldLot = null;

	private CButton bLot = new CButton(Msg.getMsg(Env.getCtx(), "New"));

	// Lot Popup
	JPopupMenu popupMenu = new JPopupMenu();

	private CMenuItem mZoom;

	// Ser No
	private VString fieldSerNo = new VString("SerNo", false, false, true, 20,
			20, null, null);

	private CButton bSerNo = new CButton(Msg.getMsg(Env.getCtx(), "New"));

	// Date

	/*
	 * * Vit4B modificado para que tome editable = false de modo que no pueda
	 * cambiar la fecha de vencimiento sino desde una pantalla * creada a tal
	 * efecto * 09/02/2007
	 */
	private VDate fieldGuaranteeDate = new VDate("GuaranteeDate", false, true,
			true, DisplayType.Date, Msg
					.translate(Env.getCtx(), "GuaranteeDate"));

	//
	private CTextField fieldDescription = new CTextField(20);

	//
	private BorderLayout mainLayout = new BorderLayout();

	private CPanel centerPanel = new CPanel();

	private ALayout centerLayout = new ALayout(5, 5, true);

	private ConfirmPanel confirmPanel = new ConfirmPanel(true);

	private boolean flagSave = false;

	private boolean flagNewAnalisis = false;

	private String NextLot = "";

	private String loteSeleccionado = "";

	/**
	 * Layout
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(mainLayout);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		centerPanel.setLayout(centerLayout);
		//
		confirmPanel.addActionListener(this);
	} // jbInit

	/**
	 * 02-11-2010 - Camarzana Mariano
	 * Metodo que me devuelve true si estamos trabajando con un workflow
	 * @param AD_Process_Workflow_ID
	 * @param AD_Window_ID
	 * @return
	 */
	private boolean esWorkflow(String AD_Process_Workflow_ID, String AD_Window_ID){
		String sql = " select count(*) from  ad_workflow adw " +
					 " join ad_wf_node adwn on (adw.ad_workflow_id = adwn.ad_workflow_id)" +
					 " left join ad_window aw on (aw.ad_window_id = adwn.ad_window_id) " +
					 " where  adw.ad_workflow_id= " + AD_Process_Workflow_ID;
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					return rs.getInt(1)>0;
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
			}

		return false;
	}
	
	/**
	 * Dyanmic Init.
	 * 
	 * @return true if initialized
	 */
	private boolean initAttributes() {

		/*
		 * INICIO DE VERIFICACIONES
		 */

		if (m_M_Product_ID == 0)
			return false;

		// Get Model
		m_masi = MAttributeSetInstance.get(Env.getCtx(),
				m_M_AttributeSetInstance_ID, m_M_Product_ID);
		if (m_masi == null) {
			log.severe("No Model for M_AttributeSetInstance_ID="
					+ m_M_AttributeSetInstance_ID + ", M_Product_ID="
					+ m_M_Product_ID);
			return false;
		}
		Env.setContext(Env.getCtx(), m_WindowNo, "M_AttributeSet_ID", m_masi
				.getM_AttributeSet_ID());

		// Get Attribute Set
		MAttributeSet as = m_masi.getMAttributeSet();
		// Product has no Attribute Set
		if (as == null) {
			ADialog.error(m_WindowNo, this, "PAttributeNoAttributeSet");
			return false;
		}
		// Product has no Instance Attributes
		if (!m_productWindow && !as.isInstanceAttribute()) {
			ADialog.error(m_WindowNo, this, "PAttributeNoInstanceAttribute");
			return false;
		}

		/*
		 * FIN DE VERIFICACIONES
		 */

		/*
		 * OBTENER ROLES Y PERMISOS PARA DETERMINAR DE ACA EN ADELANTE LA
		 * ESTRUCTURA DE LA PANTALLA ENFUNCION DE LO QUE PUEDO HACER
		 */

		int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
		MRole rol = new MRole(Env.getCtx(), AD_Role_ID, null);

		/*
		 * Camarzana Mariano - 02/11/2010 - Modificado para recuperar el id de la ventana para poder ver las partidas
		 * FALTA SETEAR ADECUADAMENTE LA VARIABLE 
		 * DE CONTEXTO AD_PROCESS_WORKFLOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		*/
		Integer AD_Window_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNoParent,"WindowID");
		AD_Process_Workflow_ID = Env.getContext(Env.getCtx(), 0,"AD_Process_Workflow_ID");
		if (AD_Process_Workflow_ID == null || AD_Process_Workflow_ID.equals(""))
			esWorkflow = false;
		else
			esWorkflow = esWorkflow(AD_Process_Workflow_ID, AD_Window_ID.toString());
		AD_Process_Workflow_ID =  esWorkflow ? AD_Process_Workflow_ID : AD_Window_ID.toString(); 
	
		// BISion - 16/07/2009 - Santiago Iba�ez
		// Modificado porque traia problemas con los permisos (existPermission)
		if (AD_Process_Workflow_ID == null || AD_Process_Workflow_ID.equals(""))
			AD_Process_Workflow_ID = "0";
		System.out.println(" En Ventana de IATTR AD_WF_Process_ID: "
				+ AD_Process_Workflow_ID);

		/*
		 * FIN DE OBTENER ROLES Y PERMISOS PARA DETERMINAR DE ACA EN ADELANTE LA
		 * ESTRUCTURA DE LA PANTALLA ENFUNCION DE LO QUE PUEDO HACER
		 */

		/*
		 * INICIO DE CONSTRUCCION DE PANTALLA
		 */

		// Show Product Attributes
		if (m_productWindow) {
			MAttribute[] attributes = as.getMAttributes(false);
			log.fine("Product Attributes=" + attributes.length);
			for (int i = 0; i < attributes.length; i++)
				if (AD_Process_Workflow_ID.equals("0")) 
					addAttributeLine(attributes[i], true, !m_productWindow,window,AD_Window_ID.toString());
				else
					addAttributeLine(attributes[i], true, !m_productWindow,workflow,AD_Process_Workflow_ID);
		} else // Set Instance Attributes
		{
			
			
			
				/**
				* 02-11-2010 - Mariano
				* Modificacion realizada para contemplar la ventana factura
				*/
			
			if (!esWorkflow && Env.isSOTrx(Env.getCtx())) {
				if (existPermission(AD_Role_ID, window, AD_Window_ID.toString(), 1)) {
					if (m_M_AttributeSetInstance_ID == 0) // new
						cbNewEdit
								.setText(Msg.getMsg(Env.getCtx(), "NewRecord"));
					else
						cbNewEdit.setText(Msg
								.getMsg(Env.getCtx(), "EditRecord"));

					cbNewEdit.addActionListener(this);
					centerPanel.add(cbNewEdit,
							new ALayoutConstraint(m_row++, 0));

				}

				/*
				 * FIN CHECK NEW/EDIT
				 */

				/*
				 * BOTON DE SELECCION DE INSTANCIAS DE ATRIBUTOS action = 2
				 * 
				 */

				if (existPermission(AD_Role_ID, window, AD_Window_ID.toString(), 2)) {

					bSelect.setText(Msg.getMsg(Env.getCtx(), "SelectExisting"));
					bSelect.addActionListener(this);
					centerPanel.add(bSelect, new ALayoutConstraint(m_row++, 1));

				}

				/*
				 * FIN BOTON DE SELECCION DE INSTANCIAS DE ATRIBUTOS
				 */

				// if(existPermission(AD_Role_ID, AD_Process_Workflow_ID, 4))
				// {
				// All Attributes
				MAttribute[] attributes = as.getMAttributes(true);
				log.fine("Instance Attributes=" + attributes.length);
				for (int i = 0; i < attributes.length; i++)
					addAttributeLine(attributes[i], false, false,window,AD_Window_ID.toString());

				// }

				/*
				 * VIT4B - Agregado para generar un nuevo analisis
				 * automaticamente CHECK DE GENERACION DE ANALISIS
				 * AUTOMATICAMENTE action = 3
				 * 
				 */
				if (existPermission(AD_Role_ID, window, AD_Window_ID.toString(), 3)) {

					centerPanel.add(CheckAnalisis, null);
					CheckAnalisis.addActionListener(this);

				}
				/*
				 * VIT4B - Agregado para generar un nuevo analisis
				 * automaticamente FIN CHECK DE GENERACION DE ANALISIS
				 * AUTOMATICAMENTE
				 */

			}
			/**
			 * Fin de la modificacion para facturas
			 * 
			 */
			
			
			
			
			

			else {

				/*
				 * CHECK NEW/EDIT action = 1
				 * 
				 */

				if (existPermission(AD_Role_ID, workflow,
						AD_Process_Workflow_ID, 1)) {
					if (m_M_AttributeSetInstance_ID == 0) // new
						cbNewEdit
								.setText(Msg.getMsg(Env.getCtx(), "NewRecord"));
					else
						cbNewEdit.setText(Msg
								.getMsg(Env.getCtx(), "EditRecord"));

					cbNewEdit.addActionListener(this);
					centerPanel.add(cbNewEdit,
							new ALayoutConstraint(m_row++, 0));

				}

				/*
				 * FIN CHECK NEW/EDIT
				 */

				/*
				 * BOTON DE SELECCION DE INSTANCIAS DE ATRIBUTOS action = 2
				 * 
				 */

				if (existPermission(AD_Role_ID, workflow,
						AD_Process_Workflow_ID, 2)) {

					bSelect.setText(Msg.getMsg(Env.getCtx(), "SelectExisting"));
					bSelect.addActionListener(this);
					centerPanel.add(bSelect, new ALayoutConstraint(m_row++, 1));

				}

				/*
				 * FIN BOTON DE SELECCION DE INSTANCIAS DE ATRIBUTOS
				 */

				// if(existPermission(AD_Role_ID, AD_Process_Workflow_ID, 4))
				// {
				// All Attributes
				MAttribute[] attributes = as.getMAttributes(true);
				log.fine("Instance Attributes=" + attributes.length);
				for (int i = 0; i < attributes.length; i++)
					addAttributeLine(attributes[i], false, false,workflow,AD_Process_Workflow_ID);

				// }

				/*
				 * VIT4B - Agregado para generar un nuevo analisis
				 * automaticamente CHECK DE GENERACION DE ANALISIS
				 * AUTOMATICAMENTE action = 3
				 * 
				 */

				if (existPermission(AD_Role_ID, workflow,
						AD_Process_Workflow_ID, 3)) {

					centerPanel.add(CheckAnalisis, null);
					CheckAnalisis.addActionListener(this);

				}

				/*
				 * VIT4B - Agregado para generar un nuevo analisis
				 * automaticamente FIN CHECK DE GENERACION DE ANALISIS
				 * AUTOMATICAMENTE
				 */

			}
		}

		// Lot
		if (!m_productWindow && as.isLot()) {

			/*
			 * VIT4B - Agregado para manejar el lote y el sufijo del lote CAMPOS
			 * DE LOTE (FIELDLOTSTRING) Y SUBLOTE (SUFIXLOTSTRING)
			 * 
			 * Action puede ser 5 o 7 en el caso de que sea 7 yo deberia de
			 * tener en cuenta que no existe el concepto de sufijo y es un solo
			 * campo (para el caso de recepcion de material donde puedo ingresar
			 * el lote como lo administra el proveedor)
			 * 
			 * 
			 * 
			 */

			CLabel label = new CLabel(Msg.translate(Env.getCtx(), "Lot"));
			label.setLabelFor(fieldLotString);

			if (existPermission(AD_Role_ID, workflow, AD_Process_Workflow_ID, 5)) {
				centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
				fieldLotString.addActionListener(this);
				centerPanel.add(fieldLotString, null);

				if (AD_Process_Workflow_ID.equals("1001072"))
					fieldLotString.setEditable(false);

				centerPanel.add(sufixLotString, null);

			}

			if (existPermission(AD_Role_ID, workflow, AD_Process_Workflow_ID, 7)) {
				centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
				centerPanel.add(fieldLotString, null);
				// centerPanel.add(sufixLotString, null);
			}

			/*
			 * INSTRUCCION QUE ASIGNA AUTOMATICAMENTE EL SIGUIENTE LOTE
			 */

			if (existPermission(AD_Role_ID, workflow, AD_Process_Workflow_ID, 8)) {
				NextLot = m_masi.getLot();
				fieldLotString.setText(m_masi.getLot());
			}

			/*
			 * INSTRUCCION QUE ASIGNA AUTOMATICAMENTE EL LOTE ACTUAL
			 */

			if (existPermission(AD_Role_ID, workflow, AD_Process_Workflow_ID, 9)) {
				String str = m_masi.getLot();

				int index = 0;

				if (!str.equals("")) {
					index = indexOfAny(str, "1234567890");
					String str_lote = str.substring(0, index + 3);
					System.out.println("Verificando str lote ... " + str_lote);
					String str_sufix = str.substring(index + 3, str.length());
					System.out
							.println("Verificando str sufix ... " + str_sufix);
					fieldLotString.setText(str_lote);
					sufixLotString.setText(str_sufix);
				}
			}

			if (existPermission(AD_Role_ID, workflow, AD_Process_Workflow_ID, 6)) {

				/*
				 * VIT4B - Agregado para manejar el lote y el sufijo del lote
				 * CAMPOS DE LOTE (FIELDLOTSTRING) Y SUBLOTE (SUFIXLOTSTRING)
				 */

				// M_Lot_ID
				// int AD_Column_ID = 9771; // M_AttributeSetInstance.M_Lot_ID
				// fieldLot = new VLookup ("M_Lot_ID", false,false, true,
				// MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID,
				// DisplayType.TableDir));
				/*
				 * SELECT DE LOTES DE PRODUCTO
				 */

				String sql = "SELECT M_Lot_ID, Name " + "FROM M_Lot l "
						+ "WHERE EXISTS (SELECT M_Product_ID FROM M_Product p "
						+ "WHERE p.M_AttributeSet_ID="
						+ m_masi.getM_AttributeSet_ID()
						+ " AND p.M_Product_ID=l.M_Product_ID)";

				fieldLot = new CComboBox(DB.getKeyNamePairs(sql, true));
				label = new CLabel(Msg.translate(Env.getCtx(), "M_Lot_ID"));
				label.setLabelFor(fieldLot);
				centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
				centerPanel.add(fieldLot, null);

				if (m_masi.getM_Lot_ID() != 0) {
					for (int i = 1; i < fieldLot.getItemCount(); i++) {
						KeyNamePair pp = (KeyNamePair) fieldLot.getItemAt(i);
						if (pp.getKey() == m_masi.getM_Lot_ID()) {
							fieldLot.setSelectedIndex(i);
							// fieldLotString.setEditable(false);
							break;
						}
					}
				}
				fieldLot.addActionListener(this);

				// Popup
				fieldLot.addMouseListener(new VPAttributeDialog_mouseAdapter(
						this)); // popup
				mZoom = new CMenuItem(Msg.getMsg(Env.getCtx(), "Zoom"), Env
						.getImageIcon("Zoom16.gif"));
				mZoom.addActionListener(this);
				popupMenu.add(mZoom);

				/*
				 * 
				 * Agregado para generar el lote automaticamente a la entrada
				 * 
				 */
				if (existPermission(AD_Role_ID, workflow,
						AD_Process_Workflow_ID, 8)) {
					KeyNamePair pp = m_masi.getCreateLot(m_M_Product_ID);

					if (pp != null) {
						fieldLot.addItem(pp);
						fieldLot.setSelectedItem(pp);
					}

					this.flagSave = true;

				}
			}

			/*
			 * FIN DE SELECT DE LOTES DE PRODUCTO
			 */

			// New Lot Button
			/*
			 * 
			 * comentado para sacar el boton de nuevo lote
			 * 
			 */

			/*
			 * if (m_masi.getMAttributeSet().getM_LotCtl_ID() != 0) { if
			 * (MRole.getDefault().isTableAccess(MLot.Table_ID, false) &&
			 * MRole.getDefault().isTableAccess(MLotCtl.Table_ID, false) &&
			 * !m_masi.isExcludeLot(m_AD_Column_ID, Env.isSOTrx(Env.getCtx(),
			 * m_WindowNoParent))) { centerPanel.add(bLot, null);
			 * bLot.addActionListener(this); } }
			 * 
			 * 
			 */

		} // Lot

		// SerNo
		if (!m_productWindow && as.isSerNo()) {
			CLabel label = new CLabel(Msg.translate(Env.getCtx(), "SerNo"));
			label.setLabelFor(fieldSerNo);
			fieldSerNo.setText(m_masi.getSerNo());
			centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
			centerPanel.add(fieldSerNo, null);
			// New SerNo Button
			if (m_masi.getMAttributeSet().getM_SerNoCtl_ID() != 0) {
				if (MRole.getDefault().isTableAccess(
						MSerNoCtl.getTableId(MSerNoCtl.Table_Name), false)
						&& !m_masi.isExcludeSerNo(m_AD_Column_ID, Env.isSOTrx(
								Env.getCtx(), m_WindowNoParent))) {
					centerPanel.add(bSerNo, null);
					bSerNo.addActionListener(this);
				}
			}
		} // SerNo

		// GuaranteeDate
		if (!m_productWindow && as.isGuaranteeDate()) {
			CLabel label = new CLabel(Msg.translate(Env.getCtx(),
					"GuaranteeDate"));
			label.setLabelFor(fieldGuaranteeDate);
			if (m_M_AttributeSetInstance_ID == 0)
				fieldGuaranteeDate.setValue(m_masi.getGuaranteeDate(true));
			else
				fieldGuaranteeDate.setValue(m_masi.getGuaranteeDate());
			centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
			centerPanel.add(fieldGuaranteeDate, null);
		} // GuaranteeDate

		/*
		 * OJO BORRADA EN FUNCION DE VER QUE PASA CON ESTA RESTRICCION
		 * 
		 * 
		 * 
		 * 
		 * if (m_row == 0) { ADialog.error(m_WindowNo, this,
		 * "PAttributeNoInfo"); return false; }
		 */

		// New/Edit Window
		/*
		 * if (!m_productWindow) {
		 * cbNewEdit.setSelected(m_M_AttributeSetInstance_ID == 0);
		 * cmd_newEdit(); }
		 */

		// Attrribute Set Instance Description
		CLabel label = new CLabel(Msg.translate(Env.getCtx(), "Description"));
		label.setLabelFor(fieldDescription);
		fieldDescription.setText(m_masi.getDescription());
		fieldDescription.setEditable(false);

		centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
		centerPanel.add(fieldDescription, null);

		// Window usually to wide (??)
		Dimension dd = centerPanel.getPreferredSize();
		dd.width = Math.min(500, dd.width);
		centerPanel.setPreferredSize(dd);

		return true;
	} // initAttribute

	/**
	 * Add Attribute Line
	 * 
	 * @param attribute
	 *            attribute
	 * @param product
	 *            product level attribute
	 * @param readOnly
	 *            value is read only
	 */
	private void addAttributeLine(MAttribute attribute, boolean product,
			boolean readOnly,String workflowWindow,String id) {

		int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
		MRole rol = new MRole(Env.getCtx(), AD_Role_ID, null);

		/*String AD_Process_Workflow_ID = Env.getContext(Env.getCtx(), 0,
				"AD_Process_Workflow_ID");*/
		System.out.println(" En Ventana de IATTR AD_WF_Process_ID: "
				+ AD_Process_Workflow_ID);

		log.fine(attribute + ", Product=" + product + ", R/O=" + readOnly);
		CLabel label = new CLabel(attribute.getName());
		if (product)
			label.setFont(new Font(label.getFont().getFontName(), Font.BOLD,
					label.getFont().getSize()));
		if (attribute.getDescription() != null)
			label.setToolTipText(attribute.getDescription());

		if (existPermission(AD_Role_ID, workflowWindow, AD_Process_Workflow_ID, 4)) {
			centerPanel.add(label, new ALayoutConstraint(m_row++, 0));
		}
		//
		MAttributeInstance instance = attribute
				.getMAttributeInstance(m_M_AttributeSetInstance_ID);
		if (MAttribute.ATTRIBUTEVALUETYPE_List.equals(attribute
				.getAttributeValueType())) {
			MAttributeValue[] values = attribute.getMAttributeValues(); // optional
			// =
			// null
			CComboBox editor = new CComboBox(values);
			boolean found = false;
			if (instance != null) {
				for (int i = 0; i < values.length; i++) {
					if (values[i] != null
							&& values[i].getM_AttributeValue_ID() == instance
									.getM_AttributeValue_ID()) {
						editor.setSelectedIndex(i);
						found = true;
						break;
					}
				}
				if (found)
					log.fine("Attribute=" + attribute.getName() + " #"
							+ values.length + " - found: " + instance);
				else
					log.warning("Attribute=" + attribute.getName() + " #"
							+ values.length + " - NOT found: " + instance);
			} // setComboBox
			else
				log.fine("Attribute=" + attribute.getName() + " #"
						+ values.length + " no instance");
			label.setLabelFor(editor);

			if (existPermission(AD_Role_ID, workflowWindow, AD_Process_Workflow_ID, 4)) {
				centerPanel.add(editor, null);
			}
			if (readOnly)
				editor.setEnabled(false);
			else
				m_editors.add(editor);
		} else if (MAttribute.ATTRIBUTEVALUETYPE_Number.equals(attribute
				.getAttributeValueType())) {
			VNumber editor = new VNumber(attribute.getName(), attribute
					.isMandatory(), false, true, DisplayType.Number, attribute
					.getName());
			if (instance != null)
				editor.setValue(instance.getValueNumber());
			else
				editor.setValue(Env.ZERO);
			label.setLabelFor(editor);
			if (existPermission(AD_Role_ID, workflowWindow, AD_Process_Workflow_ID, 4)) {
				centerPanel.add(editor, null);
			}
			if (readOnly)
				editor.setEnabled(false);
			else
				m_editors.add(editor);
		} else // Text Field
		{
			VString editor = new VString(attribute.getName(), attribute
					.isMandatory(), false, true, 20, INSTANCE_VALUE_LENGTH,
					null, null);
			if (instance != null)
				editor.setText(instance.getValue());
			label.setLabelFor(editor);
			if (existPermission(AD_Role_ID, workflowWindow, AD_Process_Workflow_ID, 4)) {
				centerPanel.add(editor, null);
			}
			if (readOnly)
				editor.setEnabled(false);
			else
				m_editors.add(editor);
		}
	} // addAttributeLine

	/**
	 * dispose
	 */
	public void dispose() {
		removeAll();
		Env.clearWinContext(m_WindowNo);
		super.dispose();
	} // dispose

	/**
	 * ActionListener
	 * 
	 * @param e
	 *            event
	 */
	public void actionPerformed(ActionEvent e) {
		// Select Instance
		if (e.getSource() == bSelect) {
			if (cmd_select())
				dispose();
		}
		// fieldLotString
		else if (e.getSource() == fieldLotString) {
			/*
			 * * Agregado para mantener numeros de analisis de 3 digitos ej: 001 *
			 * VIT4B 24/11/2006
			 */

			String nroLote = fieldLotString.getText();

			if (nroLote.length() == 1)
				fieldLotString.setText("00" + nroLote);
			else if (nroLote.length() == 2)
				fieldLotString.setText("0" + nroLote);

			/*
			 * * FIN * VIT4B 24/11/2006
			 */
		}

		// New/Edit
		else if (e.getSource() == cbNewEdit) {
			cmd_newEdit();
		}

		// VIT4B - New Attribute

		else if (e.getSource() == CheckAnalisis){
			this.flagNewAnalisis = true;
			/*15-08-2011 Camarzana Mariano
			 * Cuando se requiere una partida nueva seteo el id en 0 de manera que cuando salvo la partida
			 * al tener el id en 0 me cree una nueva
			 */
			this.m_masi.setM_AttributeSetInstance_ID(0); 
		}

		// Select Lot from existing
		else if (e.getSource() == fieldLot) {
			this.flagSave = false;
			KeyNamePair pp = (KeyNamePair) fieldLot.getSelectedItem();
			if (pp != null && pp.getKey() != -1) {
				/*
				 * En este punto lo que recupero
				 * 
				 */

				String str = pp.getName();
				loteSeleccionado = str;
				int index = 0;

				if (!str.equals("")) {
					index = indexOfAny(str, "1234567890");
					String str_lote = str.substring(0, index + 3);
					System.out.println("Verificando str lote ... " + str_lote);
					String str_sufix = str.substring(index + 3, str.length());
					System.out
							.println("Verificando str sufix ... " + str_sufix);
					fieldLotString.setText(str_lote);
					sufixLotString.setText(str_sufix);
				}

				// fieldLotString.setEditable(false);
				m_masi.setM_Lot_ID(pp.getKey());
			} else {
				// fieldLotString.setEditable(true);
				m_masi.setM_Lot_ID(0);
			}
		}

		// Create New Lot
		else if (e.getSource() == bLot) {
			// KeyNamePair pp = m_masi.createLot(m_M_Product_ID,
			// sufixLotString.getText());
			KeyNamePair pp = m_masi.getCreateLot(m_M_Product_ID);

			if (pp != null) {
				fieldLot.addItem(pp);
				fieldLot.setSelectedItem(pp);
			}

			this.flagSave = true;
		}
		// Create New SerNo
		else if (e.getSource() == bSerNo) {
			fieldSerNo.setText(m_masi.getSerNo(true));
		}

		// OK
		else if (e.getActionCommand().equals(ConfirmPanel.A_OK)) {

			/*
			 * * En el caso de que tenga permisos para editar el analisis
			 * deberia de verificar * que el analisis no exista y de existir
			 * informarlo y no dejar continuar.
			 */

			/*
			 * int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx()); MRole rol = new
			 * MRole(Env.getCtx(),AD_Role_ID,null); String
			 * AD_Process_Workflow_ID =
			 * Env.getContext(Env.getCtx(),0,"AD_Process_Workflow_ID");
			 * 
			 * 
			 * if(existPermission(AD_Role_ID, AD_Process_Workflow_ID, 4)) {
			 * MAttributeSet as = m_masi.getMAttributeSet();
			 * 
			 * if (as == null) return;
			 * 
			 * MAttribute[] att = as.getMAttributes(!m_productWindow); for (int
			 * i = 0; i < att.length; i++) { if
			 * (MAttribute.ATTRIBUTEVALUETYPE_List.equals(att[i].getAttributeValueType())) {
			 * CComboBox editor = (CComboBox)m_editors.get(i); MAttributeValue
			 * value = (MAttributeValue)editor.getSelectedItem();
			 * 
			 * if(this.verificarValueAttributeInstance(value.getName(),att[i].get_ID())) {
			 * JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"An�lisis
			 * existente"), "Info" , JOptionPane.INFORMATION_MESSAGE); return; } }
			 * else if
			 * (MAttribute.ATTRIBUTEVALUETYPE_Number.equals(att[i].getAttributeValueType())) {
			 * VNumber editor = (VNumber)m_editors.get(i); BigDecimal value =
			 * (BigDecimal)editor.getValue();
			 * if(this.verificarValueAttributeInstance(value.toString(),att[i].get_ID())) {
			 * JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"An�lisis
			 * existente"), "Info" , JOptionPane.INFORMATION_MESSAGE); return; } }
			 * else { VString editor = (VString)m_editors.get(i); String value =
			 * editor.getText();
			 * if(this.verificarValueAttributeInstance(value.toString(),att[i].get_ID())) {
			 * JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"An�lisis
			 * existente"), "Info" , JOptionPane.INFORMATION_MESSAGE); return; } } } //
			 * for all attributes
			 *  }
			 * 
			 */

			/*
			 * * Si esta vacio el lote crea un lote automaticamente para el
			 * producto. * VIT4B - previo al 27/11/2006
			 * 
			 * 
			 * if(fieldLotString.getText().equals("")) {
			 * 
			 * KeyNamePair pp = m_masi.getCreateLot(m_M_Product_ID);
			 * 
			 * if (pp != null) { fieldLotString.setText (pp.getName()); }
			 * 
			 * this.flagSave = true; }
			 *  /* * Agregado para mantener n�meros de an�lisis de 3 digitos ej:
			 * 001 * VIT4B 24/11/2006
			 * 
			 * 
			 * String nroLote = fieldLotString.getText();
			 * 
			 * if(nroLote.length() == 1) fieldLotString.setText("00" + nroLote);
			 * else if(nroLote.length() == 2) fieldLotString.setText("0" +
			 * nroLote);
			 *  /* * FIN * VIT4B 24/11/2006
			 */

			/*
			 * 
			 * String loteCompleto = fieldLotString.getText() +
			 * sufixLotString.getText();
			 * 
			 * 
			 * if(!loteSeleccionado.equals(loteCompleto)) { if(this.flagSave) {
			 * m_masi.createLot(m_M_Product_ID, sufixLotString.getText());
			 * System.out.println("por save FLAG: " + this.flagSave + " TEXTO: " +
			 * sufixLotString.getText());
			 *  } else if(!sufixLotString.getText().equals("")) {
			 * m_masi.createLot(m_M_Product_ID, fieldLotString.getText(),
			 * sufixLotString.getText()); System.out.println("por save pero con =
			 * lote FLAG: " + this.flagSave + " TEXTO: " +
			 * sufixLotString.getText()); } }
			 * 
			 * 
			 * 
			 * if (saveSelection()) dispose();
			 * 
			 */

			/*
			 * * Agregado para administrar la creacion de lotes con 3 digitos *
			 * VIT4B 27/11/2006
			 */

			if (fieldLotString.getText().equals("")) {
				/*
				 * 10-02-2011 Camarzana Mariano
				 * Bloque comentado porque creaba las partidas al seleccionar "ok" 
				 */
				
				/*
				 * * Crea el lote autom�ticamente * VIT4B - 27/11/2006
				 */
				KeyNamePair pp = m_masi.createLot(m_M_Product_ID);
				if (pp != null) {
					fieldLotString.setText(pp.getName());
				}
			} else {
			
				String loteCompleto = fieldLotString.getText()
						+ sufixLotString.getText();

				/*
				 * * Verificar que le lote ingresado manualmente no exista *
				 * VIT4B - 30/11/2006
				 */

				/*
				 * * Crea el lote a partir de los datos pasados en los campos de
				 * lote y sufijo * VIT4B - 27/11/2006
				 */
				// if(!loteSeleccionado.equals(loteCompleto) && result == 0)
				if (!loteSeleccionado.equals(loteCompleto)) {
					int result = validarLote(loteCompleto, m_M_Product_ID);
					/*
					 * Vit4B Modificacion para que ahora si permita dos
					 * instancias con el mismo lote
					 * 
					 * 12/11/2007
					 * 
					 * 
					 * if(result != 0) { m_masi.setM_Lot_ID(result); } else {
					 * m_masi.createLot(m_M_Product_ID,
					 * fieldLotString.getText(), sufixLotString.getText()); }
					 */

					m_masi.createLot(m_M_Product_ID, fieldLotString.getText(),
							sufixLotString.getText());

				}		
				if (saveSelection())
					dispose();
			}
		}
		// Cancel
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {
			m_changed = false;
			dispose();
		}
		// Zoom M_Lot
		else if (e.getSource() == mZoom) {
			cmd_zoom();
		} else
			log.log(Level.SEVERE, "not found - " + e);
	} // actionPerformed

	/**
	 * Instance Selection Button
	 * 
	 * @return true if selected
	 */
	private boolean cmd_select() {
		log.config("");

		/*
		 * VIT4B - Cambio para que la elecci�n de la instancia de atributos se
		 * realice del deposito correpondiente y no del deposito de inicio que
		 * esta en el contexto.
		 * 
		 */

		int AD_Role_ID = Env.getAD_Role_ID(Env.getCtx());
		MRole rol = new MRole(Env.getCtx(), AD_Role_ID, null);
		String sql = "";
		String title = "";
		int M_Warehouse_ID = 0;

		boolean flagWharehouse = true;
		/*
		 * OJO CON ESTOOOOOOOOOOO
		 * 
		 */

		if (rol.getProcessAccess(1000122) == null) {
			M_Warehouse_ID = Env.getContextAsInt(Env.getCtx(),
					m_WindowNoParent, "M_Warehouse_ID");
			sql = "SELECT p.Name, w.Name FROM M_Product p, M_Warehouse w "
					+ "WHERE p.M_Product_ID=? AND w.M_Warehouse_ID=?";
		} else {
			int DocTypeLocal = Env.getContextAsInt(Env.getCtx(),
					m_WindowNoParent, "C_DocType_ID");

			if (DocTypeLocal == 1000149) {
				int M_Locator_ID = Env.getContextAsInt(Env.getCtx(),
						m_WindowNoParent, "M_Locator_ID");
				MLocator MLocatorInstance = MLocator.getDefault(Env.getCtx(),
						M_Locator_ID);
				M_Warehouse_ID = MLocatorInstance.getM_Warehouse_ID();

				// M_Wharehouse;

				sql = "SELECT p.Name, w.Name FROM M_Product p, M_Warehouse w "
						+ "WHERE p.M_Product_ID=? AND w.M_Warehouse_ID=?";
				flagWharehouse = true;
			} else {
				sql = "SELECT p.Name, w.Name FROM M_Product p, M_Warehouse w "
						+ "WHERE p.M_Product_ID=?";
				flagWharehouse = false;
			}

		}

		/*
		 * VIT4B - FIN
		 * 
		 */

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			if (flagWharehouse)
				pstmt.setInt(2, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				title = rs.getString(1) + " - " + rs.getString(2);
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		try {
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			pstmt = null;
		}
		
		PAttributeInstance pai = new PAttributeInstance(this, title,M_Warehouse_ID, 0,
				m_M_Product_ID, m_C_BPartner_ID,esWorkflow,Env.getContextAsInt(Env.getCtx(), m_WindowNoParent,"WindowID"));
		if (pai.getM_AttributeSetInstance_ID() != -1) {
			m_M_AttributeSetInstance_ID = pai.getM_AttributeSetInstance_ID();
			m_M_AttributeSetInstanceName = pai.getM_AttributeSetInstanceName();
			m_changed = true;
			return true;

		}
		return false;
	} // cmd_select

	/**
	 * Instance New/Edit
	 */
	private void cmd_newEdit() {
		boolean rw = cbNewEdit.isSelected();
		log.config("R/W=" + rw + " " + m_masi);
		//
		fieldLotString.setEditable(rw && m_masi.getM_Lot_ID() == 0);
		if (fieldLot != null)
			fieldLot.setReadWrite(rw);
		bLot.setReadWrite(rw);
		fieldSerNo.setReadWrite(rw);
		bSerNo.setReadWrite(rw);
		fieldGuaranteeDate.setReadWrite(rw);
		//
		for (int i = 0; i < m_editors.size(); i++) {
			CEditor editor = (CEditor) m_editors.get(i);
			editor.setReadWrite(rw);
		}
	} // cmd_newEdit

	/**
	 * Zoom M_Lot
	 */
	private void cmd_zoom() {
		int M_Lot_ID = 0;
		KeyNamePair pp = (KeyNamePair) fieldLot.getSelectedItem();
		if (pp != null)
			M_Lot_ID = pp.getKey();
		MQuery zoomQuery = new MQuery("M_Lot");
		zoomQuery.addRestriction("M_Lot_ID", MQuery.EQUAL, M_Lot_ID);
		log.info(zoomQuery.toString());
		//
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//
		int AD_Window_ID = 257; // Lot
		AWindow frame = new AWindow();
		if (frame.initWindow(AD_Window_ID, zoomQuery)) {
			this.setVisible(false);
			this.setModal(false); // otherwise blocked
			this.setVisible(true);
			AEnv.showScreen(frame, SwingConstants.EAST);
		}
		// async window - not able to get feedback
		frame = null;
		//
		setCursor(Cursor.getDefaultCursor());
	} // cmd_zoom

	/**
	 * Save Selection
	 * 
	 * @return true if saved
	 */
	private boolean saveSelection() {

		MAttributeSet as = m_masi.getMAttributeSet();

		if (as == null)
			return true;

		MAttribute[] att = as.getMAttributes(!m_productWindow);
		for (int i = 0; i < att.length; i++) {
			if (MAttribute.ATTRIBUTEVALUETYPE_List.equals(att[i]
					.getAttributeValueType())) {
				CComboBox editor = (CComboBox) m_editors.get(i);
				MAttributeValue value = (MAttributeValue) editor
						.getSelectedItem();
				/*
				 * if(this.verificarValueAttributeInstance(value.getName(),att[i].get_ID())) {
				 * JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"An�lisis
				 * existente"), "Info" , JOptionPane.INFORMATION_MESSAGE);
				 * return false; }
				 */
			} else if (MAttribute.ATTRIBUTEVALUETYPE_Number.equals(att[i]
					.getAttributeValueType())) {
				VNumber editor = (VNumber) m_editors.get(i);
				BigDecimal value = (BigDecimal) editor.getValue();
				/*
				 * if(this.verificarValueAttributeInstance(value.toString(),att[i].get_ID())) {
				 * JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"An�lisis
				 * existente"), "Info" , JOptionPane.INFORMATION_MESSAGE);
				 * return false; }
				 */
			} else {
				VString editor = (VString) m_editors.get(i);
				String value = editor.getText();
				/*
				 * if(this.verificarValueAttributeInstance(value.toString(),att[i].get_ID())) {
				 * JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"An�lisis
				 * existente"), "Info" , JOptionPane.INFORMATION_MESSAGE);
				 * return false; }
				 */
			}
			m_changed = true;
		} // for all attributes

		log.info("saveSelection");
		String str = fieldLotString.getText();
		System.out.println("str del lote: " + str + " longitud: "
				+ str.length());
		/*
		 * if (str.length() != 3) { ADialog.error(m_WindowNo, this, "Lote
		 * inv�lido", null); return false; }
		 */

		//
		m_changed = false;
		String mandatory = "";
		if (!m_productWindow && as.isLot()) {
			log.fine("Lot=" + fieldLotString.getText()
					+ sufixLotString.getText());
			String text = fieldLotString.getText() + sufixLotString.getText();

			m_masi.setLot(text);
			if (as.isLotMandatory() && (text == null || text.length() == 0))
				mandatory += " - " + Msg.translate(Env.getCtx(), "Lot");
			m_changed = true;
		} // Lot
		if (!m_productWindow && as.isSerNo()) {
			log.fine("SerNo=" + fieldSerNo.getText());
			String text = fieldSerNo.getText();
			m_masi.setSerNo(text);
			if (as.isSerNoMandatory() && (text == null || text.length() == 0))
				mandatory += " - " + Msg.translate(Env.getCtx(), "SerNo");
			m_changed = true;
		} // SerNo
		if (!m_productWindow && as.isGuaranteeDate()) {
			log.fine("GuaranteeDate=" + fieldGuaranteeDate.getValue());
			Timestamp ts = (Timestamp) fieldGuaranteeDate.getValue();
			m_masi.setGuaranteeDate(ts);
			if (as.isGuaranteeDateMandatory() && ts == null)
				mandatory += " - "
						+ Msg.translate(Env.getCtx(), "GuaranteeDate");
			m_changed = true;
		} // GuaranteeDate

		// *** Save Attributes ***
		// New Instance
		if (m_changed || m_masi.getM_AttributeSetInstance_ID() == 0) {
			m_masi.save();
			m_M_AttributeSetInstance_ID = m_masi.getM_AttributeSetInstance_ID();
			m_M_AttributeSetInstanceName = m_masi.getDescription();
		}

		// Save Instance Attributes

		/*
		 * Aqu� es donde obtiene el an�lisis desde el campo de la interfaz.
		 * podr�a setear con un valor por defecto en caso de que sea vacio para
		 * que no me asigne un numero de forma autom�tica.
		 * 
		 * setMAttributeInstance es quien actualiza o graba una instancia de
		 * analisis
		 * 
		 * 
		 */

		MAttribute[] attributes = as.getMAttributes(!m_productWindow);
		for (int i = 0; i < attributes.length; i++) {

			VString editor_tmp = (VString) m_editors.get(i);
			String value_tmp = editor_tmp.getText();
			boolean CheckValue = ((Boolean) CheckAnalisis.getValue())
					.booleanValue();

			if (!value_tmp.equals("") || CheckValue)

			{

				if (CheckValue) {

					if (MAttribute.ATTRIBUTEVALUETYPE_List.equals(attributes[i]
							.getAttributeValueType())) {
						CComboBox editor = (CComboBox) m_editors.get(i);
						MAttributeValue value = (MAttributeValue) editor
								.getSelectedItem();
						log.fine(attributes[i].getName() + "=" + value);
						if (attributes[i].isMandatory() && value == null)
							mandatory += " - " + attributes[i].getName();
						attributes[i].setMAttributeInstance(
								m_M_AttributeSetInstance_ID, "");
					} else if (MAttribute.ATTRIBUTEVALUETYPE_Number
							.equals(attributes[i].getAttributeValueType())) {
						VNumber editor = (VNumber) m_editors.get(i);
						BigDecimal value = (BigDecimal) editor.getValue();
						log.fine(attributes[i].getName() + "=" + value);
						if (attributes[i].isMandatory() && value == null)
							mandatory += " - " + attributes[i].getName();
						attributes[i].setMAttributeInstance(
								m_M_AttributeSetInstance_ID, "");
					} else {
						VString editor = (VString) m_editors.get(i);
						String value = editor.getText();
						log.fine(attributes[i].getName() + "=" + value);
						if (attributes[i].isMandatory()
								&& (value == null || value.length() == 0))
							mandatory += " - " + attributes[i].getName();
						attributes[i].setMAttributeInstance(
								m_M_AttributeSetInstance_ID, "");
					}
					m_changed = true;
				} else {
					if (MAttribute.ATTRIBUTEVALUETYPE_List.equals(attributes[i]
							.getAttributeValueType())) {
						CComboBox editor = (CComboBox) m_editors.get(i);
						MAttributeValue value = (MAttributeValue) editor
								.getSelectedItem();
						log.fine(attributes[i].getName() + "=" + value);
						if (attributes[i].isMandatory() && value == null)
							mandatory += " - " + attributes[i].getName();
						attributes[i].setMAttributeInstance(
								m_M_AttributeSetInstance_ID, value);
					} else if (MAttribute.ATTRIBUTEVALUETYPE_Number
							.equals(attributes[i].getAttributeValueType())) {
						VNumber editor = (VNumber) m_editors.get(i);
						BigDecimal value = (BigDecimal) editor.getValue();
						log.fine(attributes[i].getName() + "=" + value);
						if (attributes[i].isMandatory() && value == null)
							mandatory += " - " + attributes[i].getName();
						attributes[i].setMAttributeInstance(
								m_M_AttributeSetInstance_ID, value);
					} else {
						VString editor = (VString) m_editors.get(i);
						String value = editor.getText();
						log.fine(attributes[i].getName() + "=" + value);
						if (attributes[i].isMandatory()
								&& (value == null || value.length() == 0))
							mandatory += " - " + attributes[i].getName();
						attributes[i].setMAttributeInstance(
								m_M_AttributeSetInstance_ID, value);
					}
					m_changed = true;

				}

			}

		}

		// Save Model
		if (m_changed) {
			m_masi.setDescription();
			m_masi.save();
		}

		m_M_AttributeSetInstance_ID = m_masi.getM_AttributeSetInstance_ID();
		m_M_AttributeSetInstanceName = m_masi.getDescription();
		//
		/*
		 * if (mandatory.length() > 0) { ADialog.error(m_WindowNo, this,
		 * "FillMandatory", mandatory); return false; }
		 */
		return true;
	} // saveSelection

	/***************************************************************************
	 * Get Instance ID
	 * 
	 * @return Instance ID
	 */
	public int getM_AttributeSetInstance_ID() {
		return m_M_AttributeSetInstance_ID;
	} // getM_AttributeSetInstance_ID

	/**
	 * Get Instance Name
	 * 
	 * @return Instance Name
	 */
	public String getM_AttributeSetInstanceName() {
		return m_M_AttributeSetInstanceName;
	} // getM_AttributeSetInstanceName

	/**
	 * Value Changed
	 * 
	 * @return true if changed
	 */
	public boolean isChanged() {
		return m_changed;
	} // isChanged

	public int indexOfAny(String word, String numbers) {
		int i = 0;
		for (i = 0; i < word.length(); i++) {
			if (numbers.indexOf(word.charAt(i)) != -1) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * VIT4B - Recuperar el nro de ventana padre ...
	 * 
	 * 
	 */

	public int getWindowNoParent() {
		return m_WindowNoParent;
	}

	/*
	 * VIT4B - FIN ...
	 * 
	 * 
	 */

	private boolean verificarValueAttributeInstance(String value,
			int M_Attribute_ID) {

		/*
		 * VIT4B - Aqu� deber�a de controlar el echo de no permitir an�lisis
		 * repetidos ...
		 * 
		 * 
		 */

		if (value.equals(""))
			return false;

		String sql = "SELECT * FROM M_AttributeInstance WHERE Value = '"
				+ value + "' AND M_Attribute_ID = " + M_Attribute_ID;

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				rs.close();
				pstmt.close();
				pstmt = null;
				return true;

			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (SQLException ex) {
			log.log(Level.SEVERE, sql, ex);
			return false;
		}

		return false;
	}

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

	/*
	 * private boolean existPermission(int AD_Role_ID, String
	 * AD_Process_Workflow_ID, int AD_Action_ID) {
	 * 
	 * String sql = "SELECT ACTIVE FROM AD_PROCESS_PERMISSION WHERE
	 * AD_WORKFLOW_ID= " + AD_Process_Workflow_ID + " AND AD_ROLE_ID= " +
	 * AD_Role_ID + " AND AD_ACTION_ID= " + AD_Action_ID;
	 * 
	 * boolean result = false;
	 * 
	 * PreparedStatement pstmt = null; try { pstmt = DB.prepareStatement(sql,
	 * null); ResultSet rs = pstmt.executeQuery(); while (rs.next()) {
	 * if(rs.getString(1).equals("Y")) result = true; } rs.close();
	 * pstmt.close(); pstmt = null; } catch (SQLException ex) {
	 * log.log(Level.SEVERE, sql, ex); return false; }
	 * 
	 * return result;
	 *  }
	 */

	private int validarLote(String loteCompleto, int product) {
		String sql = "SELECT m.m_attributesetinstance_id "
				+ "FROM m_attributesetinstance m INNER JOIN m_lot l ON (m.m_lot_id = l.m_lot_id) "
				+ "WHERE l.name = '" + loteCompleto + "' AND l.m_product_id = "
				+ product;

		int result = 0;

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (SQLException ex) {
			log.log(Level.SEVERE, sql, ex);
		}

		return result;
	}

} // VPAttributeDialog

/*******************************************************************************
 * Mouse Listener for Popup Menu
 */
final class VPAttributeDialog_mouseAdapter extends java.awt.event.MouseAdapter {
	/**
	 * Constructor
	 * 
	 * @param adaptee
	 *            adaptee
	 */
	VPAttributeDialog_mouseAdapter(VPAttributeDialog adaptee) {
		this.adaptee = adaptee;
	} // VPAttributeDialog_mouseAdapter

	private VPAttributeDialog adaptee;

	/**
	 * Mouse Listener
	 * 
	 * @param e
	 *            MouseEvent
	 */
	public void mouseClicked(MouseEvent e) {
		// System.out.println("mouseClicked " + e.getID() + " " +
		// e.getSource().getClass().toString());
		// popup menu
		if (SwingUtilities.isRightMouseButton(e))
			adaptee.popupMenu.show((Component) e.getSource(), e.getX(), e
					.getY());
	} // mouse Clicked

} // VPAttributeDialog_mouseAdapter
