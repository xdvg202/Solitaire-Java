import java.util.*;

public class Deck {

    private ArrayList<Card> Deck = new ArrayList<Card>();
    private int[] frontXY = { 0, 0 };

    public Deck(boolean toBePopulated) {
        if (toBePopulated) {
            addCleanDeck();
        }
    }
    //TODO add method to have an array of cards that are face up and face down and then move that array around.

    // non modifier method to get the front card in the stack
    public Card getFirstCard() {
        if (Deck.size() > 0) {
            return Deck.get(0);
        } else {
            return null;
        }
    }

    // stores the x and y of the front card in the stack.
    public void setFrontXY(int x, int y) {
        frontXY[0] = x;
        frontXY[1] = y;
    }

    public int[] getFrontXY() {
        return frontXY;
    }

    public Card popFirstCard() {
        if (Deck.size() > 0) {
            Card tempCard = Deck.get(0);
            Deck.remove(0);
            return tempCard;
        } else {
            return null;
        }
    }

    //TODO might have to move/remove this method
    public boolean isCardSequential(Card c){
        int value = c.getValue();
        Card back = Deck.get(Deck.size()-1);

        //notes the color of the other card trying to be put on.
        boolean red = false;
        if(c.getSuit().equals("h")||c.getSuit().equals("d")){
            red = true;
        }
        //notes the color of the card in the back of this deck
        boolean red2 = false;
        if(back.getSuit().equals("h")||back.getSuit().equals("d")){
            red = true;
        }
        //if the card value difference is one.
        if(value-(back.getValue()) == 1){
            //check if suits match
            if(red2!=red){
                return true;
            }
           
        }
        return false;

    }

    public void addCleanDeck() {
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j <= 4; j++) {
                String temp = null;
                if (j == 1) {
                    temp = "d";
                }
                if (j == 2) {
                    temp = "s";
                }
                if (j == 3) {
                    temp = "c";
                }
                if (j == 4) {
                    temp = "h";
                }
                Card tempCard = new Card(i, temp);

                Deck.add(tempCard);
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(Deck);
    }

    public void addtoFront(Card c) {
        Deck.add(0, c);
    }

    public int getSize() {
        return Deck.size();
    }

    public Card get(int i) {
        return Deck.get(i);
    }

    public Card getFirst() {
        return Deck.get(0);
    }

}
