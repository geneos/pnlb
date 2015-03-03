/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author santiago
 */
public abstract class ImportProcess {

    public static String Resultado_OK = "ok";

    public static int Ausencia_Valor = -1;

    public abstract String procesar();

    //Conexión donde se van a insertar los datos (Destino)
    private Connection conexionDestino;

    //Coenxion de la BD desde donde se van a obtener los datos (Origen)
    private Connection conexionFuente;

    private String trxName;

    private String NombreObjetoImportar;

    private String mensajeSalida;

    /**
     * @param conexionInicial the conexionInicial to set
     */
    public void setConexionDestino(Connection conexionInicial) {
        this.conexionDestino = conexionInicial;
    }

    /**
     * @param conexionFuente the conexionFuente to set
     */
    public void setConexionFuente(Connection conexionFuente) {
        this.conexionFuente = conexionFuente;
    }

    /**
     * @param trxName the trxName to set
     */
    public void setTrxName(String trxName) {
        this.trxName = trxName;
    }

    /**
     * @return the conexionInicial
     */
    public Connection getConexionDestino() {
        return conexionDestino;
    }

    /**
     * @return the conexionFuente
     */
    public Connection getConexionFuente() {
        return conexionFuente;
    }

    /**
     * @return the trxName
     */
    public String getTrxName() {
        return trxName;
    }

    /**
     * @return the NombreObjetoImportar
     */
    public String getNombreObjetoImportar() {
        return NombreObjetoImportar;
    }

    /**
     * @param NombreObjetoImportar the NombreObjetoImportar to set
     */
    public void setNombreObjetoImportar(String NombreObjetoImportar) {
        this.NombreObjetoImportar = NombreObjetoImportar;
    }

    /** BISion - 20/04/2010 - Santiago Ibañez
     * Metodo que retorna un int dado.
     * @param tableName tabla donde se guarda el ID
     * @param columnName columna que cumple la condicion de igualdad
     * @param value valor utilizado para hacer matching con columnName
     * @param columnSelect columna a seleccionar
     * @param fuente ¿utilizar conexionFuente o conexionDestino?
     * @return
     * @throws java.sql.SQLException
     */
    public int getIDByName(String tableName, String columnName, String value, String columnSelect, boolean fuente) throws SQLException{
        String sqlSelect = "select "+columnSelect+" from "+tableName+" where "+columnName+" like ?";
        PreparedStatement ps = fuente ? getConexionFuente().prepareStatement(sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE) : getConexionDestino().prepareStatement(sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.setString(1, value);
        ResultSet rs = ps.executeQuery();
        int ID = -1;
        if (rs.next()){
            ID = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return ID;
    }

    /** BISion - 20/04/2010 - Santiago Ibañez
     * Método que retorna un String para una columna y tabla dadas
     * @param tableName tabla sobre la cual se quiere obtener el valor
     * @param columnName columna de la tabla utilizada en la condicion de igualdad
     * @param value valor numérico utilizado en la condición de igualdad
     * @param columnSelect columna sobre la cual se quiere obtener el valor
     * @param fuente ¿utilizar conexionFuente o conexionDestino?
     * @return
     * @throws java.sql.SQLException
     */
    public String getValueByID(String tableName, String columnName, int value, String columnSelect, boolean fuente) throws SQLException{
        String sqlSelect = "select "+columnSelect+" from "+tableName+" where "+columnName+" = ?";
        PreparedStatement ps = fuente ? getConexionFuente().prepareStatement(sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE) : getConexionDestino().prepareStatement(sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ps.setInt(1, value);
        ResultSet rs = ps.executeQuery();
        String valueRet = null;
        if (rs.next()){
            valueRet = rs.getString(1);
        }
        rs.close();
        ps.close();
        return valueRet;
    }

    /**
     * Dada una tabla y una clausula where retorna el valor correspondiente a la
     * columna columnSelect
     * @param tableName
     * @param columnSelect
     * @param sqlWhere
     * @param fuente
     * @return
     */
    public int getID(String tableName, String columnSelect, String sqlWhere, boolean fuente) throws SQLException{
        int id=Ausencia_Valor;
        String sqlSelect = "select "+columnSelect+" from "+tableName+" where "+sqlWhere;
        PreparedStatement ps = fuente ? getConexionFuente().prepareStatement(sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE) : getConexionDestino().prepareStatement(sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            id = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return id;
    }

    /**
     * @return the mensajeSalida
     */
    public String getMensajeSalida() {
        return mensajeSalida;
    }

    /**
     * @param mensajeSalida the mensajeSalida to set
     */
    public void setMensajeSalida(String mensajeSalida) {
        this.mensajeSalida = mensajeSalida;
    }


}
