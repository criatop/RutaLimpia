@echo off
setlocal
cd /d "%~dp0"
docker compose stop
echo Contenedores detenidos (conserva los datos de MySQL).
endlocal
exit /b 0