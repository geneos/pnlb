/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2004 Jorg Janke, ComPiere, Inc. 
 * Copyright (C) 2004 Victor Pérez, e-Evolution, S.C.
 * All Rights Reserved.
 * Contributor(s): Victor Pérez, e-Evolution, S.C.
 *************************************************************************
 * $Id: MPC_Order_Plan.sql,v 1.1 2004/02/11 19:06:51 vpj-cd Exp $
 ***
 * Title:	MPC_Order_Plan
 * Description:
 ************************************************************************/
DROP TABLE MPC_Schedule CASCADE CONSTRAINTS;
CREATE TABLE MPC_Schedule  
(
    MPC_Schedule_ID          	     NUMBER   (10)                   NOT NULL
  , AD_Client_ID                     NUMBER   (10)                   NOT NULL
  , AD_Org_ID                        NUMBER   (10)                   NOT NULL
  , ScheduleType                     CHAR     (3)                    NOT NULL
  , IsActive                         CHAR     (1)                    DEFAULT 'Y' NOT NULL
  , Created                          DATE                            DEFAULT SYSDATE NOT NULL
  , CreatedBy                        NUMBER   (10)                   NOT NULL
  , Updated                          DATE                            DEFAULT SYSDATE NOT NULL
  , UpdatedBy                        NUMBER   (10)                   NOT NULL
  , IsSOTrx                          CHAR     (1)                    DEFAULT 'Y' NOT NULL
  , DocumentNo                       NVARCHAR2(60)                   NOT NULL                 
  , S_Resource_ID                    NUMBER   (10)		     NOT NULL 
  , M_Warehouse_ID                   NUMBER   (10)                   NOT NULL
  , IsApproved                       CHAR     (1)                    DEFAULT 'Y' NOT NULL
  , IsPrinted                        CHAR     (1)                    DEFAULT 'N' NOT NULL
  , IsSelected                       CHAR     (1)                    DEFAULT 'N' NOT NULL
  , Planner_ID                       NUMBER   (10) 		     NOT NULL	
  , DateStartShedule                 DATE                            DEFAULT SYSDATE NOT NULL
  , DateFinishShedule                DATE                            DEFAULT SYSDATE NOT NULL
  , DateStart                        DATE                            DEFAULT SYSDATE NOT NULL
  , DateFinish                       DATE                            DEFAULT SYSDATE NOT NULL
  , CopyFrom                         CHAR     (1)                           
  , Name                             NVARCHAR2(120)                  NOT NULL
  , Description                      NVARCHAR2(510)                  
  , IsCreated                        CHAR     (1)                    DEFAULT 'N' NOT NULL
  , Processed                        CHAR     (1)                    DEFAULT 'N' NOT NULL
  , Processing                       CHAR     (1)                    
  , AD_OrgTrx_ID                     NUMBER   (10)                   
  , C_Project_ID                     NUMBER   (10)                   
  , C_Campaign_ID                    NUMBER   (10)                   
  , C_Activity_ID                    NUMBER   (10)                   
  , User1_ID                         NUMBER   (10)                   
  , User2_ID                         NUMBER   (10)        
  , CHECK (IsActive in ('Y','N'))
  , CONSTRAINT MPC_Schedule_Key PRIMARY KEY (MPC_Schedule_ID)           
);
--
-- MPC_Schedule
--
ALTER TABLE MPC_Schedule ADD CONSTRAINT ADClientMPCSchedule
    FOREIGN KEY (AD_Client_ID)
    REFERENCES AD_Client(AD_Client_ID)
;

ALTER TABLE MPC_Schedule ADD CONSTRAINT ADOrgMPCSchedule
    FOREIGN KEY (AD_Org_ID)
    REFERENCES AD_Org(AD_Org_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT SResourceMPCSchedule
    FOREIGN KEY (S_Resource_ID)
    REFERENCES S_Resource(S_Resource_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT MWarehouseMPCSchedule
    FOREIGN KEY (M_Warehouse_ID)
    REFERENCES M_Warehouse(M_Warehouse_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT Planner_IDMPCSchedule
    FOREIGN KEY (Planner_ID)
    REFERENCES AD_User(AD_User_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT ADOrgTrxMPCSchedule
    FOREIGN KEY (AD_OrgTrx_ID)
    REFERENCES AD_Org(AD_Org_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT AD_OrgMPCSchedule
    FOREIGN KEY (C_Project_ID)
    REFERENCES C_Project(C_Project_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT CCampaignMPCSchedule
    FOREIGN KEY (C_Campaign_ID)
    REFERENCES C_Campaign(C_Campaign_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT CActivityMPCSchedule
    FOREIGN KEY (C_Activity_ID)
    REFERENCES C_Activity(C_Activity_ID)
;
ALTER TABLE MPC_Schedule ADD CONSTRAINT CElementValue1MPCSchedule 
    FOREIGN KEY (User2_ID)
    REFERENCES C_ElementValue(C_ElementValue_ID)
;

ALTER TABLE MPC_Schedule ADD CONSTRAINT CElementValue2MPCSchedule 
    FOREIGN KEY (User1_ID)
    REFERENCES C_ElementValue(C_ElementValue_ID)
; 
   
 



