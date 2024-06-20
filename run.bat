
@echo off
SET DEVELOPMENT_HOME=C:\MusalaSoft\Projects

cd %DEVELOPMENT_HOME%\event-booking\

REM Create the docker network for the application
call docker network rm event_b_network

REM Create the docker network for the application
call docker network create --driver bridge event_b_network

REM Shutdown or stop any previous kafka server that is running
call docker-compose -f src/main/docker/services.yml down

REM Build the docker container for kafka server that is running
call docker-compose -f src/main/docker/services.yml up -d --build

REM Shutdown or stop any previous docker container that is running
call docker-compose down

REM Build the docker image using the Dockerfile and create a container with the image and start it.
call docker-compose up -d --build

REM press ctl+c to stop  docker container in an interactive mode
