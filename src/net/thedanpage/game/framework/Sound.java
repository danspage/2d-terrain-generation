package net.thedanpage.game.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A sound object, used to simply the unfortunately complex audio system
 * included with Java. Thanks oracle <333
 * 
 * @author Dan
 *
 */
public class Sound {

	private static Random random = new Random();

	/**
	 * A list of clips that could potentially be played. If the sound is not
	 * randomized, then this list will only contain one clip.
	 */
	private List<AudioClip> clips = new ArrayList<AudioClip>();

	/**
	 * Controls whether the sound will be randomized from a list or not.
	 */
	private boolean isRandomized;

	/**
	 * Loads the sound class, based on a string relative to the resources/sounds
	 * folder.<br>
	 * <br>
	 * 
	 * Example: <code>public Sound("player/jump");</code>
	 * 
	 * @param fileName
	 */
	public Sound(String fileName) {
		try {
			AudioClip clip = new AudioClip("sounds/" + fileName);
			this.clips.add(clip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.isRandomized = false;
	}

	/**
	 * Loads the sound class, based on a string of file names relative to the
	 * resources/sounds folder. This method will load multiple sounds and select a
	 * random one upon being played. <br>
	 * <br>
	 * 
	 * Example:
	 * <code>public Sound(new String[]{"player/footsteps_1", "player/footsteps_2"});</code>
	 * 
	 * @param fileNames
	 */
	public Sound(String[] fileNames) {
		try {
			for (String fileName : fileNames) {
				AudioClip clip = new AudioClip("sounds/" + fileName);
				this.clips.add(clip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.isRandomized = true;
	}

	/**
	 * Play the sound at its original volume
	 */
	public void play() {

		if (this.isRandomized) {
			int rand = random.nextInt(this.clips.size());

			this.clips.get(rand).setVolume(1);
			this.clips.get(rand).start();
		} else {
			this.clips.get(0).setVolume(1);

			this.clips.get(0).start();
		}
	}

	/**
	 * Play the sound at a specific volume rather than the volume it's already at
	 * 
	 * @param volume Any double from 0 to 1, with 1 being normal volume
	 */
	public void play(double volume) {
		// Handle illegal arguments for the javax sound library
		if (volume < 0f || volume > 1f)
			throw new IllegalArgumentException("Volume not valid: " + volume);

		if (this.isRandomized) {
			int rand = random.nextInt(this.clips.size());

			this.clips.get(rand).setVolume(volume);
			this.clips.get(rand).start();
		} else {
			this.clips.get(0).setVolume(volume);
			this.clips.get(0).start();
		}
	}

	/** Plays the clip in a loop until it's stopped */
	public void loop() {
		for (AudioClip clip : clips) {
			clip.loop();
		}
	}

	/** Plays the clip in a loop until it's stopped, at a specified volume */
	public void loop(double volume) {
		for (AudioClip clip : clips) {
			clip.setVolume(volume);
			clip.loop();
		}
	}

	/**
	 * Plays all sounds at a volume of zero so that they are buffered and don't lag
	 * the first time
	 */
	public void init() {
		for (AudioClip clip : clips) {
			clip.start();
		}
	}

	/** Stops the sound if it's playing */
	public void stop() {
		for (AudioClip clip : clips) {
			clip.stop();
		}
	}
}
