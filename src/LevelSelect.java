import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;

public class LevelSelect extends JPanel implements ActionListener, Scene {
    //DEBUG temp level folder path
    private final File levelsPath = new File("C:\\\\Users\\ricar\\Documents\\github\\CSDS393-RhythmGame\\levels");
	//level list
    private ArrayList<Level> levels = new ArrayList<>();
    //level card list
    private ArrayList<LevelCard> cardList = new ArrayList<>();
    //cards shown per page
    private final int pageMax = 5;
    //number of pages in use
    private int numPages = 1;
    //current page number
    private int page;
    //graphics handler for level select
    private final LevelSelectGraphics graphicsHandler;
    
    public LevelSelect() {
    	//load level data
        loadLevels();
        //create cardList
        for (Level level : levels)
        	cardList.add(new LevelCard(level));
        //calculate the number of pages
        numPages = (int) Math.ceil((double) (cardList.size() + 1) / pageMax);
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
    public void setSceneRunner(SceneRunner sr) {
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
    	ArrayList<LevelCard> pageCards = new ArrayList<>(pageMax);
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
    public int getPage() { return page; }
    //get number of pages
    public int getNumPages() { return numPages; }
    //get max number of cards per page
    public int getPageMax() { return pageMax; }
    //get level folder path
    public File getLevelsPath() { return levelsPath; }
    //method to sort levelCard list
    public void sortCards(int type) {
        switch (type) {
            case 0:
                //Title A-Z
            break;
            case 1:
                //Author A-Z
            break;
            default:
                //default
            break;
        }
        //refresh graphics
        graphicsHandler.refreshList();
    }
    //load levels from level folder
    public void loadLevels() {
        //iterate through folders in levelsPath
        for (final File entry : levelsPath.listFiles()) {
            //ignore entry if it is not a folder
            if (entry.isDirectory()) {
                //get json file path, assumes all json files are named 'level.json'
                File jsonPath = new File(entry.getPath() + "\\level.json");
                //attempt to read json file
                try {
                    levels.add(Level.loadFromFile(jsonPath.getPath()));
                } catch (java.io.FileNotFoundException e) {
                    //TODO: how to handle this exception?
                    e.printStackTrace();
                }
            }
        }
    }
}