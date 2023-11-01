import junit.framework.TestCase;

public class SudokuTest extends TestCase {
    public void test1() {
        int[][] grid = Sudoku.stringsToGrid(
                "123456789",
                "456789123",
                "789123456",
                "234567891",
                "567891234",
                "891234567",
                "345678912",
                "678912345",
                "912345670"
        );
        String answer = "123456789\n456789123\n789123456\n234567891\n567891234\n891234567\n345678912\n678912345\n912345678";
        Sudoku sudoku = new Sudoku(grid);
        int solutions = sudoku.solve();
        assertEquals(1, solutions);
        String calculatedAnswer = sudoku.getSolutionText();
        assertEquals(answer, calculatedAnswer);
    }
}
