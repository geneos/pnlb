/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.compiere.model.M_Column;
import org.compiere.model.X_AD_Tab;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author santiago
 */
public class ImportTab extends ImportProcess{
    
    private String WhereClause;
    private String ReadOnlyLogic;
    private String OrderByClause;
    private String Name;
    private String DisplayLogic;
    private String Description;
    private String Help;
    private String CommitWarning;
    private String EntityType;
    private int Included_Tab_ID;
    private int AD_ColumnSortYesNo_ID;
    private int AD_ColumnSortOrder_ID;
    private int AD_Window_ID;
    private int AD_Table_ID;
    private int AD_Process_ID;
    private int AD_Org_ID;
    private int AD_Image_ID;
    private int AD_Column_ID;
    private int AD_Client_ID;
    private boolean IsTranslationTab;
    private boolean IsSortTab;
    private boolean IsSingleRow;
    private boolean IsReadOnly;
    private boolean IsInsertRecord;
    private boolean IsInfoTab;
    private boolean IsAdvancedTab;
    private boolean IsActive;
    private boolean HasTree;
    private int TabLevel;
    private int SeqNo;
    private int AD_Tab_ID;

    public void resetParametrosTab(){
        WhereClause = "";
        ReadOnlyLogic = "";
        OrderByClause = "";
        Name = "";
        DisplayLogic = "";
        Description = "";
        Help = "";
        CommitWarning = "";
        EntityType = "";
        Included_Tab_ID = 0;
        AD_ColumnSortYesNo_ID = 0;
        AD_ColumnSortOrder_ID = 0;
        setAD_Window_ID(0);
        AD_Table_ID = 0;
        AD_Process_ID = 0;
        AD_Org_ID = 0;
        AD_Image_ID = 0;
        AD_Column_ID = 0;
        AD_Client_ID = 0;
        TabLevel = 0;
        SeqNo = 0;
        AD_Tab_ID = 0;
    }

