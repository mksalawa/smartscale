FROM java:8

EXPOSE 9002

## Something like this should get it working
#RUN apt-get update && apt-get install -y git
#RUN git clone <REPO> /smartscale

WORKDIR /smartscale
COPY . /smartscale

RUN /smartscale/gradlew build
CMD while true; do /smartscale/gradlew run; sleep 20; done