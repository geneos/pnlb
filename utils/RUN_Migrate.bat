@Rem $Id: RUN_Migrate.bat,v 1.12 2005/08/24 22:50:37 jjanke Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Data Migration - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Echo Data Migration is an optional service for a fee.
@Echo Please check http://www.compiere.org/migrate/

@REM	Optional Set Source/Reference Database
@REM	This requires that RUN_ImportReference was completed
@REM	The default is:
@REM SET SOURCEDB=%COMPIERE_DB_URL% reference reference

@REM	Optionally Set Target Database
@REM	If you set the target database, the Source Database need to be set too!!
@REM	If not, the default connection will be used:
@REM SET TARGETDB=%COMPIERE_DB_URL% compiere compiere

@Rem	Example connecting to database user compiere/compierePw in instance dev2 on server dev2server 
@REM SET TARGETDB=jdbc:oracle:thin:@//dev2server:1521:dev2.compiere.org compiere compierePw

@echo -------------------------------------
@echo Start UI
@echo -------------------------------------
@"%COMPIERE_JAVA%" %COMPIERE_JAVA_OPTIONS% -cp %CLASSPATH% com.compiere.client.StartMaintain %SOURCEDB% %TARGETDB%

@echo -------------------------------------
@echo Create SQLJ 
@echo -------------------------------------
@call %COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\create %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%

@echo -------------------------------------
@echo Check System
@echo -------------------------------------
@sqlplus %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\AfterImport.sql


@pause


