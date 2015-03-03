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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.compiere.util.DB;

/**
 * 	Callout for Allocate Payments
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutPaymentAllocate.java,v 1.2 2005/11/06 01:17:27 jjanke Exp $
 */
public class CalloutConciliacionBancaria extends CalloutEngine
{
	/**
	 *  27/04/2009 - Daniel Gini
	 *  	REQ-058	--- Verifica que no se superpongan las conciliaciones.
	 */
	public String dates (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
		
		//	Get Info from Tab
		Timestamp FDesde = (Timestamp)mTab.getValue("FROMDATE");
		Timestamp FHasta = (Timestamp)mTab.getValue("TODATE");
		Integer C_BankAccount_Id = (Integer)mTab.getValue("C_BankAccount_ID");
		Integer C_CONCILIACIONBANCARIA_ID = (Integer)mTab.getValue("C_CONCILIACIONBANCARIA_ID");
		
		if (FDesde!= null && FHasta!=null && C_CONCILIACIONBANCARIA_ID!=null && C_BankAccount_Id!=null)
		{
			try
			{
				String sql = "SELECT FROMDATE, TODATE " +
							" FROM C_CONCILIACIONBANCARIA " +
							" WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND IsActive = 'Y'";
			
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
			
				pstmt.setInt(1, C_BankAccount_Id);
				pstmt.setInt(2, C_CONCILIACIONBANCARIA_ID);
		
				ResultSet rs = pstmt.executeQuery();
		
				while (rs.next())
				{
					Timestamp from = rs.getTimestamp(1);
					Timestamp to = rs.getTimestamp(2);
					
					if ((from.after(FDesde) && from.before(FHasta)) || (to.after(FDesde) && to.before(FHasta)))
					{
						JOptionPane.showMessageDialog(null,"Existe Superpocición entre Conciliaciones para la misma Cuenta Bancaria","ERROR - Superpocición de Fechas", JOptionPane.ERROR_MESSAGE);
						return "";
					}
				}
				
				setSaldoInicial(mTab,C_CONCILIACIONBANCARIA_ID, FDesde, C_BankAccount_Id);
			}
			catch (SQLException sqlE){}
			
		}

		setCalloutActive(false);
		return "";
	}	//	Dates
	
	private void setSaldoInicial(MTab mTab, int C_CONCILIACIONBANCARIA_ID, Timestamp FDesde, int C_BankAccount_Id)
	{
		try{
			String sql = "SELECT TODATE, AMOUNTCIERRE " +
				 	  	 " FROM C_CONCILIACIONBANCARIA " +
				 	  	 " WHERE C_BankAccount_Id = ? AND C_CONCILIACIONBANCARIA_ID !=? AND IsActive = 'Y' AND TODATE < ? AND DocStatus IN ('CO','CL')" +
				 	  	 " Order By TODATE desc";
		
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
		
			pstmt.setInt(1, C_BankAccount_Id);
			pstmt.setInt(2, C_CONCILIACIONBANCARIA_ID);
			pstmt.setTimestamp(3, FDesde);
		
			ResultSet rs = pstmt.executeQuery();
		
			BigDecimal bd = (BigDecimal)mTab.getValue("AMOUNTINICIAL");
			
			if (rs.next())
				bd = rs.getBigDecimal(2);
			
			mTab.setValue("AMOUNTINICIAL", bd);
			
			BigDecimal conciliado = (BigDecimal)mTab.getValue("AMOUNTCONCILIADO");
			BigDecimal cierre = (BigDecimal)mTab.getValue("AMOUNTCIERRE");
			
			mTab.setValue("AMOUNTACONCILIAR", cierre.subtract(bd).subtract(conciliado));
			
		}
		catch (SQLException sqlE){}
	}
	
