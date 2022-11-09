package Players;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Cards.Card;
import Cards.CardDeck;

public class AIPlayer extends Player {
    private static int playerCnt = 1;

    public AIPlayer(CardDeck deck) {
        super("Player" + Integer.toString(playerCnt), deck);
        playerCnt++;
    }

    @Override
    protected void chooseFaceupCards() {
        try {
            System.out.println(this.getName() + " Choosing faceup cards...");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < this.hand.size(); i++) {
            if (this.hand.get(i).isAlwaysPlayable()) {
                this.faceupCards.add(this.hand.get(i));
            }
            if (this.faceupCards.size() == 3)
                break;
        }
        for (int i = 0; i < this.faceupCards.size(); i++) {
            this.hand.remove(this.faceupCards.get(i));
        }

        while (this.faceupCards.size() != 3) {
            Card max = this.hand.get(0);
            for (int i = 0; i < this.hand.size(); i++) {
                if (this.hand.get(i).getValue() > max.getValue())
                    max = this.hand.get(i);
            }
            this.faceupCards.add(max);
            this.hand.remove(max);
        }

    }

    @Override
    protected List<Card> playCards(Card lastPlayedCard, List<Card> cards) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Card> chosenCards = new ArrayList<Card>();
        if (this.hand.size() != 0 || this.faceupCards.size() != 0) {
            Card min = null;
            // Searching for "weak" playable card
            for (int i = 0; i < cards.size(); i++) {
                if (!cards.get(i).isAlwaysPlayable() && cards.get(i).canPutOn(lastPlayedCard)) {
                    min = updatedMin(min, cards.get(i));
                }
            }
            // There is not "weak" playable card, search for any minimum playable card
            if (min == null) {
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i).isAlwaysPlayable()) {
                        min = updatedMin(min, cards.get(i));
                    }
                }
            }

            if (min != null) {
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i).getValue() == min.getValue()) {
                        chosenCards.add(cards.get(i));
                    }
                }
                for (int i = 0; i < chosenCards.size(); i++) {
                    cards.remove(chosenCards.get(i));
                }
            }

        } else {
            chosenCards.add(this.facedownCards.get(0));
            this.facedownCards.remove(0);
        }
        return chosenCards;
    }

    protected List<Card> playFacedownCard(Card lastPlayedCard) {
        List<Card> cards = new ArrayList<Card>();
        Card chosenCard;
        chosenCard = this.facedownCards.get(0);
        this.facedownCards.remove(chosenCard);
        System.out.println(this.getName() + " got: ");
        chosenCard.print();
        System.out.println(" from facedown card");
        if (!chosenCard.isAlwaysPlayable() && !(lastPlayedCard == null)
                && !chosenCard.canPutOn(lastPlayedCard)) {
            System.out.println("Unlucky! the card is not playable, get the pile!");
            this.hand.add(chosenCard);
        } else {
            cards.add(chosenCard);
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cards;
    }

    private Card updatedMin(Card min, Card card) {
        if (min != null) {
            if (min.getValue() > card.getValue())
                min = card;
        } else {
            min = card;
        }
        return min;
    }

    @Override
    public int choosePlayer(List<Player> players) {
        int index = 0, min = 54;

        for (int i = 0; i < players.size(); i++) {
            if (!this.equals(players.get(i))) {
                if (players.get(i).getHand().size() < min) {
                    index = i;
                    min = players.get(i).getHand().size();
                }
            }
        }
        return index;
    }

}
