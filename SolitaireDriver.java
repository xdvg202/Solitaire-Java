
/* ICS Culminating project 2023 
 * Solitaire solo player card game by:
 * ---------------------------
 * |                         |
 * |        Victor           |
 * |       Poltavski         |
 * |                         |
 * ---------------------------
 * 
 * The goal of the game is to organize all 52 cards into the top four stacks.
 * The game is played by sorting the piles by specific order.
 * By sorting more and more, more cards are revealed for your usage.
 * There is also a pickup stack where you can pickup cards from. 
 * The cards are sorted in a reciprocating color order and go down by face value towards the front of the stack.
 * 
 * 
 * Refer to readme for more detail...
 */
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
    public static JButton exitButton;

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
    public static BufferedImage bg;
    public static JButton readMeBut;
    public static boolean loadDemoImage;

    public static int mouseX;
    public static int mouseY;

    // method that initializes all the variables/GUI related things
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
        if (state == inMenu) {
            // initialize the frame
            frame0 = new JFrame("Solitaire By Victor Poltavski.");
            frame0.setSize(800, 700);
            frame0.setLayout(new BorderLayout());
            // fill out basic functions of game (the buttons for them)
            butPanel = new JPanel(new FlowLayout());
            startButton = new JButton("Start/Restart Game");
            menuButton = new JButton("Menu");
            instrButton = new JButton("Instructions");
            exitButton = new JButton("Quit");
            butPanel.add(startButton);
            butPanel.add(menuButton);
            butPanel.add(instrButton);
            butPanel.add(exitButton);
            exitButton.addActionListener(new SolitaireDriver());
            startButton.addActionListener(new SolitaireDriver());
            menuButton.addActionListener(new SolitaireDriver());
            instrButton.addActionListener(new SolitaireDriver());
            // readme button on instruction screen
            readMeBut = new JButton("README");
            canvas0.add(readMeBut);
            readMeBut.addActionListener(new SolitaireDriver());
            readMeBut.setBounds(50, 125, 100, 50);
            readMeBut.setVisible(false);
            // demo button, controls demo image in the instruction screen
            demoBut = new JButton("Click here to see what game might look like");
            demoBut.setBounds(50, 25, 300, 50);
            demoBut.addActionListener(new SolitaireDriver());
            demoBut.setVisible(false);
            canvas0.add(demoBut);
            // all the frame related finilizations
            frame0.addMouseListener(new SolitaireDriver());
            frame0.addMouseMotionListener(new SolitaireDriver());
            frame0.add(canvas0, BorderLayout.CENTER);
            frame0.add(butPanel, BorderLayout.SOUTH);
            frame0.requestFocus();
            frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame0.setVisible(true);
            timer0.start();
        }
    }

    // method that fills each stack up
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

    // method that checks for many types of triggers
    public void actionPerformed(ActionEvent e) {
        // check for refresh of timer
        if (e.getSource() == timer0) {

            // change appearances of buttons based off of game state
            if (state == instructions) {
                readMeBut.setVisible(true);
                demoBut.setVisible(true);
            }

            if (state == inMenu || state == inGame) {
                readMeBut.setVisible(false);
                demoBut.setVisible(false);
            }
            // checks if player has won
            if (secondaryStacks[0].getSize() == 13) {
                if (secondaryStacks[1].getSize() == 13) {
                    if (secondaryStacks[2].getSize() == 13) {
                        if (secondaryStacks[3].getSize() == 13) {

                            state = won;
                        }
                    }
                }
            }

        }
        // check if the menu button was clicked
        if (e.getSource() == menuButton) {
            state = inMenu;
        }
        // check if the start button was clicked, and if it was, change states based on
        // current state
        if (e.getSource() == startButton) {
            if (state == inMenu) {
                state = inGame;
            }
            if (state == inGame) {
                initialize();
            }
            if (state == instructions) {
                state = inGame;
            }

        }
        // all the button to fulfill their functions
        if (e.getSource() == instrButton) {
            state = instructions;
        }
        if (e.getSource() == readMeBut) {
            isTxtRunning = true;
        }
        // decide whether or not to draw the demo image in the instruction screen
        if (e.getSource() == demoBut) {
            if (loadDemoImage) {
                loadDemoImage = false;
            } else {
                loadDemoImage = true;
            }

        }
        if (e.getSource() == exitButton) {
            System.exit(0);
        }
        canvas0.repaint();
    }

    // check where the mouse was clicked, and decide what to do with that info
    public void mouseClicked(MouseEvent e) {

        if (state == inGame) {

            // check click detenction for pickup deck
            if ((e.getX() < 20 + cardWidth) && (e.getX() > 20)) {

                if ((e.getY() < 60 + cardHeight) && (e.getY() > 60)) {
                    // if pickup is available
                    if (pickupDeck.getSize() > 0) {

                        Card temp = pickupDeck.pop();
                        temp.setFaceDown(false);
                        wasteDeck.push(temp);
                    }
                    // in the case that it isnt, reset the pickup pile
                    else {
                        while (wasteDeck.getSize() > 0) {
                            Card temp = wasteDeck.pop();
                            temp.setFaceDown(true);
                            pickupDeck.push(temp);
                        }
                    }
                }
            }
        }

    }

    // check where the mouse was pressed, and decide what to do with that info
    public void mousePressed(MouseEvent e) {
        if (state == inGame) {
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
            // if the mouse was pressed on the waste deck, perform correct function
            if (wasteDeck.getSize() > 0) {
                if (mouseX > 110 && mouseX < 110 + cardWidth) {
                    if (mouseY > 30 && mouseY < 30 + cardHeight) {

                        highlightedCards.add(wasteDeck.get(wasteDeck.getSize() - 1));
                        highlightCardSource = pickupCard;

                    }
                }
            }
        }
    }

    // removes the cards that are in the highlighted card arraylist from the primary
    // stacks
    public static void removeHighlightedCards() {
        // check if the card derives either from the primary card stack
        if (highlightCardSource == primaryCard) {
            int stackNum = 0;
            // go through each pile
            for (int i = 0; i < primaryStacks.length; i++) {
                // go through each card
                for (int j = 0; j < primaryStacks[i].getSize(); j++) {
                    // if it is found note its pile.
                    if (primaryStacks[i].get(j) == highlightedCards.get(0)) {
                        stackNum = i;
                        break;
                    }
                }
            }
            // remove the cards from locatred pile
            for (int k = 0; k < highlightedCards.size(); k++) {
                primaryStacks[stackNum].pop();
            }
        }
        // if it comes from the pickup, remove the card off the pile
        else if (highlightCardSource == pickupCard) {
            wasteDeck.pop();
        }

    }

    // adds all the cards that were specified by method call
    public static void addCardsToHighlightedDeck(int pileNum, int cardIndex) {
        // go through the pile and add every card after the card that was clicked
        // (including the card itself)
        for (int p = cardIndex; p < primaryStacks[pileNum].getSize(); p++) {
            highlightedCards.add(primaryStacks[pileNum].get(p));
        }

    }

    // method to check where the mouse was released, and what to do with that info
    public void mouseReleased(MouseEvent e) {
        if (state == inGame) {

            mouseX = e.getX();
            mouseY = e.getY() - 30;

            // if released on a primary stack
            if (mouseY > 250) {
                // go thru each pile
                for (int i = 0; i < primaryStacks.length; i++) {
                    // check that pile is empty
                    if (primaryStacks[i].getSize() == 0) {
                        // make sure the xs and ys align
                        if (mouseX > (25 + i * 110) && mouseX < (25 + i * 110 + cardWidth)) {

                            if (mouseY > 250 && mouseY < 250 + cardHeight) {
                                // if the highlighted card stack is of viable length
                                if (highlightedCards.size() > 0) {
                                    // check if the first card being added to the empty pile is a king (rule of
                                    // solitaire)
                                    if (highlightedCards.get(0).getValue() == 13) {
                                        // add them jawns
                                        for (int v = 0; v < highlightedCards.size(); v++) {

                                            primaryStacks[i].push(highlightedCards.get(v));

                                        }
                                        // clear out all highlighted cards from their original pile and also the
                                        // highlighted card arraylist
                                        removeHighlightedCards();
                                        clearHighLightedCards();

                                    }
                                }
                            }
                        }
                    }

                    // go thru each card of the pile
                    for (int j = 0; j < primaryStacks[i].getSize(); j++) {

                        int x = primaryStacks[i].get(j).getX();
                        int y = primaryStacks[i].get(j).getY();
                        // check if the mouse was released into a primary stack

                        // check if the x's align between mouse and card
                        if (mouseX > x && mouseX < x + cardWidth) {
                            if (highlightedCards.size() > 0) {

                                // check if its the front card if so it will change what Y's its looking for.
                                // then call the method with the current pile number and current card number

                                if (primaryStacks[i].getSize() - 1 == j) {

                                    if (mouseY > y && mouseY < y + cardHeight) {
                                        // check if they can really be placed
                                        if (isCardSequential(highlightedCards.get(0), primaryStacks[i].get(j), true)) {

                                            // move highlighted cards into the stack by first removing the cards from
                                            // their original piles.

                                            removeHighlightedCards();

                                            while (highlightedCards.size() > 0) {

                                                primaryStacks[i].push(highlightedCards.get(0));
                                                highlightedCards.remove(0);
                                            }

                                        }
                                        // conditions where things didnt go perfectly to just clear the highlightedstack
                                        else {
                                            clearHighLightedCards();
                                        }

                                    } else {
                                        clearHighLightedCards();
                                    }

                                }
                            }
                        }

                        // check if the mouse was released on a secondary stack

                    }

                }
            }
            // if released on a secondary stack
            // multiple if statements to make sure all conditions are met before adding the
            // cards into the pile
            else if (mouseY > 30 && mouseY < 30 + cardHeight) {

                for (int m = 0; m < secondaryStacks.length; m++) {

                    if (mouseX > (300 + m * 110) && mouseX < (300 + m * 110) + cardWidth) {
                        // if only one card is being added
                        if (highlightedCards.size() == 1) {
                            // if adding to an empty secondary stack, remove card from original stack and
                            // add to secondary
                            if (secondaryStacks[m].getSize() == 0) {

                                if (highlightedCards.get(0).getValue() == 1) {
                                    removeHighlightedCards();
                                    secondaryStacks[m].push(highlightedCards.get(0));

                                }
                            }
                            // case where secondary pile already has cards, check conditions if it can add
                            // them to the stack
                            else {
                                if (isCardSequential(highlightedCards.get(0),
                                        secondaryStacks[m].get(secondaryStacks[m].getSize() - 1), false)) {
                                    removeHighlightedCards();
                                    secondaryStacks[m].push(highlightedCards.get(0));
                                }
                            }

                        }
                        // if conditions not met, clear cards
                        clearHighLightedCards();
                    }
                }
            }
            clearHighLightedCards();
        }
    }

    // clear the highlighted cards
    public static void clearHighLightedCards() {
        while (highlightedCards.size() > 0) {
            highlightedCards.remove(0);
        }
    }

    // update mousex and mousey upon dragging of the mouse
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    // check if the cards can be put in order to each other
    public boolean isCardSequential(Card a, Card b, boolean primary) {

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
            red2 = true;
        }
        // if the card value difference is one.

        // check if suits match

        // condition for when checking sequentiality based on which card stacks we are
        // comparing
        if (primary) {
            if (back.getValue() - value == 1) {
                if (red2 != red) {
                    return true;
                }
            }

        } else {
            if (red2 == red) {
                if (value - back.getValue() == 1) {
                    return true;
                }
            }
        }
        return false;

    }

    public static void main(String[] args) {
        state = inMenu;
        initialize();

    }

    // the draw canvas metthod which draws every component
    public static class DrawCanvas extends JPanel {

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            setBackground(Color.darkGray);

            if (state == inMenu) {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("hello", Font.BOLD, 50));
                g2d.drawString("Welcome to Solitaire!", canvas0.getWidth() / 2 - 250, canvas0.getHeight() / 2);
                g2d.setFont(new Font("TimesRoman", Font.ITALIC, 20));
                g2d.drawString("Click an option below!", canvas0.getWidth() / 2 - 100, canvas0.getHeight() / 2 + 100);

            }
            if (state == inGame) {

                // load background
                try {
                    bg = ImageIO.read(new File("bg.png"));
                } catch (Exception e) {
                    System.out.println("could not fetch file");
                }
                g2d.drawImage(bg, 0, 0, canvas0.getWidth(), canvas0.getHeight(), this);
                // for each individual stack
                for (int i = 0; i < primaryStacks.length; i++) {

                    int x = 25 + (110 * i);
                    // draw the outline for each primary stack.
                    g2d.fillRoundRect(x, 250, cardWidth, cardHeight, 10, 10);
                    // go through each pile
                    for (int j = 0; j < primaryStacks[i].getSize(); j++) {

                        int y = 250 + j * 20;

                        // check if the card is the very front one.
                        if (j == primaryStacks[i].getSize() - 1) {

                            primaryStacks[i].get(j).setFaceDown(false);
                            g2d.drawImage(primaryStacks[i].get(j).getCardImage(), x, y, cardWidth, cardHeight,
                                    this);
                        }
                        // if it isn't
                        else {
                            g2d.drawImage(primaryStacks[i].get(j).getCardImage(), x, y, cardWidth, cardHeight,
                                    this);
                        }
                        primaryStacks[i].get(j).setXY(x, y);
                    }

                }
                // draw the pickup pile outline
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRoundRect(20, 30, cardWidth, cardHeight, 10, 10);

                // pickup pile text

                g2d.setColor(Color.BLACK);
                g2d.drawString("Click Here", 22, 70);
                g2d.drawString("To Reset", 22, 90);

                g2d.setColor(Color.LIGHT_GRAY);
                // this is for the pickup stack:

                if (pickupDeck.getSize() > 0) {
                    g2d.drawImage(pickupDeck.get(pickupDeck.getSize() - 1).getCardImage(), 20, 30, cardWidth,
                            cardHeight, this);
                }

                // this draws the waste stack
                g2d.fillRoundRect(110, 30, cardWidth, cardHeight, 10, 10);
                if (wasteDeck.getSize() > 0) {
                    g2d.drawImage(wasteDeck.get(wasteDeck.getSize() - 1).getCardImage(), 110, 30, cardWidth, cardHeight,
                            this);
                }

                // draw the empty card outlines and if a secondary stack has a card, draw the
                // first one.
                for (int k = 0; k < secondaryStacks.length; k++) {
                    g2d.fillRoundRect(300 + k * 110, 30, cardWidth, cardHeight, 10, 10);
                    // if there is a card in the secondary stack, draw it
                    if (secondaryStacks[k].getSize() > 0) {

                        g2d.drawImage(secondaryStacks[k].getFirstCard().getCardImage(), (300 + k * 110), 30,
                                cardWidth, cardHeight, this);

                    }

                }
                // draw the highlighted cards as they are dragged throughout the panel
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
                // open notepad with readme if the button is clicked
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
                    // string repre of basic instructions
                    g2d.setColor(Color.WHITE);
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
            if (state == won) {
                g2d.drawString("YOU WON YAYYYYYY!!!!", canvas0.getWidth() / 2, canvas0.getHeight() / 2);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
