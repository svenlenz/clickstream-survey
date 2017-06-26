package processing.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareToPrototypes {
	
	//lenz
	private static int[] prototype3_1 = new int[]{1, 2, 5, 6, 7, 8, 10, 13, 15, 16, 19, 24, 26, 29, 30, 32, 36, 43, 44, 46, 49, 50, 54, 57, 58, 60, 61, 62, 63, 64, 66, 67, 68, 69, 71, 77, 78, 81, 82, 88, 91, 92, 93, 94, 95, 96, 97, 100, 101, 102, 103, 106, 107, 110, 111, 113, 114, 115, 116, 117, 118, 120, 124};
	private static int[] prototype3_2 = new int[]{12, 20, 21, 22, 23, 25, 27, 35, 39, 41, 45, 47, 48, 55, 59, 72, 73, 79, 83, 84, 85, 89, 105, 108, 109, 112, 122};
	private static int[] prototype3_3 = new int[]{3, 4, 9, 11, 14, 17, 18, 28, 31, 33, 34, 37, 38, 40, 42, 51, 52, 53, 56, 65, 70, 74, 75, 76, 80, 86, 87, 90, 98, 99, 104, 119, 121, 123, 125, 126};
	
	//lenz clickers >=30
//	private static int[] prototype3_1 = new int[]{1, 8, 14, 20, 24, 26, 34, 39, 42, 46, 47};
//	private static int[] prototype3_2 = new int[]{2, 4, 11, 12, 13, 15, 16, 17, 21, 30, 36, 38, 40, 41, 43, 44, 5};
//	private static int[] prototype3_3 = new int[]{3, 5, 6, 7, 9, 10, 18, 19, 22, 23, 25, 27, 28, 29, 31, 32, 33, 35, 37, 45, 48, 49, 51, 52};
//	
	
	
	//lenz clickers
//	private static int[] prototype3_1 = new int[]{1, 2, 5, 6, 7, 8, 10, 13, 15, 16, 19, 24, 26, 29, 30, 32, 36, 43, 44, 46, 49, 50};
//	private static int[] prototype3_2 = new int[]{12, 20, 21, 22, 23, 25, 27, 35, 39, 41, 45, 47, 48};
//	private static int[] prototype3_3 = new int[]{3, 4, 9, 11, 14, 17, 18, 28, 31, 33, 34, 37, 38, 40, 42, 51, 52};
	
	//asendorf
//	private static int[] prototype3_1 = new int[]{1, 10, 12, 20, 26, 32, 41, 46, 50, 54, 59, 62, 63, 81, 92, 93, 102, 110, 111, 112, 117, 120};
//	private static int[] prototype3_2 = new int[]{2, 4, 15, 16, 19, 21, 22, 23, 27, 29, 30, 35, 36, 38, 39, 43, 44, 45, 47, 57, 61, 64, 66, 67, 69, 71, 72, 78, 82, 84, 85, 89, 90, 91, 94, 96, 97, 101, 103, 105, 106, 113, 114, 118, 124};
//	private static int[] prototype3_3 = new int[]{3, 5, 6, 7, 8, 9, 11, 13, 14, 17, 18, 24, 25, 28, 31, 33, 34, 37, 40, 42, 48, 49, 51, 52, 53, 55, 56, 58, 60, 65, 68, 70, 73, 74, 75, 76, 77, 79, 80, 83, 86, 87, 88, 95, 98, 99, 100, 104, 107, 108, 109, 115, 116, 119, 121, 122, 123, 125, 126};
	static ArrayList<int[]> prototype3 =  new ArrayList<int[]>();

					
	private static int[] dch3_1 = new int[]{1, 2, 3, 4, 5, 6, 8, 9, 11, 12, 13, 14, 15, 17, 20, 21, 22, 25, 26, 27, 31, 32, 37, 38, 40, 41, 47, 49, 51, 52};
	private static int[] dch3_2 = new int[]{18, 30};
	private static int[] dch3_3 = new int[]{7, 10, 16, 19, 23, 24, 28, 29, 33, 34, 35, 36, 39, 42, 43, 44, 45, 46, 48, 50};

	static ArrayList<int[]> dch3 =  new ArrayList<int[]>();
	
	
//	private static int[] prototype4_1 = new int[]{2, 3, 5, 6, 8, 15, 18, 19, 22, 24, 26, 29, 30, 31, 32, 35, 36, 37, 39, 40, 43, 44, 49, 57, 58, 60, 61, 67, 68, 71, 77, 78, 84, 88, 90, 94, 95, 97, 99, 100, 101, 103, 106, 107, 113, 115, 116, 121, 124, 125};
//	private static int[] prototype4_2 = new int[]{1, 10, 12, 16, 20, 21, 23, 41, 45, 46, 47, 50, 54, 62, 63, 66, 72, 81, 92, 93, 96, 102, 110, 111, 114, 117, 118, 120};
//	private static int[] prototype4_3 = new int[]{7, 9, 13, 53, 64, 65, 69, 75, 76, 82, 87, 89, 91, 104, 108, 109, 123, 126};
//	private static int[] prototype4_4 = new int[]{4, 11, 14, 17, 25, 27, 28, 33, 34, 38, 42, 48, 51, 52, 55, 56, 59, 70, 73, 74, 79, 80, 83, 85, 86, 98, 105, 112, 119, 122};
	
	//clickers 
	private static int[] prototype4_1 = new int[]{2, 3, 5, 6, 10, 11, 14, 15, 16, 17, 18, 19, 23, 28, 29, 31, 33, 36, 40, 41, 43, 44, 45, 48, 50, 51};
	private static int[] prototype4_2 = new int[]{1, 8, 12, 13, 20, 21, 24, 26, 34, 39, 42, 46};
	private static int[] prototype4_3 = new int[]{7, 25, 30, 32, 37, 38, 52};
	private static int[] prototype4_4 = new int[]{4, 9, 22, 27, 35, 47, 49};

	static ArrayList<int[]> prototype4 =  new ArrayList<int[]>();	
	
	
	private static int[] dch4_1 = new int[]{6, 8, 10, 15, 16, 17, 19, 22, 31, 37, 38, 44, 45, 52};
	private static int[] dch4_2 = new int[]{5, 11, 33, 42, 46};
	private static int[] dch4_3 = new int[]{2, 7, 9, 20, 21, 27, 35, 40, 47, 49, 50};
	private static int[] dch4_4 = new int[]{1, 13, 14, 25, 29, 32, 43, 51};
	static ArrayList<int[]> dch4 =  new ArrayList<int[]>();

	
	public static void main(String[] args) {
//		prototype3.add(prototype3_1);
//		prototype3.add(prototype3_2);
//		prototype3.add(prototype3_3);
		
		prototype4.add(prototype4_1);
		prototype4.add(prototype4_2);
		prototype4.add(prototype4_3);
		prototype4.add(prototype4_4);
		
		dch3.add(dch3_1);
		dch3.add(dch3_2);
		dch3.add(dch3_3);

		dch4.add(dch4_1);
		dch4.add(dch4_2);
		dch4.add(dch4_3);
		dch4.add(dch4_4);
		
		for(int i = 0; i < 3; i++) {
			prototype3.add(prototype3_1);
			prototype3.add(prototype3_2);
			prototype3.add(prototype3_3);
			
			for(int didx = 0; didx < dch3.size(); didx++) {
				int matcher = -1;
				double lastPercent = 0;
				for(int pIdx = 0; pIdx < prototype3.size(); pIdx++) {
						int[] sub = prototype3.get(pIdx).length < dch3.get(didx).length ? prototype3.get(pIdx) : dch3.get(didx);
						int[] all = prototype3.get(pIdx).length < dch3.get(didx).length ?  dch3.get(didx) : prototype3.get(pIdx);
						double percent = getPercentThatMatchFromSubsample(sub, all);
						if(percent > lastPercent) {
							lastPercent = percent;
							matcher = pIdx;
						}
				}
				System.out.println("best match: "  + lastPercent);
				System.out.println("prototype: " + Arrays.toString(prototype3.get(matcher)));
				System.out.println("dch: " + Arrays.toString(dch3.get(didx)));
				prototype3.remove(matcher);
												
			}
			System.out.println("------------");
			int[] move = dch3.remove(0);
			dch3.add(move);
		}
		
//		
//		System.out.println("---------------------");
//		for(int i = 0; i < 4; i++) {
//			prototype4.add(prototype4_1);
//			prototype4.add(prototype4_2);
//			prototype4.add(prototype4_3);
//			prototype4.add(prototype4_4);
//			
//			for(int didx = 0; didx < dch4.size(); didx++) {
//				int matcher = -1;
//				double lastPercent = 0;
//				for(int pIdx = 0; pIdx < prototype4.size(); pIdx++) {
//						int[] sub = prototype4.get(pIdx).length < dch4.get(didx).length ? prototype4.get(pIdx) : dch4.get(didx);
//						int[] all = prototype4.get(pIdx).length < dch4.get(didx).length ?  dch4.get(didx) : prototype4.get(pIdx);
//						double percent = getPercentThatMatchFromSubsample(sub, all);
//						if(percent > lastPercent) {
//							lastPercent = percent;
//							matcher = pIdx;
//						}
//				}
//				System.out.println("best match: "  + lastPercent);
//				System.out.println("prototype: " + Arrays.toString(prototype4.get(matcher)));
//				System.out.println("dch: " + Arrays.toString(dch4.get(didx)));
//				prototype4.remove(matcher);
//												
//			}
//			System.out.println("------------");
//			int[] move = dch4.remove(0);
//			dch4.add(move);
//		}
	}

	private static void overallPercentage(String s, ArrayList<int[]> clusterList) {
		List<List<int[]>> permutatedPlsaK3C1 = listPermutations(clusterList);		
		List<Double> percentages = new ArrayList<Double>();
		permutatedPlsaK3C1.forEach(li -> {			
			int[] first = li.get(0);
			int[] second = li.get(1);
			percentages.add(getPercentThatMatch(first, second));
		});
		
		double min = 100;
		double total = 0;
		for(int i = 0; i < percentages.size(); i++) {
			double p = percentages.get(i);
			if(p < min) {
				min = p;
			}
			total += percentages.get(i);
		}
		System.out.println("min: " + min);
		System.out.println("--------> overall match " + s + " :"+ (total-min) / (percentages.size()-1));
	}

	public static double getPercentThatMatch(int[] winningNumbers,
	        int[] numberList) { // it is confusing to call an array as List
	    int match = 0;
	    for (int win : winningNumbers) {
	        for (int my : numberList ){
	            if (win == my){
	                match++;
	            }
	        }
	    }
	    
	    int max = (numberList.length > winningNumbers.length) ? numberList.length : winningNumbers.length; // assume that same length
	    double devide = match / max; // it won't be good, because the result will be intm so Java will trunc it!
	    devide = (double) match / max; // you need to cast to float or double

	    double percent = devide * 100; 
	    return percent;
	}
	
	public static double getPercentThatMatchFromSubsample(int[] sub,
	        int[] all) { // it is confusing to call an array as List
	    int match = 0;
	    for (int win : sub) {
	        for (int my : all ){
	            if (win == my){
	                match++;
	            }
	        }
	    }
	    
	    int max = sub.length; //assume length of smaller one
	    double devide = match / max; // it won't be good, because the result will be intm so Java will trunc it!
	    devide = (double) match / max; // you need to cast to float or double

	    double percent = devide * 100; 
	    return percent;
	}	
	
	public static List<List<int[]>> listPermutations(List<int[]> list) {

	    if (list.size() == 0) {
	        List<List<int[]>> result = new ArrayList<List<int[]>>();
	        result.add(new ArrayList<int[]>());
	        return result;
	    }

	    List<List<int[]>> returnMe = new ArrayList<List<int[]>>();

	    int[] firstElement = list.remove(0);

	    List<List<int[]>> recursiveReturn = listPermutations(list);
	    for (List<int[]> li : recursiveReturn) {

	        for (int index = 0; index <= li.size(); index++) {
	            List<int[]> temp = new ArrayList<int[]>(li);
	            temp.add(index, firstElement);
	            returnMe.add(temp);
	        }

	    }
	    return returnMe;
	}
}
