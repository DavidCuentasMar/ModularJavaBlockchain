# BlcockChain Modular Simulator

Academic Modular Blockchain Initiative

# Instructions

For this first approach you have to simple build the project out side before create the docker imagen and run it, because currently there is a problem that we aren't solved yet with the libraries

# Install require libraries

You must install json from the group org.json through this link:

https://jar-download.com/artifacts/org.json/json/20131018/source-code

Also free pastry:

https://www.freepastry.org/FreePastry/

### important note:

The .jar requires for the project are in the the folder called "jar-requires" at the top of the project main folder, so you only have to install manually.

# Docker Installation

In this step you have two possibilities with Dockerfile or whit docker-compose.yml.

## DockerFile

### docker create image

    $ docker build -t blockchain-image .

### docker run and see the console

    $ docker run -it --rm --name blockchain blockchain-image

## docker-compose

### create image and up containers

    $ docker-compose up --build

### to stop all running containers

    $ docker-compose down

### to see all the address ip of containers

    $ docker inspect -f '{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)

## Run local

    $ java -jar ModularJavaBlockchain.jar localbindport bootIP bootPort

    $ example java -jar ModularJavaBlockchain.jar 9000 192.168.1.11 9000
