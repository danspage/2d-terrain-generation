package net.thedanpage.game.framework;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * An audio clip class, used to interface with the javax.sound library
 * 
 * @author Dan
 *
 */
class AudioClip {

	// A javax clip
	private Clip clip;

	/**
	 * Creates an audio clip from a path relatative to the resources folder,
	 * excluding the file extension
	 * 
	 * @param filename
	 */
	public AudioClip(String filename) {
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem
					.getAudioInputStream(new BufferedInputStream(Util.getResourceAsStream(filename + ".wav"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the volume of the clip, with 0 being silent and 1 being normal volume
	 */
	public void setVolume(double volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
	}

	/** Plays the clip */
	public void start() {
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
	/** Stops the clip if it's playing */
	public void stop() {
		clip.stop();
	}
	
	/** Plays the clip in a loop until it's stopped */
	public void loop() {
		new Thread() {
			public void run() {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				try {
					Thread.sleep(999999999);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
