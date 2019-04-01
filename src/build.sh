#!/bin/bash
echo "Builing Project: alphacraft"

function addToArray() {
  echo "Building: $1"
  # if [ ! -f $1*.class ]; then
  #   rm $1*.class
  # fi
  javac $1*.java
}

addToArray alphacraft/
addToArray alphacraft/controllers/
addToArray alphacraft/controllers/splash/
addToArray alphacraft/controllers/display/
addToArray alphacraft/engine/optimiser/
addToArray alphacraft/engine/resources/
