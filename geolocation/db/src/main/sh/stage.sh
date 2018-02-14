#!/bin/bash
set -e

python3 -m zipfile -e /gisdata/*state.zip /stage/
python3 -m zipfile -e /gisdata/*county.zip /stage/

shp2pgsql -s 4269:4326 -g geom -W "latin1" /stage/*_state.shp tiger_staging.state >> /docker-entrypoint-initdb.d/zdata.sql
shp2pgsql -s 4269:4326 -g geom -W "latin1" /stage/*_county.shp tiger_staging.county >> /docker-entrypoint-initdb.d/zdata.sql

rm /stage/*
