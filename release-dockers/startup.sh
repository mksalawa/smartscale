#!/bin/bash

yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user
curl -L https://github.com/docker/compose/releases/download/1.9.0/docker-compose-`uname -s`-`uname -m` > /usr/bin/docker-compose
chmod +x /usr/bin/docker-compose

echo "
version: '2'
services:

  smartscale:
    container_name: smartscale
    image: marg/smartscale:latest
    ports:
     - \"9002:9002\"
    environment:
      AWS_ACCESS_KEY_ID: <ACCESS_KEY>
      AWS_SECRET_ACCESS_KEY: <SECRET_KEY>
      TIME: 01:15
      STRATEGY: LINEAR
      GROUP_NAME: smartscale
      MAX_INSTANCES: 10

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
    command: bash -c \"sleep 5 && hflow run hyperflow/examples/Montage143/workflow_decorated.json -p hyperflow-monitoring-plugin\"
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
