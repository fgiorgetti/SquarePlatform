package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
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

	// Dimensions
	public static final int PLAYER_HEIGHT_UP = 20;
	public static final int PLAYER_HEIGHT_CROUCH = 12;
	public static final int PLAYER_WIDTH = 10;

	private long accelerationStarted, deaccelerationStarted;

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
		setWidth(PLAYER_WIDTH);
		setHeight(PLAYER_HEIGHT_UP);
    	setDirection(SpriteDirection.RIGHT);
    	setState(SpriteState.IDLE);
    	
    }
    
    @Override
    public void update(TileMap map) {
        this.map = map;
        setX(getX() + getXSpeed());
        setY(getY() + getYSpeed());
    }

    @Override
    public void draw(Graphics2D g) {

        if ( getCurrentAnimation() != null ) {
        	if ( getDirection() == SpriteDirection.RIGHT ) {
        		g.drawImage(getCurrentAnimation(), getLeftX(), getTopY(), getWidth(), getHeight(), null);
        	} else {
        		g.drawImage(getCurrentAnimation(), getRightX(), getTopY(), -getWidth(), getHeight(), null);
        	}
        }

        //g.setColor(Color.green);
		// Draw a line at top
		//g.drawLine(0, 0, GamePanel.WIDTH, 0);
		// Draw a line at bottom
		//g.drawLine(0, GamePanel.HEIGHT-1, GamePanel.WIDTH, GamePanel.HEIGHT-1);
		/*
		drawPlayerBorders(g);
		g.setColor(Color.GREEN);
		g.drawString("Player State = " + getState(), 10, 10);
        */
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

		if ( this.height == PLAYER_HEIGHT_CROUCH ) {
			return;
		}

		this.height = PLAYER_HEIGHT_CROUCH;
		this.y -= (PLAYER_HEIGHT_UP - PLAYER_HEIGHT_CROUCH);

		setState(SpriteState.CROUCHING);
		this.xSpeed = 0;

	}
	
	public void standup() {

		if ( this.height == PLAYER_HEIGHT_UP ) {
			return;
		}

		this.height = PLAYER_HEIGHT_UP;
		this.y += PLAYER_HEIGHT_CROUCH / 2;
		setState(SpriteState.IDLE);
		
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
