package ca.mcgill.ecse223.block.helper;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class MoreInfo {
	public static void close(Button close) {
		 Tooltip tClose = new Tooltip("Close App");
		 Tooltip.install(close, tClose);
	}
	
	public static void logoutAndClose(Button logout, Button close) {
		 Tooltip tLogout = new Tooltip("Logout");
		 Tooltip.install(logout, tLogout);
		 close(close);
	}
	
	public static void saveLogoutClose(Button saveAndExit, Button logout, Button close) {
		 Tooltip tSaveAndExit = new Tooltip("Save and Exit");
		 Tooltip.install(saveAndExit, tSaveAndExit);
		logoutAndClose(logout, close);
	}
	
	public static void quit(Button quit) {
		 Tooltip tQuit = new Tooltip("Save and Exit");
		 Tooltip.install(quit, tQuit);
	}
}
