package martin.ringenberg.game.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Kukeke11 on 06/14/15.
 */
public class SpriteSheet
{
    public String path;
    public int width;
    public int height;

    public int[] pixels;

    public SpriteSheet(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image == null)
        {
            return;
        }

        this.path = path;
        this.height = image.getHeight();
        this.width = image.getWidth();

        pixels = image.getRGB(0,0,width,height,null,0,width);

        for (int i = 0; i< pixels.length; i++)
        {
            pixels[i] = (pixels[i] & 0xff)/64;
        }

        for (int i = 0; i<8; i++)
        {
            System.out.println(pixels[i]);
        }
    }
}
