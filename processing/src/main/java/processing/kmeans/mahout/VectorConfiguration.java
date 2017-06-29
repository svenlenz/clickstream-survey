package processing.kmeans.mahout;

import org.apache.lucene.analysis.Analyzer;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.codehaus.jackson.JsonNode;

/**
 * configuration for vector / keyword creation. can either be used with default
 * settings (default constructor) or with custom settings.
 * 
 * TODO: reference why we choose this default settings.
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class VectorConfiguration {

	private Class analyzerClass;
	private int minSupport;
	private int minDf;
	private int maxDfPercent;
	private int maxNGramSize;
	private int minLLRValue;
	private int reduceTasks;
	private int chunkSize;
	private int normPower;
	private int posTagging;
	
	private String fileFilterLanguages = "*";
	private String fileFilterSpacetypes = "*";
	private String fileFilterSpacestatus = "*";
	private String fileFilterSpacecategories = "*";
	private String fileFilterPageStatus = "*";
	private int fileFilterMinContentLength = 0;
	
	private boolean wikiCache = false;
	private boolean vectorizationCache = false;

	/**
	 * default configuration with the values:
	 *
	 * <ul>
	 * <li>analyzerClass = recognition.analyzer.PivwAnalyzer</li>
	 * <li>minSupport = 1</li>
	 * <li>minDf = 1</li>
	 * <li>maxDfPercent = 70</li>
	 * <li>maxNGramSize = 2</li>
	 * <li>minLLRValue = 100</li>
	 * <li>reduceTasks = 1</li>
	 * <li>chunkSize = 200</li>
	 * <li>normPower = 2</li>
	 * <li>posTagging = 2 (0=off, 1=before, 2=after)</li>
	 * </ul>
	 */
	public VectorConfiguration() throws ClassNotFoundException {
		analyzerClass = Class.forName("org.apache.lucene.analysis.standard.StandardAnalyzer");
		
		minSupport = 1;
		minDf = 1;
		maxDfPercent = 70;
		maxNGramSize = 2;
		minLLRValue = 100;
		reduceTasks = 1;
		chunkSize = 200;
		normPower = 2;
		posTagging = 2;

		fileFilterLanguages = "*";
		fileFilterSpacetypes = "*";
		fileFilterSpacestatus = "*";
		fileFilterSpacecategories = "*";
		fileFilterPageStatus = "*";
		fileFilterMinContentLength = 0;
	
	}

	/**
	 * custom configuration
	 * 
	 * @param analyzerClass
	 *            The Lucene {@link Analyzer} for tokenizing the UTF-8 text
	 * @param minSupport
	 *            the minimum frequency of the feature in the entire corpus to
	 *            be considered for inclusion in the sparse vector
	 * @param minDf
	 *            The minimum document frequency. Default 1
	 * @param maxDfPercent
	 *            The max percentage of vectors for the DF. Can be used to
	 *            remove really high frequency features. Expressed as an integer
	 *            between 0 and 100. Default 99
	 * @param maxNGramSize
	 *            1 = unigram, 2 = unigram and bigram, 3 = unigram, bigram and
	 *            trigram
	 * @param minLLRValue
	 *            minValue of log likelihood ratio to used to prune ngrams
	 * @param reduceTasks
	 *            The number of reducers to spawn. This also affects the
	 *            possible parallelism since each reducer will typically produce
	 *            a single output file containing tf-idf vectors for a subset of
	 *            the documents in the corpus.
	 * @param chunkSize
	 *            the size in MB of the feature => id chunk to be kept in memory
	 *            at each node during Map/Reduce stage. Its recommended you
	 *            calculated this based on the number of cores and the free
	 *            memory available to you per node. Say, you have 2 cores and
	 *            around 1GB extra memory to spare we recommend you use a split
	 *            size of around 400-500MB so that two simultaneous reducers can
	 *            create partial vectors without thrashing the system due to
	 *            increased swapping
	 * @param normPower
	 *            The normalization value. Must be greater than or equal to 0 or
	 *            equal to {@link #NO_NORMALIZING}
	 * @param posTagging
	 *            part of speech tagging (0=off, 1=before, 2=after)
	 * @throws ClassNotFoundException
	 */
	public VectorConfiguration(Class analyzerClass, int minSupport, int minDf,
			int maxDfPercent, int maxNGramSize, int minLLRValue,
			int reduceTasks, int chunkSize, int normPower, int posTagging) throws Exception {
		this();
		this.analyzerClass = analyzerClass;
		this.minSupport = minSupport;
		this.minDf = minDf;
		this.maxDfPercent = maxDfPercent;
		this.maxNGramSize = maxNGramSize;
		this.minLLRValue = minLLRValue;
		this.reduceTasks = reduceTasks;
		this.chunkSize = chunkSize;
		this.normPower = normPower;
		this.posTagging = posTagging;
	}
	
	public VectorConfiguration(JsonNode configuration) throws Exception {
		this();
		if(configuration.has("analyzerClass")) {
			String ac = configuration.get("analyzerClass").asText();
			analyzerClass =  Class.forName(ac);
		}
		
		if(configuration.has("minSupport")) {
			minSupport = configuration.get("minSupport").asInt();
		}
		
		if(configuration.has("minDf")) {
			minDf = configuration.get("minDf").asInt();
		}
		
		if(configuration.has("maxDfPercent")) {
			maxDfPercent = configuration.get("maxDfPercent").asInt();
		}
		
		if(configuration.has("maxNGramSize")) {
			maxNGramSize = configuration.get("maxNGramSize").asInt();
		}
		
		if(configuration.has("minLLRValue")) {
			minLLRValue = configuration.get("minLLRValue").asInt();
		}	
		
		if(configuration.has("reduceTasks")) {
			reduceTasks = configuration.get("reduceTasks").asInt();
		}	
		
		if(configuration.has("chunkSize")) {
			chunkSize = configuration.get("chunkSize").asInt();
		}	
		
		if(configuration.has("normPower")) {
			chunkSize = configuration.get("normPower").asInt();
		}	
		
		if(configuration.has("posTagging")) {
			posTagging = configuration.get("posTagging").asInt();
		}			
		
		if(configuration.has("filefilter.languages")) {
			this.fileFilterLanguages = configuration.get("filefilter.languages").asText().toLowerCase();
		}			
		if(configuration.has("filefilter.spacetypes")) {
			this.fileFilterSpacetypes = configuration.get("filefilter.spacetypes").asText().toLowerCase();
		}			
		if(configuration.has("filefilter.spacestatus")) {
			this.fileFilterSpacestatus = configuration.get("filefilter.spacestatus").asText().toLowerCase();
		}			
		if(configuration.has("filefilter.spacecategories")) {
			this.fileFilterSpacecategories = configuration.get("filefilter.spacecategories").asText().toLowerCase();
		}			
		if(configuration.has("filefilter.pagestatus")) {
			this.fileFilterPageStatus = configuration.get("filefilter.pagestatus").asText().toLowerCase();
		}			
		if(configuration.has("filefilter.mincontentlength")) {
			this.fileFilterMinContentLength = configuration.get("filefilter.mincontentlength").asInt();
		}			
		
	}	

	public Class getAnalyzerClass() {
		return analyzerClass;
	}

	public int getMinSupport() {
		return minSupport;
	}

	public int getMinDf() {
		return minDf;
	}

	public int getMaxDfPercent() {
		return maxDfPercent;
	}

	public int getMaxNGramSize() {
		return maxNGramSize;
	}

	public int getMinLLRValue() {
		return minLLRValue;
	}

	public int getReduceTasks() {
		return reduceTasks;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public int getNormPower() {
		return normPower;
	}
	
	public int getPosTagging() {
		return posTagging;
	}
	
	public String getFileFilterLanguages() {
		return fileFilterLanguages;
	}

	public void setFileFilterLanguages(String fileFilterLanguages) {
		this.fileFilterLanguages = fileFilterLanguages;
	}

	public String getFileFilterSpacetypes() {
		return fileFilterSpacetypes;
	}

	public void setFileFilterSpacetypes(String fileFilterSpacetypes) {
		this.fileFilterSpacetypes = fileFilterSpacetypes;
	}

	public String getFileFilterSpacestatus() {
		return fileFilterSpacestatus;
	}

	public void setFileFilterSpacestatus(String fileFilterSpacestatus) {
		this.fileFilterSpacestatus = fileFilterSpacestatus;
	}

	public String getFileFilterSpacecategories() {
		return fileFilterSpacecategories;
	}

	public void setFileFilterSpacecategories(String fileFilterSpacecategories) {
		this.fileFilterSpacecategories = fileFilterSpacecategories;
	}

	public String getFileFilterPageStatus() {
		return fileFilterPageStatus;
	}

	public void setFileFilterPageStatus(String fileFilterPageStatus) {
		this.fileFilterPageStatus = fileFilterPageStatus;
	}

	public int getFileFilterMinContentLength() {
		return fileFilterMinContentLength;
	}

	public void setFileFilterMinContentLength(int fileFilterMinContentLength) {
		this.fileFilterMinContentLength = fileFilterMinContentLength;
	}

	public boolean isWikiCache() {
		return wikiCache;
	}

	public void setWikiCache(boolean wikiCache) {
		this.wikiCache = wikiCache;
	}

	public boolean isVectorizationCache() {
		return vectorizationCache;
	}

	public void setVectorizationCache(boolean vectorizationCache) {
		this.vectorizationCache = vectorizationCache;
	}
}
