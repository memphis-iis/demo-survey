#!/bin/bash

# This is a manual Maven-site-to-GitHub-project-pages script
#
# It is inspired by the GitHub project X1011/git-directory-deploy
#
# It assumes:
#     * You are currently on master
#     * All local changes stashed or committed
#     * The gh-pages branch exists

# Stop on errors
set -e

# We firgure where to work based on the fact that this script should be in the
# root of the project (next to the pom.xml file)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

export WT="$SCRIPT_DIR/target/site"

# Ensure we're on master - this shouldn't do anything
git checkout master

# Clean target directory and create the site
mvn clean site

# Ensure we have a site
if [ ! -d "$WT" ]; then
    echo "PANIC: Could not find directory $WT"
    exit 1
fi

# Our actual work: set up HEAD/index to point to gh-pages and $WT, then
# add/commit/push our site, then reset everything

git symbolic-ref HEAD refs/heads/gh-pages
git --work-tree="$WT" reset --mixed --quiet

git --work-tree="$WT" add --all
git --work-tree="$WT" commit -m "maven site commit $(date) from $(hostname)"
git push origin gh-pages

git symbolic-ref HEAD refs/heads/master
git reset --mixed
