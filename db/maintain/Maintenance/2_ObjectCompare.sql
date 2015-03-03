/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: 2_ObjectCompare.sql,v 1.4 2005/04/27 17:50:34 jjanke Exp $
 ***
 * Title:	Compare existance of Objects in Compiere and Reference
 * Description:
 ************************************************************************/

--	Procedures, ..
SELECT OBJECT_NAME "Object", InitCap(OBJECT_TYPE) "Type", InitCap(OWNER) "Only in"
FROM ALL_OBJECTS a1 
WHERE OWNER = 'COMPIERE'
  AND OBJECT_TYPE NOT IN ('INDEX', 'VIEW', 'LOB')
  AND NOT EXISTS (SELECT * FROM ALL_OBJECTS a2 
	WHERE OWNER = 'REFERENCE' AND a1.OBJECT_NAME=a2.OBJECT_NAME
	  AND OBJECT_TYPE NOT IN ('INDEX', 'VIEW', 'LOB'))
ORDER BY 2,1;

SELECT OBJECT_NAME "Object", InitCap(OBJECT_TYPE) "Type", InitCap(OWNER) "Only in"
FROM ALL_OBJECTS a1 
WHERE OWNER = 'REFERENCE'
  AND OBJECT_TYPE NOT IN ('INDEX', 'VIEW', 'LOB')
  AND NOT EXISTS (SELECT * FROM ALL_OBJECTS a2 
	WHERE OWNER = 'COMPIERE' AND a1.OBJECT_NAME=a2.OBJECT_NAME
	  AND OBJECT_TYPE NOT IN ('INDEX', 'VIEW', 'LOB'))
ORDER BY 2,1;

--	Index, Views
SELECT OBJECT_NAME "Object", InitCap(OBJECT_TYPE) "Type", InitCap(OWNER) "Only in"
FROM ALL_OBJECTS a1 
WHERE OWNER = 'COMPIERE'
  AND OBJECT_TYPE IN ('INDEX', 'VIEW')
  AND NOT EXISTS (SELECT * FROM ALL_OBJECTS a2 
	WHERE OWNER = 'REFERENCE' AND a1.OBJECT_NAME=a2.OBJECT_NAME
	  AND OBJECT_TYPE IN ('INDEX', 'VIEW'))
ORDER BY 2,1;

SELECT OBJECT_NAME "Object", InitCap(OBJECT_TYPE) "Type", InitCap(OWNER) "Only in"
FROM ALL_OBJECTS a1 
WHERE OWNER = 'REFERENCE'
  AND OBJECT_TYPE IN ('INDEX', 'VIEW')
  AND NOT EXISTS (SELECT * FROM ALL_OBJECTS a2 
	WHERE OWNER = 'COMPIERE' AND a1.OBJECT_NAME=a2.OBJECT_NAME
	  AND OBJECT_TYPE IN ('INDEX', 'VIEW'))
ORDER BY 2,1;

