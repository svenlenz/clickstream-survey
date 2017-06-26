package processing.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareClusters {
	
	//plsa, 50 sessions, runs 1-10, 3 clusters
	private static int[] plsaK3R1C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,14,17,18,19,20,21,22,24,28,30,32,37,41,49,50};
	private static int[] plsaK3R1C2 = new int[]{9,10,16,25,29,31,35,38,39,42,44,45,46};
	private static int[] plsaK3R1C3 = new int[]{15,23,26,27,33,34,36,40,43,47,48};
	
	private static int[] plsaK3R2C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,17,18,19,20,21,22,24,28,30,32,37,49,50};
	private static int[] plsaK3R2C2 = new int[]{10,14,16,25,29,31,38,39,41,44,45,46};
	private static int[] plsaK3R2C3 = new int[]{9,15,23,26,27,33,34,35,36,40,42,43,47,48};
	
	private static int[] plsaK3R3C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,17,18,19,20,21,22,24,28,30,32,37,41,49,50};
	private static int[] plsaK3R3C2 = new int[]{9,10,16,25,29,31,35,38,39,42,44,45,46};
	private static int[] plsaK3R3C3 = new int[]{14,15,23,26,27,33,34,36,40,43,47,48};
	
	private static int[] plsaK3R4C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,14,17,18,19,20,21,22,24,28,30,32,37,49,50};
	private static int[] plsaK3R4C2 = new int[]{25,36,38,45};
	private static int[] plsaK3R4C3 = new int[]{9,10,15,16,23,26,27,29,31,33,34,35,39,40,41,42,43,44,46,47,48};
	
	
	private static int[] plsaK3R5C1 = new int[]{2,4,5,6,12,13,14,17,21,24,25,38,41,50};
	private static int[] plsaK3R5C2 = new int[]{1,3,7,8,9,18,19,20,22,28,30,32,35,37,49};
	private static int[] plsaK3R5C3 = new int[]{10,11,15,16,23,26,27,29,31,33,34,36,39,40,42,43,44,45,46,47,48};
	
	private static int[] plsaK3R6C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,17,18,19,20,21,22,24,28,30,32,37,41,49,50};
	private static int[] plsaK3R6C2 = new int[]{9,10,16,25,29,31,35,38,39,42,44,45,46};
	private static int[] plsaK3R6C3 = new int[]{14,15,23,26,27,33,34,36,40,43,47,48};
	
	private static int[] plsaK3R7C1 = new int[]{2,4,5,6,8,12,13,17,21,24,25,38,41,50};
	private static int[] plsaK3R7C2 = new int[]{1,3,7,9,18,19,20,22,28,30,32,35,37,49};
	private static int[] plsaK3R7C3 = new int[]{10,11,14,15,16,23,26,27,29,31,33,34,36,39,40,42,43,44,45,46,47,48};
	
	private static int[] plsaK3R8C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,17,18,19,20,21,22,24,28,30,32,37,41,49,50};
	private static int[] plsaK3R8C2 = new int[]{9,10,16,25,29,31,35,38,39,42,44,45,46};
	private static int[] plsaK3R8C3 = new int[]{14,15,23,26,27,33,34,36,40,43,47,48};
	
	private static int[] plsaK3R9C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,17,18,19,20,21,22,24,28,30,32,37,41,49,50};
	private static int[] plsaK3R9C2 = new int[]{9,10,16,25,29,31,35,38,39,42,44,45,46};
	private static int[] plsaK3R9C3 = new int[]{14,15,23,26,27,33,34,36,40,43,47,48};
	
	private static int[] plsaK3R10C1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,14,17,18,19,20,21,22,24,28,30,32,37,41,49,50};
	private static int[] plsaK3R10C2 = new int[]{9,10,16,25,29,31,35,38,39,42,44,45,46};
	private static int[] plsaK3R10C3 = new int[]{15,23,26,27,33,34,36,40,43,47,48};
	
	//plsa, 25 sessions, runs 1, 3 clusters
	private static int[] plsaK3R1HALFC1 = new int[]{1,2,3,4,5,6,7,8,11,12,13,17,18,19,20,21,22,24,25};
	private static int[] plsaK3R1HALFC2 = new int[]{9,10,14};
	private static int[] plsaK3R1HALFC3  = new int[]{15,16,23};
	
	private static int[] plsaK3R1DOUBLEC1 = new int[]{1,2,3,4,5,6,7,12,14,15,19,21,22,23,24,25,26,27,28,31,33,35,36,37,39,40,41,43,44,45,46,47,48,50,54,55,58,59,61,62,63,64,65,66,67,69,70,72,73,74,75,80,81,82,85,87,90,91,93,94,97,99};
	private static int[] plsaK3R1DOUBLEC2 = new int[]{10,29,30,32,38,53,60,68,71,76,77,84,88,92,96,100};
	private static int[] plsaK3R1DOUBLEC3  = new int[]{8,9,11,13,16,17,18,20,34,42,49,51,52,56,57,78,79,83,86,89,95,98};
	
	static ArrayList<int[]> plsaK3C1 =  new ArrayList<int[]>();
	static ArrayList<int[]> plsaK3C2 =  new ArrayList<int[]>();
	static ArrayList<int[]> plsaK3C3 =  new ArrayList<int[]>();
	
	//k-means, 50 sessions, runs 1-10, 3 clusters
	private static int[] kmeansK3R1C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 23, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R1C2 = new int[]{1, 6, 7, 17, 18, 19, 21, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R1C3 = new int[]{12, 37};
	
	private static int[] kmeansK3R2C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R2C2 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R2C3 = new int[]{37};
	
	private static int[] kmeansK3R3C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R3C2 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R3C3 = new int[]{37};
	
	private static int[] kmeansK3R4C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 23, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R4C2 = new int[]{1, 6, 7, 17, 18, 19, 21, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R4C3 = new int[]{12, 37};
	
	
	private static int[] kmeansK3R5C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 23, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R5C2 = new int[]{1, 6, 7, 17, 18, 19, 21, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R5C3 = new int[]{12, 37};
	
	private static int[] kmeansK3R6C1 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R6C2 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R6C3 = new int[]{37};
	
	private static int[] kmeansK3R7C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R7C2 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R7C3 = new int[]{37};
	
	private static int[] kmeansK3R8C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R8C2 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R8C3 = new int[]{37};
	
	private static int[] kmeansK3R9C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R9C2 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R9C3 = new int[]{37};
	
	private static int[] kmeansK3R10C1 = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47};
	private static int[] kmeansK3R10C2 = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50};
	private static int[] kmeansK3R10C3 = new int[]{37};
	
	//k-means, 25 sessions, runs 1, 3 clusters
	private static int[] kmeansK3R1C1_HALF  = new int[]{2, 3, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25};
	private static int[] kmeansK3R1C2_HALF = new int[]{1, 4, 6, 12, 19, 21, 23};
	private static int[] kmeansK3R1C3_HALF= new int[]{7, 17, 18};
	
	private static int[] kmeansK3R1C1_DOUBLE  = new int[]{2, 3, 4, 5, 8, 9, 10, 11, 13, 14, 15, 16, 20, 22, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 38, 39, 42, 43, 44, 45, 46, 47, 51, 52, 56, 57, 59, 61, 62, 63, 65, 66, 67, 69, 70, 71, 72, 73, 74, 76, 78, 79, 80, 82, 85, 86, 87, 88, 89, 90, 91, 93, 94, 95, 96, 97, 98, 99, 100};
	private static int[] kmeansK3R1C2_DOUBLE = new int[]{1, 6, 7, 12, 17, 18, 19, 21, 23, 26, 29, 30, 40, 41, 48, 49, 50, 53, 54, 55, 58, 60, 68, 75, 77, 81, 83, 84, 92};
	private static int[] kmeansK3R1C3_DOUBLE= new int[]{37, 64};
	
	static ArrayList<int[]> kmeansK3C1 =  new ArrayList<int[]>();
	static ArrayList<int[]> kmeansK3C2 =  new ArrayList<int[]>();
	static ArrayList<int[]> kmeansK3C3 =  new ArrayList<int[]>();	

	//dhc, 50 sessions, runs 1-10, 3 clusters
	private static int[] dhcK3R1C1 = new int[]{1, 3, 7, 9, 14, 17, 22, 23, 27, 32, 34, 35, 38, 39, 40, 43, 46, 47, 49};
	private static int[] dhcK3R1C2 = new int[]{2, 4, 5, 10, 11, 12, 13, 15, 20, 21, 24, 25, 28, 31, 33, 41, 42, 48};
	private static int[] dhcK3R1C3 = new int[]{6, 8, 16, 18, 19, 26, 29, 30, 36, 37, 44, 45, 50};
	
	//dhc, 25 sessions, runs 1, 3 clusters
	private static int[] dhcK3R1HALFC1 = new int[]{1, 3, 7, 9, 14, 17, 22, 23};
	private static int[] dhcK3R1HALFC2 = new int[]{2, 4, 5, 12, 13, 15, 21, 24, 25};
	private static int[] dhcK3R1HALFC3  = new int[]{6, 8, 10, 11, 16, 18, 19, 20};

	
	public static void main(String[] args) {
		plsaK3C1.add(plsaK3R1C1);
		plsaK3C1.add(plsaK3R2C1);
		plsaK3C1.add(plsaK3R3C1);
		plsaK3C1.add(plsaK3R4C1);
		plsaK3C1.add(plsaK3R5C1);
		plsaK3C1.add(plsaK3R6C1);
		plsaK3C1.add(plsaK3R7C1);
		plsaK3C1.add(plsaK3R8C1);
		plsaK3C1.add(plsaK3R9C1);
		plsaK3C1.add(plsaK3R10C1);		
		overallPercentage("PLSA, C1", (ArrayList<int[]>)plsaK3C1.clone());
		

		

		
		
		plsaK3C2.add(plsaK3R1C2);
		plsaK3C2.add(plsaK3R2C2);
		plsaK3C2.add(plsaK3R3C2);
		plsaK3C2.add(plsaK3R4C2);
		plsaK3C2.add(plsaK3R5C2);
		plsaK3C2.add(plsaK3R6C2);
		plsaK3C2.add(plsaK3R7C2);
		plsaK3C2.add(plsaK3R8C2);
		plsaK3C2.add(plsaK3R9C2);
		plsaK3C2.add(plsaK3R10C2);
		overallPercentage("PLSA, C2", (ArrayList<int[]>)plsaK3C2.clone());
		
		plsaK3C3.add(plsaK3R1C3);
		plsaK3C3.add(plsaK3R2C3);
		plsaK3C3.add(plsaK3R3C3);
		plsaK3C3.add(plsaK3R4C3);
		plsaK3C3.add(plsaK3R5C3);
		plsaK3C3.add(plsaK3R6C3);
		plsaK3C3.add(plsaK3R7C3);
		plsaK3C3.add(plsaK3R8C3);
		plsaK3C3.add(plsaK3R9C3);
		plsaK3C3.add(plsaK3R10C3);
		overallPercentage("PLSA, C3", (ArrayList<int[]>)plsaK3C3.clone());
		
		double total = 0;
		for(int i = 0; i < plsaK3C1.size(); i++) {
			total+=getPercentThatMatch(plsaK3C1.get(i), dhcK3R1C1);
		}
		System.out.println("Cluster 1 PLSA <-> DHC " + (total/plsaK3C1.size()));
		total = 0;
		for(int i = 0; i < plsaK3C2.size(); i++) {
			total+=getPercentThatMatch(plsaK3C2.get(i), dhcK3R1C2);
		}
		System.out.println("Cluster 2 PLSA <-> DHC " + (total/plsaK3C2.size()));
		total = 0;
		for(int i = 0; i < plsaK3C3.size(); i++) {
			total+=getPercentThatMatch(plsaK3C3.get(i), dhcK3R1C3);
		}
		System.out.println("Cluster 3 PLSA <-> DHC " + (total/plsaK3C3.size()));
		System.out.println("---------------");
		
		kmeansK3C1.add(kmeansK3R1C1);
		kmeansK3C1.add(kmeansK3R2C1);
		kmeansK3C1.add(kmeansK3R3C1);
		kmeansK3C1.add(kmeansK3R4C1);
		kmeansK3C1.add(kmeansK3R5C1);
		kmeansK3C1.add(kmeansK3R6C1);
		kmeansK3C1.add(kmeansK3R7C1);
		kmeansK3C1.add(kmeansK3R8C1);
		kmeansK3C1.add(kmeansK3R9C1);
		kmeansK3C1.add(kmeansK3R10C1);
		overallPercentage("kmeans, C1", (ArrayList<int[]>)kmeansK3C1.clone());
		
		kmeansK3C2.add(kmeansK3R1C2);
		kmeansK3C2.add(kmeansK3R2C2);
		kmeansK3C2.add(kmeansK3R3C2);
		kmeansK3C2.add(kmeansK3R4C2);
		kmeansK3C2.add(kmeansK3R5C2);
		kmeansK3C2.add(kmeansK3R6C2);
		kmeansK3C2.add(kmeansK3R7C2);
		kmeansK3C2.add(kmeansK3R8C2);
		kmeansK3C2.add(kmeansK3R9C2);
		kmeansK3C2.add(kmeansK3R10C2);
		overallPercentage("kmeans, C2",(ArrayList<int[]>)kmeansK3C2.clone() );
		
		
		kmeansK3C3.add(kmeansK3R1C3);
		kmeansK3C3.add(kmeansK3R2C3);
		kmeansK3C3.add(kmeansK3R3C3);
		kmeansK3C3.add(kmeansK3R4C3);
		kmeansK3C3.add(kmeansK3R5C3);
		kmeansK3C3.add(kmeansK3R6C3);
		kmeansK3C3.add(kmeansK3R7C3);
		kmeansK3C3.add(kmeansK3R8C3);
		kmeansK3C3.add(kmeansK3R9C3);
		kmeansK3C3.add(kmeansK3R10C3);
		overallPercentage("kmeans, C3",(ArrayList<int[]>)kmeansK3C3.clone() );	
		total = 0;
		for(int i = 0; i < kmeansK3C1.size(); i++) {
			total+=getPercentThatMatch(kmeansK3C1.get(i), dhcK3R1C1);
		}
		System.out.println("Cluster 1 KMEANS <-> DHC " + (total/kmeansK3C1.size()));
		total = 0;
		for(int i = 0; i < kmeansK3C2.size(); i++) {
			total+=getPercentThatMatch(kmeansK3C2.get(i), dhcK3R1C2);
		}
		System.out.println("Cluster 2 KMEANS <-> DHC " + (total/kmeansK3C2.size()));
		total = 0;
		for(int i = 0; i < kmeansK3C3.size(); i++) {
			total+=getPercentThatMatch(kmeansK3C3.get(i), dhcK3R1C3);
		}
		System.out.println("Cluster 3 KMEANS <-> DHC " + (total/kmeansK3C3.size()));
		
		
		
		total = 0;
		for(int i = 0; i < kmeansK3C1.size(); i++) {
			for(int j = 0; j < plsaK3C1.size(); j++) {
				total+=getPercentThatMatch(kmeansK3C1.get(i), plsaK3C1.get(j));	
			}			
		}
		System.out.println("Cluster 1 KMEANS <-> PLSA " + (total/(kmeansK3C1.size()*plsaK3C1.size())));
		total = 0;
		for(int i = 0; i < kmeansK3C2.size(); i++) {
			for(int j = 0; j < plsaK3C2.size(); j++) {
				total+=getPercentThatMatch(kmeansK3C2.get(i), plsaK3C2.get(j));	
			}			
		}
		System.out.println("Cluster 2 KMEANS <-> PLSA " + (total/(kmeansK3C2.size()*plsaK3C2.size())));
		total = 0;
		for(int i = 0; i < kmeansK3C3.size(); i++) {
			for(int j = 0; j < plsaK3C3.size(); j++) {
				total+=getPercentThatMatch(kmeansK3C3.get(i), plsaK3C3.get(j));	
			}			
		}
		System.out.println("Cluster 3 KMEANS <-> PLSA " + (total/(kmeansK3C3.size()*plsaK3C3.size())));
		
		
		
		System.out.println("Subsample DHC1 half " + getPercentThatMatchFromSubsample(dhcK3R1HALFC1, dhcK3R1C1));
		System.out.println("Subsample DHC2 half " + getPercentThatMatchFromSubsample(dhcK3R1HALFC2, dhcK3R1C2));
		System.out.println("Subsample DHC3 half " + getPercentThatMatchFromSubsample(dhcK3R1HALFC3, dhcK3R1C3));
		
		System.out.println("Subsample KMEANS half " + getPercentThatMatchFromSubsample(kmeansK3R1C1_HALF, kmeansK3R1C1));
		System.out.println("Subsample KMEANS half " + getPercentThatMatchFromSubsample(kmeansK3R1C2_HALF, kmeansK3R1C2));
		System.out.println("Subsample KMEANS half " + getPercentThatMatchFromSubsample(kmeansK3R1C3_HALF, kmeansK3R1C3));
		
		System.out.println("Subsample KMEANS " + getPercentThatMatchFromSubsample(kmeansK3R1C1, kmeansK3R1C1_DOUBLE));
		System.out.println("Subsample KMEANS " + getPercentThatMatchFromSubsample(kmeansK3R1C2, kmeansK3R1C2_DOUBLE));
		System.out.println("Subsample KMEANS " + getPercentThatMatchFromSubsample(kmeansK3R1C3, kmeansK3R1C3_DOUBLE));
		
		System.out.println("Subsample PLSA half " + getPercentThatMatchFromSubsample(plsaK3R1HALFC1, plsaK3R1C1));
		System.out.println("Subsample PLSA half " + getPercentThatMatchFromSubsample(plsaK3R1HALFC2, plsaK3R1C2));
		System.out.println("Subsample PLSA half " + getPercentThatMatchFromSubsample(plsaK3R1HALFC3, plsaK3R1C3));
		
		System.out.println("Subsample PLSA double " + getPercentThatMatchFromSubsample(plsaK3R1C1, plsaK3R1DOUBLEC1));
		System.out.println("Subsample PLSA double " + getPercentThatMatchFromSubsample(plsaK3R1C2, plsaK3R1DOUBLEC2));
		System.out.println("Subsample PLSA double " + getPercentThatMatchFromSubsample(plsaK3R1C3, plsaK3R1DOUBLEC3));
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
