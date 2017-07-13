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
public class EditorPlayer extends MovableSprite {

	@Override
	public void draw(Graphics2D g) {
	}

	@Override
	public void update(TileMap map) {
		this.map = map;
	}

	@Override
	public void setX(int newX) {
		int oldX = this.x;

		if ( map != null && newX > map.getCols() * map.getWidth() ) {
			this.x = map.getCols() * map.getWidth();
		} else if ( newX < getWidth() ) {
			this.x = getWidth();
		} else {
			this.x = newX;
		}

		// If map not yet provided
		if ( this.map == null ) {
			return;
		}

	}

	@Override
	public void setY(int newY) {
		int oldY = this.y;

		if ( map != null && newY > map.getRows() * map.getHeight()) {
			this.y = map.getRows() * map.getHeight();
		} else if ( newY < getHeight()) {
			this.y = getHeight();
		} else {
			this.y = newY;
		}

		if ( this.map == null ) {
			return;
		}

	}
}
