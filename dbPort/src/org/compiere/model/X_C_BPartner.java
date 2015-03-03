/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for C_BPartner
 *
 * @author Jorg Janke (generated)
 * @version Release 2.5.3b - 2008-04-04 14:05:58.734
 */
public class X_C_BPartner extends PO {

    /** Standard Constructor */
    public X_C_BPartner(Properties ctx, int C_BPartner_ID, String trxName) {
        super(ctx, C_BPartner_ID, trxName);
        /**
         * if (C_BPartner_ID == 0) { setC_BP_Group_ID (0); setC_BPartner_ID (0);
         * setIsCustomer (false); setIsEmployee (false); setIsOneTime (false);
         * setIsProspect (false); setIsSalesRep (false); setIsSummary (false);
         * setIsVendor (false); setName (null); setSO_CreditLimit (Env.ZERO);
         * setSO_CreditUsed (Env.ZERO); setSendEMail (false); setValue (null); }
         */
    }

    /** Load Constructor */
    public X_C_BPartner(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    /** TableName=C_BPartner */
    public static final String Table_Name = "C_BPartner";
    /** AD_Table_ID */
    public int Table_ID;
    protected KeyNamePair Model;

    /** Load Meta Data */
    protected POInfo initPO(Properties ctx) {
        POInfo info = initPO(ctx, Table_Name);
        Table_ID = info.getAD_Table_ID();
        Model = new KeyNamePair(Table_ID, Table_Name);
        return info;
    }
    protected BigDecimal accessLevel = new BigDecimal(3);

    /** AccessLevel 3 - Client - Org */
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_C_BPartner[").append(get_ID()).append("]");
        return sb.toString();
    }
    /** AD_Language AD_Reference_ID=327 */
    public static final int AD_LANGUAGE_AD_Reference_ID = 327;

    /**
     * Set Language. Language for this entity
     */
    public void setAD_Language(String AD_Language) {
        if (AD_Language != null && AD_Language.length() > 6) {
            log.warning("Length > 6 - truncated");
            AD_Language = AD_Language.substring(0, 5);
        }
        set_Value("AD_Language", AD_Language);
    }

    /**
     * Get Language. Language for this entity
     */
    public String getAD_Language() {
        return (String) get_Value("AD_Language");
    }

    /**
     * Set Linked Organization. The Business Partner is another Organization for
     * explicit Inter-Org transactions
     */
    public void setAD_OrgBP_ID(String AD_OrgBP_ID) {
        if (AD_OrgBP_ID != null && AD_OrgBP_ID.length() > 22) {
            log.warning("Length > 22 - truncated");
            AD_OrgBP_ID = AD_OrgBP_ID.substring(0, 21);
        }
        set_Value("AD_OrgBP_ID", AD_OrgBP_ID);
    }

    /**
     * Get Linked Organization. The Business Partner is another Organization for
     * explicit Inter-Org transactions
     */
    public String getAD_OrgBP_ID() {
        return (String) get_Value("AD_OrgBP_ID");
    }

    /**
     * Set Acquisition Cost. The cost of gaining the prospect as a customer
     */
    public void setAcqusitionCost(BigDecimal AcqusitionCost) {
        set_Value("AcqusitionCost", AcqusitionCost);
    }

