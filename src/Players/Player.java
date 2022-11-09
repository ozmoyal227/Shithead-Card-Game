package Players;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Cards.Card;
import Cards.CardDeck;

public abstract class Player {
    private final int MIN_CARDS_IN_HAND = 3;
    private String name;
    protected boolean wining;
    protected List<Card> hand = new ArrayList<Card>();
    protected List<Card> facedownCards = new ArrayList<Card>();
    protected List<Card> faceupCards = new ArrayList<Card>();

    public Player(String name, CardDeck deck) {
        this.name = name;
        this.wining = false;
        for (int i = 0; i < 3; i++)
            this.facedownCards.add(deck.draw());

        for (int i = 0; i < 6; i++)
            this.hand.add(deck.draw());

        this.chooseFaceupCards();
    }

    // Called at start of each turn.
    // Activating playCards/playFacedownCard with relevant player cards on top card
    // of pile.
    public List<Card> play(Card lastPlayedCard) {
        if (!this.hand.isEmpty()) {
            return playCards(lastPlayedCard, this.hand);
        } else if (!this.faceupCards.isEmpty()) {
            return playCards(lastPlayedCard, this.faceupCards);
        } else {
            return playFacedownCard(lastPlayedCard);
        }
    }

    public void takePile(List<Card> pile) {
        this.hand.addAll(pile);
    }

    // Function that called every time an active player finished playing is cards.
    // Starting by drawing cards from the game deck if needed, Then wining check.
    public void endTurn(CardDeck deck) {
        while (this.hand.size() < MIN_CARDS_IN_HAND && deck.getLength() > 0) {
            this.hand.add(deck.draw());
        }
        if (this.hand.isEmpty() && this.faceupCards.isEmpty() && this.facedownCards.isEmpty())
            this.wining = true;
    }

    public String getName() {
        return this.name;
    }

    public List<Card> getHand() {
        return this.hand;
    }

    public List<Card> getFaceupCards() {
        return this.faceupCards;
    }

    public List<Card> getFacedownCards() {
        return this.facedownCards;
    }

    public boolean isWining() {
        return this.wining;
    }

    public void printCards(List<Card> cards) {
        System.out.println(this.name + " cards:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.print(i + 1 + ": ");
            cards.get(i).print();
            System.out.println();
        }
        System.out.println();
    }

    // Function to print the player current state.
    public void printStatus() {
        System.out.print(this.name + " : hand-" + this.hand.size());
        System.out.print(", faceup-" + this.faceupCards.size());
        System.out.println(", facedown-" + this.facedownCards.size() + ".");
    }

    // Abstract functions that must be implemented differently by different type of
    // Players:

    protected abstract void chooseFaceupCards();

    protected abstract List<Card> playCards(Card lastPlayedCard, List<Card> cards);

    protected abstract List<Card> playFacedownCard(Card lastPlayed);

    public abstract int choosePlayer(List<Player> players);

}
