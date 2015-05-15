package br.com.giorgetti.games.squareplatform.gamestate.interaction;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.TreeSet;

/**
 * Created by fgiorgetti on 5/15/15.
 */
public class DialogGameState implements GameState {

    private int x, y, width, height;
    private Font font;
    private Color fgColor, bgColor;
    private String originalMessage = "";
    private StringBuilder message = new StringBuilder();
    private StringBuilder input   = new StringBuilder();
    private boolean enabled;
    private DialogCallbackHandler handler;

    public DialogGameState(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.fgColor = Color.WHITE;
        this.bgColor = Color.BLUE;
        this.font = Font.getFont(Font.DIALOG_INPUT);

    }

    /**
     * Creates a new dialog and enables the component.
     * @param message
     */
    public void createDialog(String message, DialogCallbackHandler handler) {

        // Ignore if already enabled
        if ( enabled )
            return;

        this.originalMessage = message;
        this.message = new StringBuilder(message);
        this.message.append(": ");
        this.enabled = true;
        this.input = new StringBuilder();
        this.handler = handler;

    }

    public void disable() {
        this.enabled = false;
        this.input = new StringBuilder();
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

        if ( !enabled )
            return;

        g.setColor(bgColor);
        g.setFont(font);
        g.fillRect(x, y, width, height);
        g.setColor(fgColor);

        g.drawString(message.toString(), 2 + x, 10 + y);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        this.input = new StringBuilder();
        this.enabled = true;
        this.message = new StringBuilder(originalMessage);
        this.message.append(": ");
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_ENTER || !enabled ) {
            this.enabled = false;
            handler.handle(input.toString());

            return;
        } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            disable();
        }

        message.append(e.getKeyChar());
        input.append(e.getKeyChar());

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Returns the input entered by the user
     *
     * @return
     */
    public String getInput() {
        return input.toString();
    }
}
