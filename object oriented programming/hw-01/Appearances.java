import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap<T, Integer> s1 = new HashMap<T, Integer>();
		HashMap<T, Integer> s2 = new HashMap<T, Integer>();
		int count = 0;
		for (T item : a) {
			if (s1.containsKey(item)) {
				Integer i = s1.get(item);
				i++;
				s1.put(item, i);
			} else {
				s1.put(item, 1);
			}
		}
		for (T item : b) {
			if (s2.containsKey(item)) {
				Integer i = s2.get(item);
				i++;
				s2.put(item, i);
			} else {
				s2.put(item, 1);
			}
		}

		for (T item : s1.keySet()) {
			if (s2.keySet().contains(item) && s1.get(item) == s2.get(item)) {
				count++;
			}
		}

		return count; // YOUR CODE HERE
	}
	
}
