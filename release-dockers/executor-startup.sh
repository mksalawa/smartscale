#!/bin/bash

yum update -y
yum install -y docker
service docker start
usermod -a -G docker ec2-user

docker run -d -e "AMQP_URL=amqp://<MASTER_IP>:5672" marg/hflow_executor:latest