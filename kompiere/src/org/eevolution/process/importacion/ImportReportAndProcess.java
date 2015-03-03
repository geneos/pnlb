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
import javax.swing.JOptionPane;
import org.compiere.model.MProcess;
import org.compiere.model.MProcessPara;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *
 * @author santiago
 */
public class ImportReportAndProcess extends ImportProcess{


    private int AD_Process_ID;
    private int AD_Client_ID;
    private int AD_Org_ID;
    private boolean IsActive;
    private String Value;
    private String Name;
    private String Description;
    private String Help;
    private String AccessLevel;
    private String EntityType;
    private String ProcedureName;
    private boolean IsReport;
    private boolean IsDirectPrint;
    private int AD_ReportView_ID;
    private String Classname;
    private int Statistic_Count;
    private int Statistic_Seconds;
    private int AD_PrintFormat_ID;
    private String WorkflowValue;
    private int AD_Workflow_ID;
    private boolean IsBetaFunctionality;
    private boolean IsServerProcess;

    private String ReportAndProcessName;

    private void setAtributosReportAndProcess() throws SQLException{
        String sql = "select * from AD_Process where value = '"+getNombreObjetoImportar()+"'";
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            Statistic_Seconds = rs.getInt("Statistic_Seconds");
            Statistic_Count = rs.getInt("Statistic_Count");
            AD_Process_ID = rs.getInt("AD_Process_ID");
            WorkflowValue = rs.getString("WorkflowValue");
            Value = rs.getString("Value");
            ProcedureName = rs.getString("ProcedureName");
            Name = rs.getString("Name");
            Description = rs.getString("Description");
            Classname = rs.getString("Classname");
            Help = rs.getString("Help");
            EntityType = rs.getString("EntityType");
            AccessLevel = rs.getString("AccessLevel");
            AD_Workflow_ID = rs.getInt("AD_Workflow_ID");
            AD_ReportView_ID = rs.getInt("AD_ReportView_ID");
            AD_PrintFormat_ID = rs.getInt("AD_PrintFormat_ID");
            AD_Org_ID = rs.getInt("AD_Org_ID");
            AD_Client_ID = rs.getInt("AD_Client_ID");
            IsServerProcess = rs.getString("IsServerProcess").equals("Y") ? true : false;
            IsReport = rs.getString("IsReport").equals("Y") ? true : false;
            IsDirectPrint = rs.getString("IsDirectPrint").equals("Y") ? true : false;
            IsBetaFunctionality = rs.getString("IsBetaFunctionality").equals("Y") ? true : false;
            IsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        ps.close();
    }

    private int PPAD_Element_ID;                                                                                                                                                                                                                                    
    private int PPSeqNo;                                                                                                                                                                                                                                            
    private int PPFieldLength;                                                                                                                                                                                                                                      
    private int PPAD_Process_Para_ID;                                                                                                                                                                                                                               
    private String PPValueMin;                                                                                                                                                                                                                                      
    private String PPValueMax;                                                                                                                                                                                                                                      
    private String PPVFormat;                                                                                                                                                                                                                                       
    private String PPName;                                                                                                                                                                                                                                          
    private String PPDescription;                                                                                                                                                                                                                                   
    private String PPDefaultValue2;                                                                                                                                                                                                                                 
    private String PPDefaultValue;                                                                                                                                                                                                                                  
    private String PPColumnName;                                                                                                                                                                                                                                    
    private String PPHelp;                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
    private String PPEntityType;                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
    private int PPAD_Reference_Value_ID;                                                                                                                                                                                                                            
    private int PPAD_Reference_ID;                                                                                                                                                                                                                                  
    private int PPAD_Val_Rule_ID;                                                                                                                                                                                                                                   
    private int PPAD_Process_ID;                                                                                                                                                                                                                                    
    private int PPAD_Org_ID;                                                                                                                                                                                                                                        
    private int PPAD_Client_ID;                                                                                                                                                                                                                                     
    private boolean PPIsRange;                                                                                                                                                                                                                                      
    private boolean PPIsMandatory;                                                                                                                                                                                                                                  
    private boolean PPIsCentrallyMaintained;                                                                                                                                                                                                                        
    private boolean PPIsActive;

