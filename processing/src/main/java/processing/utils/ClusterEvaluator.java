package processing.utils;

import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClusterEvaluator {
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		
		try{
        Object obj = parser.parse(new FileReader(
                "/Users/sle/Repos/clickstream-survey/clustering/testresult.json"));
        JSONArray clusters = (JSONArray) obj;	
        extractCluster(clusters, "");
        
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	public static void extractCluster(JSONArray clusters, String indent) {
		Iterator<JSONObject> iterator = clusters.iterator();
		JSONArray children;
		while(iterator.hasNext()) {
			Object next = iterator.next();
			if(next instanceof String) {
				String s = (String)next;
				if(s.contains("t")) {
					System.out.println(indent + "TREE");
					indent += "\t";
				} else if(s.contains("l")){
					System.out.println(indent + "LIST");
					indent += "\t";
					
				}
			} else if(next instanceof JSONArray) {
				children = (JSONArray) next;
				extractCluster(children, indent);
			} else if(next instanceof JSONObject) {
			} else {
				System.out.println(indent + next);
			}

		}

		
	}
}
