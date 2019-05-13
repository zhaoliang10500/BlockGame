package ca.mcgill.ecse223.block.view.adminLobby;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.helper.Initialize;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class AdminLobbyController implements Initializable {	
	@FXML
	private AnchorPane root;
	
    @FXML
    private Text welcome;

	@FXML
	private JFXListView<String> gameList;
	
    @FXML
    private JFXButton logout;

    @FXML
    private JFXButton close;
    
    private void showErrMsg() {
    	Snackbar.show(root, "You have to select a game first.");
    }
	
	@FXML
	private void CreateNewGameOnClick() {
		LoadPage.load(getClass(), root, "../gameSettings/gameSettings.fxml");
	}
	
	@FXML
	private void logoutOnClick() {
		LoadPage.logout(getClass(), root);
	}

	@FXML
	private void editOnClick() {
		try {
			String gameName = gameList.getSelectionModel().getSelectedItem(); 
		    if (gameName == null) {
		    	showErrMsg();
		    } else {
				Block223Controller.selectGame(gameName);
		
				LoadPage.load(getClass(), root, "../GameSettings/gameSettings.fxml");
		    }
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}

	@FXML
	private void deleteOnClick() {		
		try {
			String gameName = gameList.getSelectionModel().getSelectedItem();
			if (gameName == null) {
				showErrMsg();
			} else {
				Block223Controller.deleteGame(gameName);
				
				refreshGameList();
			}			
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void testOnClick() {
		
		LoadPage.playGame(getClass(), root, gameList, false);
	}
	
	@FXML
	private void publishOnClick() {
		try {
			String gameName = gameList.getSelectionModel().getSelectedItem();
			if (gameName == null) {
				showErrMsg();
			} else {
				Block223Controller.selectGame(gameName);
				Block223Controller.publishAndSave();
				refreshGameList();
			}			
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		MoreInfo.logoutAndClose(logout, close);
		
		Initialize.lobby(root, welcome, gameList, false);
	}
	
	private void refreshGameList() {
		Initialize.lobby(root, welcome, gameList, false);
	}
	
	@FXML
	private void closeApp() {
		System.exit(0);
	}
}