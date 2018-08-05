# Locale basics

Provides very basic language and currency information about a locale using Java built-in resources.

## Technologies

- Spring application
- WebFlux
- Netty
- Logging
- Monitoring
- Tracing

# Cloud environments

## Docker

From project root directory

```bash
./gradlew basics:bootJar
docker build basics --tag tracking/basics
docker run -p 8080:8080 tracking/basics
```
