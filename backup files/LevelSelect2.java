import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.*;

public class LevelSelect2 extends JPanel implements ActionListener, Scene {
	//level list
    private static ArrayList<Level> levels = new ArrayList<Level>();
    //current page number
    private static int page;
    //cards shown per page
    private static int pageMax = 5;
    //number of pages in use
    private static int numPages;
    //level card list
    private static ArrayList<LevelCard> displayList = new ArrayList<LevelCard>();
    //level select graphics
    //private final LevelSelectGraphics graphicsHandler;
    
    public LevelSelect2() {
    	//DEBUG generate test level data
    	StoredNote[][] ng = {
    			{new StoredNote(600, 0), new StoredNote(1000, 0)},
    			{new StoredNote(800, 1), new StoredNote(1200, 1)},
    			{new StoredNote(600, 2), new StoredNote(1000, 2)},
    			{new StoredNote(800, 3), new StoredNote(1200, 3)}
    	};
    	for (int i = 1; i <= 20; i ++)
    		levels.add(new Level("Test " + i, "Ricardo", ng));
    	// JPanel properties
        this.setPreferredSize(new Dimension(800, 450));
        this.setFocusable(true);
        //create displayList
        for (int i = 0; i < levels.size() - 1; i ++)
        	displayList.add(new LevelCard(levels.get(i)));
        //sort displayList
        
        //calculate number of pages
        numPages = (int) Math.ceil((double) displayList.size() / pageMax);
        //start at first page
        page = 1;
        //initialize graphics handler
        //graphicsHandler = new LevelSelectGraphics();
    }
    
    /*
    @Override
    public void paint (Graphics graphics) {
        // cast graphics to 2D
        Graphics2D g = (Graphics2D) graphics;

        // clear the previous frame
        super.paintComponent(g);

        // tell GameGraphics to draw the frame
        graphicsHandler.drawFrame(g);
    }*/
    
    @Override
    public void changeScene(Scene scene) {
    	//
    }
    
    @Override
    public void update(long delta) {
    	//draw frame
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
    //changePage
    public static void changePage(boolean nextP) {
    	//page logic
    	if (nextP) page = (page + 1) % numPages;
    	else page = (page - 1) % numPages;
    	//refresh graphics
    	//graphicsHandler.updateList();
    }
    //get level cards in current page
    public static ArrayList<LevelCard> getPageCards() {
    	//create arraylist of size pageMax
    	ArrayList<LevelCard> pageCards = new ArrayList<LevelCard>(pageMax);
    	//iterate through displayList
    	int pos = pageMax * (page - 1);
    	for (int i = pos; i < displayList.size() && i < pos + pageMax; i ++)
    		pageCards.add(displayList.get(i));
    	return pageCards;
    }
    
    //get current page number
    public static int getPage() {
    	return page;
    }
    
    //get number of pages
    public static int getNumPages() {
    	return numPages;
    }
    
    //method to load local level data into the displayList array
    private void populateDisplayList() {
    	//
    }
}