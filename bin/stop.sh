#!/bin/sh
PID="`ps -ef | grep -v grep | grep \"PGREP=CAR_PUSH_AGENT\" | awk '{print $2}'`"

if [ $PID"X" = "X" ]; then
        printf "\t   process not running.  \n"
        exit;
fi

kill -9 $PID
