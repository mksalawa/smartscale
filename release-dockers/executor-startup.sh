#!/bin/bash

yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user

docker run -d \
    -e "AMQP_URL=amqp://<MASTER_IP>:5672" \
    -e "STORAGE=cloud" \
    -e "AWS_ACCESS_KEY_ID=<AWS_ACCESS_KEY_ID>" \
    -e "AWS_SECRET_ACCESS_KEY=<AWS_SECRET_ACCESS_KEY>" \
    -e "AWS_REGION=<AWS_REGION>" \
    marg/hflow_executor:latest