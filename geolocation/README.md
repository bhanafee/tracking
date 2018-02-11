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
docker build geolocation/db --file geolocation/db/Dockerfile.gisdata --tag tracking/gisdata
docker build geolocation/db --file geolocation/db/Dockerfile.db --tag tracking/db
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
docker run --mount src=gisdata,target=/gisdata --mount src=trackingdb,target=/var/lib/postgresql/data tracking/db
```

Once built, the GIS database can be run using the basic PostGIS docker container

```
docker run mdillon/postgis --mount src=trackingdb,target=/var/lib/postgresql/data
```

### Build the Application

```bash
gradle geolocation:app:bootJar
docker build geolocation/app --tag tracking/geolocation
docker run -p 8080:8080 tracking/geolocation
```
