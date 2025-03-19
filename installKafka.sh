sudo apt update
sudo apt install -y openjdk-17-jdk maven
java -version
mvn -version
wget https://downloads.apache.org/kafka/3.9.0/kafka_2.13-3.9.0.tgz
tar -xvzf kafka_2.13-3.9.0.tgz
mv kafka_2.13-3.9.0 kafka
cd kafka
