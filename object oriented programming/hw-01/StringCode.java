import java.io.EOFException;
import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.length() == 0)
			return 0;
		int maxRun = 1;
		int currRun = 1;
		char currChar = str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == currChar) {
				currRun++;
				maxRun = Math.max(maxRun, currRun);
			} else {
				currRun = 1;
				currChar = str.charAt(i);
			}
		}

		return maxRun;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		StringBuilder builder = new StringBuilder(str);
		int i = 0;
		while (i < builder.length()) {
			char c = builder.charAt(i);
			if (i == builder.length() - 1 && (c >= '0' && c <= '9')) {
				builder.deleteCharAt(i);
			} else {
				if (c >= '0' && c <= '9') {
					char k = builder.charAt(i + 1);
					builder.deleteCharAt(i);
					for (int j = 0; j < (int)(c - '0'); j++) {
						builder.insert(i, k);
					}
					i = i + (int)(c - '0');
				} else {
					i++;
				}
			}
		}
		return builder.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	// abc abc 3
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> s = new HashSet<String>();
		for (int i = 0; i < a.length() - len + 1; i++) {
			s.add(a.substring(i, i + len));
		}
		for (int i = 0; i < b.length() - len + 1; i++) {
			if (s.contains(b.substring(i, i + len))) {
				return true;
			}
		}
		return false;
	}
}
