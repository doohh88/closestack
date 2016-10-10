#!/bin/sh
if [ -z "${CLOSESTACK_HOME}" ]; then
  export CLOSESTACK_HOME="$(cd ..; pwd)"
fi
#echo "CLOSESTACK_HOME = $CLOSESTACK_HOME"

if [ "$CLOSESTACK_PORT" = "" ]; then
  CLOSESTACK_PORT=2551
fi
#echo "CLOSESTACK_PORT = $CLOSESTACK_PORT"

if [ "$CLOSESTACK_HOST" = "" ]; then
  CLOSESTACK_HOST="$(wget http://ipecho.net/plain -O - -q ; echo)"
fi
#echo "CLOSESTACK_HOST = $CLOSESTACK_HOST"

CLASS="com.ssmksh.closestack.worker.WorkerMain"
java -cp $CLOSESTACK_HOME/jars/closestack-core.jar $CLASS -h $CLOSESTACK_HOST $1 $2 &
#echo $!> worker.pid