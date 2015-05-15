package br.com.giorgetti.games.squareplatform.gamestate.editor;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.gamestate.interaction.MessageGameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.Tile;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;
import br.com.giorgetti.games.squareplatform.tiles.TileSet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by fgiorgetti on 5/1/15.
 */
public class MapEditorStateManager implements GameState {

    protected TileMap map;

    private Tile currentTile = null;
    private TileSet currentTileSet = null;
    private LinkedList<TileSet> tileSetList = null;
    private int curTileSetPos = 0;
    private int curTilePos = 0;
    private int curTileType = 0;
    private MessageGameState messages = null;

    public MapEditorStateManager(String mapPath) {

        this.map = new TileMap();
        this.map.loadTileMap(mapPath);
        this.map.setPlayerX(map.getWidth() / 2);
        this.map.setPlayerY(map.getHeight() / 2);


        tileSetList = new LinkedList<>(getMap().getTileSetMap().values());
        currentTileSet = tileSetList.get(curTileSetPos);
        currentTile = currentTileSet.getTile(curTilePos);

        messages = new MessageGameState(GamePanel.WIDTH - 128,
                                        GamePanel.HEIGHT - 64,
                                        128,
                                        128,
                                        1
                );

    }

    public void update() {

        map.update();
        messages.update();;

    }

    public void draw(Graphics2D g) {

        map.draw(g);
        messages.draw(g);

        //g.setColor(Color.BLACK);
        //g.drawRect(map.getPlayerX()-1-map.getX(), GamePanel.HEIGHT-map.getPlayerY()-1+map.getY(), 3, 3);

        // Draw currently selected tile on screen
        g.drawImage(currentTile.getTileImage(),
                    map.getPlayerX()-map.getX()-map.getWidth()/2,
                    GamePanel.HEIGHT-map.getPlayerY()+map.getY()-map.getHeight()/2,
                    null);

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        // detect row and column
        int row = getMap().getPlayerRow();
        int col = getMap().getPlayerCol();

        // go right a tile
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {

            getMap().setPlayerX(getMap().getPlayerX()+map.getWidth());

            // if reached right of map
            if ( getMap().getPlayerX() == getMap().getCols() * getMap().getWidth() ) {
                getMap().setPlayerX(getMap().getPlayerX() - map.getWidth() / 2);
            }

        // go left a tile
        } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {

            getMap().setPlayerX(getMap().getPlayerX()-map.getWidth());

            // if reached left of map
            if ( getMap().getPlayerX() == 0 ) {
                getMap().setPlayerX(map.getWidth() / 2);
            }

        }

        // go up a tile
        if ( e.getKeyCode() == KeyEvent.VK_UP ) {

            getMap().setPlayerY(getMap().getPlayerY() + map.getHeight());

            // if reached top of map
            if ( getMap().getPlayerY() == getMap().getRows() * getMap().getHeight() ) {
                getMap().setPlayerY(getMap().getPlayerY() - map.getHeight() / 2);
            }

        // go down a tile
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {

            getMap().setPlayerY(getMap().getPlayerY() - map.getHeight());

            // if reached bottom of map
            if ( getMap().getPlayerY() == 0 ) {
                getMap().setPlayerY(map.getHeight() / 2);
            }

        // Add selected tile to the selected row/column
        } else if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {

            // If row does not exist on the map, add it
            if (!getMap().getMap().containsKey(row))
                getMap().getMap().put(row, new HashMap<Integer, String[]>());

            // Add selected tile to the selected position on the map
            getMap().getMap().get(row).put(col,
                    (col + TileMap.PROP_SEPARATOR +
                            (curTileSetPos+1) + TileMap.PROP_SEPARATOR +
                            curTilePos + TileMap.PROP_SEPARATOR +
                            curTileType).split(","));

        // Change tile
        } else if ( e.getKeyCode() == KeyEvent.VK_T ) {

            // Get next tile within same tileset
            if ( currentTileSet.getNumTiles() > ++curTilePos ) {
                currentTile = currentTileSet.getTile(curTilePos);
            // Go to next tileset (if any)
            } else {

                // Use the first tile of the next tileset
                curTilePos = 0;

                // If already on last tile set, go back to first
                if ( tileSetList.size() == ++curTileSetPos ) {
                    curTileSetPos = 0;
                } else {
                    currentTileSet = tileSetList.get(curTileSetPos);
                }

                currentTile = currentTileSet.getTile(curTilePos);

            }
            System.out.println("curtileset = " + curTileSetPos + " - curtilepos = " + curTilePos);

        // change tile type
        } else if ( e.getKeyCode() == KeyEvent.VK_Y ) {

            Tile.TileType[] types = Tile.TileType.values();

            if ( ++curTileType >= types.length ) {
                curTileType = 0;
            }


            messages.addMessage(Tile.TileType.fromType(curTileType).name());
            System.out.println(Tile.TileType.fromType(curTileType));

        // delete a tile from the map
        } else if ( e.getKeyCode() == KeyEvent.VK_X ) {

            // Remove selected tile
            if ( getMap().getMap().containsKey(row) &&
                    getMap().getMap().get(row).containsKey(col) ) {
                getMap().getMap().get(row).remove(col);

                // If row is empty, remove row
                if ( getMap().getMap().get(row).isEmpty() ) {
                    getMap().getMap().remove(row);
                }

            }

        }

        // Output map content
        if ( e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println(getMap().toString());
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(new File(getClass().getResource("/maps/level1.dat").toURI())));
                out.write(getMap().toString());
                out.flush();
                out.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public TileMap getMap() {
        return map;
    }

}
