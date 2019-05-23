package ca.mcgill.ecse223.block.application;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.model.PlayedGame;

public class Block223Application extends Application {

	private static Block223 block223;
	private static UserRole currentUserRole;
	private static Game currentGame;
	private static PlayedGame pgame;
	private double xOffset, yOffset;

	public static void main(String[] args) {
		block223 = Block223Persistence.load(); 
		block223.reinitialize();
		System.out.println(block223.getUsers());
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("../view/login/login.fxml"));
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});
		
		Scene scene = new Scene(root, 620, 470);
		scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Roboto");
		scene.getStylesheets().add(
			Block223Application.class.getResource("style.css").toExternalForm()
		);
		scene.setFill(Color.TRANSPARENT);
		
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.getIcons().add(new Image("brick-wall.png"));
		primaryStage.setTitle("Block223 Breakout!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static Block223 getBlock223() {
		if (block223 == null) block223 = Block223Persistence.load();
 		return block223;
	}

	public static Block223 resetBlock223() {
		if (block223 != null) {
			block223.delete();
		}
		setCurrentGame(null);
		block223 = Block223Persistence.load();
		return block223;
	}

	public static UserRole getCurrentUserRole() {
		return currentUserRole;
	}

	public static void setCurrentUserRole(UserRole aUserRole) {
		currentUserRole = aUserRole;
	}

	public static void setCurrentGame(Game aGame) {
		currentGame = aGame;
	}

	public static Game getCurrentGame() {
		return currentGame;
	}

	public static void setCurrentPlayableGame(PlayedGame aGame) {
		pgame = aGame;
	}
	
	public static PlayedGame getCurrentPlayableGame() {
		return pgame; 
	}

	/* Jeffery custom stuff */
	private static String username;

	public static void setUsername(String uname) {
		username = uname;
	}
	
	public static String getUsername() {
		return username;
	}
}