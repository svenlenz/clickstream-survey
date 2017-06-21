# clickstream-survey
Self-Contained System to collect survey data and clickstream events
The self contained system is a docker composion of the following images:
* TBD
    <!--
        http://divolte.io/
        https://hub.docker.com/r/mkieboom/divolte-collector-docker/
        https://github.com/divThis/docker-divolte
        export JAVA_HOME=`/usr/libexec/java_home`
        find ./divolte_clicklogs/published -name '*divolte-tracking-*.avro' | sort | tail -n1 | xargs ./divolte-collector-0.4.1/bin/avro-tools tojson --pretty
        https://www.slideshare.net/fvanvollenhoven/prototyping-online-ml-with-divolte-collector
        http://126kr.com/article/542jmbepx15 "Rapid Prototyping of Online Machine Learning with Divolte Collector"
        Kafka Anbindung: https://github.com/ayman-elgharabawy/Kafka-SpringBoot-WebSocket/tree/master/AngularDocker/public-html/app
        /login
    -->

## Setup

### Premise
the SCS relies on a shared host volume. he first thing you have to do is to point the environment variable to the shared data volume:

```
$ export SURVEY_DATA_VOLUME=/ABSOLULT_PATH_TO_GIT_PROJECT/data
```

if the variable isn't set, the docker-compose will not run, because it will be defaulted to an empty string (which isn't a valid path).
note: only absoulte path are possible!
hint: store it permanent in your .profile (or wherever you can store it in windows ;))


### Run on AWS

create instance:
```
docker-machine create -d amazonec2 --amazonec2-region eu-central-1 --amazonec2-vpc-id vpc-f0c74e98 clickstream-survey
```

environment:
 ```
 docker-machine env clickstream-survey
 ```

configure shell (From now on all docker, docker-machine and docker-compose commands will point to the AWS EC2 instance):
 ```
 eval $(docker-machine env clickstream-survey)
 ```

run & build:
```
docker-compose build
docker-compose up -d
```

TODO:
- how to get the files?
- backup?
-






ssh -i ./data/aws_clickstream.pem ec2-user@ec2-52-58-231-162.eu-central-1.compute.amazonaws.com

sudo yum install git

mkdir repos

cd repos

git clone https://github.com/svenlenz/clickstream-survey.git

/home/ec2-user/repos/clickstream-survey

scp -r -i ./data/aws_clickstream.pem ec2-user@ec2-52-58-231-162.eu-central-1.compute.amazonaws.com:/mnt/clickstream-data/ ./data

---
Formatieren:
sudo mkfs -t ext4 /dev/xvdb

Mount:
sudo mount /dev/xvdb /mnt/clickstream-data/


---

DOCKER
http://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html#install_docker

sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

DOCKER-COMPOSE
sudo  pip install docker-compose


---
docker-machine create -d amazonec2 --amazonec2-region eu-central-1 --amazonec2-vpc-id vpc-f0c74e98 clickstream-survey
eval "$(docker-machine env -u)"
eval $(docker-machine env clickstream-survey)


http://sandlab.cs.ucsb.edu/clickstream/

Wie es funktioniert: https://de.slideshare.net/HyunjeongLee1/unsupervised-clickstream-clustering-for-user-behavior-analysis-chi2016
