package br.com.giorgetti.games.squareplatform.gamestate.editor;

import br.com.giorgetti.games.squareplatform.exception.InvalidMapException;
import br.com.giorgetti.games.squareplatform.gameobjects.GameObjectSelector;
import br.com.giorgetti.games.squareplatform.gameobjects.MovableSprite;
import br.com.giorgetti.games.squareplatform.gameobjects.Sprite;
import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.gamestate.interaction.DialogCallbackHandler;
import br.com.giorgetti.games.squareplatform.gamestate.interaction.DialogGameState;
import br.com.giorgetti.games.squareplatform.gamestate.interaction.MessageGameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.main.TileMapEditor;
import br.com.giorgetti.games.squareplatform.tiles.Tile;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;
import br.com.giorgetti.games.squareplatform.tiles.TileSet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Controls the tile map editor and handle commands.
 *
 * Created by fgiorgetti on 5/1/15.
 */
public class MapEditorStateManager implements GameState {

    protected TileMap map;
    protected Sprite player;

    private Tile currentTile = null;
    private TileSet currentTileSet = null;
    private LinkedList<TileSet> tileSetList = null;
    private int curTileSetPos = 0;
    private int curTilePos = 0;
    private int curTileType = Tile.TileType.BLOCKED.getType();
    private String curGameObject = GameObjectSelector.getNextGameObject("");
    private MessageGameState messages = null;
    private DialogGameState dialog = null;
    private int playerInitialX;
    private int playerInitialY;

    private enum TileIndex {
        PREV, NEXT;
    }

    public MapEditorStateManager(String mapPath, Sprite p) throws InvalidMapException {

        this.map = new TileMap(true);
        this.map.loadTileMap(mapPath, p);
        this.player = p;
        this.player.setMap(this.map);
        this.playerInitialX = this.map.getPlayerX();
        this.playerInitialY = this.map.getPlayerY();
        player.setX(map.getWidth() / 2);
        player.setY(map.getHeight() / 2);
        player.setWidth(this.map.getWidth());
        player.setHeight(this.map.getHeight());

        tileSetList = new LinkedList<>(getMap().getTileSetMap().values());
        currentTileSet = tileSetList.get(curTileSetPos);
        currentTile = currentTileSet.getTile(curTilePos);

        messages = new MessageGameState(GamePanel.WIDTH - 160,
                                        GamePanel.HEIGHT - 64,
                                        160,
                                        128,
                                        1
                );

        this.dialog = new DialogGameState(50, 100, 250, 30);

    }

    public void update() {

        map.update();
        messages.update();
        dialog.update();

    }

    public void draw(Graphics2D g) {

        map.draw(g, true);
        messages.draw(g);
        dialog.draw(g);

        // Draw currently selected tile on screen
        g.drawImage(currentTile.getTileImage(),
                player.getX() - map.getX() - map.getWidth() / 2,
                GamePanel.HEIGHT - player.getY() + map.getY() - map.getHeight() / 2,
                null);

        // Draw a yellow rectangle around the tile
        g.setColor(Color.YELLOW);
        g.drawRect(player.getX() - map.getX() - map.getWidth() / 2,
                GamePanel.HEIGHT - player.getY() + map.getY() - map.getHeight() / 2,
                map.getWidth(), map.getHeight());


        // Draw a B on current tile
        if ( curTileType == Tile.TileType.BLOCKED.getType() ) {
            g.setColor(Color.black);
            g.fillRect(
                    player.getX() - map.getX() - 2,
                    GamePanel.HEIGHT - player.getY() + map.getY() - 10,
                    10, 15
            );
            g.setColor(Color.white);
            g.drawString("B",
                    player.getX() - map.getX(),
                    GamePanel.HEIGHT - player.getY() + map.getY()
            );
        }

        // Draw selected game object
        g.setColor(Color.white);
        g.drawString("GameObject: " + curGameObject, 9, 11);
        g.setColor(Color.black);
        g.drawString("GameObject: " + curGameObject, 10, 10);

        // If player's initial position on screen, draw it
        g.setColor(Color.green);
        boolean playerPosOnScreen = playerInitialX >= map.getX() && playerInitialX <= map.getRightX()
                                && playerInitialY >= map.getY() && playerInitialY <= map.getTopY();
        if ( playerPosOnScreen ) {
            g.setColor(Color.black);
            g.drawString("PLAYER", playerInitialX - map.getX() - 1, GamePanel.HEIGHT - playerInitialY + map.getY() + 1);
            g.setColor(Color.green);
            g.drawString("PLAYER", playerInitialX - map.getX(), GamePanel.HEIGHT - playerInitialY + map.getY());
        }
        //g.drawString("On screen? = " + playerPosOnScreen, 10, 30);
        //g.drawString("Player X / Y = " + player.getX() + " / " + player.getY(), 10, 50);
        //g.drawString("Map X / Y = " + map.getX() + " / " + map.getY(), 10, 70);
        //g.drawString("Right X / Top Y = " + map.getRightX() + " / " + map.getTopY(), 10, 90);

    }

