package Cards;

public class Wildcard extends Card {

    public Wildcard(int value) {
        super(value);
        if (value == 7 || value == 8)
            setAlwaysPlayable(false);
        else
            setAlwaysPlayable(true);
    }

}
