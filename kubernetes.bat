@echo off

REM Shutdown or stop any previous kafka server that is running
call docker-compose -f src/main/docker/services.yml down

REM Create the docker network for the application
call docker network create --driver bridge event_b_network

REM Build the docker container for kafka server that is running
call docker-compose -f src/main/docker/services.yml up -d --build

REM Build the docker image
call docker build -t event-booking-img-v1 .
REM Tag the docker image
call docker tag event-booking-img-v1  omameazy/event-booking-img-v1
REM Push the docker image to docker hub...you might need to login to your docker hube and change the <omameazy> url
call docker push  omameazy/event-booking-img-v1

REM Remove the previous horizontal pod auto scaling, service and deployment
call kubectl delete -f event-booking-hpa-v1.yaml
call kubectl delete service event-booking-service-001
call kubectl delete deployment event-booking-rocket-001

REM Apply the current deployment, service and horizontal pod auto scaling
call kubectl apply -f event-booking-deployment.yaml
call kubectl apply -f event-booking-service.yaml
call kubectl apply -f event-booking-hpa-v1.yaml

REM press ctl+c to stop and visit: http://localhost:30086/event-booking/swagger-ui.html for the Swagger URL
