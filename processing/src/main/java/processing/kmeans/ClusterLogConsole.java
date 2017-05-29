package processing.kmeans;

public class ClusterLogConsole extends ClusterLogBase implements ClusterLog {
	
	@Override
	public void log(String message) {		
		System.out.println(addTimestampToMessage(message));
	}	
}
