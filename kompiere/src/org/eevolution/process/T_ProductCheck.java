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
import java.util.logging.*;
import org.compiere.model.MProduct;
import org.compiere.model.MQuery;
import org.compiere.model.MStorage;

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
 *  @author Carlos Ruiz (globalqss)
 *  @version $Id: T_InventoryValue_Create.java,v 1.0 2005/09/21 20:29:00 globalqss Exp $
 */
public class T_ProductCheck extends SvrProcess
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
    private int p_M_Locator_ID = 0;
	/** The Record */
	private int		p_Record_ID = 0;
	
        /** The Instance */
	private int     p_PInstance_ID;

    	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
        protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
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
            }
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = para[i].getParameterAsInt();
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
            }
            else if (name.equals("M_Warehouse_ID"))
                p_M_Warehouse_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
        vaciarDetalle();
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
        /*MPrintFormat format = new MPrintFormat(getCtx(), 1000898, get_TrxName());
        MQuery mquery = new MQuery("T_ProductCheck");
        ReportEngine re = new ReportEngine(Env.getCtx(), format, mquery, new PrintInfo(getProcessInfo()));
        //LayoutEngine le = new LayoutEngine(format, re.getPrintData(), mquery);
        Viewer viewer = new Viewer(re);*/
        UtilProcess.initViewer("Explosion Consolidada Header", 0, getProcessInfo());
        return "ok";
	}	//	doIt

    /** BISion - 14/01/2010 - Santiago Ibañez
     * Metodo que retorna la cantidad en un deposito en particular
     * para un producto dado
     * @return
     */
    private BigDecimal getQtyDeposito(int M_Product_ID, String deposito){
        BigDecimal qty = null;
        try {
            String sql = "select nvl(sum(qtyonhand),0) from m_storage st " +
                    "join m_locator loc on loc.m_locator_id = st.m_locator_id " +
                    "join m_warehouse w on w.m_warehouse_id = loc.m_warehouse_id " +
                    "where w.value = ? and st.m_product_id = "+ M_Product_ID;
            PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
            ps.setString(1, deposito);
            qty = BigDecimal.ZERO;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getBigDecimal(1);
            }
        } catch (SQLException sQLException) {
            System.out.println(sQLException.getMessage());
        }
        return qty;
    }

    private void vaciarHeader(){
        String sqldel ="delete from T_EXPLOSION_MATERIALES";
        DB.executeUpdate(sqldel,null);
    }
    private void cargarHeader(int M_Product_ID){
        try {
            MProduct p = MProduct.get(getCtx(), M_Product_ID);
            String producto = p.getValue() + " " + p.getName();
            String sqlIns = "insert into T_EXPLOSION_MATERIALES values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlIns, null);
            ps.setInt(1, getAD_Client_ID());
            ps.setInt(2, Env.getAD_Org_ID(getCtx()));
            ps.setString(3, "Y");
            ps.setString(8, producto);
            ps.setInt(6, p_Quantity);
            ps.setInt(7, p_M_Locator_ID);
            ps.setInt(5, M_Product_ID);
            ps.setInt(4, getAD_PInstance_ID());
            ps.executeUpdate();
        } catch (SQLException sQLException) {
            System.out.println(sQLException.getMessage());
        }
    }

    private void cargarDetalle(int M_Product_ID) throws SQLException{
        BigDecimal qtyCuarentena = Env.ZERO;
                BigDecimal qtyOnHand = Env.ZERO;
                BigDecimal qtyBOM = Env.ZERO;
		BigDecimal qtyRequiered = Env.ZERO;
                BigDecimal qtyAvailable = Env.ZERO;
                BigDecimal qtyReserved = Env.ZERO;

                String res = "";
                String sqlupd = "";
		String sqlins = "";
		String desc = "";

                int productBOM = 0;
                int cntu = 0;
		int cnti = 0;

		log.info("Inventory Product Valuation Temporary Table (T_ProductCheck)");

                

                /*
                 *
                        Fijo para Aprobado Famatina M_Wharehouse_ID = 1000028
                 *
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

                for(int i=0;i < bomline.length;i++)
                {
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
			//pstmt.setInt(1, AD_Client_ID);
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
                    catch(SQLException tex)
                    {
                    }


                    sqlt = "select nvl(sum(qtyonhand),0) from M_Storage where M_Attributesetinstance_ID <> 0 and  M_Product_ID = " + productBOM + " and M_Locator_ID = " + p_M_Locator_ID;
                    try
                    {
                        pstmtt = DB.prepareStatement(sqlt,null);
			//pstmt.setInt(1, AD_Client_ID);
			rst = pstmtt.executeQuery();

                        while (rst.next())
                        {
                            qtyOnHand = rst.getBigDecimal(1);
                        }
                        rst.close();
                        pstmtt.close();
                    }
                    catch(SQLException tex)
                    {
                    }

                    if(qtyOnHand == null)
                        qtyOnHand = Env.ZERO;


                    sqlt = "select nvl(sum(qtyonhand),0) from M_Storage where M_Attributesetinstance_ID <> 0 and  M_Product_ID = " + productBOM + " and M_Locator_ID = 1000277";
                    try
                    {
                        pstmtt = DB.prepareStatement(sqlt,null);
			//pstmt.setInt(1, AD_Client_ID);
			rst = pstmtt.executeQuery();

                        while (rst.next())
                        {
                            qtyCuarentena = rst.getBigDecimal(1);
                        }
                        rst.close();
                        pstmtt.close();
                    }
                    catch(SQLException tex)
                    {
                    }

                    if(qtyCuarentena == null)
                        qtyCuarentena = Env.ZERO;


                    storage = MStorage.get(getCtx(), p_M_Locator_ID, productBOM, 0, null);

                    if(storage == null)
                    {
                        qtyAvailable = Env.ZERO;
                        qtyReserved =  Env.ZERO;

                    }
                    else
                    {
                        qtyAvailable =  qtyOnHand.subtract(storage.getQtyReserved());
                        qtyReserved = storage.getQtyReserved();
                    }

                    if(qtyAvailable.compareTo(Env.ZERO) < 0)
                        qtyAvailable = Env.ZERO;

                    /** BISion - 13/01/2009 - Santiago Ibañez
                     *  COM-PAN-CHR-02.007.01
                     */
                    /** BISion - 02/03/2010 - Santiago Ibañez
                     * COM-PAN-REQ-01.001.01
                     * Antes de insertar en la tabla T_ProductCheck verifico que no exista,
                     * si existe acumulo cantidades sino inserto fila.
                     */
                     BigDecimal requerido = getCantidadRequerida(desc);
                     if (yaExiste){
                         //Actualizo
                         sqlins = "update t_productcheck set qtyrequiered = "+requerido.add(qtyRequiered)+" where name like '"+desc+"'";
                     }
                     else{
                        //Agrego una fila nueva
                        BigDecimal qtyAndreani = getQtyDeposito(productBOM,"Andreani MP Aprobado");
                        BigDecimal qtyAndreaniCuarentena = getQtyDeposito(productBOM,"Andreani MP Cuarentena");
                        /** Santiago Ibañez - 10/08/2010 - BISion
                         * Modificacion realizada para incluir el Depósito de Terceros Aprobado
                         */
                        BigDecimal qtyAprobadoTerceros = getQtyDeposito(productBOM, "Dep. de Terceros Aprobado");
                        //fin modificacion BISion
                        sqlins  = "INSERT INTO T_PRODUCTCHECK "
                            + "(AD_CLIENT_ID,AD_ORG_ID,M_PRODUCT_ID,NAME,QTYREQUIERED,QTYONHAND,QTYRESERVED,QTYAVAILABLE,QTYCUARENTENA,AD_PInstance_ID,M_Locator_ID,Quantity,QTYANDREANI,QTYANDREANICUARENTENA,QTYTERCEROSAPROBADO) VALUES "
                            + "(1000002, 1000033, " + M_Product_ID + ", '" + desc + "', " + qtyRequiered + ", "
                            + qtyOnHand + ", " + qtyReserved + ", " + qtyAvailable + ", " + qtyCuarentena + ", " + 0 + ", " + p_M_Locator_ID + ", " + p_Quantity +", " +qtyAndreani+","+qtyAndreaniCuarentena+","+qtyAprobadoTerceros+")";
                     }
                     /*BigDecimal qtyAndreani = getQtyAndreaniAprobado(productBOM);
                     sqlins  = "INSERT INTO T_PRODUCTCHECK "
                            + "(AD_CLIENT_ID,AD_ORG_ID,M_PRODUCT_ID,NAME,QTYREQUIERED,QTYONHAND,QTYRESERVED,QTYAVAILABLE,QTYCUARENTENA,AD_PInstance_ID,M_Locator_ID,Quantity,QTYANDREANI) VALUES "
                            + "(1000002, 1000033, " + M_Product_ID + ", '" + desc + "', " + qtyRequiered + ", "
                            + qtyOnHand + ", " + qtyReserved + ", " + qtyAvailable + ", " + qtyCuarentena + ", " + 0 + ", " + p_M_Locator_ID + ", " + p_Quantity +", " +qtyAndreani+")";*/



                    pstmtt = DB.prepareStatement(sqlins.toString(),null);
			//pstmt.setInt(1, AD_Client_ID);
		    rst = pstmtt.executeQuery();

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

    /** BISion - 02/03/2010 - Santiago Ibañez
     * Metodo que retorna la cantidad requerida ya existente de un producto
     * para que sea acumulada y no se ingrese una fila repetida
     * @return
     */
    private boolean yaExiste = false;

    private BigDecimal getCantidadRequerida(String value) throws SQLException{
        BigDecimal qty = BigDecimal.ZERO;
        String sql = "select QTYREQUIERED from t_productcheck where name = ?";
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
        log.info("Delete T_ProductCheck");
        String sqldel ="delete from T_ProductCheck";
        DB.executeUpdate(sqldel,null);
    }

    private void cargarHeader()throws SQLException{
        String sqlDel = "delete from T_ProductCheck_Header";
        DB.executeUpdate(sqlDel, null);
        String sqlIns = "insert into T_ProductCheck_Header(AD_Client_ID, AD_Org_ID,IsActive,AD_PInstance_ID,M_Locator_ID, " +
                        "Producto1, Cantidad1, Producto2, Cantidad2, " +
                        "Producto3, Cantidad3, Producto4, Cantidad4, " +
                        "Producto5, Cantidad5, Producto6, Cantidad6, " +
                        "Producto7, Cantidad7, Producto8, Cantidad8, " +
                        "Producto9, Cantidad9, Producto10, Cantidad10) " +
                        "values(1000002,1000033,'Y',0,"+this.p_M_Locator_ID+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
