import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;

// class to read user data from the data folder
public class UserData {
    // PATHS, temporary for testing
    private static final File LOCAL_PATH = new File("./data");
    private static final File LEVEL_PATH = new File(LOCAL_PATH.getPath() + "\\levels\\");
    private static final File SETTINGS_PATH = new File(LOCAL_PATH.getPath() + "\\settings.json");
    private static final File SCORE_PATH = new File(LOCAL_PATH.getPath() + "\\scores.json");
    // level data
    private static ArrayList<Level> levels = new ArrayList<>();
    // player scores data
    private static ArrayList<LevelScore> scores = new ArrayList<>();
    // settings data
    private SettingsConfig settingsConfig;
    // constructor
    public UserData() {
        System.out.println("user data object created");
        //DEBUG: create test level and score data
        StoredNote[][] ng = {
                {new StoredNote(600, 0)},
                {new StoredNote(610, 1)},
                {new StoredNote(620, 2)},
                {new StoredNote(630, 3)}
        };
        createLevelFile(new Level("song", "creator", ng));
    }
    // basic getters
    public File getLevelPath() { return LEVEL_PATH; }
    public ArrayList<Level> getLevels() { return levels; }
    // store level file
    public boolean createLevelFile(Level level) {
        // get level title for folder name
        String title = level.getTitle();
        // create file path using title
        File levelPath = new File(LEVEL_PATH.getPath() + "\\" + title);
        // return false if path already exists
        if (levelPath.exists()) return false;
        // create folder
        levelPath.mkdir();
        // create file path
        levelPath = new File(levelPath.getPath() + "\\level.json");
        // create file
        Gson gson = new Gson();
        try {
            Writer fileWriter = Files.newBufferedWriter(levelPath.toPath());
            gson.toJson(level, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    // method that loads the level data
    public void loadLevelData() {
        // clear previous data
        levels.clear();
        // intialize gson
        Gson gson = new Gson();
        // iterate through directory
        for (final File entry : LEVEL_PATH.listFiles()) {
            // ignore entry if it is not a folder
            if (entry.isDirectory()) {
                // get json file path, assumes all json files are named 'level.json'
                File jsonPath = new File(entry.getPath() + "\\level.json");
                // attempt to read json file
                try {
                    Level level = gson.fromJson(new InputStreamReader(new FileInputStream(jsonPath)), Level.class);
                    levels.add(level);
                } catch (java.io.FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    // return settings config object from json
    public static SettingsConfig getSettingsConfig() throws FileNotFoundException {
        System.out.println("getSettingsConfig called");
        // create default settings config
        SettingsConfig config = new SettingsConfig();
        // get file path
        File settingsPath = new File(SETTINGS_PATH.getPath());
        // if the file does not exist, save the default config
        if (!settingsPath.exists()) {
            System.out.println(settingsPath.getPath() + " does not exist, creating new one");
            saveSettingsConfig(config);
        } else {
            // load config from json file
            System.out.println("loading " + settingsPath.getPath());
            Gson gson = new Gson();
            config = gson.fromJson(new InputStreamReader(new FileInputStream(settingsPath)), SettingsConfig.class);
        }
        return config;
    }
    // method to save settings config as json file
    public static void saveSettingsConfig(SettingsConfig config) {
        System.out.println("saveSettingsConfig called");
        // get path to settings file
        File settingsPath = new File(SETTINGS_PATH.getPath());
        // delete previous json file
        if (settingsPath.exists()) settingsPath.delete();
        // write config to json file
        Gson gson = new Gson();
        try {
            Writer fileWriter = Files.newBufferedWriter(SETTINGS_PATH.toPath());
            gson.toJson(config, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    // class to store level scores
    public class LevelScore {
        // level id for this score
        private int id;
        // highscore value
        private int highscore;
        // grade value
        private char grade;
        // constructors
        public LevelScore(int id, int highscore, char grade) {
            this.id = id;
            this.highscore = highscore;
            this.grade = grade;
        }
        // empty score constructor
        public LevelScore(int id) {
            this.id = id;
            this.highscore = 000000;
            this.grade = '-';
        }
        // getters
        public int getId() { return id; }
        public int getHighScore() { return highscore; }
        public char getGrade() { return grade; }
        // method to update score
        public void updateScore(int highscore, char grade) {
            this.highscore = highscore;
            this.grade = grade;
        }
    }
    // method to load player score data
    public void loadScoreData() {
        // clear previous score data
        scores.clear();
        // read json file and build score array
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<LevelScore>>(){}.getType();
        try {
            scores = gson.fromJson(new InputStreamReader(new FileInputStream(SCORE_PATH)), listType);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    // method to save player score data
    public void saveScoreData() {
        System.out.println("saveScoreData called");
        // get path to settings file
        File scorePath = new File(SCORE_PATH.getPath());
        // delete previous json file
        if (scorePath.exists()) scorePath.delete();
        // write config to json file
        Gson gson = new Gson();
        try {
            Writer fileWriter = Files.newBufferedWriter(SCORE_PATH.toPath());
            gson.toJson(scores, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    // get the score data for a given level
    public LevelScore getLevelScore(Level level) {
        // get level id
        int id = level.getId();
        // find corresponding score
        for (LevelScore score : scores) {
            if (score.getId() == id) return score;
        }
        // score not found, return and save empty score
        LevelScore empty = new LevelScore(id);
        scores.add(empty);
        saveScoreData();
        return empty;
    }
}