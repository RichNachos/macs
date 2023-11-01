import javax.accessibility.AccessibleValue;
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	private Spot[][] grid;
	public class Spot {
		private final int Row;
		private final int Col;
		private int Value;
		public Set<Integer> AvailableIntegers;
		public Spot(int row, int col) {
			if (row < 0 || col < 0 || row > 8 || col > 8) {
				throw new RuntimeException("Illegal Value");
			}
			Row = row;
			Col = col;
			Value = 0;
			AvailableIntegers = new HashSet<Integer>();
		}
		public Spot(int value, int row, int col) {
			if (value < 0 || row < 0 || col < 0 || value > 9 || row > 8 || col > 8) {
				throw new RuntimeException("Illegal Value");
			}
			Row = row;
			Col = col;
			Value = value;
			AvailableIntegers = new HashSet<Integer>();
		}
		public int getRow() {
			return Row;
		}
		public int getCol() {
			return Col;
		}
		public int getValue() {
			return Value;
		}
		public void setValue(int value) {
			if (value < 0 || value > 8)
				throw new RuntimeException("Illegal Value");
			Value = value;
		}
	}

	public class SpotComparator implements Comparator<Spot>{
		@Override
		public int compare(Spot a, Spot b) {
			return a.AvailableIntegers.size() - b.AvailableIntegers.size();
		}
	}
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	private List<Spot> orderOfSpots;
	private int solutions;
	private long timeElapsed;
	private String savedSolution;
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = new Spot[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Spot sp = new Spot(ints[i][j], i, j);
				grid[i][j] = sp;
			}
		}

		orderOfSpots = calculateOrderOfSpots(grid);
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		solveRecursion(grid, 0);
		long stopTime = System.currentTimeMillis();
		timeElapsed = stopTime - startTime;
		return solutions;
	}
	private void solveRecursion(Spot[][] currGrid, int spotNum) {
		if (orderOfSpots.size() <= spotNum) {
			solutions++;
			if (solutions == 1) {
				savedSolution = buildSolutionGrid(currGrid);
			}
			return;
		}
		if (solutions >= MAX_SOLUTIONS) {
			return;
		}
		Spot currSpot = orderOfSpots.get(spotNum);
		Set<Integer> currAvailable = calculateAvailableIntegers(currGrid, currSpot);

		int subSolutions = 0;
		for (Integer num : currAvailable) {
			Spot[][] newGrid = new Spot[SIZE][];
			for (int i = 0; i < SIZE; i++) {
				newGrid[i] = currGrid[i].clone();
			}

			newGrid[currSpot.getRow()][currSpot.getCol()] = new Spot(num, currSpot.getRow(), currSpot.getCol());
			solveRecursion(newGrid, spotNum + 1);
		}
	}
	
	public String getSolutionText() {
		return savedSolution;
	}
	
	public long getElapsed() {
		return timeElapsed;
	}

	// Returns a sorted by available integers Spot list which can be used to
	// determine in what order we should start filling Sudoku board
	private List<Spot> calculateOrderOfSpots(Spot[][] grid) {
		List<Spot> order = new ArrayList<Spot>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Spot sp = grid[i][j];
				if (sp.getValue() == 0) {
					sp.AvailableIntegers =  calculateAvailableIntegers(grid, sp);
					order.add(sp);
				}
			}
		}
		order.sort(new SpotComparator());

		return order;
	}

	// Calculates at this moment the integers which can be written in the given Spot
	// If there already is a value assigned to given spot the set will |NOT| contain that integer
	private Set<Integer> calculateAvailableIntegers(Spot[][] currGrid, Spot sp) {
		Set<Integer> AvailableIntegers = new HashSet<Integer>();
		for (int i = 0; i < SIZE; i++) {
			AvailableIntegers.add(i+1);
		}

		for (int i = 0; i < SIZE; i++) {
			AvailableIntegers.remove(currGrid[sp.getRow()][i].getValue());
			AvailableIntegers.remove(currGrid[i][sp.getCol()].getValue());
		}
		int x = sp.getRow() / PART; int y = sp.getCol() / PART;
		for (int i = x * PART; i < x * PART + PART; i++) {
			for (int j = y * PART; j < y * PART + PART; j++) {
				AvailableIntegers.remove(currGrid[i][j].getValue());
			}
		}
		return AvailableIntegers;
	}

	private String buildSolutionGrid(Spot[][] currGrid) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				builder.append(currGrid[i][j].getValue());
			}
			builder.append('\n');
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

}
