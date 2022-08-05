# Mini media manager

### Simple application that allows a user to manage media content (photos)

#### GraphQL API -> http://localhost:8090/graphiql

AWS CLI commands:
* aws configure -> creating configuration for AWS
* aws configure list -> show AWS configuration list

AWS S3:
* aws --endpoint-url http://localhost:4566 s3 mb s3://media-bucket -> creating a new S3 bucket
* aws --endpoint-url http://localhost:4566 s3 rb s3://media-bucket --force -> deleting an S3 bucket
    * https://docs.aws.amazon.com/cli/latest/userguide/cli-services-s3-commands.html (aws s3 commands)
* aws --endpoint-url http://localhost:4566 s3 ls -> printing all buckets

AWS DynamoDB:
* aws dynamodb create-table --cli-input-json file://post_table.json --endpoint-url http://localhost:4566 -> creating DynamoDB table with post_table.json format
* aws dynamodb create-table --cli-input-json file://user_table.json --endpoint-url http://localhost:4566 -> creating DynamoDB table with user_table.json format
* aws dynamodb describe-table --table-name posts --endpoint-url http://localhost:4566 -> ~ describe posts table
* aws dynamodb describe-table --table-name users --endpoint-url http://localhost:4566 -> ~ describe users table
