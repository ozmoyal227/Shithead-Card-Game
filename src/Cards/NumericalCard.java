package Cards;

public class NumericalCard extends Card {

    public NumericalCard(int value) {
        super(value);
        setAlwaysPlayable(false);
    }
}
