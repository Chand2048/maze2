package com.anderson;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Canvas {

    private static final long serialVersionUID = 1L;

    // add some system property control flags. Setting these flags as "True"
    // with a captial will notify you via the console if the particular
    // pipeline D3D or Opengl has been successfully enabled. Using "true" all
    // lowercase will turn it on silently, you can still find out if it was
    // successful using the trace property as it will show calls to either D3D
    // or opengl, or neither.
    //
    // NOTE: trace does not seem to work for opengl with jdk1.6.0_20
    static {
        System.setProperty("sun.java2d.trace", "timestamp,log,count");
        System.setProperty("sun.java2d.transaccel", "True");
        System.setProperty("sun.java2d.opengl", "True");
        // System.setProperty("sun.java2d.d3d", "false"); //default on windows
        // System.setProperty("sun.java2d.ddforcevram", "true");
    }

    private static final String TITLE = "GPSnippets: BufferStrategy Snippet";

    // the strategy used for double buffering, or any number of buffered frames.
    private BufferStrategy strategy;
    private final Timer timer;
    private TimerTask renderTask;
    private View view;
    private int height;
    private int width;

    public Main(int width, int height) {
        this.height = height;
        this.width = width;

        // we will be doing our own rendering, using the strategy.
        this.setIgnoreRepaint(true);
        this.view = new View(this.width / 3, this.height / 3);

        timer = new Timer(); // used for the render thread

        // add quick and dirty 2-directional movement for our block
        // addKeyListener(new KeyAdapter() {

        // @Override
        // public void keyPressed(KeyEvent e) {
        // if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        // right = true;
        // }

        // if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        // left = true;
        // }
        // }

        // @Override
        // public void keyReleased(KeyEvent e) {
        // if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        // right = false;
        // }

        // if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        // left = false;
        // }
        // }
        // });
    }

    public void reset_path() {
        this.view.reset_path();
    }
    public void reset_land() {
        this.view.reset_land();
    }

    /**
     * Our drawing function which utilizes the Bu bkG = (Graphics2D)
     * strategy.getDrawGraphics();fferStrategy that enables us to do offscreen
     * rendering without having to wait for swing to repaint, the component. It also
     * eliminates flickering and splicing issues.
     */
    public void render() {
        try {
            Graphics2D bkG = (Graphics2D) strategy.getDrawGraphics();
            bkG.setColor(Color.BLACK);
            bkG.fillRect(0, 0, getWidth(), getHeight());
            this.view.draw(bkG, this.width, this.height);
            bkG.dispose();

            // flip/draw the backbuffer to the canvas component.
            strategy.show();

            // synchronize with the display refresh rate.
            Toolkit.getDefaultToolkit().sync();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            String s = e.getStackTrace().toString();
            System.out.println(s);
        }
    }

    /**
     * It is necessary to wait until after the component has been displayed in order
     * to create and retrieve the buffer strategy. This is the part that took me the
     * longest to figure out. But it makes sense since the component requires native
     * resources in order to perform hardware acceleration and those resources are
     * handled by the component itself, and are only available once the component is
     * created and displayed.
     */
    public void setup() {
        // create a strategy that uses two buffers, or is double buffered.
        this.createBufferStrategy(2);

        // get a reference to the strategy object, for use in our render method
        // this isn't necessary but it eliminates a call during rendering.
        strategy = this.getBufferStrategy();
        start();
    }

    private class RenderTask extends TimerTask {
        // private long lasttime = System.currentTimeMillis();
        private View view;

        public RenderTask(View view) {
            super();
            this.view = view;
        }

        @Override
        public void run() {

            // get the current system time
            // long time = System.currentTimeMillis();

            // calculate the time passed in milliseconds
            // double dt = (time - lasttime) * 0.001;

            // save the current time
            // lasttime = time;

            // Move based on time delay
            this.view.next_frame();

            render();
        }
    }

    /**
     * Initialize the render and update tasks, to call the render method, do timing
     * and FPS counting, handling input and canceling existing tasks.
     */
    public void start() {
        // if the render task is already running stop it, this may cause an
        // exception to be thrown if the task is already canceled.
        if (this.renderTask != null) {
            this.renderTask.cancel();
        }

        // our main task for handling the rendering and for updating and
        // handling input and movement events. The timer class isn't the most
        // reliable for game updating and rendering but it will suffice for the
        // purpose of this snippet.
        this.renderTask = new RenderTask(this.view);

        // These will cap our frame rate but give us unexpected results if our
        // rendering or updates take longer than the 'period' time. It
        // is likely that we could have overlapping calls.
        timer.schedule(this.renderTask, 0, 16);
    }

    /**
     * Stops the rendering cycle so that the application can close gracefully.
     */
    protected void stop() {
        this.renderTask.cancel();
    }

    /**
     * Creates a Frame and adds a new canvas to the Frame, displays it and
     * initializes the rendering method.
     */
    protected static void createAndDisplay(int width, int height) {
        // Never mix swing and awt, since we use a canvas to utilize the
        // buffered strategy we will put the canvas in a Frame instead of a
        // JFrame.
        final Frame frame = new Frame(TITLE);
        frame.setLayout(new BorderLayout());
        frame.setUndecorated(true);
        final Main canvas = new Main(width, height);
        frame.add(canvas);

        // convenience exiting from the demo using the ESCAPE key.
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    canvas.stop(); // first stop the drawing and updating
                    frame.setVisible(false); // hide the window quickly
                    frame.dispose(); // release all system resources
                    System.exit(0); // finally exit.
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    canvas.reset_land();
                    canvas.invalidate();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    canvas.reset_path();
                    canvas.invalidate();
                }
            }
        });

        // need this to trap when the user attempts to close the window using
        // the close icon for the window, or the close option from the window
        // menu or alt+f4 or by other means.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                canvas.stop(); // first stop the drawing and updating
                frame.setVisible(false); // hide the window quickly
                frame.dispose(); // release all system resources
                System.exit(0); // finally exit.
            }
        });

        frame.setSize(width, height); // should use configurable properties here
        frame.setLocationRelativeTo(null); // centers window on screen
        frame.setVisible(true); // creates and displays the actual window

        // this is our scene setup to initialize all necessary configurable
        // objects and properties. Using a setup method helps control the way
        // things look from a single location, it can be extended to include
        // how things act as well.
        canvas.setup();
    }

    /**
     * Calls the swing event thread to create and display a new application frame.
     * This is done so that the setVisible method is not part of the main
     * application thread but is done within the swing event thread.
     *
     * @param args
     */
    public static void main(String[] args) {
        // create and display the window.
        Rectangle bounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        createAndDisplay((int)(bounds.height * .9), (int)(bounds.height * .9));
    }
}
