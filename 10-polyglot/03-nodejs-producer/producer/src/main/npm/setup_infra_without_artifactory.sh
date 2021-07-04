#!/bin/bash

set -o errexit

pushd docker
    docker-compose -f docker-compose-only-mongo.yml build
    docker-compose -f docker-compose-only-mongo.yml up -d
popd
