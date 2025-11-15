package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Board {
    final int MAX_COL = 3;
    final int MAX_ROW = 3;
    public static final int SQUARE_SITE = 200;
    public static final int HALF_SQUARE_SITE = SQUARE_SITE / 2;

    public void draw(Graphics2D g2) {
        boolean coloring = true;;
        for (int r = 0; r<MAX_ROW; r++) {
            for (int c = 0; c < MAX_COL; c++) {
                if (coloring) {
                    // white
                    g2.setColor(new Color(251, 246, 243));
                    coloring = false;
                } else {
                    g2.setColor(new Color(54, 214, 231));
                    coloring = true;
                }

                g2.fillRect(c * SQUARE_SITE, r * SQUARE_SITE, SQUARE_SITE, SQUARE_SITE); // x, y, width, height
            }

            // color do not change in next row - use for n*n board when n is even
            // coloring = !coloring;
        }
    }

    public void drawXO(Graphics2D g2, int row, int col, char status) {
        // if is a empty block: draw nothing
        if (status == '_') {
            return;
        }

        // drawing x/ o to the block
        String imgPath = status == 'x' ? "/assets/x.png" : "/assets/o.png";
        g2.drawImage(getImage(imgPath), getX(col), getY(row), SQUARE_SITE, SQUARE_SITE, null);
    }

    public BufferedImage getImage(String imgPath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public int getX(int col) {
        return col * SQUARE_SITE;
    }

    public int getY(int row) {
        return row * SQUARE_SITE;
    }
}
