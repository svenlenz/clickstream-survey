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

	public static void main(String[] args) throws Exception {


		JSONParser parser = new JSONParser();

		for (int i = 1; i <= 100; i++) {

			try {
				// i = 61;
				Object obj = parser.parse(new FileReader(
				// "/Users/sle/switchdrive/Master/survey_results/clickers/" + i
				// + "/events.json"));
//						"/Users/sle/switchdrive/Master/survey_results/" + i + "/events.json"));
				 "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+i+"\\events.json"));

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
					} else {
						clusteringEventLogDetailed = clusteringEventLogDetailed.replaceAll("TBD", durationSinceLastEvent.toString());
					}
					durationSinceLastEvent = duration;

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
					
					//low level
//					 clusteringEventLogDetailed += eventId + "(" + durationInSeconds + ")";
					 clusteringEventLogDetailed += eventId + "(TBD)";
//					 
					 //medium level
//					 clusteringEventLogDetailed += eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId: "") + (!"".equals(detail) ? "Detail" + detail: "") + "(" + durationInSeconds + ")";
//					 clusteringEventLogDetailed += eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId: "") + (!"".equals(detail) ? "Detail" + detail: "") + "(" + duration + ")";

					 //high level
//					clusteringEventLogDetailed += eventId + productId + detail + linkId + "("+ durationInSeconds + ")";
//					clusteringEventLogDetailed += eventId + productId + detail + linkId + "("+ duration + ")";
					 

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
				clusteringEventLogDetailed = clusteringEventLogDetailed.replaceAll("TBD", durationSinceLastEvent.toString());
				
				if (!lastEventMachted) {
					clusteringEventLogCounter += lastEvent + "(" + counter + ")";
				}

				String categories = clusteringEventLogDetailed.replaceAll("browserBackToProduct", "back").replaceAll("browserBackToOverview", "back").replaceAll("backToProduct", "back").replaceAll("backButton", "back").replaceAll("backToOverview", "back").replaceAll("playingVideo", "video").replaceAll("pausedVideo", "video").replaceAll("endedVideo", "video").replaceAll("favorite_true", "favorite").replaceAll("favorite_false", "favorite").replaceAll("discount_on", "discount")
						.replaceAll("discount_off", "discount");

				clusteringEventLogCounter = clusteringEventLogCounter.replaceAll("browserBackToProduct", "back").replaceAll("browserBackToOverview", "back").replaceAll("backToProduct", "back").replaceAll("backButton", "back").replaceAll("backToOverview", "back").replaceAll("playingVideo", "video").replaceAll("pausedVideo", "video").replaceAll("endedVideo", "video").replaceAll("favorite_true", "favorite").replaceAll("favorite_false", "favorite").replaceAll("discount_on", "discount")
						.replaceAll("discount_off", "discount");

				// System.out.println(clusteringEventLogDetailed);
				 System.out.println(categories);
				// System.out.println(clusteringEventLogCondensed);
//				System.out.println(clusteringEventLogCounter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
