package br.com.giorgetti.games.squareplatform.tiles;

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
