## Images

To create images needed by docker-compose.yml, run:

```
docker build app -t hflow_app:latest
docker build executor -t hflow_executor:latest
docker build smartscale -t smartscale:latest
```

Ideally these images will be available on DockerHub.

## Adding executors 

Executors may be added through the following command:

```
docker run -e "AMQP_URL=amqp://172.22.0.4:5672" --network releasedockers_default hflow_executor:latest
```

Network is specified, so this executor could connect to RabbitMQ, which is connected to that network.
