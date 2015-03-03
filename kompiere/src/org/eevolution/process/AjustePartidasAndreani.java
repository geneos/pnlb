/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MLot;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.eevolution.tools.UtilProcess;

/**
 *
 * @author santiago
 */
public class AjustePartidasAndreani extends SvrProcess{

    //Dep. Andreani U. y Dep. Exp. Andreani U.
    private int ubicacionesAndreani[]={1000262,1000279};
    private Timestamp from;
    private Timestamp to;

    @Override
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i=0;i<para.length;i++){
            from = (Timestamp) para[i].getParameter();
            to = (Timestamp) para[i].getParameter_To();
        }
    }

    @Override
    protected String doIt() throws Exception {
        //corregirPartidasDefectuosas();
        //vacio la tabla
        DB.executeUpdate("delete from T_HISTORICOSTOCK_ANDREANI", get_TrxName());
        //ingresar las partidas en una tabla (partida-cantidad-ubicacion)
        cargarTabla();
        //poner en cero las partidas (actualizar storage)
        actualizarStorage();
        //comiteo los cambios para que el informe tenga datos para mostrar
        Trx.get(get_TrxName(), false).commit();
        //mostrar informe
        UtilProcess.initViewer("Informe Stock Historico Andreani", getAD_PInstance_ID(), getProcessInfo());
        return "ok";
    }

    private void cargarTabla() throws SQLException{

        int AD_Client_ID;
        int AD_Org_ID;
        BigDecimal qtyOnHand;
        int M_Locator_ID;
        int M_AttributeSetInstance_ID;
        String description;
        Timestamp created;

        //Busco las partidas con los depositos considerados
        String sqlSel = "select * from m_storage where m_locator_id in (";
        for (int i=0;i<ubicacionesAndreani.length-1;i++)
            sqlSel += ubicacionesAndreani[i]+", ";
        sqlSel += ubicacionesAndreani[ubicacionesAndreani.length-1]+")";
        if (from!=null&&!from.toString().equals(""))
            sqlSel+= " and created >= ? ";
        if (to!=null&&!to.toString().equals(""))
            sqlSel+= " and created <= ? ";
        sqlSel += " and qtyOnHand <> 0";
        PreparedStatement psSelect = DB.prepareStatement(sqlSel, get_TrxName());

        if (from!=null&&!from.toString().equals(""))
            psSelect.setTimestamp(1, from);
        if (to!=null&&!to.toString().equals("")){
            if (from!=null&&!from.toString().equals(""))
                psSelect.setTimestamp(2, to);
            else
                psSelect.setTimestamp(1, to);
        }
        ResultSet rs = psSelect.executeQuery();
        while (rs.next()){
            AD_Client_ID = rs.getInt("AD_Client_ID");
            AD_Org_ID = rs.getInt("AD_Org_ID");
            qtyOnHand = rs.getBigDecimal("qtyOnHand");
            M_Locator_ID = rs.getInt("M_Locator_ID");
            M_AttributeSetInstance_ID = rs.getInt("M_AttributeSetInstance_ID");
            MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), M_AttributeSetInstance_ID, get_TrxName());
            description = asi.getDescription();
            created = rs.getTimestamp("Created");
            String sqlIns = "insert into T_HISTORICOSTOCK_ANDREANI values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlIns, get_TrxName());
            ps.setInt(1, AD_Client_ID);
            ps.setInt(2, AD_Org_ID);
            ps.setString(3, "Y");
            ps.setInt(4, M_AttributeSetInstance_ID);
            ps.setString(5, description);
            ps.setBigDecimal(6, qtyOnHand);
            ps.setInt(7, M_Locator_ID);
            ps.setInt(8, getAD_PInstance_ID());
            ps.setTimestamp(9, created);
            ps.executeUpdate();
            ps.close();
        }
        rs.close();
        psSelect.close();
    }

    private void actualizarStorage() throws SQLException{
        String sqlUpdate = "update m_storage set qtyonhand = 0 where m_locator_id in (";
        for (int i=0;i<ubicacionesAndreani.length-1;i++)
            sqlUpdate += ubicacionesAndreani[i]+", ";
        sqlUpdate += ubicacionesAndreani[ubicacionesAndreani.length-1]+")";
        if (from!=null&&!from.toString().equals(""))
            sqlUpdate+= " and created >= ? ";
        if (to!=null&&!to.toString().equals(""))
            sqlUpdate+= " and created <= ? ";
        sqlUpdate += " and qtyOnHand <> 0";
        PreparedStatement psUpdate = DB.prepareStatement(sqlUpdate, get_TrxName());
        if (from!=null&&!from.toString().equals(""))
            psUpdate.setTimestamp(1, from);
        if (to!=null&&!to.toString().equals("")){
            if (from!=null&&!from.toString().equals(""))
                psUpdate.setTimestamp(2, to);
            else
                psUpdate.setTimestamp(1, to);
        }
        psUpdate.executeUpdate();
        psUpdate.close();
    }

    private String corregirPartidaDefectuosa(int M_AttributeSetInstance_ID) throws SQLException{
        //Obtener las partidas
        //obtener analisis
        //Comentado porque por ahora solo se corrige la descripcion
        //String analisis = getAnalisis(M_AttributeSetInstance_ID);
        //if (!analisis.matches("[0-9]+"))
            //analisis = "";
        //fin comentarios
        String analisisDescripcion = getAnalisisFromDescription(M_AttributeSetInstance_ID);
        //String analisisCorregido = getMayorAnalisisCorregido(analisisDescripcion, analisis);
        //corregir analisis

        //obtener lote
        String lote = getLoteFromDescription(M_AttributeSetInstance_ID);
        //corregir lote
        String fechaGarantia = getFechaFromDescription(M_AttributeSetInstance_ID);
        if (fechaGarantia.startsWith("_"))
            fechaGarantia = fechaGarantia.substring(1, fechaGarantia.length());
        String descripcionFinal = "";
        if (!analisisDescripcion.equals(""))
            descripcionFinal = analisisDescripcion;
        if (!lote.equals("")){
            if (!descripcionFinal.equals(""))
                descripcionFinal +="_«"+lote+"»";
            else
                descripcionFinal += "«"+lote+"»";;     
        }
        if (!fechaGarantia.equals("")){
            if (!descripcionFinal.equals(""))
                descripcionFinal += "_"+fechaGarantia;
            else
                descripcionFinal += fechaGarantia;
        }
            
        return descripcionFinal;
        //setear descripcion de la partida con el analisis y lote corregidos
    }
    
    private void corregirPartidasDefectuosas() throws SQLException{
        String partidaModelo = "9147_«015»_25/07/2009";
        String k = "123";
        if (k.matches("[0-9]{3}"))
            System.out.println("Hay matching");
        String sql = "select description, m_attributesetinstance_id from m_attributesetinstance";
        PreparedStatement ps = DB.prepareStatement(sql,null);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String s = rs.getString(1);
            //String s = partidaModelo;
            if (s==null)
                continue;
            if (s!=null)
                s = s.replaceAll("/", "");
            int id = rs.getInt(2);
            int j;
            if (id==1009972)
                j=0;
            boolean matching = false;
            //analisis_«lote»_fecha
            if (s!=null && s.matches("[0-9]+\\_«[0-9]+»\\_[0-9]{8}"))
                matching = true;
            //«lote»_fecha
            if (s!=null && s.matches("«[0-9]+»\\_[0-9]{8}"))
                matching = true;
            //«lote»
            if (s!=null && s.matches("«[0-9]+»"))
                matching = true;
            //«loteAlfanumerico»_fecha
            if (s!=null && s.matches("«[A-Z0-9]+»\\_[0-9]{8}"))
                matching = true;
            //
            if (s!=null && s.matches("«[A-Z0-9]+»\\_[0-9]{8}"))
                matching = true;
            //«loteAlfanumerico»
            if (s!=null && s.matches("«[A-Z0-9]+»"))
                matching = true;
            //analisis_«lote»
            if (s!=null && s.matches("[0-9]+\\_«[0-9]+»"))
                matching = true;
            //analisis_«loteAlfanumerico»
            if (s!=null && s.matches("[0-9]+\\_«[A-Z0-9]+»"))
                matching = true;
            //analisis_«loteAlfanumerico»_fecha
            if (s!=null && s.matches("[0-9]+\\_«[A-Z0-9]+»\\_[0-9]{8}"))
                matching = true;
            if (!matching && id!=0){
               if (id==1001417 || id ==1007498)
                   System.out.println("ACA");
               String ac = corregirPartidaDefectuosa(id);
               System.out.println("("+id+") Partida: "+rs.getString(1)+" ***ANALISIS CORREGIDO: "+ac);
               String sqlUpdate = "update m_attributesetinstance set description = '"+ac+"' where m_attributesetinstance_id = "+id;
               DB.executeUpdate(sqlUpdate, get_TrxName());
            }
        }
        rs.close();
        ps.close();
    }

    /** BISion - 05-
     * Retorna el mayor Analisis de la descripcion de la partida o de la tabla
     * de M_AttributeInstance
     * @param AnalisisPartida
     * @param Analisis
     * @return
     */
    private String getMayorAnalisisCorregido(String AnalisisPartida, String Analisis){
        String analisisCorregido;
        if (AnalisisPartida.length()>Analisis.length())
            analisisCorregido = AnalisisPartida;
        else if (AnalisisPartida.equals(Analisis))
            analisisCorregido = Analisis;
        else
            analisisCorregido = AnalisisPartida;

        return analisisCorregido;
    }

    /** BISion - 05/03/2010 - Santiago Ibañez
     * Metodo que retorna el Analisis de una partida dada (desde la tabla M_AttributeInstance)
     * @param M_AttributeSetInstance_ID
     * @return
     */
    private String getAnalisis(int M_AttributeSetInstance_ID){
        String nroAnalisis = "";
        try {
            String sql = "select value from m_attributeinstance where m_attributesetinstance_id = " + M_AttributeSetInstance_ID;
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nroAnalisis = rs.getString(1);
            }
        } catch (SQLException sQLException) {
            System.out.println("No se pudo obtener el Nro. de Analisis");
        }
        if (nroAnalisis!=null)
            return getAnalisisEnFormaNumerica(nroAnalisis);
        else
            return "";
    }

    /**
     * 
     * @param M_AttributeSetInstance_ID
     * @return
     */
    private String getAnalisisFromDescription(int M_AttributeSetInstance_ID){
        MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), M_AttributeSetInstance_ID, get_TrxName());
        String description = asi.getDescription();
        if (description == null)
            return "";
        int indiceSepLote = description.indexOf("«");
        String analisis;
        //Si no esta presente el lote
        if (indiceSepLote!=-1)
            analisis = description.substring(0,indiceSepLote);
        else
            analisis = description;
        return getAnalisisEnFormaNumerica(analisis);
    }

    /**
     * Metodo que retorna el numero mas largo a partir de un string
     * @param descripcion
     * @return
     */
    private String getAnalisisEnFormaNumerica(String descripcion){
        String analisisCorregido = "";
        String analisisCandidato ="";
        //Proceso de extraccion del analisis
        for (int i=0;i<descripcion.length();i++){
            String caracter = descripcion.charAt(i)+"";
            if (!esNumerico(caracter) && analisisCandidato.length()!=0){
                if (analisisCandidato.length()>analisisCorregido.length())
                    analisisCorregido = analisisCandidato;
                analisisCandidato = "";
            }
            else if (esNumerico(caracter)){
                //Agrego si es numerico
                analisisCandidato+=caracter;
            }
        }
        if (analisisCandidato.length()>analisisCorregido.length())
            analisisCorregido = analisisCandidato;
        return analisisCorregido;
    }

    private boolean esNumerico(String s){
        return s.matches("[0-9]");
    }

    private String getLoteFromDescription(int M_AttributeSetInstance_ID){
        MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), M_AttributeSetInstance_ID, get_TrxName());
        String descripcion = asi.getDescription();
        String lote = "";
        //indice de comienzo del lote
        int comienzo = descripcion.indexOf("«");
        if (comienzo==-1)
            return "";
        if (descripcion.contains("VTO:") && descripcion.indexOf("VTO:")>comienzo){
            int fin = descripcion.indexOf("VTO:");
            lote = descripcion.substring(comienzo+1, fin);
        }
        else if (descripcion.contains("V:") && descripcion.indexOf("V:")>comienzo){
            int fin = descripcion.indexOf("V:");
            lote = descripcion.substring(comienzo+1, fin);
        }
        else{
            int fin = descripcion.indexOf("»");
            lote = descripcion.substring(comienzo+1, fin);
        }
        return lote;
    }

    private String getFechaFromDescription(int M_AttributeSetInstance_ID){
        MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), M_AttributeSetInstance_ID, get_TrxName());
        String descripcion = asi.getDescription();
        int comienzo = descripcion.indexOf("»");
        if (comienzo==-1){
            int pos = descripcion.lastIndexOf("_");
            if (pos==-1)
                return "";
            else
                return descripcion.substring(pos,descripcion.length());
        }
        return descripcion.substring(comienzo+1, descripcion.length());
    }

    private String getLote(int M_AttributeSetInstance_ID){
        MAttributeSetInstance asi = new MAttributeSetInstance(getCtx(), M_AttributeSetInstance_ID, get_TrxName());
        int lot = asi.getM_Lot_ID();
        MLot lote = new MLot(getCtx(), lot, get_TrxName());
        return lote.getName();
    }

}
