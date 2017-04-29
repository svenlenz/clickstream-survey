# Recursive Hierarchical Clustering

This scirpt is used to perform user behaviour clustering. Users are put into a hierachy of clusters, each identified by a list of features.

---
##Usage
The main file is `recursiveHierarchicalCustering.py`, can be used either through command line interface or simply import it.

###Command Line Interface

```
$> python recursiveHierarchicalCustering.py synTrace.txt sample/ 0.05
```

#### Arguments
**inputPath**: (e.g. synTrace.txt) specify the path to a files that contains information for the users to be clustered. Each line is represent a user.
> user_id \t A(1)G(10)

Where the `A` and `G` are actions and `1` and `10` are the frequencies of each action. The `user_id` grows from 1 to the total number of users.

**outputPath**: The directory to place all temporary files as well as the final result.

**sizeThreshold**: Defines the minimum size of the cluster, that we are going to further divide.
`0.05` means clusters containing less than 5% of the total instances is not going to be further splitted.


### Python Interface
```python
import recursiveHierarchicalClustering as rhc> 
data = rhc.getSidNgramMap(inputPath)> 
matrixPath = '%smatrix.dat' % inputPath> 
treeData = rhc.runDiana(outputPath, data, matrixPath)
```

There treeData is the resulting cluster tree. Same as `resultDir/result.json` if ran through CLI.

### Result
**resultDir/matrix.dat**: A distance matrix for the root level is stored to avoid repeated calculation. If the file is available, the scirpt will read in the matrix instead of calculating it again. The file format is a N*N distance matrix scaled to integer in the range of (0-100).

**resultDir/result.json**: Stores the clustering result, in the form of `['t', subcluster list, cluster info]` or `['l', user list, cluster info]`.

* **Node type**: node type can be either `t` or `l`. `l` means leaf which means the cluster is not further split. `t` means tree meaning there are further splitting for the given cluster.
* **Subcluster list**: a list of clusters that is the resulting cluster derived from splitting the current cluster.
* **User list**: a list of user ids repesenting the users in the given cluster.
* **Cluster info**: a dictionary containing meta data for the cluster.
	* **gini**: gini coefficient for chi-square score value distribution, measures the skewness of feature importance distribution.
	* **sweetspot**: the modularity for the best `k` we picked when further splitting this cluster.
	* **exlusions**: a list of top features (ranked) that helps to distinguish the cluster from others.
	* **exclusionsScore**: the chi-square scores correspond to the top features listed in **exclusions**.
	
##Configuration
This script is designed to be run distributedly on multiple machines with shared file system. However, it is also be configed to run locally.
The configuration is stored in `server.json` in the following format:
```javascript
{
	"threadNum": 5,
	"minPerSlice": 1000,
	"server":
		["server1.example.com", "server2.example.com"]
}	
```

* **threadNum** specifies how many threads can be ran on each server.

* **minPerSlice** specifies the minimum nember of users each thread should handle.
* **server** specifies the server to be used for matrix computation task. If you want to run it locally, specify it as `["localhost"]`.

##Publications

* Gang Wang, Tristan Konolige, Christo Wilson, Xiao Wang, Haitao Zheng and Ben Y. Zhao [You are How You Click: Clickstream Analysis for Sybil Detection](http://www.cs.ucsb.edu/~ravenben/publications/abstracts/clickstream-usenixsec13.html)
* Gang Wang, Xinyi Zhang, Shiliang Tang, Haitao Zheng, Ben Y. Zhao [Unsupervised Clickstream Clustering For User Behavior Analysis](http://www.cs.ucsb.edu/~ravenben/publications/abstracts/clickstream-chi16.html) 
Proceedings of SIGCHI Conference on Human Factors in Computing Systems (CHI) 
San Jose, CA, May 2016. 

##Dependency
Python library `numpy` is required.