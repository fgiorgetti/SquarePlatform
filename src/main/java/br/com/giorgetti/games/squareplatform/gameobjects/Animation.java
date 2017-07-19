package br.com.giorgetti.games.squareplatform.gameobjects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Handles animation for Sprites that need it.
 * @author fgiorgetti
 *
 */
public class Animation {

	private static Map<String, BufferedImage> imagesMap = new HashMap<>();
	private String id;
	private BufferedImage[] images;
	private int currentImage;
	private int animationDelayMs;
	private long animationStarted;
	
	private boolean onceOnly;
	private boolean completedOnce;
	
	public Animation(String id, String resourceImagePath, int spriteWidth) {

		this.id = id;

        try {

            BufferedImage image = null;

            // Tries to load from the map
            synchronized ( imagesMap ) {
				if (imagesMap.containsKey(resourceImagePath)) {
					image = imagesMap.get(resourceImagePath);
				} else {
					image = ImageIO.read(this.getClass().getResourceAsStream(resourceImagePath));
					imagesMap.put(resourceImagePath, image);
				}
			}

            int numSprites = image.getWidth() / spriteWidth;
            
            images = new BufferedImage[numSprites];
            
            // Loading all scenes
            for ( int x = 0, y = 0; y < numSprites ; x += spriteWidth, y++ ) {
            	images[y] = image.getSubimage(x, 0, spriteWidth, image.getHeight());
            }
            
        } catch (IOException e) {
        	this.images = new BufferedImage[]{new BufferedImage(0,0,BufferedImage.TYPE_INT_RGB)};
        }

	}
	
	public String getId() {
		return this.id;
	}
	
	public boolean isAnimationDone() {
		return onceOnly && completedOnce;
	}

	public static Animation newAnimation(String id, String resourceImagePath, int spriteWidth) {
		return new Animation(id, resourceImagePath, spriteWidth);
	}
	
	public Animation onlyOnce() {
		this.onceOnly = true;
		return this;
	}
	
	public Animation delay(int delayMs) {
		this.animationDelayMs = delayMs;
		return this;
	}
	
	public void reset() {
		this.animationStarted = 0L;
		this.currentImage = 0;
		this.completedOnce = false;
	}
	
	public BufferedImage getAnimation() {
		
		if ( System.currentTimeMillis() - animationStarted < animationDelayMs ) {
			return images[currentImage];
		}
		
		animationStarted = System.currentTimeMillis();
		
		if ( currentImage == images.length-1 ) {
			completedOnce = true;
		}
		
		// If allowed to continue to first frame after completing
		if ( !isAnimationDone() ) {
			currentImage = ++currentImage >= images.length? 0:currentImage;
		}
		
		return images[currentImage];
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animation other = (Animation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
