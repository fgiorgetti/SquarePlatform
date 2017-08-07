package br.com.giorgetti.games.squareplatform.main;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        window.setContentPane(GamePanel.getInstance(new LevelStateManager("/maps/level1.dat", new Player())));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true); // without it, it does not hide taskbar on linux
        window.setUndecorated(true);
        window.setIgnoreRepaint(true);
		window.setCursor(hideCursor());
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = null;

        // If fullscreen mode is supported, then use it.
		if ( gd.isFullScreenSupported() ) {
//			gd.setFullScreenWindow(window);
		}

		DisplayMode[] availableModes = gd.getDisplayModes();

		// Available display modes
		for (DisplayMode mode : availableModes) {
			if ( mode.getWidth() == GamePanel.WIDTH * GamePanel.SCALE ) {
				System.out.println(mode.getWidth() + "x" + mode.getHeight() + " - " + mode.getBitDepth() + " - " + mode.getRefreshRate());
				displayMode = mode;
			}
		}

		// Must be in Full Screen to test if change display mode is allowed
		if ( gd.isDisplayChangeSupported() && displayMode != null ) {
//			gd.setDisplayMode(displayMode);
		}

		window.validate();
		window.pack();
		window.setVisible(false);
		window.setVisible(true);

	}

	public static Cursor hideCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Point hotSpot = new Point(0,0);
		BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
		return toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
	}

}
