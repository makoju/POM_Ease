#!/bin/bash
#this file should be used by harness server for the nightly runs
mvn -f ../modules/parent/pom.xml install
cd ../modules/assembly
echo $(pwd)
mvn -f pom.xml package
#recompile automation-jsystem module to copy tests/resoucres/webdriver/ to the classes directory
mvn -f ../automation-jsystem/pom.xml compile
