/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2004 Jorg Janke, ComPiere, Inc. 
 * Copyright (C) 2004 Victor Pérez, e-Evolution, S.C.
 * Copyright (C) 2004 Colorado Correctional Industries 
 * All Rights Reserved.
 *************************************************************************
 * Title:	 Create a Default Window Tab
 * Description:
 *	Creates a Window Tab with typical default values
 *
 *	Steps:
 *
 *  1. Get the Table and Window IDs
 *	2. Delete the old tab if it already exists
 *  3. Insert the Tab Record
 *  4. Create Tab Fields
 *  5. Setup Compiere field defaults
 *
 *****************************************************************************/

CREATE OR REPLACE FUNCTION CreateDefaultTab (v_Debug       NUMBER,
                                              v_EntityType  CHAR,
                                              v_WindowName  VARCHAR,
                                              v_TableName   VARCHAR,
                                              v_TabName     VARCHAR,
                                              v_TabLevel    NUMBER,
                                              v_Sequence    NUMBER,
                                              v_Description VARCHAR,
                                              v_Help        VARCHAR) 
  RETURN NUMBER IS

  CURSOR Cur_Column (Tab NUMBER, TableID NUMBER) IS
    SELECT Name, Description, AD_Column_ID, FieldLength, EntityType 
      FROM AD_Column c
		 WHERE NOT EXISTS (SELECT * FROM AD_Field f 
			                         WHERE c.AD_Column_ID=f.AD_Column_ID
			                           AND c.AD_Table_ID=TableID
			                           AND f.AD_Tab_ID=Tab) 
		                             AND AD_Table_ID=TableID
		                             AND NOT (UPPER(Name) LIKE 'CREATED%' 
                                      OR UPPER(Name) LIKE 'UPDATED%')
		                             AND IsActive='Y';

  v_TabExists     NUMBER := 0;
  v_NextNo        NUMBER;
	v_AD_Window_ID  NUMBER;
	v_AD_Table_ID   NUMBER;
	v_AD_Tab_ID     NUMBER;

