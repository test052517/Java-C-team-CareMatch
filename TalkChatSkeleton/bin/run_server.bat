@echo off
cd /d "%~dp0\.."
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
if not exist out mkdir out
set "CP=out"
for %%F in ("lib\*.jar") do set "CP=%CP%;%%~fF"
del sources.txt 2>nul
for /r "src" %%G in (*.java) do echo %%G>>sources.txt
javac -encoding UTF-8 -cp "%CP%" -d out @sources.txt || (echo build failed & pause & exit /b 1)
java -cp "%CP%" Talk.server.TalkServer
pause
