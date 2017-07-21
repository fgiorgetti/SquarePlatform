package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by fgiorgetti on 21/07/17.
 */
public class QuestionBaby extends Question {

    private final LinkedHashMap<String, String> choices = new LinkedHashMap<String, String>();

    public QuestionBaby() {
        choices.put("A", "Lucas");
        choices.put("B", "Fernando");
        choices.put("C", "Antonio");
        choices.put("D", "Henrique");
    }

    @Override
    public String correctChoice() {
        return "A";
    }

    @Override
    public String question() {
        return "What is the baby's name?";
    }

    @Override
    public LinkedHashMap<String, String> choicesAndAnswers() {
        return choices;
    }
}
