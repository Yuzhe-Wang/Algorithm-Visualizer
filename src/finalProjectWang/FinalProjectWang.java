package finalProjectWang;
// a program to visualize different kinds of sorting and path finding algorithms
//bubble sort, insertion sort, merge sort, quick sort, binary search, Dijkstra's algorithm, and A* 

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.shape.*;

import java.lang.reflect.Array;
import java.util.*;

import javafx.scene.paint.*;

public class FinalProjectWang extends Application{
	Random r;
	Driver dr;
	VBox controlPanel;
	ArrayList<String> algorithms = new ArrayList<>(Arrays.asList("Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Binary Search", "Dijkstra's Algorithm", "A* Pathfinding"));
	ArrayList<Button> mButtons;
	Pane root;
	VBox detailView;
	int mode; // labeling which algorithm to run
	Pane display;
	ArrayList<Double> data;
	ArrayList<Rectangle> mRecs;
	Rectangle target;
	Slider mSlider;
	Label foundLabel;
	Label instructionLabel;
	int inputSize;
	double width;
	double height;
	HBox RecView;
	
	//data member for the binary search algorithm
	int left;
	int right;
	
	
	public static void main( String[] args )
	{ launch(args);}
	
	// is called once when object is created
	public void init() {
        r = new Random();
        dr = new Driver();
        mode = 0;
        data = new ArrayList<>();
        mRecs = new ArrayList<>();
	}
	
    @Override
    public void start(Stage stage) {  
        stage.setTitle("Sorting and Pathing Finding Visilization Tool");
        root = new Pane();
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.show();
        setUpPanel();
        setUpButtons();
    }
    
    // setting up the control panel
    public void setUpPanel() {
    	controlPanel = new VBox();
    	mButtons = new ArrayList<>();
    	for(int i = 0; i < 7; ++i) {
    		Button oneButton = new Button();
    		oneButton.setText(algorithms.get(i));
    		mButtons.add(oneButton);
    		controlPanel.getChildren().add(oneButton);
    	}
    	root.getChildren().add(controlPanel);
    }
    
    // set up each individual buttons
    public void setUpButtons() {
    	bubbleSortInit(mButtons.get(0));
    	binarySearchInit(mButtons.get(4));
    }
    
    // set up functions for different buttons
    public void bubbleSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// set mode accordingly
    		mode = 1;
    		
    		// setup the window
    		root.getChildren().remove(display);
    		root.getChildren().remove(detailView);
    		display = new Pane();
    		root.getChildren().add(display);
    		display.setPrefSize(500,500);
    		display.setLayoutX(500);
    		detailView = new VBox();
    		
    		// a slider defining the scale of the input
    		mSlider = new Slider();
    		mSlider = new Slider(10,100,50);
    		mSlider.setMajorTickUnit(10);
    		mSlider.setSnapToTicks(true);
    		mSlider.setShowTickMarks(true);
    		mSlider.setShowTickLabels(true);
    		detailView.getChildren().add(mSlider);
    		
