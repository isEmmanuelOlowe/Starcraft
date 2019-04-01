Write-Host "Building Project: alphacraft"

$directories = @()

function addToArray($item) {
  $script:directories += $item
}

addToArray("alphacraft/")
addToArray("alphacraft/controllers/")
addToArray("alphacraft/controllers/splash/")
addToArray("alphacraft/controllers/display/")
addToArray("alphacraft/engine/optimiser/")
addToArray("alphacraft/engine/resources/")

foreach($item in $directories){
  Write-Host "Building $($item)"
  if (Test-Path "$($item)*.class")
  {
    Remove-Item "$($item)*.class"
    }
    javac "$($item)*.java"
}


Write-Host "Building Complete"
