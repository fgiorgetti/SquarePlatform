package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.*;

/**
 * General behavior for sprites in the game.
 * 
 * Created by fgiorgetti on 5/15/15.
 */
public abstract class Sprite {

    protected int x;
	protected int y;
	protected int width = PLAYER_WIDTH;
	protected int height = PLAYER_HEIGHT_UP;
	protected TileMap map = null;
	protected Animation currentAnimation;
	protected Map<String, Animation> animations = new HashMap<String, Animation>();

	protected SpriteState state;

	public void setMap(TileMap map) {
		this.map = map;
	}

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

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
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

	/**
	 * Returns true if Sprite is on screen
	 * @return
	 */
	public boolean isOnScreen() {
		return getRightX() >= map.getX() && getLeftX() <= map.getRightX()
				&& getTopY() >= map.getY() && getBottomY() <= map.getTopY();
	}

	public boolean hasPlayerCollision() {

		int lx = getX() - getHalfWidth();
		int rx = getX() + getHalfWidth();
		int ty = getY() + getHalfHeight();
		int by = getY() - getHalfHeight();

		//boolean playerLeftCollision = map.getPlayerX() - map.getpl
		boolean leftCollision = lx <= this.map.getPlayerRightX() &&
								lx >= this.map.getPlayerLeftX() &&
								(
								 	 ( ty >= this.map.getPlayerBottomY() && ty <= this.map.getPlayerTopY() ) //top left has collision
								  || ( by <= this.map.getPlayerTopY() && by >= this.map.getPlayerBottomY() ) // bottom left has collision
			  		              || ( ty >= this.map.getPlayerTopY() && by <= this.map.getPlayerBottomY() ) // player inside sprite on left
	                              || ( by >= this.map.getPlayerBottomY() && ty <= this.map.getPlayerTopY() ) // sprite inside player on left
								);

		boolean rightCollision = rx >= this.map.getPlayerLeftX() &&
				                 rx <= this.map.getPlayerRightX() &&
								(
								   ( ty >= this.map.getPlayerBottomY() && ty <= this.map.getPlayerTopY() ) //top left has collision
								|| ( by <= this.map.getPlayerTopY() && by >= this.map.getPlayerBottomY() ) // bottom left has collision
								|| ( ty >= this.map.getPlayerTopY() && by <= this.map.getPlayerBottomY() ) // player inside sprite on left
								|| ( by >= this.map.getPlayerBottomY() && ty <= this.map.getPlayerTopY() ) // sprite inside player on left
								);

		return leftCollision || rightCollision;

	}

	public void loadAnimation(Animation animation) {
		animations.put(animation.getId(), animation);
	}
	
	public void setAnimation(String animationId) {
		if ( currentAnimation == null || !currentAnimation.getId().equals(animationId) ) {
			currentAnimation = animations.get(animationId);
			currentAnimation.reset();
		}
	}
	
	public BufferedImage getCurrentAnimation() {
		return currentAnimation == null? null:currentAnimation.getAnimation();
	}
	
}
