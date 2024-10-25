// holds all static data about a level & provides functions to load / save a level file
public class Level {
    // store note data
    private StoredNote[][] noteGrid;
    // store path to mp3

    public Level(){

    }

    // saves the level data to a json file
    public void saveToFile(String dest){

    }

    // loads a level from a json file
    public Level loadFromFile(String path){
        return null;
    }

    //get StoredNote from noteGrid
    public StoredNote getStoredNote(int index, int track){
        return noteGrid[index][track];
    }
}
