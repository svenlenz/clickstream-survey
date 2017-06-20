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
		for (int i = 123; i <= 123; i++) {

			JSONParser parser = new JSONParser();
			String base = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\"+i+"\\";
			try {
				Object obj = parser.parse(new FileReader(
						// "C:\\Users\\slenz\\workspace\\clickstream-survey\\data\\test.json"));
						base + "survey.json"));
				JSONObject object = (JSONObject) obj;
				JSONObject answers = (JSONObject) object.get("big5_answers");
				System.out.println(answers);
				Iterator<?> keys = answers.keySet().iterator();

				// String csvFile =
				// "C:\\Users\\slenz\\workspace\\clickstream-survey\\data\\test.csv";
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
