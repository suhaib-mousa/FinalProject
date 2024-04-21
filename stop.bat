FOR /F "tokens=*" %%i IN ('docker ps -aq') DO docker stop %%i
FOR /F "tokens=*" %%i IN ('docker ps -a -q') DO docker rm %%i
