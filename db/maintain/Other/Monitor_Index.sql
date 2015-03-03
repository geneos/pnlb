/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2002 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Monitor_Index.sql,v 1.1 2002/07/29 04:56:47 jjanke Exp $
 ***
 * Title:	Monitor Index Usage
 * Description:
		SELECT * FROM V$OBJECT_USAGE WHERE USED<>'NO' ORDER BY 1;
		SELECT * FROM V$OBJECT_USAGE WHERE USED='NO' ORDER BY 1;
 ************************************************************************/

DECLARE
	CURSOR CUR_IDX IS
		SELECT 	Index_Name, Index_Type 
		FROM 	USER_INDEXES
		WHERE	INDEX_TYPE<>'LOB';	--	 no LOB Indexes
	v_Cmd		VARCHAR2(2000);
BEGIN
	FOR i IN Cur_IDX LOOP
		v_Cmd := 'ALTER INDEX ' || i.Index_Name || ' MONITORING USAGE';
--		v_Cmd := 'ALTER INDEX ' || i.Index_Name || ' NOMONITORING USAGE';
		EXECUTE IMMEDIATE v_Cmd;
  	END LOOP;
END;
/
