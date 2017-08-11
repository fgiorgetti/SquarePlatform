package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.Animation;
import br.com.giorgetti.games.squareplatform.gameobjects.sprite.SpriteState;
import br.com.giorgetti.games.squareplatform.gamestate.GameState;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.tiles.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public abstract class WizardInformation extends InteractiveSprite implements GameState {

    public abstract String getTitle();
    public abstract List<String> getMessages();

    private static final int LINES_PER_PAGE = 6;
    private int currentPage = 0;

    public WizardInformation() {

        this.setWidth(20);
        this.setHeight(30);

        loadAnimation(Animation.newAnimation(
                SpriteState.IDLE.name(),
                "/sprites/objects/wizard.png",
                20).delay(300));

        setAnimation(SpriteState.IDLE.name());

    }

    @Override
    public void executeInteraction() {
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

        //g.setColor(Color.green);
        //g.fillRoundRect(getLeftX(), getTopY(), getWidth(), getHeight(), 5, 5);

        if ( GamePanel.gsm.temporaryState == this ) {
            drawMessage(g);
            // Draw the wizard
            g.drawImage(getCurrentAnimation(), (GamePanel.WIDTH/5)*3+30, getTopY(), getWidth()*2, getHeight()*2,null);
        } else {
            // Draw the wizard
            g.drawImage(getCurrentAnimation(), getLeftX(), getTopY(), getWidth(), getHeight(),null);
        }

    }

    private void drawMessage(Graphics2D g) {

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
        g.drawString(getTitle(), GamePanel.WIDTH/5 + 5, lineY + (linesOffset * ++linesPrinted));
        linesPrinted++;

        int initialLine = currentPage * LINES_PER_PAGE;
        for ( int i = initialLine; i < initialLine + LINES_PER_PAGE && i < getMessages().size(); i++ ) {
            String line = getMessages().get(i);
            g.drawString(String.format("%s", line),
                    GamePanel.WIDTH/5 + 5,
                    lineY + (linesOffset * ++linesPrinted));
        }

        linesPrinted+=2;
        g.setBackground(Color.black);
        g.setColor(Color.white);
        String optKey = getMessages().size() > LINES_PER_PAGE? "Keys: <SPACE>|":"Key: ";
        g.drawString(optKey + "<ESC>", GamePanel.WIDTH/5 + 5,
                lineY + (linesOffset * ++linesPrinted));

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

        if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
            if ( (currentPage+1) * LINES_PER_PAGE < getMessages().size() ) {
                currentPage++;
            } else {
                closeMessage();
            }
        } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            closeMessage();
        }

    }

    private void closeMessage() {
        currentPage = 0;
        GamePanel.gsm.cleanTemporaryState();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
