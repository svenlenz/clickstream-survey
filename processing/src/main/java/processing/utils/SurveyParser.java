package processing.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SurveyParser {
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		
		try{
        Object obj = parser.parse(new FileReader(
                "C:\\Users\\slenz\\workspace\\clickstream-survey\\data\\test.json"));
        JSONObject object = (JSONObject) obj;	
        System.out.println(object);
        Iterator<?> keys = object.keySet().iterator();
        
        String csvFile = "C:\\Users\\slenz\\workspace\\clickstream-survey\\data\\test.csv";
        FileWriter writer = new FileWriter(csvFile);
     

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            System.out.println(object.get(key));
            List<String> csvLine = null;
            Long answer = (Long)object.get(key);
            if(0 == answer) {
            	csvLine = Arrays.asList("X", "", "", "");
            } else if (1 == answer) {
            	csvLine = Arrays.asList("", "X", "", "");
            } else if (2 == answer) {
            	csvLine = Arrays.asList("", "", "X", "");
            } else if (3 == answer) {
            	csvLine = Arrays.asList("", "", "", "X");
            }
            CSVUtils.writeLine(writer, csvLine);
        }
        writer.flush();
        writer.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
