package processing.kmeans;

import gov.sandia.cognition.learning.algorithm.clustering.KMeansClusterer;
import gov.sandia.cognition.learning.algorithm.clustering.cluster.CentroidCluster;
import gov.sandia.cognition.learning.algorithm.clustering.cluster.ClusterCreator;
import gov.sandia.cognition.learning.algorithm.clustering.cluster.VectorMeanCentroidClusterCreator;
import gov.sandia.cognition.learning.algorithm.clustering.divergence.CentroidClusterDivergenceFunction;
import gov.sandia.cognition.learning.algorithm.clustering.divergence.ClusterDivergenceFunction;
import gov.sandia.cognition.learning.algorithm.clustering.initializer.DistanceSamplingClusterInitializer;
import gov.sandia.cognition.learning.function.distance.EuclideanDistanceSquaredMetric;
import gov.sandia.cognition.math.Semimetric;
import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.math.matrix.VectorFactory;
import gov.sandia.cognition.math.matrix.Vectorizable;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import processing.utils.Big5Result;
import processing.utils.StatDump;

/**
 * This example demonstrates how to use the k-means clustering from the learning
 * package in the Cognitive Foundry. It demonstrates several different ways to
 * initialize k-means, depending on how much customization of the algorithm you
 * wish to perform.
 * 
 * @author Justin Basilico
 * @since 3.0
 */
public class SimpleKMeansExample {

	public static boolean USE_WINDOWS = false;
	public static boolean WITH_LIERS = true;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";
	static int numRequestedClusters = 3; // The "k" in k-means.

	public static HashMap<Integer, double[]> index = new HashMap<Integer, double[]>();
	
	public static ArrayList<ArrayList<double[]>> centroidIndex = new ArrayList<ArrayList<double[]>>();

