/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: 0_SyncNames.sql,v 1.8 2005/04/27 17:50:34 jjanke Exp $
 ***
 * Title:	Synchronize Names
 * Description:
 *		Update Column and Field with Names from Element and Process
 *		Update Process Parameters from Elements
 *		Update Workflow Nodes from Windows
 *
SELECT w.Name "Window", t.Name "Tab", f.Name "Field", c.Name "Column"
FROM AD_Field f
INNER JOIN AD_Tab t ON (t.AD_Tab_ID=f.AD_Tab_ID)
INNER JOIN AD_Window w ON (w.AD_Window_ID=t.AD_Window_ID)
INNER JOIN AD_Column c ON (c.AD_Column_ID=f.AD_Column_ID)
WHERE f.IsCentrallyMaintained='N'
ORDER BY 1,2 
 *
SELECT p.Name "Process", pp.Name "Parameter", pp.ColumnName "Par Column", e.Name "Element", e.ColumnName "Ele Column"
FROM AD_Process_Para pp
INNER JOIN AD_Process p ON (p.AD_Process_ID=pp.AD_Process_ID)
LEFT OUTER JOIN AD_Element e ON (e.AD_Element_ID=pp.AD_Element_ID)
WHERE pp.IsCentrallyMaintained='N' 
 *
 ************************************************************************/

BEGIN
	AD_Synchronize(null);
	COMMIT;
END;
/
