package Entities;

public class Tile {
    private int x;
    private int y;
    private boolean isWall;
    private boolean isFood;

    public Tile(int x, int y, boolean isWall, boolean isFood) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
        this.isFood = isFood;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean isFood() {
        return isFood;
    }
}
