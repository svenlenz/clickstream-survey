package processing.kmeans.mahout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.utils.vectors.VectorHelper;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import processing.utils.Big5Result;
import processing.utils.ClusterEvaluator;
import processing.utils.StatDump;

/**
 * Dump Cluster Output
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class SimpleClusterOutput {

	private final String PATH_TO_SHARED_VOLUME;

	public StringBuilder simpleOutput;

	public HashMap<String, ClusterDescriptor> clusterDescriptorIndex;
	
	//public static MaxentTagger tagger = new MaxentTagger("english-left3words-distsim.tagger");
	
	public boolean posTagging = false;

	public SimpleClusterOutput(String pathToSharedVolume) throws Exception {
//			Map<String, String> filenameToUserMapping) throws Exception {
		// TODO Auto-generated method stub
		PATH_TO_SHARED_VOLUME = pathToSharedVolume;
		posTagging = posTagging;
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		try {
			BufferedWriter bw;
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(conf);
			SequenceFile.Reader reader = new SequenceFile.Reader(
					fs,
					new Path(
							PATH_TO_SHARED_VOLUME
									+ "/clustering/clusters/clusteredPoints/part-m-00000"),
					conf);

			IntWritable key = new IntWritable();
			WeightedPropertyVectorWritable value = new WeightedPropertyVectorWritable();

			clusterDescriptorIndex = new HashMap<String, ClusterDescriptor>();
			HashMap<String, ArrayList<Integer>> clusterFilenameIndex = new HashMap<String, ArrayList<Integer>>();

			while (reader.next(key, value)) {
				// new:
				ClusterDescriptor clusterDescriptor;
				String clusterId = key.toString();
				if (clusterDescriptorIndex.containsKey(clusterId)) {
					clusterDescriptor = clusterDescriptorIndex.get(clusterId);
				} else {
					clusterDescriptor = new ClusterDescriptor(clusterId);
					clusterDescriptorIndex.put(clusterId, clusterDescriptor);
				}

				String filename = value.getVector().asFormatString()
						.replaceAll(":\\{.*\\}", "").replace("/", "");
				
				int id = Integer.valueOf(filename.substring(0, filename.indexOf(".")));
				if (clusterFilenameIndex.containsKey(clusterId)) {
					clusterFilenameIndex.get(clusterId).add(id);
				} else {
					ArrayList a = new ArrayList<Integer>();
					a.add(id);
					clusterFilenameIndex.put(clusterId, a);
				}

				
				// ***************************************** CHANGED BY HEUSI *************************************
				// *********************** MUSS NOCH ANGEPASST WERDEN *********************************************
				// TODO Zeile hinzugef�gt durch Heusi
//				String username = res.getGeneratedFile(filename).getContributors().next();
				// TODO Zeile auskommentiert durch Heusi
//				String username = filenameToUserMapping.get(filename);
//				clusterDescriptor.addUserDocument(username, filename);
				// ***************************************** ENDE CHANGED BY HEUSI ********************************

				// get title for document!
//				String originalFilename = res.getGeneratedFile(filename).getSourceFiles().next();
//				if (originalFilename.startsWith("content_")) {
//					String title = extractTitle(dBuilder, clusterDescriptor,
//							originalFilename);
//					clusterDescriptor.documentTitles.add(title);
//				}
			}
			reader.close();
			
			Set<String> clusterkeys = clusterFilenameIndex.keySet();
			System.out.println("--------------------------------");
			clusterkeys.forEach(k -> {
				System.out.println(clusterFilenameIndex.get(k));
			});
			System.out.println("--------------------------------");
			System.out.println(Big5Result.csvHeader());
			StringBuffer sb = new StringBuffer();
			clusterkeys.forEach(k -> {
//				System.out.println("cluster " + k + " " + clusterFilenameIndex.get(k));
				ClusterEvaluator.meanResultValues(clusterFilenameIndex.get(k));
				
				
				List<Integer> clusterOne = new ArrayList<Integer>();
				List<Integer> clusterTwo = new ArrayList<Integer>();
				List<Integer> clusterThree = new ArrayList<Integer>();
				List<Integer> clusterFour = new ArrayList<Integer>();
				List<Integer> clusterFive = new ArrayList<Integer>();

				clusterFilenameIndex.get(k).forEach(id -> {
					Big5Result b5result = StatDump.toBig5Result(id);
					int[] profilePoints = {b5result.meanNeuro, b5result.meanExtra, b5result.meanGewissen, b5result.meanOffen, b5result.meanVertrag};
					StatDump.calculateNDistance(id, profilePoints, clusterOne, clusterTwo, clusterThree, clusterFour, clusterFive, 5);
				});
				sb.append("cl all " + clusterFilenameIndex.get(k).size() + " cl1 " + clusterOne.size() + " cl2 " + clusterTwo.size() + " cl3 " + clusterThree.size());
				sb.append("\n");
				sb.append("---> cluster one " + (int)(new Double(100)/clusterFilenameIndex.get(k).size()*clusterOne.size()));
				sb.append("\n");
				sb.append("---> cluster two " + (int)(new Double(100)/clusterFilenameIndex.get(k).size()*clusterTwo.size()));
				sb.append("\n");
				sb.append("---> cluster three " + (int)(new Double(100)/clusterFilenameIndex.get(k).size()*clusterThree.size()));
				sb.append("\n");
				sb.append("---> cluster four " + (int)(new Double(100)/clusterFilenameIndex.get(k).size()*clusterFour.size()));
				sb.append("\n");
				sb.append("---> cluster five " + (int)(new Double(100)/clusterFilenameIndex.get(k).size()*clusterFive.size()));
				sb.append("\n");
			});
			System.out.println(sb);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StringBuilder getResult() {
		return simpleOutput;
	}

	/**
	 * for display in user interface
	 * @return
	 */
	public List<JsonNode> getClustersAsJson() {

		ArrayList<JsonNode> clusterList = new ArrayList<JsonNode>();
		clusterDescriptorIndex.forEach((clusterDescriptorKey,
				clusterDescriptorValue) -> {

			ObjectMapper mapper = new ObjectMapper();
			ObjectNode cluster = JsonNodeFactory.instance.objectNode();
			cluster.put("clusterid", clusterDescriptorValue.clusterId);
			if(clusterDescriptorValue.clusterName != null && !"".equals(clusterDescriptorValue.clusterName)) {
				cluster.put("clustername",
						clusterDescriptorValue.clusterName);
			}
			ArrayList<String> terms = new ArrayList<String>();
			clusterDescriptorValue.topTerms.forEach(termPair -> {
				String term = termPair.getFirst();
				terms.add(term);
			});
			ArrayNode termsArray = mapper.valueToTree(terms);
			cluster.put("terms", termsArray);

			ArrayNode usersArray = JsonNodeFactory.instance.arrayNode();
			clusterDescriptorValue.userDocumentIndex.forEach((userKey,
					userValue) -> {
				ObjectNode user = JsonNodeFactory.instance.objectNode();
				user.put("username", userValue.userId);
				ArrayNode documentArray = mapper
						.valueToTree(userValue.documentIds);
				user.put("documents", documentArray);
				usersArray.add(user);
			});
			cluster.put("users", usersArray);

			clusterList.add(cluster);
		});
		return clusterList;
	}

	/**
	 * for elastic index
	 * @return
	 */
	public List<JsonNode> getFlatClustersAsJson() {
		ArrayList<JsonNode> documentList = new ArrayList<JsonNode>();
		clusterDescriptorIndex
				.forEach((clusterDescriptorKey, clusterDescriptorValue) -> {

					ObjectMapper mapper = new ObjectMapper();

					ArrayList<String> terms = new ArrayList<String>();
					clusterDescriptorValue.topTerms.forEach(termPair -> {
						String term = termPair.getFirst();
						terms.add(term);
					});
					ArrayNode termsArray = mapper.valueToTree(terms);

					ArrayNode usersArray = JsonNodeFactory.instance.arrayNode();
					clusterDescriptorValue.userDocumentIndex.forEach((userKey,
							userValue) -> {
						ObjectNode document = JsonNodeFactory.instance
								.objectNode();
						document.put("clusterid",
								clusterDescriptorValue.clusterId);
						if(clusterDescriptorValue.clusterName != null && !"".equals(clusterDescriptorValue.clusterName)) {
							document.put("clustername",
									clusterDescriptorValue.clusterName);	
						}
						document.put("terms", termsArray);

						ObjectNode user = JsonNodeFactory.instance.objectNode();
						document.put("username", userValue.userId);
						ArrayNode documentArray = mapper
								.valueToTree(userValue.documentIds);
						document.put("documents", documentArray);
						documentList.add(document);
					});
				});
		return documentList;
		// http://stackoverflow.com/questions/20646836/is-there-any-way-to-import-a-json-filecontains-100-documents-in-elasticsearch
	}

	public static File[] listFilesMatching(File root, String regex) {
		if (!root.isDirectory()) {
			throw new IllegalArgumentException(root + " is no directory.");
		}
		final Pattern p = Pattern.compile(regex); // careful: could also throw
													// an exception!
		return root.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return p.matcher(file.getName()).matches();
			}
		});
	}

	private StringBuilder createClusterDescriptorIndex(HashMap<String, ClusterDescriptor> clusterDescriptorIndex, boolean detailed) {
		
		
		
		simpleOutput = new StringBuilder();

		simpleOutput.append("clustersize " + clusterDescriptorIndex.size());
		simpleOutput.append("\n");
		for (Entry<String, ClusterDescriptor> entry : clusterDescriptorIndex
				.entrySet()) {
			simpleOutput.append("\n");
			simpleOutput.append("cluster ID " + entry.getKey());
			ClusterDescriptor clusterDescriptor = entry.getValue();
			
			simpleOutput.append("\n\t");
			simpleOutput.append("top terms");
			simpleOutput.append("\n\t\t");
			
			if(detailed) {				
				for (Pair<String, Double> item : clusterDescriptor.topTerms) {
					String term = item.getFirst();
					simpleOutput.append("\n\t\t");
					simpleOutput.append(StringUtils.rightPad(term, 40));
					simpleOutput.append("=>");
					simpleOutput.append(StringUtils.leftPad(item.getSecond()
							.toString(), 20));
				}

				simpleOutput.append("\n\t");
				for (Entry<String, UserDocuments> userEntry : clusterDescriptor.userDocumentIndex
						.entrySet()) {
					String userId = userEntry.getKey();
					simpleOutput.append("\n\t-\n");
					simpleOutput.append("\n\t");
					simpleOutput.append("user: " + userId);
					simpleOutput.append("\n\t");
					simpleOutput.append("number of documents: "
							+ userEntry.getValue().documentIds.size());
					simpleOutput.append("\n\t");
					simpleOutput.append("documents: ");
					for (String document : userEntry.getValue().documentIds) {
						simpleOutput.append("\n\t\t");
						simpleOutput.append(document);
					}
				}								
			} else {			

				for (Pair<String, Double> item : clusterDescriptor.topTerms) {
					String term = item.getFirst();
					if(posTagging) {
//						String posTagged = tagger.tagString(term);  
//				          if(posTagged.indexOf("_") > 0) {
//				        	  String[] parts = posTagged.split("_");
//				        	  if(parts[parts.length-1].startsWith("N")) {
//				        		  simpleOutput.append(term);
//				  				simpleOutput.append(", ");
//				        	  }
//				          }		
					} else {
		        		simpleOutput.append(term);
		  				simpleOutput.append(", ");
					}
					
				}
	
				simpleOutput.append("\n\t");
				simpleOutput.append("users:\n\t");
				for (Entry<String, UserDocuments> userEntry : clusterDescriptor.userDocumentIndex
						.entrySet()) {
					String userId = userEntry.getKey();				
					simpleOutput.append(userId + ", ");
				}
			}
			simpleOutput.append("\n");
			simpleOutput.append("\n");
		}

		return simpleOutput;
	}

	private static class ClusterDescriptor {
		String clusterId;
		String clusterName;
		ArrayList<String> documentTitles = new ArrayList<String>();
		ArrayList<String> relevantTitles = new ArrayList<String>();
		Map<String, UserDocuments> userDocumentIndex;
		Collection<Pair<String, Double>> topTerms;
		
		ClusterDescriptor(String clusterId) {
			this.clusterId = clusterId;
			userDocumentIndex = new HashMap<String, UserDocuments>();
		}

		public void addUserDocument(String userId, String userDocumentId) {
			UserDocuments userDocuments = null;
			if (userDocumentIndex.containsKey(userId)) {
				userDocuments = userDocumentIndex.get(userId);
			} else {
				userDocuments = new UserDocuments(userId);
				userDocumentIndex.put(userId, userDocuments);
			}
			userDocuments.addDocument(userDocumentId);
		}

		public void addTopTerms(Collection<Pair<String, Double>> topTerms, boolean filterNouns) {
			
			if(filterNouns) {
//			Collection<Pair<String, Double>> nounTerms = topTerms.stream()
//				    .filter(p -> tagger.tagString(p.getFirst()).contains("_N")).collect(Collectors.toList());
//				this.topTerms = nounTerms;
			} else {
				this.topTerms = topTerms;	
			}
		}

		public String getClusterId() {
			return clusterId;
		}

		public String getClusterName() {
			return clusterName;
		}

		public Map<String, UserDocuments> getUserDocumentIndex() {
			return userDocumentIndex;
		}

		public Collection<Pair<String, Double>> getTopTerms() {
			return topTerms;
		}
	}

	private static class UserDocuments {
		String userId;
		List<String> documentIds;

		UserDocuments(String userId) {
			this.userId = userId;
			documentIds = new ArrayList<String>();
		}

		public void addDocument(String document) {
			documentIds.add(document);
		}
	}

	private static class TermIndexWeight {
		private final int index;
		private final double weight;

		TermIndexWeight(int index, double weight) {
			this.index = index;
			this.weight = weight;
		}
	}

	public static Collection<Pair<String, Double>> getTopFeatures(
			Vector vector, String[] dictionary, int numTerms) {

		List<TermIndexWeight> vectorTerms = new ArrayList<TermIndexWeight>();
		for (Element elt : vector.all()) {
			vectorTerms.add(new TermIndexWeight(elt.index(), elt.get()));
		}

		// Sort results in reverse order (ie weight in descending order)
		Collections.sort(vectorTerms, new Comparator<TermIndexWeight>() {
			public int compare(TermIndexWeight one, TermIndexWeight two) {
				return Double.compare(two.weight, one.weight);
			}
		});

		Collection<Pair<String, Double>> topTerms = new LinkedList<Pair<String, Double>>();
		for (int i = 0; i < vectorTerms.size() && i < numTerms; i++) {
			int index = vectorTerms.get(i).index;
			String dictTerm = dictionary[index];
			if (dictTerm == null) {
				System.out.println("Dictionary entry missing for " + index);
				continue;
			}
			//TODO: parameter...
			if(dictTerm.contains("_N")) {
				dictTerm = dictTerm.substring(0, dictTerm.indexOf("_N"));
			}
				
			topTerms.add(new Pair<String, Double>(dictTerm,
					vectorTerms.get(i).weight));
		}

		return topTerms;
	}

}
