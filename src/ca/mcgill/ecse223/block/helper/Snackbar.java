package ca.mcgill.ecse223.block.helper;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Snackbar {	
	public static void show(Pane root, Exception e) {
		String errMsg = e.getMessage();
		if (errMsg == null) {
			errMsg = "This is an error with no specified message, yikes.";
			e.printStackTrace();
		}
		
		show(root, errMsg);
	}
	
	public static void show(Pane root, String msg) {
		JFXSnackbar snackbar = new JFXSnackbar(root);
		
		Label label = new Label(msg);
	    label.setPrefHeight(30);
	    label.setPrefWidth(400);
	    label.setTextFill(Color.WHITE);
	    label.setBackground(new Background(
	    	new BackgroundFill(Color.web("0x222222", 1.0),
	    	new CornerRadii(5, 5, 0, 0, false),
	    	new Insets(0, 10, 0, 10)))
	    );
	    label.setAlignment(Pos.CENTER);
	    ImageView imgView = new ImageView(new Image("warning.png"));
	    imgView.setFitHeight(20);
	    imgView.setPreserveRatio(true);
	    label.setGraphic(imgView);
		
		snackbar.enqueue(new SnackbarEvent(label));
	}
}
