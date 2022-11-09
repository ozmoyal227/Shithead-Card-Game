package Players;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Cards.CardDeck;
import Cards.Card;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, CardDeck deck) {
        super(name, deck);
    }

    @Override
    protected void chooseFaceupCards() {
        this.printCards(this.hand);
        System.out.println("Please enter your desierd faceup cards numbers: ");
        Scanner scan = new Scanner(System.in);
        String line;
        boolean validInput = false;
        do {
            line = scan.nextLine();
            int[] chosenCards = validFaceupCards(line);
            if (chosenCards != null) {
                Card[] cards = new Card[3];
                for (int i = 0; i < cards.length; i++) {
                    cards[i] = this.hand.get(chosenCards[i] - 1);
                }
                for (int i = 0; i < cards.length; i++) {
                    this.faceupCards.add(cards[i]);
                    this.hand.remove(cards[i]);
                }

                validInput = true;
            } else {
                System.out.println("Please enter valid cards numbers: (Three numbers separated with commas)");
            }
        } while (!validInput);

    }

    // Playing player cards from user input.
    // Get last played card to play on and cards to play with.
    // Returning playable chosen card/s.
    protected List<Card> playCards(Card lastPlayedCard, List<Card> cards) {
        System.out.println("Please choose card/s to play or enter '0' to take the pile(If not empty):");
        Scanner scan = new Scanner(System.in);
        String line;
        boolean validInput = false;
        List<Card> chosenCards;
        do {
            line = scan.nextLine();
            chosenCards = validPlay(line, lastPlayedCard, cards);
            // When valid cards chosen, Removing them from hand/faceup-cards.
            if (chosenCards != null) {
                for (int i = 0; i < chosenCards.size(); i++) {
                    cards.remove(chosenCards.get(i));
                }
                validInput = true;
            } else {
                System.out.println(
                        "Cant play this move \nPlease enter valid cards numbers: \n(Card numbers separated with commas)");
            }
        } while (!validInput);
        return chosenCards;
    }

    protected List<Card> playFacedownCard(Card lastPlayedCard) {
        List<Card> cards = new ArrayList<Card>();
        Scanner scan = new Scanner(System.in);
        String line;
        boolean validInput = false;
        Card chosenCard;
        do {
            try {
                line = scan.nextLine();
                String[] validInt = validateInput(line, "^\\d$");
                int index = Integer.parseInt(validInt[0]);
                if (index >= 1 && index <= this.facedownCards.size()) {
                    chosenCard = this.facedownCards.get(Integer.parseInt(validInt[0]) - 1);
                    this.facedownCards.remove(chosenCard);
                    validInput = true;
                    System.out.println("Your chosen card is: ");
                    chosenCard.print();
                    if (!chosenCard.isAlwaysPlayable() && !(lastPlayedCard == null)
                            && !chosenCard.canPutOn(lastPlayedCard)) {
                        System.out.println("Unlucky! your card is not playable, You get the pile.");
                        this.hand.add(chosenCard);
                    } else {
                        cards.add(chosenCard);
                    }

                    TimeUnit.SECONDS.sleep(3);
                } else {
                    System.out.println(
                            "Cant play this move \nPlease enter valid card number: \n(One proper card number)");
                }
            } catch (Exception e) {
                System.out.println(
                        "Cant play this move \nPlease enter valid card number: \n(One proper card number)");
            }

        } while (!validInput);
        return cards;
    }

    private int[] validFaceupCards(String input) {
        String[] splittedLine = validateInput(input, "[1-6]+,[1-6]+,[1-6]$");
        if (splittedLine != null) {

            if (!splittedLine[0].equals(splittedLine[1]) && !splittedLine[2].equals(splittedLine[1])
                    && !splittedLine[0].equals(splittedLine[2])) {
                int[] chosenCards = new int[3];
                for (int i = 0; i < 3; i++)
                    chosenCards[i] = Integer.parseInt(splittedLine[i]);
                return chosenCards;
            }
        }
        return null;

    }

    // Validate user input of cards to play on last played card.
    // Returning chosen cards List or empty List(When user choosing to take pile) at
    // valid moves, Else return null.
    private List<Card> validPlay(String input, Card lastPlayedCard, List<Card> cards) {
        String[] splittedLine = validateInput(input, "^\\d+(,\\d+)*$");
        int index;
        if (splittedLine != null) {
            List<Card> chosenCards = new ArrayList<Card>();
            // Check for take the pile option
            if (splittedLine.length == 1 && splittedLine[0].equals("0") && lastPlayedCard != null)
                return chosenCards;

            if (splittedLine.length > 0 && splittedLine.length <= cards.size()) {
                for (int i = 0; i < splittedLine.length; i++) {
                    index = Integer.parseInt(splittedLine[i]) - 1;
                    if (index >= cards.size() || index == -1)
                        return null;
                    chosenCards.add(cards.get(index));
                    // When choosing multiple cards, all cards need to be equals.
                    if (!chosenCards.get(0).equals(chosenCards.get(i)))
                        return null;
                }
                // And last, check if the valid chosen cards are playable.
                if (lastPlayedCard == null || chosenCards.get(0).isAlwaysPlayable()
                        || chosenCards.get(0).canPutOn(lastPlayedCard))
                    return chosenCards;
            }
        }
        return null;
    }

    private String[] validateInput(String input, String regex) {
        String[] res = null;
        if (input.matches(regex)) {
            res = input.split(",");
        }
        return res;
    }

    @Override
    public int choosePlayer(List<Player> players) {
        System.out.println("JOKER!!, Please choose player number to take the pile:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println(i + 1 + ": " + players.get(i).getName());
        }
        int index = 1;
        Scanner scan = new Scanner(System.in);
        boolean validInput = false;
        do {
            try {
                index = Integer.parseInt(scan.nextLine());
                if (index > 0 && index <= players.size()) {
                    validInput = true;
                } else {
                    System.out.println("Please enter valid player name:");
                }
            } catch (Exception e) {
                System.out.println("Please enter valid player name:");
                continue;
            }

        } while (!validInput);

        return index - 1;
    }

}
