import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenuGraphics {

    private MainMenu mainMenu;

    private JPanel mainPanel;

    private final int width = 800;
    private final int height = 450;
    private final int titleHeight = 120;
    private final int buttonHeight = 110;

    private final Color cBackground = new Color(151, 109, 194);
    private final Color cButtons = new Color(106, 65, 148);

    public MainMenuGraphics(MainMenu s) {
        this.mainMenu = s;
        this.mainPanel = s.getPanel();
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setFocusable(true);
        mainPanel.setLayout(null);

        // Title panel
        JPanel pTitle = new JPanel();
        pTitle.setBounds(0, 0, width, titleHeight);
        pTitle.setBackground(cBackground);
        JLabel title = new JLabel("I Can't Believe It's Not Boggle!");
        title.setFont(new Font("Arial", Font.BOLD, 52));
        title.setForeground(Color.BLACK);
        pTitle.add(title);
        mainPanel.add(pTitle);

        // Level select panel
        JPanel pLevelSelect = new JPanel();
        pLevelSelect.setVisible(true);
        JButton levelSelect = new JButton("Level Select");
        levelSelect.setBounds(0, titleHeight, width, buttonHeight);
        levelSelect.setBackground(cButtons);
        levelSelect.setForeground(Color.white);
        pLevelSelect.add(levelSelect);
        mainPanel.add(levelSelect);
        levelSelect.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mainMenu.levelSelect();
                    }
                });
        // Level editor panel
        JPanel pLevelEditor = new JPanel();
        pLevelEditor.setBackground(cButtons);
        JButton levelEditor = new JButton("Level Editor");
        levelEditor.setBounds(0, titleHeight + buttonHeight, width, buttonHeight);
        levelEditor.setBackground(cButtons);
        levelEditor.setForeground(Color.white);
        pLevelEditor.add(levelEditor);
        mainPanel.add(levelEditor);
        levelEditor.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mainMenu.levelEditor();
                    }
                });
        // Settings panel
        JPanel pSettings = new JPanel();
        pSettings.setBackground(cButtons);
        JButton settings = new JButton("Settings");
        settings.setBounds(0, titleHeight + (2 * buttonHeight), width, buttonHeight);
        settings.setBackground(cButtons);
        settings.setForeground(Color.white);
        pSettings.add(settings);
        mainPanel.add(settings);
        settings.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mainMenu.settings();
                    }
                });
    }

}