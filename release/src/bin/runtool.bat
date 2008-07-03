@echo off

REM  Licensed to the Apache Software Foundation (ASF) under one or more
REM  contributor license agreements.  See the NOTICE file distributed with
REM  this work for additional information regarding copyright ownership.
REM  The ASF licenses this file to You under the Apache License, Version 2.0
REM  (the "License"); you may not use this file except in compliance with
REM  the License.  You may obtain a copy of the License at
REM
REM      http://www.apache.org/licenses/LICENSE-2.0
REM
REM  Unless required by applicable law or agreed to in writing, software
REM  distributed under the License is distributed on an "AS IS" BASIS,
REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM  See the License for the specific language governing permissions and
REM  limitations under the License.

REM This is an inordinately troublesome piece of code, particularly because it
REM tries to work on both Win9x and WinNT-based systems. If we could abandon '9x
REM support, things would be much easier, but sadly, it is not yet time.
REM Be cautious about editing this, and only add WinNT specific stuff in code that
REM only runs on WinNT.

if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

rem %~dp0 is expanded pathname of the current script under NT
set JOO_HOME=%~dp0..

set _JOO_CLASS_NAME=%1
shift
set _JOO_CLASSPATH=%1
shift

set JOO_CMD_LINE_ARGS=%1
if ""%1""=="""" goto doneStart
shift
:setupArgs
if ""%1""=="""" goto doneStart
set JOO_CMD_LINE_ARGS=%JOO_CMD_LINE_ARGS% %1
shift
goto setupArgs

:doneStart

:findJooHome
if exist "%JOO_HOME%\lib" goto checkJava

:noJooHome
echo Jangaroo tools could not be located.
goto end

:checkJava
set _JAVACMD=%JAVACMD%

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe
set _JOO_CLASSPATH=%_JOO_CLASSPATH%;%JAVA_HOME%\lib\tools.jar
goto runJoo

:noJavaHome
echo Error: JAVA_HOME is not defined correctly.
goto end

:runJoo
"%_JAVACMD%" -classpath "%_JOO_CLASSPATH%" %_JOO_CLASS_NAME% %JOO_CMD_LINE_ARGS%
set JOO_ERROR=%ERRORLEVEL%
goto end

:end
set _JAVACMD=
set _JOO_CMD_LINE_ARGS=
set _JOO_CLASSPATH=
set _JOO_CLASS_NAME=

if "%JOO_ERROR%"=="0" goto mainEnd

rem Set the ERRORLEVEL if we are running NT.
if "%OS%"=="Windows_NT" color 00

goto omega

:mainEnd

rem If there were no errors, we run the post script.
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal

:omega
exit /B %JOO_ERROR%
