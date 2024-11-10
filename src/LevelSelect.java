import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class LevelSelect extends JPanel implements ActionListener, Scene {
    // reference to the sceneRunner so scenes can be changed
    private SceneRunner sceneChanger;

    // reference to all the levels
    private ArrayList<Level> levels = new ArrayList<>();
    private ArrayList<LevelCard> displayList = new ArrayList<LevelCard>();
    private final LevelSelectGraphics graphicsHandler;
    public LevelSelect(Level level) {
    	// JPanel properties
        this.setPreferredSize(new Dimension(800, 450));
        this.setFocusable(true);
        //read level data and store in levels array
        displayList.add(new LevelCard(level));
        //create displayList
        
        //initialize graphics handler
        graphicsHandler = new LevelSelectGraphics(displayList);
    }
    
    @Override
    public void paint (Graphics graphics) {
        // cast graphics to 2D
        Graphics2D g = (Graphics2D) graphics;

        // clear the previous frame
        super.paintComponent(g);

        // tell GameGraphics to draw the frame
        graphicsHandler.drawFrame(g);
    }
    
    // sets reference for the sceneChanger
    @Override
    public void setSceneRunner(SceneRunner sceneRunner){
        sceneChanger = sceneRunner;
    }
    
    @Override
    public void update(long delta) {
    	repaint();
    }
    
    @Override
    public JPanel getPanel() {
    	return (JPanel)this;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //
    }
    
    //method to load local level data into the displayList array
    private void populateDisplayList() {
    	//
    }
}