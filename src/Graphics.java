import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * This is a class
 * Created 2020-03-25
 *
 * @author Magnus Silverdal
 */
public class Graphics extends Canvas implements Runnable {
    private String title = "Graphics";
    private int width;
    private int height;

    private JFrame frame;
    private BufferedImage image;
    private int[] pixels;

    private Thread thread;
    private boolean running = false;
    private int fps = 60;
    private int ups = 20;

    private final double[] sine = {
            0.000000000,  0.017452406,  0.034899497,  0.052335956,  0.069756474,  0.087155743,
            0.104528463,  0.121869343,  0.139173101,  0.156434465,  0.173648178,  0.190808995,
            0.207911691,  0.224951054,  0.241921896,  0.258819045,  0.275637356,  0.292371705,
            0.309016994,  0.325568154,  0.342020143,  0.358367950,  0.374606593,  0.390731128,
            0.406736643,  0.422618262,  0.438371147,  0.453990500,  0.469471563,  0.484809620,
            0.500000000,  0.515038075,  0.529919264,  0.544639035,  0.559192903,  0.573576436,
            0.587785252,  0.601815023,  0.615661475,  0.629320391,  0.642787610,  0.656059029,
            0.669130606,  0.681998360,  0.694658370,  0.707106781,  0.719339800,  0.731353702,
            0.743144825,  0.754709580,  0.766044443,  0.777145961,  0.788010754,  0.798635510,
            0.809016994,  0.819152044,  0.829037573,  0.838670568,  0.848048096,  0.857167301,
            0.866025404,  0.874619707,  0.882947593,  0.891006524,  0.898794046,  0.906307787,
            0.913545458,  0.920504853,  0.927183855,  0.933580426,  0.939692621,  0.945518576,
            0.951056516,  0.956304756,  0.961261696,  0.965925826,  0.970295726,  0.974370065,
            0.978147601,  0.981627183,  0.984807753,  0.987688341,  0.990268069,  0.992546152,
            0.994521895,  0.996194698,  0.997564050,  0.998629535,  0.999390827,  0.999847695,
            1.000000000,  0.999847695,  0.999390827,  0.998629535,  0.997564050,  0.996194698,
            0.994521895,  0.992546152,  0.990268069,  0.987688341,  0.984807753,  0.981627183,
            0.978147601,  0.974370065,  0.970295726,  0.965925826,  0.961261696,  0.956304756,
            0.951056516,  0.945518576,  0.939692621,  0.933580426,  0.927183855,  0.920504853,
            0.913545458,  0.906307787,  0.898794046,  0.891006524,  0.882947593,  0.874619707,
            0.866025404,  0.857167301,  0.848048096,  0.838670568,  0.829037573,  0.819152044,
            0.809016994,  0.798635510,  0.788010754,  0.777145961,  0.766044443,  0.754709580,
            0.743144825,  0.731353702,  0.719339800,  0.707106781,  0.694658370,  0.681998360,
            0.669130606,  0.656059029,  0.642787610,  0.629320391,  0.615661475,  0.601815023,
            0.587785252,  0.573576436,  0.559192903,  0.544639035,  0.529919264,  0.515038075,
            0.500000000,  0.484809620,  0.469471563,  0.453990500,  0.438371147,  0.422618262,
            0.406736643,  0.390731128,  0.374606593,  0.358367950,  0.342020143,  0.325568154,
            0.309016994,  0.292371705,  0.275637356,  0.258819045,  0.241921896,  0.224951054,
            0.207911691,  0.190808995,  0.173648178,  0.156434465,  0.139173101,  0.121869343,
            0.104528463,  0.087155743,  0.069756474,  0.052335956,  0.034899497,  0.017452406,
            0.000000000, -0.017452406, -0.034899497, -0.052335956, -0.069756474, -0.087155743,
           -0.104528463, -0.121869343, -0.139173101, -0.156434465, -0.173648178, -0.190808995,
           -0.207911691, -0.224951054, -0.241921896, -0.258819045, -0.275637356, -0.292371705,
           -0.309016994, -0.325568154, -0.342020143, -0.358367950, -0.374606593, -0.390731128,
           -0.406736643, -0.422618262, -0.438371147, -0.453990500, -0.469471563, -0.484809620,
           -0.500000000, -0.515038075, -0.529919264, -0.544639035, -0.559192903, -0.573576436,
           -0.587785252, -0.601815023, -0.615661475, -0.629320391, -0.642787610, -0.656059029,
           -0.669130606, -0.681998360, -0.694658370, -0.707106781, -0.719339800, -0.731353702,
           -0.743144825, -0.754709580, -0.766044443, -0.777145961, -0.788010754, -0.798635510,
           -0.809016994, -0.819152044, -0.829037573, -0.838670568, -0.848048096, -0.857167301,
           -0.866025404, -0.874619707, -0.882947593, -0.891006524, -0.898794046, -0.906307787,
           -0.913545458, -0.920504853, -0.927183855, -0.933580426, -0.939692621, -0.945518576,
           -0.951056516, -0.956304756, -0.961261696, -0.965925826, -0.970295726, -0.974370065,
           -0.978147601, -0.981627183, -0.984807753, -0.987688341, -0.990268069, -0.992546152,
           -0.994521895, -0.996194698, -0.997564050, -0.998629535, -0.999390827, -0.999847695,
           -1.000000000, -0.999847695, -0.999390827, -0.998629535, -0.997564050, -0.996194698,
           -0.994521895, -0.992546152, -0.990268069, -0.987688341, -0.984807753, -0.981627183,
           -0.978147601, -0.974370065, -0.970295726, -0.965925826, -0.961261696, -0.956304756,
           -0.951056516, -0.945518576, -0.939692621, -0.933580426, -0.927183855, -0.920504853,
           -0.913545458, -0.906307787, -0.898794046, -0.891006524, -0.882947593, -0.874619707,
           -0.866025404, -0.857167301, -0.848048096, -0.838670568, -0.829037573, -0.819152044,
           -0.809016994, -0.798635510, -0.788010754, -0.777145961, -0.766044443, -0.754709580,
           -0.743144825, -0.731353702, -0.719339800, -0.707106781, -0.694658370, -0.681998360,
           -0.669130606, -0.656059029, -0.642787610, -0.629320391, -0.615661475, -0.601815023,
           -0.587785252, -0.573576436, -0.559192903, -0.544639035, -0.529919264, -0.515038075,
           -0.500000000, -0.484809620, -0.469471563, -0.453990500, -0.438371147, -0.422618262,
           -0.406736643, -0.390731128, -0.374606593, -0.358367950, -0.342020143, -0.325568154,
           -0.309016994, -0.292371705, -0.275637356, -0.258819045, -0.241921896, -0.224951054,
           -0.207911691, -0.190808995, -0.173648178, -0.156434465, -0.139173101, -0.121869343,
           -0.104528463, -0.087155743, -0.069756474, -0.052335956, -0.034899497, -0.017452406
    };

