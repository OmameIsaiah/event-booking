#!/usr/bin/env bash

echo "Shutdown or stop any previous kafka server that is running"
docker-compose -f src/main/docker/services.yml down

echo "Create the docker network for the application"
docker network create --driver bridge event_b_network

echo "Build the docker container for kafka server that is running"
docker-compose -f src/main/docker/services.yml up -d --build

echo "Build the docker image"
docker build -t event-booking-img-v1 .
echo "Tag the docker image"
docker tag event-booking-img-v1 omameazy/event-booking-img-v1
echo "Push the docker image to docker hub...you might need to login to your docker hube and change the <omameazy> url"
docker push omameazy/event-booking-img-v1

echo "Remove the previous horizontal pod auto scaling, service and deployment"
kubectl delete -f event-booking-hpa-v1.yaml
kubectl delete service event-booking-service-001
kubectl delete deployment event-booking-rocket-001

echo "Apply the current deployment, service and horizontal pod auto scaling"
kubectl apply -f event-booking-deployment.yaml
kubectl apply -f event-booking-service.yaml
kubectl apply -f event-booking-hpa-v1.yaml

echo "press ctl+c to stop and visit: http://localhost:30086/event-booking/swagger-ui.html for the Swagger URL"
