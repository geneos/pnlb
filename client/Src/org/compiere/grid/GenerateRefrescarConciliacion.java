/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.compiere.model.MCONCILIACIONBANCARIA;
import org.compiere.model.MMOVIMIENTOCONCILIACION;
import org.compiere.model.MMOVIMIENTOFONDOS;
import org.compiere.model.MPAYMENTVALORES;
import org.compiere.model.MVALORPAGO;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 * @author Daniel Gini Bision
 */
public class GenerateRefrescarConciliacion extends SvrProcess{

    private int p_instance = 0;
    private long org = 0;

    protected void prepare() {
       	p_instance = getAD_PInstance_ID();
    }

    protected String doIt() throws Exception{

    	MCONCILIACIONBANCARIA concBancaria = new MCONCILIACIONBANCARIA(Env.getCtx(),getRecord_ID(),null);

        // Actualizar Movimientos Pendientes
        actualizarPendientes(concBancaria);

        concBancaria.refrescarSaldos();
        concBancaria.save();

        // Actualizar Movimientos Posteriores
        if (concBancaria.deleteMovPosteriores())
            MCONCILIACIONBANCARIA.completarMovPosteriores(concBancaria);
        
        concBancaria.save();

        return "success";
    }
    
     /*
      * Genera las conciliaciones para items que todavia no esten en la conciliacion.
      */
     
     public void actualizarPendientes(MCONCILIACIONBANCARIA concBancaria)
 	 {
 		//	  Get AccountDate
 		Timestamp tsTo = concBancaria.getToDate();
                Timestamp  tsFrom = concBancaria.getFromDate();
                int C_BankAccount_ID = concBancaria.getC_BankAccount_ID();
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
                
                
 		try
 		{
 			KeyNamePair pp;

           /*
            *  Agregado para tipos dinamicos de movimientos de fondos
            *  Zynnia - 10/07/2012
            */








			/**
			 * 	Zynnia 22/05/2012
                         *      Anexado para separar los valores negociados que deben tomarse a partir del 01/05/2012
                         *      AND mf.datetrx >= to_date('2012/05/01', 'yyyy/mm/dd')
			 */

 			// TIPO DE MOVIMIENTO: Valores Negociados (N)

 			String sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, ba.C_BankAccount_Id, mf.tipo"
 					+	" FROM C_MovimientoFondos mf"
 					+	" INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID = mfd.C_MovimientoFondos_ID)"
                                        +       " INNER JOIN C_BankAccount ba ON (mfd.C_BankAccount_Id = ba.C_BankAccount_Id)"
 					+	" WHERE mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_NEGOCIADOS+"' AND ba.C_BankAccount_Id=? AND mf.datetrx >= to_date('2012/05/01', 'yyyy/mm/dd') AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') "
 					+       " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
 					+	" ORDER BY mfd.C_MovimientoFondos_Deb_Id";

 			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, C_BankAccount_ID);
 			pstmt.setTimestamp(2, tsTo);

