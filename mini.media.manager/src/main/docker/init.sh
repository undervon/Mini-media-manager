#!/usr/bin/env bash
printf "Configuring resources...\n"

ACCESS_KEY="test-access-key"
SECRET_ACCESS_KEY="test-secret-access-key"
LOCALSTACK_URL="http://localhost:4566"

set -x

# set the AWS variables
aws configure set aws_access_key_id "$ACCESS_KEY"
aws configure set aws_secret_access_key "$SECRET_ACCESS_KEY"
aws configure set region eu-west-1
aws configure set output json

# print the AWS configuration list
aws configure list

# waiting for tables and bucket to be created
sleep 1
aws s3 mb s3://media-bucket \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb create-table \
  --cli-input-json file:////docker-entrypoint-initdb.d/post_table.json \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb create-table \
  --cli-input-json file:////docker-entrypoint-initdb.d/user_table.json \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb create-table \
  --cli-input-json file:////docker-entrypoint-initdb.d/image_table.json \
  --endpoint-url "$LOCALSTACK_URL"

# print resources for validation
sleep 1
aws s3 ls \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb list-tables \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb describe-table \
  --table-name Post \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb describe-table \
  --table-name User \
  --endpoint-url "$LOCALSTACK_URL"
aws dynamodb describe-table \
  --table-name Image \
  --endpoint-url "$LOCALSTACK_URL"

echo "Resources created and ready!"
