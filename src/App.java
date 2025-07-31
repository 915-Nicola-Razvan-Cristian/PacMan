import java.util.HashMap;
import Entities.*;
import Game.PacMan;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("PacMan");

        int tileSize = 24;

        int mapHeight = 36;
        int mapWidth = 28;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(tileSize * mapWidth, tileSize * mapHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        PacMan game = new PacMan();
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
    }
}
