
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {

	private HashMap<T, HashSet<T> > rules;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		this.rules = new HashMap<T, HashSet<T> >();
		for (int i = 0; i < rules.size() - 1; i++) {
			if (rules.get(i) != null && rules.get(i + 1) != null) {
				if (this.rules.containsKey(rules.get(i))) {
					this.rules.get(rules.get(i)).add(rules.get(i + 1));
				} else {
					HashSet<T> s = new HashSet<>();
					s.add(rules.get(i + 1));
					this.rules.put(rules.get(i), s);
				}
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if (rules.containsKey(elem)) {
			return rules.get(elem);
		}
		return Collections.emptySet();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		Iterator<T> it = list.listIterator();
		T prev = it.next();
		while (it.hasNext()) {
			T next = it.next();
			if (noFollow(prev).contains(next)) {
				it.remove();
			} else {
				prev = next;
			}
		}
	}
}
