import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JPanel implements ActionListener, Scene {
    // player performance information
    private int noteHits;
    private int noteMisses;
    private int health;
    private int score;

    // number of tracks
    private final int numTracks = 4;
    
    // stores information about the game state for the graphics handler to display
    private final GameState gameState = new GameState(numTracks);

    // handles game input
    private final InputEventDriver inputHandler;
    // does all graphics
    private final GameGraphics graphicsHandler;
    // plays sound
    private final GameAudio gameAudio;

    // reference to the sceneRunner so scenes can be changed
    private SceneRunner sceneChanger;

    // current level
    private Level level;

    // time elapsed
    private long elapsedTime;
    
    // note movement speed (%/Ms)
    private double noteSpeed = 0.0005;

    // sets up the game
    public Game(Level level) {
    	// set level to play
    	this.level = level;

        // JPanel properties
        this.setPreferredSize(new Dimension(800, 450)); // screen size/resolution can be changed later, I just picked one to start
        this.setFocusable(true);

        // set up the input handler
        inputHandler = new InputEventDriver(this); // create input event driver
        addKeyListener(inputHandler); // tells JPanel where to send input events

        // set up graphics handler
        graphicsHandler = new GameGraphics(gameState);

        // set up audio
        gameAudio = new GameAudio();
        // load song
        String pathToSong = level.getSongPath();
        // play song if file exists
        if(!pathToSong.isEmpty())
            gameAudio.loadSong(level.getSongPath());
        else{
            System.out.println("no song found");
        }
        
        // instantiate fields
        noteHits = 0;
        noteMisses = 0;
        health = 0;
        score = 0;
    }

    // this is the frame update function
    @Override
    public void update(long delta) {
        elapsedTime += delta; //update elapsed time

        // check for new notes to spawn (only checks once per track bc why would 2 notes spawn in one frame)
        // spawnTime is current time - the amount of time it takes for a note to travel to the target line
        long spawnTime = elapsedTime - (long)(graphicsHandler.getTargetCenter() / noteSpeed);
        for(int track = 0; track < numTracks; track++){ // iterate through tracks
            // get next note
            StoredNote nextNote = level.getNextNote(track, spawnTime);

            // check that next note is not null (null means no notes can spawn on this track right now)
            if(nextNote != null){
                // calculate precise spawn location / offset (in ms)
                float spawnOffset = spawnTime - nextNote.getPos();
                
                // get note object
                Note note = nextNote.getNote();

                // set initial spawn position
                note.updatePos(spawnOffset * noteSpeed);

                // add note to the list of active notes
                gameState.spawnNote(track, note);
            }
        }

        // iterate through active notes
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        //iterate through tracks
        for (ArrayList<Note> track : tracks) {
            //iterate through notes in the track
            for (int i = 0; i < track.size(); i ++) {
                Note n = track.get(i);
                // check if note should despawn
                if (n.getPos() > 1) {
                    // despawn note
                    track.remove(i);
                    i--;
                } else
                    n.updatePos(noteSpeed * delta); // move note
            }
        }

        // check for end of song

        // draw frame
        repaint();
    }

    // sets reference for the sceneChanger
    @Override
    public void setSceneRunner(SceneRunner sceneRunner){
        sceneChanger = sceneRunner;
    }

    @Override
    public void paint (Graphics graphics) {
        // cast graphics to 2D
        Graphics2D g = (Graphics2D) graphics;

        // clear the previous frame
        super.paintComponent(g);

        // tell GameGraphics to draw the frame
        graphicsHandler.drawFrame(g);
    }

    // processes input events from various sources at any time & reacts to events
    // accepts events from: InputEventDriver
    @Override
    public void actionPerformed(ActionEvent e) {
        // should check the type of action and react accordingly
        // strikes
        switch (e.getID()) {
            case 2000 -> // 2000 = strike lane 1
                strike(0);
            case 2001 -> // 2001 = strike lane 2
                strike(1);
            case 2002 -> // 2002 = strike lane 3
                strike(2);
            case 2003 -> // 2003 = strike lane 4
                strike(3);
            case 2004 -> // 2004 = pause
                System.out.println("pause not implemented");
            default -> {
                System.out.println("invalid actionEvent id");
            }
        }
    }

    // casts this object to jpanel and returns it
    @Override
    public JPanel getPanel(){
        return (JPanel)this;
    }

    // this function is called when a user inputs a strike
    private void strike(int lane){
        // get note if there is one inside the target area
        Note hitNote = getTargetNote(lane);

        // check if note exists
        if(hitNote != null){
            // adjust score
            hitSuccess();
            // remove note from notes list
            gameState.getTracks().get(lane).remove(hitNote);
        }else{
            hitFail();
        }
    }

    // checks if there is a note within the target zone of a specified lane
    private Note getTargetNote(int lane){
        ArrayList<Note> track = gameState.getTracks().get(lane);
        for(Note note : track){
            if(note.getPos() >= graphicsHandler.getTargetStart() && note.getPos() <= graphicsHandler.getTargetEnd())
                return note;
        }
        return null;
    }

    private void hitSuccess(){
        noteHits += 1;
        score += 1;
    }

    private void hitFail(){
        noteMisses += 1;
        health -= 5;
    }
}
