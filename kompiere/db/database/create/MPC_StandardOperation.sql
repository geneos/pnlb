/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 2003-2003 Victor Pérez, e-Evolution S.C. All Rights Reserved.
 *************************************************************************
 * $Id: MP_StandardOperation;.sql,v 1 2002/10/21 05:49:46 vpj-cd Exp $
 ***
 * Title:	Standard Operation
 * Description:
 ************************************************************************/
DROP TABLE MPC_StandardOperation CASCADE CONSTRAINTS;
CREATE TABLE MPC_StandardOperation
(
    AD_Client_ID                     NUMBER   (10)                   NOT NULL
  , AD_Org_ID                        NUMBER   (10)                   NOT NULL
  , A_Asset_ID                       NUMBER   (10)                   
  , BatchQty                         NUMBER                          DEFAULT 0 NOT NULL
  , C_BPartner_ID                    NUMBER   (10)                   
  , CreatedBy                        NUMBER   (10)                   NOT NULL
  , CyclesHour                       NUMBER                          
  , Description                      NVARCHAR2 (510)                                    
  , IsActive                         CHAR     (1)                    DEFAULT ('Y') NOT NULL
  , MPC_StandardOperation_ID         NUMBER   (10)                   NOT NULL
  , S_Resource_ID           	     NUMBER   (10)                   NOT NULL
  , IsMilestone                      CHAR     (1)                    DEFAULT ('N') NOT NULL 
  , MoveTime                         NUMBER                 
  , Name                             VARCHAR2 (120)                  NOT NULL              
  , NumberAsset                      NUMBER                          
  , NumberPeople                     NUMBER                          
  , OverlapUnits                     NUMBER                          
  , PercentEfficiency                NUMBER                          
  , QueueTime                        NUMBER                                           
  , RunCrew                          NUMBER                          
  , RunTime                          NUMBER                          
  , SetupCrew                        NUMBER                          
  , SetupTime                        NUMBER                          
  , Type                             CHAR (1)                    
  , UnitsCycles                      NUMBER                          
  , Updated                          DATE                            DEFAULT SYSDATE NOT NULL
  , UpdatedBy                        NUMBER   (10)                   NOT NULL
  , WaitTime                         NUMBER                          
  , Created                          DATE                            DEFAULT SYSDATE NOT NULL
  , IsSubContracting                 CHAR     (1)                    DEFAULT ('N') NOT NULL
  , Value                            VARCHAR2 (80)                   NOT NULL  
  , ValidFrom                        DATE                            NOT NULL
  , ValidTo                          DATE                            NULL
  , CHECK (IsActive in ('Y','N'))
  , CHECK (IsMilestone in ('Y','N'))
  , CHECK (IsSubContracting in ('Y','N')) 
  , CONSTRAINT MPC_StandardOperation_Key PRIMARY KEY (MPC_StandardOperation_ID)
);

-- 
-- TABLE: MP_StandardOperation
--
ALTER TABLE MPC_StandardOperation ADD CONSTRAINT ADOrgMPCStandadrOp
    FOREIGN KEY (AD_Org_ID)
    REFERENCES AD_Org(AD_Org_ID)
;

ALTER TABLE MPC_StandardOperation ADD CONSTRAINT ADClientMPCStandardOp
    FOREIGN KEY (AD_Client_ID)
    REFERENCES AD_Client(AD_Client_ID)
;

ALTER TABLE MPC_StandardOperation ADD CONSTRAINT AAssetMPCStandardOp
    FOREIGN KEY (A_Asset_ID)
    REFERENCES A_Asset(A_Asset_ID)
;
ALTER TABLE MPC_StandardOperation ADD CONSTRAINT CBPartnerMPCStandardOp
    FOREIGN KEY (C_BPartner_ID)
    REFERENCES C_BPartner(C_BPartner_ID)
;
ALTER TABLE MPC_StandardOperation ADD CONSTRAINT SResourceMPCStandardOp
    FOREIGN KEY (S_Resource_ID)
    REFERENCES C_BPartner(S_Resource_ID)
;




