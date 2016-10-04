#!/bin/sh
apt-add-repository --yes ppa:zfs-native/stable -y
add-apt-repository ppa:ubuntu-lxc/lxd-stable -y
add-apt-repository ppa:webupd8team/java -y
apt-get update