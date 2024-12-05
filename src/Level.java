//holds all static data about a level & provides functions to load / save a level file
public class Level {
    //store note data (kept in order by spawn time)
    private StoredNote[][] noteGrid;
    //store path to mp3
    private String songPath = "";
    //level tempo (bpm)
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

    // keeps track of the last note spawned / next note that can be spawned
    private int[] spawnIndex;

    public Level(String title, String creator, StoredNote[][] noteGrid){
    	this.title = title;
    	this.creator = creator;
    	this.noteGrid = noteGrid;
        spawnIndex = new int[noteGrid.length];
        // initialize all indices to 0
        for(int i = 0; i < spawnIndex.length; i++)
            spawnIndex[i] = 0;
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

    public int getTempo(){
        return tempo;
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
    
    // get StoredNote from noteGrid
    public StoredNote getStoredNote(int track, int index) {
        return noteGrid[track][index];
    }

    // given a track and a time, will return the next note that can spawn in the track
    // targetTime - the time (in ms) that the note should align with the center of the target line
    // this function will return null if no notes can spawn at this time
    public StoredNote getNextNote(int track, long targetTime){
        // check if there are any notes left to spawn
        if(spawnIndex[track] >= noteGrid[track].length)
            return null;
        
        // check if the next note to spawn can spawn
        if(noteGrid[track][spawnIndex[track]].getPos() <= targetTime){
            // increment spawn index
            spawnIndex[track]++;

            // return the note
            return noteGrid[track][spawnIndex[track]-1];
        }

        // no note can be spawned
        return null;
    }
}
