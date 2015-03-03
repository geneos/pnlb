/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.compiere.model.MDocType;
import org.compiere.model.MMessage;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.MTransaction;
import org.compiere.process.SvrProcess;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.StringUtil;
import org.eevolution.model.MMPCOrder;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author santiago
 */
public class GenerateProductosTemporales extends SvrProcess{

    @Override
    protected void prepare() {
        AD_Client_ID = Env.getAD_Client_ID(getCtx());
        AD_Org_ID = Env.getAD_Org_ID(getCtx());
        MMessage.getAD_Message_ID(getCtx(), "LinesWithoutProductAttribute");
    }

    //Orden para producto granel
    private int MPC_Order_ID_level1;
    //Orden para producto semielaborado
    private int MPC_Order_ID_level2;
    //Orden para producto terminado
    private int MPC_Order_ID_level3;
    //Granel fabricado
    private int M_Product_IDlevel1;
    //Partida del granel fabricado por OM1
    private int M_AttributeSetInstance_IDlevel1;
    //Semielaborao fabricado
    private int M_Product_IDlevel2;
    //Partida del semielaborado fabricado por OM2
    private int M_AttributeSetInstance_IDlevel2;
    //Producto terminado fabricado por OM3
    private int M_Product_IDlevel3;
    //Partida del producto terminado fabricado por OM3
    private int M_AttributeSetInstance_IDlevel3;
    //Compañia
    private int AD_Client_ID;
    //Organizacion
    private int AD_Org_ID;

