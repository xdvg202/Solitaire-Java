import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;

public class SolitaireDriver implements MouseListener, ActionListener, MouseMotionListener {

    public static int state;
    public static int inMenu = -1;
    public static int inGame = 0;
    public static int instructions = 1;
    public static int won = 2;

    public static Timer timer0 = new Timer(1, new SolitaireDriver());
    public static JFrame frame0;

    public static DrawCanvas canvas0 = new DrawCanvas();
    public static JPanel butPanel;
    public static JButton startButton;
    public static JButton menuButton;
    public static JButton instrButton;

    public static Deck sourceDeck;
    public static Deck[] primaryStacks = new Deck[7];
    public static Deck[] secondaryStacks = new Deck[4];
    public static Deck pickupDeck;
    public static Deck wasteDeck;
    public static ArrayList<Card> highlightedCards;

    public static int highlightCardSource;
    public static final int pickupCard = -1;
    public static final int primaryCard = 0;

    public static final int cardWidth = 75;
    public static final int cardHeight = 150;
    public static final int cardEdgeHeight = 20;

    public static boolean isTxtRunning = false;
    public static JButton demoBut;
    public static BufferedImage demoImage;
    public static JButton readMeBut;
    public static boolean loadDemoImage;

    public static int mouseX;
    public static int mouseY;

    public static void initialize() {
        canvas0.setLayout(null);
        // all NON-GUI related functions go here
        sourceDeck = new Deck(true);
        sourceDeck.shuffleDeck();
        highlightedCards = new ArrayList<Card>();
        mouseX = 0;
        mouseY = 0;

        initializeAllStacks();

        // all GUI related functions go here

        frame0 = new JFrame("Solitaire By Victor Poltavski.");
        frame0.setSize(800, 600);
        frame0.setLayout(new BorderLayout());

        butPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Start/Restart Game");
        menuButton = new JButton("Menu");
        instrButton = new JButton("Instructions");
        butPanel.add(startButton);
        butPanel.add(menuButton);
        butPanel.add(instrButton);
        startButton.addActionListener(new SolitaireDriver());
        menuButton.addActionListener(new SolitaireDriver());
        instrButton.addActionListener(new SolitaireDriver());

        readMeBut = new JButton("README");
        canvas0.add(readMeBut);
        readMeBut.addActionListener(new SolitaireDriver());
        readMeBut.setBounds(50, 125, 100, 50);
        readMeBut.setVisible(false);

        demoBut = new JButton("Click here to see what game might look like");
        demoBut.setBounds(50, 25, 300, 50);
        demoBut.addActionListener(new SolitaireDriver());
        demoBut.setVisible(false);
        canvas0.add(demoBut);

        frame0.addMouseListener(new SolitaireDriver());
        frame0.addMouseMotionListener(new SolitaireDriver());
        frame0.add(canvas0, BorderLayout.CENTER);
        frame0.add(butPanel, BorderLayout.SOUTH);
        frame0.requestFocus();
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setVisible(true);
        timer0.start();

    }

    public static void initializeAllStacks() {
        // first for the primary stacks
        for (int i = 0; i < 7; i++) {
            // loop to fill each stack
            for (int j = 0; j <= i; j++) {
                // initialize the stack on first run through
                if (j == 0) {
                    primaryStacks[i] = new Deck(false);
                }

                Card temp = sourceDeck.pop();

                // add the card to the pile
                primaryStacks[i].push(temp);
            }
        }

        pickupDeck = new Deck(false);
        wasteDeck = new Deck(false);
        // go through entire source deck and add those remaining cards into the pickup
        // deck
        while (sourceDeck.getSize() > 0) {
            Card temp = sourceDeck.pop();
            pickupDeck.push(temp);
        }
        for (int k = 0; k < secondaryStacks.length; k++) {
            secondaryStacks[k] = new Deck(false);
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer0) {

            if (state == instructions) {
                readMeBut.setVisible(true);
                demoBut.setVisible(true);
            }
            if (state == inMenu || state == inGame) {
                readMeBut.setVisible(false);
                demoBut.setVisible(false);
            }
        }
        if (e.getSource() == menuButton) {
            state = inMenu;
        }
        if (e.getSource() == startButton) {
            state = inGame;
        }
        if (e.getSource() == instrButton) {
            state = instructions;
        }
        if (e.getSource() == readMeBut) {
            isTxtRunning = true;
        }
        if (e.getSource() == demoBut) {
            if (loadDemoImage) {
                loadDemoImage = false;
            } else {
                loadDemoImage = true;
            }

        }
        canvas0.repaint();
    }

