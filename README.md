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