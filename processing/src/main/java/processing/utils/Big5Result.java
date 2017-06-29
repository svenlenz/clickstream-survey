package processing.utils;

/**
 * Model for Big5 results.
 *  
 * @author sven.lenz@msc.htwchur.ch
 */
public class Big5Result {
	public int neuro;
	public int numberOfHighestNeuro;
	public int numberOfLowestNeuro;
	public int extra;
	public int numberOfHighestExtra;
	public int numberOfLowestExtra;
	public int gewissen;
	public int numberOfHighestGewissen;
	public int numberOfLowestGewissen;
	public int offen;
	public int numberOfHighestOffen;
	public int numberOfLowestOffen;
	public int vertrag;
	public int numberOfHighestVertrag;
	public int numberOfLowestVertrag;
	public int anerkennung;
	public int macht;
	public int sicher;
	public int ehrlich;
	public int technique;
	public int numberOfmales;
	public int numberofWomen;
	public int age;
	public int numberOfClicks;
	public int duration;
	public String highest;
	public String lowest;
	public String gender;

	public Big5Result() {
	}
	

	@Override
	public String toString() {
		return "neuro: " + neuro + "\nextra: " + extra + "\ngewissen:" + gewissen + "\noffen:" + offen + "\nvertrag:" + vertrag 
				+ "\nanerkennung:" + anerkennung + "\nmacht:" + macht + "\nsicher:" + sicher + "\nehrlich: " + ehrlich;
	}
	
	public String toCSV() {
		return neuro + "," + extra + "," + gewissen + "," + offen + "," + vertrag 
				+ "," + anerkennung + "," + macht + "," + sicher + "," + ehrlich
				+ "," + numberOfHighestNeuro + "," + numberOfLowestNeuro + "," + numberOfHighestExtra + "," + numberOfLowestExtra
				+ "," + numberOfHighestGewissen + "," + numberOfLowestGewissen + "," + numberOfHighestOffen + "," + numberOfLowestOffen
				+ "," + numberOfHighestVertrag + "," + numberOfLowestVertrag
				+ "," + technique + "," + numberOfmales + "," + numberofWomen + "," + age
				+ "," + numberOfClicks + "," + duration;
	}
	
	public static String csvHeader() {
		return "Neuro,Extra,Gewissen , Offen , Vertrag" + 
				", anerkennung , macht , sicher , ehrlich" +
				", numberOfHighestNeuro , numberOfLowestNeuro , numberOfHighestExtra , numberOfLowestExtra" +
				", numberOfHighestGewissen , numberOfLowestGewissen , numberOfHighestOffen , numberOfLowestOffen" +
				", numberOfHighestVertrag , numberOfLowestVertrag" +
				", Technique , numberOfmales , numberofWomen , Age" +
				", numberOfClicks , Duration";
	}
}