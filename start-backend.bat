@echo off
echo ============================================
echo   Kessoku Blog - Starting Backend Server
echo ============================================
set JAVA_HOME=C:\Program Files\Java\jdk-21.0.11
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d D:\blog\blog-backend
echo.
echo Java version:
java -version 2>&1 | findstr "version"
echo.
echo Starting Spring Boot on port 8080...
echo Admin: admin / admin123
echo ============================================
maven\bin\mvn spring-boot:run
pause
