/**
 *	Create SQL Java Functions (Oracle)
 *
 *	Author + Copyright 1999-2005 Jorg Janke
 *	$Header: /cvsroot/compiere/sqlj/oracle/createSQLJ.sql,v 1.13 2005/07/23 04:54:41 jjanke Exp $
 */
 
CREATE OR REPLACE FUNCTION compiereVersion
 	RETURN VARCHAR2
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Compiere.getVersion() return java.lang.String';
/
CREATE OR REPLACE FUNCTION compiereProperties
 	RETURN VARCHAR2
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Compiere.getProperties() return java.lang.String';
/
CREATE OR REPLACE FUNCTION compiereProperty(p_key VARCHAR2)
 	RETURN VARCHAR2
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Compiere.getProperty(java.lang.String) return java.lang.String';
/

/** Product	**/
CREATE OR REPLACE FUNCTION productAttribute (M_AttributeSetInstance_ID NUMBER)
 	RETURN NVARCHAR2
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.attributeName(int) return java.lang.String';
/

CREATE OR REPLACE FUNCTION bomPriceLimit (M_Product_ID NUMBER, M_PriceList_Version_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomPriceLimit(int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomPriceList (M_Product_ID NUMBER, M_PriceList_Version_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomPriceList(int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomPriceStd (M_Product_ID NUMBER, M_PriceList_Version_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomPriceStd(int,int) return java.math.BigDecimal';
/

CREATE OR REPLACE FUNCTION bomQtyAvailable (M_Product_ID NUMBER, M_Warehouse_ID NUMBER, 
        M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyAvailable(int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomQtyOnHand (M_Product_ID NUMBER, M_Warehouse_ID NUMBER, 
        M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyOnHand(int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomQtyOrdered (M_Product_ID NUMBER, M_Warehouse_ID NUMBER, 
        M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyOrdered(int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomQtyReserved (M_Product_ID NUMBER, M_Warehouse_ID NUMBER, 
        M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyReserved(int,int,int) return java.math.BigDecimal';
/

/** Currency **/
CREATE OR REPLACE FUNCTION currencyBase (Amount NUMBER, C_CurrencyFrom_ID NUMBER, 
        ConversionDate DATE, AD_Client_ID NUMBER, AD_Org_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Currency.base(java.math.BigDecimal,int,java.sql.Timestamp,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION currencyConvert (Amount NUMBER, C_CurrencyFrom_ID NUMBER, 
        C_CurrencyTo_ID NUMBER,
    ConversionDate DATE, C_ConversionType_ID NUMBER, AD_Client_ID NUMBER, AD_Org_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Currency.convert(java.math.BigDecimal,int,int,java.sql.Timestamp,int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION currencyRate (C_CurrencyFrom_ID NUMBER, C_CurrencyTo_ID NUMBER,
        ConversionDate DATE, C_ConversionType_ID NUMBER, AD_Client_ID NUMBER, AD_Org_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Currency.rate(int,int,java.sql.Timestamp,int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION currencyRound (Amt NUMBER, C_CurrencyTo_ID NUMBER, IsCosting VARCHAR2)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Currency.round(java.math.BigDecimal,int,java.lang.String) return java.math.BigDecimal';
/

/** BPartner **/
CREATE OR REPLACE FUNCTION bpartnerRemitLocation (p_C_BPartner_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.BPartner.remitLocation(int) return int';
/

/** Invoice **/
CREATE OR REPLACE FUNCTION invoiceOpen (p_C_Invoice_ID NUMBER, p_C_InvoicePaySchedule_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Invoice.open(int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION invoicePaid (p_C_Invoice_ID NUMBER, p_C_Currency_ID NUMBER, 
        p_MultiplierAP NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Invoice.paid(int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION invoiceDiscount (p_C_Invoice_ID NUMBER, p_PayDate Date, 
        p_C_InvoicePaySchedule_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Invoice.discount(int,java.sql.Timestamp,int) return java.math.BigDecimal';
/

/** Payment Term **/
CREATE OR REPLACE FUNCTION paymentTermDueDays (p_C_PaymentTerm_ID NUMBER, p_DocDate DATE, 
        p_PayDate DATE)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.PaymentTerm.dueDays(int,java.sql.Timestamp,java.sql.Timestamp) return int';
/
CREATE OR REPLACE FUNCTION paymentTermDiscount (p_Amount NUMBER, p_C_Currency_ID NUMBER,
		p_C_PaymentTerm_ID NUMBER, p_DocDate DATE, p_PayDate DATE)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.PaymentTerm.discount(java.math.BigDecimal,int,int,java.sql.Timestamp,java.sql.Timestamp) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION paymentTermDueDate (p_C_PaymentTerm_ID NUMBER, p_DocDate DATE)
 	RETURN DATE
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.PaymentTerm.dueDate(int,java.sql.Timestamp) return java.sql.Timestamp';
/

/** Payment **/
CREATE OR REPLACE FUNCTION paymentAllocated (p_C_Payment_ID NUMBER, p_C_Currency_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Payment.allocated(int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION paymentAvailable (p_C_Payment_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Payment.available(int) return java.math.BigDecimal';
/

/** Account **/
CREATE OR REPLACE FUNCTION acctBalance (p_Account_ID NUMBER, p_AmtDr NUMBER, p_AmtCr NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Account.balance(int,java.math.BigDecimal,java.math.BigDecimal) return java.math.BigDecimal';
/

/** General	**/
BEGIN
	dbms_java.grant_permission('COMPIERE','SYS:java.util.PropertyPermission', '*', 'read,write');
END;
/

--begin vpj-cd e-evolution
CREATE OR REPLACE FUNCTION bomQtyAvailable2 (M_Product_ID NUMBER, M_AttributeSetInstance_ID NUMBER, M_Warehouse_ID NUMBER, M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyAvailable(int,int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomQtyOnHand2 (M_Product_ID NUMBER, M_AttributeSetInstance_ID NUMBER,M_Warehouse_ID NUMBER, M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyOnHand(int,int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomQtyReserved2 (M_Product_ID NUMBER, M_AttributeSetInstance_ID NUMBER, M_Warehouse_ID NUMBER, M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyReserved(int,int,int,int) return java.math.BigDecimal';
/
CREATE OR REPLACE FUNCTION bomQtyOrdered2 (M_Product_ID NUMBER, M_AttributeSetInstance_ID NUMBER, M_Warehouse_ID NUMBER, M_Locator_ID NUMBER)
 	RETURN NUMBER
 	AS LANGUAGE JAVA 
	NAME 'org.compiere.sqlj.Product.bomQtyOrdered(int,int,int,int) return java.math.BigDecimal';
/
--end vpj-cd e-evolution.com

/** Get Character at Position   */
CREATE OR REPLACE FUNCTION charAt
(
    p_string    VARCHAR2,
    p_pos       NUMBER
)
 	RETURN VARCHAR2
AS
BEGIN
    RETURN SUBSTR(p_string, p_pos, 1);
END;    
/
/** GetDate                     */
CREATE OR REPLACE FUNCTION getdate
 	RETURN DATE
AS
BEGIN
    RETURN SysDate;
END;    
/
/** First Of DD/DY/MM/Q         */
CREATE OR REPLACE FUNCTION firstOf
(
    p_date      DATE,
    p_datePart  VARCHAR2
)
 	RETURN DATE
AS
BEGIN
    RETURN TRUNC(p_date, p_datePart);
END;    
/
/** Add Number of Days      */
CREATE OR REPLACE FUNCTION addDays
(
    p_date      DATE,
    p_days      NUMBER
)
 	RETURN DATE
AS
BEGIN
    RETURN TRUNC(p_date) + p_days;
END;    
/
/** Difference in Days      */
CREATE OR REPLACE FUNCTION daysBetween
(
    p_date1     DATE,
    p_date2     DATE
)
 	RETURN NUMBER
AS
BEGIN
    RETURN TRUNC(p_date1) - TRUNC(p_date2);
END;    
/

create or replace FUNCTION getPartWord(Input varchar2, inf number,sup number) 
RETURN VARCHAR2 
AS LANGUAGE JAVA NAME 'org.compiere.sqlj.Invoice.getPart(java.lang.String,int,int) return java.lang.String';
/

SELECT --compiereVersion(), compiereProperty('java.vendor'), 
    TRUNC(getdate()) FROM DUAL
/

EXIT
