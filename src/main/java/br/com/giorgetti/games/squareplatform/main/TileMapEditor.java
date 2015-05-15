package br.com.giorgetti.games.squareplatform.main;

import br.com.giorgetti.games.squareplatform.gamestate.editor.MapEditorStateManager;

import javax.swing.*;

/**
 * Created by fgiorgetti on 5/1/15.
 */
public class TileMapEditor {

    public static void main(String [] args ) {

        JFrame window = new JFrame("TileMap Editor");
        window.setContentPane(new GamePanel(new MapEditorStateManager("/maps/level1.dat")));

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);

    }

}
