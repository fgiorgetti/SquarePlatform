package br.com.giorgetti.games.squareplatform.tiles;

import br.com.giorgetti.games.squareplatform.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

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
            System.out.printf("BG WIDTH = %d - HEIGHT = %d\n", this.image.getWidth(), this.image.getHeight());

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
