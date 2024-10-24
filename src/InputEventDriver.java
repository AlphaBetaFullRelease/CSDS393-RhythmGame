import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

// sends input events to the game
// this only handles input for gameplay
public class InputEventDriver implements KeyListener{
    // where to send events to (should be Game object casted down to an ActionListener)
    private final ActionListener game;
    private final int KEY1 = 90, KEY2 = 88, KEY3 = 46, KEY4 = 47;

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
        int code = e.getKeyCode();
        switch (code) {
            case KEY1 -> game.actionPerformed(new ActionEvent(this, 2000, "KEY1"));
            case KEY2 -> game.actionPerformed(new ActionEvent(this, 2000, "KEY2"));
            case KEY3 -> game.actionPerformed(new ActionEvent(this, 2000, "KEY3"));
            case KEY4 -> game.actionPerformed(new ActionEvent(this, 2000, "KEY4"));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
