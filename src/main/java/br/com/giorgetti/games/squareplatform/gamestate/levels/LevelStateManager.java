package br.com.giorgetti.games.squareplatform.gamestate.levels;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.Background;
import br.com.giorgetti.games.squareplatform.tiles.Tile;
import br.com.giorgetti.games.squareplatform.tiles.Tile.TileType;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * Created by fgiorgetti on 5/1/15.
 */
public class LevelStateManager implements GameState {

    protected TileMap map;

    public LevelStateManager(String mapPath) {
        this.map = new TileMap();
        this.map.loadTileMap(mapPath);
    }

    public void update() {

        map.setX(map.getPlayerX() - GamePanel.WIDTH / 2);
        map.setY(map.getPlayerY() - GamePanel.HEIGHT / 2);
        map.setColOffset(map.getX() / map.getWidth());
        map.setRowOffset(map.getY() / map.getHeight() + 1);

    }

    public void draw(Graphics2D g) {

        // Drawing backgrounds
        // TODO Consider current position and move speed
        for ( Background bg : map.getBackgrounds() ) {
            g.drawImage(bg.getImage(), 0, 0, null);
            //g.drawImage(bg.getImage().getScaledInstance(GamePanel.WIDTH, GamePanel.HEIGHT, 0), 0, 0, null);
        }

        // Drawing map on the screen...
        for ( int row : map.getMap().keySet() ) {

            if ( row < map.getRowOffset() || row > map.getRowOffset() + map.getMaxRowsOnScreen() )
                continue;

            for ( int col : map.getMap().get(row).keySet() ) {

                if ( col < map.getColOffset() || col > map.getColOffset() + map.getMaxColsOnScreen()+1 )
                    continue;

                // Drawing the tile map
                String[] arr = map.getMap().get(row).get(col);
                Tile t = map.getTileSetMap().get(arr[TileMap.POS_MAP_TILESET_ID]).getTile(arr[TileMap.POS_MAP_TILEPOS_ID]);
                t.setType(TileType.fromType(arr[TileMap.POS_MAP_TILE_TYPE]));
                g.drawImage(t.getTileImage(),
                        col * map.getWidth() - map.getX(),
                        GamePanel.HEIGHT - row * map.getHeight() + map.getY(),
                        null);

            }
        }

        g.setColor(Color.BLACK);
        g.drawRect(map.getPlayerX()-2-map.getX(), GamePanel.HEIGHT-map.getPlayerY()-2+map.getY(), 4, 4);

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            getMap().setPlayerX(getMap().getPlayerX()+4);
        } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            getMap().setPlayerX(getMap().getPlayerX()-4);
        }

        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            getMap().setPlayerY(getMap().getPlayerY()+4);
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            getMap().setPlayerY(getMap().getPlayerY()-4);
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public TileMap getMap() {
        return map;
    }

}
