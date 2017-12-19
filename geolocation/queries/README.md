# Geolocation services

Converts longitude/latitude to enclosing areas list

## Technologies

- Play Framework
- Logging
- Monitoring
- Tracing
- JPA

## Queries supported

- Longitude/latitude -> areas

# Cloud environments

## Docker

```bash
gradle geolocation:queries:dist
docker build geolocation/queries --tag tracking/geoqueries
export APPLICATION_SECRET='yourApplicationSecret'
docker run -p 8080:9000 -e APPLICATION_SECRET tracking/geoqueries
```
