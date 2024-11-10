//holds all static data about a level & provides functions to load / save a level file
public class Level {
    //store note data
    private StoredNote[][] noteGrid;
    //store path to mp3
    private String songPath = "";
    //level tempo
    private int tempo = 130;
    //level start delay (ms)
    private long startDelay = 0;
    //level title
    private String title;
    //level creator
    private String creator;
    //level duration
    private long duration;
    //level difficulty
    private int difficulty;

    public Level(String title, String creator, StoredNote[][] noteGrid){
    	this.title = title;
    	this.creator = creator;
    	this.noteGrid = noteGrid;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public String getCreator() {
    	return creator;
    }
    
    public int getDifficulty() {
    	return difficulty;
    }
    
    public String getDuration() {
    	return "00:00:00";
    }

    public String getSongPath(){
        return songPath;
    }

	//saves the level data to a json file
    public void saveToFile(String dest) {

    }

    //loads a level from a json file
    public Level loadFromFile(String path) {
        return null;
    }
    
    //get the size of noteGrid[i]
    public int getTrackLength(int track) {
    	return noteGrid[track].length;
    }
    
    //get StoredNote from noteGrid
    public StoredNote getStoredNote(int track, int index) {
        return noteGrid[track][index];
    }
}
