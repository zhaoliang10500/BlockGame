package ca.mcgill.ecse223.block.view.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BlockController implements Initializable {
	@FXML
	private JFXListView<Integer> blockList;
	
	@FXML
	private JFXTextField pts;
	
	@FXML
	private JFXColorPicker colorPicker;
	
	@FXML
	private JFXButton deleteBlock;
	
	@FXML
	private AnchorPane root;
	
	@FXML
	private JFXButton logout;
	
	@FXML
	private JFXButton close;
	
	@FXML
	private JFXButton saveAndExit;
	
	List<Integer> pickedInts = new ArrayList<Integer>();
	
	@FXML
	private void createBlockOnClick() {
		try {
			int[][] array = {
				{26, 188, 156},
				{46, 204, 113},
				{52, 152, 219},
				{155, 89, 182},
				{52, 73, 94},
				{22, 160, 133},
				{39, 174, 96} ,
				{41, 128, 185},
				{142, 68, 173},
				{44, 62, 80},
				{241, 196, 15},
				{230, 126, 34},
				{231, 76, 60},
				{236, 240, 241},
				{149, 165, 166},
				{243, 156, 18},
				{211, 84, 0},
				{192, 57, 43},
				{189, 195, 199},
				{127, 140, 141},
			};
			
			Random rand = new Random();
			int i = rand.nextInt(20);
			while (pickedInts.contains(i) && pickedInts.size() < array.length) {
				i = rand.nextInt(20);
			}
			
			if (pickedInts.size() >= array.length) {
				Block223Controller.addBlock(
					rand.nextInt(256),
					rand.nextInt(256),
					rand.nextInt(256),
					10
				);
			} else {
				pickedInts.add(i);				
				Block223Controller.addBlock(array[i][0], array[i][1], array[i][2], 10);
			}			
			
			refreshBlockList();
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	private int format(double num) {
		return (int) Math.round(num * 255);
	}
	
	private void updateBlock() {
		try {
			int id = blockList.getSelectionModel().getSelectedItem();
			Color color = colorPicker.getValue();
			
			Block223Controller.updateBlock(
				id,
				format(color.getRed()),
				format(color.getGreen()),
				format(color.getBlue()),
				Integer.parseInt(pts.getText())
			);
		} catch (InvalidInputException e) {
			Snackbar.show(root, e);
		} catch (Exception e) {
			Snackbar.show(root, "Points has to be an integer.");
		}
	}
	
	@FXML
	private void updatePointsOnClick() {
		updateBlock();
	}
	
	@FXML
	private void colorPickerOnClick() {
		updateBlock();
	}
	
	@FXML
	private void deleteBlockOnClick() {
		Integer id = blockList.getSelectionModel().getSelectedItem();
		if (id != null) {
			try {
				Block223Controller.deleteBlock(id);
				refreshBlockList();
			} catch (Exception e) {
				Snackbar.show(root, e);
			}
		}
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
    void backOnClick() {
    	LoadPage.load(getClass(), root, "../gameSettings/gameSettings.fxml");
    }
	
	@FXML
	private void nextOnClick() {
		try {
			if (Block223Controller.getBlocksOfCurrentDesignableGame().size() != 0) {
				LoadPage.load(getClass(), root, "../grid/grid.fxml");
			} else {
				Snackbar.show(root, "You need to have at least one block to progress.");	
			}
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}

	@FXML
	private void blockListOnMouseClicked() {
		try {
			Integer id = blockList.getSelectionModel().getSelectedItem();
			blockListItemOnMouseClicked(id);
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	private void blockListItemOnMouseClicked(Integer id) throws InvalidInputException {
		if (id != null) {
			TOBlock toBlock = Block223Controller.getBlockOfCurrentDesignableGame(id);
			
			colorPicker.setValue(Color.rgb(toBlock.getRed(), toBlock.getGreen(), toBlock.getBlue()));
			pts.setText(toBlock.getPoints() + "");
		}
	}
	
	private void refreshBlockList() throws InvalidInputException {
		List<Integer> tmpBlockList = Block223Controller.getBlocksOfCurrentDesignableGame()
			.stream()
			.map(toBlock -> toBlock.getId())
			.collect(Collectors.toList());
		
		blockList.getItems().clear();
		Collections.reverse(tmpBlockList);
		blockList.setItems(FXCollections.observableArrayList(tmpBlockList));
		
		blockList.getSelectionModel().select(0);
		Integer id = blockList.getSelectionModel().getSelectedItem();
		blockListItemOnMouseClicked(id);
		
		if (tmpBlockList.isEmpty()) {
			colorPicker.setValue(Color.WHITE);
			pts.clear();
			
			colorPicker.setDisable(true);
			pts.setDisable(true);
			deleteBlock.setDisable(true);
		} else {
			colorPicker.setDisable(false);
			pts.setDisable(false);
			deleteBlock.setDisable(false);
			
			blockList.setCellFactory(param -> new ListCell<Integer>() {
				@Override
				public void updateItem(Integer id, boolean empty) {
					super.updateItem(id, empty);
					
					if (empty) {
						// thank you so much for the advice!
						// https://lippolweblog.wordpress.com/2015/08/12/javafx-avoid-ghost-duplicated-elements-in-listview/
						setBackground(null);
					} else {
						try {
							TOBlock toBlock = Block223Controller.getBlockOfCurrentDesignableGame(id);
							Color blockColor = Color.color(
									toBlock.getRed() / 255.0,
									toBlock.getGreen() / 255.0,
									toBlock.getBlue() / 255.0
									);
								setBackground(new Background (new BackgroundFill(
										blockColor, CornerRadii.EMPTY, Insets.EMPTY
										)));
						} catch (Exception e) {
							Snackbar.show(root, e);
						}
					}
				}	      
			});
		}
		
	}
	
	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		try {
			MoreInfo.saveLogoutClose(saveAndExit, logout, close);
			
			refreshBlockList();
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void logoutOnClick() {
		LoadPage.logout(getClass(), root);
	}
	
	@FXML
	private void closeApp() {
		System.exit(0);
	}
	
	@FXML
	private void saveAndExitOnClick() {
		nextOnClick();
		LoadPage.load(getClass(), root, "../adminLobby/adminLobby.fxml");
	}
}
