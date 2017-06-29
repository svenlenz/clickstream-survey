package processing;

import static spark.Spark.port;
import static spark.Spark.post;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Server application to collect survey results and click events. 
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
	final static Logger logger = Logger.getLogger(ProcessingServer.class);

	
	public static void main(String[] args) {
		 
		logger.info("Starting application");
		 
		port(4568);
		post("/survey",
				(req, res) -> {
					logger.info("write new survey");
					System.out.println("write new survey");
					ObjectMapper mapper = new ObjectMapper();
					if (null != req.body() && !"".equals(req.body())) {
						JsonNode surveryResult = mapper.readTree(req.body());
						logger.info("using survey " + surveryResult);
						System.out.println("using survey " + surveryResult);					
						String sessionId = surveryResult.get("sessionId").getTextValue().replaceAll(":", "_");
						logger.info("using sessionId " + sessionId);
						System.out.println("using sessionId " + sessionId);
						
						String path = PATH_TO_SHARED_VOLUME + "/" + sessionId + "/survey.json";
						logger.info("write " + path);
						System.out.println("---> write: " + path);
						final File file = new File(path);						
						final File parentDirectory = file.getParentFile();
						if (null != parentDirectory)
						{
							parentDirectory.mkdirs();
						}

						FileWriter writer = new FileWriter(file);						
						writer.write(surveryResult.toString());
						logger.info("Successfully Copied JSON Object to File...");
						System.out.println("Successfully Copied JSON Object to File...");
						logger.info("\nJSON Object: " + surveryResult);
						System.out.println("\nJSON Object: " + surveryResult);
						writer.close();
					}

					return "ok";
				});
		
		post("/clickstream",
				(req, res) -> {
					logger.debug("write new event");
					System.out.println("write new event");
					ObjectMapper mapper = new ObjectMapper();
					if (null != req.body() && !"".equals(req.body())) {
						JsonNode clickstreamEvent = mapper.readTree(req.body());
						System.out.println("using event " + clickstreamEvent);
						logger.debug("using event " + clickstreamEvent);
						String sessionId = clickstreamEvent.get("sessionId").getTextValue().replaceAll(":", "_");
						System.out.println("using sessionId " + sessionId);
						
						String path = PATH_TO_SHARED_VOLUME + "/" + sessionId + "/events.json";		
						System.out.println("---> write: " + path);
						logger.debug("write " + path);
						final File file = new File(path);
						final File parentDirectory = file.getParentFile();
						if (null != parentDirectory)
						{
							parentDirectory.mkdirs();
						}
					
						boolean firstEntry = false;
						if(!file.exists()) { 
							System.out.println("create new events.json file");
							firstEntry = true;
						    Files.write(Paths.get(file.toURI()),"[]".getBytes(), StandardOpenOption.CREATE);	
						}					
						RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
						long pos = randomAccessFile.length();
						while (randomAccessFile.length() > 0) {
						    pos--;
						    randomAccessFile.seek(pos);
						    if (randomAccessFile.readByte() == ']') {
						        randomAccessFile.seek(pos);
						        break;
						    }
						}
						String jsonElement = clickstreamEvent.toString();
						if(firstEntry) {
							randomAccessFile.writeBytes(jsonElement + "]");
						} else {
							randomAccessFile.writeBytes("," + jsonElement + "]");
						}
						randomAccessFile.close();
						logger.debug("Successfully appended event to JSON File...");
						logger.debug("\nJSON Object: " + clickstreamEvent);
						System.out.println("Successfully appended event to JSON File...");
						System.out.println("\nJSON Object: " + clickstreamEvent);
						
					}

					return "ok";
				});		
	}
}
