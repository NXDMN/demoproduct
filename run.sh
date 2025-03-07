#!/bin/bash
if [ "$(docker ps -a | grep my-postgres)" ]; then
    docker rm -f my-postgres
fi

# run database container
docker run -d --name my-postgres -v ./database/my-postgres-volume:/var/lib/postgresql/data -p 5433:5432 --network my-network demo-postgres

# the postgres container need some time to init since got volume
sleep .5

# run server container
docker run -it --rm --name my-go -p 3000:3000 --network my-network demo-go