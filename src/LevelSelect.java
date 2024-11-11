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
    //page number display
    private JLabel pageDisplay;
    //list display
    private ListDisplay listDisplay;
    
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
    	// JPanel properties
        this.setPreferredSize(new Dimension(800, 450));
        this.setFocusable(true);
        //create cardList
        for (int i = 0; i < levels.size() - 1; i ++)
        	cardList.add(new LevelCard(levels.get(i)));
        //sort cardList
        
        //calculate number of pages
        numPages = (int) Math.ceil((double) cardList.size() / pageMax);
        //start at first page
        page = 1;
        //set panel layout
        getPanel().setLayout(null);
        //initialize GUI panels
        JPanel pHeader = new JPanel();
        pHeader.setLayout(null);
        pHeader.setBounds(0, 0, 800, 50);
        pHeader.setBackground(Color.green);
        
        JPanel pBody = new JPanel();
        pBody.setLayout(null);
        pBody.setBounds(0, 50, 800, 400);
        pBody.setBackground(Color.orange);
        //buttons
        JButton bPrev = new JButton("<");
        bPrev.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			changePage(false);
        		}
        	}
        );
        bPrev.setBounds(0, 130, 50, 100);
        pBody.add(bPrev);
        
        JButton bNext = new JButton(">");
        bNext.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			changePage(true);
        		}
        	}
        );
        bNext.setBounds(750, 130, 50, 100);
        pBody.add(bNext);
        
        JButton bExit = new JButton("main menu");
        bExit.setBounds(0, 0, 100, 50);
        pHeader.add(bExit);
        
        JButton bOpen = new JButton("level folder");
        bOpen.setBounds(0, 350, 100, 50);
        pBody.add(bOpen);
        //labels
        JLabel lHeader = new JLabel("Level Select");
        lHeader.setBounds(100, 0, 550, 50);
        pHeader.add(lHeader);
        
        pageDisplay = new JLabel("1");
        pageDisplay.setBounds(650, 0, 50, 50);
        pHeader.add(pageDisplay);
        
        //dropdown
        JComboBox bSort = new JComboBox(new String[]{"Name A-Z","Author A-Z","Diff Asc"});
        bSort.setBounds(700, 0, 100, 50);
        pHeader.add(bSort);
        //level list
        listDisplay = new ListDisplay();
        listDisplay.setBounds(100, 0, 600, 400);
        listDisplay.setBackground(Color.blue);
        pBody.add(listDisplay);
        
        getPanel().add(pHeader);
        getPanel().add(pBody);
        //refresh card display
        refreshList();
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
    	refreshList();
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
    //redraw the level card list
    public void refreshList() {
    	//update list
    	listDisplay.drawList(this.getPageCards());
    	//update page number display
    	pageDisplay.setText("" + getPage() + "/" + getNumPages());
    	//redraw JPanel
    	getPanel().revalidate();
    	getPanel().repaint();
    }
    //class to manage the list display
    private class ListDisplay extends JPanel {
    	//constructor, just draw list
    	public ListDisplay() {
    		//set layout
    		this.setLayout(null);
    	}
    	//method to draw the list
    	private void drawList(ArrayList<LevelCard> list) {
    		//clear list
    		this.removeAll();
    		//draw list
    		for (int i = 1; i <= list.size(); i ++) {
    			JButton cardButton = new JButton(list.get(i - 1).getHeader());
    			cardButton.setBounds(0, 80 * (i -1), 600, 80);
    			this.add(cardButton);
    		}
    	}
    }
}