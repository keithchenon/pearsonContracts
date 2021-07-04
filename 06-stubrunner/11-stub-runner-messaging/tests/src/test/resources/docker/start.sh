#!/usr/bin/env bash
./stop.sh
docker-compose up -d
echo "Waiting for 20 seconds to start"
sleep 20