import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;

public class Settings extends JPanel implements ActionListener, Scene {
    // graphics handler for settings
    private SettingsGraphics graphicsHandler;
    // user data object
    private UserData userData;
    // settings config
    private SettingsConfig settingsConfig;
    // scene runner object reference
    private SceneRunner sceneChanger;
    // listener
    private BindListener bindListener;
    // constructor
    public Settings() {
        // get focus
        this.setFocusable(true);
        this.requestFocus();
        // initialize user data object
        userData = new UserData();
        // load settings config
        settingsConfig = userData.getSettingsConfig();
        // initialize the GUI
        graphicsHandler = new SettingsGraphics(this);
        // init listener
        bindListener = new BindListener();
        addKeyListener(bindListener);
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
        sceneChanger.changeScene(new MainMenu());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class BindListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (graphicsHandler.isListening()) graphicsHandler.setKeyBind(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
