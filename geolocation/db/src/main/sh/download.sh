#!/bin/sh
set -e

export YEAR=$1
export CONGRESS=$2
export CENSUS=$3

# States
wget --no-clobber https://www2.census.gov/geo/tiger/TIGER${YEAR}/STATE/tl_${YEAR}_us_state.zip

# Counties
wget --no-clobber https://www2.census.gov/geo/tiger/TIGER${YEAR}/COUNTY/tl_${YEAR}_us_county.zip

# Congressional districts
wget --no-clobber https://www2.census.gov/geo/tiger/TIGER${YEAR}/CD/tl_${YEAR}_us_cd${CONGRESS}.zip

# ZIP code tabulation areas
wget --no-clobber https://www2.census.gov/geo/tiger/TIGER${YEAR}/ZCTA5/tl_${YEAR}_us_zcta5${CENSUS}.zip
