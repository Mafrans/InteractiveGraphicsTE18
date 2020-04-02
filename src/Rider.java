import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
    public boolean onGround;
    private BufferedImage image;
    private BufferedImage rawImage;
    public Rider(int w, int h) {
        super(w, h);

        shadow = new Sprite(32, 24);
        pixels = new int[getWidth()*getHeight()];

        image = null;
        try {
            rawImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("rider.bmp"));
            // Since the type of image is unknown it must be copied into an INT_RGB
            image = new BufferedImage(rawImage.getWidth(), rawImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            image.getGraphics().drawImage(rawImage, 0, 0, null);


        } catch (IOException e) {
            e.printStackTrace();
        }
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void updateImage() {
        if(onGround) {
            double rotationRequired = Math.toRadians(-45);
            double locationX = image.getWidth() / 2;
            double locationY = image.getHeight() / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            image.getGraphics().clearRect(0, 0, image.getWidth(), image.getHeight());
            image.getGraphics().drawImage(op.filter(rawImage, null), 0, 0, null);
        }
        else {
            image.getGraphics().clearRect(0, 0, image.getWidth(), image.getHeight());
            image.getGraphics().drawImage(rawImage, 0, 0, null);
        }
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

            if(!onGround) {
                updateImage();
            }

            onGround = true;
        }
        else if(Math.abs(y - level) > 30 && yVelocity > 15) {
            if(onGround) {
                updateImage();
            }

            onGround = false;
        }
    }
}
