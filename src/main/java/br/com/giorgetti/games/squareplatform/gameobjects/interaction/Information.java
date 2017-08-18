package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Information extends AutoInteractiveSprite {

    private final int DRAW_HEIGHT = 15;
    private final int FULL_HEIGHT = GamePanel.HEIGHT; // Even if player jumps message gets displayed
    private final int WIDTH = 15;
    private long timeStamp;

    // Use a max of 28 characters per line
    private ArrayList<String> messages = new ArrayList<String>();

    //TODO Needs some enhancements and make it abstract, so concrete classes will only provide the message
    public Information() {
        this.width = WIDTH;
        this.height = FULL_HEIGHT;
        this.ySpeed = 0;

        messages.add("---- Important Information ----");
        messages.add("");
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
        Font oldFont = g.getFont();

        int drawX = getX() - map.getX();
        int drawY = GamePanel.HEIGHT-getY()+(DRAW_HEIGHT/2)+map.getY();

        g.setFont(new Font("TimeRoman", Font.BOLD, 16));
        g.drawString("!", drawX - 1, drawY - 1);
        g.setColor(Color.yellow);
        g.drawString("!", drawX, drawY);
        g.setFont(oldFont);

        // If timeStamp is not refreshed within 100ms message is hidden
        if ( System.currentTimeMillis() - timeStamp <= 100 ) {
            /*
            g.setColor(Color.white);
            g.fillRoundRect(GamePanel.WIDTH/5-15, GamePanel.HEIGHT/5-5,
                    (GamePanel.WIDTH/5)*3+30, (GamePanel.HEIGHT/5)*3+10,
                    5, 5);
            g.setColor(Color.black);
            g.fillRoundRect(GamePanel.WIDTH/5-10, GamePanel.HEIGHT/5,
                    (GamePanel.WIDTH/5)*3+20, (GamePanel.HEIGHT/5)*3,
                    5, 5);
            */
            for ( int i = 0 ; i < messages.size(); i++ ) {
                g.setColor(Color.black);
                g.drawString(messages.get(i), GamePanel.WIDTH/5 + 4,
                        GamePanel.HEIGHT/5+(i+1)*20-1);
                g.setColor(Color.white);
                g.drawString(messages.get(i), GamePanel.WIDTH/5 + 5,
                       GamePanel.HEIGHT/5+(i+1)*20);
            }
        }

    }

}
