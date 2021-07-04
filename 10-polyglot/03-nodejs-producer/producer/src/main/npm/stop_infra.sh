#!/bin/bash

set -o errexit

pushd docker
    docker-compose kill || echo "Failed to kill docker"
    yes | docker-compose rm -v || echo "Failed to kill docker"
    docker-compose -f docker-compose-only-mongo.yml kill || echo "Failed to kill docker"
popd

./stop_stubrunner.sh