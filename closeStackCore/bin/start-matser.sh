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

CLASS="com.ssmksh.closestack.master.MasterMain"
java -cp $CLOSESTACK_HOME/jars/distDeep-core-0.0.1.jar $CLASS -h $CLOSESTACK_HOST &
echo $!> master.pid