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
    private int KEY1 = 'd', KEY2 = 'd', KEY3 = 'f', KEY4 = 's';
    private boolean[] down = {false, false, false, false};

    // create new input event driver with a receiver (can receive input events)
    public InputEventDriver(ActionListener actionListener, SettingsConfig config){
        // set key binds
        KEY1 = config.getKey1();
        KEY2 = config.getKey2();
        KEY3 = config.getKey3();
        KEY4 = config.getKey4();

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
        if (code == KEY1) {
            if (!down[0]) {
                down[0] = true;
                game.actionPerformed(new ActionEvent(this, 2000, null));
            }
        } else if (code == KEY2) {
            if (!down[1]) {
                down[1] = true;
                game.actionPerformed(new ActionEvent(this, 2001, null));
            }
        } else if (code == KEY3) {
            if (!down[2]) {
                down[2] = true;
                game.actionPerformed(new ActionEvent(this, 2002, null));
            }
        } else if (code == KEY4) {
            if (!down[3]) {
                down[3] = true;
                game.actionPerformed(new ActionEvent(this, 2003, null));
            }
        }
        /*
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
        }*/
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KEY1) down[0] = false;
        else if (code == KEY2) down[1] = false;
        else if (code == KEY3) down[2] = false;
        else if (code == KEY4) down[3] = false;
        /*
        switch (code) {
            case KEY1 -> down[0] = false;
            case KEY2 -> down[1] = false;
            case KEY3 -> down[2] = false;
            case KEY4 -> down[3] = false;
        }*/
    }
}
