import java.util.ArrayList;

// stores dynamic information about the game's state to communicate between Game and GraphicsRenderer
public class GameState {
    // stores the notes that are on each track
    public ArrayList<ArrayList<Note>> tracks = new ArrayList<>();

    // stores the width & height of the game
    

    public GameState(){}

    // returns the tracks array
    public ArrayList<ArrayList<Note>> getTracks(){
        return tracks;
    }
}
