import javax.sound.sampled.AudioInputStream;		// for playing sound clips
import javax.sound.sampled.*;
import java.io.*;

import java.util.HashMap;				// for storing sound clips


public class SoundManager {				// a Singleton class
	HashMap<String, Clip> clips;

   	// Clip hitClip = null;				// played when bat hits ball
   	// Clip appearClip = null;				// played when ball is re-generated
   	// Clip backgroundClip = null;			// played continuously after ball is created

	private static SoundManager instance = null;	// keeps track of Singleton instance

	private SoundManager () {
		clips = new HashMap<String, Clip>();

		Clip clip = loadClip("sounds/level1background.wav");
		clips.put("l1background", clip);		// level 1 background theme sound

		clip = loadClip("sounds/level2background.wav");
		clips.put("l2background", clip);		// level 2background theme sound

		clip = loadClip("sounds/nutritiousSound.wav");
		clips.put("nutritious", clip);			// played when player's sprite collides 
													// with nutritious sprite
		clip = loadClip("sounds/boyHurt.wav");
		clips.put("boyHurt", clip);			// played when player's sprite collides 
		
		clip = loadClip("sounds/knifePirateHurt.wav");
		clips.put("knifeHurt", clip);			// played when player's sprite collides 
		
		clip = loadClip("sounds/captainHurt.wav");
		clips.put("captainHurt", clip);			// played when player's sprite collides 
		
		clip = loadClip("sounds/BirdSound.wav");
		clips.put("bird", clip);				// played for bird pirate
	}


	public static SoundManager getInstance() {	// class method to get Singleton instance
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


	public Clip getClip (String title) {
		return clips.get(title);		// gets a sound by supplying key
	}


    public Clip loadClip (String fileName) {	// gets clip from the specified file
 		AudioInputStream audioIn;
		Clip clip = null;

		try {
    			File file = new File(fileName);
    			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
    			clip = AudioSystem.getClip();
    			clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("Error opening sound files: " + e);
		}
    		return clip;
    }


    public void playSound(String title, Boolean looping) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}
    }

	public void playSound(String title, boolean looping, long startTime) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.setFramePosition(0);
			clip.setMicrosecondPosition(startTime);
			if (looping){
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			else{
				clip.start();
			}
		}
	}
	
	
	public long getClipPosition(String title){
			Clip clip = getClip(title);
			if (clip != null) {
				return clip.getMicrosecondPosition();
			}
			return -1;
	}	


    public void stopSound(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
		}
    }
}