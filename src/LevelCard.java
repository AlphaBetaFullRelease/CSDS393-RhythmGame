//
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
    // level highscore
    private int highScore;
    // level grade
    private char grade;
    
    public LevelCard(Level l, UserData.LevelScore score) {
        this.level = l;
    	this.title = l.getTitle();
    	this.creator = l.getCreator();
    	this.duration = l.getDurationString();
    	this.difficulty = l.getDifficulty();
        this.highScore = score.getHighScore();
        this.grade = score.getGrade();
    }

    public int getHighScore() { return highScore; }

    public char getGrade() { return grade; }

    public Level getLevel() { return level; }
    
    public String getTitle() { return title; }
    
    public String getCreator() { return creator; }
    
    public String getDuration() { return duration; }
    
    public int getDifficulty() { return difficulty; }
	//method to read user scores and set level card score data accordingly
	public void getUserScore() {
		//no implementation...
	}
}
