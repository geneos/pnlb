/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Portions created by Carlos Ruiz are Copyright (C) 2005 QSS Ltda.
 * Add e-Evolution by Perez Juarez
 * Contributor(s): Carlos Ruiz (globalqss)
 *****************************************************************************/
package org.eevolution.process;


import java.sql.*;
import java.math.*;
import java.util.List;
import java.util.Vector;
import java.util.logging.*;
import org.compiere.model.MLocator;
import org.compiere.model.MProduct;
import org.compiere.model.MQuery;
import org.compiere.model.MStorage;
import org.compiere.model.MWarehouse;

import org.compiere.model.PrintInfo;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.print.layout.GridElement;
import org.compiere.print.layout.LayoutEngine;
import org.compiere.print.layout.ParameterElement;
import org.compiere.util.*;
import org.compiere.process.*;
import org.eevolution.model.MMPCProductBOM;
import org.eevolution.model.MMPCProductBOMLine;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.tools.UtilProcess;

/**
 * Title:	Inventory Valuation Temporary Table
 *	
 *  @author Jose Fantasia (Zynnia)
 *  @version $Id: T_ProductCheckActualizado.java
 */
public class T_ProductCheckActualizado extends SvrProcess
{

	/** The Parameters */
    
	private int p_M_Warehouse_ID = 0;
	private int p_M_Product_ID1 = 0;
        private int p_M_Product_ID2 = 0;
        private int p_M_Product_ID3 = 0;
        private int p_M_Product_ID4 = 0;
        private int p_M_Product_ID5 = 0;
        private int p_M_Product_ID6 = 0;
        private int p_M_Product_ID7 = 0;
        private int p_M_Product_ID8 = 0;
        private int p_M_Product_ID9 = 0;
        private int p_M_Product_ID10 = 0;
        private int p_Quantity = 0;
        private int p_Quantity1 = 0;
        private int p_Quantity2 = 0;
        private int p_Quantity3 = 0;
        private int p_Quantity4 = 0;
        private int p_Quantity5 = 0;
        private int p_Quantity6 = 0;
        private int p_Quantity7 = 0;
        private int p_Quantity8 = 0;
        private int p_Quantity9 = 0;
        private int p_Quantity10 = 0;
        
        /*
         *  Reemplaza el antiguo p_M_Locator_ID ya que ahora va todo por medio de Almacenes y no depósitos
         *  Pedido por Luis Bernetti
         *  03/09/2012
         *  Zynnia
         * 
         */
        	
        /** The Record */
	private int p_Record_ID = 0;
	
        /** The Instance */
	private int p_PInstance_ID;
        
        int count;

        /* IDs de Almacenes Aprobados */
        private int p_WH_AprobadoFamatina = 1000028 ;
        private int p_WH_AndreaniMPAprobado = 1000076 ;
        private int p_WH_DepositoTercerosAprobado = 1000032 ;
        private Vector<Integer> listAprobados = new Vector<Integer>();

        

