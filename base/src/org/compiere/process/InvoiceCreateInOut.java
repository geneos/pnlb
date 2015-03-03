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

import org.compiere.util.*;
import org.compiere.model.*;
 
/**
 *	Create (Generate) Shipment from Invoice
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoiceCreateInOut.java,v 1.12 2005/09/19 04:49:45 jjanke Exp $
 */
public class InvoiceCreateInOut extends SvrProcess
{
	/**	Warehouse			*/
	private int p_M_Warehouse_ID = 0;
	/** Invoice				*/
	private int p_C_Invoice_ID = 0;
        private String DocumentNo = "";

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
			else if (name.equals("DocumentNo"))
				DocumentNo = para[i].getParameter().toString();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Invoice_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		
                /*
                ** Vit4B - Agregado para tomar el remito en formato 1-2 y pasarlo
                ** a formato 001-00000002
                ** 26/02/2007
                */
                
                if(DocumentNo == "" || DocumentNo == null)
                {
                    JOptionPane.showMessageDialog(null,"Ingrese Número de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return "";
                }

                int indexOf = DocumentNo.indexOf("-");

                if(indexOf == -1 || indexOf == 0 || indexOf == DocumentNo.length())
                {
                    JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return "";
                }

                String sucursal = DocumentNo.substring(0,indexOf);
                String nro = DocumentNo.substring(indexOf+1,DocumentNo.length());

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

                DocumentNo = sucursal + "-" + nro;
                
                log.info("C_Invoice_ID=" + p_C_Invoice_ID 
			+ ", M_Warehouse_ID=" + p_M_Warehouse_ID);
		if (p_C_Invoice_ID == 0)
			throw new IllegalArgumentException("@NotFound@ @C_Invoice_ID@");
		if (p_M_Warehouse_ID == 0)
			throw new IllegalArgumentException("@NotFound@ @M_Warehouse_ID@");
		//
		MInvoice invoice = new MInvoice (getCtx(), p_C_Invoice_ID, null);
		if (invoice.get_ID() == 0)
			throw new IllegalArgumentException("@NotFound@ @C_Invoice_ID@");
		if (!MInvoice.DOCSTATUS_Completed.equals(invoice.getDocStatus()))
			throw new IllegalArgumentException("@InvoiceCreateDocNotCompleted@");
		//

                /*
                **  Vit4B - 10/04/2007 Modificación para que tome DocumentNo
                **
                */

                //MInOut ship = new MInOut (invoice, 0, null, p_M_Warehouse_ID);
                
                MInOut ship = new MInOut (invoice, 0, null, p_M_Warehouse_ID, DocumentNo);


		if (!ship.save())
			throw new IllegalArgumentException("@SaveError@ Receipt");
		//
		MInvoiceLine[] invoiceLines = invoice.getLines(false);
		for (int i = 0; i < invoiceLines.length; i++)
		{
			MInvoiceLine invoiceLine = invoiceLines[i];
			MInOutLine sLine = new MInOutLine(ship);
			sLine.setInvoiceLine(invoiceLine, 0,	//	Locator 
				invoice.isSOTrx() ? invoiceLine.getQtyInvoiced() : Env.ZERO);
			sLine.setQtyEntered(invoiceLine.getQtyEntered());
			sLine.setMovementQty(invoiceLine.getQtyInvoiced());
			if (invoice.isCreditMemo())
			{
				sLine.setQtyEntered(sLine.getQtyEntered().negate());
				sLine.setMovementQty(sLine.getMovementQty().negate());
			}
			if (!sLine.save())
				throw new IllegalArgumentException("@SaveError@ @M_InOutLine_ID@");
			//
			invoiceLine.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
			if (!invoiceLine.save())
				throw new IllegalArgumentException("@SaveError@ @C_InvoiceLine_ID@");
		}
		
		return ship.getDocumentNo();
	}	//	doIt
	
}	//	InvoiceCreateInOut
