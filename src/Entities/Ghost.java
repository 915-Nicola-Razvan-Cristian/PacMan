package Entities;

import java.awt.Image;

public class Ghost extends PacMan {

    public char name; // b = blue, o = orange, p = pink, r = red

    public Ghost(int x, int y, Image image, char name) {
        super(x, y, image);
        this.name = name;
    }

    public void updateDirection(char newDirection) {
        this.oldDirection = this.direction;
        // System.out.println("Old Direction: " + oldDirection + ", New Direction: " +
        // newDirection);

        this.direction = newDirection;
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
}