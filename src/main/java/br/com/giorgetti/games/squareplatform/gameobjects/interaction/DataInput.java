package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.*;
import java.util.List;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public abstract class DataInput extends InteractiveSprite implements GameState {

    private final int HEIGHT = 15;
    private final int WIDTH = 15;

    /**
     * Concrete class must provide a list of parameters
     * that will be requested to the user
     * @return
     */
    public abstract List<String> parameterList();

    /**
     * Executed once user press ENTER after entering the
     * expected parameters.
     */
    public abstract void complete();

    private int cursorIndex = 0;
    private static Map<String,String> userInput = new LinkedHashMap<>();

    private boolean askQuestion = false;

    public DataInput() {
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    @Override
    public void executeInteraction() {

        askQuestion = true;
        // Add non-existing parameters
        for ( String param : parameterList() ) {
            if ( !userInput.containsKey(param) ) {
                userInput.put(param, "");
            }
        }
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
        g.drawString("I/O",getX() - map.getX() - 1, GamePanel.HEIGHT - getY() + map.getY() + 1);
        g.setColor(Color.BLUE);
        g.drawString("I/O",getX() - map.getX(), GamePanel.HEIGHT - getY() + map.getY());

        if ( askQuestion ) {
            drawUserDataInput(g);
        }

    }

    private void drawUserDataInput(Graphics2D g) {

        // If timeStamp is not refreshed within 100ms message is hidden
        g.setColor(Color.white);
        g.fillRoundRect(GamePanel.WIDTH/5-15, GamePanel.HEIGHT/4-5,
                (GamePanel.WIDTH/5)*3+30, (GamePanel.HEIGHT/4)*2+10,
                5, 5);
        g.setColor(Color.black);
        g.fillRoundRect(GamePanel.WIDTH/5-10, GamePanel.HEIGHT/4,
                (GamePanel.WIDTH/5)*3+20, (GamePanel.HEIGHT/4)*2,
                5, 5);
        g.setColor(Color.white);
        g.drawString("Enter the parameters below", GamePanel.WIDTH/5 + 5, GamePanel.HEIGHT/4 + 15);
        g.drawString("--------------------------", GamePanel.WIDTH/5 + 5, GamePanel.HEIGHT/4 + 30);
        int i = 0;
        for ( String param : parameterList() ) {

            int subIdx = userInput.get(param).length() - 20;

            g.setColor(Color.white);

            if ( i++ == cursorIndex ) {
                g.drawRect(GamePanel.WIDTH/5 + 4,
                            GamePanel.HEIGHT/4+(i)*15+30 + 1,
                            (GamePanel.WIDTH/5)*3-1,
                            15
                        );
            }
            g.drawString(param + ": "
                            + userInput.get(param).substring(subIdx<0?0:subIdx),
                    GamePanel.WIDTH/5 + 5,
                    GamePanel.HEIGHT/4+(i+1)*15+30);
        }

        g.setBackground(Color.black);
        g.setColor(Color.white);
        g.drawString("Keys: <UP>|<DOWN>|<ESC>|<ENTER>", GamePanel.WIDTH/5 + 5,
                GamePanel.HEIGHT/4+(userInput.size()+2)*15+30);

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

        if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            for ( String param : parameterList() ) {
                userInput.put(param, "");
            }
            askQuestion = false;
            GamePanel.gsm.cleanTemporaryState();
        } else if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
            askQuestion = false;
            GamePanel.gsm.cleanTemporaryState();
            complete();
        } else if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            cursorIndex = cursorIndex - 1 < 0? 0:--cursorIndex;
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            cursorIndex = cursorIndex + 1 >= parameterList().size()? parameterList().size()-1:++cursorIndex;
        } else if ( e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                e.getKeyCode() == KeyEvent.VK_DELETE ) {
            String param = parameterList().get(cursorIndex);
            String value = userInput.get(param);
            if ( value.length() == 0) {
                userInput.put(param, "");
            } else {
                userInput.put(param, value.substring(0, value.length()-1));
            }
        // Enhance and add a list of supported keys
        } else if (
                   ( e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z )
                || ( e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9 )
                || ( e.getKeyCode() >= KeyEvent.VK_NUMPAD0 && e.getKeyCode() <= KeyEvent.VK_NUMPAD9 )
                || e.getKeyCode() == KeyEvent.VK_SPACE
                || e.getKeyCode() == KeyEvent.VK_PERIOD
                || e.getKeyCode() == KeyEvent.VK_COMMA
                || e.getKeyCode() == KeyEvent.VK_COLON
                || e.getKeyCode() == KeyEvent.VK_MINUS
                || e.getKeyCode() == KeyEvent.VK_SLASH
                || e.getKeyCode() == KeyEvent.VK_BACK_SLASH ) {

            String param = parameterList().get(cursorIndex);
            String value = userInput.get(param) + String.valueOf(e.getKeyChar());
            userInput.put(param, value);

        } else {

            /* Debug invalid keys
            String param = parameterList().get(cursorIndex);
            String value = userInput.get(param) + e.getKeyCode();
            userInput.put(param, value);
            */

        }

    }

    public String getDataInput(String key) {
        return userInput.get(key);
    }
}
