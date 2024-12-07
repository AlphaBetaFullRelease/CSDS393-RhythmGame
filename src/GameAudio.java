import java.io.File; 
import java.io.IOException; 
import java.io.UncheckedIOException;
import java.util.Scanner;

import javax.sound.sampled.*;

// plays audio (sfx & music) for gameplay
public class GameAudio {
    // stores the volume to play music at
    private float musicVolume;

    // stores current frame of the file (to track progress)
    private Long curFrame;

    // clip plays the loaded section of the song
    private Clip clip;

    // this streams in the audio file so the whole song doesn't need to be loaded at once
    private AudioInputStream audioInputStream; 

    // creates a new audio player
    public GameAudio(float musicVolume){
        this.musicVolume = ((float) musicVolume) / 100;
    }

    // loads a song (long audio file) from file path
    // this does not play the song
    public void loadSong(String path) {
        try{
            // create input stream at path specified
            audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());

            // create clip
            clip = AudioSystem.getClip(); 

            // set input stream into the clip
            clip.open(audioInputStream);

            // set clip volume
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            float gain = (range * musicVolume) + volumeControl.getMinimum();
            volumeControl.setValue(gain);

            // set clip to loop
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(IOException e){
            // notify user that if they pull from the github, they need to manually import audio files
            System.out.println("If you pulled this project from the github repo, please manually import the audio files or extract this project from a zip that contains the song files");
            throw new RuntimeException(e);
        }catch(LineUnavailableException | UnsupportedAudioFileException e){
            // wrap in runtime exception
            throw new RuntimeException(e);
        }
    }

    // starts the song playback
    public void startSong(){
        clip.start();
    }

}
