#!/bin/sh
kill -9 `cat < worker.pid `
rm worker.pid