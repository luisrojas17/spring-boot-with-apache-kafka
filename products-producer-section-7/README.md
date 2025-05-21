# products-producer-section-7

This repository contains Microservice application which exposes REST endpoint to create products 
and produce events to Kafka topic.

On the other hand, this module depends on Kafka brokers and Kafka topic. So, when the application starts
it will create Kafka topic called "products-created-topic". This configuration is defined into
"com.acme.common.config.kafka.KafkaConfig" class.

The main goal in this project is to learn how to configure next characteristics:
1. Kafka Producer Minimum Insync Replicas
2. Kafka Producer Acknowledgment Mode
3. Kafka Producer Retries
4. Kafka Producer Delivery Timeout
5. Kafka Producer Batch Size


## How to Run the Application
**Note: [You have to start Kafka containers before starting application.](#How-to-Start-Kafka-Containers)**

To start the application from command line you can use next commands:

Using Java jar command:

    ** java -jar target/products-producer-section-7-1.0.0.jar

Where X means number module that you can run. For example:

    ** java -jar target/products-producer-section-7-1.0.0.jar

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

## How to Start Kafka Containers
Before to start the application, we need to start Kafka containers. So, you can start Kafka containers in two ways 
from command line:

1. Through Docker Compose
2. Through Docker CLI

### Docker Compose
To start Kafka containers you can run next command:

    docker-compose up -d -f $PATH_LOCATION/docker-compose_3.yml --env-file environment.env

Where $PATH_LOCATION is path to docker-compose.yml file.

### Docker CLI
Use next commands to start Kafka containers:

    docker start container_id

To get container id you have to run next command:

    docker ps -a

### Starting Kafka Containers
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

### Kafka Console Consumer
Once you connect and started Kafka brokers, go to next path:

    cd /opt/bitnami/kafka/bin/

To consume the events from Kafka topic run the following command:

    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9092 --from-beginning -property print.key=true
    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9094 --from-beginning -property print.key=true
    ./kafka-console-consumer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9096 --from-beginning -property print.key=true

### Kafka Console Producer
Once you connect and started Kafka brokers, go to next path:

    cd /opt/bitnami/kafka/bin/

To produce the events to Kafka topic run the following command:

    ./kafka-console-producer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9092 --property parse.key=true --property "key.separator=:"
    ./kafka-console-producer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9094 --property parse.key=true --property "key.separator=:"
    ./kafka-console-producer.sh --topic products-created-topic --bootstrap-server host.docker.internal:9096 --property parse.key=true --property "key.separator=:"

## How to Configure Kafka Producer
Kafka Producer can be configured by two ways: 

1. Adding properties to application.properties file.
2. Or programmatically way creating Kafka config class annotated with Spring Boot annotation
   **org.springframework.context.annotation.Configuration**.

   Go to **com.acme.common.config.kafka.KafkaConfig** to get more details.

   **Note: This way override any configuration defined in application.properties file.**

If you choose to configure Kafka Producer programmatically you will basically have to define next:

1. Define a Java Map with Producer configs. 
2. Create a ProducerFactory with the producer config map.
3. Create a KafkaTemplate with the producer factory.

### Bootstrap Servers List
Bootstrap servers list corresponds to Kafka brokers (list of the servers that compose your Kafka cluster). To configure
bootstrap servers list you will have to add next property into application.properties file:

    spring.kafka.producer.bootstrap-servers=localhost:9092,localhost:9094,localhost:9096

### Key and Value Serializers
Key and value serializers work together to serialize (convert to bytes) key and value's Kafka message. 
To configure key and value serializers you can use next properties into application.properties file:

      spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
      spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

### Kafka Producer Acknowledgements
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

### Kafka Producer Minimum Insync Replicas
Minimum insync replicas is the number of replicas that must acknowledge write to be considered successful.

You can configure Kafka producer minimum insync replicas when is creating new topic by CLI:

    ./kafka-topics.sh --create --topic topic_name --partitions 3 --replication-factor 3 --bootstrap-server host.docker.internal:9092 --config min.insync.replicas=2

Also, you can add new configuration to existing topic by CLI with next command:
    
    ./kafka-configs.sh --bootstrap-server host.docker.internal:9092 --alter --entity-type topics --entity-name topic_name --add-config min.insync.replicas=2

And, to check the configuration that you just modified run next command:

    ./kafka-topics.sh --describe --bootstrap-server host.docker.internal:9092

**Note: By default, the value of minimum insync replicas is 1.**

### Kafka Producer Retries 
This configuration defines the number of times to retry to send the message to Kafka broker if it fails.
To configure it you will have to add next property to application.properties file:

      spring.kafka.producer.retries

      Note: Default value is 2 147 483 647

On the other hand, you can specify the time in milliseconds to wait before retrying to send the message to Kafka broker.
In other words, it is the time (in milliseconds) interval between retries. To configure it you will have to add next 
two properties (minimum and maximum) to application.properties file:

      spring.kafka.producer.properties.retry.backoff.ms
   
      Note: Default value is 100

      spring.kafka.producer.properties.retry.backoff.max.ms

      Note: Default value is 1000

### Kafka Producer Delivery Timeout
This configuration defines the maximum timeout in milliseconds to wait response for all replicas after sending the message 
including retry operations. To configure it you will have to add next property to application.properties file:

      spring.kafka.producer.properties.delivery.timeout.ms

      Note: Default value is 120000 milleseconds (2 minutes).

This property specifies the time for entire process which includes:

1. Time to send the request
2. Time to wait for acknowledgement and
3. Time to retriable sent failures.

This value have to be greater than spring.kafka.producer.properties.request.timeout.ms property value

### Kafka Producer Batch Size
This configuration defines message batching delays sending messages in the hope that more messages destined for 
the same broker will be sent, allowing them to be batched into a single produce request. Batching is a compromise 
between higher latency in return for higher throughput. Time-based batching is configured using "linger.ms", 
and size-based batching is configured using "batch.size" in bytes.

If a maximum "batch.size" in bytes is used, a request is sent when the maximum is reached, or messages have been queued 
for longer than linger.ms (whichever comes sooner). Adding the delay allows batches to accumulate messages up to 
the batch size.

      spring.kafka.producer.properties.batch.size

      Note: Default value is 16384 bytes.

The linger property adds a delay in milliseconds so that larger batches of messages are accumulated and sent in a request. 
The default is 0'

      spring.kafka.producer.properties.linger.ms

      Note: Default value is 0.