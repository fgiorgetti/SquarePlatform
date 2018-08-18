package br.com.giorgetti.games.squareplatform.editor;

import br.com.giorgetti.games.squareplatform.gameobjects.interaction.action.data.NameDataInput;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.action.question.QuestionBaby;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.action.wizard.WizardLevel1;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.Sprite;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.action.*;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto.Checkpoint;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto.Coin;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto.EndLevel;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto.Information;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.enemy.EnemyDumb;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.enemy.EnemyFly;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.platform.LeftRightPlatform;
import br.com.giorgetti.games.squareplatform.gameobjects.interaction.platform.UpDownPlatform;

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
        gameObjectsMap.put("ButtonPlatform", ButtonPlatform.class);
        gameObjectsMap.put("Coin", Coin.class);
        gameObjectsMap.put("Checkpoint", Checkpoint.class);
        gameObjectsMap.put("End Level", EndLevel.class);
        gameObjectsMap.put("Enemy - Dumb", EnemyDumb.class);
        gameObjectsMap.put("Enemy - Fly", EnemyFly.class);
        gameObjectsMap.put("Information", Information.class);
        gameObjectsMap.put("Question Baby", QuestionBaby.class);
        gameObjectsMap.put("UserInput - Name", NameDataInput.class);
        gameObjectsMap.put("UpDownPlatform", UpDownPlatform.class);
        gameObjectsMap.put("LeftRightPlatform", LeftRightPlatform.class);
        gameObjectsMap.put("Open Door 1", OpenBlockingDoor.class);
        gameObjectsMap.put("Door 1", BlockingDoor.class);
        gameObjectsMap.put("Wizard Level 1", WizardLevel1.class);

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
