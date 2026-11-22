@echo off
setlocal
cd /d "%~dp0"

where java >nul 2>nul
if errorlevel 1 (
    echo Java 17+ not found in PATH. Install JDK 17 and try again.
    pause
    exit /b 1
)

java -Xms512m -Xmx2g -Dfile.encoding=UTF-8 -jar InTheMyst-all.jar %*
if errorlevel 1 pause
