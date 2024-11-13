import javax.swing.JPanel;

public interface Scene {
    public void setSceneRunner(SceneRunner sceneRunner);
    public void update(long delta);
    public JPanel getPanel();
}
