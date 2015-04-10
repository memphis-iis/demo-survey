#!/bin/bash

# We firgure where to work based on the fact that this script should be in the
# root of the project (next to the pom.xml file)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

export WT="$SCRIPT_DIR/target/site"

#mvn clean site

git --work-tree="$WT" checkout gh-pages
git --work-tree="$WT" add --all
git --work-tree="$WT" commit -m "maven site commit"
#git --work-tree="$WT" push
#git --work-tree="$WT" checkout master

#mvn clean
