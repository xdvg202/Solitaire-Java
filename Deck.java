import java.util.*;

public class Deck {

    private ArrayList<Card> Deck = new ArrayList<Card>();
    private int[] frontXY = { 0, 0 };

    public Deck(boolean toBePopulated) {
        if (toBePopulated) {
            addCleanDeck();
        }
    }

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
