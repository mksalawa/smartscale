FROM ruby:2.3

RUN gem install --no-ri --no-rdoc hyperflow-amqp-executor
COPY executor_config.yml executor_config.yml

# Montage10k, Montage143
ENV PATH $PATH:/code/smartscale/hflowrunner/Montage_v3.3_patched_4/bin

# MolecularDynamicsParameterStudy

# Montage143
ENV PATH $PATH:/workdir
COPY run-cmd-openmp.sh /workdir/run-cmd-openmp.sh
COPY make-movie.sh /workdir/make-movie.sh
RUN chmod +x /workdir/run-cmd-openmp.sh /workdir/make-movie.sh

CMD ping -c 10 hflow_rabbitmq && hyperflow-amqp-executor executor_config.yml
