/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2002 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Apps_Tuning.sql,v 1.1 2002/07/29 04:56:47 jjanke Exp $
 ***
 * Title:		Application Tuning
 * Description:
 *
 ************************************************************************/

/********	Cursor Sharing		*****************************************/

--	Identify High Cost Operations
SELECT Object_Name, Operation, Options, Cost, Access_Predicates, Filter_Predicates
FROM V$SQL_PLAN
WHERE OBJECT_OWNER='COMPIERE'
AND Cost > 2
ORDER BY COST DESC
/
/**	Options
ALTER SYSTEM FLUSH SHARED_POOL;
ALTER SYSTEM SET CURSOR_SHARING = 'SIMILAR';
ALTER SESSION SET CURSOR_SHARING = 'SIMILAR';
ALTER SYSTEM SET CURSOR_SHARING = 'EXACT';
ALTER SESSION SET CURSOR_SHARING = 'EXACT';
**/
SELECT * FROM V$PARAMETER WHERE NAME='cursor_sharing'
/
SELECT * FROM V$SPPARAMETER WHERE NAME='cursor_sharing'
/