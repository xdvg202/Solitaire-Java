import java.util.*;

public class Deck {

    private ArrayList<Card> Deck = new ArrayList<Card>();

    public Deck(boolean toBePopulated) {
        if (toBePopulated) {
            addCleanDeck();
        }
    }

    public Card popFirstCard() {
        Card tempCard = Deck.get(0);
        Deck.remove(0);
        return tempCard;
    }

    public void addToBack(Card c) {
        Deck.add(Deck.size() - 1, c);
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
                Card tempCard = new Card(i, temp, false);

                Deck.add(tempCard);
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(Deck);
    }
    public void addtoFront(Card c){
        Deck.add(0, c);
    }

    public int getSize() {
        return Deck.size();
    }
    public Card get(int i){
        return Deck.get(i);
    }

}
