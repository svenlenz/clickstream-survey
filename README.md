# clickstream-survey

## clickstream-survey application
Self-Contained System to collect survey data and clickstream events
The self contained system is a docker composion of the following images:
* survey: nginx server & client application
* Processing: Server application to store server results
* Divolte: clickstream collector


### Setup

#### Premise
the SCS relies on a shared host volume. he first thing you have to do is to point the environment variable to the shared data volume:

```
$ export SURVEY_DATA_VOLUME=/ABSOLULT_PATH_TO_GIT_PROJECT/data
```

if the variable isn't set, the docker-compose will not run, because it will be defaulted to an empty string (which isn't a valid path).
note: only absoulte path are possible!
hint: store it permanent in your .profile (or wherever you can store it in windows ;))


## Clusterings
Two python clustering approaches are integrated into this repository.
All rights to this code belong to the original authors & the code was not changed from my side.
A third approach is based on a java library. 

### Divisive Hierarchical Clustering
See: https://github.com/svenlenz/clickstream-survey/tree/master/clustering
Credits: http://sandlab.cs.ucsb.edu/clickstream/

### Probabilistic Latent Semantic Analysis
See: https://github.com/svenlenz/clickstream-survey/tree/master/PLSA
Credits: https://github.com/laserwave/PLSA

### K-Means
Java implementaiton: 
see: https://github.com/svenlenz/clickstream-survey/tree/master/processing/src/main/java/processing/kmeans
Credits: using a cognitive foundry library (https://github.com/algorithmfoundry/Foundry)

## Results
* Anonymised big5 & event results for 126 user sessions: https://github.com/svenlenz/clickstream-survey/tree/master/results/survey_results
* different clickstream modelings used for divisive hierarchical clustering (+evaluation): https://github.com/svenlenz/clickstream-survey/tree/master/results/clickstreams