# spring-boot-with-apache-kafka

This repository contains several modules developed with Spring Boot and Apache Kafka.

If you want to learn more about Spring Boot and Apache Kafka, you can visit the following links:
1. https://developer.confluent.io/courses/spring/apache-kafka-intro/
2. https://developer.confluent.io/courses/spring/spring-boot-with-apache-kafka-hands-on/
3. https://developer.confluent.io/courses/spring/send-messages/
4. https://developer.confluent.io/courses/spring/hands-on-send-messages/
5. https://developer.confluent.io/courses/spring/consume-messages/
6. https://developer.confluent.io/courses/spring/hands-on-consume-messages/
7. https://developer.confluent.io/courses/spring/create-kafka-topics/
8. https://developer.confluent.io/courses/spring/hands-on-create-kafka-topics/
9. https://developer.confluent.io/courses/spring/process-messages-with-kafka-streams/
10. https://developer.confluent.io/courses/spring/hands-on-process-messages-with-kafka-streams/
11. https://developer.confluent.io/courses/spring/cloud-schema-registry/
12. https://developer.confluent.io/courses/spring/hands-on-cloud-schema-registry/
13. https://developer.confluent.io/courses/spring/summary/


# Installing Kafka through Docker Compose
To configure Kafka Brokers using Docker Compose you will have to execute next steps:

1. Create next structure folders in user's home directory:

    ** mkdir $HOME_USER/kafka/docker-compose/volumes
 
2. Go to next folder:

    ** spring-boot-project-with-apache-kafka/docker-compose

3. And execute next command:

    ** docker compose -f docker-compose_3.yml --env-file environment.env up

** Note: The YAML file "docker-compose_3.yml" contains the configuration for three Kafka brokers.
         Go to docker-compose folder where you can find this file and environment.env file. 
         Also, you can find config files to set up Kafka Brokers.