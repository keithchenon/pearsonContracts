#!/usr/bin/env bash

set -o errexit

# setup
pushd src/main/npm
    ./run_all_tests.sh
popd