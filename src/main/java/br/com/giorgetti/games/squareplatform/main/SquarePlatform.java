package br.com.giorgetti.games.squareplatform.main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import br.com.giorgetti.games.squareplatform.gameobjects.Player;
import br.com.giorgetti.games.squareplatform.gamestate.levels.LevelStateManager;
import br.com.giorgetti.games.squareplatform.gamestate.title.TitleState;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class SquarePlatform {

	private static JFrame window;
	private static DisplayMode originalDisplayMode;
	private static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static Cursor originalCursor;

	public static void main(String [] args ) {

        window = new JFrame("Square Platform");
        window.setContentPane(GamePanel.getInstance(TitleState.getInstance()));
		//window.setContentPane(GamePanel.getInstance(new LevelStateManager("/maps/level1.dat", new Player())));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true); // without it, it does not hide taskbar on linux
		window.setUndecorated(true);
        window.setIgnoreRepaint(true);
		window.setUndecorated(false);
		originalCursor = window.getCursor();
		originalDisplayMode = gd.getDisplayMode();

		switchFullScreen(LevelStateManager.isFullScreen());

	}

	public static void switchFullScreen(boolean fullScreen) {

        if ( fullScreen && gd.isFullScreenSupported() ) {
			window.setCursor(hideCursor());
			window.setResizable(true); // without it, it does not hide taskbar on linux
		} else {
			window.setResizable(false);
			window.setCursor(originalCursor);
		}

		DisplayMode displayMode = null;

		// If fullscreen mode is supported, then use it.
		if ( fullScreen && gd.isFullScreenSupported() ) {
			gd.setFullScreenWindow(window);
		} else {
			gd.setFullScreenWindow(null);
			if ( !gd.getDisplayMode().equals(originalDisplayMode) ) {
				gd.setDisplayMode(originalDisplayMode);
			}
		}

		DisplayMode[] availableModes = gd.getDisplayModes();

		// Available display modes
		for (DisplayMode mode : availableModes) {
			if ( mode.getWidth() == GamePanel.WIDTH * GamePanel.SCALE ) {
				//System.out.println(mode.getWidth() + "x" + mode.getHeight() + " - " + mode.getBitDepth() + " - " + mode.getRefreshRate());
				displayMode = mode;
			}
		}

		// Must be in Full Screen to test if change display mode is allowed
		if ( fullScreen && gd.isFullScreenSupported() &&
				gd.isDisplayChangeSupported() && displayMode != null ) {
			gd.setDisplayMode(displayMode);
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
