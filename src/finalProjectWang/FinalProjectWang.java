package finalProjectWang;
// a program to visualize different kinds of sorting and path finding algorithms
//bubble sort, insertion sort, merge sort, quick sort, binary search

//All the buttons already have voice over enabled

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.shape.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

import javafx.scene.paint.*;

public class FinalProjectWang extends Application{
	Driver dr;
	Random r;
	VBox controlPanel;
	ArrayList<String> algorithms = new ArrayList<>(Arrays.asList("Bubble Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Binary Search"));
	ArrayList<Button> mButtons;
	Group root;
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
	AlgoData algo = new AlgoData();
	boolean running;
	
	public static void main( String[] args )
	{ launch(args);}
	
	// is called once when object is created
	public void init() {
        dr = new Driver();
        r = new Random();
        mode = 0;
        data = new ArrayList<>();
        mRecs = new ArrayList<>();
        running = false;
	}
	
    @Override
    public void start(Stage stage) {  
        stage.setTitle("Sorting Visilization Tool");
        root = new Group();
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.show();
        
        setupBackgroundImage();
        setupPanel();
        setupButtons();
    }
    
    // setup the background image
    public void setupBackgroundImage() {
    	Image image;
		try {
			image = new Image(new FileInputStream("background.jpeg"));
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(600); 
		    imageView.setFitWidth(1000); 
		    root.getChildren().add(imageView);
		} catch (FileNotFoundException e) {
			System.out.println("Here");
		}
    }
    
    // setup the control panel
    public void setupPanel() {
    	controlPanel = new VBox();
    	mButtons = new ArrayList<>();
    	for(int i = 0; i < 5; ++i) {
    		Button oneButton = new Button();
    		oneButton.setText(algorithms.get(i));
    		oneButton.setPrefWidth(100);
    		oneButton.setPrefHeight(50);
    		mButtons.add(oneButton);
    		controlPanel.getChildren().add(oneButton);
    	}
    	controlPanel.setLayoutY(150);
    	root.getChildren().add(controlPanel);
    }
    
    // set up each individual buttons
    /* Attention!!
     *  Yes, there will be a few duplicate codes for setting up the buttons.
     *  But each algorithm differ from each other.
     *  I still gave each algorighm its own init function so that the implementation is clear and adding new algorithms will be easier
     */
    
    public void setupButtons() {
    	bubbleSortInit(mButtons.get(0));
    	insertionSortInit(mButtons.get(1));
    	mergeSortInit(mButtons.get(2));
    	quickSortInit(mButtons.get(3));
    	binarySearchInit(mButtons.get(4));
    }
    
