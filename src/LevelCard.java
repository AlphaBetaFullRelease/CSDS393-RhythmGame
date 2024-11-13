import javax.swing.*;
import java.awt.*;

public class LevelCard {
	//level id
	private int id;
    // level title
    private String title;
    // level creator
    private String creator;
    // level duration
    private String duration;
    // level difficulty
    private int difficulty;
    
    public LevelCard(Level l) {
    	this.title = l.getTitle();
    	this.creator = l.getCreator();
    	this.duration = l.getDurationString();
    	this.difficulty = l.getDifficulty();
		//read player score update score info if entry exists
		getUserScore();
    }
    //method that creates a JPanel display of the level card
    public JPanel getDisplay() {
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
    	JLabel score = new JLabel("------");
    	score.setBounds(0, 0, 120, 20);
    	scoreArea.add(score);
    	
    	JLabel grade = new JLabel("-");
    	grade.setBounds(0, 20, 120, 40);
    	scoreArea.add(grade);
    	
    	JLabel title = new JLabel(getTitle());
    	title.setBounds(0, 0, 360 - 20, 27);
    	header.add(title);
    	
    	JLabel author = new JLabel(getCreator());
    	author.setBounds(0, 27, 360, 26);
    	header.add(author);
    	
    	JLabel duration = new JLabel(getDuration());
    	duration.setBounds(0, 53, 360, 26);
    	header.add(duration);

		//draw difficulty
		for (int i = 1; i <= 4; i ++) {
			//create panel
			JPanel star = new JPanel();
			//position panel
			star.setBounds(340 + 27 * (i-1), 0, 27, 27);
			//set color
			if (i <= getDifficulty()) star.setBackground(Color.yellow);
			else star.setBackground(Color.gray);
			//add to header
			header.add(star);
		}
    	
    	return display;
    }
    
    public int getId() { return id; }
    
    public String getTitle() { return title; }
    
    public String getCreator() { return creator; }
    
    public String getHeader() { return title + " - " + creator; }
    
    public String getDuration() { return duration; }
    
    public int getDifficulty() { return difficulty; }
	//method to read user scores and set level card score data accordingly
	public void getUserScore() {
		//no implementation...
	}
}
