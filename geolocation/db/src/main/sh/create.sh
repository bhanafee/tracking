#!/bin/bash
set -e

pg_ctl -D /data start

psql <<EOF
CREATE DATABASE locator;
\c locator
CREATE EXTENSION postgis;
CREATE SCHEMA tiger;
CREATE SCHEMA areas;
EOF