    @Override
    protected String doIt() throws Exception {
        //getPartidasNoRespetanPatron();
        ArrayList<filaInsumosTemporales> filasPartida = new ArrayList<filaInsumosTemporales>();
        //¿Existe para una partida una fila del informe que le falte alguna orden o remito?
        boolean insertaFilas = false;

        //Pregunta si quiere generar un Remito a partir de este movimiento
        Object[] options = {"SI","NO"};
        int n = JOptionPane.showOptionDialog(null,
            "�Desea recalcular?",
            "Recalcular",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        if (n==0){
            //obtener materias primas involucradas
            DB.executeUpdate("delete from t_insumostemporales", null);
            ResultSet insumos = getMateriasPrimas();
            boolean tieneOrden1 = false;
            boolean tieneOrden2 = false;
            boolean tieneOrden3 = false;
            ArrayList<Integer> listM_AttributeSetInstance_ID = getIntArray(insumos, "M_AttributeSetInstance_ID");
            insumos.close();
            //por cada materia prima y partida
            Iterator itM_AttributeSetInstance_ID = listM_AttributeSetInstance_ID.iterator();
            while (itM_AttributeSetInstance_ID.hasNext()){
                filaInsumosTemporales fila = new filaInsumosTemporales();
                fila.setM_AttributeSetInstance_ID((Integer) itM_AttributeSetInstance_ID.next());
                if (fila.getM_AttributeSetInstance_ID()== 1010280)
                    System.out.println("ACA");
                fila.setM_Product_ID(getProductoFromPartida(fila.getM_AttributeSetInstance_ID()));
                MProduct p = new MProduct(getCtx(),fila.getM_Product_ID(),get_TrxName());
                fila.setProducto(p.getName());
                fila.setCodigo(p.getValue());
                fila.setAnalisis(getAnalisis(fila.getM_AttributeSetInstance_ID()));
                fila.setRecibido(getComprado(fila.getM_Product_ID(),fila.getM_AttributeSetInstance_ID()));
                fila.setQtyCuarentena(getCantidadAlmacenada("Cuarentena Famatina U.",fila.getM_Product_ID(),fila.getM_AttributeSetInstance_ID()));
                fila.setQtyAprobado(getCantidadAlmacenada("Aprobado Famatina U.",fila.getM_Product_ID(),fila.getM_AttributeSetInstance_ID()));
                fila.setQtyOtros(getCantidadAlmacenada(null,fila.getM_Product_ID(),fila.getM_AttributeSetInstance_ID()).subtract(fila.getQtyCuarentena().add(fila.getQtyAprobado())));
                ArrayList<Integer> listOrdenesGranel = getOrdenes(fila.getM_Product_ID(), fila.getM_AttributeSetInstance_ID());
                Iterator itOrdenesGranel = listOrdenesGranel.iterator();
                while (itOrdenesGranel.hasNext()){
                    tieneOrden1 = true;
                    MPC_Order_ID_level1 = (Integer) itOrdenesGranel.next();
                    filaInsumosTemporales filaNueva1 = fila.getFilaNuevaOM1();
                    filaNueva1.setPlanta1(getPlanta(MPC_Order_ID_level1));
                    MMPCOrder o1 = new MMPCOrder(getCtx(), MPC_Order_ID_level1, get_TrxName());
                    filaNueva1.setOm1(o1.getDocumentNo());
                    M_AttributeSetInstance_IDlevel1 = o1.getM_AttributeSetInstance_ID();
                    M_Product_IDlevel1 = o1.getM_Product_ID();
                    //Lote de la partida del Insumo
                    filaNueva1.setLote1(getLote(M_AttributeSetInstance_IDlevel1));
                    filaNueva1.setProducto1(MProduct.get(getCtx(), o1.getM_Product_ID()).getName());
                    //¿Con cuanto de la materia prima se surtio la om1?
                    filaNueva1.setSurtido1(getSurtimiento(MPC_Order_ID_level1,fila.getM_Product_ID(),fila.getM_AttributeSetInstance_ID()));
                    //Obtengo las ordenes de semielaborados que usaron este granel
                    ArrayList<Integer> listOrdenesSemielaborado = getOrdenes(M_Product_IDlevel1,M_AttributeSetInstance_IDlevel1);
                    Iterator itOrdenesSemielaborado = listOrdenesSemielaborado.iterator();
                    while (itOrdenesSemielaborado.hasNext()){
                        filaInsumosTemporales filaNueva2 = filaNueva1.getFilaNuevaOM2();
                        MPC_Order_ID_level2 = (Integer) itOrdenesSemielaborado.next();
                        MMPCOrder o2 = new MMPCOrder(getCtx(),MPC_Order_ID_level2,get_TrxName());
                        if (!o2.getDocStatus().equals(MMPCOrder.DOCSTATUS_Closed))
                            continue;
                        tieneOrden2 = true;

                        filaNueva2.setOm2(o2.getDocumentNo());
                        M_AttributeSetInstance_IDlevel2 = o2.getM_AttributeSetInstance_ID();
                        M_Product_IDlevel2 = o2.getM_Product_ID();
                        filaNueva2.setProducto2(MProduct.get(getCtx(), o2.getM_Product_ID()).getName());
                        //Lote de la partida del Granel
                        filaNueva2.setLote2(getLote(M_AttributeSetInstance_IDlevel2));
                        filaNueva2.setPlanta2(getPlanta(MPC_Order_ID_level2));
                        ArrayList<Integer> listOrdenesTerminado = getOrdenes(M_Product_IDlevel2, M_AttributeSetInstance_IDlevel2);
                        Iterator itOrdenesTerminado = listOrdenesTerminado.iterator();
                        while(itOrdenesTerminado.hasNext()){
                            filaInsumosTemporales filaNueva3 = filaNueva2.getFilaNuevaOM3();
                            MPC_Order_ID_level3 = (Integer)itOrdenesTerminado.next();
                            MMPCOrder o3 = new MMPCOrder(getCtx(), MPC_Order_ID_level3, get_TrxName());
                            if (!o3.getDocStatus().equals(MMPCOrder.DOCSTATUS_Closed))
                                continue;
                            tieneOrden3 = true;
                            filaNueva3.setProducto3(MProduct.get(getCtx(), o3.getM_Product_ID()).getName());
                            if (MPC_Order_ID_level3 == 1019807)
                                System.out.println("ACA");
                            filaNueva3.setOm3(o3.getDocumentNo());
                            M_AttributeSetInstance_IDlevel3 = o3.getM_AttributeSetInstance_ID();
                            M_Product_IDlevel3 = o3.getM_Product_ID();
                            filaNueva3.setLote3(getLote(M_AttributeSetInstance_IDlevel3));
                            filaNueva3.setPlanta3(getPlanta(MPC_Order_ID_level3));
                            filaNueva3.setRemito(getRemito());
                            if (!filaNueva3.getQtyAprobado().equals(BigDecimal.ZERO) || !filaNueva3.getQtyCuarentena().equals(BigDecimal.ZERO) || filaNueva3.getRemito() ==null)
                                insertaFilas = true;
                            filasPartida.add(filaNueva3);
                        }
                        if (!tieneOrden3){
                            insertaFilas = true;
                            filasPartida.add(filaNueva2);
                        }
                            
                    }
                    if (!tieneOrden2){
                        insertaFilas = true;
                        filasPartida.add(filaNueva1);
                    }

                }
                //Si no surtio ninguna orden entonces tiene que tener existencia
                if (!tieneOrden1 && (!fila.getQtyAprobado().equals(BigDecimal.ZERO) || !fila.getQtyCuarentena().equals(BigDecimal.ZERO))){
                    insertaFilas = true;
                    filasPartida.add(fila);
                }
                if (insertaFilas)
                    insertarFilasPartida(filasPartida);
                insertaFilas = false;
                filasPartida.clear();
            }
        }
        UtilProcess.initViewer("Formato Insumos Temporales en proceso",0,getProcessInfo());
        return "ok";
    }

    

    private ResultSet getMateriasPrimas(){
        System.out.println("En getMateriasPrimas()");
        ResultSet rs = null;
        //MATERIAS PRIMAS TEMPORALES ASOCIADAS A TRX DE OM COMPLETAS O CERRADAS
        String sql ="select p.m_product_id, p.name, p.value, trx.m_attributesetinstance_id from m_transaction trx"+
                    " join mpc_order o on o.mpc_order_id = trx.mpc_order_id"+
                    " join m_product p on p.m_product_id = trx.m_product_id"+
                    " join m_product_category pc on pc.m_product_category_id = p.m_product_category_id "+
                    " where (o.docstatus = 'CO' or o.docstatus = 'CL') and pc.name = 'Temporal'";
        sql = "select distinct(st.m_attributesetinstance_id), p.m_product_id, p.name, p.value from m_storage st " +
              " join m_product p on p.m_product_id = st.m_product_id"+
              " join m_product_category pc on pc.m_product_category_id = p.m_product_category_id "+
              " where pc.name = 'Temporal' and st.m_attributesetinstance_id <> 0";
        //codigo para retornar ordenes de produccion.
        PreparedStatement ps = DB.prepareStatement(sql,get_TrxName());
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getMateriasPrimas()");
        }
        System.out.println("Saliendo de getMateriasPrimas()");
        return rs;
    }


