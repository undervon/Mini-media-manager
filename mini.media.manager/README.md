# Mini media manager

### Simple application that allows a user to manage media content (photos)

#### GraphQL API -> http://localhost:8090/graphiql

AWS CLI commands:
* aws configure -> creating configuration for AWS
    * aws configure set aws_access_key_id test-access-key
    * aws configure set aws_secret_access_key test-secret-access-key
    * aws configure set region eu-west-1
    * aws configure set output json
* aws configure list -> show AWS configuration list

Amazon S3:
* aws s3 mb s3://media-bucket --endpoint-url http://localhost:4566 -> creating a new S3 bucket
* aws s3 rb s3://media-bucket --force --endpoint-url http://localhost:4566 -> deleting an S3 bucket
    * https://docs.aws.amazon.com/cli/latest/userguide/cli-services-s3-commands.html (aws s3 commands)
* aws s3 ls --endpoint-url http://localhost:4566 -> printing all buckets
* aws s3 ls s3://media-bucket --recursive --human-readable --summarize --endpoint-url "http://localhost:4566" -> print everything from media-bucket
* aws s3 rm s3://media-bucket --recursive --endpoint-url "http://localhost:4566" -> delete everything from media-bucket

Amazon SQS:
* aws sqs create-queue --queue-name MediaQueue.fifo --attributes FifoQueue=true --endpoint-url http://localhost:4566 -> creating a FIFO Queue in sqs
* aws sqs list-queues --endpoint-url http://localhost:4566 -> list all queues
* aws sqs send-message --queue-url http://localhost:4566/000000000000/MediaQueue.fifo --message-body "First message" --message-group-id media --endpoint-url http://localhost:4566 -> send a message
* aws sqs send-message --queue-url http://localhost:4566/000000000000/MediaQueue.fifo --message-body "Second message" --message-group-id media --endpoint-url http://localhost:4566 -> send one more message
* aws sqs receive-message --queue-url http://localhost:4566/000000000000/MediaQueue.fifo --endpoint-url http://localhost:4566 -> receive a message

Amazon DynamoDB:
* aws dynamodb create-table --cli-input-json file://post_table.json --endpoint-url http://localhost:4566 -> creating DynamoDB table with post_table.json format
* aws dynamodb create-table --cli-input-json file://user_table.json --endpoint-url http://localhost:4566 -> creating DynamoDB table with user_table.json format
* aws dynamodb create-table --cli-input-json file://image_table.json --endpoint-url http://localhost:4566 -> creating DynamoDB table with image_table.json format 
* aws dynamodb list-tables --endpoint-url http://localhost:4566 -> list all tables
* aws dynamodb describe-table --table-name Post --endpoint-url http://localhost:4566 -> ~ describe Post table
* aws dynamodb describe-table --table-name User --endpoint-url http://localhost:4566 -> ~ describe User table
* aws dynamodb describe-table --table-name Image --endpoint-url http://localhost:4566 -> ~ describe Image table
