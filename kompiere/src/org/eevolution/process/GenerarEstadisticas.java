/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.MConversionRate;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author jose
 */
public class GenerarEstadisticas extends SvrProcess{

    private String causa = "";
    private String causaHasta = "";
    private String condicionVenta = "";
    private String condicionVentaHasta = "";
    private String documento = "";
    private String documentoHasta = "";
    private String cliente = "";
    private String clienteHasta = "";
    private String vendedor = "";
    private String vendedorHasta = "";
    private String desdeFecha = "";
    private String hastaFecha = "";
    private String moneda = "";
    private String monedaExposicion = "";
    private String region = "";
    private String regionHasta = "";
    private String localizacion = "";
    private int listaPrecios = 0;
    private String productosCargos = "";
    private String producto = "";
    private String productoHasta = "";
    private String cargo = "";
    private String cargoHasta = "";
    private String tipoInforme = "";


    protected void prepare() {
        ProcessInfoParameter parametros[] = getParameter();
        int flagCliente=0;
        int flagDocumento=0;
        int flagVendedor=0;
        int flagCausa=0;
        int flagCondicion=0;
        int flagRegion=0;
        int flagProducto=0;
        int flagCargo=0;
        int flagMoneda=0;


        for (int i=0;i<parametros.length;i++){
            if (parametros[i].getParameterName().equals("C_CAUSA_EMISION_ID")){
                if (flagCausa == 0){
                    causa = parametros[i].getParameter().toString();
                    flagCausa = 1;
                }
                else{
                    causaHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_Charge_ID")){
                if (flagCargo == 0){
                    cargo = parametros[i].getParameter().toString();
                    flagCargo = 1;
                }
                else{
                    cargoHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("M_Product_ID")){
                if (flagProducto == 0){
                    producto = parametros[i].getParameter().toString();
                    flagProducto = 1;
                }
                else{
                    productoHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_PaymentTerm_ID")){
                if (flagCondicion == 0){
                    condicionVenta = parametros[i].getParameter().toString();
                    flagCondicion = 1;
                }
                else{
                    condicionVentaHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_DocType_ID")){
                if (flagDocumento == 0){
                    documento = parametros[i].getParameter().toString();
                    flagDocumento = 1;
                }
                else{
                    documentoHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_BPartner_ID")){
                if (flagCliente == 0){
                    cliente = parametros[i].getParameter().toString();
                    flagCliente = 1;
                }
                else{
                    clienteHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("AD_User_ID")){
                if (flagVendedor == 0){
                    vendedor = parametros[i].getParameter().toString();
                    flagVendedor = 1;
                }
                else{
                    vendedorHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_Currency_ID")){
                if (flagMoneda == 0){
                    moneda = parametros[i].getParameter().toString();
                    flagMoneda = 1;
                }
                else{
                    monedaExposicion = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_SalesRegion_ID")){
                if (flagRegion == 0){
                    region = parametros[i].getParameter().toString();
                    flagRegion = 1;
                }
                else{
                    regionHasta = parametros[i].getParameter().toString();
                }
            }
            else if (parametros[i].getParameterName().equals("C_Location_ID")){
                localizacion = parametros[i].getParameter().toString();
            }
            else if (parametros[i].getParameterName().equals("M_PriceList_ID")){
                listaPrecios = parametros[i].getParameterAsInt();
           }
            else if (parametros[i].getParameterName().equals("FECHA")){
                desdeFecha = parametros[i].getParameter().toString().substring(0, parametros[i].getParameter().toString().length()-2);
                hastaFecha = parametros[i].getParameter_To().toString().substring(0, parametros[i].getParameter_To().toString().length()-2);
            }
            else if (parametros[i].getParameterName().equals("ProductosCargos")){
                productosCargos = parametros[i].getParameter().toString();
           }
            // Determina que tipo de informe es si el general, de zonas, causas de emision o vendedor
            else if (parametros[i].getParameterName().equals("TipoInforme")){
            	tipoInforme = parametros[i].getParameter().toString();
           }            

                
        }
    }


    protected String doIt() throws Exception {


        String sqlWhereProductos = "";
        String sqlWhereCargos = "";

        
        if(productosCargos.equals("Productos-Cargos") || productosCargos.equals("")) {
           sqlWhereProductos = validarParametrosProcuctos();
           sqlWhereCargos = validarParametrosCargos();
        } else if (productosCargos.equals("Productos")){
           sqlWhereProductos = validarParametrosProcuctos();
        } else if (productosCargos.equals("Cargos")){
           sqlWhereCargos = validarParametrosCargos();
        }

        if(sqlWhereProductos.contains("ERROR:")) {
            return sqlWhereProductos;
        }

        if(sqlWhereCargos.contains("ERROR:")) {
            return sqlWhereCargos;
        }

        int flagWhere = 0;
        String sqlWhere = new String("");

        // Inicialmente se deben borrar los registros existentes en la tabla temporal T_ESTADISTICAS

        DB.executeUpdate("Delete from T_Estadisticas", null);


        String consultaProductos = "SELECT i.AD_Client_ID,i.AD_Org_ID,i.IsActive,i.Created,i.CreatedBy,i.Updated,i.UpdatedBy, " +
                "i.IsSOTrx, i.dateinvoiced as fecha, part.c_bpartner_id, part.name as cliente, vend.AD_User_id, " +
                "vend.name as vendedor, locInv.c_location_id, locInv.address1 as localizacion, prod.M_Product_ID, " +
                "prod.value as codigo, prod.name as producto,  doc.c_doctype_id, doc.name as documento, " +
                "reg.c_salesregion_id, reg.name as region, causa.c_causa_emision_id, causa.name, " +
                "condicion.c_paymentterm_id, condicion.name, moneda.c_currency_id, moneda.cursymbol, " +
                "case doc.DOCBASETYPE when 'ARI' then sum(il.linenetamt) " +
                "when 'ARC' then -sum(il.linenetamt) else 0 end  neto, " +
                "case doc.DOCBASETYPE when 'ARI' then sum(il.linenetamt+(il.linenetamt*tax.rate/100)) " +
                "when 'ARC' then -sum(il.linenetamt+(il.linenetamt*tax.rate/100)) else 0 end  total, " +
                "case doc.DOCBASETYPE when 'ARI' then sum(il.qtyinvoiced) " +
                "when 'ARC' then -sum(il.qtyinvoiced) else 0  end  cantidad " +
                
                // Dependencias
                
                "FROM  C_Invoice i " +
                "INNER JOIN C_Invoiceline il ON (i.C_Invoice_ID=il.C_Invoice_ID) " +
                "LEFT JOIN C_Tax tax ON (tax.c_tax_id=il.c_tax_id) " +
                "INNER JOIN C_BPartner part ON (part.C_BPartner_ID=i.C_BPartner_ID) " +
                "LEFT JOIN C_Location locInv ON (i.C_location_id=locInv.C_Location_ID) " +
                "LEFT JOIN C_BPartner_Location loc ON (locInv.C_Location_ID=loc.C_Location_ID) " +
                "INNER JOIN M_Product prod ON (prod.M_Product_ID=il.M_Product_ID) " +
                "INNER JOIN AD_User vend ON (vend.AD_User_ID=i.salesrep_ID) " +
                "INNER JOIN C_Doctype doc ON (doc.c_doctype_id=i.c_doctype_id) " +
                "LEFT JOIN C_Salesregion reg on (loc.c_salesregion_id=reg.c_salesregion_id) " +        
                "LEFT JOIN C_Causa_Emision causa on (causa.C_Causa_Emision_id=i.C_Causa_Emision_id) " +
                "INNER JOIN C_Paymentterm condicion on (condicion.C_Paymentterm_id=i.C_Paymentterm_id) " +
                "INNER JOIN C_Currency moneda on (moneda.C_Currency_id=i.C_Currency_id) ";

        if (sqlWhereProductos.equals(""))
            consultaProductos = consultaProductos.concat(" WHERE i.issotrx like 'Y' and i.docstatus = 'CO' and i.AD_CLIENT_ID = 1000002 and i.AD_ORG_ID = 1000033");
        else
            consultaProductos = consultaProductos.concat(" WHERE  i.issotrx like 'Y' and i.docstatus = 'CO' and i.AD_CLIENT_ID = 1000002 and i.AD_ORG_ID = 1000033 and " + sqlWhereProductos.substring(0, (sqlWhereProductos.length()-4)));


        // Agrupamiento

        consultaProductos = consultaProductos.concat(" GROUP BY i.AD_Client_ID,i.AD_Org_ID,i.IsActive,i.Created,i.CreatedBy,i.Updated,i.UpdatedBy, " +
        "i.IsSOTrx, i.dateinvoiced, part.c_bpartner_id, part.name, vend.AD_User_id, vend.name, " +
        "locInv.c_location_id, locInv.address1, prod.M_Product_ID, prod.value, prod.name, " +
        "doc.c_doctype_id, doc.name, reg.c_salesregion_id, reg.name, causa.c_causa_emision_id, causa.name, " +
        "condicion.c_paymentterm_id, condicion.name, moneda.c_currency_id, moneda.cursymbol, doc.DOCBASETYPE ORDER BY reg.name,prod.value,part.c_bpartner_id");


        String consultaCargos = "SELECT i.AD_Client_ID,i.AD_Org_ID,i.IsActive,i.Created,i.CreatedBy,i.Updated,i.UpdatedBy, " +
        "i.IsSOTrx, i.dateinvoiced as fecha, part.c_bpartner_id, part.name as cliente, vend.AD_User_id, " +
        "vend.name as vendedor, locInv.c_location_id, locInv.address1 as localizacion, cargo.C_Charge_ID, " +
        "cargo.name as codigo, cargo.name as producto,  doc.c_doctype_id, doc.name as documento, " +
        "reg.c_salesregion_id, reg.name as region, causa.c_causa_emision_id, causa.name, " +
        "condicion.c_paymentterm_id, condicion.name, moneda.c_currency_id, moneda.cursymbol, " +
        "case doc.DOCBASETYPE when 'ARI' then sum(il.linenetamt) " +
        "when 'ARC' then -sum(il.linenetamt) else 0 end  neto, " +
        "case doc.DOCBASETYPE when 'ARI' then sum(il.linenetamt+(il.linenetamt*tax.rate/100)) " +
        "when 'ARC' then -sum(il.linenetamt+(il.linenetamt*tax.rate/100)) else 0 end  total, " +
        "case doc.DOCBASETYPE when 'ARI' then sum(il.qtyinvoiced) " +
        "when 'ARC' then -sum(il.qtyinvoiced) else 0  end  cantidad " +

        // Dependencias

        "FROM  C_Invoice i " +
        "INNER JOIN C_Invoiceline il ON (i.C_Invoice_ID=il.C_Invoice_ID) " +
        "LEFT JOIN C_Tax tax ON (tax.c_tax_id=il.c_tax_id) " +
        "INNER JOIN C_BPartner part ON (part.C_BPartner_ID=i.C_BPartner_ID) " +
        "LEFT JOIN C_Location locInv ON (i.C_location_id=locInv.C_Location_ID) " +
        "LEFT JOIN C_BPartner_Location loc ON (locInv.C_Location_ID=loc.C_Location_ID) " +
        "INNER JOIN C_Charge cargo ON (cargo.C_Charge_ID=il.C_Charge_ID) " +
        "INNER JOIN AD_User vend ON (vend.AD_User_ID=i.salesrep_ID) " +
        "INNER JOIN C_Doctype doc ON (doc.c_doctype_id=i.c_doctype_id) " +
        "LEFT JOIN C_Salesregion reg on (loc.c_salesregion_id=reg.c_salesregion_id) " +
        "LEFT JOIN C_Causa_Emision causa on (causa.C_Causa_Emision_id=i.C_Causa_Emision_id) " +
        "INNER JOIN C_Paymentterm condicion on (condicion.C_Paymentterm_id=i.C_Paymentterm_id) " +
        "INNER JOIN C_Currency moneda on (moneda.C_Currency_id=i.C_Currency_id) ";
        if (sqlWhereCargos.equals(""))
            consultaCargos = consultaCargos.concat(" WHERE i.issotrx like 'Y' and i.docstatus = 'CO' and i.AD_CLIENT_ID = 1000002 and i.AD_ORG_ID = 1000033");
        else
            consultaCargos = consultaCargos.concat(" WHERE  i.issotrx like 'Y' and i.docstatus = 'CO' and i.AD_CLIENT_ID = 1000002 and i.AD_ORG_ID = 1000033 and " + sqlWhereCargos.substring(0, (sqlWhereCargos.length()-4)));

        // Agrupamiento

        consultaCargos = consultaCargos.concat(" GROUP BY i.AD_Client_ID,i.AD_Org_ID,i.IsActive,i.Created,i.CreatedBy,i.Updated,i.UpdatedBy, " +
        "i.IsSOTrx, i.dateinvoiced, part.c_bpartner_id, part.name, vend.AD_User_id, vend.name, " +
        "locInv.c_location_id, locInv.address1, cargo.C_Charge_ID, cargo.name, cargo.name, " +
        "doc.c_doctype_id, doc.name, reg.c_salesregion_id, reg.name, causa.c_causa_emision_id, causa.name, " +
        "condicion.c_paymentterm_id, condicion.name, moneda.c_currency_id, moneda.cursymbol, doc.DOCBASETYPE ORDER BY reg.name,cargo.name ,part.c_bpartner_id");

        System.out.println(consultaProductos);
        System.out.println(consultaCargos);

        if(productosCargos.equals("Productos-Cargos") || productosCargos.equals("") || productosCargos.equals("Productos")) {

            Trx trx = Trx.get(Trx.createTrxName("CE"), true);

            PreparedStatement stmEstadisticas = DB.prepareStatement(consultaProductos, ResultSet.TYPE_SCROLL_SENSITIVE,  ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
            ResultSet resEstadisticas = stmEstadisticas.executeQuery();

            String sqlInsert = "";
            PreparedStatement ps = null;

            // Se necesita acumular los registros del mismo producto

            int flagPrimero = 0;
            int acumuladoCantidad = 0;
            BigDecimal acumuladoNeto = Env.ZERO;
            BigDecimal acumuladoFacturado = Env.ZERO;
            BigDecimal acumuladoLista = Env.ZERO;
            String codigoAnt = "";
            String codigoProx = "";
            String zonaAnt = "";
            String zonaProx = "";
            String clienteAnt = "";
            String clienteProx = "";

            while (resEstadisticas.next()) {
            		
            		codigoAnt = resEstadisticas.getString(17);
            		zonaAnt = resEstadisticas.getString(22);
            		clienteAnt = resEstadisticas.getString(11);
                    System.out.println(codigoAnt);

                    if ((codigoAnt != null && zonaAnt != null && clienteAnt != null)&&
                    		(codigoAnt.equals(codigoProx) && zonaAnt.equals(zonaProx) && clienteAnt.equals(clienteProx))
                    		||  flagPrimero == 0)
                    {
                        acumuladoCantidad += resEstadisticas.getInt(31);
                        acumuladoNeto = acumuladoNeto.add(resEstadisticas.getBigDecimal(29));
                        acumuladoFacturado = acumuladoFacturado.add(resEstadisticas.getBigDecimal(30));

                        // Precio de lista va en funcion del parametro

                        if(listaPrecios != 0)
                            acumuladoLista = acumuladoLista.add(precioLista(resEstadisticas.getInt(16)).multiply(resEstadisticas.getBigDecimal(31)));
                        else
                            acumuladoLista = Env.ZERO;

                        codigoProx = codigoAnt;
                        zonaProx = zonaAnt;  
                        clienteProx = clienteAnt;
                        flagPrimero = 1;
                    }
                    else
                    {

                        resEstadisticas.previous();

                        sqlInsert = "INSERT INTO T_Estadisticas" +
                        			" VALUES(?,?,'Y',to_date('" + resEstadisticas.getDate(4) + "','yyyy/mm/dd hh24:mi:ss'),?," +
                        			" to_date('" + resEstadisticas.getDate(6) + "','yyyy/mm/dd hh24:mi:ss'),?,0,?,?,?,?,?,?,?,?,?,?)";

                        ps = DB.prepareStatement(sqlInsert, null);
                        ps.setInt(1, resEstadisticas.getInt(1));            //	CLIENTE
                        ps.setInt(2, resEstadisticas.getInt(2));            //	ORGANIZACION
                        //ps.setDate(3, resEstadisticas.getDate(4));        //	FECHA CREADO
                        ps.setInt(3, resEstadisticas.getInt(5));            //	CREADO POR
                        //ps.setDate(5, resEstadisticas.getDate(6));        //	FECHA  ACTUALIZADO
                        ps.setInt(4, resEstadisticas.getInt(7));            //	ACTUALIZADO POR
                        ps.setString(5, resEstadisticas.getString(13));     //	VENDEDOR
                        ps.setString(6, resEstadisticas.getString(22));     //	ZONA
                        ps.setString(7, resEstadisticas.getString(24));     //	CAUSA EMISION
                        ps.setString(8, resEstadisticas.getString(17));     //	CODIGO
                        ps.setString(9, resEstadisticas.getString(18));     //	PRODUCTO
                        
                        /*
                         * 26-01-2011 Camarzanan Mariano
                         * Agregado del campo cliente
                         */
                        ps.setString(14, resEstadisticas.getString(11));     //	CLIENTE
                        
                        // Valido si la moneda de origen es la misma que la de exposicion, en caso de no ser asi hago la conversion
                        
                        if(moneda.equals(monedaExposicion)){
                            System.out.println("no conversion por iguales " + acumuladoNeto);
                            ps.setBigDecimal(10, acumuladoNeto);                //	NETO
                            ps.setBigDecimal(11, acumuladoFacturado);           //	FACTURADO
                            ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                            ps.setBigDecimal(12, acumuladoLista);               //	LISTA
                        } else {
                            System.out.println("conversion " + acumuladoNeto + " de " + Integer.parseInt(moneda) + " a " + Integer.parseInt(monedaExposicion) + " monto " + MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));
                            ps.setBigDecimal(10, MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));                //	NETO
                            ps.setBigDecimal(11, MConversionRate.convert(this.getCtx(), acumuladoFacturado, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));           //	FACTURADO
                            ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                            ps.setBigDecimal(12, MConversionRate.convert(this.getCtx(), acumuladoLista, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));               //	LISTA
                        }

                        ps.executeUpdate();
                        ps.close();

                        DB.commit(true, get_TrxName());


                        System.out.println("P:" + resEstadisticas.getString(17) + "-C:" + acumuladoCantidad + "-N:" +
                                acumuladoNeto + "-T:" + acumuladoFacturado);

                        codigoProx = codigoAnt;
                		zonaProx = zonaAnt;
                		clienteProx = clienteAnt;
                        if (resEstadisticas.next()){

                        acumuladoCantidad = resEstadisticas.getInt(31);
                        acumuladoNeto = resEstadisticas.getBigDecimal(29);
                        acumuladoFacturado = resEstadisticas.getBigDecimal(30);

                        if(listaPrecios != 0)
                            acumuladoLista = precioLista(resEstadisticas.getInt(16)).multiply(resEstadisticas.getBigDecimal(31));
                        else
                            acumuladoLista = Env.ZERO;

                        }
                    }



            }



            // Cuando termino me resta agregar el ultimo solo en caso que exista al menos 1 registro

            if(flagPrimero == 1)
            {
                if (resEstadisticas.previous()){

                sqlInsert = "INSERT INTO T_Estadisticas VALUES(?,?,'Y',to_date('" + resEstadisticas.getDate(4) + "','yyyy/mm/dd hh24:mi:ss'),?,to_date('" + resEstadisticas.getDate(6) + "','yyyy/mm/dd hh24:mi:ss'),?,0,?,?,?,?,?,?,?,?,?,?)";

                ps = DB.prepareStatement(sqlInsert, null);
                ps.setInt(1, resEstadisticas.getInt(1));                //	CLIENTE
                ps.setInt(2, resEstadisticas.getInt(2));                //	ORGANIZACION
                //ps.setDate(3, resEstadisticas.getDate(4));            //	FECHA CREADO
                ps.setInt(3, resEstadisticas.getInt(5));                //	CREADO POR
                //ps.setDate(5, resEstadisticas.getDate(6));            //	FECHA  ACTUALIZADO
                ps.setInt(4, resEstadisticas.getInt(7));                //	ACTUALIZADO POR
                ps.setString(5, resEstadisticas.getString(13));         //	VENDEDOR
                ps.setString(6, resEstadisticas.getString(22));         //	ZONA
                ps.setString(7, resEstadisticas.getString(24));         //	CAUSA EMISION
                ps.setString(8, resEstadisticas.getString(17));         //	CODIGO
                ps.setString(9, resEstadisticas.getString(18));         //	PRODUCTO
                
                /*
                 * 26-01-2011 Camarzanan Mariano
                 * Agregado del campo cliente
                 */
                ps.setString(14, resEstadisticas.getString(11));     //	CLIENTE


                // Valido si la moneda de origen es la misma que la de exposicion, en caso de no ser asi hago la conversion

                if(moneda.equals(monedaExposicion)){
                    System.out.println("no conversion por iguales " + acumuladoNeto);
                    ps.setBigDecimal(10, acumuladoNeto);                //	NETO
                    ps.setBigDecimal(11, acumuladoFacturado);           //	FACTURADO
                    ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                    ps.setBigDecimal(12, acumuladoLista);               //	LISTA
                } else {
                    System.out.println("conversion " + acumuladoNeto + " de " + Integer.parseInt(moneda) + " a " + Integer.parseInt(monedaExposicion) + " monto " + MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));

                    ps.setBigDecimal(10, MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));                //	NETO
                    ps.setBigDecimal(11, MConversionRate.convert(this.getCtx(), acumuladoFacturado, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));           //	FACTURADO
                    ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                    ps.setBigDecimal(12, MConversionRate.convert(this.getCtx(), acumuladoLista, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));               //	LISTA
                }

                ps.executeUpdate();
                ps.close();

                DB.commit(true, get_TrxName());
            }




            resEstadisticas.close();
            stmEstadisticas.close();

            trx.close();

            }
        }

        if (productosCargos.equals("Productos-Cargos") || productosCargos.equals("") || productosCargos.equals("Cargos")) {

            Trx trx = Trx.get(Trx.createTrxName("CE"), true);

            PreparedStatement stmEstadisticas = DB.prepareStatement(consultaCargos, ResultSet.TYPE_SCROLL_SENSITIVE,  ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
            ResultSet resEstadisticas = stmEstadisticas.executeQuery();

            String sqlInsert = "";
            PreparedStatement ps = null;

            // Se necesita acumular los registros del mismo producto

            int flagPrimero = 0;
            int acumuladoCantidad = 0;
            BigDecimal acumuladoNeto = Env.ZERO;
            BigDecimal acumuladoFacturado = Env.ZERO;
            BigDecimal acumuladoLista = Env.ZERO;
            String codigoAnt = "";
            String codigoProx = "";
            String zonaAnt = "";
            String zonaProx = "";
            String clienteAnt = "";
            String clienteProx = "";

            while (resEstadisticas.next()) {
	        		codigoAnt = resEstadisticas.getString(17);
	        		zonaAnt = resEstadisticas.getString(22);
	        		clienteAnt = resEstadisticas.getString(11);
	                System.out.println(codigoAnt);

                    if ((codigoAnt != null && zonaAnt != null && clienteAnt != null)&&
                    		(codigoAnt.equals(codigoProx) && zonaAnt.equals(zonaProx) && clienteAnt.equals(clienteProx))
                    		||  flagPrimero == 0)
                    {	
                        acumuladoCantidad += resEstadisticas.getInt(31);
                        acumuladoNeto = acumuladoNeto.add(resEstadisticas.getBigDecimal(29));
                        acumuladoFacturado = acumuladoFacturado.add(resEstadisticas.getBigDecimal(30));

                        codigoProx = codigoAnt;
                        zonaProx = zonaAnt;  
                        clienteProx = clienteAnt;
                        flagPrimero = 1;
                    }
                    else
                    {

                        resEstadisticas.previous();

                        sqlInsert = "INSERT INTO T_Estadisticas VALUES(?,?,'Y',to_date('" + resEstadisticas.getDate(4) + "','yyyy/mm/dd hh24:mi:ss'),?,to_date('" + resEstadisticas.getDate(6) + "','yyyy/mm/dd hh24:mi:ss'),?,0,?,?,?,?,?,?,?,?,?,?)";

                        ps = DB.prepareStatement(sqlInsert, null);
                        ps.setInt(1, resEstadisticas.getInt(1));            //	CLIENTE
                        ps.setInt(2, resEstadisticas.getInt(2));            //	ORGANIZACION
                        //ps.setDate(3, resEstadisticas.getDate(4));        //	FECHA CREADO
                        ps.setInt(3, resEstadisticas.getInt(5));            //	CREADO POR
                        //ps.setDate(5, resEstadisticas.getDate(6));        //	FECHA  ACTUALIZADO
                        ps.setInt(4, resEstadisticas.getInt(7));            //	ACTUALIZADO POR
                        ps.setString(5, resEstadisticas.getString(13));     //	VENDEDOR
                        ps.setString(6, resEstadisticas.getString(22));     //	ZONA
                        ps.setString(7, resEstadisticas.getString(24));     //	CAUSA EMISION
                        
                       
                        /*
                         * 04-04-2011 Camarzana Mariano
                         * Comentado ya que no debe ir el codigo cuando se trate de un concepto
                         */
                        //ps.setString(8, resEstadisticas.getString(17));         //	CODIGO

                        
                        ps.setString(8, "");     //	CODIGO
                        
                        
                        ps.setString(9, resEstadisticas.getString(18));     //	PRODUCTO

                        /*
                         * 26-01-2011 Camarzanan Mariano
                         * Agregado del campo cliente
                         */
                        ps.setString(14, resEstadisticas.getString(11));     //	CLIENTE

                        
                        // Valido si la moneda de origen es la misma que la de exposici�n, en caso de no ser as� hago la conversi�n

                        if(moneda.equals(monedaExposicion)){
                            System.out.println("no conversion por iguales " + acumuladoNeto);
                            ps.setBigDecimal(10, acumuladoNeto);                //	NETO
                            ps.setBigDecimal(11, acumuladoFacturado);           //	FACTURADO
                            ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                            ps.setBigDecimal(12, acumuladoLista);               //	LISTA
                        } else {
                            System.out.println("conversion " + acumuladoNeto + " de " + Integer.parseInt(moneda) + " a " + Integer.parseInt(monedaExposicion) + " monto " + MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));

                            ps.setBigDecimal(10, MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));                //	NETO
                            ps.setBigDecimal(11, MConversionRate.convert(this.getCtx(), acumuladoFacturado, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));           //	FACTURADO
                            ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                            ps.setBigDecimal(12, MConversionRate.convert(this.getCtx(), acumuladoLista, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));               //	LISTA
                        }



                        ps.executeUpdate();
                        ps.close();

                        DB.commit(true, get_TrxName());


                        System.out.println("P:" + resEstadisticas.getString(17) + "-C:" + acumuladoCantidad + "-N:" +
                                acumuladoNeto + "-T:" + acumuladoFacturado);

                        codigoProx = codigoAnt;
                		zonaProx = zonaAnt;
                		clienteProx = clienteAnt;
                        
                		resEstadisticas.next();

                        acumuladoCantidad = resEstadisticas.getInt(31);
                        acumuladoNeto = resEstadisticas.getBigDecimal(29);
                        acumuladoFacturado = resEstadisticas.getBigDecimal(30);

                    }



            }

            // Cuando termino me resta agregar el ultimo solo en caso que exista al menos 1 registro

            if(flagPrimero == 1)
            {
                if (resEstadisticas.previous()){

                sqlInsert = "INSERT INTO T_Estadisticas VALUES(?,?,'Y',to_date('" + resEstadisticas.getDate(4) + "','yyyy/mm/dd hh24:mi:ss'),?,to_date('" + resEstadisticas.getDate(6) + "','yyyy/mm/dd hh24:mi:ss'),?,0,?,?,?,?,?,?,?,?,?,?)";

                ps = DB.prepareStatement(sqlInsert, null);
                ps.setInt(1, resEstadisticas.getInt(1));                //	CLIENTE
                ps.setInt(2, resEstadisticas.getInt(2));                //	ORGANIZACION
                //ps.setDate(3, resEstadisticas.getDate(4));            //	FECHA CREADO
                ps.setInt(3, resEstadisticas.getInt(5));                //	CREADO POR
                //ps.setDate(5, resEstadisticas.getDate(6));            //	FECHA  ACTUALIZADO
                ps.setInt(4, resEstadisticas.getInt(7));                //	ACTUALIZADO POR
                ps.setString(5, resEstadisticas.getString(13));         //	VENDEDOR
                ps.setString(6, resEstadisticas.getString(22));         //	ZONA
                ps.setString(7, resEstadisticas.getString(24));         //	CAUSA EMISION
                
                /*
                 * 04-04-2011 Camarzana Mariano
                 * Comentado ya que no debe ir el codigo cuando se trate de un concepto
                 */
                //ps.setString(8, resEstadisticas.getString(17));         //	CODIGO
                
                ps.setString(8, "");         //	CODIGO
                
                
                ps.setString(9, resEstadisticas.getString(18));         //	PRODUCTO
                
                /*
                 * 26-01-2011 Camarzanan Mariano
                 * Agregado del campo cliente
                 */
                ps.setString(14, resEstadisticas.getString(11));     //	CLIENTE
                
                
                // Valido si la moneda de origen es la misma que la de exposicion, en caso de no ser asi hago la conversion

                if(moneda.equals(monedaExposicion)){
                    System.out.println("no conversion por iguales " + acumuladoNeto);
                    ps.setBigDecimal(10, acumuladoNeto);                //	NETO
                    ps.setBigDecimal(11, acumuladoFacturado);           //	FACTURADO
                    ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                    ps.setBigDecimal(12, acumuladoLista);               //	LISTA
                } else {
                    System.out.println("conversion " + acumuladoNeto + " de " + Integer.parseInt(moneda) + " a " + Integer.parseInt(monedaExposicion) + " monto " + MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));

                    ps.setBigDecimal(10, MConversionRate.convert(this.getCtx(), acumuladoNeto, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));                //	NETO
                    ps.setBigDecimal(11, MConversionRate.convert(this.getCtx(), acumuladoFacturado, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));           //	FACTURADO
                    ps.setInt(13, acumuladoCantidad);                   //	CANTIDAD
                    ps.setBigDecimal(12, MConversionRate.convert(this.getCtx(), acumuladoLista, Integer.parseInt(moneda), Integer.parseInt(monedaExposicion), 1000002, 1000033));               //	LISTA
                }


                ps.executeUpdate();
                ps.close();

                DB.commit(true, get_TrxName());
            }




            resEstadisticas.close();
            stmEstadisticas.close();

            trx.close();
            }

        }



        if(tipoInforme.equals("Por Zonas")){
            UtilProcess.initViewer("Estadisticas de Ventas Por Zona",0,getProcessInfo());
    	} else if(tipoInforme.equals("Por Causa de Emision")){
            UtilProcess.initViewer("Estadisticas de Ventas Por Cuasa",0,getProcessInfo());		
		} else if(tipoInforme.equals("Por Vendedor")){
            UtilProcess.initViewer("Estadisticas de Ventas Por Vendedor",0,getProcessInfo());		
		} else {
            UtilProcess.initViewer("Estadisticas de Ventas",0,getProcessInfo());			
		}
        
        return "OK";
	
    }

    /**
     *  Funcion que obtiene a partir de la lista de precios del parametro el precio del producto.
     *
     *  @author jose
     *
     */

    private BigDecimal precioLista(int M_Product_ID) {
        BigDecimal retorno = Env.ZERO;
        try {
            Trx trx = Trx.get(Trx.createTrxName("local"), true);
            
            
            
            String con = "select p.pricelist from m_productprice p " +
                    "inner join m_pricelist_version plv on (p.m_pricelist_version_ID = plv.m_pricelist_version_ID) " +
                    "inner join m_pricelist pl on (pl.m_pricelist_ID = plv.m_pricelist_ID)" +
                    "where p.m_product_ID = " + M_Product_ID + " and pl.m_pricelist_ID = " + listaPrecios;
            
            PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
            ResultSet resLocal = stmlocal.executeQuery();
            while (resLocal.next()) {
                retorno = resLocal.getBigDecimal(1);
            }

            resLocal.close();
            stmlocal.close();
            trx.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
        
    }
    
    
    /**
     *  Funcion que valida la correcta asignacion de parametros y en
     *  funcion de la convinacion, si es correcto, genera la clausula where
     *  en caso contrario regresa un mensaje de error.
     *
     *  Para Productos.
     *
     *  @author jose
     *
     */

    private String validarParametrosProcuctos() {


        String sqlWhereAux = "";

        /* Inicio analisis de causa de emision
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparacion por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nombre del parametro.
         *      si solo tiene el incial tomo como valor inicio el parametro
         *
         *      @author jose
         */

        if(!causa.equals("") && !causaHasta.equals("") && !causa.equals(causaHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_CAUSA_EMISION_ID from C_CAUSA_EMISION where C_CAUSA_EMISION_ID = " + causa + " or C_CAUSA_EMISION_ID = " + causaHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(causa))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Causa de emisi�n mal parametrizada");
                else
                    sqlWhereAux = sqlWhereAux.concat(" causa.name > '" + inicio + "' and causa.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(causa.equals("") && !causaHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_CAUSA_EMISION_ID from C_CAUSA_EMISION where C_CAUSA_EMISION_ID = " + causaHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" causa.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!causa.equals("") && causaHasta.equals("")) || (causa.equals(causaHasta) && !causa.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_Causa_Emision_id = " + causa + " and");
        }

        /* Inicio analisis de producto
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparacion por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del parametro.
         *      si solo tiene el incial tomo como valor iniico el parametro
         *
         *      @author jose
         */

        if(!producto.equals("") && !productoHasta.equals("") && !producto.equals(productoHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, M_Product_ID from M_Product where M_Product_ID = " + producto + " or M_Product_ID = " + productoHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(producto))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Producto mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" prod.name > '" + inicio + "' and prod.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(producto.equals("") && !productoHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, M_Product_ID from M_Product where M_Product_ID = " + productoHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" prod.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!producto.equals("") && productoHasta.equals("")) || (producto.equals(productoHasta) && !producto.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" il.M_Product_id = " + producto + " and");
        }


        /* Inicio an�lisis de condici�n de venta
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!condicionVenta.equals("") && !condicionVentaHasta.equals("") && !condicionVenta.equals(condicionVentaHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_PaymentTerm_ID from C_PaymentTerm where C_PaymentTerm_ID = " + condicionVenta + " or C_PaymentTerm_ID = " + condicionVentaHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(condicionVenta))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Condici�n de venta mal parametrizada");
                else
                    sqlWhereAux = sqlWhereAux.concat(" condicion.name > '" + inicio + "' and condicion.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(condicionVenta.equals("") && !condicionVenta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_PaymentTerm_ID from C_PaymentTerm where C_PaymentTerm_ID = " + condicionVentaHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" condicion.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!condicionVenta.equals("") && condicionVentaHasta.equals("")) || (condicionVenta.equals(condicionVentaHasta) && !condicionVenta.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_Paymentterm_id = " + condicionVenta + " and");
        }



        /* Inicio analisis de vendedor
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparacion por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del parametro.
         *      si solo tiene el incial tomo como valor iniico el parametro
         *
         *      @author jose
         */

        if(!vendedor.equals("") && !vendedorHasta.equals("") && !vendedor.equals(vendedorHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, AD_User_ID from AD_User where AD_User_ID = " + vendedor + " or AD_User_ID = " + vendedorHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(vendedor))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Vendedor mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" vend.name > '" + inicio + "' and vend.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(vendedor.equals("") && !vendedorHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, AD_User_ID from AD_User where AD_User_ID = " + vendedorHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" vend.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!vendedor.equals("") && vendedorHasta.equals("") || (vendedor.equals(vendedorHasta) && !vendedor.equals("")))) //&& !vendedor.equals("0"))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.salesrep_ID = " + vendedor + " and");
        }


        /* Inicio anaisis de cliente
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparacion por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del parametro.
         *      si solo tiene el incial tomo como valor iniiico el parametro
         *
         *      @author jose
         */

        if(!cliente.equals("") && !clienteHasta.equals("") && !cliente.equals(clienteHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_BPartner_ID from C_BPartner where C_BPartner_ID = " + vendedor + " or C_BPartner_ID = " + vendedorHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(cliente))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Cliente mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" part.name > '" + inicio + "' and part.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(cliente.equals("") && !clienteHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_BPartner_ID from C_BPartner where C_BPartner_ID = " + clienteHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" part.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!cliente.equals("") && clienteHasta.equals("")) || (cliente.equals(clienteHasta) && !cliente.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_BPartner_ID = " + cliente + " and");
        }



        /* Inicio analisis de region
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparacion por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del parametro.
         *      si solo tiene el incial tomo como valor inicio el parametro
         *
         *      @author jose
         */

        if(!region.equals("") && !regionHasta.equals("") && !region.equals(regionHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_SalesRegion_ID from C_SalesRegion where C_SalesRegion_ID = " + region + " or C_SalesRegion_ID = " + regionHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(region))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Regi�n de ventas mal parametrizada");
                else
                    sqlWhereAux = sqlWhereAux.concat(" reg.name > '" + inicio + "' and reg.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(region.equals("") && !regionHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_SalesRegion_ID from C_SalesRegion where C_SalesRegion_ID = " + regionHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" reg.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!region.equals("") && regionHasta.equals("")) || (region.equals(regionHasta) && !region.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" reg.c_salesregion_id = " + region + " and");
        }



        /* Inicio analisis de tipos de documentos
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaciion por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del parametro.
         *      si solo tiene el incial tomo como valor inico el parametro
         *
         *      @author jose
         */

        if(!documento.equals("") && !documentoHasta.equals("") && !documento.equals(documentoHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_Doctype_ID from C_Doctype where C_Doctype_ID = " + documento + " or C_Doctype_ID = " + documentoHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(documento))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Tipo de documento mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" doc.name > '" + inicio + "' and doc.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(documento.equals("") && !documentoHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_Doctype_ID from C_Doctype where C_Doctype_ID = " + regionHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" doc.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!documento.equals("") && documentoHasta.equals("")) || (documento.equals(documentoHasta) && !documento.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" doc.c_doctype_id = " + documento + " and");
        }


        if(!desdeFecha.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.dateinvoiced >= TO_DATE('" + desdeFecha + "', 'yyyy/mm/dd hh24:mi:ss')" + " and");

        if(!hastaFecha.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.dateinvoiced <= TO_DATE('" + hastaFecha + "', 'yyyy/mm/dd hh24:mi:ss')" + " and");

        if(!moneda.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.C_Currency_id = " + moneda + " and");

        if(!localizacion.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.C_location_id = " + localizacion + " and");

        return sqlWhereAux;

    }



    /**
     *  Funci�n que valida la correcta asignaci�n de par�metros y en
     *  funci�n de la convinaci�n, si es correcto, genera la cla�sula where
     *  en caso contrario regresa un mensaje de error.
     *
     *  Para cargos.
     *
     *  @author jose
     *
     */

    private String validarParametrosCargos() {


        //private String documento = "";
        //private String desdeFecha = "";
        //private String hastaFecha = "";
        //private String moneda = "";
        //private String region = "";
        //private String regionHasta = "";
        //private String localizacion = "";

        String sqlWhereAux = "";

        /* Inicio an�lisis de causa de emisi�n
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!causa.equals("") && !causaHasta.equals("") && !causa.equals(causaHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_CAUSA_EMISION_ID from C_CAUSA_EMISION where C_CAUSA_EMISION_ID = " + causa + " or C_CAUSA_EMISION_ID = " + causaHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(causa))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Causa de emisi�n mal parametrizada");
                else
                    sqlWhereAux = sqlWhereAux.concat(" causa.name > '" + inicio + "' and causa.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(causa.equals("") && !causaHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_CAUSA_EMISION_ID from C_CAUSA_EMISION where C_CAUSA_EMISION_ID = " + causaHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" causa.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!causa.equals("") && causaHasta.equals("")) || (causa.equals(causaHasta) && !causa.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_Causa_Emision_id = " + causa + " and");
        }




        /* Inicio an�lisis de cargo
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!cargo.equals("") && !cargoHasta.equals("") && !cargo.equals(cargoHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_Charge_ID from C_Charge where C_Charge_ID = " + cargo + " or C_Charge_ID = " + cargoHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(cargo))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Cargo mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" cargo.name > '" + inicio + "' and cargo.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(cargo.equals("") && !cargoHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_Charge_ID from C_Charge where C_Charge_ID = " + cargoHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" cargo.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!cargo.equals("") && cargoHasta.equals("")) || (cargo.equals(cargoHasta) && !cargo.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_Charge_id = " + cargo + " and");
        }




        /* Inicio an�lisis de condici�n de venta
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!condicionVenta.equals("") && !condicionVentaHasta.equals("") && !condicionVenta.equals(condicionVentaHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_PaymentTerm_ID from C_PaymentTerm where C_PaymentTerm_ID = " + condicionVenta + " or C_PaymentTerm_ID = " + condicionVentaHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(condicionVenta))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Condici�n de venta mal parametrizada");
                else
                    sqlWhereAux = sqlWhereAux.concat(" condicion.name > '" + inicio + "' and condicion.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(condicionVenta.equals("") && !condicionVenta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_PaymentTerm_ID from C_PaymentTerm where C_PaymentTerm_ID = " + condicionVentaHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" condicion.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!condicionVenta.equals("") && condicionVentaHasta.equals("")) || (condicionVenta.equals(condicionVentaHasta) && !condicionVenta.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_Paymentterm_id = " + condicionVenta + " and");
        }



        /* Inicio an�lisis de vendedor
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!vendedor.equals("") && !vendedorHasta.equals("") && !vendedor.equals(vendedorHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, AD_User_ID from AD_User where AD_User_ID = " + vendedor + " or AD_User_ID = " + vendedorHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(vendedor))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Vendedor mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" vend.name > '" + inicio + "' and vend.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(vendedor.equals("") && !vendedorHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, AD_User_ID from AD_User where AD_User_ID = " + vendedorHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" vend.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!vendedor.equals("") && vendedorHasta.equals("")) || (vendedor.equals(vendedorHasta) && !vendedor.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.salesrep_ID = " + vendedor + " and");
        }


        /* Inicio an�lisis de cliente
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!cliente.equals("") && !clienteHasta.equals("") && !cliente.equals(clienteHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_BPartner_ID from C_BPartner where C_BPartner_ID = " + vendedor + " or C_BPartner_ID = " + vendedorHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(cliente))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Cliente mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" part.name > '" + inicio + "' and part.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(cliente.equals("") && !clienteHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_BPartner_ID from C_BPartner where C_BPartner_ID = " + clienteHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" part.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!cliente.equals("") && clienteHasta.equals("")) || (cliente.equals(clienteHasta) && !cliente.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" i.C_BPartner_ID = " + cliente + " and");
        }



        /* Inicio an�lisis de region
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!region.equals("") && !regionHasta.equals("") && !region.equals(regionHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_SalesRegion_ID from C_SalesRegion where C_SalesRegion_ID = " + region + " or C_SalesRegion_ID = " + regionHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(region))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Regi�n de ventas mal parametrizada");
                else
                    sqlWhereAux = sqlWhereAux.concat(" reg.name > '" + inicio + "' and reg.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(region.equals("") && !regionHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_SalesRegion_ID from C_SalesRegion where C_SalesRegion_ID = " + regionHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" reg.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!region.equals("") && regionHasta.equals("")) || (region.equals(regionHasta) && !region.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" reg.c_salesregion_id = " + region + " and");
        }



        /* Inicio an�lisis de tipos de documentos
         *      si es rango, valido el incio el fin y tomo los nombres entre uno y otro valor, comparaci�n por nombre.
         *      si solo tiene hasta tomo desde el primero hasta el nonmbre del par�metro.
         *      si solo tiene el incial tomo como valor �nico el par�metro
         *
         *      @author jose
         */

        if(!documento.equals("") && !documentoHasta.equals("") && !documento.equals(documentoHasta))
        {

            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_Doctype_ID from C_Doctype where C_Doctype_ID = " + documento + " or C_Doctype_ID = " + documentoHasta;
                String inicio = "";
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    if(resLocal.getString(2).equals(documento))
                       inicio = resLocal.getString(1);
                    else
                       fin = resLocal.getString(1);
                }
                if(inicio.compareTo(fin) > 0)
                    return ("ERROR: Tipo de documento mal parametrizado");
                else
                    sqlWhereAux = sqlWhereAux.concat(" doc.name > '" + inicio + "' and doc.name < '" + fin + "' and");

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if(documento.equals("") && !documentoHasta.equals(""))
        {
            try {
                Trx trx = Trx.get(Trx.createTrxName("local"), true);
                String con = "select name, C_Doctype_ID from C_Doctype where C_Doctype_ID = " + regionHasta;
                String fin = "";
                PreparedStatement stmlocal = DB.prepareStatement(con, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, trx.getTrxName());
                ResultSet resLocal = stmlocal.executeQuery();
                while (resLocal.next()) {
                    fin = resLocal.getString(1);
                    sqlWhereAux = sqlWhereAux.concat(" doc.name < '" + fin + "' and");
                }

                resLocal.close();
                stmlocal.close();
                trx.close();
            } catch (SQLException ex) {
                Logger.getLogger(GenerarEstadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if((!documento.equals("") && documentoHasta.equals("")) || (documento.equals(documentoHasta) && !documento.equals("")))
        {
            sqlWhereAux = sqlWhereAux.concat(" doc.c_doctype_id = " + documento + " and");
        }


        if(!desdeFecha.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.dateinvoiced >= TO_DATE('" + desdeFecha + "', 'yyyy/mm/dd hh24:mi:ss')" + " and");

        if(!hastaFecha.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.dateinvoiced <= TO_DATE('" + hastaFecha + "', 'yyyy/mm/dd hh24:mi:ss')" + " and");

        if(!moneda.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.C_Currency_id = " + moneda + " and");

        if(!localizacion.equals(""))
            sqlWhereAux = sqlWhereAux.concat(" i.C_location_id = " + localizacion + " and");

        return sqlWhereAux;

    }

    
}
