# connectedcars
 Produce Data to Kafka Topic
 
 
 
 # Kafka Setup
 
 Download the Kafka https://kafka.apache.org/downloads

# Start the Zookeeper-
C:\kafka\bin\windows>zookeeper-server-start.bat ..\..\config\zookeeper.properties

# Start the Zookeeper-
C:\kafka\bin\windows>kafka-server-start.bat ..\..\config\server.properties

# Create a kafka topic
C:\kafka\bin\windows>
kafka-topics.bat --create --topic connected-cars-topic --zookeeper localhost:2181 --partitions 3 --replication-factor 1

# Keep Check the Kafka Published Messages
C:\kafka\bin\windows>kafka-console-consumer.bat --bootstrap-server LT167-Revanas.aditiconsulting.com:9092 --topic connected-cars-topic
