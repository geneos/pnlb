/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for I_ICBC_CHECK
 *  @author Jorg Janke (generated) 
 *  @version Release 3.3.0.0 - 2020-01-08 17:54:06.596 */
public class X_I_ICBC_CHECK extends PO
{
/** Standard Constructor */
public X_I_ICBC_CHECK (Properties ctx, int I_ICBC_CHECK_ID, String trxName)
{
super (ctx, I_ICBC_CHECK_ID, trxName);
/** if (I_ICBC_CHECK_ID == 0)
{
setI_ICBC_CHECK_ID (0);
setI_IsImported (false);
}
 */
}
/** Load Constructor */
public X_I_ICBC_CHECK (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=5000074 */
public static final int Table_ID=5000076;

/** TableName=I_ICBC_CHECK */
public static final String Table_Name="I_ICBC_CHECK";

protected static KeyNamePair Model = new KeyNamePair(5000074,"I_ICBC_CHECK");

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
StringBuffer sb = new StringBuffer ("X_I_ICBC_CHECK[").append(get_ID()).append("]");
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
/** Set Payment.
Payment identifier */
public void setC_VALORPAGO_ID (int C_VALORPAGO_ID)
{
if (C_VALORPAGO_ID <= 0) set_Value ("C_VALORPAGO_ID", null);
 else 
set_Value ("C_VALORPAGO_ID", new Integer(C_VALORPAGO_ID));
}
/** Get Payment.
Payment identifier */
public int getC_VALORPAGO_ID() 
{
Integer ii = (Integer)get_Value("C_VALORPAGO_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ESTADO_CHEQUE */
public void setESTADO_CHEQUE (String ESTADO_CHEQUE)
{
if (ESTADO_CHEQUE != null && ESTADO_CHEQUE.length() > 3)
{
log.warning("Length > 3 - truncated");
ESTADO_CHEQUE = ESTADO_CHEQUE.substring(0,2);
}
set_Value ("ESTADO_CHEQUE", ESTADO_CHEQUE);
}
/** Get ESTADO_CHEQUE */
public String getESTADO_CHEQUE() 
{
return (String)get_Value("ESTADO_CHEQUE");
}
/** Set ESTADO_CHEQUE2 */
public void setESTADO_CHEQUE2 (String ESTADO_CHEQUE2)
{
if (ESTADO_CHEQUE2 != null && ESTADO_CHEQUE2.length() > 2)
{
log.warning("Length > 2 - truncated");
ESTADO_CHEQUE2 = ESTADO_CHEQUE2.substring(0,1);
}
set_Value ("ESTADO_CHEQUE2", ESTADO_CHEQUE2);
}
/** Get ESTADO_CHEQUE2 */
public String getESTADO_CHEQUE2() 
{
return (String)get_Value("ESTADO_CHEQUE2");
}
/** Set ESTADO_GENERAL */
public void setESTADO_GENERAL (String ESTADO_GENERAL)
{
if (ESTADO_GENERAL != null && ESTADO_GENERAL.length() > 1)
{
log.warning("Length > 1 - truncated");
ESTADO_GENERAL = ESTADO_GENERAL.substring(0,0);
}
set_Value ("ESTADO_GENERAL", ESTADO_GENERAL);
}
/** Get ESTADO_GENERAL */
public String getESTADO_GENERAL() 
{
return (String)get_Value("ESTADO_GENERAL");
}
/** Set ESTADO_PAGO */
public void setESTADO_PAGO (String ESTADO_PAGO)
{
if (ESTADO_PAGO != null && ESTADO_PAGO.length() > 15)
{
log.warning("Length > 15 - truncated");
ESTADO_PAGO = ESTADO_PAGO.substring(0,14);
}
set_Value ("ESTADO_PAGO", ESTADO_PAGO);
}
/** Get ESTADO_PAGO */
public String getESTADO_PAGO() 
{
return (String)get_Value("ESTADO_PAGO");
}
/** Set FECHA_EMICION_LISTA */
public void setFECHA_EMICION_LISTA (Timestamp FECHA_EMICION_LISTA)
{
set_Value ("FECHA_EMICION_LISTA", FECHA_EMICION_LISTA);
}
/** Get FECHA_EMICION_LISTA */
public Timestamp getFECHA_EMICION_LISTA() 
{
return (Timestamp)get_Value("FECHA_EMICION_LISTA");
}
/** Set FECHA_IMPRESION */
public void setFECHA_IMPRESION (Timestamp FECHA_IMPRESION)
{
set_Value ("FECHA_IMPRESION", FECHA_IMPRESION);
}
/** Get FECHA_IMPRESION */
public Timestamp getFECHA_IMPRESION() 
{
return (Timestamp)get_Value("FECHA_IMPRESION");
}
/** Set FECHA_LIQUIDACION */
public void setFECHA_LIQUIDACION (Timestamp FECHA_LIQUIDACION)
{
set_Value ("FECHA_LIQUIDACION", FECHA_LIQUIDACION);
}
/** Get FECHA_LIQUIDACION */
public Timestamp getFECHA_LIQUIDACION() 
{
return (Timestamp)get_Value("FECHA_LIQUIDACION");
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
/** Set I_ICBC_CHECK_ID */
public void setI_ICBC_CHECK_ID (int I_ICBC_CHECK_ID)
{
if (I_ICBC_CHECK_ID < 1) throw new IllegalArgumentException ("I_ICBC_CHECK_ID is mandatory.");
set_ValueNoCheck ("I_ICBC_CHECK_ID", new Integer(I_ICBC_CHECK_ID));
}
/** Get I_ICBC_CHECK_ID */
public int getI_ICBC_CHECK_ID() 
{
Integer ii = (Integer)get_Value("I_ICBC_CHECK_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set NUMERO_CHEQUE */
public void setNUMERO_CHEQUE (String NUMERO_CHEQUE)
{
if (NUMERO_CHEQUE != null && NUMERO_CHEQUE.length() > 11)
{
log.warning("Length > 11 - truncated");
NUMERO_CHEQUE = NUMERO_CHEQUE.substring(0,10);
}
set_Value ("NUMERO_CHEQUE", NUMERO_CHEQUE);
}
/** Get NUMERO_CHEQUE */
public String getNUMERO_CHEQUE() 
{
return (String)get_Value("NUMERO_CHEQUE");
}
/** Set NUMERO_COMPROBANTE */
public void setNUMERO_COMPROBANTE (String NUMERO_COMPROBANTE)
{
if (NUMERO_COMPROBANTE != null && NUMERO_COMPROBANTE.length() > 12)
{
log.warning("Length > 12 - truncated");
NUMERO_COMPROBANTE = NUMERO_COMPROBANTE.substring(0,11);
}
set_Value ("NUMERO_COMPROBANTE", NUMERO_COMPROBANTE);
}
/** Get NUMERO_COMPROBANTE */
public String getNUMERO_COMPROBANTE() 
{
return (String)get_Value("NUMERO_COMPROBANTE");
}
/** Set NUMERO_CUENTA */
public void setNUMERO_CUENTA (String NUMERO_CUENTA)
{
if (NUMERO_CUENTA != null && NUMERO_CUENTA.length() > 22)
{
log.warning("Length > 22 - truncated");
NUMERO_CUENTA = NUMERO_CUENTA.substring(0,21);
}
set_Value ("NUMERO_CUENTA", NUMERO_CUENTA);
}
/** Get NUMERO_CUENTA */
public String getNUMERO_CUENTA() 
{
return (String)get_Value("NUMERO_CUENTA");
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
/** Set TIPO_CUENTA */
public void setTIPO_CUENTA (String TIPO_CUENTA)
{
if (TIPO_CUENTA != null && TIPO_CUENTA.length() > 2)
{
log.warning("Length > 2 - truncated");
TIPO_CUENTA = TIPO_CUENTA.substring(0,1);
}
set_Value ("TIPO_CUENTA", TIPO_CUENTA);
}
/** Get TIPO_CUENTA */
public String getTIPO_CUENTA() 
{
return (String)get_Value("TIPO_CUENTA");
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
