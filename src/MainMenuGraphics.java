import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuGraphics{
  
  private MainMenu mainMenu;

  private final int width = 800;
  private final int height = 450;
  private final int titleHeight = 120;
  private final int buttonHeight = 110;

  private final Color cBackground = Color.cyan;
  private final Color cButtons = Color.pink;
  private final Color cText = Color.blue;
  private final Color cExtra = Color.white;

  public MainMenuGraphics(MainMenu s){
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
    pTitle.add(title);
    //Level select panel
    JPanel pLevelSelect = new JPanel();
    pLevelSelect.setLayout(null);
    pLevelSelect.setBounds(0, titleHeight, width, buttonHeight);
    pLevelSelect.setBackground(cButtons);
    JButton levelSelect = new JButton("Level Select");
    pLevelSelect.add(levelSelect);
    levelSelect.setVisible(true);
    //Level editor panel
    JPanel pLevelEditor = new JPanel();
    pLevelEditor.setLayout(null);
    pLevelEditor.setBounds(0, titleHeight+buttonHeight, width, titleHeight+(2*buttonHeight));
    pLevelEditor.setBackground(cButtons);
    JButton levelEditor = new JButton("Level Editor");
    pLevelEditor.add(levelEditor);
    levelEditor.setVisible(true);
    //Settings panel
    Jpanel pSettings = new JPanel();
    pSettings.setLayout(null);
    pSettings.setBounds(0, titleHeight+(2*buttonHeight), width, height);
    pSettings.setBackground(cButtons);
    JButton settings =  new JButton("Settings");
    pSettings.add(settings);
    settings.setVisible(true);
    
    
  }
  
}
