package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.player.PlayerState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;

import java.awt.*;

/**
 * Created by fgiorgetti on 7/30/15.
 */
public class Player extends Sprite {

    private static final int MAX_XSPEED = 8;
	private int playerX, playerY;
    private int playerXSpeed, playerYSpeed;
    private long accelerationStarted, deaccelerationStarted;
    private static int ACCELERATION_DELAY = 100, DEACCELERATION_DELAY = 50;
    private static int ACCELERATION_RATE = 2, DEACCELERATION_RATE = 4;
    
    private int playerWidth = 16, playerHeight = 20;

    private PlayerState state;
    private SpriteDirection direction;
    private Animation[] animations; // Animation indexed by state
    
    private TileMap map = null;
    
    // DONE -> Handle acceleration and deacceleration X
    // DONE -> Added player height and width
    // Handle movement from player class, not level state...
    // Collision as player moves
    // Block if in contact with Block tile
    // Gravity
    // Jumping
    // Crouching
    
    @Override
    public void update(TileMap map) {

        this.map = map;
        
        // If not blocked right and moving right or 
        // if not blocked left and moving left
        setPlayerX(getPlayerX() + getPlayerXSpeed());
        setPlayerY(getPlayerY() + getPlayerYSpeed());

    }

    @Override
    public void draw(Graphics2D g) {

    	// Left line on player
        g.setColor(isBlockedLeftTopCorner()||isBlockedLeftBottomCorner()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerLeftX(), getPlayerTopY(), getPlayerLeftX(), getPlayerBottomY());

        // Right line
        g.setColor(isBlockedRightTopCorner() || isBlockedRightBottomCorner()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerRightX(), getPlayerTopY(), getPlayerRightX(), getPlayerBottomY());
        
        // Top line
        //g.setColor(isBlockedTopLeftCorner() || isBlockedTopRightCorner()? Color.RED : Color.BLACK);
        g.setColor(isBlockedLeftTopCorner() || isBlockedRightTopCorner()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerLeftX(), getPlayerTopY(), getPlayerRightX(), getPlayerTopY());

        // Bottom line
        //g.setColor(isBlockedBottomLeftCorner() || isBlockedBottomRightCorner()? Color.RED : Color.BLACK);
        g.setColor(isBlockedLeftBottomCorner() || isBlockedRightBottomCorner()? Color.RED : Color.BLACK);
        g.drawLine(getPlayerLeftX(), getPlayerBottomY(), getPlayerRightX(), getPlayerBottomY());

        //g.drawRect(getPlayerLeftX(), getPlayerTopY(), getPlayerWidth(), getPlayerHeight());
        
        g.setColor(Color.GREEN);
        g.drawOval(getPlayerLeftX()-1, getPlayerTopY()-1, 1, 1);
        g.drawOval(getPlayerRightX()-1, getPlayerTopY()-1, 1, 1);
        g.drawOval(getPlayerLeftX()-1, getPlayerBottomY()-1, 1, 1);
        g.drawOval(getPlayerRightX()-1, getPlayerBottomY()-1, 1, 1);
    }

	public int getPlayerTopY() {
		return GamePanel.HEIGHT-getPlayerY()-getHalfPlayerHeight()+map.getY();
		//return getPlayerY()-getHalfPlayerHeight()-1;
	}

	public int getPlayerBottomY() {
		return getPlayerTopY() + getPlayerHeight()-1;
		//return getPlayerTopY() + getPlayerHeight() + 1;
	}

	public int getPlayerLeftX() {
		return getPlayerX()-getHalfPlayerWidth()-map.getX();
		//return getPlayerX()-getHalfPlayerWidth();
	}
	
	public int getPlayerRightX() {
		return getPlayerX() + getHalfPlayerWidth() - map.getX();
		//return getPlayerX() + getHalfPlayerWidth();
	}

    private int getHalfPlayerHeight() {
		return getPlayerHeight() / 2;
	}

	private int getHalfPlayerWidth() {
		return getPlayerWidth() / 2;
	}

	public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
    	
    	int oldPlayerX = this.playerX;
    	
        if ( map != null && playerX > map.getCols() * map.getWidth() - getHalfPlayerWidth() - 1 ) {
            this.playerX = map.getCols() * map.getWidth() - getHalfPlayerWidth() - 1;
        } else if ( playerX < getHalfPlayerWidth() ) {
            this.playerX = getHalfPlayerWidth();
        } else {
            this.playerX = playerX;
        }
        
