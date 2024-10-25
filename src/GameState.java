import java.util.ArrayList;

// stores dynamic information about the game's state to communicate between Game and GraphicsRenderer
public class GameState {
    // stores the notes that are on each track
    private ArrayList<ArrayList<Note>> tracks = new ArrayList<ArrayList<Note>>();

    public GameState(int tracks){
    	for (int i = 0; i < tracks; i ++) {
    		this.tracks.add(new ArrayList<Note>());
    	}
    }

    // returns the tracks array
    public ArrayList<ArrayList<Note>> getTracks(){
        return tracks;
    }

    public void setTracks(ArrayList<ArrayList<Note>> t){
        this.tracks = t;
    }

    public void spawnNote(int track, Note n){
        tracks.get(track).add(n);
    }
}
