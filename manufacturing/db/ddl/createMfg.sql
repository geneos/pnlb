SET SERVEROUTPUT ON;

@removeMfg;

@tables/Create_PC_WorkCell;
@tables/Create_PC_WorkCenter;
@tables/Create_PC_Workstation;

-- Perform System-Level Maintenance

@../maintain/0_Add_New_Column.sql;
@../maintain/AD_Element_Check.sql;

-- GET ../maintain/0_Add_New_Field.sql
-- /
@../maintain/0_SyncNames.sql;

-- Perform Table-Level Updates

@tables/Update_PC_WorkCell;
@tables/Update_PC_WorkCenter;
@tables/Update_PC_Workstation;

-- Recreate the Manufacturing menus




