package br.com.giorgetti.games.squareplatform.gamestate.levels;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.sprites.Player;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by fgiorgetti on 5/1/15.
 */
public class LevelStateManager implements GameState {

    protected TileMap map;
    protected boolean [] keyMap = new boolean[255];
    protected ArrayList<Integer> supportedKeys = new ArrayList<>();
    protected Player player;

    public LevelStateManager(String mapPath, Player p) {
        this.map = new TileMap();
        this.map.loadTileMap(mapPath, p);
        this.player = p;

        keyMap[KeyEvent.VK_UP] = false;
        keyMap[KeyEvent.VK_DOWN] = false;
        keyMap[KeyEvent.VK_LEFT] = false;
        keyMap[KeyEvent.VK_RIGHT] = false;

        this.supportedKeys.add(KeyEvent.VK_UP);
        this.supportedKeys.add(KeyEvent.VK_DOWN);
        this.supportedKeys.add(KeyEvent.VK_LEFT);
        this.supportedKeys.add(KeyEvent.VK_RIGHT);

    }

    public void update() {

        map.update();
        updatePlayer();

    }

    private void updatePlayer() {

        player.setPlayerXSpeed(
                keyMap[KeyEvent.VK_RIGHT]? +4:
                keyMap[KeyEvent.VK_LEFT]?  -4:
                        0
        );

        player.setPlayerYSpeed(
                keyMap[KeyEvent.VK_UP]? +4:
                        keyMap[KeyEvent.VK_DOWN]?  -4:
                                0
        );

    }

    public void draw(Graphics2D g) {

        map.draw(g);

        g.setColor(Color.BLACK);
        g.drawRect(player.getPlayerX()-2-map.getX(), GamePanel.HEIGHT-player.getPlayerY()-2+map.getY(), 4, 4);

    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {

        if ( supportedKeys.contains(e.getKeyCode()) == false )
            return;
        keyMap[e.getKeyCode()] = true;

    }

    public void keyReleased(KeyEvent e) {

        if ( supportedKeys.contains(e.getKeyCode()) == false )
            return;
        keyMap[e.getKeyCode()] = false;

    }

    public TileMap getMap() {
        return map;
    }

}
