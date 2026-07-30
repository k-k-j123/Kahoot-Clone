@echo off
setlocal enabledelayedexpansion

set BASEDIR=%~dp0
set PIDS=

call :start_service "service-registry" 8761
call :start_service "quiz-service"     8081
call :start_service "auth-service"     8082
call :start_service "api-gateway"      8080

echo.
echo === All services starting. Press Ctrl+C to stop. ===
echo.

:wait_loop
timeout /t 5 /nobreak >nul
goto wait_loop

:start_service
set DIR=%~1
set PORT=%2
echo --- Starting %DIR% on port %PORT% ---
start "%DIR%" /B /D "%BASEDIR%%DIR%" cmd /c "mvnw spring-boot:run -q 2>&1"
set PIDS=!PIDS! !ERRORLEVEL!
call :wait_for_port %PORT% %DIR%
exit /b

:wait_for_port
set PORT=%~1
set NAME=%~2
set TIMEOUT=60
set ELAPSED=0
echo Waiting for %NAME% on :%PORT%...
:wait_loop_port
ping -n 2 127.0.0.1 >nul
set /a ELAPSED+=1
if !ELAPSED! geq !TIMEOUT! (
    echo ERROR: %NAME% did not start within !TIMEOUT!s
    exit /b 1
)
netstat -an 2>nul | findstr ":%PORT% " >nul
if errorlevel 1 (
    netstat -an 2>nul | findstr ":%PORT%" >nul
    if errorlevel 1 goto wait_loop_port
)
echo %NAME% is up.
exit /b
