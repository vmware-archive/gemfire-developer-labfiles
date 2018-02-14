rem "JAVA_HOME must not have spaces in it"
set JAVA_HOME=C:\Java_64bit\jdk1.8.0

rem Clever little trick to pull the LAB_HOME from the root of the git folder
git rev-parse --show-toplevel > tmpfile
set /p LAB_HOME=<tmpfile
del tmpfile

rem "Assumes a path relative to the lab home - unless you've installed"
rem "GemFire elsewhere"
set GEMFIRE=%LAB_HOME%\pivotal-gemfire-9.3.0
set PATH=%PATH%;%GEMFIRE%\bin;%JAVA_HOME%\bin


