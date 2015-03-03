@Rem $Id: RUN_ImportReference.bat,v 1.11 2005/01/22 21:59:15 jjanke Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Import Reference - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)


@echo Re-Create Reference User and import %COMPIERE_HOME%\data\compiere.dmp - (%COMPIERE_DB_NAME%)
@dir %COMPIERE_HOME%\data\compiere.dmp
@echo == The import will show warnings. This is OK ==
@pause

@Rem Parameter: <systemAccount> <CompiereID> <CompierePwd>
@call %COMPIERE_DB_PATH%\ImportCompiere system/%COMPIERE_DB_SYSTEM% reference reference

@pause
