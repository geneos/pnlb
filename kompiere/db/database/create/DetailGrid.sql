--------------------------------------------------------------------------------
---   ALTER TABLE ... ADD ... File for Database : e-Evolution Kompiere (Build 228)
---
---   Date of creation: 2005-08-25 03:29:36
--------------------------------------------------------------------------------

--- Table: I_Shipper -----------------------------------------------------------

--- Table: AD_Field ------------------------------------------------------------

ALTER TABLE AD_Field 
ADD Included_Tab_ID NUMERIC(10,0);

ALTER TABLE AD_Field 
MODIFY Included_Tab_ID NUMERIC(10,0);


--- Table: AD_FieldGroup -------------------------------------------------------

ALTER TABLE AD_FieldGroup 
ADD IsTab CHAR(1) DEFAULT 'N' NOT NULL CHECK (IsTab IN ('Y', 'N'));

ALTER TABLE AD_FieldGroup 
MODIFY IsTab CHAR(1) DEFAULT 'N' NOT NULL CHECK (IsTab IN ('Y', 'N'));


--------------------------------------------------------------------------------
