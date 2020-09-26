#!/bin/zsh

echo "==========================="
echo "Starting Kafka..."
docker-compose -f docker-compose.yml up -d --build zookeeper kafka

echo "Build Artifact..."
mvn dependency:tree -U
mvn dependency:resolve-plugins -U
mvn clean install -DDB_URL=jdbc:h2:mem:earthquake -DDB_USER=sa -DDB_PASSWORD=password -DDB_DIALECT=H2Dialect -DDB_DRIVER=org.h2.Driver -DLOG_LEVEL=INFO -DKAFKA_HOST=localhost -DKAFKA_PORT=9092

if [ $? != 0]; then
  exit -1;

echo "Starting Earthquake Service..."
docker-compose -f docker-compose.yml up -d --build earthquake
echo "==========================="
