#!/bin/bash

set -o errexit

./stop_infra.sh

echo "Removing stored artifacts"
rm -rf ~/.m2/repository/com/example/bookstore

echo "Fetching submodules"
pushd ../../../../../
    git submodule init
    git submodule update
    git submodule foreach git pull origin master
popd

echo "Building docker"
pushd docker
    docker-compose build
    docker-compose up -d
popd

echo "Waiting for 30 seconds for artifactory to boot properly"
sleep 30
