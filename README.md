# publicis-sapient-assignment

Git location : https://github.com/labb1982/publicis-sapient-assignment

After mvn clean install, the code coverage report will be available at : <BASE_DIR>/target/site/jacoco/index.html

In an EC2 instance, the following artifacts need to be installed:
Openjdk-8, maven, GIT, Jenkins, Docker

Define a Jenkins pipeline with the content from the Jenkinsfile available under the root directory of the Git Codebase

The required Dockerfile is also available under the root directory of the Git Codebase

During the build of Jenkins pipeline, 

1. the code will be pulled from the git location mentioned above

2. maven clean package will be executed to generate target jar

3. a docker image will be generated and will be pushed to https://hub.docker.com/repository/docker/labbdocker/lab82jenkinsrepo


In the second EC2 instance,

install docker

run the following command: docker run -dp  28082:28082 labbdocker/lab82jenkinsrepo

In the browser, open http://HOST:28082/swagger-ui/index.html

Try out : GET  ​/weather​/1.0​/forecast  (=> http://HOSTNAME:28082//weather/1.0/forecast) 

Query parameters are:

   1. city : mandatory
   
   2. thresholdDate : reference date, default: today. To get a full response, try with  thresholdDate = 2017-01-17
   
   3. minNDays : starting date from the thresholdDate, default : thresholdDate + 1
   
   4. maxNDays : ending date(inclusive) from the thresholdDate, default : thresholdDate + 3
