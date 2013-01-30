#!/bin/sh

COMMAND="java -jar -Xmx1024m CodeStory2K13-1.0.jar"
PID=0

## To avoid system input and keep command running in background
## Because of System.in.read()
(while true; do sleep 10000; done) | $COMMAND  &> /dev/null & 

PID=`ps ax | grep "$COMMAND" | head -1 | awk '{print $1}'`

if [ $PID -gt 0 ] 
then
  echo $PID > .codestory.pid
  echo "CodeStory server started !"
else
  echo "Starting CodeStory server failed!"
fi
