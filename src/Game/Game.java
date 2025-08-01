package Game;

import Entities.Food;
import Entities.Ghost;
import Entities.PacMan;
import Entities.Tile;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;

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
    private Image cherryImage;
    private Image scaredGhost;

    // X = wall, O = skip, P = pac man, ' ' = food
    // Ghosts: b = blue, o = orange, p = pink, r = red
    // private String[] tileMap = {
    // "XXXXXXXXXXXXXXXXXXX",
    // "X X X",
    // "X XX XXX X XXX XX X",
    // "X X",
    // "X XX X XXXXX X XX X",
    // "X X X X",
    // "XXXX XXXX XXXX XXXX",
    // "OOOX X X XOOO",
    // "XXXX X XXrXX X XXXX",
    // "O bpo O",
    // "XXXX X XXXXX X XXXX",
    // "OOOX X X XOOO",
    // "XXXX X XXXXX X XXXX",
    // "X X X",
    // "X XX XXX X XXX XX X",
    // "X X P X X",
    // "XX X X XXXXX X X XX",
    // "X X X X X",
    // "X XXXXXX X XXXXXX X",
    // "X X",
    // "XXXXXXXXXXXXXXXXXXX"
    // };

    HashSet<Tile> walls = new HashSet<Tile>();
    HashSet<Food> foods = new HashSet<Food>();
    HashSet<Ghost> ghosts = new HashSet<Ghost>();

    PacMan pacman;

    private Boolean poweredUp = false;

    java.util.Timer timer;
    TimerTask timerTask;

    Timer gameLoop;
    char[] directions = { 'U', 'D', 'L', 'R' };

    public Game() {
        setPreferredSize(new Dimension(tileSize * mapWidth, tileSize * mapHeight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        scaredGhost = new ImageIcon("./assets/scaredGhost.png").getImage();
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
        cherryImage = new ImageIcon("./assets/cherry.png").getImage();

        timer = new java.util.Timer();

        // check if images are loaded
        if (wallImage.getWidth(null) == -1 || redGhost.getWidth(null) == -1 ||
                foodImage.getWidth(null) == -1 || pinkGhost.getWidth(null) == -1 ||
                orangeGhost.getWidth(null) == -1 || blueGhost.getWidth(null) == -1 ||
                pacmanUp.getWidth(null) == -1 || pacmanDown.getWidth(null) == -1 ||
                pacmanLeft.getWidth(null) == -1 || pacmanRight.getWidth(null) == -1) {

            throw new RuntimeException("Image loading failed");
        }

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

        gameLoop = new Timer(50, this);
        gameLoop.start();

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

        Iterator<Food> foodIterator = foods.iterator();
        while (foodIterator.hasNext()) {
            Food food = foodIterator.next();
            if (Tile.collision(pacman, food)) {
                if (food.isCherry) {
                    food.image = null;
                    foodIterator.remove();
                    poweredUp = true;
                    for (Ghost ghost : ghosts) {
                        ghost.image = scaredGhost;
                    }
                    timer = new java.util.Timer();
                    if (timerTask != null) {
                        timerTask.cancel();
                    }
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            for (Ghost ghost : ghosts) {
                                ghost.image = ghost.name == 'r' ? redGhost
                                        : ghost.name == 'b' ? blueGhost
                                                : ghost.name == 'o' ? orangeGhost : pinkGhost;
                            }
                            poweredUp = false;
                        }
                    };
                    timer.schedule(timerTask, 5000);
                } else {
                    food.image = null;
                    foodIterator.remove();
                }
            }
        }

        Iterator<Ghost> ghostIterator = ghosts.iterator();

        while (ghostIterator.hasNext()) {
            Ghost ghost = ghostIterator.next();

            ghost.setX(ghost.getX() + ghost.getVelocityX());
            ghost.setY(ghost.getY() + ghost.getVelocityY());

            if (Tile.collision(ghost, pacman)) {
                if (poweredUp) {
                    ghost.image = null;
                    ghostIterator.remove();
                    if (ghosts.isEmpty()) {
                        JDialog dialog = new JDialog();
                        dialog.setTitle("Game Over");
                        dialog.setSize(420, 150);
                        dialog.setLocationRelativeTo(null);
                        JLabel label = new JLabel("You win! All ghosts are gone!", SwingConstants.CENTER);
                        label.setFont(new Font("Arial", Font.BOLD, 16));
                        dialog.add(label);
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        dialog.setVisible(true);
                        this.gameLoop.stop();
                    }
                } else {
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Game Over");
                    dialog.setSize(420, 150);
                    dialog.setLocationRelativeTo(null);
                    JLabel label = new JLabel("Game Over! Pacman was caught by a ghost!", SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 16));
                    dialog.add(label);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                    this.gameLoop.stop();
                    return;
                }
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

        File file;
        Scanner scanner;

        try {
            file = new File("./assets/map2.txt");
            if (!file.exists()) {
                throw new FileNotFoundException("Map file not found at " + file.getAbsolutePath());
            }
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("Map file not found: " + e.getMessage());
            return;
        }
        String[] tileMap = new String[mapHeight];
        int h = 0;
        while (scanner.hasNextLine() && h < mapHeight) {
            String line = scanner.nextLine();
            if (line.length() != mapWidth) {
                scanner.close();
                throw new RuntimeException("Invalid map line length at line " + (h + 1) + ": expected " + mapWidth
                        + ", got " + line.length());
            }
            tileMap[h++] = line;
        }

        scanner.close();

        for (h = 0; h < mapHeight; h++) {
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
                        ghosts.add(new Ghost(x, y, blueGhost, 'b'));
                        break;
                    case 'o':
                        ghosts.add(new Ghost(x, y, orangeGhost, 'o'));
                        break;
                    case 'p':
                        ghosts.add(new Ghost(x, y, pinkGhost, 'p'));
                        break;
                    case 'r':
                        ghosts.add(new Ghost(x, y, redGhost, 'r'));
                        break;
                    case ' ':
                        foods.add(new Food(x + 14, y + 14, foodImage, 4));
                        break;
                    case 'C':
                        foods.add(new Food(x, y, cherryImage, true));
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
