import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));
				
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any formatting or parsing issues are encountered
		if (fileScan.hasNextInt()) {
			ROWS = fileScan.nextInt(); //replace with initialization statements using values from file
		}
		else {
			throw new InvalidFileFormatException("Invalid row input.");
		}
		if (fileScan.hasNextInt()) {
			COLS = fileScan.nextInt();
		}
		else {
			throw new InvalidFileFormatException("Invalid column input.");
		}
				
		board = new char[ROWS][COLS];
		
		boolean startFound = false;
		boolean endFound = false;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				// checks for too little values
				if (!fileScan.hasNext()) {
					throw new InvalidFileFormatException("Invalid amount of values in grid.");
				}
				
				// assigns the value to the board.
				board[row][col] = fileScan.next().charAt(0);
				
				// checks for invalid characters
				if (!ALLOWED_CHARS.contains("" + board[row][col])) {
					throw new InvalidFileFormatException("" + board[row][col] + " is not an acceptable character.");
				}
				
				// checks for multiple starts and ends. 
				if (board[row][col] == '1' && startFound == false) {
					startingPoint = new Point(row, col);
					startFound = true;
				}
				else if (board[row][col] == '1' && startFound == true){
					throw new InvalidFileFormatException("Multiple starts points in grid.");
				}
				if (board[row][col] == '2' && endFound == false) {
					endingPoint = new Point(row, col);
					endFound = true;
				}
				else if (board[row][col] == '2' && endFound == true) {
					throw new InvalidFileFormatException("Multiple ending points in grid.");
				}
			}
		}
		if (fileScan.hasNext()) {
			throw new InvalidFileFormatException("Rows or columns in grid do not mach input.");
		}
		
		if (endFound == false || startFound == false) {
			throw new InvalidFileFormatException("No connection can be made. Grid doesn't have a start or end.");
		}
		
		// final check to determine if the rows match the rows designated by the grid. i.e. if the rows/cols were switched.
		Scanner rowScan = new Scanner(new File(filename));
		int rowCheck = -1;
		while(rowScan.hasNextLine()) {
			if (rowScan.nextLine().equals("")) {
				break;
			}
			rowCheck++;
		}
		if (rowCheck != ROWS) {
			throw new InvalidFileFormatException("Rows/Cols do not match values of grid.");
		}
		
		fileScan.close();
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
