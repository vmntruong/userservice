# User service

This app exposes two services : 
* Register a user
* Get details of a user by his id

## How to build
<code>mvn clean install</code>

## Run
Let's run the <code>.jar</code> file created

<code>java -jar userservice-0.0.1-SNAPSHOT.jar --server.port=8282</code>

## Embedded database
<a>http://localhost:8282/h2-console/</a>
* Driver Class : org.h2.Driver
* JDBC URL: jdbc:h2:file:/data/userservice
* Username : sa
* Password :

## API Swagger
<a>http://localhost:8282/swagger-ui/</a>
