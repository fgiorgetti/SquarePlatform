package br.com.giorgetti.games.squareplatform.gamestate.levels;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
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

        map.update();

    }

    public void draw(Graphics2D g) {

        map.draw(g);

        g.setColor(Color.BLACK);
        g.drawRect(map.getPlayerX()-2-map.getX(), GamePanel.HEIGHT-map.getPlayerY()-2+map.getY(), 4, 4);

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            getMap().setPlayerXSpeed(4);
        } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            getMap().setPlayerXSpeed(-4);
        }

        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            getMap().setPlayerYSpeed(+4);
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            getMap().setPlayerYSpeed(-4);
        }

    }

    public void keyReleased(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            getMap().setPlayerXSpeed(0);
        } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            getMap().setPlayerXSpeed(0);
        }

        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            getMap().setPlayerYSpeed(0);
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            getMap().setPlayerYSpeed(0);
        }

    }

    public TileMap getMap() {
        return map;
    }

}
