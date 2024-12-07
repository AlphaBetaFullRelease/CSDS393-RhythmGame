import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.*;

public class LevelEditor extends JPanel implements ActionListener, Scene, KeyListener {

    // level data
    private Level level;
    // number of tracks
    private int numTracks = 4;
    // notes for the level (in linked list for more efficient access)
    private LinkedList<StoredNote>[] notes = new LinkedList[numTracks];
    // index always points to the topmost note on screen (or -1 if none on screen)
    private int[] topIndex = new int[numTracks];
    // index always points to the bottommost note on screen (or -1 if none on screen)
    private int[] bottomIndex = new int[numTracks];

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
    // keeps track of which tool is currently active using an enumerated type
    private Tool curTool = Tool.ADD;

    // holds preview notes on screen
    private GameState previewNotes;
    // speed of the level preview (used to determine note spacing) (%/Ms)
    private double noteSpeed = 0.001;
    // the amount of time it takes for a note to move across the screen (in ms)
    private long trackDuration = (long)(1 / noteSpeed);
    // the current scroll position on the screen
    private long curTime = 0;
    private UserData userData;
    private boolean isCtrlHeld;
    private JFileChooser fileChooser;
    private File audioFile;
    private String title;
    private String author;

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

        addKeyListener(this);
        
        // set the level reference
        level = inpLevel;

        userData = new UserData();

        previewNotes = new GameState(numTracks);

        // create graphics
        graphicsHandler = new LevelEditorGraphics(previewNotes);

