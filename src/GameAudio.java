import java.io.File; 
import java.io.IOException; 
import java.io.UncheckedIOException;
import java.util.Scanner; 
  
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;

// plays audio (sfx & music) for gameplay
public class GameAudio {
    // stores current frame of the file (to track progress)
    private Long curFrame;

    // clip plays the loaded section of the song
    private Clip clip;

    // this streams in the audio file so the whole song doesn't need to be loaded at once
    private AudioInputStream audioInputStream; 

    // creates a new audio player
    public GameAudio(){
        
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

            // set clip to loop
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(Exception e){
            // wrap in runtime exception
            throw new RuntimeException(e);
        }
    }

    // starts the song playback
    public void startSong(){
        clip.start();
    }

}
