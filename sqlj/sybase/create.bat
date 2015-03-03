@Title Create Sybase SQLJ
@Rem	
@Rem	Author + Copyright 1999-2005 Jorg Janke
@Rem	$Id: create.bat,v 1.4 2005/02/14 02:24:11 jjanke Exp $
@Rem
@Rem	Currently user/etc. name hard coded
@Rem

@Echo Load Sybase Database ...
instjava -f "C:\Compiere2\lib\sqlj.jar" -update -U compiere -P compiere -D compiere

@Echo Create Sybase Functions ...
isql -U compiere -P compiere -D compiere -i createSQLJ.sql

@pause

