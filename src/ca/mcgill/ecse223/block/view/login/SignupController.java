package ca.mcgill.ecse223.block.view.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SignupController implements Initializable {
	@FXML
	private PasswordField adminPassword; 
	
	@FXML
	private PasswordField playerPassword; 
	
	@FXML
	private TextField username; 
	
	@FXML
	private AnchorPane root; 
	
    @FXML
    private JFXButton close;
	
	@FXML
    private void backToLoginOnClick() {
		try {
			Parent newRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
        	root.getChildren().setAll(newRoot);
		} catch(Exception e) {
			Snackbar.show(root, e);
		}
    }
	
	@FXML
	private void signUpOnClick() {
		try {
			Block223Controller.register(
				username.getText().trim(),
				playerPassword.getText().trim(),
				adminPassword.getText().trim()
			);
			
			String playerType = "Admin";
			if (adminPassword.getText().trim().isEmpty()) {
				playerType = "Player";
			}
			Snackbar.show(root, playerType + " successfully created.");
			
			username.clear();
			playerPassword.clear();
			adminPassword.clear();
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
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
