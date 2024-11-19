import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//holds all static data about a level & provides functions to load / save a level file
public class Level {
    //store unique level id
    private int id;
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

    public String getSongPath(){ return songPath; }

	//saves the level data to a json file
    public void saveToFile(String dest) {

    }

    //loads a level from a json file
    public static Level loadFromFile(String path) throws FileNotFoundException {
        // do you think dr chaudhary would kill me if he saw this jerry-rigged json parser??
        // i think under this current implementation a json file with { [ } or ] inside a
        // string value will cause this to throw a parse error. however i don't see any
        // reason for that scenario to arise in normal use unless the end user specifically
        // messes with the level files after creation
        // get the file
        File file = new File(path);
        // intialize scanner
        Scanner scanner = new Scanner(file);
        // initialize parsing values
        String openBrackets = "";
        String title = "", author = "", audioPath = "";
        boolean colonSeen = false, inString = false;
        String key = "", value = "";
        int lane = -1, tempo = -1, difficulty = -1;
        double time = -1, duration = -1, delay = -1;
        long pos = -1;
        ArrayList<ArrayList<StoredNote>> notes = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            notes.add(new ArrayList<>());
        }
        // iterate through lines of the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            colonSeen = false;
            inString = false;
            key = "";
            value = "";
            for (char c : line.toCharArray()) {
                if (c == '{' || c == '[') {
                    openBrackets += c;
                } else if (c == '"') {
                    if (openBrackets.lastIndexOf('"') == openBrackets.length() - 1) {
                        openBrackets = openBrackets.substring(0, openBrackets.length() - 1);
                        inString = false;
                    } else {
                        openBrackets += '"';
                        inString = true;
                    }
                } else if (c == '}') {
                    if (openBrackets.lastIndexOf('{') == openBrackets.length() - 1) {
                        openBrackets = openBrackets.substring(0, openBrackets.length() - 1);
                    } else {
                        throw new RuntimeException("Parse error: mismatched brackets");
                    }
                } else if (c == ']') {
                    if (openBrackets.lastIndexOf('[') == openBrackets.length() - 1) {
                        openBrackets = openBrackets.substring(0, openBrackets.length() - 1);
                    } else {
                        throw new RuntimeException("Parse error: mismatched brackets");
                    }
                } else if (c == ':') {
                    colonSeen = true;
                } else if (inString) {
                    if (!colonSeen) {
                        key += c;
                    } else {
                        value += c;
                    }
                }
            }

            System.out.printf("%s: %s\n", key, value);

            if (key.equals("name")) {
                title = value;
            } else if (key.equals("author")) {
                author = value;
            } else if (key.equals("delay")) {
                delay = Integer.parseInt(value);
            } else if (key.equals("difficulty")) {
                difficulty = Integer.parseInt(value);
            } else if (key.equals("tempo")) {
                tempo = Integer.parseInt(value);
            } else if (key.equals("audio")) {
                audioPath = value;
            } else if (key.equals("time")) {
                time = Double.parseDouble(value);
                pos = (int) ((60 / tempo * time + delay) * 1000);
            } else if (key.equals("lane")) {
                lane = Integer.parseInt(value);
            } else if (key.equals("duration")) {
                duration = Double.parseDouble(value);
                notes.get(lane).add(new StoredNote(pos, lane));
            }
        }

        StoredNote[][] tracks = {new StoredNote[notes.get(0).size()], new StoredNote[notes.get(1).size()], new StoredNote[notes.get(2).size()], new StoredNote[notes.get(3).size()]};
        for (int i = 0; i < tracks.length; i++) {
            for (int j = 0; j < tracks[i].length; j++) {
                tracks[i][j] = notes.get(i).get(j);
            }
        }

        Level level = new Level(title, author, tracks);
        level.setDifficulty(difficulty);
        level.setSongPath(audioPath);
        level.setStartDelay((long) (delay * 1000));
        level.setTempo(tempo);
        level.calculateDuration();

        return level;
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

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
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

    // remove this main method once all testing of the file is complete
    /*public static void main(String[] args) {
        try {
            Level level = loadFromFile("src\\test.json");
            System.out.println(level.duration);
            System.out.println(level.getDurationString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
