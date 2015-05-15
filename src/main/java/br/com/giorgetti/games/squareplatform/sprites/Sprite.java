package br.com.giorgetti.games.squareplatform.sprites;

import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;

/**
 * Created by fgiorgetti on 5/15/15.
 */
public interface Sprite {

    public void update(TileMap map);
    public void draw(Graphics2D g);

}
