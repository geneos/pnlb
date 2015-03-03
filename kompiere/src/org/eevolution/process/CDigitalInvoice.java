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
package org.eevolution.process;

import java.io.*;
import java.sql.*;
import java.math.*;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Enumeration;
import java.util.logging.*;
import java.lang.StringBuffer;
import java.security.*;
import java.security.Provider;
import java.security.cert.X509Certificate; 
import javax.crypto.spec.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import sun.misc.BASE64Encoder;
import org.w3c.dom.*;
import org.compiere.*;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *	Reads all Messages and stores them in a HashMap
 *
 *  @author     Oscar Gómez Islas
 *  @version    $Id: cfd.java,v 1.00 2005/06/08 05:02:59 ogomezi Exp $
 */
public final class CDigitalInvoice
{
	private CLogger				log = CLogger.getCLogger(getClass());
	
	// Lee Certificado Digital basado en el KeyStore
	public static X509Certificate getCertificate (Properties ctx, X_AD_Sequence eSequence)
	{
	   	try {
		    KeyStore eKStore = CDigitalInvoice.getKeyStoreNoFile(ctx,eSequence); //eee
		    String   eAlias  = getAliasKS(eKStore);
	    	X509Certificate certificate = (X509Certificate)eKStore.getCertificate(eAlias);
	    	return certificate;
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
			return null;
		}		
	}	// Terminate getCertificate

	
	//
	// Lee el Número de Serie del Certificado
	public static String getCertificateSN (Properties ctx, X_AD_Sequence eSequence)
	{
	    try{
	    	X509Certificate certificate = CDigitalInvoice.getCertificate(ctx,eSequence);
	    	BigInteger certificateSN = certificate.getSerialNumber();
	    	return certificateSN.toString();
		}catch (Exception e){ 
			e.printStackTrace(); 
			return ""; 
		}		
	}	// Terminate getCertificateSN
	
	
	//
	//
	// Lee el Certificado(KeyStore)
	public static KeyStore getKeyStoreNoFile (Properties ctx, X_AD_Sequence eSeq)
	{
	   	try {
	   	   	Security.addProvider(new BouncyCastleProvider());
	   		// Lee información del certificado(Ruta y Password)
			byte[] dataB = new byte[1024], dataE = new byte[1024];
	   		dataB = eSeq.getAttachment().getBinaryData();
	        dataE = eSeq.getAttachment().getEntryData(0);
	        //
			if (dataE.length == 0 || dataE == null){
				System.out.println("DataError");//log.debug("loadLOBData - ZipSize=" + data.length);
				return null;}
			//
	   		KeyStore ks = KeyStore.getInstance("PKCS12");
			String eCertPass = DB.getSQLValueString("E_Certificate","SELECT CertificatePass FROM E_Certificate WHERE AD_Sequence_ID=?",eSeq.getAD_Sequence_ID());
	   		char[] password =  eCertPass.toCharArray();
	   		//
	   		ByteArrayInputStream bai = new ByteArrayInputStream(dataE);  
	   		ks.load(bai, password);
	   		return ks;
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
			return null; 
		}		
	}	// Terminate getKeyStore		
	//
	//
	//
	
		
	
