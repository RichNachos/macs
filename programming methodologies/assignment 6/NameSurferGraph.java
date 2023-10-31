/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	private ArrayList<NameSurferEntry> entryList = new ArrayList<NameSurferEntry>();
	private Color[] colors = { Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW };
	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		//	 You fill in the rest //
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		entryList = new ArrayList<NameSurferEntry>();
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		if (!entryList.contains(entry))
			entryList.add(entry);
		update();
	}
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		addDecadeColumns();
		addDecadeLabels();
		addMarginLines();
		addEntryGraphs();
	}
	
	private void addDecadeColumns() {
		for (int i = 0; i < NDECADES; i++) {
			GLine decadeLine = new GLine(getWidth() / NDECADES * i, 0, getWidth() / NDECADES * i, getHeight());
			add(decadeLine);
		}
	}
	
	private void addDecadeLabels() {
		for (int i = 0; i < NDECADES; i++) {
			GLabel decadeLabel = new GLabel(Integer.toString(1900 + 10 * i));
			add(decadeLabel, getWidth() / NDECADES * i + DECADE_MARGIN, getHeight() - DECADE_MARGIN);
		}
	}
	
	private void addMarginLines() {
		GLine topMarginLine = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		add(topMarginLine);
		GLine bottomMarginLine = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add (bottomMarginLine);
	}
	
	private void addEntryGraphs() {
		int colorIndex = 0;
		for (NameSurferEntry entry : entryList) {
			Color color = colors[colorIndex % 4];
			for (int i = 0; i < NDECADES - 1; i++) {
				GLabel entryName = new GLabel(entry.getName() +" "+ entry.getRank(i));
				entryName.setColor(color);
				double PosX1 = getWidth() / NDECADES * i;
				double PosY1 = (double)entry.getRank(i) / 1000 * (getHeight() - GRAPH_MARGIN_SIZE * 2) + GRAPH_MARGIN_SIZE;
				double PosX2 = getWidth() / NDECADES * (i+1);
				double PosY2 = (double)entry.getRank(i+1) / 1000 * (getHeight() - GRAPH_MARGIN_SIZE * 2) + GRAPH_MARGIN_SIZE;
				
				if (entry.getRank(i) == 0) {
					PosY1 = getHeight() - GRAPH_MARGIN_SIZE;
					entryName.setLabel(entry.getName() + " *");
				}
				if (entry.getRank(i+1) == 0) {
					PosY2 = getHeight() - GRAPH_MARGIN_SIZE;
				}
				
				GLine entryLine = new GLine(PosX1,PosY1,PosX2,PosY2);
				entryLine.setColor(color);
				add(entryLine);
				add(entryName, PosX1 + 3, PosY1 - 3);
				
				if (i == NDECADES - 2) {
					GLabel lastEntryName = new GLabel(entry.getName() +" " + entry.getRank(i+1));
					lastEntryName.setColor(color);
					add(lastEntryName, PosX2 + 3, PosY2 - 3);
				}
			}
			colorIndex++;
		}
	}
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