    public void keyTyped(KeyEvent e) {  }

    public void keyPressed(KeyEvent e) {

        if ( dialog.isEnabled() ) {
            dialog.keyPressed(e);
            return;
        }

        // detect row and column
        int row = getMap().getPlayerRow();
        int col = getMap().getPlayerCol();

        // go right a tile
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {

            player.setX(player.getX()+map.getWidth());

            // if reached right of map
            if ( player.getX() == getMap().getCols() * getMap().getWidth() ) {
                player.setX(player.getX() - map.getWidth() / 2);
            }

        // go left a tile
        } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {

            player.setX(player.getX() - map.getWidth());

            // if reached left of map
            if (player.getX() == 0) {
                player.setX(map.getWidth() / 2);
            }

        // go up a tile
        } else if ( e.getKeyCode() == KeyEvent.VK_UP ) {

            player.setY(player.getY() + map.getHeight());

            // if reached top of map
            if ( player.getY() == getMap().getRows() * getMap().getHeight() ) {
                player.setY(player.getY() - map.getHeight() / 2);
            }

        // go down a tile
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {

            // if reached bottom of map
            if ( player.getY() > player.getHeight() ) {
                player.setY(player.getY() - map.getHeight());
            }

        // Add selected tile to the selected row/column
        } else if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {

            // If row does not exist on the map, add it
            if (!getMap().getMap().containsKey(row))
                getMap().getMap().put(row, new ConcurrentHashMap<Integer, String[]>());

            // Add selected tile to the selected position on the map
            getMap().getMap().get(row).put(col,
                    (col + TileMap.PROP_SEPARATOR +
                            (curTileSetPos+1) + TileMap.PROP_SEPARATOR +
                            curTilePos + TileMap.PROP_SEPARATOR +
                            curTileType).split(","));

        // Save player position on the map
        } else if ( e.getKeyCode() == KeyEvent.VK_P ) {

            this.playerInitialX = this.player.getX();
            this.playerInitialY = this.player.getY();
            messages.addMessage("Player position defined");

        // Change tile
        } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        // Change tile
        } else if ( e.getKeyCode() == KeyEvent.VK_R ) {
            changeSelectedTile(TileIndex.PREV);
            // change tile type
        } else if ( e.getKeyCode() == KeyEvent.VK_T ) {
            changeSelectedTile(TileIndex.NEXT);
            // change tile type
        } else if ( e.getKeyCode() == KeyEvent.VK_Y ) {

            Tile.TileType[] types = Tile.TileType.values();

            if ( ++curTileType >= types.length ) {
                curTileType = 0;
            }

            messages.addMessage(Tile.TileType.fromType(curTileType).name());

        // delete a tile from the map
        } else if ( e.getKeyCode() == KeyEvent.VK_X ) {

            // Remove selected tile
            if (getMap().getMap().containsKey(row) &&
                    getMap().getMap().get(row).containsKey(col)) {
                getMap().getMap().get(row).remove(col);

                // If row is empty, remove row
                if (getMap().getMap().get(row).isEmpty()) {
                    getMap().getMap().remove(row);
                }

            }

            // Remove sprite at player x,y
            getMap().removeSpriteAtPlayer();

        // Output map content
        } else if ( e.getKeyCode() == KeyEvent.VK_S ) {

            // Saving player current position
            int playerX = player.getX();
            int playerY = player.getY();

            String baseDir = System.getProperty("user.dir") + "/src/main/resources/maps/";
            // Setting the pre-defined player position
            player.setX(playerInitialX);
            player.setY(playerInitialY);
            System.out.println("Saving file as: " + baseDir + TileMapEditor.getMapPath());
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(new File(baseDir + TileMapEditor.getMapPath())));
                out.write(getMap().toString());
                out.flush();
                out.close();
                messages.addMessage("Map has been saved");
            } catch (IOException e1) {
                messages.addMessage("Error saving map");
                messages.addMessage(e1.getMessage());
            }

