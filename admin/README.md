# Tracking admin

Administer and explore the other services.

## Technologies

- Play Framework
- Logging
- Monitoring
- Tracing
- JPA

# Cloud environments

## Docker

```bash
gradle admin:dist
docker build admin --tag tracking/admin
export APPLICATION_SECRET='yourApplicationSecret'
docker run -p 8080:9000 -e APPLICATION_SECRET tracking/admin
```
