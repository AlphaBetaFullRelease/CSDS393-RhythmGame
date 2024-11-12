import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.*;

public class LevelSelect extends JPanel implements ActionListener, Scene {
	//level list
    private ArrayList<Level> levels = new ArrayList<Level>();
    //level card list
    private ArrayList<LevelCard> cardList = new ArrayList<LevelCard>();
    //cards shown per page
    private final int pageMax = 5;
    //number of pages in use
    private int numPages;
    //current page number
    private int page;
    //graphics handler for level select
    private final LevelSelectGraphics graphicsHandler;
    
    public LevelSelect() {
    	//DEBUG generate test level data
    	StoredNote[][] ng = {
    			{new StoredNote(600, 0), new StoredNote(1000, 0)},
    			{new StoredNote(800, 1), new StoredNote(1200, 1)},
    			{new StoredNote(600, 2), new StoredNote(1000, 2)},
    			{new StoredNote(800, 3), new StoredNote(1200, 3)}
    	};
    	for (int i = 1; i <= 20; i ++)
    		levels.add(new Level("Test " + i, "Ricardo", ng));
        //create cardList
        for (int i = 0; i < levels.size() - 1; i ++)
        	cardList.add(new LevelCard(levels.get(i)));
        //sort cardList
        
        //calculate number of pages
        numPages = (int) Math.ceil((double) cardList.size() / pageMax);
        //start at first page
        page = 1;
        //initialize GUI, pass this object as parameter
        graphicsHandler = new LevelSelectGraphics(this);
    }
    
    @Override
    public void update(long delta) {
    	//do nothing
    }
    
    @Override
    public void changeScene(Scene scene) {
    	//No implementation so far...
    }
    
    //cast this object as JPanel and return it
    @Override
    public JPanel getPanel() {
    	return (JPanel)this;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //No implementation...
    }
    //get level cards in current page
    public ArrayList<LevelCard> getPageCards() {
    	//create arraylist of size pageMax
    	ArrayList<LevelCard> pageCards = new ArrayList<LevelCard>(pageMax);
    	//iterate through cardList
    	int pos = pageMax * (page - 1);
    	for (int i = pos; i < cardList.size() && i < pos + pageMax; i ++)
    		pageCards.add(cardList.get(i));
    	return pageCards;
    }
    //change cardList pages (true = next page, false = prev page)
    public void changePage(boolean nextP) {
    	//page logic
    	if (nextP) {
    		page += 1;
    		if (page > numPages) page = 1;
    	} else {
    		page -= 1;
    		if (page < 1) page = numPages;
    	}
    	//refresh graphics
    	graphicsHandler.refreshList();
    }
    //get current page number
    public int getPage() {
    	return page;
    }
    //get number of pages
    public int getNumPages() {
    	return numPages;
    }
    //get max number of cards per page
    public int getPageMax() {
    	return pageMax;
    }
}