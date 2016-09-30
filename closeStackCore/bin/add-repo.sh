#!/bin/sh
apt-add-repository --yes ppa:zfs-native/stable -y
add-apt-repository ppa:ubuntu-lxc/lxd-stable -y
apt-get update