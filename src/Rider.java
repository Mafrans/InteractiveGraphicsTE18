import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Rider extends Sprite {
    private double yVelocity;
    private int[] pixels;
    public int level;
    public Sprite shadow;
    public int x = 0;
    public int y = 0;
    public int z = 0;
    public static final double gravity = 9.82/10;
    public Rider(int w, int h) {
        super(w, h);

        shadow = new Sprite(32, 24);
        pixels = new int[getWidth()*getHeight()];

        BufferedImage image = null;
        try {
            BufferedImage rawImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("rider.png"));
            // Since the type of image is unknown it must be copied into an INT_RGB
            image = new BufferedImage(rawImage.getWidth(), rawImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(rawImage, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void setGroundLevel(int level, boolean addMomentum) {
        if(addMomentum && level < y) {
            yVelocity += y - level;
        }
        else if(!addMomentum) {
            y = level;
        }
        this.level = level;
    }

    @Override
    public int[] getPixels() {
        return pixels;
    }

    public int getDisplayX() {
        return x - getWidth()/2;
    }

    public int getDisplayY() {
        return y - getHeight() + z/2;
    }

    public void update() {
        yVelocity -= gravity;

        y -= yVelocity;
        if(y > level) {
            y = level;
            yVelocity = Math.max(0, yVelocity);
        }
    }
}
