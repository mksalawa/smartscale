## Configuration

It is expected, that in the current directory, there are two directories:

* data/
* Montage_v3.3_patched_4/

Both should be created according to the Hyperflow Wiki page: [TutorialAMQP](https://github.com/dice-cyfronet/hyperflow/wiki/TutorialAMQP)

Also, it is expected that you have [Docker Compose](https://docs.docker.com/compose/install/) installed.


## Run

In the current directory run:

    docker-compose build
    docker-compose up

Running the command: <br />

    docker ps

should now show 4 running containers:

* hflow_redis - the instance with Redis installed and running,
* hflow_rabbitmq - the instance with RabbitMQ Broker on port 5672 and 15672 (management),
* hflow_executor - the instance on which the [hyperflow-amqp-executor](https://github.com/dice-cyfronet/hyperflow-amqp-executor)
 is run,
* hflow_app - the instance on which hyperflow is installed in ```/hyperflow``` directory.<br />
<br />

To run the workflow:
1. Switch shell using command:

        docker exec -it hflow_app bash
2. Run the workflow, eg.:

        hflow run hyperflow/examples/Montage143/workflow_decorated.json -s

<i>Warning: Depending on the workflow, you might need to adjust ```WORKDIR``` environment variable - the working directory
passed by Hyperflow engine to the executors.</i>

### Adding [Hyperflow monitoring plugin](https://github.com/dice-cyfronet/hyperflow-monitoring-plugin)

To use the monitoring plugin, you need to adjust the ```METRIC_COLLECTOR``` environment variable.
You can achieve it by simply exporting it in the ```hflow_app``` container or (as a permanent solution)
adjust the line in ```app/Dockerfile``` accordingly, eg.

        ...
        ENV METRIC_COLLECTOR localhost:9002
        ...

Also, you will need to provide a message aggregator for this address.
(For a simple example, see [Hyperflow monitoring plugin - Usage](https://github.com/dice-cyfronet/hyperflow-monitoring-plugin#start-a-dummy-message-aggregator))

Now you are all set up to run the workflow and collect the metrics:

        hflow run hyperflow/examples/Montage143/workflow_decorated.json -s -p hyperflow-monitoring-plugin


