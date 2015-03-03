#!/bin/sh
#
echo Set Unix Environment
# $Id: RUN_UnixEnvTemplate.sh,v 1.3 2004/03/11 05:41:13 jjanke Exp $

echo ===================================
echo Setup Client Environment
echo ===================================

echo Please add COMPIERE_HOME and JAVA_HOME to your environment

JAVA_HOME=@JAVA_HOME@
export JAVA_HOME
COMPIERE_HOME=@COMPIERE_HOME@
export COMPIERE_HOME

echo You chould also have set LD_LIBRARY_PATH

# ORACLE_HOME=/var/oracle/OraHome92
# export ORACLE_HOME

# Please check Oracle Installation documentation for details
# LD_LIBRARY_PATH=$ORACLE_HOME/lib
# export LD_LIBRARY_PATH
