import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece sq, l;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		l = new Piece(Piece.STICK_STR);
		sq = new Piece(Piece.SQUARE_STR);
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way

		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());

		assertEquals(2, sq.getWidth());
		assertEquals(2, sq.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0}, sq.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, sq.computeNextRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {0}, l.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, l.computeNextRotation().getSkirt()));
	}
	
	public void testEquals() {
		Piece nPiece = pyr4.computeNextRotation();
		assertTrue(pyr1.equals(nPiece));
		assertFalse(pyr1.equals(pyr4));

		assertFalse(pyr1.equals(new Object()));
		assertTrue(pyr1.equals(pyr1));

		assertFalse(l.equals(sq));
		assertFalse(l.computeNextRotation().equals(pyr1));
	}

	public void testFastRotations() {
		Piece[] pieces = Piece.getPieces();
		Piece pyramid = pieces[Piece.PYRAMID];
		assertTrue(pyramid.equals(pyramid));
		assertFalse(pyramid.equals(pyramid.fastRotation()));

		assertFalse(pyramid.equals(pyramid.fastRotation().fastRotation()));
		assertTrue(pyramid.equals(pyramid.fastRotation().fastRotation().fastRotation().fastRotation()));

		Piece square = pieces[Piece.SQUARE];
		assertTrue(square.equals(square));
		assertTrue(square.equals(square.fastRotation()));

		assertTrue(square.equals(square.fastRotation().fastRotation().fastRotation().fastRotation().fastRotation()));
	}

	public void testException() {
		try {
			Piece badPiece = new Piece("11 11 1 101 11");
			assertTrue(false);
		}
		catch (Exception e){
			// Exception happened
		}
	}
}
