FROM tracking/tiger
ENV version 2017
WORKDIR /tiger

FROM mdillon/postgis
ADD src/main/sh/create.sh /scripts/create.sh
WORKDIR /data
RUN chown postgres:postgres /data
USER postgres
RUN initdb -A trust -D /data
RUN /scripts/create.sh
