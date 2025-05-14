# products-producer-section-7

This repository contains Microservice application which exposes REST endpoint to create products 
and produce events to Kafka topic.

On the other hand, the Microservices application depends on Kafka brokers and Kafka topic. 
So, when the application starts it will create Kafka topic called "products-created-topic".
The Kafka Brokers configuration is defined into "com.acme.common.config.kafka.KafkaConfig" class 
which defines three Kafka brokers.


## How to Run the Application
**Note: [You have to start Kafka containers before starting application.](#How-to-Start-Kafka-Containers)**

To start the application from command line you can use next commands:

Using Java jar command:

    ** java -jar target/products-producer-section-6-1.0.0.jar

Where X means number module that you can run. For example:

    ** java -jar target/products-producer-section-6-1.0.0.jar

Using Maven:

    ** mvn spring-boot:run

However, it is necessary to add next plugin to your pom.xml file:

```
...
<build>
    <plugins>
    ...
        <plugin>
		    <groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
			<configuration>
				<excludes>
					<exclude>
						<groupId>org.projectlombok</groupId>
						<artifactId>lombok</artifactId>
					</exclude>
				</excludes>
			</configuration>
		</plugin>
    ..
    </plugins>
</build>
...
```

# How to Start Kafka Containers
Before to start the application, we need to start Kafka containers. So, you can start Kafka containers in two ways 
from command line:

1. Through Docker Compose
2. Through Docker CLI

## Docker Compose
To start Kafka containers you can run next command:

    docker-compose up -d -f $PATH_LOCATION/docker-compose_3.yml --env-file environment.env

Where $PATH_LOCATION is path to docker-compose.yml file.

## Docker CLI
Use next commands to start Kafka containers:

    docker start container_id

To get container id you have to run next command:

    docker ps -a

## Starting Kafka Containers
After starting Kafka containers you can connect to Kafka brokers from command line:

    docker exec -it <container ID or name> bash

1. Docker Compose Names

        docker exec -it docker-compose-kafka-1-1 bash
        docker exec -it docker-compose-kafka-2-1 bash
        docker exec -it docker-compose-kafka-3-1 bash

    **Note: Docker Compose Names are defined in "docker-compose_3.yml" file through "container_name" attribute.
        It is important to note that docker compose add the prefix "docker-compose" to container name. 
        At the end, the final name it will be "docker-compose-container_name".**

2. Docker Container ID 

        docker exec -it 9c5bc1dfd9bb bash
        docker exec -it a804bd0560ce bash 
        docker exec -it 78efb35e6e95 bash

To get container id you can run next command:

    docker ps -a


# Kafka Console Consumer
Once you connect and start Kafka brokers, go to next path:

    cd /opt/bitnami/kafka/bin/

To consume the events from Kafka topic run the following command:

    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9092 --from-beginning -property print.key=true
    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9094 --from-beginning -property print.key=true
    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9096 --from-beginning -property print.key=true

# How to Configure Kafka Producer Acknowledgements
The default value of acknowledgment (ack) mode is "1".
In Apache Kafka, message acknowledgement modes dictate when a producer receives confirmation from the broker that 
a message has been successfully written. The main options are acks=0, acks=1, and acks=all.

1. acks=0 (No Acknowledgment): 
   The producer sends the message and does not wait for any acknowledgement from the broker. This is the fastest option 
   but provides the least durability, as there's no guarantee the message was received.
2. acks=1 (Leader Acknowledgment): 
   The producer waits for an acknowledgement from the leader broker after the message has been written to disk on 
   the leader. This offers a balance between speed and durability, but a message could still be lost if the leader broker 
   crashes before the message is replicated to other replicas.
3. acks=all (All In-Sync Replicas Acknowledgment):
   The producer waits for acknowledgements from all in-sync replicas (ISR) of the partition after the message 
   has been written to disk on those replicas. This is the most durable option but also the slowest, as it requires 
   all replicas to acknowledge the message.

To configure the acknowledgement mode in Producer Spring Boot application you will have to add next property 
to application.properties file:

    spring.kafka.producer.acks=all

This property configuration is related to minimum insync replicas configuration. Go next section to learn more.

# How to Configure Kafka Producer Minimum Insync Replicas
Minimum insync replicas is the number of replicas that must acknowledge write to be considered successful.

You can configure Kafka producer minimum insync replicas when is creating new topic by CLI:

    ./kafka-topics.sh --create --topic topic_name --partitions 3 --replication-factor 3 --bootstrap-server host.docker.internal:9092 --config min.insync.replicas=2

Also, you can add new configuration to existing topic by CLI with next command:
    
    ./kafka-configs.sh --bootstrap-server host.docker.internal:9092 --alter --entity-type topics --entity-name topic_name --add-config min.insync.replicas=2

And, to check the configuration that you just modified run next command:

    ./kafka-topics.sh --describe --bootstrap-server host.docker.internal:9092

**Note: By default, the value of minimum insync replicas is 1.**