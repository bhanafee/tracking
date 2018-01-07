# Web front end

Secured front end for access to the cloud services

## Technologies

- Spring application
- Lombok
- Tomcat
- WebMVC
- Logging
- Monitoring (Actuator)
- Tracing
- Security

# Cloud environments

## Docker

```bash
gradle web:bootJar
docker build web --tag tracking/web
docker run -p 8080:8080 tracking/web
```

The default user is `user`. Find the generated password GUID on the console output:

```
Using default security password: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

