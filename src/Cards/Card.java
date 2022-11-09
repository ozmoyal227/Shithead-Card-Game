package Cards;

public abstract class Card {

    private int value;
    private boolean alwaysPlayable;

    public Card(int value) {
        this.value = value;
    }

    public boolean canPutOn(Card card) {
        if (card == null)
            return true;
        return card.getValue() == 7 ? (this.value <= 7) : card.getValue() <= this.value;

    };

    public int getValue() {
        return this.value;
    }

    public boolean isAlwaysPlayable() {
        return this.alwaysPlayable;
    }

    public void setAlwaysPlayable(boolean state) {
        this.alwaysPlayable = state;
    }

    public boolean equals(Card card) {
        return this.value == card.getValue();
    }

    public void print() {
        String symbol;
        switch (this.value) {
            case 11:
                symbol = "J";
                break;
            case 12:
                symbol = "Q";
                break;
            case 13:
                symbol = "K";
                break;
            case 14:
                symbol = "A";
                break;
            case 15:
                symbol = "JOKER";
                break;
            default:
                symbol = String.valueOf(this.value);
        }

        System.out.print(symbol);
    }

}