package processing.utils;

public class Big5Result {
	public int meanNeuro;
	public int numberOfHighestNeuro;
	public int numberOfLowestNeuro;
	public int meanExtra;
	public int numberOfHighestExtra;
	public int numberOfLowestExtra;
	public int meanGewissen;
	public int numberOfHighestGewissen;
	public int numberOfLowestGewissen;
	public int meanOffen;
	public int numberOfHighestOffen;
	public int numberOfLowestOffen;
	public int meanVertrag;
	public int numberOfHighestVertrag;
	public int numberOfLowestVertrag;
	public int anerkennung;
	public int macht;
	public int sicher;
	public int ehrlich;
	public int meanTechnique;
	public int numberOfmales;
	public int numberofWomen;
	public int meanAge;
	public int numberOfClicks;
	public int meanDuration;

	public Big5Result() {
	}
	

	@Override
	public String toString() {
		return "neuro: " + meanNeuro + "\nextra: " + meanExtra + "\ngewissen:" + meanGewissen + "\noffen:" + meanOffen + "\nvertrag:" + meanVertrag 
				+ "\nanerkennung:" + anerkennung + "\nmacht:" + macht + "\nsicher:" + sicher + "\nehrlich: " + ehrlich;
	}
	
	public String toCSV() {
		return meanNeuro + "," + meanExtra + "," + meanGewissen + "," + meanOffen + "," + meanVertrag 
				+ "," + anerkennung + "," + macht + "," + sicher + "," + ehrlich
				+ "," + numberOfHighestNeuro + "," + numberOfLowestNeuro + "," + numberOfHighestExtra + "," + numberOfLowestExtra
				+ "," + numberOfHighestGewissen + "," + numberOfLowestGewissen + "," + numberOfHighestOffen + "," + numberOfLowestOffen
				+ "," + numberOfHighestVertrag + "," + numberOfLowestVertrag
				+ "," + meanTechnique + "," + numberOfmales + "," + numberofWomen + "," + meanAge
				+ "," + numberOfClicks + "," + meanDuration;
	}
	
	public static String csvHeader() {
		return "meanNeuro,meanExtra,meanGewissen , meanOffen , meanVertrag" + 
				", anerkennung , macht , sicher , ehrlich" +
				", numberOfHighestNeuro , numberOfLowestNeuro , numberOfHighestExtra , numberOfLowestExtra" +
				", numberOfHighestGewissen , numberOfLowestGewissen , numberOfHighestOffen , numberOfLowestOffen" +
				", numberOfHighestVertrag , numberOfLowestVertrag" +
				", meanTechnique , numberOfmales , numberofWomen , meanAge" +
				", numberOfClicks , meanDuration";
	}
}