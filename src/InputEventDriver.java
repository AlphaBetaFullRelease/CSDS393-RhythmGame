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
    private final int KEY1 = 'Z', KEY2 = 'X', KEY3 = '.', KEY4 = '/';
    private boolean[] down = {false, false, false, false};

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
            case KEY1:
                if (!down[0]) {
                    down[0] = true;
                    game.actionPerformed(new ActionEvent(this, 2000, null));
                }
                break;
            case KEY2:
                if (!down[1]) {
                    down[1] = true;
                    game.actionPerformed(new ActionEvent(this, 2001, null));
                }
                break;
            case KEY3:
                if (!down[2]) {
                    down[2] = true;
                    game.actionPerformed(new ActionEvent(this, 2002, null));
                }
                break;
            case KEY4:
                if (!down[3]) {
                    down[3] = true;
                    game.actionPerformed(new ActionEvent(this, 2003, null));
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KEY1 -> down[0] = false;
            case KEY2 -> down[1] = false;
            case KEY3 -> down[2] = false;
            case KEY4 -> down[3] = false;
        }
    }
}
