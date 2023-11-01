// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

import static org.junit.Assert.assertNotEquals;

public class TabooTest extends TestCase {
    private List<String> stringToList(String s) {
        List<String> list = new ArrayList<String>();
        for (int i=0; i<s.length(); i++) {
            list.add(String.valueOf(s.charAt(i)));
            // note: String.valueOf() converts lots of things to string form
        }
        return list;
    }

    public void testTaboo1() {
        List<String> rules = stringToList("acab");
        Taboo<String>  tb = new Taboo<String>(rules);

        Set<String> set = new HashSet<String>(stringToList("cb"));
        assertEquals(set, tb.noFollow("a"));

        set = new HashSet<String>(stringToList("a"));
        assertEquals(set, tb.noFollow("c"));

        assertNotEquals(set, tb.noFollow("b"));
        assertEquals(Collections.emptySet(), tb.noFollow("x"));
    }

    public void testTaboo2() {
        List<Integer> rules = Arrays.asList(1,2,1,3);
        Taboo<Integer>  tb = new Taboo<Integer>(rules);

        Set<Integer> set = new HashSet<Integer>(Arrays.asList(2,3));
        assertEquals(set, tb.noFollow(1));
        assertNotEquals(Collections.emptySet(), tb.noFollow(2));
        assertEquals(Collections.emptySet(), tb.noFollow(3));
        set.remove(2);
        assertNotEquals(set, tb.noFollow(1));
    }

    public void testTaboo3() {
        List<String> rules = stringToList("acab");
        Taboo<String>  tb = new Taboo<String>(rules);

        List<String> minim = stringToList("acbxca");
        tb.reduce(minim);

        assertEquals(stringToList("axc"), minim);
        assertEquals(Collections.emptySet(),tb.noFollow(null));

        minim = stringToList("acacacacacabb");
        tb.reduce(minim);
        assertNotEquals(stringToList("acacacacacabb") ,minim);
        assertEquals(stringToList("aaaaaa"), minim);
    }

    public void testTaboo4() {
        List<String> rules = stringToList("ac");
        rules.add(null); rules.add("b"); rules.add("a"); rules.add(null);
        Taboo<String>  tb = new Taboo<String>(rules);

        List<String> minim = stringToList("acbxbaca");
        tb.reduce(minim);

        assertEquals(stringToList("abxbca"), minim);
        assertEquals(Collections.emptySet(), tb.noFollow(null));
        assertEquals(Collections.emptySet(), tb.noFollow("c"));
    }
}
