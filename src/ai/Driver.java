package ai;

import model.GameModel;

/**
 * Plays N games and computes the number of wins.
 */
public final class Driver
{
	private static final int ALL_CARDS = 52;
	private static final int NUMBER_OF_GAMES = 10000;
	private static final int TO_PERCENT = 100;
	
	private Driver() {}
	
	/**
	 * @param pArgs Not used.
	 */
	public static void main(String[] pArgs)
	{
		int total = 0;
		int totalWon = 0;
		GameModel model = new GameModel(new GreedyPlayingStrategy());
		for( int i = 0; i < NUMBER_OF_GAMES; i++ )
		{
			playGame(model);
			int score = model.getScore();
			total += score;
			if( score == ALL_CARDS )
			{
				totalWon++;
			}
		}
		System.out.println(String.format("Ratio won     %d/%d=%.1f%%", totalWon, NUMBER_OF_GAMES,
				((double)totalWon)/((double)NUMBER_OF_GAMES)*TO_PERCENT));
		System.out.println(String.format("Average score %d/%d=%.1f", total, NUMBER_OF_GAMES, 
				((double)total)/((double)NUMBER_OF_GAMES)));
	}
	
	private static void playGame(GameModel pModel)
	{
		pModel.reset();
		boolean advanced = true;
		while( advanced )
		{
			advanced = pModel.tryToAutoPlay();
		}
	}
}
