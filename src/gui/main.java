package gui;

import ai.GreedyPlayingStrategy;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import model.FoundationPile;
import model.GameModel;
import model.TableauPile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.*;

/**
 * Application class for Solitaire. The responsibility of this class is limited
 * to assembling the major UI components and launching the application. All
 * gesture handling logic is handled by its composed elements, which act
 * as observers of the game model.
 */
public class main extends Application
{
	private static final int WIDTH = 680;
	private static final int HEIGHT = 500;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitaire";
	private static final String VERSION = "8.8.8";

	SoundPlayer gameMusic = new SoundPlayer("res/background.wav");

	/**
	 * Application head.
	 */
	public main() {}
    
	/**
	 * Launches the application.
	 * @param pArgs This program takes no argument.
	 */
	public static void main(String[] pArgs) 
	{
        launch(pArgs);
    }
	    
    @Override
    public void start(Stage pPrimaryStage)
    {
		gameMusic.setLoop();
		gameMusic.startPlay();

		pPrimaryStage.setTitle(TITLE + " " + VERSION); 

        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: green;");
        root.setHgap(MARGIN_OUTER);
        root.setVgap(MARGIN_OUTER);
        root.setPadding(new Insets(MARGIN_OUTER));
        
    	final GameModel model = new GameModel(new GreedyPlayingStrategy());
    	DeckView deckView = new DeckView(model);
        DiscardPileView discardPileView = new DiscardPileView(model);
		Button giveUpBtn = new Button("give up");

		root.add(giveUpBtn, 0, 0);
        root.add(deckView, 0, 1);
        root.add(discardPileView, 1, 1);
		
        for( FoundationPile index : FoundationPile.values() )
        {
        	root.add(new SuitStack(model, index), 3+index.ordinal(), 1);
        }
      
        for( TableauPile index : TableauPile.values() )
        {
        	root.add(new CardPileView(model, index), index.ordinal(), 2);
        }

        root.setOnKeyTyped(new EventHandler<KeyEvent>()
		{

			@Override
			public void handle(final KeyEvent pEvent)
			{
				if( pEvent.getCharacter().equals("\r"))
				{
					model.tryToAutoPlay();
				}
				else if( pEvent.getCharacter().equals("u"))
				{
					model.undoLast();
				}
				pEvent.consume();
			}
		});

		giveUpBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent pEvent){
				model.reset();
			}
		});
        
        pPrimaryStage.setResizable(false);
        pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        pPrimaryStage.show();
    }
}