        /* Lista de ID de Almacenes Cuarentena */
        private Vector<Integer> listCuarentena = new Vector<Integer>();
    	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
        protected void prepare(){ 
            
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
                    
                    String name = para[i].getParameterName();
                    String info = para[i].getInfo();
                    Object o = para[i].getParameter();
                    
                    if (para[i].getParameter() == null)
                            ;			
                    else if (name.equals("M_Product_ID")){
                        //Como el nombre es el mismo para los 3 me fijo cual encuentra primero
                        if (i==1)
                            p_M_Product_ID1 = para[i].getParameterAsInt();
                        else if (i==3)
                            p_M_Product_ID2 = para[i].getParameterAsInt();
                        else if (i==5)
                            p_M_Product_ID3 = para[i].getParameterAsInt();
                        else if (i==7)
                            p_M_Product_ID4 = para[i].getParameterAsInt();
                        else if (i==9)
                            p_M_Product_ID5 = para[i].getParameterAsInt();
                        else if (i==11)
                            p_M_Product_ID6 = para[i].getParameterAsInt();
                        else if (i==13)
                            p_M_Product_ID7 = para[i].getParameterAsInt();
                        else if (i==15)
                            p_M_Product_ID8 = para[i].getParameterAsInt();
                        else if (i==17)
                            p_M_Product_ID9 = para[i].getParameterAsInt();
                        else
                            p_M_Product_ID10 = para[i].getParameterAsInt();
                   
                    } else if (name.equals("M_Warehouse_ID"))
                            p_M_Warehouse_ID = para[i].getParameterAsInt();    
                    else if (name.equals("Quantity")){
                        if (i==2)
                            p_Quantity1 = para[i].getParameterAsInt();
                        else if (i==4)
                            p_Quantity2 = para[i].getParameterAsInt();
                        else if (i== 6)
                            p_Quantity3 = para[i].getParameterAsInt();
                        else if (i== 8)
                            p_Quantity4 = para[i].getParameterAsInt();
                        else if (i== 10)
                            p_Quantity5 = para[i].getParameterAsInt();
                        else if (i== 12)
                            p_Quantity6 = para[i].getParameterAsInt();
                        else if (i== 14)
                            p_Quantity7 = para[i].getParameterAsInt();
                        else if (i== 16)
                            p_Quantity8 = para[i].getParameterAsInt();
                        else if (i== 18)
                            p_Quantity9 = para[i].getParameterAsInt();
                        else
                            p_Quantity10 = para[i].getParameterAsInt();
                    
                    //} else if (name.equals("M_Warehouse_ID"))
                    //    p_M_Warehouse_ID = para[i].getParameterAsInt();
                    
                    } else
                        log.log(Level.SEVERE, "Unknown Parameter: " + name);
                    }
                
                    p_Record_ID = getRecord_ID();
                    p_PInstance_ID = getAD_PInstance_ID();
                    
                    /*
                     *  Creacion de la lista de todos los almacenes de tipo aprobado.
                     */
                    //Almacen Aprobado Famatina
                    listAprobados.add(p_WH_AprobadoFamatina);
                    //Almacen Andreani MP Aprobado
                    listAprobados.add(p_WH_AndreaniMPAprobado);
                    //Almacen Deposito de Terceros Aprobado
                    listAprobados.add(p_WH_DepositoTercerosAprobado);

                    /*
                     *  Creacion de la lista de todos los almacenes de Cuarentena.
                     */
                    //Almacen Cuarentena Famatina
                    listCuarentena.add(1000042);
                    //Almacen Andreani MP Cuarentena
                    listCuarentena.add(1000077);
                    //Almacen Deposito de Terceros en Cuarentena
                    listCuarentena.add(1000080);

	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
        
            
            count = 1;

            boolean flag = true;
            String sqlBloqueo;
            PreparedStatement psBloqueo;
            ResultSet rsBloqueo;

            while(flag) {

                sqlBloqueo = "SELECT bloqueado FROM T_Bloqueos WHERE AD_Process_ID = 1000292";
                psBloqueo = DB.prepareStatement(sqlBloqueo, get_TrxName());
                rsBloqueo = psBloqueo.executeQuery();

                if(rsBloqueo.next()) {

                        if(rsBloqueo.getString(1).equals("N")){
                            log.info("Bloqueo de doIt para Explosión de materiales");
                            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'Y' where AD_Process_ID = 1000292";
                            DB.executeUpdate(sqlUpdateBloqueo,null);
                            flag = false;
                        }

                }
                rsBloqueo.close();
                psBloqueo.close();

            }


            vaciarDetalle();
            vaciarHeader();
            cargarHeader();

