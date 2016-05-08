FROM ruby:2.3

RUN apt-get update
RUN apt-get -y install rabbitmq-server
RUN gem install --no-ri --no-rdoc hyperflow-amqp-executor
COPY executor_config.yml executor_config.yml
CMD service rabbitmq-server start && hyperflow-amqp-executor executor_config.yml