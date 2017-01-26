package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.*;

import java.awt.*;

/**
 * Defines the player customizations over the Sprite base class.
 * Provides methods to be called by LevelStateManager, so it can interact
 * with player.
 * 
 * Created by fgiorgetti on 7/30/15.
 */
public class Player extends Sprite {

	private long accelerationStarted, deaccelerationStarted, jumpingStarted;
    private boolean jumping; // Set to true while key is pressed
    
    
    // DONE -> Handle acceleration and deacceleration X
    // DONE -> Added player height and width
    // DONE -> Handle movement from player class, not level state...
    // DONE -> Collision as player moves
    // DONE -> Block if in contact with Block tile
    // DONE -> Gravity (can be improved)
    // DONE -> Jumping (can be improved - acceleration)
    // Crouching
    
    @Override
    public void update(TileMap map) {

        this.map = map;
        
        setX(getX() + getXSpeed());
        setY(getY() + getYSpeed());

        //System.out.printf("Player X / Y = %d / %d\n", getPlayerX(), getPlayerY());
    }

    @Override
    public void draw(Graphics2D g) {

    	// Left line on player
        g.setColor(isBlockedLeft()? Color.RED : Color.BLACK);
        g.drawLine(getLeftX(), getTopY(), getLeftX(), getBottomY());

        // Right line
        g.setColor(isBlockedRight()? Color.RED : Color.BLACK);
        g.drawLine(getRightX(), getTopY(), getRightX(), getBottomY());
        
        // Top line
        g.setColor(isBlockedTop()? Color.RED : Color.BLACK);
        g.drawLine(getLeftX(), getTopY(), getRightX(), getTopY());

        // Bottom line
        g.setColor(isBlockedBottom()? Color.RED : Color.BLACK);
        g.drawLine(getLeftX(), getBottomY(), getRightX(), getBottomY());

        g.setColor(Color.GREEN);
        g.drawOval(getLeftX()-1, getTopY()-1, 1, 1);
        g.drawOval(getRightX()-1, getTopY()-1, 1, 1);
        g.drawOval(getLeftX()-1, getBottomY()-1, 1, 1);
        g.drawOval(getRightX()-1, getBottomY()-1, 1, 1);
        
    }

    @Override
    public void setY(int newY) {
    	
    	// Use the common logic
    	super.setY(newY);
    	
        if ( this.map == null ) {
        	return;
        }

    	// Player customization for Y
    	
        // Jumping
        //System.out.printf("YSpeed = %d - STATE = %s\n", this.ySpeed, this.state);
        if ( this.ySpeed > 0 ) {
        	
        	if ( isBlockedTop() ) {
        		setYSpeed(0); // stop jumping
        	}
        	        	
        	fall();
        	
        // Falling
        } else if ( this.ySpeed < 0 ) {
        	
        	if ( !isBlockedBottom() ) {
        		this.state = SpriteState.FALLING;
        		//System.out.println("I am falllingggggg !!!!!");
        	} else if ( this.state == SpriteState.FALLING ) {
        		this.state = SpriteState.IDLE;
        		this.ySpeed = FALL_SPEED;
        	}
        	
        } else if ( this.state == SpriteState.JUMPING ) {
    		fall();
        }
        
    }

    public SpriteState getState() {
		return state;
	}

	public void setState(SpriteState state) {
		this.state = state;
	}

	public void accelerate() {

		long elapsed = System.currentTimeMillis() - accelerationStarted;
		
		// If player inverted the direction, reset the speed to 0
		if ( this.xSpeed > 0 && this.direction == SpriteDirection.LEFT ) {
			this.xSpeed = 0;
		} else if ( this.xSpeed < 0 && this.direction == SpriteDirection.RIGHT ) {
			this.xSpeed = 0;
		}
		
		// Not enough time passed
		if ( elapsed < ACCELERATION_DELAY ) {
			return;
		}

		// Accelerate
		this.xSpeed += ACCELERATION_RATE * getDirectionFactor();
		if ( this.xSpeed * getDirectionFactor() > MAX_XSPEED ) {
			this.xSpeed = MAX_XSPEED * getDirectionFactor();
		}
		this.accelerationStarted = System.currentTimeMillis();
		
	}

	public void deaccelerate() {

		if ( this.xSpeed == 0 ) {
			return;
		}
		
		long elapsed = System.currentTimeMillis() - deaccelerationStarted;
		if ( elapsed < DEACCELERATION_DELAY ) {
			return;
		}
		
		this.xSpeed -= DEACCELERATION_RATE * getDeaccelerationFactor();
		if ( this.xSpeed * getDirectionFactor() <= 0 ) {
			this.xSpeed = 0;
		}
		this.deaccelerationStarted = System.currentTimeMillis();
		
	}
	
	public void crouch() {
		
		if ( this.height == PLAYER_HEIGHT_CROUCH ) {
			return;
		}
		
		this.height = PLAYER_HEIGHT_CROUCH;
		this.y -= PLAYER_HEIGHT_CROUCH / 2;
		this.state = SpriteState.CROUCHING;
		
	}
	
	public void standup() {

		if ( this.height == PLAYER_HEIGHT_UP ) {
			return;
		}

		this.height = PLAYER_HEIGHT_UP;
		this.y += PLAYER_HEIGHT_CROUCH / 2;
		this.state = SpriteState.IDLE;
		
	}
	
	public void jump() {
		
		if ( this.state == SpriteState.JUMPING || 
				this.state == SpriteState.FALLING ) {
			return;
		}
		
		setYSpeed(JUMP_SPEED);
		this.jumpingStarted = System.currentTimeMillis();
		this.state = SpriteState.JUMPING;
		this.jumping = true;
		
	}
	
	public void jumpReleased() {
		this.jumping = false;
	}
	
	public void fall() {
		
		long elapsed = System.currentTimeMillis() - jumpingStarted;
		if ( elapsed < FALL_DELAY ) {
			return;
		}
		
		setYSpeed(FALL_RATE);
		this.jumpingStarted = System.currentTimeMillis();
		
	}
	
	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

}
