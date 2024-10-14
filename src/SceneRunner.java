public class SceneRunner implements Scene {
    private Scene loadedScene;
    private static long lastFrameCallTime;

    public SceneRunner() {
        // load the main scene (title screen) upon instantiating this object
    }

    @Override
    public void changeScene(Scene scene) {
        loadedScene = scene;
    }

    public static long deltaTime() {
        return System.currentTimeMillis() - lastFrameCallTime;
    }

    @Override
    public void update() {
        loadedScene.update();
        lastFrameCallTime = System.currentTimeMillis();
    }
}
