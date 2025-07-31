package Game;

import Entities.Ghost;
import Entities.PacMan;
import Entities.Tile;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {
    private int tileSize = 32;
    private int mapHeight = 21;
    private int mapWidth = 19;

    private Image wallImage;
    private Image redGhost;
    private Image foodImage;
    private Image pinkGhost;
    private Image orangeGhost;
    private Image blueGhost;
    private Image pacmanUp;
    private Image pacmanDown;
    private Image pacmanLeft;
    private Image pacmanRight;

    // X = wall, O = skip, P = pac man, ' ' = food
    // Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Tile> walls = new HashSet<Tile>();
    HashSet<Tile> foods = new HashSet<Tile>();
    HashSet<Ghost> ghosts = new HashSet<Ghost>();

    PacMan pacman;

    Timer gameLoop;
    char[] directions = { 'U', 'D', 'L', 'R' };

    public Game() {
        setPreferredSize(new Dimension(tileSize * mapWidth, tileSize * mapHeight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon("./assets/wall.png").getImage();
        redGhost = new ImageIcon("./assets/redGhost.png").getImage();
        foodImage = new ImageIcon("./assets/powerFood.png").getImage();
        pinkGhost = new ImageIcon("./assets/pinkGhost.png").getImage();
        orangeGhost = new ImageIcon("./assets/orangeGhost.png").getImage();
        blueGhost = new ImageIcon("./assets/blueGhost.png").getImage();
        pacmanUp = new ImageIcon("./assets/pacmanUp.png").getImage();
        pacmanDown = new ImageIcon("./assets/pacmanDown.png").getImage();
        pacmanLeft = new ImageIcon("./assets/pacmanLeft.png").getImage();
        pacmanRight = new ImageIcon("./assets/pacmanRight.png").getImage();

        // check if images are loaded
        if (wallImage.getWidth(null) == -1 || redGhost.getWidth(null) == -1 ||
                foodImage.getWidth(null) == -1 || pinkGhost.getWidth(null) == -1 ||
                orangeGhost.getWidth(null) == -1 || blueGhost.getWidth(null) == -1 ||
                pacmanUp.getWidth(null) == -1 || pacmanDown.getWidth(null) == -1 ||
                pacmanLeft.getWidth(null) == -1 || pacmanRight.getWidth(null) == -1) {

            throw new RuntimeException("Image loading failed");
        }

        gameLoop = new Timer(50, this);
        gameLoop.start();

        loadMap();
        if (pacman == null) {
            throw new RuntimeException("Pacman not found in map");
        }
        if (walls.isEmpty()) {
            throw new RuntimeException("No walls found in map");
        }
        if (foods.isEmpty()) {
            throw new RuntimeException("No food found in map");
        }
        if (ghosts.isEmpty()) {
            throw new RuntimeException("No ghosts found in map");
        }

        for (Ghost ghost : ghosts) {
            char randomDirection = directions[new Random().nextInt(4)];
            ghost.updateDirection(randomDirection);
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {

        for (Tile wall : walls) {
            g.drawImage(wall.image, wall.getX(), wall.getY(), wall.size, wall.size, null);
        }
        for (Tile food : foods) {
            g.drawImage(food.image, food.getX(), food.getY(), food.size, food.size, null);
        }
        for (Tile ghost : ghosts) {
            g.drawImage(ghost.image, ghost.getX(), ghost.getY(), ghost.size, ghost.size, null);
        }
        if (pacman != null) {
            g.drawImage(pacman.image, pacman.getX(), pacman.getY(), pacman.size, pacman.size, null);
        }
    }

    public void move() {

        pacman.setX(pacman.getX() + pacman.getVelocityX());
        pacman.setY(pacman.getY() + pacman.getVelocityY());

        if (pacman.getY() == tileSize * 9 && pacman.getX() < 0) {
            pacman.setX(getWidth() - pacman.size);
        } else if (pacman.getY() == tileSize * 9 && pacman.getX() > getWidth() - pacman.size) {
            pacman.setX(0);
        }

        for (Tile wall : walls) {
            if (Tile.collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.getVelocityX());
                pacman.setY(pacman.getY() - pacman.getVelocityY());
            }
        }

        for (Ghost ghost : ghosts) {

            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());

            if (Tile.collision(ghost, pacman)) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Game Over");
                dialog.setSize(300, 150);
                dialog.setLocationRelativeTo(null);
                JLabel label = new JLabel("Game Over! Pacman was caught by a ghost!", SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 16));
                dialog.add(label);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
                this.gameLoop.stop();
                return;
            }

            if (ghost.getY() == tileSize * 9 && ghost.getX() < 0) {
                ghost.setX(getWidth() - ghost.size);

            } else if (ghost.getY() == tileSize * 9 && ghost.getX() > getWidth() - ghost.size) {
                ghost.setX(0);

            }

            if (ghost.getY() == tileSize * 9) {
                if (new Random().nextInt(100) < 20) {
                    ghost.updateDirection(directions[new Random().nextInt(2)]);
                }
            }

            for (Tile wall : walls) {
                if (Tile.collision(ghost, wall)) {
                    ghost.setX(ghost.getX() - ghost.getVelocityX());
                    ghost.setY(ghost.getY() - ghost.getVelocityY());
                    char randomDirection = directions[new Random().nextInt(4)];
                    ghost.updateDirection(randomDirection);
                }
            }

            // Randomly change direction
            if (new Random().nextInt(100) < 5) { // 5% chance to change direction
                char randomDirection = directions[new Random().nextInt(4)];
                ghost.updateDirection(randomDirection);
            }
        }

    }

    private void loadMap() {
        for (int h = 0; h < mapHeight; h++) {
            String line = tileMap[h];
            for (int i = 0; i < mapWidth; i++) {
                int x = i * tileSize;
                int y = h * tileSize;
                switch (line.charAt(i)) {
                    case 'X':
                        walls.add(new Tile(x, y, wallImage));
                        break;
                    case 'P':
                        pacman = new PacMan(x, y, pacmanRight);
                        break;
                    case 'b':
                        ghosts.add(new Ghost(x, y, blueGhost));
                        break;
                    case 'o':
                        ghosts.add(new Ghost(x, y, orangeGhost));
                        break;
                    case 'p':
                        ghosts.add(new Ghost(x, y, pinkGhost));
                        break;
                    case 'r':
                        ghosts.add(new Ghost(x, y, redGhost));
                        break;
                    case ' ':
                        foods.add(new Tile(x + 14, y + 14, foodImage, 4));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void validateDirection() {
        pacman.setX(pacman.getX() + pacman.getVelocityX());
        pacman.setY(pacman.getY() + pacman.getVelocityY());
        for (Tile wall : walls) {
            if (Tile.collision(pacman, wall)) {
                pacman.setX(pacman.getX() - pacman.getVelocityX());
                pacman.setY(pacman.getY() - pacman.getVelocityY());
                pacman.setDirection(pacman.oldDirection);
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                pacman.updateDirection('U');
                validateDirection();
                break;
            case KeyEvent.VK_DOWN:
                pacman.updateDirection('D');
                validateDirection();
                break;
            case KeyEvent.VK_LEFT:
                pacman.updateDirection('L');
                validateDirection();
                break;
            case KeyEvent.VK_RIGHT:
                pacman.updateDirection('R');
                validateDirection();
                break;
            default:
                break;
        }
        if (pacman.getDirection() == 'U') {
            pacman.changeImage(pacmanUp);
        } else if (pacman.getDirection() == 'D') {
            pacman.changeImage(pacmanDown);
        } else if (pacman.getDirection() == 'L') {
            pacman.changeImage(pacmanLeft);
        } else if (pacman.getDirection() == 'R') {
            pacman.changeImage(pacmanRight);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        // System.out.println("Key released: " + e.getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }
}
