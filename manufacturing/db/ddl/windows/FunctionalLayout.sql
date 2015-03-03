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
 *	Deletes and Restores the 'Functional Layout' Form
 *
 * Steps:	
 *	1. Create the Window and override default settings
 *  2. Create the Tabs and override default settings
 *     
 *****************************************************************************/

SET SERVEROUTPUT ON;

DECLARE

  v_EntityType   CHAR(1)     := 'A';  -- Application
  v_WindowName   VARCHAR(80) := 'Functional Layout';
  v_Description  VARCHAR(80) := 'Manufacturing Functional Layout';
  v_Help         VARCHAR(80) := 'Manufacturing Functional Layout';

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
  -- Create the 'Factory' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_Facility',                 -- Table Name
                                  'Facility',                     -- Tab Name
                                   0,                             -- Tab Level 0-...
                                   10,                            -- Sequence
                                  'Manufacturing Factory/Plant',  -- Description
                                  'Manufacturing Factory/Plant'); -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Facility ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Facility';

  -- Resource ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Resource';
  
  -- Location / Address --------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD
     SET SEQNO = 100,
    ISSAMELINE = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Location / Address';

  -- ---------------------------------------------------------------------------
  -- Create the 'Work Cell' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_WorkCell',                 -- Table Name
                                  'Work Cell',                    -- Tab Name
                                   1,                             -- Tab Level 0-...
                                   20,                            -- Sequence
                                  'Manufacturing Work Cell/Production Line',  -- Description
                                  'Manufacturing Work Cell/Production Line'); -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Work Cell -----------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Work Cell';

  -- Facility ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Facility';

  -- Resource ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Resource';
  
  -- ---------------------------------------------------------------------------
  -- Create the 'Work Center' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_WorkCenter',               -- Table Name
                                  'Work Center',                  -- Tab Name
                                   2,                             -- Tab Level 0-...
                                   30,                            -- Sequence
                                  'Manufacturing Work Center',    -- Description
                                  'Manufacturing Work Center');   -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Work Center ---------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Work Center';

  -- Work Cell -----------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Work Cell';

  -- Resource ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Resource';
  
  -- ---------------------------------------------------------------------------
  -- Create the 'Workstation' Tab
  -- ---------------------------------------------------------------------------

  v_AD_Tab_ID := CreateDefaultTab (v_Debug,                       -- Debug
                                   v_EntityType,                  -- Entity Type
                                   v_WindowName,                  -- Window Name
                                  'MPC_Workstation',              -- Table Name
                                  'Workstation',                  -- Tab Name
                                   3,                             -- Tab Level 0-...
                                   40,                            -- Sequence
                                  'Manufacturing Workstation',    -- Description
                                  'Manufacturing Workstation');   -- Help

  -- ---------------------------------------------------------------------------
  -- Override Defaults
  -- ---------------------------------------------------------------------------

	DBMS_OUTPUT.PUT_LINE('Overriding Tab Defaults. AD_Tab_ID = ' || v_AD_Tab_ID);

  -- Workstation ---------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Workstation';

  -- Work Center ---------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD 
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Work Center';

  -- Resource ------------------------------------------------------------------

  UPDATE COMPIERE.AD_FIELD
     SET SEQNO = 0,
         ISDISPLAYED = 'N'
   WHERE AD_TAB_ID  = v_AD_Tab_ID
     AND NAME = 'Resource';

  -- ---------------------------------------------------------------------------
  -- Setup default window access
  -- ---------------------------------------------------------------------------

  @../../maintain/AD_Window_Access.sql


  COMMIT;
END;
/
