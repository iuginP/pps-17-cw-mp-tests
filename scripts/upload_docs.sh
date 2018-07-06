#!/bin/bash

if [ -n "$GITHUB_API_KEY" ]
  [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
  [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing docs...\n"
  find . -type d -name 'latest-docs/java' -exec cp -R '{}' $HOME/latest-javadoc ';'
  find . -type d -name 'latest-docs/scala' -exec cp -R '{}' $HOME/latest-scaladoc ';'
  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${$GITHUB_API_KEY}@github.com/iuginP/pps-17-cw-mp-tests gh-pages > /dev/null
  cd gh-pages
  git rm -rf ./java
  git rm -rf ./scala
  cp -Rf $HOME/latest-javadoc ./java
  cp -Rf $HOME/latest-scaladoc ./scala
  git add -f .
  git commit -m "Latest docs on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null
  echo -e "Published docs to gh-pages.\n"
fi
