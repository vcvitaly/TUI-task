#!/usr/bin/env bash

cd $(dirname $BASH_SOURCE)
echo "Creating an ecr repository stack"
aws cloudformation create-stack --stack-name tui-task-repo-stack --template-body file://ecr.yml \
      --parameter file://ecr.json --region=eu-west-2