REM ======================================================================
REM ===   Sql Script for Database : e-Evolution Kompiere
REM ===
REM === Build : 235
REM ======================================================================

CREATE TABLE AD_FieldGroup
  (
    IsTab  CHAR(1)   DEFAULT 'N' NOT NULL  Yes-No  IsTab,
    CHECK(IsTab IN ('Y', 'N'))
  );

REM ======================================================================

CREATE TABLE AD_Field
  (
    Included_Tab_ID  NUMBER(10,0)    Table  AD_Tab  Included_Tab_ID
  );

REM ======================================================================

-- 
-- VIEW: AD_Field_v 
--
DROP VIEW AD_Field_v;
CREATE OR REPLACE VIEW AD_Field_v 
AS
SELECT t.AD_Window_ID, f.AD_Tab_ID, f.AD_Field_ID, tbl.AD_Table_ID, f.AD_Column_ID, 
	f.Name, f.Description, f.Help, f.IsDisplayed, f.DisplayLogic, f.DisplayLength, 
	f.SeqNo, f.SortNo, f.IsSameLine, f.IsHeading, f.IsFieldOnly, f.IsReadOnly, 
	f.IsEncrypted AS IsEncryptedField, f.ObscureType,
	c.ColumnName, c.ColumnSQL, c.FieldLength, c.VFormat, c.DefaultValue, c.IsKey, c.IsParent, 
	c.IsMandatory, c.IsIdentifier, c.IsTranslated, c.AD_Reference_Value_ID, 
	c.Callout, c.AD_Reference_ID, c.AD_Val_Rule_ID, c.AD_Process_ID, c.IsAlwaysUpdateable,
	c.ReadOnlyLogic, c.IsUpdateable, c.IsEncrypted AS IsEncryptedColumn, c.IsSelectionColumn,
	tbl.TableName, c.ValueMin, c.ValueMax, 
	fg.Name AS FieldGroup, vr.Code AS ValidationCode,
--begin vpj-cd e-evolution 08/09/2005
        f.Included_Tab_ID
--end vpj-cd  e-evolution 08/09/2005
FROM AD_Field f 
  INNER JOIN AD_Tab t ON (f.AD_Tab_ID = t.AD_Tab_ID)
  LEFT OUTER JOIN AD_FieldGroup fg ON (f.AD_FieldGroup_ID = fg.AD_FieldGroup_ID) 
  LEFT OUTER JOIN AD_Column c ON (f.AD_Column_ID = c.AD_Column_ID)
	INNER JOIN AD_Table tbl ON (c.AD_Table_ID = tbl.AD_Table_ID)
	INNER JOIN AD_Reference r ON (c.AD_Reference_ID = r.AD_Reference_ID)
	LEFT OUTER JOIN AD_Val_Rule vr ON (c.AD_Val_Rule_ID=vr.AD_Val_Rule_ID)
WHERE f.IsActive = 'Y' 
  AND c.IsActive = 'Y';

REM ======================================================================

-- 
-- VIEW: AD_Field_vt 
--
CREATE OR REPLACE VIEW AD_Field_vt 
AS
SELECT trl.AD_Language, t.AD_Window_ID, f.AD_Tab_ID, f.AD_Field_ID, tbl.AD_Table_ID, f.AD_Column_ID, 
	trl.Name, trl.Description, trl.Help, f.IsDisplayed, f.DisplayLogic, f.DisplayLength, 
	f.SeqNo, f.SortNo, f.IsSameLine, f.IsHeading, f.IsFieldOnly, f.IsReadOnly, 
	f.IsEncrypted AS IsEncryptedField, f.ObscureType,
	c.ColumnName, c.ColumnSQL, c.FieldLength, c.VFormat, c.DefaultValue, c.IsKey, c.IsParent, 
	c.IsMandatory, c.IsIdentifier, c.IsTranslated, c.AD_Reference_Value_ID, 
	c.Callout, c.AD_Reference_ID, c.AD_Val_Rule_ID, c.AD_Process_ID, c.IsAlwaysUpdateable,
	c.ReadOnlyLogic, c.IsUpdateable, c.IsEncrypted AS IsEncryptedColumn, c.IsSelectionColumn,
	tbl.TableName, c.ValueMin, c.ValueMax, 
	fgt.Name AS FieldGroup, vr.Code AS ValidationCode,
--begin vpj-cd e-evolution 08/09/2005
        f.Included_Tab_ID
--end vpj-cd e-evolution 08/09/2005
FROM AD_Field f 
	INNER JOIN AD_Field_Trl trl ON (f.AD_Field_ID = trl.AD_Field_ID)
  INNER JOIN AD_Tab t ON (f.AD_Tab_ID = t.AD_Tab_ID)
  LEFT OUTER JOIN AD_FieldGroup_Trl fgt ON 
	(f.AD_FieldGroup_ID = fgt.AD_FieldGroup_ID AND trl.AD_Language=fgt.AD_Language)
  LEFT OUTER JOIN AD_Column c ON (f.AD_Column_ID = c.AD_Column_ID)
	INNER JOIN AD_Table tbl ON (c.AD_Table_ID = tbl.AD_Table_ID)
	INNER JOIN AD_Reference r ON (c.AD_Reference_ID = r.AD_Reference_ID)
	LEFT OUTER JOIN AD_Val_Rule vr ON (c.AD_Val_Rule_ID=vr.AD_Val_Rule_ID)
WHERE f.IsActive = 'Y' 
  AND c.IsActive = 'Y';

REM ======================================================================

