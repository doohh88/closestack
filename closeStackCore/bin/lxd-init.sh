#!/bin/sh
echo -e "\n\n\n\n500\n\n\n\n\n\n" | lxd init
lxc profile device set default eth0 parent br0
#lxc profile device show default