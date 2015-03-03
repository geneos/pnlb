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
 * Title:	Default Window
 * Description:
 *	Creates a window with typical default settings
 *
 *	Steps:
 *
 *  1. Delete the old window if it already exists
 *	2. Create the main window
 *
 *****************************************************************************/

CREATE OR REPLACE FUNCTION CreateDefaultWindow (v_Debug       NUMBER,
                                                v_EntityType  CHAR,
                                                v_WindowName  VARCHAR,
                                                v_Description VARCHAR,
                                                v_Help        VARCHAR) 
  RETURN NUMBER IS

  v_MenuExists    NUMBER := 0;
  v_WindowExists  NUMBER := 0;
  v_AD_Window_ID  NUMBER := 0;
  v_AD_Table_ID   NUMBER := 0;
  v_AD_Menu_ID    NUMBER := 0;

BEGIN

  -- ---------------------------------------------------------------------------
  -- Display the input parameters
  -- ---------------------------------------------------------------------------

  IF v_Debug = 0 THEN
    DBMS_OUTPUT.PUT_LINE('Debug - Executing Default Window with the following parameters');
    DBMS_OUTPUT.PUT_LINE('Debug - Window Name: ' || v_WindowName);
    DBMS_OUTPUT.PUT_LINE('Debug - Description: ' || v_Description);
    DBMS_OUTPUT.PUT_LINE('Debug - Help.......: ' || v_Help);
    DBMS_OUTPUT.PUT_LINE('Debug - Entity Type: ' || v_EntityType);
  END IF;

  -- -------------------------------------------------------------------------
  -- Save the menu item if one exists - work around for database constraint.
  -- -------------------------------------------------------------------------

  SELECT COUNT(*) INTO v_MenuExists
    FROM AD_MENU
   WHERE UPPER(NAME) = UPPER(v_WindowName)
     AND AD_Window_ID = (SELECT AD_WINDOW_ID FROM AD_WINDOW WHERE NAME = v_WindowName);

   IF v_Debug = 0 THEN
     IF v_MenuExists > 0 THEN 
       DBMS_OUTPUT.PUT_LINE('Debug - Menu exists...saving id');
     ELSE
       DBMS_OUTPUT.PUT_LINE('Debug - Menu does not exist - create pending.');
     END IF;
   END IF;

  IF v_MenuExists > 0 THEN
    SELECT AD_Menu_ID INTO v_AD_Menu_ID
      FROM AD_MENU
     WHERE UPPER(NAME) = UPPER(v_WindowName)
       AND AD_Window_ID = (SELECT AD_WINDOW_ID FROM AD_WINDOW WHERE NAME = v_WindowName);

    UPDATE AD_MENU SET AD_Window_ID = null
     WHERE AD_Menu_ID = v_AD_Menu_ID;

    IF v_Debug = 0 THEN
      DBMS_OUTPUT.PUT_LINE('Debug - Menu Id: ' || v_AD_Menu_ID || ' set to null');
    END IF;
  END IF;

  -- ---------------------------------------------------------------------------
  -- Delete the old window if it already exists
  -- ---------------------------------------------------------------------------

  SELECT COUNT(*) INTO v_WindowExists
    FROM AD_WINDOW
   WHERE UPPER(NAME) = UPPER(v_WindowName);

   IF v_Debug = 0 THEN
     IF v_WindowExists > 0 THEN 
       DBMS_OUTPUT.PUT_LINE('Debug - Window exists...deleting');
     ELSE
       DBMS_OUTPUT.PUT_LINE('Debug - Window does not exist...creating');
     END IF;
   END IF;

  IF v_WindowExists > 0 THEN
    DELETE FROM AD_WINDOW   
     WHERE UPPER(NAME) = UPPER(v_WindowName);

    IF v_Debug = 0 THEN
       DBMS_OUTPUT.PUT_LINE('Debug - Window deleted...creating new window');
    END IF;
   END IF;

