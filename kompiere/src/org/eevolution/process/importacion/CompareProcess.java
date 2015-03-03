/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

/**
 *
 * @author santiago
 */
public abstract class CompareProcess {

    public static String Resultado_OK = "OK";

    public static int Ausencia_Valor = -1;

    protected abstract boolean Comparar() throws Exception;

    //Conexión donde se van a insertar los datos (Destino)
    private Connection conexionDestino;

    //Coenxion de la BD desde donde se van a obtener los datos (Origen)
    private Connection conexionFuente;

    private String trxName;

    private String NombreColumna;

    private String NombreTabla;
    
    private String mensajeSalida;
    
    private String mensaje;
    
    private String titMensaje;
    
    /**
     * @param conexionInicial the conexionInicial to set
     */
    public void setConexionDestino(Connection conexionInicial) {
        this.conexionDestino = conexionInicial;
    }

    public String getNombreColumna() {
		return NombreColumna;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTitMensaje() {
		return titMensaje;
	}

	public void setTitMensaje(String titMensaje) {
		this.titMensaje = titMensaje;
	}

	public void setNombreColumna(String nombreColumna) {
		NombreColumna = nombreColumna;
	}

	public String getNombreTabla() {
		return NombreTabla;
	}

	public void setNombreTabla(String nombreTabla) {
		NombreTabla = nombreTabla;
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
    
    public String procesar(){
        try{
            JOptionPane.showMessageDialog(null, mensaje, titMensaje, JOptionPane.INFORMATION_MESSAGE);
            Comparar();
        }
        catch(Exception e){
        	e.printStackTrace();
            return getMensajeSalida();
        }
        return "OK";
    }
    
    protected void addBuffersString(StringBuffer into, StringBuffer value, String campo, ResultSet rsInicial, ResultSet rsFuente)	throws Exception{
    	if ( (rsInicial.getString(campo)!=null && rsFuente.getString(campo)==null) ||
    		 (rsInicial.getString(campo)==null && rsFuente.getString(campo)!=null) ||
    		 (rsInicial.getString(campo)!=null && rsFuente.getString(campo)!=null &&
    				 !rsInicial.getString(campo).equals(rsFuente.getString(campo))) )
    	{	into.append("," + campo);
			value.append(",'" + rsFuente.getString(campo) + "'");
    	}
    }
    
    protected void addBuffersInteger(StringBuffer into, StringBuffer value, String campo, ResultSet rsInicial, ResultSet rsFuente)	throws Exception{
    	if (rsInicial.getInt(campo)!=rsFuente.getInt(campo))
    	{	into.append("," + campo);
			value.append("," + rsFuente.getInt(campo));
    	}
    }
    
    protected void addBuffersStringColumna(StringBuffer into, StringBuffer value, String campo, String cInicial, String cFuente)	throws Exception{
		if ((cInicial!=null && cFuente==null) || (cInicial==null && cFuente!=null) || (cInicial!=null && cFuente!=null && !cInicial.equals(cFuente)) )
       	{	into.append("," + campo);
   			value.append(",'N'");
       	}
    }
    
    protected void addBuffersBooleanColumna(StringBuffer into, StringBuffer value, String campo, boolean cInicial, boolean cFuente)	throws Exception{
    	if (cInicial!=cFuente)
    	{	into.append("," + campo);
    		if (cFuente)
    			value.append(",'Y'");
    		else
    			value.append(",'N'");
    	}
    }
    
    protected void addBuffersBigDecimalColumna(StringBuffer into, StringBuffer value, String campo, BigDecimal cInicial, BigDecimal cFuente)	throws Exception{
    	if (cInicial.compareTo(cFuente)!=0)
    	{	into.append("," + campo);
			value.append(",'N'");
    	}
    }
    
    protected void addBuffersIntegerColumna(StringBuffer into, StringBuffer value, String campo, int cInicial, int cFuente)	throws Exception{
    	if (cInicial!=cFuente)
    	{	if (campo.endsWith("_ID"))
   				into.append("," + campo.substring(0, campo.length()-3));
   			else
   				into.append("," + campo);
    		value.append(",'N'");
    	}
    }
     
    protected void addBuffersTimestampColumna(StringBuffer into, StringBuffer value, String campo, Timestamp cInicial, Timestamp cFuente)	throws Exception{
    	if ( (cInicial!=null && cFuente==null) || (cInicial==null && cFuente!=null) ||
       		 (cInicial!=null && cFuente!=null && !cInicial.equals(cFuente)) )
    	{	into.append("," + campo);
			value.append(",'N'");
    	}
    }

}
