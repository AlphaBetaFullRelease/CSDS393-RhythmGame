import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, ActionListener, Scene {
    private int noteHits;
    private int noteMisses;
    private int health;
    private int score;
    private final Timer TIMER;

    public Game() {
        // JPanel properties
        this.setPreferredSize(new Dimension(800, 450)); // screen size/resolution can be changed later, I just picked one to start
        this.setFocusable(true);
        addKeyListener(this);

        // instantiate fields
        noteHits = 0;
        noteMisses = 0;
        health = 0;
        score = 0;
        TIMER = new Timer(17, this);
        TIMER.start();
    }

    @Override
    public void update() {

    }

    @Override
    public void changeScene(Scene scene) {

    }

    @Override
    public void paint (Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
