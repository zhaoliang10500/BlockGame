package ca.mcgill.ecse223.block.view.playerLobby;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import ca.mcgill.ecse223.block.helper.Initialize;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class PlayerLobbyController implements Initializable {
	@FXML
	public AnchorPane root;
	
    @FXML
    private Text welcome;

    @FXML
    private ListView<String> gameList;
    
    @FXML
    private JFXButton logout;

    @FXML
    private JFXButton close;
	
	@FXML
	public void logoutOnClick() {
		LoadPage.logout(getClass(), root);
    }

    @FXML
    private void playOnClick() {
    	LoadPage.playGame(getClass(), root, gameList, true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
		MoreInfo.logoutAndClose(logout, close);
    	
		Initialize.lobby(root, welcome, gameList, true);
    }
	
	@FXML
	private void closeApp() {
		System.exit(0);
	}
}