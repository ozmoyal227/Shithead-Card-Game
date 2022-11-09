package Cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CardDeck {
    final int NUM_OF_CARDS = 54;
    final Integer[] WILDCARDS = new Integer[] { 2, 3, 7, 8, 10 };
    final int KING_VALUE = 13;
    final int CARD_REPETITION = 4;
    List<Card> deck = new ArrayList<Card>();

    public CardDeck() {
        // Adding cards to the deck, separating numerical cards and wildcards, Ace
        // represented as 14
        for (int i = 2; i <= KING_VALUE + 1; i++)
            for (int j = 0; j < CARD_REPETITION; j++) {
                if (Arrays.asList(WILDCARDS).contains(i)) {
                    this.deck.add(new Wildcard(i));
                } else
                    this.deck.add(new NumericalCard(i));
            }
        // Standard deck of cards contains two jokers, which represented with value: 15
        this.deck.add(new Wildcard(KING_VALUE + 2));
        this.deck.add(new Wildcard(KING_VALUE + 2));
        // Shuffle the deck:
        Random rand = new Random();
        for (int i = 0; i < NUM_OF_CARDS; i++) {
            int randomNum = rand.nextInt(NUM_OF_CARDS);
            Card temp = deck.get(i);
            deck.set(i, deck.get(randomNum));
            deck.set(randomNum, temp);
        }

    }

    public int getLength() {
        return this.deck.size();
    }

    public Card get(int i) {
        if (0 <= i && i < NUM_OF_CARDS)
            return this.deck.get(i);
        return null;
    }

    public Card draw() {
        return deck.remove(0);
    }

}
