#!/usr/bin/env bash
set -x

echo "########### Setting AWS variables ##########"
ACCESS_KEY=test-access-key
SECRET_ACCESS_KEY=test-secret-access-key
AWS_REGION=eu-west-1

echo "########### Setting up the AWS variables ###########"
aws configure set aws_access_key_id $ACCESS_KEY
aws configure set aws_secret_access_key $SECRET_ACCESS_KEY
aws configure set region $AWS_REGION
aws configure set output json

echo "########### List AWS configuration ###########"
aws configure list

sleep 1
echo "########### Setting localstack URL and S3 and SQS and DynamoDB tables names as env variables ###########"
LOCALSTACK_URL=http://localhost:4566
BUCKET_NAME=media-bucket
QUEUE_NAME=MediaQueue
POST_TABLE_NAME=Post
USER_TABLE_NAME=User
IMAGE_TABLE_NAME=Image

echo "########### Creating S3 bucket ###########"
aws s3 mb s3://$BUCKET_NAME \
  --endpoint-url $LOCALSTACK_URL

echo "########### List S3 buckets ###########"
aws s3 ls \
  --endpoint-url $LOCALSTACK_URL

echo "########### Creating SQS Queue ###########"
aws sqs create-queue \
  --queue-name $QUEUE_NAME \
  --endpoint-url $LOCALSTACK_URL

echo "########### List SQS queues ###########"
aws sqs list-queues \
  --endpoint-url $LOCALSTACK_URL

echo "########### Set S3 bucket notification configurations ###########"
aws s3api put-bucket-notification-configuration \
  --bucket $BUCKET_NAME \
  --notification-configuration '{
                                  "QueueConfigurations": [
                                    {
                                      "QueueArn": "http://localhost:4566/000000000000/'"$QUEUE_NAME"'",
                                      "Events": [
                                        "s3:ObjectCreated:*"
                                      ]
                                    }
                                  ]
                                }' \
  --endpoint-url $LOCALSTACK_URL

echo "########### Get S3 bucket notification configurations ###########"
aws s3api get-bucket-notification-configuration \
  --bucket $BUCKET_NAME \
  --endpoint-url $LOCALSTACK_URL

sleep 1
echo "########### Creating DynamoDB tables ###########"
aws dynamodb create-table \
  --cli-input-json '{
                      "TableName": "'"$POST_TABLE_NAME"'",
                      "AttributeDefinitions": [
                        {
                          "AttributeName": "id",
                          "AttributeType": "S"
                        }
                      ],
                      "KeySchema": [
                        {
                          "AttributeName": "id",
                          "KeyType": "HASH"
                        }
                      ],
                      "ProvisionedThroughput": {
                        "ReadCapacityUnits": 5,
                        "WriteCapacityUnits": 5
                      }
                    }' \
  --endpoint-url $LOCALSTACK_URL
aws dynamodb create-table \
  --cli-input-json '{
                        "TableName": "'"$USER_TABLE_NAME"'",
                        "AttributeDefinitions": [
                          {
                            "AttributeName": "id",
                            "AttributeType": "S"
                          }
                        ],
                        "KeySchema": [
                          {
                            "AttributeName": "id",
                            "KeyType": "HASH"
                          }
                        ],
                        "ProvisionedThroughput": {
                          "ReadCapacityUnits": 5,
                          "WriteCapacityUnits": 5
                        }
                      }' \
  --endpoint-url $LOCALSTACK_URL
aws dynamodb create-table \
  --cli-input-json '{
                        "TableName": "'"$IMAGE_TABLE_NAME"'",
                        "AttributeDefinitions": [
                          {
                            "AttributeName": "id",
                            "AttributeType": "S"
                          }
                        ],
                        "KeySchema": [
                          {
                            "AttributeName": "id",
                            "KeyType": "HASH"
                          }
                        ],
                        "ProvisionedThroughput": {
                          "ReadCapacityUnits": 5,
                          "WriteCapacityUnits": 5
                        }
                      }' \
  --endpoint-url $LOCALSTACK_URL

echo "########### List DynamoDB tables ###########"
aws dynamodb list-tables \
  --endpoint-url $LOCALSTACK_URL

echo "########### Describe each DynamoDB tables ###########"
aws dynamodb describe-table \
  --table-name $POST_TABLE_NAME \
  --endpoint-url $LOCALSTACK_URL
aws dynamodb describe-table \
  --table-name $USER_TABLE_NAME \
  --endpoint-url $LOCALSTACK_URL
aws dynamodb describe-table \
  --table-name $IMAGE_TABLE_NAME \
  --endpoint-url $LOCALSTACK_URL

sleep 1
echo "########### Resources created and ready ###########"
