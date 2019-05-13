package ca.mcgill.ecse223.block.view;

import java.util.HashMap;

import javax.swing.JFrame;

import com.jfoenix.controls.JFXButton;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOCurrentBlock;
import ca.mcgill.ecse223.block.controller.TOCurrentlyPlayedGame;
import ca.mcgill.ecse223.block.controller.TOHallOfFameEntry;
import ca.mcgill.ecse223.block.model.HallOfFameEntry;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.Player;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Block223PlayModeView extends JFrame implements Block223PlayModeInterface {

	private static final long serialVersionUID = -613534727974834342L;
	private Block223PlayModeListener bp;
	private Pane root;
	private Label currentLevel;
	private Label livesLeft;
	private Label score;
	private AnchorPane playArea;
	private JFXButton resume;

	public Block223PlayModeView(
			Pane root,
			Label currentLevel,
			Label livesLeft,
			Label score,
			AnchorPane playArea,
			JFXButton resume
	) {
		bp = new Block223PlayModeListener();
		this.root = root;
		this.currentLevel = currentLevel;
		this.livesLeft = livesLeft;
		this.score = score;
		this.playArea = playArea;
		this.resume = resume;
		
		gameLoop();
	}
	
	public void gameLoop() {
		bp = new Block223PlayModeListener();
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				// in the actual game, add keyListener to the game window
				root.setOnKeyPressed(bp);
				
				root.addEventFilter(KeyEvent.KEY_PRESSED, bp);
			}
		};
		Thread t1 = new Thread(r1);
		t1.start();
		// to be on the safe side use join to start executing thread t1 before executing
		// the next thread
		try {
			t1.join();
		} catch (InterruptedException e1) {
		}

		// initiating a thread to start the game loop
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				try {
					Block223Controller.getCurrentPlayableGame();
					
					try {
						Block223Controller.startGame(Block223PlayModeView.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch(Exception e) {
					try {
						Block223Controller.testGame(Block223PlayModeView.this);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		};
		Thread t2 = new Thread(r2);
		t2.start();
	}

	@Override
	public String takeInputs() {
		if (bp == null) {
			return "";
		}
		return bp.takeInputs();
	}

	@Override
	public void refresh() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					playArea.getChildren().clear();
					
					TOCurrentlyPlayedGame toPGame = Block223Controller.getCurrentPlayableGame();
					HashMap<String, Integer> gameConstants = Block223Controller.gameConstants();
					
					// Add blocks
					for (TOCurrentBlock toBlock : toPGame.getBlocks()) {
						Color color = Color.color(
							toBlock.getRed() / 255.0,
							toBlock.getGreen() / 255.0,
							toBlock.getBlue() / 255.0
						);
						Rectangle block = new Rectangle(
							toBlock.getX(),
							toBlock.getY(),
							gameConstants.get("BLOCK_SIZE"),
							gameConstants.get("BLOCK_SIZE")
						);
						block.setFill(color);
						playArea.getChildren().add(block);
					}
					
					// Add ball
					Circle ball = new Circle(
						toPGame.getCurrentBallX(),
						toPGame.getCurrentBallY(),
						gameConstants.get("BALL_DIAMETER") / 2,
						Color.PINK
					);
					playArea.getChildren().add(ball);
					
					// Add paddle
					Rectangle paddle = new Rectangle(
						toPGame.getCurrentPaddleX(),
						gameConstants.get("PLAY_AREA_SIZE")
							- gameConstants.get("PADDLE_VERTICAL_DISTANCE")
							- gameConstants.get("BALL_DIAMETER"),
						toPGame.getCurrentPaddleLength(),
						Block223Controller.getPaddleWidth()
					);

					paddle.setFill(Color.BLUE);
					playArea.getChildren().add(paddle);
					
					// Level, Lives, Score
					currentLevel.setText(toPGame.getCurrentLevel() + "");
					livesLeft.setText(toPGame.getLives() + "");
					score.setText(toPGame.getScore() + "");
					
					// resume button
					if (toPGame.getPaused()) {
						resume.setDisable(false);
					} else {						
						resume.setDisable(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void endGame(int nrOfLives, TOHallOfFameEntry hof) {
		// PlayedGame g = Block223Application.getCurrentPlayableGame(); 
		// g.getGame().addHallOfFameEntry(new HallOfFameEntry(hof.getScore(), hof.getPlayername(), (Player)Block223Application.getCurrentUserRole(), g.getGame(), Block223Application.getBlock223())); 
		// g.delete(); 
		
	}
}