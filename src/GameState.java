import java.util.ArrayList;

// stores dynamic information about the game's state to communicate between Game and GraphicsRenderer
public class GameState {
    // stores the notes that are on each track
    private ArrayList<ArrayList<Note>> tracks;
    // player performance information
    private final double maxHealth = 100;
    private int noteHits;
    private int noteMisses;
    private int health;
    private int score;

    public GameState(int lanes){
    	this.tracks = new ArrayList<ArrayList<Note>>(lanes);
    	for (int i = 0; i < lanes; i ++) {
    		this.tracks.add(new ArrayList<Note>(1));
    	}
        // initialize fields
        this.noteHits = 0;
        this.noteMisses = 0;
        this.health = 100;
        this.score = 0;
    }

    // returns the tracks array
    public ArrayList<ArrayList<Note>> getTracks(){
        return tracks;
    }

    public double getHealth(){ return health; }

    public double getMaxHealth() { return maxHealth; }

    public int getScore(){ return score; }

    public void setTracks(ArrayList<ArrayList<Note>> t){
        this.tracks = t;
    }

    public void spawnNote(int lane, Note n){
    	tracks.get(lane).add(n);
    }

    public void noteMiss() {
        noteMisses ++;
        health -= 5;
    }

    public void noteHit() {
        noteHits ++;
        score += 1;
    }
    // clears all tracks
    public void resetTracks(){
        int num = tracks.size();
        tracks = new ArrayList<>();
        for (int i = 0; i < num; i ++)
            this.tracks.add(new ArrayList<>());
    }
}
