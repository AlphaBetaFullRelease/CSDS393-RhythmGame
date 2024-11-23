//holds all static data about a level & provides functions to load / save a level file
public class Level {
    //store unique level id
    private int id;
    //store note data (kept in order by spawn time)
    private StoredNote[][] noteGrid;
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
    	//set unique id
    	this.id = generateId(); //TO DO: use system time + random number to make id?
    	this.title = title;
    	this.creator = creator;
    	this.noteGrid = noteGrid;
        spawnIndex = new int[noteGrid.length];
        // initialize all indices to 0
        for(int i = 0; i < spawnIndex.length; i++)
            spawnIndex[i] = 0;
    }
    //TODO: code that generates a unique id using the system clock
    private static int generateId() {
    	return 0;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }
    
    public String getCreator() { return creator; }
    
    public int getDifficulty() { return difficulty; }
    
    public String getDurationString() {
        int hrs = (int) (duration / 60 / 60);
        int mins = (int) (duration / 60 % 60);
        int secs = (int) (duration % 60);

        return String.format("%2d:%2d:%2d", hrs, mins, secs);
    }

    //get the size of noteGrid[i]
    public int getTrackLength(int track) {
    	return noteGrid[track].length;
    }
    
    //get StoredNote from noteGrid
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

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getSongPath() {
        return "./data\\levels\\Mary had a little lamb\\song.wav";
    }

    public void setStartDelay(long startDelay) {
        this.startDelay = startDelay;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    private void calculateDuration() {
        int lastNoteTime = (int) (noteGrid[0][noteGrid[0].length - 1].getNote().getPos() / 1000);

        for (int i = 1; i < 4; i++) {
            if (noteGrid[i].length > 0) {
                if (noteGrid[i][noteGrid[i].length - 1].getNote().getPos() / 1000 > lastNoteTime) {
                    lastNoteTime = (int) (noteGrid[i][noteGrid[i].length - 1].getNote().getPos() / 1000);
                }
            }
        }

        this.duration = lastNoteTime;
    }
}
