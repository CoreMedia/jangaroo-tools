@echo off

if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

set JOO_HOME=%~dp0
set JAVA_ARGS=-cp "%JOO_HOME%\ext-xml-${jangaroo.version}.jar;%JOO_HOME%\jangaroo-utils-${jangaroo.version}.jar;%JOO_HOME%\file-management-1.2.1.jar;%JOO_HOME%\freemarker-2.3.15.jar;%JOO_HOME%\jtidy-4aug2000r7-dev.jar;%JOO_HOME%\maven-shared-io-1.1.jar;%JOO_HOME%\plexus-utils-2.0.1.jar" net.jangaroo.extxml.ExtXml

if "%JAVA_HOME%" == "" (
  echo ERROR: JAVA_HOME environment variable is not set.
  echo Please set the JAVA_HOME variable in your environment to match the
  echo location of your Java installation.
) else (
  "%JAVA_HOME%\bin\java" %JAVA_ARGS% %*
)
