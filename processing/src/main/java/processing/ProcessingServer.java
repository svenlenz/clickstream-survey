package processing;

import static spark.Spark.port;
import static spark.Spark.post;

/**
 * TBD
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class ProcessingServer {

	public static void main(String[] args) {

		port(4568);
		//webSocket("/clusteringws", ClusteringWebSocketHandler.class);

		post("/test",
				(req, res) -> {


					return "test";
				});
	}
}
