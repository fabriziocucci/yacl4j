#!/bin/bash

USAGE="No release version specified. Please try again like this: $0 <releaseVersion>"

if [ "$#" == "0" ]; then
	echo "$USAGE"
	exit 1
fi

releaseVersion=$1

./mvnw --batch-mode release:clean release:prepare release:perform -DreleaseVersion=${releaseVersion} -P release