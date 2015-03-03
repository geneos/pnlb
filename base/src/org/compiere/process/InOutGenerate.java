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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Generate Shipments
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutGenerate.java,v 1.30 2005/10/28 00:59:28 jjanke Exp $
 */
public class InOutGenerate extends SvrProcess
{
	/**	Manual Selection		*/
	private boolean 	p_Selection = false;
	/** Warehouse				*/
	private int			p_M_Warehouse_ID = 0;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** Promise Date			*/
	private Timestamp	p_DatePromised = null;
	/** Include Orders w. unconfirmed Shipments	*/
	private boolean		p_IsUnconfirmedInOut = false;
	/** DocAction				*/
	private String		p_docAction = DocAction.ACTION_Complete;
	/** Consolidate				*/
	private boolean		p_ConsolidateDocument = true;
	
	/**	The current Shipment	*/
	private MInOut 		m_shipment = null;
	/** Numner of Shipments		*/
	private int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
	/** Movement Date			*/
	private Timestamp	m_movementDate = null;
	/**	Last BP Location		*/
	private int			m_lastC_BPartner_Location_ID = -1;

	/** The Query sql			*/
	private String 		m_sql = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("DatePromised"))
				p_DatePromised = (Timestamp)para[i].getParameter();
			else if (name.equals("Selection"))
				p_Selection = "Y".equals(para[i].getParameter());
			else if (name.equals("IsUnconfirmedInOut"))
				p_IsUnconfirmedInOut = "Y".equals(para[i].getParameter());
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(para[i].getParameter());
			else if (name.equals("DocAction"))
				p_docAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
			//	Login Date
			m_movementDate = Env.getContextAsDate(getCtx(), "#Date");
			if (m_movementDate == null)
				m_movementDate = new Timestamp(System.currentTimeMillis());
			//	DocAction check
			if (!DocAction.ACTION_Complete.equals(p_docAction))
				p_docAction = DocAction.ACTION_Prepare;
		}
	}	//	prepare

	/**
	 * 	Generate Shipments
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("Selection=" + p_Selection
			+ ", M_Warehouse_ID=" + p_M_Warehouse_ID 
			+ ", C_BPartner_ID=" + p_C_BPartner_ID 
			+ ", Consolidate=" + p_ConsolidateDocument
			+ ", IsUnconfirmed=" + p_IsUnconfirmedInOut
			+ ", Movement=" + m_movementDate);
		
		if (p_M_Warehouse_ID == 0)
			throw new CompiereUserError("@NotFound@ @M_Warehouse_ID@");
		
		if (p_Selection)	//	VInOutGen
		{
			m_sql = "SELECT * FROM C_Order "
				+ "WHERE IsSelected='Y' AND DocStatus='CO' AND IsSOTrx='Y' AND AD_Client_ID=? "
				+ "ORDER BY M_Warehouse_ID, PriorityRule, C_BPartner_ID, C_BPartner_Location_ID, C_Order_ID";
		}
		else
		{
			m_sql = "SELECT * FROM C_Order o "
				+ "WHERE DocStatus='CO' AND IsSOTrx='Y'"
				//	No Offer,POS
				+ " AND o.C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType "
					+ "WHERE DocBaseType='SOO' AND DocSubTypeSO NOT IN ('ON','OB','WR'))"
				+ "	AND o.IsDropShip='N'"
				//	No Manual
				+ " AND o.DeliveryRule<>'M'"
				//	Open Order Lines with Warehouse
				+ " AND EXISTS (SELECT * FROM C_OrderLine ol "
					+ "WHERE ol.M_Warehouse_ID=?"
					+ " AND o.C_Order_ID=ol.C_Order_ID AND ol.QtyOrdered<>ol.QtyDelivered)";
			if (p_C_BPartner_ID != 0)
				m_sql += " AND C_BPartner_ID=?";
			if (p_DatePromised != null)
				m_sql += " AND TRUNC(DatePromised)<=?";
			m_sql += " ORDER BY M_Warehouse_ID, PriorityRule, C_BPartner_ID, C_BPartner_Location_ID, C_Order_ID";
		}
	//	m_sql += " FOR UPDATE";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (m_sql, get_TrxName());
			int index = 1;
			if (p_Selection)
				pstmt.setInt(index++, Env.getAD_Client_ID(getCtx()));
			else	
			{
				pstmt.setInt(index++, p_M_Warehouse_ID);
				if (p_C_BPartner_ID != 0)
					pstmt.setInt(index++, p_C_BPartner_ID);
				if (p_DatePromised != null)
					pstmt.setTimestamp(index++, p_DatePromised);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_sql, e);
		}
		return generate(pstmt);
	}	//	doIt
	
	/**
	 * 	Generate Shipments
	 * 	@param pstmt Order Query
	 *	@return info
	 */
	private String generate (PreparedStatement pstmt)
	{
		MClient client = MClient.get(getCtx());
		try
		{
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())		//	Order
			{
				MOrder order = new MOrder (getCtx(), rs, get_TrxName());
				//	New Header Shipment Location
				if (!p_ConsolidateDocument 
					|| (m_shipment != null 
					&& m_shipment.getC_BPartner_Location_ID() != order.getC_BPartner_Location_ID()) )
					completeShipment();
				log.fine("check: " + order + " - DeliveryRule=" + order.getDeliveryRule());
				//
				Timestamp minGuaranteeDate = m_movementDate;
				boolean completeOrder = MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule());
				//
				String where = " AND M_Warehouse_ID=" + p_M_Warehouse_ID;
				//	Exclude Auto Delivery if not Force
				if (!MOrder.DELIVERYRULE_Force.equals(order.getDeliveryRule()))
					where += " AND (C_OrderLine.M_Product_ID IS NULL"
						+ " OR EXISTS (SELECT * FROM M_Product p "
						+ "WHERE C_OrderLine.M_Product_ID=p.M_Product_ID"
						+ " AND IsExcludeAutoDelivery='N'))";
				//	Exclude Unconfirmed
				if (!p_IsUnconfirmedInOut)
					where += " AND NOT EXISTS (SELECT * FROM M_InOutLine iol"
							+ " INNER JOIN M_InOut io ON (iol.M_InOut_ID=io.M_InOut_ID) "
								+ "WHERE iol.C_OrderLine_ID=C_OrderLine.C_OrderLine_ID AND io.DocStatus IN ('IP','WC'))";
				//	Deadlock Prevention - Order by M_Product_ID
				MOrderLine[] lines = order.getLines (where, "ORDER BY C_BPartner_Location_ID, M_Product_ID");
				for (int i = 0; i < lines.length; i++)
				{
					MOrderLine line = lines[i];
					if (line.getM_Warehouse_ID() != p_M_Warehouse_ID)
						continue;
					log.fine("check: " + line);
					BigDecimal onHand = Env.ZERO;
					BigDecimal toDeliver = line.getQtyOrdered().subtract(line.getQtyDelivered());
					MProduct product = line.getProduct();
					//	Nothing to Deliver
					if (product != null && toDeliver.signum() == 0)
						continue;
					
					//	Check / adjust for confirmations
					BigDecimal unconfirmedShippedQty = Env.ZERO;
					if (p_IsUnconfirmedInOut && product != null && toDeliver.signum() != 0)
					{
						String where2 = "EXISTS (SELECT * FROM M_InOut io WHERE io.M_InOut_ID=M_InOutLine.M_InOut_ID AND io.DocStatus IN ('IP','WC'))";
						MInOutLine[] iols = MInOutLine.getOfOrderLine(getCtx(), 
							line.getC_OrderLine_ID(), where2, null);
						for (int j = 0; j < iols.length; j++) 
							unconfirmedShippedQty = unconfirmedShippedQty.add(iols[j].getMovementQty());
						String logInfo = "Unconfirmed Qty=" + unconfirmedShippedQty 
							+ " - ToDeliver=" + toDeliver + "->";					
						toDeliver = toDeliver.subtract(unconfirmedShippedQty);
						logInfo += toDeliver;
						if (toDeliver.signum() < 0)
						{
							toDeliver = Env.ZERO;
							logInfo += " (set to 0)";
						}
						//	Adjust On Hand
						onHand = onHand.subtract(unconfirmedShippedQty);
						log.fine(logInfo);					
					}
					
					//	Comments & lines w/o product & services
					if ((product == null || !product.isStocked())
						&& (line.getQtyOrdered().signum() == 0 	//	comments
							|| toDeliver.signum() != 0))			//	lines w/o product
					{
						if (!MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule()))	//	printed later
							createLine (order, line, toDeliver, null, false);
						continue;
					}

					//	Stored Product
					MProductCategory pc = MProductCategory.get(order.getCtx(), product.getM_Product_Category_ID());
					String MMPolicy = pc.getMMPolicy();
					if (MMPolicy == null || MMPolicy.length() == 0)
						MMPolicy = client.getMMPolicy();
					//
					MStorage[] storages = MStorage.getWarehouse(getCtx(), 
						line.getM_Warehouse_ID(), 
						line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
						product.getM_AttributeSet_ID(),
						line.getM_AttributeSetInstance_ID()==0, minGuaranteeDate, 
						MClient.MMPOLICY_FiFo.equals(MMPolicy), get_TrxName());
					for (int j = 0; j < storages.length; j++)
					{
						MStorage storage = storages[j];
						onHand = onHand.add(storage.getQtyOnHand());
					}
					boolean fullLine = onHand.compareTo(toDeliver) >= 0
						|| toDeliver.signum() < 0;
					
					//	Complete Order
					if (completeOrder && !fullLine)
					{
						log.fine("Failed CompleteOrder - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver + " - " + line);
						completeOrder = false;
						break;
					}
					//	Complete Line
					else if (fullLine && MOrder.DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule()))
					{
						log.fine("CompleteLine - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ ", ToDeliver=" + toDeliver + " - " + line);
						//	
						createLine (order, line, toDeliver, storages, false);
					}
					//	Availability
					else if (MOrder.DELIVERYRULE_Availability.equals(order.getDeliveryRule())
						&& (onHand.signum() > 0
							|| toDeliver.signum() < 0))
					{
						BigDecimal deliver = toDeliver;
						if (deliver.compareTo(onHand) > 0)
							deliver = onHand;
						log.fine("Available - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver 
							+ ", Delivering=" + deliver + " - " + line);
						//	
						createLine (order, line, deliver, storages, false);
					}
					//	Force
					else if (MOrder.DELIVERYRULE_Force.equals(order.getDeliveryRule()))
					{
						BigDecimal deliver = toDeliver;
						log.fine("Force - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver 
							+ ", Delivering=" + deliver + " - " + line);
						//	
						createLine (order, line, deliver, storages, true);
					}
					//	Manual
					else if (MOrder.DELIVERYRULE_Manual.equals(order.getDeliveryRule()))
						log.fine("Manual - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ ") - " + line);
					else
						log.fine("Failed: " + order.getDeliveryRule() + " - OnHand=" + onHand 
							+ " (Unconfirmed=" + unconfirmedShippedQty
							+ "), ToDeliver=" + toDeliver + " - " + line);
				}	//	for all order lines
				
				//	Complete Order successful
				if (completeOrder && MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule()))
				{
					for (int i = 0; i < lines.length; i++)
					{
						MOrderLine line = lines[i];
						if (line.getM_Warehouse_ID() != p_M_Warehouse_ID)
							continue;
						MProduct product = line.getProduct();
						BigDecimal toDeliver = line.getQtyOrdered().subtract(line.getQtyDelivered());
						//
						MStorage[] storages = null;
						if (product != null && product.isStocked())
						{
							MProductCategory pc = MProductCategory.get(order.getCtx(), product.getM_Product_Category_ID());
							String MMPolicy = pc.getMMPolicy();
							if (MMPolicy == null || MMPolicy.length() == 0)
								MMPolicy = client.getMMPolicy();
							//
							storages = MStorage.getWarehouse(getCtx(), 
								line.getM_Warehouse_ID(), 
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
								product.getM_AttributeSet_ID(),
								line.getM_AttributeSetInstance_ID()==0, minGuaranteeDate, 
								MClient.MMPOLICY_FiFo.equals(MMPolicy), null);
						}
						//	
						createLine (order, line, toDeliver, storages, false);
					}
				}
				m_line += 1000;
			}	//	while order
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_sql, e);
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
		completeShipment();
		return "@Created@ = " + m_created;
	}	//	generate
	
	
	
	/**************************************************************************
	 * 	Create Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qty qty
	 *	@param force force delivery
	 */
	private void createLine (MOrder order, MOrderLine orderLine, BigDecimal qty, 
		MStorage[] storages, boolean force)
	{
		//	Complete last Shipment - can have multiple shipments
		if (m_lastC_BPartner_Location_ID != orderLine.getC_BPartner_Location_ID() )
			completeShipment();
		m_lastC_BPartner_Location_ID = orderLine.getC_BPartner_Location_ID();
		//	Create New Shipment
		if (m_shipment == null)
		{
			m_shipment = new MInOut (order, 0, m_movementDate);
			m_shipment.setM_Warehouse_ID(orderLine.getM_Warehouse_ID());	//	sets Org too
			if (order.getC_BPartner_ID() != orderLine.getC_BPartner_ID())
				m_shipment.setC_BPartner_ID(orderLine.getC_BPartner_ID());
			if (order.getC_BPartner_Location_ID() != orderLine.getC_BPartner_Location_ID())
				m_shipment.setC_BPartner_Location_ID(orderLine.getC_BPartner_Location_ID());
			if (!m_shipment.save())
				throw new IllegalStateException("Could not create Shipment");
		}
		//	Non Inventory Lines
		if (storages == null)
		{
			
			MInOutLine line = new MInOutLine (m_shipment);
			line.setOrderLine(orderLine, 0, Env.ZERO);
			line.setQty(qty);	//	Correct UOM
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(qty
					.multiply(orderLine.getQtyEntered())
					.divide(orderLine.getQtyOrdered(), BigDecimal.ROUND_HALF_UP));
			line.setLine(m_line + orderLine.getLine());
			if (!line.save())
				throw new IllegalStateException("Could not create Shipment Line");
			log.fine(line.toString());
			return;
		}
		
		//	Product
		MProduct product = orderLine.getProduct();
		boolean linePerASI = false;
		if (product.getM_AttributeSet_ID() != 0)
		{
			MAttributeSet mas = MAttributeSet.get(getCtx(), product.getM_AttributeSet_ID());
			linePerASI = mas.isInstanceAttribute();
		}
		
		//	Inventory Lines
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		BigDecimal toDeliver = qty;
		for (int i = 0; i < storages.length; i++)
		{
			MStorage storage = storages[i];
			BigDecimal deliver = toDeliver;
			//	Not enough On Hand
			if (deliver.compareTo(storage.getQtyOnHand()) > 0 
				&& storage.getQtyOnHand().signum() >= 0)		//	positive storage
			{
				if (!force	//	Adjust to OnHand Qty  
					|| (force && i+1 != storages.length))	//	if force not on last location
					deliver = storage.getQtyOnHand();
			}
			if (deliver.signum() == 0)	//	zero deliver
				continue;
			int M_Locator_ID = storage.getM_Locator_ID();
			//
			MInOutLine line = null;
			if (!linePerASI)	//	find line with Locator
			{
				for (int ll = 0; ll < list.size(); ll++)
				{
					MInOutLine test = (MInOutLine)list.get(ll);
					if (test.getM_Locator_ID() == M_Locator_ID)
					{
						line = test;
						break;
					}
				}
			}
			if (line == null)	//	new line
			{
				line = new MInOutLine (m_shipment);
				line.setOrderLine(orderLine, M_Locator_ID, order.isSOTrx() ? deliver : Env.ZERO);
				line.setQty(deliver);
				list.add(line);
			}
			else				//	existing line
				line.setQty(line.getMovementQty().add(deliver));
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(line.getMovementQty().multiply(orderLine.getQtyEntered())
					.divide(orderLine.getQtyOrdered(), BigDecimal.ROUND_HALF_UP));
			line.setLine(m_line + orderLine.getLine());
			if (linePerASI)
				line.setM_AttributeSetInstance_ID(storage.getM_AttributeSetInstance_ID());
			if (!line.save())
				throw new IllegalStateException("Could not create Shipment Line");
			log.fine("ToDeliver=" + qty + "/" + deliver + " - " + line);
			toDeliver = toDeliver.subtract(deliver);
			if (toDeliver.signum() == 0)
				break;
		}		
		if (toDeliver.signum() != 0)
			throw new IllegalStateException("Not All Delivered - Remainder=" + toDeliver);
	}	//	createLine
		
	/**
	 * 	Complete Shipment
	 */
	private void completeShipment()
	{
		if (m_shipment != null)
		{
			//	Fails if there is a confirmation
			if (!m_shipment.processIt(p_docAction))
				log.warning("Failed: " + m_shipment);
			m_shipment.save();
			//
			addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null, m_shipment.getDocumentNo());
			m_created++;
		}
		m_shipment = null;
		m_line = 0;
	}	//	completeOrder
	
}	//	InOutGenerate
