package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.awt.*;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Information extends AutoInteractiveSprite {

    private final int HEIGHT = 20;
    private final int WIDTH = 5;
    private long timeStamp;

    // Use a max of 28 characters per line
    private ArrayList<String> messages = new ArrayList<String>();

    public Information() {
        this.width = WIDTH;
        this.height = HEIGHT;
        messages.add("---- Important Information ----");
        messages.add("---------------------------");
        messages.add("This is just a test message.");
        messages.add("It will be hidden as soon as you");
        messages.add("move off the question mark.");
    }

    @Override
    public void executeInteraction() {
        timeStamp = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.black);
        g.fillOval(getX() - getHalfWidth() - map.getX() - 1, GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY() + 1, WIDTH, HEIGHT);
        g.setColor(Color.blue);
        g.fillOval(getX() - getHalfWidth() - map.getX(), GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(), WIDTH, HEIGHT);

        // If timeStamp is not refreshed within 100ms message is hidden
        if ( System.currentTimeMillis() - timeStamp <= 100 ) {
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
                       GamePanel.HEIGHT/4+(i+1)*20);
            }
        }

    }

}
