package processing;

import static spark.Spark.port;
import static spark.Spark.post;

import java.io.FileWriter;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * TBD
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class ProcessingServer {

	// environment variable shared data volume
	public final static String DATA_VOLUME_ENV_VAR = "SURVEY_DATA_VOLUME";
	// base data path
	private static String PATH_TO_SHARED_VOLUME;
	// static initializer to correctly set paths for docker/eclipse
	static {
		// if defined: eclipse, else docker
		String s = System.getenv().get(DATA_VOLUME_ENV_VAR);
		if (s != null) {
			PATH_TO_SHARED_VOLUME = s;
		} else {
			PATH_TO_SHARED_VOLUME = "/opt/data";
		}
	}	
	
	public static void main(String[] args) {
		port(4568);
		post("/survey",
				(req, res) -> {
					System.out.println("---> aaaaaight");
					ObjectMapper mapper = new ObjectMapper();
					if (null != req.body() && !"".equals(req.body())) {
						System.out.println("---> weiter");
						JsonNode surveryResult = mapper.readTree(req.body());
						// try-with-resources statement based on post comment below :)
						System.out.println("---> write: " + PATH_TO_SHARED_VOLUME + "/file1.txt");
						try (FileWriter file = new FileWriter(PATH_TO_SHARED_VOLUME + "/file1.txt")) {
							file.write(surveryResult.toString());
							//TODO: logger!!! (fallback...)
							System.out.println("Successfully Copied JSON Object to File...");
							System.out.println("\nJSON Object: " + surveryResult);
						} catch(Exception e) {
							System.out.println("---> error");
							e.printStackTrace();							
						}
					}

					return "ok";
				});
	}
}
