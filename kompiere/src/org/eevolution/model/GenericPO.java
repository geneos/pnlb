// ----------------------------------------------------------------------
// Generic PO.
// Used to insert/update data from a compieredata.xml file.
package org.eevolution.model;

// import for GenericPO
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.model.*;
import org.compiere.util.*;

public class GenericPO extends PO {

    //private Logger  log = Logger.getCLogger(getClass());

    /** Standard Constructor */
    public GenericPO (Properties ctx, int ID , String trxName)
    {
	super (ctx, ID, trxName);
    }

    /** Load Constructor */
    public GenericPO (Properties ctx, ResultSet rs,String trxName)
    {
	super (ctx, rs,trxName);
    }
    
    /** Document Value Workflow Manager		*/
    private static DocWorkflowMgr		s_docWFMgr = null;
     
    	/**
	 * 	Set Document Value Workflow Manager
	 *	@param docWFMgr mgr
	 */
    public static void setDocWorkflowMgr (DocWorkflowMgr docWFMgr)
    {
		s_docWFMgr = docWFMgr;
		s_log.config (s_docWFMgr.toString());
    }	//	setDocWorkflowMgr
        
    private int Table_ID = 0;
    protected BigDecimal accessLevel = new BigDecimal(3);
    /**	Logger							*/
    protected transient CLogger	log = CLogger.getCLogger (getClass());
	/** Static Logger					*/
    private static CLogger		s_log = CLogger.getCLogger (PO.class);
    /** AccessLevel 3 - Client - Org  */
    protected int get_AccessLevel()
    {
    return accessLevel.intValue();
    }
    
    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
	Table_ID = Integer.valueOf(ctx.getProperty("compieredataTable_ID")).intValue();
	//log.info("Table_ID: "+Table_ID);
	POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
	return poi;
    }

    public String toString()
    {
	StringBuffer sb = new StringBuffer ("GenericPO[Table=").append(""+Table_ID+",ID=").append(get_ID()).append("]");
	return sb.toString();
    }

    public static final int AD_ORGTRX_ID_AD_Reference_ID=130;

    /** Set Trx Organization.
	Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
	if (AD_OrgTrx_ID == 0) set_Value ("AD_OrgTrx_ID", null);
	else 
	    set_Value ("AD_OrgTrx_ID", new Integer(AD_OrgTrx_ID));
    }
    /** Get Trx Organization.
	Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
	Integer ii = (Integer)get_Value("AD_OrgTrx_ID");
	if (ii == null) return 0;
	return ii.intValue();
    }
    // setValue
    public void setValue(String columnName, Object value) {
	set_Value(columnName, value);
    }
    // setValueNoCheck
    public void setValueNoCheck(String columnName, Object value) {
	set_ValueNoCheck(columnName, value);
	}
    // setValue
    public void setValue(int index, Object value) {
	set_Value(index, value);
    }
    
    public void setDocWorkflowMgr()
    {
        
    }
}   // GenericPO

