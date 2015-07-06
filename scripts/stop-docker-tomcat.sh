#!/bin/bash

docker_container_id="${CONTAINER_ID}"

echo -n "Stoping docker container"
docker stop ${docker_container_id}

docker rm ${docker_container_id}