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

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.compiere.apps.ADialog;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Create Transactions for Bank Statements
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromStatement.java,v 1.30 2005/11/14 02:10:58 jjanke Exp $
 */
@SuppressWarnings("serial")
public class VCreateFromConciliacion extends VCreateFrom implements VetoableChangeListener {

    //Columnas
    static private final int col_C_MovimientoFondos_ID = 1;
    static private final int col_DocumentNo = 2;
    static private final int col_REG_MovimientoFondos = 3;
    static private final int col_EfectivaDate = 4;
    static private final int col_Amt = 5;
    static private final int col_Tipo = 6;
    static private final int col_C_ValorPago_ID = 7;
    static private final int col_C_PaymentValores_ID = 8;
    static private final int col_C_Payment_ID = 9;
    static private final int col_NroCheque = 10;
    static private final int col_AFavor = 11;
    static private final int col_VendimientoDate = 12;
    static private final int col_ReleasedDate = 13;
    static private final int col_AmtExt = 14;

    /**
     *  Protected Constructor
     *  @param mTab MTab
     */
    VCreateFromConciliacion(MTab mTab) {
        super(mTab);
        log.info("");
    }   //  VCreateFromStatement

    /**
     *  Dynamic Init
     *  @throws Exception if Lookups cannot be initialized
     *  @return true if initialized
     */
    protected boolean dynInit() throws Exception {
        if (p_mTab.getValue("C_CONCILIACIONBANCARIA_ID") == null) {
            ADialog.error(0, this, "SaveErrorRowNotFound");
            return false;
        }

        setTitle("Conciliación Bancaria .. " + Msg.translate(Env.getCtx(), "CreateFrom") + " Movimiento de Fondos");
        parameterStdPanel.setVisible(false);
        //  Set Default
        int C_BankAccount_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BankAccount_ID");
        //  initial Loading
        loadMovimientos(C_BankAccount_ID);
        return true;
    }   //  dynInit

    /**
     *  Init Details (never called)
     *  @param C_BPartner_ID BPartner
     */
    protected void initBPDetails(int C_BPartner_ID) {
    }   //  initDetails

    /**
     *  Change Listener
     *  @param e event
     */
    public void vetoableChange(PropertyChangeEvent e) {
        log.config(e.getPropertyName() + "=" + e.getNewValue());

        //  BankAccount
        if (e.getPropertyName() == "C_BankAccount_ID") {
            int C_BankAccount_ID = ((Integer) e.getNewValue()).intValue();
            loadMovimientos(C_BankAccount_ID);
        }
        tableChanged(null);
    }   //  vetoableChange
    private Timestamp tsTo = null;
    private Timestamp tsFrom = null;

    /**
     *  Load Data - Bank Account
     *  @param C_BankAccount_ID Bank Account
     */
    private void loadMovimientos(int C_BankAccount_ID) {
        log.config("C_BankAccount_ID=" + C_BankAccount_ID);
        /**
         *  Selected        - 0
         *  Date Mov o Pay  - 1
         *  Type		    - 2
         *  Number  		- 3
         *  Amt             - 4
         *  Date payment	- 5
         *  Cheque			- 6
         *  Released Date   - 6a    Zynnia
         *  Socio de Neg	- 7
         */
        int nFields = 8;

        //	  Get AccountDate
        tsTo = (Timestamp) p_mTab.getValue("TODATE");
        tsFrom = (Timestamp) p_mTab.getValue("FROMDATE");

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

        int C_ConciliacionBancaria_ID = ((Integer) p_mTab.getValue("C_ConciliacionBancaria_ID")).intValue();
        MCONCILIACIONBANCARIA concBancaria = new MCONCILIACIONBANCARIA(Env.getCtx(), C_ConciliacionBancaria_ID, null);
        actualizarPendientes(concBancaria);
        try {
            KeyNamePair pp;

            //	TIPO DE MOVIMIENTO: Movimientos Pendientes
            String sql = " SELECT C_ConciliacionBancaria_ID , documentno, C_MovimientoConciliacion_Id, "
                    + "efectivadate, importe, movimiento, vencimientodate, nrocheque, favor, "
                    + "c_payment_id, c_movimientofondos_id, releasedate, tipo"
                    + " FROM C_MovimientoConciliacion"
                    + " WHERE conciliado = 'N' and oldPendiente = 'N' AND C_BankAccount_Id=?";

            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BankAccount_ID);
            ResultSet rs = pstmt.executeQuery();
            int flag;
            while (rs.next()) {

                flag = 0;

                if (rs.getInt(11) != 0) {
                    MMOVIMIENTOFONDOS mov = new MMOVIMIENTOFONDOS(Env.getCtx(), rs.getInt(11), null);
                    if (mov.getDocStatus().equals("CO")) {
                        flag = 1;
                    }
                }
                if (rs.getInt(10) != 0) {
                    MPayment pay = new MPayment(Env.getCtx(), rs.getInt(10), null);
                    if (pay.getDocStatus().equals("CO")) {
                        flag = 1;
                    }
                }
                if (flag == 1) {
                    Vector<Object> line = new Vector<Object>(nFields);

                    line.add(new Boolean(false));       //  0-Selection
                    line.add(rs.getTimestamp(4));       //  1-DateTrx
                    line.add(new KeyNamePair(0, rs.getString(6))); //  2-Type Movement
                    pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
                    line.add(pp);                       //  3-Number
                    line.add(rs.getBigDecimal(5));      //  4-Amount
                    line.add(rs.getTimestamp(7));		//  5-Date Payment
                    pp = new KeyNamePair(rs.getInt(3), rs.getString(8));
                    line.add(pp);                  		//  6-Nro Cheque
                    line.add(rs.getTimestamp(12));          //  6a-Released Date
                    line.add(rs.getString(9));			//  7-BParner
                    line.add(new Boolean(true));		//  8-Pendiente

                    data.add(line);

                }
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            log.log(Level.SEVERE, "SqlQuery", e);
        }

        //  Header Info
        Vector<String> columnNames = new Vector<String>(nFields);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add("Fecha Movimiento");
        columnNames.add("Tipo Movimiento");
        columnNames.add("Número");
        columnNames.add("Importe");
        columnNames.add("Fecha Pago");
        columnNames.add("Nro. Cheque");
        columnNames.add("Fecha Emisión");
        columnNames.add("Socio de Negocio");
        columnNames.add("Pendiente");

        //  Remove previous listeners
        dataTable.getModel().removeTableModelListener(this);
        //  Set Model
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        model.addTableModelListener(this);
        dataTable.setModel(model);
        //

        dataTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
        dataTable.setColumnClass(1, Timestamp.class, true);     //  1-TrxDate
        dataTable.setColumnClass(2, KeyNamePair.class, true);   //  2-Type
        dataTable.setColumnClass(3, KeyNamePair.class, true);	//  3-Number
        dataTable.setColumnClass(4, BigDecimal.class, true);    //  4-Amount
        dataTable.setColumnClass(5, Timestamp.class, true);     //  5-DateCheque
        dataTable.setColumnClass(6, KeyNamePair.class, true);    	//  6-Cheque

        dataTable.setColumnClass(7, Timestamp.class, true);     //  6-Released Date

        dataTable.setColumnClass(8, String.class, true);    	//  7-BPartner
        dataTable.setColumnClass(9, Boolean.class, true);       //  8-Pendiente
        //  Table UI
        dataTable.autoSize();
    }   //  loadBankAccount

