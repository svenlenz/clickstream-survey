package processing.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StatDump {

	public static boolean USE_WINDOWS = false;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";
	
	static int avgNeuro = 5;
	static int avgExo = 5;
	static int avgGewissen = 5;
	static int avgOffen = 5;
	static int avgVertrag = 5;

	public static void main(String[] args) {
		HashMap<String, List<Integer>> profileMap = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterOne = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterTwo = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterThree = new HashMap<String, List<Integer>>();

		JSONParser parser = new JSONParser();
		System.out.println(Big5Result.csvHeader());
		for (int id = 1; id <= 92; id++) {
			String survey = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
					+ "survey.json";
			String events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
					+ "events.json";

			Big5Result b5result = new Big5Result();

			try {
				Object obj = parser.parse(new FileReader(survey));
				JSONObject object = (JSONObject) obj;
				JSONObject answers = (JSONObject) object.get("big5_result");
				JSONObject detailed = (JSONObject) answers.get("detailed");
				b5result.meanNeuro = Integer.valueOf((String) detailed.get("neuro"));
				b5result.meanExtra = Integer.valueOf((String) detailed.get("extra"));
				b5result.meanGewissen = Integer.valueOf((String) detailed.get("gewissen"));
				b5result.meanOffen = Integer.valueOf((String) detailed.get("offen"));
				b5result.meanVertrag = Integer.valueOf((String) detailed.get("vertrag"));
				b5result.anerkennung = Integer.valueOf((String) detailed.get("anerkennung"));
				b5result.macht = Integer.valueOf((String) detailed.get("macht"));
				b5result.sicher = Integer.valueOf((String) detailed.get("sicher"));
				b5result.ehrlich = Integer.valueOf((String) detailed.get("ehrlich"));
				JSONObject metric = (JSONObject) answers.get("metric");
				b5result.highest = (String) metric.get("highest");
				b5result.lowest = (String) metric.get("lowest");
				b5result.meanAge = Integer.valueOf((String) object.get("age"));
				b5result.meanTechnique = Integer.valueOf((String) object.get("technique"));
				b5result.gender = (String) object.get("gender");
					

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
				
				String profile = "";
				if(b5result.meanNeuro > 6) {
					profile += "H";
				} else if(b5result.meanNeuro < 4) {
					profile += "L";
				} else {
					profile += "A";
				}
				
				if(b5result.meanExtra > 6) {
					profile += "H";
				} else if(b5result.meanExtra < 4) {
					profile += "L";
				} else {
					profile += "A";
				}
				
				if(b5result.meanGewissen > 6) {
					profile += "H";
				} else if(b5result.meanGewissen < 4) {
					profile += "L";
				} else {
					profile += "A";
				}
				
				if(b5result.meanOffen > 6) {
					profile += "H";
				} else if(b5result.meanOffen < 4) {
					profile += "L";
				} else {
					profile += "A";
				}
				
				if(b5result.meanVertrag > 6) {
					profile += "H";
				} else if(b5result.meanVertrag < 4) {
					profile += "L";
				} else {
					profile += "A";
				}
				
				if(profileMap.containsKey(profile)) {
					profileMap.get(profile).add(id);
				} else {
					ArrayList<Integer> idList = new ArrayList<Integer>();
					idList.add(id);
					profileMap.put(profile, idList);
				}
				
					
				
				System.out.println(b5result.toCSV());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		System.out.println("---------------------------------");
		System.out.println("number of profiles: " + profileMap.size());
		profileMap.entrySet().forEach(entry -> {
			System.out.println("profile: " + entry.getKey() + ", size: " + entry.getValue().size());
			
			if(entry.getKey().matches("^H....$")) {
				System.out.println("---> high");
			}
			
		});
		
		//TODO: three cluster solution -> match H****, ...
		//TODO: four cluster solution -> match H****, ...
		//TODO: five cluster solution -> match H****, ...		
	}
}
