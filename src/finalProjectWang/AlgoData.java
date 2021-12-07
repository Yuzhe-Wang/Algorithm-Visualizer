package finalProjectWang;

import java.util.Random;
import java.util.Stack;

public class AlgoData {
	// a new rand for generating the random colors of the rectangles
		Random rand = new Random();
		
		// a variable to slow down the driver
		long lastTime = 0;
		boolean firstTime = true;
		
		// data members for the bubble sort algorithm
		int bubbleCur;
		int bubbleStep;
		
		// data member for the insertion sort algorithm
		int insertionCur;
		int insertionIterator;
		
		//data members for the merge sort algorithm
		int low;
		int high;
		int step;
		
		//data members for the quick sort algorithm
		Stack<Pair> mStack;
		int start;
		int end;
		int scale = 1;
		
		// data members for the binary search algorithm
		int binaryLeft;
		int binaryRight;
}
