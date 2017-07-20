package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

import static br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteConstants.*;

/**
 * Defines the player customizations over the Sprite base class.
 * Provides methods to be called by LevelStateManager, so it can interact
 * with player.
 * 
 * Created by fgiorgetti on 7/30/15.
 */
public class Player extends MovableSprite {

	private long accelerationStarted, deaccelerationStarted, jumpingStarted;
    private boolean jumping; // Set to true while key is pressed

	private int score;

    public Player() {

		loadAnimation(Animation.newAnimation(SpriteState.WALKING.name(),
				"/sprites/player/player_walkright.png",
				10).delay(100));
    	//loadAnimation(Animation.newAnimation(SpriteState.WALKING.name(),
    	//			"/sprites/player/blocky_walkright.png",
    	//			14).delay(100));

		loadAnimation(Animation.newAnimation(SpriteState.IDLE.name(),
				"/sprites/player/player_right.png",
				10).delay(500));
    	//loadAnimation(Animation.newAnimation(SpriteState.IDLE.name(),
		//		"/sprites/player/blocky_right.png",
		//		16).delay(500));

		loadAnimation(Animation.newAnimation(SpriteState.JUMPING.name(),
				"/sprites/player/player_jumping.png",
				10).delay(200).onlyOnce());
    	//loadAnimation(Animation.newAnimation(SpriteState.JUMPING.name(),
		//		"/sprites/player/blocky_right.png",
		//		16).delay(500));

    	loadAnimation(Animation.newAnimation(SpriteState.FALLING.name(),
				"/sprites/player/player_right.png",
				10).delay(500));

    	loadAnimation(Animation.newAnimation(SpriteState.CROUCHING.name(),
				"/sprites/player/player_crouching.png",
				10).delay(150).onlyOnce());

    	// Setting initial state
		setWidth(10);
		setHeight(20);
    	setDirection(SpriteDirection.RIGHT);
    	setState(SpriteState.IDLE);
    	
    }
    
    @Override
    public void update(TileMap map) {

        this.map = map;

        setX(getX() + getXSpeed());
        setY(getY() + getYSpeed());

        //System.out.printf("Player X / Y = %d / %d\n", getPlayerX(), getPlayerY());
    }

    @Override
    public void draw(Graphics2D g) {

		/*
    	drawPlayerBorders(g);
        g.setColor(Color.GREEN);
        g.drawString("Player State = " + getState(), 10, 10);
        */

        if ( getCurrentAnimation() != null ) {
        	if ( getDirection() == SpriteDirection.RIGHT ) {
        		g.drawImage(getCurrentAnimation(), getLeftX(), getTopY(), getWidth(), getHeight(), null);
        	} else {
        		g.drawImage(getCurrentAnimation(), getRightX(), getTopY(), -getWidth(), getHeight(), null);
        	}
        }
    }

    // Draw borders around player ( for debugging )
	private void drawPlayerBorders(Graphics2D g) {
		
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
        		setState(SpriteState.FALLING);
            	fall();
        		//System.out.println("I am falllingggggg !!!!!");
        	} else if ( this.state == SpriteState.FALLING ) {
        	    if ( this.getXSpeed() != 0 ) {
        	    	setState(SpriteState.WALKING);
				} else {
	        		setState(SpriteState.IDLE);
				}
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
    	if ( this.state == state ) {
    		return;
		}
		this.state = state;
		setAnimation(this.state.name());
	}

	public void accelerate() {

		long elapsed = System.currentTimeMillis() - accelerationStarted;
		
		// Not enough time passed
		if ( elapsed < ACCELERATION_DELAY ) {
			return;
		}

		// Accelerate
		this.xSpeed += ACCELERATION_RATE * getDirectionFactor();
		if ( this.xSpeed * getDirectionFactor() > MAX_XSPEED ) {
			this.xSpeed = MAX_XSPEED * getDirectionFactor();
		}
		
		if ( !isJumpingOrFalling() ) {
			if ( this.xSpeed != 0 ) {
				setState(SpriteState.WALKING);
				setAnimation(this.state.name());
			}
		}
		
		this.accelerationStarted = System.currentTimeMillis();
		
	}

	public void deaccelerate() {

		if ( this.xSpeed == 0 ) {
			if ( !isJumpingOrFalling() && !isCrouching() ) {
				setState(SpriteState.IDLE);
			}
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

		setState(SpriteState.CROUCHING);
		this.xSpeed = 0;

		if ( this.height == PLAYER_HEIGHT_CROUCH ) {
			return;
		}

		this.height = PLAYER_HEIGHT_CROUCH;
		this.y -= (PLAYER_HEIGHT_UP - PLAYER_HEIGHT_CROUCH);

	}
	
	public void standup() {

		if ( this.height == PLAYER_HEIGHT_UP ) {
			return;
		}

		this.height = PLAYER_HEIGHT_UP;
		this.y += PLAYER_HEIGHT_CROUCH / 2;
		setState(SpriteState.IDLE);
		
	}
	
	public void jump() {
		
		if ( this.state == SpriteState.JUMPING || 
				this.state == SpriteState.FALLING ) {
			return;
		}
		
		//System.out.println("Before jumping y speed: " + getYSpeed());
		setYSpeed(JUMP_SPEED);
		this.jumpingStarted = System.currentTimeMillis();
		setState(SpriteState.JUMPING);
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
		//System.out.println(getYSpeed());
		this.jumpingStarted = System.currentTimeMillis();
		
	}
	
	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isJumpingOrFalling() {
		return getState() == SpriteState.JUMPING
				|| getState() == SpriteState.FALLING;
	}

	public boolean isCrouching() {
		return getState() == SpriteState.CROUCHING;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int score) {
		this.score += score;
	}

	public void subScore(int score) {
        this.score -= score;
    }

}
