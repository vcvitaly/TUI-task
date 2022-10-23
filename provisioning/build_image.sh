#!/usr/bin/env bash

cd $(dirname $BASH_SOURCE)
docker build -f Dockerfile ../ -t $1