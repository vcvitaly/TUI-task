#!/usr/bin/env bash

cd $(dirname $BASH_SOURCE)
source env.sh
./create_ecr/create.sh
echo "Logging in to ECR"
aws ecr get-login-password --region eu-west-2 | docker login \
  --username AWS --password-stdin "$ECR_REGISTRY"
echo "Building an image"
./build_image.sh
docker tag tui_task:0.0.1 "$ECR_REGISTRY"/tui-task-repo:tui_task_0.0.1
echo "Pushing the image"
docker push "$ECR_REGISTRY"/tui-task-repo:tui_task_0.0.1

echo "Deleting an existing stack"
aws cloudformation delete-stack --stack-name tui-task-deployment
echo "Waiting until the stack is deleted"
aws cloudformation wait stack-delete-complete --stack-name tui-task-deployment
echo "Creating a new stack"
aws cloudformation create-stack --stack-name tui-task-deployment --template-body file://ecs.yml \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameters 'ParameterKey=SubnetID,ParameterValue=subnet-069412dc3b3a4a639'