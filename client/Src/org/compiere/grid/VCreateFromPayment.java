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
package org.compiere.grid;

import java.awt.event.*;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JOptionPane;
import javax.swing.table.*;

import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.search.Info_Column;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Create Invoice Transactions from PO Orders or AP Invoices
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromInvoice.java,v 1.35 2005/11/14 02:10:58 jjanke Exp $
 */
public class VCreateFromPayment extends VCreateFrom implements VetoableChangeListener
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	VCreateFromPayment(MTab mTab)
	{
		super (mTab);
		log.info(mTab.toString());
	}   //  VCreateFromInvoice

	private boolean 	m_actionActive = false;

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	protected boolean dynInit() throws Exception
	{
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "C_Payment_ID", false) + " .. " + "Asignaci�n Facturas");

		parameterBankPanel.setVisible(false);
		parameterStdPanel.setVisible(false); 
		invoiceLabel.setVisible(false);
		invoiceField.setVisible(false);
		locatorLabel.setVisible(false);
		locatorField.setVisible(false);
                
                /*
                String Payment = "SELECT DISTINCT(C_PAYMENT_ID) FROM T_INVOICE_ALLOCATE";
                PreparedStatement pstmt = DB.prepareStatement(Payment, null);	
                ResultSet rs = pstmt.executeQuery();
                MPayment payment = new MPayment (Env.getCtx(), ((Integer)p_mTab.getValue("C_Payment_ID")).intValue(), null);
                if (rs.next()){
                    if(rs.getInt(1)==payment.getC_Payment_ID()){
                 */
                        String delete = "TRUNCATE TABLE T_INVOICE_ALLOCATE";
                        PreparedStatement pstmt1 = DB.prepareStatement(delete, null);	
                        ResultSet rs1 = pstmt1.executeQuery();
                        rs1.close();
                        pstmt1.close();
                        /*
                    }
                }
                rs.close();
                pstmt.close();
                */
		loadInvoice(Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Payment_ID"));
		return true;
	}   //  dynInit

	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		if (m_actionActive)
			return;
		m_actionActive = true;
		log.config("Action=" + e.getActionCommand());
		m_actionActive = false;
	}   //  actionPerformed

	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		log.config(e.getPropertyName() + "=" + e.getNewValue());

		//  BPartner - load Order/Invoice/Shipment
		if (e.getPropertyName() == "C_BPartner_ID")
		{
			int C_BPartner_ID = ((Integer)e.getNewValue()).intValue();
			initBPartnerOIS (C_BPartner_ID, true);
		}
		tableChanged(null);
	}   //  vetoableChange


	/**
	 *  Load Data - Shipment not invoiced
	 *  @param M_InOut_ID InOut
	 */
	private void loadInvoice (int M_Payment_ID)
	{
		log.config("M_Payment_ID=" + M_Payment_ID);
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuffer sql = new StringBuffer("SELECT "	//	QtyEntered
			+ " i.C_Invoice_ID,"									//  3..4
			+ " bp.C_BPartner_ID,"									//  5..8
			+ " bp.Name,"        									//  5..8
			+ " i.DateInvoiced,"        							//  5..8
			+ " i.DocumentNo,"        								//  5..8
			+ " c.C_Currency_ID,"
			+ " c.ISO_Code,"
			+ " i.GrandTotal,"        								//  5..8
			+ " i.GrandTotal*i.COTIZACION,"        					//  5..8
			+ " invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID)," //  5..8
			+ " i.Description"        								//  5..8
			+ " from C_invoice_V i"
			+ " inner join C_BPartner bp ON (bp.C_BPartner_ID=i.C_BPartner_ID)"
			+ " inner join C_Currency c ON (c.C_Currency_ID=i.C_Currency_ID)"
                        /*
                         *  Anexo para que filtre y muestre solo las activas
                         *  isactive = 'Y'
                         *  Zynnia 16/04/2012
                         */
			+ " where c.C_Currency_ID=? and i.IsSOTrx=? and i.IsPaid='N' and i.IsActive='Y' and i.C_BPartner_ID=?"
                        /*
                         *  Anexo para que filtre y no muestre las que ya se encuentran asignadas
                         *  isactive = 'Y'
                         *  Zynnia 02/05/2012
                         */     
                        /*
                         *  Ojo que si queda saldo abierto debe permitir asignar ese saldo
                         *  Zynnia 21/11/2012
                         */                              
                        + " and i.C_Invoice_ID not in (select C_Invoice_ID from C_PaymentAllocate where C_Payment_ID ="+M_Payment_ID+" AND amount = invoiceamt)");
		
		try
		{
			MPayment payment = new MPayment(Env.getCtx(), M_Payment_ID, null);
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, payment.getC_Currency_ID());
			if (payment.isReceipt())
				pstmt.setString(2, "Y");
			else
				pstmt.setString(2, "N");
			//pstmt.setString(2, (payment.isReceipt()==true ? "Y" : "N"));
			pstmt.setInt(3, payment.getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(7);
				
				line.add(new Boolean(false));   //  0-Selection
				KeyNamePair pp = new KeyNamePair(rs.getInt(2), rs.getString(3).trim());
				line.add(pp);                   //  2 - C_BPartner_ID
				line.add(rs.getDate(4));	  	//  3 - DateInvoiced
				pp = new KeyNamePair(rs.getInt(1), rs.getString(5).trim());
				line.add(pp);                   //  4 - Documentno
				pp = new KeyNamePair(rs.getInt(6), rs.getString(7).trim());
				line.add(pp);                   //  5 - C_Currency_ID
				line.add(rs.getBigDecimal(8));  //  6 - GranTotal
				line.add(rs.getBigDecimal(9));  //  7 - Convertido
                                //Verifica si el saldo abierto es todo el monto o si ya hay algo asignado
                                String sqlSaldoAbierto = "SELECT amount FROM C_PaymentAllocate WHERE C_Invoice_ID = "+rs.getInt(1) +
                                                         " AND C_Payment_ID = "+ M_Payment_ID ;
                                PreparedStatement ps = DB.prepareStatement(sqlSaldoAbierto, null);
                                ResultSet rs1 = ps.executeQuery();
                                if (rs1.next()){
                                    line.add(rs.getBigDecimal(10).subtract(rs1.getBigDecimal(1))); //  8 - Saldo Abierto
                                    line.add(rs.getBigDecimal(10).subtract(rs1.getBigDecimal(1))); //  8 - Total a Asignar
                                }
                                else
                                {
                                    line.add(rs.getBigDecimal(10)); //  8 - Saldo Abierto
                                    line.add(rs.getBigDecimal(10)); //  8 - Total a Asignar
                                }
				line.add(rs.getString(11));  	//  9 - Descripcion
				
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		// 	Header Info
		Vector<String> columnNames = new Vector<String>(7);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));	// 0
		columnNames.add("Socio de Negocio");					// 1
		columnNames.add("Fecha Factura");						// 2
		columnNames.add("Nro Documento");						// 3 
		columnNames.add("Moneda");								// 4 
		columnNames.add("Gran Total");							// 5
		columnNames.add("Convertido");							// 6
		columnNames.add("Saldo Abierto");						// 7
		columnNames.add("Total");								// 8
		columnNames.add("Descripción");							// 9
		
		//	  Remove previous listeners
		dataTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		model.addTableModelListener(this);
		dataTable.setModel(model);
		//
		
		dataTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		dataTable.setColumnClass(1, String.class, true);     	//  1-BPartner
		dataTable.setColumnClass(2, Timestamp.class, true);     //  2-DateTrx
		dataTable.setColumnClass(3, String.class, true);	    //  3-DocumentNo
		dataTable.setColumnClass(4, String.class, true);   		//  4-Currency
		dataTable.setColumnClass(5, BigDecimal.class, true);    //  5-GranTotal
		dataTable.setColumnClass(6, BigDecimal.class, true);    //  6-Convert
		dataTable.setColumnClass(7, BigDecimal.class, true);    //  7-OpenAmt
		dataTable.setColumnClass(8, BigDecimal.class, false);   //  8-Amount
		dataTable.setColumnClass(9, String.class, true);       //  9-Description
		//  Table UI
		dataTable.autoSize();
	}   //  loadInvoice

	/**
	 *  List number of rows selected
	 */
	protected void info()
	{
            TableModel model = dataTable.getModel();
            int rows = model.getRowCount();
            int count = 0;
            MPayment payment = new MPayment (Env.getCtx(), ((Integer)p_mTab.getValue("C_Payment_ID")).intValue(), null);
            String bar = "";
            if (payment.getPayAmt().equals(Env.ZERO)){
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal total = payment.getPayAmt().subtract(payment.getAmountAllocate());
                    for (int i = 0; i < rows; i++)
                    {
                            if (((Boolean)model.getValueAt(i, 0)).booleanValue())
                            {	
                                    count++;
                                    amount = amount.add((BigDecimal)model.getValueAt(i, 8));
                            }
                    }
                    bar = "Seleccionados: " + String.valueOf(count) + "   - Importe: " + amount.toString() + "   - Importe Remanente: " + total.subtract(amount).toString();
                    statusBar.setStatusLine(bar);
                }
                else
                {
                    BigDecimal amount = payment.getPayAmt().subtract(payment.getAmountAllocate());
                    String sqlImporte = "SELECT SUM(IMPORTE) FROM T_INVOICE_ALLOCATE ";
                    PreparedStatement pstmImporte = DB.prepareStatement(sqlImporte,null);
                    ResultSet rsImporte;
                    try {
                         rsImporte = pstmImporte.executeQuery();
                         rsImporte.next();
                         BigDecimal diff = rsImporte.getBigDecimal(1);
                         if (!(diff == null)){
                             amount = amount.subtract(diff);                           
                         } else {
                             diff = Env.ZERO;                             
                         }
                             
                         pstmImporte.close();
                         rsImporte.close();
                    
                            MCurrency moneda = MCurrency.get (Env.getCtx(), payment.getC_Currency_ID());                
                            BigDecimal zeroScale = Env.ZERO;

                            if (moneda != null) {                    
                                zeroScale = Env.ZERO.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                                amount = amount.setScale(moneda.getStdPrecision(),BigDecimal.ROUND_HALF_UP);
                            }

                                BigDecimal total = Env.ZERO;
                                for (int i = 0; i < rows; i++)
                                {

                                        if (((Boolean)model.getValueAt(i, 0)).booleanValue())
                                        {
                   
                                            KeyNamePair pp = (KeyNamePair)model.getValueAt(i, 3);
                                            count++;
                                            int idColumn = pp.getKey();
                                            String sql = "SELECT C_INVOICE_ID FROM T_INVOICE_ALLOCATE WHERE C_INVOICE_ID = "+ idColumn;
                                            PreparedStatement pstm = DB.prepareStatement(sql,null);
                                            ResultSet rs;
                                            /*
                                            *  20/11/2012 Zynnia. 
                                            *  Agregamos que calcule todo lo que ya esta asignado, pero que aun no esta guardado en la pestaña
                                            *  de asignacion. Lo tiene que calcular antes de empezar a asignar nada, asi no influye el orden y funciona 
                                            *  de buen modo.
                                            *
                                            */
                                            try {
                                                rs = pstm.executeQuery();
                                                if (!rs.next()){
                                                    
                                                    if (amount.equals(zeroScale)){
                                                        JOptionPane.showMessageDialog(null,"Ya ha alcanzado el límite de Monto que usted ha ingresado.","Monto alcanzado.", JOptionPane.ERROR_MESSAGE);
                                                        confirmPanel.getOKButton().setEnabled(false);
                                                    }
                                                    else
                                                    {
                                                    amount = amount.subtract((BigDecimal)model.getValueAt(i, 8));
                                                    String insert = "INSERT INTO T_INVOICE_ALLOCATE(c_payment_id,c_invoice_id,c_bpartner_id,documentno,grandtotal,importe) VALUES(?,?,?,?,?,?)";
                                                    PreparedStatement pstmt = DB.prepareStatement(insert, null);
                                                    pstmt.setInt(1, payment.getC_Payment_ID());
                                                    pstmt.setInt(2, idColumn);  
                                                    KeyNamePair pbp = (KeyNamePair)model.getValueAt(i, 1);
                                                    int idbpartner = pbp.getKey();
                                                    pstmt.setInt(3, idbpartner);
                                                    KeyNamePair documentno = (KeyNamePair)model.getValueAt(i, 3);
                                                    String doc = documentno.getName();
                                                    pstmt.setString(4, doc);
                                                    BigDecimal GrandTotal = (BigDecimal)model.getValueAt(i, 5);
                                                    pstmt.setBigDecimal(5, GrandTotal);
                                                    if ((!(amount.signum() == -1))||(amount.equals(Env.ZERO))){
                                                        BigDecimal impor = (BigDecimal)model.getValueAt(i, 8);
                                                        pstmt.setBigDecimal(6, impor);
                                                        total = total.add((BigDecimal)model.getValueAt(i, 8));  
                                                        bar = "Seleccionados: " + String.valueOf(count) + "   - Importe: " + total.toString() + "   - Importe Remanente: " + amount.toString();
                                                        pstmt.executeQuery();
                                                        pstmt.close();
                                                    }
                                                    else
                                                    {
                                                        /*
                                                        *  17/11/2012 Zynnia. 
                                                        *  Agregamos que calcule todo lo que ya esta asignado, pero que aun no esta guardado en la pestaña
                                                        *  de asignacion. Tomamos que el Total de Importe sea, lo que se puso en el Total, menos el total de la pestaña
                                                        *  de asignacion, menos lo que va quedando en el importe que se va asignando actualmente.
                                                        *
                                                        */
                                                        BigDecimal diferencia = payment.getPayAmt().subtract(payment.getAmountAllocate());

                                                        diferencia = diferencia.subtract(diff);
                                                        
                                                        amount = payment.getPayAmt();
                                                        pstmt.setBigDecimal(6, diferencia);
                                                        JOptionPane.showMessageDialog(null,"Ya ha alcanzado el limite de Monto que usted ha ingresado. Guarde los cambios.","Monto sobrepasado.", JOptionPane.ERROR_MESSAGE);
                                                        bar = "Seleccionados: " + String.valueOf(count) + "   - Importe: " + amount.toString() + "   - Importe Remanente: " + Env.ZERO.toString();  
                                                        pstmt.executeQuery();
                                                        pstmt.close();
                                                        model.setValueAt(diferencia, i, 8);
                                                    }

                                                    }
                                                }
                                                else
                                                {
                                                    // Si existe tengo que validar si cambio el valor o si lo dejo como estaba
                                                    // model.getValueAt(i, 8) vs. IMPORTE en T_INVOICE_ALLOCATE
                                                    
                                                    BigDecimal tot = (BigDecimal)model.getValueAt(i, 8);
                                                    String sqlImp = "SELECT IMPORTE FROM T_INVOICE_ALLOCATE WHERE C_INVOICE_ID = " + idColumn;
                                                    PreparedStatement pstmImp = DB.prepareStatement(sqlImp,null);
                                                    BigDecimal importePrevio = Env.ZERO;
                                                    ResultSet rsImp;
                                                    try {
                                                        rsImp = pstmImp.executeQuery();
                                                        if (rsImp.next()){
                                                            importePrevio = rsImp.getBigDecimal(1);
                                                        }
                                                    } catch (SQLException ex) {
                                                        Logger.getLogger(VCreateFromPayment.class.getName()).log(Level.SEVERE, null, ex);
                                                    }   

                                                    // Para los casos que se cambia el valor registra la diferencia entre el total
                                                    // y lo que se quiere imputar                                                    
                                                    
                                                    BigDecimal difValue = importePrevio.subtract((BigDecimal)model.getValueAt(i, 8));
                                                    amount = amount.add(difValue);        
                                                    
                                                    String sqlUpdate = "UPDATE T_INVOICE_ALLOCATE SET IMPORTE = " + tot.toString() + " WHERE C_INVOICE_ID = " + idColumn;
                                                    DB.executeUpdate(sqlUpdate, null);  
                                                    
                                                    
                                                    total = total.add(tot);
                                                    bar = "Seleccionados: " + String.valueOf(count) + "   - Importe: " + total.toString() + "   - Importe Remanente: " + amount.toString();
                                                }
                                            } catch (SQLException ex) {
                                                Logger.getLogger(VCreateFromPayment.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            

                                        }
                                        else
                                        {
                                            KeyNamePair pp = (KeyNamePair)model.getValueAt(i, 3);
                                            int idColumn = pp.getKey();
                                            String existe = "SELECT C_Invoice_ID FROM T_INVOICE_ALLOCATE WHERE C_INVOICE_ID = "+ idColumn;
                                            PreparedStatement pstmt = DB.prepareStatement(existe, null);
                                            try {
                                                ResultSet rs = pstmt.executeQuery();
                                                if (rs.next()){
                                                    String delete = "DELETE FROM T_INVOICE_ALLOCATE WHERE C_INVOICE_ID = "+ rs.getInt(1);
                                                    PreparedStatement del = DB.prepareStatement(delete,null);
                                                    del.executeQuery();
                                                    del.close();
                                                    amount = amount.add((BigDecimal)model.getValueAt(i, 8));
                                                    model.setValueAt((BigDecimal)model.getValueAt(i, 7), i, 8);
                                                    bar = "Seleccionados: " + String.valueOf(count) + "   - Importe: " + total.toString() + "   - Importe Remanente: " + amount.toString();
                                                    
                                                }
                                            } catch (SQLException ex) {
                                                Logger.getLogger(VCreateFromPayment.class.getName()).log(Level.SEVERE, null, ex);
                                            } 

                                        }
                                }

            } catch (SQLException ex) {
                 Logger.getLogger(VCreateFromPayment.class.getName()).log(Level.SEVERE, null, ex);
            }
                statusBar.setStatusLine(bar);
           }
                
	}   //  infoInvoice

	private BigDecimal importe = null;
	/**
	 *  Save - Create Invoice Lines
	 *  @return true if saved
	 */
	protected boolean save()
	{
		log.config("");
		TableModel model = dataTable.getModel();
		int rows = model.getRowCount();
		if (rows == 0)
			return false;

		if (checkTotals(model))
		{
			//  Invoice
			int C_Payment_ID = ((Integer)p_mTab.getValue("C_Payment_ID")).intValue();
			Trx trx = Trx.get("payAllocation",true);
			MPayment payment = new MPayment (Env.getCtx(), C_Payment_ID, trx.getTrxName());
			log.config(payment.toString());
			if(!payment.getPayAmt().equals(Env.ZERO)){
                        
                            BigDecimal monto = BigDecimal.ZERO;
                            BigDecimal disponible = BigDecimal.ONE;

                            try{
                                    String sql = "SELECT C_Invoice_ID, GrandTotal, Importe "
                                               + "FROM T_Invoice_Allocate";
                                    PreparedStatement pstmt = DB.prepareStatement(sql, null);
                                    ResultSet rs = pstmt.executeQuery();
                                        while(rs.next()){
                                            MPaymentAllocate payAll = null;
                                            String sqlPAll = "SELECT * FROM C_PAYMENTALLOCATE WHERE C_PAYMENT_ID = "+ C_Payment_ID +
                                                    " AND C_INVOICE_ID = "+rs.getInt(1);
                                            PreparedStatement pstmta = DB.prepareStatement(sqlPAll, null);
                                            ResultSet rsa = pstmta.executeQuery();
                                            /*if(!rsa.next()){
                                                MPaymentAllocate payAll = new MPaymentAllocate(Env.getCtx(),0,null);
                                                payAll.setC_Invoice_ID(rs.getInt(1));
                                                payAll.setC_Payment_ID(C_Payment_ID);
                                                BigDecimal am = rs.getBigDecimal(3);
                                             */
                                             /* 
                                             * - Si esta factura todavia no esta asignada 
                                             * entonces creo una nueva asignacion
                                             * - Si la factura ya estaba asignada y se pudo seleccionar
                                             * entonces quiere decir que todavia esa factura no esta totalmente paga
                                             * pero el monto de la OP se incremento, 
                                             * por lo tanto tenemos que actualizar la asignacion
                                             */
                                                BigDecimal am = BigDecimal.ZERO;
                                                if(!rsa.next()){
                                                    payAll = new MPaymentAllocate(Env.getCtx(),0,null);
                                                    payAll.setC_Invoice_ID(rs.getInt(1));
                                                    payAll.setC_Payment_ID(C_Payment_ID);
                                                    am = rs.getBigDecimal(3);
                                                }
                                                else{
                                                    payAll = new MPaymentAllocate(Env.getCtx(),rsa.getInt(1),null);
                                                    am = rs.getBigDecimal(3).add(payAll.getAmount());
                                                }
                                                payAll.setAmount(am);
                                                BigDecimal iam = rs.getBigDecimal(2);
                                                payAll.setInvoiceAmt(iam);
                                                payAll.save(null);
                                                monto = monto.add(am);
                                        }
                                        payment.setFlagSave(false);
                                        payment.save(trx.getTrxName());
                                        trx.commit();
                                }	// try
                                catch(Exception e){
				trx.rollback();
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se pudo realizar la asignaci�n seleccionada.","Error - Asignaci�n",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            BigDecimal monto = BigDecimal.ZERO;
                            BigDecimal disponible = BigDecimal.ONE;

                            try
                            {
                                int i=0;
                                while (i < rows)
                                {
                                    if (((Boolean)model.getValueAt(i, 0)).booleanValue())
                                    {
                                        importe = (BigDecimal)model.getValueAt(i, 8);	
                                        KeyNamePair pp = (KeyNamePair)model.getValueAt(i, 3);
                                        int idColumn = pp.getKey();
                                        BigDecimal openAmt = (BigDecimal)model.getValueAt(i, 7);

                                        if (importe.compareTo(BigDecimal.ZERO)!=0)
                                        {
                                            MPaymentAllocate payAll = new MPaymentAllocate(Env.getCtx(),0,null);

                                            payAll.setC_Invoice_ID(idColumn);
                                            payAll.setC_Payment_ID(C_Payment_ID);
                                            payAll.setAmount(importe);
                                            payAll.setInvoiceAmt(openAmt);
                                            payAll.save(null);
                                        }

                                        monto = monto.add(importe);
                                    }	// if selected
                                    i++;
                                }	// while - all rows
                               
                                payment.setFlagSave(false);
                                payment.setPayAmt(monto);
                                payment.save(trx.getTrxName());

                                trx.commit();

                                if (i<rows)
                                        JOptionPane.showMessageDialog(null, "Asignaci�n Completa. No se contabilizaron todos los comprobantes seleccionados.","Informaci�n - Asignaci�n",JOptionPane.INFORMATION_MESSAGE);

                            }	// try
                            catch(Exception e){
                                    trx.rollback();
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "No se pudo realizar la asignaci�n seleccionada.","Error - Asignaci�n",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        
                        //payment.recalcularRetenciones();
                        
                        
                }

		return true;
	}   //  saveInvoice

	// Check All Totals less OpenAmt
	protected boolean checkTotals(TableModel model)
	{
		int rows = model.getRowCount();
		if (rows == 0)
			return false;
		
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())	// Si esta seleccionada
			{
				BigDecimal openAmt = (BigDecimal)model.getValueAt(i, 7); // OpenAmt
				BigDecimal total = (BigDecimal)model.getValueAt(i, 8); // Total
				
				if (openAmt.compareTo(total)==-1)
				{	
					JOptionPane.showMessageDialog(null, "Existe un registro con Total mayor al importe Abierto.","Error - Verificaci�n de Montos",JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}
	
	//	 Obtiene la disponibilidad del monto asignado, con respecto al Total de Pago.
	protected BigDecimal getMontoDisponible(BigDecimal inicial, BigDecimal total, BigDecimal asignado)
	{
		BigDecimal nuevoAsignado = asignado.add(importe).add(inicial);
		if (nuevoAsignado.compareTo(total)==1)
		{	
			importe = total.subtract(asignado.add(inicial));
			return BigDecimal.ZERO;
		}

		return total.subtract(nuevoAsignado);
	}
	
	@Override
	void initBPDetails(int C_BPartner_ID) {
		// TODO Auto-generated method stub
		
	}
        
        public BigDecimal getAllocatedAmt (MPayment payment)
	{
		BigDecimal retValue = BigDecimal.ZERO;
		if (payment.getC_Charge_ID() != 0)
			return payment.getPayAmt();
		//
		String sql = "SELECT SUM(al.amount)"
			+ " FROM C_PaymentAllocate al"
			+ " WHERE al.C_Payment_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1, payment.getC_Payment_ID());

			ResultSet rs = pstmt.executeQuery();
			if (rs.next() && rs.getBigDecimal(1)!=null)
				retValue = rs.getBigDecimal(1);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getAllocatedAmt", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}

}   //  VCreateFromInvoice