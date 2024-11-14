import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;

// does all the graphical work for the level editor
public class LevelEditorGraphics {
    // dimensions of the screen
    private int width = 800;
    private int height = 450;

    // preview box coordinates / dimensions
    private int previewX = width / 4;
    private int previewY = 0;
    private int previewWidth = width * 3/4;
    private int previewHeight = height * 3/4;

    // GameGraphics used to display a preview of the level 
    private final GameGraphics previewGraphics;
    
    // GameState used to track visible notes in the preview
    public LevelEditorGraphics(GameState gs){
        // create preview graphics object
        previewGraphics = new GameGraphics(gs, previewX, previewY, previewWidth, previewHeight);
    }

    // draws all the elements to the screen
    public void drawFrame(Graphics2D g){
        drawPreview(g);

        // draw scrollbar
        // draw scroll buttons

        // draw place button
        // draw remove button
        // draw move button

        // draw play button

        // draw cursor
    }

    public void drawPreview(Graphics2D g){
        // display level preview
        previewGraphics.drawFrame(g);

        // draw bounding box around level preview
        g.setStroke(new BasicStroke(2f));
        g.setColor(Colors.previewBorder);
        g.drawLine(previewX, previewY, previewX+previewWidth, previewY);
        g.drawLine(previewX, previewY, previewX, previewY+previewHeight);
        g.drawLine(previewX+previewWidth, previewY, previewX+previewWidth, previewY+previewHeight);
        g.drawLine(previewX, previewY+previewHeight, previewX+previewWidth, previewY+previewHeight);

        // draw bpm ticks

    }

    // getter methods for preview window
    public int getPreviewX(){
        return previewX;
    }
    public int getPreviewY(){
        return previewY;
    }
    public int getPreviewWidth(){
        return previewWidth;
    }
    public int getPreviewHeight(){
        return previewHeight;
    }

    // returns reference to GameGraphics object for layout concerns
    public GameGraphics getPreview(){
        return previewGraphics;
    }

    // holds color information for graphics
    private static class Colors {
        // border of preview
        public static final Color previewBorder = new Color(0, 0, 0);
    }
}