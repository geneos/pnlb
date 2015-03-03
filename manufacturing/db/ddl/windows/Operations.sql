/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2004 Jorg Janke, ComPiere, Inc. 
 *                         Colorado Correctional Industries 
 * All Rights Reserved.
 *************************************************************************
 * Title:	 Create the Operation Form
 * Description:
 *	Deletes and Restores the Operation Form
 *
 * Steps:	
 *	1. Create the Window and override default settings
 *  2. Create the Tabs and override default settings
 *     
 *****************************************************************************/

SET SERVEROUTPUT ON;

DECLARE

  v_EntityType   CHAR(1)     := 'A';  -- Application
  v_WindowName   VARCHAR(80) := 'Operation';
  v_Description  VARCHAR(80) := 'Maintain manufacturing operations';
  v_Help         VARCHAR(80) := 'Maintain manufacturing operations';

  v_AD_Window_ID NUMBER  := 0;
  v_AD_Tab_ID    NUMBER  := 0;
  v_Debug        NUMBER  := 0;  -- Debug 0 = Yes / 1 = No

BEGIN

	DBMS_OUTPUT.ENABLE(80000);
	/**	**/
	DBMS_OUTPUT.PUT_LINE('Building the Operations Window');

  -- ---------------------------------------------------------------------------
  -- Create the 'Operation' Window
  -- ---------------------------------------------------------------------------

  v_AD_Window_ID := CreateDefaultWindow (v_Debug,                 -- Debug
                                         v_EntityType,            -- Entity Type
                                         v_WindowName,            -- Window Name
                                         v_Description,           -- Description
                                         v_Help);                 -- Help

  -- ---------------------------------------------------------------------------
  -- Create the 'Operation' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_Operation',                -- Table Name
                                  'Operation',                    -- Tab Name
                                   0,                             -- Tab Level 0-...
                                   10,                            -- Sequence
                                  'Manufacturing Operation',      -- Description
                                  'Manufacturing Operation');     -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Operation -------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Operation';
  
  -- Product -------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 100,
         DISPLAYLENGTH = 11
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Product';

  -- ---------------------------------------------------------------------------
  -- Setup default window access
  -- ---------------------------------------------------------------------------

  @../../maintain/AD_Window_Access.sql

  COMMIT;
END;
/
