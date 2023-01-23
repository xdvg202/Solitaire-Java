import java.util.*;

public class Deck {

    private ArrayList<Card> Deck = new ArrayList<Card>();
    
//contructor which will fill the deck if it has been marked for that task.
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

//pops the first card(does remove it)
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
//fills with a clean deck that isnt shuffled
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
//shuffle the deck
    public void shuffleDeck() {
        Collections.shuffle(Deck);
    }
//add a card to the front
    public void addtoFront(Card c) {
        Deck.add(0, c);
    }
//get the size of the deck
    public int getSize() {
        return Deck.size();
    }
//get a card from a certain index (without removing it)
    public Card get(int i) {
        return Deck.get(i);
    }
//get the front card from the deck (without removing it)
    public Card getFirst() {
        return Deck.get(0);
    }

}
