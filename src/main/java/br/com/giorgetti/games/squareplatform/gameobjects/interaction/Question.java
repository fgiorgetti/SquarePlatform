package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Question extends InteractiveSprite implements GameState {

    private final int HEIGHT = 10;
    private final int WIDTH = 10;

    private ArrayList<String> messages = new ArrayList<String>();
    private boolean askQuestion = false;
    private boolean correct = false;
    private boolean showResult = false;
    private int correctAnswer = KeyEvent.VK_A;
    private int [] validAnswers  = new int[]{KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C};
    private long timestamp;

    public Question() {
        this.width = WIDTH;
        this.height = HEIGHT;
        messages.add("Question");
        messages.add("---------------------------");
        messages.add("What is the baby's name ?");
        messages.add("A) Lucas");
        messages.add("B) Fernando");
        messages.add("C) Henrique");
        messages.add("---------------------------");
        messages.add("Type a character");
    }

    @Override
    public void executeInteraction() {

        showResult = false;
        askQuestion = true;
        GamePanel.gsm.addTemporaryState(this);
    }

    @Override
    public void update(TileMap map) {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.black);
        g.drawString("?",getX() - map.getX() - 1, GamePanel.HEIGHT - getY() + map.getY() + 1);
        g.setColor(!correct? Color.magenta:Color.green);
        g.drawString("?",getX() - map.getX(), GamePanel.HEIGHT - getY() + map.getY());

        if ( askQuestion ) {
            drawQuestion(g);
        }

    }

    private void drawQuestion(Graphics2D g) {

        // If timeStamp is not refreshed within 100ms message is hidden
        g.setColor(Color.white);
        g.fillRoundRect(GamePanel.WIDTH/4-5, GamePanel.HEIGHT/4-5,
                (GamePanel.WIDTH/4)*2+10, (GamePanel.HEIGHT/4)*2+10,
                5, 5);
        g.setColor(Color.black);
        g.fillRoundRect(GamePanel.WIDTH/4, GamePanel.HEIGHT/4,
                (GamePanel.WIDTH/4)*2, (GamePanel.HEIGHT/4)*2,
                5, 5);
        g.setColor(Color.white);
        for ( int i = 0 ; i < messages.size(); i++ ) {
            g.drawString(messages.get(i), GamePanel.WIDTH/4 + 5,
                    GamePanel.HEIGHT/4+(i+1)*15);
        }

        if ( showResult ) {

            g.drawString(correct? "Correct: " + "A":"Incorrect!", GamePanel.WIDTH/4 + 5,
                    GamePanel.HEIGHT/4+(messages.size()+1)*15);

            if ( System.currentTimeMillis() - timestamp > 2000 ) {
                askQuestion = false;
                showResult = false;
                GamePanel.gsm.cleanTemporaryState();
            }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if ( !askQuestion ) {
            return;
        }

        boolean valid = false;
        for ( int k : validAnswers ) {
            if ( k == e.getKeyCode() ) {
                valid = true;
                break;
            }
        }

        if ( ! valid ) {
            return;
        }

        if ( e.getKeyCode() == correctAnswer ) {
            correct = true;
        } else {
            correct = false;
        }

        showResult = true;
        timestamp = System.currentTimeMillis();

    }
}
