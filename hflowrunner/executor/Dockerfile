FROM ruby:2.3

RUN gem install --no-ri --no-rdoc hyperflow-amqp-executor
COPY executor_config.yml executor_config.yml
CMD hyperflow-amqp-executor executor_config.yml