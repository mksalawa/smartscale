FROM node:4

RUN npm install https://github.com/dice-cyfronet/hyperflow/archive/develop.tar.gz
RUN cd /node_modules/hyperflow/ && npm install https://github.com/dice-cyfronet/hyperflow-monitoring-plugin/archive/master.tar.gz

RUN ln -s /node_modules/hyperflow/

ENV PATH $PATH:node_modules/hyperflow/bin/

ENV REDIS_URL redis://hflow_redis:6379
ENV AMQP_URL amqp://hflow_rabbitmq:5672
ENV METRIC_COLLECTOR smartscale:9002

ENV METRIC_COLLECTOR_TYPE visor

COPY ./examples /smartscale/examples
ENV PATH $PATH:$WORKDIR/resources

#docker exec -it hflow_app hflow run /smartscale/examples/MolecularDynamicsParameterStudy/workflow.json -p hyperflow-monitoring-plugin
CMD tail -f /dev/null