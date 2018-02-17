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

The PostGIS database is built from TIGER geographic data the US census site. The first step is to download the
shapefiles to a local volume. Once built, it only needs to be updated yearly when the Census Bureau updates the data.

```bash
docker build --file geolocation/db/Dockerfile.gisdata --tag tracking/gisdata geolocation/db
docker build --file geolocation/db/Dockerfile.db --tag tracking/db geolocation/db
```

The `gisdata` container uses the following `ENV` variables:

| Name     | Value | Description         |
|----------|------:|---------------------|
| YEAR     |  2017 | Shapefile version   |
| CONGRESS |   115 | Meeting of Congress |
| CENSUS   |    10 | 2010 census         |


The database is built as follows:
1. Create a `gisdata` volume for the raw GIS data
2. Download the GIS data
3. Create a `trackingdb` volume for the PostGIS database
4. Create and load the database

```bash
docker volume create gisdata
docker run --mount src=gisdata,target=/gisdata tracking/gisdata
docker volume create trackingdb
docker run --mount src=gisdata,target=/gisdata --mount src=trackingdb,target=/var/lib/postgresql/data tracking/db --single
```

Once built, the GIS database can be opened using the basic PostGIS docker container

```bash
docker run --mount src=trackingdb,target=/var/lib/postgresql/data --publish 5432:5432 mdillon/postgis
```

For testing, you can connect to the database using psql

```bash
psql postgresql://localhost:5432 -U postgres
```

### Build the Application

```bash
gradle geolocation:app:bootJar
docker build geolocation/app --tag tracking/geolocation
docker run -p 8080:8080 tracking/geolocation
```
