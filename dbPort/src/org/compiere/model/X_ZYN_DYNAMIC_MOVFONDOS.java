/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/** Generated Model for ZYN_DYNAMIC_MOVFONDOS
 *
 * @author Jorg Janke (generated)
 * @version Release 3.2.2.1 - 2012-07-15 20:43:11.403 */
public class X_ZYN_DYNAMIC_MOVFONDOS extends PO {

    /** Standard Constructor */
    public X_ZYN_DYNAMIC_MOVFONDOS(Properties ctx, int ZYN_DYNAMIC_MOVFONDOS_ID, String trxName) {
        super(ctx, ZYN_DYNAMIC_MOVFONDOS_ID, trxName);
        /** if (ZYN_DYNAMIC_MOVFONDOS_ID == 0)
         * {
         * setAD_Ref_List_ID (0);
         * setCRE_CANCEL_FACT (false);
         * setCRE_CHEQUE_DEPO (false);
         * setCRE_CHEQUE_PROPIO (false);
         * setCRE_CHEQUE_PRO_RECH (false);
         * setCRE_CHEQUE_PRO_VENC (false);
         * setCRE_CHEQUE_RECI (false);
         * setCRE_CHEQUE_TER_RECH (false);
         * setCRE_CHEQUE_TER_VENC (false);
         * setCRE_CUENTA_BANCO_DESC (false);
         * setCRE_DEPOSITO_PEND (false);
         * setCRE_EFECTIVO (false);
         * setCRE_TRANSFERENCIA (false);
         * setCRE_VALORES_NEG (false);
         * setCode (null);
         * setDEB_CHEQUE_PRO_RECH (false);
         * setDEB_CHEQUE_PRO_VENC (false);
         * setDEB_CHEQUE_REC (false);
         * setDEB_CHEQUE_TERCERO (false);
         * setDEB_CHEQUE_TER_RECH (false);
         * setDEB_CHEQUE_TER_VENC (false);
         * setDEB_CREDITO_BANCO (false);
         * setDEB_CUENTA_BANCO (false);
         * setDEB_CUENTA_DEBITO (false);
         * setDEB_DEPOSITO (false);
         * setDEB_DEPOSITO_PEND (false);
         * setName (null);
         * setZYN_DYNAMIC_MOVFONDOS_ID (0);
         * }
         */
    }

