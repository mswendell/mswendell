import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
/**
 * The GUICircuit will generate a simple GUI showing a circuit grid and a solution found using 
 * the CircuitTracer class. The grid can be changed depending on the selected solution from the
 * ComboBox on the right.
 * @author Michael Wendell
 *
 */

public class GUICircuit extends JPanel {
	private int rows;
	private int cols;
	private JLabel[][] tileGrid;
	private JComboBox<String> solutionsBox;
	private JPanel tilePanel;
	private ArrayList<TraceState> paths;
	JFrame frame;
	
	/**
	 * Initializes the GUI and sets up the Jframe for the GUI.
	 * @param paths the solution paths as determined by the CircuitTracer class.
	 */
	public GUICircuit(ArrayList<TraceState> paths) {
		this.paths = paths;
		frame = new JFrame("Circuit Tracer Search");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(SetupGUI(paths));
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * The SetupGUI method will create a JPanel with the solution grid and changeable solutions box.
	 * When the solutions box is changed the ActionListener will re-draw the panel with a new solution.
	 * @param paths the solution paths as determined by the CircuitTracer class.
	 * @return Component the JPanel that is configured to display the solution grid on solutions
	 * panel.
	 */
	private Component SetupGUI(ArrayList<TraceState> paths) {
		Font bigFont = new Font("sanserif", Font.PLAIN, 48);
		Font smallFont = new Font("sanserif", Font.PLAIN, 20);
		
		rows = paths.get(0).getBoard().numRows();
		cols = paths.get(0).getBoard().numCols();
		
		//black border to show a grid.
		Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
		
		// panel to store each tile and create a grid.
		tilePanel = new JPanel();
		tilePanel.setLayout(new GridLayout(rows, cols));
		tileGrid = new JLabel[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				tileGrid[row][col] = new JLabel("" + paths.get(0).getBoard().charAt(row, col), JLabel.CENTER);
				tileGrid[row][col].setPreferredSize(new Dimension(75,75));
				tileGrid[row][col].setFont(bigFont);
				tileGrid[row][col].setBorder(blackBorder);
				tilePanel.add(tileGrid[row][col]);
				if (paths.get(0).getBoard().charAt(row, col) == 'T') { //changes solution tiles red.
					tileGrid[row][col].setForeground(Color.RED);
				}
			}
		}
		
		String[] numSolutions = new String[ paths.size()];
		for (int i = 0; i < paths.size(); i++) {
			numSolutions[i] = "Solution " + (i+1);
		}
		
		// panel to hold the solutions label and box.
		JPanel solutionPanel = new JPanel();
		solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.Y_AXIS));
		JLabel solutionsLabel = new JLabel("Solutions");
		solutionsLabel.setFont(smallFont);
		solutionPanel.add(solutionsLabel);
		JPanel solutionsSelect = new JPanel();
		solutionsSelect.setPreferredSize(new Dimension(200,400));
		solutionsSelect.setFont(smallFont);
		solutionsSelect.setEnabled(false);
		
		//box to change based on different number of solutions.
		solutionsBox = new JComboBox<String>(numSolutions);
		solutionsBox.setSelectedIndex(0);
		solutionsBox.setPreferredSize(new Dimension(200,50));
		solutionsSelect.add(solutionsBox);
		solutionsBox.addActionListener(new MyActionListener());
		solutionPanel.add(solutionsSelect);
		
		//final panel with all panels added.
		JPanel finalPanel = new JPanel();
		finalPanel.add(tilePanel);
		finalPanel.add(solutionPanel);
		
		return finalPanel;
	}
	
	/**
	 * MyActionListener class that will implement the action listener for the JComboBox. 
	 * This will detect if the JComboBox has been altered and repaint the grid based on the solution chosen. 
	 */
	private class MyActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			tilePanel.removeAll();
			int newIndex = solutionsBox.getSelectedIndex();
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					tileGrid[row][col] = new JLabel("" + paths.get(newIndex).getBoard().charAt(row, col), JLabel.CENTER);
					tileGrid[row][col].setPreferredSize(new Dimension(75,75));
					tileGrid[row][col].setFont(new Font("sanserif", Font.PLAIN, 48));
					tileGrid[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					tilePanel.add(tileGrid[row][col]);
					if (paths.get(newIndex).getBoard().charAt(row, col) == 'T') {
						tileGrid[row][col].setForeground(Color.RED);
					}
				}
			}
			// Repaints the screen after every change has been made.
			frame.revalidate();
		}
	}
}
