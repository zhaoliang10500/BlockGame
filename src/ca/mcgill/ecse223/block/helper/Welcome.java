package ca.mcgill.ecse223.block.helper;

import java.util.Random;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import javafx.scene.text.Text;

public class Welcome {
	public static void greet(Text textNode) {
		String username = Block223Controller.getUsername();
		
		if (username != null) {
			String greeting = "";
			Random rand = new Random();
			
			switch (rand.nextInt(10)) {
				case 0:
					greeting = "Howdy";
					break;
				case 1:
					greeting = "Welcome back";
					break;
				case 2:
					greeting = "Sup";
					break;
				case 3:
					greeting = "Hello,";
					break;
				case 4:
					greeting = "How's it going,";
					break;
				case 5:
					greeting = "Ciao";
					break;
				case 6:
					greeting = "Glad to have you back,";
					break;
				case 7:
					greeting = "Get ready to own some noobs,";
					break;
				case 8:
					greeting = "It's great to have you back,";
					break;
				case 9:
					greeting = "This is where the fun begins,";
					break;
			}
			
			textNode.setText(greeting + " " + username);
		}
	}
}
