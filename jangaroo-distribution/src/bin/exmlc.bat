@echo off

if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

set JOO_HOME=%~dp0
set JAVA_ARGS=-jar "%JOO_HOME%\exml-compiler-${jangaroo.version}-jar-with-dependencies.jar"

if "%JAVA_HOME%" == "" (
  echo ERROR: JAVA_HOME environment variable is not set.
  echo Please set the JAVA_HOME variable in your environment to match the
  echo location of your Java installation.
) else (
  "%JAVA_HOME%\bin\java" %JAVA_ARGS% %*
)
