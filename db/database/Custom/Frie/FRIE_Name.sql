CREATE OR REPLACE FUNCTION FRIE_Name
(
	Name				IN	VARCHAR2
)
RETURN VARCHAR2
AS
/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: FRIE_Name.sql,v 1.3 2002/10/21 04:49:45 jjanke Exp $
 ***
 * Title:	Return "clean" Product Name
 * Description:
 ************************************************************************/

 	RetValue			VARCHAR2(60);
BEGIN
	--	Clean double spaces
	RetValue := REPLACE (Name, '  ', ' ');
	RetValue := REPLACE (RetValue, '  ', ' ');
	RetValue := REPLACE (RetValue, '  ', ' ');
	--	Isolate R
	RetValue := REPLACE (RetValue, '0R1', '0 R 1');
	RetValue := REPLACE (RetValue, '0R 1', '0 R 1');
	RetValue := REPLACE (RetValue, '0 R1', '0 R 1');

	RetValue := REPLACE (RetValue, '5R1', '5 R 1');
	RetValue := REPLACE (RetValue, '5R 1', '5 R 1');
	RetValue := REPLACE (RetValue, '5 R1', '5 R 1');

	RetValue := REPLACE (RetValue, '0R2', '0 R 2');
	RetValue := REPLACE (RetValue, '0R 2', '0 R 2');
	RetValue := REPLACE (RetValue, '0 R2', '0 R 2');

	RetValue := REPLACE (RetValue, '5R2', '5 R 2');
	RetValue := REPLACE (RetValue, '5R 2', '5 R 2');
	RetValue := REPLACE (RetValue, '5 R2', '5 R 2');

	RetValue := REPLACE (RetValue, '2R2', '2 R 2');
	RetValue := REPLACE (RetValue, '2R 2', '2 R 2');
	RetValue := REPLACE (RetValue, '2 R2', '2 R 2');

	RetValue := REPLACE (RetValue, '0R8', '0 R 8');


	--	Isolate HR
	RetValue := REPLACE (RetValue, '0HR1', '0 HR 1');
	--	Isolate VR
	RetValue := REPLACE (RetValue, '0VR1', '0 VR 1');
	RetValue := REPLACE (RetValue, '5VR1', '5 VR 1');
	RetValue := REPLACE (RetValue, '0VR3', '0 VR 3');
	RetValue := REPLACE (RetValue, '5VR3', '5 VR 3');
	RetValue := REPLACE (RetValue, '0VR 1', '0 VR 1');
	RetValue := REPLACE (RetValue, '5VR 1', '5 VR 1');
	--	Isolate ZR
	RetValue := REPLACE (RetValue, '0ZR1', '0 ZR 1');
	RetValue := REPLACE (RetValue, '5ZR1', '5 ZR 1');
	RetValue := REPLACE (RetValue, '0ZR 1', '0 ZR 1');
	RetValue := REPLACE (RetValue, '5ZR 1', '5 ZR 1');
	RetValue := REPLACE (RetValue, '0 ZR1', '0 ZR 1');
	RetValue := REPLACE (RetValue, '5 ZR1', '5 ZR 1');

	--	Change "B." => B
	IF (SUBSTR(RetValue,1,2) = 'B.') THEN
		RetValue := REPLACE (RetValue, 'B.', 'B');
	END IF;

	--	Remove "D."
	IF (SUBSTR(RetValue,1,2) = 'D.') THEN
		RetValue := SUBSTR(RetValue,3);
	END IF;
	--	Remove "D "
	IF (SUBSTR(RetValue,1,2) = 'D ') THEN
		RetValue := SUBSTR(RetValue,3);
	END IF;
	--
	RETURN TRIM(RetValue);
END FRIE_Name;
/
