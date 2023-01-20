import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.image.*;

public class SolitaireDriver implements MouseListener, ActionListener {

    public static int state;
    public static int inMenu = -1;
    public static int inGame = 0;
    public static int instructions = 1;
    public static int won = 2;

    public static JFrame frame0;
    public static DrawCanvas canvas0 = new DrawCanvas();
    public static JPanel butPanel;
    public static JButton startButton;
    public static JButton menuButton;
    public static JButton instrButton;
    public static Timer timer0 = new Timer(1, new SolitaireDriver());

    public static Deck sourceDeck;
    public static Deck[] primaryStacks = new Deck[7];
    public static Deck[] secondaryStacks = new Deck[4];
    public static Deck pickupDeck;
    public static Deck wasteDeck;
    public static final int cardWidth = 75;
    public static final int cardHeight = 150;
    public static boolean isTxtRunning = false;

    public static JButton swag;
    public static JPanel buttons;

    public static void initialize() {
        // all NON-GUI related functions go here
        sourceDeck = new Deck(true);
        sourceDeck.shuffleDeck();

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

        buttons = new JPanel(new FlowLayout());
        swag = new JButton("swag");
        buttons.add(swag);
        buttons.setBounds(200, 200, 10, 10);

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
                // initialize the stack on first runthrough
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
        canvas0.repaint();
    }

    public void mouseClicked(MouseEvent e) {

        // 20x 30y for pickup deck

        // check click detenction for pickup deck
        if ((e.getX() < 20 + cardWidth) && (e.getX() > 20)) {

        }

    }

    public void mousePressed(MouseEvent e) {

        // go thru entire primary stacks array and check if the mouse is clicked
        // on the front card using the getFrontCardx/y method
        for (int i = 0; i < primaryStacks.length; i++) {
            int[] xy = primaryStacks[i].getFrontXY();
            int x = xy[0];
            int y = xy[1];

            if (e.getX() == x) {
                if (e.getY() == y) {

                }
            }
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
                // TODO draw all stacks

                int j = 0;
                for (int i = 0; i < primaryStacks.length; i++) {

                    int x = 25 + (110 * i);
                    // draw the outline for each primary stack.
                    g2d.drawRoundRect(x, 250, cardWidth, cardHeight, 10, 10);

                    for (j = 0; j < primaryStacks[i].getSize(); j++) {

                        int y = 250 + j * 20;
                        // check if the card is the very first one.
                        if (j == primaryStacks[i].getSize() - 1) {
                            g2d.drawImage(primaryStacks[i].get(j).getCardImage(false), x, y, cardWidth, cardHeight,
                                    this);
                            // this records the X and Y of the front card in the stack
                            primaryStacks[i].setFrontXY(x, y);
                        } else {
                            g2d.drawImage(primaryStacks[i].get(j).getCardImage(true), x, y, cardWidth, cardHeight,
                                    this);
                        }
                    }

                }
                // this is for the pickup stack:
                g2d.drawImage(pickupDeck.get(0).getCardImage(true), 20, 30, cardWidth, cardHeight, this);
                g2d.drawRoundRect(20, 30, cardWidth, cardHeight, 10, 10);

                // this draws the waste stack
                g2d.drawRoundRect(110, 30, cardWidth, cardHeight, 10, 10);
                if (wasteDeck.getSize() > 0) {
                    g2d.drawImage(wasteDeck.getFirst().getCardImage(false), 110, 30, cardWidth, cardHeight, this);
                }

                // draw the empty card outlines and if a secondary stack has a card, draw the
                // first one.
                for (int k = 0; k < secondaryStacks.length; k++) {
                    if (secondaryStacks[k].getSize() > 0) {
                        g2d.drawImage(secondaryStacks[k].getFirst().getCardImage(false), (250 + k * 110), 30, cardWidth,
                                cardHeight, this);
                    }
                    g2d.drawRoundRect(300 + k * 110, 30, cardWidth, cardHeight, 10, 10);
                }
            }
            if (state == instructions) {

                // add button and listener to open notpad with the readme attached
                //TODO figure out how to add buttons into the canvas. 
                /*
                possibly have them in the BtnPanel and hide them when not in instruction menu
                make a shape in the instruction menu and see if mouse clicked within the bounds of that shape
                then exit instructions

                WHEN DONE INSTRUCTION MENU DELETE THIS COMMENT AND POST TO 5/8
                */

                if (!isTxtRunning) {
                    ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "README.txt");
                    try {
                        pb.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isTxtRunning = true;
                }
                // the demo image loader
                JButton demoBut = new JButton("Click here to see what game might look like");
                // g2d.
                BufferedImage demo = new BufferedImage(3, 3, 3);
                try {
                    demo = ImageIO.read(new File("demo.png"));
                } catch (Exception e) {
                    System.out.println("could not load file");
                }
                // g2d.drawImage(demo, 50, 50, 400, 300, this);

            }

        }
    }

}
