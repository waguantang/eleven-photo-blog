@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal enabledelayedexpansion

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "MAVEN_USER_HOME=%USERPROFILE%\.m2"

set "MVNW_VERBOSE=false"
set "MVNW_MAVEN_HOME=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven"

if not defined JAVA_HOME (
    echo ERROR: JAVA_HOME is not set
    exit /b 1
)

set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
set "MVNW_MAVEN_ZIP=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven.zip"

if not exist "%MVNW_MAVEN_HOME%" (
    echo Downloading Maven...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip' -OutFile '%MVNW_MAVEN_ZIP%'"
    powershell -Command "Expand-Archive -Path '%MVNW_MAVEN_ZIP%' -DestinationPath '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\temp'"
    move "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\temp\apache-maven-*" "%MVNW_MAVEN_HOME%"
    rmdir /s /q "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\temp"
    del "%MVNW_MAVEN_ZIP%"
)

set "MAVEN_CMD=%MVNW_MAVEN_HOME%\bin\mvn"
"%JAVA_EXE%" %MAVEN_OPTS% -classpath "%MVNW_MAVEN_HOME%\boot\plexus-classworlds-*.jar" "-Dclassworlds.conf=%MVNW_MAVEN_HOME%\bin\m2.conf" "-Dmaven.home=%MVNW_MAVEN_HOME%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.codehaus.plexus.classworlds.launcher.Launcher %MAVEN_CONFIG% %*
