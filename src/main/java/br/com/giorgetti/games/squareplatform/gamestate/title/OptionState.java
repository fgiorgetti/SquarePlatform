package br.com.giorgetti.games.squareplatform.gamestate.title;

import br.com.giorgetti.games.squareplatform.config.OptionsConfig;
import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;

public class OptionState implements GameState {

    private static final LinkedHashMap<String, OptionType> OPTIONS = new LinkedHashMap<>();
    private static final LinkedHashMap<String, Object> INPUT = new LinkedHashMap<>();
    private static String[] OPTIONS_ARR;
    private int cursorIndex = 0;

    public enum OptionType {
        INTEGER_GAUGE
    }

    public static final String OPT_MUSIC_VOLUME = "Music volume";
    public static final String OPT_SFX_VOLUME = "Effects volume";

    static {

        OPTIONS.put(OPT_MUSIC_VOLUME, OptionType.INTEGER_GAUGE);
        OPTIONS.put(OPT_SFX_VOLUME, OptionType.INTEGER_GAUGE);

        INPUT.put(OPT_MUSIC_VOLUME, (int) (OptionsConfig.getInstance().getMusicVolume() * 100));
        INPUT.put(OPT_SFX_VOLUME, (int) OptionsConfig.getInstance().getSfxVolume() * 100);

        OPTIONS_ARR = OPTIONS.keySet().toArray(new String[0]);

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.white);
        g.fillRoundRect(GamePanel.WIDTH/5-15, GamePanel.HEIGHT/4-5,
                (GamePanel.WIDTH/5)*3+30, (GamePanel.HEIGHT/4)*2+10,
                5, 5);
        g.setColor(Color.blue);
        g.fillRoundRect(GamePanel.WIDTH/5-10, GamePanel.HEIGHT/4,
                (GamePanel.WIDTH/5)*3+20, (GamePanel.HEIGHT/4)*2,
                5, 5);
        g.setColor(Color.white);
        g.drawString("GAME OPTIONS", GamePanel.WIDTH/5 + 5, GamePanel.HEIGHT/4 + 15);
        g.drawString("--------------------------", GamePanel.WIDTH/5 + 5, GamePanel.HEIGHT/4 + 30);

        int i = 0;
        for ( String param : OPTIONS.keySet() ) {

            g.setColor(Color.white);

            if ( i++ == cursorIndex ) {
                g.drawRect(GamePanel.WIDTH/5 + 4,
                        GamePanel.HEIGHT/4+(i)*15+30 + 1,
                        (GamePanel.WIDTH/5)*3-1,
                        15
                );
            }

            g.drawString(param, GamePanel.WIDTH/5 + 5, GamePanel.HEIGHT/4+(i+1)*15+30);

            if ( OPTIONS.get(param).equals(OptionType.INTEGER_GAUGE) ) {
                drawGauge(g, GamePanel.WIDTH/5 + 100, GamePanel.HEIGHT/4+(i)*15+34, param);
            }

        }

        g.setBackground(Color.blue);
        g.setColor(Color.white);
        g.drawString("Keys: <UP>|<DOWN>|<ESC>|<ENTER>", GamePanel.WIDTH/5 + 5,
                GamePanel.HEIGHT/4+(OPTIONS.size()+2)*15+30);

    }

    private void drawGauge(Graphics2D g, int x, int y, String param) {

        int maxX = (GamePanel.WIDTH / 5) * 3 - x + 80;
        int maxY = 10;

        int value = (int) INPUT.get(param);
        int valuePct = maxX * value / 100;

        // Draw the borders of the gauge
        g.setColor(Color.white);
        g.drawRect(x, y, maxX, maxY);

        // Draw the pct selected by user
        g.fillRect(x, y, valuePct, maxY);

    }

    @Override
    public void notifySwitchedOff() {

    }

    @Override
    public void notifySwitchedOn() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {

            GamePanel.gsm.cleanTemporaryState();

            double value = 0;

            value = ((int) INPUT.get(OPT_MUSIC_VOLUME) / 100.0);
            OptionsConfig.getInstance().setMusicVolume(value);

            value = ((int) INPUT.get(OPT_SFX_VOLUME) / 100.0);
            OptionsConfig.getInstance().setSfxVolume(value);

        } else if ( OPTIONS.get(OPTIONS_ARR[cursorIndex]).equals(OptionType.INTEGER_GAUGE) ) {

            System.out.println("CUR VALUE = " + INPUT.get(OPTIONS_ARR[cursorIndex]));
            Integer curValue = (Integer) INPUT.get(OPTIONS_ARR[cursorIndex]);

            if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
                curValue--;
            } else if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                curValue++;
            }

            if ( curValue < 0 ) {
                curValue = 0;
            } else if ( curValue > 100 ) {
                curValue = 100;
            }

            INPUT.put(OPTIONS_ARR[cursorIndex], curValue);

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            cursorIndex = cursorIndex - 1 < 0? 0:--cursorIndex;
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            cursorIndex = cursorIndex + 1 >= OPTIONS.size()? OPTIONS.size()-1:++cursorIndex;
        // TODO Enhance and add a list of supported keys
        } else {

            /* Debug invalid keys
            String param = parameterList().get(cursorIndex);
            String value = userInput.get(param) + e.getKeyCode();
            userInput.put(param, value);
            */

        }
    }

}
