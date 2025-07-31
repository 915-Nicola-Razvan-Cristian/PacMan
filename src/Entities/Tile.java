package Entities;

import java.awt.Image;

public class Tile {
    protected int x;
    protected int y;
    public Image image;
    public int size = 32; // 32*32 pixels by default

    public Tile(int x, int y, Image image, int size) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.size = size;
    }

    public Tile(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public static boolean collision(Tile a, Tile b) {
        return a.getX() < b.getX() + b.size &&
                a.getX() + a.size > b.getX() &&
                a.getY() < b.getY() + b.size &&
                a.getY() + a.size > b.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
