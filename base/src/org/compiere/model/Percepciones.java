package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.compiere.util.*;

public class Percepciones extends CalloutEngine 
{
	
	//	FIN IVA
	BigDecimal periva (BigDecimal TotalLines, BigDecimal LimiteMinimo)
	{
		BigDecimal PERIVA = BigDecimal.ZERO;
		
		if (TotalLines.compareTo(LimiteMinimo)!=-1)
			PERIVA = TotalLines;
                
		return PERIVA;
	}
	//	FIN IVA
	
	private static MJURISDICCION[] getJurisdicciones(MInvoice invoice, String trxName){
		
		MBPartner bpartner = new MBPartner(Env.getCtx(),invoice.getC_BPartner_ID(), trxName);
		boolean convenio = bpartner.IsConvenioMultilateral();
		
		ArrayList<MJURISDICCION> jurisdicciones = new ArrayList<MJURISDICCION>();
		
		if (convenio)
		{
			try 
			{   String consulta = "SELECT C_JURISDICCION_ID " +
								  "FROM C_BPARTNER_JURISDICCION " +
								  "WHERE SIMULTANEO='Y' AND C_BPartner_ID = " + bpartner.getC_BPartner_ID();
		        PreparedStatement pstmt = DB.prepareStatement(consulta, null);
		        ResultSet rs = pstmt.executeQuery();
		        
		        int i=0;
		        while (rs.next()){
		        	jurisdicciones.add(new MJURISDICCION(Env.getCtx(),rs.getInt(1),trxName));
		        	i++;
		        }
			}    
			catch (SQLException e){
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			MLocation loc = new MLocation(Env.getCtx(),invoice.getC_Location_ID(),trxName);
			jurisdicciones.add(new MJURISDICCION(Env.getCtx(),loc.getC_Jurisdiccion_ID(),trxName));
		}
		
		MJURISDICCION[] jurisResult = new MJURISDICCION[jurisdicciones.size()];
		return jurisdicciones.toArray(jurisResult);
	}
	
	private static BigDecimal calcularPercepcionIB(MInvoice invoice, MJURISDICCION jurisdiccion, Percepcion percep, String trxName){
		
		BigDecimal tTotalminimoIB =  BigDecimal.ZERO;
		BigDecimal tLimiteminimoIB =  BigDecimal.ZERO;
		
		BigDecimal tAlicuotaMedic =  BigDecimal.ZERO;
		BigDecimal IBOtros =  BigDecimal.ZERO;
		BigDecimal IBMedic =  BigDecimal.ZERO;
		BigDecimal tAlicuotaOtros =  BigDecimal.ZERO;
		
		MBPartner bpartner = new MBPartner(Env.getCtx(),invoice.getC_BPartner_ID(), trxName);
		
		String consulta = " SELECT  ALIPER, MINAMTPER, LIMMINPER " +
						" FROM  C_BPartner_Jurisdiccion " + 
						" WHERE  C_BPartner_ID = ? AND C_JURISDICCION_ID = ?";
		
		try{
		
			PreparedStatement pstmt = DB.prepareStatement(consulta, null);  
		
			pstmt.setInt(1, invoice.getC_BPartner_ID());
			pstmt.setInt(2, jurisdiccion.getC_Jurisdiccion_ID());
			ResultSet rs = pstmt.executeQuery();  
			
			if (rs.next())
			{  
				
				tAlicuotaMedic = rs.getBigDecimal(1);
					/*
					 * 13-01-2011 Camarzana Mariano
					 * Valida si el Socio de Negocio no es Exento de IB debe de tener
					 * la alicuota cargada
					 */
				if (tAlicuotaMedic.equals(BigDecimal.ZERO))
					{	JOptionPane.showMessageDialog(null, "Debe de Cargar La alicuota Correspondiente al Socio de Negocio", "ERROR", JOptionPane.ERROR_MESSAGE);
						return null;
					}
				
				tTotalminimoIB = rs.getBigDecimal(2); 
				tLimiteminimoIB = rs.getBigDecimal(3);
				
			}
			/*
			 *13-01-2011 Camarzana Mariano
			 *Si no tiene cargada la jurisdiccion para ese socio de negocio no
			 *debe calcular la percepcion 
			 */
			else
				return null;

	
			
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		if (invoice.getTotalLines().compareTo(tLimiteminimoIB)!=-1)
		{
			MInvoiceLine[] iLines = invoice.getLines();
		
			for (int i=0; i<iLines.length; i++)
			{  
				tAlicuotaOtros = null;
				
				if (bpartner.getCondicionIVACode().equals(MBPartner.CIVA_MONOTRIBUTISTA))
					tAlicuotaOtros = MPRODUCTPERCEPTION.getAlicuotaMonotributo(iLines[i].getM_Product_ID(),jurisdiccion.getC_Jurisdiccion_ID());
				if (bpartner.getCondicionIVACode().equals(MBPartner.CIVA_RESPINSCRIPTO))
					tAlicuotaOtros = MPRODUCTPERCEPTION.getAlicuotaRespInscripto(iLines[i].getM_Product_ID(),jurisdiccion.getC_Jurisdiccion_ID());
				
				if (tAlicuotaOtros != null)
				{	
					IBOtros = iLines[i].getLineNetAmt().multiply(tAlicuotaOtros.divide(new BigDecimal(100)));
					percep.setPercepcion(IBOtros,tAlicuotaOtros,true);
				}
				else
					{
						IBMedic = iLines[i].getLineNetAmt().multiply(tAlicuotaMedic.divide(new BigDecimal(100)));
						percep.setPercepcion(IBMedic,tAlicuotaMedic,false);
					}
				
			}
		}
		
		BigDecimal PERIB = percep.getTotalPercepcionIB();
		
		if (PERIB.compareTo(tTotalminimoIB)!=-1)
			return PERIB;
			
		return null;        
	}
	
	//static private int ROUND = BigDecimal.ROUND_HALF_UP;

	public static String inicio (int C_Invoice_ID, MInvoice invoice, String trxName)
	{   
		BigDecimal cPERCEPCIONIB = BigDecimal.ZERO;
		BigDecimal cPERCEPCIONIVA = BigDecimal.ZERO;
		
		boolean tExencionIB = false;
		boolean tExencionIVA = false;
		
		//int SCALE = MPriceList.getPricePrecision(Env.getCtx(), invoice.getM_PriceList_ID());
		
		int AS_Currency = (new MAcctSchema(Env.getCtx(),Env.getContextAsInt(Env.getCtx(),"$C_AcctSchema_ID"),trxName)).getC_Currency_ID();

		try 
		{
	        String consulta;
	        PreparedStatement pstmt;
	        ResultSet rs;
	        Percepciones per = new Percepciones();
	        
	        if (invoice!=null && (invoice.getC_Currency_ID()==AS_Currency))
            {
    			/*
    			 * 		BORRADO DE CÁLCULO ANTERIOR
    			 */
	        	BigDecimal amtPercepciones = invoice.getPercepcionIB().add(invoice.getPercepcionIVA()); //MINVOICEPERCEPSOTRX.getPercepcionesAmount(invoice.getC_Invoice_ID());
	            BigDecimal GrandTotal = invoice.getGrandTotal();
                
                GrandTotal = GrandTotal.subtract(amtPercepciones);
                
                String sqlRemove = "DELETE FROM C_INVOICEPERCEP_SOTRX WHERE C_Invoice_ID = " + invoice.getC_Invoice_ID();
	        	DB.executeUpdate(sqlRemove, trxName);
	        	
	        	/*
                 *      Obtencion de datos generales.
                 */                              
    			String excluido = "SELECT EXENCIONPERIB,EXENCIONPERIVA " +
    							"FROM C_BPARTNER WHERE C_BPARTNER_ID = ?";
    			
    			pstmt = DB.prepareStatement(excluido, null);
                pstmt.setLong(1, invoice.getC_BPartner_ID());
                
    			rs = pstmt.executeQuery();  
    			
    			if (rs.next())
    			{
    				tExencionIB = rs.getString(1).equals("Y");
    				tExencionIVA = rs.getString(2).equals("Y");
    			}
                
//PERCEPCIONES INGRESOS BRUTOS
    			
    			/*
                 *      Obtencion de regimen a aplicar para IIBB.
                 */                        
    			
    			if (!tExencionIB)
                {
    				//Coeficiente IIBB
    				
    				MJURISDICCION[] jurisdicciones = getJurisdicciones(invoice,trxName);
    				
    				for (int i=0; i<jurisdicciones.length; i++)
    				{
    					Percepcion percepcion = new Percepcion();
    					
    					BigDecimal cPERCEPCIONIBJur = calcularPercepcionIB(invoice,jurisdicciones[i],percepcion,trxName);
    					
    					if (cPERCEPCIONIBJur!=null)
    					{
    						MINVOICEPERCEPSOTRX invoicePercep;
    						
    						for (int j=0; j<percepcion.getSize(); j++)
    						{
    							if (!percepcion.getMonto(j).equals(BigDecimal.ZERO))
    							{
    								invoicePercep= new MINVOICEPERCEPSOTRX(Env.getCtx(),0,trxName);
        							invoicePercep.setC_Invoice_ID(invoice.getC_Invoice_ID());
        							invoicePercep.setMonto(percepcion.getMonto(j));//.setScale(SCALE, ROUND));
    	    						invoicePercep.setDescripcion("IB");
    	    						invoicePercep.setC_Jurisdiccion_ID(jurisdicciones[i].getC_Jurisdiccion_ID());
    	    						invoicePercep.setOtros(percepcion.getOtro(j));
    	    						invoicePercep.setAlicuota(percepcion.getAlicuota(j));
    	    						invoicePercep.save(trxName);
    							}
    						}
    						
    						cPERCEPCIONIB = cPERCEPCIONIB.add(cPERCEPCIONIBJur);
    					}
	    				
    				} // END FOR

                }
    			               
                rs.close();  
    			pstmt.close();                
                
//PERCEPCIONES IVA
    			/*
                 *      Obtencion de regimen a aplicar para IVA.
                 */                        
    			
    			if (!tExencionIVA)
                {
    				//Coeficiente IIBB
    				consulta = "SELECT THRESHOLDMIN " +
							   "FROM C_WITHHOLDING " +
							   "WHERE ISACTIVE='Y' AND NAME = 'PERIVA'";
    				
    				pstmt = DB.prepareStatement(consulta, null);

    				rs = pstmt.executeQuery();  

    				/*
                     *      Si no existe registro, es posible que no este ISACTIVO='N'.
                     *      En este caso no se aplica PERCEPCION.
                     */
    				
    				BigDecimal tLimiteminimoIVA =  BigDecimal.ZERO;
    				
    				if (rs.next())
    	            {  
    					tLimiteminimoIVA = rs.getBigDecimal(1);
    					
        				
    					consulta = 	"SELECT  LINENETAMT, C_TAX_ID " +
        							"FROM  C_InvoiceLine " + 
        							"WHERE  C_Invoice_ID = ?";
        				
        				pstmt = DB.prepareStatement(consulta, trxName);  
        				pstmt.setInt(1, C_Invoice_ID);
        				
        				rs = pstmt.executeQuery();  

        				BigDecimal Acumulado = BigDecimal.ZERO;
        				
        				Percepcion percepcion = new Percepcion();
        				
        				while (rs.next())
        	            {  
        					
        					BigDecimal tNETAMT = rs.getBigDecimal(1);
        					long tTax = rs.getLong(2);
        					
        					BigDecimal tAlicuota = BigDecimal.ZERO;
        					
        					consulta = "SELECT  ALIPER " +
									   "FROM  C_TAX " + 
									   "WHERE  C_TAX_ID = ?";
				
        					PreparedStatement pstmtInt = DB.prepareStatement(consulta, null);  
        					pstmtInt.setLong(1, tTax);
				
							ResultSet rsInt = pstmtInt.executeQuery();
				
							if (rsInt.next())
								tAlicuota = rsInt.getBigDecimal(1);
							
							Acumulado = Acumulado.add(tNETAMT.multiply(tAlicuota.divide(new BigDecimal(100))));
							
							percepcion.setPercepcion(tNETAMT, tAlicuota, false);
							
        				}
        				
        				cPERCEPCIONIVA = per.periva (Acumulado, tLimiteminimoIVA);
        				
        				for (int j=0; j<percepcion.getSize(); j++)
						{
							if (!percepcion.getMonto(j).equals(BigDecimal.ZERO))
							{
		        				MINVOICEPERCEPSOTRX invoicePercep = new MINVOICEPERCEPSOTRX(Env.getCtx(),0,trxName);
    							invoicePercep.setC_Invoice_ID(invoice.getC_Invoice_ID());
    							invoicePercep.setMonto(percepcion.getMonto(j));//.setScale(SCALE, ROUND));
	    						invoicePercep.setDescripcion("IVA");
	    						invoicePercep.setC_Jurisdiccion_ID(0);
	    						invoicePercep.setOtros(percepcion.getOtro(j));
	    						invoicePercep.setAlicuota(percepcion.getAlicuota(j));
	    						invoicePercep.save(trxName);
							}
						}
        				
    	            }
	                    rs.close();  
	        			pstmt.close();
                }
    			
    			BigDecimal totalPercep = cPERCEPCIONIB.add(cPERCEPCIONIVA);
    			MINVOICEPERCEPSOTRX invoicePercep = new MINVOICEPERCEPSOTRX(Env.getCtx(),0,trxName);
				invoicePercep.setC_Invoice_ID(invoice.getC_Invoice_ID());
				invoicePercep.setMonto(totalPercep);//.setScale(SCALE, ROUND));
				invoicePercep.setDescripcion("TOTAL");
				invoicePercep.setC_Jurisdiccion_ID(0);
				invoicePercep.setOtros(false);
				invoicePercep.save(trxName);
    			
    			invoice.setPercepcionIB(cPERCEPCIONIB);
                invoice.setPercepcionIVA(cPERCEPCIONIVA);
                invoice.setGrandTotal(GrandTotal.add(totalPercep)); 	//MSC
                invoice.setFlagSave(true);
                invoice.save(trxName);
                
            }
	        else
			{
                
            }

		}
		catch (Exception exc) {System.out.println(exc.toString());};
		return "";  
	}
        
        //  CREADO POR BISION - MATIAS MAENZA - 17/04/08
	//  completa el combo codigo de regimen 
        public String charge (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{   
            if(value !=null){
            Integer C_Charge_ID = (Integer)value;        
            String sql="";
            String sqlQuery = "select taxtype from c_charge where c_charge_id="+C_Charge_ID.intValue()+" and istax='Y'";
                    Integer valueReg;
                    try {
                        PreparedStatement pstmt = DB.prepareStatement(sqlQuery,null);
                        ResultSet rs = pstmt.executeQuery();                        
                        if(rs.next()){  
                           mTab.setValue("ISTAX","Y");
                       	   MLookup lookup = (MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup();
                       	   lookup.removeAllElements();
                           sql = "select C_REGIM_RETEN_PERCEP_RECIB_id,codigo_regimen from C_REGIM_RETEN_PERCEP_RECIB where C_REGIM_RETEN_PERCEP_RECIB.tipo_percep='"+rs.getString(1)+"'";
                           PreparedStatement pstmt2 = DB.prepareStatement(sql,null);
                           ResultSet rs2 = pstmt2.executeQuery();
                           KeyNamePair k=null;
                           while (rs2.next()) { 
                                valueReg= new Integer (((Long)rs2.getLong(1)).intValue()); 
                                k= new KeyNamePair(valueReg.intValue(),rs2.getString(2));
                                ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).addElement(k);
                           }                           
                           ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).refresh(true);
                        }
                        else
                            mTab.setValue("ISTAX","N");

                    }catch (Exception exception) {
                        exception.printStackTrace();
                        return "error";
                  }
            }
                    return "";
        }
        
        
        // MODIFICADO POR BISION - MATIAS MAENZA - 08/05/08
        
        public String code (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{   
            if ( value != null ){
                Integer C_Charge_ID = (Integer)value;        
                
                String sqlQuery = "select ISRETBANK,ISPERCEPADUANERA from c_charge where c_charge_id="+C_Charge_ID.intValue()+" and  (c_charge.ISRETBANK='Y' or c_charge.ISPERCEPADUANERA='Y ' ) and taxtype='IIB' ";
                       
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sqlQuery,null);
                    ResultSet rs = pstmt.executeQuery();
                    if( ! rs.next() ){
                       //mTab.getField("CODIGO_JURISDICCION").getLookup().removeAllElements(); 
                       mTab.setValue("ESRETBANCARIA","N");
                       mTab.setValue("ESPERCEPADUANERA","N");
                    }else{
                       /*if ( mTab.getField("CODIGO_JURISDICCION").getLookup().getSize() == 0 ){
                          KeyNamePair k=null;
                          for (int cod=901;cod<925;cod++){
                            k= new KeyNamePair(cod,String.valueOf(cod)); 
                            ((MLookup)mTab.getField("CODIGO_JURISDICCION").getLookup()).addElement(k);
                             
                          }
                       }*/  
                        if(rs.getString(1)!=null)
                            if(rs.getString(1).equals("Y"))  mTab.setValue("ESRETBANCARIA","Y");
                            else mTab.setValue("ESRETBANCARIA","N");
                        if(rs.getString(2)!=null)
                            if(rs.getString(2).equals("Y"))  mTab.setValue("ESPERCEPADUANERA","Y");
                            else mTab.setValue("ESPERCEPADUANERA","N");
                          
                       
                   }

                }catch (Exception exception) {
                    exception.printStackTrace();
                    return "error";
              }
                return "";
            }
            return "";
        } 

         //  CREADO POR BISION - MATIAS MAENZA - 30/04/08  -  Modificado 08/05/08
	//  completa y muestra, segun corresponda, el combo del mes y del año. 
        public String periodo(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
	{   
            if ( value != null ){
                Integer C_Charge_ID = (Integer)value;                        
                String sqlQuery = "select c_charge_id from c_charge where c_charge_id="+C_Charge_ID.intValue()+" and c_charge.ISRETBANK='Y' and taxtype='IIB' ";
                       
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sqlQuery,null);
                    ResultSet rs = pstmt.executeQuery();
                    if( ! rs.next() ){                       
                        mTab.setValue("ESRETBANCARIA","N");
                    }else{                       
                        mTab.setValue("ESRETBANCARIA","Y");
                    }    
                       
                }catch (Exception exception) {
                    exception.printStackTrace();
                    return "error";             
                }           
            }
            return "";
       }
        
}