    private String getAnalisis(int M_AttributeSetInstance_ID){
        System.out.println("En getAnalisis()");
        String a = null;
        //NUMERO DE ANALISIS PARA UNA PARTIDA.
        String sql = "select ai.value from M_AttributeInstance ai " +
                     " left join M_Attribute atr on (ai.m_attribute_id = atr.m_attribute_id) " +
                     " where atr.name LIKE 'Nro. de Análisis' and ai.M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                a = rs.getString(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getAnalisis()");
        }
        System.out.println("Saliendo de getAnalisis()");
        return a;
    }

    private BigDecimal getCantidadAlmacenada(String locator, int M_Product_ID, int M_AttributeSetInstance_ID){
        System.out.println("En getCantidadAlmacenada()");
        BigDecimal qty = BigDecimal.ZERO;
        String sql;
        if (locator != null)
            //SUMA DE CANTIDAD EXISTENTE PARA UN PRODUCTO Y PARTIDA EN UNA UBICACION.
            sql = "select nvl(sum(qtyonhand),0) from m_storage st "+
                     " join m_locator loc on loc.m_locator_id = st.m_locator_id "+
                     " where st.m_product_id = "+M_Product_ID+
                     " and st.M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID+
                     " and loc.value = '"+locator+"'";
        else
            //SUMA DE CANTIDAD EXISTENTE PARA UN PRODUCTO Y PARTIDA.
            sql = "select nvl(sum(qtyonhand),0) from m_storage st "+
                     " where st.m_product_id = "+M_Product_ID+
                     " and st.M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getCantidadAlmacenada()");
        }
        System.out.println("Saliendo de getCantidadAlmacenada()");
        return qty;
    }


    private ArrayList<Integer> getOrdenes(int M_Product_ID,int M_AttributeSetInstance_ID) throws SQLException{
        System.out.println("En getOrdenes()");
        ResultSet rs = null;
        //De las transacciones obtengo las ordenes completas o cerradas asociadas al producto y partida dados.
        String sql = "select distinct(o.mpc_order_id) from m_transaction trx" +
                     " join mpc_order o on o.mpc_order_id = trx.mpc_order_id"+
                     " where trx.m_product_id = "+M_Product_ID+
                     " and trx.M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID+
                     " and (o.docstatus = 'CO' or o.docstatus = 'CL' and o.qtydelivered > 0) and trx.movementtype = '"+MTransaction.MOVEMENTTYPE_Production_+"'";
        //codigo para retornar ordenes de produccion.
        PreparedStatement ps = DB.prepareStatement(sql,get_TrxName());
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getOrdenes()");
        }
        System.out.println("Saliendo de getOrdenes()");
        ArrayList list = getIntArray(rs, "MPC_Order_ID");
        rs.close();
        ps.close();
        return list;
    }

    private BigDecimal getComprado(int M_Product_ID, int M_AttributeSetInstance_ID){
        System.out.println("En getComprado()");
        BigDecimal qty = BigDecimal.ZERO;
        ResultSet rs = null;
        //CANTIDAD RECIBIDA DE LAS RECEPCIONES COMPLETAS PARA UN PRODUCTO Y PARTIDA.
        String sql = "select nvl(sum(qtyentered),0) from m_inoutline il" +
                     " join m_inout i on i.m_inout_id = il.m_inout_id" +
                     " join c_doctype dt on dt.c_doctype_id = i.c_doctype_id"+
                     " where i.docstatus = 'CO' and m_product_id = "+M_Product_ID+
                     " and M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID+
                     " and (dt.docbasetype = '"+MDocType.DOCBASETYPE_RecepcionMaterialesTerceros+"'"+
                     " or dt.docbasetype = '"+MDocType.DOCBASETYPE_DevolucionMaterialesTerceros+"')";
        //codigo para retornar ordenes de produccion.
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getComprado()");
        }
        System.out.println("Saliendo de getComprado()");
        return qty;
    }

    private BigDecimal getSurtimiento(int MPC_Order_ID, int M_Product_ID, int M_AttributeSetInstance_ID){
        System.out.println("En getSurtimiento()");
        BigDecimal qty = BigDecimal.ZERO;
        ResultSet rs = null;
        //SUMA DE LAS TRX PARA UN PRODUCTO, PARTIDA Y OM.
        String sql = "select sum(movementqty) from m_transaction " +
                     " where m_product_id = "+M_Product_ID+" and MPC_Order_ID = "+MPC_Order_ID+" and M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getSurtimiento()");
        }
        System.out.println("Saliendo de getSurtimiento()");
        return qty.negate();
    }

    private String getLote(int M_AttributeSetInstance_ID){
        System.out.println("En getLote()");
        String lot="";
        ResultSet rs = null;
        //NOMBRE DEL LOTE DE UNA PARTIDA DADA
        String sql = "select lot.name from m_attributesetinstance asi"+
                     " join m_lot lot on lot.m_lot_id = asi.m_lot_id"+
                     " where m_attributesetinstance_id = "+M_AttributeSetInstance_ID;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            rs = ps.executeQuery();
            if (rs.next())
                lot = rs.getString(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getLote()");
        }
        System.out.println("Saliendo de getLote()");
        return lot;
    }

    private void getInsertFila() throws SQLException{

        System.out.println("Saliendo de insertFila()");
    }

    private String getRemito(){
        System.out.println("En getRemito()");
        String sql = "select r.documentno from m_movement mov"+
                     " join m_movementline movl on movl.m_movement_id = mov.m_movement_id"+
                     " join m_inout r on r.m_movement_id = mov.m_movement_id"+
                     " join c_doctype dt on dt.c_doctype_id = mov.c_doctype_id"+
                     " where dt.name = 'Transfer a Distribuidor' and movl.m_product_id = "+M_Product_IDlevel3+ " and movl.m_attributesetinstance_id = "+M_AttributeSetInstance_IDlevel3;
        String rem=null;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                rem = rs.getString(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getRemito() "+sql);
        }
        System.out.println("Saliendo de getRemito()");
        return rem;

    }

    private ArrayList<String> getStringArray(ResultSet rs, String colName)throws SQLException{
        //rs.last();
        //int size = rs.getRow();
        //rs.first();
        ArrayList<String> list = new ArrayList<String>();
        while (rs.next()){
            list.add(rs.getString(colName));
        }
        return list;
    }

    private ArrayList<Integer> getIntArray(ResultSet rs, String colName)throws SQLException{
        //rs.last();
        //int size = rs.getRow();
        //rs.first();
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (rs.next()){
            list.add(rs.getInt(colName));
        }
        return list;
    }

    private ArrayList<BigDecimal> getBigDecimalArray(ResultSet rs, String colName)throws SQLException{
        rs.last();
        int size = rs.getRow();
        rs.first();
        ArrayList<BigDecimal> list = new ArrayList<BigDecimal>(size);
        while (rs.next()){
            list.add(rs.getBigDecimal(colName));
        }
        return list;
    }

    private int getProductoFromPartida(int M_AttributeSetInstance_ID){
        System.out.println("En getProductoFromPartida()");
        String sql = "select m_product_id from m_storage where m_attributesetinstance_ID = "+M_AttributeSetInstance_ID;
        int prod = 0;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                prod = rs.getInt(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getProductoFromPartida() "+sql);
        }
        System.out.println("Saliendo de getRemito()");
        return prod;
    }

    private String getPlanta(int MPC_Order_ID){
        String sql = "select s.name from mpc_order o"+
                     " join s_resource s on s.s_resource_id = o.s_resource_id" +
                     " where o.mpc_order_id = "+MPC_Order_ID;
        String name = "";
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                name = rs.getString(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getPlanta() "+sql);
        }
        System.out.println("Saliendo de getPlanta()");
        return name;
    }

    private void insertarFilasPartida(ArrayList<filaInsumosTemporales> filasNuevas){
        Iterator it = filasNuevas.iterator();
        while (it.hasNext()){
            ((filaInsumosTemporales)it.next()).guardar();
        }

    }
    private BigDecimal getRecibido(int M_AttributeSetInstance_ID){
        String sql = "select qtydelvered from m_inoutline il " +
                     "join m_inout_id i on i.m_inout_id = il.m_inout_id"+
                     " where i.docstatus = 'CO' and il.M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
        BigDecimal qty = BigDecimal.ZERO;
        PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                qty = rs.getBigDecimal(1);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateProductosTemporales.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion en: getRecibido() "+sql);
        }
        System.out.println("Saliendo de getPlanta()");
        return qty;
    }
    
    /**
     * Clase que agrupa una fila de la tabla T_InsumosTemporales 
     * Esto es porque no se puede ingresar de a una fila por vez, sino que se 
     * deben ingresar Ej 5 filas a la vez
     */
    public class filaInsumosTemporales{
        // CADA UNA DE LAS COLUMNAS DE LA TABLA T_INSUMOSTEMPORALES

        private int M_Product_ID;
        private int M_AttributeSetInstance_ID;
        private String producto;
        private String codigo;
        private String analisis;
        private BigDecimal recibido;
        private BigDecimal qtyCuarentena;
        private BigDecimal qtyAprobado;
        private BigDecimal qtyOtros;
        private String om1;
        private String lote1;
        private BigDecimal surtido1;
        private String om2;
        private String lote2;
        private String om3;
        private String lote3;
        private String remito;
        private String planta1;
        private String planta2;
        private String planta3;
        private String producto1;
        private String producto2;
        private String producto3;
        
        private filaInsumosTemporales(){
            producto = null;
            codigo = null;
            analisis = null;
            recibido = BigDecimal.ZERO;
            qtyCuarentena = null;
            qtyAprobado = null;
            qtyOtros = null;
            om1 = null;
            lote1 = null;
            surtido1 = null;
            om2 = null;
            lote2 = null;
            om3 = null;
            lote3 = null;
            remito = null;
            planta1 = null;
            planta2 = null;
            planta3 = null;
            producto1=null;
            producto2=null;
            producto3=null;
        }

        public void guardar(){
            System.out.println("En insertFila()");
            try{
                String sql = "insert into T_INSUMOSTEMPORALES values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = DB.prepareStatement(sql, null);
                ps.setInt(1, AD_Client_ID);
                ps.setInt(2, AD_Org_ID);
                ps.setString(3, "Y");
                ps.setInt(4,getM_Product_ID());
                ps.setInt(5,getM_AttributeSetInstance_ID());
                ps.setString(6, producto);
                ps.setString(7, codigo);
                ps.setString(8, analisis);
                ps.setBigDecimal(9, recibido);
                ps.setBigDecimal(10, qtyCuarentena);
                ps.setBigDecimal(11, qtyOtros);
                ps.setBigDecimal(12, qtyAprobado);
                ps.setString(13, om1);
                ps.setString(14, lote1);
                ps.setBigDecimal(15, surtido1);
                ps.setString(16, om2);
                ps.setString(17, lote2);
                ps.setString(18, om3);
                ps.setString(19, lote3);
                ps.setString(20, remito);
                ps.setInt(21, 0);
                ps.setString(22, planta1);
                ps.setString(23, planta2);
                ps.setString(24, planta3);
                ps.setString(25, producto1);
                ps.setString(26, producto2);
                ps.setString(27, producto3);
                ps.executeUpdate();
                ps.close();
            }
            catch(SQLException ex){
                System.out.println("No se pudo guardar fila en T_INSUMOSTEMPORALES "+ex.getMessage());
            }
        }

        /**
         * @return the producto
         */
        public String getProducto() {
            return producto;
        }

        /**
         * @return the codigo
         */
        public String getCodigo() {
            return codigo;
        }

        /**
         * @return the analisis
         */
        public String getAnalisis() {
            return analisis;
        }

        /**
         * @return the recibido
         */
        public BigDecimal getRecibido() {
            return recibido;
        }

        /**
         * @return the qtyCuarentena
         */
        public BigDecimal getQtyCuarentena() {
            return qtyCuarentena;
        }

        /**
         * @return the qtyAprobado
         */
        public BigDecimal getQtyAprobado() {
            return qtyAprobado;
        }

        /**
         * @return the qtyOtros
         */
        public BigDecimal getQtyOtros() {
            return qtyOtros;
        }

        /**
         * @return the om1
         */
        public String getOm1() {
            return om1;
        }

        /**
         * @return the lote1
         */
        public String getLote1() {
            return lote1;
        }

        /**
         * @return the surtido1
         */
        public BigDecimal getSurtido1() {
            return surtido1;
        }

        /**
         * @return the om2
         */
        public String getOm2() {
            return om2;
        }

        /**
         * @return the lote2
         */
        public String getLote2() {
            return lote2;
        }

        /**
         * @return the om3
         */
        public String getOm3() {
            return om3;
        }

        /**
         * @return the lote3
         */
        public String getLote3() {
            return lote3;
        }

        /**
         * @return the remito
         */
        public String getRemito() {
            return remito;
        }

        /**
         * @return the planta
         */
        public String getPlanta1() {
            return planta1;
        }

        /**
         * @param producto the producto to set
         */
        public void setProducto(String producto) {
            this.producto = producto;
        }

        /**
         * @param codigo the codigo to set
         */
        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        /**
         * @param analisis the analisis to set
         */
        public void setAnalisis(String analisis) {
            this.analisis = analisis;
        }

        /**
         * @param recibido the recibido to set
         */
        public void setRecibido(BigDecimal recibido) {
            this.recibido = recibido;
        }

        /**
         * @param qtyCuarentena the qtyCuarentena to set
         */
        public void setQtyCuarentena(BigDecimal qtyCuarentena) {
            this.qtyCuarentena = qtyCuarentena;
        }

        /**
         * @param qtyAprobado the qtyAprobado to set
         */
        public void setQtyAprobado(BigDecimal qtyAprobado) {
            this.qtyAprobado = qtyAprobado;
        }

        /**
         * @param qtyOtros the qtyOtros to set
         */
        public void setQtyOtros(BigDecimal qtyOtros) {
            this.qtyOtros = qtyOtros;
        }

        /**
         * @param om1 the om1 to set
         */
        public void setOm1(String om1) {
            this.om1 = om1;
        }

        /**
         * @param lote1 the lote1 to set
         */
        public void setLote1(String lote1) {
            this.lote1 = lote1;
        }

        /**
         * @param surtido1 the surtido1 to set
         */
        public void setSurtido1(BigDecimal surtido1) {
            this.surtido1 = surtido1;
        }

        /**
         * @param om2 the om2 to set
         */
        public void setOm2(String om2) {
            this.om2 = om2;
        }

        /**
         * @param lote2 the lote2 to set
         */
        public void setLote2(String lote2) {
            this.lote2 = lote2;
        }

        /**
         * @param om3 the om3 to set
         */
        public void setOm3(String om3) {
            this.om3 = om3;
        }

        /**
         * @param lote3 the lote3 to set
         */
        public void setLote3(String lote3) {
            this.lote3 = lote3;
        }

        /**
         * @param remito the remito to set
         */
        public void setRemito(String remito) {
            this.remito = remito;
        }

        /**
         * @param planta the planta to set
         */
        public void setPlanta1(String planta) {
            this.planta1 = planta;
        }

        /**
         * @param M_Product_ID the M_Product_ID to set
         */
        public void setM_Product_ID(int M_Product_ID) {
            this.M_Product_ID = M_Product_ID;
        }

        /**
         * @param M_AttributeSetInstance_ID the M_AttributeSetInstance_ID to set
         */
        public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
            this.M_AttributeSetInstance_ID = M_AttributeSetInstance_ID;
        }

        /**
         * @return the M_Product_ID
         */
        public int getM_Product_ID() {
            return M_Product_ID;
        }

        /**
         * @return the M_AttributeSetInstance_ID
         */
        public int getM_AttributeSetInstance_ID() {
            return M_AttributeSetInstance_ID;
        }
        @Override
        public String toString(){
            return codigo +" "+analisis+"_<<"+lote1+">> om1: "+om1+" om2: "+om2+" om3: "+om3;
        }
        public filaInsumosTemporales getFilaNuevaOM1(){
            filaInsumosTemporales fila = new filaInsumosTemporales();
            fila.setAnalisis(analisis);
            fila.setCodigo(codigo);
            fila.setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
            fila.setProducto(producto);
            fila.setQtyAprobado(qtyAprobado);
            fila.setQtyCuarentena(qtyCuarentena);
            fila.setQtyOtros(qtyOtros);
            fila.setRecibido(recibido);
            return fila;
        }

        public filaInsumosTemporales getFilaNuevaOM2(){
            filaInsumosTemporales fila = getFilaNuevaOM1();
            fila.setLote1(lote1);
            fila.setOm1(om1);
            fila.setPlanta1(planta1);
            fila.setSurtido1(surtido1);
            fila.setProducto1(producto1);
            return fila;
        }
        public filaInsumosTemporales getFilaNuevaOM3(){
            filaInsumosTemporales fila = getFilaNuevaOM2();
            fila.setLote2(lote2);
            fila.setOm2(om2);
            fila.setPlanta2(planta2);
            fila.setProducto2(producto2);
            return fila;
        }



        /**
         * @return the planta2
         */
        public String getPlanta2() {
            return planta2;
        }

        /**
         * @param planta2 the planta2 to set
         */
        public void setPlanta2(String planta2) {
            this.planta2 = planta2;
        }

        /**
         * @return the planta3
         */
        public String getPlanta3() {
            return planta3;
        }

        /**
         * @param planta3 the planta3 to set
         */
        public void setPlanta3(String planta3) {
            this.planta3 = planta3;
        }

        /**
         * @return the producto1
         */
        public String getProducto1() {
            return producto1;
        }

        /**
         * @param producto1 the producto1 to set
         */
        public void setProducto1(String producto1) {
            this.producto1 = producto1;
        }

        /**
         * @return the producto2
         */
        public String getProducto2() {
            return producto2;
        }

        /**
         * @param producto2 the producto2 to set
         */
        public void setProducto2(String producto2) {
            this.producto2 = producto2;
        }

        /**
         * @return the producto3
         */
        public String getProducto3() {
            return producto3;
        }

        /**
         * @param producto3 the producto3 to set
         */
        public void setProducto3(String producto3) {
            this.producto3 = producto3;
        }

    }


}
