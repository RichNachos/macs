/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.util.ArrayList;

import acm.util.*;
import acmx.export.java.io.FileReader;

public class HangmanLexicon {

	private ArrayList<String> Words = new ArrayList<String>();
	
	// This constructor writes words into the ArrayList
	public HangmanLexicon() {
		try {
			BufferedReader rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				else {
					Words.add(line);
				}
			}
			rd.close();
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
	}
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return Words.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return Words.get(index);
	}
}
