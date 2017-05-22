package processing.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StatDump {

	public static boolean USE_WINDOWS = true;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";

	static int avgNeuro = 5;
	static int avgExo = 5;
	static int avgGewissen = 5;
	static int avgOffen = 5;
	static int avgVertrag = 5;

	public static void main(String[] args) {
		HashMap<String, List<Integer>> profileMap = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterOneFiveClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterTwoFiveClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterThreeFiveClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterFourFiveClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterFiveFiveClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterlessFiveClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> doubleMatchFiveClusterSolution = new HashMap<String, List<Integer>>();

		HashMap<String, List<Integer>> clusterOneThreeClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterTwoThreeClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterThreeThreeClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> clusterlessThreeClusterSolution = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> doubleMatchThreeClusterSolution = new HashMap<String, List<Integer>>();
		
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

				int high = 6;
				int low = 4;
				String profile = "";
				if (b5result.meanNeuro > high) {
					profile += "H";
				} else if (b5result.meanNeuro < low) {
					profile += "L";
				} else {
					profile += "A";
				}

				if (b5result.meanExtra > high) {
					profile += "H";
				} else if (b5result.meanExtra < low) {
					profile += "L";
				} else {
					profile += "A";
				}

				if (b5result.meanGewissen > high) {
					profile += "H";
				} else if (b5result.meanGewissen < low) {
					profile += "L";
				} else {
					profile += "A";
				}

				if (b5result.meanOffen > 5) {
					profile += "H";
				} else if (b5result.meanOffen < 3) {
					profile += "L";
				} else {
					profile += "A";
				}

				if (b5result.meanVertrag > 5) {
					profile += "H";
				} else if (b5result.meanVertrag < 3) {
					profile += "L";
				} else {
					profile += "A";
				}

				if (profileMap.containsKey(profile)) {
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
		 boolean removeDuplicates = false;
		profileMap.entrySet().forEach(entry -> {
			System.out.println("profile: " + entry.getKey() + ", size: " + entry.getValue().size());
			// NEURO; EXTRA; GEWISSEN; OFFEN; VERTRAG

			// 5 cluster solution
			/*
			 * The first cluster (N¼276) was characterized by its low scores on
			 * Neuroticism and high scores on Extraversion, Agreeableness
			 * (vertrag), and Conscientiousness (gewissen) and moderately
			 * positive scores on Openness to Experience (offen).
			 */
			HashMap<String, List<Integer>> lastFiveClusterSolution = null;
			boolean hasClusterFiveClusterSolution = false;
//			if (entry.getKey().matches("^(L|A)(A|H)(A|H)(A|H)(A|H)$")) {
			if (entry.getKey().matches("^.HL..$")) {			
				clusterOneFiveClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterFiveClusterSolution = true;
				lastFiveClusterSolution = clusterOneFiveClusterSolution;
			}

			/*
			 * had pronounced scores on Neuroticism, low scores on Extraversion
			 * and medium to low scores on Openness, Agreeableness, and
			 * Conscientiousness,
			 */
//			if (entry.getKey().matches("^(H|A)(A|L)(A|L)(A|L)(A|L)$")) {
			if (entry.getKey().matches("^(H|A)(H|A)(H|A)(H|A)L$")) {			
				if(hasClusterFiveClusterSolution) {
					if(removeDuplicates)
						lastFiveClusterSolution.remove(entry.getKey());
					doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
				} else {
					clusterTwoFiveClusterSolution.put(entry.getKey(), entry.getValue());
					hasClusterFiveClusterSolution = true;
					lastFiveClusterSolution = clusterTwoFiveClusterSolution;
				}			
			}

			/*
			 * high scores on Neuroticism, moderate scores on Extraversion and
			 * Openness, and low scores on Agreeableness and Conscientiousness.
			 */
//			if (entry.getKey().matches("^(H|A)(A|H)(A|L)(A|H)(A|L)$")) {
			if (entry.getKey().matches("^(L|A)(H|A)(H|A)(H|A)(H|A)$")) {				
				if(hasClusterFiveClusterSolution) {
					if(removeDuplicates)
						lastFiveClusterSolution.remove(entry.getKey());
					doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
				} else {
					clusterThreeFiveClusterSolution.put(entry.getKey(), entry.getValue());
					hasClusterFiveClusterSolution = true;
					lastFiveClusterSolution = clusterThreeFiveClusterSolution;
				}					
			}

			/*
			 * had medium scores on Neuroticism, Agreeableness, and
			 * Conscientiousness and moderately high scores on Extraversion and
			 * Openness. Finally
			 */
//			if (entry.getKey().matches("^(A)(A|H)(A)(A|H)(A)$")) {
			if (entry.getKey().matches("^(H|A)(L|A)(H|A)(L|A)(L|A)$")) {	
				if(hasClusterFiveClusterSolution) {
					if(removeDuplicates)
						lastFiveClusterSolution.remove(entry.getKey());
					doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
				} else {
					clusterFourFiveClusterSolution.put(entry.getKey(), entry.getValue());
					hasClusterFiveClusterSolution = true;
					lastFiveClusterSolution = clusterFourFiveClusterSolution;
				}	
			}

			/*
			 * have low scores on Neuroticism, Extraversion, and Openness, and
			 * moderately positive scores on Agreeableness and
			 * Conscientiousness.
			 */
//			if (entry.getKey().matches("^(L|A)(A|L)(A|H)(A|L)(A|H)$")) {
			if (entry.getKey().matches("^(L|A)(A|L)(A|L)(A|L)(A|H)$")) {
				if(hasClusterFiveClusterSolution) {
					if(removeDuplicates)
						lastFiveClusterSolution.remove(entry.getKey());
					doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
				} else {
					clusterFiveFiveClusterSolution.put(entry.getKey(), entry.getValue());
					hasClusterFiveClusterSolution = true;
					lastFiveClusterSolution = clusterFiveFiveClusterSolution;
				}	
			}

			if (!hasClusterFiveClusterSolution) {
				System.out.println("--> no cluster");
				clusterlessFiveClusterSolution.put(entry.getKey(), entry.getValue());
			}

			// 3 cluster solution

			boolean hasClusterThreeClusterSolution = false;
			HashMap<String, List<Integer>> lastThreeClusterSolution = null;

			/*
			 * resilient personality is indicated by scores
				above the average for all the Big-Five dimensions, except for neuroticism - a
				dimension in which resilient people obtain low scores. 
			 */
			if (entry.getKey().matches("^(L|A)(A|H)(A|H)(A|H)(A|H)$")) {
				clusterOneThreeClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterThreeClusterSolution = true;
				lastThreeClusterSolution = clusterOneThreeClusterSolution;
			}

			/*
				overcontrolled personality typically involves below average scores for extraversion
				combined with above average ones for the traits of neuroticism and
				conscientiousness, the other two trait dimensions being less relevant for describing
				this type. 
			 */
			if (entry.getKey().matches("^(H|A)(A|L)(A|H)..$")) {
				if(hasClusterThreeClusterSolution) {
					if(removeDuplicates)
						lastThreeClusterSolution.remove(entry.getKey());
					doubleMatchThreeClusterSolution.put(entry.getKey(), entry.getValue());
				} else {
					clusterTwoThreeClusterSolution.put(entry.getKey(), entry.getValue());
					hasClusterThreeClusterSolution = true;
					lastThreeClusterSolution = clusterTwoThreeClusterSolution;
				}					
			}

			/*
				 undercontrolled personality type is usually
				characterized by low agreeableness and conscientiousness in combination with high
				levels of neuroticism and extraversion. 
			 */
			if (entry.getKey().matches("^(H|A)(A|H)(A|L).(A|L)$")) {
				if(hasClusterThreeClusterSolution) {
					if(removeDuplicates)
						lastThreeClusterSolution.remove(entry.getKey());
					doubleMatchThreeClusterSolution.put(entry.getKey(), entry.getValue());
				} else {
					clusterThreeThreeClusterSolution.put(entry.getKey(), entry.getValue());
					hasClusterThreeClusterSolution = true;
					lastThreeClusterSolution = clusterThreeThreeClusterSolution;
				}	
			}
			
			if (!hasClusterThreeClusterSolution) {
				System.out.println("--> no cluster");
				clusterlessThreeClusterSolution.put(entry.getKey(), entry.getValue());
			}		
		});
		System.out.println("-------------- 5 cluster -------------------");

		System.out.println("-> cluster 1 " + clusterOneFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterOneFiveClusterSolution));
		System.out.println("-> cluster 2 " + clusterTwoFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterTwoFiveClusterSolution));
		System.out.println("-> cluster 3 " + clusterThreeFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterThreeFiveClusterSolution));
		System.out.println("-> cluster 4 " + clusterFourFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterFourFiveClusterSolution));
		System.out.println("-> cluster 5 " + clusterFiveFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterFiveFiveClusterSolution));
		System.out.println("-> clusterless " + clusterlessFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterlessFiveClusterSolution));
		System.out.println("-> doublematch " + doubleMatchFiveClusterSolution.entrySet().size() + " "
				+ summerizeClusters(doubleMatchFiveClusterSolution));		
		
		System.out.println("-------------- 3 cluster -------------------");
		System.out.println("-> cluster 1 " + clusterOneThreeClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterOneThreeClusterSolution));
		System.out.println("-> cluster 2 " + clusterTwoThreeClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterTwoThreeClusterSolution));
		System.out.println("-> cluster 3 " + clusterThreeThreeClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterThreeThreeClusterSolution));
		System.out.println("-> clusterless " + clusterlessThreeClusterSolution.entrySet().size() + " "
				+ summerizeClusters(clusterlessThreeClusterSolution));		
		System.out.println("-> doublematch " + doubleMatchThreeClusterSolution.entrySet().size() + " "
				+ summerizeClusters(doubleMatchThreeClusterSolution));				

		// TODO: three cluster solution -> match H****, ...
		// TODO: four cluster solution -> match H****, ...
		// TODO: five cluster solution -> match H****, ...
	}

	public static int summerizeClusters(HashMap<String, List<Integer>> cluster) {
		int counter = 0;
		Iterator clusterIterator = cluster.entrySet().iterator();
		while (clusterIterator.hasNext()) {
			Entry<String, List<Integer>> entry = (Entry<String, List<Integer>>) clusterIterator.next();
			counter += entry.getValue().size();
		}
		return counter;
	}
}
