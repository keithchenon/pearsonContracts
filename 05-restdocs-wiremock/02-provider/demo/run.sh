#!/bin/bash

mvn -o org.springframework.cloud:spring-cloud-contract-maven-plugin:2.1.0.BUILD-SNAPSHOT:run -Dspring.cloud.contract.verifier.stubs=com.example:05-02-provider