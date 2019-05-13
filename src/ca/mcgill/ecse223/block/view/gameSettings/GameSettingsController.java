package ca.mcgill.ecse223.block.view.gameSettings;

import java.net.URL;
import java.util.ResourceBundle;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class GameSettingsController implements Initializable {
	@FXML
	private TextField name;

	@FXML
	private TextField nrOfLevels;

	@FXML
	private TextField blocksPerLevel;

	@FXML
	private TextField minLength;

	@FXML
	private TextField maxLength;

	@FXML
	private TextField minHorizontalSpeed;

	@FXML
	private TextField minVerticalSpeed;

	@FXML
	private TextField speedIncreaseFactor;

	@FXML
	private AnchorPane root;

	@FXML
	private Button logout;
	
	@FXML
	private Button close;
	
	@FXML
	private void nextOnClick() {
		boolean createGameErr = false;

		try {
			Block223Controller.getCurrentDesignableGame();
		} catch (Exception e) {
			try {
				Block223Controller.createGame(name.getText().trim());
				Block223Controller.selectGame(name.getText().trim());
			} catch (Exception e1) {
				Snackbar.show(root, e1);
				createGameErr = true;
			}
		}

		if (createGameErr) {
			try {
				Block223Controller.resetBlock223();
			} catch(Exception e) {
				Snackbar.show(root, e);
			}
		} else {
			boolean needToReset = false;
			try {
				Block223Controller.setGameDetails(
					Integer.parseInt(nrOfLevels.getText().trim()),
					Integer.parseInt(blocksPerLevel.getText().trim()),
					Integer.parseInt(minHorizontalSpeed.getText().trim()),
					Integer.parseInt(minVerticalSpeed.getText().trim()),
					Double.parseDouble(speedIncreaseFactor.getText().trim()),
					Integer.parseInt(maxLength.getText().trim()),
					Integer.parseInt(minLength.getText().trim())
				);

				
				LoadPage.load(getClass(), root, "../block/block.fxml");
			} catch(NumberFormatException e) {
				Snackbar.show(root, "All fields must be filled. Some fields only accept integers.");
				needToReset = true;
			} catch (Exception e) {
				Snackbar.show(root, e);
				needToReset = true;
			}
			
			if (needToReset) {
				try {
					Block223Controller.resetBlock223();
				} catch(Exception e) {
					Snackbar.show(root, e);
				}
			}
		}
	}

	@FXML
	private void setDefaultValuesOnClick() {
		root.requestFocus();
		
		try {
			Block223Controller.getCurrentDesignableGame();
		} catch (Exception e) {
			if (Block223Controller.gameExists("New Game")) {
				int version = 1;
				
				while (Block223Controller.gameExists("New Game " + version)) {
					version++;
				}
				
				name.setText("New Game " + version);
			} else {
				name.setText("New Game");
			}
		}
		nrOfLevels.setText("5");
		blocksPerLevel.setText("20");
		minLength.setText("20");
		maxLength.setText("80");
		minHorizontalSpeed.setText("5");
		minVerticalSpeed.setText("5");
		speedIncreaseFactor.setText("1.1");
	}

	@FXML
	private void cancelOnClick() {
		try {
			Block223Controller.resetBlock223();
			
			LoadPage.load(getClass(), root, "../adminLobby/adminLobby.fxml");
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void saveAndExitOnClick() {
		try {
			nextOnClick();
			Block223Controller.saveGame();
			LoadPage.load(getClass(), root, "../adminLobby/adminLobby.fxml");
			
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			MoreInfo.logoutAndClose(logout, close);
			
			TOGame toGame = Block223Controller.getCurrentDesignableGame();
			name.setText(toGame.getName());
			name.setDisable(true);
			nrOfLevels.setText(toGame.getNrLevels() + "");
			blocksPerLevel.setText(toGame.getNrBlocksPerLevel() + "");
			minLength.setText(toGame.getMinPaddleLength() + "");
			maxLength.setText(toGame.getMaxPaddleLength() + "");
			minHorizontalSpeed.setText(toGame.getMinBallSpeedX() + "");
			minVerticalSpeed.setText(toGame.getMinBallSpeedY() + "");
			speedIncreaseFactor.setText(toGame.getBallSpeedIncreaseFactor() + "");
		} catch (Exception e) {}
	}

	@FXML
	private void logoutOnClick() {
		LoadPage.logout(getClass(), root);
	}
	
	@FXML
	private void closeApp() {
		System.exit(0);
	}
}
