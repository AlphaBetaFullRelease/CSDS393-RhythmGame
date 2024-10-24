import javax.swing.JPanel;

public interface Scene {
    public void update(long delta);
    public void changeScene(Scene scene);
    public JPanel getPanel();
}
