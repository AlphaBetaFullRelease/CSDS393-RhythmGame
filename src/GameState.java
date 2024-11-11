import java.util.ArrayList;

// stores dynamic information about the game's state to communicate between Game and GraphicsRenderer
public class GameState {
    // stores the notes that are on each track
    public ArrayList<ArrayList<Note>> tracks;

    public GameState(int lanes){
    	this.tracks = new ArrayList<ArrayList<Note>>(lanes);
    	for (int i = 0; i < lanes; i ++) {
    		this.tracks.add(new ArrayList<Note>(1));
    	}
    }

    // returns the tracks array
    public ArrayList<ArrayList<Note>> getTracks(){
        return tracks;
    }

    public void setTracks(ArrayList<ArrayList<Note>> t){
        this.tracks = t;
    }

    public void spawnNote(int lane, Note n){
    	tracks.get(lane).add(n);
    }
}
