package br.com.giorgetti.games.squareplatform.gamestate.title;

import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.gamestate.levels.LevelStateManager;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.Background;
import javafx.embed.swing.JFXPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;

public class TitleState extends JFXPanel implements GameState {

    public static final String SPEED_PCT = "40";
    private Background backgroundSky;
    private BufferedImage titleImage;

    private int bgMaxX, bgMaxY;
    private int x, y;
    private int ySpeed = 1;

    public static final String START = "Start";
    public static final String CHOOSE_LEVEL = "Choose level";
    public static final String OPTIONS = "Options";
    private int selectedOption = 0;
    private String[] options = new String[]{START, OPTIONS};

    private static final LinkedHashMap<String, GameState> menuOptions = new LinkedHashMap<>();
    private static TitleState instance;

    /**
     * Make this a singleton
     * @return
     */
    public static TitleState getInstance() {
        if ( instance == null ) {
            instance = new TitleState();
        }
        return instance;
    }

    private TitleState() {

        try {
            backgroundSky   = new Background("/backgrounds/background.png", SPEED_PCT);
            titleImage = ImageIO.read(this.getClass().getResourceAsStream("/title/title.png"));

            int drawTimes = 100 / Integer.parseInt(SPEED_PCT);
            bgMaxX = backgroundSky.getImage().getWidth() * drawTimes;
            bgMaxY = backgroundSky.getImage().getHeight() * drawTimes;

            menuOptions.put(START, new LevelStateManager());
            menuOptions.put(OPTIONS, OptionState.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {

        x++;
        y+=ySpeed;

        if ( y >= bgMaxY || y <= 0) {
            ySpeed = ySpeed * -1;
        }

    }

    @Override
    public void draw(Graphics2D g) {

        // Draw the BG
        backgroundSky.draw(g, x, y);

        // Draw the Title
        g.drawImage(titleImage, 0,0, null);

        // Draw the menu options
        int strX = GamePanel.WIDTH / 10 * 5;
        int strY = 220;
        for ( String option : options ) {

            boolean selected = option.equals(options[selectedOption]);

            g.setColor(Color.BLACK);
            g.drawString(option, strX, strY);
            if ( selected ) { g.drawString(">", strX-15, strY); }

            g.setColor(Color.WHITE);
            g.drawString(option, strX+1, strY+1);
            if ( selected ) { g.drawString(">", strX-14, strY+1); }

            strY += 20;

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
            assignState();
        } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            System.exit(0);
        }

        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            if ( --selectedOption < 0 ) {
                selectedOption = options.length - 1;
            }
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            if ( ++selectedOption >= options.length ) {
                selectedOption = 0;
            }
        }

    }

    private void assignState() {
        String opt = options[selectedOption];
        GameState gs = menuOptions.get(opt);

        switch (opt) {
            case START:
                gs = new LevelStateManager();
                menuOptions.put(opt, gs);
                GamePanel.gsm.switchGameState(gs);
                break;
            case OPTIONS:
                GamePanel.gsm.addTemporaryState(gs);
                break;
            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void notifySwitchedOff() {
    }

    @Override
    public void notifySwitchedOn() {
    }

}
