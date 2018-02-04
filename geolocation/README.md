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

### Build the PostGIS Database

This step downloads the TIGER shapefiles from the US Census web site. Once built, it only needs to be updated yearly
when the Census Bureau updates the data. 

```bash
docker build geolocation/db -f geolocation/db/Dockerfile.tiger --tag tracking/tiger
```

Build the DB itself. This is a multi-stage build.

1. Mount the TIGER data from above
2. Build the database
   - Create the DB and schema
   - Enable the PostGIS extension
   - For each TIGER file
     - Unzip the file
     - Use `pgsql` to transform it to loadable PSQL
     - Load the data
3. Create the final image with just the DB

```bash
docker build geolocation/db --tag tracking/db
```

### Build the Application

```bash
gradle basics:bootJar
docker build geolocation --tag tracking/geolocation
docker run -p 8080:8080 tracking/geolocation
```
