package org.compiere.model;

import java.math.*;
import java.lang.String;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

// Zynnia

public class RetencionesChanges extends CalloutEngine 
{
	//TODO PASAR A CONSTANTE
	private static int DOCTYPE_IVA = 1000182;
	private static int DOCTYPE_GAN = 1000183;
	private static int DOCTYPE_SUSS = 1000220;
	
	//	INICIO GANANCIAS
	float retgan (float MSR, float TotalFijo, float Porcentaje, float LimiteMinimo, float TotalMinimo, float RetAnterior)
	{
		float RETGAN = 0;
		
		if (MSR >= LimiteMinimo)
			RETGAN = (MSR - LimiteMinimo) * (Porcentaje/100) + TotalFijo - RetAnterior;
		
		if (RETGAN >= TotalMinimo)
			return RETGAN;
		
		return 0;
	}
	//	FIN GANANCIAS
	
	//	INICIO IIBB
	float retib (float MSR, float Porcentaje, float LimiteMinimo, float TotalMinimo) 
	{
		float RETIB = 0;
		
		if (MSR >= LimiteMinimo)
			RETIB = (MSR * Porcentaje/100);
		
		if (RETIB >= TotalMinimo)
			return RETIB;
		
		return 0;        
	}
	//	FIN IIBB

	//	INICIO SUSS
	float retsuss (float MSR, float TotalFijo, float Porcentaje, float LimiteMinimo, float TotalMinimo) 
	{
		float RETSUSS = 0;
		
		if (MSR >= LimiteMinimo)
			RETSUSS = TotalFijo + (MSR * Porcentaje/100);
		
		if (RETSUSS >= TotalMinimo)
			return RETSUSS;
		
		return 0;
	}
	//	FIN SUSS
	
	//INICIO IVA
	float retiva (float MSR, float TotalFijo, float Porcentaje, float LimiteMinimo, float TotalMinimo) 
	{
		float RETIVA = 0;

		if (MSR >= LimiteMinimo)
			RETIVA = TotalFijo + (MSR * Porcentaje/100);
        
		if (RETIVA >= TotalMinimo)
			return RETIVA;
		
		return 0;
	}
	//FIN IVA
	
	float getMSR (int C_Payment_ID, float TotalPago, float tIVA) 
	{
		float Divisor = 1 + tIVA / 100;
		
		float MSR = 0;

		try
		{	
			String consulta = "Select C_Invoice_Id from C_Payment where c_payment_id = ?";
			
			PreparedStatement pstmt = DB.prepareStatement(consulta, null);  
			 
	        pstmt.setInt(1, C_Payment_ID);
	        
	        ResultSet rs = pstmt.executeQuery();  
	        
	        if (rs.next() && (rs.getLong(1)!=0))
	        {
	        	//Asignación de la factura por el campo Factura de la Ventana Pago.
	        	
	        	long C_Invoice_ID = rs.getLong(1);
	        	
	        	consulta = "Select GrandTotal, TotalLines from C_Invoice where c_invoice_id = ?";
	    		
	    		pstmt = DB.prepareStatement(consulta, null);  
	    		 
	            pstmt.setLong(1, C_Invoice_ID);
	            
	            rs = pstmt.executeQuery();
	            
	            float GranTotal = 0;
	            float TotalLineas = 0;
	            
	            if (rs.next())	{
	            	GranTotal = rs.getFloat(1);
	            	TotalLineas = rs.getFloat(2);
	            }
	            
	            if ((TotalPago <= GranTotal) && (GranTotal!=0))
	            	MSR = TotalPago  * (TotalLineas / GranTotal);
	            else	
	            	MSR = TotalLineas + (TotalPago - GranTotal) / Divisor;
	            
	        }
	        else
	        {
	        	consulta = "Select C_Invoice_Id, Amount from C_PaymentAllocate where c_payment_id = ?";
	    		
	    		pstmt = DB.prepareStatement(consulta, null);  
	    		 
	            pstmt.setInt(1, C_Payment_ID);
	            
	            rs = pstmt.executeQuery();
	            
	            float Fi = 0;
            	float AcumTotal = 0;
	            
            	/*
            	 * 	Si entra al ciclo al menos 1 vez:
            	 *		Asignación de la factura por la pestaña Asignación de la Ventana Pago.
            	 * 	Si no entra en el ciclo:	
            	 * 		Pago sin asignación a Factura.
            	 */
            	
            	while (rs.next())
	            {
            			long C_Invoice_ID = rs.getLong(1);
            			float Total = rs.getFloat(2);
		            	
	            		consulta = "Select GrandTotal, TotalLines from C_Invoice where C_Invoice_Id = ?";
	            		
	            		PreparedStatement pstmtInt = DB.prepareStatement(consulta, null);
	        			pstmtInt.setLong(1, C_Invoice_ID);
	                
	        			ResultSet rsInt = pstmtInt.executeQuery();
	                	
	                	if (rsInt.next())	{
	                	
	                		float GranTotal = rsInt.getFloat(1);
	                        float TotalLineas = rsInt.getFloat(2);
	                        
	                        Fi =  Fi + (TotalLineas/GranTotal) * Total;
	                        
	                        AcumTotal = AcumTotal + Total;
	                	}
	                		
	            }
	            
            	//Si no entra en el ciclo, se reduce a: MSR = TotalPago / Divisor
	            MSR = Fi + (TotalPago - AcumTotal) / Divisor;
	            
	        }// else
	        	                        
	        rs.close();  
	        pstmt.close();
	        
		}
        catch (Exception exc) {
        	System.out.println(exc.toString());
        }
		return MSR;
	}
	
