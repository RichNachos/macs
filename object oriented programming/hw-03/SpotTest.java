import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpotTest extends TestCase {
    private Sudoku sud;
    private Sudoku.Spot sp;
    protected void setUp() throws Exception {
        super.setUp();

        sud = new Sudoku(Sudoku.easyGrid);
        sp = sud.new Spot(0, 0, 0);
    }
    public void test1() {
        assertEquals(0, sp.getCol());
        assertEquals(0, sp.getRow());
        assertEquals(0, sp.getValue());
        sp.setValue(1);
        assertEquals(1, sp.getValue());
    }
    public void test2() {
        try {
            sp = sud.new Spot(9, 9, 9);
        }
        catch (Exception e) {
            // passed
        }
        assertEquals(Collections.emptySet(), sp.AvailableIntegers);
    }
}
