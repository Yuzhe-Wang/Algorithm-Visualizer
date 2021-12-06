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
	
	// data members for the bubble sort algorithm
	int bubbleCur;
	int bubbleStep;
	
	// data member for the insertion sort algorithm
	int insertionCur;
	int insertionIterator;
	
	//data member for the merge sort algorithm
	Random rand = new Random();
	int low;
	int high;
	int step;
	
	// data members for the binary search algorithm
	int binaryLeft;
	int binaryRight;
	
	
	
	
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
    	insertionSortInit(mButtons.get(1));
    	mergeSortInit(mButtons.get(2));
    	binarySearchInit(mButtons.get(4));
    }
    
    // set up functions for different buttons
    public void bubbleSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
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
    
    public void insertionSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode accordingly
    		mode = 2;
    		
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
    		instructionLabel.setText("The sorted sublist is colored in green, \nand the current value is colored in yellow");
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
    
    public void mergeSortInit(Button b) {
    	b.setOnAction((ActionEvent e)-> {
    		// stop the previous process
    		dr.stop();
    		
    		// set mode accordingly
    		mode = 3;
    		
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
    		instructionLabel.setText("Elements in the same list have the same color");
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
    		// stop the previous process
    		dr.stop();
    		
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
			bubbleCur = 0;
			bubbleStep = 0;
			mRecs.get(bubbleCur).setFill(Color.GREEN);
			mRecs.get(bubbleCur + 1).setFill(Color.GREEN);
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
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			
			// initialize two pointer, current rectangle, and the rectangle it's being compared with (which is on the left of the current rectangle)
			insertionCur = 1;
			insertionIterator = insertionCur - 1;
			mRecs.get(insertionCur).setFill(Color.GREEN);
			mRecs.get(insertionCur - 1).setFill(Color.GREEN);
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
				rec.setFill(Color.rgb(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
				mRecs.add(rec);
			}
			for(Rectangle i: mRecs) {
				RecView.getChildren().add(i);
			}
			display.getChildren().add(RecView);
			
			// initiate the parameters
			low = 0;
			high = mRecs.size() - 1;
			//System.out.println(high);
			step = 1;
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
			binaryLeft = 0;
			binaryRight = inputSize - 1;
			mRecs.get(binaryLeft).setFill(Color.YELLOW);
			mRecs.get(binaryRight).setFill(Color.YELLOW);
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
    		// a dummy for loop to slow the animation down
    		for(int i =0; i< 50; ++i) {
    			
    		}
    		step();
    	}
    }

     // take a single step of the simulation
     public void step()
     {
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
     	
     	case 5: 
     		BinarySearchStep();
     		break;
     	}	
     }
     
     // different algorithms
     public void BubbleSortStep() {
    	 mRecs.get(bubbleCur).setFill(Color.GREEN);
    	 mRecs.get(bubbleCur + 1).setFill(Color.GREEN);
		 if(mRecs.get(bubbleCur).getHeight() > mRecs.get(bubbleCur + 1).getHeight()) {
    		 Rectangle temp = mRecs.get(bubbleCur);
    		 mRecs.set(bubbleCur,mRecs.get(bubbleCur+1));
    		 mRecs.set(bubbleCur+1,temp);
    		 
    		 RecView.getChildren().clear();
    		 for(Rectangle i: mRecs) {
    			 RecView.getChildren().add(i);
    		 }
    	 } 
		 mRecs.get(bubbleCur).setFill(Color.BLACK);
		 
		// color the already sorted rectangles in green
		 for(int i = mRecs.size() - bubbleStep; i < mRecs.size(); ++i) {
			 mRecs.get(i).setFill(Color.GREEN);
		 }
		 bubbleCur ++;
		 if(bubbleCur == mRecs.size() - 1 - bubbleStep) {
			 bubbleCur = 0;
			 bubbleStep ++;
		 }
		 if(bubbleStep == mRecs.size() - 1) {
			 foundLabel = new Label();
			 foundLabel.setText("Fully Sorted!");
			 detailView.getChildren().add(foundLabel);
			 mRecs.get(0).setFill(Color.GREEN);
			 dr.stop();
		 }
     }
     
     public void InsertionSortStep() {
    	 if(insertionCur == mRecs.size()) {
    		 foundLabel = new Label();
			 foundLabel.setText("Fully Sorted!");
			 detailView.getChildren().add(foundLabel);
			 mRecs.get(mRecs.size() - 1).setFill(Color.GREEN);
    		 dr.stop();
    	 } else {
    		Rectangle key = mRecs.get(insertionCur);
    		insertionIterator = insertionCur - 1;
    		mRecs.get(insertionCur).setFill(Color.YELLOW);
    		mRecs.get(insertionIterator).setFill(Color.YELLOW);
    		while(insertionIterator >= 0 && key.getHeight() < mRecs.get(insertionIterator).getHeight()) {
    			mRecs.set(insertionIterator + 1, mRecs.get(insertionIterator));
    			insertionIterator --;
    		}
    		mRecs.set(insertionIterator + 1, key);
    		for(int i = 0; i < insertionCur; ++i) {
	   			mRecs.get(i).setFill(Color.GREEN);
	   		 }
    		RecView.getChildren().clear();
	   		for(Rectangle i: mRecs) {
	   			 RecView.getChildren().add(i);
	   		 }
	   		insertionCur ++;
    	 }
     }
     
     public void MergeSortStep() {
    	 if(step > high - low) {
    		 dr.stop();
    	 } else {
    		 for(int i = low; i < high; i += 2*step) {
    			 int from = i;
    			 int mid = Integer.min(i + step - 1, high);
    			 int to = Integer.min(i + 2*step - 1, high);
    			 System.out.println();
    			 System.out.println("i: " + i);
    			 System.out.println("Step: " + step);
    			 System.out.println("From: " + from);
    			 System.out.println("Mid: " + mid);
    			 System.out.println("To: " + to);
    			 Merge(from,mid,to);
    		 }
    		System.out.println(mRecs.size());
    		RecView.getChildren().clear();
 	   		for(Rectangle i: mRecs) {
 	   			 RecView.getChildren().add(i);
 	   		 }
    		 step *= 2;
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
    	 while(first < x && second < y) {
    		 System.out.println("Left height: " + left.get(first).getHeight());
    		 System.out.println("Right height: " + right.get(second).getHeight());
    		 if(left.get(first).getHeight()<=right.get(second).getHeight()) {
    			 System.out.println("left");
    			 Rectangle temp = new Rectangle();
    			 temp.setWidth(left.get(first).getWidth());
        		 temp.setHeight(left.get(first).getHeight());
        		 temp.setFill(left.get(first).getFill());
    			 mRecs.set(curr,temp);
    			 first ++;
    		 } else {
    			 System.out.println("right");
    			 Rectangle temp = new Rectangle();
    			 temp.setWidth(right.get(second).getWidth());
        		 temp.setHeight(right.get(second).getHeight());
        		 temp.setFill(right.get(second).getFill());
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
    		 temp.setFill(left.get(first).getFill());
    		 mRecs.set(curr,temp);
    		 first ++;
    		 curr ++;
    	 }
    	 while(second < y) {
    		 Rectangle temp = new Rectangle();
    		 temp.setWidth(right.get(second).getWidth());
    		 temp.setHeight(right.get(second).getHeight());
    		 temp.setFill(right.get(second).getFill());
    		 mRecs.set(curr,temp);
    		 second ++;
    		 curr ++;
    	 }
     }
     
     public void BinarySearchStep() {
    	 if(binaryLeft> binaryRight) {
    		 dr.stop();
    	 } else {
    		 int mid = (binaryLeft + binaryRight) / 2;
    		 if(mRecs.get(mid).getHeight() == target.getHeight()) {
    			 mRecs.get(mid).setFill(Color.RED);
    			 foundLabel = new Label();
    			 foundLabel.setText("Target Found!");
    			 detailView.getChildren().add(foundLabel);
    			 dr.stop();
    		 } else {
    			 if(mRecs.get(mid).getHeight() < target.getHeight()) {
    				 mRecs.get(binaryRight).setFill(Color.BLACK);
    				 binaryRight = mid;
    				 mRecs.get(mid).setFill(Color.YELLOW);
    			 } else {
    				 mRecs.get(binaryLeft).setFill(Color.BLACK);
    				 binaryLeft = mid;
    				 mRecs.get(binaryLeft).setFill(Color.YELLOW);
    			 }
    		 }
    	 }
     }
     
     
}