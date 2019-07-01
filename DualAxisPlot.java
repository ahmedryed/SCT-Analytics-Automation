package analytics;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class DualAxisPlot extends ApplicationFrame {

	public DualAxisPlot(final String title, ArrayList<DataPoint> set, int type) { //type = 0 for sampled NOx, type = 1 for peak NOx
		super(title);
		
		final String chartTitle = title;
		final XYDataset dataSet = createNOxSet(set,type);
		final JFreeChart chart;
		
		if(type == 0) { //Sampled NOx
			chart = ChartFactory.createTimeSeriesChart( // Create a chart with the first data set (NOx)
					chartTitle, 
					"Time", 
					"Sampled NOx (ppm)", 
					dataSet,
					true,
					true,
					false
			);
		}else { // Peak NOx
			chart = ChartFactory.createTimeSeriesChart( // Create a chart with the first data set (NOx)
					chartTitle, 
					"Time", 
					"Peak NOx (ppm)", 
					dataSet,
					true,
					true,
					false
			);	
		}
		
		chart.setBackgroundPaint(new Color(255,228,196)); // Orange
		
		final XYPlot plot = chart.getXYPlot(); // Grab plot from chart
		final NumberAxis axis2 = new NumberAxis("O2 (%)"); // create second axis for Oxygen percentage data
		axis2.setAutoRangeIncludesZero(false); // doesn't need to include zero
		plot.setRangeAxis(1,axis2); // set second range to second axis
		plot.setDataset(1,createO2Set(set)); 
		plot.mapDatasetToRangeAxis(1,1); // set oxygend data set to oxygen axis
		
		final XYItemRenderer renderer = plot.getRenderer();
		renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
		if (renderer instanceof StandardXYItemRenderer) {
            final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
            //rr.setPlotShapes(true);
            rr.setShapesFilled(true);
        }
		
		final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        //renderer2.setPlotShapes(true);
        renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        plot.setRenderer(1, renderer2);
        
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        //axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
       // setContentPane(chartPanel);
        
        JFrame f = new JFrame(title);
        f.setTitle(title);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(800, 400);
        f.setLayout(new BorderLayout(0, 5));
        f.add(chartPanel, BorderLayout.CENTER);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setHorizontalAxisTrace(true);
        chartPanel.setVerticalAxisTrace(true);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
		
		
	}
	
	/**
	 * This method reads the set of NOx data and creates a set of coordinates.
	 * Time is X coordinate, NOx (ppm) is y coordinate
	 * @param set
	 * @return
	 */
	private XYDataset createNOxSet(ArrayList<DataPoint> set, int type) {
		final TimeSeries s1;
		
		if(type == 0) { // Option to plot sampled NOx values
			s1 = new TimeSeries("Sampled NOx", Second.class); //Time series with unit second
			for(DataPoint dp : set) { // Add each NOx value to set
				s1.add(dp.getTimePeriod(),dp.getNOx());
			}
		}else { // Option to plot Peak NOx values
			s1 = new TimeSeries("Peak NOx", Second.class); //Time series with unit second
			for(DataPoint dp : set) { // Add each NOx value to set
				s1.add(dp.getTimePeriod(),dp.getPeakNOx());	
			}
		}
		
		final TimeSeriesCollection dataset = new TimeSeriesCollection(); // Create a time collection to hold the time series
        dataset.addSeries(s1);

        return dataset;
	}
	
	/**
	 * This method is the same as the one about but creates a set with the O2 percentage values.
	 * @param set
	 * @return
	 */
	private XYDataset createO2Set(ArrayList<DataPoint> set) {
		final TimeSeries s2 = new TimeSeries("O2", Second.class);
		
		for(DataPoint dp : set) {
			s2.add(dp.getTimePeriod(),dp.getO2());
		}
		
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s2);

        return dataset;
	}
}
