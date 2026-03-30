param(
    [string]$AvdName = "MathIslandTablet_API33",
    [string[]]$Tests = @(
        "com.mathisland.app.feature.map.MapTabletScreenTest",
        "com.mathisland.app.feature.island.IslandOverlaySheetTest",
        "com.mathisland.app.feature.level.RewardOverlayTest"
    ),
    [int]$BootTimeoutSec = 240,
    [int]$TestTimeoutSec = 300,
    [switch]$SkipBuild,
    [switch]$KeepEmulator
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Get-SdkRoot {
    if ($env:ANDROID_HOME) { return $env:ANDROID_HOME }
    if ($env:ANDROID_SDK_ROOT) { return $env:ANDROID_SDK_ROOT }
    $fallback = Join-Path $env:LOCALAPPDATA "Android\Sdk"
    if (Test-Path $fallback) { return $fallback }
    throw "ANDROID_HOME / ANDROID_SDK_ROOT 未设置，且默认 SDK 路径不存在。"
}

function Wait-ForBoot {
    param(
        [string]$AdbPath,
        [int]$TimeoutSec
    )

    & $AdbPath wait-for-device | Out-Null
    $deadline = (Get-Date).AddSeconds($TimeoutSec)
    while ((Get-Date) -lt $deadline) {
        $boot = (& $AdbPath shell getprop sys.boot_completed 2>$null).Trim()
        if ($boot -eq "1") {
            & $AdbPath shell input keyevent 82 | Out-Null
            return
        }
        Start-Sleep -Seconds 2
    }

    throw "模拟器在 ${TimeoutSec}s 内没有完成开机。"
}

function Run-InstrumentationTest {
    param(
        [string]$AdbPath,
        [string]$ClassName,
        [int]$TimeoutSec
    )

    $stdout = [System.IO.Path]::GetTempFileName()
    $stderr = [System.IO.Path]::GetTempFileName()
    try {
        & $AdbPath shell am force-stop com.mathisland.app | Out-Null
        $proc = Start-Process `
            -FilePath $AdbPath `
            -ArgumentList @(
                "shell",
                "am",
                "instrument",
                "-w",
                "-e", "class", $ClassName,
                "com.mathisland.app.test/androidx.test.runner.AndroidJUnitRunner"
            ) `
            -RedirectStandardOutput $stdout `
            -RedirectStandardError $stderr `
            -PassThru

        if (-not $proc.WaitForExit($TimeoutSec * 1000)) {
            $proc.Kill()
            throw "Instrumentation 超时: $ClassName"
        }

        $output = (Get-Content -Path $stdout -Raw) + "`n" + (Get-Content -Path $stderr -Raw)
        if (
            $proc.ExitCode -ne 0 -or
            $output -match "FAILURES!!!" -or
            $output -match "Process crashed" -or
            $output -match "INSTRUMENTATION_FAILED" -or
            $output -notmatch "OK \("
        ) {
            throw "Instrumentation 失败: $ClassName`n$output"
        }

        Write-Host "[PASS] $ClassName"
    } finally {
        Remove-Item -Path $stdout, $stderr -Force -ErrorAction SilentlyContinue
    }
}

$sdkRoot = Get-SdkRoot
$adbPath = Join-Path $sdkRoot "platform-tools\adb.exe"
$emulatorPath = Join-Path $sdkRoot "emulator\emulator.exe"

if (-not (Test-Path $adbPath)) {
    throw "找不到 adb: $adbPath"
}
if (-not (Test-Path $emulatorPath)) {
    throw "找不到 emulator: $emulatorPath"
}

$projectRoot = Split-Path -Parent $PSScriptRoot
$appApk = Join-Path $projectRoot "app\build\outputs\apk\debug\app-debug.apk"
$testApk = Join-Path $projectRoot "app\build\outputs\apk\androidTest\debug\app-debug-androidTest.apk"

try {
    Push-Location $projectRoot

    ./gradlew.bat --stop | Out-Null

    if (-not $SkipBuild) {
        $env:ORG_GRADLE_OPTS = "-Dkotlin.compiler.execution.strategy=in-process"
        ./gradlew.bat clean | Out-Null
        ./gradlew.bat assembleDebug | Out-Null
        ./gradlew.bat :app:packageDebugAndroidTest | Out-Null
    }

    if (-not (Test-Path $appApk) -or -not (Test-Path $testApk)) {
        throw "找不到 app/test APK，构建可能未成功。"
    }

    Start-Process -FilePath $emulatorPath -ArgumentList @("-avd", $AvdName) | Out-Null
    Wait-ForBoot -AdbPath $adbPath -TimeoutSec $BootTimeoutSec

    & $adbPath install -r $appApk | Out-Null
    & $adbPath install -r $testApk | Out-Null

    foreach ($className in $Tests) {
        Run-InstrumentationTest -AdbPath $adbPath -ClassName $className -TimeoutSec $TestTimeoutSec
    }

    Write-Host "Focused emulator regression finished."
} finally {
    if (-not $KeepEmulator) {
        Get-Process | Where-Object { $_.ProcessName -eq "emulator" -or $_.ProcessName -eq "qemu-system-x86_64" } |
            Stop-Process -Force -ErrorAction SilentlyContinue
        & $adbPath kill-server | Out-Null
    }
    Pop-Location
}
