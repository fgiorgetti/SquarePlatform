package br.com.giorgetti.games.squareplatform.gamestate.levels;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.tiles.Tile;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class Level1 extends LevelStateManager {

    private TileMap map;

    public Level1() {

        map = new TileMap();
        map.loadTileMap("/maps/level1.dat");

    }
    public void draw(Graphics2D g) {

        // Drawing map on the screen...
        for ( int row : map.getMap().keySet() ) {
            for ( int col : map.getMap().get(row).keySet() ) {
                String[] arr = map.getMap().get(row).get(col);
                Tile t = map.getTileSetMap().get(arr[TileMap.POS_MAP_TILESET_ID]).getTile(arr[TileMap.POS_MAP_TILEPOS_ID]);
                g.drawImage(t.getTileImage(), col * map.getWidth(), row * map.getHeight(), null);
            }
        }

    }

}
