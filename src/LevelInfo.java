import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LevelInfo extends JPanel implements Scene {
    LevelInfoGraphics graphicsHandler;
    private UserData userData;
    private Level level;
    private SceneRunner sceneChanger;
    private boolean newLevel;
    public LevelInfo(Level level) {
        userData = new UserData();
        userData.loadLevelData();
        newLevel = (level == null);
        ArrayList<StoredNote>[] ng = new ArrayList[]{new ArrayList<StoredNote>(), new ArrayList<StoredNote>(), new ArrayList<StoredNote>(), new ArrayList<StoredNote>()};
        if (newLevel) this.level = new Level("Title", "Creator", ng);
        else this.level = level;
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
    //
    public boolean isNewLevel() { return newLevel; }
    //
    public void save() {
        userData.deleteLevelFile(level);
        userData.createLevelFile(level, true);
    }

    public void exit() {
        sceneChanger.changeScene(new LevelSelect());
    }
}
