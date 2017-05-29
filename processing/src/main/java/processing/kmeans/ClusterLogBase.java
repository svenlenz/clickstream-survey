package processing.kmeans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClusterLogBase {
	
	public String addTimestampToMessage(String message) {
		return getCurrentTimeStamp() + ": " + message;
	}
	
	private String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
}
