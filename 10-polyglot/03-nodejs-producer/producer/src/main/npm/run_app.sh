#!/bin/bash

set -o errexit

echo -e "\n\nRunning the application\n\n"
./stop_infra.sh
./setup_infra_without_artifactory.sh

# Kill & Run app
pkill -f "node app" || echo "Failed to kill app"
echo "Working around certificate issues" && npm config set strict-ssl false
yes | npm install || echo "Failed to install packages"
nohup node app &