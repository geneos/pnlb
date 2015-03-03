/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Privileges.sql,v 1.2 2001/11/19 14:26:47 jjanke Exp $
 ***
 * Title:		Pivileges - Access to Compiere
 * Description:
 ************************************************************************/
DECLARE
	--	Read only to all Tables but Dictionary
	CURSOR Cur_Tables_RO IS
		SELECT 	TableName 
		FROM 	AD_Table
		WHERE 	TableName NOT LIKE 'AD_%';

	CMD			VARCHAR2(256);
BEGIN
	DBMS_OUTPUT.PUT_LINE('Granting Access to Public ...');
	FOR ro IN Cur_Tables_RO LOOP	
		CMD := 'GRANT SELECT ON ' || ro.TableName || ' TO PUBLIC';
		DBMS_OUTPUT.PUT(ro.TableName);
		BEGIN
			EXECUTE IMMEDIATE CMD;
			DBMS_OUTPUT.PUT_LINE('  ok');
			EXCEPTION
				WHEN OTHERS THEN
					DBMS_OUTPUT.PUT_LINE('  ** error **');		
	  	END;
	END LOOP;
END;
/
