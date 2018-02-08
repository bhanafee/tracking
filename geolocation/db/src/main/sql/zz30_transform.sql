CREATE TABLE areas.monitored (
  tiger VARCHAR(20),
  abbrev CHAR(2),
  name VARCHAR(100),
  geom geometry(MultiPolygon, 4326));
INSERT INTO areas.monitored (tiger, abbrev, name, geom)
  SELECT s.geoid, s.stusps, s.name, s.geom
  FROM tiger.states AS s;
-- INSERT INTO areas.monitored (tiger, name, geom)
--   SELECT c.geoid, c.name, c.geom
--   FROM tiger.counties AS c;
CREATE INDEX idx_monitored_geom ON areas.monitored USING gist(geom);
