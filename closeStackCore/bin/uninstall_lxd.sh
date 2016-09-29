#!/bin/sh
apt-get purge --auto-remove lxd -y
apt-get purge --auto-remove lxc -y
deluser --remove-home lxd
groupdel lxd
rm -rf /var/lib/lxc