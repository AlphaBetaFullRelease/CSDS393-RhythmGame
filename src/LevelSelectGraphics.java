import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

//does all of the graphical work for the level select
public class LevelSelectGraphics {
	//reference to level select object
	private LevelSelect levelSelect;
	//main JPanel
	private JPanel mainPanel;
	//page number display
    private JLabel pageDisplay;
    //list display
    private ListDisplay listDisplay;
    //layout info
    private final int width = 800;
    private final int height = 450;
    private final int cardWidth = 600;
    private final int cardHeight = 80;
    private final int navButtonY = 130;
    private final int navButtonWidth = 50;
    private final int navButtonHeight = 100;
    private final int contButtonWidth = 100;
    private final int contButtonHeight = 50;
    private final int pageNumberWidth = 50;
    
    public LevelSelectGraphics(LevelSelect s) {
    	//get level select
    	this.levelSelect = s;
    	//get main jpanel
    	this.mainPanel = s.getPanel();
    	// JPanel properties
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setFocusable(true);
        mainPanel.setLayout(null);
        //initialize GUI panels
        JPanel pHeader = new JPanel();
        pHeader.setLayout(null);
        pHeader.setBounds(0, 0, width, contButtonHeight);
        pHeader.setBackground(Color.green);
        
        JPanel pBody = new JPanel();
        pBody.setLayout(null);
        pBody.setBounds(0, contButtonHeight, width, height - contButtonHeight);
        pBody.setBackground(Color.orange);
        //buttons
        JButton bPrev = new JButton("<");
        //add action listener to go to previous page
        bPrev.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			levelSelect.changePage(false);
        		}
        	}
        );
        bPrev.setBounds(0, navButtonY, navButtonWidth, navButtonHeight);
        pBody.add(bPrev);
        
        JButton bNext = new JButton(">");
        //add action listener to go to next page
        bNext.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			levelSelect.changePage(true);
        		}
        	}
        );
        bNext.setBounds(width - navButtonWidth, navButtonY, navButtonWidth, navButtonHeight);
        pBody.add(bNext);
        
        JButton bExit = new JButton("main menu");
        bExit.setBounds(0, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bExit);
        
        JButton bOpen = new JButton("level folder");
        //add action listener to open level folder path in file explorer
        bOpen.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Desktop.getDesktop().open(levelSelect.getLevelsPath());
                        } catch (IOException e1) {
                            //TODO: idk what to do here
                        }
                    }
                }
        );
        bOpen.setBounds(0, height -  2 * contButtonHeight, contButtonWidth, contButtonHeight);
        pBody.add(bOpen);
        //labels
        JLabel lHeader = new JLabel("Level Select");
        lHeader.setBounds(contButtonWidth, 0, cardWidth - pageNumberWidth, contButtonHeight);
        pHeader.add(lHeader);
        
        pageDisplay = new JLabel("1");
        pageDisplay.setBounds(contButtonWidth + cardWidth - pageNumberWidth, 0, 
        pageNumberWidth, contButtonHeight);
        pHeader.add(pageDisplay);
        //dropdown
        JComboBox bSort = new JComboBox(new String[]{"Name A-Z","Author A-Z","Diff Asc"});
        bSort.setBounds(width - contButtonWidth, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bSort);
        //level list
        listDisplay = new ListDisplay();
        listDisplay.setBounds(contButtonWidth, 0, cardWidth, height - contButtonHeight);
        listDisplay.setBackground(Color.blue);
        pBody.add(listDisplay);
        
        mainPanel.add(pHeader);
        mainPanel.add(pBody);
        //refresh card display
        refreshList();
    }
    //redraw the level card list
    public void refreshList() {
    	//update list
    	listDisplay.drawList(levelSelect.getPageCards());
    	//update page number display
    	pageDisplay.setText("" + levelSelect.getPage() + "/" + levelSelect.getNumPages());
    	//redraw JPanel
    	mainPanel.revalidate();
    	mainPanel.repaint();
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
                //create button
    			JButton cardButton = new JButton();
                //set layout and position
    			cardButton.setLayout(null);
    			cardButton.setBounds(0, cardHeight * (i -1), cardWidth, cardHeight);
                //get level card display panel and add it to the button
    			cardButton.add(list.get(i - 1).getDisplay());
                //add card button to the listDisplay
    			this.add(cardButton);
    		}
    	}
    }
}
