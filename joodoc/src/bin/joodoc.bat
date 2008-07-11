@echo off

set JOO_HOME=%~dp0..

set JOO_CLASSPATH=%JOO_HOME%\lib\jangaroo-doc-${joo.tools.version}.jar;%JOO_HOME%\lib\jangaroo-compiler-${joo.tools.version}.jar
set JOO_CLASSPATH=%JOO_CLASSPATH%;%JOO_HOME%\lib\java-cup-runtime-10k.jar;%JOO_HOME%\lib\commons-cli-1.1.jar

"%~dp0runtool.bat" net.jangaroo.joodoc.Main "%JOO_CLASSPATH%" %*