FROM java:8
MAINTAINER sven.lenz@msc.htwchur.ch

# Install maven
RUN apt-get clean && apt-get update && apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN mvn dependency:resolve && mvn verify

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN mvn package

EXPOSE 4568
ENTRYPOINT ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/ProcessingServer-jar-with-dependencies.jar"]
