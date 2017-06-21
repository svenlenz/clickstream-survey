package processing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MoveClickers {
	public static boolean USE_WINDOWS = false;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";

	private static final Random random = new Random();

	private static int next() {
	  if (random.nextBoolean()) {
	    return 1;
	  } else {
	    return 2;
	  }
	}
	
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		System.out.println(Big5Result.csvHeader());
		
	
		
		int newCounter = 1;
		for (int id = 1; id <= 126; id++) {
			String events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
					+ "events.json";
//			if(next() == 1) {
//				continue;
//			}
			
			try {
				Object evt = parser.parse(new FileReader(events));
				JSONArray eventsArray = (JSONArray) evt;
				Iterator i = eventsArray.iterator();
				int eventCounter = eventsArray.size();
				boolean fromDetail = false;
				boolean hasBackToProduct = false;
				boolean hasBackToOverview = false;
				boolean fromProduct = false;
				while(i.hasNext()) {					
					JSONObject event = (JSONObject)i.next();
					String eventId = (String) event.get("event");	
	                String productId = (String)event.get("productId");
	                String detail = (String)event.get("detail");
	                if(eventId.contains("backToProduct") || eventId.contains("home")) {
	                	hasBackToProduct = true;
	                }	
	                
	                if(eventId.contains("backToOverview") || eventId.contains("home")) {
	                	hasBackToOverview = true;
	                }	
	                if(!"undefined".equals(detail)) {
	                	fromDetail = true;
	                } else if("undefined".equals(detail) && fromDetail && !hasBackToProduct) {
	                	fromDetail = false;
	                	eventCounter++;
	                } else if(!"undefined".equals(productId) && hasBackToProduct) {
	                	fromDetail = false;
	                	hasBackToProduct = false;
	                }
	                if(!"undefined".equals(productId)) {
	                	fromProduct = true;
	                } else if("undefined".equals(productId) && fromProduct && !hasBackToOverview) {
	                	fromProduct = false;
	                	hasBackToOverview = false;
	                	eventCounter++;
	                } else if("undefined".equals(productId) && fromProduct && hasBackToOverview) {
	                	fromProduct = false;
	                	hasBackToOverview = false;
	                }
//					if(eventId.equals("open")) {
//						System.out.println("open");
//					}
				};
				System.out.println(eventCounter);
//	            Iterator<JSONObject> iterator = eventsArray.iterator();

//				String eventId = (String)event.get("event");
				if(eventCounter >= 30) {		           
					File source = new File((USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/"));
					File target = new File((USE_WINDOWS ? BASE_PATH_WINDOWS + "\\clickers\\" + newCounter + "\\" : BASE_PATH_IOS + "/clickers/" + newCounter + "/"));
					newCounter++;
					MoveClickers.copyDirectory(source, target);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	 public static void copyDirectory(File sourceLocation , File targetLocation)
			    throws IOException {

			        if (sourceLocation.isDirectory()) {
			            if (!targetLocation.exists()) {
			                targetLocation.mkdir();
			            }

			            String[] children = sourceLocation.list();
			            for (int i=0; i<children.length; i++) {
			                copyDirectory(new File(sourceLocation, children[i]),
			                        new File(targetLocation, children[i]));
			            }
			        } else {

			            InputStream in = new FileInputStream(sourceLocation);
			            OutputStream out = new FileOutputStream(targetLocation);

			            // Copy the bits from instream to outstream
			            byte[] buf = new byte[1024];
			            int len;
			            while ((len = in.read(buf)) > 0) {
			                out.write(buf, 0, len);
			            }
			            in.close();
			            out.close();
			        }
			    }
}
