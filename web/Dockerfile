FROM openjdk:8
VOLUME /tmp
ADD build/libs/web.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
