$ErrorActionPreference = "Stop"

Write-Host "=== マヤ暦アプリ 起動スクリプト (完全版) ===" -ForegroundColor Cyan

$distDir = "$PSScriptRoot\.maven_dist"
if (-not (Test-Path $distDir)) {
    New-Item -ItemType Directory -Force -Path $distDir | Out-Null
}

# --- 1. Java (JDK 17) の準備 ---
$javaVersion = "17"
# Attempt to find java. If not found or wrong version (simplification: just check existence), download portable
try {
    $javaCheck = Get-Command java -ErrorAction Stop
    $hasJava = $true
    Write-Host "システムにインストールされたJavaを使用します。" -ForegroundColor Gray
} catch {
    $hasJava = $false
    Write-Host "Javaが見つかりません。ポータブル版を準備します..." -ForegroundColor Yellow
}

if (-not $hasJava) {
    $jdkDirName = "jdk-17.0.10+7"
    $jdkZipName = "OpenJDK17U-jdk_x64_windows_hotspot_17.0.10_7.zip"
    # Using Eclipse Temurin (Adoptium)
    $jdkUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10%2B7/$jdkZipName"
    $localJdkPath = "$distDir\jdk-17"
    $javaExe = "$localJdkPath\$jdkDirName\bin\java.exe"

    if (-not (Test-Path $javaExe)) {
        Write-Host "JDK 17 ($jdkZipName) をダウンロード中..."
        $jdkZipPath = "$distDir\$jdkZipName"
        Invoke-WebRequest -Uri $jdkUrl -OutFile $jdkZipPath
        
        Write-Host "JDKを解凍中..."
        # Extract direct to distDir. It usually creates a folder like 'jdk-17.0.10+7'
        Expand-Archive -Path $jdkZipPath -DestinationPath "$distDir\jdk-temp" -Force
        
        # Move to a predictable path
        $extractedRoot = Get-ChildItem "$distDir\jdk-temp" | Select-Object -First 1
        Move-Item $extractedRoot.FullName $localJdkPath -Force
        Remove-Item "$distDir\jdk-temp" -Recurse -Force
        Remove-Item $jdkZipPath
        
        Write-Host "JDKの準備完了。" -ForegroundColor Green
    }
    
    # Set environment variables for this session ONLY
    $env:JAVA_HOME = "$localJdkPath"
    $env:Path = "$localJdkPath\bin;$env:Path"
    $javaBin = "$localJdkPath\bin\java.exe"
}

# --- 2. Maven の準備 ---
$mavenVersion = "3.9.6"
$mavenBin = "$distDir\apache-maven-$mavenVersion\bin\mvn.cmd"

if (-not (Test-Path $mavenBin)) {
    Write-Host "Maven ($mavenVersion) をダウンロード中..."
    $mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
    $zipPath = "$distDir\maven.zip"
    
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath
    Write-Host "Mavenを解凍中..."
    Expand-Archive -Path $zipPath -DestinationPath $distDir -Force
    Remove-Item $zipPath
    Write-Host "Mavenの準備完了。" -ForegroundColor Green
}

# --- 3. ビルド＆実行 ---
Write-Host "`n>>> アプリをビルドしています (初回は時間がかかります)..." -ForegroundColor Cyan
& $mavenBin clean install -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n>>> 起動します！ (http://localhost:8080)" -ForegroundColor Green
    Write-Host "また遅いと感じたら、この画面で Ctrl + C を押して終了し、もう一度実行してください。" -ForegroundColor Gray
    
    # Run with increased memory
    & $mavenBin spring-boot:run "-Dspring-boot.run.jvmArguments=-Xmx512m"
} else {
    Write-Host "`n[エラー] ビルドに失敗しました。" -ForegroundColor Red
    Read-Host "Enterキーを押して終了してください..."
}
