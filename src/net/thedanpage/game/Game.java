package net.thedanpage.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import net.thedanpage.game.framework.Constants;
import net.thedanpage.game.framework.Sounds;
import net.thedanpage.game.framework.Util;
import net.thedanpage.game.framework.input.Keyboard;
import net.thedanpage.game.graphics.Fonts;
import net.thedanpage.game.graphics.Screen;
import net.thedanpage.game.graphics.Textures;
import net.thedanpage.game.world.map.Map;
import net.thedanpage.game.world.map.block.Blocks;

/**
 * This is the main class of the game, which controls the game look, and runs
 * the main update and render functions. The application window is set up here
 * as well.
 * 
 * @author Dan
 *
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	// Defining basic information regarding the properties of the game window.

	/**
	 * The width of the game window
	 */
	public static final int WIDTH = 400;

	/**
	 * The height of the game window, calculated relative to the window's width
	 */
	public static final int HEIGHT = WIDTH / 16 * 9;

	/**
	 * This is how large the game's pixels should be scaled. For example, a scale of
	 * 4 would mean that each pixel in {@link pixels} will appear as a 4x4 square of
	 * pixels when rendered.
	 */
	public static final int SCALE = 3;

	/**
	 * The title used for the application window.
	 */
	public static final String TITLE = "2D Game";

	/**
	 * The thread that the game will run on. A separate thread is used so that it
	 * can be stopped directly from the code in a clean way.
	 */
	private Thread thread;

	/**
	 * The JFrame that the game is rendered onto. {@link Game} extends
	 * {@link java.awt.Canvas}, so it will be directly drawn onto the frame.
	 */
	private JFrame frame;

	/**
	 * The game's instance of the {@link Keyboard} class, used to handle input.
	 */
	public static Keyboard keyboard;

	/**
	 * Controls whether the game loop is running or not.
	 */
	private boolean running = false;

	/**
	 * The time between updates, used to make sure that operations in the world
	 * happen at a steady rate, regardless of lag.
	 */
	private static double delta;

	/**
	 * Counts up by 1 each time the game updates. Currently used only for the block
	 * animation timer.
	 */
	private static int ticks = 0;

	/**
	 * The game's {@link Screen} object, which will have pixels drawn directly to
	 * it. It will then pass its pixel data to the game window in the main
	 * {@link #render()} function.
	 */
	public static Screen screen;

	/**
	 * The BufferedImage used to display the pixel data on the JFrame.
	 */
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	/**
	 * The integer array that holds RGB values for the game, which are then passed
	 * directly to {@link #image}.
	 */
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	// Used for accessing this info when text is displayed in-game
	private static int gameUps, gameFps;

	/**
	 * The main game window, which contains the JFrame, the {@link Screen} object,
	 * and the key listener.
	 */
	public Game() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);

		screen = new Screen(WIDTH, HEIGHT);
		frame = new JFrame();
		keyboard = new Keyboard();

		addKeyListener(keyboard);
	}

	/**
	 * Starts the game's thread. {@link #run()} is where all the cool stuff happens,
	 * there's not much exciting going on here.
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	/**
	 * Stops the game thread.
	 */
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aside from the initialization, I'm not really sure how most of this works on
	 * a technical level. The code was copied from a youtube video a couple months
	 * ago and I lost the link. ¯\_()_/¯ However, its purpose is to render the
	 * screen as frequently as possible, and update the game exactly 60 times per
	 * second.
	 */
	public void run() {

		// Initialization
		Util.init();
		Textures.init();
		Blocks.loadProperties();
		Map.init();
		Sounds.init();
		Fonts.init();

		// End of initialization
		System.out.println("Initialization complete.");

		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		delta = 0;
		int frames = 0;
		int updates = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			// Render the screen
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				// Update the loop timer
				timer += 1000;

				/*
				 * Update the variables that keep track of the game's updates per second and
				 * FPS. These are used with public getters and setters so that the game can draw
				 * them to the screen.
				 */
				gameUps = updates;
				gameFps = frames;

				// Reset the timer's update and frame counts
				updates = 0;
				frames = 0;
			}
		}

		stop();
	}

	public void update() {

		// Update the keyboard class (for I/O)
		keyboard.update();

		// Update the map. This is everything that has to do with the world and the
		// player.
		Map.update();

		// Increase the game's update count tracker by 1
		ticks++;

	}

	/**
	 * Reads the pixel data from {@link #pixels} and renders it to the game window.
	 */
	public void render() {
		// Not too sure what this does. I copied it from the youtube video that the game
		// loop code came from.
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// Clean the screen before drawing to it again
		screen.clear();

		// Render!
		screen.render();

		// Copy the pixel data from the screen object to the pixel data in this class
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		// Draw the BufferedImage to this class (remember, it extends Canvas)
		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		g.dispose();
		bs.show();
	}

	/**
	 * @return time since the last game update
	 */
	public static double getDelta() {
		return delta * Constants.DELTA_MULT;
	}

	/**
	 * @return most recent updates per second
	 */
	public static int getCurrentUps() {
		return gameUps;
	}

	/**
	 * @return most recent frames per second
	 */
	public static int getCurrentFps() {
		return gameFps;
	}

	/**
	 * @return the number of total game updates that have elapsed
	 */
	public static int getTicks() {
		return ticks;
	}

	/**
	 * A WindowFocusListener used to release all keys when the window has lost focus
	 */
	private static WindowFocusListener lostFocusAction = new WindowFocusListener() {
		@Override
		public void windowLostFocus(WindowEvent e) {
			keyboard.releaseAll();
		}

		@Override
		public void windowGainedFocus(WindowEvent e) {
		}
	};

	// Instantiate the game, set the JFrame properties, and start it running
	public static void main(String[] args) {
		Game game = new Game();

		game.frame.setResizable(false);
		game.frame.setTitle(TITLE);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.frame.setAlwaysOnTop(true);
		game.frame.setAlwaysOnTop(false);

		game.frame.addWindowFocusListener(lostFocusAction);

		game.start();
	}
}
