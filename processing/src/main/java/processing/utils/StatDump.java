package processing.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class StatDump {

	public static boolean USE_WINDOWS = true;
	public static boolean WITH_LIERS = true;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";

	static int avgNeuro = 5;
	static int avgExo = 5;
	static int avgGewissen = 5;
	static int avgOffen = 5;
	static int avgVertrag = 5;
	
	// NEURO; EXTRA; GEWISSEN; OFFEN; VERTRAG

	
	/*
	 * resilient personality is indicated by scores above the average
	 * for all the Big-Five dimensions, except for neuroticism - a
	 * dimension in which resilient people obtain low scores.
	 * 
	 * overcontrolled personality typically involves below average
	 * scores for extraversion combined with above average ones for the
	 * traits of neuroticism and conscientiousness, the other two trait
	 * dimensions being less relevant for describing this type.
	 * 
	 * undercontrolled personality type is usually characterized by low
	 * agreeableness and conscientiousness in combination with high
	 * levels of neuroticism and extraversion.
	 */
	public static List<Pattern> romanianCommonThreeClusterPatterns = new ArrayList<Pattern>(Arrays.asList(
			Pattern.compile("^(L|A)(A|H)(A|H)(A|L)(A|H)$"),
			Pattern.compile("^(H|A)(A|L)(A|H)(.)(.)$"),
			Pattern.compile("^(H|A)(A|H)(A|L)(.)(A|L)$")
			));
	
	public static List<Pattern> asendorpfThreeClusterPatterns = new ArrayList<Pattern>(Arrays.asList(
			Pattern.compile("^(L|A)(A|H)(A|H)(A|L)(A|H)$"),
			Pattern.compile("^(H|A)(L)(A|L)(A|H)(A|L)$"),
			Pattern.compile("^(L|A)(A|H)(L)(A|H)(A|L)$")
			));

	
	public static List<Pattern> romanianFiveClusterPatterns = new ArrayList<Pattern>(Arrays.asList(
			Pattern.compile("^(.)(H)(L)(.)(A|L)$"),
			Pattern.compile("^(H|A)(H|A)(H|A)(H|A)(L)$"),
			Pattern.compile("^(L|A)(H|A)(H|A)(H|A)(H|A)$"),
			Pattern.compile("^(H|A)(L|A)(H|A)(L|A)(L|A)$"),
			Pattern.compile("^(L|A)(A|L)(A|L)(A|L)(A|H)$")
			));
	
	
	
	// 5 cluster solution
	/*
	 * The first cluster (N¼276) was characterized by its low scores on
	 * Neuroticism and high scores on Extraversion, Agreeableness
	 * (vertrag), and Conscientiousness (gewissen) and moderately
	 * positive scores on Openness to Experience (offen).
	 * 
	 * had pronounced scores on Neuroticism, low scores on Extraversion
	 * and medium to low scores on Openness, Agreeableness, and
	 * Conscientiousness,
	 * 
	 * high scores on Neuroticism, moderate scores on Extraversion and
	 * Openness, and low scores on Agreeableness and Conscientiousness.
	 * 
	 * had medium scores on Neuroticism, Agreeableness, and
	 * Conscientiousness and moderately high scores on Extraversion and
	 * Openness. Finally
	 * 
	 * have low scores on Neuroticism, Extraversion, and Openness, and
	 * moderately positive scores on Agreeableness and
	 * Conscientiousness.
	 */	
	public static List<Pattern> germanFiveClusterPatterns = new ArrayList<Pattern>(Arrays.asList(
			Pattern.compile("^(L|A)(A|H)(A|H)(A|H)(A|H)$"),
			Pattern.compile("^(H|A)(A|L)(A|L)(A|L)(A|L)$"),
			Pattern.compile("^(H|A)(A|H)(A|L)(A|H)(A|L)$"),
			Pattern.compile("^(A)(A|H)(A)(A|H)(A)$"),
			Pattern.compile("^(L|A)(A|L)(A|L)(A|L)(A|H)$")
			));
	
	public static List<int[]> asendorpfThreeClusterPoints = new ArrayList<int[]>(Arrays.asList(
	new int[]{3,6,7,5,5},
	new int[]{7,2,5,5,4},
	new int[]{4,6,2,6,4}));	
	
	// NEURO; EXTRA; CON/GEWISSEN; OPENNESS/OFFEN; AGREE/VERTRAG
	public static List<int[]> germanFiveClusterPoints = new ArrayList<int[]>(Arrays.asList(
			new int[]{3,6,6,5,6},
			new int[]{7,3,4,4,4},
			new int[]{7,5,3,5,3},
			new int[]{5,6,5,6,5},
			new int[]{3,3,6,3,6}));
	
	// NEURO; EXTRA; CON/GEWISSEN; OPENNESS/OFFEN; AGREE/VERTRAG
	//SAVA POPPA
//	public static List<int[]> germanFiveClusterPoints = new ArrayList<int[]>(Arrays.asList(
//			new int[]{5,8,3,5,4},
//			new int[]{6,6,7,6,3},
//			new int[]{3,6,6,7,6},
//			new int[]{6,4,6,4,4},
//			new int[]{3,3,3,3,6}));
	
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
		
		
		List<Integer> clusterOne = new ArrayList<Integer>();
		List<Integer> clusterTwo = new ArrayList<Integer>();
		List<Integer> clusterThree = new ArrayList<Integer>();
		List<Integer> clusterFour = new ArrayList<Integer>();
		List<Integer> clusterFive = new ArrayList<Integer>();

		JSONParser parser = new JSONParser();
		System.out.println(Big5Result.csvHeader());
		for (int id = 1; id <= 101; id++) {
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

				if(b5result.ehrlich <= 3 && !WITH_LIERS) {
					System.out.println("LIER!");
					continue;
				}
				
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
				
						
				int[] profilePoints = {b5result.meanNeuro, b5result.meanExtra, b5result.meanGewissen, b5result.meanOffen, b5result.meanVertrag};
				float lastndistance = 0;
				int match = -1;
				for(int i = 0; i < germanFiveClusterPoints.size(); i++) {
					int[] cluster = germanFiveClusterPoints.get(i);
					float ndistance = ndistance(profilePoints, cluster);
					if(ndistance < lastndistance) {						
						lastndistance = ndistance;
						match = i;
					} else if(lastndistance == 0) {
						lastndistance = ndistance;
						match = i;
					}
				}				
				if(match == 0) {
					System.out.println("-> one");
					clusterOne.add(id);
				} else if(match == 1) {
					System.out.println("-> two");
					clusterTwo.add(id);
				} else if(match == 2) {
					System.out.println("-> three");
					clusterThree.add(id);
				} else if(match == 3) {
					System.out.println("-> four");
					clusterFour.add(id);
				} else if(match == 4) {
					System.out.println("-> five");
					clusterFive.add(id);
				}

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
		System.out.println("euclidiean distance");
		System.out.println(clusterOne);
		System.out.println(clusterTwo);
		System.out.println(clusterThree);
		System.out.println(clusterFour);
		System.out.println(clusterFive);
		System.out.println(Big5Result.csvHeader());
		ClusterEvaluator.meanResultValues(clusterOne);		
		ClusterEvaluator.meanResultValues(clusterTwo);
		ClusterEvaluator.meanResultValues(clusterThree);
		ClusterEvaluator.meanResultValues(clusterFour);
		ClusterEvaluator.meanResultValues(clusterFive);

		System.out.println("---------------------------------");
		System.out.println("number of profiles: " + profileMap.size());
		boolean removeDuplicates = false;
		profileMap.entrySet().forEach(entry -> {
			System.out.println("profile: " + entry.getKey() + ", size: " + entry.getValue().size());
			fiveClusterSolution(romanianFiveClusterPatterns, clusterOneFiveClusterSolution, clusterTwoFiveClusterSolution,
					clusterThreeFiveClusterSolution, clusterFourFiveClusterSolution, clusterFiveFiveClusterSolution,
					clusterlessFiveClusterSolution, doubleMatchFiveClusterSolution, removeDuplicates, entry);

			threeClusterSolution(romanianCommonThreeClusterPatterns, clusterOneThreeClusterSolution, clusterTwoThreeClusterSolution,
					clusterThreeThreeClusterSolution, clusterlessThreeClusterSolution, doubleMatchThreeClusterSolution,
					removeDuplicates, entry);
		});				
		
		printFiveClusterSolution(clusterOneFiveClusterSolution, clusterTwoFiveClusterSolution,
				clusterThreeFiveClusterSolution, clusterFourFiveClusterSolution, clusterFiveFiveClusterSolution,
				clusterlessFiveClusterSolution, doubleMatchFiveClusterSolution);
		
		System.out.println(Big5Result.csvHeader());
		getMeanResultValues(clusterOneFiveClusterSolution);
		getMeanResultValues(clusterTwoFiveClusterSolution);
		getMeanResultValues(clusterThreeFiveClusterSolution);
		getMeanResultValues(clusterFourFiveClusterSolution);
		getMeanResultValues(clusterFiveFiveClusterSolution);
		getMeanResultValues(clusterlessFiveClusterSolution);		

		printThreeClusterSolution(clusterOneThreeClusterSolution, clusterTwoThreeClusterSolution,
				clusterThreeThreeClusterSolution, clusterlessThreeClusterSolution, doubleMatchThreeClusterSolution);
		
		System.out.println(Big5Result.csvHeader());
		getMeanResultValues(clusterOneThreeClusterSolution);
		getMeanResultValues(clusterTwoThreeClusterSolution);
		getMeanResultValues(clusterThreeThreeClusterSolution);
		getMeanResultValues(clusterlessThreeClusterSolution);
		
		profileMap.entrySet().forEach(entry -> {
			System.out.println("profile: " + entry.getKey() + ", size: " + entry.getValue().size());
			fiveClusterSolution(germanFiveClusterPatterns, clusterOneFiveClusterSolution, clusterTwoFiveClusterSolution,
					clusterThreeFiveClusterSolution, clusterFourFiveClusterSolution, clusterFiveFiveClusterSolution,
					clusterlessFiveClusterSolution, doubleMatchFiveClusterSolution, removeDuplicates, entry);

		});
		
		printFiveClusterSolution(clusterOneFiveClusterSolution, clusterTwoFiveClusterSolution,
				clusterThreeFiveClusterSolution, clusterFourFiveClusterSolution, clusterFiveFiveClusterSolution,
				clusterlessFiveClusterSolution, doubleMatchFiveClusterSolution);
		
		System.out.println(Big5Result.csvHeader());
		getMeanResultValues(clusterOneFiveClusterSolution);
		getMeanResultValues(clusterTwoFiveClusterSolution);
		getMeanResultValues(clusterThreeFiveClusterSolution);
		getMeanResultValues(clusterFourFiveClusterSolution);
		getMeanResultValues(clusterFiveFiveClusterSolution);
		getMeanResultValues(clusterlessFiveClusterSolution);	
				
	}




	private static void getMeanResultValues(HashMap<String, List<Integer>> clusterOneFiveClusterSolution) {
		List<Integer> clusterIds = new ArrayList<Integer>();
		clusterOneFiveClusterSolution.entrySet().forEach(entry -> {			
			clusterIds.addAll(entry.getValue());
		});
		ClusterEvaluator.meanResultValues(clusterIds);
	}
	
	


	private static void printFiveClusterSolution(HashMap<String, List<Integer>> clusterOneFiveClusterSolution,
			HashMap<String, List<Integer>> clusterTwoFiveClusterSolution,
			HashMap<String, List<Integer>> clusterThreeFiveClusterSolution,
			HashMap<String, List<Integer>> clusterFourFiveClusterSolution,
			HashMap<String, List<Integer>> clusterFiveFiveClusterSolution,
			HashMap<String, List<Integer>> clusterlessFiveClusterSolution,
			HashMap<String, List<Integer>> doubleMatchFiveClusterSolution) {
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
	}

	private static void printThreeClusterSolution(HashMap<String, List<Integer>> clusterOneThreeClusterSolution,
			HashMap<String, List<Integer>> clusterTwoThreeClusterSolution,
			HashMap<String, List<Integer>> clusterThreeThreeClusterSolution,
			HashMap<String, List<Integer>> clusterlessThreeClusterSolution,
			HashMap<String, List<Integer>> doubleMatchThreeClusterSolution) {
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
	}

	private static void threeClusterSolution(
			List<Pattern> clusterPatterns,
			HashMap<String, List<Integer>> clusterOneThreeClusterSolution,
			HashMap<String, List<Integer>> clusterTwoThreeClusterSolution,
			HashMap<String, List<Integer>> clusterThreeThreeClusterSolution,
			HashMap<String, List<Integer>> clusterlessThreeClusterSolution,
			HashMap<String, List<Integer>> doubleMatchThreeClusterSolution, boolean removeDuplicates,
			Entry<String, List<Integer>> entry) {

		boolean hasClusterThreeClusterSolution = false;
		int matchingGroups = 0;
		HashMap<String, List<Integer>> lastThreeClusterSolution = null;
		Matcher m = clusterPatterns.get(0).matcher(entry.getKey());
		if (m.matches()) {
			clusterOneThreeClusterSolution.put(entry.getKey(), entry.getValue());
			hasClusterThreeClusterSolution = true;
			lastThreeClusterSolution = clusterOneThreeClusterSolution;
			matchingGroups = m.groupCount();
		}

		m = clusterPatterns.get(1).matcher(entry.getKey());
		if (m.matches()) {
			if (hasClusterThreeClusterSolution) {
				if (removeDuplicates && matchingGroups < m.groupCount()) {
					lastThreeClusterSolution.remove(entry.getKey());
				}
				doubleMatchThreeClusterSolution.put(entry.getKey(), entry.getValue());					
			} else {
				clusterTwoThreeClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterThreeClusterSolution = true;
				lastThreeClusterSolution = clusterTwoThreeClusterSolution;
				matchingGroups = m.groupCount();
			}
		}

		m = clusterPatterns.get(2).matcher(entry.getKey());
		if (m.matches()) {
			if (hasClusterThreeClusterSolution) {
				if (removeDuplicates && matchingGroups < m.groupCount())
					lastThreeClusterSolution.remove(entry.getKey());
				doubleMatchThreeClusterSolution.put(entry.getKey(), entry.getValue());
			} else {
				clusterThreeThreeClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterThreeClusterSolution = true;
				lastThreeClusterSolution = clusterThreeThreeClusterSolution;
				matchingGroups = m.groupCount();
			}
		}

		if (!hasClusterThreeClusterSolution) {
			clusterlessThreeClusterSolution.put(entry.getKey(), entry.getValue());
		}
	}
	
	private static void fiveClusterSolution(
			List<Pattern> clusterPatterns,
			HashMap<String, List<Integer>> clusterOneFiveClusterSolution,
			HashMap<String, List<Integer>> clusterTwoFiveClusterSolution,
			HashMap<String, List<Integer>> clusterThreeFiveClusterSolution,
			HashMap<String, List<Integer>> clusterFourFiveClusterSolution,
			HashMap<String, List<Integer>> clusterFiveFiveClusterSolution,
			HashMap<String, List<Integer>> clusterlessFiveClusterSolution,
			HashMap<String, List<Integer>> doubleMatchFiveClusterSolution, boolean removeDuplicates,
			Entry<String, List<Integer>> entry) {

		int matchingGroups = 0;		
		HashMap<String, List<Integer>> lastFiveClusterSolution = null;
		Matcher m = clusterPatterns.get(0).matcher(entry.getKey());
		boolean hasClusterFiveClusterSolution = false;
		if (m.matches()) {
			clusterOneFiveClusterSolution.put(entry.getKey(), entry.getValue());
			hasClusterFiveClusterSolution = true;
			lastFiveClusterSolution = clusterOneFiveClusterSolution;
		}

		m = clusterPatterns.get(1).matcher(entry.getKey());
		if (m.matches()) {
			if (hasClusterFiveClusterSolution) {
				if (removeDuplicates && matchingGroups < m.groupCount())
					lastFiveClusterSolution.remove(entry.getKey());
				doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
			} else {
				clusterTwoFiveClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterFiveClusterSolution = true;
				lastFiveClusterSolution = clusterTwoFiveClusterSolution;
				matchingGroups = m.groupCount();
			}
		}

		m = clusterPatterns.get(2).matcher(entry.getKey());
		if (m.matches()) {
			if (hasClusterFiveClusterSolution) {
				if (removeDuplicates && matchingGroups < m.groupCount())
					lastFiveClusterSolution.remove(entry.getKey());
				doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
			} else {
				clusterThreeFiveClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterFiveClusterSolution = true;
				lastFiveClusterSolution = clusterThreeFiveClusterSolution;
				matchingGroups = m.groupCount();
			}
		}

		m = clusterPatterns.get(3).matcher(entry.getKey());
		if (m.matches()) {
			if (hasClusterFiveClusterSolution) {
				if (removeDuplicates && matchingGroups < m.groupCount())
					lastFiveClusterSolution.remove(entry.getKey());
				doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
			} else {
				clusterFourFiveClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterFiveClusterSolution = true;
				lastFiveClusterSolution = clusterFourFiveClusterSolution;
			}
		}

		m = clusterPatterns.get(4).matcher(entry.getKey());
		if (m.matches()) {
			if (hasClusterFiveClusterSolution) {
				if (removeDuplicates && matchingGroups < m.groupCount())
					lastFiveClusterSolution.remove(entry.getKey());
				doubleMatchFiveClusterSolution.put(entry.getKey(), entry.getValue());
			} else {
				clusterFiveFiveClusterSolution.put(entry.getKey(), entry.getValue());
				hasClusterFiveClusterSolution = true;
				lastFiveClusterSolution = clusterFiveFiveClusterSolution;
				matchingGroups = m.groupCount();
			}
		}

		if (!hasClusterFiveClusterSolution) {
			System.out.println("--> no cluster");
			clusterlessFiveClusterSolution.put(entry.getKey(), entry.getValue());
		}
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
	
	public static float ndistance(int[] a, int[] b) {
	    int total = 0, diff;
	    for (int i = 0; i < a.length; i++) {
	        diff = b[i] - a[i];
	        total += diff * diff;
	    }
	    return (float) Math.sqrt(total);
	}
}
