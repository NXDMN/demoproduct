#!/bin/bash
if [ "$(docker ps -a | grep my-postgres)" ]; then
    docker rm -f my-postgres
fi

docker run -d --name my-postgres -p 5433:5432 demo-postgres