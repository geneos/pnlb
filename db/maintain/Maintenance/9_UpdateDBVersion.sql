/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: 9_UpdateDBVersion.sql,v 1.59 2005/12/31 06:32:42 jjanke Exp $
 ***
 * Title:	Update Database Version
 * Description:
 ************************************************************************/

UPDATE AD_System 
-----------------==========-----
  SET	ReleaseNo = '253b',
        Version='2005-12-30',
-----------------==========----- 
        Created=Sysdate,
		Updated=SysDate;
--
COMMIT;
--
SELECT * FROM AD_System;

