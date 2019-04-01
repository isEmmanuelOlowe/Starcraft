<<<<<<< HEAD
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
=======
function exe() {
	javac $1*.java
}

exe alphacraft/
exe alphacraft/controllers/
exe alphacraft/controllers/splash/
exe alphacraft/controllers/display/
exe alphacraft/engine/optimiser/
exe alphacraft/engine/resources/
>>>>>>> f26301127eeee0e93ef947f31d799dec1538de7b
