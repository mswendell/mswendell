import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;




/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {

	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		//print out clear usage instructions when there are problems with
		// any command line args
		// See https://en.wikipedia.org/wiki/Usage_message for format and content guidance
		System.out.print("Invalid Command. Usage: java CircuitTracer [-q for queue|-s for stack] [-c for console | -g for GUI] filename.dat");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		//parse and validate command line args - first validation provided
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}
		
		//initialize an empty Storage object called stateStore that stores objects of type TraceState
		Storage<TraceState> stateStore;
		if (args[0].contains("-q")) {
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
		}
		else if (args[0].contains("-s")) {
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		}
		else {
			System.out.print("Invalid Command. Usage: java CircuitTracer [-q for queue|-s for stack] [-c for console | -g for gui] filename.dat");
			return;
		}
		
		//read in the CircuitBoard from the given file
		CircuitBoard newCircuit;
		try {
			newCircuit = new CircuitBoard(args[2]);
			//run the search for best paths
			
			//initialize an empty List called bestPaths that stores objects of type TraceState
			ArrayList<TraceState> bestPaths = new ArrayList<TraceState>();
			
			//add a new initial TraceState object (a path with one trace) to stateStore for each open position adjacent to the starting component
			TraceState pathSearch;
			if (newCircuit.isOpen(newCircuit.getStartingPoint().x + 1, newCircuit.getStartingPoint().y)) {
				pathSearch = new TraceState(newCircuit, newCircuit.getStartingPoint().x + 1, newCircuit.getStartingPoint().y);
				stateStore.store(pathSearch);
			}
			if (newCircuit.isOpen(newCircuit.getStartingPoint().x - 1, newCircuit.getStartingPoint().y)) {
				pathSearch = new TraceState(newCircuit, newCircuit.getStartingPoint().x - 1, newCircuit.getStartingPoint().y);
				stateStore.store(pathSearch);
			}
			if (newCircuit.isOpen(newCircuit.getStartingPoint().x, newCircuit.getStartingPoint().y + 1)) {
				pathSearch = new TraceState(newCircuit, newCircuit.getStartingPoint().x, newCircuit.getStartingPoint().y + 1);
				stateStore.store(pathSearch);
			}
			if (newCircuit.isOpen(newCircuit.getStartingPoint().x, newCircuit.getStartingPoint().y - 1)) {
				pathSearch = new TraceState(newCircuit, newCircuit.getStartingPoint().x, newCircuit.getStartingPoint().y - 1);
				stateStore.store(pathSearch);
			}
			
			//loops through each TraceState in stateStore
			while (!stateStore.isEmpty()) {
				TraceState newPath = stateStore.retrieve();
				if (newPath.isComplete()) { //checks to add new path to list of best paths
					if (bestPaths.isEmpty() || newPath.pathLength() == bestPaths.get(0).pathLength()) {
						bestPaths.add(newPath);
					}
					// checks if path is shorter and will empty best path list and add new one
					else if(newPath.pathLength() < bestPaths.get(0).pathLength()) {
						bestPaths.clear();
						bestPaths.add(newPath);
					}
				}
				else { // adds additional TraceStates to StateStore for each state open and adjacent to the current. 
					if (newPath.getBoard().isOpen(newPath.getRow() + 1, newPath.getCol())) {
						pathSearch = new TraceState(newPath, newPath.getRow() + 1, newPath.getCol());
						stateStore.store(pathSearch);
					}
					if (newPath.getBoard().isOpen(newPath.getRow() - 1, newPath.getCol())) {
						pathSearch = new TraceState(newPath, newPath.getRow() - 1, newPath.getCol());
						stateStore.store(pathSearch);
					}
					if (newPath.getBoard().isOpen(newPath.getRow(), newPath.getCol() + 1)) {
						pathSearch = new TraceState(newPath, newPath.getRow(), newPath.getCol() + 1);
						stateStore.store(pathSearch);
					}
					if (newPath.getBoard().isOpen(newPath.getRow(), newPath.getCol() - 1)) {
						pathSearch = new TraceState(newPath, newPath.getRow(), newPath.getCol() - 1);
						stateStore.store(pathSearch);
					}
					
				}
			}
			
			
			//output results to console or GUI, according to specified choice
			if (args[1].equals("-c")) {
				if (bestPaths.isEmpty()) {
					System.out.print("");
				}
				else {
					for (int i = 0; i < bestPaths.size(); i++) {
					System.out.println(bestPaths.get(i).toString());
					}
				}
			}
			else if (args[1].equals("-g")) {
				if (bestPaths.isEmpty()) {
					System.out.print("");
				}
				else {
					//System.out.print("GUI not available");
					GUICircuit newGUI = new GUICircuit(bestPaths);
				}
			}
			else {
				System.out.print("Invalid Command. Usage: java CircuitTracer [-q for queue|-s for stack] [-c for console | -g for gui] filename.dat");
				return;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			
		} 
		catch (InvalidFileFormatException e) {
			System.out.println(e.toString());
			}
		
	}
	
} // class CircuitTracer
