echo	Compiere Reference Database Import 	$Revision: 1.2 $

# $Id: ImportReference.sh,v 1.2 2005/01/22 21:59:15 jjanke Exp $

echo	Importing Reference DB from $COMPIERE_HOME/data/Reference.dmp

if [ $# -eq 2 ] 
  then
    echo "Usage:		$0 <systemAccount>"
    echo "Example:	$0 system/manager"
    exit 1
fi
if [ "$COMPIERE_HOME" = "" -o  "$COMPIERE_DB_NAME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "	COMPIERE_HOME	e.g. /Compiere2"
    echo "	COMPIERE_DB_NAME	e.g. compiere.compiere.org"
    exit 1
fi


echo -------------------------------------
echo Re-Create new user
echo -------------------------------------
sqlplus $1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/CreateUser.sql Reference Compiere

echo -------------------------------------
echo Import Reference
echo -------------------------------------
imp $1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/Reference.dmp FROMUSER=(reference) TOUSER=reference

echo -------------------------------------
echo Check System
echo Import may show some warnings. This is OK as long as the following does not show errors
echo -------------------------------------
sqlplus reference/compiere@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/AfterImport.sql

