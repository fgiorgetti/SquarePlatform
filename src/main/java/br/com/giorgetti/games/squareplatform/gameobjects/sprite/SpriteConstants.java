package br.com.giorgetti.games.squareplatform.gameobjects.sprite;

/**
 * Constants for Sprite movements.
 * 
 * @author fgiorgetti
 *
 */
public class SpriteConstants {

	// Movement ( left, right)
    public static final int MAX_XSPEED = 3;
    public static int ACCELERATION_DELAY = 300;
    public static int DEACCELERATION_DELAY = 300;
    public static int ACCELERATION_RATE = 1;
    public static int DEACCELERATION_RATE = 1;
    public static int DEACCELERATION_RATE_SNOW = 1; // A value of 1 would be nice for a snow stage
    
    // Falling
    public static final int FALL_SPEED = -10;
    public static int FALL_RATE = -1;
    public static int FALL_DELAY = 50;
    
    // Jumping
    public static final int JUMP_SPEED = 17;
    
	// Dimensions
	public static final int PLAYER_HEIGHT_UP = 20;
    public static final int PLAYER_HEIGHT_CROUCH = 12;
    public static final int PLAYER_WIDTH = 10;

}
