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
    char[][] stboard;

    // BOOLEANS
    boolean gameEnd;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        // additional methods
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

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Board
        board.draw(g2);

        // X and O

        // STATUS MESSAGES

        // ENGDING GAME
    }
 

}
