#!/usr/bin/env bash
docker-compose up -d
echo "Waiting for 40 seconds for Artifactory to start"
sleep 40