#!/bin/bash

./convert.sh

pushd target/stubs
	./mvnw org.springframework.cloud:spring-cloud-contract-maven-plugin:2.0.2.RELEASE:run
popd