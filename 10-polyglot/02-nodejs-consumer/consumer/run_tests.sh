#!/usr/bin/env bash

set -o errexit

# setup
./stop_stubrunner.sh
# cleanup
trap './stop_stubrunner.sh' EXIT

# given
./start_stubrunner.sh

# when
result="$(node/node app)"

# then
if [ "${result}" !=  '["marcin","josh"]' ];then
    echo "FAILED TO RETRIEVE PROPER RESULT. GOT [${result}]"
    exit 1
else
    echo "SUCCESS! GOT:"
    echo "${result}"
    exit 0
fi