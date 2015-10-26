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
package org.compiere.model;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;

import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Shipment Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInOut.java,v 1.78 2006/01/04 05:39:20 jjanke Exp $
 */
public class MInOut extends X_M_InOut implements DocAction
{
	/**
	 * 	Create Shipment From Order
	 *	@param order order
	 *	@param movementDate optional movement date
	 *	@param forceDelivery ignore order delivery rule
	 *	@param allAttributeInstances if true, all attribute set instances
	 *	@param minGuaranteeDate optional minimum guarantee date if all attribute instances
	 *	@param complete complete document (Process if false, Complete if true)
	 *	@return Shipment or null
	 */
	public static MInOut createFrom (MOrder order, Timestamp movementDate,
		boolean forceDelivery, boolean allAttributeInstances, Timestamp minGuaranteeDate,
		boolean complete, String trxName)
	{
		if (order == null)
			throw new IllegalArgumentException("No Order");
		//
		if (!forceDelivery && DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule()))
		{
			return null;
		}

		//	Create Meader
		MInOut retValue = new MInOut (order, 0, movementDate);
		retValue.setDocAction(complete ? DOCACTION_Complete : DOCACTION_Prepare);

		//	Check if we can create the lines
		MOrderLine[] oLines = order.getLines(true, "M_Product_ID");
		for (int i = 0; i < oLines.length; i++)
		{
			BigDecimal qty = oLines[i].getQtyOrdered().subtract(oLines[i].getQtyDelivered());
			//	Nothing to deliver
			if (qty.signum() == 0)
				continue;
			//	Stock Info
			MStorage[] storages = null;
			MProduct product = oLines[i].getProduct();
			if (product != null && product.get_ID() != 0 && product.isStocked())
			{
				MProductCategory pc = MProductCategory.get(order.getCtx(), product.getM_Product_Category_ID());
				String MMPolicy = pc.getMMPolicy();
				if (MMPolicy == null || MMPolicy.length() == 0)
				{
					MClient client = MClient.get(order.getCtx());
					MMPolicy = client.getMMPolicy();
				}
				storages = MStorage.getWarehouse (order.getCtx(), order.getM_Warehouse_ID(),
					oLines[i].getM_Product_ID(), oLines[i].getM_AttributeSetInstance_ID(),
					product.getM_AttributeSet_ID(),
					allAttributeInstances, minGuaranteeDate,
					MClient.MMPOLICY_FiFo.equals(MMPolicy), trxName);
			}
			if (!forceDelivery)
			{
				BigDecimal maxQty = Env.ZERO;
				for (int ll = 0; ll < storages.length; ll++)
					maxQty = maxQty.add(storages[ll].getQtyOnHand());
				if (DELIVERYRULE_Availability.equals(order.getDeliveryRule()))
				{
					if (maxQty.compareTo(qty) < 0)
						qty = maxQty;
				}
				else if (DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule()))
				{
					if (maxQty.compareTo(qty) < 0)
						continue;
				}
			}
			//	Create Line
			if (retValue.get_ID() == 0)	//	not saved yet
				retValue.save(trxName);
			//	Create a line until qty is reached
			for (int ll = 0; ll < storages.length; ll++)
			{
				BigDecimal lineQty = storages[ll].getQtyOnHand();
				if (lineQty.compareTo(qty) > 0)
					lineQty = qty;
				MInOutLine line = new MInOutLine (retValue);
				line.setOrderLine(oLines[i], storages[ll].getM_Locator_ID(),
					order.isSOTrx() ? lineQty : Env.ZERO);
				line.setQty(lineQty);	//	Correct UOM for QtyEntered
				/**
                 * BISion - 25/06/2009 - Santiago Ibañez
                 * Modificacion realizada para que no haya diferencias entre la
                 * cantidad ingresada y la del movimiento.
                 * Se comento la linea a continuacion
                 */
                /*if (oLines[i].getQtyEntered().compareTo(oLines[i].getQtyOrdered()) != 0)
					line.setQtyEntered(lineQty
						.multiply(oLines[i].getQtyEntered())
						//begin e-evolution vpj-cd bigfix version 1.80, Sat Jan 28 01:28:28 2006 UTC
						//.divide(oLines[i].getQtyOrdered(), BigDecimal.ROUND_HALF_UP));
						 .divide(oLines[i].getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));*/
						//end e-evolution vpj-cd bigfix
                //fin modificacion BISion
				line.save(trxName);
				//	Delivered everything ?
				qty = qty.subtract(lineQty);
			//	storage[ll].changeQtyOnHand(lineQty, !order.isSOTrx());	// Credit Memo not considered
			//	storage[ll].save(get_TrxName());
				if (qty.signum() == 0)
					break;
			}
		}	//	for all order lines

		//	No Lines saved
		if (retValue.get_ID() == 0)
			return null;

		return retValue;
	}	//	createFrom

	/**
	 * 	Create new Shipment by copying
	 * 	@param from shipment
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocType_ID doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trxName trx
	 * 	@param setOrder set the order link
	 *	@return Shipment
	 */
	public static MInOut copyFrom (MInOut from, Timestamp dateDoc,
		int C_DocType_ID, boolean isSOTrx, boolean counter, String trxName, boolean setOrder)
	{
		MInOut to = new MInOut (from.getCtx(), 0, null);
		to.set_TrxName(trxName);
		copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("M_InOut_ID", I_ZERO);

		String sql = "SELECT d.IsDocNoControlled, s.CurrentNext "
			+ "FROM C_DocType d, AD_Sequence s "
			+ "WHERE C_DocType_ID=?"		//	1
			+ " AND d.DocNoSequence_ID=s.AD_Sequence_ID(+)";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocType_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				if (rs.getString(1).equals("Y"))
					to.setDocumentNo("<" + rs.getString(2) + ">");
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {e.printStackTrace();}

		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setC_DocType_ID (C_DocType_ID);
		to.setIsSOTrx(isSOTrx);
		if (counter)
			to.setMovementType (isSOTrx ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReceipts);
		//
		to.setDateOrdered (dateDoc);
		to.setDateAcct (dateDoc);
		to.setMovementDate(dateDoc);
		to.setDatePrinted(null);
		to.setIsPrinted (false);
		to.setDateReceived(null);
		to.setNoPackages(0);
		to.setShipDate(null);
		to.setPickDate(null);
		to.setIsInTransit(false);
		//
		to.setIsApproved (false);
		to.setC_Invoice_ID(0);
		to.setTrackingNo(null);
		to.setIsInDispute(false);
		//
		to.setPosted (false);
		to.setProcessed (false);
		to.setC_Order_ID(0);	//	Overwritten by setOrder
		if (counter)
		{
			to.setC_Order_ID(0);
			to.setRef_InOut_ID(from.getM_InOut_ID());
			//	Try to find Order/Invoice link
			if (from.getC_Order_ID() != 0)
			{
				MOrder peer = new MOrder (from.getCtx(), from.getC_Order_ID(), from.get_TrxName());
				if (peer.getRef_Order_ID() != 0)
					to.setC_Order_ID(peer.getRef_Order_ID());
			}
			if (from.getC_Invoice_ID() != 0)
			{
				MInvoice peer = new MInvoice (from.getCtx(), from.getC_Invoice_ID(), from.get_TrxName());
				if (peer.getRef_Invoice_ID() != 0)
					to.setC_Invoice_ID(peer.getRef_Invoice_ID());
			}
		}
		else
		{
			to.setRef_InOut_ID(0);
			if (setOrder)
				to.setC_Order_ID(from.getC_Order_ID());
		}
		//
		if (!to.save(trxName))
			throw new IllegalStateException("Could not create Shipment");
		if (counter)
			from.setRef_InOut_ID(to.getM_InOut_ID());

		if (to.copyLinesFrom(from, counter, setOrder) == 0)
			throw new IllegalStateException("Could not create Shipment Lines");

		return to;
	}	//	copyFrom


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOut_ID
	 *	@param trxName rx name
	 */
	public MInOut (Properties ctx, int M_InOut_ID, String trxName)
	{
		super (ctx, M_InOut_ID, trxName);
		if (M_InOut_ID == 0)
		{
		//	setDocumentNo (null);
		//	setC_BPartner_ID (0);
		//	setC_BPartner_Location_ID (0);
		//	setM_Warehouse_ID (0);
		//	setC_DocType_ID (0);
			setIsSOTrx ((Env.getContext(ctx, "IsSOTrx").equals("Y") ? true : false));
			setMovementDate (new Timestamp (System.currentTimeMillis ()));
			setDateAcct (getMovementDate());
		//	setMovementType (MOVEMENTTYPE_CustomerShipment);
			setDeliveryRule (DELIVERYRULE_Availability);
			setDeliveryViaRule (DELIVERYVIARULE_Pickup);
			setFreightCostRule (FREIGHTCOSTRULE_FreightIncluded);
			setDocStatus (DOCSTATUS_Drafted);
			setDocAction (DOCACTION_Complete);
			setPriorityRule (PRIORITYRULE_Medium);
			setNoPackages(0);
			setIsInTransit(false);
			setIsPrinted (false);
			setSendEMail (false);
			setIsInDispute(false);
			//
			setIsApproved(false);
			super.setProcessed (false);
			setProcessing(false);
			setPosted(false);
		}
	}	//	MInOut

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MInOut (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInOut

	/**
	 * 	Order Constructor - create header only
	 *	@param order order
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MOrder order, int C_DocTypeShipment_ID, Timestamp movementDate)
	{
		this (order.getCtx(), 0, order.get_TrxName());
		setClientOrg(order);
		setC_BPartner_ID (order.getC_BPartner_ID());
		setC_BPartner_Location_ID (order.getC_BPartner_Location_ID());	//	shipment address
		setBill_Location_ID (order.getBill_Location_ID());	//	invoice address

		//--- AGREGADO POR DANIEL GINI -> REQ-053 y REQ-054
		setLocation_Bill_ID(order.getLocation_Bill_ID());
		setC_Location_ID(order.getC_Location_ID());
		setVALORDECLARADO(order.getTotalLines());
		//---

		setAD_User_ID(order.getAD_User_ID());
		//
		setM_Warehouse_ID (order.getM_Warehouse_ID());
		setIsSOTrx (order.isSOTrx());
		setMovementType (order.isSOTrx() ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReceipts);
		if (C_DocTypeShipment_ID == 0)
			C_DocTypeShipment_ID = DB.getSQLValue(null,
				"SELECT C_DocTypeShipment_ID FROM C_DocType WHERE C_DocType_ID=?",
				order.getC_DocType_ID());
		setC_DocType_ID (C_DocTypeShipment_ID);

		//	Default - Today
		if (movementDate != null)
			setMovementDate (movementDate);
		setDateAcct (getMovementDate());

		//	Copy from Order
		setC_Order_ID(order.getC_Order_ID());
		setDeliveryRule (order.getDeliveryRule());
		setDeliveryViaRule (order.getDeliveryViaRule());
		setM_Shipper_ID(order.getM_Shipper_ID());
		setFreightCostRule (order.getFreightCostRule());
		setFreightAmt(order.getFreightAmt());
		setSalesRep_ID(order.getSalesRep_ID());
		//
		setC_Activity_ID(order.getC_Activity_ID());
		setC_Campaign_ID(order.getC_Campaign_ID());
		setC_Charge_ID(order.getC_Charge_ID());
		setChargeAmt(order.getChargeAmt());
		//
		setC_Project_ID(order.getC_Project_ID());
		setDateOrdered(order.getDateOrdered());
		setDescription(order.getDescription());
		setPOReference(order.getPOReference());
		setSalesRep_ID(order.getSalesRep_ID());
		setAD_OrgTrx_ID(order.getAD_OrgTrx_ID());
		setUser1_ID(order.getUser1_ID());
		setUser2_ID(order.getUser2_ID());
	}	//	MInOut

	/**
	 * 	Invoice Constructor - create header only
	 *	@param invoice invoice
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MInvoice invoice, int C_DocTypeShipment_ID, Timestamp movementDate, int M_Warehouse_ID)
	{
		this (invoice.getCtx(), 0, invoice.get_TrxName());
		setClientOrg(invoice);
		setC_BPartner_ID (invoice.getC_BPartner_ID());
		setC_BPartner_Location_ID (invoice.getC_BPartner_Location_ID());	//	shipment address
		setAD_User_ID(invoice.getAD_User_ID());
		//
		setM_Warehouse_ID (M_Warehouse_ID);
		setIsSOTrx (invoice.isSOTrx());
		setMovementType (invoice.isSOTrx() ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReceipts);
		MOrder order = null;
		if (invoice.getC_Order_ID() != 0)
			order = new MOrder (invoice.getCtx(), invoice.getC_Order_ID(), invoice.get_TrxName());
		if (C_DocTypeShipment_ID == 0 && order != null)
			C_DocTypeShipment_ID = DB.getSQLValue(null,
				"SELECT C_DocTypeShipment_ID FROM C_DocType WHERE C_DocType_ID=?",
				order.getC_DocType_ID());
		if (C_DocTypeShipment_ID != 0)
			setC_DocType_ID (C_DocTypeShipment_ID);
		else
			setC_DocType_ID();

		//	Default - Today
		if (movementDate != null)
			setMovementDate (movementDate);
		setDateAcct (getMovementDate());

		//	Copy from Invoice
		setC_Order_ID(invoice.getC_Order_ID());
		setSalesRep_ID(invoice.getSalesRep_ID());
		//
		setC_Activity_ID(invoice.getC_Activity_ID());
		setC_Campaign_ID(invoice.getC_Campaign_ID());
		setC_Charge_ID(invoice.getC_Charge_ID());
		setChargeAmt(invoice.getChargeAmt());
		//
		setC_Project_ID(invoice.getC_Project_ID());
		setDateOrdered(invoice.getDateOrdered());
		setDescription(invoice.getDescription());
		setPOReference(invoice.getPOReference());
		setAD_OrgTrx_ID(invoice.getAD_OrgTrx_ID());
		setUser1_ID(invoice.getUser1_ID());
		setUser2_ID(invoice.getUser2_ID());

		if (order != null)
		{
			setDeliveryRule (order.getDeliveryRule());
			setDeliveryViaRule (order.getDeliveryViaRule());
			setM_Shipper_ID(order.getM_Shipper_ID());
			setFreightCostRule (order.getFreightCostRule());
			setFreightAmt(order.getFreightAmt());
		}
	}	//	MInOut


	/**
	 * 	Vit4B - 10/4/2007 Creado para que tome como parametro el Nro de documento con formato 0000-00000000
	 * 	Invoice Constructor - create header only
	 *	@param invoice invoice
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MInvoice invoice, int C_DocTypeShipment_ID, Timestamp movementDate, int M_Warehouse_ID, String DocumentNo)
	{
		this (invoice.getCtx(), 0, invoice.get_TrxName());
                // Agregada para setear el DocumentNo

                super.setDocumentNo(DocumentNo);

                setClientOrg(invoice);
		setC_BPartner_ID (invoice.getC_BPartner_ID());
		setC_BPartner_Location_ID (invoice.getC_BPartner_Location_ID());	//	shipment address
		setAD_User_ID(invoice.getAD_User_ID());
		//
		setM_Warehouse_ID (M_Warehouse_ID);
		setIsSOTrx (invoice.isSOTrx());
		setMovementType (invoice.isSOTrx() ? MOVEMENTTYPE_CustomerShipment : MOVEMENTTYPE_VendorReceipts);
		MOrder order = null;
		if (invoice.getC_Order_ID() != 0)
			order = new MOrder (invoice.getCtx(), invoice.getC_Order_ID(), invoice.get_TrxName());
		if (C_DocTypeShipment_ID == 0 && order != null)
			C_DocTypeShipment_ID = DB.getSQLValue(null,
				"SELECT C_DocTypeShipment_ID FROM C_DocType WHERE C_DocType_ID=?",
				order.getC_DocType_ID());
		if (C_DocTypeShipment_ID != 0)
			setC_DocType_ID (C_DocTypeShipment_ID);
		else
			setC_DocType_ID();

		//	Default - Today
		if (movementDate != null)
			setMovementDate (movementDate);
		setDateAcct (getMovementDate());

		//	Copy from Invoice
		setC_Order_ID(invoice.getC_Order_ID());
		setSalesRep_ID(invoice.getSalesRep_ID());
		//
		setC_Activity_ID(invoice.getC_Activity_ID());
		setC_Campaign_ID(invoice.getC_Campaign_ID());
		setC_Charge_ID(invoice.getC_Charge_ID());
		setChargeAmt(invoice.getChargeAmt());
		//
		setC_Project_ID(invoice.getC_Project_ID());
		setDateOrdered(invoice.getDateOrdered());
		setDescription(invoice.getDescription());
		setPOReference(invoice.getPOReference());
		setAD_OrgTrx_ID(invoice.getAD_OrgTrx_ID());
		setUser1_ID(invoice.getUser1_ID());
		setUser2_ID(invoice.getUser2_ID());

		if (order != null)
		{
			setDeliveryRule (order.getDeliveryRule());
			setDeliveryViaRule (order.getDeliveryViaRule());
			setM_Shipper_ID(order.getM_Shipper_ID());
			setFreightCostRule (order.getFreightCostRule());
			setFreightAmt(order.getFreightAmt());
		}
	}	//	MInOut



	/**
	 * 	Copy Constructor - create header only
	 *	@param original original
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MInOut original, int C_DocTypeShipment_ID, Timestamp movementDate)
	{
		this (original.getCtx(), 0, original.get_TrxName());
		setClientOrg(original);
		setC_BPartner_ID (original.getC_BPartner_ID());
		setC_BPartner_Location_ID (original.getC_BPartner_Location_ID());	//	shipment address
		setAD_User_ID(original.getAD_User_ID());
		//
		setM_Warehouse_ID (original.getM_Warehouse_ID());
		setIsSOTrx (original.isSOTrx());
		setMovementType (original.getMovementType());
		if (C_DocTypeShipment_ID == 0)
			setC_DocType_ID(original.getC_DocType_ID());
		else
			setC_DocType_ID (C_DocTypeShipment_ID);

		//	Default - Today
		if (movementDate != null)
			setMovementDate (movementDate);
		setDateAcct (getMovementDate());

		//	Copy from Order
		setC_Order_ID(original.getC_Order_ID());
		setDeliveryRule (original.getDeliveryRule());
		setDeliveryViaRule (original.getDeliveryViaRule());
		setM_Shipper_ID(original.getM_Shipper_ID());
		setFreightCostRule (original.getFreightCostRule());
		setFreightAmt(original.getFreightAmt());
		setSalesRep_ID(original.getSalesRep_ID());
		//
		setC_Activity_ID(original.getC_Activity_ID());
		setC_Campaign_ID(original.getC_Campaign_ID());
		setC_Charge_ID(original.getC_Charge_ID());
		setChargeAmt(original.getChargeAmt());
		//
		setC_Project_ID(original.getC_Project_ID());
		setDateOrdered(original.getDateOrdered());
		setDescription(original.getDescription());
		setPOReference(original.getPOReference());
		setSalesRep_ID(original.getSalesRep_ID());
		setAD_OrgTrx_ID(original.getAD_OrgTrx_ID());
		setUser1_ID(original.getUser1_ID());
		setUser2_ID(original.getUser2_ID());
	}	//	MInOut


	/**	Lines					*/
	private MInOutLine[]	m_lines = null;
	/** Confirmations			*/
	private MInOutConfirm[]	m_confirms = null;
	/** BPartner				*/
	private MBPartner		m_partner = null;


	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 *	String representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInOut[")
			.append (get_ID()).append("-").append(getDocumentNo())
			.append(",DocStatus=").append(getDocStatus())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.SHIPMENT, getC_Invoice_ID());
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Get Lines of Shipment
	 * 	@return lines
	 */
	public MInOutLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery)
			return m_lines;
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		String sql = "SELECT * FROM M_InOutLine WHERE M_InOut_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getM_InOut_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MInOutLine(getCtx(), rs, get_TrxName()));
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
			list = null;
		//	throw new DBException(ex);
		}
		finally
		{
			try
			{
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			}
			catch (SQLException e)
			{
			}
		}
		pstmt = null;
		rs = null;
		//
		if (list == null)
			return null;
		//
		m_lines = new MInOutLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}	//	getMInOutLines

	/**
	 * 	Get Lines of Shipment
	 * 	@return lines
	 */
	public MInOutLine[] getLines()
	{
		return getLines(false);
	}	//	getLines


	/**
	 * 	Get Confirmations
	 * 	@param requery requery
	 *	@return array of Confirmations
	 */
	public MInOutConfirm[] getConfirmations(boolean requery)
	{
		if (m_confirms != null && !requery)
			return m_confirms;

		ArrayList<MInOutConfirm> list = new ArrayList<MInOutConfirm> ();
		String sql = "SELECT * FROM M_InOutConfirm WHERE M_InOut_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_InOut_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MInOutConfirm(getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}

		m_confirms = new MInOutConfirm[list.size ()];
		list.toArray (m_confirms);
		return m_confirms;
	}	//	getConfirmations


	/**
	 * 	Copy Lines From other Shipment
	 *	@param otherShipment shipment
	 *	@param counter set counter info
	 *	@param setOrder set order link
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MInOut otherShipment, boolean counter, boolean setOrder)
	{
		if (isProcessed() || isPosted() || otherShipment == null)
			return 0;
		MInOutLine[] fromLines = otherShipment.getLines(false);
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MInOutLine line = new MInOutLine (this);
			MInOutLine fromLine = fromLines[i];
			line.set_TrxName(get_TrxName());
			if (counter)	//	header
				PO.copyValues(fromLine, line, getAD_Client_ID(), getAD_Org_ID());
			else
				PO.copyValues(fromLine, line, fromLine.getAD_Client_ID(), fromLine.getAD_Org_ID());
			//begin e-evolution vpj-cd bug-fix  MInOut.java,v 1.80 2006/01/28 01:28:28 jjanke Exp $
			//line.setM_InOutLine_ID(0);	//	new
			line.setM_InOut_ID(getM_InOut_ID());
			//end  e-evolution vpj-cd bug-fix
			//	Reset
			if (!setOrder)
				line.setC_OrderLine_ID(0);
			if (!counter)
				line.setM_AttributeSetInstance_ID(0);
		//	line.setS_ResourceAssignment_ID(0);
			line.setRef_InOutLine_ID(0);
			line.setIsInvoiced(false);
			//
			line.setConfirmedQty(Env.ZERO);
			line.setPickedQty(Env.ZERO);
			line.setScrappedQty(Env.ZERO);
			line.setTargetQty(Env.ZERO);
			//	Set Locator based on header Warehouse
			if (getM_Warehouse_ID() != otherShipment.getM_Warehouse_ID())
			{
				line.setM_Locator_ID(0);
				line.setM_Locator_ID(Env.ZERO);
			}
			//
			if (counter)
			{
				line.setRef_InOutLine_ID(fromLine.getM_InOutLine_ID());
				if (fromLine.getC_OrderLine_ID() != 0)
				{
					MOrderLine peer = new MOrderLine (getCtx(), fromLine.getC_OrderLine_ID(), get_TrxName());
					if (peer.getRef_OrderLine_ID() != 0)
						line.setC_OrderLine_ID(peer.getRef_OrderLine_ID());
				}
			}
			//
			line.setProcessed(false);
			if (line.save(get_TrxName()))
				count++;
			//	Cross Link
			if (counter)
			{
				fromLine.setRef_InOutLine_ID(line.getM_InOutLine_ID());
				fromLine.save(get_TrxName());
			}
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

	/** Reversal Flag		*/
	private boolean m_reversal = false;

	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	private void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	private boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String sql = "UPDATE M_InOutLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE M_InOut_ID=" + getM_InOut_ID();
		int noLine = DB.executeUpdate(sql, get_TrxName());
		m_lines = null;
		log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed

	/**
	 * 	Get BPartner
	 *	@return partner
	 */
	public MBPartner getBPartner()
	{
		if (m_partner == null)
			m_partner = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
		return m_partner;
	}	//	getPartner

	/**
	 * 	Set Document Type
	 * 	@param DocBaseType doc type MDocType.DOCBASETYPE_
	 */
	public void setC_DocType_ID (String DocBaseType)
	{
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND DocBaseType=?"
			+ " AND IsSOTrx='" + (isSOTrx() ? "Y" : "N") + "' "
			+ "ORDER BY IsDefault DESC";
		int C_DocType_ID = DB.getSQLValue(null, sql, getAD_Client_ID(), DocBaseType);
		if (C_DocType_ID <= 0)
			log.log(Level.SEVERE, "Not found for AC_Client_ID="
				+ getAD_Client_ID() + " - " + DocBaseType);
		else
		{
			log.fine("DocBaseType=" + DocBaseType + " - C_DocType_ID=" + C_DocType_ID);
			setC_DocType_ID (C_DocType_ID);
			boolean isSOTrx = MDocType.DOCBASETYPE_MaterialDelivery.equals(DocBaseType);
			setIsSOTrx (isSOTrx);
		}
	}	//	setC_DocType_ID

	/**
	 * 	Set Default C_DocType_ID.
	 * 	Based on SO flag
	 */
	public void setC_DocType_ID()
	{
		if (isSOTrx())
			setC_DocType_ID(MDocType.DOCBASETYPE_MaterialDelivery);
		else
			setC_DocType_ID(MDocType.DOCBASETYPE_MaterialReceipt);
	}	//	setC_DocType_ID

	/**
	 * 	Set Business Partner Defaults & Details
	 * 	@param bp business partner
	 */
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());

		//	Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (int i = 0; i < locs.length; i++)
			{
				if (locs[i].isShipTo())
					setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
			}
			//	set to first if not set
			if (getC_BPartner_Location_ID() == 0 && locs.length > 0)
				setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
			log.log(Level.SEVERE, "Has no To Address: " + bp);

		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if (contacts != null && contacts.length > 0)	//	get first User
			setAD_User_ID(contacts[0].getAD_User_ID());
	}	//	setBPartner

	/**
	 * 	Create the missing next Confirmation
	 */
	public void createConfirmation()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		boolean pick = dt.isPickQAConfirm();
		boolean ship = dt.isShipConfirm();
		//	Nothing to do
		if (!pick && !ship)
		{
			log.fine("No need");
			return;
		}

		//	Create Both .. after each other
		if (pick && ship)
		{
			boolean havePick = false;
			boolean haveShip = false;
			MInOutConfirm[] confirmations = getConfirmations(false);
			for (int i = 0; i < confirmations.length; i++)
			{
				MInOutConfirm confirm = confirmations[i];
				if (MInOutConfirm.CONFIRMTYPE_PickQAConfirm.equals(confirm.getConfirmType()))
				{
					if (!confirm.isProcessed())		//	wait intil done
					{
						log.fine("Unprocessed: " + confirm);
						return;
					}
					havePick = true;
				}
				else if (MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm.equals(confirm.getConfirmType()))
					haveShip = true;
			}
			//	Create Pick
			if (!havePick)
			{
				MInOutConfirm.create (this, MInOutConfirm.CONFIRMTYPE_PickQAConfirm, false);
				return;
			}
			//	Create Ship
			if (!haveShip)
			{
				MInOutConfirm.create (this, MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm, false);
				return;
			}
			return;
		}
		//	Create just one
		if (pick)
			MInOutConfirm.create (this, MInOutConfirm.CONFIRMTYPE_PickQAConfirm, true);
		else if (ship)
			MInOutConfirm.create (this, MInOutConfirm.CONFIRMTYPE_ShipReceiptConfirm, true);
	}	//	createConfirmation


	/**
	 * 	Set Warehouse and check/set Organization
	 *	@param M_Warehouse_ID id
	 */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID == 0)
		{
			log.severe("Ignored - Cannot set AD_Warehouse_ID to 0");
			return;
		}
		super.setM_Warehouse_ID (M_Warehouse_ID);
		//
		MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
		if (wh.getAD_Org_ID() != getAD_Org_ID())
		{
			log.warning("M_Warehouse_ID=" + M_Warehouse_ID
				+ ", Overwritten AD_Org_ID=" + getAD_Org_ID() + "->" + wh.getAD_Org_ID());
			setAD_Org_ID(wh.getAD_Org_ID());
		}
	}	//	setM_Warehouse_ID


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Warehouse Org


		if (newRecord)
		{
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (getAD_Org_ID()!=0)
			{
				if (wh.getAD_Org_ID() != getAD_Org_ID())
				{
					log.saveError("WarehouseOrgConflict", "");
					return false;
				}
			}
		}

		//	Shipment - Needs Order
		if (isSOTrx())
		{

			/**
			 *  	AGREGADO PARA VERIFICAR FORMATO DE DOCUMENTNO
			 *
			 * 		13/03/2009
			 *
            */
			String documentNo = this.getDocumentNo();

			//   MDocType doc = MDocType.get(getCtx(),getC_DocType_ID());

        	if(documentNo != null && !documentNo.equals(""))
            {
                /*
                 *      Si el numero es definido por el sistema automaticamente le asigna <>
                 *      en este caso se debe reformatear para anexarle los ceros y aumentar
                 *      el numerador a mano ya que el sistema pierde la referencia automatica.
                 */

                if(documentNo.indexOf("<") != -1)
                {
                    documentNo = documentNo.substring(1,documentNo.length()-1);
                    MDocType docType = MDocType.get(getCtx(), this.getC_DocType_ID());
                    MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                    int next = seq.getCurrentNext();
                    seq.setCurrentNext(next + 1);
                    seq.save(get_TrxName());

                }

                int indexOf = documentNo.indexOf("-");

                String prefijo = "";
                String nro = "";

                if(indexOf == -1)
                {
                    MDocType docType = MDocType.get(getCtx(), this.getC_DocType_ID());
                    MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                    prefijo = seq.getPrefix();
                    nro = documentNo;

                }
                else
                {
                    prefijo = this.getDocumentNo().substring(0,indexOf);
                    nro = this.getDocumentNo().substring(indexOf+1,this.getDocumentNo().length());
                }

                if(nro == "" || nro == null)
                {
                    JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if(prefijo == "" || prefijo == null)
                {
                    JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }



                switch (prefijo.length()) {

                  case 1:
                        prefijo = "000" + prefijo;
                        break;
                  case 2:
                        prefijo = "00" + prefijo;
                        break;
                  case 3:
                        prefijo = "0" + prefijo;
                        break;
                  case 4:
                        break;

                  default:
                        JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;

                }



                switch (nro.length()) {
                  case 1:
                        nro = "0000000" + nro;
                        break;
                  case 2:
                        nro = "000000" + nro;
                        break;
                  case 3:
                        nro = "00000" + nro;
                        break;
                  case 4:
                        nro = "0000" + nro;
                        break;
                  case 5:
                        nro = "000" + nro;
                        break;
                  case 6:
                        nro = "00" + nro;
                        break;
                  case 7:
                        nro = "0" + nro;
                        break;
                  case 8:
                        break;
                  default:
                        JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                }

                String val = prefijo + "-" + nro;

                if (ValueFormat.validFormat(val,"0000-00000000"))
                {
                	try
    				{	String query = "select M_InOut_ID, C_DocType_ID from M_InOut where DocumentNo = ?";

    					PreparedStatement pstmt = DB.prepareStatement(query, null);
    					pstmt.setString(1, val);
    					ResultSet rs = pstmt.executeQuery();

    					if (rs.next() && (getM_InOut_ID() != rs.getInt(1)) && (getC_DocType_ID() == rs.getInt(2)))
    					{
    						JOptionPane.showMessageDialog(null,"El Nro de Documento ingresado ya existe para este tipo de documento.","Error - Nro. Documento duplicado", JOptionPane.ERROR_MESSAGE);
    						return false;
    					}

    					rs.close();
    					pstmt.close();
    				}
    				catch (Exception n){}

                	this.setDocumentNo(val);
                }
                else
                {
                	JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            }
            else
            {
            	JOptionPane.showMessageDialog(null,"No ingreso Numero de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;


		if (is_ValueChanged("AD_Org_ID"))
		{
			String sql = "UPDATE M_InOutLine ol"
				+ " SET AD_Org_ID ="
					+ "(SELECT AD_Org_ID"
					+ " FROM M_InOut o WHERE ol.M_InOut_ID=o.M_InOut_ID) "
				+ "WHERE M_InOut_ID=" + getC_Order_ID();
			int no = DB.executeUpdate(sql, get_TrxName());
			log.fine("Lines -> #" + no);
		}
		return true;
	}	//	afterSave


	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	process

	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		 /**
                 * BISion - 18/12/2008 - Santiago Ibañez
                 * Modificacion realizada para obligar a que tenga cada una de
                 * las lineas de la recepcion tenga asignada una orden de
                 * compra. Las recepciones solo se realizan debido a una orden
                 * de compra.
                 */
                 /*if (!verificarOrdenCompraAsignada()){
                    this.m_processMsg = "Hay al menos una linea que no se " +
                                        " le asigno una orden de compra";
                    return DocAction.STATUS_Invalid;
                 }*/
                log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}

		//	Credit Check
		if (isSOTrx() && !isReversal())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
			if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
			if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
			BigDecimal notInvoicedAmt = MBPartner.getNotInvoicedAmt(getC_BPartner_ID());
			if (MBPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus(notInvoicedAmt)))
			{
				m_processMsg = "@BPartnerOverSCreditHold@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance() + ", @NotInvoicedAmt@=" + notInvoicedAmt
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
		}

		//	Lines
		MInOutLine[] lines = getLines(true);
		if (lines == null || lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		/**
         * Modificacion realizada para que la partida la chequee solo
         * si el tipo de documento NO es Remito Famatina, Loma Hermosa o Florida
         */
        //	Mandatory Attributes
        MDocType docType = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        if (!docType.getName().equals("Remito Loma Hermosa")&&
            !docType.getName().equals("Remito Florida")&&
            !docType.getName().equals("Remito Famatina")){
                for (int i = 0; i < lines.length; i++)
                {
                    if (lines[i].getM_AttributeSetInstance_ID() != 0)
                        continue;
                    MProduct product = lines[i].getProduct();
                    if (product != null)
                    {
                        int M_AttributeSet_ID = product.getM_AttributeSet_ID();
                        if (M_AttributeSet_ID != 0)
                        {
                            MAttributeSet mas = MAttributeSet.get(getCtx(), M_AttributeSet_ID);
                            if (mas != null
                                && ((isSOTrx() && mas.isMandatory())
                                    || (!isSOTrx() && mas.isMandatoryAlways())) )
                            {
                                m_processMsg = "@M_AttributeSet_ID@ @IsMandatory@";
                                return DocAction.STATUS_Invalid;
                            }
                        }
                    }
                }
                //fin de chequeo de partidas

                if (!isReversal())	//	don't change reversal
                {
                    checkMaterialPolicy();	//	set MASI
                    createConfirmation();
                }
        }
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	/** BISion - 18/12/2008 - Santiago Iba�ez
         * M�todo realizado para verificar si cada una de las l�neas de la
         * recepci�n tiene asignada una orden de compra. Las recepciones solo se
         * realizan debido a una orden de compra.
         */
        private boolean verificarOrdenCompraAsignada(){
            //Obtengo cada una de las lineas
            MInOutLine[] lines = this.getLines();
            for (int i=0;i<lines.length;i++){
              MInOutLine line = lines[i];
              if (line.getC_OrderLine_ID()==0)
                  return false;
            }
            return true;
        }

        /**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */

        public String completeIt(){
            //	Re-Check
            if (!m_justPrepared){
                String status = prepareIt();
                if (!DocAction.STATUS_InProgress.equals(status))
                        return status;
            }

            //	Outstanding (not processed) Incoming Confirmations ?
            MInOutConfirm[] confirmations = getConfirmations(true);
            for (int i = 0; i < confirmations.length; i++){
                MInOutConfirm confirm = confirmations[i];
                if (!confirm.isProcessed()){
                    if (MInOutConfirm.CONFIRMTYPE_CustomerConfirmation.equals(confirm.getConfirmType()))
                            continue;
                    //
                    m_processMsg = "Open @M_InOutConfirm_ID@: " +
                            confirm.getConfirmTypeName() + " - " + confirm.getDocumentNo();
                    return DocAction.STATUS_InProgress;
                }
            }


            //	Implicit Approval
            if (!isApproved())
                    approveIt();
            log.info(toString());
            StringBuffer info = new StringBuffer();

            //BISion - 04/02/2009 - Santiago Ibañez
            //Chequeo (solo si es un recibo de materiales) que no supere el umbral predeterminado
            //if (getMovementType().equals(MOVEMENTTYPE_VendorReceipts)){

            //Si el tipo de documento es Recepcion de Materiales de terceros chequeo umbral
            
            MDocType docType = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
            if (docType.getDocBaseType().equals(MDocType.DOCBASETYPE_RecepcionMaterialesTerceros)){
                int supera = superaUmbral();
                if (supera!=0){
                    System.out.println("Chequeando umbral de recepcion de materiales...");
                    m_processMsg = "La linea "+supera+" supera el umbral predeterminado";
                    JOptionPane.showMessageDialog(null,"La linea " +supera+" supera el umbral predeterminado.","Error - Umbral de recepcion superado",JOptionPane.ERROR_MESSAGE);
                    return DocAction.STATUS_Invalid;
                }
            }
       
            
            /**
            * BISion - 22/06/2010 - Santiago Ibañez
            * Si el tipo de documento es Remito Famatina, Remito Loma Hermosa o Remito Florida
            * entonces no debe decrementar stock
            */
            
            if (!docType.getName().equals("Remito Loma Hermosa")&&
                !docType.getName().equals("Remito Florida")&&
                !docType.getName().equals("Remito Famatina")){

                // For all lines
		MInOutLine[] lines = getLines(false);
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++){
                    
                    MInOutLine sLine = lines[lineIndex];
                    MProduct product = sLine.getProduct();
                    
                    // Validación de cantidad negativa en la devolución
                    if (getC_DocType_ID() == 5000020 && (sLine.getQtyEntered().compareTo(Env.ZERO) >= 0))
                    {
                        JOptionPane.showMessageDialog(null, "La cantidad en una devolución debe ser negativa.", "Salvando", JOptionPane.INFORMATION_MESSAGE);
                        return DocAction.STATUS_Invalid;
                    }                       

                    //	Qty & Type
                    String MovementType = getMovementType();
                    //BISion - 29/10/2009 - Santiago Ibañez
                    //COM-PAN-BUG-05.001.01
                    BigDecimal Qty = sLine.getQtyEntered();
                    if (MovementType.charAt(1) == '-')	//	C- Customer Shipment - V- Vendor Return
                            Qty = Qty.negate();
                    BigDecimal QtySO = Env.ZERO;
                    BigDecimal QtyPO = Env.ZERO;

                    //	Update Order Line
                    MOrderLine oLine = null;

                    if (sLine.getC_OrderLine_ID() != 0){
                        
                        MOrder order = new MOrder(getCtx(),getC_Order_ID(),null);
                        if (!order.isSOTrx())
                                //	Para que tome la OC sin los cambios hechos por esta transaccion se pone null
                                oLine = new MOrderLine (getCtx(), sLine.getC_OrderLine_ID(), null);
                        else
                                oLine = new MOrderLine (getCtx(), sLine.getC_OrderLine_ID(), get_TrxName());

                        log.fine("OrderLine - Reserved=" + oLine.getQtyReserved()
                                + ", Delivered=" + oLine.getQtyDelivered());
                        if (isSOTrx())
                            //QtySO = sLine.getMovementQty();
                            //BISion - 29/10/2009 - Santiago Ibañez
                            //COM-PAN-BUG-05.001.01
                            QtySO = sLine.getQtyEntered();
                        else
                            //QtyPO = sLine.getMovementQty();
                            //BISion - 29/10/2009 - Santiago Ibañez
                            //COM-PAN-BUG-05.001.01
                            QtyPO = sLine.getQtyEntered();
                        /**
                        * BISion - 10/03/2009 - Santiago Ibañez
                        * Modificacion realizada para actualizar correctamente los ordenados
                        */
                        // situaciones
                        //Si esta recepcion + entregado supera la cantidad
                        //      solamente actualizo ordenado con remanente
                        if (oLine.getQtyDelivered().add(sLine.getQtyEntered()).compareTo(oLine.getQtyEntered())>=0){
                            QtyPO = oLine.getQtyEntered().subtract(oLine.getQtyDelivered());
                            //Si entra por aca es que lo entregado ya era mayor a la cantidad ingresada en la orden de compra
                            if (QtyPO.signum()==-1)
                                QtyPO = BigDecimal.ZERO;
                        }else
                            //Si esta recepcion + entregado no supere la cantidad actualizo ordenado con la cantidad del movimiento
                            QtyPO = sLine.getQtyEntered();
                    }

                    log.info("Line=" + sLine.getLine() + " - Qty=" + sLine.getQtyEntered());

                    // Stock Movement - Counterpart MOrder.reserveStock
                    // Agregado por Ticket 27 - 04/05/2010 - && getMPC_Order_ID()==0
                    if (product != null && product.isStocked() && getMPC_Order_ID()==0){
                            log.fine("Material Transaction");
                            MTransaction mtrx = null;
                            //	Reservation ASI - assume none
                            int reservationAttributeSetInstance_ID = 0; // sLine.getM_AttributeSetInstance_ID();
                            if (oLine != null)
                                reservationAttributeSetInstance_ID = oLine.getM_AttributeSetInstance_ID();
                            //
                            if (sLine.getM_AttributeSetInstance_ID() == 0){
                                MInOutLineMA mas[] = MInOutLineMA.get(getCtx(),
                                sLine.getM_InOutLine_ID(), get_TrxName());
                                for (int j = 0; j < mas.length; j++){
                                    MInOutLineMA ma = mas[j];
                                    BigDecimal QtyMA = ma.getMovementQty();
                                    if (MovementType.charAt(1) == '-')	//	C- Customer Shipment - V- Vendor Return
                                        QtyMA = QtyMA.negate();
                                    BigDecimal QtySOMA = Env.ZERO;
                                    BigDecimal QtyPOMA = Env.ZERO;
                                    if (sLine.getC_OrderLine_ID() != 0){
                                        if (isSOTrx())
                                            QtySOMA = ma.getMovementQty();
                                        else
                                            QtyPOMA = ma.getMovementQty();
                                    }
                                    /**
                                    * BISion - 10/03/2009 - Santiago Ibañez
                                    * Modificacion realizada para actualizar correctamente los ordenados
                                    */
                                    // situaciones
                                    //Si esta recepcion + entregado supera la cantidad
                                    //      solamente actualizo ordenado con remanente
                                    if (oLine != null){
                                        if (oLine.getQtyDelivered().add(sLine.getQtyEntered()).compareTo(oLine.getQtyEntered())>=0)
                                            QtyPOMA = oLine.getQtyEntered().subtract(oLine.getQtyDelivered());
                                        else
                                            QtyPOMA = sLine.getQtyEntered();
                                    }
                                    //fin modificacion BISion
                                    
                                    //	Update Storage - see also VMatch.createMatchRecord
                                    if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
                                        sLine.getM_Locator_ID(),
                                        sLine.getM_Product_ID(),
                                        ma.getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID,
                                        QtyMA, QtySOMA.negate(), QtyPOMA.negate(), get_TrxName())){

                                            m_processMsg = "Cannot correct Inventory (MA)";
                                            return DocAction.STATUS_Invalid;
                                    }
                                    //	Create Transaction
                                    mtrx = new MTransaction (getCtx(),MovementType, sLine.getM_Locator_ID(),
                                            sLine.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
                                            QtyMA, getMovementDate(), get_TrxName()); mtrx.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
                                    //14-06-2011 Camarzana Mariano Se le agrego el nombre de la transaccion
                                    if (!mtrx.save(get_TrxName())){
                                            m_processMsg = "Could not create Material Transaction (MA)";
                                            return DocAction.STATUS_Invalid;
                                    }
                                }
                            }

                            //	sLine.getM_AttributeSetInstance_ID() != 0
                            if (mtrx == null){
                                /** BISion - 11/03/2009 - Santiago Ibañez
                                * Modificacion para considerar las devoluciones
                                * Devolucion: QtyPO es negativo
                                * Recepcion: QtyPO es positivo
                                */
                                //Se trata de una devolucion
                                if (sLine.getQtyEntered().signum()==-1 && !isSOTrx()){
                                    // Chequear que lo que se devuelve este dentro del ordenado
                                    // para actualizar correctamente el Storage
                                    // si devuelvo mas de lo que entregue el ordenado a incrementar es lo que entregue
                                    if (QtyPO.negate().compareTo(oLine.getQtyDelivered())>=0){
                                        QtyPO = oLine.getQtyDelivered();
                                        //si ya hay mas entregado que lo que se pidio
                                        if (oLine.getQtyDelivered().compareTo(oLine.getQtyEntered())>0)
                                            QtyPO = oLine.getQtyEntered();
                                        QtyPO = QtyPO.negate();
                                    }
                                    //si lo que entregue menos lo que devuelvo sigue siendo mayor a la cantidad
                                    else if (oLine.getQtyEntered().compareTo(oLine.getQtyDelivered().subtract(QtyPO.negate()))<0){
                                        //ordenado a incrementar = Cantidad - (Cantidad Entregada - Devolucion)
                                        QtyPO = Env.ZERO;
                                    }
                                    //si la cantidad entregada es mayor a la cantidad
                                    else if (oLine.getQtyDelivered().compareTo(oLine.getQtyEntered())>0){
                                        QtyPO = oLine.getQtyEntered().subtract(oLine.getQtyDelivered().subtract(QtyPO.negate()));
                                        QtyPO = QtyPO.negate();
                                    }
                                }
                                //fin modificacion BISion

                                //Si es una Anulacion de Remito
                                MDocType dtype = MDocType.get(getCtx(), getC_DocType_ID());

                                //if (dtype.getDocBaseType().equals(MDocType.DOCBASETYPE_RemitoRevertido))
                                /*
                                * 07-01-2011 Camarzana Mariano
                                * Comentado porque al anular el remito, ya trae la cantidad positiva
                                * para incrementar el stock (Ticket 133)
                                */
                                //Qty = Qty.negate();

                                //	Fallback: Update Storage - see also VMatch.createMatchRecord
                                if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
                                    sLine.getM_Locator_ID(),
                                    sLine.getM_Product_ID(),
                                    sLine.getM_AttributeSetInstance_ID(), reservationAttributeSetInstance_ID,
                                    Qty, QtySO.negate(), QtyPO.negate(), get_TrxName())){
                                        m_processMsg = "Cannot correct Inventory";
                                        return DocAction.STATUS_Invalid;
                                }
                                //	FallBack: Create Transaction
                                mtrx = new MTransaction (getCtx(),
                                        MovementType, sLine.getM_Locator_ID(),
                                        sLine.getM_Product_ID(), sLine.getM_AttributeSetInstance_ID(),
                                        Qty, getMovementDate(), get_TrxName());
                                mtrx.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
                                //14-06-2011 2011 Camarzana Mariano Se le agrego el nombre de la transaccion
                                if (!mtrx.save(get_TrxName())){
                                        m_processMsg = "Could not create Material Transaction";
                                        return DocAction.STATUS_Invalid;
                                }
                            }
                    }	//	stock movement

                    //	Correct Order Line
                    //if (product != null && oLine != null)		//	other in VMatch.createMatchRecord
                            //oLine.setQtyReserved(oLine.getQtyReserved().subtract(sLine.getQtyEntered()));
                        

                    //	Update Sales Order Line
                    if (oLine!=null){

                        /*
                        *  16/08/2012 Zynnia. 
                        *  Modificacion para que si la cantidad entregada supera la que se ordeno, en 
                        *  reservada no quede un valor negativo, sino ponga valor 0.
                        * 
                        */
                        if (oLine.getQtyReserved().signum()==-1){
                            oLine.setQtyReserved(BigDecimal.ZERO);
                        }
                        
                        
                        /**
                        * BISion - Santiago Ibañez - 19/06/2009
                        * Modificacion realizada para actualizar el ordenado de la OC
                        */
                        oLine.setQtyReserved(oLine.getQtyReserved().subtract(QtyPO));
                        oLine.setQtyOrdered(oLine.getQtyOrdered().subtract(QtyPO));
                        //Chequeo que la cantidad ordendada de la OC este entre 0 y cantidad pedida
                        if (oLine.getQtyOrdered().compareTo(BigDecimal.ZERO)==-1){
                            oLine.setQtyReserved(BigDecimal.ZERO);
                            oLine.setQtyOrdered(BigDecimal.ZERO);
                        }
                        else if (oLine.getQtyOrdered().compareTo(oLine.getQtyEntered())>=1){
                            oLine.setQtyReserved(oLine.getQtyEntered());
                            oLine.setQtyOrdered(oLine.getQtyEntered());
                        }
                        //fin modificacion BISion

                        if (isSOTrx() || sLine.getM_Product_ID() == 0){
                            if (isSOTrx())
                                oLine.setQtyDelivered(oLine.getQtyDelivered().subtract(Qty));
                            else
                                oLine.setQtyDelivered(oLine.getQtyDelivered().add(Qty));
                            oLine.setDateDelivered(getMovementDate());	//	overwrite=last
                        }

                        if (!oLine.save(get_TrxName())){
                                m_processMsg = "Could not update Order Line";
                                return DocAction.STATUS_Invalid;
                        } else
                                log.fine("OrderLine -> Reserved=" + oLine.getQtyReserved()
                                        + ", Delivered=" + oLine.getQtyReserved());
                    }

                    //	Create Asset for SO
                    if (product != null && isSOTrx() && product.isCreateAsset()
                        && sLine.getMovementQty().signum() > 0 && !isReversal()){

                        log.fine("Asset");
                        info.append("@A_Asset_ID@: ");
                        int noAssets = sLine.getMovementQty().intValue();
                        if (!product.isOneAssetPerUOM())
                                noAssets = 1;
                        for (int i = 0; i < noAssets; i++){
                            if (i > 0)
                                    info.append(" - ");
                            int deliveryCount = i+1;
                            if (!product.isOneAssetPerUOM())
                                deliveryCount = 0;
                            MAsset asset = new MAsset (this, sLine, deliveryCount);
                            if (!asset.save(get_TrxName())){
                                m_processMsg = "Could not create Asset";
                                return DocAction.STATUS_Invalid;
                            }
                            info.append(asset.getValue());
                        }

                    }	//	Asset


                    //	Matching
                    if (!isSOTrx() && sLine.getM_Product_ID() != 0 && !isReversal()){
                        BigDecimal matchQty = sLine.getQtyEntered();
                        //	Invoice - Receipt Match (requires Product)
                        MInvoiceLine iLine = MInvoiceLine.getOfInOutLine (sLine);
                        if (iLine != null && iLine.getM_Product_ID() != 0){
                            MMatchInv[] matches = MMatchInv.get(getCtx(),
                                    sLine.getM_InOutLine_ID(), iLine.getC_InvoiceLine_ID(), get_TrxName());
                            if (matches == null || matches.length == 0){
                                MMatchInv inv = new MMatchInv (iLine, getMovementDate(), matchQty);
                                if (sLine.getM_AttributeSetInstance_ID() != iLine.getM_AttributeSetInstance_ID()){
                                    iLine.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                                    iLine.save();	//	update matched invoice with ASI
                                    inv.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                                }
                                if (!inv.save(get_TrxName())){
                                        m_processMsg = "Could not create Inv Matching";
                                        return DocAction.STATUS_Invalid;
                                }
                            }
                        }

                        //	Link to Order
                        if (sLine.getC_OrderLine_ID() != 0){
                            log.fine("PO Matching");
                            //	Ship - PO
                            MMatchPO po = MMatchPO.create (null, sLine, getMovementDate(), matchQty);
                            if (!po.save(get_TrxName())){
                                    m_processMsg = "Could not create PO Matching";
                                    return DocAction.STATUS_Invalid;
                            }
                            //	Update PO with ASI
                            if (oLine != null && oLine.getM_AttributeSetInstance_ID() == 0){
                                    oLine.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                                    oLine.save(get_TrxName());
                            }
                        } else {
                            //	No Order - Try finding links via Invoice

                            //	Invoice has an Order Link
                            if (iLine != null && iLine.getC_OrderLine_ID() != 0){
                                //	Invoice is created before  Shipment
                                log.fine("PO(Inv) Matching");
                                //	Ship - Invoice
                                MMatchPO po = MMatchPO.create (iLine, sLine,
                                        getMovementDate(), matchQty);
                                if (!po.save(get_TrxName())){
                                        m_processMsg = "Could not create PO(Inv) Matching";
                                        return DocAction.STATUS_Invalid;
                                }
                                //	Update PO with ASI
                                oLine = new MOrderLine (getCtx(), po.getC_OrderLine_ID(), get_TrxName());
                                if (oLine != null && oLine.getM_AttributeSetInstance_ID() == 0){
                                        oLine.setM_AttributeSetInstance_ID(sLine.getM_AttributeSetInstance_ID());
                                        oLine.save(get_TrxName());
                                }
                            }
                        }	//	No Order
                    }	//	PO Matching
                    if (oLine!=null){
                        if (isSOTrx())
                            oLine.setDelivered(Qty.negate());
                        else
                            oLine.setDelivered(Qty);
                        oLine.actualizarCantidadRegistroMRP();
                    }
                    sLine.setQty(Qty);
                }
                //fin for all lines
            }
            // fin comprobacion tipo de documento

            //	Counter Documents
            MInOut counter = createCounterDoc();
            if (counter != null)
                info.append(" - @CounterDoc@: @M_InOut_ID@=").append(counter.getDocumentNo());
            //	User Validation
            String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
            if (valid != null){
                    m_processMsg = valid;
                    return DocAction.STATUS_Invalid;
            }

            m_processMsg = info.toString();
            setProcessed(true);
            setDocAction(DOCACTION_Close);
            return DocAction.STATUS_Completed;
	}	//	completeIt


	/**
	 * 	Check Material Policy
	 * 	Sets line ASI
	 */
	private void checkMaterialPolicy()
	{
		int no = MInOutLineMA.deleteInOutMA(getM_InOut_ID(), get_TrxName());
		if (no > 0)
			log.config("Delete old #" + no);
		MInOutLine[] lines = getLines(false);

		//	Incoming Trx
		String MovementType = getMovementType();
		boolean inTrx = MovementType.charAt(1) == '+';	//	V+ Vendor Receipt
		MClient client = MClient.get(getCtx());

		//	Check Lines
		for (int i = 0; i < lines.length; i++)
		{
			MInOutLine line = lines[i];
			boolean needSave = false;
			MProduct product = line.getProduct();

			//	Need to have Location
			if (product != null
				&& line.getM_Locator_ID() == 0)
			{
				line.setM_Warehouse_ID(getM_Warehouse_ID());
				line.setM_Locator_ID(inTrx ? Env.ZERO : line.getMovementQty());	//	default Locator
				needSave = true;
			}

			//	Attribute Set Instance
			if (product != null
				&& line.getM_AttributeSetInstance_ID() == 0)
			{
				if (inTrx)
				{
					MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), 0, get_TrxName());
					asi.setClientOrg(getAD_Client_ID(), 0);
					asi.setM_AttributeSet_ID(product.getM_AttributeSet_ID());
					if (asi.save())
					{
						line.setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
						log.config("New ASI=" + line);
						needSave = true;
					}
				}
				else	//	Outgoing Trx
				{
					MProductCategory pc = MProductCategory.get(getCtx(), product.getM_Product_Category_ID());
					String MMPolicy = pc.getMMPolicy();
					if (MMPolicy == null || MMPolicy.length() == 0)
						MMPolicy = client.getMMPolicy();
					//
					MStorage[] storages = MStorage.getAllWithASI(getCtx(),
						line.getM_Product_ID(),	line.getM_Locator_ID(),
						MClient.MMPOLICY_FiFo.equals(MMPolicy), get_TrxName());
					BigDecimal qtyToDeliver = line.getMovementQty();
					for (int ii = 0; ii < storages.length; ii++)
					{
						MStorage storage = storages[ii];
						if (ii == 0)
						{
							if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0)
							{
								line.setM_AttributeSetInstance_ID(storage.getM_AttributeSetInstance_ID());
								needSave = true;
								log.config("Direct - " + line);
								qtyToDeliver = Env.ZERO;
							}
							else
							{
								log.config("Split - " + line);
								MInOutLineMA ma = new MInOutLineMA (line,
									storage.getM_AttributeSetInstance_ID(),
									storage.getQtyOnHand());
								if (!ma.save())
									;
								qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
								log.fine("#" + ii + ": " + ma + ", QtyToDeliver=" + qtyToDeliver);
							}
						}
						else	//	 create addl material allocation
						{
							MInOutLineMA ma = new MInOutLineMA (line,
								storage.getM_AttributeSetInstance_ID(),
								qtyToDeliver);
							if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0)
								qtyToDeliver = Env.ZERO;
							else
							{
								ma.setMovementQty(storage.getQtyOnHand());
								qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
							}
							if (!ma.save())
								;
							log.fine("#" + ii + ": " + ma + ", QtyToDeliver=" + qtyToDeliver);
						}
						if (qtyToDeliver.signum() == 0)
							break;
					}	//	 for all storages

					//	No AttributeSetInstance found for remainder
					if (qtyToDeliver.signum() != 0)
					{
						MInOutLineMA ma = new MInOutLineMA (line,
							0, qtyToDeliver);
						if (!ma.save())
							;
						log.fine("##: " + ma);
					}
				}	//	outgoing Trx
			}	//	attributeSetInstance

			if (needSave && !line.save())
				log.severe("NOT saved " + line);
		}	//	for all lines
	}	//	checkMaterialPolicy


	/**************************************************************************
	 * 	Create Counter Document
	 */
	private MInOut createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_InOut_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID();
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
		log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
			if (C_DocTypeTarget_ID <= 0)
				return null;
		}

		//	Deep Copy
		MInOut counter = copyFrom(this, getMovementDate(),
			C_DocTypeTarget_ID, !isSOTrx(), true, get_TrxName(), true);

		//
		counter.setAD_Org_ID(counterAD_Org_ID);
		counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
		//
		counter.setBPartner(counterBP);
		//	Refernces (Should not be required
		counter.setSalesRep_ID(getSalesRep_ID());
		counter.save(get_TrxName());

		String MovementType = counter.getMovementType();
		boolean inTrx = MovementType.charAt(1) == '+';	//	V+ Vendor Receipt

		//	Update copied lines
		MInOutLine[] counterLines = counter.getLines(true);
		for (int i = 0; i < counterLines.length; i++)
		{
			MInOutLine counterLine = counterLines[i];
			counterLine.setClientOrg(counter);
			counterLine.setM_Warehouse_ID(counter.getM_Warehouse_ID());
			counterLine.setM_Locator_ID(0);
			counterLine.setM_Locator_ID(inTrx ? Env.ZERO : counterLine.getMovementQty());
			//
			counterLine.save(get_TrxName());
		}

		log.fine(counter.toString());

		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				counter.processIt(counterDT.getDocAction());
				counter.save(get_TrxName());
			}
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Void Comprobantes Anulados.
	 * 	@return true if success
	 */
	public boolean anularComprobantesAsociados()
	{
		//	Desasociar Lineas de Orden de Venta
		MMatchPO[] mPO = MMatchPO.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
		for (int i = 0; i < mPO.length; i++)
			// Desasocir cada Match con la orden
			mPO[i].delete(true);

		/*
		 	 Anular Comprobante Asociado
			Se setea el estado anulado momentaneamente para que no entre en un ciclo infinito.
		 */
		String estadoant = getDocStatus();
		setDocStatus(DOCSTATUS_Voided);
		save(get_TrxName());

		// Desasociar Lineas y Anular factura
		MMatchInv[] mInv = MMatchInv.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
		for (int i = 0; i < mInv.length; i++)
		{	//	Anular remito de cada linea asociada, generalmente es solo uno.
			MInvoiceLine invoiceLine = new MInvoiceLine(getCtx(),mInv[i].getC_InvoiceLine_ID(),get_TrxName());
			MInvoice invoice = new MInvoice(getCtx(),invoiceLine.getC_Invoice_ID(),get_TrxName());
			if (!invoice.voidIt())
				return false;
			mInv[i].delete(true);
		}

		setDocStatus(estadoant);

		//	Desasociar Orden de Venta, decremento por linea la cantidad entregada e incremento la cantidad reservada
		for (int i = 0; i < getLines().length; i++)
		{	//	Anular remito de cada linea asociada, generalmente es solo uno.
			MInOutLine inoutLine = getLines()[i];
			MOrderLine oLine = new MOrderLine(getCtx(),inoutLine.getC_OrderLine_ID(),get_TrxName());
			oLine.setQtyReserved(oLine.getQtyReserved().add(inoutLine.getMovementQty()));
			oLine.setQtyOrdered(oLine.getQtyOrdered().add(inoutLine.getMovementQty()));
			oLine.setQtyDelivered(oLine.getQtyDelivered().subtract(inoutLine.getMovementQty()));
			inoutLine.setC_OrderLine_ID(0);
			if (!inoutLine.save(get_TrxName()) || !oLine.save(get_TrxName()))
				return false;
		}

		setC_Order_ID(0);

		return true;
	}

	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	public boolean voidIt(){
            
		log.info(toString());

		// Temporal.. ver como se cambia
		if (DOCSTATUS_Voided.equals(getDocStatus())){
                    return true;
		}

		if (DOCSTATUS_Closed.equals(getDocStatus())
                    || DOCSTATUS_Reversed.equals(getDocStatus())
                    || DOCSTATUS_Voided.equals(getDocStatus())){
                    
                    m_processMsg = "Document Closed: " + getDocStatus();
                    return false;
                    
		}

                /*
                 *  Zynnia - 14/12/2012
                 *  JF
                 *  Se debe validar que si existe cantidad facturada no permita anular
                 * 
                 */
                
                int flagFact = 0;
                
                MInOutLine[] linesInOut = getLines(false);
                for (int i = 0; i < linesInOut.length; i++){

                        MInOutLine lineInOut = linesInOut[i];
                        MOrderLine oLineInOut = new MOrderLine(getCtx(),lineInOut.getC_OrderLine_ID(),get_TrxName());
                        if(!oLineInOut.getQtyInvoiced().equals(Env.ZERO)){
                            JOptionPane.showMessageDialog(null,"Línea " + oLineInOut.getLine() + " de OC con cantidad facturada, no puede anular este comprobante", "Info", JOptionPane.INFORMATION_MESSAGE);
                            return false;
                        }
                            
                }
                
		if (DOCSTATUS_Closed.equals(getDocStatus())
                    || DOCSTATUS_Reversed.equals(getDocStatus())
                    || DOCSTATUS_Voided.equals(getDocStatus())){
                    
                    m_processMsg = "Document Closed: " + getDocStatus();
                    return false;
                    
		}                

                /** BISion - 22/06/2009 - Santiago Ibañez
                 * Modificacion realizada para corregir el ordenado en la OC asociada
                 */
                //corregirOrdenadoOC();
                
		//	Not Processed

                if (DOCSTATUS_Drafted.equals(getDocStatus())
                    || DOCSTATUS_Invalid.equals(getDocStatus())
                    || DOCSTATUS_InProgress.equals(getDocStatus())
                    || DOCSTATUS_Approved.equals(getDocStatus())
                    || DOCSTATUS_NotApproved.equals(getDocStatus()) ){
                    
                    //	Set lines to 0
                    MInOutLine[] lines = getLines(false);
                    for (int i = 0; i < lines.length; i++){
                        MInOutLine line = lines[i];
                        BigDecimal old = line.getMovementQty();
                        if (old.signum() != 0){
                            line.setQty(Env.ZERO);
                            line.addDescription("Void (" + old + ")");
                            line.save(get_TrxName());
                        }
                    }
                    
		} else {
                    //	return reverseCorrectIt();
                    /*
                     *   Zynnia 29/05/2012
                     *   
                     *   Modificacion para que actualice bien el Stock y no deje en negativo.
                     *   Modificacion para que actualice de forma correcta la cantidad entregada y reservada de la OC
                     */

                    MInOutLine[] lines = getLines(false);
                    for (int i = 0; i < lines.length; i++){

                        MInOutLine line = lines[i];

                        MStorage mStor = MStorage.get(getCtx(),line.getM_Locator_ID(),line.getM_Product_ID(),line.getM_AttributeSetInstance_ID(),get_TrxName());
                        /*
                         *  Verificacion para que solo modifique en el stock en el caso de que el
                         *  producto sea Stockeable. Sino tiene que poderse anular pero sin modificar
                         *  el stock
                         */

                        
                        /*
                         *  Zynnia 29/04/2013
                         *  Modificacion para que tome el mStorCero teniendo en cuenta el Locator por defecto
                         *  y no el de la linea.
                         */

                        //Obtengo Locator de la linea
                        MLocator lineLocator = MLocator.get(getCtx(),line.getM_Locator_ID());

                        //Obtengo Almacen de la linea
                        MWarehouse warehouse = MWarehouse.get(getCtx(),lineLocator.getM_Warehouse_ID());

                        //Obtengo el id del default Locator del Almacen
                        int defaultLocatorID = warehouse.getDefaultLocator().getM_Locator_ID();

                        //Obtengo mStorCero
                        MStorage mStorCero = MStorage.get(getCtx(),defaultLocatorID,line.getM_Product_ID(),0,get_TrxName());
                        
                        BigDecimal QtyOnHand = Env.ZERO;
                        if (mStor != null){
                            QtyOnHand = mStor.getQtyOnHand();
                        }
                        MOrderLine oLine = new MOrderLine(getCtx(),line.getC_OrderLine_ID(),get_TrxName());
                        /*
                         *  Zynnia 06/12/2012
                         *  Agregamos para que se cree una transaccion con el negativo de lo que
                         *  tenia la recepcion de materiales.
                         *  Toma la misma partida, y para ese "Recibo de proveedor" crea una linea con la qty en negativo.
                         *
                         */
                        MTransaction mtrx = null;
                        mtrx = new MTransaction (getCtx(),this.getMovementType(),line.getM_Locator_ID(),line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),line.getQtyEntered().negate(), getMovementDate(), get_TrxName());
                        mtrx.setM_InOutLine_ID(line.getM_InOutLine_ID());
                        if (!mtrx.save(get_TrxName())){
                                m_processMsg = "Could not create Material Transaction";
                                                return false;
                        }

                        /*
                         *  Zynnia 10/08/2012
                         *  Agregamos para que se actualice tambien la tabla matchPO que es de donde
                         *  a partir del create from se sacan los datos, por ende, las cantidades de las lineas deben ser cero
                         *  para que de este modo despues se pueda elegir.
                         */

                        String updMPO = "UPDATE M_MatchPO SET qty = 0 WHERE M_InOutLine_ID = " + line.getM_InOutLine_ID();

                        int noLine = DB.executeUpdate(updMPO, get_TrxName());
                        log.fine(" Lines=" + noLine);
                        BigDecimal QtyDlv = oLine.getQtyDelivered();
                        BigDecimal QtyRsv = oLine.getQtyReserved();
                        BigDecimal QtyOrd = oLine.getQtyOrdered();

                        oLine.setQtyDelivered(line.getQtyEntered().negate().add(QtyDlv));


                        // Siempre tengo que tener en cuanta de lo que pido y lo que entrego lo que me resta.
                        // Nunca sería negativo

                        BigDecimal valActualizar = oLine.getQtyEntered().subtract(oLine.getQtyDelivered());

                        BigDecimal ordAnt = oLine.getQtyOrdered();

                        oLine.setQtyReserved(valActualizar);
                        oLine.setQtyOrdered(valActualizar);


                        if (mStor != null)
                            mStor.setQtyOnHand(QtyOnHand.add(line.getQtyEntered().negate()));

                        if ( mStorCero != null ){
                            if(oLine.getQtyEntered().compareTo(line.getQtyEntered()) == -1 ){
                                mStorCero.setQtyOrdered(mStorCero.getQtyOrdered().add(oLine.getQtyEntered()));
                            } else {
                                mStorCero.setQtyOrdered(mStorCero.getQtyOrdered().add(valActualizar).subtract(ordAnt));
                            }
                            mStorCero.save(get_TrxName());
                        }

                        if (mStor != null){
                            if (mStor.getQtyOnHand().signum() == -1){
                                mStor.setQtyOnHand(Env.ZERO);
                            }
                        }
                        oLine.actualizarCantidadRegistroMRP();
                        oLine.save(get_TrxName());
                        if (mStor != null)
                            mStor.save(get_TrxName());

                    }
                }

		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction - same date
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		if (!verificarAnular())
			return false;

        /** BISion - 22/06/2010 - Santiago Ibañez
         * Los Remitos Florida, Loma Hermosa y Famatina
         * no generan remitos revertidos
         */
        MDocType docType = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
        if (docType.getName().equals("Remito Loma Hermosa")||
            docType.getName().equals("Remito Florida")||
            docType.getName().equals("Remito Famatina")){
            m_processMsg = getDocumentNo();
            setProcessed(true);
            setDocStatus(DOCSTATUS_Reversed);		//	 may come from void
            setDocAction(DOCACTION_None);
            return true;
        }

		//	Reverse/Delete Matching
		if (!isSOTrx())
		{
			MMatchInv[] mInv = MMatchInv.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
			for (int i = 0; i < mInv.length; i++)
				mInv[i].delete(true);
			MMatchPO[] mPO = MMatchPO.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
			for (int i = 0; i < mPO.length; i++)
			{
				if (mPO[i].getC_InvoiceLine_ID() == 0)
					mPO[i].delete(true);
				else
				{
					mPO[i].setM_InOutLine_ID(0);
					mPO[i].save();

				}
			}
		}
		else
		{

			if (getC_Order_ID()!=0)
				//-	Desde una OV
				//-	Factura, Remito y Nota de Cr�dito deben ser anulados y se desasocia la Orden de venta.
				{
					JOptionPane.showMessageDialog(null,"Se anular� el Remito " + getDocumentNo() + ", asociado a la Orden de venta", "Info", JOptionPane.INFORMATION_MESSAGE);

					if (!anularComprobantesAsociados())
					{
						m_processMsg = "@Fallo el proceso de Anular/Revertir Comprobantes Asociados@";
						return false;
					}

				}
				else
				//�	Desde la ventana Remito
				//�	Si el Remito fue creado desde una Factura.
				{
					//	No revertir, solo elimina los Match
					MMatchInv[] mInv = MMatchInv.getInOut(getCtx(), getM_InOut_ID(), get_TrxName());
					for (int i = 0; i < mInv.length; i++)
						mInv[i].delete(true);
				}
		}

		

		//	Deep Copy
		int docType_ID = getC_DocType_ID();
		if (docType.getC_RevDocType_ID()!=0)
			docType_ID = docType.getC_RevDocType_ID();

		MInOut reversal = copyFrom (this, getMovementDate(),
				docType_ID, isSOTrx(), false, get_TrxName(), true);
		if (reversal == null)
		{
			m_processMsg = "Could not create Ship Reversal";
			return false;
		}
		reversal.setReversal(true);

		//	Reverse Line Qty
		MInOutLine[] sLines = getLines(false);
		MInOutLine[] rLines = reversal.getLines(false);
		for (int i = 0; i < rLines.length; i++)
		{
			MInOutLine rLine = rLines[i];
			rLine.setQtyEntered(rLine.getQtyEntered().negate());
			/**
             * BISion - 25/06/2009 - Santiago Ibañez
             * Modificacion realizada para que no haya diferencias entre la
             * cantidad ingresada y la del movimiento.
             */
			//rLine.setMovementQty(rLine.getMovementQty().negate());
			rLine.setMovementQty(rLine.getQtyEntered().negate());
			//fin modificacion BISIon
			rLine.setM_AttributeSetInstance_ID(sLines[i].getM_AttributeSetInstance_ID());
			rLine.setC_OrderLine_ID(sLines[i].getC_OrderLine_ID());
			if (!rLine.save(get_TrxName()))
			{
				m_processMsg = "Could not correct Ship Reversal Line";
				return false;
			}
			//	We need to copy MA
			if (rLine.getM_AttributeSetInstance_ID() == 0)
			{
				MInOutLineMA mas[] = MInOutLineMA.get(getCtx(),
					sLines[i].getM_InOutLine_ID(), get_TrxName());
				for (int j = 0; j < mas.length; j++)
				{
					MInOutLineMA ma = new MInOutLineMA (rLine,
						mas[j].getM_AttributeSetInstance_ID(),
						mas[j].getMovementQty().negate());
					if (!ma.save())
						;
				}
			}
			//	De-Activate Asset
			MAsset asset = MAsset.getFromShipment(getCtx(), sLines[i].getM_InOutLine_ID(), get_TrxName());
			if (asset != null)
			{
				asset.setIsActive(false);
				asset.addDescription("(" + reversal.getDocumentNo() + " #" + rLine.getLine() + "<-)");
				asset.save();
			}
		}

		reversal.setC_Order_ID(getC_Order_ID());
		reversal.addDescription("{->" + getDocumentNo() + ")");
		//
		if (!reversal.processIt(DocAction.ACTION_Complete)
			|| !reversal.getDocStatus().equals(DocAction.STATUS_Completed))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.save(get_TrxName());
		//
		addDescription("(" + reversal.getDocumentNo() + "<-)");

		m_processMsg = reversal.getDocumentNo();
		setProcessed(true);
		setDocStatus(DOCSTATUS_Reversed);		//	 may come from void
		setDocAction(DOCACTION_None);
		return true;
	}	//	reverseCorrectionIt

    /**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		return false;
	}	//	reActivateIt


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(":")
		//	.append(Msg.translate(getCtx(),"TotalLines")).append("=").append(getTotalLines())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getSalesRep_ID();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return Env.ZERO;
	}	//	getApprovalAmt

	/**
	 * 	Get C_Currency_ID
	 *	@return Accounting Currency
	 */
	public int getC_Currency_ID ()
	{
		return Env.getContextAsInt(getCtx(),"$C_Currency_ID ");
	}	//	getC_Currency_ID

    /**
        * 	Document Status is Complete or Closed
        *	@return true if CO, CL or RE
        */
    public boolean isComplete()
    {
            String ds = getDocStatus();
            return DOCSTATUS_Completed.equals(ds)
                    || DOCSTATUS_Closed.equals(ds)
                    || DOCSTATUS_Reversed.equals(ds);
    }	//	isComplete

    protected boolean verificarAnular()	{
            log.info(toString());
            MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
            if (!MPeriod.isOpen(getCtx(), getMovementDate(), dt.getDocBaseType()))
            {
                    m_processMsg = "@PeriodClosed@";
                    return false;
            }

            return true;
    }


    /**
     * BISion -04/02/2009 - Santiago Ibañez
     * Metodo creado para comprobar que la cantidad ingresada no supera el total
     * recibido para la misma linea de orden de compra + umbral
     * @return 0 si no supera el umbral > 0 caso contrario
     */
    private int superaUmbral(){
        try {
            //Obtengo el umbral predeterminado
            int umbral = 0;
            String sqlUmbral = "SELECT valor FROM M_UMBRAL WHERE ad_workflow_id = 1001078";
            PreparedStatement pstmt = DB.prepareStatement(sqlUmbral, null);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                umbral = rs.getInt(1);
            }
            rs.close();
            //Obtengo cada una de las lineas de este recibo
            MInOutLine[] lines = getLines(false);
            //para cada una de estas lineas compruebo que no superen el umbral
            for (int i=0;i<lines.length;i++){
                //Obtengo la suma de las recepciones realizadas sobre esta linea OC
                MMatchPO[] matchs = MMatchPO.getByOrderLine(Env.getCtx(),lines[i].getC_OrderLine_ID(),null);
                //Cantidad recibida para la linea
                BigDecimal qty = BigDecimal.ZERO;
                //sumo todas las cantidades recibidas
                for (int j=0;j<matchs.length;j++){
                    qty = qty.add(matchs[j].getQty());
                }
                //a la suma  de lo total recibido le sumo la cant de este recibo
                qty = qty.add(lines[i].getQtyEntered());
                //Obtengo la linea de la OC para saber la cantidad ordenada
                MOrderLine orderLine = new MOrderLine(Env.getCtx(),lines[i].getC_OrderLine_ID(),null);
                //Obtengo la cantidad total ordenada
                BigDecimal qtyEntered = orderLine.getQtyEntered();
                //divido el umbral por 100
                BigDecimal u = new BigDecimal(umbral);
                u = u.divide(new BigDecimal(100));
                //si hay una linea que supera el umbral entonces termino
                if (qty.compareTo(qtyEntered.add(qtyEntered.multiply(u)))>0)
                    //retorno para informar el numero de linea que supera umbral
                    return lines[i].getLine();
            } //continua con la proxima linea de recibo

        } catch (SQLException ex) {
            Logger.getLogger(MInOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}	//	MInOut