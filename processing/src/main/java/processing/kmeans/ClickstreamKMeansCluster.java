package processing.kmeans;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.evaluation.RepresentativePointsDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.DocumentProcessor;
import org.apache.mahout.vectorizer.tfidf.TFIDFConverter;

/**
 * pipeline of commands to cluster files in a given path.
 * 
 * @author sven.lenz@msc.htwchur.ch
 * @see: http://sujitpal.blogspot.ch/2012/09/learning-mahout-clustering.html
 */
public class ClickstreamKMeansCluster {

	public static boolean USE_WINDOWS = true;
	public static String BASE_PATH_WINDOWS = "C:\\Users\\slenz\\switchdrive\\Master\\survey_results\\";
	public static String BASE_PATH_IOS = "/Users/sle/switchdrive/Master/survey_results/";
	
//	private static String PATH_TO_SHARED_VOLUME = "/Users/sle/switchdrive/Master/survey_results/kmeans";
	
	private static String PATH_TO_SHARED_VOLUME = USE_WINDOWS ? BASE_PATH_WINDOWS + "kmeans" : BASE_PATH_IOS + "kmeans";
	// extracted data path
	private static String WIKI_FILES_BASE_PATH = "/Users/sle/switchdrive/Master/survey_results/kmeans";

	// latest extracted data path
	public static String EXTRACTED_WIKI_FILES_BASE_PATH = WIKI_FILES_BASE_PATH + "/extracted";

	// copied data from (don't change latest)
	// from latest to parsed
	private static String CURRENT_WIKI_FILES_BASE_PATH;

	// parsed files
	private static String PARSED_WIKI_FILES_BASE_PATH;
	
	private static Configuration conf = new Configuration();

	// static initializer to correctly set paths for docker/eclipse
	static {
		EXTRACTED_WIKI_FILES_BASE_PATH = WIKI_FILES_BASE_PATH + "/latest";
		CURRENT_WIKI_FILES_BASE_PATH = WIKI_FILES_BASE_PATH + "/current";
		PARSED_WIKI_FILES_BASE_PATH = WIKI_FILES_BASE_PATH
				+ "/parsed/moreFiles";

	}


	public SimpleClusterOutput simpleClusterOutput;
	
	public ArrayList<Pair<Double, Double>> densities;

