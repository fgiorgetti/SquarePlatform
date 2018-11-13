package br.com.giorgetti.games.squareplatform.gamestate.levels;

import br.com.giorgetti.games.squareplatform.exception.InvalidMapException;
import br.com.giorgetti.games.squareplatform.gameobjects.player.Player;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteDirection;
import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.gamestate.title.OptionState;
import br.com.giorgetti.games.squareplatform.gamestate.title.TitleState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.main.SquarePlatform;
import br.com.giorgetti.games.squareplatform.media.MediaPlayer;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;
import javafx.embed.swing.JFXPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


/**
 * Manages user events on the loaded Map, and updates
 * map, player and all the states needed for the game to run.
 * 
 * Created by fgiorgetti on 5/1/15.
 */
public class LevelStateManager extends JFXPanel implements GameState {

	private int currentLevel = 0;
	private String[] levels = new String[] {
        "level1",
		"level2"
    };

	private boolean initString = false;
    private static final Font GAME_OVER_FONT = Font.getFont(Font.DIALOG_INPUT);
	protected TileMap map;
    protected boolean [] keyMap = new boolean[255];
    protected ArrayList<Integer> supportedKeys = new ArrayList<>();
    protected Player player = new Player();
    protected boolean gameOver = false;
    
    protected long realTimeElapsed;
	protected boolean showFps = false;
	private int fps = 0;
	private long lastDrawFps;

	private MediaPlayer mediaPlayer;

	private static boolean fullScreen = false;
	private static LevelStateManager instance = null;

	public static LevelStateManager getInstance() {
		if ( instance == null ) {
			instance = new LevelStateManager();
		}
		return instance;
	}

	private LevelStateManager() {
    }

    public void newGame() {

		this.player = new Player();
		gameOver = false;
		currentLevel = 0;
		loadLevel("/maps/" + levels[currentLevel] + ".dat", player);

	}

	public void nextLevel() {

		if ( ++currentLevel >= levels.length ) {
			destroy();
			// TODO Create a final state to congratulate player
			GamePanel.gsm.switchGameState(TitleState.getInstance());
		} else {
		    this.mediaPlayer.stop();
			loadLevel("/maps/" + levels[currentLevel] + ".dat", player);
			this.mediaPlayer.play();
		}

	}

    private void loadLevel(String mapPath, Player p) {

	    synchronized (p) {

			TileMap loadedMap = new TileMap();

			try {
				loadedMap.loadTileMap(mapPath, p);
			} catch (InvalidMapException e) {
				System.out.println("Invalid map. Fix game setup - Not found = " + mapPath);
				System.exit(1);
			}
			this.player = p;

			keyMap[KeyEvent.VK_UP] = false;
			keyMap[KeyEvent.VK_DOWN] = false;
			keyMap[KeyEvent.VK_LEFT] = false;
			keyMap[KeyEvent.VK_RIGHT] = false;
			keyMap[KeyEvent.VK_F12] = false;
			keyMap[KeyEvent.VK_SPACE] = false;
			keyMap[KeyEvent.VK_ESCAPE] = false;
			keyMap[KeyEvent.VK_F] = false;
			keyMap[KeyEvent.VK_O] = false;

			this.supportedKeys.add(KeyEvent.VK_UP);
			this.supportedKeys.add(KeyEvent.VK_DOWN);
			this.supportedKeys.add(KeyEvent.VK_LEFT);
			this.supportedKeys.add(KeyEvent.VK_RIGHT);
			this.supportedKeys.add(KeyEvent.VK_F12);
			this.supportedKeys.add(KeyEvent.VK_SPACE);
			this.supportedKeys.add(KeyEvent.VK_ESCAPE);
			this.supportedKeys.add(KeyEvent.VK_F);
			this.supportedKeys.add(KeyEvent.VK_O);

			p.setMap(loadedMap);
			this.map = loadedMap;
		}

    }

