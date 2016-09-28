#!/bin/sh
echo "run worker-bridge.sh"
apt-get install bridge-utils -y

if [ "$CLOSESTACK_WORKER_HOST" = "" ]; then
  CLOSESTACK_WORKER_HOST="$(wget http://ipecho.net/plain -O - -q ; echo)"
fi

if [ "$CLOSESTACK_WORKER_GATE" = "" ]; then
  CLOSESTACK_WORKER_GATE="${CLOSESTACK_WORKER_HOST%.*}.1"
fi

mv /etc/network/interfaces /etc/network/interfaces.cpy
INTERFACES_PATH="/etc/network/interfaces"
echo "" > $INTERFACES_PATH
echo "auto lo
iface lo inet loopback

# The primary network interface
auto br0
iface br0 inet static
        address $CLOSESTACK_WORKER_HOST
        netmask 255.255.255.0
        gateway $CLOSESTACK_WORKER_GATE     
        dns-nameservers 8.8.8.8 8.8.4.4
        bridge_ports eth0

iface eth0 inet manual
" >> $INTERFACES_PATH 

ifdown br0
ifup br0
