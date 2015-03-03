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
 * $Id: MPC_Product_BOM.sql,v 1.3 2004/02/11 16:54:13 vpj-cd Exp $
 ***
 * Title:	Bill of Material
 * Description:
 ************************************************************************/
 
 ALTER TABLE M_Prduct ADD LowLevel NUMBER();

 ALTER TABLE AD_Workflow ADD DocumentNo NVARCHAR2(30);
 ALTER TABLE AD_Workflow ADD IsRouting  CHAR(1) DEFAULT('N');
 ALTER TABLE AD_Workflow ADD ProcessType CHAR(3);
 ALTER TABLE AD_Workflow ADD S_Resource_ID  NUMBER DEFAULT 0;
 
 
 
 
 ALTER TABLE AD_WF_Node ADD AD_WF_NodeStandard_ID NUMBER(10);
 ALTER TABLE AD_WF_Node ADD C_BPartner_ID NUMBER(10);
 ALTER TABLE AD_WF_Node ADD IsMilestone CHAR(1) DEFAULT ('N');
 ALTER TABLE AD_WF_Node ADD IsSubcontracting CHAR(1) DEFAULT ('N');
 ALTER TABLE AD_WF_Node ADD S_Resource_ID  NUMBER (10);|
 ALTER TABLE AD_WF_Node ADD UnitsCycles NUMBER DEFAULT 0;
 ALTER TABLE AD_WF_Node ADD ValidFrom DATE;
 ALTER TABLE AD_WF_Node ADD ValidTo DATE;
 ALTER TABLE AD_WF_Node MODIFY WorkingTime NUMBER;
 
  
 
  
  ALTER TABLE AD_WF_Node ADD Yield NUMBER DEFAULT 0
  
  ALTER TABLE AD_WF_NodeNext ADD MovingTime NUMBER DEFAULT 0;
  ALTER TABLE AD_WF_NodeNext ADD OverlapUnits NUMBER DEFAULT 0;
  ALTER TABLE AD_WF_NodeNext ADD QueuingTime NUMBER DEFAULT 0;

  
  ALTER TABLE S_Resource ADD IsManufacturingResource CHAR(1) DEFAULT ('N');
  ALTER TABLE S_Resource ADD DailyCapacity NUMBER;
  ALTER TABLE S_Resource ADD ManufacturingResourceType CHAR(2);
  ALTER TABLE S_Resource ADD PercentUtillization NUMBER;
  ALTER TABLE S_Resource ADD QueuingTime NUMBER;
  ALTER TABLE S_Resource ADD WaitingTime NUMBER;
  
 
  
 
 
