CREATE OR REPLACE FUNCTION FRIE_Value
(
	ValueName			IN	VARCHAR2
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
 * $Id: FRIE_Value.sql,v 1.3 2002/10/21 04:49:45 jjanke Exp $
 ***
 * Title:	Return "clean" Product Value
 * Description:	
 ************************************************************************/

 	RetValue			VARCHAR2(2000);
	AddOn				VARCHAR2(20) := '';
BEGIN
	RetValue := ValueName;

	--	Delete ' R ', ' B ', '-', '.', '/'
	RetValue := REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(RetValue,' R ',''),' B ',''), '-',''), '.',''), '/','');
	RetValue := REPLACE(RetValue, ',','');
	--	Delete ' HR ', ' VR ', ' ZR '
	IF (INSTR(RetValue, ' HR ') <> 0) THEN
		RetValue := REPLACE(RetValue, ' HR ', '');
		AddOn := 'H';
	END IF;
	IF (INSTR(RetValue, ' SR ') <> 0) THEN
		RetValue := REPLACE(RetValue, ' SR ', '');
		AddOn := 'S';
	END IF;
	IF (INSTR(RetValue, ' VR ') <> 0) THEN
		RetValue := REPLACE(RetValue, ' VR ', '');
		AddOn := 'V';
	END IF;
	IF (INSTR(RetValue, ' ZR ') <> 0) THEN
		RetValue := REPLACE(RetValue, ' ZR ', '');
		AddOn := 'Z';
	END IF;
	--	Clean spaces
	RetValue := REPLACE(RetValue, ' ', '');
	--
	RETURN TRIM(RetValue) || AddOn;
END FRIE_Value;
/
