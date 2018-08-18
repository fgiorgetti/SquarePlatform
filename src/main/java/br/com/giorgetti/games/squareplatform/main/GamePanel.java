package br.com.giorgetti.games.squareplatform.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.gamestate.GameStateManager;
import javafx.embed.swing.JFXPanel;

/**
 * Handles the main loop in the game, drawing all data to the screen
 * coordinating the delays needed and triggering all interactions.
 * Created by fgiorgetti on 5/1/15.
 */
@SuppressWarnings("serial")
public class GamePanel extends JFXPanel implements Runnable {

    // Game panel dimensions
    //public static final int WIDTH = 800;
    //public static final int HEIGHT = 600;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    //public static final int SCALE = 1;
    public static final int SCALE = 2;
    public static final int FPS = 60;
    public static final long TARGET_TIME = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;

    private static GamePanel instance;
    public static GameStateManager gsm;

    private Thread thread = null;

    public static GamePanel getInstance(GameState gs) {
        if ( instance == null ) {
            instance = new GamePanel(gs);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private GamePanel(GameState gs) {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        setIgnoreRepaint(true);
        setDoubleBuffered(true);
        requestFocus();
        gsm = new GameStateManager(gs);
    }


    /**
     * This method overrides the one provided by
     * JComponent (parent of JFXPanel) to notify that this object
     * now has a parent object and so it is active.
     *
     * The main game thread will be started here.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if ( this.thread == null ) {
            this.thread = new Thread(this);
            this.thread.setName("GameLoop");
            addKeyListener(gsm);
            init();
            thread.start();
        }
    }

    /**
     * Sleep the amount of time needed to achieve the target FPS
     * @param startTimeMillis
     */
    public void sleepTarget(long startTimeMillis) {
        long elapsed = System.currentTimeMillis() - startTimeMillis;
        if ( TARGET_TIME > elapsed ) {
            try { Thread.sleep(TARGET_TIME - elapsed); } catch (InterruptedException e) {}
        }
    }

    public void init() {

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

    }


    public void run() {

        // Main game loop
        long start = System.currentTimeMillis();
        while ( instance != null ) {

            // Let the game state manager handle the content
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            gsm.gameLoop(g);

            // Draw prepared content to screen
            drawToScreen();

            // Sleeping amount of time to achieve FPS
            sleepTarget(start);
            start = System.currentTimeMillis();

        }
    }

    /**
     * Draw prepared image to the JFXPanel
     */
    private void drawToScreen() {

        Graphics2D g2 = (Graphics2D) getGraphics();

        // When changing the instance (resetInstance) g2 might become null
        try {
            g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
            g2.dispose();
        } catch (NullPointerException npe) {}

    }

}

