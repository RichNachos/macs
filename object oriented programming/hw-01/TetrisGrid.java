//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

import java.util.Arrays;

public class TetrisGrid {

	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		for (int y = 0; y < grid[0].length; y++) {
			boolean toBeCleared = true;
			for (int x = 0; x < grid.length; x++) {
				if (grid[x][y] == false) {
					toBeCleared = false;
					pushDown(y);
					break;
				}
			}

			if (toBeCleared) {
				for (int x = 0; x < grid.length; x++) {
					grid[x][y] = false;
				}
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}

	private void pushDown(int y) {
		while (y > 0) {
			for (int x = 0; x < grid.length; x++) {
				if (grid[x][y-1]) {
					return;
				}
			}
			for (int x = 0; x < grid.length; x++) {
				grid[x][y-1] = grid[x][y];
				grid[x][y] = false;
			}
			y--;
		}
	}
}
