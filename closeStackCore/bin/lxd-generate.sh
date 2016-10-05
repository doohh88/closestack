#!/bin/sh
#/1.os/2.lxdname/3.cpu/4.mem/5.disk/6.ip/7.username/8.password


if [ "$1" = "ubuntu" ]; then
echo "generate ubuntu"
#OS=ubuntu/trusty/amd64
lxc launch ubuntu: $2
else
echo "generate centos"
OS=centos/7
lxc launch images:$OS $2
fi


lxc config set $2 limits.cpu $3
lxc config set $2 limits.memory $4GB
lxc config device set $2 root size $5GB



if [ "$1" = "ubuntu" ]; then
echo "send lxd-setubuntu.sh to root"
lxc file push lxd-setubuntu.sh $2/root/
lxc exec $2 -- ./lxd-setubuntu.sh $6
else
echo "send lxd-setcentos.sh to root"
lxc file push lxd-setcentos.sh $2/root/
lxc exec $2 -- ./lxd-setcentos.sh $6 $7 $8
fi

lxc restart $2

if [ "$1" = "ubuntu" ]; then
sudo echo -e "$8\n$8\n\n\n\n\n\nY\n" | lxc exec $2 -- adduser $7
else
echo "lxc exec.."
fi

