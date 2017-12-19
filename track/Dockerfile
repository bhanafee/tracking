FROM openjdk:8
VOLUME /tmp
ADD build/distributions/track.tar /
ENV JAVA_OPTS=""
EXPOSE 8080
WORKDIR "/track"
ENTRYPOINT ["bin/track"]
