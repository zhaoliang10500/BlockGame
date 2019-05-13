package ca.mcgill.ecse223.block.view;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Block223PlayModeListener implements EventHandler<KeyEvent> {

	/**
	 * 'String input from keyboard - marked as volatile since it is shared by two
	 * threads
	 */
	private volatile String keyString = "";

	@Override
	public synchronized void handle(KeyEvent e) {
		try {
			keyInputs(e);
		} catch (Exception e1) {
			System.out.print(e1);
		}
	}

	private synchronized String keyInputs(KeyEvent e) {
		KeyCode location = e.getCode();
		if (location == KeyCode.LEFT) {
			keyString += "l";
		} else if (location == KeyCode.RIGHT) {
			keyString += "r";
		} else if (location == KeyCode.SPACE) {
			keyString += " ";
		} else {
			// ignore all other keys
		}
		return keyString;
	}

	/**
	 * Takes key inputs and clears the input string. marked as synchronized to make
	 * the key inputs as thread-safe
	 */
	public synchronized String takeInputs() {
		String passString = keyString;
		keyString = "";
		return passString;
	}

}

