FROM openjdk:9
VOLUME /tmp
ADD build/libs/app.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]