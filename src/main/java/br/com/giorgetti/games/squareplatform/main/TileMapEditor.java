package br.com.giorgetti.games.squareplatform.main;

import br.com.giorgetti.games.squareplatform.exception.InvalidMapException;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.EditorPlayer;
import br.com.giorgetti.games.squareplatform.gamestate.editor.MapEditorStateManager;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Main class to run the map editor.
 * Created by fgiorgetti on 5/1/15.
 */
public class TileMapEditor {

    private static final String mapPrefix = "/maps/";
    private static String mapPath = "level1.dat";
    public static final JFrame WINDOW = new JFrame("TileMap Editor");
    private static final String baseDir = "src/main/resources/maps/";
    private static MapEditorStateManager msm;

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

        if ( args.length == 1 ) {
            mapPath = args[0];
        }

        msm = null;
        loadMap(mapPath);

    }

    public static void loadMap(String mapFile) {

        try {
            msm = new MapEditorStateManager(mapPrefix + mapFile, new EditorPlayer());
            mapPath = mapFile;
        } catch (InvalidMapException e) {

            if ( WINDOW.isVisible() == true ) {
                WINDOW.setVisible(false);
            }

            System.out.println("Handling new map");
            createNewMap(mapFile);

            loadMap(mapFile);
            return;
        }

        if ( WINDOW.isVisible() ) {
            WINDOW.setVisible(false);
            WINDOW.pack();
            GamePanel.resetInstance();
        }

        WINDOW.setContentPane(GamePanel.getInstance(msm));
        WINDOW.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        WINDOW.setResizable(false);
        WINDOW.pack();
        WINDOW.setVisible(true);

    }

    public static void createNewMap(String mapFile) {

        String rows = "50";
        String cols = "100";
        String width = "32";
        String height = "32";

        // statically defined ( manually change if needed )
        String bg = "BG=25,/backgrounds/background.png\n";
        String tileset = "TS=1,default,/tileset/tileset1.png,32,32,11 2,ground,/tileset/ground2.png,32,32,6\n";
        String playerini = "PLAYER=48,80\n";

        rows = readVar("Number of rows", rows);
        cols = readVar("Number of columns", cols);
        width = readVar("Tile width", width);
        height = readVar("Tile height", height);

        // read tile sets
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(baseDir + mapFile)));
            out.write("ROWS=" + rows + "\n");
            out.write("COLS=" + cols + "\n");
            out.write("WIDTH=" + width + "\n");
            out.write("HEIGHT=" + height + "\n");
            out.write(bg);
            out.write(tileset);
            out.write(playerini);
            out.flush();
            out.close();
        } catch ( Exception e) {
            System.out.println("Error saving map: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Map has been saved successfully: " + mapFile);

    }

    public static String readVar(String message, String _default) {

        Scanner s = new Scanner(System.in);
        String value = null;

        while ( true ) {
            System.out.printf("%s %s: ", message, _default != null? "[" + _default + "]":"");
            value = s.nextLine();

            if ( value.isEmpty() ) {
                value = _default;
            }

            break;
        }

        return value;

    }

    public static String getMapPath() {
        return mapPath;
    }
}