        isCtrlHeld = false;

        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        initLevel();
    }

    private void initLevel() {
        // load all notes from level into the notes linkedList
        // iterate through tracks
        for(int track = 0; track < numTracks; track++){
            // create draggable container list for each track
            notesDrag[track] = new ArrayList<>();
            // create notes list
            notes[track] = new LinkedList<>();
            // instantiate indices
            topIndex[track] = -1;
            bottomIndex[track] = -1;

            // iterate through all the notes in the track
            for(int noteIndex = 0; noteIndex < level.getTrackLength(track); noteIndex++){
                // add note
                addNote(track, level.getStoredNote(track, noteIndex));
            }

            // display initial notes
            ListIterator<StoredNote> iter = notes[track].listIterator();
            while(iter.hasNext()){
                // get next note
                StoredNote next = iter.next();

                // check if on screen
                if(next.getPos() <= trackDuration){
                    // display note
                    displayNote(track, next);
                }
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
        if(null != curTool)switch (curTool) {
            case MOVE -> // check for grabbing a note
                checkGrabNote();
            case ADD -> // try to add note
                checkAddNote();
            case REMOVE -> // try to remove note
                checkRemoveNote();
            default -> {
            }
        }
    }

    // function called when mouse released
    public void mouseUp(){
        // check if an object is being dragged
        if(isDragging){
            // if scrollbar being dragged, update each note's draggablePos

            // if note being dragged, update pos in notes

            // release the object
            isDragging = false;
            currentlyDragging = null;
        }
    }

    // scrolls the preview window by the specified amount in milliseconds
    private void scroll(long ms){
        // bounds restriction
        if(curTime + ms < 0)
            ms = -curTime;

        // increment curTime
        curTime += ms;

        // undisplay all notes
        previewNotes.resetTracks();
        notesDrag = new ArrayList[numTracks];

        // iterate through each track
        for(int track = 0; track < numTracks; track++) {
            // reset index
            bottomIndex[track] = -1;
            topIndex[track] = -1;
            // initialize notesDrag
            notesDrag[track] = new ArrayList<>();

            // get iterator starting at the bottom note
            ListIterator<StoredNote> iter = notes[track].listIterator();

            StoredNote note;

            // iterate until no notes left or we reach the first note to be displayed
            while(iter.hasNext()){
                // get next note
                note = iter.next();

                // increment bottom index
                bottomIndex[track]++;

                // check if note should be undisplayed
                if(note.getPos() > bottomTime()) {
                    // set index one back
                    iter.previous();
                    break;
                }
            }

            // iterate through all notes in the preview region
            while(iter.hasNext()){
                // get next note
                note = iter.next();

                // check if note is below top of screen
                if(note.getPos() < topTime()){
                    // set bottom index if this is first
                    if(bottomIndex[track] == -1)
                        bottomIndex[track] = iter.previousIndex();

                    // set top index
                    topIndex[track] = iter.previousIndex();

                    // display note
                    displayNote(track, note);
                }else{
                    // break once note is beyond top of screen
                    break;
                }
            }
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

    // tries to remove a note where the user is clicking
    private void checkRemoveNote(){
        // iterate through all tracks
        for(int track = 0; track < numTracks; track++){
            // iterate through all visible notes in the track
            for(int i = 0; i < previewNotes.getTracks().get(track).size(); i++){
                Note note = previewNotes.getTracks().get(track).get(i);
                // check for note collision with mouse
                Point notePos = new Point((int)graphicsHandler.getPreview().getTrackCenter(track), graphicsHandler.getPreview().getNoteY(note.getPos()) + graphicsHandler.getPreview().getNoteWid()/2);
                if(notePos.distance(mousePos) < graphicsHandler.getPreview().getNoteWid() / 2){
                    // remove the note
                    removeNote(track, note);
                    return;
                }
            }
        }
    }

    // tries to add a note where the user is clicking
    private void checkAddNote(){
        int track = -1;
        // check if the mouse is hovering over a track & find out which track
        for(int t = 0; t < numTracks; t++)
            if(mousePos.x > graphicsHandler.getTrackStart(t) && mousePos.x < graphicsHandler.getTrackEnd(t))
                track = t;
            
        // mouse not hovering over a track, abandon note add
        if(track == -1)
            return;

        // calculate note pos value from mouse y coordinate
        float pos = graphicsHandler.getPreview().getNotePos(mousePos.y);
        // confirm that note is within bounds
        if(pos < 0 || pos > 1)
            return;
        // calculate note millisecond value from pos value
        long millis = getTimeFromPos(pos);

        // add note
        addNote(track, new StoredNote((long)millis, track));
    }

    // adds note to the stored notes list
    private void addNote(int track, StoredNote note){
        // get iterator for list
        ListIterator<StoredNote> iter = notes[track].listIterator();
        
        // try to find the point to insert the note
        while(iter.hasNext()){
            // check if next note comes after this note
            if(iter.next().compareTo(note) > 0){
                // back up one index then add note
                iter.previous();
                iter.add(note);

                // check if note should be on screen
                if(note.getPos() < topTime() && note.getPos() > bottomTime()){
                    // display note
                    displayNote(track, note);

                    // check if new note is above top note
                    if(topIndex[track] == -1 || notes[track].get(topIndex[track]).getPos() < note.getPos()){
                        // set new top index
                        topIndex[track] = iter.previousIndex();
                    }

                    // check if new note is below bottom note
                    if(bottomIndex[track] == -1 || notes[track].get(bottomIndex[track]).getPos() > note.getPos()){
                        // set new bottom index
                        bottomIndex[track] = iter.previousIndex();
                    }
                }

                return;
            }
        }
        // list iterated through, add to end
        iter.add(note);
        // check if note should be on screen
        if(note.getPos() < topTime() && note.getPos() > bottomTime()){
            // display note
            displayNote(track, note);
            // this note should be only note on screen, so update indices
            bottomIndex[track] = iter.previousIndex();
            topIndex[track] = iter.previousIndex();
        }
    }

    // removes a note
    private void removeNote(int track, Note note){
        // remove note from notes list
        ListIterator<StoredNote> iter = notes[track].listIterator();
        while(iter.hasNext()){
            if(iter.next().getNote() == note){
                iter.previous();
                iter.remove();
            }
        }

        // un display note
        unDisplayNote(track, note);
    }

    // un-displays a note
    private void unDisplayNote(int track, Note note){
        // remove note from previewNotes
        ArrayList<Note> noteList = previewNotes.getTracks().get(track);
        int noteIndex = -1;
        for(int i = 0; i < noteList.size(); i++){
            // check if note matches
            if(note.getPos() == noteList.get(i).getPos()){
                // remove note at index
                noteList.remove(i);
                noteIndex = i;
                break;
            }
        }

        // remove note from draggableNotes
        for(int i = 0; i < notesDrag[track].size(); i++){
            // check if note matches
            if(Math.abs(note.getPos() - notesDrag[track].get(i).mapTo(0,1)) < .01){
                // remove note at index
                noteList.remove(i);
                break;
            }
        }

        // if note was at the top of the screen, decrement topIndex
        if(noteIndex == topIndex[track]){
            topIndex[track]--;
            // if no notes are left on the screen, set topIndex and bottomIndex to -1
            if(previewNotes.getTracks().get(track).isEmpty()){
                bottomIndex[track] = -1;
                topIndex[track] = -1;
            }
        }
    }

    // adds a note to the preview & creates a draggable object for it
    private void displayNote(int track, StoredNote note){
        // calculate & set position offset
        float spawnOffset = curTime + trackDuration - note.getPos();
        Note noteObj = note.getNote();
        noteObj.setPos(spawnOffset * noteSpeed);

        // add note to previewNotes
        previewNotes.spawnNote(track, noteObj);

        // create draggable object for note
        notesDrag[track].add(createDraggableNote(track, noteObj));
    }

    // returns the highest time value (in ms) where notes can be displayed
    private long topTime(){
        return curTime + trackDuration;
    }

    // returns the lowest time value (in ms) where notes can be displayed on screen
    private long bottomTime(){
        return curTime;
    }

    // returns the time in ms a note will have given a pos value on screen
    private long getTimeFromPos(float pos){
        return (long)(curTime + (1-pos) * trackDuration);
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
        DraggableNote retVal = new DraggableNote(pos, preview.getNoteWid(), preview.getYOffset(), preview.getYOffset() + preview.getHeight() + 2*preview.getNoteOffset(), linkedNote);
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
        ArrayList[] ng = new ArrayList[4];
        ng[0] = new ArrayList<StoredNote>();
        ng[1] = new ArrayList<StoredNote>();
        ng[2] = new ArrayList<StoredNote>();
        ng[3] = new ArrayList<StoredNote>();
        Level testLevel = new Level("Test", "Sam", ng);

        // create level editor scene
        LevelEditor editor = new LevelEditor(testLevel);

        // create a new scenerunner with the starting scene
        SceneRunner sceneRunner = new SceneRunner(editor);

        editor.setTitle();
        editor.setAuthor();
        editor.setMusic();

        // main loop
        while(true){
            // update loaded scene
            sceneRunner.update();

            // wait for the rest of the frame (if there is any time left)
            sceneRunner.waitUntilNextFrame();
        }
    }

    private void saveLevel() {
        ArrayList<StoredNote>[] _notes = new ArrayList[notes.length];
        for (int i = 0; i < notes.length; i++) {
            _notes[i] = new ArrayList<>();
            for (StoredNote note : notes[i]) {
                _notes[i].add(note);
            }
        }
        level = new Level(title, author, _notes);
        userData.createLevelFile(level, true);
    }

    private void setTitle() {
        title = JOptionPane.showInputDialog(this, "Enter new title.", "Title", JOptionPane.PLAIN_MESSAGE);
    }

    private void setAuthor() {
        author = JOptionPane.showInputDialog(this, "Enter level author name.", "Author", JOptionPane.PLAIN_MESSAGE);
    }

    private void setMusic() {
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            audioFile = fileChooser.getSelectedFile();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 17) { // CTRL
            isCtrlHeld = true;
        }
        if (e.getKeyCode() == 'T') {
            if (!isCtrlHeld) {
                setTitle();
            }
        }
        if (e.getKeyCode() == 'A') {
            if (!isCtrlHeld) {
                setAuthor();
            }
        }
        if (e.getKeyCode() == 'Q') {
            curTool = Tool.ADD;
        }
        if (e.getKeyCode() == 'W') {
            curTool = Tool.REMOVE;
        }
        if (e.getKeyCode() == 'E') {
            curTool = Tool.MOVE;
        }
        if (e.getKeyCode() == 'S') {
            if (isCtrlHeld) {
                saveLevel();
                if (audioFile != null) {
                    try {
                        userData.addMusicFile(level, audioFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        if (e.getKeyCode() == 'M') {
            if (isCtrlHeld) {
                setMusic();
            }
        }
        if (e.getKeyCode() == 'O') {
            if (isCtrlHeld) {
                loadLevel();
            }
        }
        if (e.getKeyCode() == 38) { // up
            scroll(200);
        }
        if (e.getKeyCode() == 40) { // down
            scroll(-200);
        }
    }

    private void loadLevel() {
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);

        int option = fileChooser.showOpenDialog(this);
        File levelFile;

        if (option == JFileChooser.APPROVE_OPTION) {
            levelFile = fileChooser.getSelectedFile();

            Gson gson = new Gson();

            try {
                level = gson.fromJson(new InputStreamReader(new FileInputStream(levelFile)), Level.class);
            } catch (java.io.FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        audioFile = null;

        initLevel();

        title = level.getTitle();
        author = level.getCreator();

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));
        fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 17) { // CTRL
            isCtrlHeld = false;
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

    private enum Tool {
        MOVE,
        ADD,
        REMOVE
    }
}
