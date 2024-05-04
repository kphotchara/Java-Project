package cards;

import java.util.ArrayList;
import java.util.List;

public class BaseStack {
    protected List<Card> aCards;

    public BaseStack(){
        aCards = new ArrayList<>();
    }

    public void push(Card pCard)
    {
        assert pCard != null;
        aCards.add(pCard);
    }

    public Card pop()
    {
        assert !isEmpty();
        return aCards.remove(aCards.size()-1);
    }

    public boolean isEmpty()
    {
        return aCards.size() == 0;
    }

    public void setACards(List<Card> aCards) {
        this.aCards = aCards;
    }
}
