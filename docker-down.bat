@echo off
setlocal
cd /d "%~dp0"
docker compose down -v
echo Contenedores, red y volumen de datos eliminados.
endlocal
exit /b 0