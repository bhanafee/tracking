# Device tracking

Tracks movement of known devices

## Technologies

- Akka core
- Akka HTTP
- Akka Logging
- OpenTracing

# Cloud environments

## Docker

From project root directory

```bash
./gradlew track:distTar
docker build track --tag tracking/track
docker run -p 8080:8080 tracking/track
```
