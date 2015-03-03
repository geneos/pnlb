/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.compiere.db.CConnection;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *
 * @author santiago
 */
public class ImportPrintFormat extends ImportProcess{

    //Atributos del Print Format
    private int PFAD_Org_ID;
    private String PFname;
    private String PFDescription;
    private boolean PFisActive;
    private boolean PFisDefault;
    private int PFAD_Table_ID;
    private boolean PFisForm;
    private int PFAD_PrintPaper_ID;
    private boolean PFisStandardHeaderFooter;
    private int PFAD_PrintTableFormat_ID;
    private int PFAD_PrintFont_ID;
    private int PFAD_PrintColor_ID;
    private int PFAD_PrintFormat_ID;

    private int AD_PrintFormat_ID;


    //Atributos del Print Format Item

    //Atributos de la Traduccion del Print Format Item

    public ImportPrintFormat(Connection cd, Connection cf, String name){
        setConexionFuente(cf);
        setConexionDestino(cd);
        setNombreObjetoImportar(name);
    }

    public ImportPrintFormat(){
    
    }

    public ImportPrintFormat(String name){
        setNombreObjetoImportar(name);
    }

    public String procesar(){
        try{
            JOptionPane.showMessageDialog(null, "Se va a importar el Print Format: "+getNombreObjetoImportar()+"\n\nTambién se incluiran los format items junto con sus respectivas traducciones.", "Importacion Report & Process", JOptionPane.INFORMATION_MESSAGE);
            setAtributosPrintFormat();
            crearPrintFormat();
            //openDialog();
        }
        catch(Exception e){
            return getMensajeSalida();
        }
        return "ok";
    }

    private void setConexiones(){
        setConexionDestino(DB.getConnectionRW());
        //Obtengo la conexion desde donde voy a extraer las OC.
        CConnection c=CConnection.get("Oracle", "192.168.1.5", 1521, "produccionpa", "compiere", "compiere");
        setConexionFuente(c.getConnection(true, Connection.TRANSACTION_READ_COMMITTED));
    }

