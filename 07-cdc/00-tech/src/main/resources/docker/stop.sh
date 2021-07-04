#!/usr/bin/env bash

set -o errexit

docker-compose kill || echo "Failed to kill apps"
yes | docker-compose rm -v artifactory || echo "Failed to kill apps"
docker stop $(docker ps | grep artifactory | awk -F' ' '{print $1}') || echo "Failed to stop artifactory"
docker stop $(docker ps | grep web | awk -F' ' '{print $1}') || echo "Failed to stop gitlab web"