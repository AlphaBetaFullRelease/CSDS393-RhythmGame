import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JFrame;

// this is the main class. it starts the game and handles all scenes
public class SceneRunner extends JFrame {
    // target frame rate
    private final double targetFPS = 60;
    private final long targetFrameMillis = (long)(1000 / targetFPS);

    // used to calculate frame rate
    private int numFrames = 0; // keeps track of how many frames have passed
    private long frameTrackStart; // the last time that numFrames was reset
    private final int resetTrackEveryN = 60; // refreshes frame rate every n frames

    // used to calculate frame delta
    private long lastFrameCallTime;

    // the current scene loaded
    private Scene loadedScene;

    // starts the game with the main menu open
    // for now, launches straight into Game since there is no main menu
    public static void main(String[] args){
        // create level select
        LevelSelect levelSelect = new LevelSelect();
        MainMenu menu = new MainMenu();
        // create a new scenerunner with the starting scene
        SceneRunner sceneRunner = new SceneRunner(menu);
        // main loop
        while(true){
            // update loaded scene
            sceneRunner.update();

            // wait for the rest of the frame (if there is any time left)
            sceneRunner.waitUntilNextFrame();
        }
    }

    // creates new sceneRunner
    public SceneRunner(Scene scene) {
        // make sure scene is not null
        Objects.requireNonNull(scene);

        // initialize time values
        lastFrameCallTime = System.currentTimeMillis();
        frameTrackStart = System.currentTimeMillis();

        // set scene
        changeScene(scene);

        // create the window
        this.setSize(800, 450);
        this.add(scene.getPanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("I Can't Believe It's Not Boggle!!");
    }

    // load a different scene
    public final void changeScene(Scene scene) {
        // make sure new scene is not null
        Objects.requireNonNull(scene);

        // deactivate current scene (if there is one)
        if(loadedScene != null){
            loadedScene.getPanel().setVisible(false);
            this.remove(loadedScene.getPanel());
        }

        // add new scene
        this.add(scene.getPanel());

        // set sceneRunner reference
        scene.setSceneRunner(this);

        getContentPane().revalidate();

        // change scene reference
        loadedScene = scene;

        // set the focus to the new scene
        loadedScene.getPanel().setFocusable(true);
        loadedScene.getPanel().requestFocusInWindow();
    }

    // calculates and returns fps (using average number of frames passed since frameTrackStart)
    public double getFPS(){
        return (double)numFrames / (System.currentTimeMillis() - frameTrackStart) * 1000;
    }

    // returns the time delta sice the last update() was called
    private long deltaTime() {
        return System.currentTimeMillis() - lastFrameCallTime;
    }

    // updates the loaded scene and delta time
    private void update() {
        // check if frame count should be reset
        if(numFrames == resetTrackEveryN){
            numFrames = 0; // reset frame count
            frameTrackStart = System.currentTimeMillis(); // reset frame timer
        }
        // increment frame count
        numFrames++;

        // track the time before update is called
        long frameStart = System.currentTimeMillis();

        // update the current scene with the time delta since the last update
        loadedScene.update(deltaTime());

        // set the last frame call to be before update was called
        lastFrameCallTime = frameStart;
    }

    // waits until the next frame (or doesn't wait at all if more than one frame's worth of time has passed)
    private void waitUntilNextFrame(){
        // get diff between time passed and target amount of time
        long diff = targetFrameMillis - deltaTime();

        // check if any extra time is left over, then wait
        if(diff > 0)
            try{Thread.sleep(diff);}catch(InterruptedException e){}
    }
}