    public void update() {

	    synchronized (this.player) {

	        if ( this.map == null ) {
	        	return;
			}

			// Need to send back to a final score page or main menu
			if ( gameOver ) {
				return;
			}

			if ( !player.isDying() ) {
				map.update();
				updatePlayer();
			} else {
				player.update(map);
			}

			// Testing when player falls in a hole
			if ( player.getY()-1 <= -player.getHeight() ) {
				if ( player.getLifes() == 0 ) {
					gameOver = true;
				} else {
					try { Thread.sleep(500); } catch (InterruptedException e) {}
					map.recoverLastCheckpoint();
					player.revive();
				}
			}

		}

	}

	/**
     * Updates player info based on keys.
     */
    private void updatePlayer() {

		// Move right or left
    	if ( keyMap[KeyEvent.VK_RIGHT] ) {
    		player.setDirection(SpriteDirection.RIGHT);
    		player.accelerate();
    	} else if ( keyMap[KeyEvent.VK_LEFT] ) {
    		player.setDirection(SpriteDirection.LEFT);
    		player.accelerate();
    	} else {
    		player.deaccelerate();
    	}
    	

    	// Jump
    	if ( keyMap[KeyEvent.VK_UP] && !player.isJumping() ) {
    		player.jump();
    	} else if ( !keyMap[KeyEvent.VK_UP] && player.isJumping() ) {
    		player.jumpReleased();
    	}

		// Crouch
		if ( !player.isJumpingOrFalling() ) {
			if ( keyMap[KeyEvent.VK_DOWN] ) {
				player.crouch();
			} else {
				player.standup();
			}
		}

    }

    public void draw(Graphics2D g) {        

        synchronized (this.player ) {

            if ( this.map == null ) {
            	return;
			}

			map.draw(g);

			// Drawing player
			player.draw(g);
			drawFps(g);

			// Enable for debugging purposes.
			//drawCollision(g);

			if ( gameOver ) {

				int panelX = GamePanel.WIDTH/2-40;
				int panelY = GamePanel.HEIGHT/2-15;

				g.setColor(Color.BLACK);
				g.setFont(GAME_OVER_FONT);
				g.fillRect(panelX, panelY, 80, 30);
				g.setColor(Color.WHITE);
				g.drawString("Game Over", panelX + 5, panelY + 20);

			}

		}

	}

	@Override
	public void destroy() {

		if ( this.mediaPlayer != null ) {
			this.mediaPlayer.remove();
			this.mediaPlayer = null;
		}

	}

	@Override
	public void notifySwitchedOff() {
		keyMap[KeyEvent.VK_LEFT] = false;
		keyMap[KeyEvent.VK_RIGHT] = false;
	}

	@Override
	public void notifySwitchedOn() {

		//TODO Customize media to play on map
		if ( this.mediaPlayer == null ) {
			this.mediaPlayer = new MediaPlayer("/music/music.mp3", MediaPlayer.MediaType.MUSIC);
			this.mediaPlayer.play(true, 2000);
		}

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

			// Recalculate after 250ms elapsed only
			if ( System.currentTimeMillis() - lastDrawFps > 250 ) {
				this.fps = (int) (1000 / (System.currentTimeMillis() - realTimeElapsed));
				lastDrawFps = realTimeElapsed;
			}
			realTimeElapsed = System.currentTimeMillis();
			g.drawString(fps+"", GamePanel.WIDTH - 18, 11);

		}
		
	}

    public void keyTyped(KeyEvent e) {

	}

    public void keyPressed(KeyEvent e) {

        if ( supportedKeys.contains(e.getKeyCode()) == false )
            return;
        keyMap[e.getKeyCode()] = true;

        // Execute interaction on sprites
        this.getMap().interactiveAction(e);

        // Show FPS
		if ( keyMap[KeyEvent.VK_F12] ) {
			showFps = !showFps;
		}

		// FullScreen mode
		if ( e.getKeyCode() == KeyEvent.VK_F ) {
			fullScreen = !fullScreen;
			SquarePlatform.switchFullScreen(fullScreen);
		}

		if ( e.getKeyCode() == KeyEvent.VK_O ) {
		    GamePanel.gsm.addTemporaryState(OptionState.getInstance());
		}

        if ( keyMap[KeyEvent.VK_ESCAPE] ) {
		    destroy();
		    GamePanel.gsm.switchGameState(TitleState.getInstance());
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

	public static boolean isFullScreen() {
		return fullScreen;
	}

}
