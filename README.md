# extreme-deduplication
This repository contains an example application using the principle of extreme deduplication

## How to run this project

```
sbt cloudformation/run
sbt domain/generateTypescript
sbt openapi/run

# brew install python
# pip3 install awscli-local
# make sure aws --version yields 1.x.y
# pip3 install localstack
# pip3 install --upgrade awscli # see https://github.com/localstack/localstack/issues/3788
localstack start

sbt service/run

curl -X POST localhost:8080/foo -d '{"name": "myfoo", "id": 1, "description": "this is a description"}'
curl -X GET 'localhost:8080/foo?name=myfoo'
```