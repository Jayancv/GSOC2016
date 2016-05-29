GSOC2016 Project 

JAVARDD
*https://spark.apache.org/docs/1.2.1/mllib-data-types.html
http://spark.apache.org/docs/latest/programming-guide.html
http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/java/function/package-summary.html
*http://spark.apache.org/docs/latest/programming-guide.html#resilient-distributed-datasets-rdds
http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/java/JavaRDD.html
*http://spark.apache.org/docs/latest/mllib-data-types.html




1057  mvn archetype:generate -DgroupId=org.gsoc.siddhi.extension.math -DartifactId=math -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
 1058  ls
 1059  mvn archetype:generate -DgroupId=org.gsoc.ml.streaming.algorithms -DartifactId=algorithms -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
 1060  ls
 1061  cd algorithms/
 1062  ls
 1063  sudo mvn clean install
 1064  ls
 1065  sudo mvn clean package
 1066  spark-submit --class org.gsoc.ml.streaming.algorithms --master local[*] target/algorithms-1.0-SNAPSHOT.jar 
 1067  spark-submit --class org.gsoc.ml.streaming.algorithms.StreamingLinearRegression --master local[*] target/algorithms-1.0-SNAPSHOT.jar 
 1068  cd ..
 1069  ls
 1070  cd ..
 1071  ls
 1072  cd ..
 1073  ls
 1074  cd siddhi/extension/
 1075  ls
 1076  mvn archetype:generate -DgroupId=org.gsoc.siddhi.extension.streaming -DartifactId=streaming -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
 1077  ls
 1078  cd streaming
 1079  ls
 1080  sudo mvn clean install
 1081  cd target/
 1082  ls
 1083  sudo cp streaming-1.0-SNAPSHOT.jar /home/mahesh/GSOC/WSO2/pre-build/wso2cep-4.1.0/repository/components/lib/
 1084  ls
 1085  cd GSOC/WSO2/pre-build/wso2cep-4.1.0/bin/
 1086  $JAVA_HOME
 1087  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1088  ls
 1089  cd GSOC/GSOC2016/gsoc/
 1090  ls
 1091  cd siddhi/extension/streaming/
 1092  ls
 1093  sudo mvn clean install
 1094  sudo mvn clean package
 1095  ls
 1096  cd ..
 1097  ls
 1098  cd ..
 1099  ls
 1100  cd ..
 1101  ls
 1102  cd ..
 1103  ls
 1104  cd ..
 1105  s;l
 1106  ls
 1107  pwd
 1108  cd GSOC2016/
 1109  ls
 1110  git status
 1111  git add -A
 1112  git commit -am "add StreamingLinearRegression classes"
 1113  git push origin master
 1114  ls
 1115  cd GSOC/WSO2/pre-build/wso2cep-4.1.0/bin/
 1116  $JAVA_HOME
 1117  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1118  cd ..
 1119  ls
 1120  cdsudo rm -rf repository/components/lib/streaming-1.0-SNAPSHOT.jar 
 1121  ls
 1122  cd repository/components/lib/
 1123  ls
 1124  cd ..
 1125  ls
 1126  cd ..
 1127  ls
 1128  cd ..
 1129  ls
 1130  cd bin
 1131  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1132  man ps
 1133  sudo netstat -nlo
 1134  sudo netstat -nlp
 1135  fuser -n tcp -k 5005
 1136  sudo fuser -n tcp -k 5005
 1137  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1138  ls
 1139  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1140  fuser -n tcp -k 5005
 1141  sudo fuser -n tcp -k 5005
 1142  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1143  sudo fuser -n tcp -k 5005
 1144  sudo JAVA_HOME=/usr/local/java/jdk1.8.0_51 ./wso2server.sh debug 5005
 1145  pwd
 1146  cd GSOC/GSOC2016/gsoc/siddhi/extension/streaming/
 1147  ls
 1148  sudo mvn clean install
 1149  sudo cp target/streaming-1.0-SNAPSHOT.jar /home/mahesh/GSOC/WSO2/pre-build/wso2cep-4.1.0/repository/components/lib/
 1150  ls
 1151  cd ls
 1152  cd GSOC/WSO2/pre-build/
 1153  ls
 1154  cd
 1155  ls
 1156  cd GSOC/GSOC2016/gsoc/siddhi/extension/streaming/
 1157  ls
 1158  sudo mvn clean install
 1159  sudo cp target/streaming-1.0-SNAPSHOT.jar /home/mahesh/GSOC/WSO2/pre-build/wso2cep-4.1.0/repository/components/lib/
 1160  ls
 1161  history
mahesh@mahesh-ThinkPad-X230:~/GSOC/GSOC2016/gsoc/siddhi/extension/streaming$ 