        if ( !isAllowedToMoveX() ) {
        	System.out.println("not allowed to move");
        	this.playerX = oldPlayerX;
        }
        
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        if ( map != null && playerY > map.getRows() * map.getHeight() - getHalfPlayerHeight()) {
            this.playerY = map.getRows() * map.getHeight() - getHalfPlayerHeight();
        } else if ( playerY < getHalfPlayerHeight() + 1) {
            this.playerY = getHalfPlayerHeight() + 1;
        } else {
            this.playerY = playerY;
        }
    }

    public int getPlayerXSpeed() {
        return this.playerXSpeed;
    }
    public int getPlayerYSpeed() {
        return this.playerYSpeed;
    }
    public void setPlayerXSpeed(int xSpeed) {
        this.playerXSpeed += xSpeed;
        if ( playerXSpeed > MAX_XSPEED )
            this.playerXSpeed = MAX_XSPEED;
        else if ( playerXSpeed < -MAX_XSPEED )
            this.playerXSpeed = -MAX_XSPEED;
    }
    public void setPlayerYSpeed(int ySpeed) {
    	
    	if ( ySpeed == 0 ) {
    		this.playerYSpeed = 0;
    	}
    	
//    	if ( ( ySpeed > 0 && !isBlockedTopLeftCorner() && !isBlockedTopRightCorner() ) 
//    			|| ( ySpeed < 0 && !isBlockedBottomLeftCorner() && !isBlockedBottomRightCorner() ) ) {
    	if ( ( ySpeed > 0 && !isBlockedLeftTopCorner() && !isBlockedRightTopCorner() ) 
    			|| ( ySpeed < 0 && !isBlockedLeftBottomCorner() && !isBlockedRightBottomCorner() ) ) {
    		this.playerYSpeed = ySpeed;
    	} else {
    		this.playerYSpeed = 0;
    	}
        
        if ( playerYSpeed > 12 )
            playerYSpeed = 12;
        else if ( playerYSpeed < -12 )
            playerYSpeed = -12;
    }

	public int getPlayerWidth() {
		return playerWidth;
	}

	public void setPlayerWidth(int playerWidth) {
		this.playerWidth = playerWidth;
	}

	public int getPlayerHeight() {
		return playerHeight;
	}

	public void setPlayerHeight(int playerHeight) {
		this.playerHeight = playerHeight;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public SpriteDirection getDirection() {
		return direction;
	}

	public void setDirection(SpriteDirection direction) {
		this.direction = direction;
	}

	public void accelerate() {

		if ( !isAllowedToMoveX() ) {
			return;
		}
		
		long elapsed = System.currentTimeMillis() - accelerationStarted;
		
		// If player inverted the direction, reset the speed to 0
		if ( this.playerXSpeed > 0 && this.direction == SpriteDirection.LEFT ) {
			this.playerXSpeed = 0;
		} else if ( this.playerXSpeed < 0 && this.direction == SpriteDirection.RIGHT ) {
			this.playerXSpeed = 0;
		}
		
		// Increase speed now
		if ( elapsed > ACCELERATION_DELAY ) {
			this.playerXSpeed += ACCELERATION_RATE * getDirectionFactor();
			if ( this.playerXSpeed * getDirectionFactor() > MAX_XSPEED ) {
				this.playerXSpeed = MAX_XSPEED * getDirectionFactor();
			}
			this.accelerationStarted = System.currentTimeMillis();
		}
		
	}

	public boolean isAllowedToMoveX() {
		
		if ( map == null ) {
			return true;
		}
		
		// On the ground
//		boolean onTheGround = (!isBlockedBottomLeftCorner() || !isBlockedBottomRightCorner())
//							&& !isBlockedTopLeftCorner() && !isBlockedTopRightCorner();
//		
//		if ( onTheGround ) {
//			return true;
//		}
		
		// tells if player is allowed to proceed to the desired x direction
		boolean allowedToMoveX = false;
		
		if (  this.direction == SpriteDirection.LEFT ) {
			allowedToMoveX = !isBlockedLeftTopCorner() && !isBlockedLeftBottomCorner();
		} else {
			allowedToMoveX = !isBlockedRightTopCorner() && !isBlockedRightBottomCorner();
		}

		return allowedToMoveX;
	}
	
	public void deaccelerate() {

		if ( this.playerXSpeed == 0 ) {
			return;
		}
		
		long elapsed = System.currentTimeMillis() - deaccelerationStarted;
		if ( elapsed > DEACCELERATION_DELAY ) {
			this.playerXSpeed -= DEACCELERATION_RATE * getDeaccelerationFactor();
			if ( this.playerXSpeed * getDirectionFactor() <= 0 ) {
				this.playerXSpeed = 0;
			}
			this.deaccelerationStarted = System.currentTimeMillis();
		}
		
	}
	
	/**
	 * Returns 1 if going right or -1 if left.
	 * @return
	 */
	public int getDirectionFactor() {
		if ( direction == SpriteDirection.RIGHT ) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * If speed positive, returns 1 meaning going right.
	 * If negative, returns -1 meaning its going left.
	 * If zero, then return 0, meaning it is idle.
	 * 
	 * @return
	 */
	public int getDeaccelerationFactor() {
		
		if ( playerXSpeed > 0 ) {
			return 1;
		} else if ( playerXSpeed < 0 ) {
			return -1;
		}
		
		return 0;
		
	}

	private boolean isBlockedLeftTopCorner() {
	
		//System.out.printf("Player               row/col: %d/%d\n", map.getPlayerRow(), map.getPlayerCol());
		//System.out.printf("Player Top Left tile row/col: %d/%d\n", map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()+1), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth()-1));

		//System.out.println("Player row/col tile type   : " + map.getTile(map.getPlayerRow(), map.getPlayerCol()).getType());
		//System.out.println("Top left corner tile type  : " + map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()+1), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth()-1)).getType());
		
		// Draw tile on left to check for collision
        boolean leftTopBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        int leftX = (map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())-1) * map.getWidth() - map.getX() + map.getWidth(); // right side of the left tile

        //System.out.printf("Left top blocked            : %s\n", leftTopBlocked);
        //System.out.printf("Player LeftX / LeftX        : %d/%d\n", getPlayerX()-getHalfPlayerWidth(), leftX);
        
        //return false;
//        return leftTopBlocked && getPlayerX()-getHalfPlayerWidth() <= leftX;
        return leftTopBlocked;
        
	}

	private boolean isBlockedLeftBottomCorner() {
		
        // Draw tile on left to check for collision
        boolean leftBottomBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        int bottomY = GamePanel.HEIGHT - (map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()) * map.getHeight()) + map.getY() + 5; // top of bottom left tile

        return leftBottomBlocked && getPlayerBottomY() >= bottomY;
        
        //return leftBottomBlocked;
	}
	

	private boolean isBlockedRightTopCorner() {

        // Draw tile on right to check for collision
        boolean rightTopBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        //int rightX = (map.getPlayerColAt(getPlayerRightX())+1) * map.getWidth() - map.getX(); // left side of right tile

        //return rightTopBlocked && getPlayerRightX() >= rightX - 1;
        
        return rightTopBlocked;
	}
	
	private boolean isBlockedRightBottomCorner() {

        // Draw tile on right to check for collision
        boolean rightBottomBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        int bottomY = GamePanel.HEIGHT - (map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()) * map.getHeight()) + map.getY() + 5; // top of bottom left tile
        //int rightX = (map.getPlayerColAt(getPlayerRightX())+1) * map.getWidth() - map.getX(); // left side of right tile

        System.out.println(getPlayerBottomY() + " / " + bottomY);
        return rightBottomBlocked && getPlayerBottomY() >= bottomY;
        //return rightBottomBlocked;
        
	}
	/*
	private boolean isBlockedTopLeftCorner() {
		
        // Draw tile on top to check for collision
        boolean topLeftBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        //int topY = GamePanel.HEIGHT - (map.getPlayerRowAt(getPlayerTopY()) + 1) * map.getHeight() + map.getY() + map.getHeight(); // bottom point of top tile on left corner

        //return topLeftBlocked && getPlayerTopY() <= topY + 1;
        return topLeftBlocked;
	}
	
	private boolean isBlockedTopRightCorner() {
		
        // Draw tile on top to check for collision
        boolean topRightBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()+getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        //int topY = GamePanel.HEIGHT - (map.getPlayerRowAt(getPlayerTopY()) + 1) * map.getHeight() + map.getY() + map.getHeight(); // bottom point of top tile on left corner

        //return topRightBlocked && getPlayerTopY() <= topY + 1;
        return topRightBlocked;
	}
	
	private boolean isBlockedBottomLeftCorner() {
		
        // Draw tile on bottom to check for collision
        boolean bottomLeftBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()-getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        int bottomY = GamePanel.HEIGHT - (map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()) * map.getHeight()) + map.getY() + 2; // top of bottom left tile

        System.out.printf("Player Y = %d / Bottom Y = %d\n", getPlayerBottomY(), bottomY);
        return bottomLeftBlocked && getPlayerBottomY() >= bottomY + 2;
        //return bottomLeftBlocked;
	}

	private boolean isBlockedBottomRightCorner() {
		
        // Draw tile on bottom to check for collision
        boolean bottomRightBlocked = map.getTile(map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()), map.getPlayerColAt(getPlayerX()+getHalfPlayerWidth())).getType() == TileType.BLOCKED;
        int bottomY = GamePanel.HEIGHT - (map.getPlayerRowAt(getPlayerY()-getHalfPlayerHeight()) * map.getHeight()) + map.getY() + 2; // top of bottom tile

        return bottomRightBlocked && getPlayerBottomY() >= bottomY + 2;
        //return bottomRightBlocked;
	}
*/
}
