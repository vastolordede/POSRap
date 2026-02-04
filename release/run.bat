@echo off
cd /d %~dp0
echo Running POSRap...
java -cp "dist\POSRap.jar;libs\*" posrap.POSRap
