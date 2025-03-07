#!/bin/bash

# remove go image
if [ "$(docker image ls | grep demo-go)" ]; then
    docker image rm -f demo-go
fi

# build go image
docker build -t demo-go ./backend 