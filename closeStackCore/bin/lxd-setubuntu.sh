#!/bin/sh
#/1.os/2.password/3.ip/
CLOSESTACK_WORKER_HOST=$1

if [ "$CLOSESTACK_WORKER_GATE" = "" ]; then
  CLOSESTACK_WORKER_GATE="${CLOSESTACK_WORKER_HOST%.*}.1"
fi

mv /etc/network/interfaces /etc/network/interfaces.cpy
INTERFACES_PATH="/etc/network/interfaces"
echo "" > $INTERFACES_PATH
echo "auto lo
iface lo inet loopback

# The primary network interface
auto eth0
iface eth0 inet static
        address $CLOSESTACK_WORKER_HOST
        netmask 255.255.255.0
        gateway $CLOSESTACK_WORKER_GATE     
        dns-nameservers 8.8.8.8 8.8.4.4
" >> $INTERFACES_PATH

sed -i 's/PasswordAuthentication/\#PasswordAuthentication/g' /etc/ssh/sshd_config

