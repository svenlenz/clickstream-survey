package processing.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClusterEvaluator {
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		
		try{
        Object obj = parser.parse(new FileReader(
//                "/Users/sle/Repos/clickstream-survey/clustering/testresult.json"));
//        		"C:\\Users\\slenz\\workspace\\clickstream-survey\\clustering\\testresult_click.json"));
				"C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\results\\testresult4.json"));

        JSONArray jsonClusters = (JSONArray) obj;	
        Cluster cluster = new Cluster();
        Cluster clusterResult = extractCluster(jsonClusters, "", cluster);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(clusterResult);
        System.out.println(jsonInString);
        List<List<Integer>> clusterCollection = new ArrayList<List<Integer>>();
        toList(clusterCollection, clusterResult);
        System.out.println("//// CLUSTER SIZE //// " + clusterCollection.size());

		
		
		List<Integer> clusterZero = new ArrayList<Integer>();

		for(int i = 1; i <= 48; i++) {
			clusterZero.add(i);
		}
		List<Integer> clusterOne = Arrays.asList(1,11,13,15,20,22,23,24,25,28,31,32);
		List<Integer> clusterTwo = Arrays.asList(19, 10, 9, 14, 16);
		List<Integer> clusterThree = Arrays.asList(2,3,4,7,8,12,17,21,27,30);
		List<Integer> clusterFour = Arrays.asList(5,6,18,26,29);

		System.out.println("##### ALL " + clusterZero.size() + " #####");
		meanResultValues(clusterZero);
		System.out.println("---------------------------------");
		clusterCollection.forEach(cl -> {
			System.out.println("##### "+ cl.size() +" #####");
			meanResultValues(cl);
			System.out.println("---------------------------------");
		});
//		meanResultValues(clusterOne);
//		System.out.println("------------");
//		meanResultValues(clusterTwo);
//		System.out.println("------------");
//		meanResultValues(clusterThree);		
//		System.out.println("------------");
//		meanResultValues(clusterFour);	
//		
        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static void toList(List<List<Integer>> collection, Cluster cluster) {
        for(Leaf f : cluster.getLeafs()) {
    		List<Integer> clusterOne = new ArrayList<Integer>();
        	clusterOne.addAll(f.getIds());
            collection.add(clusterOne);

        }
        cluster.getClusters().forEach(cl -> {
        	toList(collection, cl);
        });
	}
	
	//t means tree cluster who has child clusters.
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
		while(iterator.hasNext()) {
			Object next = iterator.next();
			if(next instanceof String) {
				String s = (String)next;
				if(s.contains("t")) {
					nextCluster = new Cluster();
					cluster.clusters.add(nextCluster);
					System.out.println(indent + "TREE");
					indent += "\t";					
				} else if(s.contains("l")){
					System.out.println(indent + "LIST");
					nextCluster.leafs.add(new Leaf());
					indent += "\t";					
				}
			} else if(next instanceof JSONArray) {
				children = (JSONArray) next;
				Cluster ret = extractCluster(children, indent, nextCluster);
			} else if(next instanceof JSONObject) {
				//exclusions etc...
			} else {
				System.out.println(indent + next);
				nextCluster.leafs.get(nextCluster.leafs.size()-1).ids.add(((Long)next).intValue());
				
				JSONParser parser = new JSONParser();		
				String survey = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+next+"\\survey.json";
				try {
					Object obj = parser.parse(new FileReader(survey));
			        JSONObject object = (JSONObject) obj;
			        JSONObject answers = (JSONObject)object.get("big5_result");
//			        System.out.println(answers);		
				} catch (Exception e) {
//					System.out.println(e);
				}
			}

		}
		return nextCluster;		
	}
	
	static class Big5Result {
		private int neuro;
		private int extra;
		private int gewissen;
		private int offen;
		private int vertrag;
		private int anerkennung;
		private int macht;
		private int sicher;
		private int ehrlich;
		private String highest;
		private String lowest;
		private int technique;
		private int male;
		private int women;
		private int age;
		private int numberOfClicks;
		private int duration;
		
		public int getNeuro() {
			return neuro;
		}
		public void setNeuro(int neuro) {
			this.neuro = neuro;
		}
		public int getExtra() {
			return extra;
		}
		public void setExtra(int extra) {
			this.extra = extra;
		}
		public int getGewissen() {
			return gewissen;
		}
		public void setGewissen(int gewissen) {
			this.gewissen = gewissen;
		}
		public int getOffen() {
			return offen;
		}
		public void setOffen(int offen) {
			this.offen = offen;
		}
		public int getVertrag() {
			return vertrag;
		}
		public void setVertrag(int vertrag) {
			this.vertrag = vertrag;
		}
		public int getAnerkennung() {
			return anerkennung;
		}
		public void setAnerkennung(int anerkennung) {
			this.anerkennung = anerkennung;
		}
		public int getMacht() {
			return macht;
		}
		public void setMacht(int macht) {
			this.macht = macht;
		}
		public int getSicher() {
			return sicher;
		}
		public void setSicher(int sicher) {
			this.sicher = sicher;
		}
		public int getEhrlich() {
			return ehrlich;
		}
		public void setEhrlich(int ehrlich) {
			this.ehrlich = ehrlich;
		}
		
		public String getHighest() {
			return highest;
		}
		public void setHighest(String highest) {
			this.highest = highest;
		}
		public String getLowest() {
			return lowest;
		}
		public void setLowest(String lowest) {
			this.lowest = lowest;
		}
		
		public int getTechnique() {
			return technique;
		}
		public void setTechnique(int technique) {
			this.technique = technique;
		}
		public int getMale() {
			return male;
		}
		public void setMale(int male) {
			this.male = male;
		}
		public int getWomen() {
			return women;
		}
		public void setWomen(int women) {
			this.women = women;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		
		public int getNumberOfClicks() {
			return numberOfClicks;
		}
		public void setNumberOfClicks(int numberOfClicks) {
			this.numberOfClicks = numberOfClicks;
		}
		public int getDuration() {
			return duration;
		}
		public void setDuration(int duration) {
			this.duration = duration;
		}
	
		@Override
		public String toString() {
			return "neuro: " + neuro + "\nextra: " + extra + "\ngewissen:" + gewissen + "\noffen:" + offen + "\nvertrag:" + vertrag;
//					+ "\nanerkennung:" + anerkennung + "\nmacht:" + macht + "\nsicher:" + sicher + "\nehrlich: " + ehrlich;
		}
	}
	
	public static void meanResultValues(List<Integer> clusterIDs) {
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
			String survey = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+id+"\\survey.json";
			String events = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+id+"\\events.json";

				try {
					Object obj = parser.parse(new FileReader(survey));
			        JSONObject object = (JSONObject) obj;
			        JSONObject answers = (JSONObject)object.get("big5_result");
			        JSONObject detailed = (JSONObject)answers.get("detailed");
			        neuroList.add(Integer.valueOf((String)detailed.get("neuro")));
			        extraList.add(Integer.valueOf((String)detailed.get("extra")));
			        gewissenList.add(Integer.valueOf((String)detailed.get("gewissen")));
			        offenList.add(Integer.valueOf((String)detailed.get("offen")));
			        vertragList.add(Integer.valueOf((String)detailed.get("vertrag")));
			        anerkennungList.add(Integer.valueOf((String)detailed.get("anerkennung")));
			        machtList.add(Integer.valueOf((String)detailed.get("macht")));
			        sicherList.add(Integer.valueOf((String)detailed.get("sicher")));
			        ehrlichList.add(Integer.valueOf((String)detailed.get("ehrlich")));
			        JSONObject metric = (JSONObject)answers.get("metric");
			        highestList.add((String)metric.get("highest"));
			        lowestList.add((String)metric.get("lowest"));
			        
			        ageList.add(Integer.valueOf((String)object.get("age")));
			        techniqueList.add(Integer.valueOf((String)object.get("technique")));
			        if(((String)object.get("gender")).equals("M")) {
				        maleList.add(1);
			        } else {
			        	womenList.add(1);
			        }
			        
			        Object evt = parser.parse(new FileReader(events));
			        JSONArray eventsArray = (JSONArray) evt;
			        numberOfClicksList.add(eventsArray.size());
			        int duration = 0;
			        Iterator evtIter = eventsArray.iterator();
			        while(evtIter.hasNext()) {	
			        	JSONObject event = (JSONObject)evtIter.next();
			        	duration += ((Long)event.get("duration")).intValue();
			        }
			        durationList.add(duration);
			        
				} catch (Exception e) {
					e.printStackTrace();
				}			
		});
		
		Big5Result b5result = new Big5Result();
		b5result.setNeuro(calculateAverage(neuroList));
		b5result.setExtra(calculateAverage(extraList));
		b5result.setGewissen(calculateAverage(gewissenList));
		b5result.setOffen(calculateAverage(offenList));
		b5result.setVertrag(calculateAverage(vertragList));
		b5result.setAnerkennung(calculateAverage(anerkennungList));
		b5result.setMacht(calculateAverage(machtList));
		b5result.setSicher(calculateAverage(sicherList));
		b5result.setEhrlich(calculateAverage(ehrlichList));
		b5result.setAge(calculateAverage(ageList));
		b5result.setTechnique(calculateAverage(techniqueList));
		b5result.setMale(maleList.size());
		b5result.setWomen(womenList.size());
		b5result.setNumberOfClicks(calculateAverage(numberOfClicksList));
		b5result.setDuration(calculateAverage(durationList));

		
		System.out.println(b5result);
		System.out.println("--- counter ---");
		System.out.println("neuro:" + counter(highestList, "neuro") + " - " + counter(lowestList, "neuro"));
		System.out.println("extra:" + counter(highestList, "extra") + " - " + counter(lowestList, "extra"));
		System.out.println("gewissen:" + counter(highestList, "gewissen") + " - " + counter(lowestList, "gewissen"));;
		System.out.println("offen:" + counter(highestList, "offen") + " - " + counter(lowestList, "offen"));
		System.out.println("vertrag:" + counter(highestList, "vertrag") + " - " + counter(lowestList, "vertrag"));
		System.out.println("--- demo ---");
		System.out.println("age:" + b5result.getAge());
		System.out.println("technique:" + b5result.getTechnique());
		System.out.println("male:" + b5result.getMale());
		System.out.println("women:" + b5result.getWomen());		
		System.out.println("--- stats ---");
		System.out.println("number of clicks: " + b5result.getNumberOfClicks());
		System.out.println("duration: " + b5result.getDuration() / 1000);

	}
	
	public static int counter(List<String> toCount, String matcher) {
		int result = 0;
		for(String ref: toCount) {
			if(ref.contains(matcher))
				result++;	
		}
		return result;
	}
	
	  private static int calculateAverage(List <Integer> marks) {
	      int sum = 0;
	      for (int i=0; i< marks.size(); i++) {
	            sum += marks.get(i);
	      }	      
	      return sum / marks.size();
	  }
}
