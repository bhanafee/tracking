# Geolocation queries

Queries and tags a location based on a geolocation database

## Technologies

- PostGis
- Spring REST (HAL)
- JPA
- ??

## Queries supported

- Longitude/latitude -> areas

## Tasks supported

- Acquire TIGER shapefiles
- Load shapefiles
- Load CSV-formatted localization data
- Explore database


# Cloud environments

## Docker

### Acquire TIGER files

```bash
docker build geolocation -f geolocation/Dockerfile.tiger --tag tracking/tiger
```

### Build the PostGIS Database


### Build the Application

```bash
gradle basics:bootJar
docker build geolocation --tag tracking/geolocation
docker run -p 8080:8080 tracking/geolocation
```
