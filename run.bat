REM Enter number of nodes that you want
SET /P nodes="Enter number of nodes that you want: "

docker network create -d bridge net

docker run -d --name bootstrapper -p 8080:8080 --network net --env numOfNodes="%nodes%" bootstrapper

FOR /L %%i IN (1,1,%nodes%) DO (
    docker run -d --name node%%i -p 808%%i:8080 --network net --env numOfNodes="%nodes%" --env nodeNumber=%%i node
)