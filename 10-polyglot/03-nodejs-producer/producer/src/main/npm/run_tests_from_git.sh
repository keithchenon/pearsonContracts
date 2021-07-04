#!/bin/bash

set -o errexit

echo -e "\n\nRunning tests from GIT\n\n"

# Start docker infra
trap './stop_infra.sh && pkill -f "node app"' EXIT
./setup_infra_without_artifactory.sh

# Kill & Run app
pkill -f "node app" || echo "Failed to kill app"
echo "Working around certificate issues" && npm config set strict-ssl false
yes | npm install || echo "Failed to install packages"
nohup node app &

# Execute contract tests
./run_contract_tests_from_git.sh

# Run stub runner
./run_stub_runner_boot_from_git.sh
echo "Wait for 10 seconds for the container to start"
sleep 10

./run_tests_for_stub_runner.sh