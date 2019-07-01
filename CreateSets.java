package analytics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateSets {

	public static String filename = "Book6.csv";
	public static ArrayList<DataPoint> totalFileDataSet = new ArrayList<DataPoint>();
	
	public static ArrayList<DataSet> setsByDay = new ArrayList<DataSet>();
	public static ArrayList<DataSet> setsByTimeStamp = new ArrayList<DataSet>();
	public static ArrayList<DataSet> setsByValidity = new ArrayList<DataSet> ();



	public CreateSets() {
		try {
			totalFileDataSet = parseFile();	// Get in all data from file
			
			// Create different sets by day, time stamp, and validity bit
			setsByDay = separateByDay(); 
			setsByTimeStamp = separateByTStamp();
			setsByValidity = separateByValidity();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Read in all data from CSV file into an ArrayList that will be used for analytics
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<DataPoint> parseFile() throws FileNotFoundException, IOException{
		ArrayList<DataPoint> allPoints = new ArrayList<DataPoint>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))){
			String line;
			try {
				br.readLine();
				while((line = br.readLine()) != null) {
					String [] values = line.split(",");
					
					
					double nOxValue = Double.parseDouble(values[0]);
					double o2Value = Double.parseDouble(values[1]);
					double peakNOx = Double.parseDouble(values[2]);
					String timeStamp = values[3];
					int validity = Integer.parseInt(values[4]);
					
					DataPoint point = new DataPoint(nOxValue, o2Value, peakNOx, timeStamp, validity);
					allPoints.add(point);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return allPoints;
	}
	
	/**
	 * This function takes all of the data from the file and then separates into multiple sets based on the day
	 * Produces analytics that are aggregates of daily values
	 * @return
	 */
	public static ArrayList<DataSet> separateByDay(){
		
		ArrayList<DataSet> days = new ArrayList<DataSet>(); // list of all data sets seperated by day
		ArrayList<DataPoint> points = new ArrayList<DataPoint>(); // list of all points with same day used to construct data set
		String currDate = totalFileDataSet.get(0).getDate();
		
		for(int i = 0; i < totalFileDataSet.size(); i++) {
			if(totalFileDataSet.get(i).getDate().equals(currDate)) { // If next point is from same day, add it to the list
				points.add(totalFileDataSet.get(i));
			}
			else {
				days.add(new DataSet(points)); // construct a new set and add it to list of sets
				points = new ArrayList<DataPoint>();
				//points.clear(); // Start new list with next day
				points.add(totalFileDataSet.get(i));
				currDate = totalFileDataSet.get(i).getDate();
			}
		}
		days.add(new DataSet(points));
		
		return days;
	}
	
	/**
	 * Takes all of the data from the file and then separates into multiple sets based on difference between time of points
	 * If a point is far in time from the previous point, a new set is started
	 * @return
	 */
	public static ArrayList<DataSet> separateByTStamp(){
		ArrayList<DataSet> sets = new ArrayList<DataSet>();
		ArrayList<DataPoint> points = new ArrayList<DataPoint>();
		int diff;
		
		for(int i = 0; i < totalFileDataSet.size(); i++) { // Iterate through all points
			
			points.add(totalFileDataSet.get(i));
			
			if(i == (totalFileDataSet.size() - 1)) { // If you reach the last value in the file
				sets.add(new DataSet(points));
				return sets;
			}
			
			diff = totalFileDataSet.get(i).getSeconds() - totalFileDataSet.get(i+1).getSeconds(); // Calculate difference between time stamps of sequential points
			
			if(diff > 12 || diff < 0 ) { // If two points are far in time, construct a set from current list of points
				try {
					sets.add(new DataSet(points));
				} catch(IndexOutOfBoundsException e) {
					//sets.remove(sets.size()-1);
				}				
				points = new ArrayList<DataPoint>(); // clear points list to begin a list for next set
			}
		}
		
		return sets;
	}

	public static ArrayList<DataSet> separateByValidity(){
		ArrayList<DataSet> sets = new ArrayList<DataSet>();
		ArrayList<DataPoint> points = new ArrayList<DataPoint>();
		
		for(int i = 0; i < totalFileDataSet.size(); i++) {
			
			points.add(totalFileDataSet.get(i));
			
			if(i == (totalFileDataSet.size() - 1)) { // If you reach the last value in the file
				sets.add(new DataSet(points));
				return sets;
			}
			
			if((totalFileDataSet.get(i).getValidity() - totalFileDataSet.get((i+1)).getValidity()) == 1) {
				sets.add(new DataSet(points));
				points = new ArrayList<DataPoint>();
			}
		}
		
		return sets;
	}


}
