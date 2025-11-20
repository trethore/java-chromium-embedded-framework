param(
    [string]$JcefJar = "$PSScriptRoot\\out\\Release\\jcef.jar",
    [string]$NativeDir = "$PSScriptRoot\\out\\Release",
    [switch]$SkipCompile
)

if (-not $IsWindows) {
    Write-Error "Run this from Windows PowerShell (not WSL).";
    exit 1
}

if (-not (Test-Path $JcefJar)) {
    Write-Error "Could not find jcef.jar at $JcefJar. Pass -JcefJar explicitly.";
    exit 1
}

if (-not (Test-Path $NativeDir)) {
    Write-Error "Could not find native directory at $NativeDir. Pass -NativeDir explicitly.";
    exit 1
}

$buildDir = Join-Path $PSScriptRoot ".keyinject_build"
if (-not (Test-Path $buildDir)) { New-Item -ItemType Directory -Path $buildDir | Out-Null }

if (-not $SkipCompile) {
    Write-Host "Compiling KeyInjectDirectTest..."
    & javac -cp $JcefJar -d $buildDir "java\\tests\\junittests\\KeyInjectDirectTest.java"
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

$classpath = "$buildDir;$JcefJar"

Push-Location $NativeDir
Write-Host "Running KeyInjectDirectTest from $NativeDir ..."
& java -cp $classpath -Djava.library.path=$NativeDir tests.junittests.KeyInjectDirectTest
$code = $LASTEXITCODE
Pop-Location

if ($code -eq 0) {
    Write-Host "`nSUCCESS: Direct CefKeyEvent path is working."
} else {
    Write-Error "`nFAIL (exit $code). See output above."
}

exit $code
