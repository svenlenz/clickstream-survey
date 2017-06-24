package processing.utils;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClusterEvaluator {

	public static boolean CSV_FORMAT = true;
	public static boolean USE_WINDOWS = false;
	public static boolean CLICKERS = false;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";
	public static String CLICKPATH_FILE = "testresult.json";

	static boolean takePLSA = false;
	static List<Integer> cl1 = Arrays.asList(15,16,34,45,47,49,52,54,57,63);
	static List<Integer> cl2 = Arrays.asList(20,23,33,43,44,50,58,59);
	static List<Integer> cl3 = Arrays.asList(1,2,3,7,9,14,17,21,22,24,25,27,29,35,39,40,42,48,51,55,60,62,66);
	static List<Integer> cl4 = Arrays.asList(8,10,11,18,19,26,30,32,36,38,41,46,56,61,64,65);
	static List<Integer> cl5 = Arrays.asList(4,5,6,12,13,28,31,37,53);


	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		try {
			String path = "/Users/sle/Repos/clickstream-survey/clustering/" + CLICKPATH_FILE;
			
			
			Object obj = parser.parse(new FileReader(path));

			JSONArray jsonClusters = (JSONArray) obj;
			Cluster cluster = new Cluster();
			Cluster clusterResult = extractCluster(jsonClusters, "", cluster);
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(clusterResult);
			System.out.println(jsonInString);
			List<List<Integer>> clusterCollection = new ArrayList<List<Integer>>();
			toList(clusterCollection, clusterResult);
			System.out.println("//// CLUSTER SIZE //// " + clusterCollection.size());

			if (takePLSA) {
				clusterCollection.clear();
				clusterCollection.add(cl1);
				clusterCollection.add(cl2);
				clusterCollection.add(cl3);
				clusterCollection.add(cl4);
				clusterCollection.add(cl5);
			}
			
			System.out.println("clusters:");
			clusterCollection.forEach(cl -> {
				Collections.sort( cl );
				System.out.println(cl);
			});

			System.out.println("//// CLUSTER CLOSENESS //// ");
			clusterCollection.forEach(cl -> {
				System.out.println("//// ----------------- //// ");

				List<Integer> clusterOne = new ArrayList<Integer>();
				List<Integer> clusterTwo = new ArrayList<Integer>();
				List<Integer> clusterThree = new ArrayList<Integer>();
				List<Integer> clusterFour = new ArrayList<Integer>();
				List<Integer> clusterFive = new ArrayList<Integer>();
				cl.forEach(id -> {
					Big5Result b5result = StatDump.toBig5Result(id);
					int[] profilePoints = { b5result.meanNeuro, b5result.meanExtra, b5result.meanGewissen,
							b5result.meanOffen, b5result.meanVertrag };
					StatDump.calculateNDistance(id, profilePoints, clusterOne, clusterTwo, clusterThree, clusterFour,
							clusterFive);
				});
				System.out.println("cl all " + cl.size() + " cl1 " + clusterOne.size() + " cl2 " + clusterTwo.size()
						+ " cl3 " + clusterThree.size());
				System.out.println("---> cluster one " + (int) (new Double(100) / cl.size() * clusterOne.size()));
				System.out.println("---> cluster two " + (int) (new Double(100) / cl.size() * clusterTwo.size()));
				System.out.println("---> cluster three " + (int) (new Double(100) / cl.size() * clusterThree.size()));
				System.out.println("---> cluster four " + (int) (new Double(100) / cl.size() * clusterFour.size()));
				System.out.println("---> cluster five " + (int) (new Double(100) / cl.size() * clusterFive.size()));
			});

			List<Integer> clusterZero = new ArrayList<Integer>();

			for (int i = 1; i <= 126; i++) {
				clusterZero.add(i);
			}

			if (CSV_FORMAT) {
				System.out.println(Big5Result.csvHeader());
				meanResultValues(clusterZero);
				clusterCollection.forEach(cl -> {
					meanResultValues(cl);
				});
			} else {
				System.out.println("##### ALL " + clusterZero.size() + " #####");
				meanResultValues(clusterZero);
				System.out.println("---------------------------------");
				clusterCollection.forEach(cl -> {
					System.out.println("##### " + cl.size() + " #####");
					meanResultValues(cl);
					System.out.println("---------------------------------");
				});
			}

			// meanResultValues(clusterOne);
			// System.out.println("------------");
			// meanResultValues(clusterTwo);
			// System.out.println("------------");
			// meanResultValues(clusterThree);
			// System.out.println("------------");
			// meanResultValues(clusterFour);
			//

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void toList(List<List<Integer>> collection, Cluster cluster) {
		boolean flat = false;
		if (flat) {
			for (Leaf f : cluster.getLeafs()) {
				List<Integer> clusterOne = new ArrayList<Integer>();
				clusterOne.addAll(f.getIds());
				collection.add(clusterOne);

			}
		} else {
			List<Integer> clusterOne = new ArrayList<Integer>();
			for (Leaf f : cluster.getLeafs()) {
				clusterOne.addAll(f.getIds());
			}
			collection.add(clusterOne);
		}

		cluster.getClusters().forEach(cl -> {
			toList(collection, cl);
		});
	}

	// t means tree cluster who has child clusters.
	static class Cluster {
		public ArrayList<Leaf> leafs = new ArrayList<Leaf>();
		public ArrayList<Cluster> clusters = new ArrayList<Cluster>();

		public Cluster() {

		}

		public ArrayList<Leaf> getLeafs() {
			return leafs;
		}

		public void setLeafs(ArrayList<Leaf> leafs) {
			this.leafs = leafs;
		}

		public ArrayList<Cluster> getClusters() {
			return clusters;
		}

		public void setClusters(ArrayList<Cluster> clusters) {
			this.clusters = clusters;
		}

	};

	// l means leaf node that cannot be further split.
	static class Leaf {
		public ArrayList<Integer> ids = new ArrayList<Integer>();

		public ArrayList<Integer> getIds() {
			return ids;
		}

		public void setIds(ArrayList<Integer> ids) {
			this.ids = ids;
		}

	}

	public static Cluster extractCluster(JSONArray clusters, String indent, Cluster cluster) {
		Iterator<JSONObject> iterator = clusters.iterator();
		JSONArray children;
		Cluster nextCluster = cluster;
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof String) {
				String s = (String) next;
				if (s.contains("t")) {
					nextCluster = new Cluster();
					cluster.clusters.add(nextCluster);
					System.out.println(indent + "TREE");
					indent += "\t";
				} else if (s.contains("l")) {
					System.out.println(indent + "LIST");
					nextCluster.leafs.add(new Leaf());
					indent += "\t";
				}
			} else if (next instanceof JSONArray) {
				children = (JSONArray) next;
				Cluster ret = extractCluster(children, indent, nextCluster);
			} else if (next instanceof JSONObject) {
				// exclusions etc...
			} else {
				System.out.println(indent + next);
				nextCluster.leafs.get(nextCluster.leafs.size() - 1).ids.add(((Long) next).intValue());
			}

		}
		return nextCluster;
	}

	public static void meanResultValues(List<Integer> clusterIDs) {

		if (clusterIDs.isEmpty()) {
			return;
		}

		JSONParser parser = new JSONParser();
		ArrayList<Integer> neuroList = new ArrayList<Integer>();
		ArrayList<Integer> extraList = new ArrayList<Integer>();
		ArrayList<Integer> gewissenList = new ArrayList<Integer>();
		ArrayList<Integer> offenList = new ArrayList<Integer>();
		ArrayList<Integer> vertragList = new ArrayList<Integer>();
		ArrayList<Integer> anerkennungList = new ArrayList<Integer>();
		ArrayList<Integer> machtList = new ArrayList<Integer>();
		ArrayList<Integer> sicherList = new ArrayList<Integer>();
		ArrayList<Integer> ehrlichList = new ArrayList<Integer>();
		ArrayList<String> highestList = new ArrayList<String>();
		ArrayList<String> lowestList = new ArrayList<String>();

		ArrayList<Integer> ageList = new ArrayList<Integer>();
		ArrayList<Integer> techniqueList = new ArrayList<Integer>();
		ArrayList<Integer> maleList = new ArrayList<Integer>();
		ArrayList<Integer> womenList = new ArrayList<Integer>();

		ArrayList<Integer> numberOfClicksList = new ArrayList<Integer>();
		ArrayList<Integer> durationList = new ArrayList<Integer>();

		clusterIDs.forEach(id -> {
			String survey = "";
			String events = "";
			if (!CLICKERS) {
				survey = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
						+ "survey.json";
				events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
						+ "events.json";
			} else {
				survey = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/clickers/" + id + "/")
						+ "survey.json";
				events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/clickers/" + id + "/")
						+ "events.json";
			}
			try {
				Object obj = parser.parse(new FileReader(survey));
				JSONObject object = (JSONObject) obj;
				JSONObject answers = (JSONObject) object.get("big5_result");
				JSONObject detailed = (JSONObject) answers.get("detailed");
				neuroList.add(Integer.valueOf((String) detailed.get("neuro")));
				extraList.add(Integer.valueOf((String) detailed.get("extra")));
				gewissenList.add(Integer.valueOf((String) detailed.get("gewissen")));
				offenList.add(Integer.valueOf((String) detailed.get("offen")));
				vertragList.add(Integer.valueOf((String) detailed.get("vertrag")));
				anerkennungList.add(Integer.valueOf((String) detailed.get("anerkennung")));
				machtList.add(Integer.valueOf((String) detailed.get("macht")));
				sicherList.add(Integer.valueOf((String) detailed.get("sicher")));
				ehrlichList.add(Integer.valueOf((String) detailed.get("ehrlich")));
				JSONObject metric = (JSONObject) answers.get("metric");
				highestList.add((String) metric.get("highest"));
				lowestList.add((String) metric.get("lowest"));

				ageList.add(Integer.valueOf((String) object.get("age")));
				techniqueList.add(Integer.valueOf((String) object.get("technique")));
				if (((String) object.get("gender")).equals("M")) {
					maleList.add(1);
				} else {
					womenList.add(1);
				}

				Object evt = parser.parse(new FileReader(events));
				JSONArray eventsArray = (JSONArray) evt;
				numberOfClicksList.add(eventsArray.size());
				int duration = 0;
				Iterator evtIter = eventsArray.iterator();
				while (evtIter.hasNext()) {
					JSONObject event = (JSONObject) evtIter.next();
					duration += ((Long) event.get("duration")).intValue();
				}
				durationList.add(duration);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Big5Result b5result = new Big5Result();
		b5result.meanNeuro = calculateAverage(neuroList);
		b5result.meanExtra = calculateAverage(extraList);
		b5result.meanGewissen = calculateAverage(gewissenList);
		b5result.meanOffen = calculateAverage(offenList);
		b5result.meanVertrag = calculateAverage(vertragList);
		b5result.anerkennung = calculateAverage(anerkennungList);
		b5result.macht = calculateAverage(machtList);
		b5result.sicher = calculateAverage(sicherList);
		b5result.ehrlich = calculateAverage(ehrlichList);
		b5result.meanAge = calculateAverage(ageList);
		b5result.meanTechnique = calculateAverage(techniqueList);
		b5result.numberOfmales = maleList.size();
		b5result.numberofWomen = womenList.size();
		b5result.numberOfClicks = calculateAverage(numberOfClicksList);
		b5result.meanDuration = calculateAverage(durationList);

		b5result.numberOfHighestNeuro = counter(highestList, "neuro");
		b5result.numberOfLowestNeuro = counter(lowestList, "neuro");
		b5result.numberOfHighestExtra = counter(highestList, "extra");
		b5result.numberOfLowestExtra = counter(lowestList, "extra");
		b5result.numberOfHighestGewissen = counter(highestList, "gewissen");
		b5result.numberOfLowestGewissen = counter(lowestList, "gewissen");
		b5result.numberOfHighestOffen = counter(highestList, "offen");
		b5result.numberOfLowestOffen = counter(lowestList, "offen");
		b5result.numberOfHighestVertrag = counter(highestList, "vertrag");
		b5result.numberOfLowestVertrag = counter(lowestList, "vertrag");

		if (!CSV_FORMAT) {
			System.out.println(b5result);
			System.out.println("--- counter ---");
			System.out.println("neuro:" + b5result.numberOfHighestNeuro + " - " + b5result.numberOfLowestNeuro);
			System.out.println("extra:" + b5result.numberOfHighestExtra + " - " + b5result.numberOfLowestExtra);
			System.out
					.println("gewissen:" + b5result.numberOfHighestGewissen + " - " + b5result.numberOfLowestGewissen);
			;
			System.out.println("offen:" + b5result.numberOfHighestOffen + " - " + b5result.numberOfLowestOffen);
			System.out.println("vertrag:" + b5result.numberOfHighestVertrag + " - " + b5result.numberOfLowestVertrag);
			System.out.println("--- demo ---");
			System.out.println("age:" + b5result.meanAge);
			System.out.println("technique:" + b5result.meanTechnique);
			System.out.println("male:" + b5result.numberOfmales);
			System.out.println("women:" + b5result.numberofWomen);
			System.out.println("--- stats ---");
			System.out.println("number of clicks: " + b5result.numberOfClicks);
			System.out.println("duration: " + b5result.meanDuration / 1000);
		} else {
			System.out.println(b5result.toCSV());
		}

	}

	public static int counter(List<String> toCount, String matcher) {
		int result = 0;
		for (String ref : toCount) {
			if (ref.contains(matcher))
				result++;
		}
		return result;
	}

	private static int calculateAverage(List<Integer> marks) {
		int sum = 0;
		for (int i = 0; i < marks.size(); i++) {
			sum += marks.get(i);
		}
		double result = 0.0;
		result = (sum * 1.0) / marks.size();
		BigDecimal b = new BigDecimal(result);
		b = b.setScale(0, BigDecimal.ROUND_HALF_UP);
		return b.intValue();
	}
}
