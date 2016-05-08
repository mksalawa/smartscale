FROM node:4

RUN npm install https://github.com/dice-cyfronet/hyperflow/archive/e0ddc26d398.tar.gz
RUN cd /node_modules/hyperflow/ && npm install https://github.com/dice-cyfronet/hyperflow-monitoring-plugin/archive/master.tar.gz

RUN ln -s /node_modules/hyperflow/

ENV PATH $PATH:node_modules/hyperflow/bin/
ENV REDIS_URL redis://hflow_redis:6379
CMD ping hflow_redis
