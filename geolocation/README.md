# Geolocation queries

Queries and tags a location based on a geolocation database

## Technologies

- PostGis
- Spring REST (HAL)
- JPA
- ??

## Queries supported

- Longitude/latitude -> areas

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
   - Unzip the shapefiles
2. Build the database
   - Add the scripts
   - Copy the TIGER shapefiles
   - Generate load scripts using `shp2pgsql`
   - Initialize the database, which runs the scripts
     - Create the database and schema with PostGIS extensions
     - Load the data to an intermediate schema
     - Insert the data into the final schema
     - Drop the intermediate schema and vacuum the database
3. Create a new image with just the final DB schema

```bash
docker build geolocation/db --tag tracking/db
```

### Build the Application

```bash
gradle basics:bootJar
docker build geolocation --tag tracking/geolocation
docker run -p 8080:8080 tracking/geolocation
```
