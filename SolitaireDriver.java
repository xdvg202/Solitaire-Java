import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;

public class SolitaireDriver implements MouseListener, ActionListener {

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

    public static final int cardWidth = 75;
    public static final int cardHeight = 150;
    public static final int cardEdgeHeight = 20;

    public static boolean isTxtRunning = false;
    public static JButton demoBut;
    public static BufferedImage demoImage;
    public static JButton readMeBut;
    public static boolean loadDemoImage;

    public static void initialize() {
        canvas0.setLayout(null);
        // all NON-GUI related functions go here
        sourceDeck = new Deck(true);
        sourceDeck.shuffleDeck();
        highlightedCards = new ArrayList<Card>();

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

                Card temp = sourceDeck.popFirstCard();

                // add the card to the pile
                primaryStacks[i].addtoFront(temp);
            }
        }

        pickupDeck = new Deck(false);
        wasteDeck = new Deck(false);
        // go through entire source deck and add those remaining cards into the pickup
        // deck
        while (sourceDeck.getSize() > 0) {
            Card temp = sourceDeck.popFirstCard();
            pickupDeck.addtoFront(temp);
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
                    Card temp = pickupDeck.popFirstCard();
                    temp.setFaceDown(false);
                    wasteDeck.addtoFront(temp);
                }
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        // easier to work with mouseX and mouseY and also since e.getY includes the
        // window title/header (which i dont need)
        int mouseX = e.getX();
        int mouseY = e.getY() - 30;

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
                    if (primaryStacks[i].get(j).getIsFront()) {
                        if (mouseY > y && mouseY < y + cardHeight) {
                            addCardsToHighlightedDeck(i, j);
                        }
                    }
                    // other condition where the card is not at the front.\
                    // then call the method with the current pile number and current card number
                    else if (mouseY > y && mouseY < y + cardEdgeHeight) {
                        addCardsToHighlightedDeck(i, j);
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

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
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

                int j = 0;
                for (int i = 0; i < primaryStacks.length; i++) {

                    int x = 25 + (110 * i);
                    // draw the outline for each primary stack.
                    g2d.drawRoundRect(x, 250, cardWidth, cardHeight, 10, 10);
                    // go through each pile
                    for (j = 0; j < primaryStacks[i].getSize(); j++) {

                        int y = 250 + j * 20;

                        // check if the card is the very front one.
                        if (j == primaryStacks[i].getSize() - 1) {
                            primaryStacks[i].get(j).setFront();
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
                    g2d.drawImage(pickupDeck.get(0).getCardImage(), 20, 30, cardWidth, cardHeight, this);
                }

                // this draws the waste stack
                g2d.drawRoundRect(110, 30, cardWidth, cardHeight, 10, 10);
                if (wasteDeck.getSize() > 0) {
                    g2d.drawImage(wasteDeck.getFirst().getCardImage(), 110, 30, cardWidth, cardHeight, this);
                }

                // draw the empty card outlines and if a secondary stack has a card, draw the
                // first one.
                for (int k = 0; k < secondaryStacks.length; k++) {
                    if (secondaryStacks[k].getSize() > 0) {
                        g2d.drawImage(secondaryStacks[k].getFirst().getCardImage(), (250 + k * 110), 30, cardWidth,
                                cardHeight, this);
                    }
                    g2d.drawRoundRect(300 + k * 110, 30, cardWidth, cardHeight, 10, 10);
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

}
