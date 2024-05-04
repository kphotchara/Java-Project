package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Models a deck of 52 cards.
 */
public class Deck extends BaseStack
{
	//private CardStack aCards;llllllll

	/**
	 * Creates a new deck of 52 cards, shuffled.
	 */
	public Deck()
	{
		super();
		shuffle();
	}
	
	/**
	 * Reinitializes the deck with all 52 cards, and shuffles them.
	 */
	public void shuffle()
	{
		//List<Card> cards = new ArrayList<>();
		for( Suit suit : Suit.values() )
		{
            for( Rank rank : Rank.values() )
            {
                aCards.add( Card.get( rank, suit ));
            }
		}
		Collections.shuffle(aCards);
		//aCards = new CardStack(cards);llll
	}
	
	/**
	 * Places pCard on top of the deck.
	 * @param pCard The card to place on top of the deck.
	 * @pre pCard !=null
	 */
	@Override
	public void push(Card pCard)
	{
		super.push(pCard);
	}
	
	/**
	 * Draws a card from the deck and removes the card from the deck.
	 * @return The card drawn.
	 * @pre !isEmpty()
	 */
	@Override
	public Card pop()
	{
		return super.pop();
	}
	
	/**
	 * @return True iff there are no cards in the deck.
	 */
	@Override
	public boolean isEmpty()
	{
		return super.isEmpty();
	}
}
