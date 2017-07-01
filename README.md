# SmartScale

## Description

SmartScale is a complete system for running distributed workflow applications, 
using [HyperFlow engine](https://github.com/balis/hyperflow) and [Amazon Web Services](https://aws.amazon.com/) platform
in urgent computing.

I consists of two major parts:
* SmartScale application
* deployment mechanisms and configuration


### SmartScale application

Application responsible for the autoscaling mechanisms. 
Uses metrics collected and pushed by [HyperFlow Monitoring Plugin](https://github.com/dice-cyfronet/hyperflow-monitoring-plugin)
describing current state of the running application to come up with the scaling decisions.

It uses AWS API ([Java SDK](https://aws.amazon.com/sdk-for-java/)) 
to configure and instruct the [AWS Auto Scaling](https://aws.amazon.com/autoscaling/) mechanisms.

### Deployment mechanisms and configuration

All necessary components have been mapped to Docker container images, available on DockerHub:
* [HyperFlow image](https://hub.docker.com/r/marg/hflow_app)
* [HyperFlow Executor image](https://hub.docker.com/r/marg/hflow_executor)
* [SmartScale App image](https://hub.docker.com/r/marg/smartscale)

Docker Compose is used for choreography of the services (HyperFlow + Redis + RabbitMQ, SmartScale).

During deployment, ready-made initialising scripts are used to configure the AWS objects during their creation.
The scripts need to be adjusted to the user and application's needs.
