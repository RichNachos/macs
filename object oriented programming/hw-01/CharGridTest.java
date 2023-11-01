
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;
import org.junit.Test;

public class CharGridTest extends TestCase {
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}

	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testCharArea3() {
		char[][] grid = new char[][] {
				{'c', 'b', 'a'},
				{'b', ' ', ' '},
				{'c', 'a', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(6, cg.charArea('a'));
		assertEquals(4, cg.charArea('b'));
		assertEquals(3, cg.charArea('c'));
	}

	public void testCharArea4() {
		char[][] grid = new char[][] {
				{'c', 'b', 'a', 'c'},
				{'b', ' ', 'a', 'c'},
				{'c', 'a', ' ', 'c'},
				{' ', ' ', 'b', 'c'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(6, cg.charArea('a'));
		assertEquals(12, cg.charArea('b'));
		assertEquals(16, cg.charArea('c'));
		assertEquals(0, cg.charArea('x'));
	}

	public void testCountPlus1() {
		char[][] grid = new char[][] {
				{' ', 'b', ' ', ' '},
				{'b', 'b', 'b', ' '},
				{' ', 'b', ' ', ' '},
				{'a', 'a', 'a', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus2() {
		char[][] grid = new char[][] {
				{'a', 'x', 'b', 'x', 'x', 'c', ' '},
				{'x', 'a', 'b', 'x', 'c', 'c', 'c'},
				{'b', 'b', 'b', 'b', 'b', 'c', ' '},
				{'x', 'x', 'b', 'x', 'a', 'c', ' '},
				{'x', 'x', 'b', 'a', 'a', 'a', ' '},
				{'x', 'x', 'x', ' ', 'a', ' ', ' '},
				{' ', 'x', ' ', ' ', ' ', ' ', ' '},
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(2, cg.countPlus());
	}

	public void testCountPlus3() {
		char[][] grid = new char[][] {
				{' ', ' ', ' ', ' ', 'c', 'c', ' '},
				{' ', ' ', 'b', ' ', 'c', 'c', 'c'},
				{' ', 'b', 'b', 'b', 'a', 'c', ' '},
				{' ', ' ', 'b', 'x', 'a', ' ', ' '},
				{' ', 'x', 'a', 'a', 'a', 'a', 'a'},
				{'x', 'x', 'x', 'a', 'a', ' ', ' '},
				{' ', 'x', 'x', ' ', 'a', ' ', ' '},
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(4, cg.countPlus());
	}
}
