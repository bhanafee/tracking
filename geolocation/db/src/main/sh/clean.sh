#!/bin/bash
set -e

pg_ctl -D /data start

psql <<EOF
\c locator
DROP SCHEMA tiger CASCADE;
VACUUM FULL ANALYZE;
EOF

pg_ctl -D /data stop
