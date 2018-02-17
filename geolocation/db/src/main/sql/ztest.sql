/* There should be at least 50 states (includes DC and various islands) */
SELECT COUNT(*) FROM tiger_staging.state;
/* There should be over 3,000 counties */
SELECT COUNT(*) FROM tiger_staging.county;

/* The top of Half Dome is in California */
WITH test AS (SELECT ST_SetSRID(ST_MakePoint(-119.5332, 37.7459),4326) AS location) SELECT name FROM tiger_staging.state, test WHERE ST_Intersects(test.location, geom);
/* Mt Diablo is in Contra Costa County */
WITH test AS (SELECT ST_SetSRID(ST_MakePoint(-121.9142, 37.8816),4326) AS location) SELECT name FROM tiger_staging.county, test WHERE ST_Intersects(test.location, geom);
/* Mt Tamalpais is in Congressional District 2 */
WITH test AS (SELECT ST_SetSRID(ST_MakePoint(-122.5965, 37.9235),4326) AS location) SELECT cd115fp, namelsad FROM tiger_staging.cd115, test WHERE ST_Intersects(test.location, geom);
/* Mission Peak is in ZIP code tabulatino area 94539 */
WITH test AS (SELECT ST_SetSRID(ST_MakePoint(-121.8805, 37.5124),4326) AS location) SELECT zcta5ce10 FROM tiger_staging.zcta510, test WHERE ST_Intersects(test.location, geom);
