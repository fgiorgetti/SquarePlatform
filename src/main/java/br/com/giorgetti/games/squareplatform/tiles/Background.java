package br.com.giorgetti.games.squareplatform.tiles;

import javax.imageio.ImageIO;
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

    private BufferedImage image;
    private int speedPct;

    public Background(String bgPath, String speedPct) {

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(bgPath));
        } catch (IOException e) {}
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

    public void setSpeedPct(int speedPct) {
        this.speedPct = speedPct;
    }
}
