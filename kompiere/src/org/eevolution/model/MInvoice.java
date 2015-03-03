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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.model.*;
//Begin e-Evolution ogi-cd CFD
import java.security.cert.X509Certificate; 
import java.security.cert.CertificateFactory;
import javax.xml.transform.stream.StreamResult;

import org.eevolution.process.*;
//End e-Evolution ogi-cd CFD


/**
 *	Invoice Model.
 * 	Please do not set DocStatus and C_DocType_ID directly. 
 * 	They are set in the process() method. 
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MInvoice.java,v 1.105 2006/01/04 05:39:20 jjanke Exp $
 */
public class MInvoice extends org.compiere.model.MInvoice implements DocAction
{

	
	
	/**************************************************************************
	 * 	Invoice Constructor
	 * 	@param ctx context
	 * 	@param C_Invoice_ID invoice or 0 for new
	 * 	@param trxName trx name
	 */
	public MInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		super (ctx, C_Invoice_ID, trxName);
		
	}	//	MInvoice

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MInvoice (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoice

	/**
	 * 	Create Invoice from Order
	 *	@param order order
	 *	@param C_DocTypeTarget_ID target document type
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate)
	{
		super( order, C_DocTypeTarget_ID, invoiceDate);
	}	//	MInvoice
	
	/**
	 * 	Create Invoice from Shipment
	 *	@param ship shipment
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MInOut ship, Timestamp invoiceDate)
	{
		super(ship,invoiceDate);
		setC_DocTypeTarget_ID (ship);
	}	
	
	/**
	 * 	Set Target Document Type.
	 * 	Based on SO flag AP/AP Invoice
	 */
	public void setC_DocTypeTarget_ID (MInOut ship)
	{
		MDocType doc = MDocType.get(ship.getCtx(),ship.getC_DocType_ID());
		if(doc.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialDelivery))
		{
			if (isSOTrx())
				setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_ARInvoice);
			else
				setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APCreditMemo);
		}
		if(doc.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialReceipt))
		{
			if (isSOTrx())
				setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_ARCreditMemo);
			else
				setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APInvoice);		  
		}		
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Create Invoice from Batch Line
	 *	@param batch batch
	 *	@param line batch line
	 */
	public MInvoice (MInvoiceBatch batch, MInvoiceBatchLine line)
	{
		super(batch,line);
	}	//	MInvoice
	
		
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		boolean result = super.beforeSave(newRecord);
		
		// Begin e-evolution ogi-cd Save to Date/hour/minute/second in to DateInvoiced
		/*if(getDocAction() == "CL" && getDocStatus() == "CO")
			setDateInvoiced(new Timestamp(System.currentTimeMillis()));
		if(getDocAction() == "" && getDocStatus() == "")
			setDateInvoiced(new Timestamp(System.currentTimeMillis()));*/
		// End e-Evolution ogi-cd 
		return result;
	}	//	beforeSave
	


	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		String status = super.completeIt();
		setDocStatus(status);
		createCFD();		
			
		if (status.equals(DocAction.STATUS_Completed) && isSOTrx())
		{
			try {
				
				/*
				 * 01-04-2011 Camarzana Mariano
				 * Se agrego la invocacion al metodo save dado que no estaba actualizando 
				 * correctamente el estado de la Factura, debido a esto al querer imprimirla no la estaba 
				 * recuperando desde la	vista rv_fact_client_header de modo tal que no realizaba la impresion
				 * (Esto sucedia al completar desde Factura y no de orden de Venta) 
				 */
				save(get_TrxName());
				
				
				Trx trx = Trx.get(get_TrxName(), false);
				trx.commit();
				/*
				 * 17-01-2011 Camarzana Mariano
				 * Ticket 118 (Los ajustes al d閎ito y al cr閐ito no es necesario que se impriman autom醫icamente)
				 */
				int docTypeTargetId = getC_DocTypeTarget_ID();
				MDocType doctype = new MDocType(Env.getCtx(),docTypeTargetId,null);
				
				if (!doctype.getPrintName().contains("AJC") &&	!doctype.getPrintName().contains("AJD")){
					ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
					re.print();
				}
			}
			catch (Exception e)
			{	
				e.printStackTrace();
				return DocAction.STATUS_Invalid;
			}
		}
			
		return status;
	}	//	completeIt

	/**
	 * 	Reverse Correction - same date
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{		
		boolean result = super.reverseCorrectIt();
		
		//int C_Invoice_ID = DB.getSQLValue(get_TrxName(),"SELECT C_Invoice_ID FROM C_Invoice i WHERE i.AD_Client_ID = " + getAD_Client_ID() + " AND DocumentNo='" + getDocumentNo() + "'");
		//int C_Invoice_ID = DB.getSQLValue(get_TrxName(),"SELECT DISTINCT  C_Invoice_ID FROM C_Invoice i WHERE i.AD_Client_ID = " + getAD_Client_ID() 
		//		+ " AND DocumentNo='" + getDocumentNo() + "' ORDER BY C_Invoice_ID DESC");
		// Begin e-Evolution ogi-cd -> Cancela Documento, coloca el mismo DocumentNo + "'"
		//MInvoice i = new MInvoice(this.getCtx(),C_Invoice_ID, get_TrxName());
		//i.setDocumentNo(getDocumentNo() + "-");
		//addDescription("(" + i.getDocumentNo() + "<-)");
		//i.save(get_TrxName());
		
		// End e-Evolution ogi-cd

		// Begin e-Evolution ogi-cd -> Cancelaci贸n de Facturas, no consuma secuencia(folio fiscal)
		/*	MDocType  eDocType = MDocType.get(getCtx(),getC_DocType_ID());
			MSequence eSeq     = new MSequence(getCtx(),eDocType.getDocNoSequence_ID(),null);
			eSeq.setCurrentNext(eSeq.getCurrentNext());
			eSeq.save(get_TrxName());
		*/	
		// End e-Evolution ogi-cd
		
		setIsPaid(true); // e-Evolution  ogi-cd paid when Reserved

		
		
		return result;
	}
				
	//	 Begin e-Evolution ogi-cd -> Create CFD
	public void createCFD()
	{
		// Define Variable de datos para la cadena original 
		String cc, c, cOriginalChain, eOriginalChain,  cSerie, cFolio, cFecha, cSNCertify, cAproba, cPago, eRfc, eNombre, eCalle,  eExt,  eInt,  eColonia,
						eLocalidad,  eReferencia,  eMunicipio, eEstado,  ePais,  eCp,
						rRfc, rNombre, rCalle,  rExt,  rInt,  rColonia,  rLocalidad,  rReferencia,  rMunicipio, rEstado,  rPais,  rCp;
		int nDocument, sqlEmite; 
		MLocation eLemite = null;
		// Tipo de Documento y Secuencia de Folios
		MDocType      eDocType  = MDocType.get(getCtx(),getC_DocType_ID());
		X_AD_Sequence eSequence = new X_AD_Sequence(getCtx(),eDocType.getDocNoSequence_ID(),null);
		//
		// Validaci贸n (solo completar facturas, solo facturas de venta, secuencia tenga attachment con extenci贸n .p12
		if (!getDocAction().equals("CO") || !isSOTrx() || eSequence.getAttachmentData(".p12") == null || getDocumentNo().indexOf("'") >= 0)
		    return;
		//
		// Informaci贸n de la Organizaci贸n
		MClient   eClient     = MClient.get(getCtx(),getAD_Client_ID());
		MOrg      eOrg        = MOrg.get(getCtx(),getAD_Org_ID());
		MOrgInfo  eOrgInfo    = MOrgInfo.get(getCtx(),getAD_Org_ID());
		MLocation eLOrg       = MLocation.get(getCtx(),eOrgInfo.getC_Location_ID(),null);
		// Informaci贸n del Socio de Negocio 
		MBPartner eBPartner   = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		MBPartnerLocation eL  = new MBPartnerLocation(getCtx(), getC_BPartner_Location_ID(), null); 
		MLocation eLBP        = MLocation.get(getCtx(),eL.getC_Location_ID(),null);
		// Informaci贸n
		cFolio     = getDocumentNo().substring(getDocumentNo().indexOf(eSequence.getPrefix())+1);
		nDocument  = Integer.parseInt(cFolio);
		//
        String sql = "SELECT AutorizationNumber FROM E_CtrlSequence WHERE AD_Sequence_ID=" + 
						eSequence.getAD_Sequence_ID()+"AND " + nDocument + " > SequenceBegin-1 AND ? < SequenceEnd+1";
		cAproba    = DB.getSQLValueString("E_CtrlSequence",sql,nDocument);  
		// Informaci贸n de la Direcci贸n de Emisi贸n del Documento, solo cuando tenga Direcci贸n
		sqlEmite   = DB.getSQLValue("E_Certificate","SELECT E_Location_ID FROM E_Certificate WHERE AD_Sequence_ID=?",
		        							eSequence.getAD_Sequence_ID());		
		// Recopila Informaci贸n principal
		cc          = "||";
		c           = "|";
		cSerie      = eSequence.getPrefix();
		cFecha      = getDateInvoiced().toString();
		cSNCertify  = CDigitalInvoice.getCertificateSN(getCtx(),eSequence); 
		cPago       = getPaymentRule().toString();
		// Datos del Emisor, Direcci贸n Fiscal
		eRfc        = eOrgInfo.getTaxID() == null ? "" : eOrgInfo.getTaxID().trim();
		eNombre     = eClient.getName() == null ? "" : eClient.getName();
		eCalle      = CDigitalInvoice.getStreet(eLOrg);
		eExt        = CDigitalInvoice.getExt(eLOrg);
		eInt        = CDigitalInvoice.getInt(eLOrg);
		eColonia    = eLOrg.getAddress2() == null ? "" : eLOrg.getAddress2();
		eLocalidad  = eLOrg.getAddress3() == null ? "" : eLOrg.getAddress3();
		eReferencia = eLOrg.getAddress4() == null ? "" : eLOrg.getAddress4();
		eMunicipio  = eLOrg.getCity() == null ? "" : eLOrg.getCity();
		eEstado     = eLOrg.getRegion() == null || eLOrg.getRegion().getDescription() == null ? "" : eLOrg.getRegion().getDescription();
		ePais       = eLOrg.getCountryName() == null ? "" : eLOrg.getCountryName();
		eCp         = eLOrg.getPostal() == null ? "" : eLOrg.getPostal();
		// La direcci贸n de emisi贸n se toma la misma que la de la organizaci贸n, antes de evaluar si existe una direcci贸n de emisi贸n
		String eeCalle=eCalle, eeExt=eExt, eeInt=eInt, eeColonia=eColonia, eeLocalidad=eLocalidad, eeReferencia=eReferencia,
				eeMunicipio=eMunicipio, eeEstado=eEstado, eePais=ePais, eeCp=eCp;
		// Datos del Emisor, Direcci贸n de Expedici贸n
		if (sqlEmite >= 0){ // Existe direcci贸n de emitir?
			eLemite      = MLocation.get(getCtx(),sqlEmite,null);
			eeCalle      = CDigitalInvoice.getStreet(eLemite);
			eeExt        = CDigitalInvoice.getExt(eLemite);
			eeInt        = CDigitalInvoice.getInt(eLemite);
			eeColonia    = eLemite.getAddress2() == null ? "" : eLemite.getAddress2();
			eeLocalidad  = eLemite.getAddress3() == null ? "" : eLemite.getAddress3();
			eeReferencia = eLemite.getAddress4() == null ? "" : eLemite.getAddress4(); 
			eeMunicipio  = eLemite.getCity() == null ? "" : eLemite.getCity();
			eeEstado     = eLemite.getRegion() == null || eLemite.getRegion().getDescription() == null ? "" : eLemite.getRegion().getDescription();
			eePais       = eLemite.getCountryName() == null ? "" : eLemite.getCountryName();
			eeCp         = eLemite.getPostal() == null ? "" : eLemite.getPostal(); 
		}
		// Datos del Cliente, Domicilio Fiscal
		rRfc        = eBPartner.getTaxID() == null ? "" : eBPartner.getTaxID();
		rNombre     = eBPartner.getName() == null ? "" : eBPartner.getName();
		rCalle      = CDigitalInvoice.getStreet(eLBP);
		rExt        = CDigitalInvoice.getExt(eLBP);
		rInt        = CDigitalInvoice.getInt(eLBP);
		rColonia    = eLBP.getAddress2() == null ? "" : eLBP.getAddress2();
		rLocalidad  = eLBP.getAddress3() == null ? "" : eLBP.getAddress3();
		rReferencia = eLBP.getAddress4() == null ? "" : eLBP.getAddress4();
		rMunicipio  = eLBP.getCity() == null ? "" : eLBP.getCity();
		rEstado     = eLBP.getRegion() == null || eLBP.getRegion().getDescription() == null ? "" : eLBP.getRegion().getDescription();
		rPais       = eLBP.getCountryName() == null ? "" : eLBP.getCountryName(); 
		rCp         = eLBP.getPostal() == null ? "" : eLBP.getPostal();
		// Agrega datos a la Cadena Original(datos maestros, emisor,receptor)
		cOriginalChain  = cc + cSerie +c+ cFolio +c+ cFecha +c+ cAproba +c+ cPago +c+ eRfc +c+ eNombre +c+ eCalle +c+ eExt +c+ eInt +c+ eColonia +c+ eLocalidad +c+ eReferencia +c+ 
								eMunicipio +c+ eEstado +c+ ePais +c+ eCp +c+
								eeCalle +c+ eeExt +c+ eeInt +c+ eeColonia +c+ eeLocalidad +c+ eeReferencia +c+ eeMunicipio +c+ eeEstado +c+ eePais +c+ eeCp +c+
				                rRfc +c+ rNombre +c+ rCalle +c+ rExt +c+ rInt +c+ rColonia +c+ rLocalidad +c+ rReferencia +c+ rMunicipio +c+ rEstado +c+ rPais +c+ rCp;
		// Agrega informaci贸n de Lineas(Conceptos)
		MInvoiceLine[] eLines = getLines(false);
		for (int i = 0; i < eLines.length; i++)
		{
			String eDescript    = "";
			MInvoiceLine eLine  = eLines[i];
			MProduct eProduct   = MProduct.get(getCtx(), eLine.getM_Product_ID());
			MCharge  eCharge    = new MCharge(getCtx(),eLine.getC_Charge_ID(),null);
			MResource eResource = new MResource(getCtx(),eLine.getS_ResourceAssignment_ID(),null);
			MUOM     eUOM       = MUOM.get(getCtx(),eLine.getC_UOM_ID());
			// 驴 producto, cargo, recurso, descripci贸n ?
			if(eLine.getM_Product_ID() != 0)
				eDescript = eProduct.getName();
				else if(eLine.getC_Charge_ID() != 0)
					eDescript = eCharge.getName();
				else if(eLine.getS_ResourceAssignment_ID() != 0)
					eDescript = eResource.getName();
				else eDescript = eLine.getDescription();
			// Agrega conceptos a Cadena Original
			cOriginalChain = cOriginalChain +c+ eLine.getQtyInvoiced() +c+ eUOM.getName() +c+ eDescript +
			                                 c+ eLine.getPriceActual() +c+   eLine.getLineNetAmt();
	    }
		// Agrega Informaci贸n de Impuestos(Retenciones & Traslados)
		String eTaxChain  = ""; // cadena traslados
		String eTaxHChain = ""; // cadena retenciones
		MInvoiceTax[] eTaxes = getTaxes(false);
		for (int i = 0; i < eTaxes.length; i++)
		{
			MInvoiceTax eInvoiceTax = eTaxes[i];
			MTax        eTax        = MTax.get(getCtx(), eInvoiceTax.getC_Tax_ID(),null);
			// 驴 retenci贸n, traslado?
			String eIndicator = eTax.getTaxIndicator();
			if(eTax.isRequiresTaxCertificate())
				eTaxHChain = c+ eTax.getName() +c+ eInvoiceTax.getTaxAmt() ;
			else
				eTaxChain  = c+ eTax.getName() +c+ eInvoiceTax.getTaxAmt() ;
	    }
		// Agrega impuestos a Cadena Original
		cOriginalChain = cOriginalChain + eTaxHChain + eTaxChain + cc;
		// Firma de la Cadena Original(UTF8), mostrado en Base64
		eOriginalChain = CDigitalInvoice.SignerChain(cOriginalChain,getCtx(),eSequence).toString();
		// Crea XML
		File eXML = CDigitalInvoice.generateXML( getCtx(), cSerie, cFolio, cFecha, eOriginalChain, cSNCertify, cAproba, cPago,
			       				eRfc, eNombre, eCalle, eExt, eInt, eColonia, eLocalidad, eReferencia, eMunicipio, eEstado, ePais, eCp,
								eeCalle, eeExt, eeInt, eeColonia, eeLocalidad, eeReferencia, eeMunicipio, eeEstado, eePais, eeCp,
								rRfc, rNombre, rCalle, rExt, rInt, rColonia, rLocalidad, rReferencia, rMunicipio, rEstado, rPais, rCp,
								eLines, eTaxes,eSequence);
		// Generate Attachment y AttachmentNote para la Factura Actual
		MAttachment eAttach = new MAttachment(getCtx(), 318, getC_Invoice_ID(), null);
		eAttach.setTitle("CFD");
		eAttach.addEntry(new File (CDigitalInvoice.eTmp(null,"CFD"+cSerie+cFolio+".xml",3)));
		eAttach.addTextMsg("XML");
		//eAttach.setORIGINALCHAIN(cOriginalChain); //Campo agregado a AD_Attachment
		eAttach.save();
		MAttachmentNote eAttNote = new MAttachmentNote(eAttach,cSNCertify,eOriginalChain.toString());
		eAttNote.save();
		CDigitalInvoice.eTmp(null,"CFD"+cSerie+cFolio+".xml",2);
		//
		CDigitalInvoice.eTmp(null,"OChain"+cSerie+cFolio+".txt",2);
	} // End e-Evolution ogi-cd CFD Jun/08/2005
	
	public void setC_DocTypeTarget_ID ()
	{
		super.setC_DocTypeTarget_ID ();
		
		if (getC_DocTypeTarget_ID() > 0)
			return;
		if (isSOTrx())
			setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_ARInvoice);
		else
			setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APInvoice);
	}	//	setC_DocTypeTarget_ID
	
}	//	MInvoice
