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

    // sets up the game
    public Game() {
        // JPanel properties
        this.setPreferredSize(new Dimension(800, 450)); // screen size/resolution can be changed later, I just picked one to start
        this.setFocusable(true);

        // set up the input handler
        inputHandler = new InputEventDriver(this); // create input event driver
        addKeyListener(inputHandler); // tells JPanel where to send input events

        // create tracks
        makeTrack();

        // DEBUG (add one note to the track to show off graphics)
        gameState.getTracks().get(0).add(new Note());

        // set up graphics handler
        graphicsHandler = new GameGraphics(gameState);
        graphicsHandler.width = 800;
        graphicsHandler.height = 450;

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
        // move notes forward
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();
        for(ArrayList<Note> track : tracks){
            for(Note note : track){
                note.pos += .01;
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
        // pause 
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
    }

    // creates a track
    private void makeTrack(){
        // add track to the game state
        gameState.getTracks().add(new ArrayList<>());
    }
}