	public static String inicio (int C_Payment_ID, MPayment payment, String trxName) 
	{   


        /*
         *      Al inicio debería borra todas las retenciones asociadas al pago.
         *
         */
        float tPayamt = 0;
        
        float tTotalfijoGAN =  0;
		float tPorcentajeGAN =  0;
		float tTotalminimoGAN =  0;
		float tLimiteminimoGAN =  0;
        float tLimitemaximoGAN =  0;
        
        float cRETENCIONGAN = 0;
        boolean tExencionGAN = false; 
        boolean tExento = false;
		boolean tRespInsc = false;
		boolean tNoContr = false;
		float tIVA =  0;
		float tSumant = 0;
		float tSumretencionganancias = 0;

        
        float tPorcentajeIB =  0;
		float tTotalminimoIB =  0;
		float tLimiteminimoIB =  0;
        
		float cRETENCIONIB = 0;
		boolean tExencionIB = false;
		
		
		float tTotalfijoSUSS =  0;
		float tPorcentajeSUSS =  0;
		float tTotalminimoSUSS =  0;
		float tLimiteminimoSUSS = 0;
		
		float cRETENCIONSUSS = 0;
		boolean tExencionSUSS = false;

		
		float tTotalfijoIVA =  0;
		float tPorcentajeIVA =  0;
		float tTotalminimoIVA =  0;
		float tLimiteminimoIVA = 0;
		
		float cRETENCIONIVA = 0;
		boolean tExencionIVA = false;

		//String espago = "";
                
        String estadodoc = payment.getDocStatus();
        
        try 
		{
                    
            /*
             *      Obtencion de datos generales.
             *
             */                              
            
			String excluido = "SELECT EXENCIONGANANCIAS,EXENCIONIB,EXENCIONIVA,EXENCIONSUSS " +
							"FROM C_BPARTNER WHERE C_BPARTNER_ID = ?";
			
			PreparedStatement pstmt = DB.prepareStatement(excluido, null);
            pstmt.setLong(1, payment.getC_BPartner_ID());
            
			ResultSet rs = pstmt.executeQuery();  
			
			if (rs.next())
			{
				tExencionGAN = rs.getString(1).equals("Y");
				tExencionIB = rs.getString(2).equals("Y");
				tExencionIVA = rs.getString(3).equals("Y");
				tExencionSUSS = rs.getString(4).equals("Y");
			}
                
			String consulta = "SELECT CONDICIONGAN_ID, IVA " + 
            			"FROM C_BPartner " + 
            			"WHERE C_BPartner_ID = ?";

            pstmt = DB.prepareStatement(consulta, null);  
            pstmt.setInt(1, payment.getC_BPartner_ID());
                        
            rs = pstmt.executeQuery();  
            
            if (rs.next())
            {   
            	if(rs.getInt(1) == MBPartner.CGAN_EXENTO)
                    tExento = true;
                else if(rs.getInt(1) == MBPartner.CGAN_RESPINSCRIPTO)
                    	tRespInsc = true;
                	else if(rs.getInt(1) == MBPartner.CGAN_NOCATEGORIZADO)
                            tNoContr = true;
                
                tIVA = rs.getFloat(2);
            }
	        
//MONTO SUJETO A RETENER            
            
    // CALCULO
            RetencionesChanges ret = new RetencionesChanges();
            
            tPayamt = payment.getPayAmt().floatValue();
            	
            float MSR = ret.getMSR(C_Payment_ID,tPayamt,tIVA);
            
            
    // ALMACENA EN LA BASE PARA LUEGO REALIZAR LOS COMPROBANTES DE RETENCIÓN
            
            
            
            String query = "update c_payment set MSR = "+ BigDecimal.valueOf(MSR).setScale(2,BigDecimal.ROUND_HALF_EVEN).floatValue() +" where c_payment_id = "+ payment.getC_Payment_ID();
            
            DB.executeUpdate(query,trxName);
            
//
            
			rs.close();  
			pstmt.close();                          

                        
            /*
             *      Una vez que tengo el acumulado y el monto del pago aplico el siguiente procedimiento:
             *
             *
             *      1 - En función de los pagos anteriores verifico en que rango cae el pago actual.
             *      2 - Con el pago saco la diferencia con el limite minimo del rango y el calculo va a ser
             *          para la retencion, ret = (((pago - limite minimo) * % del rango) + fijo) - retenciones acumuladas
             *
             *
             */
                    
			
// RETENCIONES GANANCIAS
			
			/*
			 * 		Si es Responsable Inscripto, toma la tabla de retenciones RETGAN
			 * 		Si No Contribuye, toma la tabla de retenciones RETGANNC
			 * 		Si es Monotributista, no se aplica retencion Ganancias   
			 */
						
			if ((!tExencionGAN) && (!tExento) && (tRespInsc || tNoContr))	{
				
			   /*
	            *      Calculo de los pagos anteriores con ese tipo de retencion.
	            */
	            
	        	consulta = "SELECT pago.C_Payment_Id, pago.PAYAMT, pago.RETENCIONGANANCIAS FROM C_PAYMENT pago " +
	                    "INNER JOIN AD_ORGINFO orginfo ON pago.AD_ORG_ID = orginfo.AD_ORG_ID " +
	                    "WHERE pago.C_REGIMENGANANCIAS_V_ID = '" + payment.getC_REGIMENGANANCIAS_V_ID() + "' AND orginfo.AGENTE = 'Y' " +
	                    "AND pago.C_DocType_ID = 1000138 AND to_char(pago.datetrx, 'yy') = to_char(?, 'yy') " +
	                    "AND to_char(pago.datetrx, 'mm') = to_char(?, 'mm') " +
	                    "AND pago.C_Payment_ID <> ? AND (pago.DOCSTATUS = 'CO' OR pago.DOCSTATUS = 'CL') AND pago.C_BPARTNER_ID = " + payment.getC_BPartner_ID();
	                        
                pstmt = DB.prepareStatement(consulta, null);  
                pstmt.setTimestamp (1, payment.getDateTrx()); 
                pstmt.setTimestamp (2, payment.getDateTrx()); 
                pstmt.setInt(3, C_Payment_ID); 

                rs = pstmt.executeQuery();  

                while (rs.next())
                {
                	tSumant += ret.getMSR(rs.getInt(1),rs.getFloat(2),tIVA);
                    tSumretencionganancias += rs.getFloat(3);
                }

                rs.close();  
				pstmt.close();
				
				
				//	INICIO - Buscar el Monto No Sujeto a Retención.
				consulta =  "SELECT MNSR " +
							"FROM C_WITHHOLDING " +
							"WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGAN'";

				pstmt = DB.prepareStatement(consulta, null);  
                pstmt.setString (1, payment.getC_REGIMENGANANCIAS_V_ID()); 
                
                rs = pstmt.executeQuery();  

                float MNSR = 0;
                
                if (rs.next())	{
                	MNSR = rs.getFloat(1);
                }

   				//	FIN - Buscar el Monto No Sujeto a Retención.
                
                
                /*
                 *  MSRFINAL - MONTO SUJETO A RETENER FINAL PARA GANANCIAS, TENIENDO EN CUENTA
                 *  		LOS PAGOS ANTERIORES Y EL MONTO NO SUJETO A RETENER.	
                 */ 
                			
                	float MSRFINAL = tSumant + MSR - MNSR; 

                	
               	// Conseguir Datos para realizar el cálculo.
				if (tRespInsc)	{
						
						consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN, THRESHOLDMAX " +
								"FROM C_WITHHOLDING " +
								"WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGAN' " +
								"ORDER BY THRESHOLDMIN";
										
				}
				else	{
						
						consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN, THRESHOLDMAX " +
								"FROM C_WITHHOLDING " +
								"WHERE ISACTIVE='Y' AND REGIMENGANANCIAS = ? AND NAME = 'RETGANNC' " +
								"ORDER BY THRESHOLDMIN";
				
				}
					
                pstmt = DB.prepareStatement(consulta, null);
                pstmt.setString(1, payment.getC_REGIMENGANANCIAS_V_ID());
                
                rs = pstmt.executeQuery();  
                        
                boolean cont = true;
                                
                while (rs.next() && cont)	{
                	
                	tTotalfijoGAN = rs.getFloat(1);
                    tPorcentajeGAN = rs.getFloat(2);
                    tTotalminimoGAN = rs.getFloat(3);
                    tLimiteminimoGAN = rs.getFloat(4);
                    tLimitemaximoGAN = rs.getFloat(5);
                            
                    if ( (MSRFINAL >= tLimiteminimoGAN && MSRFINAL < tLimitemaximoGAN) || (tLimiteminimoGAN == 0 && tLimitemaximoGAN == 0) )	{
                    	cRETENCIONGAN = ret.retgan (MSRFINAL,tTotalfijoGAN,tPorcentajeGAN,tLimiteminimoGAN,tTotalminimoGAN,tSumretencionganancias);
                    	cont = false;
                	}
                }
                      
			} /* 	if (!tMonotributo) -- ELSE  cRETENCIONGAN = 0, pero ya se inicializa
			   *												con este valor.
               */      

			
// RETENCIONES INGRESOS BRUTOS
			
			/*
             *      Obtencion de regimen a aplicar para IIBB.
             *
             *      Se verifica si al socio de negocio se aplica retención.
             *
             */                        
			
			if (!tExencionIB)
            {
				//Coeficiente IIBB
				consulta = 	"SELECT  ALIRET, MINAMTRET, LIMMINRET " +
							"FROM  C_BPartner_Jurisdiccion " + 
							"WHERE  C_BPartner_ID = ? " + 
									"AND C_JURISDICCION_ID = (SELECT C_JURISDICCION_ID " + 
												"FROM C_Payment " +
												"WHERE C_Payment_Id = ?)";
								
				
				pstmt = DB.prepareStatement(consulta, null);  
				pstmt.setInt(1, payment.getC_BPartner_ID());
				pstmt.setInt(2, C_Payment_ID);
				
				rs = pstmt.executeQuery();  

				if (rs.next())
	            {  
					tPorcentajeIB = rs.getFloat(1);
					tTotalminimoIB = rs.getFloat(2); 
	                tLimiteminimoIB = rs.getFloat(3);
	            }
				
				cRETENCIONIB = ret.retib (MSR, tPorcentajeIB, tLimiteminimoIB, tTotalminimoIB);

            }
			               
            rs.close();  
			pstmt.close();  
			
			
//RETENCIONES SUSS			
			
			/*
             *      Obtencion de regimen a aplicar para SUSS.
             *
             */ 
			
			if (!tExencionSUSS)
            {

				consulta = "SELECT FIXAMT, PERCENT, MINAMT, THRESHOLDMIN " +
						"FROM C_WITHHOLDING " +
						"WHERE ISACTIVE='Y' AND NAME = 'RETSUSS' ";
				
	            pstmt = DB.prepareStatement(consulta, null);  
	    		
	            rs = pstmt.executeQuery();  
	            
	            if (rs.next())
	            {   
	                tTotalfijoSUSS = rs.getFloat(1);
	                tPorcentajeSUSS = rs.getFloat(2);
	                tTotalminimoSUSS = rs.getFloat(3); 
	                tLimiteminimoSUSS = rs.getFloat(4);
	            }
	            							
				cRETENCIONSUSS = ret.retsuss (MSR, tTotalfijoSUSS, tPorcentajeSUSS, tLimiteminimoSUSS, tTotalminimoSUSS);

            }
			               
            rs.close();  
			pstmt.close();


//RETENCIONES IVA			
			
			/*
             *      Obtencion de regimen a aplicar para IVA.
             */ 
			
			if (!tExencionIVA)
            {
				//Coeficiente IVA
				consulta = 	"SELECT COEFICIENTEIVA " +
							"FROM  C_BPartner " + 
							"WHERE  C_BPartner_ID = ?";
			
				pstmt = DB.prepareStatement(consulta, null);  
				pstmt.setInt(1, payment.getC_BPartner_ID());
				
				rs = pstmt.executeQuery();  

				if (rs.next())
	            {  
					tPorcentajeIVA = rs.getFloat(1);
	            }	
				
				consulta = "SELECT FIXAMT, MINAMT, THRESHOLDMIN " +
						"FROM C_WITHHOLDING " +
						"WHERE ISACTIVE='Y' AND NAME = 'RETIVA' ";
				
	            pstmt = DB.prepareStatement(consulta, null);  
	    		
	            rs = pstmt.executeQuery();  
	            
	            if (rs.next())
	            {   
	                tTotalfijoIVA = rs.getFloat(1);
	                tTotalminimoIVA = rs.getFloat(2); 
	                tLimiteminimoIVA = rs.getFloat(3);
	            }
	            							
	            cRETENCIONIVA = ret.retiva (MSR, tTotalfijoIVA, tPorcentajeIVA, tLimiteminimoIVA, tTotalminimoIVA);

            }
			               
            rs.close();  
			pstmt.close();
			
//--------------------------------------			
			
							//Si es pago genera las retenciones
							//Si no se omite la generacion                   
				            /*
				            PreparedStatement psval = DB.prepareStatement("select ISRECEIPT from C_PAYMENT where C_PAYMENT_ID=?", null);  
				            psval.setInt(1, C_Payment_ID); 
				            ResultSet rsval = psval.executeQuery();  
				            if (rsval.next())
				            {	espago= rsval.getString(1);	}
				            rsval.close();  
				            psval.close();
				            */  

			// Vit4B 14/03/2208 Actualizacion del neto en función de el valor del pago menos las retenciones
	        
	        float neto = tPayamt - cRETENCIONIVA - cRETENCIONGAN - cRETENCIONSUSS - cRETENCIONIB;
	        
	        payment.setPAYNET(BigDecimal.valueOf(neto).setScale(2,BigDecimal.ROUND_HALF_EVEN));
	        
	        
	        /*
	         *      Vit4B 18/12/2007
	         *      Modificacion para genarar las retenciones en una tabla separada a la de pagos
	         *      c_paymentret
	         */
                            
            //Guarda en tabla de retenciones

            BigDecimal valcRETENCIONIVA = BigDecimal.valueOf(cRETENCIONIVA).setScale(2,BigDecimal.ROUND_HALF_EVEN);
            BigDecimal valcRETENCIONGAN = BigDecimal.valueOf(cRETENCIONGAN).setScale(2,BigDecimal.ROUND_HALF_EVEN);
            BigDecimal valcRETENCIONSUSS = BigDecimal.valueOf(cRETENCIONSUSS).setScale(2,BigDecimal.ROUND_HALF_EVEN);
            BigDecimal valcRETENCIONIB = BigDecimal.valueOf(cRETENCIONIB).setScale(2,BigDecimal.ROUND_HALF_EVEN);
                            
            if (estadodoc == "VO"){
                    String sqlestado = new String("update c_paymentret set isactive='N' where c_payment_id=" + C_Payment_ID);                            
                    Statement pstmtestado = null;
                    pstmtestado = DB.createStatement();
                    pstmtestado.executeUpdate(sqlestado);
                    pstmtestado.close();
            }
            else
            {
            	/*      
                 *      Vit4B 11/03/2008
                 *      Agregado para tomar la numeracion desde un tipo de documento
                 *      JF
                 *
                 */
            	if (cRETENCIONIVA > 0)
            	{
            		
	                String documentNo = MSequence.getDocumentNo(DOCTYPE_IVA, null).toString();
	                String prefix = documentNo.substring(10,documentNo.length());
	                
	                String prefixIni = documentNo.substring(0,10);
	                
	                switch (prefix.length()) {
	                  case 1:
	                        prefix = "0000000" + prefix;
	                        break;
	                  case 2:
	                        prefix = "000000" + prefix;
	                        break;
	                  case 3:
	                        prefix = "00000" + prefix;
	                        break;
	                  case 4:
	                        prefix = "0000" + prefix;
	                        break;
	                  case 5:
	                        prefix = "000" + prefix;
	                        break;
	                  case 6:
	                        prefix = "00" + prefix;
	                        break;
	                  case 7:
	                        prefix = "0" + prefix;
	                        break;
	                  case 8:
	                        break;
	                  default:
	                        JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
	                }
	                
	                String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='I'";
	                PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
	                ResultSet rsact = pstmtact.executeQuery(); 
	                if (rsact.next() == true)
	                {
	                    String sqliva = new String("update c_paymentret set importe=" + valcRETENCIONIVA + " where tipo_ret='I' and c_payment_id=" + C_Payment_ID);                            
	                    Statement pstmtiva = null;
	                    pstmtiva = DB.createStatement();
	                    pstmtiva.executeUpdate(sqliva);
	                    pstmtiva.close(); 
	                }
	                else
	                {
	                    String sqliva = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684,"+DOCTYPE_IVA+",'" + prefixIni + prefix + "','I'," + valcRETENCIONIVA + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");                            
	                    Statement pstmtiva = null;
	                    pstmtiva = DB.createStatement();
	                    pstmtiva.executeUpdate(sqliva);
	                    pstmtiva.close(); 
	                }
	                rsact.close();  
	                pstmtact.close();                               
	
	            }
	            

                /*      
                 *      Vit4B 11/03/2008
                 *      Agregado para tomar la numeracion desde un tipo de documento
                 *      JF
                 *
                 */
                
                if (cRETENCIONIB > 0)
                {
                    String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='B'";
                	PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
                	ResultSet rsact = pstmtact.executeQuery(); 
                	if (rsact.next() == true)
                	{
                		String sqlib = new String("update c_paymentret set importe=" + valcRETENCIONIB + " where tipo_ret='B' and c_payment_id=" + C_Payment_ID);                            
                		Statement pstmtib = null;
                		pstmtib = DB.createStatement();
                		pstmtib.executeUpdate(sqlib);
                		pstmtib.close(); 
                	}
                	else
                	{
                		int doc = 0;
                        
                        consulta = "SELECT C_DOCTYPE_ID " +
    							   "FROM C_JURISDICCION " +
    							   "WHERE C_JURISDICCION_ID=?";
    			
                        pstmt = DB.prepareStatement(consulta, null);
                        pstmt.setInt(1, payment.getC_JURISDICCION_ID());
        		
                        rs = pstmt.executeQuery();  
                
                        if (rs.next())
                        {   	
                        	doc = rs.getInt(1);
                        }
                        
                        String sqlib = "";
                		
                		if (doc!=0)
                		{
                			String documentNo = MSequence.getDocumentNo(doc, null).toString();
                			String prefix = documentNo.substring(10,documentNo.length());
                    
                			String prefixIni = documentNo.substring(0,10);
                    
                			switch (prefix.length()) {
                			case 1:
                				prefix = "0000000" + prefix;
                				break;
                			case 2:
                				prefix = "000000" + prefix;
                				break;
                			case 3:
                				prefix = "00000" + prefix;
                				break;
                			case 4:
                				prefix = "0000" + prefix;
                				break;
                			case 5:
                				prefix = "000" + prefix;
                				break;
                			case 6:
                				prefix = "00" + prefix;
                				break;
                			case 7:
                				prefix = "0" + prefix;
                				break;
                			case 8:
                				break;
                			default:
                				JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
                			}
                			sqlib = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684,"+ doc +",'" + prefixIni + prefix + "','B'," + valcRETENCIONIB + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");
                		}
                		else
                		{
                			sqlib = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684,0,'0','B'," + valcRETENCIONIB + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");
                		}

                		Statement pstmtib = null;
                   		pstmtib = DB.createStatement();
                   		pstmtib.executeUpdate(sqlib);
                   		pstmtib.close(); 
                   	}
                    
                    rsact.close();  
                    pstmtact.close();
                    
                }	//	if (cRETENCIONIB > 0)
               
