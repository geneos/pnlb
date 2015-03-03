#!/bin/sh
#
# myEnvironment defines the variables used for Compiere
# Do not edit directly - use RUN_setup
#
# $Id: myEnvironmentTemplate.sh,v 1.12 2005/02/21 03:17:21 jjanke Exp $

echo Setting myEnvironment ....
#	Clients only needs
#		COMPIERE_HOME
#		JAVA_HOME 
#	Server install needs to change
#		COMPIERE_DB_NAME	(for Oracle)
#		passwords
#
#	For a HTML browser, Compiere will call "netscape <targetURL>"
#	If not in the path, provide a link called netscape to your browser

# 	Homes ...
COMPIERE_HOME=@COMPIERE_HOME@
export COMPIERE_HOME
JAVA_HOME=@JAVA_HOME@
export JAVA_HOME

#	Database ...
COMPIERE_DB_USER=@COMPIERE_DB_USER@
export COMPIERE_DB_USER
COMPIERE_DB_PASSWORD=@COMPIERE_DB_PASSWORD@
export COMPIERE_DB_PASSWORD
COMPIERE_DB_URL=@COMPIERE_DB_URL@
export COMPIERE_DB_URL

#	Oracle Specifics ...
COMPIERE_DB_PATH=oracle
export COMPIERE_DB_PATH
COMPIERE_DB_NAME=@COMPIERE_DB_NAME@
export COMPIERE_DB_NAME
COMPIERE_DB_SYSTEM=@COMPIERE_DB_SYSTEM@
export COMPIERE_DB_SYSTEM

#	Homes(2)
COMPIERE_DB_HOME=$COMPIERE_HOME/utils/$COMPIERE_DB_PATH
export COMPIERE_DB_HOME
JBOSS_HOME=$COMPIERE_HOME/jboss
export JBOSS_HOME

#	Apps Server
COMPIERE_APPS_SERVER=@COMPIERE_APPS_SERVER@
export COMPIERE_APPS_SERVER
COMPIERE_WEB_PORT=@COMPIERE_WEB_PORT@
export COMPIERE_WEB_PORT
COMPIERE_JNP_PORT=@COMPIERE_JNP_PORT@
export COMPIERE_JNP_PORT
#	SSL Settings - see jboss/server/compiere/deploy/jbossweb.sar/META-INF/jboss-service.xml
COMPIERE_SSL_PORT=@COMPIERE_SSL_PORT@
export COMPIERE_SSL_PORT
COMPIERE_KEYSTORE=@COMPIERE_KEYSTORE@
export COMPIERE_KEYSTORE
COMPIERE_KEYSTOREPASS=@COMPIERE_KEYSTOREPASS@
export COMPIERE_KEYSTOREPASS

#	etc.
COMPIERE_FTP_SERVER=@COMPIERE_FTP_SERVER@
export COMPIERE_FTP_SERVER

#	Java
COMPIERE_JAVA=$JAVA_HOME/bin/java
export COMPIERE_JAVA
COMPIERE_JAVA_OPTIONS="@COMPIERE_JAVA_OPTIONS@ -DCOMPIERE_HOME=$COMPIERE_HOME"
export COMPIERE_JAVA_OPTIONS
CLASSPATH="$COMPIERE_HOME/lib/Compiere.jar:$COMPIERE_HOME/lib/CompiereCLib.jar"
export CLASSPATH

if [ $DOLLAR$# -eq 0 ] 
  then
    cp myEnvironment.sh myEnvironment.sav
fi