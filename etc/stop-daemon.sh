#!/bin/sh 
PID=$(cat .codestory.pid) 
kill -9 $PID

echo "STOPPED !"