    		// label for the slider
    		Label sliderLabel = new Label();
    		sliderLabel.setText("Input Size");
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("Current comparison are colored in red, \nand sorted rectangles are colored in green");
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    		});
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			step();
    		});
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			setUpCanvas();
    		});
    		detailView.getChildren().add(resetButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(200);
    	});
    }
    
    public void binarySearchInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// set mode accordingly
    		mode = 5;
    		
    		//setup the window 
    		root.getChildren().remove(display);
    		root.getChildren().remove(detailView);
    		display = new Pane();
    		root.getChildren().add(display);
    		display.setPrefSize(500,500);
    		display.setLayoutX(500);
    		detailView = new VBox();
    		
    		// a slider defining the scale of the input
    		mSlider = new Slider();
    		mSlider = new Slider(10,100,50);
    		mSlider.setMajorTickUnit(10);
    		mSlider.setSnapToTicks(true);
    		mSlider.setShowTickMarks(true);
    		mSlider.setShowTickLabels(true);
    		detailView.getChildren().add(mSlider);
    		
    		// label for the slider
    		Label sliderLabel = new Label();
    		sliderLabel.setText("Input Size");
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("Current upper and lower bounds are colored in yellow, \nand the target is colored in white");
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    		});
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			step();
    		});
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			setUpCanvas();
    		});
    		detailView.getChildren().add(resetButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(200);
    	});
    }
    
    public void setUpCanvas() {
    	switch(mode) {
    	case 1: // for bubble sort
    		// remove the old result from detailView
    		detailView.getChildren().remove(foundLabel);
    		
    		// get the value of the slider and generate the visilization
    		display.getChildren().clear();
    		inputSize = (int) mSlider.getValue();
    		
    		//generate and add rectangles
			width = 500/inputSize;
			height = 0;
			RecView = new HBox();
			mRecs = new ArrayList<>();
			data = new ArrayList<>();
			for(int i = 0; i < inputSize; ++i) {
				height = r.nextDouble() * 500;
				data.add(height);
				Rectangle rec = new Rectangle(width,height);
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			
			//initialize a pointer, current rectangle
			int cur = 0;
			mRecs.get(cur).setFill(Color.RED);
			mRecs.get(cur + 1).setFill(Color.RED);
			display.getChildren().add(RecView);
			break;
    	
    	case 5: // for binary search
    		// remove the old result from detailView
    		detailView.getChildren().remove(foundLabel);
    		
    		// get the value of the slider and generate the visilization
			display.getChildren().clear();
			inputSize = (int) mSlider.getValue();
			
			//generate and add rectangles
			width = 500/inputSize;
			height = 0;
			RecView = new HBox();
			mRecs = new ArrayList<>();
			data = new ArrayList<>();
			for(int i = 0; i < inputSize; ++i) {
				height = r.nextDouble() * 500;
				data.add(height);
				Rectangle rec = new Rectangle(width,height);
				mRecs.add(rec);
			}
			// sort both the rectangles and store their height into the arraylist data
			Collections.sort(mRecs, new Comparator<Rectangle>() {
			    @Override
			    public int compare(Rectangle lhs, Rectangle rhs) {
			        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
			        return lhs.getHeight()> rhs.getHeight() ? -1 : (lhs.getHeight() < rhs.getHeight()) ? 1 : 0;
			    }
			});
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			
			// select a random target
			int index = (int) (r.nextDouble() * inputSize);
			target = mRecs.get(index);
			
			// initiate two pointers: left and right
			left = 0;
			right = inputSize - 1;
			mRecs.get(left).setFill(Color.YELLOW);
			mRecs.get(right).setFill(Color.YELLOW);
			target.setFill(Color.WHITE);
			
			display.getChildren().add(RecView);
			break;
    	}
    }
    
    // the inner driver class
    public class Driver extends AnimationTimer
    {
    	  @Override
       public void handle( long now ) {
          step();
       }
    }

     // take a single step of the simulation
     public void step()
     {
     	switch (mode) {
     		case 5: 
     			BinarySearchStep();
     			
     	}	
     }
     
     // different algorithms
     public void BinarySearchStep() {
    	 if(left > right) {
    		 dr.stop();
    	 } else {
    		 int mid = (left + right) / 2;
    		 if(mRecs.get(mid).getHeight() == target.getHeight()) {
    			 mRecs.get(mid).setFill(Color.RED);
    			 foundLabel = new Label();
    			 foundLabel.setText("Target Found!");
    			 detailView.getChildren().add(foundLabel);
    			 dr.stop();
    		 } else {
    			 if(mRecs.get(mid).getHeight() < target.getHeight()) {
    				 mRecs.get(right).setFill(Color.BLACK);
    				 right = mid;
    				 mRecs.get(mid).setFill(Color.YELLOW);
    			 } else {
    				 mRecs.get(left).setFill(Color.BLACK);
    				 left = mid;
    				 mRecs.get(left).setFill(Color.YELLOW);
    			 }
    		 }
    	 }
     }
}