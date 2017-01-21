package br.com.giorgetti.games.squareplatform.gameobjects;

import java.awt.image.BufferedImage;

/**
 * Draft for Animation
 * @author fgiorgetti
 *
 */
public class Animation {

	private BufferedImage[] images;
	private int currentImage;
	
	private boolean onceOnly;
	private boolean completedOnce;
	
	public boolean isAnimationDone() {
		return onceOnly && completedOnce;
	}
	
}
