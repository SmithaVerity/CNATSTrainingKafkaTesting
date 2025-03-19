cd kafka
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
mkdir spring-kafka-app && cd spring-kafka-app
mvn archetype:generate -DgroupId=com.example -DartifactId=kafka-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
cd kafka


bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092
