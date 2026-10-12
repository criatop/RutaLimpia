@echo off
setlocal EnableExtensions
cd /d "%~dp0"

echo ============================================
echo   RutaLimpia - Limpiando contenedores...
echo ============================================
docker compose down -v

echo.
echo ============================================
echo   RutaLimpia - Compilando y construyendo imagenes...
echo ============================================
call docker-build.bat
if errorlevel 1 exit /b 1

echo.
echo ============================================
echo   RutaLimpia - Levantando infraestructura...
echo ============================================
docker compose up -d

echo.
echo Esperando registro de los microservicios en Eureka...
set "CURL=%SystemRoot%\System32\curl.exe"
if not exist "%CURL%" set "CURL=curl"
call :espera_ms && echo   Microservicios registrados.

echo.
echo Refrescando el registro del API Gateway...
docker compose restart api-gateway >nul 2>&1
call :espera_puerto 8080 || echo   [aviso] Gateway aun no responde, revisa "docker logs api-gateway".

echo.
echo ============================================
echo   RutaLimpia listo!
echo     Eureka  : http://localhost:8761
echo     Gateway : http://localhost:8080
echo     MySQL   : localhost:3306 (root/root)
echo   Para detener todo: detener.bat
echo ============================================
endlocal
exit /b 0

:app_ok
"%CURL%" -s -o nul -f "http://localhost:8761/eureka/apps/%~1" >nul 2>&1
exit /b %errorlevel%

:espera_ms
set /a N=0
:lazo_ms
set "TODOS=1"
call :app_ok MS-AUTH || set "TODOS=0"
call :app_ok MS-FLOTA || set "TODOS=0"
call :app_ok MS-RUTAS || set "TODOS=0"
call :app_ok MS-SOLICITUDES || set "TODOS=0"
if "%TODOS%"=="1" exit /b 0
set /a N+=1
if %N% GEQ 100 (
  echo [!] Algunos microservicios no se registraron en ~300s. Revisa: docker compose ps
  exit /b 1
)
timeout /t 3 /nobreak >nul
goto lazo_ms

:espera_puerto
set "PUERTO=%~1"
set /a N=0
:lazo_port
"%CURL%" -s -o nul "http://localhost:%PUERTO%/" >nul 2>&1
if not errorlevel 1 exit /b 0
set /a N+=1
if %N% GEQ 60 (
  echo [!] puerto %PUERTO% no respondio tras ~180s.
  exit /b 1
)
timeout /t 3 /nobreak >nul
goto lazo_port