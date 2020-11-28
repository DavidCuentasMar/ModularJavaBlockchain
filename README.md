# Modular BlockChain Simulator

Academic Modular Blockchain Initiative. This project is intended to be open-source and for academic propose. The main idea is to create an application to simulate the behavior of a blockchain node and then connect to other nodes through peer to peer network. Also, the system is modular, and with time and collaboration, it might have more options and structures to represent the blockchain, the java contracts, the transactions, etc.

## 1. Before local or docker run it

For this first version, you have to simply build the project before the run with docker-compose or run it locally. Also, maybe you will have problems with the version of java at the project level, but you only have to resolve problems and config your java compatible version.

## 2. Install require libraries

You must install some external libraries manually, they are at the top file level in the folder call it "jar-requieres".

### 2b. Second option

You can download them from the source page:

#### Free Pastry:

https://www.freepastry.org/FreePastry/

#### Jackson:

https://github.com/FasterXML/jackson-core

#### Important note:

In the future we want to make compatible with maven and don have to make the whole process to add the libraries.

## 3. run locally

you have to open a terminal and run this command to boot up the genesis node:

#### the structure of the command:

    $ java -jar dist/ModularJavaBlockchain.jar localbindport bootIP bootPort

#### the example for the genesis node:

    $ java -jar dist/ModularJavaBlockchain.jar 9000 192.168.1.11 9000

#### if you want to start i nodes in the same network:

    $ example java -jar dist/ModularJavaBlockchain.jar 9000+i 192.168.1.11 9000

## 4. Docker Installation

In this step you have to install Docker and docker-compose whit docker-compose.yml.

##### see the documentation: https://www.docker.com/

## 5. docker-compose

#### create image and up containers

    $ docker-compose up --build

#### if you want to see the logs of a specific container

    $ docker logs --follow [your_docker_container_id]

#### to stop all running containers

    $ docker-compose down

### 6. Common troubles

Some times in the docker-compose .yml are specified the internal IP that all the nodes connect when they up and because this is a first version you have to figure out whit what IP the node_1 is boot up and configure into .yml file, to make all the others nodes connect to the genesis node.

### you can use the next literaly command to see all the address ip of containers

    $ docker inspect -f '{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)
