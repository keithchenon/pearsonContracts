#!/usr/bin/env bash

function my_ip() {
	case "`uname`" in
	  Darwin* )
		echo "host.docker.internal"
		;;
	  * )
		ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1' | head -n 1
		;;
	esac
}

export EXTERNAL_IP="$( my_ip )"
echo "External IP will be [${EXTERNAL_IP}]"

echo "Starting Artifactory & GitLab"
docker-compose up -d

echo "Waiting 30 seconds for apps to start"
sleep 30