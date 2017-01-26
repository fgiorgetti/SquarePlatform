package br.com.giorgetti.games.squareplatform.gamestate.levels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import br.com.giorgetti.games.squareplatform.gameobjects.Player;
import br.com.giorgetti.games.squareplatform.gameobjects.SpriteDirection;
import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;


/**
 * Created by fgiorgetti on 5/1/15.
 */
public class LevelStateManager implements GameState {

    protected TileMap map;
    protected boolean [] keyMap = new boolean[255];
    protected ArrayList<Integer> supportedKeys = new ArrayList<>();
    protected Player player;
    
    protected long realTimeElapsed;
	protected boolean showFps = false;
	private int fps = 0;
	private long lastDrawFps;
    
    public LevelStateManager(String mapPath, Player p) {
        this.map = new TileMap();
        this.map.loadTileMap(mapPath, p);
        this.player = p;

        keyMap[KeyEvent.VK_UP] = false;
        keyMap[KeyEvent.VK_DOWN] = false;
        keyMap[KeyEvent.VK_LEFT] = false;
        keyMap[KeyEvent.VK_RIGHT] = false;
        keyMap[KeyEvent.VK_F12] = false;

        this.supportedKeys.add(KeyEvent.VK_UP);
        this.supportedKeys.add(KeyEvent.VK_DOWN);
        this.supportedKeys.add(KeyEvent.VK_LEFT);
        this.supportedKeys.add(KeyEvent.VK_RIGHT);
        this.supportedKeys.add(KeyEvent.VK_F12);
        
    }

    public void update() {

        map.update();
        updatePlayer();

    }

	/**
     * Updates player info based on keys.
     */
    private void updatePlayer() {

    	if ( keyMap[KeyEvent.VK_RIGHT] ) {
    		player.setDirection(SpriteDirection.RIGHT);
    		player.accelerate();
    	} else if ( keyMap[KeyEvent.VK_LEFT] ) {
    		player.setDirection(SpriteDirection.LEFT);
    		player.accelerate();
    	} else {
    		player.deaccelerate();
    	}
    	

    	// Crouch
    	if ( keyMap[KeyEvent.VK_DOWN] ) {
    		player.crouch();
    	} else {
    		player.standup();
    	}
    	
    	// Jump
    	if ( keyMap[KeyEvent.VK_UP] && !player.isJumping() ) {
    		player.jump();
    	} else if ( !keyMap[KeyEvent.VK_UP] && player.isJumping() ) {
    		player.jumpReleased();
    	}
    	
//       player.setPlayerYSpeed(
//               keyMap[KeyEvent.VK_UP]? +2:
//                       keyMap[KeyEvent.VK_DOWN]?  -2:
//                               0
//       );

    }

    public void draw(Graphics2D g) {

        map.draw(g);

        // Drawing player
        player.draw(g);
        drawFps(g);

        drawCollision(g);
                
    }

    private void drawCollision(Graphics2D g) {

        // Draw tile borders (top left, bottom left, top right, bottom right)
		drawTileBorders(g, map.getPlayerRow() + 1, map.getPlayerCol() - 1);
		drawTileBorders(g, map.getPlayerRow() - 1, map.getPlayerCol() - 1);
		drawTileBorders(g, map.getPlayerRow() + 1, map.getPlayerCol() + 1);
		drawTileBorders(g, map.getPlayerRow() - 1, map.getPlayerCol() + 1);

	}

    /**
     * Draw the borders of a tile based on the given row and col.
     * If tile is a blocked tile, then border color will be red.
     * Otherwise blue.
     * 
     * @param g
     * @param row
     * @param col
     */
	public void drawTileBorders(Graphics2D g, int row, int col) {
		/*
		Color color = null;
        if ( map.getTile(row, col).getType() == TileType.BLOCKED ) {
        	color = Color.RED;
        } else {
        	color = Color.BLUE;
        }
        g.setColor(color);
        g.drawRect(
        		(col) * map.getWidth() - map.getX(),
        		GamePanel.HEIGHT - (row) * map.getHeight() + map.getY(),
        		map.getWidth(), 
        		map.getHeight());
        */
	}

	/**
     * Draw the FPS box
     * @param g
     */
	private void drawFps(Graphics2D g) {
		
		if ( !showFps  ) {
			return;
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(GamePanel.WIDTH - 21, 0, 20, 12);

		if ( realTimeElapsed == 0 ) {
			realTimeElapsed = System.currentTimeMillis();
		} else {

			// Recalculate after 1 sec elapsed only
			if ( System.currentTimeMillis() - lastDrawFps > 500 ) {
				this.fps = (int) (1000 / (System.currentTimeMillis() - realTimeElapsed));
				lastDrawFps = realTimeElapsed;
			}
			realTimeElapsed = System.currentTimeMillis();
			g.drawString(fps+"", GamePanel.WIDTH - 18, 11);

		}
		
	}

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {

        if ( supportedKeys.contains(e.getKeyCode()) == false )
            return;
        keyMap[e.getKeyCode()] = true;

        // Show FPS
        if ( keyMap[KeyEvent.VK_F12] ) {
        	showFps = !showFps;
        }
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
