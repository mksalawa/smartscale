FROM java:8

EXPOSE 9002

## Something like this should get it working
# RUN apt-get update && apt-get install -y git
# RUN git clone <REPO> /smartscale
# RUN /smartscale/gradlew build
# CMD /smartscale/gradlew run

## This is a stub of smartscale
COPY listen.sh listen.sh
RUN apt-get update && apt-get install -y netcat
RUN chmod a+x /listen.sh
CMD /listen.sh
