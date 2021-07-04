#!/bin/bash

set -o errexit

# Provide the Spring Cloud Contract Docker version
SC_CONTRACT_DOCKER_VERSION="2.1.0.BUILD-SNAPSHOT"
# Spring Cloud Contract Stub Runner properties
STUBRUNNER_PORT="8083"
# Stub coordinates 'groupId:artifactId:version:classifier:port'
STUBRUNNER_IDS="com.example:10-02-producer:0.0.1-SNAPSHOT:stubs:9876"
# Run the docker with Stub Runner Boot
docker run  --rm -e "STUBRUNNER_IDS=${STUBRUNNER_IDS}" \
-e "STUBRUNNER_STUBS_MODE=LOCAL" \
-p "${STUBRUNNER_PORT}:${STUBRUNNER_PORT}" \
-p "9876:9876"  \
-v "${HOME}/.m2/:/root/.m2:ro" \
--detach \
springcloud/spring-cloud-contract-stub-runner:"${SC_CONTRACT_DOCKER_VERSION}"
