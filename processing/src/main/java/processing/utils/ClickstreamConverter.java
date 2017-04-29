package processing.utils;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ClickstreamConverter {

	   public static void main(String[] args) {
	        JSONParser parser = new JSONParser();
	 
	        try {
	 
	            Object obj = parser.parse(new FileReader(
	                    "/Users/sle/Repos/clickstream-survey/data/testrun_sle/events.json"));
	 
	            JSONArray events = (JSONArray) obj;	           
	            events = shuffleJsonArray(events);
	            Iterator<JSONObject> iterator = events.iterator();
	            String clusteringEventLogDetailed = "";
	            String clusteringEventLogCondensed = "";
	            boolean first = true;
	            Long durationSinceLastEvent = 1L;
	            int normalizedDurationSinceLastEvent = 1;
	            while (iterator.hasNext()) {
//	                System.out.println(iterator.next());
	                JSONObject event = iterator.next();
	                String eventId = (String)event.get("event");
	                String datetime = (String)event.get("datetime");
	                Long duration = (Long)event.get("duration");
	                String sessionId = (String)event.get("sessionId");
	                String productId = (String)event.get("productId");
	                String detail = (String)event.get("detail");
	                String linkId = (String)event.get("linkId");
	                
	                Random random = new Random();
	                if (random.nextBoolean()) {
	                    continue;
	                }
	                
	                if(first) {
	                	clusteringEventLogDetailed = sessionId + "\t";
	                	clusteringEventLogCondensed = "1\t";
	                	first = false;
	                }
	                
	                if("undefined".equals(productId)) {
	                	productId = "";
	                }
	                
	                if("undefined".equals(detail)) {
	                	detail = "";
	                }
	                
	                if("undefined".equals(linkId)) {
	                	linkId = "";
	                }
	                
	                clusteringEventLogDetailed += eventId + productId + detail + "(" + durationSinceLastEvent + ")";
	                clusteringEventLogCondensed += eventId.charAt(0) + productId + "(" + normalizedDurationSinceLastEvent + ")";
	                durationSinceLastEvent = duration;
	                if(0 == durationSinceLastEvent) {
	                	durationSinceLastEvent = 1L;
	                	normalizedDurationSinceLastEvent = 1;
	                } else {
	                	if(durationSinceLastEvent <= 5000) {
	                		normalizedDurationSinceLastEvent = 2;	
	                	}
	                	
	                	if(durationSinceLastEvent > 5000 && durationSinceLastEvent <= 10000) {
	                		normalizedDurationSinceLastEvent = 3;	
	                	}
	                	
	                	if(durationSinceLastEvent > 10000 &&  durationSinceLastEvent <= 20000) {
	                		normalizedDurationSinceLastEvent = 4;	
	                	}
	                	
	                	if(durationSinceLastEvent > 20000 && durationSinceLastEvent <= 30000) {
	                		normalizedDurationSinceLastEvent = 5;	
	                	}
	                	
	                	if(durationSinceLastEvent > 30000 && durationSinceLastEvent <= 40000) {
	                		normalizedDurationSinceLastEvent = 6;	
	                	}
	                	
	                	if(durationSinceLastEvent > 40000 && durationSinceLastEvent <= 50000) {
	                		normalizedDurationSinceLastEvent = 7;	
	                	}
	                	
	                	if(durationSinceLastEvent > 50000 && durationSinceLastEvent <= 60000) {
	                		normalizedDurationSinceLastEvent = 8;	
	                	}	
	                	
	                	if(durationSinceLastEvent > 60000) {
	                		normalizedDurationSinceLastEvent = 9;	
	                	}	                	
	                }
	                
	                
	            }
	            System.out.println(clusteringEventLogDetailed);
	            System.out.println(clusteringEventLogCondensed);
//	 
//	            String name = (String) jsonObject.get("Name");
//	            String author = (String) jsonObject.get("Author");
//	            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
//	 
//	            System.out.println("Name: " + name);
//	            System.out.println("Author: " + author);
//	            System.out.println("\nCompany List:");
//	            Iterator<String> iterator = companyList.iterator();
//	            while (iterator.hasNext()) {
//	                System.out.println(iterator.next());
//	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	   
	   public static JSONArray shuffleJsonArray (JSONArray array) throws JSONException {
		    // Implementing Fisher–Yates shuffle
		        Random rnd = new Random();
		        for (int i = array.size() - 1; i >= 0; i--)
		        {
		          int j = rnd.nextInt(i + 1);
		          // Simple swap
		          Object object = array.get(j);
		          array.add(j, array.get(i));
		          array.add(i, object);
		        }
		    return array;
		}
}
