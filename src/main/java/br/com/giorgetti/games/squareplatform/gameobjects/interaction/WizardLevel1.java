package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import java.util.ArrayList;
import java.util.List;

public class WizardLevel1 extends WizardInformation {

    private static final List<String> messages = new ArrayList<>();

    static {
        messages.add("I will explain how you can play this game");
        messages.add("- Use the arrows to move your player");
        messages.add("- Press SPACE to interact with objects");
        messages.add("- Press ESC to quit at any time");
        messages.add("- UP Arrow is used to jump");
        messages.add("- DOWN Arrow is used to crouch");
        messages.add("- Questions can only be answered once");
        messages.add("- Each 10 coins give you an extra life");
    }

    @Override
    public String getTitle() {
        return "Hello! I am the cognitive wizard.";
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }

}
