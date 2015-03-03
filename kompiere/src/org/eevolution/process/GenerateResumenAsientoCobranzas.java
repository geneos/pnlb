/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MDocType;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MSequence;
import org.compiere.process.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.logging.*;

import org.compiere.util.*;
/**
 *  Esta clase inserta tuplas en la tabla temporal T_LIBRO_IVA_VENTA luego de un previo filtrado por fecha 
 *  y calculos posteriores.
 *
 *	@author BISion
 *	@version 1.0
 */
public class GenerateResumenAsientoCobranzas extends SvrProcess{

    //private int p_PInstance_ID;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Integer AD_CLIENT_ID = 0;
    private Integer AD_ORG_ID = 0;
    private Integer C_ACCTSCHEMA_ID = 0;
    private Boolean invoice = false;
    private Integer AD_TABLE_ID = 0;
    private Integer RECORD_ID = 0;
    private String DESCRIPTION = "";
    private String msg = null;
    private BigDecimal C_Period_ID = BigDecimal.ZERO;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        BigDecimal SCHEMA = BigDecimal.ZERO;
        RECORD_ID = 0;
        DESCRIPTION = "Resumen asiento Cobranzas";
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            //Resumen de un dia ?
            if(name.equals("DATEACCT")){
            	fromDate=(Timestamp)para[i].getParameter();
            }
            else
                //Resumen de un periodo ?
                    if(name.equals("C_Period_ID")){
                            C_Period_ID = (BigDecimal)para[i].getParameter();
                            MPeriod per = MPeriod.get(Env.getCtx(), C_Period_ID.intValue(), get_TrxName());
                            fromDate=per.getStartDate();
                            toDate = per.getEndDate();
                }
                //Esquema contable
                    else
                            SCHEMA = ((BigDecimal)para[i].getParameter());
        }
        C_ACCTSCHEMA_ID = SCHEMA.intValue();

    }
   
    protected String doIt() {
        try {
        	
        	if (fromDate == null)
        		msg = "El proceso ha sido finalizado. Debe seleccionar un d�a o un per�odo.";
        	else
        	{	
                    loadRecord();
                    deleteResumenes();
                    createFactsResumen(loadResumen());
                    DB.commit(true, get_TrxName());
	    		
        	}
        } catch (SQLException ex) {
            Logger.getLogger(GenerateResumenAsientoCobranzas.class.getName()).log(Level.SEVERE, null, ex);
            try {
                DB.rollback(true, get_TrxName());
            }
            catch (SQLException e){}          
        }
        
        if (msg!=null)
        	return msg;
        
        return "El proceso ha finalizado con �xito.";
    }


    /*
    protected char revisarSuperposicion()
    {
    	try {
    		String sql = " SELECT FACT_ACCT_RESUMEN_ID " +
    					 " FROM FACT_ACCT_RESUMEN " +
    					 " WHERE ((? >= FROMDATE AND ? <= TODATE AND ? > TODATE) " +
    					 "	  OR (? >= FROMDATE AND ? <= TODATE AND ? < FROMDATE)) " +
    					 "	  AND INVOICE = 'Y'";	    	
    		
	    	PreparedStatement ps = DB.prepareStatement(sql,null);
	    	ps.setTimestamp(1, fromDate);
	    	ps.setTimestamp(2, fromDate);
	    	ps.setTimestamp(3, toDate);
	    	
	    	ps.setTimestamp(4, toDate);
	    	ps.setTimestamp(5, toDate);
	    	ps.setTimestamp(6, fromDate);
	    	
	    	ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next())
	    		return 'S';
	    	
	    	rs.close();
	    	ps.close();

	    	sql = " SELECT FACT_ACCT_RESUMEN_ID " +
			  " FROM FACT_ACCT_RESUMEN " +
			  " WHERE ? <= FROMDATE AND ? >= TODATE " +
			  "	  AND INVOICE = 'Y'";	    	

		  	ps = DB.prepareStatement(sql,null);
		  	ps.setTimestamp(1, fromDate);
		  	ps.setTimestamp(2, toDate);
		  	
		  	rs = ps.executeQuery();
		  	if (rs.next())
		  		return 'I';
		  	
		  	rs.close();
		  	ps.close();
		  	
	    	sql = " SELECT FACT_ACCT_RESUMEN_ID " +
				  " FROM FACT_ACCT_RESUMEN " +
				  " WHERE ? >= FROMDATE AND ? <= TODATE " +
				  "	  AND ? >= FROMDATE AND ? <= TODATE " +
  		  		  "	  AND INVOICE = 'Y'";	    	

	    	ps = DB.prepareStatement(sql,null);
	    	ps.setTimestamp(1, fromDate);
	    	ps.setTimestamp(2, fromDate);
	    	ps.setTimestamp(3, toDate);
	    	ps.setTimestamp(4, toDate);
	    	
	    	rs = ps.executeQuery();
	    	if (rs.next())
	    		return 'A';
	    	
	    	rs.close();
	    	ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return 'P';
		}

		return 'O';
    }*/
    
    protected void deleteResumenes() throws  SQLException
    {
    	/*try {*/
    		String sql;

            //Busco Resumenes de Pago en ese mismo rango
            sql = " SELECT FACT_ACCT_ID " +
              " FROM FACT_ACCT " +
              " WHERE DATEACCT >= ? AND DATEACCT <= ? " +
              "	  AND DESCRIPTION = '"+DESCRIPTION+"'";

            PreparedStatement ps = DB.prepareStatement(sql,null);
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);

            ResultSet rs = ps.executeQuery();
            //Por Cada uno elimino el mapeo de en FACT_ACCT_RESUMEN
            while (rs.next())
            {
                int res_id = rs.getInt(1);
                mappingFromResumen(res_id);
            }

        /*    }
        catch (SQLException e) {
                e.printStackTrace();
        }*/
    }

        protected void createFactsResumen(ResultSet rs) throws  SQLException
	{
            MAcctSchema as = MAcctSchema.get(Env.getCtx(), C_ACCTSCHEMA_ID);
            int LINE_ID = 1;
           /* try{*/
                while (rs.next())
                {
                    if (!rs.getBigDecimal(2).equals(BigDecimal.ZERO) || !rs.getBigDecimal(3).equals(BigDecimal.ZERO))
                    {	createFactLine(LINE_ID,rs.getInt(1),DESCRIPTION,rs.getBigDecimal(2),rs.getBigDecimal(3),as.getC_Currency_ID());
                            LINE_ID++;
                            if (RECORD_ID > 0)
                                mappingToResumen(rs.getInt(1));
                    }
                }
           /* }catch(SQLException e){
                e.printStackTrace();
            }*/
	}


    /*
     * Metodo que crea la FactLine del resumen para una cuenta y actualiza RECORD_ID al ID de la misma.
     */
    private void createFactLine(int LINE_ID,int account_id,String descripcion,BigDecimal cargo, BigDecimal abono, int currency_id)
    throws  SQLException
    {
        String sqlInsert =
                    " INSERT INTO FACT_ACCT (FACT_ACCT_ID, AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, CREATED, CREATEDBY, UPDATED, " +
            "		UPDATEDBY, C_ACCTSCHEMA_ID, ACCOUNT_ID, DATETRX, DATEACCT, C_PERIOD_ID, AD_TABLE_ID, RECORD_ID, DESCRIPTION," +
            "		LINE_ID, GL_CATEGORY_ID, POSTINGTYPE, C_CURRENCY_ID, AMTSOURCEDR, AMTSOURCECR, AMTACCTDR, AMTACCTCR) " +
            " VALUES (?,?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	PreparedStatement pstmt = DB.prepareStatement(sqlInsert, get_TrxName());

    	/*try	{*/
                RECORD_ID = MSequence.getNextID(AD_CLIENT_ID, "Fact_Acct", get_TrxName());
    		pstmt.setLong(1, RECORD_ID);
	    	pstmt.setLong(2, AD_CLIENT_ID);
	    	pstmt.setLong(3, AD_ORG_ID);
	    	pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	    	pstmt.setLong(5, getAD_User_ID());
	    	pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
	    	pstmt.setLong(7, getAD_User_ID());
	    	pstmt.setLong(8, C_ACCTSCHEMA_ID);
	    	pstmt.setLong(9, account_id);
	    	pstmt.setTimestamp(10, toDate);
	    	pstmt.setTimestamp(11, toDate);
	    	pstmt.setLong(12, MPeriod.getC_Period_ID(Env.getCtx(), toDate));
	    	pstmt.setLong(13, AD_TABLE_ID);
	    	pstmt.setLong(14, RECORD_ID);
	    	pstmt.setString(15, descripcion);
		pstmt.setInt(16, LINE_ID);
	    	pstmt.setLong(17, 0);
	    	pstmt.setString(18, "A");
	    	pstmt.setLong(19, currency_id);
	    	pstmt.setBigDecimal(20, cargo);
	    	pstmt.setBigDecimal(21, abono);
	    	pstmt.setBigDecimal(22, cargo);
	    	pstmt.setBigDecimal(23, abono);

	    	pstmt.executeQuery();
	       /* DB.commit(true, get_TrxName());
    	}catch (SQLException e)	{
    		e.printStackTrace();
    	}*/

    }

    
    protected ResultSet loadResumen() throws SQLException
	{
    	String sqlQuery = "";
    	PreparedStatement pstmt = null;
    	
    	/*try {*/

            /*
             * Obtengo las cuentas con sus respectivo CREDITO y DEBITO entre 2 fechas
             */

              sqlQuery= " SELECT ACCOUNT_ID," +
                        "    CASE WHEN SUM(DEBITO)>SUM(CREDITO) THEN SUM(DEBITO)-SUM(CREDITO) ELSE 0 END AS DEBITO," +
                        "    CASE WHEN SUM(DEBITO)<SUM(CREDITO) THEN SUM(CREDITO)-SUM(DEBITO) ELSE 0 END AS CREDITO " +
                        " FROM (" +
                        " 		SELECT fa.ACCOUNT_ID," +
                        "			   CASE WHEN SUM(fa.AMTACCTDR)>SUM(fa.AMTACCTCR) THEN SUM(fa.AMTACCTDR)-SUM(fa.AMTACCTCR) ELSE 0 END AS DEBITO," +
                        "			   CASE WHEN SUM(fa.AMTACCTDR)<SUM(fa.AMTACCTCR) THEN SUM(fa.AMTACCTCR)-SUM(fa.AMTACCTDR) ELSE 0 END AS CREDITO" +
                        "		FROM Fact_Acct fa" +
                        "		INNER JOIN AD_TABLE t ON (t.AD_TABLE_ID = fa.AD_TABLE_ID)" +
                        "		WHERE  t.TABLENAME = 'C_AllocationHdr' AND fa.DATEACCT BETWEEN ? AND ?" +
                        "		  AND fa.ISACTIVE = 'Y' AND fa.C_ACCTSCHEMA_ID = ?" +
                        "		  AND fa.RECORD_ID in "+
                                            "(SELECT DISTINCT al.C_AllocationHdr_id FROM c_allocationline al "+
                                            " INNER JOIN C_Payment p ON (al.C_payment_id = p.c_payment_id) "+
                                            " WHERE C_AllocationHdr_id=fa.RECORD_ID"+
                                            " AND p.isreceipt = 'Y')" +
                        "		GROUP BY fa.ACCOUNT_ID" +
                        "		UNION ALL" +
                        "		SELECT fa.ACCOUNT_ID," +
                        "			   CASE WHEN SUM(fa.AMTACCTDR)>SUM(fa.AMTACCTCR) THEN SUM(fa.AMTACCTDR)-SUM(fa.AMTACCTCR) ELSE 0 END AS DEBITO," +
                        "			   CASE WHEN SUM(fa.AMTACCTDR)<SUM(fa.AMTACCTCR) THEN SUM(fa.AMTACCTCR)-SUM(fa.AMTACCTDR) ELSE 0 END AS CREDITO" +
                        "		FROM Fact_Acct fa" +
                        "		INNER JOIN AD_TABLE t ON (t.AD_TABLE_ID = fa.AD_TABLE_ID)" +
                        "		INNER JOIN C_PAYMENT p ON (p.C_PAYMENT_ID = fa.RECORD_ID)" +
                        "		WHERE t.TABLENAME = 'C_Payment' AND fa.DATEACCT BETWEEN ? AND ?" +
                        "		  AND fa.ISACTIVE = 'Y' AND fa.C_ACCTSCHEMA_ID = ? AND p.isreceipt = 'Y'" +
                        "		GROUP BY fa.ACCOUNT_ID" +
                        "	 )" +
                        " GROUP BY ACCOUNT_ID";

                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                pstmt.setTimestamp(1, fromDate);
                pstmt.setTimestamp(2, toDate);
                pstmt.setLong(3, C_ACCTSCHEMA_ID);
                pstmt.setTimestamp(4, fromDate);
                pstmt.setTimestamp(5, toDate);
                pstmt.setLong(6, C_ACCTSCHEMA_ID);
	    	return pstmt.executeQuery();
    	/*}catch (SQLException e){
    		e.printStackTrace();
    	}    	
    	return null;*/
	}

    private void mappingFromResumen(int resumen_id) throws  SQLException
    {
        PreparedStatement pstmt = null;
        PreparedStatement delete = null;
        PreparedStatement update = null;
        /*try {*/
            String querySel = "SELECT FACT_ACCT_ID FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ?";
            pstmt = DB.prepareStatement(querySel, get_TrxName());
            pstmt.setLong(1, resumen_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                //Activo el asiento en FACT_ACCT
                String queryUpdate = "UPDATE FACT_ACCT SET ISACTIVE = 'Y' WHERE FACT_ACCT_ID = ?";
                update = DB.prepareStatement(queryUpdate, get_TrxName());
                update.setLong(1, rs.getLong(1));
                update.executeQuery();
                update.close();
            }
            rs.close();
            pstmt.close();
            //Borro los asientos de FACT_ACCT_RESUMEN
            String queryDelete = "DELETE FROM FACT_ACCT_RESUMEN WHERE RECORD_RES_ID = ?";
            delete = DB.prepareStatement(queryDelete, get_TrxName());
            delete.setLong(1, resumen_id);
            delete.executeQuery();
            delete.close();
            //Borro Resumen de FACT_ACCT
            queryDelete = "DELETE FROM FACT_ACCT WHERE FACT_ACCT_ID = ?";
            delete = DB.prepareStatement(queryDelete, get_TrxName());
            delete.setLong(1, resumen_id);
            delete.executeQuery();
            delete.close();

            //COMMIT
	    /*DB.commit(true, get_TrxName());
        }
        catch (SQLException e){
            e.printStackTrace();
        }*/
    }

     private void mappingToResumen(int account_id) throws  SQLException
    {

        String sqlQuery = "";
    	PreparedStatement pstmt = null;
        PreparedStatement insert = null;
        PreparedStatement update = null;
    	long FACT_ACCT_RESUMEN_ID = 0;

    	/*try {*/
                // Obtengo el siguiente ID para FACT_ACCT_RESUMEN

    		sqlQuery = "SELECT MAX(FACT_ACCT_RESUMEN_ID) FROM FACT_ACCT_RESUMEN";
    		pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next())
    			FACT_ACCT_RESUMEN_ID = rs.getLong(1);
    		rs.close();
    		pstmt.close();

                //Obtengo los registros de FACT_ACCT que tengo que mover a FACT_ACCT_RESUMEN
	    	sqlQuery= " SELECT * " +
			    " FROM (" +
			    " 		SELECT fa.* " +
			    "		FROM Fact_Acct fa" +
			    "		INNER JOIN AD_TABLE t ON (t.AD_TABLE_ID = fa.AD_TABLE_ID)" +
			    "		WHERE  t.TABLENAME = 'C_AllocationHdr' AND fa.DATEACCT BETWEEN ? AND ?" +
			    "		  AND fa.ISACTIVE = 'Y' AND fa.C_ACCTSCHEMA_ID = ?" +
			     "		  AND fa.RECORD_ID in "+
                                                "(SELECT DISTINCT al.C_AllocationHdr_id FROM c_allocationline al "+
                                                " INNER JOIN C_Payment p ON (al.C_payment_id = p.c_payment_id) "+
                                                " WHERE C_AllocationHdr_id=fa.RECORD_ID"+
                                                " AND p.isreceipt = 'Y')" +
			    "		UNION ALL" +
			    "		SELECT fa.*" +
			    "		FROM Fact_Acct fa" +
			    "		INNER JOIN AD_TABLE t ON (t.AD_TABLE_ID = fa.AD_TABLE_ID)" +
                            "		INNER JOIN C_PAYMENT p ON (p.C_PAYMENT_ID = fa.RECORD_ID)" +
			    "		WHERE t.TABLENAME = 'C_Payment' AND fa.DATEACCT BETWEEN ? AND ?" +
			    "		  AND fa.ISACTIVE = 'Y' AND fa.C_ACCTSCHEMA_ID = ? AND p.isreceipt = 'Y'" +
			    "	 )" +
			    " WHERE ACCOUNT_ID ="+account_id;

                PreparedStatement pst = DB.prepareStatement(sqlQuery, get_TrxName());
                pst.setTimestamp(1, fromDate);
                pst.setTimestamp(2, toDate);
                pst.setLong(3, C_ACCTSCHEMA_ID);
                pst.setTimestamp(4, fromDate);
                pst.setTimestamp(5, toDate);
                pst.setLong(6, C_ACCTSCHEMA_ID);

	    	rs = pst.executeQuery();
                //Cada uno lo inserto en FACT_ACCT_RESUMEN y lo pongo como inactivo

	    	while (rs.next()) {
                    FACT_ACCT_RESUMEN_ID++;
                    sqlQuery = "Insert into FACT_ACCT_RESUMEN values(?,?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,'N','N',?)";
                    insert = DB.prepareStatement(sqlQuery, get_TrxName());
                    insert.setLong(1, FACT_ACCT_RESUMEN_ID);
                    insert.setLong(2, AD_CLIENT_ID); //AD_CLIENT_ID
	            insert.setLong(3, AD_ORG_ID); //AD_ORG_ID
	            insert.setLong(4, C_ACCTSCHEMA_ID); //C_ACCTSCHEMA_ID
                    insert.setTimestamp(5, new Timestamp(System.currentTimeMillis())); //CREATED
                    insert.setLong(6, getAD_User_ID()); //CREATEDBY
                    insert.setTimestamp(7, new Timestamp(System.currentTimeMillis())); //UPDATED
                    insert.setLong(8, getAD_User_ID()); //UPDATEDBY
                    insert.setLong(9, AD_TABLE_ID); //TABLE_RES_ID
	            insert.setLong(10, RECORD_ID); //RECORD_RES_ID
	            insert.setLong(11, rs.getLong(14)); //TABLE_FACT_ID
	            insert.setLong(12, rs.getLong(15)); //RECORD_FACT_ID
	            insert.setTimestamp(13, fromDate);
	            insert.setTimestamp(14, toDate);
                    insert.setLong(15, rs.getLong(1)); //FACT_ACCT_ID

	            insert.executeQuery();

                    sqlQuery = "Update FACT_ACCT set isactive = 'N' where fact_acct_id = ? ";
                    update = DB.prepareStatement(sqlQuery, get_TrxName());
                    update.setLong(1, rs.getLong(1));
                    update.executeQuery();
                    //COMMIT
	            //DB.commit(true, get_TrxName());
                    insert.close();
                    update.close();
	    	}

    	/*}catch (SQLException e){
    		e.printStackTrace();
    	}*/
    }   
    
    private void loadRecord()
    {
		MAcctSchema as = MAcctSchema.get(Env.getCtx(), C_ACCTSCHEMA_ID);
    	AD_CLIENT_ID = as.getAD_Client_ID();
        AD_ORG_ID =  Env.getAD_Org_ID(Env.getCtx());
       

    	try {
    		String sqlQuery = 
	   			" SELECT AD_TABLE_ID FROM AD_TABLE WHERE TABLENAME = 'Fact_Acct'";
	
		    PreparedStatement pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
                    ResultSet rs = pstmt.executeQuery();
	    	
	    	if (rs.next())
	    		AD_TABLE_ID = rs.getInt(1);

                //Si solo seleccione un dia mi rango ese solo ese dia.
	    	if (toDate == null)
	    		toDate = fromDate;
    	}
    	catch (SQLException e){
    		e.printStackTrace();
    	}
    }
    
  
}
