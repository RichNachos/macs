/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	private JTextField nameInput;
	private JButton graphButton;
	private JButton clearButton;
	private JLabel nameInputLabel;
	private NameSurferGraph graph;
	private NameSurferDataBase db;
	
	public void init() {
		// Initializes JLabel next to textfield
		nameInputLabel = new JLabel("Name");
		add(nameInputLabel, SOUTH);
		
		// Initializes input text field
	    nameInput = new JTextField(20);
	    add(nameInput, SOUTH);
	    nameInput.addActionListener(this);
	    
	    // Initializes graph button
	    graphButton = new JButton("Graph");
	    add(graphButton, SOUTH);
	    graphButton.addActionListener(this);
	    
	    // Initializes graph clear button
	    clearButton = new JButton("Clear");
	    add(clearButton, SOUTH);
	    clearButton.addActionListener(this);
	    
	    // Initializes the graph
	    graph = new NameSurferGraph();
	    add(graph);
	    
	    // Initializes the database to read names
	    db = new NameSurferDataBase(NAMES_DATA_FILE);
	}
/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nameInput || e.getSource() == graphButton) {
			NameSurferEntry entry = db.findEntry(nameInput.getText());
			if (entry != null) 
				graph.addEntry(entry);
		}
		if (e.getSource() == clearButton) {
			graph.clear();
		}
	}
}
