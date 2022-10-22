#!/usr/bin/env bash

cd $(dirname $BASH_SOURCE)
docker build -f ../Dockerfile ../ -t tui_task:0.0.1