    /** Load Constructor */
    public X_ZYN_DYNAMIC_MOVFONDOS(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    /** AD_Table_ID=5000043 */
    public static final int Table_ID = 5000043;
    /** TableName=ZYN_DYNAMIC_MOVFONDOS */
    public static final String Table_Name = "ZYN_DYNAMIC_MOVFONDOS";
    protected static KeyNamePair Model = new KeyNamePair(5000043, "ZYN_DYNAMIC_MOVFONDOS");
    protected BigDecimal accessLevel = new BigDecimal(3);

    /** AccessLevel 3 - Client - Org */
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO(Properties ctx) {
        POInfo poi = POInfo.getPOInfo(ctx, Table_ID);
        return poi;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_ZYN_DYNAMIC_MOVFONDOS[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set Reference List.
     * Reference List based on Table */
    public void setAD_Ref_List_ID(int AD_Ref_List_ID) {
        if (AD_Ref_List_ID < 1) {
            throw new IllegalArgumentException("AD_Ref_List_ID is mandatory.");
        }
        set_Value("AD_Ref_List_ID", new Integer(AD_Ref_List_ID));
    }

    /** Get Reference List.
     * Reference List based on Table */
    public int getAD_Ref_List_ID() {
        Integer ii = (Integer) get_Value("AD_Ref_List_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /** Set CRE_CANCEL_FACT */
    public void setCRE_CANCEL_FACT(boolean CRE_CANCEL_FACT) {
        set_Value("CRE_CANCEL_FACT", new Boolean(CRE_CANCEL_FACT));
    }

    /** Get CRE_CANCEL_FACT */
    public boolean isCRE_CANCEL_FACT() {
        Object oo = get_Value("CRE_CANCEL_FACT");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_DEPO */
    public void setCRE_CHEQUE_DEPO(boolean CRE_CHEQUE_DEPO) {
        set_Value("CRE_CHEQUE_DEPO", new Boolean(CRE_CHEQUE_DEPO));
    }

    /** Get CRE_CHEQUE_DEPO */
    public boolean isCRE_CHEQUE_DEPO() {
        Object oo = get_Value("CRE_CHEQUE_DEPO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_PROPIO */
    public void setCRE_CHEQUE_PROPIO(boolean CRE_CHEQUE_PROPIO) {
        set_Value("CRE_CHEQUE_PROPIO", new Boolean(CRE_CHEQUE_PROPIO));
    }

    /** Get CRE_CHEQUE_PROPIO */
    public boolean isCRE_CHEQUE_PROPIO() {
        Object oo = get_Value("CRE_CHEQUE_PROPIO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_PRO_RECH */
    public void setCRE_CHEQUE_PRO_RECH(boolean CRE_CHEQUE_PRO_RECH) {
        set_Value("CRE_CHEQUE_PRO_RECH", new Boolean(CRE_CHEQUE_PRO_RECH));
    }

    /** Get CRE_CHEQUE_PRO_RECH */
    public boolean isCRE_CHEQUE_PRO_RECH() {
        Object oo = get_Value("CRE_CHEQUE_PRO_RECH");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_PRO_VENC */
    public void setCRE_CHEQUE_PRO_VENC(boolean CRE_CHEQUE_PRO_VENC) {
        set_Value("CRE_CHEQUE_PRO_VENC", new Boolean(CRE_CHEQUE_PRO_VENC));
    }

    /** Get CRE_CHEQUE_PRO_VENC */
    public boolean isCRE_CHEQUE_PRO_VENC() {
        Object oo = get_Value("CRE_CHEQUE_PRO_VENC");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_RECI */
    public void setCRE_CHEQUE_RECI(boolean CRE_CHEQUE_RECI) {
        set_Value("CRE_CHEQUE_RECI", new Boolean(CRE_CHEQUE_RECI));
    }

    /** Get CRE_CHEQUE_RECI */
    public boolean isCRE_CHEQUE_RECI() {
        Object oo = get_Value("CRE_CHEQUE_RECI");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_TER_RECH */
    public void setCRE_CHEQUE_TER_RECH(boolean CRE_CHEQUE_TER_RECH) {
        set_Value("CRE_CHEQUE_TER_RECH", new Boolean(CRE_CHEQUE_TER_RECH));
    }

    /** Get CRE_CHEQUE_TER_RECH */
    public boolean isCRE_CHEQUE_TER_RECH() {
        Object oo = get_Value("CRE_CHEQUE_TER_RECH");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CHEQUE_TER_VENC */
    public void setCRE_CHEQUE_TER_VENC(boolean CRE_CHEQUE_TER_VENC) {
        set_Value("CRE_CHEQUE_TER_VENC", new Boolean(CRE_CHEQUE_TER_VENC));
    }

    /** Get CRE_CHEQUE_TER_VENC */
    public boolean isCRE_CHEQUE_TER_VENC() {
        Object oo = get_Value("CRE_CHEQUE_TER_VENC");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_CUENTA_BANCO_DESC */
    public void setCRE_CUENTA_BANCO_DESC(boolean CRE_CUENTA_BANCO_DESC) {
        set_Value("CRE_CUENTA_BANCO_DESC", new Boolean(CRE_CUENTA_BANCO_DESC));
    }

    /** Get CRE_CUENTA_BANCO_DESC */
    public boolean isCRE_CUENTA_BANCO_DESC() {
        Object oo = get_Value("CRE_CUENTA_BANCO_DESC");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_DEPOSITO_PEND */
    public void setCRE_DEPOSITO_PEND(boolean CRE_DEPOSITO_PEND) {
        set_Value("CRE_DEPOSITO_PEND", new Boolean(CRE_DEPOSITO_PEND));
    }

    /** Get CRE_DEPOSITO_PEND */
    public boolean isCRE_DEPOSITO_PEND() {
        Object oo = get_Value("CRE_DEPOSITO_PEND");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_EFECTIVO */
    public void setCRE_EFECTIVO(boolean CRE_EFECTIVO) {
        set_Value("CRE_EFECTIVO", new Boolean(CRE_EFECTIVO));
    }

    /** Get CRE_EFECTIVO */
    public boolean isCRE_EFECTIVO() {
        Object oo = get_Value("CRE_EFECTIVO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_EFECTIVO */
    public void setCRE_CUENTA_CREDITO(boolean CRE_CUENTA_CREDITO) {
        set_Value("CRE_CUENTA_CREDITO", new Boolean(CRE_CUENTA_CREDITO));
    }

    /** Get CRE_CUENTA_CREDITO */
    public boolean isCRE_CUENTA_CREDITO() {
        Object oo = get_Value("CRE_CUENTA_CREDITO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_TRANSFERENCIA */
    public void setCRE_TRANSFERENCIA(boolean CRE_TRANSFERENCIA) {
        set_Value("CRE_TRANSFERENCIA", new Boolean(CRE_TRANSFERENCIA));
    }

    /** Get CRE_TRANSFERENCIA */
    public boolean isCRE_TRANSFERENCIA() {
        Object oo = get_Value("CRE_TRANSFERENCIA");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_VALORES_NEG */
    public void setCRE_VALORES_NEG(boolean CRE_VALORES_NEG) {
        set_Value("CRE_VALORES_NEG", new Boolean(CRE_VALORES_NEG));
    }

    /** Get CRE_VALORES_NEG */
    public boolean isCRE_VALORES_NEG() {
        Object oo = get_Value("CRE_VALORES_NEG");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CRE_VALORES_NEG */
    public void setCRE_CUENTA_CREDITO_FIJA(boolean CRE_CUENTA_CREDITO_FIJA) {
        set_Value("CRE_CUENTA_CREDITO_FIJA", new Boolean(CRE_CUENTA_CREDITO_FIJA));
    }

    /** Get CRE_VALORES_NEG */
    public boolean isCRE_CUENTA_CREDITO_FIJA() {
        Object oo = get_Value("CRE_CUENTA_CREDITO_FIJA");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Validation code.
     * Validation Code */
    public void setCode(String Code) {
        if (Code == null) {
            throw new IllegalArgumentException("Code is mandatory.");
        }
        if (Code.length() > 60) {
            log.warning("Length > 60 - truncated");
            Code = Code.substring(0, 59);
        }
        set_Value("Code", Code);
    }

    /** Get Validation code.
     * Validation Code */
    public String getCode() {
        return (String) get_Value("Code");
    }

    /** Set DEB_CHEQUE_PRO_RECH */
    public void setDEB_CHEQUE_PRO_RECH(boolean DEB_CHEQUE_PRO_RECH) {
        set_Value("DEB_CHEQUE_PRO_RECH", new Boolean(DEB_CHEQUE_PRO_RECH));
    }

    /** Get DEB_CHEQUE_PRO_RECH */
    public boolean isDEB_CHEQUE_PRO_RECH() {
        Object oo = get_Value("DEB_CHEQUE_PRO_RECH");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CHEQUE_PRO_VENC */
    public void setDEB_CHEQUE_PRO_VENC(boolean DEB_CHEQUE_PRO_VENC) {
        set_Value("DEB_CHEQUE_PRO_VENC", new Boolean(DEB_CHEQUE_PRO_VENC));
    }

    /** Get DEB_CHEQUE_PRO_VENC */
    public boolean isDEB_CHEQUE_PRO_VENC() {
        Object oo = get_Value("DEB_CHEQUE_PRO_VENC");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CHEQUE_REC */
    public void setDEB_CHEQUE_REC(boolean DEB_CHEQUE_REC) {
        set_Value("DEB_CHEQUE_REC", new Boolean(DEB_CHEQUE_REC));
    }

    /** Get DEB_CHEQUE_REC */
    public boolean isDEB_CHEQUE_REC() {
        Object oo = get_Value("DEB_CHEQUE_REC");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CHEQUE_TERCERO */
    public void setDEB_CHEQUE_TERCERO(boolean DEB_CHEQUE_TERCERO) {
        set_Value("DEB_CHEQUE_TERCERO", new Boolean(DEB_CHEQUE_TERCERO));
    }

    /** Get DEB_CHEQUE_TERCERO */
    public boolean isDEB_CHEQUE_TERCERO() {
        Object oo = get_Value("DEB_CHEQUE_TERCERO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CHEQUE_TER_RECH */
    public void setDEB_CHEQUE_TER_RECH(boolean DEB_CHEQUE_TER_RECH) {
        set_Value("DEB_CHEQUE_TER_RECH", new Boolean(DEB_CHEQUE_TER_RECH));
    }

    /** Get DEB_CHEQUE_TER_RECH */
    public boolean isDEB_CHEQUE_TER_RECH() {
        Object oo = get_Value("DEB_CHEQUE_TER_RECH");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CHEQUE_TER_VENC */
    public void setDEB_CHEQUE_TER_VENC(boolean DEB_CHEQUE_TER_VENC) {
        set_Value("DEB_CHEQUE_TER_VENC", new Boolean(DEB_CHEQUE_TER_VENC));
    }

    /** Get DEB_CHEQUE_TER_VENC */
    public boolean isDEB_CHEQUE_TER_VENC() {
        Object oo = get_Value("DEB_CHEQUE_TER_VENC");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CREDITO_BANCO */
    public void setDEB_CREDITO_BANCO(boolean DEB_CREDITO_BANCO) {
        set_Value("DEB_CREDITO_BANCO", new Boolean(DEB_CREDITO_BANCO));
    }

    /** Get DEB_CREDITO_BANCO */
    public boolean isDEB_CREDITO_BANCO() {
        Object oo = get_Value("DEB_CREDITO_BANCO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CUENTA_BANCO */
    public void setDEB_CUENTA_BANCO(boolean DEB_CUENTA_BANCO) {
        set_Value("DEB_CUENTA_BANCO", new Boolean(DEB_CUENTA_BANCO));
    }

    /** Get DEB_CUENTA_BANCO */
    public boolean isDEB_CUENTA_BANCO() {
        Object oo = get_Value("DEB_CUENTA_BANCO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_CUENTA_DEBITO */
    public void setDEB_CUENTA_DEBITO(boolean DEB_CUENTA_DEBITO) {
        set_Value("DEB_CUENTA_DEBITO", new Boolean(DEB_CUENTA_DEBITO));
    }

    /** Get DEB_CUENTA_DEBITO */
    public boolean isDEB_CUENTA_DEBITO() {
        Object oo = get_Value("DEB_CUENTA_DEBITO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Get DEB_CUENTA_DEBITO */
    public boolean isDEB_CUENTA_DEBITO_FIJA() {
        Object oo = get_Value("DEB_CUENTA_DEBITO_FIJA");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_DEPOSITO */
    public void setDEB_DEPOSITO(boolean DEB_DEPOSITO) {
        set_Value("DEB_DEPOSITO", new Boolean(DEB_DEPOSITO));
    }

    /** Get DEB_DEPOSITO */
    public boolean isDEB_DEPOSITO() {
        Object oo = get_Value("DEB_DEPOSITO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set DEB_DEPOSITO_PEND */
    public void setDEB_DEPOSITO_PEND(boolean DEB_DEPOSITO_PEND) {
        set_Value("DEB_DEPOSITO_PEND", new Boolean(DEB_DEPOSITO_PEND));
    }

    /** Get DEB_DEPOSITO_PEND */
    public boolean isDEB_DEPOSITO_PEND() {
        Object oo = get_Value("DEB_DEPOSITO_PEND");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Description.
     * Optional short description of the record */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 254);
        }
        set_Value("Description", Description);
    }

    /** Get Description.
     * Optional short description of the record */
    public String getDescription() {
        return (String) get_Value("Description");
    }

    /** Set Name.
     * Alphanumeric identifier of the entity */
    public void setName(String Name) {
        if (Name == null) {
            throw new IllegalArgumentException("Name is mandatory.");
        }
        if (Name.length() > 510) {
            log.warning("Length > 510 - truncated");
            Name = Name.substring(0, 509);
        }
        set_Value("Name", Name);
    }

    /** Get Name.
     * Alphanumeric identifier of the entity */
    public String getName() {
        return (String) get_Value("Name");
    }

    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), getName());
    }

    /** Set ZYN_DYNAMIC_MOVFONDOS_ID */
    public void setZYN_DYNAMIC_MOVFONDOS_ID(int ZYN_DYNAMIC_MOVFONDOS_ID) {
        if (ZYN_DYNAMIC_MOVFONDOS_ID < 1) {
            throw new IllegalArgumentException("ZYN_DYNAMIC_MOVFONDOS_ID is mandatory.");
        }
        set_ValueNoCheck("ZYN_DYNAMIC_MOVFONDOS_ID", new Integer(ZYN_DYNAMIC_MOVFONDOS_ID));
    }

    /** Get ZYN_DYNAMIC_MOVFONDOS_ID */
    public int getZYN_DYNAMIC_MOVFONDOS_ID() {
        Integer ii = (Integer) get_Value("ZYN_DYNAMIC_MOVFONDOS_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Accounting Schema. Rules for accounting
     */
    public void setC_Element_ID(int C_Element_ID) {
        if (C_Element_ID < 1) {
            throw new IllegalArgumentException("C_Element_ID is mandatory.");
        }
        set_ValueNoCheck("C_Element_ID", new Integer(C_Element_ID));
    }

    /**
     * Get Accounting Schema. Rules for accounting
     */
    public int getC_Element_ID() {
        Integer ii = (Integer) get_Value("C_Element_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Get Accounting Schema. Rules for accounting
     */
    public int getDEB_C_Element_ID() {
        Integer ii = (Integer) get_Value("DEB_C_ELEMENT_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }
}
