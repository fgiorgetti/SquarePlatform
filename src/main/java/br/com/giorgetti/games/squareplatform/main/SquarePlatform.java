package br.com.giorgetti.games.squareplatform.main;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import br.com.giorgetti.games.squareplatform.gameobjects.Player;
import br.com.giorgetti.games.squareplatform.gamestate.levels.LevelStateManager;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class SquarePlatform {

    public static void main(String [] args ) {

        final JFrame window = new JFrame("Square Platform");
        window.setContentPane(new GamePanel(new LevelStateManager("/maps/level1.dat", new Player())));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = new DisplayMode(
        		GamePanel.WIDTH * GamePanel.SCALE, 
        		GamePanel.HEIGHT * GamePanel.SCALE, 
        		32, 
        		60);
        
        // If fullscreen mode is supported, then use it.
    	if ( gd.isFullScreenSupported() && gd.isDisplayChangeSupported() ) {
    		gd.setFullScreenWindow(window);
    		gd.setDisplayMode(displayMode);
    		window.validate();
    		window.pack();
    		window.setVisible(false);
    		window.setVisible(true);
    	} else {
    		window.pack();
    		window.setVisible(true);
    	}
    }

}
