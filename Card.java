import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.*;

public class Card {
    private int value;
    private String suit;
    private BufferedImage cardImg;
    private BufferedImage backImg;
    private boolean fd;

    private int x;
    private int y;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;

        fd = true;
        loadImage();

    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setFaceDown(boolean down) {

        if (down) {
            fd = true;
        } else {
            fd = false;
        }
    }

    public String toString() {
        return value + " " + suit;
    }

    // method that loads the cards image into a variable.
    public void loadImage() {
        // load the image of the back of the card
        try {
            backImg = ImageIO.read(new File("deckImages/back.png"));
        } catch (Exception e) {
            System.out.println("could not load file");
        }
        // load the image of the face.
        String pathName = value + suit + ".png";

        try {
            cardImg = ImageIO.read(new File("deckImages/" + pathName));
        } catch (Exception e) {
            System.out.println("could not load file");
        }

    }

    // fetch the value of the card
    public int getValue() {
        return value;
    }

    // fetch the suit of the card
    public String getSuit() {
        return suit;
    }

    // returns the image of the card
    public BufferedImage getCardImage() {
        // gets either the front or the back
        if (fd) {
            return backImg;
        }
        return cardImg;
    }
}