	/**
	 *  27/04/2009 - Daniel Gini
	 *  	Actualiza los montos ante una eventual modificación del saldo inicial.
	 */
	public String saldoInicial (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
		
		BigDecimal saldoInicial = (BigDecimal)value;

		//	Get Info from Tab
		BigDecimal saldoCierre = (BigDecimal) mTab.getValue("AMOUNTCIERRE");
		//BigDecimal saldoContable = saldoCierre.add((BigDecimal)mTab.getValue("AMOUNTPENDIENTE"));
		
		//	Set Info to Tab
		mTab.setValue("AMOUNTCIERRE", saldoCierre);

		mTab.setValue("AMOUNTACONCILIAR", saldoCierre.subtract(saldoInicial).subtract((BigDecimal) mTab.getValue("AMOUNTCONCILIADO")));		
		setCalloutActive(false);
		return "";
	}	//	saldoInicial
	
	/**
	 *  22/01/2010 - Daniel Gini
	 *  	Actualiza los montos ante una eventual modificación del saldo cierre. Ahora, editable.
	 */
	public String saldoCierre (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
		
		BigDecimal saldoCierre = (BigDecimal)value;

		//	Get Info from Tab
		BigDecimal saldoInicial = (BigDecimal) mTab.getValue("AMOUNTINICIAL");
		BigDecimal saldoAConciliar = saldoCierre.subtract(saldoInicial).subtract((BigDecimal) mTab.getValue("AMOUNTCONCILIADO"));
		
		//	Set Info to Tab
		mTab.setValue("AMOUNTACONCILIAR", saldoAConciliar);
		
		setCalloutActive(false);
		return "";
	}	//	saldoCierre
	
	/**
	 *  22/01/2010 - Daniel Gini
	 *  	Ante el cambio de una fecha, eliminar movimientos realizados.
	 */
	public String eliminarMovimientos(Properties ctx, int WindowNo, MTab mTab, MField mField, Object value, Object oldValue)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";
		setCalloutActive(true);
		
		int C_CONCILIACIONBANCARIA_ID = 0;
		
		try	{
			C_CONCILIACIONBANCARIA_ID = (Integer)mTab.getValue("C_CONCILIACIONBANCARIA_ID");
		}
		catch (NullPointerException e)		{
			
		}
		
		if (C_CONCILIACIONBANCARIA_ID!=0)
		{
			MCONCILIACIONBANCARIA conc = new MCONCILIACIONBANCARIA(ctx,C_CONCILIACIONBANCARIA_ID,null);
		
			if (conc.getMovPosteriores()!= null || conc.getMovConciliados(true)!=null || conc.getMovConciliados(false)!=null)
			{
				int rta = JOptionPane.showConfirmDialog(null, "Para confirmar el cambio de Fecha, se deben eliminar los movimientos conciliados. Desea continuar?", "Existen Movimientos Conciliados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (rta!=JOptionPane.YES_OPTION)
				{
					setCalloutActive(false);
					return "";
				}
				
				conc.deleteMovConciliados();
				conc.deleteMovPendientes();
				conc.deleteMovPosteriores();
				
				Timestamp FDesde = (Timestamp)mTab.getValue("FROMDATE");
				Integer C_BankAccount_Id = (Integer)mTab.getValue("C_BankAccount_ID");
				mTab.setValue("AMOUNTCONCILIADO", BigDecimal.ZERO);
				mTab.setValue("AMOUNTPENDIENTE", BigDecimal.ZERO);
				
				setSaldoInicial(mTab,C_CONCILIACIONBANCARIA_ID, FDesde, C_BankAccount_Id);
			}
			else
			{
				BigDecimal inicial = (BigDecimal)mTab.getValue("AMOUNTINICIAL");
				BigDecimal cierre = (BigDecimal)mTab.getValue("AMOUNTCIERRE");
				mTab.setValue("AMOUNTCONCILIADO", BigDecimal.ZERO);
				mTab.setValue("AMOUNTPENDIENTE", BigDecimal.ZERO);
				
				//mTab.setValue("AMOUNTREGCONTABLE", cierre);
				mTab.setValue("AMOUNTACONCILIAR", inicial.subtract(cierre));
			}
		}
		
		setCalloutActive(false);
		return "";
	}	//	dates
        
}	//	CalloutPaymentValores
