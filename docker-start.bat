@echo off
setlocal
cd /d "%~dp0"
docker compose up -d
if errorlevel 1 (
  echo [ERROR] No se pudo levantar. Quizas falta construir las imagenes: docker-build.bat
  exit /b 1
)
echo Contenedores levantados. Revisa el estado con: docker compose ps
endlocal
exit /b 0