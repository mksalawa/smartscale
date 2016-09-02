FROM java:8

EXPOSE 9002

## Something like this should get it working
COPY . /smartscale
WORKDIR /smartscale

RUN /smartscale/gradlew build
CMD /smartscale/gradlew run

