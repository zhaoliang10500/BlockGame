package ca.mcgill.ecse223.block.view.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable {
	@FXML
	private JFXPasswordField password;
	
	@FXML
	private JFXTextField username;
	
    @FXML
    private AnchorPane root;
    
    @FXML
    private JFXButton close;
	
	@FXML
	private void loginOnClick() {
		try {
			Block223Controller.login(username.getText().trim(), password.getText().trim());
			Block223Controller.setUsername(username.getText().trim());
			
			String lobbyType = "player";
			if (Block223Controller.getUserMode().getMode() == Mode.Design) {
				lobbyType = "admin";
			}
			
			LoadPage.load(getClass(), root, "../" + lobbyType + "Lobby/" + lobbyType + "Lobby.fxml");
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void signupPageOnClick() {
		LoadPage.load(getClass(), root, "signup.fxml");
	}
	
	@FXML
	private void closeApp() {
		System.exit(0);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MoreInfo.close(close);
	}
}
