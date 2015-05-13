package br.com.giorgetti.games.squareplatform.main;

import br.com.giorgetti.games.squareplatform.gamestate.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class GamePanel extends JPanel implements Runnable {

    // Game panel dimensions
    public static final int WIDTH = 320;
    public static final int HEIGHT = 200;

    public static final int SCALE = 2;
    public static final int FPS = 60;
    public static final long TARGET_TIME = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;

    private GameStateManager gsm;

    private Thread thread = null;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
        gsm = new GameStateManager();
    }


    /**
     * This method is called overrides the one provided by
     * JComponent (parent of JPanel) to notify that this object
     * now has a parent object and so it is active.
     *
     * The main game thread will be started here.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if ( this.thread == null ) {
            this.thread = new Thread(this);
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
        while ( true ) {

            // Let the game state manager handle the content
            gsm.gameLoop(g);

            // Draw prepared content to screen
            drawToScreen();

            // Sleeping amount of time to achieve FPS
            sleepTarget(start);
            start = System.currentTimeMillis();

        }
    }

    /**
     * Draw prepared image to the JPanel
     */
    private void drawToScreen() {

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();

    }

}

