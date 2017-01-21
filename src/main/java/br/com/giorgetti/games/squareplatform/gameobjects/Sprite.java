package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 5/15/15.
 */
public abstract class Sprite {

    public abstract void update(TileMap map);
    public abstract void draw(Graphics2D g);

}
