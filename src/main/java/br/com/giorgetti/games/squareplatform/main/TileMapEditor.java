package br.com.giorgetti.games.squareplatform.main;

import br.com.giorgetti.games.squareplatform.gameobjects.EditorPlayer;
import br.com.giorgetti.games.squareplatform.gamestate.editor.MapEditorStateManager;

import javax.swing.*;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class TileMapEditor {

    public static void main(String [] args ) throws Exception {

        /* Uses Google Guava to find classes within a given package
        // Not using it because I dont want to add a 3MB dependency to this project.

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
        for (ClassPath.ClassInfo clazz : cp.getTopLevelClassesRecursive("br.com.giorgetti.games.squareplatform.gameobjects") ) {
            //System.out.println(clazz.getName());
            if ( Sprite.class.isAssignableFrom(clazz.load()) ) {
                System.out.println(clazz.getName() + " is a valid Sprite");
            }
        }

        if ( true ) System.exit(1);
        */

        JFrame window = new JFrame("TileMap Editor");
        window.setContentPane(GamePanel.getInstance(new MapEditorStateManager("/maps/level1.dat", new EditorPlayer())));

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);

    }

}