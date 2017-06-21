package processing.kmeans;

import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;
import org.codehaus.jackson.JsonNode;

/**
 * configuration for kmeans clustering.
 * can either be used with default settings (default constructor) or with custom settings.
 * 
 * TODO: reference why we choose this default settings.  
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class ClusteringConfiguration {

	private DistanceMeasure distanceMeasure;
	private int k;
	private double convergenceDelta;
	private int maxIterations;
	private double clusterClassificationThreshold;
	private boolean evaluateClusters = false;
	
	/**
	 * default configuration with the values:
	 *
	 *<ul>
	 * 	<li>distanceMeasure = TanimotoDistanceMeasure;</li>
	 * 	<li> k = 50 </li>
	 * 	<li> convergenceDelta = 0.01 </li>
	 * 	<li> maxIterations = 10 </li>
	 *  <li> clusterClassificationThreshold = 0.01 </li>
	 * </ul>
	 */
	public ClusteringConfiguration() {
		distanceMeasure = new EuclideanDistanceMeasure();
		//https://www.youtube.com/watch?v=g7SdhkdGTyo
		k = 3;
		convergenceDelta = 0.5;
		maxIterations = 100;
		clusterClassificationThreshold = 0.2;
	}
	
	/**
	 * parse from json & overwrite defaults (@see default constructor). 
	 * note: distanceMeasure has to be a FQDN class name.
	 *  
	 * @param configuration
	 * @throws Exception
	 */
	public ClusteringConfiguration(JsonNode configuration) throws Exception {
		this();
		if(configuration.has("distanceMeasure")) {
			String dm = configuration.get("distanceMeasure").asText();
			Class<DistanceMeasure> c =  (Class<DistanceMeasure>) Class.forName(dm);
			distanceMeasure = c.newInstance();
		}
		
		if(configuration.has("k")) {
			k = configuration.get("k").asInt();
		}
		
		if(configuration.has("convergenceDelta")) {
			convergenceDelta = configuration.get("convergenceDelta").asDouble();
		}
		
		if(configuration.has("maxIterations")) {
			maxIterations = configuration.get("maxIterations").asInt();
		}		
		
		if(configuration.has("clusterClassificationThreshold")) {
			clusterClassificationThreshold = configuration.get("clusterClassificationThreshold").asDouble();
		}		
		
		if(configuration.has("evaluateClusters")) {
			evaluateClusters = configuration.get("evaluateClusters").asBoolean();
		}	
	}
	

	/**
	 * custom configuration
	 *
	 * @param distanceMeasure
	 *          distance measurement
	 * @param k
	 *          number of clusters           
	 * @param convergenceDelta
	 *          the convergence delta value
	 * @param maxIterations
	 *          the maximum number of iterations
	 * @param clusterClassificationThreshold
	 *          Is a clustering strictness / outlier removal parameter. Its value should be between 0 and 1. Vectors
	 *          having pdf below this value will not be clustered.
	 */
	public ClusteringConfiguration(DistanceMeasure distanceMeasure, int k,
			double convergenceDelta, int maxIterations,
			double clusterClassificationThreshold) {
		super();
		this.distanceMeasure = distanceMeasure;
		this.k = k;
		this.convergenceDelta = convergenceDelta;
		this.maxIterations = maxIterations;
		this.clusterClassificationThreshold = clusterClassificationThreshold;
	}


	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}


	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}


	public int getK() {
		return k;
	}


	public void setK(int k) {
		this.k = k;
	}

	public double getConvergenceDelta() {
		return convergenceDelta;
	}

	public void setConvergenceDelta(double convergenceDelta) {
		this.convergenceDelta = convergenceDelta;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getClusterClassificationThreshold() {
		return clusterClassificationThreshold;
	}

	public void setClusterClassificationThreshold(
			double clusterClassificationThreshold) {
		this.clusterClassificationThreshold = clusterClassificationThreshold;
	}

	public boolean isEvaluateClusters() {
		return evaluateClusters;
	}

	public void setEvaluateClusters(boolean evaluateClusters) {
		this.evaluateClusters = evaluateClusters;
	}
}
