public class LevelCard {
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
    	this.duration = l.getDuration();
    	this.difficulty = l.getDifficulty();
    }
    
    public String getHeader() {
    	return title + " - " + creator;
    }
    
    public String getDuration() {
    	return duration;
    }
    
    public String getDifficulty() {
    	return Integer.toString(difficulty);
    }
}
