REM this file should be used by harness server for the nightly runs
call mvn -f ../modules/parent/pom.xml install
cd ../modules/assembly
call mvn -f pom.xml package
REM recompile automation-jsystem module to copy tests/resoucres/webdriver/ to the classes directory
call mvn -f ../automation-jsystem/pom.xml compile