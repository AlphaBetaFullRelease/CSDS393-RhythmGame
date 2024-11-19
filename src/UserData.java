import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// class to read user data from the data folder
public class UserData {
    // PATHS, temporary for testing
    private static final File LOCAL_PATH = new File("./data");
    private static final File LEVEL_PATH = new File(LOCAL_PATH.getPath() + "\\levels\\");
    private static final File SETTINGS_PATH = new File(LOCAL_PATH.getPath() + "\\settings.json");
    // level data
    private static ArrayList<Level> levels = new ArrayList<>();
    // player scores data
    private static ArrayList<LevelScore> scores = new ArrayList<>();
    // TEMP: settings data
    private static int config;

    // constructor
    public UserData() {
        System.out.println("user data object created");
    }

    // get level path
    public File getLevelPath() {
        return LEVEL_PATH;
    }

    // method that loads both the level data and user scores
    public void loadLevelData() {
        // clear previous data
        levels.clear();
        // iterate through directory
        for (final File entry : LEVEL_PATH.listFiles()) {
            // ignore entry if it is not a folder
            if (entry.isDirectory()) {
                // get json file path, assumes all json files are named 'level.json'
                File jsonPath = new File(entry.getPath());
                // attempt to read json file
                try {
                    levels.add(Level.loadFromFile(jsonPath.getPath()));
                } catch (java.io.FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // load player scores
        File scoresJSON = new File(LOCAL_PATH.getPath() + "\\scores.json");
        // DEBUG: create levelscore
        for (Level level : levels) {
            scores.add(new LevelScore(level.getId(), 101010, 'A'));
        }
    }

    // get level list
    public ArrayList<Level> getLevels() {
        return levels;
    }

    // get levelscore for a given level
    public LevelScore getLevelScore(Level level) throws RuntimeException {
        int i = 0;
        while (i < scores.size() && scores.get(i).getId() != level.getId()) i++;
        if (i >= scores.size()) throw (new RuntimeException());
        else {
            return scores.get(i);
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

        // constructor
        public LevelScore(int id, int highscore, char grade) {
            this.id = id;
            this.highscore = highscore;
            this.grade = grade;
        }

        // getters
        public int getId() {
            return id;
        }

        public int getHighScore() {
            return highscore;
        }

        public char getGrade() {
            return grade;
        }

        // method to update score
        public void updateScore(int highscore, char grade) {
            this.highscore = highscore;
            this.grade = grade;
            //TODO: write changes to file

        }
    }
    // helper method that writes to json and returns a default settings config
    public static SettingsConfig createDefaultSettings() {
        System.out.println("creating default settings config");
        // initialize default config
        SettingsConfig defSet = new SettingsConfig();
        // write config to disk
        saveSettingsConfig(defSet);
        // return default config
        System.out.println("default settings config returned");
        return defSet;
    }
    // return settings config object from json, a slightly modified version of Level's getFromFile method
    public static SettingsConfig getSettingsConfig() throws FileNotFoundException {
        System.out.println("getting settings config");
        // get the file
        File file = new File(LOCAL_PATH.getPath() + "\\settings.json");
        // if file does not exist, initialize default config
        if (!file.exists()) {
            System.out.println("settings.json file not found");
            return createDefaultSettings();
        }
        // intialize scanner
        Scanner scanner = new Scanner(file);
        // initialize parsing values
        int volumeSfx = 0, volumeMusic = 0, frameRate = 0, latency = 0;
        char key1 = '.', key2 = '.', key3 = '.', key4 = '.';
        boolean ghostTap = false;
        String openBrackets = "";
        boolean colonSeen = false, inString = false;
        String key = "", value = "";
        // iterate through lines of the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            colonSeen = false;
            inString = false;
            key = "";
            value = "";
            // parse char by char!
            for (char c : line.toCharArray()) {
                switch (c) {
                    case '{':
                    case '[':
                        openBrackets += c;
                        break;
                    case '"':
                        if (openBrackets.lastIndexOf('"') == openBrackets.length() - 1) {
                            openBrackets = openBrackets.substring(0, openBrackets.length() - 1);
                            inString = false;
                        } else {
                            openBrackets += '"';
                            inString = true;
                        }
                        break;
                    case '}':
                        if (openBrackets.lastIndexOf('{') == openBrackets.length() - 1)
                            openBrackets = openBrackets.substring(0, openBrackets.length() - 1);
                        else throw new RuntimeException("Parse error: mismatched brackets");
                        break;
                    case ']':
                        if (openBrackets.lastIndexOf('[') == openBrackets.length() - 1)
                            openBrackets = openBrackets.substring(0, openBrackets.length() - 1);
                        else throw new RuntimeException("Parse error: mismatched brackets");
                        break;
                    case ':':
                        colonSeen = true;
                        break;
                    default:
                        if (inString) {
                            if (!colonSeen) key += c;
                            else value += c;
                        }
                        break;
                }
            }
            // set parsed values
            switch (key) {
                case "sfx":
                    volumeSfx = Integer.parseInt(value);
                    break;
                case "music":
                    volumeMusic = Integer.parseInt(value);
                    break;
                case "rate":
                    frameRate = Integer.parseInt(value);
                    break;
                case "latency":
                    latency = Integer.parseInt(value);
                    break;
                case "gTap":
                    ghostTap = Boolean.parseBoolean(value);
                    break;
                case "key1":
                    key1 = value.charAt(0);
                    break;
                case "key2":
                    key2 = value.charAt(0);
                    break;
                case "key3":
                    key3 = value.charAt(0);
                    break;
                case "key4":
                    key4 = value.charAt(0);
                    break;
                default:
                    System.out.println("ERROR: parsing default key");
                    break;
            }

        }
        // return settings config from parsed values
        System.out.println("settings config returned");
        return new SettingsConfig(volumeSfx, volumeMusic, frameRate, ghostTap, latency, key1, key2, key3, key4);
    }
    // method to 'save' settings file, even more cursed
    public static void saveSettingsConfig(SettingsConfig config) {
        System.out.println("saving settings config");
        // initialize key String value array
        String[] keys = SettingsConfig.getKeys();
        // initialize 'json' String
        String sJson = "{\n\"settings\":\n{\n";
        // iterate through the keys and just add the corresponding String
        for (final String key : keys) {
            // get the value from config that corresponds to this key
            String value = "";
            String ending = "";
            // idk how else
            switch (key) {
                case "sfx":
                    value = String.valueOf(config.getVolumeSfx());
                    ending = ",\n";
                    break;
                case "music":
                    value = String.valueOf(config.getVolumeMusic());
                    ending = ",\n";
                    break;
                case "rate":
                    value = String.valueOf(config.getFrameRate());
                    ending = ",\n";
                    break;
                case "gTap":
                    value = String.valueOf(config.getGhostTap());
                    ending = ",\n";
                    break;
                case "latency":
                    value = String.valueOf(config.getLatency());
                    ending = ",\n";
                    break;
                case "key1":
                    value = String.valueOf(config.getKey1());
                    ending = ",\n";
                    break;
                case "key2":
                    value = String.valueOf(config.getKey2());
                    ending = ",\n";
                    break;
                case "key3":
                    value = String.valueOf(config.getKey3());
                    ending = ",\n";
                    break;
                case "key4":
                    value = String.valueOf(config.getKey4());
                    ending = "\n}\n}";
                    break;
            }
            // add formatted string
            sJson += "\"" + key + "\":\"" + value  + "\"" + ending;
        }
        // get path to settings file
        File settingsPath = new File(SETTINGS_PATH.getPath());
        // delete previous json file
        if (!settingsPath.exists()) {
            settingsPath.delete();
        }
        try {
            // create new json file
            settingsPath.createNewFile();
            // write 'json' string to the file
            // create file writer
            FileWriter fw = new FileWriter(settingsPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sJson);
            bw.close();
            // DEBUG print contents of file
            //System.out.println(new FileInputStream(settingsPath));
        } catch (IOException e) {
            System.out.println("ERROR: could not write settings file!");
        }
        System.out.println("settings file written to disk");
    }
}