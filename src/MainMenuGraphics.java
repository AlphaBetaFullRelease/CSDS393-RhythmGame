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

  private final Color cHeader = Color.cyan;
  private final Color cButton = Color.pink;
  private final Color cBody = Color.blue;
  private final Color cCard = Color.white;

  public MainMenuGraphics(MainMenu s){
    this.mainPanel = s.getPanel();
    mainPanel.setPreferredSize(new Dimension(width, height));
    mainPanel.setFocusable(true);
    mainPanel.setLayout(null);

    
    
  }
  
}
