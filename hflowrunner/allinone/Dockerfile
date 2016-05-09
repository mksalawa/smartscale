FROM node:4

RUN npm install https://github.com/dice-cyfronet/hyperflow/archive/e0ddc26d398.tar.gz
RUN cd /node_modules/hyperflow/ && npm install https://github.com/dice-cyfronet/hyperflow-monitoring-plugin/archive/master.tar.gz

RUN ln -s /node_modules/hyperflow/

ENV PATH $PATH:node_modules/hyperflow/bin/
ENV REDIS_URL redis://hflow_redis:6379

RUN apt-get update
RUN apt-get -y install rubygems ruby2.1 ruby2.1-dev build-essential libxml2-dev libxslt1-dev rabbitmq-server
RUN apt-get -y install netcat

RUN gem install --no-ri --no-rdoc hyperflow-amqp-executor
COPY executor_config.yml executor_config.yml
CMD service rabbitmq-server start && hyperflow-amqp-executor executor_config.yml