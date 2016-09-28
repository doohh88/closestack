#!/bin/sh
if [ -z "${CLOSESTACK_HOME}" ]; then
  export CLOSESTACK_HOME="$(cd ..; pwd)"
fi
#echo "CLOSESTACK_HOME = $CLOSESTACK_HOME"

if [ "$CLOSESTACK_HOST" = "" ]; then
  CLOSESTACK_HOST="$(wget http://ipecho.net/plain -O - -q ; echo)"
fi
#echo "CLOSESTACK_HOST = $CLOSESTACK_HOST"

ACTOR=` cat < actor.url `

CLASS="com.ssmksh.closestack.query.QueryMain"
java -cp $CLOSESTACK_HOME/jars/closestack-core.jar $CLASS -h $CLOSESTACK_HOST -a $ACTOR -c clazz $@ &