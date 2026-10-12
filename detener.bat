@echo off
REM ============================================================
REM  RutaLimpia - detiene y elimina los contenedores en Docker
REM  (conserva los datos de MySQL en el volumen mysql_data)
REM ============================================================
setlocal
cd /d "%~dp0"
docker compose down
echo Servicios detenidos.
endlocal
exit /b 0