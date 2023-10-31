import java.io.BufferedReader;
import java.util.ArrayList;

import acmx.export.java.io.FileReader;
import acmx.export.java.util.List;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	
	ArrayList<NameSurferEntry> entryList = new ArrayList<NameSurferEntry>();
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename));
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				else {
					entryList.add(new NameSurferEntry(line));
				}
			}
			rd.close();
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		for (int i = 0; i < entryList.size(); i++) {
			if (entryList.get(i).getName().equals(name))
				return entryList.get(i);
		}
		return null;
	}
}

