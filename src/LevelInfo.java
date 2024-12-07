import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LevelInfo extends JPanel implements Scene {
    LevelInfoGraphics graphicsHandler;
    private UserData userData;
    private Level level;
    private SceneRunner sceneChanger;
    private File musicFile;
    public LevelInfo(Level level) {
        userData = new UserData();
        userData.loadLevelData();
        this.level = level;
        this.musicFile = null;
//        try {
//            this.musicFile = userData.getLevelMusicFile(level);
//        } catch (FileNotFoundException e) {
//            this.musicFile = null;
//        }
        graphicsHandler = new LevelInfoGraphics(this);
    }
    @Override
    public void update(long delta) {
        // do nothing
    }
    @Override
    public void setSceneRunner(SceneRunner sr) { this.sceneChanger = sr; }
    public JPanel getPanel() { return (JPanel) this; }
    public Level getLevel() { return level; }
    public UserData getUserData() { return userData; }

    public void edit() {
        userData.createLevelFile(level, true);
        try {
            userData.addMusicFile(level, musicFile);
            sceneChanger.changeScene(new LevelEditor(level));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateLevelSongFile(Level level) {
        // file chooser
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("WAV files", "wav");
        fileChooser.setFileFilter(filter);
        // create dialogue
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // set level song path
            musicFile = new File(fileChooser.getSelectedFile().getPath());
        }
    }

    public File getMusicFile() { return musicFile; }
    public void exit() {
        sceneChanger.changeScene(new MainMenu());
    }
}
