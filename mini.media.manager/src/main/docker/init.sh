#!/usr/bin/env bash
set -x

echo "########### Setting localstack URL and S3 and SQS and DynamoDB tables names as env variables ###########"
BUCKET_NAME=mini-media-manager-bucket
QUEUE_NAME=MediaQueue
POST_TABLE_NAME=Post
USER_TABLE_NAME=User
IMAGE_TABLE_NAME=Image

echo "########### Creating S3 bucket ###########"
aws s3 mb s3://$BUCKET_NAME

echo "########### List S3 buckets ###########"
aws s3 ls

echo "########### Creating SQS Queue ###########"
aws sqs create-queue \
  --queue-name $QUEUE_NAME

echo "########### List SQS queues ###########"
aws sqs list-queues

echo "########### Set S3 bucket notification configurations ###########"
aws s3api put-bucket-notification-configuration \
  --bucket $BUCKET_NAME \
  --notification-configuration '{
                                  "QueueConfigurations": [
                                    {
                                      "QueueArn": "arn:aws:sqs:us-east-1:445628024359:'"$QUEUE_NAME"'",
                                      "Events": [
                                        "s3:ObjectCreated:*"
                                      ]
                                    }
                                  ]
                                }'

echo "########### Get S3 bucket notification configurations ###########"
aws s3api get-bucket-notification-configuration \
  --bucket $BUCKET_NAME

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
                    }'
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
                      }'
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
                      }'

echo "########### List DynamoDB tables ###########"
aws dynamodb list-tables

echo "########### Describe each DynamoDB tables ###########"
aws dynamodb describe-table \
  --table-name $POST_TABLE_NAME
aws dynamodb describe-table \
  --table-name $USER_TABLE_NAME
aws dynamodb describe-table \
  --table-name $IMAGE_TABLE_NAME

sleep 1
echo "########### Resources created and ready ###########"
