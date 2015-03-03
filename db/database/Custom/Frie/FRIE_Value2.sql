CREATE OR REPLACE FUNCTION FRIE_Value2
(
	Product_ID		IN	NUMBER
)
RETURN NVARCHAR2
AS
/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: FRIE_Value2.sql,v 1.4 2002/10/21 04:49:45 jjanke Exp $
 ***
 * Title:	Return Product Value with Vendor indicator
 * Description:	
 *			Cannot be used directly as mutating
 ************************************************************************/
 /*
	CREATE TABLE TEMP_PROD
	(	Product_ID	NUMBER,
		Value		VARCHAR2(60),
		CONSTRAINT TEMP_Key PRIMARY KEY (Product_ID)
    	USING INDEX TABLESPACE INDX	);
	INSERT INTO TEMP_PROD (PRODUCT_ID, VALUE)
	SELECT M_Product_ID, FRIE_Value2(M_Product_ID)
	FROM M_Product WHERE AD_Client_ID=1000000;
	UPDATE M_Product p
	SET Value = (SELECT Value FROM TEMP_Prod t WHERE p.M_Product_ID=t.Product_ID)
	WHERE EXISTS (SELECT * FROM TEMP_Prod t WHERE p.M_Product_ID=t.Product_ID AND Value IS NOT NULL);
	COMMIT;
	DROP TABLE TEMP_Prod;
 */
 	v_BPartner_ID			NUMBER;
	v_Value					M_Product.Value%TYPE;
	v_Prefix				VARCHAR2(2);
BEGIN
	--	Get Value/Partner
	SELECT	p.Value, DECODE(po.C_BPartner_ID,
			1001881,	'BA',		-- 	Barum
			1001725,	'BR',		--	Bridgestone
			1001738,	'CO',		--	Conti
			1001853,	'SE',		--	Semperit
			1001755,	'FI',		--	Firestone
			1001759,	'FU',		--	Fulda
			1001763,	'GY',		--	Goodyear
			1001792,	'KE',		--	Kelly
			1001795,	'KL',		--	Kleber
			1001883,	'LE',		--	Lee
			1001812,	'ME',		--	Metzeler
			1001815,	'MI',		--	Michelin
			1001824,	'pi',		--	Pirelli Motorrad
			1001868,	'PI',		--	Pirelli
			1001744,	'DU',		--	Dunlop
			1001864,	'UN',		--	Uniroyal
			1001872,	'VR',		--	Vredestein
			1001877,	'YO',		--	Yokohama
						'')			--	Othewise
	  INTO	v_Value, v_Prefix
	FROM 	M_Product p, M_Product_PO po	--	will through exception, if not found
	WHERE p.M_Product_ID=po.M_Product_ID
	  AND po.IsCurrentVendor='Y'
	  AND p.M_Product_ID=Product_ID
	  AND RowNum=1;		--	to be sure

	--	Are the first two characters numbers?
	IF (SUBSTR(v_Value,1,1) < '0' OR SUBSTR(v_Value,1,1) > '9'		--	not NLS compliant
			OR SUBSTR(v_Value,2,1) < '0' OR SUBSTR(v_Value,2,1) > '9') THEN
	--	DBMS_OUTPUT.PUT_LINE('No Number: ' || Value);
		RETURN SUBSTR(v_Value,1,40);
	END IF;
	--	Return with prefix
	IF (LENGTH(v_Prefix) = 2) THEN
	--	DBMS_OUTPUT.PUT_LINE('Prefix');
		RETURN SUBSTR(v_Prefix || v_Value, 1,40);
	END IF;
	--	Otherwise
	Return SUBSTR(v_Value, 1, 40);

	--	We must catch error as otherwise the function returns null
EXCEPTION 
	WHEN OTHERS THEN
--	DBMS_OUTPUT.PUT('Error: (ID=' || Product_ID || ') ' || SQLERRM);
	-- just return value of product
	SELECT	Value || N' '
	  INTO	v_Value
	FROM 	M_Product
	WHERE 	M_Product_ID=Product_ID;
--	DBMS_OUTPUT.PUT_LINE(' - ' || Value);
	--
	RETURN v_Value;

END FRIE_Value2;
/
