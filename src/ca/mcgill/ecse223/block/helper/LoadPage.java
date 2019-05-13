package ca.mcgill.ecse223.block.helper;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.view.Block223PlayModeView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class LoadPage {
	// old root pane, path of where to go
	public static void load(Class<?> aClass, Pane root, String path) {
		try {
		Parent newRoot = FXMLLoader.load(aClass.getResource(path));
		root.getChildren().setAll(newRoot);
		} catch (Exception e) {
			//Snackbar.show(root, e);
			e.printStackTrace();
		}
	}
	
	public static void logout(Class<?> aClass, Pane root) {
		try {
			Block223Controller.logout();
			Parent newRoot = FXMLLoader.load(aClass.getResource("../login/login.fxml"));
			root.getChildren().setAll(newRoot);
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	public static void playGame(Class<?> aClass, Pane root, ListView<String> gameList, boolean isPublished) {
		try {
			String gameName = gameList.getSelectionModel().getSelectedItem();
			if (gameName == null) {
				String verb = "test";
				if (isPublished) {
					verb = "play";
				}
				Snackbar.show(root, "First select the game you want to " + verb + ".");
			} else {
				if (isPublished) {
					String[] tup_name_id = gameName.split("@"); 
					if (tup_name_id.length < 2) Block223Controller.selectPlayableGame(tup_name_id[0], -1);
					else Block223Controller.selectPlayableGame(null, Integer.parseInt(tup_name_id[1]));
				} else {
					Block223Controller.selectGame(gameName);
				}
				
				LoadPage.load(aClass, root, "../game/game.fxml");
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			Snackbar.show(root, e.toString());
		}
	}
}
