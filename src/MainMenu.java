import javax.swing.*;
import java.util.ArrayList;

public class MainMenu extends JPanel implements Scene {

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

  public void levelSelect() {
    sceneChanger.changeScene(new LevelSelect());
  }

  public void levelEditor() {
    ArrayList[] ng = new ArrayList[4];
    ng[0] = new ArrayList<StoredNote>();
    ng[1] = new ArrayList<StoredNote>();
    ng[2] = new ArrayList<StoredNote>();
    ng[3] = new ArrayList<StoredNote>();
    Level blankLevel = new Level("blankLevel", "creator", ng);
    sceneChanger.changeScene(new LevelEditor(blankLevel));
  }

  public void settings() {
    try {
      sceneChanger.changeScene(new Settings());
    }catch(Exception e){

    }
  }
  
}
