#!/bin/bash
set -e

UNZIP="python3 -m zipfile -e"
CONVERT="shp2pgsql -s 4269:4326 -g geom -W 'latin1'"
SOURCES=(state county cd115 zcta510)
TARGET=${SCRIPTS}zdata.sql
VIEW=${SCRIPTS}zview.sql

cd $STAGE

for TABLE in "${SOURCES[@]}"
do
  $UNZIP /gisdata/*_${TABLE}.zip .
  $CONVERT *_${TABLE}.shp tiger_staging.${TABLE} >> $TARGET
  case ${TABLE} in
    'state'   ) echo "UNION SELECT 'state'  AS tag, stusps    AS id, name      AS description, geom AS geom FROM tiger_staging.${TABLE}" >> ${VIEW}
                ;;
    'county'  ) echo "UNION SELECT 'county' AS tag, name      AS id, namelsad  AS description, geom AS geom FROM tiger_staging.${TABLE}" >> ${VIEW}
                ;;
    cd*       ) echo "UNION SELECT 'cd'     AS tag, cd115fp   AS id, namelsad  AS description, geom AS geom FROM tiger_staging.${TABLE}" >> ${VIEW}
                ;;
    zcta5*    ) echo "UNION SELECT 'zcta'   AS tag, zcta5ce10 AS id, zcta5ce10 AS description, geom AS geom FROM tiger_staging.${TABLE}" >> ${VIEW}
                ;;
  esac
done

echo \; >>  ${VIEW}
