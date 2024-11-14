import javax.swing.*;
import java.awt.*;

public class LevelCard {
	//reference to level file
	private Level level;
    // level title
    private String title;
    // level creator
    private String creator;
    // level duration
    private String duration;
    // level difficulty
    private int difficulty;
    
    public LevelCard(Level l) {
        this.level = l;
    	this.title = l.getTitle();
    	this.creator = l.getCreator();
    	this.duration = l.getDurationString();
    	this.difficulty = l.getDifficulty();
		//read player score update score info if entry exists
		getUserScore();
    }
    
    public Level getLevel() { return level; }
    
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