 			ResultSet rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				String typeMovement = null;
 				//if (rs.getString(7).equals("N"))
 				typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.N);
				/*else
					typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.M);*/

 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(C_BankAccount_ID);
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(typeMovement);
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo(rs.getString(7));
 				movConc.setConciliado(false);
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
 					log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el movimiento:"+rs.getInt(1));
 			}
 			rs.close();
 			pstmt.close();


 			// 1/9 - TIPO DE MOVIMIENTO: Movimiento de Efectivo (M)
 			/*sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, ba.C_BankAccount_Id, mf.tipo"
 					+	" FROM C_MovimientoFondos mf"
 					+	" INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
 					+	" INNER JOIN C_BankAccount_Acct baa ON (baa.B_Asset_Acct = mfd.MV_DEBITO_ACCT)"
 					+	" INNER JOIN C_BankAccount ba ON (baa.C_BankAccount_Id = ba.C_BankAccount_Id)"
 					+	" WHERE mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_EFECTIVO+"' AND ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') "
 					+   "   AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
 					+	" order by mfd.C_MovimientoFondos_Deb_Id";*/

                        // TIPO DE MOVIMIENTO: Movimiento de Efectivo (M)
			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, ba.C_BankAccount_Id, mf.tipo"
					+	" FROM C_MovimientoFondos mf"
					+	" INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
					+	" INNER JOIN C_BankAccount ba ON (mfd.C_BankAccount_Id = ba.C_BankAccount_Id)"
					+	" WHERE mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_EFECTIVO+"' AND ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') "
					+   "   AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
					+	" order by mfd.C_MovimientoFondos_Deb_Id";


 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, C_BankAccount_ID);
 			pstmt.setTimestamp(2, tsTo);

 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				String typeMovement = null;
 				typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.M);

 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(C_BankAccount_ID);
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(typeMovement);
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo(rs.getString(7));
 				movConc.setConciliado(false);
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
 					log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el movimiento:"+rs.getInt(1));
 			}
 			rs.close();
 			pstmt.close();


 			//	2/9 - TIPO DE MOVIMIENTO: Transferencia Bancaria Cobro
 			/*sql = " SELECT vp.C_PaymentValores_Id, vp.C_Payment_Id, p.documentno, p.datetrx, vp.importe, b.name"
 				+ " FROM C_PaymentValores vp"
 				+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
 				+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
 				+ "	WHERE vp.tipo='"+MPAYMENTVALORES.BANCO+"' AND p.docstatus IN ('CO','CL') AND vp.C_BankAccount_Id=? AND p.dateacct <= ?"
 				+ " AND vp.C_PaymentValores_Id NOT IN (Select mc.C_PaymentValores_Id From C_MOVIMIENTOCONCILIACION mc)";*/

                        //	TIPO DE MOVIMIENTO: Transferencia Bancaria Cobro (Z)
			sql = " SELECT vp.C_PaymentValores_Id, vp.C_Payment_Id, p.documentno, p.datetrx, vp.importe, b.name, vp.realeaseddate "
				+ " FROM C_PaymentValores vp"
				+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
				+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
				+ "	WHERE vp.tipo='"+MPAYMENTVALORES.BANCO+"' AND p.docstatus IN ('CO','CL') AND vp.C_BankAccount_Id=? AND p.dateacct <= ?"
				+ " AND vp.C_PaymentValores_Id NOT IN (Select mc.C_PaymentValores_Id From C_MOVIMIENTOCONCILIACION mc)";




 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);
 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(0);
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(rs.getInt(1));
 				movConc.setC_Payment_ID(rs.getInt(2));
 				movConc.setC_MovimientoFondos_ID(0);
 				movConc.setDocumentNo(rs.getString(3));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.Z));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(rs.getString(6));
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo("B");
 				movConc.setEfectivaDate(rs.getTimestamp(4));
                                movConc.setRELEASEDATE(rs.getTimestamp(7));
 				movConc.setConciliado(false);
//				movPendientes = movPendientes.add(TrxAmt);

 				if (!movConc.save())
 					log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre la transferencia:"+rs.getInt(1));
 			}
 			rs.close();
 			pstmt.close();

 			//	3/9 - TIPO DE MOVIMIENTO: Transferencia Bancaria Pago
 			/*sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, p.datetrx, -vp.importe, b.name"
 				+ " FROM C_ValorPago vp"
 				+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
 				+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
 				+ " WHERE vp.C_BankAccount_Id=? AND vp.tipo='"+MVALORPAGO.BANCO+"' AND p.docstatus IN ('CO','CL') AND p.dateacct <= ?"
 				+ " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";
                        */

                        //	TIPO DE MOVIMIENTO: Transferencia Bancaria Pago (F)
			sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, p.datetrx, -vp.importe, b.name, vp.realeaseddate "
				+ " FROM C_ValorPago vp"
				+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
				+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
				+ " WHERE vp.C_BankAccount_Id=? AND vp.tipo='"+MVALORPAGO.BANCO+"' AND p.docstatus IN ('CO','CL') AND p.dateacct <= ?"
				+ " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";


 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);
 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(0);
 				movConc.setC_ValorPago_ID(rs.getInt(1));
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(rs.getInt(2));
 				movConc.setC_MovimientoFondos_ID(0);
 				movConc.setDocumentNo(rs.getString(3));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.F));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(rs.getString(6));
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo("B");
 				movConc.setConciliado(false);
 				MVALORPAGO valpay = new MVALORPAGO(Env.getCtx(), rs.getInt(1), null);
				movConc.setEfectivaDate(valpay.getDebitoDate());
                                movConc.setRELEASEDATE(rs.getTimestamp(7));
// 				movPendientes = movPendientes.add(TrxAmt);
 				if (!movConc.save())
