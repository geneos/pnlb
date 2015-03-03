--------------------------------------------------------------------------------
---   ALTER TABLE ... ADD ... File for Database : PostgreSQL db (Build 237)
---
---   Date of creation: 2005-09-04 21:06:36
--------------------------------------------------------------------------------

--- Table: AD_FieldGroup -------------------------------------------------------

ALTER TABLE AD_FieldGroup 
ADD IsTab CHAR(1) not null default 'N' CHECK (IsTab IN ('Y', 'N'));

ALTER TABLE AD_FieldGroup 
MODIFY IsTab CHAR(1) not null default 'N' CHECK (IsTab IN ('Y', 'N'));


--- Table: AD_Field ------------------------------------------------------------

ALTER TABLE AD_Field 
ADD Included_Tab_ID decimal(10,0);

ALTER TABLE AD_Field 
MODIFY Included_Tab_ID decimal(10,0);


--------------------------------------------------------------------------------
