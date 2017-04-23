#!/bin/bash

updateReadme() {
	
	releaseVersion=$1
	
	NEW_README_FILE=README.md.new
	OLD_README_FILE=README.md
	MAVEN_VERSION_REGEXP="(.*<version>)(0.8.0)(</version>)"
	GRADLE_VERSION_REGEXP="(.*compile group: \"com.github.fabriziocucci\", name:\"yacl4j-core\", version: \")(0.8.0)(\")"

	while IFS='' read -r line || [[ -n "$line" ]]; do
		if [[ $line =~ $MAVEN_VERSION_REGEXP ]]; then
			mavenVersionPrefix=${BASH_REMATCH[1]}
			mavenVersionSuffix=${BASH_REMATCH[3]}
			echo "$mavenVersionPrefix$releaseVersion$mavenVersionSuffix" >> $NEW_README_FILE
		elif [[ $line =~ $GRADLE_VERSION_REGEXP ]]; then
			gradleVersionPrefix=${BASH_REMATCH[1]}
			gradleVersionSuffix=${BASH_REMATCH[3]}
			echo "$gradleVersionPrefix$releaseVersion$gradleVersionSuffix" >> $NEW_README_FILE
		else 
			echo "$line" >> $NEW_README_FILE
		fi
	done < README.md

	rm $OLD_README_FILE
	mv $NEW_README_FILE $OLD_README_FILE

}

updateChangelog() {
	
	releaseVersion=$1
	
	previousVersion=
	NEW_CHANGELOG_FILE=CHANGELOG.md.new
	OLD_CHANGELOG_FILE=CHANGELOG.md
	UNRELEASED_REGEXP="(\#\# \[Unreleased\]\(https://github.com/fabriziocucci/yacl4j/compare/yacl4j-)(.*)(...HEAD\))"

	touch $NEW_CHANGELOG_FILE

	while IFS='' read -r line || [[ -n "$line" ]]; do
		if [[ $line =~ $UNRELEASED_REGEXP ]]; then
			unreleasedPrefix=${BASH_REMATCH[1]}
			previousVersion=${BASH_REMATCH[2]}
			unreleasedSuffix=${BASH_REMATCH[3]}
			echo "$unreleasedPrefix$releaseVersion$unreleasedSuffix" >> $NEW_CHANGELOG_FILE
			echo "" >> $NEW_CHANGELOG_FILE
			echo "## [yacl4j-$releaseVersion](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-$previousVersion...yacl4j-$releaseVersion)" >> $NEW_CHANGELOG_FILE
		else
			echo "$line" >> $NEW_CHANGELOG_FILE
		fi
	done < CHANGELOG.md

	rm $OLD_CHANGELOG_FILE
	mv $NEW_CHANGELOG_FILE $OLD_CHANGELOG_FILE

}

USAGE="No release version specified. Please try again like this: $0 <releaseVersion>"

if [ "$#" == "0" ]; then
	echo "$USAGE"
	exit 1
fi

releaseVersion=$1

updateReadme $releaseVersion										&& \
updateChangelog $releaseVersion										&& \
git add README.md CHANGELOG.md										&& \
git commit -m "Updated README.md and CHANGELOG.md before release"	&& \
git push															&& \
./mvnw --batch-mode release:clean release:prepare release:perform -DreleaseVersion=${releaseVersion} -P release