// 	 				 TODO - Cambiar el mje.
 					log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre la transferencia:"+rs.getInt(1));
 			}

 			rs.close();
 			pstmt.close();

 			//	4/9 - TIPO DE MOVIMIENTO: Emisi�n de Cheque Propio
                        /*sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, mf.C_MovimientoFondos_Id, mf.documentno, mf.datetrx, -vp.importe, vp.nrocheque, vp.favor, b.name, vp.STATE, vp.paymentdate "
 					+ " FROM C_ValorPago vp"
 					+ " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
 					+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
 					+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
 					+ " WHERE vp.C_BankAccount_Id=? AND (vp.STATE = '"+MVALORPAGO.EMITIDO+"' OR vp.STATE = '"+MVALORPAGO.PENDIENTEDEBITO+"') AND vp.tipo='"+MVALORPAGO.CHEQUEPROPIO+"' AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL'))"
 					+ " AND (vp.paymentdate <= ?)"
 				 	+ " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";
                        */
                        //	TIPO DE MOVIMIENTO: Emisión de Cheque Propio
                        sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, mf.C_MovimientoFondos_Id, mf.documentno, mf.datetrx, -vp.importe, vp.nrocheque, vp.favor, b.name, vp.STATE, vp.paymentdate, vp.realeaseddate, vp.tipo "
                                + " FROM C_ValorPago vp"
                                + " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
                                + " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
                                + " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
                                + " WHERE vp.C_BankAccount_Id=? AND (vp.STATE = '"+MVALORPAGO.EMITIDO+"' OR vp.STATE = '"+MVALORPAGO.PENDIENTEDEBITO+"' OR vp.STATE = '"+MVALORPAGO.IMPRESO+"') AND (vp.tipo='"+MVALORPAGO.CHEQUEPROPIO+"' or vp.tipo='"+MVALORPAGO.PCBANKING+"') AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL'))"
                                // debe ser laq fecha de emisión no como ahora: " AND (vp.paymentdate <= ?)"
                                + " AND (vp.REALEASEDDATE <= ?)"
                                + " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";




 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);

 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				int C_MovimientoFondos_ID = 0;
 				int C_Payment_ID = 0;
 				int C_ValorPago_ID = rs.getInt(1);
				String nroCheque = rs.getString(8);
 				String bpartner = null;
 				String documentNo = null;
 				String typeMovement = null;

 				if (rs.getInt(2) == 0)
 				{
                                    if (rs.getString(14) == null ? MVALORPAGO.PCBANKING == null : rs.getString(14).equals(MVALORPAGO.PCBANKING))
                                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.X);
                                    else
                                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.E);
                                    bpartner = rs.getString(9);
                                    C_MovimientoFondos_ID = rs.getInt(4);
                                    documentNo = rs.getString(5);
 				}
 				else
 				{
                                    if (rs.getString(14) == null ? MVALORPAGO.PCBANKING == null : rs.getString(14).equals(MVALORPAGO.PCBANKING))
                                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.Y);
                                    else
                                        typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.C);
                                    bpartner = rs.getString(10);
                                    C_Payment_ID = rs.getInt(2);
                                    documentNo = rs.getString(3);
 				}


 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(0);
 				movConc.setC_ValorPago_ID(C_ValorPago_ID);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(C_Payment_ID);
 				movConc.setC_MovimientoFondos_ID(C_MovimientoFondos_ID);
 				movConc.setDocumentNo(documentNo);
 				movConc.setMovimiento(typeMovement);
 				movConc.setAmt(rs.getBigDecimal(7));
 				movConc.setNroCheque(nroCheque);
 				movConc.setAFavor(bpartner);
 				movConc.setVencimientoDate(rs.getTimestamp(12));
                                movConc.setRELEASEDATE(rs.getTimestamp(13));
 				movConc.setOld(false);
 				movConc.setTipo("E");
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);

 				MVALORPAGO valpay = new MVALORPAGO(Env.getCtx(), C_ValorPago_ID, null);
 				if ( (C_ValorPago_ID != 0) && (!nroCheque.equals("")) )
 				{
 					if (valpay.getEstado().equals("E"))
 						movConc.setEstado("Emitido");
 					if (valpay.getEstado().equals("P"))
 						movConc.setEstado("Pendiente de D�bito");
 					if (valpay.getEstado().equals("C"))
 						movConc.setEstado("Rechazado");
 				}
 				movConc.setEfectivaDate(valpay.getPaymentDate());

 				if (!movConc.save())
                                        log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el cheque propio:"+rs.getInt(1));


 			}

 			rs.close();
 			pstmt.close();
 			//  Emisi�n de Cheque Propio

 			//	5/9 - TIPO DE MOVIMIENTO: Cheque Propio Rechazado
 			/*sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, mf.C_MovimientoFondos_Id, mf.documentno, mf.datetrx, -vp.importe, vp.nrocheque, vp.favor, b.name, vp.paymentdate "
 				+ " FROM C_ValorPago vp"
 				+ " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
 				+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
 				+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
 				+ " WHERE vp.C_BankAccount_Id=? AND vp.STATE = '"+MVALORPAGO.RECHAZADO+"' AND vp.tipo='"+MVALORPAGO.CHEQUEPROPIO+"' AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL')) "
 				+ " AND (mf.datetrx <= ? OR p.dateacct <= ?)"
 				+ " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)";
                        */
                        //	TIPO DE MOVIMIENTO: Cheque Propio Rechazado (H)
			sql = " SELECT vp.C_ValorPago_Id, vp.C_Payment_Id, p.documentno, mf.C_MovimientoFondos_Id, mf.documentno, mf.datetrx, -vp.importe, vp.nrocheque, vp.favor, b.name, vp.paymentdate, vp.realeaseddate "
				+ " FROM C_ValorPago vp"
				+ " LEFT OUTER JOIN C_MovimientoFondos mf ON (mf.C_MovimientoFondos_Id=vp.C_MovimientoFondos_Id)"
				+ " LEFT OUTER JOIN C_Payment p ON (p.C_Payment_ID=vp.C_Payment_ID)"
				+ " LEFT OUTER JOIN C_BPartner b ON (p.c_Bpartner_id=b.c_Bpartner_id)"
				+ " WHERE vp.C_BankAccount_Id=? AND vp.STATE = '"+MVALORPAGO.RECHAZADO+"' AND (vp.tipo='"+MVALORPAGO.CHEQUEPROPIO+"' OR vp.tipo='"+MVALORPAGO.PCBANKING+"') AND (p.docstatus IN ('CO','CL') OR mf.DocStatus IN ('CO','CL')) "
				+ " AND (mf.datetrx <= ? OR p.dateacct <= ?)"
				+ " AND vp.C_ValorPago_Id NOT IN (Select mc.C_ValorPago_Id From C_MOVIMIENTOCONCILIACION mc)"
                                + " ORDER BY vp.REALEASEDDATE DESC";




 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);
 			pstmt.setTimestamp(3, tsTo);

 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				int C_ValorPago_ID = rs.getInt(1);
 				int C_MovimientoFondos_ID = 0;
 				int C_Payment_ID = 0;
 				String nroCheque = rs.getString(8);
 				String documentNo = null;
 				String tipo = null;
 				String bpartner = null;
 				String typeMovement = null;

 				if (rs.getInt(2) == 0)
 				{
 					C_MovimientoFondos_ID = rs.getInt(4);
 					tipo = "R";
 					bpartner = rs.getString(9);
 					documentNo = rs.getString(5);
 					typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.H);
 				}
 				else
 				{
 					C_Payment_ID = rs.getInt(2);
 					tipo = "P";
 					bpartner = rs.getString(10);
 					documentNo = rs.getString(3);
 					typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.R);
 				}

 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(0);
 				movConc.setC_ValorPago_ID(C_ValorPago_ID);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(C_Payment_ID);
 				movConc.setC_MovimientoFondos_ID(C_MovimientoFondos_ID);
 				movConc.setDocumentNo(documentNo);
 				movConc.setMovimiento(typeMovement);
 				movConc.setAmt(rs.getBigDecimal(7));
 				movConc.setNroCheque(nroCheque);
 				movConc.setAFavor(bpartner);
 				movConc.setVencimientoDate(rs.getTimestamp(11));
                                movConc.setRELEASEDATE(rs.getTimestamp(12));
 				movConc.setOld(false);
 				movConc.setTipo(tipo);
				movConc.setConciliado(false);
