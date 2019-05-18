package br.com.giorgetti.games.squareplatform.gameobjects.interaction.action.wizard;

import java.util.ArrayList;
import java.util.List;

/**
 * Level1 information provided by the Wizard.
 */
public class WizardLevel1 extends WizardInformation {

    private static final List<String> messages = new ArrayList<>();

    static {
        messages.add("Hello! I am the Cognitive Wizard!");
        messages.add("I will guide you through this game.");
        messages.add("Here are the basic keys you can use:");
        messages.add("- Left and right arrows to move");
        messages.add("- Up arrow is used to jump");
        messages.add("- Down arrow is used to crouch");
        messages.add("- Press SPACE to interact with objects");
        messages.add("- Press ESC to quit at any time.");
        messages.add("");
        messages.add("We will meet again soon! Bye.");
    }

    @Override
    public String getTitle() {
        return "How to play this game.";
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }

}
