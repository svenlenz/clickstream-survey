package processing.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
				"C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\results\\testresult2.json"));

        JSONArray jsonClusters = (JSONArray) obj;	
        Cluster cluster = new Cluster();
        Cluster clusterResult = extractCluster(jsonClusters, "", cluster);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(clusterResult);
        System.out.println(jsonInString);

		} catch(Exception e) {
			System.out.println(e);
		}
		
		List<Integer> clusterZero = new ArrayList<Integer>();

		for(int i = 1; i <= 32; i++) {
			clusterZero.add(i);
		}
		List<Integer> clusterOne = Arrays.asList(1,11,13,15,20,22,23,24,25,28,31,32);
		List<Integer> clusterTwo = Arrays.asList(19, 10, 9, 14, 16);
		List<Integer> clusterThree = Arrays.asList(2,3,4,7,8,12,17,21,27,30);
		List<Integer> clusterFour = Arrays.asList(5,6,18,26,29);

		System.out.println("------------");
		meanResultValues(clusterZero);
		System.out.println("------------");
		meanResultValues(clusterOne);
		System.out.println("------------");
		meanResultValues(clusterTwo);
		System.out.println("------------");
		meanResultValues(clusterThree);		
		System.out.println("------------");
		meanResultValues(clusterFour);	
	}
	
	//t means tree cluster who has child clusters.
	static class Cluster {
		public ArrayList<Leaf> leafs = new ArrayList<Leaf>();
		public ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		public Cluster() {
			
		}
	};
	
	// l means leaf node that cannot be further split.
	static class Leaf {
		public ArrayList<Long> ids = new ArrayList<Long>();
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
				nextCluster.leafs.get(nextCluster.leafs.size()-1).ids.add((Long)next);
				
				JSONParser parser = new JSONParser();		
				String survey = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+next+"\\survey.json";
				try {
					Object obj = parser.parse(new FileReader(survey));
			        JSONObject object = (JSONObject) obj;
			        JSONObject answers = (JSONObject)object.get("big5_result");
			        System.out.println(answers);		
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
		
		clusterIDs.forEach(id -> {
			String survey = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+id+"\\survey.json";
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
				} catch (Exception e) {
	//				System.out.println(e);
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
		
		System.out.println(b5result);
	}
	
	  private static int calculateAverage(List <Integer> marks) {
	      int sum = 0;
	      for (int i=0; i< marks.size(); i++) {
	            sum += marks.get(i);
	      }	      
	      return sum / marks.size();
	  }
}
