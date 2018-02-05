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
# PostGIS base image doesn't unzip
RUN unzip tl_${version}_us_state
#RUN unzip tl_${version}_us_county
#RUN unzip tl_${version}_us_cd${congress}
#RUN unzip tl_${version}_us_zcta5${census}

#
# Configure and load the database
#
FROM mdillon/postgis
ENV version 2017
ENV congress 115
ENV census 10
ADD src/main/sh/*.sh /scripts/

# Set up the database in /data
WORKDIR /data
RUN chown postgres:postgres /data
USER postgres
RUN initdb -A trust -D /data
RUN /scripts/create.sh

# Unpack, transform and load the shapefiles
COPY --from=0 /tiger/*state* /shapefiles/
WORKDIR /shapefiles
RUN /scripts/load.sh ${version} ${congress} ${census}
RUN /scripts/transform.sh
RUN /scripts/clean.sh
#
# Create an image with only the DB
#