    private void setAtributosPrintFormat() throws Exception{
        String sql = "select * from AD_PrintFormat where name = '"+getNombreObjetoImportar()+"'";
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            PFAD_Org_ID = rs.getInt("AD_Org_ID");
            PFDescription = rs.getString("Description");
            PFisActive = rs.getString("isActive").equals("Y") ? true : false;
            PFisDefault = rs.getString("isDefault").equals("Y") ? true : false;
            PFAD_Table_ID= getPFAD_Table_ID(rs.getInt("AD_Table_ID"));
            PFisForm = rs.getString("isForm").equals("Y") ? true : false;
            PFAD_PrintPaper_ID= rs.getInt("AD_PrintPaper_ID");
            PFisStandardHeaderFooter = rs.getString("isStandardHeaderFooter").equals("Y") ? true : false;
            PFAD_PrintTableFormat_ID= rs.getInt("AD_PrintTableFormat_ID");
            PFAD_PrintFont_ID= rs.getInt("AD_PrintFont_ID");
            PFAD_PrintColor_ID= rs.getInt("AD_PrintColor_ID");
            PFname = rs.getString("name");
            //El ID lo uso solo para luego obtener los items
            PFAD_PrintFormat_ID = rs.getInt("AD_PrintFormat_ID");
        }
        rs.close();
        ps.close();
    }

    /** BISion - 27/08/2009 - Santiago Ibañez
     * Dado un ID de tabla en la BD fuente, obtengo el correspondiente ID en la BD destino
     * Los Id de las tablas pueden variar en una u otra base.
     * @return
     */
    private int getPFAD_Table_ID(int AD_Table_ID) throws SQLException{
        //Obtengo el nombre de la tabla de la BD fuente
        String sql = "select name from AD_Table where  AD_Table_ID = "+ AD_Table_ID + " AND AD_Client_ID = "
		+ Env.getAD_Client_ID(Env.getCtx());
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        String tableName = null;
        if (rs.next())
            tableName = rs.getString(1);
        rs.close();
        ps.close();
        //Obtengo el ID de la tabla en la BD destino
        sql = "select AD_Table_ID from AD_Table where upper(name) = '"+tableName.toUpperCase()+"'";
        ps = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        int ret = 0;
        rs = ps.executeQuery();
        if (rs.next())
            ret = rs.getInt(1);
        rs.close();
        ps.close();
        if (ret==0){
            setMensajeSalida("No existe la tabla: "+tableName+". Debe crearla primero.");
            throw new SQLException();
        }
        return ret;
    }

    private void crearPrintFormat() throws SQLException{
        MPrintFormat pf = new MPrintFormat(Env.getCtx(), 0, getTrxName());
        pf.setAD_Org_ID(PFAD_Org_ID);
        pf.setName(PFname);
        pf.setDescription(PFDescription);
        pf.setIsActive(PFisActive);
        pf.setIsDefault(PFisDefault);
        pf.setAD_Table_ID(PFAD_Table_ID);
        pf.setIsForm(PFisForm);
        pf.setAD_PrintPaper_ID(PFAD_PrintPaper_ID);
        pf.setIsStandardHeaderFooter(PFisStandardHeaderFooter);
        pf.setAD_PrintTableFormat_ID(PFAD_PrintTableFormat_ID);
        pf.setAD_PrintFont_ID(PFAD_PrintFont_ID);
        pf.setAD_PrintColor_ID(PFAD_PrintColor_ID);
        pf.save();
        setAD_PrintFormat_ID(pf.getAD_PrintFormat_ID());
        //Obtengo todos los items del print format origen
        String sql = "select ad_printformatitem_id from ad_printformatitem where ad_printformat_id = "+ PFAD_PrintFormat_ID;
        try{
            PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                crearItem(rs.getInt(1),pf.getAD_PrintFormat_ID());
            }
            rs.close();
            ps.close();
            Trx.get(getTrxName(), false).commit();
            PreparedStatement psUpdate = getConexionDestino().prepareStatement("update ad_printformat set updatedby = 0,ad_client_id = 0 where ad_printformat_id = "+pf.getAD_PrintFormat_ID(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            psUpdate.executeUpdate();
            psUpdate.close();
            psUpdate = getConexionDestino().prepareStatement("update ad_printformatitem set updatedby = 0,ad_client_id = 0 where ad_printformat_id = "+pf.getAD_PrintFormat_ID(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            psUpdate.executeUpdate();
            psUpdate.close();
            Trx.get(getTrxName(), false).commit();
        }
        catch (SQLException e){
            throw e;
        }
        
    }

    //Atributos del Print Format Item
    private int PFIYSpace;
    private int PFIYPosition;
    private int PFIXSpace;
    private int PFIXPosition;
    private int PFISortNo;
    private int PFISeqNo;
    private int PFIRunningTotalLines;
    private int PFIMaxWidth;
    private int PFIMaxHeight;
    private int PFILineWidth;
    private int PFIBelowColumn;
    private int PFIArcDiameter;
    private int PFIAD_PrintFormatItem_ID;
    private String PFIPrintNameSuffix;
    private String PFIPrintName;
    private String PFIName;
    private String PFIImageURL;
    private Timestamp PFIUpdated;
    private Timestamp PFICreated;
    private String PFIShapeType;
    private String PFIPrintFormatType;
    private String PFIPrintAreaType;
    private String PFILineAlignmentType;
    private String PFIFieldAlignmentType;
    private int PFIUpdatedBy;
    private int PFICreatedBy;
    private int PFIAD_PrintFormatChild_ID;
    private int PFIAD_PrintGraph_ID;
    private int PFIAD_PrintFormat_ID;
    private int PFIAD_PrintFont_ID;
    private int PFIAD_PrintColor_ID;
    private int PFIAD_Org_ID;
    private int PFIAD_Column_ID;
    private int PFIAD_Client_ID;
    private boolean PFIIsVarianceCalc;
    private boolean PFIIsSuppressNull;
    private boolean PFIIsSummarized;
    private boolean PFIIsSetNLPosition;
    private boolean PFIIsRunningTotal;
    private boolean PFIIsRelativePosition;
    private boolean PFIIsPrinted;
    private boolean PFIIsPageBreak;
    private boolean PFIIsOrderBy;
    private boolean PFIIsNextPage;
    private boolean PFIIsNextLine;
    private boolean PFIIsMinCalc;
    private boolean PFIIsMaxCalc;
    private boolean PFIIsImageField;
    private boolean PFIIsHeightOneLine;
    private boolean PFIIsGroupBy;
    private boolean PFIIsFixedWidth;
    private boolean PFIIsFilledRectangle;
    private boolean PFIIsDeviationCalc;
    private boolean PFIIsCounted;
    private boolean PFIIsCentrallyMaintained;
    private boolean PFIIsAveraged;
    private boolean PFIIsActive;
    private boolean PFIImageIsAttached;

    private void crearItem(int AD_PrintFormatItem_ID, int IDPFnuevo) throws SQLException{
        MPrintFormatItem pfi = new MPrintFormatItem(Env.getCtx(), 0, getTrxName());
        String sql = "select * from ad_printformatitem where ad_printformatitem_id = "+AD_PrintFormatItem_ID;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            PFIYSpace = rs.getInt("YSpace");
            PFIYPosition = rs.getInt("YPosition");
            PFIXSpace = rs.getInt("XSpace");
            PFIXPosition = rs.getInt("XPosition");
            PFISortNo = rs.getInt("SortNo");
            PFISeqNo = rs.getInt("SeqNo");
            PFIRunningTotalLines = rs.getInt("RunningTotalLines");
            PFIMaxWidth = rs.getInt("MaxWidth");
            PFIMaxHeight = rs.getInt("MaxHeight");
            PFILineWidth = rs.getInt("LineWidth");
            PFIBelowColumn = rs.getInt("BelowColumn");
            PFIArcDiameter = rs.getInt("ArcDiameter");
            if (rs.getInt("AD_PrintFormatItem_ID")!=0)
                PFIAD_PrintFormatItem_ID =rs.getInt("AD_PrintFormatItem_ID");
            else
                PFIAD_PrintFormatItem_ID =0;
            PFIPrintNameSuffix = rs.getString("PrintNameSuffix");
            PFIPrintName = rs.getString("PrintName");
            PFIName = rs.getString("Name");
            PFIImageURL = rs.getString("ImageURL");
            PFIUpdated = rs.getTimestamp("Updated");
            PFICreated = rs.getTimestamp("Created");
            PFIShapeType = rs.getString("ShapeType");
            PFIPrintFormatType = rs.getString("PrintFormatType");
            PFIPrintAreaType = rs.getString("PrintAreaType");
            PFILineAlignmentType = rs.getString("LineAlignmentType");
            PFIFieldAlignmentType = rs.getString("FieldAlignmentType");
            PFIUpdatedBy =rs.getInt("UpdatedBy");
            PFICreatedBy =rs.getInt("CreatedBy");
            if (rs.getInt("AD_PrintFormatChild_ID")!=0)
                PFIAD_PrintFormatChild_ID =getID(rs.getInt("AD_PrintFormatChild_ID"), "AD_PRINTFORMAT", "name");
            else
                PFIAD_PrintFormatChild_ID=0;
            PFIAD_PrintGraph_ID =rs.getInt("AD_PrintGraph_ID");
            PFIAD_PrintFont_ID =rs.getInt("AD_PrintFont_ID");
            PFIAD_PrintColor_ID =rs.getInt("AD_PrintColor_ID");
            PFIAD_Org_ID =rs.getInt("AD_Org_ID");
            if (rs.getInt("AD_Column_ID")!=0)
                PFIAD_Column_ID = getID(rs.getInt("AD_Column_ID"), "AD_COLUMN", "columnname");
            else
                PFIAD_Column_ID=0;
            PFIAD_Client_ID =0;
            PFIIsVarianceCalc = rs.getString("IsVarianceCalc").equals("Y") ? true : false;
            PFIIsSuppressNull = rs.getString("IsSuppressNull").equals("Y") ? true : false;
            PFIIsSummarized = rs.getString("IsSummarized").equals("Y") ? true : false;
            PFIIsSetNLPosition = rs.getString("IsSetNLPosition").equals("Y") ? true : false;
            PFIIsRunningTotal = rs.getString("IsRunningTotal").equals("Y") ? true : false;
            PFIIsRelativePosition = rs.getString("IsRelativePosition").equals("Y") ? true : false;
            PFIIsPrinted = rs.getString("IsPrinted").equals("Y") ? true : false;
            PFIIsPageBreak = rs.getString("IsPageBreak").equals("Y") ? true : false;
            PFIIsOrderBy = rs.getString("IsOrderBy").equals("Y") ? true : false;
            PFIIsNextPage = rs.getString("IsNextPage").equals("Y") ? true : false;
            PFIIsNextLine = rs.getString("IsNextLine").equals("Y") ? true : false;
            PFIIsMinCalc = rs.getString("IsMinCalc").equals("Y") ? true : false;
            PFIIsMaxCalc = rs.getString("IsMaxCalc").equals("Y") ? true : false;
            PFIIsImageField = rs.getString("IsImageField").equals("Y") ? true : false;
            PFIIsHeightOneLine = rs.getString("IsHeightOneLine").equals("Y") ? true : false;
            PFIIsGroupBy = rs.getString("IsGroupBy").equals("Y") ? true : false;
            PFIIsFixedWidth = rs.getString("IsFixedWidth").equals("Y") ? true : false;
            PFIIsFilledRectangle = rs.getString("IsFilledRectangle").equals("Y") ? true : false;
            PFIIsDeviationCalc = rs.getString("IsDeviationCalc").equals("Y") ? true : false;
            PFIIsCounted = rs.getString("IsCounted").equals("Y") ? true : false;
            PFIIsCentrallyMaintained = rs.getString("IsCentrallyMaintained").equals("Y") ? true : false;
            PFIIsAveraged = rs.getString("IsAveraged").equals("Y") ? true : false;
            PFIIsActive = rs.getString("IsActive").equals("Y") ? true : false;
            PFIImageIsAttached = rs.getString("ImageIsAttached").equals("Y") ? true : false;
            if (PFIAD_Column_ID!=0)
                pfi.setAD_Column_ID(PFIAD_Column_ID);
            pfi.setAD_Org_ID(PFIAD_Org_ID);
            pfi.setAD_PrintColor_ID(PFIAD_PrintColor_ID);
            pfi.setAD_PrintFont_ID(PFIAD_PrintFont_ID);
            if (PFIAD_PrintFormatChild_ID!=0)
                pfi.setAD_PrintFormatChild_ID(PFIAD_PrintFormatChild_ID);
            pfi.setAD_PrintFormat_ID(IDPFnuevo);
            if (PFIAD_PrintGraph_ID!=0)
                pfi.setAD_PrintGraph_ID(PFIAD_PrintGraph_ID);
            pfi.setArcDiameter(PFIArcDiameter);
            pfi.setBelowColumn(PFIBelowColumn);
            pfi.setFieldAlignmentType(PFIFieldAlignmentType);
            pfi.setImageIsAttached(PFIImageIsAttached);
            pfi.setImageURL(PFIImageURL);
            pfi.setIsActive(PFIIsActive);
            pfi.setIsAveraged(PFIIsAveraged);
            pfi.setIsCentrallyMaintained(PFIIsCentrallyMaintained);
            pfi.setIsCounted(PFIIsCounted);
            pfi.setIsDeviationCalc(PFIIsDeviationCalc);
            pfi.setIsFilledRectangle(PFIIsFilledRectangle);
            pfi.setIsFixedWidth(PFIIsFixedWidth);
            pfi.setIsGroupBy(PFIIsGroupBy);
            pfi.setIsHeightOneLine(PFIIsHeightOneLine);
            pfi.setIsImageField(PFIIsImageField);
            pfi.setIsMaxCalc(PFIIsMaxCalc);
            pfi.setIsMinCalc(PFIIsMinCalc);
            pfi.setIsNextLine(PFIIsNextLine);
            pfi.setIsNextPage(PFIIsNextPage);
            pfi.setIsOrderBy(PFIIsOrderBy);
            pfi.setIsPageBreak(PFIIsPageBreak);
            pfi.setIsPrinted(PFIIsPrinted);
            pfi.setIsRelativePosition(PFIIsRelativePosition);
            pfi.setIsRunningTotal(PFIIsRunningTotal);
            pfi.setIsSetNLPosition(PFIIsSetNLPosition);
            pfi.setIsSummarized(PFIIsSummarized);
            pfi.setIsSuppressNull(PFIIsSuppressNull);
            pfi.setIsVarianceCalc(PFIIsVarianceCalc);
            pfi.setLineAlignmentType(PFILineAlignmentType);
            pfi.setLineWidth(PFILineWidth);
            pfi.setMaxHeight(PFIMaxHeight);
            pfi.setMaxWidth(PFIMaxWidth);
            pfi.setName(PFIName);
            pfi.setPrintAreaType(PFIPrintAreaType);
            pfi.setPrintFormatType(PFIPrintFormatType);
            pfi.setPrintName(PFIPrintName);
            pfi.setPrintNameSuffix(PFIPrintNameSuffix);
            pfi.setRunningTotalLines(PFIRunningTotalLines);
            pfi.setSeqNo(PFISeqNo);
            pfi.setShapeType(PFIShapeType);
            pfi.setSortNo(PFISortNo);
            pfi.setXPosition(PFIXPosition);
            pfi.setXSpace(PFIXSpace);
            pfi.setYPosition(PFIYPosition);
            pfi.setYSpace(PFIYSpace);
            if (!pfi.save()){
                setMensajeSalida("No se pudo crear el item "+PFIName);
                throw new SQLException();
            }
            PreparedStatement psUpdate = getConexionFuente().prepareStatement("update ad_printformatitem set updatedby = 0 where ad_printformatitem_id = "+pfi.getAD_PrintFormatItem_ID(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            psUpdate.executeUpdate();
            psUpdate.close();
            Trx.get(getTrxName(), false).commit();
            crearTraduccionItem(pfi.getAD_PrintFormatItem_ID(), PFIAD_PrintFormatItem_ID);
        }
        rs.close();
        ps.close();
    }

    /**
     * El Print format o los items tienen claves extranjeras que pueden diferir
     * entre una BD u otra. Para eso yo tengo el ID del registro en la BD fuente
     * y obtengo el nombre. Este nombre lo utilizo para obtener el ID en la BD destino
     * @param IDFuente id en la BD fuente
     * @param tableName tabla en cuestion
     * @return
     */
    private int getID(int IDFuente, String tableName, String colName) throws SQLException{
        String pk = tableName+"_ID";
        //Obtengo el nombre correspondiente de BD fuente
        String sql = "select upper("+colName+") from "+tableName+" where "+pk+" = "+IDFuente;
        PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();
        String name ="";
        if (rs.next()){
            name = rs.getString(1);
        }
        rs.close();
        ps.close();
        //Obtengo el ID de BD destino utilizando el nombre anterior
        if (!tableName.equals("AD_COLUMN"))
            sql = "select "+pk+" from "+tableName+" where upper("+colName+") = '"+name+"'";
        else
            sql = "select ad_column_id from ad_column where upper("+colName+") = '"+name+"' and AD_TABLE_ID = "+PFAD_Table_ID;
        ps = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        rs = ps.executeQuery();
        int id=0;
        if (rs.next()){
            id = rs.getInt(1);
        }
        rs.close();
        ps.close();
        return id;
    }
    
    /** BISion - 02/10/2009 - Santiago Ibañez
     * Metodo que obtiene la traduccion del Item fuente y crea una traduccion igual
     * para el item destino
     * @param IDItemNuevo
     * @param IDItemFuente
     */
    private void crearTraduccionItem(int IDItemNuevo, int IDItemFuente)throws SQLException{
        String PRINTNAME=null;
        try{
            //Obtengo la traduccion del Item
            String sql = "select * from ad_printformatitem_trl where ad_printformatitem_id = "+IDItemFuente;
            PreparedStatement ps = getConexionFuente().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                //Seteo los parametros
                String AD_LANGUAGE = rs.getString("AD_LANGUAGE");
                int AD_CLIENT_ID = rs.getInt("AD_CLIENT_ID");
                int AD_ORG_ID = rs.getInt("AD_ORG_ID");
                String ISACTIVE = rs.getString("ISACTIVE");
                Timestamp CREATED = new Timestamp(System.currentTimeMillis());
                Timestamp UPDATED = new Timestamp(System.currentTimeMillis());
                PRINTNAME = rs.getString("PRINTNAME");
                String ISTRANSLATED = rs.getString("ISTRANSLATED");
                String PRINTNAMESUFFIX = rs.getString("PRINTNAMESUFFIX");
                rs.close();
                ps.close();
                //Inserto los parametros seteados
                //sql = "insert into ad_printformatitem_trl (AD_PRINTFORMATITEM_ID, AD_LANGUAGE, AD_CLIENT_ID, AD_ORG_ID, ISACTIVE, CREATED, CREATEDBY, UPDATED, UPDATEDBY, PRINTNAME, ISTRANSLATED, PRINTNAMESUFFIX) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                sql = "update ad_printformatitem_trl set AD_PRINTFORMATITEM_ID=?, AD_LANGUAGE=?, AD_CLIENT_ID=?, AD_ORG_ID=?, ISACTIVE=?, CREATED=?, CREATEDBY=?, UPDATED=?, UPDATEDBY=?, PRINTNAME=?, ISTRANSLATED=?, PRINTNAMESUFFIX=? where AD_PRINTFORMATITEM_ID="+IDItemNuevo;
                PreparedStatement psInsert = getConexionDestino().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                psInsert.setInt(1, IDItemNuevo);
                psInsert.setString(2, AD_LANGUAGE);
                psInsert.setInt(3, AD_CLIENT_ID);
                psInsert.setInt(4, AD_ORG_ID);
                psInsert.setString(5, ISACTIVE);
                psInsert.setTimestamp(6, CREATED);
                psInsert.setInt(7,0);
                psInsert.setTimestamp(8, UPDATED);
                psInsert.setInt(9,0);
                psInsert.setString(10, PRINTNAME);
                psInsert.setString(11, ISTRANSLATED);
                psInsert.setString(12, PRINTNAMESUFFIX);
                psInsert.executeUpdate();
                psInsert.close();
            }
            rs.close();
            ps.close();
        }
        catch(SQLException ex){
            setMensajeSalida("No se pudo crear la traduccion del item "+PRINTNAME);
            throw ex;
        }
    }

    private JPanel dialogPanel;
    public void openDialog(){
        dialogPanel = new JPanel();
        dialogPanel.setName("Importacion en progreso...");
        dialogPanel.setVisible(true);
    }

    /**
     * @return the AD_PrintFormat_ID
     */
    public int getAD_PrintFormat_ID() {
        return AD_PrintFormat_ID;
    }

    /**
     * @param AD_PrintFormat_ID the AD_PrintFormat_ID to set
     */
    public void setAD_PrintFormat_ID(int AD_PrintFormat_ID) {
        this.AD_PrintFormat_ID = AD_PrintFormat_ID;
    }
}
