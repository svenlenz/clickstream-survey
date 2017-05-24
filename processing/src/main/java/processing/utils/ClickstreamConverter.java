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
	 
	        for (int i = 1; i <= 101; i++) {
	        		       
		        try {
//		        	i = 61;
		            Object obj = parser.parse(new FileReader(
//		            		"/Users/sle/switchdrive/Master/survey_results/clickers/" + i + "/events.json"));
//		                    "/Users/sle/switchdrive/Master/survey_results/" + i + "/events.json"));
        					"C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+i+"\\events.json"));
		            
		            
		 
		            JSONArray events = (JSONArray) obj;	           
//		            events = shuffleJsonArray(events);
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
		            while (iterator.hasNext()) {
		                JSONObject event = iterator.next();

//		                System.out.println(event);
		                String eventId = (String)event.get("event");
		                String datetime = (String)event.get("datetime");
		                Long duration = (Long)event.get("duration");
		                String sessionId = (String)event.get("sessionId");
		                String productId = (String)event.get("productId");
		                String detail = (String)event.get("detail");
		                String linkId = (String)event.get("linkId");
		                
//		                Random random = new Random();
//		                if (random.nextBoolean()) {
//		                    continue;
//		                }
		                
		                if(first) {
	//	                	clusteringEventLogDetailed = sessionId + "\t";
		                	clusteringEventLogDetailed = i + "\t";
		                	clusteringEventLogCondensed = i + "\t";
		                	clusteringEventLogCounter = i + "\t";
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
		                
		                if(lastEvent == null || lastEvent.equals(eventId)) {
		                	counter++;
		                } else {
		                	clusteringEventLogCounter += lastEvent +"("+counter+")";
		                	counter = 1;
		                	lastEventMachted = true;
		                }
	                	lastEvent = eventId;		                
		                
		                int durationInSeconds = (int) (durationSinceLastEvent/1000);
		                if(durationInSeconds == 0)
		                	durationInSeconds = 1;
		                
		                if(duration == 0)
		                	duration = 1L;
		                
//		                clusteringEventLogDetailed += eventId + productId + detail + linkId + "(" + durationInSeconds + ")";
		                clusteringEventLogDetailed += eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId : "") + (!"".equals(detail) ? "Detail" + detail : "") + linkId + "(" + duration + ")";//		                clusteringEventLogDetailed += eventId + productId + detail + linkId + "(" + normalizedDurationSinceLastEvent + ")";
//		                clusteringEventLogDetailed += eventId + (("".equals(detail) && "".equals(linkId)) ? "Product" + productId : "") + (!"".equals(detail) ? "Detail" + detail : "") + linkId + "(" + normalizedDurationSinceLastEvent + ")";
//		                clusteringEventLogDetailed += eventId + "(" + normalizedDurationSinceLastEvent + ")";
		                clusteringEventLogCondensed += eventId.charAt(0) + productId + "(" + normalizedDurationSinceLastEvent + ")";
		                durationSinceLastEvent = duration;
		                if(0 == durationSinceLastEvent) {
		                	durationSinceLastEvent = 1L;
		                	normalizedDurationSinceLastEvent = 1;
		                } else {
		                	if(durationSinceLastEvent <= 1000) {
		                		normalizedDurationSinceLastEvent = 2;	
		                	}
		                	
		                	if(durationSinceLastEvent > 1000 && durationSinceLastEvent <= 2000) {
		                		normalizedDurationSinceLastEvent = 3;	
		                	}
		                	
		                	if(durationSinceLastEvent > 2000 &&  durationSinceLastEvent <= 3000) {
		                		normalizedDurationSinceLastEvent = 4;	
		                	}
		                	
		                	if(durationSinceLastEvent > 3000 && durationSinceLastEvent <= 5000) {
		                		normalizedDurationSinceLastEvent = 5;	
		                	}
		                	
		                	if(durationSinceLastEvent > 5000 && durationSinceLastEvent <= 8000) {
		                		normalizedDurationSinceLastEvent = 6;	
		                	}
		                	
		                	if(durationSinceLastEvent > 8000 && durationSinceLastEvent <= 13000) {
		                		normalizedDurationSinceLastEvent = 7;	
		                	}
		                	
		                	if(durationSinceLastEvent > 13000 && durationSinceLastEvent <= 20000) {
		                		normalizedDurationSinceLastEvent = 8;	
		                	}	
		                	
		                	if(durationSinceLastEvent > 20000 && durationSinceLastEvent <= 30000) {
		                		normalizedDurationSinceLastEvent = 9;	
		                	}
		                	
		                	if(durationSinceLastEvent > 30000 && durationSinceLastEvent <= 60000) {
		                		normalizedDurationSinceLastEvent = 10;	
		                	}		                	
		                	
		                	if(durationSinceLastEvent > 60000) {
		                		normalizedDurationSinceLastEvent = 11;	
		                	}	                	
		                }
		                
		                
		            }
		            
		        
			        if(!lastEventMachted) {
	                	clusteringEventLogCounter += lastEvent +"("+counter+")";
			        }
//		            System.out.println(clusteringEventLogDetailed);
//		            System.out.println(clusteringEventLogCondensed);
		            System.out.println(clusteringEventLogCounter);

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
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
