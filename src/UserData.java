import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

// class to read user data from the data folder
public class UserData {
    // PATHS
    private static final File LOCAL_PATH = new File("./data");
    private static final File LEVEL_PATH = new File(LOCAL_PATH.getPath() + "\\levels\\");
    private static final File SETTINGS_PATH = new File(LOCAL_PATH.getPath() + "\\settings.json");
    private static final File SCORE_PATH = new File(LOCAL_PATH.getPath() + "\\scores.json");
    // level path dictionary
    private static Dictionary<Integer, String> levelPathDict = new Hashtable<>();
    // level data
    private static ArrayList<Level> levels = new ArrayList<>();
    // player scores data
    private static ArrayList<LevelScore> scores = new ArrayList<>();
    // settings data
    private SettingsConfig settingsConfig;
    // constructor
    public UserData() {
        System.out.println("user data object created");
        /*
        //DEBUG: create test level and score data
        ArrayList<StoredNote> track0 = new ArrayList<>();
        track0.add(new StoredNote(1000, 0));
        track0.add(new StoredNote(9000, 0));
        track0.add(new StoredNote(14000, 0));
        ArrayList<StoredNote> track1 = new ArrayList<>();
        track1.add(new StoredNote(750, 1));
        track1.add(new StoredNote(1500, 1));
        track1.add(new StoredNote(4000, 1));
        track1.add(new StoredNote(4500, 1));
        track1.add(new StoredNote(5000, 1));
        track1.add(new StoredNote(8750, 1));
        track1.add(new StoredNote(9500, 1));
        track1.add(new StoredNote(12000, 1));
        track1.add(new StoredNote(12500, 1));
        track1.add(new StoredNote(13500, 1));
        ArrayList<StoredNote> track2 = new ArrayList<>();
        track2.add(new StoredNote(0, 2));
        track2.add(new StoredNote(2000, 2));
        track2.add(new StoredNote(2500, 2));
        track2.add(new StoredNote(3000, 2));
        track2.add(new StoredNote(6000, 2));
        track2.add(new StoredNote(8000, 2));
        track2.add(new StoredNote(10000, 2));
        track2.add(new StoredNote(10500, 2));
        track2.add(new StoredNote(11000, 2));
        track2.add(new StoredNote(11500, 2));
        track2.add(new StoredNote(13000, 2));
        ArrayList<StoredNote> track3 = new ArrayList<>();
        track3.add(new StoredNote(6500, 3));
        track3.add(new StoredNote(7000, 3));
        ArrayList<StoredNote>[] ng = new ArrayList[]{track0, track1, track2, track3};
        Level level = new Level("Mary had no delay", "Ricardo", ng);
        createLevelFile(level, true);*/
    }
    // basic getters
    public File getLevelPath() { return LEVEL_PATH; }
    public ArrayList<Level> getLevels() { return levels; }
    // method to create or overwrite a Level json file
    public boolean createLevelFile(Level level, boolean overwrite) {
        // first check creation conditions for a file
        // get level title for folder name
        String title = level.getTitle();
        // get folder and json file path
        File folderPath = new File(LEVEL_PATH.getPath() + "\\" + title);
        File levelPath = new File(folderPath.getPath() + "\\level.json");
        // if a level file exists at the path, try to overwrite it
        if (levelPath.exists()) {
            // fail if we cannot overwrite file, otherwise delete old file
            if (!overwrite) return false;
            else levelPath.delete();
        } else if (!folderPath.exists()) folderPath.mkdir(); // create level folder if nonexistent
        // write file
        Gson gson = new Gson();
        try {
            Writer fileWriter = Files.newBufferedWriter(levelPath.toPath());
            gson.toJson(level, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    //
    public boolean deleteLevelFile(Level level) {
        System.out.println("deleting level " + level.getTitle());
        // get file and folder paths
        File folderPath = new File(levelPathDict.get(level.getId()));
        File levelPath = new File(folderPath.getPath() + "\\level.json");
        // try to delete them
        levelPath.delete();
        folderPath.delete();
        // reload level data
        loadLevelData();
        return true;
        /*
        System.out.println("attempting to delete level file");
        //
        Gson gson = new Gson();
        // iterate through directory
        for (final File entry : LEVEL_PATH.listFiles()) {
            // ignore entry if it is not a folder
            if (entry.isDirectory()) {
                // get json file path, assumes all json files are named 'level.json'
                File jsonPath = new File(entry.getPath() + "\\level.json");
                // attempt to read json file
                try {
                    FileInputStream fileInputStream = new FileInputStream(jsonPath);
                    Level check = gson.fromJson(new InputStreamReader(fileInputStream), Level.class);
                    if (level.getId() == check.getId()) {
                        System.out.println("deleting level file!");
                        try {
                            fileInputStream.close();
                            jsonPath.delete();
                            entry.delete();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        return true;
                    }
                } catch (java.io.FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("could not find level file to delete!");
        return false;*/
    }
    // method that loads the level data
    public void loadLevelData() {
        // clear previous level data and dictionary
        levelPathDict = new Hashtable<>();
        levels.clear();
        // initialize gson
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
                    levelPathDict.put(level.getId(), entry.getPath());
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
            System.out.println(settingsPath.getPath() + " does not exist, creating new one!");
            saveSettingsConfig(config);
        } else {
            // load config from json file
            System.out.println("loading " + settingsPath.getPath() + "!");
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