	public static void main(String[] args) throws Exception {
		VectorConfiguration vectorConfiguration = new VectorConfiguration();
		ClusteringConfiguration clusteringConfiguration = new ClusteringConfiguration();
		
		for (int id = 1; id <= 101; id++) {
			String events = (USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\" : BASE_PATH_IOS + "/" + id + "/")
					+ "events.json";
			File source = new File((USE_WINDOWS ? BASE_PATH_WINDOWS + "\\" + id + "\\events.json" : BASE_PATH_IOS + "/" + id + "/events.json"));
			File target = new File((USE_WINDOWS ? BASE_PATH_WINDOWS + "\\kmeans\\latest\\" + id +".json" : BASE_PATH_IOS + "/kmeans/latest/" + id + ".json"));
			FileUtils.copyFile(source, target);
		}
		
		ClickstreamKMeansCluster clustering = new ClickstreamKMeansCluster(
				vectorConfiguration, clusteringConfiguration,
				new ClusterLogConsole());
		// KMeansCluster clustering = new
		// KMeansCluster(WikiFileParserStrategy.DIFF_PER_REVISION_STRATEGY,
		// vectorConfiguration, clusteringConfiguration, new
		// ClusterLogConsole());
		clustering.run();
	}

	private VectorConfiguration vectorConfiguration;
	private ClusteringConfiguration clusteringConfiguration;
	private ClusterLog logging;

	public ClickstreamKMeansCluster(VectorConfiguration vectorConfiguration,
			ClusteringConfiguration clusteringConfiguration, ClusterLog logging)
			throws Exception {
		
		this.vectorConfiguration = vectorConfiguration;
		this.clusteringConfiguration = clusteringConfiguration;
		this.logging = logging;
	}

	public void run() throws IOException, Exception, ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException,
			InterruptedException {
		logging.log("start kmeans clustering");

		java.nio.file.Path extracedWikiPath = Paths
				.get(EXTRACTED_WIKI_FILES_BASE_PATH);
		java.nio.file.Path currentWikiPath = Paths
				.get(CURRENT_WIKI_FILES_BASE_PATH);
		java.nio.file.Path parsedWikiPath = Paths
				.get(PARSED_WIKI_FILES_BASE_PATH);


		String filePathToWorkWith = extracedWikiPath.toString();
		String inputDir = PATH_TO_SHARED_VOLUME
				+ "/clustering/sequenced"; // directory of doc
															// sequence file(s)
		String outputDir = PATH_TO_SHARED_VOLUME + "/clustering"; // directory
																				// where
																				// clusters
																				// will
																				// be
																				// written

		doVectorization(filePathToWorkWith, inputDir, outputDir, conf);

		logging.log("start clustering");
		densities = new ArrayList<Pair<Double, Double>>();
		if(clusteringConfiguration.isEvaluateClusters()) {
			logging.log("evaluate cluster mode (looping from 1 to K)");
			for(int i = 1; i <= clusteringConfiguration.getK(); i++) {	
				logging.log("---------------------");
				doClustering(outputDir, i, densities);
			}
			
			String intraCluster = "";
			String interCluster = "";
			for(Pair<Double, Double> pair : densities) {
				interCluster += pair.getFirst() + ",";
				intraCluster += pair.getSecond() + ",";
			}
			logging.log("interCluster " + interCluster);	
			logging.log("intraCluster " + intraCluster);
		} else {
			logging.log("cluster mode (generate K clusters)");
			doClustering(outputDir, clusteringConfiguration.getK(), densities);
		}


		logging.log("collecting output");
		boolean posTagging = false;
		simpleClusterOutput = new SimpleClusterOutput(PATH_TO_SHARED_VOLUME);
		logging.log("collecting done");

		System.out.println(simpleClusterOutput.getResult());
	}

	private void doClustering(String outputDir, int i, ArrayList<Pair<Double, Double>> density) throws IOException,
			InterruptedException, ClassNotFoundException {
		// reads tfidf-vectors from output_dir/tfidf-vectors
		// and writes out random centroids at output_dir/centroids
		Path tfidfVectorPath = new Path(outputDir, "tfidf-vectors");
		Path centroidPaths = new Path(outputDir, "centroids");
		Path clusterPath = new Path(outputDir, "clusters");
		//clean up old data
		HadoopUtil.delete(conf, centroidPaths);
		HadoopUtil.delete(conf, clusterPath);
		
		// reads tfidf-vectors from output_dir/tfidf-vectors and
		// refers to directory path for initial clusters, and
		// writes out clusters to output_dir/clusters
		logging.log("generate " + i + " random centroids with distance measurement " + clusteringConfiguration.getDistanceMeasure());
		RandomSeedGenerator.buildRandom(conf, tfidfVectorPath, centroidPaths,
				i,
				clusteringConfiguration.getDistanceMeasure());

		// reads tfidf-vectors from output_dir/tfidf-vectors and
		// refers to directory path for initial clusters, and
		// writes out clusters to output_dir/clusters
		KMeansDriver.run(conf, tfidfVectorPath, centroidPaths, clusterPath,
				clusteringConfiguration.getConvergenceDelta(),
				clusteringConfiguration.getMaxIterations(), true,
				clusteringConfiguration.getClusterClassificationThreshold(),
				false);

		logging.log("clustering done");

		evaluateClusters(clusterPath, density);
	}

	
	private void doVectorization(String filePathToWorkWith, String inputDir,
			String outputDir, Configuration conf) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, InterruptedException {
		logging.log("start sequencing");
		SequenceConfiguration sequenceConfiguration = new SequenceConfiguration(
				filePathToWorkWith, PATH_TO_SHARED_VOLUME
						+ "/clustering/sequenced");

		HadoopUtil.delete(conf, new Path(outputDir));

		SequenceFilesFromDirectory seqD = new SequenceFilesFromDirectory();
		seqD.run(sequenceConfiguration.getArguments());
		logging.log("sequencing done");

		logging.log("start tokenizing");
		// converts input docs in sequence file format in input_dir
		// into token array in output_dir/tokenized-documents
		Path inputPath = new Path(inputDir);
		Path tokenizedDocPath = new Path(outputDir,
				DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER);
		Class c = vectorConfiguration.getAnalyzerClass();
		logging.log("use analyzer " + c);
		DocumentProcessor.tokenizeDocuments(inputPath, c, tokenizedDocPath,
				conf);
		logging.log("tokenizing done");

		logging.log("start creating vectors");
		// reads token array in output_dir/tokenized-documents and
		// writes term frequency vectors in output_dir (under tf-vectors)
		DictionaryVectorizer.createTermFrequencyVectors(tokenizedDocPath,
				new Path(outputDir),
				DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER, conf,
				vectorConfiguration.getMinSupport(),
				vectorConfiguration.getMaxNGramSize(),
				vectorConfiguration.getMinLLRValue(), -1.0f,
				false, // never ever change norm here (it won't generate
						// TFIDF-Vectors afterwards)
				vectorConfiguration.getReduceTasks(),
				vectorConfiguration.getChunkSize(), true, false);

		// converts term frequency vectors in output_dir/tf-vectors
		// to TF-IDF vectors in output_dir (under tfidf-vectors)
		Path tfVectorPath = new Path(outputDir,
				DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER);
		Path outputPath = new Path(outputDir);
		Pair<Long[], List<Path>> docFreqs = TFIDFConverter.calculateDF(
				tfVectorPath, outputPath, conf,
				vectorConfiguration.getChunkSize());
		TFIDFConverter.processTfIdf(tfVectorPath, outputPath, conf, docFreqs,
				vectorConfiguration.getMinDf(),
				vectorConfiguration.getMaxDfPercent(),
				vectorConfiguration.getNormPower(), true, true, false,
				vectorConfiguration.getReduceTasks());
		logging.log("creating vectors done");
	}
	
	/**
	 * goal: low intra-cluster distances (high intra-cluster similarity) and high inter-cluster distances (low inter-cluster similarity)
	 * @param clusterPath
	 */
	private void evaluateClusters(Path clusterPath, ArrayList<Pair<Double, Double>> density) {
		try {
			String finalFolder = null;
			File[] files = new File(clusterPath.toString()).listFiles();
		    for (File file : files) {
		        if (file.isDirectory()) {
		            if(file.getName().contains("final")) {
		            	finalFolder = file.getName();
		            }
		        }
		    }
			
			Path pClusters = new Path(clusterPath, finalFolder);
			Path pClusteredPoints = new Path(clusterPath, "clusteredPoints");
			Path pOutput = new Path(clusterPath, "representativePoints");
			RepresentativePointsDriver.run(conf, pClusters, pClusteredPoints,
					pOutput, new EuclideanDistanceMeasure(),
					clusteringConfiguration.getMaxIterations(), true);
//
//			ClusterEvaluator cv = new ClusterEvaluator(conf, pClusters);
//			logging.log("Inter cluster density: " + cv.interClusterDensity());
//			logging.log("Intra cluster density: " + cv.intraClusterDensity());
//			density.add(new Pair(cv.interClusterDensity(), cv.intraClusterDensity()));
		} catch (Exception ex) {
			logging.log("Exception");
			logging.log(" - " + ex.getMessage());
			StackTraceElement[] le = ex.getStackTrace();
			for (int i = 0; i < le.length; i++) {
				logging.log(" - " + le[i].toString());
			}
		}
	}
}
