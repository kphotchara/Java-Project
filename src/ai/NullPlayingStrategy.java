package ai;

import model.GameModelView;
import model.Move;

/**
 * Never does anything.
 */
public class NullPlayingStrategy implements PlayingStrategy
{
	/**
	 * Creates a new strategy.
	 */
	public NullPlayingStrategy() {}
	
	@Override
	public Move getLegalMove(GameModelView pModel)
	{
		return pModel.getNullMove();
	}
}
