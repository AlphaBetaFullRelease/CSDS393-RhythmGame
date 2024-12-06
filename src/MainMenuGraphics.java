import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuGraphics{

  private MainMenu mainMenu;

  private JPanel mainPanel;

  private final int width = 800;
  private final int height = 450;
  private final int titleHeight = 120;
  private final int buttonHeight = 110;

  private final Color cBackground = Color.cyan;
  private final Color cButtons = Color.pink;
  private final Color cText = Color.blue;
  private final Color cExtra = Color.white;

  public MainMenuGraphics(MainMenu s){
    this.mainMenu = s;
    this.mainPanel = s.getPanel();
    mainPanel.setPreferredSize(new Dimension(width, height));
    mainPanel.setFocusable(true);
    mainPanel.setLayout(null);
    
    //Title panel
    JPanel pTitle = new JPanel();
    pTitle.setLayout(null);
    pTitle.setBounds(0, 0, width, titleHeight);
    pTitle.setBackground(cBackground);
    JLabel title = new JLabel("I Can't Believe It's Not Boggle!");
    title.setBounds(0, 0, width, titleHeight);
    pTitle.add(title);
    
    //Level select panel
    JPanel pLevelSelect = new JPanel();
    pLevelSelect.setLayout(null);
    pLevelSelect.setBounds(0, titleHeight, width, buttonHeight);
    pLevelSelect.setBackground(cButtons);
    JButton levelSelect = new JButton("Level Select");
    levelSelect.setBounds(0, 0, width, buttonHeight);
    levelSelect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainMenu.levelSelect();
      }
    });
    pLevelSelect.add(levelSelect);
    levelSelect.setVisible(true);
    //Level editor panel
    JPanel pLevelEditor = new JPanel();
    pLevelEditor.setLayout(null);
    pLevelEditor.setBounds(0, titleHeight+buttonHeight, width, buttonHeight);
    pLevelEditor.setBackground(cButtons);
    JButton levelEditor = new JButton("Level Editor");
    levelEditor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainMenu.levelEditor();
      }
    });
    levelEditor.setBounds(0, 0, width, buttonHeight);
    pLevelEditor.add(levelEditor);
    levelEditor.setVisible(true);
    //Settings panel
    JPanel pSettings = new JPanel();
    pSettings.setLayout(null);
    pSettings.setBounds(0, titleHeight+(2*buttonHeight), width, buttonHeight);
    pSettings.setBackground(cButtons);
    JButton settings =  new JButton("Settings");
    settings.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainMenu.settings();
      }
    });
    settings.setBounds(0, 0, width, buttonHeight);
    pSettings.add(settings);
    settings.setVisible(true);

    mainPanel.add(pTitle);
    mainPanel.add(pLevelSelect);
    mainPanel.add(pLevelEditor);
    mainPanel.add(pSettings);

    refresh();
  }

  // method to refresh graphics
  public void refresh() {
    // redraw this JPanel
    mainPanel.revalidate();
    mainPanel.repaint();
  }
  
}
