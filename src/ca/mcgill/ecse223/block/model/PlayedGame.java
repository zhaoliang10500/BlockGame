/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.block.model;
import java.awt.geom.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.Serializable;
import java.util.*;

// line 22 "../../../../../Block223PlayMode.ump"
// line 103 "../../../../../Block223Persistence.ump"
// line 1 "../../../../../Block223States.ump"
public class PlayedGame implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------


  /**
   * at design time, the initial wait time may be adjusted as seen fit
   */
  public static final int INITIAL_WAIT_TIME = 20;
  private static int nextId = 1;
  public static final int NR_LIVES = 3;

  /**
   * the PlayedBall and PlayedPaddle are not in a separate class to avoid the bug in Umple that occurred for the second constructor of Game
   * no direct link to Ball, because the ball can be found by navigating to Game and then Ball
   */
  public static final int BALL_INITIAL_X = Game.PLAY_AREA_SIDE / 2;
  public static final int BALL_INITIAL_Y = Game.PLAY_AREA_SIDE / 2;

  /**
   * no direct link to Paddle, because the paddle can be found by navigating to Game and then Paddle
   * pixels moved when right arrow key is pressed
   */
  public static final int PADDLE_MOVE_RIGHT = 5;

  /**
   * pixels moved when left arrow key is pressed
   */
  public static final int PADDLE_MOVE_LEFT = -5;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PlayedGame Attributes
  private int score;
  private int lives;
  private int currentLevel;
  private double waitTime;
  private String playername;
  private double ballDirectionX;
  private double ballDirectionY;
  private double currentBallX;
  private double currentBallY;
  private double currentPaddleLength;
  private double currentPaddleX;
  private double currentPaddleY;

  //Autounique Attributes
  private int id;

  //PlayedGame State Machines
  public enum PlayStatus { Ready, Moving, Paused, GameOver }
  private PlayStatus playStatus;

  //PlayedGame Associations
  private Player player;
  private Game game;
  private List<PlayedBlockAssignment> blocks;
  private BouncePoint bounce;
  private Block223 block223;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PlayedGame(String aPlayername, Game aGame, Block223 aBlock223)
  {
    // line 61 "../../../../../Block223PlayMode.ump"
    boolean didAddGameResult = setGame(aGame);
          if (!didAddGameResult)
          {
             throw new RuntimeException("Unable to create playedGame due to game");
          }
    // END OF UMPLE BEFORE INJECTION
    score = 0;
    lives = NR_LIVES;
    currentLevel = 1;
    waitTime = INITIAL_WAIT_TIME;
    playername = aPlayername;
    resetBallDirectionX();
    resetBallDirectionY();
    resetCurrentBallX();
    resetCurrentBallY();
    currentPaddleLength = getGame().getPaddle().getMaxPaddleLength();
    resetCurrentPaddleX();
    currentPaddleY = Game.PLAY_AREA_SIDE - Paddle.VERTICAL_DISTANCE - Paddle.PADDLE_WIDTH;
    id = nextId++;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create playedGame due to game");
    }
    blocks = new ArrayList<PlayedBlockAssignment>();
    boolean didAddBlock223 = setBlock223(aBlock223);
    if (!didAddBlock223)
    {
      throw new RuntimeException("Unable to create playedGame due to block223");
    }
    setPlayStatus(PlayStatus.Ready);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setScore(int aScore)
  {
    boolean wasSet = false;
    score = aScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setLives(int aLives)
  {
    boolean wasSet = false;
    lives = aLives;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentLevel(int aCurrentLevel)
  {
    boolean wasSet = false;
    currentLevel = aCurrentLevel;
    wasSet = true;
    return wasSet;
  }

  public boolean setWaitTime(double aWaitTime)
  {
    boolean wasSet = false;
    waitTime = aWaitTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayername(String aPlayername)
  {
    boolean wasSet = false;
    playername = aPlayername;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionX(double aBallDirectionX)
  {
    boolean wasSet = false;
    ballDirectionX = aBallDirectionX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionX()
  {
    boolean wasReset = false;
    ballDirectionX = getDefaultBallDirectionX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionY(double aBallDirectionY)
  {
    boolean wasSet = false;
    ballDirectionY = aBallDirectionY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionY()
  {
    boolean wasReset = false;
    ballDirectionY = getDefaultBallDirectionY();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallX(double aCurrentBallX)
  {
    boolean wasSet = false;
    currentBallX = aCurrentBallX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallX()
  {
    boolean wasReset = false;
    currentBallX = getDefaultCurrentBallX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallY(double aCurrentBallY)
  {
    boolean wasSet = false;
    currentBallY = aCurrentBallY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallY()
  {
    boolean wasReset = false;
    currentBallY = getDefaultCurrentBallY();
    wasReset = true;
    return wasReset;
  }

  public boolean setCurrentPaddleLength(double aCurrentPaddleLength)
  {
    boolean wasSet = false;
    currentPaddleLength = aCurrentPaddleLength;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentPaddleX(double aCurrentPaddleX)
  {
    boolean wasSet = false;
    currentPaddleX = aCurrentPaddleX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentPaddleX()
  {
    boolean wasReset = false;
    currentPaddleX = getDefaultCurrentPaddleX();
    wasReset = true;
    return wasReset;
  }

  public int getScore()
  {
    return score;
  }

  public int getLives()
  {
    return lives;
  }

  public int getCurrentLevel()
  {
    return currentLevel;
  }

  public double getWaitTime()
  {
    return waitTime;
  }

  /**
   * added here so that it only needs to be determined once
   */
  public String getPlayername()
  {
    return playername;
  }

  /**
   * 0/0 is the top left corner of the play area, i.e., a directionX/Y of 0/1 moves the ball down in a straight line
   */
  public double getBallDirectionX()
  {
    return ballDirectionX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionX()
  {
    return getGame().getBall().getMinBallSpeedX();
  }

  public double getBallDirectionY()
  {
    return ballDirectionY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionY()
  {
    return getGame().getBall().getMinBallSpeedY();
  }

  /**
   * the position of the ball is at the center of the ball
   */
  public double getCurrentBallX()
  {
    return currentBallX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallX()
  {
    return BALL_INITIAL_X;
  }

  public double getCurrentBallY()
  {
    return currentBallY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallY()
  {
    return BALL_INITIAL_Y;
  }

  public double getCurrentPaddleLength()
  {
    return currentPaddleLength;
  }

  /**
   * the position of the paddle is at its top left corner
   */
  public double getCurrentPaddleX()
  {
    return currentPaddleX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentPaddleX()
  {
    return (Game.PLAY_AREA_SIDE - currentPaddleLength) / 2;
  }

  public double getCurrentPaddleY()
  {
    return currentPaddleY;
  }

  public int getId()
  {
    return id;
  }

  public String getPlayStatusFullName()
  {
    String answer = playStatus.toString();
    return answer;
  }

  public PlayStatus getPlayStatus()
  {
    return playStatus;
  }

  public boolean play()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Ready:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      case Paused:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pause()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        setPlayStatus(PlayStatus.Paused);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean move()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        if (hitPaddle())
        {
        // line 11 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBoundsAndLastLife())
        {
        // line 12 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBounds())
        {
        // line 13 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.Paused);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlockAndLastLevel())
        {
        // line 14 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlock())
        {
        // line 15 "../../../../../Block223States.ump"
          doHitBlockNextLevel();
          setPlayStatus(PlayStatus.Ready);
          wasEventProcessed = true;
          break;
        }
        if (hitBlock())
        {
        // line 16 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (hitWall())
        {
        // line 17 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        // line 18 "../../../../../Block223States.ump"
        doHitNothingAndNotOutOfBounds();
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setPlayStatus(PlayStatus aPlayStatus)
  {
    playStatus = aPlayStatus;

    // entry actions and do activities
    switch(playStatus)
    {
      case Ready:
        // line 6 "../../../../../Block223States.ump"
        doSetup();
        break;
      case GameOver:
        // line 24 "../../../../../Block223States.ump"
        doGameOver();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public PlayedBlockAssignment getBlock(int index)
  {
    PlayedBlockAssignment aBlock = blocks.get(index);
    return aBlock;
  }

  public List<PlayedBlockAssignment> getBlocks()
  {
    List<PlayedBlockAssignment> newBlocks = Collections.unmodifiableList(blocks);
    return newBlocks;
  }

  public int numberOfBlocks()
  {
    int number = blocks.size();
    return number;
  }

  public boolean hasBlocks()
  {
    boolean has = blocks.size() > 0;
    return has;
  }

  public int indexOfBlock(PlayedBlockAssignment aBlock)
  {
    int index = blocks.indexOf(aBlock);
    return index;
  }
  /* Code from template association_GetOne */
  public BouncePoint getBounce()
  {
    return bounce;
  }

  public boolean hasBounce()
  {
    boolean has = bounce != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Block223 getBlock223()
  {
    return block223;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setPlayer(Player aPlayer)
  {
    boolean wasSet = false;
    Player existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      existingPlayer.removePlayedGame(this);
    }
    if (aPlayer != null)
    {
      aPlayer.addPlayedGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removePlayedGame(this);
    }
    game.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBlocks()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PlayedBlockAssignment addBlock(int aX, int aY, Block aBlock)
  {
    return new PlayedBlockAssignment(aX, aY, aBlock, this);
  }

  public boolean addBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasAdded = false;
    if (blocks.contains(aBlock)) { return false; }
    PlayedGame existingPlayedGame = aBlock.getPlayedGame();
    boolean isNewPlayedGame = existingPlayedGame != null && !this.equals(existingPlayedGame);
    if (isNewPlayedGame)
    {
      aBlock.setPlayedGame(this);
    }
    else
    {
      blocks.add(aBlock);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasRemoved = false;
    //Unable to remove aBlock, as it must always have a playedGame
    if (!this.equals(aBlock.getPlayedGame()))
    {
      blocks.remove(aBlock);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBlockAt(PlayedBlockAssignment aBlock, int index)
  {  
    boolean wasAdded = false;
    if(addBlock(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBlockAt(PlayedBlockAssignment aBlock, int index)
  {
    boolean wasAdded = false;
    if(blocks.contains(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBlockAt(aBlock, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setBounce(BouncePoint aNewBounce)
  {
    boolean wasSet = false;
    bounce = aNewBounce;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBlock223(Block223 aBlock223)
  {
    boolean wasSet = false;
    if (aBlock223 == null)
    {
      return wasSet;
    }

    Block223 existingBlock223 = block223;
    block223 = aBlock223;
    if (existingBlock223 != null && !existingBlock223.equals(aBlock223))
    {
      existingBlock223.removePlayedGame(this);
    }
    block223.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (player != null)
    {
      Player placeholderPlayer = player;
      this.player = null;
      placeholderPlayer.removePlayedGame(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayedGame(this);
    }
    while (blocks.size() > 0)
    {
      PlayedBlockAssignment aBlock = blocks.get(blocks.size() - 1);
      aBlock.delete();
      blocks.remove(aBlock);
    }
    
    bounce = null;
    Block223 placeholderBlock223 = block223;
    this.block223 = null;
    if(placeholderBlock223 != null)
    {
      placeholderBlock223.removePlayedGame(this);
    }
  }

  // line 108 "../../../../../Block223Persistence.ump"
   public static  void reinitializeAutouniqueID(List<PlayedGame> games){
    nextId = 0; 
    for (PlayedGame game : games) {
      if (game.getId() > nextId) {
        nextId = game.getId();
      }
    }
    nextId++;
  }

  // line 29 "../../../../../Block223States.ump"
   private boolean hitPaddle(){
    BouncePoint bp = calculateBouncePointPaddle();
  	setBounce(bp);
    return bp != null;
  }

  // line 35 "../../../../../Block223States.ump"
   private boolean isOutOfBoundsAndLastLife(){
    boolean outOfBounds = false;
    if (lives == 1) {
    	outOfBounds = isBallOutOfBounds();
    }
    return outOfBounds;
  }

  // line 43 "../../../../../Block223States.ump"
   private boolean isBallOutOfBounds(){
    boolean ballOutOfBounds = false;
	if (getCurrentBallY() >= Game.PLAY_AREA_SIDE - Ball.BALL_DIAMETER / 2) {
		ballOutOfBounds = true;
	}
	return ballOutOfBounds;
  }

  // line 51 "../../../../../Block223States.ump"
   private boolean isOutOfBounds(){
    boolean outOfBounds = false;
	outOfBounds = isBallOutOfBounds();
	return outOfBounds;
  }

  // line 57 "../../../../../Block223States.ump"
   private boolean hitLastBlockAndLastLevel(){
    Game game = getGame();
    int nrLevels = game.numberOfLevels();
    setBounce(null);

    if (nrLevels == currentLevel) {
      int nrBlocks = numberOfBlocks();

      if (nrBlocks == 1) {
        BouncePoint bp = calculateBouncePointBlock(getBlock(0));
        setBounce(bp);

        return bp != null;
      }
    }

    return false;
  }

  // line 76 "../../../../../Block223States.ump"
   private boolean hitLastBlock(){
    int nrBlocks = numberOfBlocks();
    
    setBounce(null);

    if (nrBlocks == 1) {
      PlayedBlockAssignment block = getBlock(0);
      BouncePoint bp = calculateBouncePointBlock(block);

      setBounce(bp);
      
      return bp != null;
    }

    return false;
  }

  // line 93 "../../../../../Block223States.ump"
   private boolean hitBlock(){
    int nrBlocks = numberOfBlocks();
    
    setBounce(null);

    for (int index = 0; index <= nrBlocks - 1; index++) {
      PlayedBlockAssignment block = getBlock(index);

      BouncePoint bp = calculateBouncePointBlock(block);
      boolean closer = isCloser(bp, bounce);

      if (closer) {
      	setBounce(bp);
      }
    }

    return getBounce() != null;
  }

  // line 112 "../../../../../Block223States.ump"
   private boolean hitWall(){
    BouncePoint bp = calculateBouncePointWall();
  	setBounce(bp);
    return bp != null;
  }


  /**
   * Actions
   */
  // line 120 "../../../../../Block223States.ump"
   private void doSetup(){
    resetCurrentBallX();
	   resetCurrentBallY();
	   resetBallDirectionX();
	   resetBallDirectionY();
	   resetCurrentPaddleX();
	   getGame();
	   Level level = game.getLevel(currentLevel - 1);
	   List<BlockAssignment> assignments = level.getBlockAssignments();
	   for (BlockAssignment a : assignments) {
		   PlayedBlockAssignment pblock = new PlayedBlockAssignment(
				   Game.WALL_PADDING + (Block.SIZE + Game.COLUMNS_PADDING) * (a.getGridHorizontalPosition() - 1),
				   Game.WALL_PADDING + (Block.SIZE + Game.ROW_PADDING) * (a.getGridVerticalPosition() - 1),
				   a.getBlock(),
				   this
				   );
	   }
	   int x = 10 + 25 * (ThreadLocalRandom.current().nextInt(0, 14));
	   int y = 10 + 22 * (ThreadLocalRandom.current().nextInt(0, 14));
	   while (numberOfBlocks() < game.getNrBlocksPerLevel()) {
		   while (findPlayedBlockAssignment(x,y) != null) {
			   if (x < 360) {
				   x = x + 25;
			   } else {
				   x = 10;
				   y = y + 22;
				   if (y > 318) {
					y = 20;
				   }
			   }
		   } 
		   PlayedBlockAssignment pblock = new PlayedBlockAssignment(
				   x,
				   y,
				   game.getRandomBlock(),
				   this
				   );
	   }
  }

  // line 160 "../../../../../Block223States.ump"
   public PlayedBlockAssignment findPlayedBlockAssignment(int gridHorizontalPosition, int gridVerticalPosition){
    int h, v;
			for (PlayedBlockAssignment assignment: getBlocks()) {
				h = assignment.getX();
				v = assignment.getY();
				if (h == gridHorizontalPosition && v == gridVerticalPosition) {
					return assignment;
				}
			}
			return null;
  }

  // line 173 "../../../../../Block223States.ump"
   private void doHitPaddleOrWall(){
    bounceBall();
  }

  // line 177 "../../../../../Block223States.ump"
   private void doOutOfBounds(){
    setLives(lives - 1);
    resetCurrentBallX();
    resetCurrentBallY();
    resetBallDirectionX();
    resetBallDirectionY();
    resetCurrentPaddleX();
  }

  // line 186 "../../../../../Block223States.ump"
   private void doHitBlock(){
    int score = getScore();
    BouncePoint bounce = getBounce();
    PlayedBlockAssignment pblock = bounce.getHitBlock();
    Block block = pblock.getBlock();
    int points = block.getPoints();

    setScore(score + points);
    pblock.delete();
    bounceBall();
  }

  // line 198 "../../../../../Block223States.ump"
   private void doHitBlockNextLevel(){
    doHitBlock();

    int level = getCurrentLevel();

    setCurrentLevel(level + 1);

    setCurrentPaddleLength(
      getGame().getPaddle().getMaxPaddleLength()
        - (getGame().getPaddle().getMaxPaddleLength()
          - getGame().getPaddle().getMinPaddleLength())
        / (getGame().numberOfLevels() - 1)
        * (getCurrentLevel() - 1)
      );

    setWaitTime(
      INITIAL_WAIT_TIME
      * Math.pow(
          getGame().getBall().getBallSpeedIncreaseFactor(),
          (getCurrentLevel() - 1)
        )
    );
  }

  // line 222 "../../../../../Block223States.ump"
   private void doHitNothingAndNotOutOfBounds(){
    // TODO implement
    double x, y, dx, dy;
   x = getCurrentBallX();
   y = getCurrentBallY();
   dx = getBallDirectionX();
   dy = getBallDirectionY();
   this.setCurrentBallX(x+dx);
   this.setCurrentBallY(y+dy);
  }

  // line 233 "../../../../../Block223States.ump"
   private void doGameOver(){
    block223 = getBlock223();
    Player p;
    p = getPlayer();
    if(p != null) {
    	HallOfFameEntry hof;
    	game = getGame();
    	hof = new HallOfFameEntry(score, playername, p, game, block223);
    	game.setMostRecentEntry(hof);
    	// delete();
    }
  }


  /**
   * Helper Methods
   * The ball bounce on a paddle --------------------------1---------------------------
   */
  // line 250 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddle(){
    int ballRadius = Ball.BALL_DIAMETER / 2;
    double paddleX = currentPaddleX;
    double paddleY = currentPaddleY;
    double paddleLength = currentPaddleLength;
    double ballX = currentBallX;
    double ballY = currentBallY;

  	Rectangle2D paddleBox = new Rectangle2D.Double(
      paddleX - ballRadius,
      paddleY - ballRadius,
      paddleLength + 2 * ballRadius,
      Paddle.PADDLE_WIDTH + ballRadius
    );
  	Line2D ballSegment = new Line2D.Double(
      ballX,
      ballY,
      ballX + ballDirectionX,
      ballY + ballDirectionY
    );
    
    //check if ball segment touches the paddle box
    if (paddleBox.intersectsLine(ballSegment)) {
    	Line2D topLine = new Line2D.Double(
    		paddleX,
    		paddleY - ballRadius,
    		paddleX + paddleLength,
    		paddleY - ballRadius
    	);
    	Line2D leftLine = new Line2D.Double(
    		paddleX - ballRadius,
    		paddleY,
    		paddleX - ballRadius,
    		paddleY + ballRadius
    	);
    	Line2D rightLine = new Line2D.Double(
    		paddleX + paddleLength + ballRadius,
    		paddleY,
    		paddleX + paddleLength + ballRadius,
    		paddleY + ballRadius
    	);
      
      Line2D[] lines = {topLine, leftLine, rightLine};
      Point2D closestIntersection = null;
      BouncePoint.BounceDirection bounceDirection = BouncePoint.BounceDirection.FLIP_Y;

      for (int i = 0; i < 3; i++) {
        Point2D intersection = findIntersection(lines[i], ballSegment);

        if (
          intersection != null
          && (
            closestIntersection == null
            || intersection.distance(ballX, ballY)
              < closestIntersection.distance(ballX, ballY)
          )
        ) {
          closestIntersection = intersection;
          if (lines[i] != topLine) {
            bounceDirection = BouncePoint.BounceDirection.FLIP_X;
          }
        }
      }
      
      // Get Point on Arc
      Point2D topLeftCircleCenter = new Point2D.Double(paddleX, paddleY);
      Point2D topRightCircleCenter = new Point2D.Double(paddleX + paddleLength, paddleY);
      Point2D[] circleCenters = {
        topLeftCircleCenter, 
        topRightCircleCenter, 
      };

      for (int i = 0; i < 2; i++) {
        int quadrant = 4;
        if (i == 1) {
          quadrant = 1;
        }

        Point2D intersection = findIntersection(circleCenters[i], ballSegment, quadrant);

        if (
          intersection != null
          && (
            closestIntersection == null
            || ballSegment.ptSegDist(intersection)
              < ballSegment.ptSegDist(closestIntersection)
          )
        ) {
          closestIntersection = intersection;
          if (ballSegment.getX2() - ballSegment.getX1() == 0) { // straight vertical
            bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
          } else if (ballSegment.getX2() - ballSegment.getX1() > 0) { // from the left
            if (circleCenters[i] == topLeftCircleCenter) {
              bounceDirection = BouncePoint.BounceDirection.FLIP_X;
            } else {
              bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
            }
          } else {
            if (circleCenters[i] == topLeftCircleCenter) {
              bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
            } else {
              bounceDirection = BouncePoint.BounceDirection.FLIP_X;
            }
          }
        }
      }

      if (closestIntersection == null) {
        return null;
      }

      // Return a BouncePoint
      return new BouncePoint(
        closestIntersection.getX(), closestIntersection.getY(), bounceDirection
      );
    } else {
      return null;
    }
  }


  /**
   * ------------------bounceBall() Method that update the ball's direction and position-------------
   */
  // line 372 "../../../../../Block223States.ump"
   public void bounceBall(){
    if (bounce.getDirection() == BouncePoint.BounceDirection.FLIP_X){
  		double newBallDirectionX = -ballDirectionX;
  		double incomeDistanceX = bounce.getX() - currentBallX;
  		double outgoingDistanceX = ballDirectionX - incomeDistanceX;
  		/*
  		if (outgoingDistanceX == 0){
  			currentBallX = bounce.getX();
  			currentBallY = bounce.getY();
  			return; 
  		}
  		*/
  		currentBallX = bounce.getX() + outgoingDistanceX / ballDirectionX * newBallDirectionX;
  		
  		double sign = 0;
  		if (ballDirectionY == 0){
  			sign = 1;
  			} else {
  			sign = Math.signum(ballDirectionY);
  		}
  		double newBallDirectionY = ballDirectionY + sign*0.1*Math.abs(ballDirectionX);
  		currentBallY = bounce.getY() + outgoingDistanceX/ballDirectionX*newBallDirectionY;
  		ballDirectionY = newBallDirectionY;
  		ballDirectionX = newBallDirectionX;
  	}
  	else if (bounce.getDirection() == BouncePoint.BounceDirection.FLIP_Y){
  		double incomingDistanceY = bounce.getY() - currentBallY;
  		double outgoingDistanceY = ballDirectionY - incomingDistanceY;
  		double newBallDirectionY = -ballDirectionY;

  		/*
  		if (outgoingDistanceY == 0){
  			currentBallX = bounce.getX();
  			currentBallY = bounce.getY();
  			return; 
  		}
  		*/
  		currentBallY = bounce.getY() + outgoingDistanceY / ballDirectionY * newBallDirectionY;
  		
  		double sign = 0;
  		if (ballDirectionX ==0){
  			sign = 1;
  		} else {
  			sign = Math.signum(ballDirectionX);
  		}
  		double newBallDirectionX = ballDirectionX + sign*0.1*Math.abs(ballDirectionY);
  		currentBallX = bounce.getX() + outgoingDistanceY/ballDirectionY*newBallDirectionX;
  		ballDirectionX = newBallDirectionX;
  		ballDirectionY = newBallDirectionY;
  	}
  	
  	
  	else if (bounce.getDirection() == BouncePoint.BounceDirection.FLIP_BOTH){
  		double newBallDirectionX = -ballDirectionX;
  		double newBallDirectionY = -ballDirectionY;
  		
  		double incomingDistanceY = bounce.getY() - currentBallY;
  		double outgoingDistanceY = ballDirectionY - incomingDistanceY;
  		double incomingDistanceX = bounce.getX() - currentBallX;
  		double outgoingDistanceX = ballDirectionX - incomingDistanceX;
  		
  		/*
  		if(outgoingDistanceX == 0 && outgoingDistanceY ==0){
  			currentBallX = bounce.getX();
  			currentBallY = bounce.getY();
  			return;
  		}
  		*/
  		currentBallY = bounce.getY() + outgoingDistanceY/ballDirectionY * newBallDirectionY;
  		ballDirectionY = newBallDirectionY;
  		
  		currentBallX = bounce.getX() + outgoingDistanceX/ballDirectionX * newBallDirectionX;
  		ballDirectionX = newBallDirectionX;
  	}
  }


  /**
   * -----------------bounceWall---------------------------------------------------------------------
   */
  // line 450 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointWall(){
    int ballRadius = Ball.BALL_DIAMETER / 2;
    Line2D ballSegment = new Line2D.Double(
      currentBallX,
      currentBallY,
      currentBallX + ballDirectionX,
      currentBallY + ballDirectionY
    );

    // Hits a Corner
    Point2D hitCorner = null;
    Point2D leftCorner = new Point2D.Double(ballRadius, ballRadius);
    Point2D rightCorner = new Point2D.Double(390 - ballRadius, ballRadius);
    if (isPointInLine(ballSegment, leftCorner)) {
      hitCorner = leftCorner;
    } else if (isPointInLine(ballSegment, rightCorner)) {
      hitCorner = rightCorner;
    }

    if (hitCorner != null) {
      return new BouncePoint(
        hitCorner.getX(), hitCorner.getY(), BouncePoint.BounceDirection.FLIP_BOTH
      );
    }

    // Hits a Wall Side
    Line2D topWall = new Line2D.Double(
      ballRadius,
      ballRadius,
      Game.PLAY_AREA_SIDE - ballRadius,
      ballRadius
    );
    Line2D leftWall = new Line2D.Double(
      ballRadius,
      ballRadius,
      ballRadius,
      Game.PLAY_AREA_SIDE - ballRadius
    );
    Line2D rightWall = new Line2D.Double(
      Game.PLAY_AREA_SIDE - ballRadius,
      ballRadius,
      Game.PLAY_AREA_SIDE - ballRadius,
      Game.PLAY_AREA_SIDE - ballRadius
    );
    Line2D[] lines = {topWall, leftWall, rightWall};

    for (int i = 0; i < 3; i++) {
      Point2D point = findIntersection(lines[i], ballSegment);

      if (point != null) {
        BouncePoint.BounceDirection bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
        if (lines[i] != topWall) {
          bounceDirection = BouncePoint.BounceDirection.FLIP_X;
        }
        
        return new BouncePoint(
          point.getX(), point.getY(), bounceDirection
        );
      }
    }
    
    return null;
  }


  /**
   * ---------------------------------------------------
   */
  // line 515 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointBlock(PlayedBlockAssignment block){
    int ballRadius = Ball.BALL_DIAMETER / 2;
    int blockX = block.getX();
    int blockY = block.getY();

    // Get Point on Line
    Rectangle2D fullBox = new Rectangle2D.Double(
      blockX - ballRadius,
      blockY - ballRadius,
      Block.SIZE + 2 * ballRadius,
      Block.SIZE + 2 * ballRadius
    );

    Line2D ballSegment = new Line2D.Double(
      currentBallX,
      currentBallY,
      currentBallX + ballDirectionX,
      currentBallY + ballDirectionY
    );

    if (fullBox.intersectsLine(ballSegment)) {
      Line2D topLine = new Line2D.Double(
        blockX,
        blockY - ballRadius,
        blockX + Block.SIZE,
        blockY - ballRadius
      );
      Line2D leftLine = new Line2D.Double(
        blockX - ballRadius,
        blockY,
        blockX - ballRadius,
        blockY + Block.SIZE
      );
      Line2D rightLine = new Line2D.Double(
        blockX + Block.SIZE + ballRadius,
        blockY,
        blockX + Block.SIZE + ballRadius,
        blockY + Block.SIZE
      );
      Line2D bottomLine = new Line2D.Double(
        blockX,
        blockY + Block.SIZE + ballRadius,
        blockX + Block.SIZE,
        blockY + Block.SIZE + ballRadius
      );
      Line2D[] lines = {topLine, leftLine, rightLine, bottomLine};
      Point2D closestIntersection = null;
      BouncePoint.BounceDirection bounceDirection = null;

      for (int i = 0; i < 4; i++) {
        Point2D intersection = findIntersection(lines[i], ballSegment);

        if (
          intersection != null
          && (
            closestIntersection == null
            || ballSegment.ptSegDist(intersection)
              < ballSegment.ptSegDist(closestIntersection)
          )
        ) {
          closestIntersection = intersection;
          if (lines[i] == topLine || lines[i] == bottomLine) {
            bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
          } else {
            bounceDirection = BouncePoint.BounceDirection.FLIP_X;
          }
        }
      }
      
      // Get Point on Arc
      Point2D topLeftCircleCenter = new Point2D.Double(blockX, blockY);
      Point2D topRightCircleCenter = new Point2D.Double(blockX + Block.SIZE, blockY);
      Point2D bottomLeftCircleCenter = new Point2D.Double(blockX, blockY + Block.SIZE);
      Point2D bottomRightCircleCenter = new Point2D.Double(blockX + Block.SIZE, blockY + Block.SIZE);
      Point2D[] circleCenters = {
        topRightCircleCenter, 
        bottomRightCircleCenter,
        bottomLeftCircleCenter, 
        topLeftCircleCenter, 
      };

      for (int i = 0; i < 4; i++) {
        Point2D intersection = findIntersection(circleCenters[i], ballSegment, i + 1);

        if (
          intersection != null
          && (
            closestIntersection == null
            || ballSegment.ptSegDist(intersection)
              < ballSegment.ptSegDist(closestIntersection)
          )
        ) {
          closestIntersection = intersection;
          if (ballSegment.getX2() - ballSegment.getX1() == 0) { // straight vertical
            bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
          } else if (ballSegment.getX2() - ballSegment.getX1() > 0) { // from the left
            if (i >= 2) { // 0 or 2 (top left or bottom left)
              bounceDirection = BouncePoint.BounceDirection.FLIP_X;
            } else {
              bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
            }
          } else {
            if (i >= 2) {
              bounceDirection = BouncePoint.BounceDirection.FLIP_Y;
            } else {
              bounceDirection = BouncePoint.BounceDirection.FLIP_X;
            }
          }
        }
      }

      // Return a BouncePoint
      if (bounceDirection == null) {
      	return null;
      } else {
	      BouncePoint bp = new BouncePoint(
	        closestIntersection.getX(), closestIntersection.getY(), bounceDirection
	      );
	      bp.setHitBlock(block);
	      return bp;
      }
    } else {
      return null;
    }
  }


  /**
   * From https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Java
   * Modified it to return null if the lines are parallel or the intersection is not within the segments
   */
  // line 643 "../../../../../Block223States.ump"
   private Point2D findIntersection(Line2D l1, Line2D l2){
    double a1 = l1.getY2() - l1.getY1();
      double b1 = l1.getX1() - l1.getX2();
      double c1 = a1 * l1.getX1() + b1 * l1.getY1();

      double a2 = l2.getY2() - l2.getY1();
      double b2 = l2.getX1() - l2.getX2();
      double c2 = a2 * l2.getX1() + b2 * l2.getY1();

      double delta = a1 * b2 - a2 * b1;

      if (delta == 0) {
        return null;
      } else {
        Point2D intersection = new Point2D.Double(
          (b2 * c1 - b1 * c2) / delta,
          (a1 * c2 - a2 * c1) / delta
        );

        if (isPointInLine(l1, intersection) && isPointInLine(l2, intersection)) {
          return intersection;
        } else {
          return null;
        }
      }
  }

  // line 670 "../../../../../Block223States.ump"
   private static  boolean isPointInLine(Line2D line, Point2D pt){
    double x = pt.getX();
	   double y = pt.getY();
	   double x1 = line.getX1();
	   double y1 = line.getY1();
	   double x2 = line.getX2();
	   double y2 = line.getY2();
	   
	   // if (x1, y1) to (x, y) is vertical
	   if (x1 == x) {
		   return x2 == x
		   && ((y >= y1 && y <= y2) || (y <= y1 && y >= y2));
	   }
	   // if (x, y) to (x2, y2) is horizontal
	   if (y1 == y) {
		   return y2 == y
		   && ((x >= x1 && x <= x2) || (x <= x1 && x >= x2));
	   }
	   
	   double deltaX1 = x - x1;
	   double deltaY1 = y - y1;
	   double deltaX2 = x2 - x;
	   double deltaY2 = y2 - y;
	   //double dotProduct = deltaX1 * deltaX2 + deltaY1 * deltaY2;
	   
	   // the check
return Math.abs(deltaX1 * deltaY2 - deltaX2 * deltaY1) < 0.001
			   && ((x >= x1 - 0.0001 && x <= x2 + 0.0001) || (x <= x1 + 0.0001 && x >= x2 - 0.0001))
			   && ((y >= y1 - 0.0001 && y <= y2 + 0.0001) || (y <= y1 + 0.0001 && y >= y2 - 0.0001));
			   /*
	   && dotProduct >= 0
	   && dotProduct < Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
	   */
  }

  // line 712 "../../../../../Block223States.ump"
   private static  boolean isPointInLine(double x1, double y1, double x2, double y2, double ptX, double ptY){
    return isPointInLine(
			   new Line2D.Double(x1, y2, x2, y2),
			   new Point2D.Double(ptX, ptY)
		);
  }

  // line 719 "../../../../../Block223States.ump"
   private Point2D findIntersection(Point2D circleCenter, Line2D line, int quadrant){
    // Set circleCenter to (0, 0), make the line relative to that
    double x1 = line.getX1() - circleCenter.getX();
    double y1 = line.getY1() - circleCenter.getY();
    double x2 = line.getX2() - circleCenter.getX();
    double y2 = line.getY2() - circleCenter.getY();

    // http://mathworld.wolfram.com/Circle-LineIntersection.html
    double dx = x2 - x1;
    double dy = y2 - y1;
    double dr = Math.sqrt(dx * dx + dy * dy);
    double det = x1 * y2 - x2 * y1;
    double discriminant = Math.pow(Ball.BALL_DIAMETER / 2, 2) * dr * dr - det * det;
    
    if (discriminant < 0) { // no intersection
      return null;
    } else {
      double closerX;
      double closerY;

      if (discriminant == 0) {
        closerX = (det * dy) / (dr * dr);
        closerY = (-1 * det * dx) / (dr * dr);
      } else {
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double xA = (det * dy + Math.signum(dy) * dx * sqrtDiscriminant) / (dr * dr);
        double yA = (-1 * det * dx + Math.abs(dy) * sqrtDiscriminant) / (dr * dr);
        double xB = (det * dy - Math.signum(dy) * dx * sqrtDiscriminant) / (dr * dr);
        double yB = (-1 * det * dx - Math.abs(dy) * sqrtDiscriminant) / (dr * dr);

        double distBallToA = Point2D.distance(x1, y1, xA, yA);
        double distBallToB = Point2D.distance(x1, y1, xB, yB);

        if (distBallToA <= distBallToB) { // == should never happen
          closerX = xA;
          closerY = yA;
        } else {
          closerX = xB;
          closerY = yB;
        }
      }

      if (
        ((quadrant == 1 && closerX > 0 && closerY < 0)
        || (quadrant == 2 && closerX > 0 && closerY > 0)
        || (quadrant == 3 && closerX < 0 && closerY > 0)
        || (quadrant == 4 && closerX < 0 && closerY < 0))
        && isPointInLine(x1, y1, x2, y2, closerX, closerY)
      ) {
        // relative point -> actual point
        return new Point2D.Double(closerX + circleCenter.getX(), closerY + circleCenter.getY());
      } else {
        return null;
      }
    }
  }

  // line 776 "../../../../../Block223States.ump"
   private boolean isCloser(BouncePoint first, BouncePoint second){
    if (first == null) {
      return false;
    } else if (second == null) {
      return true;
    } else {
      double firstDistance = Point2D.distance(
        currentBallX, currentBallY, first.getX(), first.getY()
      );
      double secondDistance = Point2D.distance(
        currentBallX, currentBallY, second.getX(), second.getY()
      );

      return firstDistance < secondDistance;
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "score" + ":" + getScore()+ "," +
            "lives" + ":" + getLives()+ "," +
            "currentLevel" + ":" + getCurrentLevel()+ "," +
            "waitTime" + ":" + getWaitTime()+ "," +
            "playername" + ":" + getPlayername()+ "," +
            "ballDirectionX" + ":" + getBallDirectionX()+ "," +
            "ballDirectionY" + ":" + getBallDirectionY()+ "," +
            "currentBallX" + ":" + getCurrentBallX()+ "," +
            "currentBallY" + ":" + getCurrentBallY()+ "," +
            "currentPaddleLength" + ":" + getCurrentPaddleLength()+ "," +
            "currentPaddleX" + ":" + getCurrentPaddleX()+ "," +
            "currentPaddleY" + ":" + getCurrentPaddleY()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bounce = "+(getBounce()!=null?Integer.toHexString(System.identityHashCode(getBounce())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "block223 = "+(getBlock223()!=null?Integer.toHexString(System.identityHashCode(getBlock223())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 106 "../../../../../Block223Persistence.ump"
  private static final long serialVersionUID = 8597675110221231714L ;

  
}