    public void mouseClicked(MouseEvent e) {

        // 20x 30y for pickup deck

        // check click detenction for pickup deck
        if ((e.getX() < 20 + cardWidth) && (e.getX() > 20)) {

            if ((e.getY() < 60 + cardHeight) && (e.getY() > 60)) {

                if (pickupDeck.getSize() > 0) {
                    System.out.println("triggered");
                    Card temp = pickupDeck.pop();
                    temp.setFaceDown(false);
                    wasteDeck.push(temp);
                } else {
                    while (wasteDeck.getSize() > 0) {
                        Card temp = wasteDeck.pop();
                        temp.setFaceDown(true);
                        pickupDeck.push(temp);
                    }
                }
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        // easier to work with mouseX and mouseY and also since e.getY includes the
        // window title/header (which i dont need)
        mouseX = e.getX();
        mouseY = e.getY() - 30;

        // go thru entire primary stacks array and check if the mouse is clicked
        for (int i = 0; i < primaryStacks.length; i++) {

            // go thru each card of the pile
            for (int j = 0; j < primaryStacks[i].getSize(); j++) {
                int x = primaryStacks[i].get(j).getX();
                int y = primaryStacks[i].get(j).getY();

                // check if the x's align between mouse and card
                if (mouseX > x && mouseX < x + cardWidth) {

                    // check if its the front card if so it will change what Y's its looking for.
                    // then call the method with the current pile number and current card number
                    if (primaryStacks[i].getSize() - 1 == j) {
                        if (mouseY > y && mouseY < y + cardHeight) {
                            addCardsToHighlightedDeck(i, j);
                            highlightCardSource = primaryCard;
                        }
                    }
                    // other condition where the card is not at the front.
                    // then call the method with the current pile number and current card number
                    else if (mouseY > y && mouseY < y + cardEdgeHeight) {
                        addCardsToHighlightedDeck(i, j);
                        highlightCardSource = primaryCard;
                    }

                }
            }
        }

        if (wasteDeck.getSize() > 0) {
            if (mouseX > 110 && mouseX < 110 + cardWidth) {
                if (mouseY > 30 && mouseY < 30 + cardHeight) {
                    highlightedCards.add(wasteDeck.get(wasteDeck.getSize() - 1));
                    highlightCardSource = pickupCard;
                }
            }
        }

    }

    public static void removeHighlightedCards() {
        for (int k = 0; k < highlightedCards.size(); k++) {
            for (int i = 0; i < primaryStacks.length; i++) {
                for (int j = 0; j < primaryStacks[i].getSize(); j++) {
                    if (primaryStacks[i].get(j) == highlightedCards.get(k)) {
                        primaryStacks[i].pop();
                    }
                }
            }

        }
    }

    public static void addCardsToHighlightedDeck(int pileNum, int cardIndex) {
        // go through the pile and add every card after the card that was clicked
        // (including the card itself)
        for (int p = cardIndex; p < primaryStacks[pileNum].getSize(); p++) {
            highlightedCards.add(primaryStacks[pileNum].get(p));
        }

    }

    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY() - 30;

        for (int i = 0; i < primaryStacks.length; i++) {

            // go thru each card of the pile
            for (int j = 0; j < primaryStacks[i].getSize(); j++) {
                int x = primaryStacks[i].get(j).getX();
                int y = primaryStacks[i].get(j).getY();

                // check if the mouse was released into a primary stack
                if (mouseY > 250) {
                    // check if the x's align between mouse and card
                    if (mouseX > x && mouseX < x + cardWidth) {
                        if (highlightedCards.size() > 0) {
                            // check if its the front card if so it will change what Y's its looking for.
                            // then call the method with the current pile number and current card number
                            if (primaryStacks[i].getSize() - 1 == j) {

                                if (mouseY > y && mouseY < y + cardHeight) {

                                    if (isCardSequential(highlightedCards.get(0), primaryStacks[i].get(j))) {
                                        removeHighlightedCards();
                                        while (highlightedCards.size() > 0) {

                                            primaryStacks[i].push(highlightedCards.get(0));
                                            highlightedCards.remove(0);
                                        }

                                        for (int g = 0; g < primaryStacks[i].getSize(); g++) {
                                            System.out.println(primaryStacks[i].get(g));
                                        }
                                    } else if (primaryStacks[i].getSize() == 0) {
                                        if (highlightedCards.get(0).getValue() == 13) {
                                            while (highlightedCards.size() > 0) {
                                                primaryStacks[i].push(highlightedCards.get(0));
                                            }
                                        }
                                    }

                                    else {
                                        clearHighLightedCards();
                                    }

                                } else {
                                    clearHighLightedCards();
                                }

                            }

                        }
                    }
                }
                
                // check if the mouse was released on a secondary stack
                else if (mouseY > 30 && mouseY < 30 + cardHeight) {
                    for (int m = 0; m < secondaryStacks.length; m++) {
                        if (mouseX > 250 && mouseX < 250 + m * 110) {
                            while (highlightedCards.size() > 0) {
                                secondaryStacks[i].push(highlightedCards.get(0));
                                highlightedCards.remove(0);
                            }
                        }
                    }
                }
            }

        }

        if (highlightCardSource == pickupCard) {
            wasteDeck.pop();
        }
    }

    public static void clearHighLightedCards() {
        while (highlightedCards.size() > 0) {
            highlightedCards.remove(0);
        }
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    
    public boolean isCardSequential(Card a, Card b) {
        int value = a.getValue();
        Card back = b;

        // notes the color of the other card trying to be put on.
        boolean red = false;
        if (a.getSuit().equals("h") || a.getSuit().equals("d")) {
            red = true;
        }
        // notes the color of the card in the back of this deck
        boolean red2 = false;
        if (back.getSuit().equals("h") || back.getSuit().equals("d")) {
            red = true;
        }
        // if the card value difference is one.
        if (back.getValue() - value == 1) {
            // check if suits match
            if (red2 != red) {
                return true;
            }

        }
        return false;

    }

    public static void main(String[] args) {
        state = inGame;
        initialize();

    }

    public static class DrawCanvas extends JPanel {

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.MAGENTA);

            if (state == inMenu) {

                g2d.setFont(new Font("hello", Font.BOLD, 50));
                g2d.drawString("Welcome to Solitaire!", canvas0.getWidth() / 2 - 250, canvas0.getHeight() / 2);
                g2d.setFont(new Font("TimesRoman", Font.ITALIC, 20));
                g2d.drawString("Click an option below!", canvas0.getWidth() / 2 - 100, canvas0.getHeight() / 2 + 100);

            }
            if (state == inGame) {

                for (int i = 0; i < primaryStacks.length; i++) {

                    int x = 25 + (110 * i);
                    // draw the outline for each primary stack.
                    g2d.drawRoundRect(x, 250, cardWidth, cardHeight, 10, 10);
                    // go through each pile
                    for (int j = 0; j < primaryStacks[i].getSize(); j++) {

                        int y = 250 + j * 20;

                        // check if the card is the very front one.
                        if (j == primaryStacks[i].getSize() - 1) {

                            primaryStacks[i].get(j).setFaceDown(false);
                            g2d.drawImage(primaryStacks[i].get(j).getCardImage(), x, y, cardWidth, cardHeight,
                                    this);
                        } else {
                            g2d.drawImage(primaryStacks[i].get(j).getCardImage(), x, y, cardWidth, cardHeight,
                                    this);
                        }
                        primaryStacks[i].get(j).setXY(x, y);
                    }

                }

                // this is for the pickup stack:
                g2d.drawRoundRect(20, 30, cardWidth, cardHeight, 10, 10);
                if (pickupDeck.getSize() > 0) {
                    g2d.drawImage(pickupDeck.get(pickupDeck.getSize() - 1).getCardImage(), 20, 30, cardWidth,
                            cardHeight, this);
                }

                // this draws the waste stack
                g2d.drawRoundRect(110, 30, cardWidth, cardHeight, 10, 10);
                if (wasteDeck.getSize() > 0) {
                    g2d.drawImage(wasteDeck.get(wasteDeck.getSize() - 1).getCardImage(), 110, 30, cardWidth, cardHeight,
                            this);
                }

                // draw the empty card outlines and if a secondary stack has a card, draw the
                // first one.
                for (int k = 0; k < secondaryStacks.length; k++) {
                    if (secondaryStacks[k].getSize() > 0) {
                        g2d.drawImage(secondaryStacks[k].getFirstCard().getCardImage(), (250 + k * 110), 30, cardWidth,
                                cardHeight, this);
                    }
                    g2d.drawRoundRect(300 + k * 110, 30, cardWidth, cardHeight, 10, 10);
                }

                if (highlightedCards.size() > 0) {
                    for (int n = 0; n < highlightedCards.size(); n++) {
                        int ytemp = mouseY + 20 * n;
                        g2d.drawImage(highlightedCards.get(n).getCardImage(), mouseX - cardWidth / 2,
                                ytemp - cardHeight / 2, cardWidth, cardHeight,
                                this);
                    }
                }
            }
            if (state == instructions) {

                if (isTxtRunning) {
                    ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "README.txt");
                    try {
                        pb.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isTxtRunning = false;
                }
                // the demo image loader
                if (loadDemoImage) {
                    try {
                        demoImage = ImageIO.read(new File("demo.png"));
                    } catch (Exception e) {
                        System.out.println("could not load file");
                    }
                    g2d.drawImage(demoImage, canvas0.getWidth() / 2 - 200, canvas0.getHeight() / 2 - 150, 550, 400,
                            this);

                } else {
                    g2d.setFont(new Font("hello", Font.BOLD, 15));
                    String[] simpInstr = {
                            "The goal of this game is to organize all the card into",
                            "the top four stacks.",
                            "The four stacks are all divided by suit.",
                            "The game starts with stacks that have",
                            "some cards face up.",
                            "To organize these piles",
                            "you have to move the face up cards around",
                            "to other piles.",
                            "The order reciprocates from black to red",
                            "and the order of card value decreases",
                            "from front to back.",
                            "For more details access the readme",
                            "(via button on the left)" };
                    for (int z = 0; z < simpInstr.length; z++) {
                        g2d.drawString(simpInstr[z], 400, 100 + z * 20);
                    }
                }

            }

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        

    }

}
//TODO fix logic
/* - make cards moveable to secondary stacks 
 * - double check the isCardSequential method because sometimes it doesnt work by color or value
 * - moving multiple cards throws errors (probably that im passing in the wrong card into the isCardSequential method)
 * - moving king to empty pile
 * - when pickup deck empty make sure to clear highlighted cards
 * - when moving whole stacks it does not properly remove the card at the front of the highlighted cards
 * - when moving 7b6r5b to some valid location 7b get cloned and stays in that pile. 
 * - when moving multiple cards and placing them, then trying to move those back somewhere it dont work. 
 * ^^^^only if the first cards moved were NOT the first faceup card (not the one closes but the furthest face up card)
 * - clean up background make more pretty
 * - make sure all game modes work (restart button works when in game.)
 * 
 */
