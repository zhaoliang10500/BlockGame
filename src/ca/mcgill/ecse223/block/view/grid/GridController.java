package ca.mcgill.ecse223.block.view.grid;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import ca.mcgill.ecse223.block.controller.TOGridCell;
import ca.mcgill.ecse223.block.helper.LoadPage;
import ca.mcgill.ecse223.block.helper.MoreInfo;
import ca.mcgill.ecse223.block.helper.Snackbar;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridController implements Initializable {
    @FXML
    private Label number;

    @FXML
    private AnchorPane root;

    @FXML
    private GridPane grid;
    
    @FXML
    private JFXListView<Integer> blockList;
    
	@FXML
	private JFXButton logout;
	
	@FXML
	private JFXButton close;
    
    private int level = 1;
    private int numCols = Block223Controller.maxHorizontalGridPositionForABlock();
    private int numRows = Block223Controller.maxVerticalGridPositionForABlock();
	
    private void changeLevel(int levelLimit, int changeBy) {
    	try {
			if (level == levelLimit) {
				Snackbar.show(
					root, 
					"The number of levels must be between 1 and "
						+ levelLimit
						+ "."
				);
			} else {
				level += changeBy;
				number.setText(level + "");
				
				refreshGrid();
			}
    	} catch(Exception e) {
    		Snackbar.show(root, e);
    	}
    }
    
	@FXML
	private void prevLevelOnClick() {
		changeLevel(1, -1);
	}
	
	@FXML
	private void nextLevelOnClick() {
		try {
			changeLevel(Block223Controller.getCurrentDesignableGame().getNrLevels(), 1);
		} catch (Exception e) {
	   		Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void emptyOnDragDropped(DragEvent e) {
    	try {
	    	String str = e.getDragboard().getString();
	    	int row = Integer.parseInt(str.substring(0, str.indexOf(",")));
	    	int col = Integer.parseInt(str.substring(str.indexOf(",") + 1, str.length()));  
    		
    		Block223Controller.removeBlock(level, row, col);
    		
    		refreshGrid();
    	} catch (Exception err) {
    		Snackbar.show(root, err);
    	}
	}
	
	@FXML
	private void emptyOnDragOver(DragEvent e) {
		handleDragOver(e);
	}
	
	@FXML
	private void cancelOnClick() {
		try {
			Block223Controller.resetBlock223();
			
			Parent newRoot = FXMLLoader.load(getClass().getResource("../adminLobby/adminLobby.fxml"));
			root.getChildren().setAll(newRoot);
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void backOnClick() {
		try {			
			Parent newRoot = FXMLLoader.load(getClass().getResource("../block/block.fxml"));
			root.getChildren().setAll(newRoot);
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@FXML
	private void saveOnClick() {
		try {
			Block223Controller.saveGame();
			
			Parent newRoot = FXMLLoader.load(getClass().getResource("../adminLobby/adminLobby.fxml"));
			root.getChildren().setAll(newRoot);
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MoreInfo.logoutAndClose(logout, close);
		
		// Set up grid
		for (int i = 0; i < numRows; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setVgrow(Priority.SOMETIMES);
			grid.getRowConstraints().add(rowConstraints);
		}
		
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(colConstraints);
        }
        
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                addRectToGrid(i, j);
            }
        }
		
        // Set up list of blocks
		try {
			List<Integer> tmpBlockList = Block223Controller.getBlocksOfCurrentDesignableGame()
					.stream()
					.map(toBlock -> toBlock.getId())
					.collect(Collectors.toList());
				
			Collections.reverse(tmpBlockList);
			blockList.getItems().addAll(tmpBlockList);
			
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
			
			refreshGrid();
		} catch (Exception e) {
			Snackbar.show(root, e);
		}
	}
	
	private void handleDragOver(DragEvent e) {
    	if (e.getDragboard().hasString()) {
    		e.acceptTransferModes(TransferMode.ANY);
    	}
	}
	
    private void addRectToGrid(int rowIndex, int colIndex) {
    	int row = rowIndex + 1;
    	int col = colIndex + 1;
        Rectangle rect = new Rectangle(20, 20, Color.TRANSPARENT);
        
        rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	try {
            		Integer blockId = blockList.getSelectionModel().getSelectedItem();
            		
            		Block223Controller.positionBlock(blockId, level, row, col);
            		refreshGrid();
            	} catch(InvalidInputException e) {
            		Snackbar.show(root, e);
            	} catch(Exception e) {
            		Snackbar.show(root, "First select a block on the right.");
            	}
            }
        });
        
	    rect.setOnDragDetected(e -> {
	    	Dragboard db = rect.startDragAndDrop(TransferMode.ANY);
	    	ClipboardContent cb = new ClipboardContent();
	    	
	    	cb.putString(row + "," + col);
	    	db.setContent(cb);
	    	e.consume();
	    });
	    rect.setOnDragOver(e -> handleDragOver(e));
	    rect.setOnDragDropped(e -> {
	    	try {
		    	String str = e.getDragboard().getString();
		    	int oldRow = Integer.parseInt(str.substring(0, str.indexOf(",")));
		    	int oldCol = Integer.parseInt(str.substring(str.indexOf(",") + 1, str.length()));  
	    		
	    		Block223Controller.moveBlock(level, oldRow, oldCol, row, col);
	    		
	    		refreshGrid();
	    	} catch (Exception err) {
	    		Snackbar.show(root, err);
	    	}
	    });
        
        grid.add(rect, rowIndex, colIndex);
    }
    
    // https://stackoverflow.com/questions/20655024/javafx-gridpane-retrieve-specific-cell-content/20656861
    private Rectangle getRectFromGrid(int col, int row) {
        for (Node node : grid.getChildren()) {
            if (
            	GridPane.getColumnIndex(node) != null
            	&& GridPane.getRowIndex(node) != null
            	&& GridPane.getColumnIndex(node) == col
            	&& GridPane.getRowIndex(node) == row
            ) {
                return (Rectangle) node;
            }
        }
        return null;
    }
	
	private void refreshGrid() throws InvalidInputException {
		List<TOGridCell> toBlockAssignments = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);
		
        for (int i = 0; i < numCols ; i++) {
            for (int j = 0; j < numRows; j++) {
    			getRectFromGrid(i, j).setFill(Color.TRANSPARENT);
            }
        }
		for (TOGridCell toBlockAssignment : toBlockAssignments) {
			getRectFromGrid(
				toBlockAssignment.getGridHorizontalPosition() - 1,
				toBlockAssignment.getGridVerticalPosition() - 1
			).setFill(Color.color(
					toBlockAssignment.getRed() / 255.0,
					toBlockAssignment.getGreen() / 255.0,
					toBlockAssignment.getBlue() / 255.0
			));
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
}
