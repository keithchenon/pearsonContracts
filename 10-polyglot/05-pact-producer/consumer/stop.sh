#!/usr/bin/env bash

set -o errexit

docker-compose kill || echo "Failed to kill apps"
yes | docker-compose rm -v || echo "Failed to kill apps"