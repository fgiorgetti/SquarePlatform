package br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto;

import br.com.giorgetti.games.squareplatform.gameobjects.interaction.auto.AutoInteractiveSprite;
import br.com.giorgetti.games.squareplatform.gamestate.levels.LevelStateManager;

import java.awt.*;

/**
 * Sprite that causes a level to end and trigger level state manager to load
 * the next level. This is still in progress and need an image or some better
 * implementation.
 */
public class EndLevel extends AutoInteractiveSprite {

    public EndLevel() {
        this.width = 20;
        this.height = 26;
    }
    @Override
    public void executeInteraction() {
        LevelStateManager.getInstance().nextLevel();
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.white);
        g.setBackground(Color.yellow);
        Font origFont = g.getFont();
        g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        g.drawString("TROPHY", getLeftX()+getHalfWidth()/2, getTopY()+getHalfHeight());
        g.setFont(origFont);
        g.drawRoundRect(getLeftX(), getTopY(), getWidth(), getHeight(), 6, 6);

    }

}
