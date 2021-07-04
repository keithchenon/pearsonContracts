#!/usr/bin/env bash
M2_SETTINGS_REPO_USERNAME=admin
M2_SETTINGS_REPO_PASSWORD=password

GROUP="com/example"
ARTIFACT="05-04-restdocs-from-dsl"
VERSION="0.0.1-RELEASE"
STUBS="${ARTIFACT}-${VERSION}-stubs.jar"
JAR="${ARTIFACT}-${VERSION}.jar"
POM="${ARTIFACT}-${VERSION}.pom"

function upload() {
	local artifactName="${1}"
	REPO_WITH_BINARIES_FOR_UPLOAD="localhost:8081/artifactory/libs-release-local/${GROUP}/${ARTIFACT}/${VERSION}/${artifactName}"
	echo "Uploading artifact to [${REPO_WITH_BINARIES_FOR_UPLOAD}]"
	curl -u "${M2_SETTINGS_REPO_USERNAME}:${M2_SETTINGS_REPO_PASSWORD}" -X PUT "${REPO_WITH_BINARIES_FOR_UPLOAD}" --upload-file "${artifactName}" --fail
}

upload "${STUBS}"
upload "${JAR}"
upload "${POM}"