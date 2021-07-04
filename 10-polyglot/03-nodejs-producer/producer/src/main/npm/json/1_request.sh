#!/bin/bash

set -e

PORT="${PORT:-9876}"

curl -H "Content-Type:application/json"  --fail -X POST --data @1_request.json http://localhost:"${PORT}"/api/books
