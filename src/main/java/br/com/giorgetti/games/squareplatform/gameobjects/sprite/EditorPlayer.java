package br.com.giorgetti.games.squareplatform.gameobjects.sprite;

import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Defines the player customizations over the Sprite base class.
 * Provides methods to be called by LevelStateManager, so it can interact
 * with player.
 * 
 * Created by fgiorgetti on 7/30/15.
 */
public class EditorPlayer extends Sprite {

	public EditorPlayer() {

	}

	@Override
	public void draw(Graphics2D g) {
	}

	@Override
	public void update(TileMap map) {
		this.map = map;
	}

}