    private Rider rider;
    private boolean keyRight;
    private boolean keyLeft;
    private boolean keyUp;
    private boolean keyDown;

    public Graphics(int w, int h) {
        this.width = w;
        this.height = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        frame = new JFrame();
        frame.setTitle(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();

        rider = new Rider(32, 32);
        rider.setGroundLevel((int) f(width/2.)[1], false);
        rider.x = (int) (width/2.) - rider.getWidth()/2;

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keyUp = true;
                        break;

                    case KeyEvent.VK_DOWN:
                        keyDown = true;
                        break;

                    case KeyEvent.VK_LEFT:
                        keyLeft = true;
                        break;

                    case KeyEvent.VK_RIGHT:
                        keyRight = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keyUp = false;
                        break;

                    case KeyEvent.VK_DOWN:
                        keyDown = false;
                        break;

                    case KeyEvent.VK_LEFT:
                        keyLeft = false;
                        break;

                    case KeyEvent.VK_RIGHT:
                        keyRight = false;
                        break;
                }
            }
        });
    }

    private void draw() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        java.awt.Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    private double[] f(double x) {
        double y = ( sin((int) ((x + 10 * t))) * 100
                + sin((int) ((x + 5 * t + 50)/2)) * 100
                + sin((int) ((x + -10 * t + 200)/3)) * 200 ) / 2
                + height / 2f;

        return new double[] {
                    y + 250,
                    y + 100,
                    y,
                    y - 75,
                    y - 150,
                };
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private double sin(int x) {
        if(x < 0) {
            x = 180 + Math.abs(x);
        }
        return sine[x % sine.length];
    }

    private int t = 0;

    private void update() {
        for (int i = 0; i < pixels.length; i++) {
            int x = i % width;
            int y = (int) Math.ceil(i / (float) width);

            pixels[i] = 0;
            for(double _y : f(x)) {
                double distance = Math.abs(y - _y);
                // int val = (int) Math.round(255 / (distance == 0 ? 1 : distance));
                // int val = (int) Math.round(255 % (distance == 0 ? 1 : distance));
                int val = y < _y ? 30 : 0;
                pixels[i] += rgb(val, val, val);
            }
        }

        if(keyUp) {
            rider.z = Math.max(rider.z - 10, -500);
        }
        if(keyDown) {
            rider.z = Math.min(rider.z + 10, 300);
        }
        if(keyRight) {
            rider.x += 5;
        }
        if(keyLeft) {
            rider.x -= 5;
        }

        System.out.println(rider.z);

        rider.setGroundLevel((int) f(rider.x)[1], true);

        rider.update();

        int sx = rider.getDisplayX();
        int sy = rider.level - rider.shadow.getHeight()/2 + rider.z/2;
        for (int i = 0 ; i < rider.shadow.getHeight() ; i++) {
            for (int j = 0 ; j < rider.shadow.getWidth() ; j++) {
                int index = (sy + i)*width + sx + j;
                if(index < 0 || index > pixels.length - 1) continue;

                pixels[index] -= 0x111111;
            }
        }

        for (int i = 0 ; i < rider.getHeight() ; i++) {
            for (int j = 0 ; j < rider.getWidth() ; j++) {
                int x = rider.getDisplayX();
                int y = rider.getDisplayY();

                int index = (y + i)*width + x + j;
                if(index < 0 || index > pixels.length - 1) continue;
                pixels[index] = rider.getPixels()[i*rider.getWidth()+j];
            }
        }

        t++;
    }

    public int rgb(int r, int g, int b) {
        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double frameUpdateinteval = 1000000000.0 / fps;
        double stateUpdateinteval = 1000000000.0 / ups;
        double deltaFrame = 0;
        double deltaUpdate = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            deltaFrame += (now - lastTime) / frameUpdateinteval;
            deltaUpdate += (now - lastTime) / stateUpdateinteval;
            lastTime = now;

            /*while (deltaUpdate >= 1) {
                update();
                deltaUpdate--;
            }*/

            while (deltaFrame >= 1) {
                update();
                draw();
                deltaFrame--;
            }
        }
        stop();
    }

}
