package net.thedanpage.game.framework;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * A sound object, used to simply the unfortunately complex audio system included with Java. Thanks oracle <333
 * 
 * @author Dan
 *
 */
public class Sound {
	
	private static Random random = new Random();
	
	/**
	 * A list of clips that could potentially be played. If the sound is not randomized, then this list will only contain one clip.
	 */
	private List<Clip> clips = new ArrayList<Clip>();
	
	/**
	 * Controls whether the sound will be randomized from a list or not.
	 */
	private boolean isRandomized;
	
	/**
	 * Loads the sound class, based on a string relative to the resources/sounds folder.<br><br>
	 * 
	 * Example: <code>public Sound("player/jump");</code>
	 * 
	 * @param fileNames
	 */
	public Sound(String fileName) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(Util.getResourceAsStream("sounds/" + fileName + ".wav"))));
			this.clips.add(clip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.isRandomized = false;
	}
	
	/**
	 * Loads the sound class, based on a string of file names relative to the resources/sounds folder.
	 * This method will load multiple sounds and select a random one upon being played.<br><br>
	 * 
	 * Example: <code>public Sound(new String[]{"player/footsteps_1", "player/footsteps_2"});</code>
	 * 
	 * @param fileNames
	 */
	public Sound(String[] fileNames) {
		try {
			for (String fileName : fileNames) {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(Util.getResourceAsStream("sounds/" + fileName + ".wav"))));
				this.clips.add(clip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.isRandomized = true;
	}
	
	/**
	 * Play the sound at its original volume.
	 */
	public void play() {
		if (this.isRandomized) {
			int rand = random.nextInt(this.clips.size());
			this.clips.get(rand).setMicrosecondPosition(0);
			this.clips.get(rand).start();
		} else {
			this.clips.get(0).setMicrosecondPosition(0);
			this.clips.get(0).start();
		}
	}
	
	/**
	 * Play the sound at a specific volume rather than the volume it's already at
	 * 
	 * @param volume Any double from 0 to 1, with 1 being normal volume
	 */
	public void play(double volume) {
		if (this.isRandomized) {
			int rand = random.nextInt(this.clips.size());
			
			// Set volume
			if (volume < 0f || volume > 1f)
		        throw new IllegalArgumentException("Volume not valid: " + volume);
		    FloatControl gainControl = (FloatControl) this.clips.get(rand).getControl(FloatControl.Type.MASTER_GAIN);        
		    gainControl.setValue(20f * (float) Math.log10(volume));
			
			// Play clip
			this.clips.get(rand).setMicrosecondPosition(0);
			this.clips.get(rand).start();
		} else {
			// Set volume
			if (volume < 0f || volume > 1f)
		        throw new IllegalArgumentException("Volume not valid: " + volume);
		    FloatControl gainControl = (FloatControl) this.clips.get(0).getControl(FloatControl.Type.MASTER_GAIN);        
		    gainControl.setValue(20f * (float) Math.log10(volume));
			
			// Play clip
			this.clips.get(0).setMicrosecondPosition(0);
			this.clips.get(0).start();
		}
	}

}
