#!/bin/sh

ARGUMENT=1

EXE_HOME=/svc/ccsp/daemon/car_push_agent
MASTER_HOME=/svc/ccsp/daemon/master
EXE_JAR=$EXE_HOME/jars/LGU_CCSS_WAS_CAR_PUSH_AGENT-0.0.1.jar
LOG_HOME=/logs/ccsp/daemon/car_push_agent

tmp=`grep "SERVER_GUBUN=" $MASTER_HOME/conf/serverinfo.cfg |grep -v "#" |awk '{split($1,s,"="); print s[2]}'`
if [ -z $tmp ]
then
echo "Error: 'SERVER_GUBUN' not defined"
echo "Error : Checking for system-directory or configuration-values ('serverinfo.cfg')"
echo " "
exit
fi
SERVER_GUBUN=$tmp

tmp=`grep "SERVER_TYPE=" $MASTER_HOME/conf/serverinfo.cfg |grep -v "#" |awk '{split($1,s,"="); print s[2]}'`
if [ -z $tmp ]
then
echo "Error: 'SERVER_TYPE' not defined"
echo "Error : Checking for system-directory or configuration-values ('serverinfo.cfg')"
echo " "
exit
fi
SERVER_TYPE=$tmp

tmp=`grep "SERVER_IP=" $MASTER_HOME/conf/serverinfo.cfg |grep -v "#" |awk '{split($1,s,"="); print s[2]}'`
if [ -z $tmp ]
then
echo "Error: 'SERVER_IP' not defined"
echo "Error : Checking for system-directory or configuration-values ('serverinfo.cfg')"
echo " "
exit
fi
SERVER_IP=$tmp

tmp=`grep "SERVER_ID=" $MASTER_HOME/conf/serverinfo.cfg |grep -v "#" |awk '{split($1,s,"="); print s[2]}'`
if [ -z $tmp ]
then
echo "Error: 'SERVER_ID' not defined"
echo "Error : Checking for system-directory or configuration-values ('serverinfo.cfg')"
echo " "
exit
fi
SERVER_ID=$tmp

PGREP=CAR_PUSH_AGENT
SERVER_NAME=car_push_agent
SERVER_PORT=UNDEFINED

gc_log=`date +"gc_%Y%m%d%H%M%S.log"`

JAVA_EXE="java"
APP_OPTION="-DPGREP=$PGREP -DSERVER_NAME=$SERVER_NAME -DSERVER_GUBUN=$SERVER_GUBUN -DSERVER_TYPE=$SERVER_TYPE -Dspring.config.location=$EXE_HOME/conf -Dspring.profiles.active=$SERVER_TYPE -DSERVER_IP=$SERVER_IP -DSERVER_PORT=$SERVER_PORT -DSERVER_ID=$SERVER_ID"
JVM_GC_OPTION="-verbose:gc -Xloggc:$LOG_HOME/$gc_log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
JVM_HEAP_OPTION="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$EXE_HOME/logs/heapdump -Djava.awt.headless=true"
#JVM_OPTION="-jar -Xbootclasspath/p:$EXE_HOME/conf"
JVM_OPTION=""
CLASSPATH="-classpath $EXE_HOME/conf:$EXE_HOME/lib/*:$EXE_JAR"
EXE_CLASS="com.lgu.ccss.App"

EXE_SCRIPT="$JAVA_EXE $CLASSPATH $JVM_GC_OPTION $JVM_HEAP_OPTION $JVM_OPTION $APP_OPTION $EXE_CLASS $ARGUMENT"

PID=`ps -ef | grep java | grep $SERVER_NAME | grep 'PGREP='$PGREP | grep $(whoami) | grep -v grep | awk '{print $2}'`

PIDLEN=`expr "$PID" : '.*'`

if test $PIDLEN != "0"
then
echo Running Agent PID:         $PID
echo $SERVER_NAME "is already running"
else
echo "Running " $EXE_SCRIPT
$EXE_SCRIPT
fi
