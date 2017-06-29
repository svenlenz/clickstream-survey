package processing.kmeans;

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
import processing.utils.Big5Result;
import processing.utils.StatDump;

/**
 * create clickstream prototype personalities based on the big5 survey results.
 * surveys are clustered with k-means ( Cognitive Foundry ) and euclidean distance for NEOACE dimension vectors
 * Average of NEOAC are calculated over N iterations (default n = 10'000)
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class CreatePrototypesWithKMeans {

	public static boolean USE_WINDOWS = true;
	public static boolean WITH_LIERS = true;
	public static String BASE_PATH_WINDOWS = "..\\results\\survey_results\\";
	public static String BASE_PATH_IOS = "../results/survey_results/";
	static int numRequestedClusters = 3; // The "k" in k-means.
	static int iterations = 10000; //number of iterations, result is average between all those iterations

	public static HashMap<Integer, double[]> index = new HashMap<Integer, double[]>();

	public static ArrayList<ArrayList<double[]>> centroidIndex = new ArrayList<ArrayList<double[]>>();

	public static void main(final String... arguments) throws Exception {
		Collection<Vector> data = new ArrayList<Vector>();
		VectorFactory<?> vectorFactory = VectorFactory.getDefault();
		JSONParser parser = new JSONParser();
		System.out.println(Big5Result.csvHeader());
		for (int id = 1; id <= 126; id++) {

			Big5Result b5result = StatDump.toBig5Result(id);

			try {
				if (b5result.ehrlich <= 3 && !WITH_LIERS) {
					System.out.println("LIER!");
					continue;
				}
				String events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
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
				b5result.duration = duration;

				double[] profilePoints = { b5result.neuro, b5result.extra, b5result.gewissen,
						b5result.offen, b5result.vertrag };
				index.put(id, profilePoints);
				data.add(vectorFactory.copyArray(profilePoints));
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		for (int i = 0; i < iterations; i++) {

			final Random random = new Random();

			if (centroidIndex.size() == 0) {
				for (int x = 0; x < numRequestedClusters; x++) {
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
					numRequestedClusters, maxIterations, initializer, clusterDivergence, creator);

			// Now run the clustering to create the clusters.
			Collection<CentroidCluster<Vector>> clusters = kMeans.learn(data);
			printClusters("Version 3: ", clusters, true);
			collectAllCentroids(clusters);

			for (int xx = 0; xx < centroidIndex.size() - 1; xx++) {
				System.out.println(centroidIndex.get(xx).size());
				System.out.println(centroidIndex.get(xx + 1).size());
				if (centroidIndex.get(xx).size() != centroidIndex.get(xx + 1).size()) {
					throw new Exception("wrong number...");
				}
			}
			assignMembers("Version 3a: ", clusters, index);
		}

		for (ArrayList<double[]> centroids : centroidIndex) {
			double n = 0.0;
			double e = 0.0;
			double a = 0.0;
			double c = 0.0;
			double o = 0.0;

			for (double[] clusterResult : centroids) {
				n += clusterResult[0];
				e += clusterResult[1];
				a += clusterResult[2];
				c += clusterResult[3];
				o += clusterResult[4];
			}
			System.out.print(Math.round(n / centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(e / centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(a / centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(c / centroids.size()));
			System.out.print(" ");
			System.out.print(Math.round(o / centroids.size()));
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
	public static void printClusters(final String title, final Collection<CentroidCluster<Vector>> clusters,
			boolean round) {
		System.out.print(title);
		System.out.println("There are " + clusters.size() + " clusters.");
		int index = 0;
		for (CentroidCluster<Vector> cluster : clusters) {
			if (round) {
				System.out.print("    " + index + " (" + cluster.getMembers().size() + ") : ");
				cluster.getCentroid().forEach(entry -> {
					System.out.print(Math.round(entry.getValue()) + " ");
				});
				System.out.println();
			} else {
				System.out
						.println("    " + index + " (" + cluster.getMembers().size() + ") : " + cluster.getCentroid());
			}
			index++;
		}
	}

	public static void collectAllCentroids(final Collection<CentroidCluster<Vector>> clusters) throws Exception {
		TreeMap<String, double[]> tt = new TreeMap<String, double[]>();
		int index = 0;
		for (CentroidCluster<Vector> cluster : clusters) {
			if (tt.containsKey(cluster.getMembers().size() + "" + cluster.hashCode()))
				throw new Exception("try again");

			tt.put(cluster.getMembers().size() + "" + cluster.hashCode(), cluster.getCentroid().toArray());
		}
		System.out.println(tt.keySet());
		for (String key : tt.keySet()) {
			double[] vector = tt.get(key);
			centroidIndex.get(index).add(vector);
			index++;
		}
	}

	public static void assignMembers(final String title, final Collection<CentroidCluster<Vector>> clusters,
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
		System.out.print(clusterOne);
		System.out.println(" (" + clusterOne.size() + ")");
		System.out.print(clusterTwo);
		System.out.println(" (" + clusterTwo.size() + ")");
		System.out.print(clusterThree);
		System.out.println(" (" + clusterThree.size() + ")");
		System.out.print(clusterFour);
		System.out.println(" (" + clusterFour.size() + ")");
		System.out.print(clusterFive);
		System.out.println(" (" + clusterFive.size() + ")");
	}

}