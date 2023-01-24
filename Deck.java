import java.util.*;

public class Deck {

    private ArrayList<Card> Deck = new ArrayList<Card>();

    // contructor which will fill the deck if it has been marked for that task.
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

    // pops the first card(does remove it)
    public Card pop() {
        if (Deck.size() > 0) {
            Card tempCard = Deck.get(Deck.size() - 1);
            Deck.remove(Deck.size() - 1);
            return tempCard;
        } else {
            return null;
        }
    }

    

    // fills with a clean deck that isnt shuffled
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

    // shuffle the deck
    public void shuffleDeck() {
        Collections.shuffle(Deck);
    }

    // add a card to the end
    public void push(Card c) {

        Deck.add(c);

    }

    // get the size of the deck
    public int getSize() {
        return Deck.size();
    }

    // get a card from a certain index (without removing it)
    public Card get(int i) {
        return Deck.get(i);
    }

    
}
