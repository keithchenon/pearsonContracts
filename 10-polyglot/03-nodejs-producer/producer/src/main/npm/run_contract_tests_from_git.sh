#!/bin/bash

set -o errexit

CURRENT_DIR="$( pwd )"

# part related to simulation of cloning a git repository
echo "Let's simulate that we are running a git server. We only do that to run the whole test locally and not to push code to GitHub"
fixed="$( date '+%Y-%m-%d-%H-%M-%S' )"
rm -rf "${CURRENT_DIR}"/build || echo "Failed to delete"
dir="${CURRENT_DIR}/build/${fixed}"
mkdir -p "${dir}"
contracts_dir="${dir}/contracts_git"
cp -r "${CURRENT_DIR}"/git_repository "${contracts_dir}"
mv "${contracts_dir}"/git "${contracts_dir}"/.git
rm -f "${CURRENT_DIR}/build/current_git"
ln -s "${contracts_dir}" "${CURRENT_DIR}/build/current_git"
echo "Now we have a git repository available under [${contracts_dir}]"

SC_CONTRACT_DOCKER_VERSION="${SC_CONTRACT_DOCKER_VERSION:-2.1.0.BUILD-SNAPSHOT}"
APP_IP="$( ./whats_my_ip.sh )"
APP_PORT="${APP_PORT:-3000}"
APPLICATION_BASE_URL="http://${APP_IP}:${APP_PORT}"
PROJECT_NAME="${PROJECT_NAME:-bookstore}"
PROJECT_GROUP="${PROJECT_GROUP:-com.example}"
PROJECT_VERSION="${PROJECT_VERSION:-0.0.1.RELEASE}"

echo "Sc Contract Version [${SC_CONTRACT_DOCKER_VERSION}]"
echo "Application URL [${APPLICATION_BASE_URL}]"
echo "Project Version [${PROJECT_VERSION}]"

# If you want to work offline just attach this volume
# -v "${HOME}/.m2/:/root/.m2:ro"
mkdir -p build/spring-cloud-contract/output
docker pull springcloud/spring-cloud-contract:"${SC_CONTRACT_DOCKER_VERSION}"
docker run  --rm -e "PUBLISH_STUBS_TO_SCM=true" \
-e "PUBLISH_ARTIFACTS=false" -e "APPLICATION_BASE_URL=${APPLICATION_BASE_URL}" \
-e "PROJECT_NAME=${PROJECT_NAME}" -e "PROJECT_GROUP=${PROJECT_GROUP}" \
-e "REPO_WITH_BINARIES_URL=${ARTIFACTORY_URL}" -e "PROJECT_VERSION=${PROJECT_VERSION}" \
-e "EXTERNAL_CONTRACTS_REPO_WITH_BINARIES_URL=git://file:/contracts_git/" \
-e "EXTERNAL_CONTRACTS_ARTIFACT_ID=${PROJECT_NAME}" \
-e "EXTERNAL_CONTRACTS_GROUP_ID=${PROJECT_GROUP}" \
-e "EXTERNAL_CONTRACTS_VERSION=${PROJECT_VERSION}" \
-v "${contracts_dir}/:/contracts_git:rw" \
-v "${CURRENT_DIR}/build/spring-cloud-contract/output:/spring-cloud-contract-output/" \
 springcloud/spring-cloud-contract:"${SC_CONTRACT_DOCKER_VERSION}"

docker run --rm -v "${CURRENT_DIR}/build/spring-cloud-contract/output:/spring-cloud-contract-output/" springcloud/spring-cloud-contract:"${SC_CONTRACT_DOCKER_VERSION}" chown -R $(id -u):$(id -g) "/spring-cloud-contract-output/"