//				movPendientes = movPendientes.add(TrxAmt);

 				MVALORPAGO valpay = new MVALORPAGO(Env.getCtx(), C_ValorPago_ID, null);
 				if ( (C_ValorPago_ID != 0) && (!nroCheque.equals("")) )
 				{
 					if (valpay.getEstado().equals("E"))
 						movConc.setEstado("Emitido");
 					if (valpay.getEstado().equals("P"))
 						movConc.setEstado("Pendiente de D�bito");
 					if (valpay.getEstado().equals("C"))
 						movConc.setEstado("Rechazado");
 				}

 				movConc.setEfectivaDate(valpay.getDebitoDate());

 				if (!movConc.save())
 					log.log(Level.SEVERE, "Fallo al crear conciliacion pendiente sobre el cheque propio:"+rs.getInt(1));

 			}

 			rs.close();
 			pstmt.close();

 			//	TIPO DE MOVIMIENTO: Transferencia Bancaria / Debito (B)
 			/*sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito"
 				+ " FROM C_MovimientoFondos mf"
 				+ " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
 				*/
                        sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito"
				+ " FROM C_MovimientoFondos mf"
				+ " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
                /*+ " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_TRANSFERENCIA+"' OR "
                + "        EXISTS (SELECT 1 "
                + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                + "                      (DEB_CUENTA_DEBITO = 'Y' OR CRE_TRANSFERENCIA = 'Y'))) AND "
                */
                + " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_TRANSFERENCIA+"' OR "
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

 			while (rs.next())
 			{
 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.B));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo("B");
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
// TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
 			}

 			rs.close();
 			pstmt.close();

                        /*
                         *  Zynnia - 12/06/2012
                         *  Modificación por error de repetición de anulados
                         *  Se eliminaron dos LEFT OUTER JOIN que generaban multiples repeticiones de registros para sacar a favor de
                         *  que ahora se pone como vacio.
                         *  JF
                         *
                         */

 			//   TIPO DE MOVIMIENTO: Rechazo de Cheques Propios (P)
 			/*sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, vp.C_ValorPago_Id, vp.nrocheque, vp.paymentdate, b.name, vp.C_Payment_id, ''"
 				+ " FROM C_MovimientoFondos mf"
 				+ " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
 				+ " INNER JOIN C_ValorPago vp ON (vp.C_ValorPago_id = mfd.C_ValorPago_id)"
 				+ " LEFT OUTER JOIN C_Payment p ON (vp.C_Payment_id = p.C_Payment_id)"
 				+ " LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_id = p.C_BPartner_id)"
 				*/
                //	TIPO DE MOVIMIENTO: Rechazo de Cheques Propios (P)
			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, vp.C_ValorPago_Id, vp.nrocheque, vp.paymentdate, b.name, vp.C_Payment_id, mfc.favor, vp.realeaseddate "
				+ " FROM C_MovimientoFondos mf"
				+ "	INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
				+ " INNER JOIN C_ValorPago vp ON (vp.C_ValorPago_id = mfd.C_ValorPago_id)"
				+ " LEFT OUTER JOIN C_Payment p ON (vp.C_Payment_id = p.C_Payment_id)"
				+ " LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_id = p.C_BPartner_id)"
				+ " LEFT OUTER JOIN C_MovimientoFondos vpmf ON (vp.C_MovimientoFondos_Id = vpmf.C_MovimientoFondos_Id)"
                                /*
                                 *  13/06/2013 Maria Jesus Martin
                                 *  Agregado en la consulta para que el nroCheque del Valor Pago sea el mismo que el de Movimiento de Fondos Cre,
                                 *  ya que sino trae movimientos de fondos duplicados pero con distintos numeros de cheques.
                                 */
				+ " LEFT OUTER JOIN C_MovimientoFondos_Cre mfc ON (vpmf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID and vp.nrocheque = mfc.nrocheque)"

                                /*
                + " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_RECHAZO_PROPIOS+"' "
                + "        EXISTS (SELECT 1 "
                + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                + "                      (DEB_CHEQUE_PROPIO = 'Y' OR CRE_CUENTA_CREDITO = 'Y'))) AND "
                */
                + " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_RECHAZO_PROPIOS+"' OR "
                + "        EXISTS (SELECT 1 "
                + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                + "                      DEB_CHEQUE_PRO_RECH = 'Y')) AND "
                + "        mf.DocStatus IN ('CO','CL') AND vp.STATE = '"+MVALORPAGO.RECHAZADO+"' AND "
                + "        vp.C_BankAccount_Id=? AND mf.datetrx <= ? AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);

 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				String bpartner = null;
 				if (rs.getInt(10) == 0)
 					bpartner = rs.getString(11);
 				else
 					bpartner = rs.getString(9);

 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(rs.getInt(6));
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(rs.getInt(10));
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.P));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque(rs.getString(7));
 				movConc.setAFavor(bpartner);
 				movConc.setVencimientoDate(rs.getTimestamp(8));
 				movConc.setOld(false);
 				movConc.setTipo("P");
 				movConc.setConciliado(false);
