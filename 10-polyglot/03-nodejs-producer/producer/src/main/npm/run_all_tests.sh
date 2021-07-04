#!/usr/bin/env bash

set -o errexit

echo "Running tests with mounted contracts"
./run_tests.sh
echo "Running tests with contracts taken from external repo"
./run_tests_for_external.sh
echo "Running tests with contracts taken from git"
./run_tests_from_git.sh