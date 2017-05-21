package processing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class MoveClickers {
	public static boolean USE_WINDOWS = false;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		System.out.println(Big5Result.csvHeader());
		
		int newCounter = 1;
		for (int id = 1; id <= 92; id++) {
			String events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
					+ "events.json";

			try {
				Object evt = parser.parse(new FileReader(events));
				JSONArray eventsArray = (JSONArray) evt;
				if(eventsArray.size() >= 3) {		           
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
