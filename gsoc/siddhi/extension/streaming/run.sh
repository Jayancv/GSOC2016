sudo mvn clean package
sudo rm -rf /home/mahesh/GSOC/WSO2/pre-build/wso2cep-4.1.0/repository/components/lib/streaming-1.0-SNAPSHOT.jar 
sudo cp target/streaming-1.0-SNAPSHOT.jar  /home/mahesh/GSOC/WSO2/pre-build/wso2cep-4.1.0/repository/components/lib/streaming-1.0-SNAPSHOT.jar
sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 /home/mahesh/GSOC/WSO2/pre-build/wso2cep-4.1.0/bin/wso2server.sh debug 5005
