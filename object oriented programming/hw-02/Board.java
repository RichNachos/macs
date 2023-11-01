// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private int[] widths;
	private int[] heights;

	private int maxHeight;

	// BACKUP
	private boolean[][] savedGrid;
	private int[] savedWidths;
	private int[] savedHeights;
	private int savedMaxHeight;

	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.maxHeight = 0;
		this.savedMaxHeight = 0;
		grid = new boolean[width][height];
		savedGrid = new boolean[width][height];
		this.widths = new int[height];
		this.heights = new int[width];
		this.savedWidths = new int[height];
		this.savedHeights = new int[width];
		committed = true;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return this.maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
			// Column Heights and Max height
			int maxHeight = 0;
			for (int x = 0; x < this.width; x++) {
				int currHeight = 0;
				for (int y = 0; y < this.height; y++) {
					if (this.grid[x][y])
						currHeight = y + 1;
				}
				if (this.heights[x] != currHeight) {
					throw new RuntimeException("Column height incorrect");
				}
				maxHeight = Math.max(currHeight, maxHeight);
			}
			if (this.maxHeight != maxHeight) {
				throw new RuntimeException("Max column height incorrect");
			}

			// Widths
			for (int y = 0; y < this.height; y++) {
				int count = 0;
				for (int x = 0; x < this.width; x++) {
					if (this.grid[x][y])
						count++;
				}
				if (this.widths[y] != count) {
					throw new RuntimeException("Row width incorrect");
				}
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int y = 0;
		for (int i = x; i < Math.min(x + piece.getWidth(), this.width); i++) {
			y = Math.max(heights[i] - piece.getSkirt()[i - x], y);
		}
		return y;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return true;
		return this.grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		committed = false;
		backup();
		int result = PLACE_OK;

		TPoint[] body = piece.getBody();
		for (int i = 0; i < body.length; i++) {
			TPoint point = body[i];
			if (x + point.x < 0 || y + point.y < 0 || x + point.x >= width || y + point.y >= height)
				return PLACE_OUT_BOUNDS;
			if (this.grid[x + point.x][y + point.y])
				return PLACE_BAD;
			this.grid[x + point.x][y + point.y] = true;
			this.widths[y + point.y]++;
			this.heights[x + point.x] = Math.max(this.heights[x + point.x], y + point.y + 1);
			this.maxHeight =  Math.max(this.heights[x + point.x], this.maxHeight);

			if (this.widths[y + point.y] == this.width) {
				result = PLACE_ROW_FILLED;
			}
		}

		sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		if (committed == true) {
			committed = false;
			backup();
		}

		for (int y = 0; y < this.getMaxHeight(); y++) {
			if (widths[y] == width) {
				rowsCleared++;
				for (int x = 0; x < width; x++) {
					grid[x][y] = false;
				}
				widths[y] = 0;
			} else if (rowsCleared != 0) {
				widths[y - rowsCleared] = widths[y];
				widths[y] = 0;
				for (int x = 0; x < width; x++) {
					grid[x][y - rowsCleared] = grid[x][y];
					grid[x][y] = false;
				}
			}
		}
		updateHeights();
		this.maxHeight -= rowsCleared;

		sanityCheck();
		return rowsCleared;
	}

	// Move the whole grid down by given integer
	private void updateHeights() {
		for (int x = 0; x < width; x++) {
			int h = 0;
			for (int y = 0; y < height; y++) {
				if (grid[x][y])
					h = y + 1;
			}
			heights[x] = h;
		}
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed) return;
		committed = true;

		boolean[][] tempGrid = savedGrid;
		savedGrid = grid;
		grid = tempGrid;

		int[] tempWidths = savedWidths;
		savedWidths = widths;
		widths = tempWidths;

		int[] tempHeights = savedHeights;
		savedHeights = heights;
		heights = tempHeights;

		maxHeight = savedMaxHeight;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		backup();
		committed = true;
	}

	private void backup() {
		// grid matrix
		for (int x = 0; x < width; x++) {
			System.arraycopy(grid[x], 0, savedGrid[x], 0, grid[x].length);
		}
		// widths, heights arrays and maxHeight
		System.arraycopy(widths, 0, savedWidths, 0, widths.length);
		System.arraycopy(heights, 0, savedHeights, 0, heights.length);
		savedMaxHeight = maxHeight;
	}

	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


