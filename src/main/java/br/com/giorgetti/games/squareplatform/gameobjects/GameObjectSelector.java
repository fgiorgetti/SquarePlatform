package br.com.giorgetti.games.squareplatform.gameobjects;

import br.com.giorgetti.games.squareplatform.gameobjects.interaction.Button;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.Coin;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.Information;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.Question;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides a list of all available GameObjects that can be
 * used through Map Editor.
 *
 * Created by fgiorgetti on 14/07/17.
 */
public class GameObjectSelector {

    private static final ArrayList<String> gameObjectsList = new ArrayList<>();
    private static final Map<String, Class<? extends Sprite>> gameObjectsMap = new TreeMap<>();

    static {
        gameObjectsMap.put("Button", Button.class);
        gameObjectsMap.put("Coin", Coin.class);
        gameObjectsMap.put("Information", Information.class);
        gameObjectsMap.put("Question", Question.class);

        for ( String key : gameObjectsMap.keySet() ) {
            gameObjectsList.add(key);
        }
    }

    public static String getObjectClassName(String objectName) {
        return gameObjectsMap.get(objectName).getName();
    }

    public static String getPreviousGameObject(String previous) {

        int idx = 0;
        if ( gameObjectsList.contains(previous) ) {
            idx = gameObjectsList.indexOf(previous) - 1;
        }

        // Cycle back to first
        if ( idx < 0 ) {
            idx = gameObjectsList.size() - 1;
        }

        return gameObjectsList.get(idx);

    }
    public static String getNextGameObject(String previous) {

        int idx = 0;
        if ( gameObjectsList.contains(previous) ) {
            idx = gameObjectsList.indexOf(previous) + 1;
        }

        // Cycle back to first
        if ( idx == gameObjectsList.size() ) {
            idx = 0;
        }

        return gameObjectsList.get(idx);

    }


}
