import javax.swing.*;
import java.io.FileNotFoundException;

public class Settings extends JPanel implements Scene {
    // graphics handler for settings
    private SettingsGraphics graphicsHandler;
    // user data object
    private UserData userData;
    // settings config
    private SettingsConfig settingsConfig;
    // scene runner object reference
    private SceneRunner sceneChanger;
    // constructor
    public Settings() throws FileNotFoundException {
        // initialize user data object
        userData = new UserData();
        // load settings config
        settingsConfig = userData.getSettingsConfig();
        // initialize the GUI
        graphicsHandler = new SettingsGraphics(this);
    }
    @Override
    public void update(long delta) {
        // do nothing
    }
    @Override
    public void setSceneRunner(SceneRunner sr) { this.sceneChanger = sr; }
    // method to save current config, pass config to userData
    public void saveConfig() {
        // save config
        userData.saveSettingsConfig(settingsConfig);
    }
    // getters
    @Override
    public JPanel getPanel() { return (JPanel) this; }
    public SettingsConfig getSettingsConfig() { return settingsConfig; }
    // change scene to main menu
    public void exitToMenu() {
        System.out.println("return to main menu");
        sceneChanger.changeScene(new LevelSelect());
    }
}