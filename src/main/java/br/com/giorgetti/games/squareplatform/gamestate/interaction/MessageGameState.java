package br.com.giorgetti.games.squareplatform.gamestate.interaction;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by fgiorgetti on 5/15/15.
 */
public class MessageGameState implements GameState {

    private int x, y, width, height, delayInSecs;
    private Font font;
    private Color fgColor, bgColor;
    private LinkedHashMap<Long, String> messages = null;

    public MessageGameState(int x, int y, int width, int height, int delayInSecs) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.delayInSecs = delayInSecs;
        this.messages = new LinkedHashMap<>();

        this.fgColor = Color.WHITE;
        this.bgColor = Color.BLUE;
        this.font = Font.getFont(Font.DIALOG_INPUT);

    }


    /**
     * Adds a new message to the message presentation list
     *
     * @param message
     */
    public void addMessage(String message) {
        messages.put(System.currentTimeMillis(), message);
    }

    @Override
    public void update() {

        // Removing old messages
        if ( !messages.isEmpty() ) {
            for (Long startTime : new TreeSet<>(messages.keySet())) {
                if (System.currentTimeMillis() - startTime > delayInSecs * 1000) {
                    messages.remove(startTime);
                }
            }
        }

    }

    @Override
    public void draw(Graphics2D g) {

        // Handle the list of messages to present
        handleMessages(g);
    }

    /**
     * Handle drawing for the multi messages panel
     * @param g
     */
    private void handleMessages(Graphics2D g) {

        if ( messages.isEmpty() )
            return;

        g.setColor(bgColor);
        g.setFont(font);
        g.fillRect(x, y, width, height + messages.size() * 10);
        g.setColor(fgColor);

        int msgCount = 0;
        for ( Long startTime : new TreeSet<>(messages.keySet()) ) {
            g.drawString(messages.get(startTime), 2 + x, 10 + y + msgCount * 10);
            msgCount++;
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

    }
}