	/**
	 * The main method where execution begins.
	 *
	 * @param arguments
	 *            The command-line arguments (ignored).
	 * @throws Exception 
	 */
	public static void main(final String... arguments) throws Exception {
		// Lets make up some random 2-dimensional data with values in [-1, +1].
		Collection<Vector> data = new ArrayList<Vector>();
		VectorFactory<?> vectorFactory = VectorFactory.getDefault();
		// for (int i = 0; i < 100; i++)
		// {
		// data.add(vectorFactory.createUniformRandom(2, -1.0, +1.0, random));
		// }
		//
		JSONParser parser = new JSONParser();
		System.out.println(Big5Result.csvHeader());
		for (int id = 1; id <= 126; id++) {

			Big5Result b5result = StatDump.toBig5Result(id);

			try {
				if (b5result.ehrlich <= 3 && !WITH_LIERS) {
					System.out.println("LIER!");
					continue;
				}
				String survey = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id
						+ "\\" : BASE_PATH_IOS + "/" + id + "/")
						+ "survey.json";
				String events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id
						+ "\\" : BASE_PATH_IOS + "/" + id + "/")
						+ "events.json";

				Object evt = parser.parse(new FileReader(events));
				JSONArray eventsArray = (JSONArray) evt;
				b5result.numberOfClicks = eventsArray.size();
				int duration = 0;
				Iterator evtIter = eventsArray.iterator();
				while (evtIter.hasNext()) {
					JSONObject event = (JSONObject) evtIter.next();
					duration += ((Long) event.get("duration")).intValue();
				}
				b5result.meanDuration = duration;

				double[] profilePoints = { b5result.meanNeuro,
						b5result.meanExtra, b5result.meanGewissen,
						b5result.meanOffen, b5result.meanVertrag };
				index.put(id, profilePoints);
				data.add(vectorFactory.copyArray(profilePoints));
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		for(int i = 0; i < 10000; i ++ ) {

		// We're going to need a random number generator both to create some
		// random data and because k-means does random initialization of the
		// clusters.
		final Random random = new Random();



		/*
		 * // Version 1: // Lets use the "easy" way to create a clusterer by
		 * just giving it // the number of clusters we want and use the default
		 * divergence metric // Euclidean distance. Note that the default
		 * k-means implementation uses // multi-threaded parallelism. int
		 * numRequestedClusters = 5; // The "k" in k-means.
		 * KMeansClusterer<Vector, CentroidCluster<Vector>> kMeans =
		 * KMeansFactory.create(numRequestedClusters, random);
		 * 
		 * // Now run the clustering to create the clusters.
		 * Collection<CentroidCluster<Vector>> clusters = kMeans.learn(data);
		 * printClusters("Version 1: ", clusters, false);
		 * printClusters("Version 1: ", clusters, true);
		 * assignMembers("Version 1a: ", clusters);
		 * 
		 * 
		 * kMeans.setNumRequestedClusters(5); clusters = kMeans.learn(data);
		 * printClusters("Version 1 (k=5): ", clusters);
		 * 
		 * // Version 2: // Now lets use a different metric, manhattan distance.
		 * kMeans = KMeansFactory.create(numRequestedClusters,
		 * ManhattanDistanceMetric.INSTANCE, random);
		 * 
		 * // Now run the clustering to create the clusters. clusters =
		 * kMeans.learn(data); printClusters("Version 2: ", clusters);
		 */
		// Version 3:
		// Lets create a non-parallel k-means implementation directly instead
		// of using the factory class. It shows all of the different things you
		// can change on the clusterer, but also shows why we provided the
		// factory class for common use-cases. We will also use the cosine
		// distance metric instead.

		
		
		if(centroidIndex.size() == 0) {
			for(int x = 0; x < numRequestedClusters; x++) {
				centroidIndex.add(new ArrayList<double[]>());
			}
		}
		Semimetric<Vectorizable> metric = EuclideanDistanceSquaredMetric.INSTANCE;
		int maxIterations = 200;
		ClusterCreator<CentroidCluster<Vector>, Vector> creator = VectorMeanCentroidClusterCreator.INSTANCE;
		DistanceSamplingClusterInitializer<CentroidCluster<Vector>, Vector> initializer = new DistanceSamplingClusterInitializer<CentroidCluster<Vector>, Vector>(
				metric, creator, random);
		ClusterDivergenceFunction<CentroidCluster<Vector>, Vector> clusterDivergence = new CentroidClusterDivergenceFunction<Vector>(
				metric);
		KMeansClusterer<Vector, CentroidCluster<Vector>> kMeans = new KMeansClusterer<Vector, CentroidCluster<Vector>>(
				numRequestedClusters, maxIterations, initializer,
				clusterDivergence, creator);

		// Now run the clustering to create the clusters.
		Collection<CentroidCluster<Vector>> clusters = kMeans.learn(data);
//		printClusters("Version 3: ", clusters, false);
		printClusters("Version 3: ", clusters, true);
		collectAllCentroids(clusters);

		for(int xx = 0; xx < centroidIndex.size()-1; xx++) {
			System.out.println(centroidIndex.get(xx).size());
			System.out.println(centroidIndex.get(xx+1).size());
			if(centroidIndex.get(xx).size() != centroidIndex.get(xx+1).size()) {
				throw new Exception("wrong number...");
			}
		} 		
		assignMembers("Version 3a: ", clusters, index);		
		}
		

		for(ArrayList<double[]> centroids : centroidIndex) {
			double n = 0.0;
			double e = 0.0;
			double a = 0.0;
			double c = 0.0;
			double o = 0.0;
			
			for(double[] clusterResult: centroids) {
				n += clusterResult[0];
				e += clusterResult[1];
				a += clusterResult[2];
				c += clusterResult[3];
				o += clusterResult[4];
			}
			System.out.print(Math.round(n/centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(e/centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(a/centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(c/centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(o/centroids.size()));
			System.out.println();
		}
	}

	/**
	 * Prints out the cluster centers.
	 *
	 * @param title
	 *            The title to print.
	 * @param clusters
	 *            The cluster centers.
	 */
	public static void printClusters(final String title,
			final Collection<CentroidCluster<Vector>> clusters, boolean round) {
		System.out.print(title);
		System.out.println("There are " + clusters.size() + " clusters.");
		int index = 0;
		for (CentroidCluster<Vector> cluster : clusters) {
			if (round) {
				System.out.print("    " + index + " ("
						+ cluster.getMembers().size() + ") : ");
				cluster.getCentroid().forEach(entry -> {
					System.out.print(Math.round(entry.getValue()) + " ");
				});
				System.out.println();
			} else {
				System.out.println("    " + index + " ("
						+ cluster.getMembers().size() + ") : "
						+ cluster.getCentroid());
			}
			index++;
			// Another useful method on a cluster is: cluster.getMembers()
		}
	}
	
	public static void collectAllCentroids(final Collection<CentroidCluster<Vector>> clusters) throws Exception {
		TreeMap<String, double[]> tt = new TreeMap<String, double[]>();
		int index = 0;
		for (CentroidCluster<Vector> cluster : clusters) {
			if(tt.containsKey(cluster.getMembers().size()+""+cluster.hashCode()))
				throw new Exception("try again");
				
			tt.put(cluster.getMembers().size()+""+cluster.hashCode(), cluster.getCentroid().toArray());			
		}
		System.out.println(tt.keySet());
		for(String key : tt.keySet()) {
			double[] vector = tt.get(key);
			centroidIndex.get(index).add(vector);
			index++;
		}
	}

	public static void assignMembers(final String title,
			final Collection<CentroidCluster<Vector>> clusters,
			HashMap<Integer, double[]> profilePoints) {
		System.out.print(title);
		System.out.println("There are " + clusters.size() + " clusters.");
		int countMatches = 0;
		ArrayList<Integer> clusterOne = new ArrayList<Integer>();
		ArrayList<Integer> clusterTwo = new ArrayList<Integer>();
		ArrayList<Integer> clusterThree = new ArrayList<Integer>();
		ArrayList<Integer> clusterFour = new ArrayList<Integer>();
		ArrayList<Integer> clusterFive = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> clusterIdx = new ArrayList<ArrayList<Integer>>();
		clusterIdx.add(clusterOne);
		clusterIdx.add(clusterTwo);
		clusterIdx.add(clusterThree);
		clusterIdx.add(clusterFour);
		clusterIdx.add(clusterFive);
		for (Entry<Integer, double[]> entry : profilePoints.entrySet()) {
			int index = 0;
			for (CentroidCluster<Vector> cluster : clusters) {
				for (Vector v : cluster.getMembers()) {
					if (Arrays.equals(v.toArray(), entry.getValue())) {
						countMatches++;
						clusterIdx.get(index).add(entry.getKey());
						index = 0;
						break;
					}
				}
				index++;
			}
			;
		}
		System.out.print(clusterOne);System.out.println(" (" + clusterOne.size()+")");
		System.out.print(clusterTwo);System.out.println(" (" + clusterTwo.size()+")");
		System.out.print(clusterThree);System.out.println(" (" + clusterThree.size()+")");
		System.out.print(clusterFour);System.out.println(" (" + clusterFour.size()+")");
		System.out.print(clusterFive);System.out.println(" (" + clusterFive.size()+")");
	}

}