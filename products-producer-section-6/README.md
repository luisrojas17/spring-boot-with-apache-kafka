# products-producer-section-6

This repository contains Microservice application which exposes REST endpoint to create products 
and produce events to Kafka topic.

On the other hand, the Microservices application depends on Kafka brokers and Kafka topic. 
So, when the application starts it will create Kafka topic called "products-created-topic".
The Kafka Brokers configuration is defined into "com.acme.common.config.kafka.KafkaConfig" class 
which defines three Kafka brokers.


## How to run the application
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
Before to start the application, we need to start Kafka containers:

After starting Kafka containers you can connect to Kafka brokers from command line:

    docker exec -it docker-compose-kafka-1-1 bash
    docker exec -it docker-compose-kafka-2-1 bash
    docker exec -it docker-compose-kafka-3-1 bash


## Kafka Console Consumer

To consume the events from Kafka topic run the following command:

    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9092 --from-beginning -property print.key=true
    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9094 --from-beginning -property print.key=true
    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9096 --from-beginning -property print.key=true