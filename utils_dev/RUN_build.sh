#	This script rebuilds Compiere 
#	Ported from Windows script Marek Mosiewicz<marek.mosiewicz@jotel.com.pl>


SAVED_DIR=`pwd`			#save current dir
cd `dirname $0`			#change dir to place where script resides - doesn not work with sym links
UTILS_DEV=`pwd`			#this is compiere source
cd $SAVED_DIR			#back to the saved directory

.  $UTILS_DEV/myDevEnv.sh	#call environment


if [ ! $COMPIERE_ENV==Y ] ; then
    echo "Can't set developemeent environemnt - check myDevEnv.sh"
    exit 1
fi

echo Cleanup ...
$JAVA_HOME/bin/java -Dant.home="." $ANT_PROPERTIES org.apache.tools.ant.Main clean

echo Building ...
$JAVA_HOME/bin/java -Xmx500m -Dant.home="." $ANT_PROPERTIES org.apache.tools.ant.Main -logger org.apache.tools.ant.listener.MailLogger complete

ls $COMPIERE_INSTALL

echo Done ...

exit 0

