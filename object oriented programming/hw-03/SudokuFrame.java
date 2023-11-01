import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame implements KeyListener {
	private JTextArea sourceArea;
	private JTextArea outputArea;
	private JButton checkButton;
	private JCheckBox autoCheck;
	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		setLayout(new BorderLayout(4,4));

		JPanel areas = new JPanel();
		// Source text area
		sourceArea = new JTextArea(15,20);
		sourceArea.addKeyListener(this);
		sourceArea.setBorder(new TitledBorder("Puzzle"));
		areas.add(sourceArea, BorderLayout.WEST);

		// Output text area
		outputArea = new JTextArea(15,20);
		outputArea.setBorder(new TitledBorder("Solution"));
		areas.add(outputArea, BorderLayout.EAST);

		add(areas, BorderLayout.NORTH);
		// Check button
		checkButton = new JButton("Check");
		checkButton.addActionListener(this::actionPerformed);

		// Auto check
		autoCheck = new JCheckBox("Auto");
		autoCheck.addActionListener(this::actionPerformed);

		// Add controls box at the bottom of the window
		JPanel controls = new JPanel();
		controls.add(checkButton);
		controls.add(autoCheck);
		add(controls, BorderLayout.WEST);

		// Could do this:
		// setLocationByPlatform(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == checkButton) {
			solveSudoku();
		}
	}

	 public void keyTyped(KeyEvent e) {}
	 public void keyPressed(KeyEvent e) {}
	 public void keyReleased(KeyEvent e) {
		if (autoCheck.isSelected()) {
			solveSudoku();
		}
	 }

	 private void solveSudoku() {
		 String text = sourceArea.getText();
		 try {
			 Sudoku sudoku = new Sudoku(Sudoku.textToGrid(text));
			 int solutions = sudoku.solve();
			 StringBuilder builder = new StringBuilder();
			 builder.append(sudoku.getSolutionText() + '\n');
			 builder.append("Solutions: " + solutions + '\n');
			 builder.append("Elapsed: " + sudoku.getElapsed() + '\n');
			 outputArea.setText(builder.toString());
		 } catch (Exception except) {
			 outputArea.setText("Parsing Error");
		 }
	 }
}
