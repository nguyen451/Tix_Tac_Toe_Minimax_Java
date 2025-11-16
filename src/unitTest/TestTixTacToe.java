package unitTest;

import main.*;

public class TestTixTacToe {
    public static GamePanel gp = new GamePanel();

    public static void main(String[] args) {
        // run all test
        testFindBestChoice();
    }

    public static void testFindBestChoice() {
        // best move must be 2;2

        char[][] stboard = {{ 'o', 'x', 'o' },
                      { 'x', 'x', 'o' },
                      { '_', '_', '_' }};
        gp.setBoard(stboard);
        ChosenBlock cb = gp.findBestMove();
        System.out.println("The bestimal move is: ");
        System.out.println("Row: " + cb.row + " Col: " + cb.col);
    }
}
