import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	public void testBasic1() {
		b.commit();
		int y = b.dropHeight(sRotated, 1);
		assertEquals(1, y);
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		b.commit();

		result = b.place(new Piece(Piece.SQUARE_STR), -1, -1);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getMaxHeight());

		y = b.dropHeight(new Piece(Piece.STICK_STR), 0);
		assertEquals(1, y);
		result = b.place(new Piece(Piece.STICK_STR), 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.commit();
		assertEquals(5, b.getColumnHeight(0));
		assertEquals(5, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(4));
		assertEquals(3, b.getRowWidth(0));
	}

	public void testBasic2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());

		result = b.clearRows();
		assertEquals(1, result);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(3, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
	}

	public void testAdvanced1() {
		b.undo(); // Remove pyramid
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));

		int result = b.place(pyr1, 0, b.dropHeight(pyr1, 0));
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.commit();
		result = b.place(pyr1, 0, b.dropHeight(pyr1, 0));
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.commit();
		result = b.place(pyr1, 0, b.dropHeight(pyr1, 0));
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.commit();
		result = b.place(pyr1, 0, b.dropHeight(pyr1, 0));
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		result = b.place(pyr1, 0, 2);
		assertEquals(Board.PLACE_BAD, result);
		b.undo();
		assertEquals(5, b.getColumnHeight(0));
		assertEquals(6, b.getColumnHeight(1));
		assertEquals(5, b.getColumnHeight(2));
		assertEquals(6, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(3, b.getRowWidth(4));
		assertEquals(1, b.getRowWidth(5));
		b.clearRows();
		b.undo();
		assertEquals(5, b.getColumnHeight(0));
		assertEquals(6, b.getColumnHeight(1));
		assertEquals(5, b.getColumnHeight(2));
		assertEquals(6, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(3, b.getRowWidth(4));
		assertEquals(1, b.getRowWidth(5));
		b.clearRows();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(3, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
	}
}
