package gui;

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

/**
 * Application class for Solitaire. The responsibility of this class is limited
 * to assembling the major UI components and launching the application. All
 * gesture handling logic is handled by its composed elements, which act
 * as observers of the game model.
 */
public class Main extends Application
{
	private static final int WIDTH = 700;
	private static final int HEIGHT = 600;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitaire";
	private static final String VERSION = "8.8.8";
	private boolean soundPlaying = true;
	private SoundPlayer gameMusic = new SoundPlayer(ClassLoader.getSystemResource("background.mp3").toString());
	private SoundPlayer giveUpSound = new SoundPlayer(ClassLoader.getSystemResource("gameover.wav").toString());
	private SoundPlayer btnSound = new SoundPlayer(ClassLoader.getSystemResource("btnSound.mp3").toString());
	private SoundPlayer homePageSound = new SoundPlayer(ClassLoader.getSystemResource("homepage.mp3").toString());

	/**
	 * Application head.
	 */
	public Main() {}

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
		homePageSound.setVolume(1.00);
		homePageSound.setLoop();
		homePageSound.startPlay();
//		gameMusic.setVolume(0.45);
//		gameMusic.setLoop();
//		gameMusic.startPlay();

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

		final GameModel model = new GameModel();
		DeckView deckView = new DeckView(model);
		DiscardPileView discardPileView = new DiscardPileView(model);
		Button newGameBtn = new Button("New Game");
		Button soundBtn = new Button();
		Button undoBtn = new Button();
		newGameBtn.setMaxSize(90,30);
		newGameBtn.setMinSize(90,30);
		newGameBtn.setStyle("-fx-background-color: white; -fx-text-fill: #36469B; -fx-background-radius: 5em;");
		ImageView soundImg = new ImageView(ClassLoader.getSystemResource("icons8-sound-50.png").toString());
		ImageView muteImg = new ImageView(ClassLoader.getSystemResource("icons8-mute-50.png").toString());
		ImageView soundImgHover = new ImageView(ClassLoader.getSystemResource("icons8-sound-50-hover.png").toString());
		ImageView muteImgHover = new ImageView(ClassLoader.getSystemResource("icons8-mute-50-hover.png").toString());
		ImageView undoImg = new ImageView(ClassLoader.getSystemResource("icons8-undo-96.png").toString());
		ImageView undoImgHover = new ImageView(ClassLoader.getSystemResource("icons8-undo-96-hover.png").toString());
		soundImg.setFitWidth(30);
		soundImg.setFitHeight(30);
		muteImg.setFitWidth(30);
		muteImg.setFitHeight(30);
		soundImgHover.setFitWidth(30);
		soundImgHover.setFitHeight(30);
		muteImgHover.setFitWidth(30);
		muteImgHover.setFitHeight(30);
		undoImg.setFitWidth(30);
		undoImg.setFitHeight(30);

		undoImgHover.setFitWidth(30);
		undoImgHover.setFitHeight(30);
		soundBtn.setGraphic(soundImg);
		soundBtn.setAlignment(Pos.CENTER);
		undoBtn.setGraphic(undoImg);
		undoBtn.setAlignment(Pos.CENTER);
		GridPane.setHalignment(undoBtn, HPos.RIGHT);

		soundBtn.setStyle("-fx-background-color: transparent, transparent, transparent, transparent, transparent;" +
				"    -fx-pref-height: 30;" +
				"    -fx-pref-width: 30;" +
				"    -fx-min-height: 30;" +
				"    -fx-min-width: 30;" +
				"    -fx-max-height: 30;" +
				"    -fx-max-width: 30;");
		undoBtn.setStyle("-fx-background-color: transparent, transparent, transparent, transparent, transparent;" +
				"    -fx-pref-height: 30;" +
				"    -fx-pref-width: 30;" +
				"    -fx-min-height: 30;" +
				"    -fx-min-width: 30;" +
				"    -fx-max-height: 30;" +
				"    -fx-max-width: 30;");


		root.add(soundBtn, 0, 0);
		root.add(undoBtn, 5, 0);
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

		undoBtn.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				undoBtn.setGraphic(undoImgHover);
			}
		});

		undoBtn.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				undoBtn.setGraphic(undoImg);
			}
		});

		soundBtn.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(soundPlaying)soundBtn.setGraphic(soundImgHover);
				else soundBtn.setGraphic(muteImgHover);
			}
		});

		soundBtn.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(soundPlaying)soundBtn.setGraphic(soundImg);
				else soundBtn.setGraphic(muteImg);
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

		undoBtn.setOnAction(e ->{
			model.undoLast();
		});

		startBtn.setOnAction(e -> {
			btnSound.stopPlay();
			btnSound.startPlay();
			pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
			homePageSound.stopPlay();
			gameMusic.startPlay();
		});


		pPrimaryStage.setResizable(false);
		pPrimaryStage.setScene(new Scene(homepageLayout, WIDTH, HEIGHT));
		pPrimaryStage.show();

	}

}