	//
	// Get Alias del KeyStore
	// Obten el Alias del Certificado, para poder leer la información
	public static String getAliasKS(KeyStore ks){
	    String ALIAS = "";
	    try {
		    // Buscar el ALIAS del certificado y llave Privada
			Enumeration en = ks.aliases();
			Vector vectaliases = new Vector();
			while (en.hasMoreElements())
				vectaliases.add(en.nextElement());
				String[] aliases = (String []) (vectaliases.toArray(new String[0]));
				for (int i = 0; i < aliases.length; i++)
					if (ks.isKeyEntry(aliases[i])) { ALIAS = aliases[i]; break; }
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace();  
		}		
			return ALIAS;
	}	// Terminate getAlias
	
	
	//
	// Genera Digestión y Firma de la Cadena Original(UTF8) y Regresala en BASE64
	public static String SignerChain(String eOriginalChain, Properties ctx, X_AD_Sequence eSeq)
	{
	   	try {
	   		// Lee Password y pasalo Char[]
			String eCertPass = DB.getSQLValueString("E_Certificate","SELECT CertificatePass FROM E_Certificate WHERE AD_Sequence_ID=?",eSeq.getAD_Sequence_ID());
	   		char[] ePassword = eCertPass.toCharArray(); 
	   		// Trae KeyStore y Alias
	   		KeyStore eKeyStore = CDigitalInvoice.getKeyStoreNoFile(ctx,eSeq); //eee
	   		String   eAlias    = CDigitalInvoice.getAliasKS(eKeyStore);
	   		// Obten la Llave Privada
	    	PrivateKey privatekey = (PrivateKey)eKeyStore.getKey(eAlias, ePassword);
			byte[] bOriginalChain = eOriginalChain.getBytes("UTF8");
			// Digestión con MD5 y Encrypta con RSA de Bouncy Castle 
			Signature sig = Signature.getInstance("MD5withRSA","BC"); 
			sig.initSign(privatekey); 
			sig.update(bOriginalChain);
			byte[] signature = sig.sign();
			// Regresa la Cadena Firmada a BASE64
	        String signatureB64 = new BASE64Encoder().encode(signature);
	        return signatureB64;
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
			return ""; 
		}		
	}	// Terminate Digestión y Firma
	
	
	//
	// Create XML to CFD(Comprobante Fiscal Digital) 
	public static File generateXML (Properties Ctx,String cSerie,String cFolio,String cFecha,String cSello,     // Maestros 
									String cSNCertify, String cAproba,String cPago,  
									String eRfc,String eNombre,String eCalle,String eExt,String eInt,String eColonia,String eLocalidad, // Emisor Fis
									String eReferencia, String eMunicipio,String eEstado,String ePais,String eCp,
									String eeCalle,String eeExt,String eeInt,String eeColonia,String eeLocalidad,String eeReferencia,   // Emisor Exp
									String eeMunicipio,String eeEstado,String eePais,String eeCp,
									String rRfc,String rNombre,String rCalle,String rExt,String rInt,String rColonia,String rLocalidad, // Receptor
									String rReferencia,String rMunicipio,String rEstado,String rPais,String rCp,
									MInvoiceLine[] eLines, MInvoiceTax[] eTaxes, X_AD_Sequence eSeq)
	{
    	// Lee certificado, trae la firma privada y  firma la cadena original
    	try {
    		// Crea la instancia del Documento
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.appendChild(document.createComment(Compiere.getSummaryAscii()));
			//	Datos principales de la factura
			Element element = document.createElement("Comprobante"); 
			document.appendChild(element);
				createElement(element,document,"serie",e(cSerie));
				createElement(element,document,"folio",e(cFolio));
				createElement(element,document,"fecha",e(cFecha));
				createElement(element,document,"sello",e(cSello));
				createElement(element,document,"noAprobacion",e(cAproba));
				createElement(element,document,"formaDePago",e(cPago));
				createElement(element,document,"noCertificado",e(cSNCertify));
				createElement(element,document,"certificado",e(CDigitalInvoice.getCertificate(Ctx,eSeq).toString()));
				// Datos del Emisor
				Element rEmisor = document.createElement ("Emisor");
				element.appendChild(rEmisor);
					createElement(rEmisor,document,"rfc",e(eRfc));
					createElement(rEmisor,document,"nombre",e(eNombre));
					Element rDFEmisor = document.createElement ("DomicilioFiscal");
					rEmisor.appendChild(rDFEmisor);
						createElement(rDFEmisor,document,"calle",e(eCalle));  // Dirección Fiscal
						createElement(rDFEmisor,document,"noExterior",e(eExt));
						createElement(rDFEmisor,document,"noInterior",e(eInt));
						createElement(rDFEmisor,document,"colonia",e(eColonia));
						createElement(rDFEmisor,document,"localidad",e(eLocalidad));
						createElement(rDFEmisor,document,"referencia",e(eReferencia));
						createElement(rDFEmisor,document,"municipio",e(eMunicipio));
						createElement(rDFEmisor,document,"estado",e(eEstado));
						createElement(rDFEmisor,document,"pais",e(ePais));
						createElement(rDFEmisor,document,"codigoPostal",e(eCp));
					Element rExpedidoEn = document.createElement ("ExpedidoEn");
					rEmisor.appendChild(rExpedidoEn);
						createElement(rExpedidoEn,document,"calle",e(eeCalle));  // Dirección de Emisión
						createElement(rExpedidoEn,document,"noExterior",e(eeExt));
						createElement(rExpedidoEn,document,"noInterior",e(eeInt));
						createElement(rExpedidoEn,document,"colonia",e(eeColonia));
						createElement(rExpedidoEn,document,"localidad",e(eeLocalidad));
						createElement(rExpedidoEn,document,"referencia",e(eeReferencia));
						createElement(rExpedidoEn,document,"municipio",e(eeMunicipio));
						createElement(rExpedidoEn,document,"estado",e(eeEstado));
						createElement(rExpedidoEn,document,"pais",e(eePais));
						createElement(rExpedidoEn,document,"codigoPostal",e(eeCp));
				// Datos del Receptor
				Element rReceptor = document.createElement ("Receptor");
				element.appendChild(rReceptor);
					createElement(rReceptor,document,"rfc",e(rRfc));
					createElement(rReceptor,document,"nombre",e(rNombre));
					Element rDFReceptor = document.createElement ("Domicilio");
					rReceptor.appendChild(rDFReceptor);
						createElement(rDFReceptor,document,"calle",e(rCalle));
						createElement(rDFReceptor,document,"noExterior",e(rExt));
						createElement(rDFReceptor,document,"noInterior",e(rInt));
						createElement(rDFReceptor,document,"colonia",e(rColonia));
						createElement(rDFReceptor,document,"localidad",e(rLocalidad));
						createElement(rDFReceptor,document,"referencia",e(rReferencia));
						createElement(rDFReceptor,document,"municipio",e(rMunicipio));
						createElement(rDFReceptor,document,"estado",e(rEstado));
						createElement(rDFReceptor,document,"pais",e(rPais));
						createElement(rDFReceptor,document,"codigoPostal",e(rCp));
				// Datos de los Conceptos
				Element rConceptos = document.createElement ("Conceptos");
				element.appendChild(rConceptos);
				for (int i = 0; i < eLines.length; i++)
				{
					String eDescript    = "";
					MInvoiceLine eLine  = eLines[i];
					MProduct eProduct   = MProduct.get(Ctx, eLine.getM_Product_ID());
					MCharge  eCharge    = new MCharge(Ctx,eLine.getC_Charge_ID(),null);
					MResource eResource = new MResource(Ctx,eLine.getS_ResourceAssignment_ID(),null);
					MUOM     eUOM       = MUOM.get(Ctx,eLine.getC_UOM_ID());
					// ¿ producto, cargo, recurso, descripción ?
					if(eLine.getM_Product_ID() != 0)
						eDescript = eProduct.getName();
						else if(eLine.getC_Charge_ID() != 0)
							eDescript = eCharge.getName();
						else if(eLine.getS_ResourceAssignment_ID() != 0)
							eDescript = eResource.getName();
						else eDescript = eLine.getDescription();
					// Atributo del Producto
						
					// Agrega conceptos a XML
					if(eLine.getQtyInvoiced().compareTo(Env.ZERO) != 0 & eLine.getLineNetAmt().compareTo(Env.ZERO) != 0) // Solo si hay cantidad y Total$
					{
						Element rConcepto = document.createElement ("Concepto");
						rConceptos.appendChild(rConcepto);
						
						// Existe Instancia de Productos, Verifica si son los datos de la Aduana(nombre,numero,fecha)
						if (eLine.getM_AttributeSetInstance_ID() > 0){
							MAttributeSetInstance eAttProduct = MAttributeSetInstance.get(Ctx,eLine.getM_AttributeSetInstance_ID(),eLine.getM_Product_ID());
							//
							if (eAttProduct.getDescription() != null)
							{
								String valorX    = "", eNumeroA="", eFechaA="", eAduanaA="", eday="", emonth="", eyear="", cadena="";
								StringBuffer sql = new StringBuffer();
								if(eAttProduct.getDescription().lastIndexOf("_") > 0) // Existe Lote y/o Serie
									cadena = eAttProduct.getDescription().substring(eAttProduct.getDescription().lastIndexOf("_")+1,eAttProduct.getDescription().length());
								else
									cadena = eAttProduct.getDescription();
								
								sql.append("SELECT att.Name, atu.M_Attribute_ID  FROM M_AttributeUse atu, M_Attribute att"+
						                     " WHERE M_AttributeSet_ID=(SELECT M_AttributeSet_ID FROM M_AttributeSetInstance " + 
											 " WHERE M_AttributeSetInstance_ID="+ eAttProduct.getM_AttributeSetInstance_ID() +")" +
						                     " AND atu.M_Attribute_ID=att.M_Attribute_ID");
								String finalSql = MRole.getDefault(Ctx, false).addAccessSQL(sql.toString(), "att", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
								finalSql        = finalSql + " ORDER By atu.SeqNo";
								//
								PreparedStatement pstmt = null;
								try	{
									pstmt = DB.prepareStatement(finalSql);
									ResultSet rs = pstmt.executeQuery();
									String cadenaX = cadena;
									int count = 1;
									while (rs.next()){
										if( rs.getString(1).equals("numero") || rs.getString(1).equals("fecha") || rs.getString(1).equals("aduana"));{
											for (int ii = 0; i < count; i++){
												if ( cadenaX.indexOf("-") > 0){
													valorX  = cadenaX.substring(0,cadenaX.indexOf("-"));
													cadenaX = cadenaX.substring(cadenaX.indexOf("-")+1,cadenaX.length());}
												else{
													valorX  = cadenaX;
												}
											}
											if (rs.getString(1).equals("numero"))
												eNumeroA = valorX;
											else if(rs.getString(1).equals("aduana"))
												eAduanaA = valorX;
											else if(rs.getString(1).equals("fecha")){
												eday    = valorX.substring(0,2);
												emonth  = valorX.substring(3,5);
												eyear   = valorX.substring(6,valorX.length());
												eFechaA = eyear+"-"+emonth+"-"+eday;}
										}
										count++;
									}
									rs.close();	pstmt.close(); pstmt = null;
								}
								catch (Exception e)
								{
									//log.error("doIt - " + finalSql, e);
								}
								Element rAduana = document.createElement ("Aduana");
								rConcepto.appendChild(rAduana);
								createElement(rAduana,document,"numero",eNumeroA);
								createElement(rAduana,document,"fecha" ,eFechaA);
								createElement(rAduana,document,"aduana",eAduanaA);
							}
						}
						createElement(rConcepto,document,"cantidad",eLine.getQtyInvoiced().toString());
						createElement(rConcepto,document,"unidad",eUOM.getName());
						createElement(rConcepto,document,"descripcion",eDescript == null ? "" : eDescript.trim());
						createElement(rConcepto,document,"valorUnitario",eLine.getPriceActual().toString());
						createElement(rConcepto,document,"importe",eLine.getLineNetAmt().toString());
					}
			    }
				// Impuestos (Retenciones y Traslados)
				BigDecimal iTaxChain  = Env.ZERO, iTaxHChain = Env.ZERO;
				// Agrega al document los impuestos
				Element rImpuestos = document.createElement ("Impuestos");
				element.appendChild(rImpuestos);
				Element rRetenciones = document.createElement ("Retenciones");
				rImpuestos.appendChild(rRetenciones);
				for (int i = 0; i < eTaxes.length; i++){ // Rretenciones
					MInvoiceTax eInvoiceTax = eTaxes[i];
					MTax        eTax        = MTax.get(Ctx, eInvoiceTax.getC_Tax_ID(),null);
					if(eTax.getRate().intValue() < 0){ // El Porcentaje es menor a 0: corresponde a una retensión de Impuesto
						Element rRetencion = document.createElement ("Retencion");
						rRetenciones.appendChild(rRetencion);						
						createElement(rRetencion,document,"impuesto",eTax.getName());
						createElement(rRetencion,document,"importe",iTaxHChain.add(eInvoiceTax.getTaxAmt()).toString() );						
				    }
				}
				Element rTraslados = document.createElement ("Traslados");
				rImpuestos.appendChild(rTraslados);
				System.err.println("================================= Cuantos Son: " + eTaxes.length);
				for (int e = 0; e < eTaxes.length; e++){ // Traslaciones
					MInvoiceTax eInvoiceTaxT = eTaxes[e];
					MTax        eTaxT        = MTax.get(Ctx, eInvoiceTaxT.getC_Tax_ID(),null);
					System.out.println("================================================== Que Número es: " + e);
					System.out.println("================================================" + eTaxT.getName() +" <> "+ eTaxT.getRate());
					if(eTaxT.getRate().intValue() >= 0){ // El Porcentaje es mayor a 0: corresponde a una retensión de Impuesto
						Element rTraslado = document.createElement ("Traslado");
						rTraslados.appendChild(rTraslado);
						createElement(rTraslado,document,"impuesto",eTaxT.getName());
						createElement(rTraslado,document,"importe",iTaxHChain.add(eInvoiceTaxT.getTaxAmt()).toString() );						
				    }
				}
				// Datos Adicionales
				Element rAddenda = document.createElement ("Addenda");
				element.appendChild(rAddenda);
				// Versión
				createElement(element,document,"version","1.0");
				// Genera el DOM
			DOMSource source = new DOMSource(document);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			//	Output
    		File out = new File(CDigitalInvoice.eTmp(null,"CFD"+cSerie+cFolio+".xml",3));
			out.createNewFile();
			// e-evolution
	        FileOutputStream streamOut = new FileOutputStream(out);
 	     	Writer writeOut = new OutputStreamWriter(streamOut, "UTF8");
			StreamResult result = new StreamResult(writeOut);			
			//	Transform
			transformer.transform (source, result);
			writeOut.close();
			return out;
    	} 
    	catch (Exception e) 
    	{ 
    		e.printStackTrace(); 
    		return null; 
    	}
	}	// Terminate XML
	
	
	//
	// Generate XML element child
	public static void createElement (Element row, Document document, String name, String value)
	{
		Element cElement = document.createElement (name);
		cElement.setAttribute("name", name); 
		cElement.appendChild(document.createTextNode(value)); 
		row.appendChild(cElement);
	 }
	
	//
	// Get the Street
	public static String getStreet (MLocation eLocation){
		String eCalle = "";
		if(eLocation == null || eLocation.getAddress1() == null) //if(eLocation.equals(null)){
			return eCalle;
		else{
			eCalle = eLocation.getAddress1();
			if (eLocation.getAddress1().indexOf("#") >= 0)
				eCalle = eLocation.getAddress1().substring(0,eLocation.getAddress1().indexOf("#"));
			return eCalle;
		}
	} // end getAdress1

	
	//
	// Get the Number Ext
	public static String getExt (MLocation eLocation){
		String eExt = "";
		if(eLocation == null || eLocation.getAddress1() == null) //if(eLocation.equals(null))
		       return eExt;
		else{
			if (eLocation.getAddress1().indexOf("#") >=  0 ){
				eExt = eLocation.getAddress1().substring(eLocation.getAddress1().indexOf("#")+1,eLocation.getAddress1().length());
				if (eExt.indexOf("i#") >= 0)
					eExt = eExt.substring(1,eExt.indexOf("i#"));
			}
			return eExt;
	    }
	} // End getAdress1

	
	//
	// Get the Street
	public static String getInt (MLocation eLocation){
		String eInt = "";
		if(eLocation == null || eLocation.getAddress1() == null)  //if(eLocation.equals(null))
	       return eInt;
	    else{
			if (eInt.indexOf("i#") >= 0)
				eInt = eLocation.getAddress1().substring(eLocation.getAddress1().indexOf("i#"),eLocation.getAddress1().length());
			return eInt;
	    }
	} // End getAdress1

	
	//
	// Valida que el valor String a incluir para el XMl no sea NULL
	public static String e(String eCadena){
	    if(eCadena == null)
	        return "";
	        else
	            return eCadena;
	} // End String Null
	
	
	//
	// Create a File TMP
	public static String eTmp (byte[] dataE, String file, int eAccion)
	{
	    String efile = System.getProperty("user.dir") + System.getProperty("file.separator");
   	    FileOutputStream fos;
   	    file = efile + file;
        File tempFile = new File(file);
        // Create / delete
        try {
            if (eAccion == 1){
    			tempFile.createNewFile();			
				fos = new FileOutputStream(file);
				fos.write(dataE);
				fos.close();
                Process p = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tempFile + "\"");
                p.waitFor();                    
            } else if (eAccion == 2) { //Delete
                if(tempFile.exists())
                	tempFile.delete();
            }
        } catch (Exception E) {
            E.printStackTrace();
            return "";
        }
 	    return file;
	} // Terminate eTmp
}