BEGIN

  -- ---------------------------------------------------------------------------
  -- Display the input parameters
  -- ---------------------------------------------------------------------------

  IF v_Debug = 0 THEN
    DBMS_OUTPUT.PUT_LINE('Debug - Executing Default Tab with the following parameters');
    DBMS_OUTPUT.PUT_LINE('Debug - Tab Name...: ' || v_TabName);
    DBMS_OUTPUT.PUT_LINE('Debug - Description: ' || v_Description);
    DBMS_OUTPUT.PUT_LINE('Debug - Help.......: ' || v_Help);
    DBMS_OUTPUT.PUT_LINE('Debug - Table Name.: ' || v_TableName);
    DBMS_OUTPUT.PUT_LINE('Debug - Tab Level..: ' || v_TableName);
    DBMS_OUTPUT.PUT_LINE('Debug - Sequence...: ' || v_Sequence);
    DBMS_OUTPUT.PUT_LINE('Debug - Window Name: ' || v_WindowName);
    DBMS_OUTPUT.PUT_LINE('Debug - Entity Type: ' || v_EntityType);
  END IF;

  -- ---------------------------------------------------------------------------
  -- Get the Table and Window IDs
  -- ---------------------------------------------------------------------------

  SELECT AD_TABLE_ID INTO v_AD_Table_ID 
    FROM COMPIERE.AD_TABLE
   WHERE UPPER(NAME) = UPPER(v_TableName);
   
   IF v_Debug = 0 THEN
     DBMS_OUTPUT.PUT_LINE('Debug - v_AD_Table_ID: ' || v_AD_Table_ID);
   END IF;

  SELECT AD_WINDOW_ID INTO v_AD_Window_ID   
    FROM COMPIERE.AD_WINDOW 
   WHERE UPPER(NAME) = UPPER(v_WindowName);

   IF v_Debug = 0 THEN
     DBMS_OUTPUT.PUT_LINE('Debug - v_AD_Window_ID: ' || v_AD_Window_ID);
   END IF;

  -- ---------------------------------------------------------------------------
  -- Delete the old tab if it already exists
  -- ---------------------------------------------------------------------------

  SELECT COUNT(*) INTO v_TabExists
    FROM AD_TAB
   WHERE UPPER(NAME) = UPPER(v_TabName)
     AND AD_WINDOW_ID = v_AD_Window_ID;

   IF v_Debug = 0 THEN
     IF v_TabExists > 0 THEN 
       DBMS_OUTPUT.PUT_LINE('Debug - Tab exists...deleting');
     ELSE
       DBMS_OUTPUT.PUT_LINE('Debug - Tab does not exist...creating');
     END IF;
   END IF;

  IF v_TabExists > 0 THEN
    DELETE FROM AD_TAB   
     WHERE UPPER(NAME) = UPPER(v_TabName)
       AND AD_WINDOW_ID = v_AD_Window_ID;

    IF v_Debug = 0 THEN
       DBMS_OUTPUT.PUT_LINE('Debug - Tab deleted...creating new tab');
    END IF;
   END IF;

  -- ---------------------------------------------------------------------------
  -- Insert the Tab Record
  -- ---------------------------------------------------------------------------

  AD_Sequence_Next('AD_Tab', 0, v_NextNo);

  INSERT INTO COMPIERE.AD_TAB (
                       AD_TAB_ID, 
                       AD_CLIENT_ID, 
                       AD_ORG_ID,
                       ISACTIVE, 
                       CREATED, 
                       CREATEDBY, 
                       UPDATED, 
                       UPDATEDBY, 
                       NAME, 
                       DESCRIPTION, 
                       HELP, 
                       AD_TABLE_ID, 
                       AD_WINDOW_ID, 
                       SEQNO, 
                       TABLEVEL, 
                       ISSINGLEROW, 
                       ISINFOTAB, 
                       ISTRANSLATIONTAB, 
                       ISREADONLY, 
                       AD_COLUMN_ID, 
                       HASTREE, 
                       WHERECLAUSE, 
                       ORDERBYCLAUSE, 
                       COMMITWARNING, 
                       AD_PROCESS_ID, 
                       PROCESSING, 
                       AD_IMAGE_ID, 
                       IMPORTFIELDS, 
                       AD_COLUMNSORTORDER_ID, 
                       AD_COLUMNSORTYESNO_ID, 
                       ISSORTTAB, 
                       ENTITYTYPE, 
                       INCLUDED_TAB_ID)
               VALUES (v_NextNo,                           -- AD_Tab_ID
                       0,                                  -- AD_CLIENT_ID
                       0,                                  -- AD_ORG_ID
                       'Y',                                -- ISACTIVE
                       SysDate,                            -- CREATED
                       0,                                  -- CREATEDBY
                       SysDate,                            -- UPDATED
                       0,                                  -- UPDATEDBY
                       v_TabName,                          -- NAME
                       v_Description,                      -- DESCRIPTION
                       v_Help,                             -- HELP
                       v_AD_Table_ID,                      -- AD_Table_ID
                       v_AD_Window_ID,                     -- AD_Window_ID
                       v_Sequence,                         -- SEQNO
                       v_TabLevel,                         -- TABLEVEL
                       'Y',                                -- ISSINGLEROW
                       'N',                                -- ISINFOTAB 
                       'N',                                -- ISTRANSLATIONTAB 
                       'N',                                -- ISREADONLY 
                       null,                               -- AD_COLUMN_ID
                       'N',                                -- HASTREE
                       null,                               -- WHERECLAUSE
                       null,                               -- ORDERBYCLAUSE
                       null,                               -- COMMITWARNING
                       null,                               -- AD_PROCESS_ID
                       'N',                                -- PROCESSING
                       null,                               -- AD_IMAGE_ID
                       null,                               -- IMPORTFIELDS
                       null,                               -- AD_COLUMNSORTORDER_ID
                       null,                               -- AD_COLUMNSORTYESNO_ID
                       'N',                                -- ISSORTTAB
                       v_EntityType,                       -- ENTITYTYPE
                       null);                              -- INCLUDED_TAB_ID

  -- ---------------------------------------------------------------------------
  -- Create Tab Fields
  -- ---------------------------------------------------------------------------

  IF v_Debug = 0 THEN
    DBMS_OUTPUT.PUT_LINE('Debug - Inserted tab ' || v_TabName || '...creating tab fields');
  END IF;

  SELECT AD_TAB_ID INTO v_AD_Tab_ID   
    FROM AD_TAB   
   WHERE UPPER(NAME) = UPPER(v_TabName)
     AND AD_WINDOW_ID = v_AD_Window_ID;

  IF v_Debug = 0 THEN
    DBMS_OUTPUT.PUT_LINE('Debug - AD_Tab_ID =' || v_AD_Tab_ID);
  END IF;

	FOR CC IN Cur_Column (v_AD_Tab_ID, v_AD_Table_ID) LOOP
		AD_Sequence_Next('AD_Field', 0, v_NextNo);	--	get ID
		INSERT INTO AD_Field
			(ad_field_id, ad_client_id, ad_org_id,
			isactive, created, createdby, updated, updatedby,
			name, description, 
			seqno, AD_Tab_ID, AD_Column_ID, DisplayLength, 
			IsCentrallyMaintained, EntityType)
		VALUES
			(v_NextNo, 0, 0,
			'Y', SysDate, 0, SysDate, 0,
			CC.Name, CC.Description,
			0, v_AD_Tab_ID, CC.AD_Column_ID, CC.FieldLength, 
			'Y', CC.EntityType);

    IF v_Debug = 0 THEN
      DBMS_OUTPUT.PUT_LINE('Debug - Adding field: ' || CC.Name);
    END IF;

	END LOOP;   --  for all columns

  -- ---------------------------------------------------------------------------
  -- Setup Compiere field defaults
  -- ---------------------------------------------------------------------------

  -- Client --------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 10
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Client';

  -- Organization --------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 20,
         ISSAMELINE = 'Y'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Organization';

  -- Search Key ----------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 30,
         DISPLAYLENGTH = 11
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Search Key';

  -- Name ----------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 40
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Name';

  -- Description ---------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 50
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Description';

  -- Active --------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 60,
         DISPLAYLENGTH = 11
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Active';

  -- ---------------------------------------------------------------------------
  -- Return AD_Tab_ID
  -- ---------------------------------------------------------------------------

  RETURN v_AD_Tab_ID;

  -- ---------------------------------------------------------------------------
  -- Exception Handling
  -- ---------------------------------------------------------------------------

EXCEPTION

  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('Error: No Data Found Exception in DefaultTab.');

END CreateDefaultTab;
/
