FROM ruby:2.3

RUN gem install --no-ri --no-rdoc hyperflow-amqp-executor
ENV AMQP_URL amqp://hflow_rabbitmq:5672

COPY ./examples /smartscale/examples
RUN chmod a+x /smartscale/examples/bin
ENV PATH $PATH:/smartscale/examples/bin

CMD hyperflow-amqp-executor
