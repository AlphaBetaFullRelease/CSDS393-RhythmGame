import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;

// does all of the graphical work for the gameplay
// uses very simple graphics for the demo, but since it's a class, we can replace it with something nicer looking later
public class GameGraphics {

    // holds all the information for the game state
    private final GameState gameState;

    // these variables control the layout of the game (all in % of the screen)
    private static final double trackSpan = 0.4; // the tracks will span this
    private static final double trackWidth = 0.06; // the width of a single track
    private static final double noteWidth = 0.055; // width of a note
    private static final double noteSpawnOffset = 0.5; // in units of noteWidth, allows the note to spawn off screen
    private static final double targetPlace = 0.8; // where the target line is displayed

    // needs to be passed a GameState object that is linked to the Game
    public GameGraphics(GameState gs){
        gameState = gs;

        // generate all dimensions for graphical objects
        Layout.initialize();
    }

    // draws all the elements to the screen
    public void drawFrame(Graphics2D g){
        // set default line thickness
        g.setStroke(new BasicStroke(2f));

        // get tracks for easier access
        ArrayList<ArrayList<Note>> tracks = gameState.getTracks();

        // draw the tracks
        for(int i = 0; i < getNumTracks(); i++)
            drawTrack(g, Layout.trackLeft[i]);
        
        // draw the notes
        for(int lane = 0; lane < getNumTracks(); lane++)
            for(Note note : tracks.get(lane))
                drawNote(g, lane, note);

        // draw the target line
        drawTarget(g);
    }

    // draws a track at the desired x coordinate (where x is the left side of the track)
    private void drawTrack(Graphics2D g, int x){
        // correct for offset borders
        x -= 2;

        // fill in background of track
        g.setColor(Colors.trackFill);
        g.fillRect(x, 0, Layout.trackWid, getHeight());

        // draw border lines to the left and right of the track
        g.setColor(Colors.trackBorder);
        g.drawLine(x, 0, x, getHeight());
        g.drawLine(x+Layout.trackWid, 0, x+Layout.trackWid, getHeight());
    }

    // draws a note in the specified lane
    private void drawNote(Graphics2D g, int lane, Note note){
        // calculate the y position of the note on the screen
        int yPos = Layout.getNoteY(note.getPos());

        // actually draw the note
        g.setColor(Colors.noteColor);
        g.fillOval(Layout.trackLeft[lane], yPos, Layout.noteWid, Layout.noteWid);
    }

    // draws the target line
    public void drawTarget(Graphics2D g){
        g.setColor(Colors.targetColor);
        g.drawLine(0, Layout.targetLevel, getWidth(), Layout.targetLevel);
    }

    // assuming that the note can only be hit when it is touching the target line, this function returns the value (0-1) when the note can first be hit
    public double getTargetStart(){
        return targetPlace - noteWidth;
    }

    // assuming that the note can only be hit when it is touching the target line, this function returns the value (0-1) when the note can first be hit
    public double getTargetEnd(){
        return targetPlace + noteWidth;
    }

    // returns the center position of the target
    public double getTargetCenter(){
        return targetPlace;
    }

    public static int getWidth(){
        return 800;
    }

    public static int getHeight(){
        return 450;
    }

    public static int getNumTracks(){
        return 4;
    }

    // holds all coordinates that only need to be calculated once
    private static class Layout {
        // coordinate for the center of each track
        public static int[] trackCenter = new int[getNumTracks()];
        // coordinate for the left border of each track
        public static int[] trackLeft = new int[getNumTracks()];
        // precalculated track width
        public static int trackWid;
        // how wide a note is
        public static int noteWid;
        // how many pixels a note's spawn & despawn should be offset by
        public static int noteOffset;
        // the length of the track (accounting for noteOffset)
        public static int trackLen;
        // the pixel level for the target line
        public static int targetLevel;

        // uses the variables in GameGraphics to generate coordinates for graphical elements
        public static void initialize(){
            // comments assume all customization variables are in pixels for simplicity (they are actually in percent of screen width)

            // calculate the distance between the center of each track ((trackSpan - trackWidth) / (numTracks-1))
            double betweenTrackDist = getWidth() * ((trackSpan - trackWidth) / (getNumTracks()-1));
            // calculate the left side of the leftmost track (trackSpan/2)
            double trackStart = getWidth() * (1 - trackSpan) / 2;
            // set center for each track
            for(int i = 0; i < getNumTracks(); i++){
                trackLeft[i] = (int)(trackStart + i * betweenTrackDist);
                trackCenter[i] = (int)(trackLeft[i] + getWidth() * trackWidth/2);
            }
            // self explanatory
            trackWid = (int)(trackWidth * getWidth());
            noteWid = (int)(noteWidth * getWidth());
            noteOffset = (int)(noteWidth * noteSpawnOffset * getWidth());
            // track length is height + 2 noteOffset (one for top of the screen and the other for bottom)
            trackLen = getHeight() + 2 * noteOffset;
            // target level needs to account for note offset
            targetLevel = (int)(trackLen * targetPlace - noteOffset);
        }

        // calculates the y position of a note on screen
        // pos is the note's position (0-1)
        public static int getNoteY(double pos){
            return (int)(pos * trackLen - noteOffset - noteWid/2);
        }
    }

    // holds color information for graphics
    private static class Colors {
        // edge of the track
        public static final Color trackBorder = new Color(61, 61, 61);
        // track fill color
        public static final Color trackFill = new Color(145, 145, 145);
        // note fill color
        public static final Color noteColor = new Color(24, 222, 163);
        // target color
        public static final Color targetColor = new Color(0, 0, 0);
    }
}
