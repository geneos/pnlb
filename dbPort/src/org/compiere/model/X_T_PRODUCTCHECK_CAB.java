/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for T_PRODUCTCHECK_CAB
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2012-10-10 14:32:09.096 */
public class X_T_PRODUCTCHECK_CAB extends PO
{
/** Standard Constructor */
public X_T_PRODUCTCHECK_CAB (Properties ctx, int T_PRODUCTCHECK_CAB_ID, String trxName)
{
super (ctx, T_PRODUCTCHECK_CAB_ID, trxName);
/** if (T_PRODUCTCHECK_CAB_ID == 0)
{
setT_PRODUCTCHECK_CAB_ID (0);
}
 */
}
/** Load Constructor */
public X_T_PRODUCTCHECK_CAB (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000041 */
public static final int Table_ID=5000041;

/** TableName=T_PRODUCTCHECK_CAB */
public static final String Table_Name="T_PRODUCTCHECK_CAB";

protected static KeyNamePair Model = new KeyNamePair(5000041,"T_PRODUCTCHECK_CAB");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel 3 - Client - Org  */
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_T_PRODUCTCHECK_CAB[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID <= 0) set_Value ("AD_PInstance_ID", null);
 else 
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set CANTIDAD1 */
public void setCANTIDAD1 (BigDecimal CANTIDAD1)
{
set_Value ("CANTIDAD1", CANTIDAD1);
}
/** Get CANTIDAD1 */
public BigDecimal getCANTIDAD1() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD1");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD10 */
public void setCANTIDAD10 (BigDecimal CANTIDAD10)
{
set_Value ("CANTIDAD10", CANTIDAD10);
}
/** Get CANTIDAD10 */
public BigDecimal getCANTIDAD10() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD2 */
public void setCANTIDAD2 (BigDecimal CANTIDAD2)
{
set_Value ("CANTIDAD2", CANTIDAD2);
}
/** Get CANTIDAD2 */
public BigDecimal getCANTIDAD2() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD3 */
public void setCANTIDAD3 (BigDecimal CANTIDAD3)
{
set_Value ("CANTIDAD3", CANTIDAD3);
}
/** Get CANTIDAD3 */
public BigDecimal getCANTIDAD3() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD3");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD4 */
public void setCANTIDAD4 (BigDecimal CANTIDAD4)
{
set_Value ("CANTIDAD4", CANTIDAD4);
}
/** Get CANTIDAD4 */
public BigDecimal getCANTIDAD4() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD4");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD5 */
public void setCANTIDAD5 (BigDecimal CANTIDAD5)
{
set_Value ("CANTIDAD5", CANTIDAD5);
}
/** Get CANTIDAD5 */
public BigDecimal getCANTIDAD5() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD5");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD6 */
public void setCANTIDAD6 (BigDecimal CANTIDAD6)
{
set_Value ("CANTIDAD6", CANTIDAD6);
}
/** Get CANTIDAD6 */
public BigDecimal getCANTIDAD6() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD6");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD7 */
public void setCANTIDAD7 (BigDecimal CANTIDAD7)
{
set_Value ("CANTIDAD7", CANTIDAD7);
}
/** Get CANTIDAD7 */
public BigDecimal getCANTIDAD7() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD7");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD8 */
public void setCANTIDAD8 (BigDecimal CANTIDAD8)
{
set_Value ("CANTIDAD8", CANTIDAD8);
}
/** Get CANTIDAD8 */
public BigDecimal getCANTIDAD8() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD8");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CANTIDAD9 */
public void setCANTIDAD9 (BigDecimal CANTIDAD9)
{
set_Value ("CANTIDAD9", CANTIDAD9);
}
/** Get CANTIDAD9 */
public BigDecimal getCANTIDAD9() 
{
BigDecimal bd = (BigDecimal)get_Value("CANTIDAD9");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Warehouse.
Storage Warehouse and Service Point */
public void setM_Warehouse_ID (int M_Warehouse_ID)
{
if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
 else 
set_Value ("M_Warehouse_ID", new Integer(M_Warehouse_ID));
}
/** Get Warehouse.
Storage Warehouse and Service Point */
public int getM_Warehouse_ID() 
{
Integer ii = (Integer)get_Value("M_Warehouse_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PRODUCTO1 */
public void setPRODUCTO1 (String PRODUCTO1)
{
if (PRODUCTO1 != null && PRODUCTO1.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO1 = PRODUCTO1.substring(0,99);
}
set_Value ("PRODUCTO1", PRODUCTO1);
}
/** Get PRODUCTO1 */
public String getPRODUCTO1() 
{
return (String)get_Value("PRODUCTO1");
}
/** Set PRODUCTO10 */
public void setPRODUCTO10 (String PRODUCTO10)
{
if (PRODUCTO10 != null && PRODUCTO10.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO10 = PRODUCTO10.substring(0,99);
}
set_Value ("PRODUCTO10", PRODUCTO10);
}
/** Get PRODUCTO10 */
public String getPRODUCTO10() 
{
return (String)get_Value("PRODUCTO10");
}
/** Set PRODUCTO2 */
public void setPRODUCTO2 (String PRODUCTO2)
{
if (PRODUCTO2 != null && PRODUCTO2.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO2 = PRODUCTO2.substring(0,99);
}
set_Value ("PRODUCTO2", PRODUCTO2);
}
/** Get PRODUCTO2 */
public String getPRODUCTO2() 
{
return (String)get_Value("PRODUCTO2");
}
/** Set PRODUCTO3 */
public void setPRODUCTO3 (String PRODUCTO3)
{
if (PRODUCTO3 != null && PRODUCTO3.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO3 = PRODUCTO3.substring(0,99);
}
set_Value ("PRODUCTO3", PRODUCTO3);
}
/** Get PRODUCTO3 */
public String getPRODUCTO3() 
{
return (String)get_Value("PRODUCTO3");
}
/** Set PRODUCTO4 */
public void setPRODUCTO4 (String PRODUCTO4)
{
if (PRODUCTO4 != null && PRODUCTO4.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO4 = PRODUCTO4.substring(0,99);
}
set_Value ("PRODUCTO4", PRODUCTO4);
}
/** Get PRODUCTO4 */
public String getPRODUCTO4() 
{
return (String)get_Value("PRODUCTO4");
}
/** Set PRODUCTO5 */
public void setPRODUCTO5 (String PRODUCTO5)
{
if (PRODUCTO5 != null && PRODUCTO5.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO5 = PRODUCTO5.substring(0,99);
}
set_Value ("PRODUCTO5", PRODUCTO5);
}
/** Get PRODUCTO5 */
public String getPRODUCTO5() 
{
return (String)get_Value("PRODUCTO5");
}
/** Set PRODUCTO6 */
public void setPRODUCTO6 (String PRODUCTO6)
{
if (PRODUCTO6 != null && PRODUCTO6.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO6 = PRODUCTO6.substring(0,99);
}
set_Value ("PRODUCTO6", PRODUCTO6);
}
/** Get PRODUCTO6 */
public String getPRODUCTO6() 
{
return (String)get_Value("PRODUCTO6");
}
/** Set PRODUCTO7 */
public void setPRODUCTO7 (String PRODUCTO7)
{
if (PRODUCTO7 != null && PRODUCTO7.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO7 = PRODUCTO7.substring(0,99);
}
set_Value ("PRODUCTO7", PRODUCTO7);
}
/** Get PRODUCTO7 */
public String getPRODUCTO7() 
{
return (String)get_Value("PRODUCTO7");
}
/** Set PRODUCTO8 */
public void setPRODUCTO8 (String PRODUCTO8)
{
if (PRODUCTO8 != null && PRODUCTO8.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO8 = PRODUCTO8.substring(0,99);
}
set_Value ("PRODUCTO8", PRODUCTO8);
}
/** Get PRODUCTO8 */
public String getPRODUCTO8() 
{
return (String)get_Value("PRODUCTO8");
}
/** Set PRODUCTO9 */
public void setPRODUCTO9 (String PRODUCTO9)
{
if (PRODUCTO9 != null && PRODUCTO9.length() > 100)
{
log.warning("Length > 100 - truncated");
PRODUCTO9 = PRODUCTO9.substring(0,99);
}
set_Value ("PRODUCTO9", PRODUCTO9);
}
/** Get PRODUCTO9 */
public String getPRODUCTO9() 
{
return (String)get_Value("PRODUCTO9");
}
/** Set T_PRODUCTCHECK_CAB_ID */
public void setT_PRODUCTCHECK_CAB_ID (int T_PRODUCTCHECK_CAB_ID)
{
if (T_PRODUCTCHECK_CAB_ID < 1) throw new IllegalArgumentException ("T_PRODUCTCHECK_CAB_ID is mandatory.");
set_ValueNoCheck ("T_PRODUCTCHECK_CAB_ID", new Integer(T_PRODUCTCHECK_CAB_ID));
}
/** Get T_PRODUCTCHECK_CAB_ID */
public int getT_PRODUCTCHECK_CAB_ID() 
{
Integer ii = (Integer)get_Value("T_PRODUCTCHECK_CAB_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
