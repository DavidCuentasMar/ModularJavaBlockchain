FROM openjdk:11.0.8

COPY /. /ModularJavaBlockchain
WORKDIR /ModularJavaBlockchain
#CMD ["java","-jar", "ModularJavaBlockchain.jar"]
#CMD ["java", "-jar", "./dist/ModularJavaBlockchain.jar", "9000", "172.26.0.2", "9000"]

#CMD ["java", "-jar", "./dist/ModularJavaBlockchain.jar", ${node}, ${host}, "9000"]

# COPY /Blockchain/src/blockchain /Blockchain/src/blockchain
# WORKDIR /Blockchain/src/blockchain
# RUN javac Blockchain.java
# CMD ["java", "Blockchain.jar"]