/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2003 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Dummy_Translation.sql,v 1.1 2003/02/20 06:42:13 jjanke Exp $
 ***
 * Title:	Create Dummy Language Translation
 * Description:
 *		For Testing Purposes.
 ************************************************************************/
DECLARE
	v_Language	VARCHAR2(30) := 'xx_XX';
	CURSOR	CUR_Trl IS
		SELECT TableName 
		FROM AD_Table t
		WHERE TableName LIKE '%Trl'
		  AND EXISTS (SELECT * FROM AD_Column c WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='Name');
	v_cmd		VARCHAR(2000);
BEGIN
	--	Non Standard
	UPDATE AD_Message_Trl SET MsgText = MsgText || v_Language WHERE AD_Language=v_Language;
	DBMS_OUTPUT.PUT_LINE('AD_Message_Trl=' || SQL%ROWCOUNT);
	UPDATE C_DunningLevel_Trl SET PrintName = PrintName || v_Language WHERE AD_Language=v_Language;
	DBMS_OUTPUT.PUT_LINE('C_DunningLevel_Trl=' || SQL%ROWCOUNT);
	UPDATE AD_PrintFormatItem_Trl SET PrintName = PrintName || v_Language WHERE AD_Language=v_Language;
	DBMS_OUTPUT.PUT_LINE('AD_PrintFormatItem_Trl=' || SQL%ROWCOUNT);
	--
	FOR t IN CUR_Trl LOOP
		v_cmd := 'UPDATE ' || t.TableName 
			|| ' SET Name = Name || ''' || v_Language 
			|| ''' WHERE AD_Language=''' || v_Language || '''';
		EXECUTE IMMEDIATE v_cmd;
		DBMS_OUTPUT.PUT_LINE(t.TableName || '=' || SQL%ROWCOUNT);
	END LOOP;	
	COMMIT;
END;
/