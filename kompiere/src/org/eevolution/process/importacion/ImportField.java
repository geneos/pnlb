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
import org.compiere.model.X_AD_Field;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author santiago
 */
public class ImportField extends ImportProcess{
    private String Name;
    private String Description;
    private String Help;
    private String DisplayLogic;
    private String ObscureType;
    private String EntityType;
    private int Included_Tab_ID;
    private int AD_Tab_ID;
    private int AD_Org_ID;
    private int AD_FieldGroup_ID;
    private int AD_Column_ID;
    private boolean IsSameLine;
    private boolean IsReadOnly;
    private boolean IsHeading;
    private boolean IsFieldOnly;
    private boolean IsEncrypted;
    private boolean IsDisplayed;
    private boolean IsCentrallyMaintained;
    private boolean IsActive;
    private BigDecimal SortNo;
    private int SeqNo;
    private int DisplayLength;
    private int AD_Field_ID;

    public ImportField(Connection cd, Connection cf, String name, String trxName){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreObjetoImportar(name);
        setTrxName(trxName);
    }

    private void setAtributosField() throws SQLException{
        String sql = "select * from AD_Field where name = '"+getNombreObjetoImportar()+"' and AD_Tab_ID = "+AD_Tab_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            Name = rs.getString("Name");
            Description = rs.getString("Description");
            Help = rs.getString("Help");
            DisplayLogic = rs.getString("DisplayLogic");
            ObscureType = rs.getString("ObscureType");
            EntityType = rs.getString("EntityType");
            Included_Tab_ID = rs.getInt("Included_Tab_ID");
            AD_Org_ID = rs.getInt("AD_Org_ID");
            AD_FieldGroup_ID = rs.getInt("AD_FieldGroup_ID");
            AD_Column_ID = rs.getInt("AD_Column_ID");
            IsSameLine = rs.getString("IsSameLine").equals("Y") ? true : false;
            IsReadOnly = rs.getString("IsReadOnly").equals("Y") ? true : false;
            IsHeading = rs.getString("IsHeading").equals("Y") ? true : false;
            IsFieldOnly = rs.getString("IsFieldOnly").equals("Y") ? true : false;
            IsEncrypted = rs.getString("IsEncrypted").equals("Y") ? true : false;
            IsDisplayed = rs.getString("IsDisplayed").equals("Y") ? true : false;
            IsCentrallyMaintained = rs.getString("IsCentrallyMaintained").equals("Y") ? true : false;
            IsActive = rs.getString("IsActive").equals("Y") ? true : false;
            SortNo = rs.getBigDecimal("SortNo");
            SeqNo = rs.getInt("SeqNo");
            DisplayLength = rs.getInt("DisplayLength");
            AD_Field_ID = rs.getInt("AD_Field_ID");
        }
        rs.close();
        ps.close();
    }

    private X_AD_Field crearField() throws SQLException{
        X_AD_Field field = new X_AD_Field(Env.getCtx(), 0, getTrxName());
        field.setAD_Column_ID(AD_Column_ID);

        if (AD_FieldGroup_ID!=0){
            String fieldGroup = getValueByID("AD_FieldGroup", "AD_FieldGroup_ID", AD_FieldGroup_ID, "name", true);
            int idFieldGroup = getIDByName("AD_Procss", "name", fieldGroup, "AD_Process_ID", false);
            if (idFieldGroup!=ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe el Field Group: "+fieldGroup);
                throw new RuntimeException();
            }
            field.setAD_FieldGroup_ID(idFieldGroup);
        }

        field.setAD_Org_ID(AD_Org_ID);

        /*if (AD_Tab_ID!=0){
            String tabName = getValueByID("AD_Tab", "AD_Tab_ID", AD_Tab_ID, "name", true);
            int idTab = getIDByName("AD_Tab", "name", tabName, "AD_Tab_ID", false);
            if (idTab==ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe el Tab: "+tabName);
                throw new RuntimeException();
            }
            field.setAD_Tab_ID(idTab);
        }*/

        field.setAD_Tab_ID(tabPadre);
        field.setDescription(Description);
        field.setDisplayLength(DisplayLength);
        field.setDisplayLogic(DisplayLogic);
        field.setEntityType(EntityType);
        field.setHelp(Help);
        field.setIncluded_Tab_ID(Included_Tab_ID);
        field.setIsActive(IsActive);
        field.setIsCentrallyMaintained(IsCentrallyMaintained);
        field.setIsDisplayed(IsDisplayed);
        field.setIsEncrypted(IsEncrypted);
        field.setIsFieldOnly(IsFieldOnly);
        field.setIsHeading(IsHeading);
        field.setIsReadOnly(IsReadOnly);
        field.setIsSameLine(IsSameLine);
        field.setName(Name);
        field.setObscureType(ObscureType);
        field.setSeqNo(SeqNo);
        field.setSortNo(SortNo);
        field.save();
        System.out.println("Field creado: "+field.getName());
        return field;
    }

    @Override
    public String procesar() {
        try{
            setAtributosField();
            X_AD_Field field = crearField();
            setAtributosTraduccionField();
            crearTraduccionField(field.getAD_Field_ID());
        }
        catch(Exception e){
            if (getMensajeSalida()==null||getMensajeSalida().equals(""))
                return e.getMessage();
            else
                return getMensajeSalida();
        }
        return "ok";
    }

    /**
     * @param AD_Tab_ID the AD_Tab_ID to set
     */
    public void setAD_Tab_ID(int AD_Tab_ID) {
        this.AD_Tab_ID = AD_Tab_ID;
    }

    private String FTName;
    private String FTDescription;
    private String FTHelp;
    private String FTAD_Language;
    private int FTAD_Org_ID;
    private int FTAD_Client_ID;
    private boolean FTIsTranslated;
    private boolean FTIsActive;

    private void setAtributosTraduccionField() throws SQLException{
        String sql = "select * from AD_Field_Trl where AD_Field_ID = "+AD_Field_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            FTName = rs.getString("Name");
            FTDescription = rs.getString("Description");
            FTHelp = rs.getString("Help");
            FTAD_Language = rs.getString("AD_Language");                                                                                                                                                                                                                       
            FTAD_Org_ID = rs.getInt("AD_Org_ID");                                                                                                                                                                                                                                                                                                                                                                                                                                               
            FTAD_Client_ID = rs.getInt("AD_Client_ID");                                                                                                                                                                                                                     
            FTIsTranslated = rs.getString("IsTranslated").equals("Y") ? true : false;                                                                                                                                                                                       
            FTIsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        ps.close();
    }

    private void crearTraduccionField(int AD_Field_ID) throws SQLException{
        String sql = "update ad_field_trl set Name=?, Description=?, Help=?, AD_Language=?, AD_Org_ID=?, AD_Field_ID=?, IsTranslated=?,IsActive=? where AD_Field_ID = ?";
        PreparedStatement sqlUpdate = DB.prepareStatement(sql, getTrxName());
        sqlUpdate.setString(1, FTName);
        sqlUpdate.setString(2, FTDescription);
        sqlUpdate.setString(3, FTHelp);
        sqlUpdate.setString(4, FTAD_Language);
        sqlUpdate.setInt(5, FTAD_Org_ID);
        sqlUpdate.setInt(6, AD_Field_ID);
        sqlUpdate.setString(7, FTIsTranslated ? "Y" : "N");
        sqlUpdate.setString(8, FTIsActive ? "Y" : "N");
        sqlUpdate.setInt(9, AD_Field_ID);
        sqlUpdate.executeUpdate();
        sqlUpdate.close();
    }

    /** BISion - 27/04/2010 - Santiago Ibañez
     * Esta variable existe porque cuando se consulta por el tab padre utilzando
     * la clase Connection, el tab aún no existe ya que es creado en forma transaccional
     * y las transacciones no se emplean con las consultas mediante Connection
     */
    private int tabPadre;

    /**
     * @param tabPadre the tabPadre to set
     */
    public void setTabPadre(int tabPadre) {
        this.tabPadre = tabPadre;
    }
}