                /*      
                 *      Vit4B 11/03/2008
                 *      Agregado para tomar la numeracion desde un tipo de documento
                 *      JF
                 *
                 */        
                if (cRETENCIONGAN > 0)
                {                                
	                String documentNo = MSequence.getDocumentNo(DOCTYPE_GAN, null).toString();
	                String prefix = documentNo.substring(10,documentNo.length());
	                
	                String prefixIni = documentNo.substring(0,10);
	                
	                switch (prefix.length()) {
	                  case 1:
	                        prefix = "0000000" + prefix;
	                        break;
	                  case 2:
	                        prefix = "000000" + prefix;
	                        break;
	                  case 3:
	                        prefix = "00000" + prefix;
	                        break;
	                  case 4:
	                        prefix = "0000" + prefix;
	                        break;
	                  case 5:
	                        prefix = "000" + prefix;
	                        break;
	                  case 6:
	                        prefix = "00" + prefix;
	                        break;
	                  case 7:
	                        prefix = "0" + prefix;
	                        break;
	                  case 8:
	                        break;
	                  default:
	                        JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
	                }
	               
	                
	                String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='G'";
	                PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
	                ResultSet rsact = pstmtact.executeQuery(); 
	                if (rsact.next() == true)
	                {
	                    for (PO payret : payment.getRetenciones())
	                    	if (((MPAYMENTRET)payret).getTIPO_RET().equals("G"))
	                    	{
	                    		((MPAYMENTRET)payret).setIMPORTE(valcRETENCIONGAN);
	                    		payret.save(trxName);
	                    	}
	                }
	                else
	                {
	                	MPAYMENTRET payret = new MPAYMENTRET(Env.getCtx(),0,trxName);
	                	payret.setAD_Client_ID(payment.getAD_Client_ID());
	                	payret.setAD_Org_ID(payment.getAD_Org_ID());
	                	payret.setIMPORTE(valcRETENCIONGAN);
	                	payret.setDateTrx(payment.getDateTrx());
	                	payret.setC_Payment_ID(C_Payment_ID);
	                	payret.setC_DocType_ID(DOCTYPE_GAN);
	                	payret.setDocumentNo(prefixIni + prefix);
	                	payret.setTIPO_RET("G");
	                	payret.setTIPO_OPERACION("C");
	                	payret.setC_REGIM_RETEN_PERCEP_RECIB_ID(0);

	                	payret.save();
	                }
	                rsact.close();  
	                pstmtact.close();
	            }
                        
