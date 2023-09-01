# Quarkus-based gRPC Service PoC

This repo contains a trivial sample gRPC Service using Quarkus.

## Starting the service

Usual Quarkus way:

```
./mvnw clean quarkus:dev
```

## Accessing service

### gRPCurl

You may want to use [grpcurl](https://github.com/fullstorydev/grpcurl) ("grpcurl"); on mac it is installed with

```
brew install grpcurl
```

after which you can:

### List exposed Services

To see what services are exposed (in "dev" mode; not enabled by default in "prod"), try:

```
grpcurl -plaintext localhost:9000 list
```

which should list something like:

```
grpc.health.v1.Health
vector_service.VectorApi
```

### Access VectorApi 

To make actual gRPC call, try:

```
grpcurl -plaintext -d '{ "contents": "Some stuff" }' localhost:9000 vector_service.VectorApi/Vectorize
```

which should return something like:

```
{
  "contents": "Some stuff",
  "tokens": [
    "Some",
    "stuff"
  ],
  "count": 2
}
```
