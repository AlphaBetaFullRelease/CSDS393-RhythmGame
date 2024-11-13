import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;

// does all the graphical work for the level editor
public class LevelEditorGraphics {
    // GameGraphics used to display a preview of the level 
    private final GameGraphics previewGraphics;
    
    // GameState used to track visible notes in the preview
    public LevelEditorGraphics(GameState gs){
        previewGraphics = new GameGraphics(gs);
    }

    // draws all the elements to the screen
    public void drawFrame(Graphics2D g){
        // display game preview
        // draw bpm ticks

        // draw scrollbar
        // draw scroll buttons

        // draw place button
        // draw remove button
        // draw move button

        // draw play button

        // draw cursor
    }
}