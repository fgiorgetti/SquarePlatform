package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.FALL_SPEED;
import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.JUMP_SPEED;
import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.MAX_XSPEED;
import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.PLAYER_HEIGHT_UP;
import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.PLAYER_WIDTH;

import java.awt.*;

/**
 * General behavior for sprites in the game.
 * 
 * Created by fgiorgetti on 5/15/15.
 */
public abstract class Sprite {

    protected int x;
	protected int y;
	protected int xSpeed;
	protected int ySpeed = FALL_SPEED;
	protected int width = PLAYER_WIDTH;
	protected int height = PLAYER_HEIGHT_UP;
	protected SpriteDirection direction;
	protected TileMap map = null;
	protected SpriteState state;
	private Animation[] animations;
	
	public abstract void update(TileMap map);
    public abstract void draw(Graphics2D g);
    
	public int getTopY() {
		return GamePanel.HEIGHT-getY()-getHalfHeight()+map.getY();
	}
	public int getBottomY() {
		return getTopY() + getHeight();//-1;
	}
	public int getLeftX() {
		return getX()-getHalfWidth()-map.getX();
	}
	public int getRightX() {
		return getX() + getHalfWidth() - map.getX();
	}
	public int getHalfHeight() {
		return getHeight() / 2;
	}
	public int getHalfWidth() {
		return getWidth() / 2;
	}
	public int getX() {
	    return x;
	}
	public int getY() {
	    return y;
	}
	public int getXSpeed() {
	    return this.xSpeed;
	}
	public int getYSpeed() {
	    return this.ySpeed;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public SpriteDirection getDirection() {
		return direction;
	}
	public void setDirection(SpriteDirection direction) {
		this.direction = direction;
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
		
		if ( xSpeed > 0 ) {
			return 1;
		} else if ( xSpeed < 0 ) {
			return -1;
		}
		
		return 0;
		
	}
	
	/**
	 * Validates whether player is allowed to move left
	 * @return
	 */
	public boolean isBlockedLeft() {
	
	    // Checks if right corners of player are blocked
	    boolean leftTopBlocked    = map.getTile(map.getRowAt(getY()+getHalfHeight()), map.getColAt(getX()-getHalfWidth())).getType() == TileType.BLOCKED;
	    boolean leftBottomBlocked = map.getTile(map.getRowAt(getY()-getHalfHeight()), map.getColAt(getX()-getHalfWidth())).getType() == TileType.BLOCKED;
	    
	    // If tiles at left corners are not blocking tiles, then it is ok to move left
	    if ( !leftTopBlocked && !leftBottomBlocked ) {
	    	return false;
	    }
	    
	    // Right X for tile on left side
	    int tileRx = (map.getColAt(getX()-getHalfWidth())) * map.getWidth() + map.getWidth();// left side of tile on the right
	    
	    // Sets player X based on right side of blocking tile on the left
	    this.x = (tileRx + getHalfWidth());
	    
	    return true;
	    
	}
	
	/**
	 * Validates whether player is allowed to move right
	 * @return
	 */
	public boolean isBlockedRight() {
	
	    // Checks if right corners of player are blocked
	    boolean rightTopBlocked    = map.getTile(map.getRowAt(getY()+getHalfHeight()), map.getColAt(getX()+getHalfWidth())).getType() == TileType.BLOCKED;
	    boolean rightBottomBlocked = map.getTile(map.getRowAt(getY()-getHalfHeight()), map.getColAt(getX()+getHalfWidth())).getType() == TileType.BLOCKED;
	    
	    // If tiles at right corners are not blocking tiles, then it is ok to move right
	    if ( !rightTopBlocked && !rightBottomBlocked ) {
	    	return false;
	    }
	    
	    // Left X for tile on right side
	    int tileLx = (map.getColAt(getX()+getHalfWidth())) * map.getWidth()-1;// left side of tile on the right
	    
	    // Sets player X based on left side of blocking tile on the right
	    this.x = (tileLx - getHalfWidth());
	    
	    return true;
	    
	}
	/**
	 * Validates whether player is allowed to move up
	 * @return
	 */
	public boolean isBlockedTop() {
		
	    // Checks if top corners of player are blocked
	    boolean leftTopBlocked    = map.getTile(map.getRowAt(getY()+getHalfHeight()), map.getColAt(getX()-getHalfWidth())).getType() == TileType.BLOCKED;
	    boolean rightTopBlocked   = map.getTile(map.getRowAt(getY()+getHalfHeight()), map.getColAt(getX()+getHalfWidth())).getType() == TileType.BLOCKED;
	    
	    // If tiles at top corners are not blocking tiles, then it is ok to move up
	    if ( !leftTopBlocked && !rightTopBlocked ) {
	    	return false;
	    }
	    
	    // Bottom Y for tile on upper side
	    int tileBy = (map.getRowAt(getY() + getHalfHeight())) * map.getHeight() - map.getHeight() - 1;// bottom y of upper tile
	    
	    // Sets player Y based on bottom side of blocking tile on the top
	    this.y = (tileBy - getHalfHeight());
	    
	    return true;
	    
	}
	/**
	 * Validates whether player is allowed to move down
	 * @return
	 */
	public boolean isBlockedBottom() {
		
	    // Checks if bottom corners of player are blocked
	    boolean leftBottomBlocked    = map.getTile(map.getRowAt(getY()-getHalfHeight()), map.getColAt(getX()-getHalfWidth())).getType() == TileType.BLOCKED;
	    boolean rightBottomBlocked   = map.getTile(map.getRowAt(getY()-getHalfHeight()), map.getColAt(getX()+getHalfWidth())).getType() == TileType.BLOCKED;
	    
	    // If tiles at bottom corners are not blocking tiles, then it is ok to move down
	    if ( !leftBottomBlocked && !rightBottomBlocked ) {
	    	return false;
	    }
	    
	    // Top Y for tile on bottom side
	    int tileTy = (map.getRowAt(getY() - getHalfHeight())) * map.getHeight();// top y of bottom tile
	    
	    // Sets player Y based on bottom side of blocking tile on the top
	    this.y = (tileTy + getHalfHeight());
	    
	    return true;
	    
	}

	public void setX(int newX) {
    	
    	int oldX = this.x;
    	
        if ( map != null && newX > map.getCols() * map.getWidth() - getHalfWidth() - 1 ) {
            this.x = map.getCols() * map.getWidth() - getHalfWidth() - 1;
        } else if ( newX < getHalfWidth() ) {
            this.x = getHalfWidth();
        } else {
            this.x = newX;
        }
        
        // If map not yet provided
        if ( this.map == null ) {
        	return;
        }
        
        // Moving right
        if ( this.x > oldX ) {
        	isBlockedRight();
    	// Moving left
        } else if ( this.x < oldX ) {
        	isBlockedLeft();
        }
        
    }

    public void setY(int newY) {
    	
    	int oldY = this.y;
    	
        if ( map != null && newY > map.getRows() * map.getHeight() - getHalfHeight()) {
            this.y = map.getRows() * map.getHeight() - getHalfHeight();
        } else if ( newY < getHalfHeight() + 1) {
            this.y = getHalfHeight() + 1;
        } else {
            this.y = newY;
        }
        
        if ( this.map == null ) {
        	return;
        }
                
    }
	public void setXSpeed(int xSpeed) {
	    this.xSpeed += xSpeed;
	    if ( xSpeed > MAX_XSPEED )
	        this.xSpeed = MAX_XSPEED;
	    else if ( xSpeed < -MAX_XSPEED )
	        this.xSpeed = -MAX_XSPEED;
	}
	public void setYSpeed(int ySpeed) {
		
		if ( ySpeed == 0 ) {
			this.ySpeed = 0;
		}
		
		if ( ( ySpeed > 0 ) 
				|| ( ySpeed < 0 ) ) {
			this.ySpeed += ySpeed;
		} else {
			this.ySpeed = 0;
		}
	    
	    if ( ySpeed > JUMP_SPEED )
	        ySpeed = JUMP_SPEED;
	    else if ( ySpeed < FALL_SPEED )
	        ySpeed = FALL_SPEED;
	    
	}

}