//				movPendientes = movPendientes.add(TrxAmt);

 				movConc.setEstado("Rechazado");
 				movConc.setEfectivaDate(rs.getTimestamp(4));
                                movConc.setRELEASEDATE(rs.getTimestamp(12));

 				if (!movConc.save())
// TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");

 			}

 			rs.close();
 			pstmt.close();

 			// TIPO DE MOVIMIENTO: Depósito de Cheques ( D )
 			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito"
 				+ " FROM C_MovimientoFondos mf"
 				+ " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"


                                /*
                + " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_DEPOSITO+"' OR "
                + "        EXISTS (SELECT 1 "
                + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                + "                      (DEB_CUENTA_BANCO = 'Y' OR CRE_CHEQUE_TERCERO = 'Y'))) AND "
                */
                + " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_DEPOSITO+"' OR "
                + "        EXISTS (SELECT 1 "
                + "                FROM ZYN_DYNAMIC_MOVFONDOS "
                + "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                + "                      DEB_DEPOSITO = 'Y')) AND "
                + "        mf.DocStatus IN ('CO','CL') AND mfd.C_BankAccount_Id=? AND mf.datetrx <= ? AND "
                + "        mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

 			pstmt = DB.prepareStatement(sql.toString(), null);
 			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);

 			rs = pstmt.executeQuery();

 			while (rs.next())
 			{
 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.D));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo("D");
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);

 				movConc.setEstado("Depositado");
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
// 	 TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
 			}

 			rs.close();
 			pstmt.close();


 			//       TIPO DE MOVIMIENTO: Rechazo de Cheques Terceros
 			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, -mfd.debito"
 				+ " FROM C_MovimientoFondos mf"
 				+ " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
 				+ " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
 				+ " INNER JOIN C_BankAccount_Acct baa ON (baa.B_Asset_Acct = mfc.MV_CREDITO_ACCT)"
 				+ " INNER JOIN C_BankAccount ba ON (baa.C_BankAccount_Id = ba.C_BankAccount_Id)"
 				+ " WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_RECHAZO_TERCEROS+"' OR "
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

 			while (rs.next())
 			{
 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.T));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setTipo("T");
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);
 				movConc.setEstado("Rechazado");
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
// 	 TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
 			}

 			rs.close();
 			pstmt.close();

 			//TIPO DE MOVIMIENTO: Dep�sitos Pendientes (Y) y Cr�ditos Bancarios (Z)
			sql =	" SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, ba.C_BankAccount_Id, mf.tipo"
				+	" FROM C_MovimientoFondos mf"
				+	" INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
				+	" INNER JOIN C_BankAccount ba ON (ba.C_BankAccount_Id = mfd.C_BankAccount_Id)"
				/*
                +	" WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE+"' OR "
                +   "        mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_CREDITO_BANCARIO+"' OR "
                +   "        EXISTS (SELECT 1 "
                +   "                FROM ZYN_DYNAMIC_MOVFONDOS "
                +   "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                +   "                      (DEB_CUENTA_BANCO = 'Y' OR CRE_CUENTA_BANCO = 'Y' OR "
                +   "                       CRE_CUENTA_BANCO_DESC = 'Y'))) AND ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND "
                */
                +	" WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE+"' OR "
                +   "        mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_CREDITO_BANCARIO+"' OR "
                +   "        EXISTS (SELECT 1 "
                +   "                FROM ZYN_DYNAMIC_MOVFONDOS "
                +   "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                +   "                      (DEB_DEPOSITO_PEND = 'Y' OR DEB_CREDITO_BANCO = 'Y'))) AND ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND "
                +   "        mf.DocStatus IN ('CO','CL') AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
				+	" ORDER BY mfd.C_MovimientoFondos_Deb_Id";

			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);

			rs = pstmt.executeQuery();

			while (rs.next())
			{
				String typeMovement = null;
				String tipo = null;
 				if (rs.getString(7).equals("Y"))
 				{	typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.S) + " - Débito";
 					tipo ="S";
 				}
				else
				{	typeMovement = MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.K);
					tipo ="K";
 				}

				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(typeMovement);
 				movConc.setTipo(tipo);
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
// TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
			}

			rs.close();
			pstmt.close();

			/**
			 * 	CORREGIDO 11 de 11
			 */
			// TIPO DE MOVIMIENTO: Depósitos Pendientes
			sql =	" SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito, ba.C_BankAccount_Id, mf.tipo"
				+	" FROM C_MovimientoFondos mf"
				+	" INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
				+	" INNER JOIN C_BankAccount ba ON (ba.C_BankAccount_Id = mfc.C_BankAccount_Id)"
				/*
                +	" WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE+"' OR "
                +   "        EXISTS (SELECT 1 "
                +   "                FROM ZYN_DYNAMIC_MOVFONDOS "
                +   "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                +   "                      (DEB_CUENTA_BANCO = 'Y' OR CRE_CUENTA_BANCO = 'Y'))) AND "
                */
                +	" WHERE (mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_DEPOSITO_PENDIENTE+"' OR "
                +   "        EXISTS (SELECT 1 "
                +   "                FROM ZYN_DYNAMIC_MOVFONDOS "
                +   "                WHERE CODE = mf.TIPO AND ISACTIVE = 'Y' AND "
                +   "                      CRE_DEPOSITO_PEND = 'Y')) AND "
                +   "        ba.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL') AND "
                +   "        mfc.C_MovimientoFondos_ID||'-'||mfc.C_MovimientoFondos_Cre_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)"
				+	" ORDER BY mfc.C_MovimientoFondos_Cre_Id";

			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, concBancaria.getC_BankAccount_ID());
 			pstmt.setTimestamp(2, tsTo);

			rs = pstmt.executeQuery();

			while (rs.next())
			{
				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.S) + " - Crédito");
 				movConc.setTipo("S");
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
// TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
			}

			rs.close();
			pstmt.close();

                        //	TIPO DE MOVIMIENTO: Transferencia entre cuentas credito

			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfc.C_MovimientoFondos_Cre_Id, mf.datetrx, -mfc.credito"
				+ " FROM C_MovimientoFondos mf"
				+ " INNER JOIN C_MovimientoFondos_Cre mfc ON (mf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID)"
				+ " WHERE mf.TIPO = '"+MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias+"' AND mfc.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL')"
				+ " AND mfc.C_MovimientoFondos_ID||'-'||mfc.C_MovimientoFondos_Cre_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, C_BankAccount_ID);
 			pstmt.setTimestamp(2, tsTo);

			rs = pstmt.executeQuery();

			while (rs.next())
			{
				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(0);
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(0);
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.W));
 				movConc.setTipo("W");
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque("");
 				movConc.setAFavor(null);
 				movConc.setVencimientoDate(null);
 				movConc.setOld(false);
 				movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);
 				movConc.setEfectivaDate(rs.getTimestamp(4));

 				if (!movConc.save())
// TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
			}

                        rs.close();
			pstmt.close();

                        //	TIPO DE MOVIMIENTO: Transferencia entre cuentas debito

			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito"
				+ " FROM C_MovimientoFondos mf"
				+ " INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
				+ " WHERE mf.TIPO = '"+MMOVIMIENTOFONDOS.TIPO_TransferenciaCuentasBancarias+"' AND mfd.C_BankAccount_Id=? AND mf.datetrx <= ? AND mf.DocStatus IN ('CO','CL')"
				+ " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";

			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, C_BankAccount_ID);
 			pstmt.setTimestamp(2, tsTo);

			rs = pstmt.executeQuery();

			while (rs.next())
			{
                            MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);
                            movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
                            movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
                            movConc.setREG_MovimientoFondos(rs.getInt(3));
                            movConc.setC_ValorPago_ID(0);
                            movConc.setC_PaymentValores_ID(0);
                            movConc.setC_Payment_ID(0);
                            movConc.setC_MovimientoFondos_ID(rs.getInt(1));
                            movConc.setDocumentNo(rs.getString(2));
                            movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.W));
                            movConc.setTipo("W");
                            movConc.setAmt(rs.getBigDecimal(5));
                            movConc.setNroCheque("");
                            movConc.setAFavor(null);
                            movConc.setVencimientoDate(null);
                            movConc.setOld(false);
                            movConc.setConciliado(false);
