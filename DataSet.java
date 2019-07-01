package analytics;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import javafx.application.Platform;

public class DataSet {

	private ArrayList<DataPoint> dataSet = new ArrayList<DataPoint>();
 
	private String begTStamp;
	private String endTStamp;
	
	public double time;
	public double avgO2Level;
	public int numInvalidPoints;
	
	//Analytics using Sampled NOx values
	public double stdDevSamp;
	public double avgNOxLevelSamp;
	public double peakNOxSamp;
	public double peakAvgRatioSamp;
	public double totalNOxSamp;
	public double avgNOxSamp;
	
	//Analytics using Peak NOx values
	public double stdDevPeak;
	public double avgNOxLevelPeak;
	public double peakNOxPeak;
	public double peakAvgRatioPeak;
	public double totalNOxPeak;
	public double avgNOxPeak;

	public DataSet(ArrayList<DataPoint> set) {
		this.dataSet = set;
		
		removeInvalidData();
		calcSTDDev();
		peakCalcs();
		//printSet();
		
		begTStamp = this.dataSet.get(0).getDate() + " " + this.dataSet.get(0).getTime();
		endTStamp = dataSet.get(dataSet.size() - 1).getDate() + " " + dataSet.get(dataSet.size() - 1).getTime();
			
	}
	


	/**
	 * Goes through data set and removes any invalid data
	 */
	private void removeInvalidData(){	
		numInvalidPoints = 0;
		for(int i = 0; i < dataSet.size(); i ++) {
			if(dataSet.get(i).getValidity() == 0) {
				dataSet.remove(i);
				numInvalidPoints++;
				i--;
			}
		}
	}
	
	/**
	 * Calculates Total NOx produced, Average NOx Level, Average O2 Level and Standard Deviation
	 * for both Sampled NOx values and Peak NOx values
	 */
	private void calcSTDDev() {			
		if(dataSet.size() == 0) {
			return;
		}
		//Calculate average NOx from set
		totalNOxSamp = 0.0;
		totalNOxPeak = 0.0;
		double totalO2 = 0.0;
		
		for(DataPoint dp : dataSet) { // sum all values NOx values for sampled and peak
			totalNOxSamp += dp.getNOx(); 
			totalNOxPeak = dp.getPeakNOx();
			totalO2 += dp.getO2(); //needed to calculate average O2 level
		}
		
		//Calculate average NOx and O2 values
		avgNOxLevelSamp = totalNOxSamp / (dataSet.size()); // divide by amount of values
		avgNOxLevelPeak = totalNOxPeak / (dataSet.size());
		avgO2Level = totalO2 / (dataSet.size());
		
		//Calculate STD Dev
		double sumOfSquaresSamp = 0.0;
		double sumOfSquaresPeak = 0.0;
		
		for(DataPoint dp: dataSet) {
			double diffSamp = dp.getNOx() - avgNOxLevelSamp; // take each value in data set and subtract mean from it
			double diffPeak = dp.getPeakNOx() - avgNOxLevelPeak;
			diffSamp = diffSamp * diffSamp; // square the difference
			diffPeak = diffPeak * diffPeak;
			sumOfSquaresSamp += diffSamp; //add up squared differences
			sumOfSquaresPeak += diffPeak;
		}
		
		sumOfSquaresSamp = sumOfSquaresSamp / (dataSet.size() - 1); // divide sumOfSquares by number of values in data set minus one
		stdDevSamp = Math.sqrt(sumOfSquaresSamp); // take square root to get std deviation
		
		sumOfSquaresPeak = sumOfSquaresPeak / (dataSet.size() - 1); // divide sumOfSquares by number of values in data set minus one
		stdDevPeak = Math.sqrt(sumOfSquaresPeak); // take square root to get std deviation
		
	}

	/**
	 * Calculates Peak NOx Level, Peak-Avg Ratio, Time, and Average NOx 
	 */
	private void peakCalcs() {
		if(dataSet.size() == 0) {
			return;
		}
		
		peakNOxSamp = dataSet.get(0).getNOx();
		peakNOxPeak = dataSet.get(0).getPeakNOx();
		
		for(DataPoint dp : dataSet) {
			if(dp.getNOx() > peakNOxSamp) {
				peakNOxSamp = dp.getNOx();
			}
			if(dp.getPeakNOx() > peakNOxPeak) {
				peakNOxPeak = dp.getPeakNOx();
			}
		}
		
		peakAvgRatioSamp = peakNOxSamp / avgNOxLevelSamp;
		peakAvgRatioPeak = peakNOxPeak / avgNOxLevelPeak;
		
		time = (dataSet.size() + numInvalidPoints - 1) * 10.0 / 60.0;
		avgNOxSamp = totalNOxSamp / time;
		avgNOxPeak = totalNOxPeak / time;
		
	}
	
	public void printSet(PrintWriter writer) {
		try {
			writer.println("First TS: " + begTStamp);
			writer.println("Last TS: " + endTStamp);
		} catch(IndexOutOfBoundsException e) {
			
		}
		
		writer.println("Analytics from Sampled NOx Values");
		writer.println("STD Dev: " + stdDevSamp);
		writer.println("Average NOx Level: " + avgNOxLevelSamp);
		writer.println("Peak NOx Level: " + peakNOxSamp);
		writer.println("Peak-Avg Ratio: " + peakAvgRatioSamp);
		writer.println("Total NOx Produced: " + totalNOxSamp);
		writer.println("Time (min): " + time);
		writer.println("Average NOx: " + avgNOxSamp);
		writer.println("Average O2: " + avgO2Level);
		
		writer.println("");
		writer.println("Analytics from Peak NOx Values");
		writer.println("STD Dev: " + stdDevPeak);
		writer.println("Average NOx Level: " + avgNOxLevelPeak);
		writer.println("Peak NOx Level: " + peakNOxPeak);
		writer.println("Peak-Avg Ratio: " + peakAvgRatioPeak);
		writer.println("Total NOx Produced: " + totalNOxPeak);
		writer.println("Time (min): " + time);
		writer.println("Average NOx: " + avgNOxPeak);
		writer.println("Average O2: " + avgO2Level);
		writer.println("----------------------------------------------------------");
	}
	
	public void displayChart(int type) { //type = 0 for sampled NOx, type = 1 for peak NOx
		Platform.runLater(() -> {
		      DualAxisPlot example = new DualAxisPlot((endTStamp + " - " + begTStamp), dataSet, type);
		      //example.setSize(800, 400);
		      //example.setLocationRelativeTo(null);
		      //example.setVisible(true);
		      //example.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		      //example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    });
	}
	
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append(endTStamp);
		sb.append(" - ");
		sb.append(begTStamp);
		return sb.toString();
	}
}
