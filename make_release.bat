@echo off
setlocal

set APP_JAR=dist\POSRap.jar
set MAIN_CLASS=posrap.POSRap

rmdir /s /q release 2>nul
mkdir release
mkdir release\dist
mkdir release\libs

copy "%APP_JAR%" release\dist\
xcopy /E /I /Y libs release\libs >nul

(
echo @echo off
echo cd /d %%~dp0
echo echo Running POSRap...
echo java -cp "dist\POSRap.jar;libs\*" %MAIN_CLASS%
) > release\run.bat

echo ===================================
echo Release created at: release\
echo Double click release\run.bat
echo ===================================
endlocal
