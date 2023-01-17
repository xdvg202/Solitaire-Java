import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.EventListener;

public class SolitaireDriver implements MouseListener, ActionListener, KeyListener {

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
        startButton = new JButton("Start Game");
        menuButton = new JButton("Menu");
        instrButton = new JButton("Instructions");
        butPanel.add(startButton);
        butPanel.add(menuButton);
        butPanel.add(instrButton);

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
                // adding the last card (the front card that is seen), make sure it is visible
                if (primaryStacks[i].getSize() == (i + 1)) {
                    temp.setVisible(true);
                }
                // add the card to the pile
                primaryStacks[i].addtoFront(temp);
            }
        }

        // then the pickup pile
        /*
         * TODO setup with loops, the pickup and waste deck with the cards
         * remaining in the source deck.
         */
        pickupDeck = new Deck(false);
        wasteDeck = new Deck(false);
        // go through entire source deck and add those remaining cards into the pickup
        // deck
        while(sourceDeck.getSize()>0) {
            Card temp = sourceDeck.popFirstCard();
            temp.setVisible(false);
            pickupDeck.addtoFront(temp);
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer0) {

        }

    }

    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

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
                g2d.drawString("Click an option below!", canvas0.getWidth()/2-100, canvas0.getHeight()/2+100);

            }
            if(state == inGame){
                //TODO draw all stacks 

                int j = 0;
                for(int i = 0; i<primaryStacks.length;i++){

                    int x = 25+(110*i);
                    int y = 100+j*20;

                    g2d.drawRoundRect(x, y, 80, 160, 4, 4);

                    for(j = 0; j<primaryStacks[i].getSize(); j++){
                        
                       
                        y = 100+j*20;
                        g2d.drawImage(primaryStacks[i].get(j).getCardImage(), x, y, 75, 150, this);
                        
                        
                    }

                }
                //this is for the pickup stack:
                g2d.drawImage(pickupDeck.get(0).getCardImage(), 24, 30, 75, 150, this);

             //TODO add outline/shadow to all the stacks (primary, secondary, pickup, and waste)
             


            }
            if(state == instructions){
                
            }

        }
    }

}
