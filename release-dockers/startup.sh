#!/bin/bash

yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user
curl -L https://github.com/docker/compose/releases/download/1.8.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

echo "
version: '2'
services:

  smartscale:
    container_name: smartscale
    image: marg/smartscale:latest
    ports:
     - \"9002:9002\"

  redis:
    container_name: hflow_redis
    image: redis:3.0

  rabbitmq:
    container_name: hflow_rabbitmq
    image: rabbitmq:3-management
    ports:
     - \"5672:5672\"
     - \"15672:15672\"

  app:
    container_name: hflow_app
    image: marg/hflow_app:latest
    depends_on:
      - smartscale
      - rabbitmq
      - redis
    environment:
      REDIS_URL: redis://hflow_redis:6379
      AMQP_URL: amqp://hflow_rabbitmq:5672
      METRIC_COLLECTOR: smartscale:9002

" > docker-compose.yml

docker-compose up -d
