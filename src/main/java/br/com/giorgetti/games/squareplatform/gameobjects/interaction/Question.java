package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class Question extends InteractiveSprite implements GameState {

    private final int HEIGHT = 10;
    private final int WIDTH = 10;

    private boolean askQuestion = false;
    private boolean correct = false;
    private boolean showResult = false;
    private long timestamp;

    public abstract String correctChoice();
    public abstract String question();
    public abstract LinkedHashMap<String, String> choicesAndAnswers();

    public Question() {
        this.width = WIDTH;
        this.height = HEIGHT;
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
        Font oldFont = g.getFont();
        g.setFont(new Font("TimeRoman", Font.BOLD, 16));
        g.drawString("?",getX() - map.getX() - 1, GamePanel.HEIGHT - getY() + map.getY() + 11);
        g.setColor(!correct? Color.magenta:Color.green);
        g.drawString("?",getX() - map.getX(), GamePanel.HEIGHT - getY() + map.getY() + 10);
        g.setFont(oldFont);

        if ( askQuestion ) {
            drawQuestion(g);
        }

    }

    private void drawQuestion(Graphics2D g) {

        g.setColor(Color.white);
        g.fillRoundRect(GamePanel.WIDTH/5-15, GamePanel.HEIGHT/5-5,
                (GamePanel.WIDTH/5)*3+30, (GamePanel.HEIGHT/5)*3+10,
                5, 5);
        g.setColor(Color.black);
        g.fillRoundRect(GamePanel.WIDTH/5-10, GamePanel.HEIGHT/5,
                (GamePanel.WIDTH/5)*3+20, (GamePanel.HEIGHT/5)*3,
                5, 5);

        int lineY = GamePanel.HEIGHT/5;
        int linesOffset = 15;
        int linesPrinted = 0;
        g.setColor(Color.white);
        g.drawString("-------- QUESTION --------", GamePanel.WIDTH/5 + 5, lineY + (linesOffset * ++linesPrinted));
        g.drawString(question(), GamePanel.WIDTH/5 + 5, lineY + (linesOffset * ++linesPrinted));
        linesPrinted++;

        int choiceIdx = 0;
        for ( Map.Entry<String, String> choice : choicesAndAnswers().entrySet() ) {
             g.drawString(String.format("%2s - %s", choice.getKey(), choice.getValue()),
                     GamePanel.WIDTH/4 + 5,
                     lineY + (linesOffset * ++linesPrinted));
        }

        if ( showResult ) {

            linesPrinted++;
            g.drawString(correct? "Correct: " + correctChoice()+"! Nice job.":"Incorrect, try again.",
                    GamePanel.WIDTH/4 + 5,
                    lineY + (linesOffset * ++linesPrinted));

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
        for ( String choice : choicesAndAnswers().keySet() ) {
            if ( choice.equalsIgnoreCase(String.valueOf(e.getKeyChar())) ) {
                valid = true;
                break;
            }
        }

        if ( ! valid ) {
            return;
        }

        if ( correctChoice().equalsIgnoreCase(String.valueOf(e.getKeyChar())) ) {
            correct = true;
        } else {
            correct = false;
        }

        showResult = true;
        timestamp = System.currentTimeMillis();

    }
}