            if (p_M_Product_ID1!=0){
                p_Quantity = p_Quantity1;
                cargarDetalle(p_M_Product_ID1);
            }
            if (p_M_Product_ID2!=0){
                p_Quantity = p_Quantity2;
                cargarDetalle(p_M_Product_ID2);
            }
            if (p_M_Product_ID3!=0){
                p_Quantity = p_Quantity3;
                cargarDetalle(p_M_Product_ID3);
            }
            if (p_M_Product_ID4!=0){
                p_Quantity = p_Quantity4;
                cargarDetalle(p_M_Product_ID4);
            }
            if (p_M_Product_ID5!=0){
                p_Quantity = p_Quantity5;
                cargarDetalle(p_M_Product_ID5);
            }
            if (p_M_Product_ID6!=0){
                p_Quantity = p_Quantity6;
                cargarDetalle(p_M_Product_ID6);
            }
            if (p_M_Product_ID7!=0){
                p_Quantity = p_Quantity7;
                cargarDetalle(p_M_Product_ID7);
            }
            if (p_M_Product_ID8!=0){
                p_Quantity = p_Quantity8;
                cargarDetalle(p_M_Product_ID8);
            }
            if (p_M_Product_ID9!=0){
                p_Quantity = p_Quantity9;
                cargarDetalle(p_M_Product_ID9);
            }
            if (p_M_Product_ID10!=0){
                p_Quantity = p_Quantity10;
                cargarDetalle(p_M_Product_ID10);
            }

            String sql = "update T_PRODUCTCHECK_CAB set AD_PINSTANCE_ID = 0";
            log.info("update T_PRODUCTCHECK_CAB set AD_PINSTANCE_ID = 0");
            DB.executeUpdate(sql,null);

            sql = "update T_PRODUCTCHECK_DET set AD_PINSTANCE_ID = 0";
            log.info("update T_PRODUCTCHECK_CAB set AD_PINSTANCE_ID = 0");
            DB.executeUpdate(sql,null);

            UtilProcess.initViewer("Explosion de Materiales - Almacen", 0, getProcessInfo());

            log.info("Bloqueo de doIt para Explosión de materiales");
            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000292";
            DB.executeUpdate(sqlUpdateBloqueo,null);

