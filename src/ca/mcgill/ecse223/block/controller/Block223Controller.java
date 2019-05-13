package ca.mcgill.ecse223.block.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.model.Admin;
import ca.mcgill.ecse223.block.model.Ball;
import ca.mcgill.ecse223.block.model.Block;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.BlockAssignment;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.HallOfFameEntry;
import ca.mcgill.ecse223.block.model.Level;
import ca.mcgill.ecse223.block.model.Paddle;
import ca.mcgill.ecse223.block.model.PlayedBlockAssignment;
import ca.mcgill.ecse223.block.model.Player;
import ca.mcgill.ecse223.block.model.User;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223PlayModeInterface;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.PlayedGame.PlayStatus;


public class Block223Controller {
	// ****************************
	// Helper methods
	// ****************************
	private static String isAdmin(UserRole currentUserRole) {
		if (!(currentUserRole instanceof Admin)) {
			return "Admin privileges are required to add a block. ";
		} else {
			return "";
		}
	}

	private static String isGameSet(Game currentGame) {
		if (currentGame == null) {
			return "A game must be selected to add a block. ";
		} else {
			return "";
		}
	}

	private static String isAdminOfGame(UserRole currentUserRole, Game currentGame) {
		if (currentUserRole != currentGame.getAdmin()) {
			return "Admin privileges are required to add a block. ";
		} else {
			return "";
		}
	}

	private static String isBlockUnique(Game currentGame, int red, int green, int blue) {
		for (Block block : currentGame.getBlocks()) {
			if (
				block.getRed() == red
				&& block.getGreen() == green
				&& block.getBlue() == blue
			) {
				return "A block with the same color already exists for the game. ";
			}
		}

		return "";
	}

	private static void throwErrMsg(String errMsg) throws InvalidInputException {
		if (errMsg.length() > 0) {
			throw new InvalidInputException(errMsg.trim());
		}
	}

	// ****************************
	// Modifier methods
	// ****************************

