package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

//import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.*;

/**
 * General behavior for sprites in the game.
 * 
 * Created by fgiorgetti on 5/15/15.
 */
public abstract class Sprite {

    protected int x;
	protected int y;

	protected int loadedId;
	protected int width = SpriteConstants.DEFAULT_WIDTH;
	protected int height = SpriteConstants.DEFAULT_HEIGHT;
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
		return getHeight() / 2 - getHeight() % 2;
	}
	public int getHalfWidth() {
		return getWidth() / 2 + getWidth() % 2;
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
	public void setHeight(int height) {
		this.height = height;
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
		return leftTopBlocked || leftBottomBlocked;

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
        return rightTopBlocked || rightBottomBlocked;

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
	    
	    return true;
	    
	}

	public void setX(int newX) {
    	
    	int oldX = this.x;

    	// Right most of screen
        if (isAtRightMost(newX)) {
            this.x = map.getCols() * map.getWidth() - getHalfWidth() - 1;
        } else if (isAtLeftMost(newX)) {
            this.x = getHalfWidth();
        } else {
            this.x = newX;
        }
        
        // If map not yet provided
        if ( this.map == null ) {
        	return;
        }
        
    }

	/**
	 * Returns true if the given X is at the left most side
	 * of the screen (beginning of map)
	 * @param x
	 * @return
	 */
	protected boolean isAtLeftMost(int x) {
		return x <= getHalfWidth();
	}

	/**
	 * Returns true if the given X is at the right most side
	 * of the screen (end of map).
	 * @param x
	 * @return
	 */
	protected boolean isAtRightMost(int x) {
		// Suppose width is 32, it goes from 0 to 31
		return map != null && x >= map.getCols() * map.getWidth() - getHalfWidth() - 1;
	}

	public void setY(int newY) {
    	
    	int oldY = this.y;
    	
        if ( map != null && newY > map.getRows() * map.getHeight() - getHalfHeight()) {
            this.y = map.getRows() * map.getHeight() - getHalfHeight();
        } else if ( newY < -getHeight() ) { // getHalfHeight() + 1) {
            this.y = -getHeight();
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

		int lx  = getX() - getHalfWidth();
		int rx  = getX() + getHalfWidth();
		int ty  = getY() + getHalfHeight();
		int by  = getY() - getHalfHeight();

		int mlx = map.getX();
		int mrx = map.getX() + GamePanel.WIDTH;
		int mty = map.getY() + GamePanel.HEIGHT;
		int mby = map.getY();

		/*
		 * Three possibilities for the sprite on each axis:
		 * - Partially in
		 * - Partially out
		 * - Throughout the screen
		 */
		boolean rightOn  = rx >= mlx && rx <= mrx;
		boolean leftOn   = lx >= mlx && lx <= mrx;
		boolean throughX = lx <= mlx && rx >= mrx;

		boolean topOn    = ty <= mty && ty >= mby;
		boolean bottomOn = by <= mty && by >= mby;
		boolean throughY = by <= mby && ty >= mty;

		return ( rightOn || leftOn || throughX ) &&
				( topOn || bottomOn || throughY );

	}

	public boolean hasPlayerCollision() {

		int lx = getX() - getHalfWidth();
		int rx = getX() + getHalfWidth();
		int ty = getY() + getHalfHeight();
		int by = getY() - getHalfHeight();

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

		boolean playerWithin = lx <= this.map.getPlayerLeftX() &&
				                 rx >= this.map.getPlayerRightX() &&
								(
								   ( ty >= this.map.getPlayerBottomY() && ty <= this.map.getPlayerTopY() ) //top left has collision
								|| ( by <= this.map.getPlayerTopY() && by >= this.map.getPlayerBottomY() ) // bottom left has collision
								|| ( ty >= this.map.getPlayerTopY() && by <= this.map.getPlayerBottomY() ) // player inside sprite on left
								|| ( by >= this.map.getPlayerBottomY() && ty <= this.map.getPlayerTopY() ) // sprite inside player on left
								);

		//System.out.printf("Player LeftX=%d|RightX=%d|TopY=%d|BottomY=%d\n", this.map.getPlayerLeftX(), this.map.getPlayerRightX(), this.map.getPlayerTopY(), this.map.getPlayerBottomY());
		//System.out.printf("Sprite LeftX=%d|RightX=%d|TopY=%d|BottomY=%d\n", lx, rx, ty, by);
		return leftCollision || rightCollision || playerWithin;

	}

	public void loadAnimation(Animation animation) {
		animations.put(animation.getId(), animation);
	}
	
	public void setAnimation(String animationId) {
		if ( currentAnimation == null || !currentAnimation.getId().equals(animationId) ) {
			//if ( currentAnimation != null ) {
			//	System.out.println("animation has changed. OLD = " + currentAnimation.getId() + " - NEW = " + animationId);
			//}
			currentAnimation = animations.get(animationId);
			currentAnimation.reset();
		}
	}
	
	public BufferedImage getCurrentAnimation() {
		return currentAnimation == null? null:currentAnimation.getAnimation();
	}

	public int getLoadedId() {
		return loadedId;
	}

	public void setLoadedId(int loadedId) {
		this.loadedId = loadedId;
	}

}
