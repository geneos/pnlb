/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for C_MOVIMIENTOFONDOS
 *  @author Jorg Janke (generated)
 *  @version Release 2.8.9 - 2011-11-07 11:53:44.482 */
public class X_C_MOVIMIENTOFONDOS extends PO
{
/** Standard Constructor */
public X_C_MOVIMIENTOFONDOS (Properties ctx, int C_MOVIMIENTOFONDOS_ID, String trxName)
{
super (ctx, C_MOVIMIENTOFONDOS_ID, trxName);
/** if (C_MOVIMIENTOFONDOS_ID == 0)
{
setC_DocType_ID (0);
setC_MOVIMIENTOFONDOS_ID (0);
setDocAction (null);	// CO
setDocStatus (null);	// DR
setDocumentNo (null);
setIsApproved (false);
setPosted (false);	// N
setProcessed (false);
setTIPO (null);
}
 */
}
/** Load Constructor */
public X_C_MOVIMIENTOFONDOS (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=1000304 */
public static final int Table_ID=1000304;

/** TableName=C_MOVIMIENTOFONDOS */
public static final String Table_Name="C_MOVIMIENTOFONDOS";

protected static KeyNamePair Model = new KeyNamePair(1000304,"C_MOVIMIENTOFONDOS");

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
StringBuffer sb = new StringBuffer ("X_C_MOVIMIENTOFONDOS[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Document Type.
Document type or rules */
public void setC_DocType_ID (int C_DocType_ID)
{
if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
set_Value ("C_DocType_ID", new Integer(C_DocType_ID));
}
/** Get Document Type.
Document type or rules */
public int getC_DocType_ID()
{
Integer ii = (Integer)get_Value("C_DocType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_MOVIMIENTOFONDOS_ID */
public void setC_MOVIMIENTOFONDOS_ID (int C_MOVIMIENTOFONDOS_ID)
{
if (C_MOVIMIENTOFONDOS_ID < 1) throw new IllegalArgumentException ("C_MOVIMIENTOFONDOS_ID is mandatory.");
set_ValueNoCheck ("C_MOVIMIENTOFONDOS_ID", new Integer(C_MOVIMIENTOFONDOS_ID));
}
/** Get C_MOVIMIENTOFONDOS_ID */
public int getC_MOVIMIENTOFONDOS_ID()
{
Integer ii = (Integer)get_Value("C_MOVIMIENTOFONDOS_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Account Date.
Accounting Date */
public void setDateAcct (Timestamp DateAcct)
{
set_Value ("DateAcct", DateAcct);
}
/** Get Account Date.
Accounting Date */
public Timestamp getDateAcct()
{
return (Timestamp)get_Value("DateAcct");
}
/** Set Transaction Date.
Transaction Date */
public void setDateTrx (Timestamp DateTrx)
{
set_Value ("DateTrx", DateTrx);
}
/** Get Transaction Date.
Transaction Date */
public Timestamp getDateTrx()
{
return (Timestamp)get_Value("DateTrx");
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription()
{
return (String)get_Value("Description");
}

/** DocAction AD_Reference_ID=135 */
public static final int DOCACTION_AD_Reference_ID=135;
/** <None> = -- */
public static final String DOCACTION_None = "--";
/** Approve = AP */
public static final String DOCACTION_Approve = "AP";
/** Close = CL */
public static final String DOCACTION_Close = "CL";
/** Complete = CO */
public static final String DOCACTION_Complete = "CO";
/** Invalidate = IN */
public static final String DOCACTION_Invalidate = "IN";
/** Post = PO */
public static final String DOCACTION_Post = "PO";
/** Prepare = PR */
public static final String DOCACTION_Prepare = "PR";
/** Reverse - Accrual = RA */
public static final String DOCACTION_Reverse_Accrual = "RA";
/** Reverse - Correct = RC */
public static final String DOCACTION_Reverse_Correct = "RC";
/** Re-activate = RE */
public static final String DOCACTION_Re_Activate = "RE";
/** Reject = RJ */
public static final String DOCACTION_Reject = "RJ";
/** Void = VO */
public static final String DOCACTION_Void = "VO";
/** Wait Complete = WC */
public static final String DOCACTION_WaitComplete = "WC";
/** Unlock = XL */
public static final String DOCACTION_Unlock = "XL";
/** Set Document Action.
The targeted status of the document */
public void setDocAction (String DocAction)
{
if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");
if (DocAction.equals("--") || DocAction.equals("AP") || DocAction.equals("CL") || DocAction.equals("CO") || DocAction.equals("IN") || DocAction.equals("PO") || DocAction.equals("PR") || DocAction.equals("RA") || DocAction.equals("RC") || DocAction.equals("RE") || DocAction.equals("RJ") || DocAction.equals("VO") || DocAction.equals("WC") || DocAction.equals("XL"));
 else throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
if (DocAction.length() > 2)
{
log.warning("Length > 2 - truncated");
DocAction = DocAction.substring(0,1);
}
set_Value ("DocAction", DocAction);
}
/** Get Document Action.
The targeted status of the document */
public String getDocAction()
{
return (String)get_Value("DocAction");
}

/** DocStatus AD_Reference_ID=131 */
public static final int DOCSTATUS_AD_Reference_ID=131;
/** Unknown = ?? */
public static final String DOCSTATUS_Unknown = "??";
/** Approved = AP */
public static final String DOCSTATUS_Approved = "AP";
/** Closed = CL */
public static final String DOCSTATUS_Closed = "CL";
/** Completed = CO */
public static final String DOCSTATUS_Completed = "CO";
/** Drafted = DR */
public static final String DOCSTATUS_Drafted = "DR";
/** Invalid = IN */
public static final String DOCSTATUS_Invalid = "IN";
/** In Progress = IP */
public static final String DOCSTATUS_InProgress = "IP";
/** Not Approved = NA */
public static final String DOCSTATUS_NotApproved = "NA";
/** Reversed = RE */
public static final String DOCSTATUS_Reversed = "RE";
/** Voided = VO */
public static final String DOCSTATUS_Voided = "VO";
/** Waiting Confirmation = WC */
public static final String DOCSTATUS_WaitingConfirmation = "WC";
/** Waiting Payment = WP */
public static final String DOCSTATUS_WaitingPayment = "WP";
/** Set Document Status.
The current status of the document */
public void setDocStatus (String DocStatus)
{
if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
if (DocStatus.equals("??") || DocStatus.equals("AP") || DocStatus.equals("CL") || DocStatus.equals("CO") || DocStatus.equals("DR") || DocStatus.equals("IN") || DocStatus.equals("IP") || DocStatus.equals("NA") || DocStatus.equals("RE") || DocStatus.equals("VO") || DocStatus.equals("WC") || DocStatus.equals("WP"));
 else throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
if (DocStatus.length() > 2)
{
log.warning("Length > 2 - truncated");
DocStatus = DocStatus.substring(0,1);
}
set_Value ("DocStatus", DocStatus);
}
/** Get Document Status.
The current status of the document */
public String getDocStatus()
{
return (String)get_Value("DocStatus");
}
/** Set Document No.
Document sequence number of the document */
public void setDocumentNo (String DocumentNo)
{
if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
if (DocumentNo.length() > 30)
{
log.warning("Length > 30 - truncated");
DocumentNo = DocumentNo.substring(0,29);
}
set_Value ("DocumentNo", DocumentNo);
}
/** Get Document No.
Document sequence number of the document */
public String getDocumentNo()
{
return (String)get_Value("DocumentNo");
}
/** Set Approved.
Indicates if this document requires approval */
public void setIsApproved (boolean IsApproved)
{
set_Value ("IsApproved", new Boolean(IsApproved));
}
/** Get Approved.
Indicates if this document requires approval */
public boolean isApproved()
{
Object oo = get_Value("IsApproved");
if (oo != null)
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Posted.
Posting status */
public void setPosted (boolean Posted)
{
set_Value ("Posted", new Boolean(Posted));
}
/** Get Posted.
Posting status */
public boolean isPosted()
{
Object oo = get_Value("Posted");
if (oo != null)
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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

/** TIPO AD_Reference_ID=1000087 */
public static final int TIPO_AD_Reference_ID=1000087;
/** Transferencia Bancaria / Débito Bancario = B */
public static final String TIPO_TraBancaria = "B";
/** Cambio de Cheques Recibidos = C */
public static final String TIPO_CambioCheque = "C";
/** Depósito de Cheques = D */
public static final String TIPO_DepCheque = "D";
/** Emisión de Cheques Propios = E */
public static final String TIPO_EmiCheque = "E";
/** Movimiento de Efectivo = M */
public static final String TIPO_MovEfectivo = "M";
/** Valores Negociados = N */
public static final String TIPO_ValNegociados = "N";
/** Rechazo de Cheques Propios = P */
public static final String TIPO_RechCqPropio = "P";
/** Reintegro de Exportación = R */
public static final String TIPO_ReintegroDeExportacion = "R";
/** Rechazo de Cheque de Terceros = T */
public static final String TIPO_RechCqTercero = "T";
/** Cheques de Terceros Vencidos = V */
public static final String TIPO_VencCqTercero = "V";
/** Cheques Propios Vencidos = X */
public static final String TIPO_VencCqPropio = "X";
/** Depósitos Pendientes = Y */
public static final String TIPO_DepositoPendiente = "Y";
/** Créditos Bancarios = Z */
public static final String TIPO_CreditoBancario = "Z";

/*
 *  Agregado por Zynnia
 *  16/02/2012
 *  Requerimientos de nuevos tipos de movimientos
 *
 */

/** Transferencia entre cuentas bancarias = S */
public static final String TIPO_TransferenciaCuentasBancarias = "S";

/** Cancelacion por Cesion de Facturas = F */
public static final String TIPO_CancelacionCesionFacturas = "F";

/** Set TIPO */
public void setTIPO (String TIPO)
{
if (TIPO == null) {
    throw new IllegalArgumentException ("TIPO is mandatory");
}
MZYNDYNAMICMOVFONDOS dynMovFondos = MZYNDYNAMICMOVFONDOS.get(Env.getCtx(), TIPO);
if (TIPO.equals("B") || TIPO.equals("C") || TIPO.equals("D") || TIPO.equals("E") || TIPO.equals("M") ||
    TIPO.equals("N") || TIPO.equals("P") || TIPO.equals("R") || TIPO.equals("T") || TIPO.equals("V") ||
    TIPO.equals("X") || TIPO.equals("Y") || TIPO.equals("Z") || dynMovFondos != null) {}
else {
    throw new IllegalArgumentException ("TIPO Invalid value - " + TIPO + " - Reference_ID=1000087 - B - C - D - E - M - N - P - R - T - V - X - Y - Z");
}
//if (TIPO.length() > 1)
//{
//log.warning("Length > 1 - truncated");
//TIPO = TIPO.substring(0,0);
//}
set_Value ("TIPO", TIPO);
}
/** Get TIPO */
public String getTIPO()
{
return (String)get_Value("TIPO");
}
}
