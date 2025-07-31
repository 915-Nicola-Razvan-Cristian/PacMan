package Entities;

import java.awt.Image;

public class Food extends Tile {

    public boolean isCherry = false;

    public Food(int x, int y, Image image, int size) {
        super(x, y, image, size);
    }

    public Food(int x, int y, Image image, boolean isCherry) {
        super(x, y, image);
        this.isCherry = isCherry;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }
}