            return "ok";
	}	//	doIt

    /** BISion - 14/01/2010 - Santiago Iba�ez
     * Metodo que retorna la cantidad en un deposito en particular
     * para un producto dado
     * @return
     */
    private BigDecimal getQtyDeposito(int productBom, String deposito){
        BigDecimal qty = null;
        try {
            String sql = "select nvl(sum(qtyonhand),0) from m_storage st " +
                    "join m_locator loc on loc.m_locator_id = st.m_locator_id " +
                    "join m_warehouse w on w.m_warehouse_id = loc.m_warehouse_id " +
                    "where loc.value = ? and st.m_product_id = "+ productBom;
            PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
            ps.setString(1, deposito);
            qty = BigDecimal.ZERO;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getBigDecimal(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException sQLException) {
            System.out.println(sQLException.getMessage());
            log.info("Bloqueo de doIt para ExplosiÃ³n de materiales");
            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000292";
            DB.executeUpdate(sqlUpdateBloqueo,null);              
            
            
        }

        return qty;
    }

        private BigDecimal getQtyReservedDeposito(int productBom, String deposito){
        BigDecimal qty = null;
        try {
            String sql = "select nvl(sum(qtyreserved),0) from m_storage st " +
                    "join m_locator loc on loc.m_locator_id = st.m_locator_id " +
                    "join m_warehouse w on w.m_warehouse_id = loc.m_warehouse_id " +
                    "where loc.value = ? and st.m_product_id = "+ productBom;
            PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
            ps.setString(1, deposito);
            qty = BigDecimal.ZERO;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getBigDecimal(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException sQLException) {
            System.out.println(sQLException.getMessage());
            log.info("Bloqueo de doIt para ExplosiÃ³n de materiales");
            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000292";
            DB.executeUpdate(sqlUpdateBloqueo,null);


        }

        return qty;
    }

    private void vaciarHeader(){

        String sqldel ="delete from T_PRODUCTCHECK_CAB";
        DB.executeUpdate(sqldel,null);        
        
    }

    private void cargarDetalle(int M_Product_ID) throws SQLException{
        
                BigDecimal qtyOnHand_AprobadoFamatina = Env.ZERO ;
                BigDecimal qtyOnHand_AndreaniMPAprobado = Env.ZERO ;
                BigDecimal qtyOnHand_DepositoTercerosAprobado = Env.ZERO ;
                 
                BigDecimal qtyOnHand = Env.ZERO;
                BigDecimal qtyCuarentena = Env.ZERO;
                BigDecimal qtyBOM = Env.ZERO;
	BigDecimal qtyRequiered = Env.ZERO;
                BigDecimal qtyAvailable = Env.ZERO;
                BigDecimal qtyReserved = Env.ZERO;

                String sqlins = "";
                String desc = "";

                int productBOM = 0;

                log.info("Inventory Product Valuation Temporary Table (T_PRODUCTCHECK_DET)");

                /**
                 *  Para el producto seleccionado busco la planeación del mismo para luego explotarlo.
                 */
                MMPCProductPlanning pps = MMPCProductPlanning.getDemandWarehouse(getCtx(), 1000033, M_Product_ID, p_M_Warehouse_ID,get_TrxName());
                if (pps==null)
                    return;
                MMPCProductBOM bom = new MMPCProductBOM(getCtx(), pps.getMPC_Product_BOM_ID(), null);
                MMPCProductBOMLine[] bomline = bom.getLines();
                MMPCProductBOMLine line = null;

                MStorage storage = null;
                PreparedStatement pstmtt;
                ResultSet rst;

                /**
                 *  Para cada linea del ProductPlanning del producto seleccionado
                 */
                for(int i=0;i < bomline.length;i++)
                {
                    /*
                     *  Inicializo nuevamente en Cero todas las Cantidades.
                     */
                    qtyCuarentena = Env.ZERO;
                    
                    qtyOnHand_AprobadoFamatina = Env.ZERO ;
                    qtyOnHand_AndreaniMPAprobado = Env.ZERO ;
                    qtyOnHand_DepositoTercerosAprobado = Env.ZERO ;
                    
                    qtyBOM = Env.ZERO;
                    qtyOnHand = Env.ZERO;
                    qtyRequiered = Env.ZERO;
                    qtyAvailable = Env.ZERO;
                    qtyReserved = Env.ZERO;
                    line = bomline[i];
                    desc = "";

                    if(line.isQtyPercentage())
                    {
                        qtyBOM = line.getQtyBatch();
                        qtyRequiered = qtyBOM.multiply(BigDecimal.valueOf(p_Quantity)).divide(BigDecimal.valueOf(100));
                    }
                    else
                    {
                        qtyBOM = line.getQtyBOM();
                        qtyRequiered = qtyBOM.multiply(BigDecimal.valueOf(p_Quantity));
                    }

                    productBOM = line.getM_Product_ID();

                    String sqlt = "select value, name from M_Product where M_Product_ID = " + productBOM;
                    try
                    {
                        pstmtt = DB.prepareStatement(sqlt,null);
			rst = pstmtt.executeQuery();

                        while (rst.next())
                        {
                            desc += rst.getString(1) + " " + rst.getString(2);
                            if(desc.length() > 255)
                                desc = desc.substring(0,255);
                        }
                        rst.close();
                        pstmtt.close();
                    }
                    catch(SQLException tex) {
                        log.info("Bloqueo de doIt para Explosión de materiales");
                        String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000292";
                        DB.executeUpdate(sqlUpdateBloqueo,null);                          
                    }

                    /*
                     *  Sumo todos los QtyOnHand para los almacenes Cuarentena guardandolo en la variable
                     *  qtyCuarentena
                     */
                    for (int j = 0; j < listCuarentena.size();j++){
                        sqlt = "select value from M_Locator where M_Warehouse_ID = " +
                                            listCuarentena.elementAt(j);
                        try
                        {
                            pstmtt = DB.prepareStatement(sqlt,null);
                            rst = pstmtt.executeQuery();
                            while (rst.next())
                            {
                                qtyCuarentena = qtyCuarentena.add(getQtyDeposito(productBOM, rst.getString(1)));
                            }
                            rst.close();
                            pstmtt.close();
                        }
                        catch(SQLException ex)
                        {
                            log.info("Bloqueo de doIt para Explosión de materiales");
                            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000292";
                            DB.executeUpdate(sqlUpdateBloqueo,null);
                        }
                    }
                    if(qtyCuarentena == null)
                        qtyCuarentena = Env.ZERO;
                    

                    /**
                     * Discrimino las cantidades para los almacenes Aprobados
                     */
                    
                    for (int j = 0; j < listAprobados.size();j++){
                        String sqlLocatorAprobados = "select value from M_Locator where M_Warehouse_ID = " +
                                            listAprobados.elementAt(j); 
                        try
                        {
                            PreparedStatement pstmtLocatorAprobados = DB.prepareStatement(sqlLocatorAprobados,null);
                            ResultSet rsLocatorAprobados = pstmtLocatorAprobados.executeQuery();

                            while (rsLocatorAprobados.next())
                            {
                                /*
                                 *  22/07/2013 Maria Jesus
                                 *  Modificacion para que tome todos los reservados y todos los qtyonhand de cada partida, no
                                 *  solo la que tiene 0.
                                 *  Se calcula como la suma de todos los reservados y de todos los qtyonhand de cada par
                                 *  <almacen, producto>
                                 */
                                    qtyAvailable =  qtyAvailable.add(getQtyDeposito(productBOM, rsLocatorAprobados.getString(1)));
                                    qtyAvailable =  qtyAvailable.subtract(getQtyReservedDeposito(productBOM, rsLocatorAprobados.getString(1)));
                                    qtyReserved = qtyReserved.add(getQtyReservedDeposito(productBOM, rsLocatorAprobados.getString(1)));
                                    
                                    //Acumulo en cada variable por separado
                                    if (listAprobados.elementAt(j) == p_WH_AprobadoFamatina) {
                                        qtyOnHand_AprobadoFamatina = qtyOnHand_AprobadoFamatina.add(getQtyDeposito(productBOM, rsLocatorAprobados.getString(1)));
                                    }
                                     //
                                    if (listAprobados.elementAt(j) == p_WH_AndreaniMPAprobado) {
                                        qtyOnHand_AndreaniMPAprobado = qtyOnHand_AndreaniMPAprobado.add(getQtyDeposito(productBOM, rsLocatorAprobados.getString(1)));
                                    }
                                     //
                                    if (listAprobados.elementAt(j) == p_WH_DepositoTercerosAprobado) {
                                       qtyOnHand_DepositoTercerosAprobado = qtyOnHand_DepositoTercerosAprobado.add(getQtyDeposito(productBOM, rsLocatorAprobados.getString(1)));
                                    }
                            }
                            rsLocatorAprobados.close();
                            pstmtLocatorAprobados.close();
                        }
                        catch(SQLException tex) {
                            log.info("Bloqueo de doIt para Explosión de materiales");
                            String sqlUpdateBloqueo ="update T_Bloqueos set bloqueado = 'N' where AD_Process_ID = 1000292";
                            DB.executeUpdate(sqlUpdateBloqueo,null);
                        }
                     }

                     /** BISion - 02/03/2010 - Santiago Iba�ez
                     * COM-PAN-REQ-01.001.01
                     * Antes de insertar en la tabla T_PRODUCTCHECK_DET verifico que no exista,
                     * si existe acumulo cantidades sino inserto fila.
                     */
                     BigDecimal requerido = getCantidadRequerida(desc);
                     if (yaExiste)
                         //Actualizo
                         sqlins = "update T_PRODUCTCHECK_DET set qtyrequiered = "+requerido.add(qtyRequiered)+" where name like '"+desc+"'";
                     
                     else
                     {
                        if(qtyAvailable.compareTo(Env.ZERO) < 0){
                            System.out.println("Ojo !!!! qtyAvailable menor a 0");
                            qtyAvailable = Env.ZERO;
                        }                      
                        
                        /*
                         sqlins  = "INSERT INTO T_PRODUCTCHECK_DET "
                            + "(AD_CLIENT_ID,AD_ORG_ID,M_PRODUCT_ID,NAME,QTYREQUIERED,QTYONHAND,QTYRESERVED,QTYAVAILABLE,QTYCUARENTENA,AD_PInstance_ID,M_Warehouse_ID,Quantity,T_PRODUCTCHECK_DET_ID,qtyOnHand_AprobadoFamatina,qtyOnHand_AndreaniMPAprobado,qtyOnHand_DepTercerosAprobado) VALUES "
                            + "(1000002, 1000033, " + productBOM + ", '" + desc + "', " + qtyRequiered + ", "
                            + qtyOnHand + ", " + qtyReserved + ", " + qtyAvailable + ", " + qtyCuarentena + ", 0" + p_M_WarehouseOrigen_ID + ", " + p_Quantity +" ,"+count+" ,"+qtyOnHand_AprobadoFamatina+" ,"+qtyOnHand_AndreaniMPAprobado+" ,"+qtyOnHand_DepositoTercerosAprobado+")";
                        
                         */
                        
                        sqlins  = "INSERT INTO T_PRODUCTCHECK_DET "
                            + "(AD_CLIENT_ID,AD_ORG_ID,M_PRODUCT_ID,NAME,QTYREQUIERED,QTYONHAND,QTYRESERVED,QTYAVAILABLE,QTYCUARENTENA,AD_PInstance_ID,M_Warehouse_ID,Quantity,T_PRODUCTCHECK_DET_ID,qtyOnHand_AprobadoFamatina,qtyOnHand_AndreaniMPAprobado,qtyOnHand_DepTercerosAprobado) VALUES "
                            + "(1000002, 1000033, " + productBOM + ", '" + desc + "', " + qtyRequiered + ", "
                            + qtyOnHand + ", " + qtyReserved + ", " + qtyAvailable + ", " + qtyCuarentena + ", 0, 0 , " + p_Quantity +" ,"+count+" ,"+qtyOnHand_AprobadoFamatina+" ,"+qtyOnHand_AndreaniMPAprobado+" ,"+qtyOnHand_DepositoTercerosAprobado+")";
                        
                        count = count+1;
                        
                     }
                     pstmtt = DB.prepareStatement(sqlins.toString(),null);
		     rst = pstmtt.executeQuery();
                     String res =  null;

                     if (rst.next())
                     {
                        res = "OK";
                     }
                     else
                     {
                        res = "KO";
                     }

                     rst.close();
                     pstmtt.close();

                }
    }

    /** BISion - 02/03/2010 - Santiago Iba�ez
     * Metodo que retorna la cantidad requerida ya existente de un producto
     * para que sea acumulada y no se ingrese una fila repetida
     * @return
     */
    private boolean yaExiste = false;

    private BigDecimal getCantidadRequerida(String value) throws SQLException{
        BigDecimal qty = BigDecimal.ZERO;
        String sql = "select QTYREQUIERED from T_PRODUCTCHECK_DET where name = ?";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        ps.setString(1, value);
        ResultSet rs = ps.executeQuery();
        yaExiste = false;
        if (rs.next()){
            yaExiste = true;
            qty = rs.getBigDecimal(1);
        }
        return qty;
    }

    private void vaciarDetalle(){

        log.info("Delete T_PRODUCTCHECK_DET");
        String sqldel ="delete from T_PRODUCTCHECK_DET";
        DB.executeUpdate(sqldel,null);

    }

    private void cargarHeader()throws SQLException{
        //String sqlDel = "delete from T_PRODUCTCHECK_CAB";
        //DB.executeUpdate(sqlDel, null);
        /*String sqlIns = "insert into T_PRODUCTCHECK_CAB(AD_Client_ID, AD_Org_ID,IsActive,AD_PInstance_ID,M_Warehouse_ID, " +
                        "Producto1, Cantidad1, Producto2, Cantidad2, " +
                        "Producto3, Cantidad3, Producto4, Cantidad4, " +
                        "Producto5, Cantidad5, Producto6, Cantidad6, " +
                        "Producto7, Cantidad7, Producto8, Cantidad8, " +
                        "Producto9, Cantidad9, Producto10, Cantidad10, T_PRODUCTCHECK_CAB_ID) " +
                        "values(1000002,1000033,'Y', 0," + p_M_WarehouseOrigen_ID + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
        */
        String sqlIns = "insert into T_PRODUCTCHECK_CAB(AD_Client_ID, AD_Org_ID,IsActive,AD_PInstance_ID,M_Warehouse_ID, " +
                        "Producto1, Cantidad1, Producto2, Cantidad2, " +
                        "Producto3, Cantidad3, Producto4, Cantidad4, " +
                        "Producto5, Cantidad5, Producto6, Cantidad6, " +
                        "Producto7, Cantidad7, Producto8, Cantidad8, " +
                        "Producto9, Cantidad9, Producto10, Cantidad10, T_PRODUCTCHECK_CAB_ID) " +
                        "values(1000002,1000033,'Y', 0,0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
        PreparedStatement ps = DB.prepareStatement(sqlIns, null);
        //Producto 1
        MProduct p = new MProduct(getCtx(), p_M_Product_ID1, null);
        String nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID1==0)
            nombreProducto = null;
        ps.setString(1, nombreProducto);
        if (p_M_Product_ID1!=0)
            ps.setInt(2, p_Quantity1);
        else
            ps.setString(2, null);
        //Producto 2
        p = new MProduct(getCtx(), p_M_Product_ID2, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID2==0)
            nombreProducto = null;
        ps.setString(3, nombreProducto);
        if (p_M_Product_ID2!=0)
            ps.setInt(4, p_Quantity2);
        else
            ps.setString(4, null);
        //Producto 3
        p = new MProduct(getCtx(), p_M_Product_ID3, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID3==0)
            nombreProducto = null;
        ps.setString(5, nombreProducto);
        if (p_M_Product_ID3!=0)
            ps.setInt(6, p_Quantity3);
        else
            ps.setString(6, null);
        //Producto 4
        p = new MProduct(getCtx(), p_M_Product_ID4, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID4==0)
            nombreProducto = null;
        ps.setString(7, nombreProducto);
        if (p_M_Product_ID4!=0)
            ps.setInt(8, p_Quantity4);
        else
            ps.setString(8, null);
        //Producto 5
        p = new MProduct(getCtx(), p_M_Product_ID5, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID5==0)
            nombreProducto = null;
        ps.setString(9, nombreProducto);
        if (p_M_Product_ID5!=0)
            ps.setInt(10, p_Quantity5);
        else
            ps.setString(10, null);
        //Producto 6
        p = new MProduct(getCtx(), p_M_Product_ID6, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID6==0)
            nombreProducto = null;
        ps.setString(11, nombreProducto);
        if (p_M_Product_ID6!=0)
            ps.setInt(12, p_Quantity6);
        else
            ps.setString(12, null);
        //Producto 7
        p = new MProduct(getCtx(), p_M_Product_ID7, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID7==0)
            nombreProducto = null;
        ps.setString(13, nombreProducto);
        if (p_M_Product_ID7!=0)
            ps.setInt(14, p_Quantity7);
        else
            ps.setString(14, null);
        //Producto 8
        p = new MProduct(getCtx(), p_M_Product_ID8, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID8==0)
            nombreProducto = null;
        ps.setString(15, nombreProducto);
        if (p_M_Product_ID8!=0)
            ps.setInt(16, p_Quantity8);
        else
            ps.setString(16, null);
        //Producto 9
        p = new MProduct(getCtx(), p_M_Product_ID9, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID9==0)
            nombreProducto = null;
        ps.setString(17, nombreProducto);
        if (p_M_Product_ID9!=0)
            ps.setInt(18, p_Quantity9);
        else
            ps.setString(18, null);
        //Producto 10
        p = new MProduct(getCtx(), p_M_Product_ID10, null);
        nombreProducto = p.getValue()+"-"+p.getName();
        if (p_M_Product_ID10==0)
            nombreProducto = null;
        ps.setString(19, nombreProducto);
        if (p_M_Product_ID10!=0)
            ps.setInt(20, p_Quantity10);
        else
            ps.setString(20, null);
        ps.executeUpdate();
    }
	
	
}	//	T_ProductCheck
