#!/bin/bash
set -e

pg_ctl -D /data start

shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$1_us_state.shp tiger.states | psql -d locator
#shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$1_us_county.shp tiger.counties | psql -d locator
#shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$1_us_cd$2.shp tiger.congress | psql -d locator
#shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$1_us_zcta5$3.shp tiger.postal | psql -d locator

pg_ctl -D /data stop