    private void setAtributosParametros(int AD_ProcessPara_ID) throws SQLException{
        String sql = "select * from AD_Process_Para where AD_Process_Para_ID = "+AD_ProcessPara_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            PPAD_Element_ID = rs.getInt("AD_Element_ID");
            PPSeqNo = rs.getInt("SeqNo");
            PPFieldLength = rs.getInt("FieldLength");
            PPAD_Process_Para_ID = rs.getInt("AD_Process_Para_ID");
            PPValueMin = rs.getString("ValueMin");
            PPValueMax = rs.getString("ValueMax");
            PPVFormat = rs.getString("VFormat");
            PPName = rs.getString("Name");
            PPDescription = rs.getString("Description");
            PPDefaultValue2 = rs.getString("DefaultValue2");
            PPDefaultValue = rs.getString("DefaultValue");
            PPColumnName = rs.getString("ColumnName");
            PPHelp = rs.getString("Help");
            PPEntityType = rs.getString("EntityType");
            PPAD_Reference_Value_ID = rs.getInt("AD_Reference_Value_ID");
            PPAD_Reference_ID = rs.getInt("AD_Reference_ID");
            PPAD_Val_Rule_ID = rs.getInt("AD_Val_Rule_ID");
            PPAD_Process_ID = rs.getInt("AD_Process_ID");
            PPAD_Org_ID = rs.getInt("AD_Org_ID");
            PPAD_Client_ID = rs.getInt("AD_Client_ID");
            PPIsRange = rs.getString("IsRange").equals("Y") ? true : false;
            PPIsMandatory = rs.getString("IsMandatory").equals("Y") ? true : false;
            PPIsCentrallyMaintained = rs.getString("IsCentrallyMaintained").equals("Y") ? true : false;
            PPIsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        ps.close();
    }

    private String PPTName;
    private String PPTDescription;
    private String PPTHelp;
    private String PPTAD_Language;
    private int PPTAD_Process_Para_ID;
    private int PPTAD_Org_ID;
    private int PPTAD_Client_ID;
    private boolean PPTIsTranslated;
    private boolean PPTIsActive;

