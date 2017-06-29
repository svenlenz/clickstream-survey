package processing.utils;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Create different clickstreams out of the events. For each user event file a new line is created. 
 * Parametrization: 
 * 		Version: 0 = high level concepts, 1 = medium level concepts, 0 = low level concepts
 * 		TimeMode: 0 = milliseconds, 1 = seconds, 2 = normalized values based on fibonacci
 * 		WithID: true: each line starts with the id of the file and a tabstopp, otherwhise directly with the clickstream
 * 
 *  
 * @author sven.lenz@msc.htwchur.ch
 */
public class ClickstreamConverter {
	
	static int maxDoc = 126;
	static int version = 2; //0 = high, 1=medium, 2=low
	static int timeMode = 1; //0 = millis, 1=seconds, 2=normalized
	static boolean withId = true;
	
	public static boolean USE_WINDOWS = true;
	public static String BASE_PATH_WINDOWS = "..\\results\\survey_results\\";
	public static String BASE_PATH_IOS = "../results/survey_results/";

	public static void main(String[] args) throws Exception {


		JSONParser parser = new JSONParser();

		for (int id = 1; id <= maxDoc; id++) {

			try {
				String evt = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
						+ "events.json";
				
				Object obj = parser.parse(new FileReader(evt));

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
						if(withId) {
							clusteringEventLogDetailed = id + "\t";
							clusteringEventLogCondensed = id + "\t";
							clusteringEventLogCounter = id + "\t";							
						}
						first = false;
					} else {
						Long time = 0L;
						time = getTime(durationSinceLastEvent, time);
						
						clusteringEventLogDetailed = clusteringEventLogDetailed.replaceAll("TBD", time.toString());
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
					int durationInSeconds = (int) (durationSinceLastEvent / 1000);
					if (durationInSeconds == 0)
						durationInSeconds = 1;

					if (duration == 0)
						duration = 1L;

					// LAST EVENT MEMORYSATION //
					if (lastEvent == null || lastEvent.equals(eventId)) {
						counter+=durationInSeconds;
					} else {
						clusteringEventLogCounter += lastEvent + "(" + counter + ")";
						counter = 1;
						lastEventMachted = true;
					}
					lastEvent = eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId : "");

	
					// add browserBack Events
					if (hasBrowserBackToProduct) {
						clusteringEventLogDetailed += "browserBackToProduct" + "(" + 1 + ")";
					}

					if (hasBrowserBackToOverview) {
						clusteringEventLogDetailed += "browserBackToOverview" + "(" + 1 + ")";
					}
					
					switch(version) {
						case 0:
//							clusteringEventLogDetailed += eventId + "(TBD)";	
							clusteringEventLogDetailed += (!eventId.contains("open") ? eventId : "")  + (("".equals(detail) && "".equals(linkId)) ? "Product" : "") + (!"".equals(detail) ? "Detail" : "") + "(TBD)";
							break;
						case 1: 
//							clusteringEventLogDetailed += eventId + (!eventId.contains("open") ? eventId : "")  + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId: "") + (!"".equals(detail) ? "Detail" + detail: "") + "(TBD)";
							clusteringEventLogDetailed += eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId: "") + (!"".equals(detail) ? "Detail" + detail: "") + "(TBD)";

							break;
						case 2:
							clusteringEventLogDetailed += eventId + productId + detail + linkId + "(TBD)";
							break;
						default:
							System.out.println("???" + version);
					}
					 

					clusteringEventLogCondensed += eventId.charAt(0) + productId + "(" + normalizedDurationSinceLastEvent + ")";
					if (0 == durationSinceLastEvent) {
						durationSinceLastEvent = 1L;
						normalizedDurationSinceLastEvent = 1;
					} else {
						normalizedDurationSinceLastEvent = normalize(durationSinceLastEvent);
					}

				}
				Long time = 0L;
				time = getTime(durationSinceLastEvent, time);
				clusteringEventLogDetailed = clusteringEventLogDetailed.replaceAll("TBD", time.toString());
				
				if (!lastEventMachted) {
					clusteringEventLogCounter += lastEvent + "(" + counter + ")";
				}

				String categories = clusteringEventLogDetailed.replaceAll("browserBackToProduct", "back").replaceAll("browserBackToOverview", "back").replaceAll("backToProduct", "back").replaceAll("backButton", "back").replaceAll("backToOverview", "back").replaceAll("playingVideo", "video").replaceAll("pausedVideo", "video").replaceAll("endedVideo", "video").replaceAll("favorite_true", "favorite").replaceAll("favorite_false", "favorite").replaceAll("discount_on", "discount")
						.replaceAll("discount_off", "discount");

				clusteringEventLogCounter = clusteringEventLogCounter.replaceAll("browserBackToProduct", "back").replaceAll("browserBackToOverview", "back").replaceAll("backToProduct", "back").replaceAll("backButton", "back").replaceAll("backToOverview", "back").replaceAll("playingVideo", "video").replaceAll("pausedVideo", "video").replaceAll("endedVideo", "video").replaceAll("favorite_true", "favorite").replaceAll("favorite_false", "favorite").replaceAll("discount_on", "discount")
						.replaceAll("discount_off", "discount");

				if(version == 2)
					System.out.println(clusteringEventLogDetailed);
				else
					System.out.println(categories);
				// System.out.println(clusteringEventLogCondensed);
//				System.out.println(clusteringEventLogCounter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Long getTime(Long durationSinceLastEvent, Long time) {
		switch(timeMode) {
			case 0: 
				time = durationSinceLastEvent;
				break;
			case 1:
				time = durationSinceLastEvent/1000;
				break;
			case 2:
				time = new Long(normalize(durationSinceLastEvent));
				break;
				
		}
		if(time == 0)
			time = 1L;
		return time;
	}

	private static int normalize(Long durationSinceLastEvent) {

		int normalizedDurationSinceLastEvent = 0;
		if (durationSinceLastEvent <= 1000) {
			normalizedDurationSinceLastEvent = 1;
		}

		if (durationSinceLastEvent > 1000 && durationSinceLastEvent <= 2000) {
			normalizedDurationSinceLastEvent = 2;
		}

		if (durationSinceLastEvent > 2000 && durationSinceLastEvent <= 3000) {
			normalizedDurationSinceLastEvent = 3;
		}

		if (durationSinceLastEvent > 3000 && durationSinceLastEvent <= 5000) {
			normalizedDurationSinceLastEvent = 5;
		}

		if (durationSinceLastEvent > 5000 && durationSinceLastEvent <= 8000) {
			normalizedDurationSinceLastEvent = 8;
		}

		if (durationSinceLastEvent > 8000 && durationSinceLastEvent <= 13000) {
			normalizedDurationSinceLastEvent = 13;
		}

		if (durationSinceLastEvent > 13000 && durationSinceLastEvent <= 20000) {
			normalizedDurationSinceLastEvent = 21;
		}

		if (durationSinceLastEvent > 20000 && durationSinceLastEvent <= 30000) {
			normalizedDurationSinceLastEvent = 34;
		}

		if (durationSinceLastEvent > 30000) {
			normalizedDurationSinceLastEvent = 55;
		}

		if (durationSinceLastEvent > 60000) {
			normalizedDurationSinceLastEvent = 185;

		}
		return normalizedDurationSinceLastEvent;
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
