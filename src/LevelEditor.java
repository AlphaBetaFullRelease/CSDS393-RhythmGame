import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class LevelEditor extends JPanel implements ActionListener, Scene {

    // level data
    private Level level;
    // number of tracks
    private int numTracks = 4;

    // graphics for the level editor
    private LevelEditorGraphics graphicsHandler;

    // mouse position
    private Point mousePos = new Point(0,0);
    // list of all notes as draggable objects
    private List<DraggableNote>[] notesDrag = new ArrayList[numTracks];
    // draggable object for the scroll bar
    private Draggable scrollDrag;
    // reference to current object being dragged
    private Draggable currentlyDragging;
    // flag determining if something is being dragged
    private boolean isDragging = false;

    // holds preview notes on screen
    private GameState previewNotes;
    // current time of the preview frame (ms)
    private long previewTime = 0;
    // speed of the level preview (used to determine note spacing) (%/Ms)
    private double noteSpeed = 0.001;
    // the amount of time it takes for a note to move across the screen (in ms)
    private long trackDuration = (long)(1 / noteSpeed);

    // reference to sceneRunner (used to change scenes)
    private SceneRunner sceneChanger;

    // constructor with level input
    public LevelEditor (Level inpLevel){
        // JPanel properties
        this.setPreferredSize(new Dimension(800, 450)); // screen size/resolution can be changed later, I just picked one to start
        this.setFocusable(true);

        // add mouse listener
        MouseClass listener = new MouseClass(this);
        addMouseMotionListener(listener);
        addMouseListener(listener);
        
        // set the level reference
        level = inpLevel;

        previewNotes = new GameState(numTracks);

        // create graphics
        graphicsHandler = new LevelEditorGraphics(previewNotes);

        // load starting preview notes (a well designed level should have no notes on screen at time=0 but we should still account for it)
        // iterate through each track
        for(int track = 0; track < numTracks; track++){
            // create draggable container list for each track
            notesDrag[track] = new ArrayList<>();

            // while there are still notes to add, add them
            StoredNote next = level.getNextNote(track, trackDuration);
            while(next != null){
                // add note
                addNote(track, next);

                // get next note in track
                next = level.getNextNote(track, trackDuration);
            }
        }
    }

    // function run every frame with a delta in ms between the last call
    @Override
    public void update(long delta){
        // draw graphical elements
        repaint();
    }

    // sets mouse position
    public void setMousePos(int x, int y){
        mousePos.x = x;
        mousePos.y = y;

        // if anything is currently being dragged, run update function
        if(isDragging){
            currentlyDragging.drag(mousePos);
        }
    }

    // function called when mouse pressed down
    public void mouseDown(){
        // check for grabbing a note
        checkGrabNote();
    }

    // function called when mouse released
    public void mouseUp(){
        // check if an object is being dragged
        if(isDragging){
            // if scrollbar being dragged, update each note's draggablePos


            // release the object
            isDragging = false;
            currentlyDragging = null;
        }
    }

    // checks all notes and initiates a grab if the mouse is hovering above one
    private void checkGrabNote(){
        // iterate through all tracks
        for(int track = 0; track < numTracks; track++){
            // iterate through all draggable notes in the track
            for(DraggableNote note : notesDrag[track]){
                // check for collision
                if(note.isTouching(mousePos)){
                    System.out.println("dragging");
                    // grab note
                    isDragging = true;
                    currentlyDragging = note;
                    note.startDragging(mousePos);
                    // also return bc we don't need to check any new notes after one is grabbed
                    return;
                }
            }
        }
    }

    // adds a note to the preview & creates a draggable object for it
    private void addNote(int track, StoredNote note){
        // calculate & set position offset
        float spawnOffset = trackDuration - note.getPos();
        Note noteObj = note.getNote();
        noteObj.updatePos(spawnOffset * noteSpeed);

        // add note to previewNotes
        previewNotes.spawnNote(track, noteObj);

        // create draggable object for note
        notesDrag[track].add(createDraggableNote(track, noteObj));
    }

    // called when an action is performed
    @Override
    public void actionPerformed(ActionEvent event){

    }

    // sets reference for the sceneChanger
    @Override
    public void setSceneRunner(SceneRunner sceneRunner){
        sceneChanger = sceneRunner;
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

    private DraggableNote createDraggableNote(int track, Note linkedNote){
        // reference to the preview graphics for layout information
        GameGraphics preview = graphicsHandler.getPreview();

        // calculate the position of the note
        Point pos = new Point((int)preview.getTrackCenter(track), preview.getNoteY(linkedNote.getPos()));

        // calculate the bounds of the track
        DraggableNote retVal = new DraggableNote(pos, preview.getNoteWid(), preview.getYOffset(), preview.getYOffset() + preview.getHeight(), linkedNote);
        return retVal;
    }

    // test scenario
    public static void main(String[] args){
        // create default test level
        // StoredNote[][] ng = {
        //     {new StoredNote(500, 0)},
        //     {new StoredNote(700, 1)},
        //     {new StoredNote(900, 2)},
        //     {new StoredNote(300, 3)}
        // };
        StoredNote[][] ng = {{},{},{},{new StoredNote(300, 3)}};
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

    // class for a draggable object (scroll bar & notes)
    // current implementation only allows vertical dragging & circular collision
    private class Draggable {
        // pixel coordinates of the center of the object
        private Point pos;
        // pixel value for the radius of the object (for collision detection)
        private int radius;
        // these are pixel bounds for how far the object can be dragged 
        private int lowBound;
        private int highBound;

        // when the object is being dragged, this offset is used so that the object doesn't snap to the
        // center of the mouse as soon as it is dragged
        private Point offset;

        public Draggable(Point pos, int radius, int lowBound, int highBound){
            this.pos = pos;
            this.radius = radius;
            this.lowBound = lowBound;
            this.highBound = highBound;
        }

        // will return true if point is intersecting the draggable object
        public boolean isTouching(Point point){
            return pos.distance(point) < radius;
        }

        // initializes the dragging process
        public void startDragging(Point anchor){
            // calculate the offset from this to the anchor
            offset = subtract(pos, anchor);
        }

        // when the object is being dragged, this function updates the position of the dragged object
        public void drag(Point anchor){
            // update pos
            pos.y = anchor.y + offset.y;

            // constrain pos to bounds (apply bounds to vertical)
            pos.y = Math.min(highBound, Math.max(lowBound, pos.y));
        }

        // maps the current position of the draggable object to a new range (lower, higher)
        public float mapTo(float lower, float higher){
            return (float)(pos.y - lowBound)/(highBound-lowBound) * (higher-lower) + lower;
        }

        // subtracts two points
        private Point subtract(Point a, Point b){
            return new Point(a.x - b.x, a.y - b.y);
        }

        // adds two points
        private Point add(Point a, Point b){
            return new Point(a.x + b.x, a.y + b.y);
        }
    }

    // draggable object with references to which note it is affecting & common helper functions for note stuff
    private class DraggableNote extends Draggable {

        // reference to the Note object associated with the draggable
        private Note linkedNote;

        public DraggableNote(Point pos, int radius, int lowBound, int highBound, Note linkedNote){
            super(pos, radius, lowBound, highBound);
            this.linkedNote = linkedNote;
        }

        // drags the object and updates Note's positon dynamically
        @Override
        public void drag(Point anchor){
            // drag the draggable object
            super.drag(anchor);
            // update the note's position
            linkedNote.setPos(mapTo(0,1));
        }
    }

    // class for listening to mouse events
    private class MouseClass extends MouseInputAdapter {

        // reference used to call mouse update functions
        private LevelEditor editor;

        public MouseClass(LevelEditor editor){
            this.editor = editor;
        }

        // function called when the mouse is moved
        @Override
        public void mouseDragged(MouseEvent e){
            // set mouse position
            editor.setMousePos(e.getX(), e.getY());
        }

        // function called when the mouse is pressed
        @Override
        public void mousePressed(MouseEvent e){
            // update position
            editor.setMousePos(e.getX(), e.getY());

            // trigger mouseUp
            editor.mouseDown();
        }

        // function called when the mouse is released
        @Override
        public void mouseReleased(MouseEvent e){
            editor.mouseUp();
        }
    }
}
