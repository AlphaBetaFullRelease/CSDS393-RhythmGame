import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LevelEditor extends JPanel implements ActionListener, Scene {

    // level data
    private Level level;

    // graphics for the level editor
    private LevelEditorGraphics graphicsHandler;

    // holds preview notes on screen
    private GameState previewNotes;
    // current time of the preview frame (ms)
    private long previewTime = 0;
    // speed of the level preview (used to determine note spacing) (%/Ms)
    private double noteSpeed = 0.0001;
    // the amount of time it takes for a note to move across the screen (in ms)
    private long trackDuration = (long)(1 / noteSpeed);

    // number of tracks
    private int numTracks = 4;

    // constructor with level input
    public LevelEditor (Level inpLevel){
        // JPanel properties
        this.setPreferredSize(new Dimension(800, 450)); // screen size/resolution can be changed later, I just picked one to start
        this.setFocusable(true);
        
        // set the level reference
        level = inpLevel;

        previewNotes = new GameState(numTracks);

        // load starting preview notes (a well designed level should have no notes on screen at time=0 but we should still account for it)
        // iterate through each track
        for(int track = 0; track < numTracks; track++){
            // while there are still notes to add, add them
            StoredNote next = level.getNextNote(track, trackDuration);
            while(next != null){
                // calculate & set position offset
                float spawnOffset = trackDuration - next.getPos();
                Note note = next.getNote();
                note.updatePos(spawnOffset * noteSpeed);

                // add note to previewNotes
                previewNotes.spawnNote(track, note);

                // get next note in track
                next = level.getNextNote(track, trackDuration);
            }
        }

        // create graphics
        graphicsHandler = new LevelEditorGraphics(previewNotes);
    }

    // function run every frame with a delta in ms between the last call
    @Override
    public void update(long delta){
        // draw graphical elements
        repaint();
    }

    // called when an action is performed
    @Override
    public void actionPerformed(ActionEvent event){

    }

    // sets a reference to the scene runner (required for change scenes)
    @Override
    public void setSceneRunner(SceneRunner sceneRunner){

    }

    // returns the panel
    @Override
    public JPanel getPanel(){
        return (JPanel)this;
    }

    @Override
    public void paint (Graphics graphics) {
        // cast graphics to 2D
        Graphics2D g = (Graphics2D)graphics;

        // clear the previous frame
        super.paintComponent(g);

        // tell GameGraphics to draw the frame
        graphicsHandler.drawFrame(g);
    }

    // test scenario
    public static void main(String[] args){
        // create default test level
        StoredNote[][] ng = {
            {new StoredNote(100, 0)},
            {new StoredNote(200, 1)},
            {new StoredNote(300, 2)},
            {new StoredNote(400, 3)}
        };
        Level testLevel = new Level("Test", "Sam", ng);

        // create level editor scene
        LevelEditor editor = new LevelEditor(testLevel);

        // create a new scenerunner with the starting scene
        SceneRunner sceneRunner = new SceneRunner(editor);

        // main loop
        while(true){
            // update loaded scene
            sceneRunner.update();

            // wait for the rest of the frame (if there is any time left)
            sceneRunner.waitUntilNextFrame();
        }
    }
}
