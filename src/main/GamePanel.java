package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
    final int FPS = 60; // frame per sec??
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();

    // STATE BOARD
    // initial state : all empty, 'x', 'o'
    char[][] stboard = new char[board.MAX_ROW][board.MAX_COL];

    // BLOCKS
    ChosenBlock chosenB = null;

    // WINNER
    String result; // You win!, draw, You lose!

    // BOOLEANS
    boolean gameEnd = false;
    boolean isPlayer1Turn = true; // player! is player, player2 is the machine

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        // additional methods
        setStartBoard(); // set up start board with all empty block
    }

    public void setStartBoard() {
        for (int row=0; row<stboard.length; row++) {
            for (int col=0; col<stboard[0].length; col++) {
                stboard[row][col] = '_';
            }
        }
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // GAME LOOP
        double drawnInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawnInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (!gameEnd) {
            if (isPlayer1Turn) { // noted by x
                // player turn: capture player's choice by mouse click, check valid move, check status and done
                
                // MOVE CONFIRMED
                if (mouse.pressed) {
                    chosenB = new ChosenBlock(mouse.x / board.SQUARE_SITE, mouse.y / board.SQUARE_SITE);
                    if (chosenB.isValid(stboard)) {
                        stboard[chosenB.row][chosenB.col] = 'x';
                        // only change the turn when player finishied his choice
                        changePlayerTurn();
                        gameEnd = checkEndGame(stboard);
                    }
                }
            } else {
                // Machine turn, run minimax and find best move
                // find best move by minimax
                chosenB = findBestMove();
                // take the move
                stboard[chosenB.row][chosenB.col] = 'o';
                // change player turn
                changePlayerTurn();
                gameEnd = checkEndGame(stboard);
            }
        } else {

        }
    }

    /*
     * The machine is the maximizer
     * The player is the minimizer
     */
    public ChosenBlock findBestMove() {
        ChosenBlock best = null;
        int score = 0;
        int bestScore = Integer.MIN_VALUE;
        boolean isMaximizer = true; // this is the turn of the machine

        for (int row =0; row <stboard.length; row++) {
            for (int col=0; col <stboard[0].length; col++) {
                if (stboard[row][col] == '_') {
                    // make simulation
                    stboard[row][col] = 'o'; // the machine is represented by o
                    score = minimaxScore(stboard, ! isMaximizer);
                    if (score > bestScore) {
                        bestScore = score;
                        best = new ChosenBlock(col, row);
                    }
                    // restore the status before simulation
                    stboard[row][col] = '_';
                }
            }
        }
        return best;
    }

    public int minimaxScore(char[][] stboard, boolean isMaximizer) {
        if (! isMoveLeft(stboard)) {
            return evaluate(stboard);
        }

        int score = 0;
        int bestScore;
        if (isMaximizer) {
            bestScore = Integer.MIN_VALUE;
        } else {
            bestScore = Integer.MAX_VALUE;
        }

        for (int row = 0; row < stboard.length; row++) {
            for (int col = 0; col < stboard[0].length; col ++) {
                if (stboard[row][col] == '_') {
                    // make simulation
                    stboard[row][col] = 'o';
                    score = minimaxScore(stboard, ! isMaximizer);
                    if (score > bestScore) {
                        bestScore = score;
                    }
                    // restore the status before simulation
                    stboard[row][col] = '_';
                }
            }
        }

        return bestScore;
    }

    public int evaluate(char[][] stboard) {
        // checking for 3x or 3o horizontal win
        for (int row =0; row <stboard.length; row ++) {
            if (stboard[row][0] == stboard[row][1] && stboard[row][1] == stboard[row][2]) {
                if (stboard[row][0] == 'x') {
                    return 1;
                } else if (stboard[row][0] == 'o') {
                    return -1;
                }
            }
        }
        // checking for 3x or 3o vertical win
        for (int col =0; col <stboard[0].length; col ++) {
            if (stboard[0][col] == stboard[1][col] && stboard[1][col] == stboard[2][col]) {
                if (stboard[0][col] == 'x') {
                    return 1;
                } else if (stboard[0][col] == 'o') {
                    return -1;
                }
            }
        }
        // checking for 3x or 3o diagonal win
        if (stboard[0][0] == stboard[1][1] && stboard[1][1] == stboard[2][2]) {
            if (stboard[1][1] == 'x') {
                return 1;
            } else if (stboard[1][1] == 'o') {
                return -1;
            }
        }
        if (stboard[2][0] == stboard[1][1] && stboard[1][1] == stboard[0][2]) {
            if (stboard[1][1] == 'x') {
                return 1;
            } else if (stboard[1][1] == 'o') {
                return -1;
            }
        }

        return 0;
    }

    public boolean isMoveLeft(char[][] stboard) {
        for (int i=0; i<stboard.length; i++) {
            for (int j=0; j<stboard[0].length; j++) {
                if (stboard[i][j] == '_') {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkEndGame(char[][] stboard) {
        // TODO: 
        int score = evaluate(stboard);
        if (score > 0) {
            result = "You lose!";
            return true;
        } else if (score < 0) {
            result = "You win!";
            return true;
        } else if (! isMoveLeft(stboard)) {
            result = "Draw!";
            return true;
        }
        return false;
    }

    private void changePlayerTurn() {
        isPlayer1Turn = !isPlayer1Turn;
        chosenB = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Board
        board.draw(g2);

        // X and O
        for (int row =0; row < stboard.length; row++) {
            for (int col=0; col < stboard[0].length; col++) {
                board.drawXO(g2, row, col, stboard[row][col]);
            }
        }

        // STATUS MESSAGES

        // ENGDING GAME
        if (gameEnd) {
            // TODO
        }
    }
 

}
