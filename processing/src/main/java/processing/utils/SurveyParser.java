package processing.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * create a CSV file out of a big5 survey results.
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class SurveyParser {
	
	public static boolean USE_WINDOWS = true;
	public static String BASE_PATH_WINDOWS = "..\\results\\survey_results\\";
	public static String BASE_PATH_IOS = "../results/survey_results/";
	
	public static void main(String[] args) {
		for (int i = 1; i <= 126; i++) {

			JSONParser parser = new JSONParser();
			String base = USE_WINDOWS ? BASE_PATH_WINDOWS : BASE_PATH_IOS;
			try {
				Object obj = parser.parse(new FileReader(base + "survey.json"));
				JSONObject object = (JSONObject) obj;
				JSONObject answers = (JSONObject) object.get("big5_answers");
				System.out.println(answers);
				Iterator<?> keys = answers.keySet().iterator();

				String csvFile = base + "survey.csv";
				FileWriter writer = new FileWriter(csvFile);

				while (keys.hasNext()) {
					String key = (String) keys.next();
					System.out.println(answers.get(key));
					List<String> csvLine = null;
					String answer = (String) answers.get(key);
					if ("0".equals(answer)) {
						csvLine = Arrays.asList("X", "", "", "");
					} else if ("1".equals(answer)) {
						csvLine = Arrays.asList("", "X", "", "");
					} else if ("2".equals(answer)) {
						csvLine = Arrays.asList("", "", "X", "");
					} else if ("3".equals(answer)) {
						csvLine = Arrays.asList("", "", "", "X");
					}
					CSVUtils.writeLine(writer, csvLine);
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
