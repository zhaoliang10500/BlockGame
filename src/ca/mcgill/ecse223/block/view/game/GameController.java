package ca.mcgill.ecse223.block.view.game;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.TOCurrentlyPlayedGame;
import ca.mcgill.ecse223.block.helper.Initialize;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.PlayedGame.PlayStatus;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223PlayModeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class GameController implements Initializable {
	
	private int pageId;
	
    @FXML
    private Pane root;
    
    @FXML
    private AnchorPane playArea;
    
    @FXML
	private JFXListView<String> hallOfFame;
    
    @FXML
	private Label currentLevel;
    
    @FXML
	private Label livesLeft;
    
    @FXML
	private Label score;
    
	@FXML
	private JFXButton quit;
	
	@FXML
	private JFXButton close;
	
	@FXML
	private JFXButton resume;
    
	private void play() {
		new Block223PlayModeView(
			root,
			currentLevel,
			livesLeft,
			score,
			playArea,
			resume
		);
	}
	
	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		try {
			//MoreInfo.quit(quit);
			MoreInfo.close(close);
			
			play();
			
			pageId = 0; 
	    	Initialize.hallOfFameList(playArea, hallOfFame, pageId); 

		} catch (Exception e) {
			e.printStackTrace();
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	void prevOnClick() {
		if (pageId < 1) return; 
		pageId = pageId - 10; 
		Initialize.hallOfFameList(playArea, hallOfFame, pageId); 
	}
	
	@FXML
	void nextOnClick() {
		pageId = pageId + 10; 
		Initialize.hallOfFameList(playArea, hallOfFame, pageId); 
	}
	
	
    @FXML
    void quitGameOnClick() {
    	try {
    		if ((Block223Controller.getCurrentPlayableGame().isPaused() || Block223Application.getCurrentPlayableGame().getPlayStatus() == PlayStatus.GameOver) && Block223Controller.getCurrentPlayableGame().getPlayername() == null) {
    			 
    			Block223Application.getCurrentPlayableGame().delete();
    			Block223Persistence.save(Block223Application.getBlock223());
    			Block223Application.setCurrentPlayableGame(null);
    			LoadPage.load(getClass(), root, "../adminLobby/adminLobby.fxml"); 
    		} else if (Block223Controller.getCurrentPlayableGame().isPaused() || Block223Application.getCurrentPlayableGame().getPlayStatus() == PlayStatus.GameOver) {
    			   	
    			if (Block223Controller.getCurrentPlayableGame().getLives() == 0) Block223Application.getCurrentPlayableGame().delete();
    			Block223Persistence.save(Block223Application.getBlock223());
    			Block223Application.setCurrentPlayableGame(null);
    			LoadPage.load(getClass(), root, "../playerLobby/playerLobby.fxml"); 
    		}
    	} catch (Exception e) {
    		Snackbar.show(root, e);
    	}
    }
    
	@FXML
	private void closeApp() {
		if (Block223Application.getCurrentPlayableGame().getPlayStatus().equals(PlayStatus.GameOver)) 
			Block223Application.getCurrentPlayableGame().delete();
		System.exit(0);
	}
	
	@FXML
	private void resumeOnClick() {
		play();
	}
}
