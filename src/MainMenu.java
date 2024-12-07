import javax.swing.*;

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
    sceneChanger.changeScene(new LevelEditor(new Level(null, null, null)));
  }

  public void settings() {
    sceneChanger.changeScene(new Settings());
  }
  
}
