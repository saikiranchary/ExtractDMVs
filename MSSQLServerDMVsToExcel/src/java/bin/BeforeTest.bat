@echo off
SET RESULTS_PATH=C:\MyWorkspace\eclipse-workspace\OutputExcel
SET BUILD_HOME=C:\MyWorkspace\eclipse-workspace\MSSQLServerDMVs\src\java
SET PATH=%PATH%
SET CLASSPATH=%BUILD_HOME%\DMV.jar;%BUILD_HOME%\dependencies\poi-3.12-20150511.jar;%BUILD_HOME%\dependencies\sqljdbc42.jar;%BUILD_HOME%\dependencies\org.apache.commons.io.jar%CLASSPATH%

mkdir %RESULTS_PATH%\BeforeTest
mkdir %RESULTS_PATH%\BeforeTest\PSP

"C:\Program Files\Java\jdk1.8.0_151\bin\java.exe" -Xmx512M com.ot.pet.util.ExtractDMVs %BUILD_HOME%\input_dbproperties.properties CS_BeforeTest %RESULTS_PATH%\BeforeTest\PSP
goto :end

:end
endlocal 

