package processing.kmeans;


/**
 * configuration for sequencing. see {@link org.apache.mahout.text.SequenceFilesFromDirectory} 
 * @author sle
 *
 * @author sven.lenz@msc.htwchur.ch
 */
public class SequenceConfiguration {
	
	private final String inputPath;
	private final String outputPath;
	private final String charset;
	private final String chunkSize;

    public SequenceConfiguration(String inputPath, String outputPath) {
    	this.inputPath = inputPath;
    	this.outputPath = outputPath;
    	charset = "UTF-8";
    	chunkSize = "5";
    }
    
    public SequenceConfiguration(String inputPath, String outputPath, String charset, String chunkSize) {
    	this.inputPath = inputPath;
    	this.outputPath = outputPath;
    	this.charset = charset;
    	this.chunkSize = chunkSize;
    }
    
    public String[] getArguments() {
      String[] sequenceArguments = {"-i", inputPath, 
		  "-o", outputPath,
		  "-c", charset,
		  "-chunk", chunkSize};
      
      return sequenceArguments;
    }
    
    
}
