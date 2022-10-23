#!/usr/bin/env bash

cd $(dirname $BASH_SOURCE)
source env.sh
./create_ecr/create.sh
echo "Logging in to ECR"
aws ecr get-login-password --region "$AWS_REGION" | docker login \
  --username AWS --password-stdin "$ECR_REGISTRY"
echo "Building an image"
./build_image.sh "$IMAGE_NAME"
docker tag "$IMAGE_NAME" "$ECR_REGISTRY"/"$RESOURCE_PREFIX"-repo:"$IMAGE_NAME"
echo "Pushing the image"
docker push "$ECR_REGISTRY"/"$RESOURCE_PREFIX"-repo:"$IMAGE_NAME"

echo "Deleting an existing stack"
aws cloudformation delete-stack --stack-name "$RESOURCE_PREFIX"-deployment
echo "Waiting until the stack is deleted"
aws cloudformation wait stack-delete-complete --stack-name "$RESOURCE_PREFIX"-deployment
echo "Creating a new stack"
aws cloudformation create-stack --stack-name "$RESOURCE_PREFIX"-deployment --template-body file://ecs.yml \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameters \
    "ParameterKey=resourcePrefix,ParameterValue=$RESOURCE_PREFIX" \
    "ParameterKey=ecrRegistry,ParameterValue=$ECR_REGISTRY" \
    "ParameterKey=imageName,ParameterValue=$IMAGE_NAME" \
    "ParameterKey=githubToken,ParameterValue=$GITHUB_TOKEN" \
    "ParameterKey=applicationProfile,ParameterValue=$APPLICATION_PROFILE"