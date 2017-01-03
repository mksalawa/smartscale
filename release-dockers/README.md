## Images

To create images needed by docker-compose.yml, run from the main directory:

```
docker build -t hflow_app:latest -f release-dockers/app/Dockerfile .
docker build -t hflow_executor:latest -f release-dockers/executor/Dockerfile .
docker build -t smartscale:latest -f release-dockers/smartscale/Dockerfile .
```

Ideally these images will be available on DockerHub.

## Adding executors 

Executors may be added through the following command:

```
docker run -e "AMQP_URL=amqp://<AMQP_IP>:5672" --network releasedockers_default hflow_executor:latest
```

Network is specified, so this executor could connect to RabbitMQ, which is connected to that network.
