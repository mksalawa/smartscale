FROM node:4

RUN npm install https://github.com/dice-cyfronet/hyperflow/archive/e0ddc26d398.tar.gz
RUN cd /node_modules/hyperflow/ && npm install https://github.com/dice-cyfronet/hyperflow-monitoring-plugin/archive/master.tar.gz

RUN ln -s /node_modules/hyperflow/

ENV PATH $PATH:node_modules/hyperflow/bin/
ENV REDIS_URL redis://hflow_redis:6379
ENV AMQP_URL amqp://hflow_rabbitmq:5672

# Montage10k
ENV WORKDIR /code/smartscale/hflowrunner/data/0.25/input
ENV METRIC_COLLECTOR <METRIC_COLLECTOR_IP>:9002
ENV METRIC_COLLECTOR_TYPE visor

# Montage143
#ENV WORKDIR /workdir

# MolecularDynamicsParameterStudy
ENV WORKDIR /workdir
COPY md_amqp2.json md_amqp2.json

CMD ping hflow_redis

#CMD hflow run code/smartscale/hflowrunner/data/0.25/workdir/dag.json -s -p hyperflow-monitoring-plugin
#CMD hflow run md_amqp2.json -s -p hyperflow-monitoring-plugin
#CMD hflow run hyperflow/examples/Montage143/workflow_decorated.json -s -p hyperflow-monitoring-plugin