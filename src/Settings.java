import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Settings extends JPanel implements Scene {
    // track if changes made, initialize to false
    private boolean changed = false;
    // graphics handler for settings
    private SettingsGraphics graphicsHandler;
    // user data object
    private UserData userData;
    // settings config
    private SettingsConfig settingsConfig;
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
    public void paint(Graphics g) {

    }
    @Override
    public void update(long delta) {

    }
    @Override
    public void setSceneRunner(SceneRunner sr) {

    }
    // getters
    @Override
    public JPanel getPanel() { return (JPanel) this; }
    public SettingsConfig getSettingsConfig() { return settingsConfig; }
}
