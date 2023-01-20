import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.*;

public class Card {
    private int value;
    private String suit;
    private BufferedImage cardImg;
    private BufferedImage backImg;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;
        loadImage(true);

    }

    public void loadImage(boolean fd) {

        try {
            backImg = ImageIO.read(new File("deckImages/back.png"));
        } catch (Exception e) {
            System.out.println("could not load file");
        }

        String pathName = value + suit + ".png";

        try {
            cardImg = ImageIO.read(new File("deckImages/" + pathName));
        } catch (Exception e) {
            System.out.println("could not load file");
        }

    }
    public int getValue(){
        return value;
    }
    public String getSuit(){
        return suit;
    }

    public BufferedImage getCardImage(boolean fd) {
        if(fd){
            return backImg;
        }
        return cardImg;
    }
}