package analytics;

import javax.swing.WindowConstants;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	
	static Stage window;
	
	/*Declaration of all tabs for controller*/
	public static TabPane tabpane;
	public static Tab daysTab;
	public static Tab validityTab;
	public static Tab timeStampTab;
	
	public static double width;
	public static double height;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ProvideFile pf = new ProvideFile();
		pf.getFile();
		
		CreateSets cs = new CreateSets();

		window = primaryStage;
		window.setTitle("SensorComm Technologies");
		
		tabpane = new TabPane();
		daysTab = new Tab("Sets by Day");
		validityTab = new Tab("Sets by Validity");
		timeStampTab = new Tab("Sets by Time Stamp");
		
		tabpane.getTabs().add(daysTab);
		tabpane.getTabs().add(validityTab);
		tabpane.getTabs().add(timeStampTab);
		
		// each of theses classes creates it's own tab
		DaySets days_class = new DaySets(daysTab);
		ValiditySets validity_class = new ValiditySets(validityTab);
		TimeStampSets ts_class = new TimeStampSets(timeStampTab);
		
		window.setOnCloseRequest(e -> {
			e.consume();
			System.exit(0);
		});
		
		width = 465;
		height = 515;
		Scene scene = new Scene(tabpane,width,height);
		window.setScene(scene);
		window.show();	
	}
	
	private class ProvideFile {

		public void getFile() {
			// setup stage
			Stage window = new Stage();
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle("Provide Name of CSV");
			window.setMinWidth(400);

			// setup label
			Label informationLabel = new Label("Enter the name of the CSV file (Eg. 'Book6.csv')");
			informationLabel.setWrapText(true);
			informationLabel.setAlignment(Pos.CENTER);
			// setting up sub-container
			HBox hbox_text = new HBox();
			hbox_text.setSpacing(10);
			hbox_text.getChildren().addAll(informationLabel);
			hbox_text.setAlignment(Pos.CENTER);
			hbox_text.setPadding(new Insets(5, 12, 5, 12));

	        TextField ipInput = new TextField();
	        ipInput.setAlignment(Pos.CENTER);
	        ipInput.setPromptText("Enter File Name");

			Button submit = new Button("submit");
			submit.setMinWidth(130);

			submit.setOnAction(e -> {
				CreateSets.filename = ipInput.getText();

				/* what heppens when user doesn't provide input */
	    		if(CreateSets.filename.equals("") || CreateSets.filename == null){
    				new PopUpNotification("Input file name", "please, input file name to start client");
	    		}
	    		else{
	    			/* what happens when user provides VALID and UNUSED user name */
						window.close();
	    			}

			});

			// setting up main container
			VBox container = new VBox();
			container.setPadding(new Insets(15, 12, 15, 12));
			container.setStyle("-fx-background-color: #fffac8;");

			// setting up sub-container
			HBox hbox = new HBox();
		    hbox.setSpacing(30);
		    hbox.getChildren().addAll(submit);
		    hbox.setAlignment(Pos.CENTER);
		    hbox.setPadding(new Insets(15, 12, 15, 12));

			container.getChildren().addAll(hbox_text, ipInput, hbox);
			container.setAlignment(Pos.CENTER);

			// displaying
			Scene scene = new Scene(container, 150, 220);
			window.setScene(scene);
			window.showAndWait();
		}
	}
	
	

	
	
}

