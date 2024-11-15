import java.io.File;
import java.util.ArrayList;

//class to read user data from the data folder
public class UserData {
    //PATH (this DEFINITELY needs to be changed later)
    private final File LOCAL_PATH = new File("C:\\\\Users\\ricar\\Documents\\github\\CSDS393-RhythmGame\\data");
    private final File LEVEL_PATH = new File(LOCAL_PATH.getPath() + "\\levels\\");
    //level data
    private static ArrayList<Level> levels = new ArrayList<>();
    //player scores data
    private static ArrayList<LevelScore> scores = new ArrayList<>();
    //TEMP: settings data
    private static int config;
    //constructor
    public UserData() {
        //what should do?
    }
    //get level path
    public File getLevelPath() { return LEVEL_PATH; }
    //method that loads both the level data and user scores
    public void loadLevelData() {
        //load level data
        //clear previous data
        levels.clear();
        //iterate through directory
        for (final File entry : LEVEL_PATH.listFiles()) {
            //ignore entry if it is not a folder
            if (entry.isDirectory()) {
                //get json file path, assumes all json files are named 'level.json'
                File jsonPath = new File(entry.getPath() + "\\level.json");
                //attempt to read json file
                try {
                    levels.add(Level.loadFromFile(jsonPath.getPath()));
                } catch (java.io.FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        //load player scores
        File scoresJSON = new File(LOCAL_PATH.getPath() + "\\scores.json");
        //DEBUG: create levelscore
        for (Level level : levels) {
            scores.add(new LevelScore(level.getId(), 101010, 'A'));
        }
    }
    //get level list
    public ArrayList<Level> getLevels() { return levels; }
    //get levelscore for a given level
    public LevelScore getLevelScore(Level level) throws RuntimeException {
        int i = 0;
        while (i < scores.size() && scores.get(i).getId() != level.getId()) i ++;
        if (i >= scores.size()) throw(new RuntimeException());
        else {
            return scores.get(i);
        }
    }
    //class to store level scores
    public class LevelScore {
        //level id for this score
        private int id;
        //highscore value
        private int highscore;
        //grade value
        private char grade;
        //constructor
        public LevelScore(int id, int highscore, char grade) {
            this.id = id;
            this.highscore = highscore;
            this.grade = grade;
        }
        //getters
        public int getId() { return id; }
        public int getHighScore() { return highscore; }
        public char getGrade() { return grade; }
        //method to update score
        public void updateScore(int highscore, char grade) {
            this.highscore = highscore;
            this.grade = grade;
            //TODO: write changes to file

        }
    }
    //TODO: write this class for retrieving and storing settings configs
    private class SettingsConfig {

    }
}