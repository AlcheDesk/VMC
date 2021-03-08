@set "filename=%Date:~10,4%%Date:~4,2%%Date:~7,2%-%Time:~0,2%%Time:~3,2%%Time:~6,2%.log"
@echo %filename%
java -Dfile.encoding=utf-8 -jar VMC-1.3.jar>%filename%