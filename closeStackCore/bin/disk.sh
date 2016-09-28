#!/bin/sh
df -P | grep -v ^Filesystem | awk '{sum += $2} END { print sum/1024}'