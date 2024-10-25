import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener, Scene {
    // player performance information
    private int noteHits;
    private int noteMisses;
    private int health;
    private int score;

    // stores information about the game state for the graphics handler to display
    private final GameState gameState = new GameState();

    // handles game input
    private final InputEventDriver inputHandler;

    // does all graphics
    private final GameGraphics graphicsHandler;

    // number of tracks
    private final int numTracks = 4;

    // sets up the game
    public Game() {
        // JPanel properties
        this.setPreferredSize(new Dimension(800, 450)); // screen size/resolution can be changed later, I just picked one to start
        this.setFocusable(true);

        // set up the input handler
        inputHandler = new InputEventDriver(this); // create input event driver
        addKeyListener(inputHandler); // tells JPanel where to send input events

        // create tracks
        for(int i = 0; i < numTracks; i++)
            makeTrack();

        // DEBUG (add one note to the track to show off graphics)
        for(int i = 0; i < numTracks; i++)
            gameState.getTracks().get(i).add(new Note(i));

        // set up graphics handler
        graphicsHandler = new GameGraphics(gameState);

        // instantiate fields
        noteHits = 0;
        noteMisses = 0;
        health = 0;
        score = 0;
    }

    // this is the frame update function
    @Override
    public void update(long delta) {
        // check for new notes to spawn
        // check for notes that have moved off screen

        // loop through every track
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        for(ArrayList<Note> track : tracks){
            // notes to remove after iterating through all notes
            ArrayList<Note> toRemove = new ArrayList<>();

            // loop through every active note in the track
            for(Note note : track){
                // check for note that has moved off screen
                if(note.pos > 1){
                    // affect score calculation
                    notePassed(note);
                    // add note to remove queue
                    toRemove.add(note);
                }
                
                // move notes forward
                note.pos += .01;
            }

            // remove all notes in remove queue
            toRemove.stream().forEach(note -> track.remove(note));
        }
        // check for end of song

        // draw frame
        repaint();
    }

    // signals a scene swap to the SceneRunner
    @Override
    public void changeScene(Scene scene) {
        // unimplemented for now becasue the demo only uses one scene
    }

    @Override
    public void paint (Graphics graphics) {
        // cast grapphics to 2D
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

    // creates a track
    private void makeTrack(){
        // add track to the game state
        gameState.getTracks().add(new ArrayList<>());
    }
}
