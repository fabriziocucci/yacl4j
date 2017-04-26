#!/bin/bash

if [[ ($# -gt 2) ]]; then
	echo ""
	echo "Wrong number of arguments! This script should be invoked as follows:"
	echo ""
	echo "$0 [<source-branch> [<target-branch>]]"
	echo ""
	echo "where:"
	echo "<source-branch> defaults to the current branch, if not specified"
	echo "<target-branch> defaults to 'master', if not specified"
	exit -1
fi

sourceBranch=${1:-$(git rev-parse --abbrev-ref HEAD)}
targetBranch=${2:-master}

# synchronise local and remote branches
git checkout $sourceBranch && git pull 		&& \
git checkout $targetBranch && git pull 		&& \

# rebase
git checkout $sourceBranch 					&& \
git rebase $targetBranch 					&& \

# merge and push
git checkout $targetBranch 					&& \ 
git merge --no-ff --no-edit $sourceBranch 	&& \
git push									&& \

# delete local and remote branches
git branch -D $sourceBranch					&& \
git push -d origin $sourceBranch