                if (cRETENCIONSUSS > 0){                            
                    String sqlactualizacion = "select c_paymentret_id from c_paymentret where c_payment_id=" + C_Payment_ID + " and tipo_ret='S'";
                    PreparedStatement pstmtact = DB.prepareStatement(sqlactualizacion, null);
                    ResultSet rsact = pstmtact.executeQuery(); 
                    if (rsact.next() == true)
                    {
                        String sqlib = new String("update c_paymentret set importe=" + valcRETENCIONSUSS + " where tipo_ret='S' and c_payment_id=" + C_Payment_ID);                            
                        Statement pstmtib = null;
                        pstmtib = DB.createStatement();
                        pstmtib.executeUpdate(sqlib);
                        pstmtib.close(); 
                    }
                    else
                    {
                    	String documentNo = MSequence.getDocumentNo(DOCTYPE_SUSS, null).toString();
    	                String prefix = documentNo.substring(10,documentNo.length());
    	                
    	                String prefixIni = documentNo.substring(0,10);
    	                
    	                switch (prefix.length()) {
    	                  case 1:
    	                        prefix = "0000000" + prefix;
    	                        break;
    	                  case 2:
    	                        prefix = "000000" + prefix;
    	                        break;
    	                  case 3:
    	                        prefix = "00000" + prefix;
    	                        break;
    	                  case 4:
    	                        prefix = "0000" + prefix;
    	                        break;
    	                  case 5:
    	                        prefix = "000" + prefix;
    	                        break;
    	                  case 6:
    	                        prefix = "00" + prefix;
    	                        break;
    	                  case 7:
    	                        prefix = "0" + prefix;
    	                        break;
    	                  case 8:
    	                        break;
    	                  default:
    	                        JOptionPane.showMessageDialog(null,"Número de Documento Inválido", "Info", JOptionPane.INFORMATION_MESSAGE);
    	                }
                    	
                    	String sqlib = new String("insert into c_paymentret values(case when (select max(c_paymentret_id)+1 from c_paymentret) is null then 1 else (select max(c_paymentret_id)+1 from c_paymentret) end," + payment.getAD_Client_ID() + "," + payment.getAD_Org_ID() + ",'Y',sysdate,1000684,sysdate,1000684,"+DOCTYPE_SUSS+",'" + prefixIni + prefix + "','S'," + valcRETENCIONSUSS + ",(select DATETRX from C_PAYMENT where C_PAYMENT_ID=" + C_Payment_ID + ")," + C_Payment_ID + ",'C',null)");                            
                        Statement pstmtib = null;
                        pstmtib = DB.createStatement();
                        pstmtib.executeUpdate(sqlib);
                        pstmtib.close(); 
                    }
                    rsact.close();  
                    pstmtact.close();
                }
            }           
                /*
                if (tValores!=0)
                {
                    //si es cobranza                            
                    totalpago=tValores+(cRETENCIONIB+cRETENCIONGAN+cRETENCIONSUSS+cRETENCIONIVA);
                    sql = "UPDATE C_PAYMENT SET PAYAMT = " + totalpago +  " WHERE C_PAYMENT_ID = " + C_Payment_ID + " "; 
                    DB.executeUpdate (sql, null);
                }
                else
                {
                    //si es pago
                    totalpago=tPayamt-(cRETENCIONIB+cRETENCIONGAN+cRETENCIONSUSS+cRETENCIONIVA);                            
                    sql = "UPDATE C_PAYMENT SET PAYNET  = " + totalpago +  " WHERE C_PAYMENT_ID = " + C_Payment_ID + " "; 
                    sql = "UPDATE C_PAYMENT SET PAYAMT  = " + tPayamt +  " WHERE C_PAYMENT_ID = " + C_Payment_ID + " "; 
                    DB.executeUpdate (sql, null);
                }
                */


                payment.setRetencionGanancias(BigDecimal.valueOf(cRETENCIONGAN).setScale(2,BigDecimal.ROUND_HALF_EVEN));
                payment.setRetencionIB(BigDecimal.valueOf(cRETENCIONIB).setScale(2,BigDecimal.ROUND_HALF_EVEN));
                payment.setRetencionIVA(BigDecimal.valueOf(cRETENCIONIVA).setScale(2,BigDecimal.ROUND_HALF_EVEN));
                payment.setRetencionSUSS(BigDecimal.valueOf(cRETENCIONSUSS).setScale(2,BigDecimal.ROUND_HALF_EVEN));
                payment.setSumAnt(BigDecimal.valueOf(tSumant).setScale(2,BigDecimal.ROUND_HALF_EVEN));
                payment.setSumRetencionGanancias(BigDecimal.valueOf(tSumretencionganancias).setScale(2,BigDecimal.ROUND_HALF_EVEN));


                payment.setFlagSave(true);

                payment.save();
            }

        	catch (Exception exc) {System.out.println(exc.toString());};
        	
        	return "";  
		
	}
        
        /*      
         *      Bision 04/04/2008
         *      Agregado para mostrar los Codigos de Regimenes correspondientes al tipo de retencion
         *      Nadia
         *
         */
        
         /*public String fillCodigoRegimen (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){

            if(value!=null){
                String typeRet=(String)mField.getValue();
                String sqlQuery = "select C_REGIM_RETEN_PERCEP_RECIB_id,codigo_regimen from C_REGIM_RETEN_PERCEP_RECIB where C_REGIM_RETEN_PERCEP_RECIB.tipo_ret='"+typeRet+"'";
                Integer valueReg;
                try {
                    PreparedStatement pstmt = DB.prepareStatement(sqlQuery);
                    ResultSet rs = pstmt.executeQuery();

                   ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).removeAllElements();

                    KeyNamePair k=null;
                    while (rs.next()) { 
                        valueReg= new Integer (((Long)rs.getLong(1)).intValue()); 
                        k= new KeyNamePair(valueReg.intValue(),rs.getString(2));
                        ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).addElement(k);

                    }

                   ((MLookup)mTab.getField("C_REGIM_RETEN_PERCEP_RECIB_ID").getLookup()).refresh(true);


                }catch (Exception exception) {
                    exception.printStackTrace();
                    return "error";
                }                
            }
                return "";


        }
        */
	//TODO PASAR A CONSTANTE
	public String fillCodigoRegimen (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){

        if(value!=null)
        {
            String typeRet=(String)mField.getValue();
            MField field = mTab.getField("REGIMENGANANCIAS");
            String sqlQuery = "";
            if (typeRet.equals("Ingresos Brutos"))
            	sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000074 Where AD_Column_ID=?";
        	else
        		if (typeRet.equals("Ganancias"))
        			sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000063 Where AD_Column_ID=?";
                else
	        		if (typeRet.equals("SUSS"))
	        			sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000077 Where AD_Column_ID=?";
	                else
	            		if (typeRet.equals("IVA"))
	            			sqlQuery = "update AD_Column set AD_Reference_Value_ID = 1000074 Where AD_Column_ID=?";
            try
            {
             	PreparedStatement pstmt = DB.prepareStatement(sqlQuery,null);
            	pstmt.setLong(1, field.getAD_Column_ID());
            	pstmt.executeQuery();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return "error";
            }   	

            ((MLookup)field.getLookup()).refresh(true);
        }
            return "";
    }
        
}
