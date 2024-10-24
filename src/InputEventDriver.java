import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

// sends input events to the game
// this only handles input for gameplay
public class InputEventDriver implements KeyListener{
    // where to send events to (should be Game object casted down to an ActionListener)
    private final ActionListener game;

    // create new input event driver with a receiver (can receive input events)
    public InputEventDriver(ActionListener actionListener){
        // check for null
        Objects.requireNonNull(actionListener);

        // save reference
        game = actionListener;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // check if any of these keys are bound to the strike keys
        // if so, send a strike event to game
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
