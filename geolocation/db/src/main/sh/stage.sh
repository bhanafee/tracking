#!/bin/bash
set -e

UNZIP="python3 -m zipfile -e"
CONVERT="shp2pgsql -s 4269:4326 -g geom -W 'latin1'"
SOURCES=(state county cd115 zcta510)
TARGET=${SCRIPTS}zdata.sql

cd $STAGE

for TABLE in "${SOURCES[@]}"
do
  $UNZIP /gisdata/*_${TABLE}.zip .
  $CONVERT *_${TABLE}.shp tiger_staging.${TABLE} >> $TARGET
done
