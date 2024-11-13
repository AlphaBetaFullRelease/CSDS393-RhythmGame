import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LevelEditor extends JPanel implements ActionListener, Scene {

    // level data
    private Level level;

    // graphics for the level editor
    private LevelEditorGraphics graphics;

    // holds preview notes on screen
    private GameState previewNotes;
    // current time of the preview frame (ms)
    private long previewTime = 0;
    // speed of the level preview (used to determine note spacing) (%/Ms)
    private double noteSpeed = 0.0001;

    // number of tracks
    private int numTracks = 4;

    // constructor with level input
    public LevelEditor (Level inpLevel){
        // set the level reference
        level = inpLevel;

        // load starting preview notes (a well designed level should have no notes on screen at time=0 but we should still account for it)
        // calculate "length" of one screen (number of milliseconds one screen length takes up) (any note with a spawn time before this should be on screen at time=0)
        long trackDuration = (long)(1.0 / noteSpeed);
        // iterate through each track
        for(int track = 0; track < numTracks; track++){
            // while there are still notes to add, add them

        }

        // create graphics
        graphics = new LevelEditorGraphics(previewNotes);
    }

    // function run every frame with a delta in ms between the last call
    @Override
    public void update(long delta){

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
}