	public static void createGame(String name) throws InvalidInputException {
		String error ="";
		Block223 block223 = Block223Application.getBlock223();
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to create a game.");
		}
		Admin admin = (Admin)Block223Application.getCurrentUserRole();
		try {
			new Game(name, 1, admin, 1, 1, 1.0, 10, 10, block223);
			// block223.addGame(newGame);
			// Block223Application.setCurrentGame(newGame);
			// block223.reinitialize();
		} catch (RuntimeException e){
			error = e.getMessage();
			if (error.equals("Cannot create due to duplicate name")) {
				error = "The name of a game must be unique.";
			}
			if (error.equals("The name of a game must be specified.")) {
				error = "The name of a game must be specified.";
			}
			throw new InvalidInputException(error.trim());
		}
	}

	public static void setGameDetails(int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {
		String error = "";
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to define game settings.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}
		if (nrLevels < 1 || nrLevels > 99) {
			throw new InvalidInputException("The number of levels must be between 1 and 99.");
		}

		Game game = Block223Application.getCurrentGame();
		
		if (minBallSpeedY == 0 && minBallSpeedX == 0) {
			throw new InvalidInputException("The minimum speed of the ball must be greater than zero.");
		}
		
		try {
			game.setNrBlocksPerLevel(nrBlocksPerLevel);
			Ball ball = game.getBall();
			ball.setMinBallSpeedX(minBallSpeedX);
			ball.setMinBallSpeedY(minBallSpeedY);
			ball.setBallSpeedIncreaseFactor(ballSpeedIncreaseFactor);
			Paddle paddle = game.getPaddle();
			paddle.setMaxPaddleLength(maxPaddleLength);
			paddle.setMinPaddleLength(minPaddleLength);
			List<Level> levels = new ArrayList<Level>();
			levels = game.getLevels();
			int size = levels.size();
			while(nrLevels > size) {
				game.addLevel();
				size = levels.size();
			}
			while(nrLevels < size) {
				Level level = game.getLevel(size - 1);
				level.delete();
				size = levels.size();
			}
		} catch (RuntimeException e) {
			String runError = "";
			error = e.getMessage();
			if (error.contains("The number of blocks per level must be greater than zero.")) {
				runError = runError + "The number of blocks per level must be greater than zero.";
			}
			if (error.contains("The maximum number of blocks per level cannot be less than the number of existing blocks in a level.")) {
				runError = runError + "The maximum number of blocks per level cannot be less than the number of existing blocks in a level.";
			}
			if (error.contains("The speed increase factor of the ball must be greater than zero.")) {
				runError = runError + "The speed increase factor of the ball must be greater than zero.";
			}
			if (error.contains("The maximum length of the paddle must be greater than zero and less than or equal to 390.")) {
				runError = runError + "The maximum length of the paddle must be greater than zero and less than or equal to 390.";
			}
			if (error.contains("The minimum length of the paddle must be greater than zero.")) {
				runError = runError + "The minimum length of the paddle must be greater than zero.";
			}

			throw new InvalidInputException(runError.trim());
		}
	}

	public static void deleteGame(String name) throws InvalidInputException {
		String error = "";

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			error = "Admin privileges are required to delete a game.";
		}
		if (error.length() > 0) {
			throw new InvalidInputException(error.trim());
		}
			Block223 block223 = Block223Application.getBlock223();
			Game game = block223.findGame(name);
			if(game != null) {
				if (!(Block223Application.getCurrentUserRole().equals(game.getAdmin()))) {
					throw new InvalidInputException("Only the admin who created the game can delete the game.");
				}
				if (game.isPublished()) throw new InvalidInputException("A published game cannot be deleted."); 
				game.delete();
				block223.removeGame(game); 
				Block223Persistence.save(block223);
			}
		
		
	}

	public static void selectGame(String name) throws InvalidInputException {
	
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to select a game.");
		}
		if (Block223Application.getBlock223().findGame(name) == null) {
			throw new InvalidInputException("A game with name " + name + " does not exist.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getBlock223().findGame(name).getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can select the game.");
		}
		if (Block223Application.getBlock223().findGame(name).isPublished()) {
			throw new InvalidInputException("A published game cannot be changed.");
		}

		Game game = Block223Application.getBlock223().findGame(name);
		Block223Application.setCurrentGame(game);
	}

	public static void updateGame(String name, int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {
		String error = "";
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to define game settings.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}
		if (name == null) throw new InvalidInputException("The name of a game must be specified."); 
		try {
			Game game = Block223Application.getCurrentGame();
			if (game.isPublished()) throw new InvalidInputException("A published game cannot be updated.");
			String currentName = game.getName(); 
			Game changedGame = Block223Application.getBlock223().findGame(name); 
/*modified*/	if (changedGame != null && !changedGame.equals(game) && !name.contentEquals(currentName)) {
					throw new InvalidInputException("The name of a game must be unique.");
				}
				if (!currentName.equals(name)) game.setName(name);
			setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY, ballSpeedIncreaseFactor, maxPaddleLength, minPaddleLength);
		} catch (Exception e) {
			String runError = "";
			error = e.getMessage();
			if (error.contains("The name of a game must be specified.")) {
				throw new InvalidInputException("The name of a game must be specified.");
			}
			if (error.contains("A published game cannot be updated.")) throw new InvalidInputException("A published game cannot be updated."); 
			if (error.contains("The name of a game must be unique.")) throw new InvalidInputException("The name of a game must be unique.");
		}
	}

	public static void addBlock(int red, int green, int blue, int points) throws InvalidInputException {
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		Game currentGame = Block223Application.getCurrentGame();
		if (currentGame == null) throw new InvalidInputException("A game must be selected to add a block."); 
		// Checks Not Covered in Domain Model
		if (!(currentUserRole instanceof Admin)) throw new InvalidInputException("Admin privileges are required to add a block."); 
		if (!currentGame.getAdmin().equals(currentUserRole)) throw new InvalidInputException("Only the admin who created the game can add a block."); 
		if (isBlockUnique(currentGame, red, green, blue).length() > 0) throw new InvalidInputException("A block with the same color already exists for the game."); 
		// Actual Functionality
		try {
			currentGame.addBlock(red, green, blue, points);
		}

		// Checks From Domain Model
		catch (RuntimeException err) {
			throw new InvalidInputException(err.getMessage());
		}
	}

	public static void deleteBlock(int id) throws InvalidInputException {
		
		Game currentGame = Block223Application.getCurrentGame();
	
		// Checks
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to delete a block.");
		}
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to delete a block.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can delete a block.");
		}
		

		// Actual Functionality
			Block block = currentGame.findBlock(id);

			if (block != null) {
				block.delete();
			}
		
	}

	public static void updateBlock(int id, int red, int green, int blue, int points) throws InvalidInputException {
		Game currentGame = Block223Application.getCurrentGame();
		if (currentGame == null) throw new InvalidInputException("A game must be selected to update a block."); 
		Block currentBlock = currentGame.findBlock(id);
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		if (!(currentUserRole instanceof Admin)) throw new InvalidInputException("Admin privileges are required to update a block."); 
		if (!currentGame.getAdmin().equals(currentUserRole)) throw new InvalidInputException("Only the admin who created the game can update a block.");
		if (currentBlock == null) throw new InvalidInputException("The block does not exist."); 
		if (points > 1000 || points < 1) throw new InvalidInputException("Points must be between 1 and 1000."); 
		for (Block block: currentGame.getBlocks()) {
			if (
				block.getBlue() == blue
				&& block.getGreen() == green
				&& block.getRed() == red
				&& block != currentBlock
			) {
				throw new InvalidInputException("A block with the same color already exists for the game."); 
			}
		}
		try {
			currentBlock.setRed(red);
			currentBlock.setGreen(green);
			currentBlock.setBlue(blue);
			currentBlock.setPoints(points);
		} catch (RuntimeException err) {
			throw new InvalidInputException(err.getMessage());
		}
	}

	public static void positionBlock(int id, int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		String error = "";
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to position a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to position a block.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can position a block.");
		}
		try {
			Game game = Block223Application.getCurrentGame();
			if (level < 1 || level > game.numberOfLevels()) {
				throw new IndexOutOfBoundsException("Level " + level + " does not exist for the game.");
			}
			Level currentLevel = game.getLevel(level - 1);
			int currentNrBlockInTheLevel = currentLevel.numberOfBlockAssignments();
			if (currentNrBlockInTheLevel == game.getNrBlocksPerLevel()) {
				throw new InvalidInputException("The number of blocks has reached the maximum number (" + game.getNrBlocksPerLevel() + ") allowed for this game.");
			}
			List<BlockAssignment> blockAssignments = currentLevel.getBlockAssignments();
			for (BlockAssignment blockAssignment: blockAssignments) {
				if (gridHorizontalPosition == blockAssignment.getGridHorizontalPosition()
				    && gridVerticalPosition == blockAssignment.getGridVerticalPosition()) {
					throw new InvalidInputException("A block already exists at location " + gridHorizontalPosition + "/" + gridVerticalPosition + ".");
				}
			}
			Block block = game.findBlock(id);
			if (block == null) {
				throw new InvalidInputException("The block does not exist.");
			}

			if (gridHorizontalPosition < 1 || gridHorizontalPosition > maxHorizontalGridPositionForABlock()) {
				throw new InvalidInputException("The horizontal position must be between 1 and " + maxHorizontalGridPositionForABlock() + ".");
			}
			if (gridVerticalPosition < 1 || gridVerticalPosition > maxVerticalGridPositionForABlock()) {
				throw new InvalidInputException("The vertical position must be between 1 and "+ maxVerticalGridPositionForABlock() + ".");
			}

			game.addBlockAssignment(gridHorizontalPosition, gridVerticalPosition, currentLevel, block); 
		}
		catch (IndexOutOfBoundsException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
        catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void moveBlock(int level, int oldGridHorizontalPosition, int oldGridVerticalPosition,
			int newGridHorizontalPosition, int newGridVerticalPosition) throws InvalidInputException {
		String error = "";
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to move a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to move a block.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can move a block.");
		}
		if (newGridHorizontalPosition < 1 || newGridHorizontalPosition > maxHorizontalGridPositionForABlock()) {
			throw new InvalidInputException("The horizontal position must be between 1 and " + maxHorizontalGridPositionForABlock() + ".");
		}
		if (newGridVerticalPosition < 1 || newGridVerticalPosition > maxVerticalGridPositionForABlock()) {
			throw new InvalidInputException("The vertical position must be between 1 and " + maxVerticalGridPositionForABlock() + ".");
		}
		try {
			Game game = Block223Application.getCurrentGame();
			if (level < 1 || level > game.numberOfLevels()) {
				throw new IndexOutOfBoundsException("Level " + level + " does not exist for the game.");
			}
			Level currentLevel = game.getLevel(level - 1);
			BlockAssignment assignment = currentLevel.findBlockAssignment(oldGridHorizontalPosition, oldGridVerticalPosition);
			
			if (assignment == null) {
				throw new InvalidInputException("A block does not exist at location " + oldGridHorizontalPosition + "/" + oldGridVerticalPosition + ".");
			}
			else {
				List<BlockAssignment> blockAssignments = currentLevel.getBlockAssignments();
				for (BlockAssignment blockAssignment: blockAssignments) {
					if (newGridHorizontalPosition == blockAssignment.getGridHorizontalPosition()
							&& newGridVerticalPosition == blockAssignment.getGridVerticalPosition()) {
						throw new InvalidInputException("A block already exists at location " + newGridHorizontalPosition + "/" + newGridVerticalPosition + ".");
					}
				}
				assignment.setGridHorizontalPosition(newGridHorizontalPosition);
				assignment.setGridVerticalPosition(newGridVerticalPosition);
			}
		}
		catch (IndexOutOfBoundsException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
        catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void removeBlock(int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		Game currentGame = Block223Application.getCurrentGame();
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		if (currentGame == null) throw new InvalidInputException("A game must be selected to remove a block."); 
		if (!(currentUserRole instanceof Admin)) throw new InvalidInputException("Admin privileges are required to remove a block."); 
		if (!currentGame.getAdmin().equals(currentUserRole)) throw new InvalidInputException("Only the admin who created the game can remove a block.");

		Level currentLevel = currentGame.getLevel(level - 1);
		if (currentLevel == null) throw new InvalidInputException("A game must be selected to remove a block."); 
		BlockAssignment currentBlock = currentLevel.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition);
		if(currentBlock!=null) {
			currentBlock.delete();
			currentLevel.removeBlockAssignment(currentBlock); 
		}

	}

	public static void saveGame() throws InvalidInputException {
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		Game currentGame = Block223Application.getCurrentGame();
		Block223 block223 = Block223Application.getBlock223();
		if (!(currentUserRole instanceof Admin)) throw new InvalidInputException("Admin privileges are required to save a game.");
		if (currentGame == null) throw new InvalidInputException("A game must be selected to save it.");
		if (!currentGame.getAdmin().equals(currentUserRole)) throw new InvalidInputException("Only the admin who created the game can save it.");
		try {
			Block223Persistence.save(block223);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void register(String username, String playerPassword, String adminPassword) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		UserRole newUserRolePlayer = null;
		UserRole newUserRoleAdmin = null;
		User newUser = null;
		if (currentUserRole != null) throw new InvalidInputException("Cannot register a new user while a user is logged in.");
		if (playerPassword != null && playerPassword.equals(adminPassword)) throw new InvalidInputException("The passwords have to be different.");
		try {
			newUserRolePlayer = new Player(playerPassword, block223);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		try {
			newUser = new User(username, block223, newUserRolePlayer);
			if (adminPassword != null && !adminPassword.isEmpty()) {
				newUserRoleAdmin = new Admin(adminPassword, block223);
				newUser.addRole(newUserRoleAdmin);
			}
			Block223Persistence.save(block223);
		} catch (RuntimeException e) {
			if (newUserRoleAdmin != null) newUserRoleAdmin.delete();
			if (newUser != null) newUser.delete();
			if (newUserRolePlayer != null) newUserRolePlayer.delete();
			if (e.getMessage().equals("The username must be specified.")) throw new InvalidInputException("The username must be specified.");
			if (e.getMessage().equals("Cannot create due to duplicate username")) throw new InvalidInputException("The username has already been taken.");
		}
	}

	public static void login(String username, String password) throws InvalidInputException {
		Block223Application.resetBlock223();
		User user = User.getWithUsername(username);
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		if (currentUserRole != null) throw new InvalidInputException("Cannot login a user while a user is already logged in.");
		if (user == null) throw new InvalidInputException("The username and password do not match.");
		List<UserRole> roles = user.getRoles();
		boolean isExist = false;
		for (UserRole role: roles) {
			String rolePassword = role.getPassword();
			if (rolePassword.equals(password)) {
				Block223Application.setCurrentUserRole(role);
				isExist = true;
				break;
			}
		}
		if (!isExist) throw new InvalidInputException("The username and password do not match.");
	}

	public static void logout() {
		Block223Application.setCurrentUserRole(null);
	}

    // play mode

	public static void selectPlayableGame(String name, int id) throws InvalidInputException  {
		if (!(Block223Application.getCurrentUserRole() instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getBlock223().findGame(name) == null && Block223Application.getBlock223().findPlayableGame(id) == null) {
			throw new InvalidInputException("The game does not exist.");
		}
		
		Game game = Block223Application.getBlock223().findGame(name);
		Block223 block223 = Block223Application.getBlock223();
		PlayedGame pgame;
		if (game != null) {
			Player player = (Player) Block223Application.getCurrentUserRole();
            String username = User.findUsername(player);
			pgame = new PlayedGame(username, game, block223);
            pgame.setPlayer(player);
		} else {
			pgame = block223.findPlayableGame(id);
			if (!Block223Application.getCurrentUserRole().equals(pgame.getPlayer())) {
				throw new InvalidInputException("Only the player that started a game can continue the game.");
			}
		}
		
		Block223Application.setCurrentPlayableGame(pgame);
	}

	public static void startGame(Block223PlayModeInterface ui) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to play it.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Admin) && (Block223Application.getCurrentPlayableGame().getPlayer() != null)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Admin) && (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin())) {
			throw new InvalidInputException("Only the admin of a game can test the game.");
		}
		if ((Block223Application.getCurrentUserRole() instanceof Player) && (Block223Application.getCurrentPlayableGame().getPlayer() == null)) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}
		
		PlayedGame game = Block223Application.getCurrentPlayableGame();
		game.play();

		ui.takeInputs();
		while (game.getPlayStatus() == PlayStatus.Moving) {
			String userInputs = ui.takeInputs();
			updatePaddlePosition(userInputs, game);
			game.move();
			if (userInputs.contains(" ")) {
     			game.pause();
			}
			try {
				TimeUnit.MILLISECONDS.sleep((int) game.getWaitTime());
				// Thread.sleep((int) game.getWaitTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

				ui.refresh();

		}

		if (game.getPlayStatus() == PlayStatus.GameOver) {
			// Block223Application.setCurrentPlayableGame(null);
			
			
			ui.endGame(game.getLives(),
					new TOHallOfFameEntry(game.getGame().numberOfHallOfFameEntries(),
					    game.getPlayername(),
					    game.getScore(),
							new TOHallOfFame(game.getGame().getName())));
			Block223Persistence.save(Block223Application.getBlock223());
			
		} else {
			if (game.getPlayer() != null) {
				game.setBounce(null);
				Block223Persistence.save(Block223Application.getBlock223());
			}
		}
	}
	
	public static void startGame1(Block223PlayModeInterface ui) throws InvalidInputException {
		System.out.println(" *********** STARTING THE GAME *********** ");

		ui.takeInputs();
		while (true) {
			String input = ui.takeInputs();
			
			if(input.contains(" ")) {
				String inputBeforeSpace = input.substring(0, (input.indexOf(" ")));
				System.out.println(" Input received by CONTROLLER: " + inputBeforeSpace + " then paused");
				break;
			}
				
			System.out.println(" Input received by CONTROLLER: " + input);
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ui.refresh();
		}
			

	}
	
	public static void updatePaddlePosition(String userInputs, PlayedGame pgame) {
		for (char c: userInputs.toCharArray()) {
			if (c == ' ') break; 
			if (c == 'l' && pgame.getCurrentPaddleX() >= -PlayedGame.PADDLE_MOVE_LEFT) {
				pgame.setCurrentPaddleX(pgame.getCurrentPaddleX() + PlayedGame.PADDLE_MOVE_LEFT);
			} 
			if (
				c == 'r'
				&& pgame.getCurrentPaddleX()
					<= 390 - PlayedGame.PADDLE_MOVE_RIGHT - pgame.getCurrentPaddleLength()
			) {
				pgame.setCurrentPaddleX(pgame.getCurrentPaddleX() + PlayedGame.PADDLE_MOVE_RIGHT); 
			}
		}
	}

	public static void testGame(Block223PlayModeInterface ui) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to test it.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can test it.");
		}
		if(Block223Application.getCurrentGame().getBlocks().size() == 0) {
			throw new InvalidInputException("At least one block must be defined for a game to be tested.");
		}
		
		Game game = Block223Application.getCurrentGame();
		UserRole admin = Block223Application.getCurrentUserRole();
		String username = User.findUsername(admin);
		Block223 block223 = Block223Application.getBlock223();
		
		PlayedGame pgame = new PlayedGame(username, game, block223); 
		pgame.setPlayer(null);
		Block223Application.setCurrentPlayableGame(pgame);
		startGame(ui);
	}

	public static void publishGame () throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to publish a game.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to publish it.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can publish it.");
		}
		if(Block223Application.getCurrentGame().getBlocks().size() == 0) {
			throw new InvalidInputException("At least one block must be defined for a game to be published.");
		}
		Game game = Block223Application.getCurrentGame();
		game.setPublished(true);

		
	}

	// ****************************
	// Query methods
	// ****************************

	public static List<TOGame> getDesignableGames() throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException ("Admin privileges are required to access game information.");
		}
		Block223 block223 = Block223Application.getBlock223();
		UserRole admin = Block223Application.getCurrentUserRole();

		List<TOGame> result = new ArrayList<TOGame>();

		List<Game> games = block223.getGames();

		for (Game game: games) {
			Admin gameAdmin = game.getAdmin();

			if(gameAdmin == admin && !game.isPublished()) {
			TOGame to = new TOGame(game.getName(),
						game.getLevels().size(),
						game.getNrBlocksPerLevel(),
						game.getBall().getMinBallSpeedX(),
						game.getBall().getMinBallSpeedY(),
						game.getBall().getBallSpeedIncreaseFactor(),
						game.getPaddle().getMaxPaddleLength(),
						game.getPaddle().getMinPaddleLength());
			result.add(to);
			}
		}

		return result;
	}

	public static List<TOGame> getDesignableGamesPlayer() throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
		UserRole admin = Block223Application.getCurrentUserRole();

		List<TOGame> result = new ArrayList<TOGame>();

		List<Game> games = block223.getGames();

		for (Game game: games) {
			Admin gameAdmin = game.getAdmin();

			if(gameAdmin.equals(admin)) {
			TOGame to = new TOGame(game.getName(),
						game.getLevels().size(),
						game.getNrBlocksPerLevel(),
						game.getBall().getMinBallSpeedX(),
						game.getBall().getMinBallSpeedY(),
						game.getBall().getBallSpeedIncreaseFactor(),
						game.getPaddle().getMaxPaddleLength(),
						game.getPaddle().getMinPaddleLength());
			result.add(to);
			}

		}

		return result;
	}

	public static TOGame getCurrentDesignableGame() throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		
		Game game = Block223Application.getCurrentGame();
		TOGame to = new TOGame(game.getName(),
				game.getLevels().size(),
				game.getNrBlocksPerLevel(),
				game.getBall().getMinBallSpeedX(),
				game.getBall().getMinBallSpeedY(),
				game.getBall().getBallSpeedIncreaseFactor(),
				game.getPaddle().getMaxPaddleLength(),
				game.getPaddle().getMinPaddleLength());
		return to;
	}

	public static List<TOBlock> getBlocksOfCurrentDesignableGame() throws InvalidInputException {
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		Game currentGame = Block223Application.getCurrentGame();
		String errMsg = "";

		// Checks
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		errMsg += isAdmin(currentUserRole);
		errMsg += isGameSet(currentGame);
		errMsg += isAdminOfGame(currentUserRole, currentGame);
		throwErrMsg(errMsg);

		// Actual Functionality
		List<TOBlock> result = new ArrayList<TOBlock>();
		List<Block> blocks = currentGame.getBlocks();

		for (Block block : blocks) {
			result.add(
				new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints())
			);
		}

		return result;
	}


	public static TOBlock getBlockOfCurrentDesignableGame(int id) throws InvalidInputException {
		UserRole currentUserRole = Block223Application.getCurrentUserRole();
		Game currentGame = Block223Application.getCurrentGame();
		if (currentGame == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (!(currentUserRole instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		Block currentBlock = null; 
		for (Block block: currentGame.getBlocks()) if (block.getId() == id) currentBlock = block; 
		
		if (currentBlock == null) throw new InvalidInputException("The block does not exist."); 
		

		// Actual Functionality

		TOBlock to = new TOBlock(currentBlock.getId(), currentBlock.getRed(), currentBlock.getGreen(), currentBlock.getBlue(), currentBlock.getPoints());
		return to;

	}

	public static List<TOGridCell> getBlocksAtLevelOfCurrentDesignableGame(int level) throws InvalidInputException {
		String error = "";
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		Game game = Block223Application.getCurrentGame();
		try {
			if (level < 1 || level > game.numberOfLevels()) {
				throw new IndexOutOfBoundsException("Level " + level + " does not exist for the game.");
			}
			Level currentLevel = game.getLevel(level - 1);
			List<BlockAssignment> assignments = currentLevel.getBlockAssignments();
			List<TOGridCell> result = new ArrayList<TOGridCell>();
			for (BlockAssignment assignment: assignments) {
				TOGridCell to = new TOGridCell(assignment.getGridHorizontalPosition(),
									assignment.getGridVerticalPosition(),
									assignment.getBlock().getId(),
									assignment.getBlock().getRed(),
									assignment.getBlock().getGreen(),
									assignment.getBlock().getBlue(),
									assignment.getBlock().getPoints());
				result.add(to);
			}
			return result;
		}
		catch (IndexOutOfBoundsException e) {
			error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}

	public static TOUserMode getUserMode() {
		UserRole userRole = Block223Application.getCurrentUserRole();
		if (userRole == null) return new TOUserMode(TOUserMode.Mode.None);
		if (userRole instanceof Player) return new TOUserMode(TOUserMode.Mode.Play);
		if (userRole instanceof Admin) return new TOUserMode(TOUserMode.Mode.Design);
		return new TOUserMode(TOUserMode.Mode.None);
	}

    // play mode

	public static List<TOPlayableGame> getPlayableGames() throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		Block223 block223 = Block223Application.getBlock223();
		Player player = (Player)Block223Application.getCurrentUserRole();
		List<TOPlayableGame> result = new ArrayList<TOPlayableGame>();
		List<Game> games = block223.getGames();
		for (Game game : games) {
			boolean published = game.isPublished();
			if (published) {
				TOPlayableGame to = new TOPlayableGame(game.getName(), -1, 0);
				result.add(to);
			}
		}
		List<PlayedGame> playedGames = player.getPlayedGames();
		for (PlayedGame pgame : playedGames) {
			TOPlayableGame to = new TOPlayableGame(pgame.getGame().getName(), pgame.getId(), pgame.getCurrentLevel());
			result.add(to);
		}
		return result;	
	}

	public static TOCurrentlyPlayedGame getCurrentPlayableGame() throws InvalidInputException {
		if (Block223Application.getCurrentUserRole() == null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to play it.");
		}
		if (Block223Application.getCurrentUserRole() instanceof Admin && Block223Application.getCurrentPlayableGame().getPlayer() != null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getCurrentUserRole() instanceof Admin && Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin of a game can test the game.");
		}
		if (Block223Application.getCurrentUserRole() instanceof Player && Block223Application.getCurrentPlayableGame().getPlayer() == null) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}
        PlayedGame pgame = Block223Application.getCurrentPlayableGame();
        TOCurrentlyPlayedGame result = new TOCurrentlyPlayedGame(
        		pgame.getGame().getName(),
        		(pgame.getPlayStatus()==PlayStatus.Ready || pgame.getPlayStatus()==PlayStatus.Paused),
        		pgame.getScore(),
        		pgame.getLives(),
        		pgame.getCurrentLevel(),
        		pgame.getPlayername(),
        		pgame.getCurrentBallX(),
        		pgame.getCurrentBallY(),
        		pgame.getCurrentPaddleLength(),
        		pgame.getCurrentPaddleX()
        		);
        List<PlayedBlockAssignment> blocks = pgame.getBlocks();
        for (PlayedBlockAssignment pblock : blocks) {
        	TOCurrentBlock to = new TOCurrentBlock(pblock.getBlock().getRed(),
        			pblock.getBlock().getGreen(),
        			pblock.getBlock().getBlue(),
        			pblock.getBlock().getPoints(), 
        			pblock.getX(),
        			pblock.getY(),
        			result
        			);
        }
        return result;
	}

	public static TOHallOfFame getHallOfFame(int start, int end) throws InvalidInputException {
        if (!(Block223Application.getCurrentUserRole() instanceof Player)) throw new InvalidInputException("Player privileges are required to access a game�s hall of fame.");
        PlayedGame pgame = Block223Application.getCurrentPlayableGame();
        if (pgame == null) throw new InvalidInputException("A game must be selected to view its hall of fame.");
        Game game = pgame.getGame();
        TOHallOfFame result = new TOHallOfFame(game.getName());
        if (start < 1) start = 1;
        if (end > game.numberOfHallOfFameEntries()) end = game.numberOfHallOfFameEntries();
        start = game.numberOfHallOfFameEntries() - start;
        end = game.numberOfHallOfFameEntries() - end;
        for (int i = start; i >= end; i--) new TOHallOfFameEntry(i + 1, User.findUsername(game.getHallOfFameEntry(i).getPlayer()), game.getHallOfFameEntry(i).getScore(), result);
        return result;
	}

	public static TOHallOfFame getHallOfFameWithMostRecentEntry(int numberOfEntries) throws InvalidInputException {
        if (!(Block223Application.getCurrentUserRole() instanceof Player)) throw new InvalidInputException("Player privileges are required to access a game�s hall of fame.");
        PlayedGame pgame = Block223Application.getCurrentPlayableGame();
        if (pgame == null) throw new InvalidInputException("A game must be selected to view its hall of fame.");
        Game game = pgame.getGame();
        TOHallOfFame result = new TOHallOfFame(game.getName());
        HallOfFameEntry mostRecentEntry = game.getMostRecentEntry();
        if (mostRecentEntry == null) return result; 
        int indexR = game.indexOfHallOfFameEntry(mostRecentEntry);
        int start = indexR + numberOfEntries / 2;
        if (start > game.numberOfHallOfFameEntries() - 1) start = game.numberOfHallOfFameEntries() - 1;
        int end = start - numberOfEntries + 1;
        if (end < 0) end = 0; 
        for (int i = start; i >= end; i--) new TOHallOfFameEntry(i + 1, User.findUsername(game.getHallOfFameEntry(i).getPlayer()), game.getHallOfFameEntry(i).getScore(), result);
        return result;
	}

	/* Extra methods Jeffery Tang defined */
	public static void setUsername(String username) {
		Block223Application.setUsername(username);
	}
	
	public static String getUsername() {
		return Block223Application.getUsername();
	}

	// also logs user out then in
	public static void resetBlock223() throws InvalidInputException {
		String password = Block223Application.getCurrentUserRole().getPassword();
		logout();
		Block223Application.resetBlock223();
		login(Block223Application.getUsername(), password);
	}

	public static int maxVerticalGridPositionForABlock() {
		return (
			Game.PLAY_AREA_SIDE
			- Game.WALL_PADDING
			+ Game.ROW_PADDING
			- Ball.BALL_DIAMETER
			- Paddle.PADDLE_WIDTH 
			- Paddle.VERTICAL_DISTANCE
		) / (Block.SIZE + Game.ROW_PADDING);
	}

	public static int maxHorizontalGridPositionForABlock() {
		return (Game.PLAY_AREA_SIDE - 2 * Game.WALL_PADDING + Game.COLUMNS_PADDING)
			/ (Block.SIZE + Game.COLUMNS_PADDING);
	}

	public static void publishAndSave() throws InvalidInputException {
		publishGame();
		saveGame();
	}

	public static boolean gameExists(String name) {
		Block223 block223 = Block223Application.getBlock223();
		return block223.findGame(name) != null;
	}

	public static int getBallDiameter() {
		return Ball.BALL_DIAMETER;
	}

	public static int getBlockSize() {
		return Block.SIZE;
	}

	public static int getPaddleWidth() {
		return Paddle.PADDLE_WIDTH;
	}

	public static int getVerticalDistance() {
		return Paddle.VERTICAL_DISTANCE;
	}
	

	public static HashMap<String, Integer> gameConstants() {
		HashMap<String, Integer> gameConstants = new HashMap<String, Integer>();
		
		gameConstants.put("BALL_DIAMETER", Ball.BALL_DIAMETER);
		gameConstants.put("BLOCK_SIZE", Block.SIZE);
		gameConstants.put("PADDLE_WIDTH", Paddle.PADDLE_WIDTH);
		gameConstants.put("PADDLE_VERTICAL_DISTANCE", Paddle.VERTICAL_DISTANCE);
		gameConstants.put("PLAY_AREA_SIZE", Game.PLAY_AREA_SIDE);
		
		return gameConstants;
	}
}