package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import br.com.giorgetti.games.squareplatform.gameobjects.Animation;
import br.com.giorgetti.games.squareplatform.main.GamePanel;
import br.com.giorgetti.games.squareplatform.media.MediaPlayer;

import java.awt.*;

/**
 * Created by fgiorgetti on 08/07/17.
 */
public class Coin extends AutoInteractiveSprite {

    private static MediaPlayer mediaPlayer = new MediaPlayer("/sounds/coin.wav", MediaPlayer.MediaType.SFX);
    private final int HEIGHT = 20;
    private final int WIDTH = 14;
    private final int SCORE = 10;

    public Coin() {

        this.width = WIDTH;
        this.height = HEIGHT;

        loadAnimation(Animation.newAnimation(Coin.class.getSimpleName(),
                "/sprites/objects/coin.png", 14).delay(100));

        setAnimation(Coin.class.getSimpleName());

    }

    @Override
    protected int getJumpSpeed() {
        return 10;
    }

    public Coin(int x, int y) {
        this();
        this.x = x;
        this.y = y;
        this.xSpeed = 1;
        jump();
    }

    @Override
    public void executeInteraction() {
        mediaPlayer.play(false);
        map.getPlayer().addScore(SCORE);
        map.removeSprite(this);
        mediaPlayer.remove();
    }

    @Override
    public void draw(Graphics2D g) {

        //System.out.printf("X=%d/Y=%d --- MAP X=%d/Y=%d\n", getX(), getY(), map.getX(), map.getY());
        g.drawImage(getCurrentAnimation(),
                    getX() - getHalfWidth() - map.getX(),
                    GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(),
                    getWidth(), getHeight(), null);

        /*
        g.setColor(Color.black);
        g.fillOval(getX() - getHalfWidth() - map.getX() - 1, GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY() + 1, WIDTH, HEIGHT);
        g.setColor(Color.yellow);
        g.fillOval(getX() - getHalfWidth() - map.getX(), GamePanel.HEIGHT - getY() - getHalfHeight() + map.getY(), WIDTH, HEIGHT);
        */

    }

}
