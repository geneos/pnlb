#!/bin/sh
#
# $Id: RUN_Migrate.sh,v 1.6 2005/01/22 21:59:15 jjanke Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo	Compiere Data Migration - $COMPIERE_HOME \($COMPIERE_DB_NAME\)
echo	.
echo	Data Migration is an optional service for a fee.
echo	Please check http://www.compiere.com/migrate/
echo	.

# Optional Set Source/Refence Database
# This requires that RUN_ImportReference was completed
# The default is:
# SOURCEDB="$COMPIERE_DB_URL reference reference"
# export SOURCEDB

# Optionally Set Target Database
# If you set the target database, the Source Database need to be set too!!
# If not, the default connection will be used:
# TARGETDB="$COMPIERE_DB_URL $COMPIERE_DB_USER $COMPIERE_DB_PASSWORD"
# export TARGETDB

$COMPIERE_JAVA $COMPIERE_JAVA_OPTIONS -cp $CLASSPATH com.compiere.client.StartMaintain $SOURCEDB $TARGETDB



