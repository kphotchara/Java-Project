package ai;

import model.GameModel;

/**
 * Plays N games and, for each game, undo
 * all moves and redo them.
 */

public final class CrashTest
{
	private static final int NUMBER_OF_GAMES = 1000;
	
	private CrashTest() {}
	
	/**
	 * @param pArgs Not used.
	 */
	public static void main(String[] pArgs)
	{
		GameModel model = new GameModel(new GreedyPlayingStrategy());
		for( int i = 0; i < NUMBER_OF_GAMES; i++ )
		{
			playGame(model);
		}
		System.out.println("Runs completed.");
	}
	
	private static void playGame(GameModel pModel)
	{
		pModel.reset();
		boolean advanced = true;
		while( advanced )
		{
			advanced = pModel.tryToAutoPlay();
		}
		while( pModel.canUndo() )
		{
			pModel.undoLast();
		}
		advanced = true;
		while( advanced )
		{
			advanced = pModel.tryToAutoPlay();
		}
	}
}
