import java.awt.Graphics2D;

// does all of the graphical work for the gameplay
// uses very simple graphics for the demo, but since it's a class, we can replace it with something nicer looking later
public class GameGraphics {

    // holds all the information for the game state
    private final GameState gameState;

    // stores width & height of the game (is only set at game creation, so update these dynamically if we want to be able to resize the game)
    // do not use these variables directly! we may have to override how this works, and it'll be a lot easier if we only have to change the getter functions
    public int width;
    public int height;

    // needs to be passed a GameState object that is linked to the Game
    public GameGraphics(GameState gs){
        gameState = gs;
    }

    // draws all the elements to the screen
    public void drawFrame(Graphics2D g){
        // example moving circle to prove that things are working
        Note firstNote = gameState.getTracks().get(0).get(0);
        g.fillOval((int)(firstNote.pos * getWidth()), 100, 20, 20);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
