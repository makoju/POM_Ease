
@echo off

REM updates JSystem classpath

setlocal

COLOR 07

rem compile & install all automation modules in local repository
call mvn -f ..\parent\pom.xml install -Dwtpversion=1.5 eclipse:eclipse -Dmaven.test.skip=true 
if ERRORLEVEL 1 goto problem

rem create onejar from impl,common,impl modules and plasce under assembly\taget\automation-tests-lib folder
PUSHD "..\assembly"
call mvn -f pom.xml package
POPD
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
Echo Failed to update JSystem classpath
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
Echo JSystem classpath was updated successfully.
echo.
:end





