package Game;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel {
    private int tileSize = 24;
    private int mapHeight = 36;
    private int mapWidth = 28;

    private Image wallImage;
    private Image redGhost;
    private Image foodImage;
    private Image pinkGhost;
    private Image orangeGhost;
    private Image blueGhost;

    public PacMan() {
        setPreferredSize(new Dimension(tileSize * mapWidth, tileSize * mapHeight));
        setBackground(Color.BLACK);
    }

}
