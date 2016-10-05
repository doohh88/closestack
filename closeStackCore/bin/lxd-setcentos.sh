#!/bin/sh
#/1.os/2.password/3.ip/
CLOSESTACK_WORKER_HOST=$1

if [ "$CLOSESTACK_WORKER_GATE" = "" ]; then
  CLOSESTACK_WORKER_GATE="${CLOSESTACK_WORKER_HOST%.*}.1"
fi

mv /etc/sysconfig/network-scripts/ifcfg-eth0 /etc/sysconfig/network-scripts/ifcfg-eth0.cpy
INTERFACES_PATH="/etc/sysconfig/network-scripts/ifcfg-eth0"
echo "" > $INTERFACES_PATH
echo "DEVICE=eth0
BOOTPROTO=static
ONBOOT=yes
HOSTNAME=LXC_NAME
NM_CONTROLLED=no
TYPE=Ethernet
IPADDR="$CLOSESTACK_WORKER_HOST"
NETMASK="255.255.255.0"
GATEWAY="$CLOSESTACK_WORKER_GATE"
DNS1="8.8.8.8"
" >> $INTERFACES_PATH

/etc/rc.d/init.d/network restart
yum install openssh-server -y
adduser $2
echo -e "$3\n$3\n" | passwd $2
