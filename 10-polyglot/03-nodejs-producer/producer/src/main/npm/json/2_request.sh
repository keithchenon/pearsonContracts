#!/bin/bash

set -e

PORT="${PORT:-9876}"

curl --fail -X GET http://localhost:"${PORT}"/api/books
