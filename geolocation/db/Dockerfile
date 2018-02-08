#
# Stage the TIGER data
#
FROM tracking/tiger
ENV version 2017
# Which meeting of US Congress, affects shapefile names
ENV congress 115
# Which census decade, affects shapefile names
ENV census 10
WORKDIR /tiger
# PostGIS base image doesn't include unzip
RUN mkdir unzipped
RUN unzip tl_${version}_us_state -d unzipped
#RUN unzip tl_${version}_us_county -d unzipped
#RUN unzip tl_${version}_us_cd${congress} -d unzipped
#RUN unzip tl_${version}_us_zcta5${census} -d unzipped

#
# Configure and load the database
#
FROM mdillon/postgis
ENV version 2017
ENV congress 115
ENV census 10
ADD src/main/sh/   /scripts/
ADD src/main/sql/  /docker-entrypoint-initdb.d/

# Unpack, transform and load the shapefiles
COPY --from=0 /tiger/unzipped/ /shapefiles
WORKDIR /shapefiles
RUN /scripts/load.sh /docker-entrypoint-initdb.d ${version} ${congress} ${census}
USER postgres

# Set up the database
RUN /usr/local/bin/docker-entrypoint.sh postgres --single

#
# Create an image with only the DB
#
FROM mdillon/postgis
USER postgres
COPY --from=1 ${PGDATA}/ ${PGDATA}
