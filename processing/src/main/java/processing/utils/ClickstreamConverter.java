package processing.utils;

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
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClickstreamConverter {

	static ArrayList<double[]> matrix = new ArrayList<double[]>();
	static HashMap<String, Integer> eventMap = new HashMap<String, Integer>();

	public static void main(String[] args) throws Exception {
		resetEventMap();

		JSONParser parser = new JSONParser();

		for (int i = 1; i <= 126; i++) {

			try {
				// i = 61;
				Object obj = parser.parse(new FileReader(
				// "/Users/sle/switchdrive/Master/survey_results/clickers/" + i
				// + "/events.json"));
						"/Users/sle/switchdrive/Master/survey_results/" + i + "/events.json"));
				// "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+i+"\\events.json"));

				JSONArray events = (JSONArray) obj;
				// events = shuffleJsonArray(events);
				Iterator<JSONObject> iterator = events.iterator();
				String clusteringEventLogDetailed = "";
				String clusteringEventLogCondensed = "";
				String clusteringEventLogCounter = "";
				boolean first = true;
				Long durationSinceLastEvent = 1L;
				int normalizedDurationSinceLastEvent = 1;

				String lastEvent = null;
				int counter = 1;
				boolean lastEventMachted = false;
				boolean fromDetail = false;
				boolean hasBackToProduct = false;
				boolean hasBackToOverview = false;
				boolean fromProduct = false;
				boolean hasBrowserBackToProduct = false;
				boolean hasBrowserBackToOverview = false;
				while (iterator.hasNext()) {
					JSONObject event = iterator.next();

					// System.out.println(event);
					String eventId = (String) event.get("event");
					String datetime = (String) event.get("datetime");
					Long duration = (Long) event.get("duration");
					String sessionId = (String) event.get("sessionId");
					String productId = (String) event.get("productId");
					String detail = (String) event.get("detail");
					String linkId = (String) event.get("linkId");
					durationSinceLastEvent = duration;
					// Random random = new Random();
					// if (random.nextBoolean()) {
					// continue;
					// }

					if (first) {
						// clusteringEventLogDetailed = sessionId + "\t";
						clusteringEventLogDetailed = i + "\t";
						clusteringEventLogCondensed = i + "\t";
						clusteringEventLogCounter = i + "\t";
						first = false;
					}

					if ("undefined".equals(productId)) {
						productId = "";
					}

					if ("undefined".equals(detail)) {
						detail = "";
					}

					if ("undefined".equals(linkId)) {
						linkId = "";
					}

					hasBrowserBackToOverview = false;
					hasBrowserBackToProduct = false;
					if (eventId.contains("backToProduct") || eventId.contains("home")) {
						hasBackToProduct = true;
					}
					if (eventId.contains("backToOverview") || eventId.contains("home")) {
						hasBackToOverview = true;
					}
					if (!"".equals(detail)) {
						fromDetail = true;
					} else if ("".equals(detail) && fromDetail && !hasBackToProduct) {
						fromDetail = false;
						hasBrowserBackToProduct = true;
					} else if (!"".equals(productId) && hasBackToProduct) {
						fromDetail = false;
						hasBackToProduct = false;
					}
					if (!"".equals(productId)) {
						fromProduct = true;
					} else if ("".equals(productId) && fromProduct && !hasBackToOverview) {
						fromProduct = false;
						hasBackToOverview = false;
						hasBrowserBackToOverview = true;
					} else if ("".equals(productId) && fromProduct && hasBackToOverview) {
						fromProduct = false;
						hasBackToOverview = false;
					}

					// LAST EVENT MEMORYSATION //
					if (lastEvent == null || lastEvent.equals(eventId)) {
						counter++;
					} else {
						clusteringEventLogCounter += lastEvent + "(" + counter + ")";
						counter = 1;
						lastEventMachted = true;
					}
					lastEvent = eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId : "");

					int durationInSeconds = (int) (durationSinceLastEvent / 1000);
					if (durationInSeconds == 0)
						durationInSeconds = 1;

					if (duration == 0)
						duration = 1L;
					// add browserBack Events
					if (hasBrowserBackToProduct) {
						clusteringEventLogDetailed += "browserBackToProduct" + "(" + 1 + ")";
					}

					if (hasBrowserBackToOverview) {
						clusteringEventLogDetailed += "browserBackToOverview" + "(" + 1 + ")";
					}
					
//					String eventDetailed = eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId : "") + (!"".equals(detail) ? "Detail" + detail : "");
					if (eventMap.containsKey(eventId)) {
						Long test = eventMap.get(eventId) + duration;
						eventMap.put(eventId, test.intValue());
					} else {
						throw new Exception(eventId);
					}


					// clusteringEventLogDe|tailed += eventId + productId +
					// detail + linkId + "(" + duration + ")";
					// clusteringEventLogDetailed += eventId + "(" +
					// durationInSeconds + ")";
					// clusteringEventLogDetailed += eventId + "(" +
					// durationInSeconds + ")";
					// clusteringEventLogDetailed += eventId +
					// (("".equals(detail) && "".equals(linkId)) ? "Product" +
					// productId : "") + (!"".equals(detail) ? "Detail" + detail
					// : "") + linkId + "(" + duration + ")";//
					// clusteringEventLogDetailed += eventId + productId +
					// detail + linkId + "(" + normalizedDurationSinceLastEvent
					// + ")";
					// clusteringEventLogDetailed += eventId +
					// (("".equals(detail) && "".equals(linkId)) ? "Product" +
					// productId : "") + (!"".equals(detail) ? "Detail" + detail
					// : "") + linkId + "(" + duration + ")";//
					// clusteringEventLogDetailed += eventId + productId +
					// detail + linkId + "(" + normalizedDurationSinceLastEvent
					// + ")";
					clusteringEventLogDetailed += eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" : "") + (!"".equals(detail) ? "Detail" : "") + "(" + duration + ")";// clusteringEventLogDetailed
																																														// +=
																																														// eventId																																									// +
																																														// ")";
					// clusteringEventLogDetailed += eventId +
					// (("".equals(detail) && "".equals(linkId)) ? "Product" +
					// productId : "") + (!"".equals(detail) ? "Detail" + detail
					// : "") + linkId + "(" + normalizedDurationSinceLastEvent +
					// ")";

					// PLSA version
					// clusteringEventLogDetailed += eventId +
					// (("".equals(detail) && "".equals(linkId)) ? "Product" +
					// productId : "") + (!"".equals(detail) ? "Detail" + detail
					// : "") + linkId + " ";

					// clusteringEventLogDetailed += eventId + "(" +
					// (("".equals(detail) && "".equals(linkId)) ? productId :
					// "") + (!"".equals(detail) ? "1" + detail : "") + ")";//
					// clusteringEventLogDetailed += eventId + productId +
					// detail + linkId + "(" + normalizedDurationSinceLastEvent
					// + ")";
					// if(clusteringEventLogDetailed.endsWith("()")) {
					// clusteringEventLogDetailed =
					// clusteringEventLogDetailed.replace("()", "(1)");
					// }else if(clusteringEventLogDetailed.endsWith("(0)")) {
					// clusteringEventLogDetailed =
					// clusteringEventLogDetailed.replace("(0)", "(1)");
					// }

					// clusteringEventLogDetailed += eventId + "(" +
					// normalizedDurationSinceLastEvent + ")";
					clusteringEventLogCondensed += eventId.charAt(0) + productId + "(" + normalizedDurationSinceLastEvent + ")";
					if (0 == durationSinceLastEvent) {
						durationSinceLastEvent = 1L;
						normalizedDurationSinceLastEvent = 1;
					} else {
						if (durationSinceLastEvent <= 1000) {
							normalizedDurationSinceLastEvent = 2;
						}

						if (durationSinceLastEvent > 1000 && durationSinceLastEvent <= 2000) {
							normalizedDurationSinceLastEvent = 3;
						}

						if (durationSinceLastEvent > 2000 && durationSinceLastEvent <= 3000) {
							normalizedDurationSinceLastEvent = 4;
						}

						if (durationSinceLastEvent > 3000 && durationSinceLastEvent <= 5000) {
							normalizedDurationSinceLastEvent = 5;
						}

						if (durationSinceLastEvent > 5000 && durationSinceLastEvent <= 8000) {
							normalizedDurationSinceLastEvent = 6;
						}

						if (durationSinceLastEvent > 8000 && durationSinceLastEvent <= 13000) {
							normalizedDurationSinceLastEvent = 7;
						}

						if (durationSinceLastEvent > 13000 && durationSinceLastEvent <= 20000) {
							normalizedDurationSinceLastEvent = 8;
						}

						if (durationSinceLastEvent > 20000 && durationSinceLastEvent <= 30000) {
							normalizedDurationSinceLastEvent = 9;
						}

						if (durationSinceLastEvent > 30000 && durationSinceLastEvent <= 60000) {
							normalizedDurationSinceLastEvent = 10;
						}

						if (durationSinceLastEvent > 60000) {
							normalizedDurationSinceLastEvent = 11;
						}
					}

				}

				if (!lastEventMachted) {
					clusteringEventLogCounter += lastEvent + "(" + counter + ")";
				}

				String categories = clusteringEventLogDetailed.replaceAll("browserBackToProduct", "back").replaceAll("browserBackToOverview", "back").replaceAll("backToProduct", "back").replaceAll("backButton", "back").replaceAll("backToOverview", "back").replaceAll("playingVideo", "video").replaceAll("pausedVideo", "video").replaceAll("endedVideo", "video").replaceAll("favorite_true", "favorite").replaceAll("favorite_false", "favorite").replaceAll("discount_on", "discount")
						.replaceAll("discount_off", "discount");

				clusteringEventLogCounter = clusteringEventLogCounter.replaceAll("browserBackToProduct", "back").replaceAll("browserBackToOverview", "back").replaceAll("backToProduct", "back").replaceAll("backButton", "back").replaceAll("backToOverview", "back").replaceAll("playingVideo", "video").replaceAll("pausedVideo", "video").replaceAll("endedVideo", "video").replaceAll("favorite_true", "favorite").replaceAll("favorite_false", "favorite").replaceAll("discount_on", "discount")
						.replaceAll("discount_off", "discount");

				// System.out.println(clusteringEventLogDetailed);
				// System.out.println(categories);
				// System.out.println(clusteringEventLogCondensed);
				System.out.println(clusteringEventLogCounter);

				double[] vector = new double[eventMap.keySet().size()];
				int x = 0;
				for (String key : eventMap.keySet()) {
					vector[x] = eventMap.get(key);
					x++;
				}
				matrix.add(vector);
				resetEventMap();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Collection<Vector> data = new ArrayList<Vector>();
		VectorFactory<?> vectorFactory = VectorFactory.getDefault();
		System.out.println("---> matrix " + matrix.size());
		HashMap<Integer, double[]> index = new HashMap<Integer, double[]>();
		int i = 1;
		for (double[] arr : matrix) {
			data.add(vectorFactory.copyArray(arr));
			index.put(i, arr);
			i++;
		}

		ArrayList<ArrayList<double[]>> centroidIndex = new ArrayList<ArrayList<double[]>>();
		int numRequestedClusters = 3; // The "k" in k-means.

		final Random random = new Random();
		if (centroidIndex.size() == 0) {
			for (int x = 0; x < numRequestedClusters; x++) {
				centroidIndex.add(new ArrayList<double[]>());
			}
		}
		Semimetric<Vectorizable> metric = EuclideanDistanceSquaredMetric.INSTANCE;
		int maxIterations = 10000;
		ClusterCreator<CentroidCluster<Vector>, Vector> creator = VectorMeanCentroidClusterCreator.INSTANCE;
		DistanceSamplingClusterInitializer<CentroidCluster<Vector>, Vector> initializer = new DistanceSamplingClusterInitializer<CentroidCluster<Vector>, Vector>(metric, creator, random);
		ClusterDivergenceFunction<CentroidCluster<Vector>, Vector> clusterDivergence = new CentroidClusterDivergenceFunction<Vector>(metric);
		KMeansClusterer<Vector, CentroidCluster<Vector>> kMeans = new KMeansClusterer<Vector, CentroidCluster<Vector>>(numRequestedClusters, maxIterations, initializer, clusterDivergence, creator);

		// Now run the clustering to create the clusters.
		Collection<CentroidCluster<Vector>> clusters = kMeans.learn(data);
		// printClusters("Version 3: ", clusters, false);
		printClusters("Version 3: ", clusters, true);
		collectAllCentroids(centroidIndex, clusters);

		for (int xx = 0; xx < centroidIndex.size() - 1; xx++) {
			System.out.println(centroidIndex.get(xx).size());
			System.out.println(centroidIndex.get(xx + 1).size());
			if (centroidIndex.get(xx).size() != centroidIndex.get(xx + 1).size()) {
				throw new Exception("wrong number...");
			}
		}
		assignMembers("Version 3a: ", clusters, index);
	}

	private static void resetEventMap() {
		eventMap = new HashMap<String, Integer>();
		eventMap.put("playingVideo", 0);
		eventMap.put("backToProduct", 0);
		eventMap.put("outgoingLink", 0);
		eventMap.put("discount_off", 0);
		eventMap.put("favorite_true", 0);
		eventMap.put("pausedVideo", 0);
		eventMap.put("discount_on", 0);
		eventMap.put("endedVideo", 0);
		eventMap.put("backToOverviewButton", 0);
		eventMap.put("open", 0);
		eventMap.put("home", 0);

	}

	/**
	 * Prints out the cluster centers.
	 *
	 * @param title
	 *            The title to print.
	 * @param clusters
	 *            The cluster centers.
	 */
	public static void printClusters(final String title, final Collection<CentroidCluster<Vector>> clusters, boolean round) {
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
				System.out.println("    " + index + " (" + cluster.getMembers().size() + ") : " + cluster.getCentroid());
			}
			index++;
			// Another useful method on a cluster is: cluster.getMembers()
		}
	}

	public static void collectAllCentroids(ArrayList<ArrayList<double[]>> centroidIndex, final Collection<CentroidCluster<Vector>> clusters) throws Exception {
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
			System.out.println("-> " + key);
		}
	}

	public static void assignMembers(final String title, final Collection<CentroidCluster<Vector>> clusters, HashMap<Integer, double[]> profilePoints) {
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

	public static JSONArray shuffleJsonArray(JSONArray array) throws JSONException {
		// Implementing Fisher–Yates shuffle
		Random rnd = new Random();
		for (int i = array.size() - 1; i >= 0; i--) {
			int j = rnd.nextInt(i + 1);
			// Simple swap
			Object object = array.get(j);
			array.add(j, array.get(i));
			array.add(i, object);
		}
		return array;
	}
}