// 				movPendientes = movPendientes.add(TrxAmt);
                            movConc.setEfectivaDate(rs.getTimestamp(4));

                            if (!movConc.save())
// TODO CAMBIAR
 					log.log(Level.SEVERE, "Line not created #");
			}

			rs.close();
			pstmt.close();


                        //	TIPO DE MOVIMIENTO: Cheques Propios Vencidos (V)
			sql = " SELECT mf.C_MovimientoFondos_Id, mf.documentno, mfd.C_MovimientoFondos_Deb_Id, mf.datetrx, mfd.debito, vp.C_ValorPago_Id, vp.nrocheque, vp.paymentdate, b.name, vp.C_Payment_id, mfc.favor, vp.realeaseddate "
				+ " FROM C_MovimientoFondos mf"
				+ "	INNER JOIN C_MovimientoFondos_Deb mfd ON (mf.C_MovimientoFondos_ID=mfd.C_MovimientoFondos_ID)"
				+ " INNER JOIN C_ValorPago vp ON (vp.C_ValorPago_id = mfd.C_ValorPago_id)"
				+ " LEFT OUTER JOIN C_Payment p ON (vp.C_Payment_id = p.C_Payment_id)"
				+ " LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_id = p.C_BPartner_id)"
				+ " LEFT OUTER JOIN C_MovimientoFondos vpmf ON (vp.C_MovimientoFondos_Id = vpmf.C_MovimientoFondos_Id)"

                                /*
                                 * GENEOS - Pablo Velaquez
                                 * 26/06/2013
                                 * Agregado en la consulta para que el nroCheque del Valor Pago sea el mismo que el de Movimiento de Fondos Cre,
                                 * ya que sino trae movimientos de fondos duplicados pero con distintos numeros de cheques.
                                 */

                                + " LEFT OUTER JOIN C_MovimientoFondos_Cre mfc ON (vpmf.C_MovimientoFondos_ID=mfc.C_MovimientoFondos_ID and vp.nrocheque = mfc.nrocheque)"
				+ " WHERE mf.TIPO = '"+MMOVIMIENTOFONDOS.MOV_PROPIOS_VENCIDOS+"' AND mf.DocStatus IN ('CO','CL') AND vp.STATE = '"+MVALORPAGO.VENCIDO+"' AND vp.C_BankAccount_Id=? AND mf.datetrx >= ? AND mf.datetrx <= ?"
				+ " AND mfd.C_MovimientoFondos_ID||'-'||mfd.C_MovimientoFondos_Deb_Id NOT IN (Select mc.C_MovimientoFondos_ID||'-'||mc.REG_MovimientoFondos From C_MOVIMIENTOCONCILIACION mc)";
