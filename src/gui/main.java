package gui;

import ai.GreedyPlayingStrategy;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.FoundationPile;
import model.GameModel;
import model.TableauPile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.w3c.dom.ls.LSOutput;

/**
 * Application class for Solitaire. The responsibility of this class is limited
 * to assembling the major UI components and launching the application. All
 * gesture handling logic is handled by its composed elements, which act
 * as observers of the game model.
 */
public class main extends Application
{
	private static final int WIDTH = 700;
	private static final int HEIGHT = 600;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitaire";
	private static final String VERSION = "8.8.8";
	boolean soundPlaying = true;
	SoundPlayer gameMusic = new SoundPlayer(ClassLoader.getSystemResource("background.mp3").toString());
	SoundPlayer giveUpSound = new SoundPlayer(ClassLoader.getSystemResource("gameover.wav").toString());

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
		gameMusic.setVolume(0.45);
		gameMusic.setLoop();
		gameMusic.startPlay();

		pPrimaryStage.setTitle(TITLE + " " + VERSION);

		VBox homepageLayout = new VBox();
		BackgroundSize backgroundSize = new BackgroundSize(700, 600, false, false, false, true);
		homepageLayout.setBackground(new Background(new BackgroundImage(new Image("homepage-background.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));;
		homepageLayout.setSpacing(10);
		homepageLayout.setPadding(new Insets(20));
		homepageLayout.setAlignment(Pos.CENTER);

		ImageView logo = new ImageView(new Image("logo.png"));
		Button startBtn = new Button("Start Game");
		startBtn.setStyle("-fx-background-color: #FB688E; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 5em; -fx-padding: 5px 20px; -fx-border-color: #FB688E; -fx-border-radius: 5em; -fx-min-width: 120px; -fx-min-height: 40px;");

		homepageLayout.getChildren().addAll(logo, startBtn);

        GridPane root = new GridPane();
		root.setBackground(new Background(new BackgroundImage(new Image("background.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));
        root.setHgap(MARGIN_OUTER);
        root.setVgap(MARGIN_OUTER);
        root.setPadding(new Insets(MARGIN_OUTER));
        
    	final GameModel model = new GameModel(new GreedyPlayingStrategy());
    	DeckView deckView = new DeckView(model);
        DiscardPileView discardPileView = new DiscardPileView(model);
		Button newGameBtn = new Button("New Game");
		Button soundBtn = new Button();
		newGameBtn.setMaxSize(90,30);
		newGameBtn.setMinSize(90,30);
		newGameBtn.setStyle("-fx-background-color: white; -fx-text-fill: #36469B; -fx-background-radius: 5em;");
		ImageView soundImg = new ImageView(ClassLoader.getSystemResource("icons8-sound-50.png").toString());
		ImageView muteImg = new ImageView(ClassLoader.getSystemResource("icons8-mute-50.png").toString());
		soundImg.setFitWidth(30);
		soundImg.setFitHeight(30);
		muteImg.setFitWidth(30);
		muteImg.setFitHeight(30);
		soundBtn.setGraphic(soundImg);
		soundBtn.setAlignment(Pos.CENTER);

		soundBtn.setStyle("-fx-background-color: transparent, transparent, transparent, transparent, transparent;" +
				"    -fx-pref-height: 30;" +
				"    -fx-pref-width: 30;" +
				"    -fx-min-height: 30;" +
				"    -fx-min-width: 30;" +
				"    -fx-max-height: 30;" +
				"    -fx-max-width: 30;");

		root.add(soundBtn, 0, 0);
		GridPane.setMargin(soundBtn, new Insets(0, 0, 0, 6));
		root.add(newGameBtn, 6, 0);
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

		newGameBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent pEvent) {
				try {
					Thread.sleep(2500);
					giveUpSound.stopPlay();
					if(soundPlaying)gameMusic.startPlay();
				}
				catch (InterruptedException ie){
					Thread.currentThread().interrupt();
				}


			}
		});

		newGameBtn.setOnMouseReleased(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent pEvent){
				giveUpSound.startPlay();
				gameMusic.stopPlay();
				model.reset();
			}
		});

		newGameBtn.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				newGameBtn.setStyle("-fx-background-color: #FB688E; -fx-background-radius: 5em; -fx-text-fill: white;");
				if(model.isCompleted())newGameBtn.setText("again?");
				else newGameBtn.setText("don't give up");
			}
		});

		newGameBtn.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				newGameBtn.setStyle("-fx-background-color: white; -fx-background-radius: 5em; -fx-text-fill: #36469B;");
				newGameBtn.setText("New Game");
			}
		});

		startBtn.setOnMouseEntered(e -> {
			startBtn.setStyle("-fx-background-color: white; -fx-background-radius: 5em; -fx-text-fill: #FB688E; -fx-font-size: 16px; -fx-border-radius: 5em; -fx-padding: 5px 20px; -fx-border-color: #FB688E; -fx-min-width: 120px; -fx-min-height: 40px;");
		});

		startBtn.setOnMouseExited(e -> {
			startBtn.setStyle("-fx-background-color: #FB688E; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 5em; -fx-padding: 5px 20px; -fx-border-color: #FB688E; -fx-border-radius: 5em; -fx-min-width: 120px; -fx-min-height: 40px;");
		});

		soundBtn.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent pEvent)
			{
				if(soundPlaying){
					if(gameMusic.isPlaying()) {
						gameMusic.stopPlay();
					}
					soundBtn.setGraphic(muteImg);
				}
				else {
					if(!gameMusic.isPlaying()) {
						gameMusic.startPlay();
					}
					soundBtn.setGraphic(soundImg);
				}
				soundPlaying = !soundPlaying;

			}
		});

		startBtn.setOnAction(e -> {
			pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		});


        pPrimaryStage.setResizable(false);
        pPrimaryStage.setScene(new Scene(homepageLayout, WIDTH, HEIGHT));
        pPrimaryStage.show();

    }

}