#!/bin/bash

shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$2_us_state.shp tiger.states > $1/zz21_states.sql
#shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$2_us_county.shp tiger.counties > $1/zz22_counties.sql
#shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$2_us_cd$3.shp tiger.congress > $1/zz23_congress.sql
#shp2pgsql -s 4269:4326 -g geom -W "latin1" tl_$2_us_zcta5$4.shp tiger.postal > $1/zz24_postal.sql