            // Restoring player position
            player.setX(playerX);
            player.setY(playerY);

        } else if ( e.getKeyCode() == KeyEvent.VK_A ) {

            dialog.createDialog("Enter file name", new DialogCallbackHandler() {
                @Override
                public void handle(String userInput) {
                    try {

                    	// Saving player current position
                    	int playerX = player.getX();
                    	int playerY = player.getY();

                        String baseDir = System.getProperty("user.dir") + "/src/main/resources/maps/";
                    	// Setting the pre-defined player position
                        player.setX(playerInitialX);
                        player.setY(playerInitialY);
                    	System.out.println("Saving file as: " + baseDir + userInput);
                        BufferedWriter out = new BufferedWriter(new FileWriter(new File(baseDir + userInput)));
                        out.write(getMap().toString());
                        out.flush();
                        out.close();
                        messages.addMessage("Map has been saved");

                        // Restoring player position
                        player.setX(playerX);
                        player.setY(playerY);
                    } catch (Exception e1) {
                        messages.addMessage("Error saving map");
                        try { Thread.sleep(1); } catch (InterruptedException e2) { e2.printStackTrace(); }
                        messages.addMessage(e1.getMessage());
                        dialog.enable();
                    }
                }
            });
        } else if ( e.getKeyCode() == KeyEvent.VK_O ) {
            dialog.createDialog("Enter file name", new DialogCallbackHandler() {
                        @Override
                        public void handle(String userInput) {
                            TileMapEditor.loadMap(userInput);
                        }
            });
        } else if ( e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET ) {
            curGameObject = GameObjectSelector.getPreviousGameObject(curGameObject);
        } else if ( e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET ) {
            curGameObject = GameObjectSelector.getNextGameObject(curGameObject);
        } else if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
            this.map.addSprite(player.getX(), player.getY(), GameObjectSelector.getObjectClassName(curGameObject));
        } else {
            messages.addMessage("Invalid key = " + e.getKeyCode());
        }

    }

    private void changeSelectedTile(TileIndex tileIndex) {

        if (tileIndex == TileIndex.NEXT) {
            curTilePos++;
        } else {
            curTilePos--;
        }

        if ( curTilePos < 0 ) {

            // If on first tile set, go back to last
            if ( --curTileSetPos < 0 ) {
                curTileSetPos = tileSetList.size() -1;
            }

            currentTileSet = tileSetList.get(curTileSetPos);

            // Use the last tile of the previous tileset
            curTilePos = currentTileSet.getNumTiles() - 1;

        // Go to next tileset (if any)
        } else if ( curTilePos >= currentTileSet.getNumTiles() ) {

            // Use the first tile of the next tileset
            curTilePos = 0;

            // If already on last tile set, go back to first
            if ( tileSetList.size() == ++curTileSetPos ) {
                curTileSetPos = 0;
            }

            currentTileSet = tileSetList.get(curTileSetPos);

        }

        currentTile = currentTileSet.getTile(curTilePos);
        System.out.println("curtileset = " + curTileSetPos + " - curtilepos = " + curTilePos);
    }

    public void keyReleased(KeyEvent e) {

        // go right a tile
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            //player.setXSpeed(0);
        } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            //player.setXSpeed(0);
        }

        // go up a tile
        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            //player.incYSpeed(0);
        // go down a tile
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            //player.incYSpeed(0);
        }

    }

    public TileMap getMap() {
        return map;
    }

    @Override
    public void notifySwitchedOff() {

    }

    @Override
    public void notifySwitchedOn() {

    }

}