    private void setAtributosTraduccionParametros(int AD_Process_Para_ID) throws SQLException{
        String sql = "select * from AD_Process_Para_Trl where AD_Process_Para_ID = "+AD_Process_Para_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            PPTName = rs.getString("Name");
            PPTDescription = rs.getString("Description");
            PPTHelp = rs.getString("Help");                                                                                                                                                                                                                                 
            PPTAD_Language = rs.getString("AD_Language");
            PPTAD_Process_Para_ID = AD_Process_Para_ID;
            PPTAD_Org_ID = rs.getInt("AD_Org_ID");
            PPTAD_Client_ID = rs.getInt("AD_Client_ID");
            PPTIsTranslated = rs.getString("IsTranslated").equals("Y") ? true : false;
            PPTIsActive = rs.getString("IsActive").equals("Y") ? true : false;
        }
        rs.close();
        ps.close();
    }

    private MProcess crearReportAndProcess() throws Exception{
        MProcess process = new MProcess(Env.getCtx(), 0, getTrxName());
        //Si el print format no existe entonces lo importa
        //Nombre del print format asociado al R&P en la BD origen
        String PFName = getValueByID("AD_PrintFormat", "AD_PrintFormat_ID", AD_PrintFormat_ID, "name", true);
        //Chequeo que no exista en la BD destino para importarlo
        int ID = getIDByName("AD_PrintFormat", "name", PFName, "AD_PrintFormat_ID", false);
        //Si no existe en la BD destino el print format entonces lo importo
        if (ID==ImportProcess.Ausencia_Valor){
            //Creo el importador de Print Formats
            ImportPrintFormat importador = new ImportPrintFormat(getConexionDestino(),getConexionFuente(),PFName);
            importador.setTrxName(getTrxName());
            String rta = importador.procesar();
            if (!rta.equals(ImportProcess.Resultado_OK)){
                setMensajeSalida(rta);
                throw new RuntimeException("");
            }
            ID = importador.getAD_PrintFormat_ID();
        }
        process.setAD_PrintFormat_ID(ID);
        process.setAD_Process_ID(AD_Process_ID);
        process.setAD_ReportView_ID(AD_ReportView_ID);
        process.setAD_Workflow_ID(AD_Workflow_ID);
        process.setAccessLevel(AccessLevel);
        process.setClassname(Classname);
        process.setDescription(Description);
        process.setEntityType(EntityType);
        process.setHelp(Help);
        process.setIsActive(IsActive);
        process.setIsBetaFunctionality(IsBetaFunctionality);
        process.setIsDirectPrint(IsDirectPrint);
        process.setIsReport(IsReport);
        process.setIsServerProcess(IsServerProcess);
        process.setName(Name);
        process.setProcedureName(ProcedureName);
        process.setStatistic_Count(Statistic_Count);
        process.setStatistic_Seconds(Statistic_Seconds);
        process.setValue(Value);
        process.setWorkflowValue(WorkflowValue);
        process.save();
        return process;
    }

    private MProcessPara crearParametro(int AD_Process_ID) throws SQLException{
        MProcessPara param = new MProcessPara(Env.getCtx(), 0,AD_Process_ID, null);

        //Chequeo que exista el System Element
        if (PPAD_Element_ID!=0){
            String columnNameElement = getValueByID("AD_Element", "AD_Element_ID", PPAD_Element_ID, "columnname", true);
            int AD_Element_ID = getIDByName("AD_Element", "columnname", columnNameElement, "AD_Element_ID", false);
            if (AD_Element_ID==ImportProcess.Ausencia_Valor){
                setMensajeSalida("Falta crear el System Element: "+columnNameElement);
                throw new SQLException();
            }
            PPAD_Element_ID = AD_Element_ID;
        }

        //Chequeo que exista el Reference
        if (PPAD_Reference_ID!=0){
            String columnNameElement = getValueByID("AD_Reference", "AD_Reference_ID", PPAD_Reference_ID, "name", true);
            int AD_Reference_ID = getIDByName("AD_Reference", "name", columnNameElement, "AD_Reference_ID", false);
            if (AD_Reference_ID==ImportProcess.Ausencia_Valor){
                setMensajeSalida("Falta crear el Reference: "+columnNameElement);
                throw new SQLException();
            }
            PPAD_Reference_ID = AD_Reference_ID;
        }
        
        //Chequeo que exista el Reference Key
        if (PPAD_Reference_Value_ID!=0){
            String columnNameElement = getValueByID("AD_Reference", "AD_Reference_ID", PPAD_Reference_Value_ID, "name", true);
            int AD_Reference_Value_ID = getIDByName("AD_Reference", "name", columnNameElement, "AD_Reference_ID", false);
            if (AD_Reference_Value_ID==ImportProcess.Ausencia_Valor){
                setMensajeSalida("Falta crear el Reference Key: "+columnNameElement);
                throw new SQLException();
            }
            PPAD_Reference_Value_ID = AD_Reference_Value_ID;
        }

        //Chequeo que exista la Validacion Dinámica asociada
        if (PPAD_Val_Rule_ID!=0){
            String columnNameElement = getValueByID("AD_Val_Rule", "AD_Val_Rule_ID", PPAD_Val_Rule_ID, "name", true);
            int AD_Reference_Value_ID = getIDByName("AD_Val_Rule", "name", columnNameElement, "AD_Val_Rule_ID", false);
            if (AD_Reference_Value_ID==ImportProcess.Ausencia_Valor){
                setMensajeSalida("Falta crear la Validacion Dinámica: "+columnNameElement);
                throw new SQLException();
            }
            PPAD_Val_Rule_ID = AD_Reference_Value_ID;
        }

        param.setAD_Element_ID(PPAD_Element_ID);
        param.setAD_Reference_ID(PPAD_Reference_ID);
        param.setAD_Reference_Value_ID(PPAD_Reference_Value_ID);
        param.setAD_Val_Rule_ID(PPAD_Val_Rule_ID);

        param.setAD_Org_ID(PPAD_Org_ID);
        param.setAD_Process_ID(AD_Process_ID);
        param.setColumnName(PPColumnName);
        param.setDefaultValue(PPDefaultValue);
        param.setDefaultValue2(PPDefaultValue2);
        param.setDescription(PPDescription);
        param.setEntityType(PPEntityType);
        param.setFieldLength(PPFieldLength);
        param.setHelp(PPHelp);
        param.setIsActive(PPIsActive);
        param.setIsCentrallyMaintained(PPIsCentrallyMaintained);
        param.setIsMandatory(PPIsMandatory);
        param.setIsRange(PPIsRange);
        param.setName(PPName);
        param.setSeqNo(PPSeqNo);
        param.setVFormat(PPVFormat);
        param.setValueMax(PPValueMax);
        param.setValueMin(PPValueMin);
        param.save();
        return param;
    }

    private void crearTraduccionParametro(int AD_Process_Para_ID) throws SQLException{
        String sql = "update AD_Process_Para_Trl set Name = ?,Description=?, Help=?, AD_Language=?, AD_Process_Para_ID=?, AD_Org_ID=?, AD_Client_ID=?, IsTranslated=?, IsActive=? where AD_Process_Para_ID = ?";
        PreparedStatement sqlIns = DB.prepareStatement(sql, getTrxName());
        sqlIns.setString(1, PPTName);
        sqlIns.setString(2, PPTDescription);
        sqlIns.setString(3, PPTHelp);
        sqlIns.setString(4, PPTAD_Language);
        sqlIns.setInt(5, AD_Process_Para_ID);
        sqlIns.setInt(6, PPTAD_Org_ID);
        sqlIns.setInt(7, PPTAD_Client_ID);
        sqlIns.setString(8, PPTIsTranslated ? "Y" : "N");
        sqlIns.setString(9, PPTIsActive ? "Y" : "N");
        sqlIns.setInt(10, AD_Process_Para_ID);
        sqlIns.executeUpdate();
        sqlIns.close();
    }

    /**
     * @param ReportAndProcessName the ReportAndProcessName to set
     */
    public void setReportAndProcessName(String ReportAndProcessName) {
        this.ReportAndProcessName = ReportAndProcessName;
    }

    public String procesar(){
        try {
            JOptionPane.showMessageDialog(null, "Se va a importar el Report & Process: "+getNombreObjetoImportar()+"\n\nTambi�n se incluiran los parametros junto con sus respectivas traducciones.", "Importacion Report & Process", JOptionPane.INFORMATION_MESSAGE);
            setAtributosReportAndProcess();
            MProcess proceso = crearReportAndProcess();
            String sql = "select ad_process_para_id from ad_process_para where ad_process_id = " + AD_Process_ID + " AND AD_Client_ID = "
			+ Env.getAD_Client_ID(Env.getCtx());
            PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                setAtributosParametros(rs.getInt(1));
                MProcessPara param = crearParametro(proceso.getAD_Process_ID());
                setAtributosTraduccionParametros(rs.getInt(1));
                crearTraduccionParametro(param.getAD_Process_Para_ID());
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            Logger.getLogger(ImportReportAndProcess.class.getName()).log(Level.SEVERE, null, ex);
            Trx.get(getTrxName(), false).rollback();
            if (getMensajeSalida()!=null || getMensajeSalida().equals(""))
                return getMensajeSalida();
            else
                return ex.getMessage();
        }
        return "ok";
    }
    
    public ImportReportAndProcess(Connection cd, Connection cf, String name){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreObjetoImportar(name);
    }

}
