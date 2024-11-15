import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    //store path to level folder
    private String levelPath = "";
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
      if (noteGrid != null) {
            spawnIndex = new int[noteGrid.length];
            // initialize all indices to 0
            for (int i = 0; i < spawnIndex.length; i++)
                spawnIndex[i] = 0;
        }
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

        return String.format("%02d:%02d:%02d", hrs, mins, secs);
    }

    public String getSongPath(){ return songPath; }

	//saves the level data to a json file
    public void saveToFile(String dest) throws IOException {
        File file = new File(dest);
        file.createNewFile();

        FileWriter writer = new FileWriter(dest);
        writer.write("{\n\t\"level\": {\n\t\t\"name\": \"" + title + "\",\n\t\t\"author\": \"" + creator + "\",\n\t\t\"audio\": \"" + songPath + "\",\n\t\t\"tempo\": \"" + tempo + "\",\n\t\t\"delay\": \"" + startDelay + "\",\n\t\t\"difficulty\": \"" + difficulty + "\",\n\t\t\"notes\": [");

        for (int i = 0; i < noteGrid.length; i++) {
            for (StoredNote note : noteGrid[i]) {
                writer.write("\n\t\t\t{\n\t\t\t\t\"time\": \"" + (tempo * (note.getPos() / 1000.0 - startDelay)) / 60.0 + "\",\n\t\t\t\t\"lane\": \"" + i + "\",\n\t\t\t\t\"duration\": \"" + tempo * note.getNote().getDur() / 1000.0 / 60.0 + "\"\n\t\t\t},");
            }
        }

        writer.write("\n\t\t]\n\t}\n}");

        writer.close();
    }

    //loads a level from a json file
    public static Level loadFromFile(String levelPath) throws FileNotFoundException {
        // do you think dr chaudhary would kill me if he saw this jerry-rigged json parser??

        // i think under this current implementation a json file with { [ } or ] inside a
        // string value will cause this to throw a parse error. however i don't see any
        // reason for that scenario to arise in normal use unless the end user specifically
        // messes with the level files after creation
        String path = levelPath + "\\level.json";
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        String openBrackets = "";
        String title = "", author = "";
        // set path to song if exists
        String audioPath = levelPath + "\\song.wav";
        File temp = new File(audioPath);
        if(!temp.isFile()){
            System.out.println("no song.wav found in " + levelPath);
            audioPath = "";
        }
        boolean colonSeen = false, inString = false;
        String key = "", value = "";
        int lane = -1, tempo = -1, difficulty = -1;
        double time = -1, duration = -1, delay = -1;
        long pos = -1;
        ArrayList<ArrayList<StoredNote>> notes = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            notes.add(new ArrayList<>());
        }

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

            //System.out.printf("%s: %s\n", key, value);

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
            } else if (key.equals("time")) {
                time = Double.parseDouble(value);
                pos = (int) ((60.0 / tempo * time) * 1000 + delay);
            } else if (key.equals("lane")) {
                lane = Integer.parseInt(value);
            } else if (key.equals("duration")) {
                duration = Double.parseDouble(value);
                notes.get(lane).add(new StoredNote(pos, lane, (int) ((60.0 / tempo * duration) * 1000)));
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

    public void setDuration(long duration) {
        this.duration = duration;
    }

    // remove this main method once all testing of the file is complete
    /*public static void main(String[] args) {
        try {
            Level level = loadFromFile("src\\test.json");
            level.saveToFile("writetest.json");
            level = loadFromFile("writetest.json");
            level.saveToFile("writetest2.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
