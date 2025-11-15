package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String args[]) {
        JFrame window = new JFrame("Tix Tac Toe");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Add gamepanel to window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame(); // call the launchGame method to start the game
    }
}
