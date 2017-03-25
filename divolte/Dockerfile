FROM java:8

MAINTAINER sven.lenz@msc.htwchur.ch

WORKDIR /usr/local/

RUN wget http://divolte-releases.s3-website-eu-west-1.amazonaws.com/divolte-collector/0.4.1/distributions/divolte-collector-0.4.1.tar.gz && \
    tar -xzvf divolte-collector-0.4.1.tar.gz --strip-components=1 && \
    rm divolte-collector-0.4.1.tar.gz

ADD conf/divolte-collector.conf /usr/local/conf/

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

EXPOSE 8290

ENTRYPOINT ["divolte-collector"]