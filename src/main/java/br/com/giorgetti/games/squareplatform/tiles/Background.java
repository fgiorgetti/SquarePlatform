package br.com.giorgetti.games.squareplatform.tiles;

import br.com.giorgetti.games.squareplatform.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * Created by fgiorgetti on 5/9/15.
 */
public class Background {

    private String bgPath;
    private BufferedImage image;
    private int speedPct;

    public Background(String bgPath, String speedPct) {

        try {

            this.bgPath = bgPath;
            this.image = ImageIO.read(getClass().getResourceAsStream(bgPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.speedPct = Integer.parseInt(speedPct);

    }

    public void draw(Graphics2D g, int mapX, int mapY) {

        int bgWidth = getImage().getWidth();
        int bgHeigth = GamePanel.HEIGHT - getImage().getHeight();

        int bgX = (int) (- mapX / 100.00 * getSpeedPct() % bgWidth);
        int bgY = (int) ( mapY / 100.00 * (getSpeedPct()/2) % bgHeigth);
        //System.out.printf("bgX = %d - bgY = %d\n", bgX, bgY);

        // Consider X offset as map.x / 100 * bg.speed
        g.drawImage(getImage(), null, bgX, bgY);
        g.drawImage(getImage(), null, bgX + bgWidth, bgY);

        g.drawImage(getImage(), null, bgX, bgY + bgHeigth);
        g.drawImage(getImage(), null, bgX + bgWidth, bgY + bgHeigth);

    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getSpeedPct() {
        return speedPct;
    }

    public void setSpeedPct(int speedPct) { this.speedPct = speedPct; }

    public String toString() {
        return speedPct + TileMap.PROP_SEPARATOR + bgPath;
    }

}