-- -----------------------------------------------------------------------------
-- Create the main window
-- -----------------------------------------------------------------------------

  AD_Sequence_Next('AD_Window', 0, v_AD_Window_ID);

  INSERT INTO COMPIERE.AD_WINDOW (AD_WINDOW_ID,
                      AD_CLIENT_ID,
                      AD_COLOR_ID,
                      AD_IMAGE_ID,
                      AD_ORG_ID,
                      CREATED,
                      CREATEDBY,
                      DESCRIPTION,
                      ENTITYTYPE,
                      HELP,
                      ISACTIVE,
                      ISDEFAULT,
                      ISSOTRX,
                      NAME,
                      PROCESSING,
                      UPDATED,
                      UPDATEDBY,
                      WINDOWTYPE)
              VALUES (v_AD_Window_ID,   -- AD_Window_ID
                      0,                -- AD_CLIENT_ID
                      null,             -- AD_COLOR_ID
                      null,             -- AD_IMAGE_ID
                      0,                -- AD_ORG_ID
                      SysDate,          -- CREATED
                      0,                -- CREATEDBY
                      v_description,    -- DESCRIPTION
                      'A',              -- ENTITYTYPE
                      v_help,           -- HELP
                      'Y',              -- ISACTIVE
                      'N',              -- ISDEFAULT
                      'N',              -- ISSOTRX
                      v_WindowName,     -- NAME
                      'N',              -- PROCESSING
                      SysDate,          -- UPDATED
                      0,                -- UPDATEDBY
                      'M');             -- WINDOWTYPE

  -- ---------------------------------------------------------------------------
  -- Create or update the menu item.
  -- ---------------------------------------------------------------------------

  IF v_MenuExists > 0 THEN
    DBMS_OUTPUT.PUT_LINE('Debug - Updating menu with AD_Window_ID: ' || v_AD_Window_ID  || ' Menu ID: ' || v_AD_Menu_ID);

    UPDATE AD_MENU SET AD_Window_ID = v_AD_Window_ID
      WHERE AD_Menu_ID = v_AD_Menu_ID;

    DBMS_OUTPUT.PUT_LINE('Debug - Menu updated');
  ELSE
    DBMS_OUTPUT.PUT_LINE('Debug - Creating new menu with AD_Window_ID: ' || v_AD_Window_ID);

    AD_Sequence_Next('AD_Menu', 0, v_AD_Menu_ID);

    INSERT INTO COMPIERE.AD_MENU (AD_MENU_ID,
                                  ACTION,
                                  AD_CLIENT_ID,
                                  AD_FORM_ID,
                                  AD_ORG_ID,
                                  AD_PROCESS_ID,
                                  AD_TASK_ID,
                                  AD_WINDOW_ID,
                                  AD_WORKBENCH_ID,
                                  AD_WORKFLOW_ID,
                                  CREATED,
                                  CREATEDBY,
                                  DESCRIPTION,
                                  ENTITYTYPE,
                                  ISACTIVE,
                                  ISREADONLY,
                                  ISSOTRX,
                                  ISSUMMARY,
                                  NAME,
                                  UPDATED,
                                  UPDATEDBY)
                          VALUES (v_AD_Menu_ID,     -- AD_Menu_ID
                                  'W',              -- ACTION / Window
                                  0,                -- AD_Client_ID
                                  null,             -- AD_FORM_ID
                                  0,                -- AD_ORG_ID
                                  null,             -- AD_PROCESS_ID
                                  null,             -- AD_TASK_ID
                                  v_AD_Window_ID,   -- AD_WINDOW_ID
                                  null,             -- AD_WORKBENCH_ID
                                  null,             -- AD_WORKFLOW_ID,
                                  SysDate,          -- CREATED
                                  0,                -- CREATEDBY
                                  v_Description,    -- DESCRIPTION,
                                  v_EntityType,     -- ENTITYTYPE
                                  'Y',              -- ISACTIVE
                                  'N',              -- ISREADONLY
                                  'N',              -- ISSOTRX
                                  'N',              -- ISSUMMARY
                                  v_WindowName,     -- NAME
                                  SysDate,
                                  0);

    DBMS_OUTPUT.PUT_LINE('Debug - Menu created');

  END IF;

  -- ---------------------------------------------------------------------------
  -- Return AD_Window_ID
  -- ---------------------------------------------------------------------------

  RETURN v_AD_Window_ID;

  -- ---------------------------------------------------------------------------
  -- Exception Handling
  -- ---------------------------------------------------------------------------

EXCEPTION

  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('Error: No Data Found Exception in DefaultWindow.');

END CreateDefaultWindow;
/