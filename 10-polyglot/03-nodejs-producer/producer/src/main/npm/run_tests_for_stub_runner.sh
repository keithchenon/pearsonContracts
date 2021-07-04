#!/usr/bin/env bash

set -o errexit

pushd json

    ./1_request.sh
    ./2_request.sh

popd
