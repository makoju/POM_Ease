@echo off
cls
setlocal

COLOR 07

if defined ECLIPSE_WORKSPACE goto create_workspace

pushd %~dp0
cd .. 
set ECLIPSE_WORKSPACE="%cd%"
popd

echo.
echo environment variable ECLIPSE_WORKSPACE is not defined, using %ECLIPSE_WORKSPACE%.


:create_workspace

call mvn  -Declipse.workspace=%ECLIPSE_WORKSPACE% eclipse:add-maven-repo -Dmaven.test.skip=true %1
if ERRORLEVEL 1 goto problem

call mvn -f ..\parent\pom.xml install eclipse:clean -Dmaven.test.skip=true %1
if ERRORLEVEL 1 goto problem

echo.
echo Generating Eclipse projects
echo.
call mvn -f ..\parent\pom.xml -Dwtpversion=1.5 eclipse:eclipse -Dmaven.test.skip=true %1
if ERRORLEVEL 1 goto problem

PUSHD "..\assembly"
call mvn -f pom.xml package
POPD
if ERRORLEVEL 1 goto problem

rem need to recompile automation-jsystem to copy tests\rsoucres\webdriver\* to the classes directory
call mvn -f ..\automation-jsystem\pom.xml compile
if ERRORLEVEL 1 goto problem

goto ok



:problem
COLOR 0C

echo.                            
echo      ^|-  -^|    
echo      ^|O  O^|
echo      ^|  ^ ^|^|    
echo      ^| -- ^|
echo       \__/ 
echo.
Echo FAIL! Too bad, generating/updating automation projects FAILED!!!!
timeout /t 5
echo.
goto end

:ok
COLOR 0A

echo.
echo   \^|^|^|^|^|/
echo  ( ~   ~ )
echo @( 0   0 )@
echo  (   C   )
echo   \ \_/ /
echo    ^|___^| 
echo     ^| ^|
echo. 
Echo Well done! You may now import the automation project into your Eclipse.
Echo Also, a shortcut was created on your dekstop for JSsytem. Use it to start JSystem.
echo.
:end





