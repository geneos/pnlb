/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for I_BANK_TRANSFER
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2020-04-20 11:40:31.825 */
public class X_I_BANK_TRANSFER extends PO
{
/** Standard Constructor */
public X_I_BANK_TRANSFER (Properties ctx, int I_BANK_TRANSFER_ID, String trxName)
{
super (ctx, I_BANK_TRANSFER_ID, trxName);
/** if (I_BANK_TRANSFER_ID == 0)
{
setI_BANK_TRANSFER_ID (0);
setI_IsImported (false);
}
 */
}
/** Load Constructor */
public X_I_BANK_TRANSFER (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000077 */
public static final int Table_ID=5000077;

/** TableName=I_BANK_TRANSFER */
public static final String Table_Name="I_BANK_TRANSFER";

protected static KeyNamePair Model = new KeyNamePair(5000077,"I_BANK_TRANSFER");

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
StringBuffer sb = new StringBuffer ("X_I_BANK_TRANSFER[").append(get_ID()).append("]");
return sb.toString();
}
/** Set BENEFICIARIO */
public void setBENEFICIARIO (String BENEFICIARIO)
{
if (BENEFICIARIO != null && BENEFICIARIO.length() > 50)
{
log.warning("Length > 50 - truncated");
BENEFICIARIO = BENEFICIARIO.substring(0,49);
}
set_Value ("BENEFICIARIO", BENEFICIARIO);
}
/** Get BENEFICIARIO */
public String getBENEFICIARIO() 
{
return (String)get_Value("BENEFICIARIO");
}
/** Set Business Partner .
Identifies a Business Partner */
public void setC_BPartner_ID (int C_BPartner_ID)
{
if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
 else 
set_Value ("C_BPartner_ID", new Integer(C_BPartner_ID));
}
/** Get Business Partner .
Identifies a Business Partner */
public int getC_BPartner_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment.
Payment identifier */
public void setC_Payment_ID (int C_Payment_ID)
{
if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
 else 
set_Value ("C_Payment_ID", new Integer(C_Payment_ID));
}
/** Get Payment.
Payment identifier */
public int getC_Payment_ID() 
{
Integer ii = (Integer)get_Value("C_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_VALORPAGO_ID */
public void setC_VALORPAGO_ID (int C_VALORPAGO_ID)
{
if (C_VALORPAGO_ID <= 0) set_Value ("C_VALORPAGO_ID", null);
 else 
set_Value ("C_VALORPAGO_ID", new Integer(C_VALORPAGO_ID));
}
/** Get C_VALORPAGO_ID */
public int getC_VALORPAGO_ID() 
{
Integer ii = (Integer)get_Value("C_VALORPAGO_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set FECHA_PAGO */
public void setFECHA_PAGO (Timestamp FECHA_PAGO)
{
set_Value ("FECHA_PAGO", FECHA_PAGO);
}
/** Get FECHA_PAGO */
public Timestamp getFECHA_PAGO() 
{
return (Timestamp)get_Value("FECHA_PAGO");
}
/** Set I_BANK_TRANSFER_ID */
public void setI_BANK_TRANSFER_ID (int I_BANK_TRANSFER_ID)
{
if (I_BANK_TRANSFER_ID < 1) throw new IllegalArgumentException ("I_BANK_TRANSFER_ID is mandatory.");
set_ValueNoCheck ("I_BANK_TRANSFER_ID", new Integer(I_BANK_TRANSFER_ID));
}
/** Get I_BANK_TRANSFER_ID */
public int getI_BANK_TRANSFER_ID() 
{
Integer ii = (Integer)get_Value("I_BANK_TRANSFER_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Import Error Message.
Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Set Imported.
Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", new Boolean(I_IsImported));
}
/** Get Imported.
Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set MONTO */
public void setMONTO (BigDecimal MONTO)
{
set_Value ("MONTO", MONTO);
}
/** Get MONTO */
public BigDecimal getMONTO() 
{
BigDecimal bd = (BigDecimal)get_Value("MONTO");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set NUMERO_BENEFICIARIO */
public void setNUMERO_BENEFICIARIO (String NUMERO_BENEFICIARIO)
{
if (NUMERO_BENEFICIARIO != null && NUMERO_BENEFICIARIO.length() > 14)
{
log.warning("Length > 14 - truncated");
NUMERO_BENEFICIARIO = NUMERO_BENEFICIARIO.substring(0,13);
}
set_Value ("NUMERO_BENEFICIARIO", NUMERO_BENEFICIARIO);
}
/** Get NUMERO_BENEFICIARIO */
public String getNUMERO_BENEFICIARIO() 
{
return (String)get_Value("NUMERO_BENEFICIARIO");
}
/** Set NUMERO_DOCUMENTO_BENEFICIARIO */
public void setNUMERO_DOCUMENTO_BENEFICIARIO (String NUMERO_DOCUMENTO_BENEFICIARIO)
{
if (NUMERO_DOCUMENTO_BENEFICIARIO != null && NUMERO_DOCUMENTO_BENEFICIARIO.length() > 11)
{
log.warning("Length > 11 - truncated");
NUMERO_DOCUMENTO_BENEFICIARIO = NUMERO_DOCUMENTO_BENEFICIARIO.substring(0,10);
}
set_Value ("NUMERO_DOCUMENTO_BENEFICIARIO", NUMERO_DOCUMENTO_BENEFICIARIO);
}
/** Get NUMERO_DOCUMENTO_BENEFICIARIO */
public String getNUMERO_DOCUMENTO_BENEFICIARIO() 
{
return (String)get_Value("NUMERO_DOCUMENTO_BENEFICIARIO");
}
/** Set Processed.
The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", new Boolean(Processed));
}
/** Get Processed.
The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set TIPO_DOCUMENTO */
public void setTIPO_DOCUMENTO (String TIPO_DOCUMENTO)
{
if (TIPO_DOCUMENTO != null && TIPO_DOCUMENTO.length() > 2)
{
log.warning("Length > 2 - truncated");
TIPO_DOCUMENTO = TIPO_DOCUMENTO.substring(0,1);
}
set_Value ("TIPO_DOCUMENTO", TIPO_DOCUMENTO);
}
/** Get TIPO_DOCUMENTO */
public String getTIPO_DOCUMENTO() 
{
return (String)get_Value("TIPO_DOCUMENTO");
}
/** Set TIPO_DOCUMENTO_BENEFICIARIO */
public void setTIPO_DOCUMENTO_BENEFICIARIO (String TIPO_DOCUMENTO_BENEFICIARIO)
{
if (TIPO_DOCUMENTO_BENEFICIARIO != null && TIPO_DOCUMENTO_BENEFICIARIO.length() > 2)
{
log.warning("Length > 2 - truncated");
TIPO_DOCUMENTO_BENEFICIARIO = TIPO_DOCUMENTO_BENEFICIARIO.substring(0,1);
}
set_Value ("TIPO_DOCUMENTO_BENEFICIARIO", TIPO_DOCUMENTO_BENEFICIARIO);
}
/** Get TIPO_DOCUMENTO_BENEFICIARIO */
public String getTIPO_DOCUMENTO_BENEFICIARIO() 
{
return (String)get_Value("TIPO_DOCUMENTO_BENEFICIARIO");
}
/** Set VERSION_RENDICION */
public void setVERSION_RENDICION (String VERSION_RENDICION)
{
if (VERSION_RENDICION != null && VERSION_RENDICION.length() > 5)
{
log.warning("Length > 5 - truncated");
VERSION_RENDICION = VERSION_RENDICION.substring(0,4);
}
set_Value ("VERSION_RENDICION", VERSION_RENDICION);
}
/** Get VERSION_RENDICION */
public String getVERSION_RENDICION() 
{
return (String)get_Value("VERSION_RENDICION");
}
}
