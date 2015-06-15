package martin.ringenberg.JavaEngine;

/**
 * Created by Kukeke11 on 06/14/15.
 */

import martin.ringenberg.game.gfx.Screen;
import martin.ringenberg.game.gfx.SpriteSheet;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 3929690636460222084L;
    public static final int WIDTH = 160;
    public static final int HEIGHT = 117;
    public static final int SCALE = 3;
    public static final String NAME = "Game";


    private JFrame frame;

    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(160, 117, 1);
    private int[] pixels;

    private Screen screen;



    public Game()
    {
        this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
        this.setMinimumSize(new Dimension(480, 351));
        this.setMaximumSize(new Dimension(480, 351));
        this.setPreferredSize(new Dimension(480, 351));

        this.frame = new JFrame(NAME);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());

        this.frame.add(this, "Center");
        this.frame.pack();

        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo((Component)null);
        this.frame.setVisible(true);
    }

    public void init()
    {
        screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("/SpriteSheet.png"));
    }

    public synchronized void start()
    {
        this.running = true;
        (new Thread(this)).start();
    }

    public synchronized void stop()
    {
        this.running = false;
    }

    public void run()
    {
        long lastTime = System.nanoTime();
        double nsPerTick = 1.6666666666666666E7D;
        int frames = 0;
        int ticks = 0;
        long lastTimer = System.currentTimeMillis();
        double delta = 0.0D;

        init();

        while(this.running)
        {
            long now = System.nanoTime();
            delta += (double)(now - lastTime) / nsPerTick;
            lastTime = now;

            boolean shouldRender;
            for(shouldRender = true; delta >= 1.0D; shouldRender = true)
            {
                ++ticks;
                this.tick();
                --delta;
            }

            try
            {
                Thread.sleep(2L);
            } catch (InterruptedException var15) {
                var15.printStackTrace();
            }

            if(shouldRender)
            {
                ++frames;
                this.render();
            }

            if(System.currentTimeMillis() - lastTimer >= 1000L)
            {
                lastTimer += 1000L;
                System.out.println("frames "+frames + ", ticks " + ticks);
                frames = 0;
                ticks = 0;
            }
        }

    }

    public void tick()
    {
        ++this.tickCount;

        for(int i = 0; i < this.pixels.length; ++i)
        {
            this.pixels[i] = i + this.tickCount;
        }

    }

    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        screen.render(pixels, 0, WIDTH);

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), (ImageObserver)null);

        g.dispose();
        bs.show();

    }

    public static void main(String[] args)
    {
        (new Game()).start();
    }
}
