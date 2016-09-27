#!/bin/sh
kill -9 `cat < master.pid `
rm master.pid