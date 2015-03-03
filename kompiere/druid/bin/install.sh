#!/bin/sh
#
echo Install e-Evolution Kompiere Extensions
export COMPIERE_HOME=/app/compiere/e-evolution/253b/install/build/Compiere2
export kompiere_extension=$1

if [ $JAVA_HOME ]; then
  JAVA=$JAVA_HOME/bin/java
  KEYTOOL=$JAVA_HOME/bin/keytool
else
  JAVA=java
  KEYTOOL=keytool
  echo JAVA_HOME is not set.
  echo You may not be able to start the Setup
  echo Set JAVA_HOME to the directory of your local JDK.
fi
 
## Check Compiere Home
if [ $COMPIERE_HOME ]; then
  CLASSPATH=$COMPIERE_HOME/lib/Compiere.jar:$COMPIERE_HOME/lib/CompiereCLib.jar:$COMPIERE_HOME/lib/Kompiere.jar:$CLASSPATH
else
  CLASSPATH=lib/Compiere.jar:lib/CompiereCLib.jar:lib/Kompiere.jar:$CLASSPATH
  echo COMPIERE_HOME is not set
  echo   You may not be able to start Compiere
  echo   Set COMPIERE_HOME to the directory of Compiere2.
fi

CP=../lib/XML2AD.jar:../lib/saxpath.jar:$COMPIERE_HOME/lib/CInstall.jar:$COMPIERE_HOME/lib/Compiere.jar:$COMPIERE_HOME/lib/CCTools.jar:$COMPIERE_HOME/lib/oracle.jar:$COMPIERE_HOME/lib/sybase.jar:$COMPIERE_HOME/lib/jboss.jar:$COMPIERE_HOME/lib/postgresql.jar:

# Trace Level Parameter, e.g. ARGS=ALL
ARGS=CONFIG
# Use -DSkipOCI=Y not to test the Oracle OCI driver

#echo ===================================
#echo Setup Compiere Server Environment
#echo ===================================
$JAVA -classpath $CP -DCOMPIERE_HOME=$COMPIERE_HOME -Dant.home="." -Dextension=$1 org.apache.tools.ant.launch.Launcher install
$JAVA -classpath $CP -DCOMPIERE_HOME=$COMPIERE_HOME -Dant.home="." -Dextension=$1 org.apache.tools.ant.launch.Launcher view
$JAVA -classpath $CP -DCOMPIERE_HOME=$COMPIERE_HOME -Dant.home="." -Dextension=$1 org.apache.tools.ant.launch.Launcher data

