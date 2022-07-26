# Mini media manager

### Simple application that allows a user to manage media content (photo)

Technologies:
* Spring Boot
* GraphQL
* Docker
* Localstack
* AWS – S3 – DynamoDB

### Theme
Let’s imagine we have the front-end logic for a simple application that allows a user to manage media content (photos in our case).

The application has a login page, an add post page and a posts page.

We only need to create the backend for this application – the front-end is already created.

Ideally, we would have GraphQL endpoints that allow us to:
* View posts
* Add a post with images and text

The posts would be saved in DynamoDB and the images in AWS S3.
