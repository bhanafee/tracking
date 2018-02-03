FROM openjdk:9
VOLUME /tmp
ADD build/distributions/playBinary.tar /
ENV JAVA_OPTS=""
EXPOSE 9000
WORKDIR "/playBinary"
ENTRYPOINT ["bin/playBinary"]
