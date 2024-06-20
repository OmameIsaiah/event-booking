#!/usr/bin/env bash
#echo "Running integration and unit testing"
#mvn verify -Pfailsafe
#
#echo "Building artifact"
#mvn clean install -DskipTests

echo "Shutdown or stop any previous services/containers and create new ones"
echo "Running docker-compose operations"
 docker docker network rm event_b_network
 docker network create --driver bridge event_b_network
 docker-compose -f src/main/docker/services.yml down
 docker-compose -f src/main/docker/services.yml up -d --build
 docker-compose down
 docker-compose up -d --build