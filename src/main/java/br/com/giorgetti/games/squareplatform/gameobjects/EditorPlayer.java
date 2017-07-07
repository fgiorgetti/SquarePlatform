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
public class EditorPlayer extends Player {
	@Override
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

	}

	@Override
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
}
