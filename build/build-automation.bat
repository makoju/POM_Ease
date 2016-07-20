REM this file should be used by harness server for the nightly runs
call mvn -f ../parent/pom.xml install
cd ../assembly
call mvn -f pom.xml package
REM recompile automation-jsystem module to copy tests/resoucres/webdriver/ to the classes directory
call mvn -f ../automation-jsystem/pom.xml compile
call target/run.bat ../Run-Ease-Regression-Suite