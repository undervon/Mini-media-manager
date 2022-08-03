# Mini media manager

### Simple application that allows a user to manage media content (photos)

Technologies:
* Spring Boot
    * https://spring.io/projects/spring-boot
* GraphQL with Netflix DGS
    * https://graphql.org
    * https://netflix.github.io/dgs/getting-started/
* Docker
    * https://www.docker.com
    * https://spring.io/guides/gs/spring-boot-docker/
* Localstack
    * https://localstack.cloud
    * https://learnbatta.com/blog/aws-localstack-with-docker-compose/
* AWS – S3 – DynamoDB
    * https://www.youtube.com/watch?v=JIbIYCM48to
    * https://docs.aws.amazon.com/AmazonS3/latest/userguide/Welcome.html
    * https://www.youtube.com/watch?v=L3dYocCSU-E
    * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html
    * https://www.youtube.com/watch?v=2k2GINpO308

### Theme
Let’s imagine we have the front-end logic for a simple application that allows a user to manage media content (photos in our case).

The application has a login page, an add post page and a posts page.

We only need to create the backend for this application – the front-end is already created.

Ideally, we would have GraphQL endpoints that allow us to:
* View posts
* Add a post with images and text

The posts would be saved in DynamoDB and the images in AWS S3.
