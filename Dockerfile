FROM openjdk:11.0.8

COPY /dist /dist
WORKDIR /dist
#CMD ["java","-jar", "ModularJavaBlockchain.jar"]
CMD ["java", "-jar", "ModularJavaBlockchain.jar", "9000", "172.17.0.1", "9000"]
# COPY /Blockchain/src/blockchain /Blockchain/src/blockchain
# WORKDIR /Blockchain/src/blockchain
# RUN javac Blockchain.java
# CMD ["java", "Blockchain.jar"]