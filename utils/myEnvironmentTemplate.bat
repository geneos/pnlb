@Rem	myEnvironment defines the variables used for Compiere
@Rem	Do not edit directly - use RUN_setup
@Rem	
@Rem	$Id: myEnvironmentTemplate.bat,v 1.12 2005/01/22 21:59:15 jjanke Exp $

@Echo Setting myEnvironment ....
@Rem	Clients only needs
@Rem		COMPIERE_HOME
@Rem		JAVA_HOME 
@Rem	Server install needs to check
@Rem		COMPIERE_DB_NAME	(for Oracle)
@Rem		passwords

@Rem 	Homes ...
@SET COMPIERE_HOME=@COMPIERE_HOME@
@SET JAVA_HOME=@JAVA_HOME@


@Rem	Database ...
@SET COMPIERE_DB_USER=@COMPIERE_DB_USER@
@SET COMPIERE_DB_PASSWORD=@COMPIERE_DB_PASSWORD@
@SET COMPIERE_DB_URL=@COMPIERE_DB_URL@

@Rem	Oracle specifics
@SET COMPIERE_DB_PATH=@COMPIERE_DB_TYPE@
@SET COMPIERE_DB_NAME=@COMPIERE_DB_NAME@
@SET COMPIERE_DB_SYSTEM=@COMPIERE_DB_SYSTEM@

@Rem	Homes(2)
@SET COMPIERE_DB_HOME=@COMPIERE_HOME@\utils\@COMPIERE_DB_TYPE@
@SET JBOSS_HOME=@COMPIERE_HOME@\jboss

@Rem	Apps Server
@SET COMPIERE_APPS_TYPE=@COMPIERE_APPS_TYPE@
@SET COMPIERE_APPS_SERVER=@COMPIERE_APPS_SERVER@
@SET COMPIERE_JNP_PORT=@COMPIERE_JNP_PORT@
@SET COMPIERE_WEB_PORT=@COMPIERE_WEB_PORT@
@SET COMPIERE_APPS_DEPLOY=@COMPIERE_APPS_TYPE@
@Rem	SSL Settings
@SET COMPIERE_SSL_PORT=@COMPIERE_SSL_PORT@
@SET COMPIERE_KEYSTORE=@COMPIERE_KEYSTORE@
@SET COMPIERE_KEYSTOREWEBALIAS=@COMPIERE_KEYSTOREWEBALIAS@
@SET COMPIERE_KEYSTOREPASS=@COMPIERE_KEYSTOREPASS@

@Rem	etc.
@SET COMPIERE_FTP_SERVER=@COMPIERE_FTP_SERVER@
@SET COMPIERE_FTP_USER=@COMPIERE_FTP_USER@

@Rem	Java
@SET COMPIERE_JAVA=@JAVA_HOME@\bin\java
@SET COMPIERE_JAVA_OPTIONS=@COMPIERE_JAVA_OPTIONS@ -DCOMPIERE_HOME=@COMPIERE_HOME@
@SET CLASSPATH="@COMPIERE_HOME@\lib\Compiere.jar;@COMPIERE_HOME@\lib\CompiereCLib.jar;"

@Rem Save Environment file
@if (%1) == () copy myEnvironment.bat myEnvironment_%RANDOM%.bat /Y 

