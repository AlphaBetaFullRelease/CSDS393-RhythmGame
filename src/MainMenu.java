import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu extends JPanel implements ActionListener, Scene {

  private final MainMenuGraphics graphicsHandler;

  private SceneRunner sceneChanger;

  public MainMenu() {
    graphicsHandler = new MainMenuGraphics(this);
  }

  @Override
  public void update(long delta) {
  }

  @Override
  public void setSceneRunner(SceneRunner sceneRunner) {
    sceneChanger = sceneRunner;
  }

  @Override
  public JPanel getPanel() {
    return (JPanel) this;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // No implementation...
  }

  public void levelSelect() {
    sceneChanger.changeScene(new LevelSelect());
  }

  public void levelEditor() {
    System.out.println("(Go to level editor)");
  }

  public void settings() {
    System.out.println("(Go to settings)");
  }

}
