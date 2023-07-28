package com.example.domaci1;

import com.example.domaci1.objects.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements EventHandler<MouseEvent> {
	public static final double WINDOW_WIDTH  = 600;
	public static final double WINDOW_HEIGHT = 800;
	
	public static final double PLAYER_WIDTH            = 20;
	public static final double PLAYER_HEIGHT           = 80;
	public static final double PLAYER_MAX_ANGLE_OFFSET = 60;
	public static final double PLAYER_MIN_ANGLE_OFFSET = -60;

	public static final double WALL_WIDTH		= 15;
	public static final double WALL_HEIGHT		= 120;
	public static final double MODIFIER_WIDTH	= 30;
	public static final double BALL_SPEED_TRESHOLD	= 800;
	public static final double LIFEPOINT_RADIUS	= 5;
	public static final double TIME_LIMIT		= 60.;
	public static final double TIME_BONUS		= 15.;
	public static final double COIN_SPAWN_RATE	= 5;
	public static final double SPACESHIP_SPAWN_RATE	= 10;
	public static final int COIN_POINTS_BONUS	= 5;
	public static final int	MAX_LIFE_POINT		= 5;

	private static final double MS_IN_S            = 1e3;
	private static final double NS_IN_S            = 1e9;
	private static final double MAXIMUM_HOLD_IN_S  = 3;
	//public static final double MAXIMUM_BALL_SPEED = 1500;
	public static final double MAXIMUM_BALL_SPEED[] = {1200, 1400, 1800};
	private static final double BALL_RADIUS        = Main.PLAYER_WIDTH / 4;
	public static final double COIN_RADIUS        = Main.BALL_RADIUS * 2;
	private static final double BALL_DAMP_FACTOR   = 0.995;
	public static final double MIN_BALL_SPEED     = 1;

	public static final double BALL_SLOW_MODIFIER = 0.90;
	public static final double BALL_ACCELERATION_MODIFIER = 1.10;

	public static final double HOLE_RADIUS = 3 * BALL_RADIUS;

	private Group root;
	private Player player;
	private Ball ball;
	private long time;
	private Hole holes[] = new Hole[4];
	private Timer timer;
	private SpaceShip spaceShip;

	private Wall walls[] = new Wall[5];
	private Wall fences[] = new Wall[4];
	private Modifier modifiers[] = new Modifier[4];
	private boolean mousePressed = false;
	private boolean ballAnimationStarted = false;
	private boolean ballAnimationFinished = false;
	private Scale speedIndicatorScale = new Scale(1, 0);
	private Scale timerScale = new Scale(1, 0);
	private double timeRemaining;
	private Text pointsText;
	private Text startText = new Text(WINDOW_WIDTH * 7 / 8 + 10,WINDOW_HEIGHT * 7 / 8 + 45, "START");
	private int points;
	private Circle lifepoints[] = new Circle[MAX_LIFE_POINT];
	private int remainingLifepoints = MAX_LIFE_POINT;
	private int levelSize = 1;
	private int levelChosen = 0;
	private int playerChosen = 1;
	private double coinSpawnTimer = COIN_SPAWN_RATE;
	private double spaceShipSpawnTimer = SPACESHIP_SPAWN_RATE;
	private Rectangle stageSelectors[] = new Rectangle[3];
	private Circle playerSelectors[] = new Circle[3];
	private Rectangle timerBar = new Rectangle();
	private Text       timerText;
	private List<Coin> coins = new ArrayList<Coin>();
	private Portal portals[] = new Portal[2];
	private Translate spaceShipPosition;
	private static Wall lastWallHit = new Wall( 0, 0, Color.GRAY, new Translate(0,0));

	private void addPoints() {
		this.pointsText = new Text(15, 15, String.valueOf(points));
		pointsText.setFont(new Font(20));
		this.root.getChildren().add( pointsText );
	}

	private void addLifePoints() {
		for (int i = 0; i < MAX_LIFE_POINT; i++) {
			Translate lifePosition = new Translate(
					 WINDOW_WIDTH * levelSize - (2 * LIFEPOINT_RADIUS + 3 * i * LIFEPOINT_RADIUS),
					2 * LIFEPOINT_RADIUS
			);
			Circle life = new Circle(LIFEPOINT_RADIUS, Color.RED);
			life.getTransforms().add(lifePosition);
			this.root.getChildren().addAll(life);
			lifepoints[i] = life;
		}
	}

	@Override
	public void start ( Stage stage ) throws IOException {
		this.root  = new Group ( );

		levelSize = 1;
		levelChosen = 0;
		playerChosen = 1;
		if (timer != null) {
			timer.stop();
			//timer = null;
		}

		Image backgroundImage = new Image (Main.class.getClassLoader().getResourceAsStream("grass.jpg"));
		ImagePattern background = new ImagePattern( backgroundImage );
		Image fenceImage = new Image (Main.class.getClassLoader().getResourceAsStream("fence.jpg"));
		ImagePattern fencePattern = new ImagePattern( fenceImage );

		Scene sceneStart = new Scene ( this.root, Main.WINDOW_WIDTH * 2, WINDOW_HEIGHT, background );

		Image stage1Image = new Image (Main.class.getClassLoader().getResourceAsStream("Stage1.png"));
		ImagePattern stage1Fill = new ImagePattern( stage1Image );
		Image stage2Image = new Image (Main.class.getClassLoader().getResourceAsStream("Stage2.png"));
		ImagePattern stage2Fill = new ImagePattern( stage2Image );
		Image stage3Image = new Image (Main.class.getClassLoader().getResourceAsStream("Stage3.png"));
		ImagePattern stage3Fill = new ImagePattern( stage3Image );

		Rectangle stage1Choice = new Rectangle(
			WINDOW_WIDTH * 3 / 8, WINDOW_HEIGHT * 3 / 8, stage1Fill
		);
		Rectangle stage1ChoiceSelector = new Rectangle(
				WINDOW_WIDTH * 3 / 8 + 10, WINDOW_HEIGHT * 3 / 8 + 10, Color.BLACK
		);
		stage1Choice.getTransforms().add(
				new Translate(  WINDOW_WIDTH / 8, WINDOW_HEIGHT / 4)
		);
		stage1ChoiceSelector.getTransforms().add(
				new Translate(  WINDOW_WIDTH / 8 - 5, WINDOW_HEIGHT / 4 - 5)
		);
		root.getChildren().addAll(
				stage1ChoiceSelector,
				stage1Choice
		);

		Rectangle stage2Choice = new Rectangle(
				WINDOW_WIDTH * 3 / 8, WINDOW_HEIGHT * 3 / 8, stage2Fill
		);
		Rectangle stage2ChoiceSelector = new Rectangle(
				WINDOW_WIDTH * 3 / 8 + 10, WINDOW_HEIGHT * 3 / 8 + 10, Color.BLACK
		);
		stage2Choice.getTransforms().add(
				new Translate(  WINDOW_WIDTH * 5 / 8, WINDOW_HEIGHT / 4)
		);
		stage2ChoiceSelector.getTransforms().add(
				new Translate(  WINDOW_WIDTH * 5 / 8 - 5, WINDOW_HEIGHT / 4 - 5)
		);
		root.getChildren().addAll(
				stage2ChoiceSelector,
				stage2Choice
		);

		Rectangle stage3Choice = new Rectangle(
				WINDOW_WIDTH * 6 / 8, WINDOW_HEIGHT * 3 / 8, stage3Fill
		);
		Rectangle stage3ChoiceSelector = new Rectangle(
				WINDOW_WIDTH * 6 / 8 + 10, WINDOW_HEIGHT * 3 / 8 + 10, Color.BLACK
		);
		stage3Choice.getTransforms().add(
				new Translate(  WINDOW_WIDTH * 9 / 8, WINDOW_HEIGHT / 4)
		);
		stage3ChoiceSelector.getTransforms().add(
				new Translate(  WINDOW_WIDTH * 9 / 8 - 5, WINDOW_HEIGHT / 4 - 5)
		);
		root.getChildren().addAll(
				stage3ChoiceSelector,
				stage3Choice
		);

		Rectangle start = new Rectangle(
				WINDOW_WIDTH * 2 / 8, WINDOW_WIDTH * 1 / 8, fencePattern
		);
		Rectangle startSelector = new Rectangle(
				WINDOW_WIDTH * 2 / 8 + 10, WINDOW_WIDTH * 1 / 8 + 10, Color.BLACK
		);
		start.getTransforms().add(
				new Translate(  WINDOW_WIDTH * 7 / 8, WINDOW_HEIGHT * 7 / 8 - 5)
		);
		startSelector.getTransforms().add(
				new Translate(  WINDOW_WIDTH * 7 / 8 - 5, WINDOW_HEIGHT * 7 / 8 - 10)
		);
		root.getChildren().addAll(
				startSelector,
				start
		);

		Translate playerPosition1 = new Translate (
				Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
				Main.WINDOW_HEIGHT * 7 / 10
		);
		Player player1 = new Player (
				Main.PLAYER_WIDTH,
				Main.PLAYER_HEIGHT,
				playerPosition1,
				1
		);
		Translate playerPosition2 = new Translate (
				Main.WINDOW_WIDTH - Main.PLAYER_WIDTH / 2,
				Main.WINDOW_HEIGHT * 7 / 10
		);
		Player player2 = new Player (
				Main.PLAYER_WIDTH,
				Main.PLAYER_HEIGHT,
				playerPosition2,
				2
		);
		Translate playerPosition3 = new Translate (
				Main.WINDOW_WIDTH * 3 / 2 - Main.PLAYER_WIDTH / 2,
				Main.WINDOW_HEIGHT * 7 / 10 - Main.PLAYER_HEIGHT / 3
		);
		Player player3 = new Player (
				Main.PLAYER_WIDTH,
				Main.PLAYER_HEIGHT,
				playerPosition3,
				3
		);
		Rectangle playerChoice = new Rectangle(
				WINDOW_WIDTH * 14 / 8, WINDOW_HEIGHT * 3 / 16, fencePattern
		);
		playerChoice.getTransforms().add(
				new Translate( WINDOW_WIDTH * 1 / 8, WINDOW_HEIGHT * 13 / 20)
		);

		Circle player1Select = new Circle(Main.PLAYER_WIDTH / 2, Color.YELLOW);
		Circle player2Select = new Circle(Main.PLAYER_WIDTH / 2, Color.BLACK);
		Circle player3Select = new Circle(Main.PLAYER_WIDTH / 2, Color.BLACK);

		player1Select.getTransforms().add(
				new Translate(Main.WINDOW_WIDTH / 2 ,
						Main.WINDOW_HEIGHT * 8 / 10 + Main.PLAYER_WIDTH));
		player2Select.getTransforms().add(
				new Translate(Main.WINDOW_WIDTH,
						Main.WINDOW_HEIGHT * 8 / 10 + Main.PLAYER_WIDTH));
		player3Select.getTransforms().add(
				new Translate(Main.WINDOW_WIDTH * 3 / 2,
						Main.WINDOW_HEIGHT * 8 / 10 + Main.PLAYER_WIDTH));

		root.getChildren ( ).addAll ( playerChoice, player1, player2, player3, player1Select, player2Select,
				player3Select );

		Text text2 = new Text(WINDOW_WIDTH * 3 / 4 + 60 + 5, 120 + 5, "GOLFER");
		text2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
		text2.setFill(Color.BLACK);
		this.root.getChildren().add( text2 );
		Text text = new Text(WINDOW_WIDTH * 3 / 4 + 60, 120, "GOLFER");
		text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
		text.setFill(fencePattern);
		this.root.getChildren().add( text );
		startText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
		startText.setFill(Color.BLACK);
		startText.addEventHandler (
				MouseEvent.MOUSE_CLICKED,
				mouseEvent -> startGame( stage, 0, true)
		);
		this.root.getChildren().add( startText );

		this.addStageHandlers(stage1Choice, stage1ChoiceSelector, stage, 1, false);
		this.addStageHandlers(stage2Choice, stage2ChoiceSelector, stage, 2, false);
		this.addStageHandlers(stage3Choice, stage3ChoiceSelector, stage, 3, false);
		this.addStageHandlers(start, startSelector, stage, 0, true);

		this.stageSelectors[0] = stage1ChoiceSelector;
		this.stageSelectors[1] = stage2ChoiceSelector;
		this.stageSelectors[2] = stage3ChoiceSelector;

		this.addPlayerHandlers(player1, 1);
		this.addPlayerHandlers(player2, 2);
		this.addPlayerHandlers(player3, 3);

		this.playerSelectors[0] = player1Select;
		this.playerSelectors[1] = player2Select;
		this.playerSelectors[2] = player3Select;

		MapPrepare.addFence(fences, root, WINDOW_WIDTH, WINDOW_HEIGHT, 2);

		stage.setTitle ( "Golfer" );
		stage.setResizable ( false );
		stage.setScene ( sceneStart );
		stage.show ( );
	}

	private void addPlayerHandlers( Player player, int playerNumber){
		player.addEventHandler (
				MouseEvent.MOUSE_CLICKED,
				mouseEvent -> choosePlayer( playerNumber)
		);
	}

	private void choosePlayer( int playerNumber){
		this.playerChosen = playerNumber;
		for (int i = 0; i < playerSelectors.length; i++) {
			playerSelectors[i].setFill(Color.BLACK);
		}
		playerSelectors[playerNumber - 1].setFill( Color.YELLOW );
	}

	private void addStageHandlers(Rectangle stageChoice, Rectangle stageChoiceSelector, Stage stage, int level, boolean startButton) {
		stageChoice.addEventHandler (
				MouseEvent.MOUSE_CLICKED,
				mouseEvent -> startGame( stage, level, startButton)
		);
		stageChoice.addEventHandler (
				MouseEvent.MOUSE_ENTERED,
				mouseEvent -> changeSelectorColor( stageChoiceSelector, Color.GRAY)
		);
		stageChoice.addEventHandler (
				MouseEvent.MOUSE_EXITED,
				mouseEvent -> changeSelectorColor( stageChoiceSelector, Color.BLACK )
		);
	}

	private void changeSelectorColor (Rectangle selector, Color color){
		if (levelChosen > 0){
			if (!selector.equals(stageSelectors[levelChosen - 1]))
				selector.setFill(color);
		} else {
			selector.setFill(color);
		}
	}

	private void startGame(Stage stage, int level, boolean startButton) {
		this.root  = new Group ( );

		if (portals[0] != null) {
			root.getChildren().removeAll(portals[0], portals[1]);
			portals[0] = null;
			portals[1] = null;
		}

		remainingLifepoints = MAX_LIFE_POINT;
		points = 0;
		ball = null;
		timeRemaining = TIME_LIMIT;
		spaceShipSpawnTimer = SPACESHIP_SPAWN_RATE;
		coinSpawnTimer = COIN_SPAWN_RATE;
		speedIndicatorScale = new Scale(1, 0);

		if (timer == null) {
			addTimer();
		}

		if ( !startButton ) {
			this.levelChosen = level;
			for (int i = 0; i < this.stageSelectors.length; i++) {
				stageSelectors[i].setFill(Color.BLACK);
			}
			stageSelectors[level - 1].setFill(Color.YELLOW);
			startText.setFill(Color.YELLOW);
			return;
		}
		if (this.levelChosen == 0 ) return;

		level = levelChosen;

		levelSize = level == 3 ? 2 : 1;

		Image backgroundImage = new Image (Main.class.getClassLoader().getResourceAsStream("grass.jpg"));
		ImagePattern background = new ImagePattern( backgroundImage );

		Scene scene = new Scene ( this.root, Main.WINDOW_WIDTH * levelSize,
				WINDOW_HEIGHT, background );

		Translate playerPosition = new Translate (
				Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
				Main.WINDOW_HEIGHT - Main.PLAYER_HEIGHT
		);

		//this.spaceShipPosition = MapPrepare.generateSpaceShipPosition(WINDOW_WIDTH * levelSize, WINDOW_HEIGHT);

		MapPrepare.addFourHoles (holes, root, level);
		MapPrepare.addFence(fences, root, WINDOW_WIDTH, WINDOW_HEIGHT, levelSize);
		MapPrepare.addIndicator(speedIndicatorScale, root);
		MapPrepare.addSpeedModifiers(modifiers, root, level);
		MapPrepare.addWalls(walls, root, level);
		//this.spaceShip = MapPrepare.addSpaceShip(spaceShipPosition, WINDOW_WIDTH * levelSize, root);
		if (level == 3) MapPrepare.addPortals(portals, root);
		this.addTimerScale(timerScale, root, levelSize);
		this.addPoints();
		this.addLifePoints();

		this.player = new Player (
				Main.PLAYER_WIDTH,
				Main.PLAYER_HEIGHT,
				playerPosition,
				playerChosen
		);

		this.root.getChildren ( ).addAll ( this.player );

		scene.addEventHandler (
				MouseEvent.MOUSE_MOVED,
				mouseEvent -> this.player.handleMouseMoved (
						mouseEvent,
						Main.PLAYER_MIN_ANGLE_OFFSET,
						Main.PLAYER_MAX_ANGLE_OFFSET
				)
		);

		scene.addEventHandler (
				KeyEvent.KEY_PRESSED,
				keyEvent -> removeBall( keyEvent )
		);

		scene.addEventHandler ( MouseEvent.ANY, this );

		timer.start ( );

		scene.addEventHandler (
				KeyEvent.KEY_PRESSED,
				keyEvent -> {
					try {
						if (keyEvent.getCode().equals(KeyCode.ESCAPE))
							start(stage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		);

		scene.setCursor ( Cursor.NONE );
		stage.setScene ( scene );
		stage.show ( );
	}

	private void addTimer(){
		timer = new Timer (
				deltaNanoseconds -> {
					double deltaSeconds = ( double ) deltaNanoseconds / Main.NS_IN_S;
					updateTimerBar(deltaSeconds);
					updateCoinTimer(deltaSeconds);
					updateSpaceShipTimer(deltaSeconds);
					if ( this.ball != null && !ballAnimationStarted && timeRemaining > 0) {

						for (Wall w: walls) {
							if ( w.handleCollision( ball, lastWallHit ) ) {
								lastWallHit = w;
							}
						}
						for (Modifier m: modifiers) {
							m.handleCollision( ball );
						}
						for (int i = 0; i < coins.size(); i++) {
							if (coins.get(i).handleCollision( ball )) {
								useCoin(coins.get(i).getType());
								root.getChildren().remove(coins.get(i));
								coins.remove(i);
								break;
							}
						}
						for (int i = 0; i < 2; i++){
							if (portals[i] != null){
								if (portals[i].handleCollision(ball)) {
									Portal.teleportBall(ball, portals[i], portals[(i + 1) % 2]);
									break;
								}
							}
						}

						boolean stopped = this.ball.update (
								deltaSeconds,
								0 + PLAYER_WIDTH,
								Main.WINDOW_WIDTH * levelSize - PLAYER_WIDTH,
								0 + PLAYER_WIDTH,
								Main.WINDOW_HEIGHT - PLAYER_WIDTH,
								Main.BALL_DAMP_FACTOR,
								Main.MIN_BALL_SPEED
						);

						if ( stopped || ballAnimationFinished ) {
							ballAnimationFinished = false;
							this.root.getChildren ( ).remove ( this.ball );
							this.ball = null;
							speedIndicatorScale.setY(0);
						}

						boolean isInHole = false;

						//if (this.ball != null)
						//	isInHole = Arrays.stream ( this.holes ).anyMatch ( hole -> hole.handleCollision ( this.ball ) );

						if (this.ball != null && this.ball.getSpeed() < BALL_SPEED_TRESHOLD)
							for (Hole h: holes) {
								if (h.handleCollision(this.ball)) {
									isInHole = true;
									points += h.getPoints();
									pointsText.setText(String.valueOf(points));
								}
							}

						if (isInHole) {
							playHoleAnimation();
						}
					}

					if (spaceShip != null && timeRemaining > 0) {
						spaceShip.update(deltaSeconds);
						if (ball != null) {
							if ( spaceShip.handleCollision(ball) ){
								ballAnimationFinished = false;
								this.root.getChildren ( ).remove ( this.ball );
								this.ball = null;
								speedIndicatorScale.setY(0);
							}
						}
					}

					if (mousePressed && this.ball == null && !ballAnimationStarted) {
						double value = ( System.currentTimeMillis ( ) - this.time ) / Main.MS_IN_S;
						double mouseDeltaSeconds = Utilities.clamp ( value, 0, Main.MAXIMUM_HOLD_IN_S );
						speedIndicatorScale.setY( speedIndicatorScale.getX() * mouseDeltaSeconds / Main.MAXIMUM_HOLD_IN_S);
					}
				}
		);
	}

	private void updateCoinTimer( double deltaSeconds){
		if (timeRemaining > 0){
			this.coinSpawnTimer -= deltaSeconds;
			if (coinSpawnTimer <= 0) {
				coinSpawnTimer = COIN_SPAWN_RATE;
				int coinType = (int) Math.floor(Math.random() * 3 + 1);
				addCoin(coinType, root);
			}
		}
	}

	private void updateSpaceShipTimer( double deltaSeconds){
		if (timeRemaining > 0){
			this.spaceShipSpawnTimer -= deltaSeconds;
			if (spaceShipSpawnTimer <= 0) {
				spaceShipSpawnTimer = SPACESHIP_SPAWN_RATE;
				root.getChildren().remove(spaceShip);
				this.spaceShipPosition = MapPrepare.generateSpaceShipPosition(WINDOW_WIDTH * levelSize, WINDOW_HEIGHT);
				this.spaceShip = MapPrepare.addSpaceShip(spaceShipPosition, WINDOW_WIDTH * levelSize, root);
			}
		}
	}

	private void addCoin(int type, Group root){
		double x = 0;
		double y = 0;
		Translate translate = new Translate(x, y);
		Coin coin = new Coin(0, translate, 1);
		boolean valid = false;

		while (!valid) {
			valid = true;
			x = Math.random() * (WINDOW_WIDTH * levelSize - PLAYER_WIDTH * 4) + PLAYER_WIDTH * 2;
			y = Math.random() * (WINDOW_HEIGHT - PLAYER_WIDTH * 4) + PLAYER_WIDTH * 2;

			translate = new Translate(x, y);
			coin = new Coin(Main.COIN_RADIUS, translate, type);

			if (MapPrepare.playerCircleIntersect(coin)){
				valid = false;
			}
			for (Wall w: walls) {
				if (w.circleIntersect(coin)){
					valid = false;
				}
			}
			for (Modifier m: modifiers) {
				if (m.circleIntersect(coin)){
					valid = false;
				}
			}
			for (Hole h: holes) {
				if (h.handleCollision(coin)){
					valid = false;
				}
			}
			if (levelChosen == 3){
				for (Portal p : portals) {
					if (p.handleCollision(coin)) {
						valid = false;
					}
				}
			}
			if (coins.size() > 0) {
				for (Coin c: coins) {
					if (c.handleCollision(coin)){
						valid = false;
					}
				}
			}
			/*if (ball != null){
				if (ball.intersects(x, y, COIN_RADIUS, COIN_RADIUS)) {
					valid = false;
				}
			}*/
		}

		coins.add( coin );
		root.getChildren().add( coin );
	}

	private void useCoin (int type) {
		switch (type) {
			case 1:
				if ( remainingLifepoints < MAX_LIFE_POINT ){
					Translate lifePosition = new Translate(
							WINDOW_WIDTH * levelSize - (2 * LIFEPOINT_RADIUS + 3 * remainingLifepoints * LIFEPOINT_RADIUS),
							2 * LIFEPOINT_RADIUS
					);
					Circle life = new Circle(LIFEPOINT_RADIUS, Color.RED);
					life.getTransforms().add(lifePosition);
					this.root.getChildren().addAll(life);
					lifepoints[remainingLifepoints++] = life;
				}
				break;
			case 2:
				timeRemaining = Utilities.clamp(timeRemaining + TIME_BONUS, 0, TIME_LIMIT);
				updateTimerBar(0);
				break;
			case 3:
				points += COIN_POINTS_BONUS;
				pointsText.setText(String.valueOf(points));
				break;
		}
	}

	private void addTimerScale(Scale scale, Group root, int levelSize) {
		timerBar = new Rectangle(Main.PLAYER_WIDTH / 2, Main.WINDOW_HEIGHT - Main.PLAYER_WIDTH * 2, Color.RED);
		root.getChildren().add(timerBar);

		timerBar.getTransforms().addAll(
				new Translate(Main.WINDOW_WIDTH * levelSize , Main.WINDOW_HEIGHT - Main.PLAYER_WIDTH),
				new Rotate(180),
				scale
		);

		timerText = new Text(15, 15, "Time remaining: " + (int) timeRemaining);
		timerText.setFont(new Font(20));
		root.getChildren().add( timerText );
		timerText.getTransforms().add(
				new Translate( Main.WINDOW_WIDTH * levelSize - Main.WINDOW_WIDTH / 3,
						Main.WINDOW_HEIGHT - Main.PLAYER_WIDTH)
		);
	}

	private void updateTimerBar(double deltaSeconds) {
		if (timeRemaining > 0){
			timeRemaining -= deltaSeconds;
			timerScale.setY(timeRemaining / TIME_LIMIT);
			timerText.setText( "Time remaining: " + (int) (timeRemaining + 1));
		}
	}

	private void removeBall(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.SPACE) && ball != null && !ballAnimationStarted) {
			this.root.getChildren().remove(this.ball);
			this.ball = null;
			speedIndicatorScale.setY(0);
		}
	}

	public void playHoleAnimation() {
		ballAnimationStarted = true;
		Scale scale = ball.getScale();
		Timeline scaling = new Timeline(
				new KeyFrame(Duration.seconds(0),
						new KeyValue(scale.xProperty(), 1, Interpolator.LINEAR),
						new KeyValue(scale.yProperty(), 1, Interpolator.LINEAR)),
				new KeyFrame(Duration.seconds(2),
						new KeyValue(scale.xProperty(), 0.1, Interpolator.LINEAR),
						new KeyValue(scale.yProperty(), 0.1, Interpolator.LINEAR))
		);
		scaling.setCycleCount(1);
		scaling.setAutoReverse(false);
		scaling.setOnFinished( e -> {
			ballAnimationStarted = false;
			ballAnimationFinished = true;
		});
		scaling.play();
	}

	public static void main ( String[] args ) {
		launch ( );
	}

	@Override public void handle ( MouseEvent mouseEvent ) {
		if ( mouseEvent.getEventType ( ).equals ( MouseEvent.MOUSE_PRESSED ) && mouseEvent.isPrimaryButtonDown ( )
				&& remainingLifepoints > 0 && timeRemaining > 0) {
			this.time = System.currentTimeMillis ( );
			this.mousePressed = true;
			//if (ball != null) System.out.println( "Ball speed is: " + this.ball.getSpeed());
		} else if ( mouseEvent.getEventType ( ).equals ( MouseEvent.MOUSE_RELEASED ) ) {
			if ( this.time != - 1 && this.ball == null && !ballAnimationStarted && timeRemaining > 0) {
				double value        = ( System.currentTimeMillis ( ) - this.time ) / Main.MS_IN_S;
				double deltaSeconds = Utilities.clamp ( value, 0, Main.MAXIMUM_HOLD_IN_S );

				double ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * Main.MAXIMUM_BALL_SPEED[playerChosen - 1];

				this.root.getChildren().remove(lifepoints [--remainingLifepoints]);
				lifepoints [remainingLifepoints] = null;
				mousePressed = false;

				Translate ballPosition = this.player.getBallPosition ( );
				Point2D   ballSpeed    = this.player.getSpeed ( ).multiply ( ballSpeedFactor );

				this.ball = new Ball ( Main.BALL_RADIUS, ballPosition, ballSpeed );
				this.root.getChildren ( ).addAll ( this.ball );
			}
			this.time = -1;
		}
	}

	public static void changeLastWallHit() {
		lastWallHit = new Wall( 0, 0, Color.GRAY, new Translate(0,0));
	}

}