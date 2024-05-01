package ai;

import cards.Card;
import cards.CardStack;
import model.FoundationPile;
import model.GameModelView;
import model.Move;
import model.TableauPile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Makes the first possible move in this order: 
 * 1. Discarding if the discard pile is empty;
 * 2. Moving a card from the discard pile to a foundation pile;
 * 3. Moving a card from the discard pile to the tableau;
 * 4. Moving a card from the tableau to a foundation pile, in order
 * of piles;
 * 5. Moving from one pile in the tableau to another, if this either reveals
 * a fresh card or frees up a pile for a king.
 * 6. None of the above was possible, discards if possible.
 * 7. If discarding was not possible, return the null move.
 */
public class GreedyPlayingStrategy implements PlayingStrategy
{
	private static final List<Function<GameModelView, Move>> SUBSTRATEGIES = new ArrayList<>();

	static
	{
		SUBSTRATEGIES.add(GreedyPlayingStrategy::substrategyDiscardIfDiscardPileIsEmpty);
		SUBSTRATEGIES.add(GreedyPlayingStrategy::substrategyMoveDiscardToFoundation);
		SUBSTRATEGIES.add(GreedyPlayingStrategy::substrategyMoveDiscardToTableau);
		SUBSTRATEGIES.add(GreedyPlayingStrategy::substrategyMoveFromTableauToFoundation);
		SUBSTRATEGIES.add(GreedyPlayingStrategy::substrategyMoveWithinTableau);
		SUBSTRATEGIES.add(GreedyPlayingStrategy::substrategyDiscard);
	}
	
	/**
	 * Creates a new strategy.
	 */
	public GreedyPlayingStrategy() {}
	
	/*
	 * If the discard pile is empty, discard. 
	 */
	private static Move substrategyDiscardIfDiscardPileIsEmpty(GameModelView pModel)
	{
		if( pModel.isDiscardPileEmpty() && !pModel.isDeckEmpty() )
		{
			return pModel.getDiscardMove();
		}
		else
		{
			return pModel.getNullMove();
		}
	}
	
	/*
	 * If it's possible to move the top of the discard pile to the foundation, do it.
	 */
	private static Move substrategyMoveDiscardToFoundation(GameModelView pModel)
	{
		if( pModel.isDiscardPileEmpty() )
		{
			return pModel.getNullMove();
		}
		for(FoundationPile pile : FoundationPile.values())
		{
			if( pModel.isLegalMove(pModel.peekDiscardPile(), pile))
			{
				return pModel.getCardMove(pModel.peekDiscardPile(), pile);
			}
		}
		return pModel.getNullMove();
	}
	
	private static Move substrategyMoveDiscardToTableau(GameModelView pModel)
	{
		if( pModel.isDiscardPileEmpty() )
		{
			return pModel.getNullMove();
		}
		for(TableauPile pile : TableauPile.values())
		{
			if( pModel.isLegalMove(pModel.peekDiscardPile(), pile))
			{
				return pModel.getCardMove(pModel.peekDiscardPile(), pile);
			}
		}
		return pModel.getNullMove();
	}
	
	private static Move substrategyMoveFromTableauToFoundation(GameModelView pModel)
	{
		for(TableauPile tableauPile : TableauPile.values())
		{
			CardStack stack = pModel.getTableauPile(tableauPile);
			if( !stack.isEmpty() )
			{
				Card card = stack.peek();
				for(FoundationPile foundationPile : FoundationPile.values())
				{
					if( pModel.isLegalMove(card, foundationPile))
					{
						return pModel.getCardMove(card, foundationPile);
					}
				}
			}	
		}
		return pModel.getNullMove();
	}
	
	/* Only if it reveals a card or empties a pile. We also don't move kings between empty piles */
	private static Move substrategyMoveWithinTableau(GameModelView pModel)
	{
		for( TableauPile pile : TableauPile.values())
		{
			CardStack stack = pModel.getTableauPile(pile);
			for( Card card : stack )
			{
				if( pModel.isBottomKing(card))
				{
					continue;
				}
				if( pModel.isLowestVisibleInTableau(card))
				{
					for( TableauPile pile2 : TableauPile.values() )
					{
						if( pModel.isLegalMove(card, pile2))
						{
							return pModel.getCardMove(card, pile2);
						}
					}
				}
			}
		}
		return pModel.getNullMove();
	}
	
	private static Move substrategyDiscard(GameModelView pModel)
	{
		if( pModel.isDeckEmpty() )
		{
			return pModel.getNullMove();
		}
		else
		{
			return pModel.getDiscardMove();
		}
	}
	
	@Override
	public Move getLegalMove(GameModelView pModel)
	{
		for( Function<GameModelView, Move> substrategy : SUBSTRATEGIES )
		{
			Move move = substrategy.apply(pModel);
			if( !move.isNull() )
			{
				return move;
			}
		}
		return pModel.getNullMove();
	}
}
