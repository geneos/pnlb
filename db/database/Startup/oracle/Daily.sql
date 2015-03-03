/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2002 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Daily.sql,v 1.2 2002/09/27 04:54:37 jjanke Exp $
 ***
 * Title:   Daily Tasks
 * Description:
 *	- Recompile 
 *  - Cleanup
 ************************************************************************/
DECLARE
	Result		VARCHAR2(2000);
BEGIN
    DBA_Recompile(Result);
    DBMS_OUTPUT.PUT_LINE(Result);
    DBA_Cleanup();
END;
/
EXIT
