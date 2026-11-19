@echo off
setlocal
call gradlew.bat bootRun --args="--spring.profiles.active=demo"
endlocal
