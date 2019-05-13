package ca.mcgill.ecse223.block.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.TOHallOfFame;
import ca.mcgill.ecse223.block.controller.TOHallOfFameEntry;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Initialize {
	public static void lobby(Pane root, Text textNode, ListView<String> gameList, boolean isPublished) {
		Welcome.greet(textNode);
		gameList(root, gameList, isPublished);
			
			// https://stackoverflow.com/questions/33592308/javafx-how-to-put-imageview-inside-listview
	        /*
			gameList.setCellFactory(param -> new ListCell<String>() {
	            private ImageView imageView = new ImageView();
	            
	            @Override
	            public void updateItem(String name, boolean empty) {
	                super.updateItem(name, empty);
	                if (empty) {
	                    setText(null);
	                    setGraphic(null);
	                } else {
	                	try {
	                    if (false) {
	                        imageView.setImage(FLAG_ICON);
	                    	imageView.setFitHeight(25);
	                    	imageView.setPreserveRatio(true);
	                    }
	                    setText(name);
	                    setGraphic(imageView);
	                	} catch (Exception e) {
	                		Snackbar.show(root, e);
	                	}
	                }
	            }	      
	        });
	        */
	}
	
	public static void gameList(Pane root, ListView<String> gameList, boolean isPublished) {
		try {
		List<String> tmpGameList = null;
		if (isPublished) {
			tmpGameList = Block223Controller.getPlayableGames()
				.stream()
				.map(toGame -> toGame.getName() + "@" + (toGame.getNumber()==-1?"":new Integer(toGame.getNumber()).toString()))
				.collect(Collectors.toList());
		} else {
			tmpGameList = Block223Controller.getDesignableGames()
				.stream()
				.map(toGame -> toGame.getName())
				.collect(Collectors.toList());
		}
		
		gameList.setItems(FXCollections.observableList(tmpGameList));
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	public static void hallOfFameList(Pane root, ListView<String> hallOfFameList, int start) {
		
		try {
		String err = ""; 
		List<String> finalList = new ArrayList<String>(); 
		finalList.add("Hall of Fame"); 
		// finalList.add("Debbie   88"); 
		// finalList.add("Lilly    45"); 
		TOHallOfFame tmpFameList = null;
		try {
			tmpFameList = Block223Controller.getHallOfFame(start, start + 10); 
		} catch (Exception e) {
			err = e.getMessage();
			Snackbar.show(root, err);
		}
		if (tmpFameList == null) {
			hallOfFameList.setItems(FXCollections.observableList(finalList));
			return; 
		}
		for (int i = 0; i < tmpFameList.numberOfEntries(); i++) {
			finalList.add(tmpFameList.getEntry(i).getPlayername() + "   " + tmpFameList.getEntry(i).getScore()); 
		}
		hallOfFameList.setItems(FXCollections.observableList(finalList));
		
		Snackbar.show(root, err);
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
}
