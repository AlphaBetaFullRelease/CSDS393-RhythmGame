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
    //colors
    private final Color cHeader = Color.cyan;
    private final Color cButton = Color.pink;
    private final Color cBody = Color.blue;
    private final Color cCard = Color.white;
    
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
        pHeader.setBackground(cHeader);
        
        JPanel pBody = new JPanel();
        pBody.setLayout(null);
        pBody.setBounds(0, contButtonHeight, width, height - contButtonHeight);
        pBody.setBackground(cBody);
        //dropdown
        JComboBox bSort = new JComboBox(new String[]{"Title A-Z","Author A-Z","Diff Asc", "Diff Desc", "Dur Asc", "Dur Desc"});
        bSort.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String s = (String) bSort.getSelectedItem();
                        //TODO: just call the specific methods from levelselect here
                        switch (s) {
                            case "Title A-Z":
                                levelSelect.sortCardsTitle();
                                break;
                            case "Author A-Z":
                                levelSelect.sortCardsAuthor();
                                break;
                            case "Diff Asc":
                                levelSelect.sortCardsDiff(true);
                                break;
                            case "Diff Desc":
                                levelSelect.sortCardsDiff(false);
                                break;
                            case "Dur Asc":
                                //
                                break;
                            case "Dur Desc":
                                //
                                break;
                        }
                        //refresh graphics
                        refreshList();
                    }
                }
        );
        bSort.setBackground(cButton);
        bSort.setBorder(BorderFactory.createTitledBorder("Sort"));
        bSort.setBounds(width - contButtonWidth, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bSort);
        //buttons
        //only draw them if there is more than one page
        if (levelSelect.getNumPages() > 1) {
            JButton bPrev = new JButton("<");
            //add action listener to go to previous page
            bPrev.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            levelSelect.changePage(false);
                        }
                    }
            );
            bPrev.setBackground(cButton);
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
            bNext.setBackground(cButton);
            bNext.setBounds(width - navButtonWidth, navButtonY, navButtonWidth, navButtonHeight);
            pBody.add(bNext);
        }

        JButton bExit = new JButton("main menu");
        bExit.setBackground(cButton);
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
        bOpen.setBackground(cButton);
        bOpen.setBounds(0, height -  2 * contButtonHeight, contButtonWidth, contButtonHeight);
        pBody.add(bOpen);

        JButton bRefresh = new JButton("refresh");
        //add action listener to refresh level data
        bRefresh.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //reload levels
                        levelSelect.loadLevelsCards();
                        //reset sort method to reflect changes
                        bSort.setSelectedIndex(0);
                        //refresh list graphics
                        refreshList();
                    }
                }
        );
        bRefresh.setBackground(cButton);
        bRefresh.setBounds(contButtonWidth + cardWidth - 3 * pageNumberWidth, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bRefresh);
        //labels
        JLabel lHeader = new JLabel("Level Select");
        lHeader.setBounds(contButtonWidth, 0, cardWidth - pageNumberWidth, contButtonHeight);
        pHeader.add(lHeader);
        
        pageDisplay = new JLabel("1");
        pageDisplay.setBackground(cButton);
        pageDisplay.setBorder(BorderFactory.createTitledBorder("#"));
        pageDisplay.setBounds(contButtonWidth + cardWidth - pageNumberWidth, 0, 
        pageNumberWidth, contButtonHeight);
        pHeader.add(pageDisplay);
        //level list
        listDisplay = new ListDisplay();
        listDisplay.setBounds(contButtonWidth, 0, cardWidth, height - contButtonHeight);
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
            //make list transparent
            this.setOpaque(false);
            //set list border
            this.setBorder(BorderFactory.createLoweredBevelBorder());
    	}
        //method to draw level cards
        private JPanel cardgetDisplay(LevelCard card) {
            //initialize display panel
            JPanel display = new JPanel();
            //set layout to null and set dimensions
            display.setLayout(null);
            display.setBounds(0, 0, 600, 80);
            //make background transparent
            display.setOpaque(false);
            //initialize header
            JPanel header = new JPanel();
            header.setLayout(null);
            header.setBounds(0, 0, 480, 80);
            header.setOpaque(false);
            display.add(header);
            //initialize score area
            JPanel scoreArea = new JPanel();
            scoreArea.setLayout(null);
            scoreArea.setBounds(480, 0, 120, 80);
            scoreArea.setOpaque(false);
            display.add(scoreArea);
            //text
            JLabel score = new JLabel("No Highscore");
            score.setBounds(0, 0, 120, 20);
            scoreArea.add(score);

            JLabel grade = new JLabel("-");
            grade.setBounds(0, 20, 120, 40);
            scoreArea.add(grade);

            JLabel title = new JLabel(card.getTitle());
            title.setBounds(0, 0, 252, 27);
            header.add(title);

            JLabel author = new JLabel(card.getCreator());
            author.setBounds(0, 27, 360, 26);
            header.add(author);

            JLabel duration = new JLabel(card.getDuration());
            duration.setBounds(0, 53, 360, 26);
            header.add(duration);

            for (int i = 1; i <= 4; i ++) {
                //create star panel (this can be changed to an icon later)
                JPanel star = new JPanel();
                //position panel
                star.setBounds(252 + 27 * (i-1), 0, 27, 27);
                //set color
                if (i <= card.getDifficulty()) star.setBackground(Color.yellow);
                else star.setBackground(Color.gray);
                //add to difficulty display
                header.add(star);
            }

            return display;
        }
    	//method to draw the list
    	private void drawList(ArrayList<LevelCard> list) {
    		//clear list
    		this.removeAll();
    		//draw list
    		for (final LevelCard card : list) {
                //create button
    			JButton cardButton = new JButton();
                //add actionlistener to open level selected
                cardButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.out.println(card.getLevel());
                        }
                    }
                );
                //set layout and position
    			cardButton.setLayout(null);
                cardButton.setBackground(cCard);
    			cardButton.setBounds(0, cardHeight * (list.indexOf(card)), cardWidth, cardHeight);
                //get level card display panel and add it to the button
    			cardButton.add(cardgetDisplay(card));
                //add card button to the listDisplay
    			this.add(cardButton);
    		}
    	}
    }
}
