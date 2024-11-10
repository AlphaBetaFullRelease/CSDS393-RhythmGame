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

    // current level note grid indexes
    private int[] noteIndex;

    // time elapsed
    private long elapsedTime;
    
    // note movement speed (%/Ms)
    private double noteSpeed = 0.0001;

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

        // instantiate fields
        noteHits = 0;
        noteMisses = 0;
        health = 0;
        score = 0;

        //initialize noteGrid index
        this.noteIndex = new int[numTracks];
    }

    // this is the frame update function
    @Override
    public void update(long delta) {
        elapsedTime += delta; //update elapsed time
        // check for new notes to spawn
        for (int i = 0; i <= noteIndex.length - 1; i ++){ //iterate through tracks
        	double diff = 0;
        	for (int y = noteIndex[i]; y < level.getTrackLength(i) && diff >= 0; y ++) {
        		StoredNote sNote = level.getStoredNote(i, y);
        		diff = elapsedTime - sNote.getPos();
        		if (diff >= 0) {
        			Note n = sNote.getNote();
        			n.updatePos(noteSpeed * diff);
        			gameState.spawnNote(i, n);
        			noteIndex[i] ++;
        		} else diff = -1;
        	}
        }
        // check for notes that have moved off screen
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        //iterate through tracks
        for (ArrayList<Note> track : tracks) {
            //iterate through notes
            for (int i = 0; i < track.size(); i ++) {
                Note n = track.get(i);
                if (n.getPos() > 1) {
                    track.remove(i);
                    i --;
                }  else n.updatePos(noteSpeed * delta); //move notes
            }
        }
        gameState.setTracks(tracks);
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
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        for(Note note : tracks.get(lane)){
            if(note.getPos() >= graphicsHandler.getTargetStart() && note.getPos() <= graphicsHandler.getTargetEnd())
                return note;
        }
        return null;
    }

    // this function is called when a note passes off screen without being hit
    private void notePassed(Note note){
        
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
