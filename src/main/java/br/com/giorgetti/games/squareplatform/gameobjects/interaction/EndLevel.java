package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gamestate.levels.LevelStateManager;

import java.awt.*;

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