//                                  and vp.nrocheque = mfd.nrocheque



			Timestamp tsLimit = Timestamp.valueOf("2013-01-01 00:00:00.0");
                        pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, C_BankAccount_ID);
                        pstmt.setTimestamp(2, tsLimit);
                        pstmt.setTimestamp(3, tsTo);

			rs = pstmt.executeQuery();

			while (rs.next())
			{
				String bpartner = null;
 				if (rs.getInt(10) == 0)
 					bpartner = rs.getString(11);
 				else
 					bpartner = rs.getString(9);

 				MMOVIMIENTOCONCILIACION movConc = new MMOVIMIENTOCONCILIACION (concBancaria);

 				movConc.setC_BankAccount_ID(concBancaria.getC_BankAccount_ID());
 				movConc.setC_ConciliacionBancaria_ID(concBancaria.getC_ConciliacionBancaria_ID());
 				movConc.setREG_MovimientoFondos(rs.getInt(3));
 				movConc.setC_ValorPago_ID(rs.getInt(6));
 				movConc.setC_PaymentValores_ID(0);
 				movConc.setC_Payment_ID(rs.getInt(10));
 				movConc.setC_MovimientoFondos_ID(rs.getInt(1));
 				movConc.setDocumentNo(rs.getString(2));
 				movConc.setMovimiento(MCONCILIACIONBANCARIA.getTexto(MCONCILIACIONBANCARIA.V));
 				movConc.setAmt(rs.getBigDecimal(5));
 				movConc.setNroCheque(rs.getString(7));
 				movConc.setAFavor(bpartner);
 				movConc.setVencimientoDate(rs.getTimestamp(8));
 				movConc.setOld(false);
 				movConc.setTipo("V");
 				movConc.setConciliado(false);
//				movPendientes = movPendientes.add(TrxAmt);

 				movConc.setEstado("Vencido");
 				movConc.setEfectivaDate(rs.getTimestamp(4));
                                movConc.setRELEASEDATE(rs.getTimestamp(12));

                                if (!movConc.save())
 					log.log(Level.SEVERE, "Line not created #");
			}

			rs.close();
			pstmt.close();



 		}
 		catch (SQLException e)
 		{
 			log.log(Level.SEVERE, "SqlQuery", e);
 		}

 	}   //  loadBankAccount

    

}
