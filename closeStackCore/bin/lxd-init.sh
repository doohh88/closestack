#!/bin/sh
lxd init
lxc profile device set default eth0 parent br0