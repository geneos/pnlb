echo	Compiere PostgreSQL Database Export 	$Revision: 1.3 $

# $Id: DBExport.sh,v 1.3 2005/01/22 21:59:15 jjanke Exp $

echo Saving database $1@$COMPIERE_DB_NAME to $COMPIERE_HOME/data/ExpDat.dump.tar.gz

pg_dump -F c -f $COMPIERE_HOME/data/ExpDat.dump.tar.gz compiere 