    private void setAtributosTab(int AD_Window_ID) throws SQLException{
        String sql = "select * from AD_Tab where name = '"+getNombreObjetoImportar()+"' and AD_Window_ID = "+AD_Window_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            WhereClause = rs.getString("WhereClause");
            ReadOnlyLogic = rs.getString("ReadOnlyLogic");
            OrderByClause = rs.getString("OrderByClause");
            Name = rs.getString("Name");
            DisplayLogic = rs.getString("DisplayLogic");
            Description = rs.getString("Description");
            Help = rs.getString("Help");
            CommitWarning = rs.getString("CommitWarning");
            EntityType = rs.getString("EntityType");
            Included_Tab_ID = rs.getInt("Included_Tab_ID");
            AD_ColumnSortYesNo_ID = rs.getInt("AD_ColumnSortYesNo_ID");
            AD_ColumnSortOrder_ID = rs.getInt("AD_ColumnSortOrder_ID");
            AD_Window_ID = rs.getInt("AD_Window_ID");
            AD_Table_ID = rs.getInt("AD_Table_ID");
            AD_Process_ID = rs.getInt("AD_Process_ID");
            AD_Org_ID = rs.getInt("AD_Org_ID");
            AD_Image_ID = rs.getInt("AD_Image_ID");
            AD_Column_ID = rs.getInt("AD_Column_ID");
            AD_Client_ID = rs.getInt("AD_Client_ID");
            IsTranslationTab = rs.getString("IsTranslationTab").equals("Y") ? true : false;
            IsSortTab = rs.getString("IsSortTab").equals("Y") ? true : false;
            IsSingleRow = rs.getString("IsSingleRow").equals("Y") ? true : false;
            IsReadOnly = rs.getString("IsReadOnly").equals("Y") ? true : false;
            IsInsertRecord = rs.getString("IsInsertRecord").equals("Y") ? true : false;
            IsInfoTab = rs.getString("IsInfoTab").equals("Y") ? true : false;
            IsAdvancedTab = rs.getString("IsAdvancedTab").equals("Y") ? true : false;
            IsActive = rs.getString("IsActive").equals("Y") ? true : false;
            HasTree = rs.getString("HasTree").equals("Y") ? true : false;
            TabLevel = rs.getInt("TabLevel");
            SeqNo = rs.getInt("SeqNo");
            AD_Tab_ID = rs.getInt("AD_Tab_ID");
        }
        rs.close();
        ps.close();
    }

    private X_AD_Tab crearTab() throws Exception{
        X_AD_Tab tab = new X_AD_Tab(Env.getCtx(), 0, getTrxName());
        tab.setAD_ColumnSortOrder_ID(AD_ColumnSortOrder_ID);
        tab.setAD_ColumnSortYesNo_ID(AD_ColumnSortYesNo_ID);

        //Esto es necesario ya que un nombre de columna se puede repetir en la BD
        //pero no en la misma tabla. Entonces se utiliza la tabla para identificar
        //la columna.
        if (AD_Column_ID!=0){
            //Obtener la tabla de la columna
            M_Column col = new M_Column(Env.getCtx(), AD_Column_ID, getTrxName());
            //Obtengo nombre de la tabla en BD fuente
            String colTabla = getValueByID("AD_Table", "AD_Table_ID", col.getAD_Table_ID(), "name", true);
            //Obtengo id de la tabla cuyo nombre coincide con el anterior, en BD destino
            int colTable = getIDByName("AD_Table", "name", colTabla, "AD_Table_ID", false);
            if (colTable==ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe la tabla: "+colTabla);
                throw new RuntimeException();
            }
            //Obtengo el id de la columna en la BD destino
            int idColTable = getID("AD_Column", "AD_Column", "columnname = '"+col.getColumnName()+"' and AD_Table_ID = "+colTable, false);
            if (idColTable==ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe la columna: "+col.getColumnName());
                throw new RuntimeException();
            }
            //Uso el id de column obtenido
            tab.setAD_Column_ID(idColTable);
        }

        tab.setAD_Image_ID(AD_Image_ID);
        tab.setAD_Org_ID(AD_Org_ID);

        if (AD_Process_ID!=0){
            String reportProcess = getValueByID("AD_Process", "AD_Process_ID", AD_Process_ID, "name", true);
            int idProcess = getIDByName("AD_Procss", "name", reportProcess, "AD_Process_ID", false);
            if (idProcess!=ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe el Report And Process: "+reportProcess);
                throw new RuntimeException();
            }
            tab.setAD_Process_ID(idProcess);
        }

        if (AD_Table_ID!=0){
            String tableName = getValueByID("AD_Table", "AD_Table_ID", AD_Table_ID, "name", true);
            int idTable = getIDByName("AD_Table", "name", tableName, "AD_Table_ID", false);
            if (idTable==ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe la tabla: "+tableName);
                throw new RuntimeException();
            }
            tab.setAD_Table_ID(idTable);
        }

        if (AD_Window_ID!=0){
            String windowName = getValueByID("AD_Window", "AD_Window_ID", AD_Window_ID, "name", true);
            int idWindow = getIDByName("AD_Window", "name", windowName, "AD_Window_ID", false);
            if (idWindow==ImportProcess.Ausencia_Valor){
                setMensajeSalida("No existe la ventana: "+windowName);
                throw new RuntimeException();
            }
            tab.setAD_Window_ID(idWindow);
        }

        tab.setCommitWarning(CommitWarning);
        tab.setDescription(Description);
        tab.setDisplayLogic(DisplayLogic);
        tab.setEntityType(EntityType);
        tab.setHasTree(HasTree);
        tab.setHelp(Help);
        tab.setIncluded_Tab_ID(Included_Tab_ID);
        tab.setIsActive(IsActive);
        tab.setIsAdvancedTab(IsAdvancedTab);
        tab.setIsInfoTab(IsInfoTab);
        tab.setIsInsertRecord(IsInsertRecord);
        tab.setIsReadOnly(IsReadOnly);
        tab.setIsSingleRow(IsSingleRow);
        tab.setIsSortTab(IsSortTab);
        tab.setIsTranslationTab(IsTranslationTab);
        tab.setName(Name);
        tab.setOrderByClause(OrderByClause);
        tab.setReadOnlyLogic(ReadOnlyLogic);
        tab.setSeqNo(SeqNo);
        tab.setTabLevel(TabLevel);
        tab.setWhereClause(WhereClause);
        tab.save();
        System.out.println("Tab creado: "+tab.getName());
        return tab;
    }

    @Override
    public String procesar() {
        try{
            setAtributosTab(AD_Window_ID);
            X_AD_Tab tab = crearTab();
            setAtributosTraduccionTab();
            crearTraduccionTab(tab.getAD_Tab_ID());
            String sql = "select name from ad_field where ad_tab_id = "+AD_Tab_ID;
            PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ImportField process = new ImportField(getConexionDestino(),getConexionFuente(),rs.getString(1), getTrxName());
                process.setTabPadre(tab.getAD_Tab_ID());
                process.setTrxName(getTrxName());
                process.setAD_Tab_ID(AD_Tab_ID);
                String rta = process.procesar();
                if (!rta.equals("ok"))
                    return rta;
            }
            rs.close();
            ps.close();
        }
        catch(Exception e){
            if (getMensajeSalida()==null || getMensajeSalida().equals(""))
                return e.getMessage();
            else
                return getMensajeSalida();
        }
        return "ok";
    }

    /**
     * @param AD_Window_ID the AD_Window_ID to set
     */
    public void setAD_Window_ID(int AD_Window_ID) {
        this.AD_Window_ID = AD_Window_ID;
    }

    public ImportTab(Connection cd, Connection cf, String name, String trxName){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreObjetoImportar(name);
        setTrxName(trxName);
    }

    private String TTName;
    private String TTDescription;
    private String TTCommitWarning;
    private String TTHelp;
    private String TTAD_Language;
    private int TTAD_Org_ID;
    private int TTAD_Client_ID;
    private boolean TTIsTranslated;
    private boolean TTIsActive;

    private void setAtributosTraduccionTab() throws SQLException{
        String sql = "select * from AD_Tab_Trl where AD_Tab_ID = "+AD_Tab_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            TTName = rs.getString("Name");
            TTDescription = rs.getString("Description");
            TTCommitWarning = rs.getString("CommitWarning");
            TTHelp = rs.getString("Help");
            TTAD_Language = rs.getString("AD_Language");
            TTAD_Org_ID = rs.getInt("AD_Org_ID");
            TTAD_Client_ID = rs.getInt("AD_Client_ID");
            TTIsTranslated = rs.getString("IsTranslated").equals("Y") ? true : false;
            TTIsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        ps.close();
    }

    private void crearTraduccionTab(int AD_Tab_ID) throws SQLException{
        String sql = "update ad_tab_trl set name=?, description=?, CommitWarning=?,Help=?,AD_Language=?,AD_Tab_ID=?,AD_Org_ID=?,AD_Client_ID=?,IsTranslated=?,IsActive=? where AD_Tab_ID = ?";
        PreparedStatement sqlUpdate = DB.prepareStatement(sql, getTrxName());
        sqlUpdate.setString(1, TTName);
        sqlUpdate.setString(2, TTDescription);
        sqlUpdate.setString(3, TTCommitWarning);
        sqlUpdate.setString(4, TTHelp);
        sqlUpdate.setString(5, TTAD_Language);
        sqlUpdate.setInt(6, AD_Tab_ID);
        sqlUpdate.setInt(7, TTAD_Org_ID);
        sqlUpdate.setInt(8, TTAD_Client_ID);
        sqlUpdate.setString(9, TTIsTranslated ? "Y" : "N");
        sqlUpdate.setString(10, TTIsActive ? "Y" : "N");
        sqlUpdate.setInt(11, AD_Tab_ID);
        sqlUpdate.executeUpdate();
        sqlUpdate.close();
    }

}
