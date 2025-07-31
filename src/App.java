
import Game.Game;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("PacMan");

        int tileSize = 32;

        int mapHeight = 21;
        int mapWidth = 19;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(tileSize * mapWidth, tileSize * mapHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        Game game;
        try {
            game = new Game();
        } catch (RuntimeException e) {
            System.err.println("Failed to initialize game: " + e.getMessage());
            return;
        }

        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}