    // set up functions for different buttons
    public void bubbleSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode and firstTime accordingly
    		mode = 1;
    		algo.firstTime = true;
    		
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
    		sliderLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("Sorted rectangles are colored in blue,\nand unsorted rectangles are colored in black");
    	    instructionLabel.setAccessibleText("Sorted rectangles are colored in blue, and unsorted rectangles are colored in black");
    		instructionLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    			running = true;
    		});
    		startButton.setPrefWidth(100);
    		startButton.setPrefHeight(50);
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			if(!running) {
    				step();
    			}
    		});
    		stepButton.setPrefWidth(100);
    		stepButton.setPrefHeight(50);
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			running = false;
    			algo.firstTime = true;
    			setUpCanvas();
    		});
    		resetButton.setPrefWidth(100);
    		resetButton.setPrefHeight(50);
    		detailView.getChildren().add(resetButton);
    		
    		Button pauseButton = new Button();
    		pauseButton.setText("Pause");
    		pauseButton.setOnAction((ActionEvent h) -> {
    			if(running) {
    				dr.stop();
    				running = false;
    			}
    		});
    		pauseButton.setPrefWidth(100);
    		pauseButton.setPrefHeight(50);
    		detailView.getChildren().add(pauseButton);
    		
    		Button resumeButton = new Button();
    		resumeButton.setText("Resume");
    		resumeButton.setOnAction((ActionEvent h) -> {
    			if(!running) {
    				dr.start();
    				running = true;
    			}
    		});
    		resumeButton.setPrefWidth(100);
    		resumeButton.setPrefHeight(50);
    		detailView.getChildren().add(resumeButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(180);
    		detailView.setLayoutY(100);
    	});
    }
    
    public void insertionSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode and firstTime accordingly
    		mode = 2;
    		algo.firstTime = true;
    		
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
    		sliderLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("The sorted sublist is colored in blue, \nand the current value is colored in yellow");
    		instructionLabel.setAccessibleText("The sorted sublist is colored in blue, and the current value is colored in yellow");
    		instructionLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    			running = true;
    		});
    		startButton.setPrefWidth(100);
    		startButton.setPrefHeight(50);
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			if(!running) {
    				step();
    			}
    		});
    		stepButton.setPrefWidth(100);
    		stepButton.setPrefHeight(50);
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			running = false;
    			algo.firstTime = true;
    			setUpCanvas();
    		});
    		resetButton.setPrefWidth(100);
    		resetButton.setPrefHeight(50);
    		detailView.getChildren().add(resetButton);
    		
    		Button pauseButton = new Button();
    		pauseButton.setText("Pause");
    		pauseButton.setOnAction((ActionEvent h) -> {
    			if(running) {
    				dr.stop();
    				running = false;
    			}
    		});
    		pauseButton.setPrefWidth(100);
    		pauseButton.setPrefHeight(50);
    		detailView.getChildren().add(pauseButton);
    		
    		Button resumeButton = new Button();
    		resumeButton.setText("Resume");
    		resumeButton.setOnAction((ActionEvent h) -> {
    			if(!running) {
    				dr.start();
    				running = true;
    			}
    		});
    		resumeButton.setPrefWidth(100);
    		resumeButton.setPrefHeight(50);
    		detailView.getChildren().add(resumeButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(180);
    		detailView.setLayoutY(100);
    	});
    }
    
    public void mergeSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode and firstTime accordingly
    		mode = 3;
    		algo.firstTime = true;
    		
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
    		sliderLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("Elements in the same list have the same color");
    		instructionLabel.setAccessibleText("Elements in the same list have the same color");
    		instructionLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    			running = true;
    		});
    		startButton.setPrefWidth(100);
    		startButton.setPrefHeight(50);
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			if(!running) {
    				step();
    			}
    		});
    		stepButton.setPrefWidth(100);
    		stepButton.setPrefHeight(50);
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			running = false;
    			algo.firstTime = true;
    			setUpCanvas();
    		});
    		resetButton.setPrefWidth(100);
    		resetButton.setPrefHeight(50);
    		detailView.getChildren().add(resetButton);
    		
    		Button pauseButton = new Button();
    		pauseButton.setText("Pause");
    		pauseButton.setOnAction((ActionEvent h) -> {
    			if(running) {
    				dr.stop();
    				running = false;
    			}
    		});
    		pauseButton.setPrefWidth(100);
    		pauseButton.setPrefHeight(50);
    		detailView.getChildren().add(pauseButton);
    		
    		Button resumeButton = new Button();
    		resumeButton.setText("Resume");
    		resumeButton.setOnAction((ActionEvent h) -> {
    			if(!running) {
    				dr.start();
    				running = true;
    			}
    		});
    		resumeButton.setPrefWidth(100);
    		resumeButton.setPrefHeight(50);
    		detailView.getChildren().add(resumeButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(180);
    		detailView.setLayoutY(100);
    	});
    }
    
    public void quickSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode and firstTime accordingly
    		mode = 4;
    		algo.firstTime = true;
    		
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
    		sliderLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("Elements in the same sorted sublist \nare colored in the same color");
    		instructionLabel.setAccessibleText("Elements in the same sorted sublist are colored in the same color");
    		instructionLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    			running = true;
    		});
    		startButton.setPrefWidth(100);
    		startButton.setPrefHeight(50);
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			if(!running) {
    				step();
    			}
    		});
    		stepButton.setPrefWidth(100);
    		stepButton.setPrefHeight(50);
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			running = false;
    			algo.firstTime = true;
    			setUpCanvas();
    		});
    		resetButton.setPrefWidth(100);
    		resetButton.setPrefHeight(50);
    		detailView.getChildren().add(resetButton);
    		
    		Button pauseButton = new Button();
    		pauseButton.setText("Pause");
    		pauseButton.setOnAction((ActionEvent h) -> {
    			if(running) {
    				dr.stop();
    				running = false;
    			}
    		});
    		pauseButton.setPrefWidth(100);
    		pauseButton.setPrefHeight(50);
    		detailView.getChildren().add(pauseButton);
    		
    		Button resumeButton = new Button();
    		resumeButton.setText("Resume");
    		resumeButton.setOnAction((ActionEvent h) -> {
    			if(!running) {
    				dr.start();
    				running = true;
    			}
    		});
    		resumeButton.setPrefWidth(100);
    		resumeButton.setPrefHeight(50);
    		detailView.getChildren().add(resumeButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(180);
    		detailView.setLayoutY(100);
    	});
    }
    
    public void binarySearchInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode and firstTime accordingly
    		mode = 5;
    		algo.firstTime = true;
    		
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
    		sliderLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(sliderLabel);
    		
    		// add some instructional labels
    		instructionLabel = new Label();
    		instructionLabel.setText("Current upper and lower bounds are colored in yellow, \nand the target is colored in white");
    		instructionLabel.setAccessibleText("Current upper and lower bounds are colored in yellow,and the target is colored in white");
    		instructionLabel.setTextFill(Color.WHITE);
    		detailView.getChildren().add(instructionLabel);
    		
    		//set up the visualization with default values
    		setUpCanvas();
    		
    		// a start button, a step button and a reset button
    		Button startButton = new Button();
    		startButton.setText("Start");
    		startButton.setOnAction(f -> {
    			dr.start();
    			running = true;
    		});
    		startButton.setPrefWidth(100);
    		startButton.setPrefHeight(50);
    		detailView.getChildren().add(startButton);
    		
    		Button stepButton = new Button();
    		stepButton.setText("Step");
    		stepButton.setOnAction((ActionEvent g)->{
    			if(!running) {
    				step();
    			}
    		});
    		stepButton.setPrefWidth(100);
    		stepButton.setPrefHeight(50);
    		detailView.getChildren().add(stepButton);
    		
    		Button resetButton = new Button();
    		resetButton.setText("Reset");
    		resetButton.setOnAction((ActionEvent h)->{
    			dr.stop();
    			running = false;
    			algo.firstTime = true;
    			setUpCanvas();
    		});
    		resetButton.setPrefWidth(100);
    		resetButton.setPrefHeight(50);
    		detailView.getChildren().add(resetButton);
    		
    		Button pauseButton = new Button();
    		pauseButton.setText("Pause");
    		pauseButton.setOnAction((ActionEvent h) -> {
    			if(running) {
    				dr.stop();
    				running = false;
    			}
    		});
    		pauseButton.setPrefWidth(100);
    		pauseButton.setPrefHeight(50);
    		detailView.getChildren().add(pauseButton);
    		
    		Button resumeButton = new Button();
    		resumeButton.setText("Resume");
    		resumeButton.setOnAction((ActionEvent h) -> {
    			if(!running) {
    				dr.start();
    				running = true;
    			}
    		});
    		resumeButton.setPrefWidth(100);
    		resumeButton.setPrefHeight(50);
    		detailView.getChildren().add(resumeButton);
    		
    		root.getChildren().add(detailView);
    		detailView.setLayoutX(180);
    		detailView.setLayoutY(100);
    	});
    }
    
    public void setUpCanvas() {
    	switch(mode) {
    	case 1: case 6: // for bubble sort
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
				rec.setFill(Color.BLACK);
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			
			//initialize a pointer, current rectangle
			algo.bubbleCur = 0;
			algo.bubbleStep = 0;
			display.getChildren().add(RecView);
			break;
    	
    	case 2: // for insertion sort
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
				rec.setFill(Color.BLACK);
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			
			// initialize two pointer, current rectangle, and the rectangle it's being compared with (which is on the left of the current rectangle)
			algo.insertionCur = 1;
			algo.insertionIterator = algo.insertionCur - 1;
			mRecs.get(algo.insertionCur).setFill(Color.BLUE);
			mRecs.get(algo.insertionCur - 1).setFill(Color.BLUE);
			display.getChildren().add(RecView);
			break;
			
    	case 3: // for merge sort
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
				rec.setFill(Color.rgb(algo.rand.nextInt(255),algo.rand.nextInt(255),algo.rand.nextInt(255)));
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			display.getChildren().add(RecView);
			
			// initiate the parameters
			algo.low = 0;
			algo.high = mRecs.size() - 1;
			algo.step = 1;
			break;
			
    	case 4: // for quick sort
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
				rec.setFill(Color.rgb(algo.rand.nextInt(255),algo.rand.nextInt(255),algo.rand.nextInt(255)));
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			display.getChildren().add(RecView);
			
			// initiate the parameters
			algo.scale = 1;
			algo.start = 0;
			algo.end = mRecs.size() - 1;
			algo.mStack = new Stack<>();
			algo.mStack.push(new Pair(algo.start, algo.end));
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
			algo.binaryLeft = 0;
			algo.binaryRight = inputSize - 1;
			mRecs.get(algo.binaryLeft).setFill(Color.YELLOW);
			mRecs.get(algo.binaryRight).setFill(Color.YELLOW);
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
    		if(algo.firstTime) {
     			algo.lastTime = now;
     			algo.firstTime = false;
     		} else {
     			double delta = (now - algo.lastTime) * 1.0e-9;
     			if(delta > 0.5) {
     				algo.lastTime = now;
     				step();
     			}
     		}
    	}
    }

     // take a single step of the simulation
     public void step() {
     	switch (mode) {
     	case 1:
     		BubbleSortStep();
     		break;
     		
     	case 2:
     		InsertionSortStep();
     		break;
     	
     	case 3:
     		MergeSortStep();
     		break;
     	
     	case 4:
     		QuickSortStep();
     		break;
     		
     	case 5: 
     		BinarySearchStep();
     		break;
     		
     	case 6:
     		BubbleSortExtraDetailStep();
     		break;
     	}	
     }
     
     // different algorithms
     public void BubbleSortStep() {
    	 mRecs.get(algo.bubbleCur).setFill(Color.YELLOW);
    	 mRecs.get(algo.bubbleCur + 1).setFill(Color.YELLOW);
		 if(mRecs.get(algo.bubbleCur).getHeight() > mRecs.get(algo.bubbleCur + 1).getHeight()) {
    		 Rectangle temp = mRecs.get(algo.bubbleCur);
    		 mRecs.set(algo.bubbleCur,mRecs.get(algo.bubbleCur+1));
    		 mRecs.set(algo.bubbleCur+1,temp);
    		 
    		 RecView.getChildren().clear();
    		 for(Rectangle i: mRecs) {
    			 RecView.getChildren().add(i);
    		 }
    	 } 
		 mRecs.get(algo.bubbleCur).setFill(Color.BLACK);
		 
		 // color the already sorted rectangles in green
		 for(int i = mRecs.size() - algo.bubbleStep; i < mRecs.size(); ++i) {
			 mRecs.get(i).setFill(Color.BLUE);
		 }
		 algo.bubbleCur ++;
		 if(algo.bubbleCur == mRecs.size() - 1 - algo.bubbleStep) {
			 algo.bubbleCur = 0;
			 algo. bubbleStep ++;
		 }
		 if(algo.bubbleStep == mRecs.size() - 1) {
			 foundLabel = new Label();
			 foundLabel.setText("Fully Sorted!");
			 detailView.getChildren().add(foundLabel);
			 mRecs.get(0).setFill(Color.BLUE);
			 dr.stop();
		 }
		 
		 /*
		  * The following is another version of bubble sort, which doesn't show the comparisons.
		  * Leaving it here just for reference
		  */
//    	 if(algo.bubbleStep == mRecs.size() - 1) {
//			 foundLabel = new Label();
//			 foundLabel.setStyle("-fx-font: 24 arial;");
//			 foundLabel.setText("Fully Sorted!");
//			 foundLabel.setTextFill(Color.WHITE);
//			 detailView.getChildren().add(foundLabel);
//			 for(Rectangle i: mRecs) {
//				i.setFill(Color.BLUE);
//			 }
//			 dr.stop();
//		 } else {
//	    	 for(algo.bubbleCur = 0; algo.bubbleCur <(mRecs.size() - algo.bubbleStep - 1); ++algo.bubbleCur) {
//	    		 if(mRecs.get(algo.bubbleCur).getHeight() > mRecs.get(algo.bubbleCur + 1).getHeight()) {
//	        		 Rectangle temp = mRecs.get(algo.bubbleCur);
//	        		 mRecs.set(algo.bubbleCur,mRecs.get(algo.bubbleCur+1));
//	        		 mRecs.set(algo.bubbleCur+1,temp);
//	        	 }
//	    	 }
//	    	 // color the already sorted rectangles in blue
//    		 for(int i = mRecs.size() - algo.bubbleStep; i < mRecs.size(); ++i) {
//    			 mRecs.get(i).setFill(Color.BLUE);
//    		 }
//    		 RecView.getChildren().clear();
//    		 for(Rectangle j: mRecs) {
//    			 RecView.getChildren().add(j);
//    		 }
//    		 algo.bubbleStep ++;
//		 } 
     }
     
     public void BubbleSortExtraDetailStep() {
    	 mRecs.get(algo.bubbleCur).setFill(Color.RED);
    	 mRecs.get(algo.bubbleCur + 1).setFill(Color.RED);
		 if(mRecs.get(algo.bubbleCur).getHeight() > mRecs.get(algo.bubbleCur + 1).getHeight()) {
    		 Rectangle temp = mRecs.get(algo.bubbleCur);
    		 mRecs.set(algo.bubbleCur,mRecs.get(algo.bubbleCur+1));
    		 mRecs.set(algo.bubbleCur+1,temp);
    		 
    		 RecView.getChildren().clear();
    		 for(Rectangle i: mRecs) {
    			 RecView.getChildren().add(i);
    		 }
    	 } 
		 mRecs.get(algo.bubbleCur).setFill(Color.BLACK);
		 
		// color the already sorted rectangles in blue
		 for(int i = mRecs.size() - algo.bubbleStep; i < mRecs.size(); ++i) {
			 mRecs.get(i).setFill(Color.BLUE);
		 }
		 algo.bubbleCur ++;
		 if(algo.bubbleCur == mRecs.size() - 1 - algo.bubbleStep) {
			 algo.bubbleCur = 0;
			 algo.bubbleStep ++;
		 }
		 if(algo.bubbleStep == mRecs.size() - 1) {
			 foundLabel = new Label();
			 foundLabel.setStyle("-fx-font: 24 arial;");
			 foundLabel.setText("Fully Sorted!");
			 foundLabel.setTextFill(Color.WHITE);
			 detailView.getChildren().add(foundLabel);
			 mRecs.get(0).setFill(Color.BLUE);
			 dr.stop();
		 }
     }
     
     public void InsertionSortStep() {
    	 if(algo.insertionCur == mRecs.size()) {
    		 foundLabel = new Label();
    		 foundLabel.setStyle("-fx-font: 24 arial;");
			 foundLabel.setText("Fully Sorted!");
			 foundLabel.setTextFill(Color.WHITE);
			 detailView.getChildren().add(foundLabel);
			 mRecs.get(mRecs.size() - 1).setFill(Color.BLUE);
    		 dr.stop();
    	 } else {
    		Rectangle key = mRecs.get(algo.insertionCur);
    		algo.insertionIterator = algo.insertionCur - 1;
    		mRecs.get(algo.insertionCur).setFill(Color.YELLOW);
    		mRecs.get(algo.insertionIterator).setFill(Color.YELLOW);
    		while(algo.insertionIterator >= 0 && key.getHeight() < mRecs.get(algo.insertionIterator).getHeight()) {
    			mRecs.set(algo.insertionIterator + 1, mRecs.get(algo.insertionIterator));
    			algo.insertionIterator --;
    		}
    		mRecs.set(algo.insertionIterator + 1, key);
    		for(int i = 0; i < algo.insertionCur; ++i) {
	   			mRecs.get(i).setFill(Color.BLUE);
	   		 }
    		RecView.getChildren().clear();
	   		for(Rectangle i: mRecs) {
	   			 RecView.getChildren().add(i);
	   		 }
	   		algo.insertionCur ++;
    	 }
     }
     
     public void MergeSortStep() {
    	 if(algo.step > algo.high - algo.low) {
    		 foundLabel = new Label();
    		 foundLabel.setStyle("-fx-font: 24 arial;");
			 foundLabel.setText("Fully Sorted!");
			 foundLabel.setTextFill(Color.WHITE);
			 detailView.getChildren().add(foundLabel);
    		 dr.stop();
    	 } else {
    		 for(int i = algo.low; i < algo.high; i += 2*algo.step) {
    			 int from = i;
    			 int mid = Integer.min(i + algo.step - 1, algo.high);
    			 int to = Integer.min(i + 2*algo.step - 1, algo.high);
    			 Merge(from,mid,to);
    		 }
    		RecView.getChildren().clear();
 	   		for(Rectangle i: mRecs) {
 	   			 RecView.getChildren().add(i);
 	   		 }
    		 algo.step *= 2;
    	 }
     }
     
     // helper function for merge sort
     public void Merge(int from, int mid, int to) {
    	 // two temperoary vectors
    	 // left[p...q], right[q+1...r]
    	 ArrayList<Rectangle> left = new ArrayList<>();
    	 ArrayList<Rectangle> right = new ArrayList<>();
    	 
    	 for(int i = from; i <=mid; ++i) {
    		 Rectangle temp = new Rectangle();
    		 temp.setWidth(mRecs.get(i).getWidth());
    		 temp.setHeight(mRecs.get(i).getHeight());
    		 temp.setFill(mRecs.get(i).getFill());
    		 left.add(temp);
    	 }
    	 for(int j = mid + 1; j <= to; ++ j) {
    		 Rectangle temp = new Rectangle();
    		 temp.setWidth(mRecs.get(j).getWidth());
    		 temp.setHeight(mRecs.get(j).getHeight());
    		 temp.setFill(mRecs.get(j).getFill());
    		 right.add(temp);
    	 }
    	 
    	 // two pointers for merging
    	 int first = 0;
    	 int second = 0;
    	 int curr = from;
    	 
    	 // the merging
    	 int x = mid - from + 1;
    	 int y = to - (mid + 1) + 1;
    	 
    	 // we also need to "merge" the rectangle's color
    	 Paint mPaint;
    	 if(first < x && second < y) {
    		 if(left.get(first).getHeight() <= right.get(second).getHeight()) {
        		 mPaint = left.get(first).getFill();
        	 } else {
        		 mPaint = right.get(second).getFill();
        	 }
    	 } else {
    		 mPaint = mRecs.get(from).getFill();
    	 }
    	 
    	 while(first < x && second < y) {
    		 if(left.get(first).getHeight()<=right.get(second).getHeight()) {
    			 Rectangle temp = new Rectangle();
    			 temp.setWidth(left.get(first).getWidth());
        		 temp.setHeight(left.get(first).getHeight());
        		 temp.setFill(mPaint);
    			 mRecs.set(curr,temp);
    			 first ++;
    		 } else {
    			 Rectangle temp = new Rectangle();
    			 temp.setWidth(right.get(second).getWidth());
        		 temp.setHeight(right.get(second).getHeight());
        		 temp.setFill(mPaint);
    			 mRecs.set(curr,temp);
    			 second ++;
    		 }
    		 curr ++;
    	 }
    	 
    	 // when we reach the end of either one of left and right, copy the rest into the original vector
    	 while(first < x) {
    		 Rectangle temp = new Rectangle();
    		 temp.setWidth(left.get(first).getWidth());
    		 temp.setHeight(left.get(first).getHeight());
    		 temp.setFill(mPaint);
    		 mRecs.set(curr,temp);
    		 first ++;
    		 curr ++;
    	 }
    	 while(second < y) {
    		 Rectangle temp = new Rectangle();
    		 temp.setWidth(right.get(second).getWidth());
    		 temp.setHeight(right.get(second).getHeight());
    		 temp.setFill(mPaint);
    		 mRecs.set(curr,temp);
    		 second ++;
    		 curr ++;
    	 }
     }
     
     public void QuickSortStep() {
    	 if(algo.mStack.empty()) {
    		 algo.scale = 1;
    		 foundLabel = new Label();
			 foundLabel.setText("Fully Sorted!");
			 foundLabel.setStyle("-fx-font: 24 arial;");
			 foundLabel.setTextFill(Color.WHITE);
			 detailView.getChildren().add(foundLabel);
    		 dr.stop();
    	 } else {
    		 int temp = 1;
    		 for(int i = 0; i < algo.scale; ++i) {
    			 if(algo.mStack.empty()) {
    	    		 algo.scale = 1;
    	    		 foundLabel = new Label();
    				 foundLabel.setText("Fully Sorted!");
    				 foundLabel.setStyle("-fx-font: 24 arial;");
    				 foundLabel.setTextFill(Color.WHITE);
    				 detailView.getChildren().add(foundLabel);
    	    		 dr.stop();
    	    		 break;
    			 }
	    		 algo.start = algo.mStack.peek().getX();
	    		 algo.end = algo.mStack.peek().getY();
	    		 algo.mStack.pop();
	    		 
	    		 int pivot = QuickSortPartition(algo.start, algo.end);
	    		 
	    		 if(pivot - 1 > algo.start) {
	    			 algo.mStack.push(new Pair(algo.start,pivot - 1));
	    			 temp += 1;
	    		 } 
	    		 if(pivot + 1 < algo.end) {
	    			 algo.mStack.push(new Pair(pivot + 1, algo.end));
	    			 temp += 1;
	    		 }
    		 }
    		 algo.scale = temp;
    		 RecView.getChildren().clear();
  	   		 for(Rectangle j: mRecs) {
  	   			 RecView.getChildren().add(j);
  	   		 }
    	 }
     }
     
     // helper functions for the quick sort
     public void QuickSortSwap(int i, int j) {
    	 Rectangle temp = mRecs.get(i);
    	 mRecs.set(i,mRecs.get(j));
    	 mRecs.set(j,temp);
     }
     
     public int QuickSortPartition(int start, int end) {
    	 double pivot = mRecs.get(end).getHeight();
    	 int pivotIndex = start;
    	 for(int i = start; i < end; ++i) {
    		 if(mRecs.get(i).getHeight() <= pivot) {
    			 QuickSortSwap(i,pivotIndex);
    			 pivotIndex ++;
    		 }
    	 }
    	 QuickSortSwap(pivotIndex, end);
    	 return pivotIndex;
     }
     
     public void BinarySearchStep() {
    	 if(algo.binaryLeft> algo.binaryRight) {
    		 dr.stop();
    	 } else {
    		 int mid = (algo.binaryLeft + algo.binaryRight) / 2;
    		 if(mRecs.get(mid).getHeight() == target.getHeight()) {
    			 mRecs.get(mid).setFill(Color.RED);
    			 foundLabel = new Label();
    			 foundLabel.setStyle("-fx-font: 24 arial;");
    			 foundLabel.setText("Target Found!");
    			 foundLabel.setTextFill(Color.WHITE);
    			 detailView.getChildren().add(foundLabel);
    			 dr.stop();
    		 } else {
    			 if(mRecs.get(mid).getHeight() < target.getHeight()) {
    				 mRecs.get(algo.binaryRight).setFill(Color.BLACK);
    				 algo.binaryRight = mid;
    				 mRecs.get(mid).setFill(Color.YELLOW);
    			 } else {
    				 mRecs.get(algo.binaryLeft).setFill(Color.BLACK);
    				 algo.binaryLeft = mid;
    				 mRecs.get(algo.binaryLeft).setFill(Color.YELLOW);
    			 }
    		 }
    	 }
     }
}