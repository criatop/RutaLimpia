@echo off
setlocal EnableExtensions
cd /d "%~dp0"

echo ============================================
echo   RutaLimpia - Compilando JARs...
echo ============================================
call :buscar_maven || exit /b 1
for %%M in (eureka-server api-gateway ms-auth ms-flota ms-rutas ms-solicitudes) do (
  echo   - %%M ...
  call %MVN% -q -B -f "%%M\pom.xml" clean package -DskipTests
  if errorlevel 1 (echo   [ERROR] fallo al compilar %%M & exit /b 1)
)

echo.
echo ============================================
echo   RutaLimpia - Construyendo imagenes Docker...
echo ============================================
docker compose build
if errorlevel 1 (echo   [ERROR] fallo al construir imagenes & exit /b 1)

echo.
echo Imagenes listas. Usa docker-start.bat para levantarlas.
endlocal
exit /b 0

:buscar_maven
set "MVN="
where mvn >nul 2>&1 && set "MVN=mvn"
if not defined MVN (
  for /r "%USERPROFILE%\.m2\wrapper\dists" %%M in (mvn.cmd) do if not defined MVN set "MVN=%%~fM"
)
if not defined MVN (
  echo [!] No se encontro Maven. Instalalo o ponlo en el PATH.
  exit /b 1
)
echo   Maven: %MVN%
exit /b 0