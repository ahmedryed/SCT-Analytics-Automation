package analytics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class ValiditySets implements EventHandler<ActionEvent> {
	
	private GridPane container;
	
	// all elements
	private Button prev;
	private Button next;
	private Button viewSampledPlot;
	private TextArea stats;
	private Label title;
	private Button viewPeakPlot;

	
	private int currIndex = 0;
	
	public ValiditySets(Tab tab) {
		container = display();
		tab.setContent(container);
	}
	
	public GridPane display() {
		GridPane grid = new GridPane();
		
		currIndex = 0; // Index of set in list of sets
		
		// create buttons
		prev = new Button("Previous");
		prev.setMinWidth(100);
		prev.setDisable(true);
		
		next = new Button("Next");
		next.setMinWidth(100);
		
		viewSampledPlot = new Button("Sampled NOx Plot");
		viewSampledPlot.setStyle("-fx-text-fill: #00b32c");
		viewSampledPlot.setMinWidth(120);
		
		viewPeakPlot = new Button("Peak NOx Plot");
		viewPeakPlot.setStyle("-fx-text-fill: #00b32c");
		viewPeakPlot.setMinWidth(120);
		
		prev.setOnAction(this);
		next.setOnAction(this);
		viewSampledPlot.setOnAction(this);
		viewPeakPlot.setOnAction(this);
		
		title = new Label(CreateSets.setsByValidity.get(currIndex).getTitle());
		
		// create text area to display stats
		stats = new TextArea();
		
		grid.add(title, 0, 0, 8, 1);
		grid.add(stats, 0, 1, 8, 23);
		grid.add(prev, 0, 26 , 1, 1);
		grid.add(viewSampledPlot, 2 , 26, 2, 1);
		grid.add(next, 7,26,1,1);
		
		// configure grid (GridPane)
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(15, 10, 0, 10));
        
        printStats();
        
        return grid;

	}
	
	public void printStats() {
		stats.clear();
	
		stats.appendText("Analytics from Sampled NOx Values\n");
		stats.appendText("STD Dev: " + CreateSets.setsByValidity.get(currIndex).stdDevSamp+ "\n");
		stats.appendText("Average NOx Level: " + CreateSets.setsByValidity.get(currIndex).avgNOxLevelSamp+ "\n");
		stats.appendText("Peak NOx Level: " + CreateSets.setsByValidity.get(currIndex).peakNOxSamp+ "\n");
		stats.appendText("Peak-Avg Ratio: " + CreateSets.setsByValidity.get(currIndex).peakAvgRatioSamp+ "\n");
		stats.appendText("Total NOx Produced: " + CreateSets.setsByValidity.get(currIndex).totalNOxSamp+ "\n");
		stats.appendText("Time (min): " + CreateSets.setsByValidity.get(currIndex).time+ "\n");
		stats.appendText("Average NOx: " + CreateSets.setsByValidity.get(currIndex).avgNOxSamp+ "\n");
		stats.appendText("Average O2: " + CreateSets.setsByValidity.get(currIndex).avgO2Level+ "\n");
		
		stats.appendText("\n");
		stats.appendText("Analytics from Peak NOx Values"+ "\n");
		stats.appendText("STD Dev: " + CreateSets.setsByValidity.get(currIndex).stdDevPeak+ "\n");
		stats.appendText("Average NOx Level: " + CreateSets.setsByValidity.get(currIndex).avgNOxLevelPeak+ "\n");
		stats.appendText("Peak NOx Level: " + CreateSets.setsByValidity.get(currIndex).peakNOxPeak+ "\n");
		stats.appendText("Peak-Avg Ratio: " + CreateSets.setsByValidity.get(currIndex).peakAvgRatioPeak+ "\n");
		stats.appendText("Total NOx Produced: " + CreateSets.setsByValidity.get(currIndex).totalNOxPeak+ "\n");
		stats.appendText("Time (min): " + CreateSets.setsByValidity.get(currIndex).time+ "\n");
		stats.appendText("Average NOx: " + CreateSets.setsByValidity.get(currIndex).avgNOxPeak+ "\n");
		stats.appendText("Average O2: " + CreateSets.setsByValidity.get(currIndex).avgO2Level);
	}

	@Override
	public void handle(ActionEvent event) {
		
		if(event.getSource() == next) {
			currIndex++;
			printStats();
			title.setText(CreateSets.setsByValidity.get(currIndex).getTitle());
		}
		if(event.getSource() == prev) {
			currIndex--;
			printStats();
			title.setText(CreateSets.setsByValidity.get(currIndex).getTitle());
		}
		if(event.getSource() == viewSampledPlot) {
			CreateSets.setsByValidity.get(currIndex).displayChart(0);
		}
		if(event.getSource() == viewPeakPlot) {
			CreateSets.setsByValidity.get(currIndex).displayChart(1);
		}
		
		if(currIndex == 0) {
			prev.setDisable(true);
		}else {
			prev.setDisable(false);
		}
		
		if(currIndex == (CreateSets.setsByValidity.size() - 1)) {
			next.setDisable(true);
		}else {
			next.setDisable(false);
		}
	}
}
