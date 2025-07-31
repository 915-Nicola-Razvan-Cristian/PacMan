package Entities;

import java.awt.Image;

public class PacMan extends Tile {
    protected int velocityX = 0;
    protected int velocityY = 0;

    public int oldVelocityX = 0;
    public int oldVelocityY = 0;

    public char oldDirection = 'U'; // U D L R

    protected char direction = 'U'; // U D L R

    public PacMan(int x, int y, Image image) {
        super(x, y, image);
    }

    public void updateDirection(char newDirection) {
        this.oldDirection = this.direction;
        // System.out.println("Old Direction: " + oldDirection + ", New Direction: " +
        // newDirection);

        this.direction = newDirection;
        updateVelocity();

    }

    public void setDirection(char direction) {
        this.direction = direction;
        updateVelocity();
    }

    protected void updateVelocity() {
        oldVelocityX = velocityX;
        oldVelocityY = velocityY;
        switch (direction) {
            case 'U':
                velocityX = 0;
                velocityY = -size / 4;
                break;
            case 'D':
                velocityX = 0;
                velocityY = size / 4;
                break;
            case 'L':
                velocityX = -size / 4;
                velocityY = 0;
                break;
            case 'R':
                velocityX = size / 4;
                velocityY = 0;
                break;
            default:
                System.err.println("Invalid direction: " + direction);
                velocityX = 0;
                velocityY = 0; // Stop if direction is invalid
        }
    }

    public void move(int dx, int dy) {
        this.velocityX = dx;
        this.velocityY = dy;
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

    public void stop() {
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public char getDirection() {
        return this.direction;
    }

    public void changeImage(Image newImage) {
        this.image = newImage;
    }

}
