import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;

public class MainMenu extends JPanel implements ActionListener, Scene {

  private final MainMenuGraphics graphicsHandler;

  private SceneRunner sceneChanger;

  public MainMenu(){
    graphicsHandler = new MainMenuGraphics(this);
  }

  @Override
  public void update(long delta) {
  }

  @Override
  public void setSceneRunner(SceneRunner sceneRunner) { sceneChanger = sceneRunner; }

  @Override
  public JPanel getPanel() {
      return (JPanel)this;
  }

  
  
}
