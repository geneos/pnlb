/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.model.X_AD_Window;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author santiago
 */
public class ImportWindow extends ImportProcess{

    private int WinWidth;                                                                                                                                                                                                                                           
    private int WinHeight;                                                                                                                                                                                                                                          
    private int AD_Window_ID;                                                                                                                                                                                                                                       
    private String Name;                                                                                                                                                                                                                                            
    private String Description;                                                                                                                                                                                                                                     
    private String Help;                                                                                                                                                                                                                                            ;                                                                                                                                                                                                                                      
    private String WindowType;                                                                                                                                                                                                                                      
    private String EntityType;                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
    private int AD_Org_ID;                                                                                                                                                                                                                                          
    private int AD_Image_ID;                                                                                                                                                                                                                                        
    private int AD_Color_ID;                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
    private boolean IsSOTrx;                                                                                                                                                                                                                                        
    private boolean IsDefault;                                                                                                                                                                                                                                      
    private boolean IsBetaFunctionality;                                                                                                                                                                                                                            
    private boolean IsActive;

    @Override
    public String procesar() {
        try {
            setAtributosVentana();
            X_AD_Window w = crearVentana();
            setAtributosTraduccionVentana();
            crearTraduccionVentana(w.getAD_Window_ID());
            //Obtengo los Tabs a Importar
            String sql = "select name from ad_tab where ad_window_id = "+AD_Window_ID;
            PreparedStatement sqlIns = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = sqlIns.executeQuery();
            if (rs.next()){
                ImportTab process = new ImportTab(getConexionDestino(),getConexionFuente(),rs.getString(1), getTrxName());
                process.resetParametrosTab();
                process.setAD_Window_ID(AD_Window_ID);
                String rta = process.procesar();
                if (!rta.equals("ok")){
                    return rta;
                }
            }
            rs.close();
            sqlIns.close();

        } catch (SQLException ex) {
            Logger.getLogger(ImportWindow.class.getName()).log(Level.SEVERE, null, ex);
            if (getMensajeSalida()==null || getMensajeSalida().equals(""))
                setMensajeSalida(ex.getMessage());
            return getMensajeSalida();
        }
        return "ok";
    }

    /** BISion - 22/04/2010 - Santiago Ibañez
     * Metodo que setea todos los atributos de la ventana que se quiere importar
     * @throws java.sql.SQLException
     */
    private void setAtributosVentana() throws SQLException{
        String sql = "select * from AD_Window where name = '"+getNombreObjetoImportar()+"'";
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            WinWidth = rs.getInt("WinWidth");
            WinHeight = rs.getInt("WinHeight");
            Name = rs.getString("Name");
            Description = rs.getString("Description");
            Help = rs.getString("Help");
            WindowType = rs.getString("WindowType");
            EntityType = rs.getString("EntityType");
            AD_Window_ID = rs.getInt("AD_Window_ID");
            AD_Org_ID = rs.getInt("AD_Org_ID");
            AD_Image_ID = rs.getInt("AD_Image_ID");
            AD_Color_ID = rs.getInt("AD_Color_ID");
            IsSOTrx = rs.getString("IsSOTrx").equals("Y") ? true : false;
            IsDefault = rs.getString("IsDefault").equals("Y") ? true : false;
            IsBetaFunctionality = rs.getString("IsBetaFunctionality").equals("Y") ? true : false;
            IsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        ps.close();
    }

    /** BISion - 22/04/2010 - Santiago Ibañez
     * Método que crea la Ventana con los atributos seteados en la BD destino
     * @return
     */
    private X_AD_Window crearVentana(){
        X_AD_Window window = new X_AD_Window(Env.getCtx(), 0, null);
        window.setAD_Color_ID(AD_Color_ID);
        window.setAD_Image_ID(AD_Image_ID);
        window.setAD_Org_ID(AD_Org_ID);
        window.setDescription(Description);
        window.setEntityType(EntityType);
        window.setHelp(Help);
        window.setIsActive(IsActive);
        window.setIsBetaFunctionality(IsBetaFunctionality);
        window.setIsDefault(IsDefault);
        window.setIsSOTrx(IsSOTrx);
        window.setName(Name);                                                                         
        window.setWinHeight(WinHeight);
        window.setWinWidth(WinWidth);
        window.setWindowType(WindowType);
        window.save();
        System.out.println("Ventana creada: "+window.getName());
        return window;
    }

    private String WTName;
    private String WTDescription;
    private String WTHelp;
    private String WTAD_Language;

    private int WTAD_Org_ID;
    private int WTAD_Client_ID;
    private boolean WTIsTranslated;
    private boolean WTIsActive;

    private void setAtributosTraduccionVentana() throws SQLException{
        String sql = "select * from AD_Window_Trl where AD_Window_ID = "+AD_Window_ID;
        PreparedStatement sqlIns = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = sqlIns.executeQuery();
        if (rs.next()){
            WTName = rs.getString("Name");
            WTDescription = rs.getString("Description");
            WTHelp = rs.getString("Help");
            WTAD_Language = rs.getString("AD_Language");
            WTAD_Org_ID = rs.getInt("AD_Org_ID");
            WTAD_Client_ID = rs.getInt("AD_Client_ID");
            WTIsTranslated = rs.getString("IsTranslated").equals("Y") ? true : false;
            WTIsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        sqlIns.close();
    }

    private void crearTraduccionVentana(int AD_Window_ID) throws SQLException{
        String sqlUpdate = "update AD_Window_Trl set Name = ?,Description = ?,Help=?,AD_Language=?,AD_Window_ID=?,AD_Org_ID=?,AD_Client_ID=?,IsTranslated=?,IsActive=? where AD_Window_ID=?";
        PreparedStatement ps = DB.prepareStatement(sqlUpdate, getTrxName());
        ps.setString(1, WTName);
        ps.setString(2, WTDescription);
        ps.setString(3, WTHelp);
        ps.setString(4, WTAD_Language);
        ps.setInt(5, AD_Window_ID);
        ps.setInt(6, WTAD_Org_ID);
        ps.setInt(7, WTAD_Client_ID);
        ps.setString(8, WTIsTranslated ? "Y" : "N");
        ps.setString(9, WTIsActive ? "Y" : "N");
        ps.setInt(10, AD_Window_ID);
        ps.executeUpdate();
        ps.close();
    }

    public ImportWindow(Connection cd, Connection cf, String name){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreObjetoImportar(name);
    }



}
