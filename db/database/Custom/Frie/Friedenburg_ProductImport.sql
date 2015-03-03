/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CPM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Friedenburg_ProductImport.sql,v 1.6 2002/10/23 03:16:57 jjanke Exp $
 ***
 * Title:	Prepare Import File
 * Description:	
 ************************************************************************/
/**
DELETE I_061_Sync_Item
WHERE I_IsImported = 'Y';

UPDATE 	I_061_Sync_Item SET H_ItemDesc=Frie_Name(H_ItemDesc) WHERE H_ItemDesc <> Frie_Name(H_ItemDesc);
UPDATE 	I_061_Sync_Item SET H_Item=Frie_Value(H_ItemDesc) WHERE H_Item <> Frie_Value(H_ItemDesc);
COMMIT;

--	There should be none with same Value + Commodity No
SELECT * FROM I_061_Sync_Item
WHERE H_Item IN (SELECT H_Item FROM I_061_Sync_Item GROUP BY H_Item, H_Commodity1 HAVING Count(*) > 1)
ORDER BY H_Item;
--	FIX MANUALLY

-- Update H_ItemDescription with Vendor Number
UPDATE I_061_Sync_Item
SET H_ItemDesc = H_ItemDesc || ' ' || H_Commodity1
WHERE H_Item IN (SELECT H_Item FROM I_061_Sync_Item GROUP BY H_Item HAVING Count(*) > 1);
UPDATE 	I_061_Sync_Item SET H_Item=Frie_Value(H_ItemDesc) WHERE H_Item <> Frie_Value(H_ItemDesc);
COMMIT;

--	Should not return any rows	** Value is unique **
SELECT * FROM I_061_Sync_Item
WHERE H_Item IN (SELECT H_Item FROM I_061_Sync_Item GROUP BY H_Item HAVING Count(*) > 1)
ORDER BY H_Item;
--	FIX MANUALLY

--	Should not return any rows	** UPC is unique **
SELECT * FROM I_061_Sync_Item
WHERE H_UPC IN (SELECT H_UPC FROM I_061_Sync_Item GROUP BY H_UPC HAVING Count(*) > 1)
ORDER BY H_UPC;

--	Should not return any rows	** VendorNo is unique **
SELECT * FROM I_061_Sync_Item
WHERE H_Commodity1 IN (SELECT H_Commodity1 FROM I_061_Sync_Item GROUP BY H_Commodity1,H_PartnrID HAVING Count(*) > 1)
ORDER BY H_Commodity1;

--	Add Prefix to Key H_Item	--
UPDATE I_061_Sync_Item i
SET H_Item = (SELECT DECODE(p.C_BPartner_ID,
			1001881,	'BA',		-- 	Barum
			1001725,	'BR',		--	Bridgestone
			1001738,	'CO',		--	Conti
			1001853,	'SE',		--	Semperit
			1001755,	'FI',		--	Firestone
			1001759,	'FU',		--	Fulda
			1001763,	'GY',		--	Goodyear
			1001792,	'KE',		--	Kelly
			1001795,	'KL',		--	Kleber
			1001883,	'LE',		--	Lee
			1001812,	'ME',		--	Metzeler
			1001815,	'MI',		--	Michelin
			1001824,	'pi',		--	Pirelli Motorrad
			1001868,	'PI',		--	Pirelli
			1001744,	'DU',		--	Dunlop
			1001864,	'UN',		--	Uniroyal
			1001872,	'VR',		--	Vredestein
			1001877,	'YO',		--	Yokohama
			1003352, 	'SA', 		-- 	Goodyear Sava
			1001705, 	'ST', 		-- 	Alcar Stahlfelgen
			1003353, 	'AL', 		-- 	Alcar Alufelgen
						'..') || H_Item FROM C_BPartner p WHERE i.H_PartnrID=p.Value);
--
COMMIT;

--	Should not return results
SELECT H_Item, H_ItemDesc, H_PartnrID FROM I_061_Sync_Item WHERE SUBSTR(H_Item, 1, 2) = '..';

/**
 *	Import Products
 */

