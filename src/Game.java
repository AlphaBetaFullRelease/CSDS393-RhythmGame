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

    // current level
    private Level level;

    // current level note grid indexes
    private int[] noteIndex;

    // time elapsed
    private long elapsedTime;

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

        //initialize noteGrid index
        this.noteIndex = new int[numTracks - 1];
    }

    // this is the frame update function
    @Override
    public void update(long delta) {
        elapsedTime += delta; //update elapsed time
        // check for new notes to spawn
        for (int i = 0; i < noteIndex.length; i ++){ //iterate through tracks
            StoredNote sNote = level.getStoredNote(noteIndex[i], i); //get nearest StoredNote at track
            double diff = elapsedTime - sNote.getPos();
            while (diff >= 0) { //position is equal or less than elapsed time, spawn it and increment track index
                Note n = sNote.getNote(); //get note object
                n.updatePos(diff);  //update pos according to delta
                gameState.spawnNote(i, n); //update gameState
                noteIndex[i] ++;
                sNote = level.getStoredNote(noteIndex[i], i); //update nearest StoredNote
                diff = elapsedTime - sNote.getPos();
            }
        }
        // check for notes that have moved off screen

        // move notes forward
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        for(ArrayList<Note> track : tracks){
            for(Note note : track){
                note.updatePos(0.1);
            }
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
        // strike
        if (e.getID() == 2000) { // 2000 = strike
            System.out.println(e.getActionCommand());
        }

        // pause
        if (e.getID() == 2001) { // 2001 = pause

        }
    }

    // casts this object to jpanel and returns it
    @Override
    public JPanel getPanel(){
        return (JPanel)this;
    }

    // this function is called when a user inputs a strike
    private void strike(int lane){
        // check if there is a note that the user can strike in the right lane
        // do the things that need to happen when a strike lands or misses
        Note note = closestNote(lane); // gets the closest note in the lane where the strike occurred
        if(note.getPos() >= 0.9) // assuming 0.9 to 1 is a valid position for successful strike
            hitSuccess();
        else
            hitFail();
    }

    // returns closest note in specified lane
    private Note closestNote(int lane){
        Note closest = new Note(lane);
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        for(ArrayList<Note> track : tracks){
            for(Note note : track){
                if(note.getCol() == lane){
                    if(note.getPos() > closest.getPos()){
                        closest = note;
                    }
                }
            }
        }
        return closest;
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
        gameState.setTracks(gameState.getTracks().add(new ArrayList<Note>()););
    }
}
