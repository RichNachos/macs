// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int minX = Integer.MAX_VALUE,minY = Integer.MAX_VALUE,maxX = Integer.MIN_VALUE,maxY = Integer.MIN_VALUE;
		boolean found = false;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (grid[row][col] == ch) {
					found = true;
					minX = Integer.min(minX, row);
					minY = Integer.min(minY, col);
					maxX = Integer.max(maxX, row);
					maxY = Integer.max(maxY, col);
				}
			}
		}
		if (found) return (maxX + 1 - minX) * (maxY + 1 - minY);
		return 0;
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int count = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				if (isPlus(grid[row][col], row, col))
					count++;
			}
		}

		return count;
	}

	private boolean isPlus(char ch, int row, int col) {
		int offset = 1;
		while (true) {
			int same = 0;
			if (row - offset >= 0 && grid[row - offset][col] == ch)
				same++;
			if (row + offset < grid.length && grid[row + offset][col] == ch)
				same++;
			if (col - offset >= 0 && grid[row][col - offset] == ch)
				same++;
			if (col + offset < grid[0].length && grid[row][col + offset] == ch)
				same++;

			if (same == 4)
				offset++;
			else if (same == 0 && offset != 1)
				return true;
			else
				return false;
		}
	}
}
