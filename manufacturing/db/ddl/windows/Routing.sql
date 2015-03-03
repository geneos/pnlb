/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2004 Jorg Janke, ComPiere, Inc. 
 *                         Colorado Correctional Industries 
 * All Rights Reserved.
 *************************************************************************
 * Title:	 Create the Routing Form
 * Description:
 *	Deletes and Restores the Routing Form
 *
 * Steps:	
 *	1. Create the Window and override default settings
 *  2. Create the Tabs and override default settings
 *     
 *****************************************************************************/

SET SERVEROUTPUT ON;

DECLARE

  v_EntityType   CHAR(1)     := 'A';  -- Application
  v_WindowName   VARCHAR(80) := 'Routing';
  v_Description  VARCHAR(80) := 'Manufacturing Routing';
  v_Help         VARCHAR(80) := 'Manufacturing Routing';

  v_AD_Window_ID NUMBER  := 0;
  v_AD_Tab_ID    NUMBER  := 0;
  v_Debug        NUMBER  := 0;  -- Debug 0 = Yes / 1 = No

BEGIN

	DBMS_OUTPUT.ENABLE(80000);
	/**	**/
	DBMS_OUTPUT.PUT_LINE('Building the Functional Layout Window');

  -- ---------------------------------------------------------------------------
  -- Create the 'Routing' Window
  -- ---------------------------------------------------------------------------

  v_AD_Window_ID := CreateDefaultWindow (v_Debug,                 -- Debug
                                         v_EntityType,            -- Entity Type
                                         v_WindowName,            -- Window Name
                                         v_Description,           -- Description
                                         v_Help);                 -- Help

  -- ---------------------------------------------------------------------------
  -- Create the 'Routing' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_Routing',                  -- Table Name
                                  'Routing',                      -- Tab Name
                                   0,                             -- Tab Level 0-...
                                   10,                            -- Sequence
                                  'Manufacturing Routing',        -- Description
                                  'Manufacturing Routing');       -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Routing -------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Routing';

  -- Product -------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 100,
         DISPLAYLENGTH = 11
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Product';

  -- Default Routing -----------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 110,
    ISSAMELINE = 'Y'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Default Routing';
  
  -- Warehouse / Service Point -------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 120
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Warehouse / Service Point';

  -- ---------------------------------------------------------------------------
  -- Create the 'Sequence' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_Sequence',                 -- Table Name
                                  'Sequence',                     -- Tab Name
                                   1,                             -- Tab Level 0-...
                                   20,                            -- Sequence
                                  'Routing Sequence',             -- Description
                                  'Routing Sequence');            -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Sequence -------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Sequence';

  -- Routing -------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Routing';

  -- Sequence ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 100,
    ISSAMELINE = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'MfgSequence';
  
  -- Operation -----------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 110,
    ISSAMELINE = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Operation';
  
  -- Work Center ---------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 120,
    ISSAMELINE = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Work Center';
  
  -- ---------------------------------------------------------------------------
  -- Setup default window access
  -- ---------------------------------------------------------------------------

  @../../maintain/AD_Window_Access.sql


  COMMIT;
END;
/
