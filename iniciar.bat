@echo off
REM ============================================================
REM  RutaLimpia - levanta todo en Docker (un solo comando)
REM  Uso:  iniciar.bat              -> compila, construye imagenes
REM                                   y levanta MySQL + Eureka + 4
REM                                   microservicios + API Gateway
REM  Requiere: Docker Desktop corriendo.
REM ============================================================
setlocal EnableExtensions
cd /d "%~dp0"

call docker-setup.bat
endlocal
exit /b 0