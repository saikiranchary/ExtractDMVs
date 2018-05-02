@echo off
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_151
SET RESULTS_PATH=C:\MyWorkspace\eclipse-workspace\OutputExcel
SET BUILD_HOME=C:\MyWorkspace\eclipse-workspace\MSSQLServerDMVs\src\java
SET PATH=%PATH%
SET CLASSPATH=%BUILD_HOME%\DMV.jar;%BUILD_HOME%\dependencies\poi-3.12-20150511.jar;%BUILD_HOME%\dependencies\sqljdbc42.jar;%BUILD_HOME%\dependencies\org.apache.commons.io.jar%CLASSPATH%
SET SCENARIO=AfterTest
mkdir %RESULTS_PATH%\%SCENARIO%

SET APP_NAME=PSP
SET APP_DB_PROP_FILE=input_pspproperties.properties
mkdir %RESULTS_PATH%\%SCENARIO%\%APP_NAME%
"%JAVA_HOME%\bin\java.exe" -Xmx512M com.ot.pet.util.ExtractDMVs %BUILD_HOME%\%APP_DB_PROP_FILE% %APP_NAME%_%SCENARIO% %RESULTS_PATH%\%SCENARIO%\%APP_NAME%

SET APP_NAME=CS
SET APP_DB_PROP_FILE=input_csproperties.properties
mkdir %RESULTS_PATH%\%SCENARIO%\%APP_NAME%
"%JAVA_HOME%\bin\java.exe" -Xmx512M com.ot.pet.util.ExtractDMVs %BUILD_HOME%\%APP_DB_PROP_FILE% %APP_NAME%_%SCENARIO% %RESULTS_PATH%\%SCENARIO%\%APP_NAME%

goto :end

:end
endlocal 