    /**
     * Get Acquisition Cost. The cost of gaining the prospect as a customer
     */
    public BigDecimal getAcqusitionCost() {
        BigDecimal bd = (BigDecimal) get_Value("AcqusitionCost");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set Actual Life Time Value. Actual Life Time Revenue
     */
    public void setActualLifeTimeValue(BigDecimal ActualLifeTimeValue) {
        set_Value("ActualLifeTimeValue", ActualLifeTimeValue);
    }

    /**
     * Get Actual Life Time Value. Actual Life Time Revenue
     */
    public BigDecimal getActualLifeTimeValue() {
        BigDecimal bd = (BigDecimal) get_Value("ActualLifeTimeValue");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set Partner Parent. Business Partner Parent
     */
    public void setBPartner_Parent_ID(int BPartner_Parent_ID) {
        if (BPartner_Parent_ID <= 0) {
            set_Value("BPartner_Parent_ID", null);
        } else {
            set_Value("BPartner_Parent_ID", new Integer(BPartner_Parent_ID));
        }
    }

    /**
     * Get Partner Parent. Business Partner Parent
     */
    public int getBPartner_Parent_ID() {
        Integer ii = (Integer) get_Value("BPartner_Parent_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /** Set CONDICIONIVA_ID */
    public void setCONDICIONIVA_ID(int CONDICIONIVA_ID) {
        if (CONDICIONIVA_ID <= 0) {
            set_Value("CONDICIONIVA_ID", null);
        } else {
            set_Value("CONDICIONIVA_ID", new Integer(CONDICIONIVA_ID));
        }
    }

    /** Get CONDICIONIVA_ID */
    public int getCONDICIONIVA_ID() {
        Integer ii = (Integer) get_Value("CONDICIONIVA_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /** Set CONDICIONGAN_ID */
    public void setCONDICIONGAN_ID(int CONDICIONGAN_ID) {
        if (CONDICIONGAN_ID <= 0) {
            set_Value("CONDICIONGAN_ID", null);
        } else {
            set_Value("CONDICIONGAN_ID", new Integer(CONDICIONGAN_ID));
        }
    }

    /** Get CONDICIONGAN_ID */
    public int getCONDICIONGAN_ID() {
        Integer ii = (Integer) get_Value("CONDICIONGAN_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Business Partner Group. Business Partner Group
     */
    public void setC_BP_Group_ID(int C_BP_Group_ID) {
        if (C_BP_Group_ID < 1) {
            throw new IllegalArgumentException("C_BP_Group_ID is mandatory.");
        }
        set_Value("C_BP_Group_ID", new Integer(C_BP_Group_ID));
    }

    /**
     * Get Business Partner Group. Business Partner Group
     */
    public int getC_BP_Group_ID() {
        Integer ii = (Integer) get_Value("C_BP_Group_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Business Partner . Identifies a Business Partner
     */
    public void setC_BPartner_ID(int C_BPartner_ID) {
        if (C_BPartner_ID < 1) {
            throw new IllegalArgumentException("C_BPartner_ID is mandatory.");
        }
        set_ValueNoCheck("C_BPartner_ID", new Integer(C_BPartner_ID));
    }

    /**
     * Get Business Partner . Identifies a Business Partner
     */
    public int getC_BPartner_ID() {
        Integer ii = (Integer) get_Value("C_BPartner_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Dunning. Dunning Rules for overdue invoices
     */
    public void setC_Dunning_ID(int C_Dunning_ID) {
        if (C_Dunning_ID <= 0) {
            set_Value("C_Dunning_ID", null);
        } else {
            set_Value("C_Dunning_ID", new Integer(C_Dunning_ID));
        }
    }

    /**
     * Get Dunning. Dunning Rules for overdue invoices
     */
    public int getC_Dunning_ID() {
        Integer ii = (Integer) get_Value("C_Dunning_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Greeting. Greeting to print on correspondence
     */
    public void setC_Greeting_ID(int C_Greeting_ID) {
        if (C_Greeting_ID <= 0) {
            set_Value("C_Greeting_ID", null);
        } else {
            set_Value("C_Greeting_ID", new Integer(C_Greeting_ID));
        }
    }

    /**
     * Get Greeting. Greeting to print on correspondence
     */
    public int getC_Greeting_ID() {
        Integer ii = (Integer) get_Value("C_Greeting_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Invoice Schedule. Schedule for generating Invoices
     */
    public void setC_InvoiceSchedule_ID(int C_InvoiceSchedule_ID) {
        if (C_InvoiceSchedule_ID <= 0) {
            set_Value("C_InvoiceSchedule_ID", null);
        } else {
            set_Value("C_InvoiceSchedule_ID", new Integer(C_InvoiceSchedule_ID));
        }
    }

    /**
     * Get Invoice Schedule. Schedule for generating Invoices
     */
    public int getC_InvoiceSchedule_ID() {
        Integer ii = (Integer) get_Value("C_InvoiceSchedule_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Payment Term. The terms of Payment (timing, discount)
     */
    public void setC_PaymentTerm_ID(int C_PaymentTerm_ID) {
        if (C_PaymentTerm_ID <= 0) {
            set_Value("C_PaymentTerm_ID", null);
        } else {
            set_Value("C_PaymentTerm_ID", new Integer(C_PaymentTerm_ID));
        }
    }

    /**
     * Get Payment Term. The terms of Payment (timing, discount)
     */
    public int getC_PaymentTerm_ID() {
        Integer ii = (Integer) get_Value("C_PaymentTerm_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /** Set CoeficienteIB */
    public void setCoeficienteIB(BigDecimal CoeficienteIB) {
        set_Value("CoeficienteIB", CoeficienteIB);
    }

    /** Set CoeficienteIVA */
    public void setCoeficienteIVA(BigDecimal CoeficienteIVA) {
        set_Value("CoeficienteIVA", CoeficienteIVA);
    }

    /** Get CoeficienteIB */
    public BigDecimal getCoeficienteIB() {
        BigDecimal bd = (BigDecimal) get_Value("CoeficienteIB");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set D-U-N-S. Dun & Bradstreet Number
     */
    public void setDUNS(String DUNS) {
        if (DUNS != null && DUNS.length() > 11) {
            log.warning("Length > 11 - truncated");
            DUNS = DUNS.substring(0, 10);
        }
        set_Value("DUNS", DUNS);
    }

    /**
     * Get D-U-N-S. Dun & Bradstreet Number
     */
    public String getDUNS() {
        return (String) get_Value("DUNS");
    }
    /** DeliveryRule AD_Reference_ID=151 */
    public static final int DELIVERYRULE_AD_Reference_ID = 151;
    /** Availability = A */
    public static final String DELIVERYRULE_Availability = "A";
    /** Force = F */
    public static final String DELIVERYRULE_Force = "F";
    /** Complete Line = L */
    public static final String DELIVERYRULE_CompleteLine = "L";
    /** Manual = M */
    public static final String DELIVERYRULE_Manual = "M";
    /** Complete Order = O */
    public static final String DELIVERYRULE_CompleteOrder = "O";
    /** After Receipt = R */
    public static final String DELIVERYRULE_AfterReceipt = "R";

    /**
     * Set Delivery Rule. Defines the timing of Delivery
     */
    public void setDeliveryRule(String DeliveryRule) {
        if (DeliveryRule != null && DeliveryRule.length() > 1) {
            log.warning("Length > 1 - truncated");
            DeliveryRule = DeliveryRule.substring(0, 0);
        }
        set_Value("DeliveryRule", DeliveryRule);
    }

    /**
     * Get Delivery Rule. Defines the timing of Delivery
     */
    public String getDeliveryRule() {
        return (String) get_Value("DeliveryRule");
    }
    /** DeliveryViaRule AD_Reference_ID=152 */
    public static final int DELIVERYVIARULE_AD_Reference_ID = 152;
    /** Delivery = D */
    public static final String DELIVERYVIARULE_Delivery = "D";
    /** Pickup = P */
    public static final String DELIVERYVIARULE_Pickup = "P";
    /** Shipper = S */
    public static final String DELIVERYVIARULE_Shipper = "S";

    /**
     * Set Delivery Via. How the order will be delivered
     */
    public void setDeliveryViaRule(String DeliveryViaRule) {
        if (DeliveryViaRule != null && DeliveryViaRule.length() > 1) {
            log.warning("Length > 1 - truncated");
            DeliveryViaRule = DeliveryViaRule.substring(0, 0);
        }
        set_Value("DeliveryViaRule", DeliveryViaRule);
    }

    /**
     * Get Delivery Via. How the order will be delivered
     */
    public String getDeliveryViaRule() {
        return (String) get_Value("DeliveryViaRule");
    }

    /**
     * Set Description. Optional short description of the record
     */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 254);
        }
        set_Value("Description", Description);
    }

    /**
     * Get Description. Optional short description of the record
     */
    public String getDescription() {
        return (String) get_Value("Description");
    }

    /**
     * Set Document Copies. Number of copies to be printed
     */
    public void setDocumentCopies(int DocumentCopies) {
        set_Value("DocumentCopies", new Integer(DocumentCopies));
    }

    /**
     * Get Document Copies. Number of copies to be printed
     */
    public int getDocumentCopies() {
        Integer ii = (Integer) get_Value("DocumentCopies");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /** Set EMPLEADOR */
    public void setEMPLEADOR(boolean EMPLEADOR) {
        set_Value("EMPLEADOR", new Boolean(EMPLEADOR));
    }

    /** Get EMPLEADOR */
    public boolean isEMPLEADOR() {
        Object oo = get_Value("EMPLEADOR");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set EXENCIONGANANCIAS */
    public void setEXENCIONGANANCIAS(boolean EXENCIONGANANCIAS) {
        set_Value("EXENCIONGANANCIAS", new Boolean(EXENCIONGANANCIAS));
    }

    /** Get EXENCIONGANANCIAS */
    public boolean isEXENCIONGANANCIAS() {
        Object oo = get_Value("EXENCIONGANANCIAS");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set EXENCIONIB */
    public void setEXENCIONIB(boolean EXENCIONIB) {
        set_Value("EXENCIONIB", new Boolean(EXENCIONIB));
    }

    /** Get EXENCIONIB */
    public boolean isEXENCIONIB() {
        Object oo = get_Value("EXENCIONIB");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set EXENCIONIVA */
    public void setEXENCIONIVA(boolean EXENCIONIVA) {
        set_Value("EXENCIONIVA", new Boolean(EXENCIONIVA));
    }

    /** Get EXENCIONIVA */
    public boolean isEXENCIONIVA() {
        Object oo = get_Value("EXENCIONIVA");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set EXENCIONPERIB */
    public void setEXENCIONPERIB(boolean EXENCIONPERIB) {
        set_Value("EXENCIONPERIB", new Boolean(EXENCIONPERIB));
    }

    /** Get EXENCIONPERIB */
    public boolean isEXENCIONPERIB() {
        Object oo = get_Value("EXENCIONPERIB");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set EXENCIONPERIVA */
    public void setEXENCIONPERIVA(boolean EXENCIONPERIVA) {
        set_Value("EXENCIONPERIVA", new Boolean(EXENCIONPERIVA));
    }

    /** Get EXENCIONPERIVA */
    public boolean isEXENCIONPERIVA() {
        Object oo = get_Value("EXENCIONPERIVA");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set EXENCIONSUSS */
    public void setEXENCIONSUSS(boolean EXENCIONSUSS) {
        set_Value("EXENCIONSUSS", new Boolean(EXENCIONSUSS));
    }

    /** Get EXENCIONSUSS */
    public boolean isEXENCIONSUSS() {
        Object oo = get_Value("EXENCIONSUSS");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set FACTURAHONORARIOS */
    public void setFACTURAHONORARIOS(boolean FACTURAHONORARIOS) {
        set_Value("FACTURAHONORARIOS", new Boolean(FACTURAHONORARIOS));
    }

    /** Get FACTURAHONORARIOS */
    public boolean isFACTURAHONORARIOS() {
        Object oo = get_Value("FACTURAHONORARIOS");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set CONVENIOMULTILATERAL */
    public void setConvenioMultilateral(boolean CONVENIOMULTILATERAL) {
        set_Value("CONVENIOMULTILATERAL", new Boolean(CONVENIOMULTILATERAL));
    }

    /** Get CONVENIOMULTILATERAL */
    public boolean IsConvenioMultilateral() {
        Object oo = get_Value("CONVENIOMULTILATERAL");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set FACTURAM */
    public void setFACTURAM(boolean FACTURAM) {
        set_Value("FACTURAM", new Boolean(FACTURAM));
    }

    /** Get FACTURAM */
    public boolean isFACTURAM() {
        Object oo = get_Value("FACTURAM");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set First Sale. Date of First Sale
     */
    public void setFirstSale(Timestamp FirstSale) {
        set_Value("FirstSale", FirstSale);
    }

    /**
     * Get First Sale. Date of First Sale
     */
    public Timestamp getFirstSale() {
        return (Timestamp) get_Value("FirstSale");
    }

    /**
     * Set Flat Discount %. Flat discount percentage
     */
    public void setFlatDiscount(BigDecimal FlatDiscount) {
        set_Value("FlatDiscount", FlatDiscount);
    }

    /**
     * Get Flat Discount %. Flat discount percentage
     */
    public BigDecimal getFlatDiscount() {
        BigDecimal bd = (BigDecimal) get_Value("FlatDiscount");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
    /** FreightCostRule AD_Reference_ID=153 */
    public static final int FREIGHTCOSTRULE_AD_Reference_ID = 153;
    /** Calculated = C */
    public static final String FREIGHTCOSTRULE_Calculated = "C";
    /** Fix price = F */
    public static final String FREIGHTCOSTRULE_FixPrice = "F";
    /** Freight included = I */
    public static final String FREIGHTCOSTRULE_FreightIncluded = "I";
    /** Line = L */
    public static final String FREIGHTCOSTRULE_Line = "L";

    /**
     * Set Freight Cost Rule. Method for charging Freight
     */
    public void setFreightCostRule(String FreightCostRule) {
        if (FreightCostRule != null && FreightCostRule.length() > 1) {
            log.warning("Length > 1 - truncated");
            FreightCostRule = FreightCostRule.substring(0, 0);
        }
        set_Value("FreightCostRule", FreightCostRule);
    }

    /**
     * Get Freight Cost Rule. Method for charging Freight
     */
    public String getFreightCostRule() {
        return (String) get_Value("FreightCostRule");
    }

    /** Set IVA */
    public void setIVA(BigDecimal IVA) {
        set_Value("IVA", IVA);
    }

    /** Get IVA */
    public BigDecimal getIVA() {
        BigDecimal bd = (BigDecimal) get_Value("IVA");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }
    /** InvoiceRule AD_Reference_ID=150 */
    public static final int INVOICERULE_AD_Reference_ID = 150;
    /** After Delivery = D */
    public static final String INVOICERULE_AfterDelivery = "D";
    /** Immediate = I */
    public static final String INVOICERULE_Immediate = "I";
    /** After Order delivered = O */
    public static final String INVOICERULE_AfterOrderDelivered = "O";
    /** Customer Schedule after Delivery = S */
    public static final String INVOICERULE_CustomerScheduleAfterDelivery = "S";

    /**
     * Set Invoice Rule. Frequency and method of invoicing
     */
    public void setInvoiceRule(String InvoiceRule) {
        if (InvoiceRule != null && InvoiceRule.length() > 1) {
            log.warning("Length > 1 - truncated");
            InvoiceRule = InvoiceRule.substring(0, 0);
        }
        set_Value("InvoiceRule", InvoiceRule);
    }

    /**
     * Get Invoice Rule. Frequency and method of invoicing
     */
    public String getInvoiceRule() {
        return (String) get_Value("InvoiceRule");
    }
    /** Invoice_PrintFormat_ID AD_Reference_ID=261 */
    public static final int INVOICE_PRINTFORMAT_ID_AD_Reference_ID = 261;

    /**
     * Set Invoice Print Format. Print Format for printing Invoices
     */
    public void setInvoice_PrintFormat_ID(int Invoice_PrintFormat_ID) {
        if (Invoice_PrintFormat_ID <= 0) {
            set_Value("Invoice_PrintFormat_ID", null);
        } else {
            set_Value("Invoice_PrintFormat_ID", new Integer(
                    Invoice_PrintFormat_ID));
        }
    }

    /**
     * Get Invoice Print Format. Print Format for printing Invoices
     */
    public int getInvoice_PrintFormat_ID() {
        Integer ii = (Integer) get_Value("Invoice_PrintFormat_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Customer. Indicates if this Business Partner is a Customer
     */
    public void setIsCustomer(boolean IsCustomer) {
        set_Value("IsCustomer", new Boolean(IsCustomer));
    }

    /**
     * Get Customer. Indicates if this Business Partner is a Customer
     */
    public boolean isCustomer() {
        Object oo = get_Value("IsCustomer");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Discount Printed. Print Discount on Invoice and Order
     */
    public void setIsDiscountPrinted(boolean IsDiscountPrinted) {
        set_Value("IsDiscountPrinted", new Boolean(IsDiscountPrinted));
    }

    /**
     * Get Discount Printed. Print Discount on Invoice and Order
     */
    public boolean isDiscountPrinted() {
        Object oo = get_Value("IsDiscountPrinted");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Employee. Indicates if this Business Partner is an employee
     */
    public void setIsEmployee(boolean IsEmployee) {
        set_Value("IsEmployee", new Boolean(IsEmployee));
    }

    /**
     * Get Employee. Indicates if this Business Partner is an employee
     */
    public boolean isEmployee() {
        Object oo = get_Value("IsEmployee");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set One time transaction */
    public void setIsOneTime(boolean IsOneTime) {
        set_Value("IsOneTime", new Boolean(IsOneTime));
    }

    /** Get One time transaction */
    public boolean isOneTime() {
        Object oo = get_Value("IsOneTime");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Prospect. Indicates this is a Prospect
     */
    public void setIsProspect(boolean IsProspect) {
        set_Value("IsProspect", new Boolean(IsProspect));
    }

    /**
     * Get Prospect. Indicates this is a Prospect
     */
    public boolean isProspect() {
        Object oo = get_Value("IsProspect");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Sales Representative. Indicates if the business partner is a sales
     * representative or company agent
     */
    public void setIsSalesRep(boolean IsSalesRep) {
        set_Value("IsSalesRep", new Boolean(IsSalesRep));
    }

    /**
     * Get Sales Representative. Indicates if the business partner is a sales
     * representative or company agent
     */
    public boolean isSalesRep() {
        Object oo = get_Value("IsSalesRep");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Summary Level. This is a summary entity
     */
    public void setIsSummary(boolean IsSummary) {
        set_Value("IsSummary", new Boolean(IsSummary));
    }

    /**
     * Get Summary Level. This is a summary entity
     */
    public boolean isSummary() {
        Object oo = get_Value("IsSummary");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Tax exempt. Business partner is exempt from tax
     */
    public void setIsTaxExempt(boolean IsTaxExempt) {
        set_Value("IsTaxExempt", new Boolean(IsTaxExempt));
    }

    /**
     * Get Tax exempt. Business partner is exempt from tax
     */
    public boolean isTaxExempt() {
        Object oo = get_Value("IsTaxExempt");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Vendor. Indicates if this Business Partner is a Vendor
     */
    public void setIsVendor(boolean IsVendor) {
        set_Value("IsVendor", new Boolean(IsVendor));
    }

    /**
     * Get Vendor. Indicates if this Business Partner is a Vendor
     */
    public boolean isVendor() {
        Object oo = get_Value("IsVendor");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }
    /** M_DiscountSchema_ID AD_Reference_ID=325 */
    public static final int M_DISCOUNTSCHEMA_ID_AD_Reference_ID = 325;

    /**
     * Set Discount Schema. Schema to calculate the trade discount percentage
     */
    public void setM_DiscountSchema_ID(int M_DiscountSchema_ID) {
        if (M_DiscountSchema_ID <= 0) {
            set_Value("M_DiscountSchema_ID", null);
        } else {
            set_Value("M_DiscountSchema_ID", new Integer(M_DiscountSchema_ID));
        }
    }

    /**
     * Get Discount Schema. Schema to calculate the trade discount percentage
     */
    public int getM_DiscountSchema_ID() {
        Integer ii = (Integer) get_Value("M_DiscountSchema_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Price List. Unique identifier of a Price List
     */
    public void setM_PriceList_ID(int M_PriceList_ID) {
        if (M_PriceList_ID <= 0) {
            set_Value("M_PriceList_ID", null);
        } else {
            set_Value("M_PriceList_ID", new Integer(M_PriceList_ID));
        }
    }

    /**
     * Get Price List. Unique identifier of a Price List
     */
    public int getM_PriceList_ID() {
        Integer ii = (Integer) get_Value("M_PriceList_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set NAICS/SIC. Standard Industry Code or its successor NAIC -
     * http://www.osha.gov/oshstats/sicser.html
     */
    public void setNAICS(String NAICS) {
        if (NAICS != null && NAICS.length() > 6) {
            log.warning("Length > 6 - truncated");
            NAICS = NAICS.substring(0, 5);
        }
        set_Value("NAICS", NAICS);
    }

    /**
     * Get NAICS/SIC. Standard Industry Code or its successor NAIC -
     * http://www.osha.gov/oshstats/sicser.html
     */
    public String getNAICS() {
        return (String) get_Value("NAICS");
    }

    /**
     * Set Name. Alphanumeric identifier of the entity
     */
    public void setName(String Name) {
        if (Name == null) {
            throw new IllegalArgumentException("Name is mandatory.");
        }
        if (Name.length() > 60) {
            log.warning("Length > 60 - truncated");
            Name = Name.substring(0, 59);
        }
        set_Value("Name", Name);
    }

    /**
     * Get Name. Alphanumeric identifier of the entity
     */
    public String getName() {
        return (String) get_Value("Name");
    }

    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), getName());
    }

    /**
     * Set Name 2. Additional Name
     */
    public void setName2(String Name2) {
        if (Name2 != null && Name2.length() > 60) {
            log.warning("Length > 60 - truncated");
            Name2 = Name2.substring(0, 59);
        }
        set_Value("Name2", Name2);
    }

    /**
     * Get Name 2. Additional Name
     */
    public String getName2() {
        return (String) get_Value("Name2");
    }

    /**
     * Set Employees. Number of employees
     */
    public void setNumberEmployees(int NumberEmployees) {
        set_Value("NumberEmployees", new Integer(NumberEmployees));
    }

    /**
     * Get Employees. Number of employees
     */
    public int getNumberEmployees() {
        Integer ii = (Integer) get_Value("NumberEmployees");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Order Reference. Transaction Reference Number (Sales Order, Purchase
     * Order) of your Business Partner
     */
    public void setPOReference(String POReference) {
        if (POReference != null && POReference.length() > 20) {
            log.warning("Length > 20 - truncated");
            POReference = POReference.substring(0, 19);
        }
        set_Value("POReference", POReference);
    }

    /**
     * Get Order Reference. Transaction Reference Number (Sales Order, Purchase
     * Order) of your Business Partner
     */
    public String getPOReference() {
        return (String) get_Value("POReference");
    }
    /** PO_DiscountSchema_ID AD_Reference_ID=325 */
    public static final int PO_DISCOUNTSCHEMA_ID_AD_Reference_ID = 325;

    /**
     * Set PO Discount Schema. Schema to calculate the purchase trade discount
     * percentage
     */
    public void setPO_DiscountSchema_ID(int PO_DiscountSchema_ID) {
        if (PO_DiscountSchema_ID <= 0) {
            set_Value("PO_DiscountSchema_ID", null);
        } else {
            set_Value("PO_DiscountSchema_ID", new Integer(PO_DiscountSchema_ID));
        }
    }

    /**
     * Get PO Discount Schema. Schema to calculate the purchase trade discount
     * percentage
     */
    public int getPO_DiscountSchema_ID() {
        Integer ii = (Integer) get_Value("PO_DiscountSchema_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }
    /** PO_PaymentTerm_ID AD_Reference_ID=227 */
    public static final int PO_PAYMENTTERM_ID_AD_Reference_ID = 227;

    /**
     * Set PO Payment Term. Payment rules for a purchase order
     */
    public void setPO_PaymentTerm_ID(int PO_PaymentTerm_ID) {
        if (PO_PaymentTerm_ID <= 0) {
            set_Value("PO_PaymentTerm_ID", null);
        } else {
            set_Value("PO_PaymentTerm_ID", new Integer(PO_PaymentTerm_ID));
        }
    }

    /**
     * Get PO Payment Term. Payment rules for a purchase order
     */
    public int getPO_PaymentTerm_ID() {
        Integer ii = (Integer) get_Value("PO_PaymentTerm_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }
    /** PO_PriceList_ID AD_Reference_ID=166 */
    public static final int PO_PRICELIST_ID_AD_Reference_ID = 166;

    /**
     * Set Purchase Pricelist. Price List used by this Business Partner
     */
    public void setPO_PriceList_ID(int PO_PriceList_ID) {
        if (PO_PriceList_ID <= 0) {
            set_Value("PO_PriceList_ID", null);
        } else {
            set_Value("PO_PriceList_ID", new Integer(PO_PriceList_ID));
        }
    }

    /**
     * Get Purchase Pricelist. Price List used by this Business Partner
     */
    public int getPO_PriceList_ID() {
        Integer ii = (Integer) get_Value("PO_PriceList_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }
    /** PaymentRule AD_Reference_ID=195 */
    public static final int PAYMENTRULE_AD_Reference_ID = 195;
    /** Cash = B */
    public static final String PAYMENTRULE_Cash = "B";
    /** Direct Debit = D */
    public static final String PAYMENTRULE_DirectDebit = "D";
    /** Credit Card = K */
    public static final String PAYMENTRULE_CreditCard = "K";
    /** On Credit = P */
    public static final String PAYMENTRULE_OnCredit = "P";
    /** Check = S */
    public static final String PAYMENTRULE_Check = "S";
    /** Direct Deposit = T */
    public static final String PAYMENTRULE_DirectDeposit = "T";

    /**
     * Set Payment Rule. How you pay the invoice
     */
    public void setPaymentRule(String PaymentRule) {
        if (PaymentRule != null && PaymentRule.length() > 1) {
            log.warning("Length > 1 - truncated");
            PaymentRule = PaymentRule.substring(0, 0);
        }
        set_Value("PaymentRule", PaymentRule);
    }

    /**
     * Get Payment Rule. How you pay the invoice
     */
    public String getPaymentRule() {
        return (String) get_Value("PaymentRule");
    }
    /** PaymentRulePO AD_Reference_ID=195 */
    public static final int PAYMENTRULEPO_AD_Reference_ID = 195;
    /** Cash = B */
    public static final String PAYMENTRULEPO_Cash = "B";
    /** Direct Debit = D */
    public static final String PAYMENTRULEPO_DirectDebit = "D";
    /** Credit Card = K */
    public static final String PAYMENTRULEPO_CreditCard = "K";
    /** On Credit = P */
    public static final String PAYMENTRULEPO_OnCredit = "P";
    /** Check = S */
    public static final String PAYMENTRULEPO_Check = "S";
    /** Direct Deposit = T */
    public static final String PAYMENTRULEPO_DirectDeposit = "T";

    /**
     * Set Payment Rule. Purchase payment option
     */
    public void setPaymentRulePO(String PaymentRulePO) {
        if (PaymentRulePO != null && PaymentRulePO.length() > 1) {
            log.warning("Length > 1 - truncated");
            PaymentRulePO = PaymentRulePO.substring(0, 0);
        }
        set_Value("PaymentRulePO", PaymentRulePO);
    }

    /**
     * Get Payment Rule. Purchase payment option
     */
    public String getPaymentRulePO() {
        return (String) get_Value("PaymentRulePO");
    }

    /**
     * Set Potential Life Time Value. Total Revenue expected
     */
    public void setPotentialLifeTimeValue(BigDecimal PotentialLifeTimeValue) {
        set_Value("PotentialLifeTimeValue", PotentialLifeTimeValue);
    }

    /**
     * Get Potential Life Time Value. Total Revenue expected
     */
    public BigDecimal getPotentialLifeTimeValue() {
        BigDecimal bd = (BigDecimal) get_Value("PotentialLifeTimeValue");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set Rating. Classification or Importance
     */
    public void setRating(String Rating) {
        if (Rating != null && Rating.length() > 1) {
            log.warning("Length > 1 - truncated");
            Rating = Rating.substring(0, 0);
        }
        set_Value("Rating", Rating);
    }

    /**
     * Get Rating. Classification or Importance
     */
    public String getRating() {
        return (String) get_Value("Rating");
    }

    /**
     * Set Reference No. Your customer or vendor number at the Business
     * Partner's site
     */
    public void setReferenceNo(String ReferenceNo) {
        if (ReferenceNo != null && ReferenceNo.length() > 40) {
            log.warning("Length > 40 - truncated");
            ReferenceNo = ReferenceNo.substring(0, 39);
        }
        set_Value("ReferenceNo", ReferenceNo);
    }

    /**
     * Get Reference No. Your customer or vendor number at the Business
     * Partner's site
     */
    public String getReferenceNo() {
        return (String) get_Value("ReferenceNo");
    }
    /** SOCreditStatus AD_Reference_ID=289 */
    public static final int SOCREDITSTATUS_AD_Reference_ID = 289;
    /** Credit Hold = H */
    public static final String SOCREDITSTATUS_CreditHold = "H";
    /** Credit OK = O */
    public static final String SOCREDITSTATUS_CreditOK = "O";
    /** Credit Stop = S */
    public static final String SOCREDITSTATUS_CreditStop = "S";
    /** Credit Watch = W */
    public static final String SOCREDITSTATUS_CreditWatch = "W";
    /** No Credit Check = X */
    public static final String SOCREDITSTATUS_NoCreditCheck = "X";

    /**
     * Set Credit Status. Business Partner Credit Status
     */
    public void setSOCreditStatus(String SOCreditStatus) {
        if (SOCreditStatus != null && SOCreditStatus.length() > 1) {
            log.warning("Length > 1 - truncated");
            SOCreditStatus = SOCreditStatus.substring(0, 0);
        }
        set_Value("SOCreditStatus", SOCreditStatus);
    }

    /**
     * Get Credit Status. Business Partner Credit Status
     */
    public String getSOCreditStatus() {
        return (String) get_Value("SOCreditStatus");
    }

    /**
     * Set Credit Limit. Total outstanding invoice amounts allowed
     */
    public void setSO_CreditLimit(BigDecimal SO_CreditLimit) {
        if (SO_CreditLimit == null) {
            throw new IllegalArgumentException("SO_CreditLimit is mandatory.");
        }
        set_Value("SO_CreditLimit", SO_CreditLimit);
    }

    /**
     * Get Credit Limit. Total outstanding invoice amounts allowed
     */
    public BigDecimal getSO_CreditLimit() {
        BigDecimal bd = (BigDecimal) get_Value("SO_CreditLimit");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set Credit Used. Current open balance
     */
    public void setSO_CreditUsed(BigDecimal SO_CreditUsed) {
        if (SO_CreditUsed == null) {
            throw new IllegalArgumentException("SO_CreditUsed is mandatory.");
        }
        set_ValueNoCheck("SO_CreditUsed", SO_CreditUsed);
    }

    /**
     * Get Credit Used. Current open balance
     */
    public BigDecimal getSO_CreditUsed() {
        BigDecimal bd = (BigDecimal) get_Value("SO_CreditUsed");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set Order Description. Description to be used on orders
     */
    public void setSO_Description(String SO_Description) {
        if (SO_Description != null && SO_Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            SO_Description = SO_Description.substring(0, 254);
        }
        set_Value("SO_Description", SO_Description);
    }

    /**
     * Get Order Description. Description to be used on orders
     */
    public String getSO_Description() {
        return (String) get_Value("SO_Description");
    }

    /** Set SUJETO */
    public void setSUJETO(boolean SUJETO) {
        set_Value("SUJETO", new Boolean(SUJETO));
    }

    /** Get SUJETO */
    public boolean isSUJETO() {
        Object oo = get_Value("SUJETO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set SUJETOPER */
    public void setSUJETOPER(boolean SUJETOPER) {
        set_Value("SUJETOPER", new Boolean(SUJETOPER));
    }

    /** Get SUJETOPER */
    public boolean isSUJETOPER() {
        Object oo = get_Value("SUJETOPER");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }
    /** SalesRep_ID AD_Reference_ID=190 */
    public static final int SALESREP_ID_AD_Reference_ID = 190;

    /**
     * Set Sales Representative. Sales Representative or Company Agent
     */
    public void setSalesRep_ID(int SalesRep_ID) {
        if (SalesRep_ID <= 0) {
            set_Value("SalesRep_ID", null);
        } else {
            set_Value("SalesRep_ID", new Integer(SalesRep_ID));
        }
    }

    /**
     * Get Sales Representative. Sales Representative or Company Agent
     */
    public int getSalesRep_ID() {
        Integer ii = (Integer) get_Value("SalesRep_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Sales Volume in 1.000. Total Volume of Sales in Thousands of Currency
     */
    public void setSalesVolume(int SalesVolume) {
        set_Value("SalesVolume", new Integer(SalesVolume));
    }

    /**
     * Get Sales Volume in 1.000. Total Volume of Sales in Thousands of Currency
     */
    public int getSalesVolume() {
        Integer ii = (Integer) get_Value("SalesVolume");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Send EMail. Enable sending Document EMail
     */
    public void setSendEMail(boolean SendEMail) {
        set_Value("SendEMail", new Boolean(SendEMail));
    }

    /**
     * Get Send EMail. Enable sending Document EMail
     */
    public boolean isSendEMail() {
        Object oo = get_Value("SendEMail");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /**
     * Set Share. Share of Customer's business as a percentage
     */
    public void setShareOfCustomer(int ShareOfCustomer) {
        set_Value("ShareOfCustomer", new Integer(ShareOfCustomer));
    }

    /**
     * Get Share. Share of Customer's business as a percentage
     */
    public int getShareOfCustomer() {
        Integer ii = (Integer) get_Value("ShareOfCustomer");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Min Shelf Life %. Minimum Shelf Life in percent based on Product
     * Instance Guarantee Date
     */
    public void setShelfLifeMinPct(int ShelfLifeMinPct) {
        set_Value("ShelfLifeMinPct", new Integer(ShelfLifeMinPct));
    }

    /**
     * Get Min Shelf Life %. Minimum Shelf Life in percent based on Product
     * Instance Guarantee Date
     */
    public int getShelfLifeMinPct() {
        Integer ii = (Integer) get_Value("ShelfLifeMinPct");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Set Tax ID. Tax Identification
     */
    public void setTaxID(String TaxID) {
        if (TaxID != null && TaxID.length() > 20) {
            log.warning("Length > 20 - truncated");
            TaxID = TaxID.substring(0, 19);
        }
        set_Value("TaxID", TaxID);
    }

    /**
     * Get Tax ID. Tax Identification
     */
    public String getTaxID() {
        return (String) get_Value("TaxID");
    }

    /**
     * Set Open Balance. Total Open Balance Amount in primary Accounting
     * Currency
     */
    public void setTotalOpenBalance(BigDecimal TotalOpenBalance) {
        set_Value("TotalOpenBalance", TotalOpenBalance);
    }

    /**
     * Get Open Balance. Total Open Balance Amount in primary Accounting
     * Currency
     */
    public BigDecimal getTotalOpenBalance() {
        BigDecimal bd = (BigDecimal) get_Value("TotalOpenBalance");
        if (bd == null) {
            return Env.ZERO;
        }
        return bd;
    }

    /**
     * Set URL. Full URL address - e.g. http://www.compiere.org
     */
    public void setURL(String URL) {
        if (URL != null && URL.length() > 120) {
            log.warning("Length > 120 - truncated");
            URL = URL.substring(0, 119);
        }
        set_Value("URL", URL);
    }

    /**
     * Get URL. Full URL address - e.g. http://www.compiere.org
     */
    public String getURL() {
        return (String) get_Value("URL");
    }

    /**
     * Set Search Key. Search key for the record in the format required - must
     * be unique
     */
    public void setValue(String Value) {
        if (Value == null) {
            throw new IllegalArgumentException("Value is mandatory.");
        }
        if (Value.length() > 40) {
            log.warning("Length > 40 - truncated");
            Value = Value.substring(0, 39);
        }
        set_Value("Value", Value);
    }

    /**
     * Get Search Key. Search key for the record in the format required - must
     * be unique
     */
    public String getValue() {
        return (String) get_Value("Value");
    }

    /** Set EXTRANJERO */
    public void setExtranjero(boolean extranjero) {
        set_Value("BPEXTRANJERO", new Boolean(extranjero));
    }

    /** Get EXTRANJERO */
    public boolean isExtranjero() {
        Object oo = get_Value("BPEXTRANJERO");
        if (oo != null) {
            if (oo instanceof Boolean) {
                return ((Boolean) oo).booleanValue();
            }
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Número Extranjero. */
    public void setNro_Extranjero(String extranjero) {
        set_Value("NROEXTRANJERO", extranjero);
    }

    /** Get Número Extranjero. */
    public String getNro_Extranjero() {
        return (String) get_Value("NROEXTRANJERO");
    }

    /** Set SUSS_PERCENTAGE */
    public void setSUSS_PERCENTAGE(int SUSS_PERCENTAGE) {
        set_Value("SUSS_PERCENTAGE", new Integer(SUSS_PERCENTAGE));
    }

    /** Get SUSS_PERCENTAGE */
    public int getSUSS_PERCENTAGE() {
        Integer ii = (Integer) get_Value("SUSS_PERCENTAGE");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }
}
