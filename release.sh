#!/bin/bash

USAGE="No release version specified. Please try again like this: $0 <releaseVersion>"

if [ "$#" == "0" ]; then
	echo "$USAGE"
	exit 1
fi

releaseVersion=$1

# Because of https://issues.apache.org/jira/browse/MSHADE-182, we need to use a snapshot version of the maven-shade-plugin.
# By default, the maven-release-plugin prevents you to release if you have some snapshot dependency. This is why we need -DignoreSnapshots=true.

mvn --batch-mode release:clean release:prepare release:perform -DignoreSnapshots=true -DreleaseVersion=${releaseVersion} -P release