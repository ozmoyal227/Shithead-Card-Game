package Game_Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Cards.*;
import Players.*;

public class Game {
    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 6;
    private List<Player> players = new ArrayList<Player>();
    private CardDeck deck = new CardDeck();
    private List<Card> pile = new ArrayList<Card>();
    private int currPlayer;
    private int burnedNum = 0;

    public Game() {
        System.out.println("Lets play SHITHEAD!");
        int numOfPlayers = getValidNumOfPlayers();
        // In this version of the game only one human player can enter(the rest are AI),
        // It is changeable.
        String name = getValidPlayerName();
        players.add(new HumanPlayer(name, this.deck));
        for (int i = 1; i < numOfPlayers; i++) {
            players.add(new AIPlayer(deck));
        }
        this.currPlayer = getFirstPlayerIndex();
    }

    // THis function manages the game flow to start and finish every player turn.
    public void startFlow() {
        while (this.players.size() != 1) {
            Card lastPlayedCard = getLastPlayedCard();
            try {
                printGameState(lastPlayedCard);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            Player playingPlayer = this.players.get(this.currPlayer);
            List<Card> chosenCards = playingPlayer.play(lastPlayedCard);
            // Always getting List when activate play, If the player cant play(Or chooses
            // to) the List is empty
            if (chosenCards.isEmpty()) {
                playingPlayer.takePile(this.pile);
                this.pile.clear();
            } else {
                this.pile.addAll(chosenCards);
            }
            activateEffect();
            playingPlayer.endTurn(this.deck);
            if (playingPlayer.isWining())
                this.players.remove(playingPlayer);
        }

        System.out.println("\nGame Over " + this.players.get(0).getName() + " is the looser!");
    }

    // Function to print current game state every new turn.
    private void printGameState(Card lastPlayedCard) throws InterruptedException, IOException {
        // This will clear the Terminal every time printGameState called, To print new
        // game state.
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        // Runtime.getRuntime().exec("cls");
        Player playingPlayer = this.players.get(this.currPlayer);
        System.out.println("Its " + playingPlayer.getName() + "'s turn:");
        System.out.println("=============================================");
        System.out.println("Remaining players: " + this.players.size() + ".");
        // Printing every player current status
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            player.printStatus();
        }
        System.out.println("Number of cards in deck: " + this.deck.getLength() + ".");
        System.out.println("Number of burned cards: " + this.burnedNum + ".");
        // Showing top three pile cards for completion consideration to quartet.
        if (!this.pile.isEmpty()) {
            System.out.print("Current top three cards on pile (LtR):");
            this.pile.get(this.pile.size() - 1).print();
            if (this.pile.size() > 1) {
                System.out.print(", ");
                this.pile.get(this.pile.size() - 2).print();
            }
            if (this.pile.size() > 2) {
                System.out.print(", ");
                this.pile.get(this.pile.size() - 3).print();
            }
            System.out.println(".");
            if (lastPlayedCard != null) {
                System.out.println("Top pile card:");
                lastPlayedCard.print();
                System.out.println();
            }
            System.out.println("Number of cards in pile: " + this.pile.size() + ".");
        } else {
            System.out.println("Pile is empty");
        }
        // When Human player is playing, printing relevant cards.
        if (playingPlayer instanceof HumanPlayer) {
            if (!playingPlayer.getHand().isEmpty())
                playingPlayer.printCards(playingPlayer.getHand());
            else if (!playingPlayer.getFaceupCards().isEmpty())
                playingPlayer.printCards(playingPlayer.getFaceupCards());
            else {
                System.out.println(
                        "Now playing from facedown cards, You have: " + playingPlayer.getFacedownCards().size()
                                + " cards.");
                System.out.println("Choose one card number in range:");
            }

        }

    }

    private void activateEffect() {
        Card currCard = this.pile.isEmpty() ? null : this.pile.get(this.pile.size() - 1);
        Player currPlayer = this.players.get(this.currPlayer);
        // If pile is empty after play no special effect.
        if (currCard != null) {
            // If pile burned.
            if (currCard.getValue() == 10 || isQuartet()) {
                this.burnedNum += this.pile.size();
                this.pile.clear();
            }
            // Joker routine
            else if (currCard.getValue() == 15) {
                int chosenPlayer = currPlayer.choosePlayer(players);
                this.players.get(chosenPlayer).takePile(this.pile);
                this.pile.clear();
                // Skipping next player on 8 play.
            } else if (currCard.getValue() == 8) {
                setNextPlayerTurn();
            }
            // Making sure to pass the turn after effects.
            if (currCard.getValue() != 10 || (currPlayer.getHand().isEmpty() && currPlayer.getFacedownCards().isEmpty()
                    && this.deck.getLength() == 0))
                setNextPlayerTurn();
        } else {
            setNextPlayerTurn();
        }

    }

    private int getFirstPlayerIndex() {
        int minValue = 3;
        boolean firstPlayerFound = false;
        do {
            for (int i = 0; i < this.players.size(); i++) {
                if (hasMinValue(this.players.get(i), minValue))
                    return i;
            }
            minValue++;

        } while (!firstPlayerFound);
        return 0;
    }

    private boolean hasMinValue(Player player, int value) {
        for (int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getValue() == value)
                return true;
        }
        return false;
    }

    private int getValidNumOfPlayers() {
        System.out.println("please enter number of players (2-6): ");
        Scanner scan = new Scanner(System.in);
        boolean validInput = false;
        int numOfPlayers = 2;
        do {
            try {
                numOfPlayers = Integer.parseInt(scan.nextLine());
                if (numOfPlayers < MIN_PLAYERS || numOfPlayers > MAX_PLAYERS) {
                    System.out.println("Please enter valid number of players:");
                } else {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Please enter valid number of players:");
                continue;
            }

        } while (!validInput);
        return numOfPlayers;
    }

    private String getValidPlayerName() {
        System.out.println("please enter your name (at least one character): ");
        Scanner scan = new Scanner(System.in);
        boolean validInput = false;
        String name;
        do {

            name = scan.nextLine();
            if (name.length() == 0) {
                System.out.println("Please enter valid number of players:");
            } else {
                validInput = true;
            }
        } while (!validInput);
        return name;
    }

    private boolean isQuartet() {
        if (this.pile.size() > 3) {
            int i = this.pile.size() - 1;
            if (this.pile.get(i).equals(this.pile.get(i - 1)) && this.pile.get(i - 1).equals(this.pile.get(i - 2))
                    && this.pile.get(i - 2).equals(this.pile.get(i - 3))) {
                return true;
            }
        }
        return false;
    }

    private void setNextPlayerTurn() {
        currPlayer++;
        if (currPlayer == this.players.size())
            currPlayer = 0;
    }

    // Called every start of a turn, Searching for top pile card, Skips 3's cards.
    private Card getLastPlayedCard() {
        Card lastPlayedCard = !this.pile.isEmpty() ? this.pile.get(this.pile.size() - 1) : null;
        int i = this.pile.size() - 2;
        // Checking and finding last card beneath the card 3
        while (lastPlayedCard != null && lastPlayedCard.getValue() == 3 && i >= 0) {
            lastPlayedCard = this.pile.get(i);
            if (lastPlayedCard.getValue() != 3)
                break;
            i--;
        }

        return lastPlayedCard;
    }
}
