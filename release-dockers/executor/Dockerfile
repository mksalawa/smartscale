FROM ruby:2.3

RUN gem install --no-ri --no-rdoc hyperflow-amqp-executor
ENV AMQP_URL amqp://hflow_rabbitmq:5672

CMD sleep 5 && hyperflow-amqp-executor
