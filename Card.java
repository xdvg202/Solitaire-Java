import java.io.File;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.*;

public class Card {
    private int value;
    private String suit;
    private BufferedImage cardImg;
    private boolean faceDown;

    public Card(int value, String suit, boolean fd) {
        this.value = value;
        this.suit = suit;
        faceDown = fd;
        loadImage();

    }

    public void loadImage() {
        if(isFaceDown()){
        String pathName = value + suit + ".png";

        try {
            cardImg = ImageIO.read(new File("deckImages/" + pathName));
        } catch (Exception e) {
            System.out.println("could not load file");
        }
    }
    else{
        String pathName = "back.png";

        try {
            cardImg = ImageIO.read(new File("deckImages/" + pathName));
        } catch (Exception e) {
            System.out.println("could not load file");
        }
    }
        
    }

    public boolean isFaceDown() {
        return faceDown;
    }

    public void setVisible(boolean b) {
        if (b) {
            faceDown = false;
        } else {
            faceDown = true;
        }
    }

    

    public BufferedImage getCardImage() {
        return cardImg;
    }
}