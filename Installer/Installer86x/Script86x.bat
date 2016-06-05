@echo off

rem delete old output and project
@RD /S /Q "%~dp0..\Installer86x\Output"
pause

rem copy project to desktop
robocopy "%~dp0..\..\..\EastWeb.V2" "%temp%\EastWeb.V2" /s /mt[:18] /xf *.iss *.exe *.bat *.jar *.zip *.png
pause

rem copy project from desktop to installer folder (this prevents infinite loop)
rem then remove project form desktop
robocopy "%temp%\EastWeb.V2" "%~dp0..\EastWeb.V2" /mir /mt[:18]
@RD /S /Q "%temp%\EastWeb.V2"
del "%~dp0..\EastWeb.V2\projects\*.*"
@RD /S /Q "%~dp0..\EastWeb.V2\Installer"
@RD /S /Q "%~dp0..\EastWeb.V2\.git"
@RD /S /Q "%~dp0..\EastWeb.V2\.settings"
@RD /S /Q "%~dp0..\EastWeb.V2\.svn"
@RD /S /Q "%~dp0..\EastWeb.V2\bin"
@RD /S /Q "%~dp0..\EastWeb.V2\doc"
@RD /S /Q "%~dp0..\EastWeb.V2\Documentation"

@RD /S /Q "%~dp0..\EastWeb.V2\lib"
@RD /S /Q "%~dp0..\EastWeb.V2\sources"
@RD /S /Q "%~dp0..\EastWeb.V2\src"
pause

rem compile installer 
"%~dp0..\InstallerCompiler\IScc.exe" %~dp0EastWeb86x.iss
@RD /S /Q "%~dp0..\EastWeb.V2"
pause