package analytics;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.data.time.Day;
import org.jfree.data.time.Second;

public class DataPoint {
	
	private double nOx;
	private double o2;
	private double peakNOx;
	private String timeStamp;
	private int validity;
	private String time;
	private String date;
	
	public DataPoint(double nOx, double o2, double peakNOx, String timeStamp, int validity) {
		this.nOx = nOx;
		this.o2 = o2;
		this.peakNOx = peakNOx;
		this.timeStamp = timeStamp;
		this.validity = validity;
		calcDateTime(this.timeStamp);
	}
	
	public double getNOx() {
		return this.nOx;
	}
	
	public double getO2() {
		//if(this.o2 < 1 && this.o2 > -1) {
		//	return (this.o2 * 100);
		//}
		//else {
			return this.o2;
		//}
		//return this.o2;
	}
	
	public double getPeakNOx() {
		return this.peakNOx;
	}
	
	public String getTimeStamp() {
		return this.timeStamp;
	}
	
	public int getValidity() {
		return this.validity;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public void calcDateTime(String timeStamp) {
		// Get Date from time stamp
		StringBuilder sb = new StringBuilder();
				
		if(timeStamp.charAt(5) != '0') {
			sb.append(timeStamp.charAt(5));
		}
		sb.append(timeStamp.charAt(6));
		sb.append('/');
		
		if(timeStamp.charAt(8) != '0') {
			sb.append(timeStamp.charAt(8));
		}
		sb.append(timeStamp.charAt(9));
		sb.append('/');
		sb.append(timeStamp.charAt(0));
		sb.append(timeStamp.charAt(1));
		sb.append(timeStamp.charAt(2));
		sb.append(timeStamp.charAt(3));
		
		this.date = sb.toString();
		
		// Get Time from time stamp
		
		// 0    5  8  11 14 17
		// 2019-04-08T14:45:12.378Z
		//            9:45:12 AM
		
		sb = new StringBuilder();
		String hrStr = timeStamp.substring(11,13);
		Integer hour = Integer.parseInt(hrStr) - 5;
		if(hour < 0) { hour += 24;}

		hrStr = hour.toString();
		
		String minString = timeStamp.substring(14,16);
		String secString = timeStamp.substring(17,19);
		
		sb.append(hrStr);
		sb.append(':');
		sb.append(minString);
		sb.append(':');
		sb.append(secString);
		
		this.time = sb.toString();
		
	}

	public int getSeconds() {
		int hr = Integer.parseInt(timeStamp.substring(11,13));
		int min = Integer.parseInt(timeStamp.substring(14,16));
		int sec = Integer.parseInt(timeStamp.substring(17,19));
		
		return (hr*3600 + min*60 + sec);
	}

	public Second getTimePeriod() {
		int year = Integer.parseInt(timeStamp.substring(0,4));
		int month = Integer.parseInt(timeStamp.substring(5,7));
		int date = Integer.parseInt(timeStamp.substring(8,10));
		//int hours = Integer.parseInt(timeStamp.substring(11,13));
		int hours = Integer.parseInt(time.substring(0,2));
		int min = Integer.parseInt(time.substring(3,5));
		int sec = Integer.parseInt(time.substring(6,8));
		
		//@SuppressWarnings("deprecation")
		//Second timePeriod = new Second(new GregorianCalendar(year - 1900, month, date, hours, min, sec));
		Second timePeriod = new Second(sec,min,hours,date,month,year);
		return timePeriod;
	}

}