    /**
     *  List total amount
     */
    protected void info() {
        DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);

        TableModel model = dataTable.getModel();
        BigDecimal total = new BigDecimal(0.0);
        int rows = model.getRowCount();
        int count = 0;
        for (int i = 0; i < rows; i++) {
            if (((Boolean) model.getValueAt(i, 0)).booleanValue()) {
                total = total.add((BigDecimal) model.getValueAt(i, 4));
                count++;
            }
        }
        statusBar.setStatusLine(String.valueOf(count) + " - " + Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(total));
    }   //  infoStatement

    /**
     *  Save Statement - Insert Data
     *  @return true if saved
     */
    protected boolean save() {
        log.config("");
        TableModel model = dataTable.getModel();
        int rows = model.getRowCount();
        if (rows == 0) {
            return false;
        }

        int C_ConciliacionBancaria_ID = ((Integer) p_mTab.getValue("C_ConciliacionBancaria_ID")).intValue();
        MCONCILIACIONBANCARIA concBancaria = new MCONCILIACIONBANCARIA(Env.getCtx(), C_ConciliacionBancaria_ID, null);
        log.config(concBancaria.toString());

        BigDecimal saldoInicial = ((BigDecimal) p_mTab.getValue("AmountInicial"));
        BigDecimal movConciliados = ((BigDecimal) p_mTab.getValue("AmountConciliado"));
        BigDecimal saldoCierre = ((BigDecimal) p_mTab.getValue("AmountCierre"));
        BigDecimal movPendientes = ((BigDecimal) p_mTab.getValue("AmountPendiente"));
        //  Lines
        for (int i = 0; i < rows; i++) {
            // Diferenciar si es un movimiento pendiente
            if (((Boolean) model.getValueAt(i, 9)).booleanValue()) {
                int conciliacionBancaria = 0;
                int C_MovimientoConciliado_ID = 0;

                KeyNamePair pp = (KeyNamePair) model.getValueAt(i, 3);
                conciliacionBancaria = pp.getKey();
                BigDecimal TrxAmt = (BigDecimal) model.getValueAt(i, 4); //  4-Amount
                pp = (KeyNamePair) model.getValueAt(i, 6);
                C_MovimientoConciliado_ID = pp.getKey();

                //Diferenciar si es un pendiente anterior a la conciliación actual
                if (conciliacionBancaria != C_ConciliacionBancaria_ID) {
                    MMOVIMIENTOCONCILIACION movConcNew = new MMOVIMIENTOCONCILIACION(concBancaria);
                    MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(Env.getCtx(), C_MovimientoConciliado_ID, null);

                    movConcNew.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                    movConcNew.setC_ConciliacionBancaria_ID(C_ConciliacionBancaria_ID);

                    movConcNew.setC_ValorPago_ID(movConc.getC_ValorPago_ID());
                    movConcNew.setC_PaymentValores_ID(movConc.getC_PaymentValores_ID());
                    movConcNew.setC_Payment_ID(movConc.getC_Payment_ID());
                    movConcNew.setC_MovimientoFondos_ID(movConc.getC_MovimientoFondos_ID());
                    movConcNew.setDocumentNo(movConc.getDocumentNo());
                    movConcNew.setMovimiento(movConc.getMovimiento());
                    movConcNew.setAmt(movConc.getAmt());
                    movConcNew.setNroCheque(movConc.getNroCheque());
                    movConcNew.setAFavor(movConc.getAFavor());
                    movConcNew.setVencimientoDate(movConc.getVencimientoDate());
                    movConcNew.setEstado(movConc.getEstado());
                    movConcNew.setTipo(movConc.getTipo());
                    movConcNew.setREG_MovimientoFondos(movConc.getREG_MovimientoFondos());
                    movConcNew.setEfectivaDate(movConc.getEfectivaDate());
                    movConcNew.setRELEASEDATE(movConc.getRELEASEDATE());
                    if (((Boolean) model.getValueAt(i, 0)).booleanValue()) {
                        movConcNew.setConciliado(true);
                        movConcNew.setLine(MMOVIMIENTOCONCILIACION.getNextLine(C_ConciliacionBancaria_ID));
                        movConciliados = movConciliados.add(movConcNew.getAmt());

                    } //   if selected
                    else {
                        movConc.setConciliado(false);
                        movPendientes = movPendientes.add(movConcNew.getAmt());
                    }
                    if (!movConcNew.save()) {
                        log.log(Level.SEVERE, "Line not created #" + i);
                    }

                    movConc.setOld(true);
                    if (!movConc.save()) {
                        log.log(Level.SEVERE, "Line not created #" + i);
                    }
                } else {
                    // Si selecciona conciliar el movimiento pendiente
                    if (((Boolean) model.getValueAt(i, 0)).booleanValue()) {
                        MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(Env.getCtx(), C_MovimientoConciliado_ID, null);
                        movConc.setConciliado(true);
                        movConc.setLine(MMOVIMIENTOCONCILIACION.getNextLine(C_ConciliacionBancaria_ID));
                        movConciliados = movConciliados.add(TrxAmt);
                        movPendientes = movPendientes.subtract(TrxAmt);

                        if (!movConc.save()) {
                            log.log(Level.SEVERE, "Line not created #" + i);
                        }
                    }
                }
            } else {
                int C_ValorPago_ID = 0;
                int C_PaymentValores_ID = 0;
                int Reg_MovimientoFondos = 0;
                int C_MovimientoFondos_ID = 0;
                int C_Payment_ID = 0;
                String nroCheque = null;
                String tipo = null;
                String estado = null;

                Timestamp trxDate = (Timestamp) model.getValueAt(i, 1);  //  1-DateTrx
                KeyNamePair typeMovement = (KeyNamePair) model.getValueAt(i, 2); //  2-Type Movement
                KeyNamePair knp1 = (KeyNamePair) model.getValueAt(i, 3);
                String documentNo = knp1.getName();						//  3-Number
                BigDecimal TrxAmt = (BigDecimal) model.getValueAt(i, 4); //  4-Amount
                Timestamp payDate = (Timestamp) model.getValueAt(i, 5);  //  5-Date Payment
                KeyNamePair knp2 = (KeyNamePair) model.getValueAt(i, 6); //  6-Debito/Credito/Cheque
                Timestamp releaseDate = (Timestamp) model.getValueAt(i, 7);  //  7-Release Payment

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.Z) {
                    C_Payment_ID = knp1.getKey();
                    tipo = "B";
                    C_PaymentValores_ID = knp2.getKey();
                    nroCheque = knp2.getName();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.F) {
                    C_Payment_ID = knp1.getKey();
                    tipo = "B";
                    C_ValorPago_ID = knp2.getKey();
                    nroCheque = knp2.getName();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.C || typeMovement.getKey() == MCONCILIACIONBANCARIA.Y) {
                    C_Payment_ID = knp1.getKey();
                    tipo = "E";
                    C_ValorPago_ID = knp2.getKey();
                    nroCheque = knp2.getName();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.E || typeMovement.getKey() == MCONCILIACIONBANCARIA.X) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "E";
                    C_ValorPago_ID = knp2.getKey();
                    nroCheque = knp2.getName();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.R) {
                    C_Payment_ID = knp1.getKey();
                    tipo = "P";
                    C_ValorPago_ID = knp2.getKey();
                    nroCheque = knp2.getName();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.H) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "R";
                    C_ValorPago_ID = knp2.getKey();
                    nroCheque = knp2.getName();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.N) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "N";
                    Reg_MovimientoFondos = knp2.getKey();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.M) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "M";
                    Reg_MovimientoFondos = knp2.getKey();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.B) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "B";
                    Reg_MovimientoFondos = knp2.getKey();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.W) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "W";
                    Reg_MovimientoFondos = knp2.getKey();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.P) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "P";
                    Reg_MovimientoFondos = knp2.getKey();
                    nroCheque = knp2.getName();
                    estado = "Rechazado";
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.D) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "D";
                    Reg_MovimientoFondos = knp2.getKey();
                    estado = "Depositado";
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.T) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "T";
                    Reg_MovimientoFondos = knp2.getKey();
                    estado = "Rechazado";
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.S) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "S";
                    Reg_MovimientoFondos = knp2.getKey();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.K) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "K";
                    Reg_MovimientoFondos = knp2.getKey();
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.V) {
                    C_MovimientoFondos_ID = knp1.getKey();
                    tipo = "V";
                    Reg_MovimientoFondos = knp2.getKey();
                    nroCheque = knp2.getName();
                    estado = "Vencido";
                }
                String bpartner = (String) model.getValueAt(i, 8); 		//  7-BPartner

                MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                movConc.setC_ConciliacionBancaria_ID(C_ConciliacionBancaria_ID);
                movConc.setREG_MovimientoFondos(Reg_MovimientoFondos);
                movConc.setC_ValorPago_ID(C_ValorPago_ID);
                movConc.setC_PaymentValores_ID(C_PaymentValores_ID);
                movConc.setC_Payment_ID(C_Payment_ID);
                movConc.setC_MovimientoFondos_ID(C_MovimientoFondos_ID);
                movConc.setDocumentNo(documentNo);
                movConc.setMovimiento(typeMovement.getName());
                movConc.setAmt(TrxAmt);
                movConc.setNroCheque(nroCheque);
                movConc.setAFavor(bpartner);
                movConc.setVencimientoDate(payDate);
                movConc.setOld(false);
                movConc.setTipo(tipo);
                movConc.setRELEASEDATE(releaseDate);

                if (((Boolean) model.getValueAt(i, 0)).booleanValue()) {
                    log.fine("Line Date=" + trxDate
                            + ", Movement=" + typeMovement.getName() + ", Amt=" + TrxAmt);
                    movConc.setConciliado(true);
                    movConc.setLine(MMOVIMIENTOCONCILIACION.getNextLine(C_ConciliacionBancaria_ID));
                    movConciliados = movConciliados.add(TrxAmt);
                } //   if selected
                else {
                    movConc.setConciliado(false);
                    movPendientes = movPendientes.add(TrxAmt);
                }

                MVALORPAGO valpay = new MVALORPAGO(Env.getCtx(), C_ValorPago_ID, null);
                if ((C_ValorPago_ID != 0) && (!nroCheque.equals(""))) {
                    if (valpay.getEstado().equals("E")) {
                        movConc.setEstado("Emitido");
                    }
                    if (valpay.getEstado().equals("P")) {
                        movConc.setEstado("Pendiente de Débito");
                    }

                    if (valpay.getEstado().equals("C")) {
                        movConc.setEstado("Rechazado");
                    }
                }
                if (estado != null) {
                    movConc.setEstado(estado);
                }

                if (typeMovement.getKey() == MCONCILIACIONBANCARIA.H || typeMovement.getKey() == MCONCILIACIONBANCARIA.R || typeMovement.getKey() == MCONCILIACIONBANCARIA.E || typeMovement.getKey() == MCONCILIACIONBANCARIA.C || typeMovement.getKey() == MCONCILIACIONBANCARIA.F) {
                    movConc.setEfectivaDate(valpay.getDebitoDate());
                } else {
                    if (typeMovement.getKey() == MCONCILIACIONBANCARIA.X || typeMovement.getKey() == MCONCILIACIONBANCARIA.Y) {
                        movConc.setEfectivaDate(releaseDate);
                    } else {
                        movConc.setEfectivaDate(trxDate);
                    }
                }
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Line not created #" + i);
                }
            }
        }   //  for all rows

        concBancaria.refrescarSaldos();
        concBancaria.save();

        tsTo = (Timestamp) p_mTab.getValue("TODATE");
        tsFrom = (Timestamp) p_mTab.getValue("FROMDATE");

        if (concBancaria.deleteMovPosteriores()) {
            MCONCILIACIONBANCARIA.completarMovPosteriores(concBancaria);
        }
        return true;
    }   //  save

    public static void actualizarPendientes(MCONCILIACIONBANCARIA concBancaria) {

        //	  Get AccountDate
        Timestamp tsTo = concBancaria.getToDate();
        Timestamp tsFrom = concBancaria.getFromDate();
        int C_BankAccount_ID = concBancaria.getC_BankAccount_ID();
        MMOVIMIENTOCONCILIACION movConc = null;
        MBankAccount bankAccount = new MBankAccount(Env.getCtx(), C_BankAccount_ID, null);

        /*
         * Se pueden conciliar los siguientes items
         *
         * 1 - Valores Negociados ( N )
         * 2 - Movimiento de Efectivo ( M )
         * 3 - Transferencia Bancaria Cobro ( Z )
         * 4 - Transferencia Bancaria Pago ( F )
         * 5 - Emisión de Cheque Propio ( X , Y , E , Z )
         * 6 - Cheque Propio Rechazado ( H , R )
         * 7 - Transferencia Bancaria / Débito ( B )
         * 8 - Rechazo de Cheques Propios ( P )
         * 9 - Depósito de Cheques ( D )
         * 10 - Rechazo de Cheques Terceros ( T )
         * 11 - Depósitos Pendientes ( S ) y Créditos Bancarios ( K ) -> Debito
         * 12 - Depósitos Pendientes ( S ) -> Credito
         * 13 - Transferencia entre cuentas credito ( W )
         * 14 - Transferencia entre cuentas debito ( W )
         * 15 - Cheques Propios Vencidos (V)
         * 16 - Movimientos Pendientes (Conciliaciones pendientes)
         */
         String sql = "";
        try {
            KeyNamePair pp;
            // TIPO DE MOVIMIENTO: Valores Negociados (N)
            sql = " SELECT"
                    + " mf.C_MovimientoFondos_Id,"
                    + " mf.documentno,"
                    + " mfd.C_MovimientoFondos_Deb_Id,"
                    + " mf.datetrx, "
                    + " mfd.debito,"
                    + " mf.tipo,"
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " null," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero --> Movimientos de fondos (N) no tienen moneda extranjera
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID = mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount ba ON (mfd.C_BankAccount_Id = ba.C_BankAccount_Id)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_NEGOCIADOS + "' AND ba.C_BankAccount_Id=? AND mf.datetrx >= to_date('2012/05/01', 'yyyy/mm/dd') AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') "
                    + " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
                    + " ORDER BY mfd.C_MovimientoFondos_Deb_Id";

            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BankAccount_ID);
            pstmt.setTimestamp(2, tsTo);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);

                String typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.N);
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el movimiento:" + rs.getInt(1));
                }
            }
            rs.close();
            pstmt.close();

            // TIPO DE MOVIMIENTO: Movimiento de Efectivo (M)
            sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id,"
                    + " mf.documentno,"
                    + " mfd.C_MovimientoFondos_Deb_Id,"
                    + " mf.datetrx, "
                    + " mfd.debito,"
                    + " mf.tipo,"
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " null," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero --> Movimientos de fondos (M) no tienen moneda extranjera
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount ba ON (mfd.C_BankAccount_Id = ba.C_BankAccount_Id)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_EFECTIVO + "' AND ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') "
                    + " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
                    + " order by mfd.C_MovimientoFondos_Deb_Id";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BankAccount_ID);
            pstmt.setTimestamp(2, tsTo);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);

                String typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.M);

                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el movimiento:" + rs.getInt(1));
                }
            }
            rs.close();
            pstmt.close();


            //	TIPO DE MOVIMIENTO: Transferencia Bancaria Cobro
            sql = " SELECT "
                    + " 0," //MovimientoFondos_ID
                    + " p.documentno," //DocumentNo
                    + " 0," //Reg_MovimientoFondos
                    + " p.datetrx, "
                    + " vp.importe," //Amt
                    + " 'B'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + "  vp.C_PaymentValores_Id," //C_PaymentValores_ID
                    + " vp.C_Payment_Id," //C_Payment_ID
                    + " null," //NroCheque
                    + " b.name," //AFavor
                    + " null," //VencimientoDate
                    + " vp.realeaseddate," //ReleasedDate
                    + " vp.convertido" //Amt Extranjero 
                    + " FROM C_PaymentValores vp"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.tipo='" + MPAYMENTVALORES.BANCO + "' AND p.docstatus IN ('CO','CL') AND vp.C_BankAccount_Id=? AND p.dateacct <= ?"
                    + " AND vp.C_PaymentValores_Id NOT IN (Select mc.C_PaymentValores_Id From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);

                String typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.Z);
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre la transferencia:" + rs.getInt(1));
                }
            }
            rs.close();
            pstmt.close();

            //	TIPO DE MOVIMIENTO: Transferencia Bancaria Pago
            sql = " SELECT "
                    + " 0," //MovimientoFondos_ID
                    + " p.documentno," //DocumentNo
                    + " 0," //Reg_MovimientoFondos
                    + " vp.debitoDate, " //Efectiva Date
                    + " -vp.importe," //Amt
                    + " 'B'," //Tipo
                    + " vp.C_ValorPago_Id," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " vp.C_Payment_Id," //C_Payment_ID
                    + " null," //NroCheque
                    + " b.name," //AFavor
                    + " null," //VencimientoDate
                    + " vp.realeaseddate," //ReleasedDate
                    + " vp.mextranjera" //Amt Extranjero 
                    + " FROM C_ValorPago vp"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.C_BankAccount_Id=? AND vp.tipo='" + MVALORPAGO.BANCO + "' AND p.docstatus IN ('CO','CL') AND p.dateacct <= ?"
                    + " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                String typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.F);
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre la transferencia:" + rs.getInt(1));
                }
            }
            rs.close();
            pstmt.close();
            
            //	TIPO DE MOVIMIENTO: Emisión de Cheque Propio
             sql = " SELECT "
                    + " 0," //MovimientoFondos_ID
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN mf.documentno"
                        + " ELSE p.documentno"
                        + " END," //DocumentNo
                    + " 0," //Reg_MovimientoFondos
                    + " vp.debitoDate, " //Efectiva Date
                    + " -vp.importe," //Amt
                    + " 'E'," //Tipo
                    + " vp.C_ValorPago_Id," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " vp.C_Payment_Id," //C_Payment_ID
                    + " vp.nrocheque," //NroCheque
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN TO_NCHAR(vp.favor)"
                        + " ELSE b.name"
                        + " END," //AFavor
                    + " vp.paymentdate," //VencimientoDate
                    + " vp.realeaseddate," //ReleasedDate
                    + " vp.mextranjera" //Amt Extranjero
                    + " FROM C_ValorPago vp"
                    + " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.C_BankAccount_Id=? AND (vp.STATE = '" + MVALORPAGO.EMITIDO + "' OR vp.STATE = '" + MVALORPAGO.PENDIENTEDEBITO + "' OR vp.STATE = '" + MVALORPAGO.IMPRESO + "') AND (vp.tipo='" + MVALORPAGO.CHEQUEPROPIO + "' or vp.tipo='" + MVALORPAGO.PCBANKING + "') AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL'))"
                    + " AND (vp.REALEASEDDATE <= ?)"
                    + " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);

                int C_ValorPago_ID = rs.getInt(col_C_ValorPago_ID);
                String nroCheque = rs.getString(col_NroCheque);
                String typeMovement = null;
               
                MVALORPAGO valpay = new MVALORPAGO(Env.getCtx(), C_ValorPago_ID, null);
                if (rs.getInt(col_C_Payment_ID) == 0) {
                    if (valpay.getTIPO() == null ? MVALORPAGO.PCBANKING == null :valpay.getTIPO().equals(MVALORPAGO.PCBANKING)) {
                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.X);
                    } else {
                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.E);
                    }

                } else {
                    if (valpay.getTIPO() == null ? MVALORPAGO.PCBANKING == null : valpay.getTIPO().equals(MVALORPAGO.PCBANKING)) {
                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.Y);
                    } else {
                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.C);
                    }
                }
                
                if ((C_ValorPago_ID != 0) && (!nroCheque.equals(""))) {
                    if (valpay.getEstado().equals("E")) {
                        movConc.setEstado("Emitido");
                    }
                    if (valpay.getEstado().equals("P")) {
                        movConc.setEstado("Pendiente de Débito");
                    }
                    if (valpay.getEstado().equals("C")) {
                        movConc.setEstado("Rechazado");
                    }
                }

                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                // Cambio para que la fecha efectiva tome la fecha de emisión y no la de debito
                movConc.setEfectivaDate(valpay.getReleasedDate());
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el cheque propio:" + rs.getInt(1));
                }
            }

            rs.close();
            pstmt.close();

            //	TIPO DE MOVIMIENTO: Emisión de Cheque Propio
             sql = " SELECT "
                    + " 0," //MovimientoFondos_ID
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN mf.documentno"
                        + " ELSE p.documentno"
                        + " END," //DocumentNo
                    + " 0," //Reg_MovimientoFondos
                    + " vp.debitoDate, " //Efectiva Date
                    + " -vp.importe," //Amt
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN 'R'"
                        + " ELSE 'P'"
                    + " END," //Tipo
                    + " vp.C_ValorPago_Id," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " vp.C_Payment_Id," //C_Payment_ID
                    + " vp.nrocheque," //NroCheque
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN TO_NCHAR(vp.favor)"
                        + " ELSE b.name"
                        + " END," //AFavor
                    + " vp.paymentdate," //VencimientoDate
                    + " vp.realeaseddate," //ReleasedDate
                    + " vp.mextranjera" //Amt Extranjero
                    + " FROM C_ValorPago vp"
                    + " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                    + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                    + " WHERE vp.C_BankAccount_Id=? AND vp.STATE = '" + MVALORPAGO.RECHAZADO + "' AND (vp.tipo='" + MVALORPAGO.CHEQUEPROPIO + "' OR vp.tipo='" + MVALORPAGO.PCBANKING + "') AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL')) "
                    + " AND (mf.datetrx <= ? OR p.dateacct <= ?)"
                    + " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)"
                    + " ORDER BY vp.REALEASEDDATE DESC";
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            pstmt.setTimestamp(3, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                int C_ValorPago_ID = rs.getInt(col_C_ValorPago_ID);
                String nroCheque = rs.getString(col_NroCheque);
                String typeMovement = null;
               
                if (rs.getInt(col_C_Payment_ID) == 0) {
                    typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.H);
                } else {
                    typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.R);
                }
 

                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
  
                MVALORPAGO valpay = new MVALORPAGO(Env.getCtx(), C_ValorPago_ID, null);
                if ((C_ValorPago_ID != 0) && (!nroCheque.equals(""))) {
                    if (valpay.getEstado().equals("E")) {
                        movConc.setEstado("Emitido");
                    }
                    if (valpay.getEstado().equals("P")) {
                        movConc.setEstado("Pendiente de Débito ");
                    }
                    if (valpay.getEstado().equals("C")) {
                        movConc.setEstado("Rechazado");
                    }
                }

                movConc.setEfectivaDate(valpay.getDebitoDate());
                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el cheque propio:" + rs.getInt(1));
                }
            }
            rs.close();
            pstmt.close();
            
            //	TIPO DE MOVIMIENTO: Transferencia Bancaria / Debito (B)
             sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfc.C_MovimientoFondos_Cre_Id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " -mfc.credito," //Amt
                    + " 'B'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_TRANSFERENCIA + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      CRE_TRANSFERENCIA = 'Y')) AND "
                    + "        mfc.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') AND "
                    + "        mfc.C_MovimientoFondos_ID||'-'||mfc.C_MovimientoFondos_Cre_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {                
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.B));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente Transferencia Bancaria / Debito (B");
                }
            }
            rs.close();
            pstmt.close();

            //   TIPO DE MOVIMIENTO: Rechazo de Cheques Propios (P)
             sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfd.C_MovimientoFondos_Deb_Id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " mfd.debito," //Amt
                    + "  'P'," //Tipo
                    + " vp.C_ValorPago_Id," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " vp.C_Payment_Id," //C_Payment_ID
                    + " vp.nrocheque," //NroCheque
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN TO_NCHAR(vp.favor)"
                        + " ELSE b.name"
                        + " END," //AFavor
                    + " vp.paymentdate," //VencimientoDate
                    + " vp.realeaseddate," //ReleasedDate
                    + " vp.mextranjera" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_ValorPago vp ON (vp.C_ValorPago_id = mfd.C_ValorPago_id)"
                    + " LEFT OUTER JOIN C_Payment p ON (vp.C_Payment_id = p.C_Payment_id)"
                    + " LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_id = p.C_BPartner_id)"
                    + " LEFT OUTER JOIN C_MovimientoFondos vpmf ON (vp.C_MovimientoFondos_Id = vpmf.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_MovimientoFondos_Cre mfc ON (vpmf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID and vp.nrocheque = mfc.nrocheque)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_RECHAZO_PROPIOS + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      DEB_CHEQUE_PRO_RECH = 'Y')) AND "
                    + "        mf.DocStatus IN ('CO','CL') AND vp.STATE = '" + MVALORPAGO.RECHAZADO + "' AND "
                    + "        vp.C_BankAccount_Id=? AND mf.datetrx <= ? AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.B));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
                movConc.setEstado("Rechazado");
                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente  Rechazo de Cheques Propios (P)");
                }
            }
            rs.close();
            pstmt.close();

            // TIPO DE MOVIMIENTO: Depósito de Cheques ( D )
            sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfd.C_MovimientoFondos_Deb_Id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " mfd.debito," //Amt
                    + " 'D'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_DEPOSITO + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      (DEB_DEPOSITO = 'Y' OR DEB_CUENTA_BANCO = 'Y'))) AND "
                    + "        mf.DocStatus IN ('CO','CL') AND mfd.C_BankAccount_Id=? AND mf.datetrx <= ? AND "
                    + "        mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.D));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
                
                movConc.setEstado("Depositado");

                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente   Depósito de Cheques ( D )");
                }
            }
            rs.close();
            pstmt.close();

            //       TIPO DE MOVIMIENTO: Rechazo de Cheques Terceros
            sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfd.C_MovimientoFondos_Deb_Id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " -mfd.debito," //Amt
                    + " 'T'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    //+ " INNER JOIN C_BankAccount_Acct baa ON (baa.B_Asset_Acct = mfc.MV_CREDITO_ACCT)"
                    //+ " INNER JOIN C_BankAccount ba ON (baa.C_BankAccount_Id = ba.C_BankAccount_Id)"
                    + " INNER JOIN C_ElementValue cev ON (cev.C_ElementValue_id = mfc.MV_CREDITO_ACCT)"
                    + " INNER JOIN C_BankAccount ba ON (cev.C_BankAccount_Id = ba.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_RECHAZO_TERCEROS + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      (DEB_CHEQUE_TER_RECH = 'Y' OR CRE_CHEQUE_TER_RECH = 'Y'))) AND "
                    + "        ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') AND "
                    + "        mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
                    + " ORDER BY mfc.C_MovimientoFondos_Cre_Id";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.T));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
                
                movConc.setEstado("Rechazado");
                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente Rechazo de Cheques Terceros");
                }
            }
            rs.close();
            pstmt.close();

            //TIPO DE MOVIMIENTO: Depositos Pendientes (Y) y Creditos Bancarios (Z)
            sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfd.C_MovimientoFondos_Deb_Id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " mfd.debito," //Amt
                    + " mf.tipo," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount ba ON (ba.C_BankAccount_Id = mfd.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE + "' OR "
                    + "        mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_CREDITO_BANCARIO + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      (DEB_DEPOSITO_PEND = 'Y' OR DEB_CREDITO_BANCO = 'Y'))) AND ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND "
                    + "        mf.DocStatus IN ('CO','CL') AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
                    + " ORDER BY mfd.C_MovimientoFondos_Deb_Id";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                String typeMovement = null;
                String tipo = null;
                if (rs.getString(col_Tipo).equals("Y")) {
                    typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.S) + " - Débito";
                    tipo = "S";
                } else {
                    typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.K);
                    tipo = "K";
                }
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(typeMovement);
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(tipo);
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));
                
                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente  Depositos Pendientes (Y) y Creditos Bancarios (Z)");
                }
            }
            rs.close();
            pstmt.close();

            // TIPO DE MOVIMIENTO: Depósitos Pendientes
            //TIPO DE MOVIMIENTO: Depositos Pendientes (Y) y Creditos Bancarios (Z)
            sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfc.C_MovimientoFondos_Cre_id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " -mfc.credito," //Amt
                    + " 'S'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " 0" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_BankAccount ba ON (ba.C_BankAccount_Id = mfc.C_BankAccount_Id)"
                    + " WHERE (mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE + "' OR "
                    + "        EXISTS (SELECT 1 "
                    + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                    + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                    + "                      CRE_DEPOSITO_PEND = 'Y')) AND "
                    + "        ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') AND "
                    + "        mfc.C_MovimientoFondos_ID||'-'||mfc.C_MovimientoFondos_Cre_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
                    + " ORDER BY mfc.C_MovimientoFondos_Cre_Id";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.S) + " - Crédito");
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                 if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente Depósitos Pendientes");
                }
            }
            rs.close();
            pstmt.close();

            //	TIPO DE MOVIMIENTO: Transferencia entre cuentas credito
             sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfc.C_MovimientoFondos_Cre_id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " -mfc.credito," //Amt
                    + " 'W'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " -mfc.credito" //Amt Extranjero (Se ingresa en moneda original y el campo convertido esta en pesos)
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias + "' AND mfc.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL')"
                    + " AND mfc.C_MovimientoFondos_ID||'-'||mfc.C_MovimientoFondos_Cre_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BankAccount_ID);
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.W));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                 if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente Transferencia entre cuentas credito");
                }
            }
            rs.close();
            pstmt.close();

            //	TIPO DE MOVIMIENTO: Transferencia entre cuentas debito
             sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfd.C_MovimientoFondos_Deb_id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " mfd.debito," //Amt
                    + " 'W'," //Tipo
                    + " 0," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " 0," //C_Payment_ID
                    + " ''," //NroCheque
                    + " null," //AFavor
                    + " null," //VencimientoDate
                    + " null," //ReleasedDate
                    + " mfd.debito" //Amt Extranjero (Se ingresa en moneda original y el campo convertido esta en pesos)
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias + "' AND mfd.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL')"
                    + " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BankAccount_ID);
            pstmt.setTimestamp(2, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.W));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }

                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente Transferencia entre cuentas debito");
                }
            }
            rs.close();
            pstmt.close();

            //	TIPO DE MOVIMIENTO: Cheques Propios Vencidos (V)
            sql = " SELECT "
                    + " mf.C_MovimientoFondos_Id," //MovimientoFondos_ID
                    + " mf.documentno," //DocumentNo
                    + " mfd.C_MovimientoFondos_Deb_Id," //Reg_MovimientoFondos
                    + " mf.datetrx, " //Efectiva Date
                    + " mfd.debito," //Amt
                    + "  'P'," //Tipo
                    + " vp.C_ValorPago_Id," //C_ValorPago_ID
                    + " 0," //C_PaymentValores_ID
                    + " vp.C_Payment_Id," //C_Payment_ID
                    + " vp.nrocheque," //NroCheque
                    + " CASE vp.C_Payment_Id"
                        + " WHEN 0 THEN TO_NCHAR(vp.favor)"
                        + " ELSE b.name"
                        + " END," //AFavor
                    + " vp.paymentdate," //VencimientoDate
                    + " vp.realeaseddate," //ReleasedDate
                    + " vp.mextranjera" //Amt Extranjero
                    + " FROM C_MovimientoFondos mf"
                    + " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
                    + " INNER JOIN C_ValorPago vp ON (vp.C_ValorPago_id = mfd.C_ValorPago_id)"
                    + " LEFT OUTER JOIN C_Payment p ON (vp.C_Payment_id = p.C_Payment_id)"
                    + " LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_id = p.C_BPartner_id)"
                    + " LEFT OUTER JOIN C_MovimientoFondos vpmf ON (vp.C_MovimientoFondos_Id = vpmf.C_MovimientoFondos_Id)"
                    + " LEFT OUTER JOIN C_MovimientoFondos_Cre mfc ON (vpmf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID and vp.nrocheque = mfc.nrocheque)"
                    + " WHERE mf.TIPO = '" + MMOVIMIENTOFONDOS.MOV_PROPIOS_VENCIDOS + "' AND mf.DocStatus IN ('CO','CL') AND vp.STATE = '" + MVALORPAGO.VENCIDO + "' AND vp.C_BankAccount_Id=? AND mf.datetrx >= ? AND mf.datetrx <= ?"
                    + " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

            Timestamp tsLimit = Timestamp.valueOf("2013-01-01 00:00:00.0");
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, C_BankAccount_ID);
            pstmt.setTimestamp(2, tsLimit);
            pstmt.setTimestamp(3, tsTo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                movConc = new MMOVIMIENTOCONCILIACION(concBancaria);
                
                movConc.setC_BankAccount_ID(C_BankAccount_ID);
                movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.V));
                movConc.setOld(false);
                movConc.setConciliado(false);

                movConc.setC_MovimientoFondos_ID(rs.getInt((col_C_MovimientoFondos_ID)));
                movConc.setDocumentNo(rs.getString(col_DocumentNo));
                movConc.setREG_MovimientoFondos(rs.getInt(col_REG_MovimientoFondos));
                movConc.setEfectivaDate(rs.getTimestamp(col_EfectivaDate));
                movConc.setAmt(rs.getBigDecimal(col_Amt));
                movConc.setTipo(rs.getString(col_Tipo));
                movConc.setC_ValorPago_ID(rs.getInt(col_C_ValorPago_ID));
                movConc.setC_PaymentValores_ID(rs.getInt(col_C_PaymentValores_ID));
                movConc.setC_Payment_ID(rs.getInt(col_C_Payment_ID));
                movConc.setNroCheque(rs.getString(col_NroCheque));
                movConc.setAFavor(rs.getString(col_AFavor));
                movConc.setVencimientoDate(rs.getTimestamp(col_VendimientoDate));
                movConc.setRELEASEDATE(rs.getTimestamp(col_ReleasedDate));

                if (bankAccount.getC_Currency_ID() != 118)  { //ARS
                    movConc.setAmt(rs.getBigDecimal(col_AmtExt));
                }
                
                if (!movConc.save()) {
                    log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente Cheques Propios Vencidos (V)");
                }
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SqlQuery", e);
            System.out.println(sql);
            e.printStackTrace();
        }
    }   //  actualizarPendientes
}   //  VCreateFromConciliacion
