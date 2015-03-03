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

import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.model.*;
 
/**
 *	Create (Generate) Invoice from Shipment
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutCreateInvoice.java,v 1.9 2005/09/19 04:49:45 jjanke Exp $
 */
public class InOutCreateInvoice extends SvrProcess
{
	/**	Shipment					*/
	private int 	p_M_InOut_ID = 0;
	/**	Price List Version			*/
	private int		p_M_PriceList_ID = 0;
	/* Document No					*/
	private String	p_InvoiceDocumentNo = null;
	
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
			else if (name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = para[i].getParameterAsInt();
			else if (name.equals("InvoiceDocumentNo"))
				p_InvoiceDocumentNo = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_InOut_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Create Invoice.
	 *	@return document no
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{


                /*
                ** Vit4B - Agregado para tomar el remito en formato 1-2 y pasarlo
                ** a formato 001-00000002
                ** 26/02/2007
                */
                
                if(p_InvoiceDocumentNo == "" || p_InvoiceDocumentNo == null)
                {
                    JOptionPane.showMessageDialog(null,"Ingrese Número de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return "";
                }

                int indexOf = p_InvoiceDocumentNo.indexOf("-");

                if(indexOf == -1 || indexOf == 0 || indexOf == p_InvoiceDocumentNo.length())
                {
                    JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return "";
                }

                String sucursal = p_InvoiceDocumentNo.substring(0,indexOf);
                String nro = p_InvoiceDocumentNo.substring(indexOf+1,p_InvoiceDocumentNo.length());

                switch (sucursal.length()) {
                  case 1:
                        sucursal = "000" + sucursal;
                        break;
                  case 2:
                        sucursal = "00" + sucursal;
                        break;
                  case 3:
                        sucursal = "0" + sucursal;
                        break;
                  case 4:
                        break;
                  default:
                        JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return "";
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
                        JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return "";
                }

                p_InvoiceDocumentNo = sucursal + "-" + nro;



		log.info("M_InOut_ID=" + p_M_InOut_ID 
			+ ", M_PriceList_ID=" + p_M_PriceList_ID
			+ ", InvoiceDocumentNo=" + p_InvoiceDocumentNo);
		if (p_M_InOut_ID == 0)
			throw new IllegalArgumentException("No Shipment");
		//
		MInOut ship = new MInOut (getCtx(), p_M_InOut_ID, null);
		if (ship.get_ID() == 0)
			throw new IllegalArgumentException("Shipment not found");
		if (!MInOut.DOCSTATUS_Completed.equals(ship.getDocStatus()))
			throw new IllegalArgumentException("Shipment not completed");
		
		MInvoice invoice = new MInvoice (ship, null);
		if (p_M_PriceList_ID != 0)
			invoice.setM_PriceList_ID(p_M_PriceList_ID);
		if (p_InvoiceDocumentNo != null && p_InvoiceDocumentNo.length() > 0)
			invoice.setDocumentNo(p_InvoiceDocumentNo);
		if (!invoice.save())
			throw new IllegalArgumentException("Cannot save Invoice");
		MInOutLine[] shipLines = ship.getLines(false);
		for (int i = 0; i < shipLines.length; i++)
		{
			MInOutLine sLine = shipLines[i];
			MInvoiceLine line = new MInvoiceLine(invoice);
			line.setShipLine(sLine);
			line.setQtyEntered(sLine.getQtyEntered());
			line.setQtyInvoiced(sLine.getMovementQty());
			if (!line.save())
				throw new IllegalArgumentException("Cannot save Invoice Line");
		}
		
		return invoice.getDocumentNo();
	}	//	InOutCreateInvoice
	
}	//	InOutCreateInvoice
