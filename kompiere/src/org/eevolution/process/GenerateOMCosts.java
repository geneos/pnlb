package org.eevolution.process;
import org.compiere.process.*;
import java.math.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.logging.*;
import org.compiere.model.*;

import org.compiere.util.*;
import org.eevolution.tools.UtilProcess;
/**
 *Esta clase inserta tuplas en la tabla temporal T_OM_COSTS luego de un previo filtrado por fecha 
 *  y calculos posteriores (TEST COMMIT SVN).
 *
*	@author BISion - Santiago Iba�ez - 22/08/2008
*	@version 1.0
 */
public class GenerateOMCosts extends SvrProcess {
    
    private int p_PInstance_ID;
    private Timestamp fromDate=null;
    private Timestamp toDate=null;
    private BigDecimal MPC_Order_ID = null;
    private BigDecimal M_Product_ID = null;
    
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
	{
            String name = para[i].getParameterName();
            //Parametro Fecha
            if (name.equals("FECHA")){
                fromDate=(Timestamp)para[i].getParameter();
                toDate=(Timestamp)para[i].getParameter_To();
            }
            //Parametro MPC_Order_ID
            else if (name.equals("MPC_Order_ID")){
                MPC_Order_ID = (BigDecimal) para[i].getParameter();
            }
            else{
                M_Product_ID = (BigDecimal) para[i].getParameter();
            }
        }
        p_PInstance_ID = getAD_PInstance_ID();     
        Env.getCtx().put("Delimitador", "");
    }   

   
    protected String doIt() throws Exception {
        String sqlQuery,sqlInsert="";
        PreparedStatement pstmtInsert = null;
        log.info("Comienzo del proceso de generacion del reporte de Costos de Ordenes de Manufactura");
        log.info("borrado de la tabla temporal T_OM_COSTS");
        String sqlRemove = "delete from T_OM_COSTS";
        DB.executeUpdate(sqlRemove, null);
        
        /** BISion 13/01/2009 - Santiago Iba�ez
         * Modificacion realizada para considerar tambien el parametro OM
         */
        PreparedStatement pstmt;
        if (fromDate!=null){
            //fecha
            sqlQuery = "SELECT mo.AD_CLIENT_ID," + //1 - AD_CLIENT_ID
                   " mo.AD_ORG_ID," +          //2 - AD_ORG_ID
                   " mo.DOCUMENTNO," +         //3 - NRO OM
                   " u.name," +                //4 - UNIDAD
                   " mo.DATEORDERED," +        //5 - FECHA     
                   " mo.MPC_Order_ID," +        //6 - MPC_Order_ID
                   " mo.M_Product_ID" +        //7 - M_Product_ID
                   " FROM MPC_ORDER mo" +      
                   " JOIN C_UOM u ON (u.C_UOM_ID = mo.C_UOM_ID)" +
                   " WHERE (mo.DOCSTATUS = 'CO' OR mo.DOCSTATUS = 'CL')" +
                   " AND (mo.DATEORDERED BETWEEN ? AND ?)";
            if (MPC_Order_ID!=null)
                sqlQuery+=  " AND MPC_Order_ID = ?";
            if (M_Product_ID!=null)
                sqlQuery+= " AND M_Product_ID = ?";
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setTimestamp(1, fromDate);
            pstmt.setTimestamp(2, toDate); 
            //fecha y OM
            int i=3;
            if (MPC_Order_ID!=null){
                pstmt.setBigDecimal(i, MPC_Order_ID);
                i++;
            }
            //Si no se ingreso una OM pregunto si se ingreso un producto
            if (M_Product_ID!=null){
                pstmt.setBigDecimal(3,M_Product_ID);
            }
            
        }
        //solo OM
        else if (MPC_Order_ID!=null){
            sqlQuery = "SELECT mo.AD_CLIENT_ID," + //1 - AD_CLIENT_ID
                   " mo.AD_ORG_ID," +          //2 - AD_ORG_ID
                   " mo.DOCUMENTNO," +         //3 - NRO OM
                   " u.name," +                //4 - UNIDAD
                   " mo.DATEORDERED," +        //5 - FECHA     
                   " mo.MPC_Order_ID," +        //6 - MPC_Order_ID
                   " mo.M_Product_ID" +        //7 - M_Product_ID
                   " FROM MPC_ORDER mo" +      
                   " JOIN C_UOM u ON (u.C_UOM_ID = mo.C_UOM_ID)" +
                   " WHERE (mo.DOCSTATUS = 'CO' OR mo.DOCSTATUS = 'CL')" +
                   " AND MPC_Order_ID = ?";
            if (M_Product_ID!=null)
                sqlQuery+= "AND M_Product_ID = ?";
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setBigDecimal(1, MPC_Order_ID);
            if (M_Product_ID!=null)
                pstmt.setBigDecimal(2, M_Product_ID);
        }
        //solo se especifico el producto
        else if (M_Product_ID!=null){
            sqlQuery = "SELECT mo.AD_CLIENT_ID," + //1 - AD_CLIENT_ID
                       " mo.AD_ORG_ID," +          //2 - AD_ORG_ID
                       " mo.DOCUMENTNO," +         //3 - NRO OM
                       " u.name," +                //4 - UNIDAD
                       " mo.DATEORDERED," +        //5 - FECHA     
                       " mo.MPC_Order_ID," +        //6 - MPC_Order_ID
                       " mo.M_Product_ID" +         //7 - M_Product_ID
                       " FROM MPC_ORDER mo" +      
                       " JOIN C_UOM u ON (u.C_UOM_ID = mo.C_UOM_ID)" +
                       " WHERE (mo.DOCSTATUS = 'CO' OR mo.DOCSTATUS = 'CL')" + 
                       " AND M_Product_ID = ?";
            pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
            pstmt.setBigDecimal(1, M_Product_ID);
        }
        //no se especifico nada como parametro
        else{
            sqlQuery = "SELECT mo.AD_CLIENT_ID," + //1 - AD_CLIENT_ID
                           " mo.AD_ORG_ID," +          //2 - AD_ORG_ID
                           " mo.DOCUMENTNO," +         //3 - NRO OM
                           " u.name," +                //4 - UNIDAD
                           " mo.DATEORDERED," +        //5 - FECHA     
                           " mo.MPC_Order_ID" +        //6 - MPC_Order_ID
                           " FROM MPC_ORDER mo" +      
                           " JOIN C_UOM u ON (u.C_UOM_ID = mo.C_UOM_ID)" +
                           " WHERE (mo.DOCSTATUS = 'CO' OR mo.DOCSTATUS = 'CL')";
                pstmt = DB.prepareStatement(sqlQuery, get_TrxName());
        }
        ResultSet rs = pstmt.executeQuery();
        try {
        while (rs.next()) {
            //preparo la consulta para insertar en la tabla temporal T_OM_COSTS
            sqlInsert = "insert into T_OM_COSTS values(?,?,'Y',?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmtInsert = DB.prepareStatement(sqlInsert, get_TrxName());
            pstmtInsert.setInt(1,rs.getInt(1));         //1 - AD_CLIENT_ID
            pstmtInsert.setInt(2,rs.getInt(2));         //2 - AD_ORG_ID
            pstmtInsert.setInt(3,p_PInstance_ID);       //3 - P_INSTANCE 
            pstmtInsert.setString(4,rs.getString(3));   //4 - NRO OM
            pstmtInsert.setString(6,rs.getString(4));   //6 - UNIDAD
            pstmtInsert.setTimestamp(12, rs.getTimestamp(5));//7 - FECHA
//          Obtengo la orden de manufactura en cuestion
            MPC_Order_ID = rs.getBigDecimal(6);
            
            //obtengo las cantidades que rindio la OM teniendo en cuenta todos los controles
            //de actividad de tipo 'Manufacturing Order Receipt'
            String sqlCant = "SELECT SUM(cc.MOVEMENTQTY) FROM MPC_COST_COLLECTOR cc " +
            		  "LEFT JOIN C_DOCTYPE d ON (cc.C_DOCTYPE_ID = d.C_DOCTYPE_ID) " +
            		  "WHERE d.name = 'Manufacturing Order Receipt' " +
            		  "AND cc.MPC_ORDER_ID = ?";
            PreparedStatement pstmtCost = DB.prepareStatement(sqlCant, get_TrxName());
            pstmtCost.setBigDecimal(1, MPC_Order_ID);
            ResultSet rsCosto = pstmtCost.executeQuery();
            BigDecimal qty;
            if(rsCosto.next()){
                pstmtInsert.setInt(5,rsCosto.getInt(1));         //5 - CANTIDAD
                qty = new BigDecimal(rsCosto.getInt(1));
            }
            else{
                pstmtInsert.setInt(5,0);
                qty = BigDecimal.ZERO;
            }
            rsCosto.close();
            pstmtCost.close();
            String sqlCost;
            BigDecimal totalCost = BigDecimal.ZERO;
            //BUSCO COSTOS REALES INDIRECTOS
            sqlCost = "SELECT COSTCUMQTY FROM MPC_ORDER_COST moc "+
                      " JOIN MPC_COST_ELEMENT ce ON (ce.MPC_COST_ELEMENT_ID = moc.MPC_COST_ELEMENT_ID)"+
                      " WHERE ce.NAME = 'Costo Real Indirecto' AND moc.MPC_Order_ID = ?";
            pstmtCost = DB.prepareStatement(sqlCost, get_TrxName());
            pstmtCost.setBigDecimal(1,MPC_Order_ID);
            rsCosto = pstmtCost.executeQuery();
            if (rsCosto.next()){
                pstmtInsert.setBigDecimal(9,rsCosto.getBigDecimal(1));
                //acumulo el costo
                totalCost = totalCost.add(rsCosto.getBigDecimal(1));
            }
            else{
                pstmtInsert.setString(9,"0");
            }
            rsCosto.close();
            pstmtCost.close();
            //BUSCO COSTOS REALES DE MATERIALES
            sqlCost = "SELECT COSTCUMQTY FROM MPC_ORDER_COST moc "+
                      " JOIN MPC_COST_ELEMENT ce ON (ce.MPC_COST_ELEMENT_ID = moc.MPC_COST_ELEMENT_ID)"+
                      " WHERE ce.NAME = 'Costo Real de Materiales' AND moc.MPC_Order_ID = ?";
            pstmtCost = DB.prepareStatement(sqlCost, get_TrxName());
            pstmtCost.setBigDecimal(1,MPC_Order_ID);
            rsCosto = pstmtCost.executeQuery();
            if(rsCosto.next()){
                pstmtInsert.setBigDecimal(8,rsCosto.getBigDecimal(1));
                //acumulo el costo
                totalCost = totalCost.add(rsCosto.getBigDecimal(1));
            }
            else{
                pstmtInsert.setString(8,"0");
            }
            rsCosto.close();
            pstmtCost.close();
            //BUSCO COSTOS REALES DE RECURSOS
            sqlCost = "SELECT COSTCUMQTY FROM MPC_ORDER_COST moc "+
                      " JOIN MPC_COST_ELEMENT ce ON (ce.MPC_COST_ELEMENT_ID = moc.MPC_COST_ELEMENT_ID)"+
                      " WHERE ce.NAME = 'Costo Real de Recursos' AND moc.MPC_Order_ID = ?";
            pstmtCost = DB.prepareStatement(sqlCost, get_TrxName());
            pstmtCost.setBigDecimal(1,MPC_Order_ID);
            rsCosto = pstmtCost.executeQuery();
            if(rsCosto.next()){
               pstmtInsert.setBigDecimal(7,rsCosto.getBigDecimal(1));
               //acumulo el costo
               totalCost = totalCost.add(rsCosto.getBigDecimal(1));
            }
            else
              pstmtInsert.setString(7,"0");
            
            pstmtInsert.setBigDecimal(10, totalCost);
         
            if (!qty.equals(BigDecimal.ZERO)&&!totalCost.equals(BigDecimal.ZERO)){
                totalCost = totalCost.divide(qty,12,BigDecimal.ROUND_HALF_UP);
            }
            else
                totalCost = BigDecimal.ZERO;
            pstmtInsert.setBigDecimal(11,totalCost);
            
            //modificado para considerar productos y/o om dadas
            pstmtInsert.setBigDecimal(13, MPC_Order_ID);
            pstmtInsert.setBigDecimal(14, M_Product_ID);
            //fin modificacion
            pstmtInsert.executeQuery();
            DB.commit(true, get_TrxName());
            pstmtInsert.close();
            pstmtCost.close();
            rsCosto.close();
        }
        } catch (Exception exception) {
           log.info("Se produjo un error en org.compiere.process.GenerateOMCosts " + exception.getMessage());
           exception.printStackTrace();
        }
        UtilProcess.initViewer("Costos Reales �rdenes de Manufactura",p_PInstance_ID,getProcessInfo());
        return "success";
    }
}