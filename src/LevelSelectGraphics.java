import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
// does all of the graphical work for the level select
public class LevelSelectGraphics {
	// reference to level select object
	private LevelSelect levelSelect;
	// level select JPanel
	private JPanel mainPanel;
	// page number display
    private JLabel pageDisplay;
    // list display
    private ListDisplay listDisplay;
    // layout info
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
    // colors
    private final Color cHeader = new Color(151, 109, 194);
    private final Color cButton = new Color(106,65,148);
    private final Color cBody = new Color(138, 121, 156);
    private final Color cCard = new Color(71,49,94);
    private final Color cDiffStar = Color.red;
    private final Color cDiffCont = Color.black;
    
    public LevelSelectGraphics(LevelSelect s) {
    	// get level select object
    	this.levelSelect = s;
    	// get level select as a JPanel
    	this.mainPanel = s.getPanel();
    	// JPanel properties
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setFocusable(true);
        mainPanel.setLayout(null);
        // initialize panels to which we will add each UI component
        // header
        JPanel pHeader = new JPanel();
        pHeader.setLayout(null);
        pHeader.setBounds(0, 0, width, contButtonHeight);
        pHeader.setBackground(cHeader);
        // body
        JPanel pBody = new JPanel();
        pBody.setLayout(null);
        pBody.setBounds(0, contButtonHeight, width, height - contButtonHeight);
        pBody.setBackground(cBody);
        // create interactive elements
        // sort types dropdown
        JComboBox bSort = new JComboBox(new String[]{"Title A-Z","Author A-Z","Diff Asc", "Diff Desc"});
        bSort.setForeground(Color.white);
        // action listener to tell level select to sort cards according to selection
        bSort.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String s = (String) bSort.getSelectedItem();
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
                        }
                        // refresh graphics
                        refreshList();
                    }
                }
        );
        bSort.setBackground(cButton);
        TitledBorder sortBorder = BorderFactory.createTitledBorder("Sort");
        sortBorder.setTitleColor(Color.white);
        bSort.setBorder(sortBorder);
        bSort.setBounds(width - contButtonWidth, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bSort);
        // navigation buttons, only drawn if there is more than one page of cards
        if (levelSelect.getNumPages() > 1) {
            // previous page button
            JButton bPrev = new JButton("<");
            bPrev.setForeground(Color.white);
            // add action listener to go to previous page
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
            // next page button
            JButton bNext = new JButton(">");
            bNext.setForeground(Color.white);
            // add action listener to go to next page
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
        // main menu button
        JButton bExit = new JButton("settings");
        bExit.setForeground(Color.white);
        // add event listener to tell level select to switch scenes
        bExit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        levelSelect.exitToMenu();
                    }
                }
        );
        bExit.setBackground(cButton);
        bExit.setBounds(0, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bExit);
        // level folder button
        JButton bOpen = new JButton("level folder");
        bOpen.setForeground(Color.white);
        // add action listener to open local level path in file explorer
        bOpen.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Desktop.getDesktop().open(levelSelect.getUserData().getLevelPath());
                        } catch (IOException e1) {
                            System.out.println("Error: " + e1.getMessage());
                        }
                    }
                }
        );
        bOpen.setBackground(cButton);
        bOpen.setBounds(0, height -  2 * contButtonHeight, contButtonWidth, contButtonHeight);
        pBody.add(bOpen);
        // refresh levels button
        JButton bRefresh = new JButton("refresh");
        bRefresh.setForeground(Color.white);
        // add action listener to tell level select to refresh level data
        bRefresh.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // reload levels, this also sorts them by name A-Z
                        levelSelect.loadLevelsCards();
                        // reset sort dropdown
                        bSort.setSelectedIndex(0);
                        // refresh list graphics
                        refreshList();
                    }
                }
        );
        bRefresh.setBackground(cButton);
        bRefresh.setBounds(contButtonWidth + cardWidth - 3 * pageNumberWidth, 0, contButtonWidth, contButtonHeight);
        pHeader.add(bRefresh);
        // text labels
        // header text
        JLabel lHeader = new JLabel("Level Select");
        lHeader.setForeground(Color.white);
        lHeader.setBounds(contButtonWidth, 0, cardWidth - pageNumberWidth, contButtonHeight);
        pHeader.add(lHeader);
        // page text
        pageDisplay = new JLabel("1");
        pageDisplay.setBackground(cButton);
        TitledBorder pageBorder = BorderFactory.createTitledBorder("#");
        pageBorder.setTitleColor(Color.white);
        pageDisplay.setBorder(pageBorder);
        pageDisplay.setBounds(contButtonWidth + cardWidth - pageNumberWidth, 0, 
        pageNumberWidth, contButtonHeight);
        pHeader.add(pageDisplay);
        // level list
        listDisplay = new ListDisplay();
        listDisplay.setBounds(contButtonWidth, 0, cardWidth, height - contButtonHeight);
        pBody.add(listDisplay);
        // add panels to level select panel
        mainPanel.add(pHeader);
        mainPanel.add(pBody);
        // refresh card display
        refreshList();
    }
    // method that re-draws the level card list
    public void refreshList() {
    	// tell the list display to re-draw list, getting the page cards from level select
    	listDisplay.drawList(levelSelect.getPageCards());
    	// update page number display
    	pageDisplay.setText("" + levelSelect.getPage() + "/" + levelSelect.getNumPages());
    	// redraw level select panel
    	mainPanel.revalidate();
    	mainPanel.repaint();
    }
    // class to display and manage the level list UI
    private class ListDisplay extends JPanel {
    	// constructor, just initializes the list panel
    	public ListDisplay() {
    		// set panel layout
    		this.setLayout(null);
            // make panel transparent
            this.setOpaque(false);
            // set panel border
            this.setBorder(BorderFactory.createLoweredBevelBorder());
    	}
        // method to actually draw the card list, takes the list as input (provided from level select)
        private void drawList(ArrayList<LevelCard> list) {
            // clear all elements from the list panel
            this.removeAll();
            // draw the card list iterating on each card from the list
            for (final LevelCard card : list) {
                // create the card button
                JButton cardButton = new JButton();
                // add an actionlistener that opens the selected level
                cardButton.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                levelSelect.playLevel(card.getLevel());
                            }
                        }
                );
                // set button layout and position
                cardButton.setLayout(null);
                cardButton.setBackground(cCard);
                cardButton.setBounds(0, cardHeight * (list.indexOf(card)), cardWidth, cardHeight);
                // create a card display panel using the level and add it to the button
                cardButton.add(cardGetDisplay(card));
                // add card button to the list panel
                this.add(cardButton);
            }
        }
        // method that returns a card display (containing the title text, author, song duration, etc.) for a given card
        private JPanel cardGetDisplay(LevelCard card) {
            // initialize card panel
            JPanel display = new JPanel();
            // set panel layout to null and set dimensions
            display.setLayout(null);
            display.setBounds(0, 0, 600, 80);
            // make panel transparent
            display.setOpaque(false);
            // initialize card header panel and add it to card panel
            JPanel header = new JPanel();
            header.setLayout(null);
            header.setBounds(0, 0, 480, 80);
            header.setOpaque(false);
            display.add(header);
            // initialize score area panel and add it to card panel
            JPanel scoreArea = new JPanel();
            scoreArea.setLayout(null);
            scoreArea.setBounds(480, 0, 120, 80);
            scoreArea.setOpaque(false);
            display.add(scoreArea);
            // text elements
            // player highscore
            JLabel score = new JLabel("" + card.getHighScore());
            score.setForeground(Color.white);
            score.setBounds(0, 0, 120, 20);
            scoreArea.add(score);
            // player grade
            JLabel grade = new JLabel("" + card.getGrade());
            grade.setForeground(Color.white);
            grade.setBounds(0, 20, 120, 40);
            scoreArea.add(grade);
            // level title
            JLabel title = new JLabel(card.getTitle());
            title.setForeground(Color.white);
            title.setBounds(0, 0, 252, 27);
            header.add(title);
            // level author
            JLabel author = new JLabel(card.getCreator());
            author.setForeground(Color.white);
            author.setBounds(0, 27, 360, 26);
            header.add(author);
            // song duration
            JLabel duration = new JLabel(card.getDuration());
            duration.setForeground(Color.white);
            duration.setBounds(0, 53, 360, 26);
            header.add(duration);
            // difficulty display
            for (int i = 1; i <= 4; i ++) {
                // create a difficulty 'star' panel (this can be changed to an icon later)
                JPanel star = new JPanel();
                // position the panel using index
                star.setBounds(252 + 27 * (i-1), 0, 27, 27);
                // set panel color according to level difficulty
                if (i <= card.getDifficulty()) star.setBackground(cDiffStar);
                else star.setBackground(cDiffCont);
                // add panel to the header panel
                header.add(star);
            }
            // return the card panel
            return display;
        }
    }
}
