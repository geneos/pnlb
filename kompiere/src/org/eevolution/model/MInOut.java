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
package org.eevolution.model;

import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

import org.compiere.model.MDocType;
import org.compiere.model.MSequence;
import org.compiere.model.MWarehouse;
import org.compiere.print.ReportEngine;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Shipment Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInOut.java,v 1.78 2006/01/04 05:39:20 jjanke Exp $
 */
public class MInOut extends org.compiere.model.MInOut implements DocAction
{
	
		
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOut_ID
	 *	@param trxName rx name
	 */
	public MInOut (Properties ctx, int M_InOut_ID, String trxName)
	{
		super(ctx,M_InOut_ID,trxName);
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
		super(order,C_DocTypeShipment_ID,movementDate);
	}	//	MInOut

	/**
	 * 	Invoice Constructor - create header only
	 *	@param invoice invoice
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MInvoice invoice, int C_DocTypeShipment_ID, Timestamp movementDate, int M_Warehouse_ID)
	{
		super(invoice,C_DocTypeShipment_ID,movementDate,M_Warehouse_ID);
	}	//	MInOut
	
	/**
	 * 	Copy Constructor - create header only
	 *	@param original original 
	 *	@param movementDate optional movement date (default today)
	 *	@param C_DocTypeShipment_ID document type or 0
	 */
	public MInOut (MInOut original, int C_DocTypeShipment_ID, Timestamp movementDate)
	{
		super(original,C_DocTypeShipment_ID,movementDate);
	}	//	MInOut	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Warehouse Org

        if (!checkFechas())
            return false;


                /*
                ** Vit4B - Agregado para tomar el remito en formato 1-2 y pasarlo
                ** a formato 001-00000002
                ** 26/02/2007
                */


                String documentNo = this.getDocumentNo();
               
                MDocType doc = MDocType.get(getCtx(),getC_DocType_ID());		
		
                //	Shipment - Needs Order
		
                /**
                 * BISion - 03/07/2009 - Santiago Ibañez
                 * 
                 */
                if (doc.getDocBaseType().equals(MDocType.DOCBASETYPE_RecepcionMaterialesTerceros)||
                    doc.getDocBaseType().equals(MDocType.DOCBASETYPE_DevolucionMaterialesTerceros))
		{
                    if(documentNo == "" || documentNo == null)
                    {
                        JOptionPane.showMessageDialog(null,"Ingrese Numero de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }

                    int indexOf = documentNo.indexOf("-");

                    if(indexOf == -1 || indexOf == 0 || indexOf == documentNo.length())
                    {
                        JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return false;
                    }

                    String sucursal = documentNo.substring(0,indexOf);
                    String nro = documentNo.substring(indexOf+1,documentNo.length());

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
                            JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Info", JOptionPane.INFORMATION_MESSAGE);
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
                            JOptionPane.showMessageDialog(null,"Numero de Documento Invalido", "Info", JOptionPane.INFORMATION_MESSAGE);
                            return false;
                    }

                    this.setDocumentNo(sucursal + "-" + nro);
		}
        else
        {
        	/**
			 *  	AGREGADO PARA VERIFICAR FORMATO DE DOCUMENTNO
			 *
			 * 		13/03/2009
			 * 
            */
            
            if (getC_DocType_ID() != 5000046) {
                    if(documentNo != null && !documentNo.equals("")) {
                    /*
                     *      Si el numero es definido por el sistema automaticamente le asigna <>
                     *      en este caso se debe reformatear para anexarle los ceros y aumentar 
                     *      el numerador a mano ya que el sistema pierde la referencia autom�tica.
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
                else {
                    JOptionPane.showMessageDialog(null,"No ingreso Numero de Documento", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }
            //Para devolucion de Prod terminado la secuencia se genera al completar
            else {
                if (getDocStatus().equals(DOCSTATUS_Drafted))
                    setDocumentNo("PENDIENTE");
            }
        }

		if (newRecord)
		{
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (wh.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("WarehouseOrgConflict", "");
				return false;
			}
		}
		
                // MDocType doc = MDocType.get(getCtx(),getC_DocType_ID());		
		
                //	Shipment - Needs Order
                //BISion - 16/07/2009 - Santiago Ibañez
                //Comentado para que no requiera la Orden de Venta.
		
/*                if (isSOTrx() && getC_Order_ID() == 0 &&  doc.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialDelivery))
		{
			log.saveError("FillMandatory", Msg.translate(getCtx(), "C_Order_ID"));
			return false;
		}*/
		return true;
	}	//	beforeSave

    /** BISion - 17/11/2009 - Santiago Ibañez
     * COM-PAN-REQ-10.001.01
     * Metodo creado para verificar que la fecha de entrega sea mayor o igual
     * a la fecha de movimiento.
     * @return
     */
    private boolean checkFechas(){
        if (getMovementDate()!=null && getShipDate()!=null && getMovementDate().compareTo(getShipDate())>0){
            JOptionPane.showMessageDialog(null,"La Fecha de Entrega debe ser posterior o igual a la Fecha de Movimiento","Fecha invalida",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public String completeIt()
	{
		
		String STATUS = super.completeIt();
		
		if (STATUS.equals(DocAction.STATUS_Completed) && isSOTrx())
		{
			try {
                                if (getDocumentNo().equals("PENDIENTE")) {
                                    MDocType docType = MDocType.get(getCtx(), this.getC_DocType_ID());
                                    MSequence seq = new MSequence(getCtx(),docType.getDocNoSequence_ID(), null);
                                    setDocumentNo(seq.getDocumentNo());
                                    seq.save(get_TrxName());
                                }
                            
				Trx trx = Trx.get(get_TrxName(), false);
				trx.commit();
				
				ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.SHIPMENT, getM_InOut_ID());
				re.print();
			}
			catch (Exception e)
			{	
				e.printStackTrace();
				return DocAction.STATUS_Invalid;
			}
		}
		
		return STATUS;
	}	// completeIt

}	//	MInOut
