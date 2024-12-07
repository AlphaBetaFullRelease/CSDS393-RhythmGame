import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

// ui template class for the creation of settings page and level editor stuff
public class LevelInfoGraphics extends JPanel implements Scene {
    private LevelInfo levelInfo;
    private JPanel mainPanel;
    private Level level;
    private String songName;
    private boolean songUploaded;
    // layout info
    private final int width = 800;
    private final int height = 450;
    private final int headerHeight = 50;
    private final int bannerHeight = 100;
    private final int headerButtonWidth = 100;
    private final int textHeaderHeight = headerHeight;
    private final int textAreaWidth = (int) width/2;
    private final int numberAreaWidth = (int) textAreaWidth/2;
    // body banner panels
    private JPanel[] pBanners = new JPanel[4];
    // radio buttons
    private JRadioButton[] difficultyButtons;
    // text input labels
    private JLabel titleLabel;
    private JLabel creatorLabel;
    // validation
    private Boolean emptyTitle = true;
    private Boolean emptyCreator = true;
    // validation strings
    private final String emptyField = " - can't be empty!";

    // colors
    Color cHeader = new Color(151, 109, 194);
    Color cBody = new Color(138, 121, 156);
    Color cBanner = new Color(71, 49, 94);
    Color cButton = new Color(106, 65, 148);
    // constructor
    public LevelInfoGraphics(LevelInfo info) {
        this.levelInfo = info;
        if (levelInfo.getMusicFile() == null) this.songUploaded = false;
        else this.songUploaded = true;
        this.mainPanel = info.getPanel();
        this.level = info.getLevel();
        this.songName = level.getSongPath();
        // JPanel properties
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setFocusable(true);
        mainPanel.setLayout(null);
        // create header panel
        JPanel pHeader = new JPanel();
        pHeader.setBackground(cHeader);
        pHeader.setLayout(null);
        pHeader.setBounds(0, 0, width, headerHeight);
        // add text to header panel
        JLabel lHeader = new JLabel();
        lHeader.setText("Edit level info");
        lHeader.setForeground(Color.white);
        lHeader.setBounds(headerButtonWidth, 0, width - headerButtonWidth * 2, headerHeight);
        pHeader.add(lHeader);
        // create body panel
        JPanel pBody = new JPanel();
        pBody.setBackground(cBody);
        pBody.setBackground(Color.WHITE);
        pBody.setLayout(null);
        pBody.setBounds(0, headerHeight, width, height - headerHeight);
        // initialize banners
        for (int i = 0; i < pBanners.length; i++) {
            // initialize banner JPanel
            pBanners[i] = new JPanel();
            // set banner properties
            pBanners[i].setBackground(cBanner);
            pBanners[i].setLayout(null);
            pBanners[i].setBounds(0, i * bannerHeight, width, bannerHeight);
            //add banner to body panel
            pBody.add(pBanners[i]);
        }
        // banner 0 text inputs
        // label
        titleLabel = new JLabel("level name");
        titleLabel.setForeground(Color.white);
        titleLabel.setSize(textAreaWidth, textHeaderHeight);
        titleLabel.setLocation(0, 0);
        // text field
        JTextField titleField = new JTextField(level.getTitle());
        //
        titleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                reset();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                reset();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                reset();
            }
            public void reset() {
                titleLabel.setText("level name");
                titleLabel.setForeground(Color.white);
            }
        });
        titleField.setSize(textAreaWidth, textHeaderHeight);
        titleField.setLocation(0, textHeaderHeight);
        // label
        creatorLabel = new JLabel("creator name");
        creatorLabel.setForeground(Color.white);
        creatorLabel.setSize(textAreaWidth, textHeaderHeight);
        creatorLabel.setLocation(textAreaWidth, 0);
        // text field
        JTextField creatorField = new JTextField(level.getCreator());
        //
        creatorField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                reset();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                reset();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                reset();
            }
            public void reset() {
                creatorLabel.setText("creator name");
                creatorLabel.setForeground(Color.white);
            }
        });
        creatorField.setSize(textAreaWidth, textHeaderHeight);
        creatorField.setLocation(textAreaWidth, textHeaderHeight);
        // add to banner
        pBanners[0].add(titleLabel);
        pBanners[0].add(titleField);
        pBanners[0].add(creatorLabel);
        pBanners[0].add(creatorField);
        // banner 1 song
        // get song path from file
        UserData data = levelInfo.getUserData();
        // upload button
        JButton uploadButton = new JButton();
        if (!songUploaded) uploadButton.setText("Upload song file");
        else uploadButton.setText("Update song file");
        uploadButton.setBounds(0, 0, width, bannerHeight);
        // add actionListener to open file dialogue
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelInfo.updateLevelSongFile(level);
                songUploaded = true;
                refresh();
            }
        });
        // add to banner
        pBanners[1].add(uploadButton);
        // banner 2 tempo and difficulty
        // label
        JLabel tempoLabel = new JLabel("tempo");
        tempoLabel.setForeground(Color.white);
        tempoLabel.setSize(textAreaWidth, bannerHeight);
        tempoLabel.setLocation(0, 0);
        // spinner
        SpinnerModel tempoModel = new SpinnerNumberModel(level.getTempo(), 30, 400, 1);
        JSpinner tempoSpinner = new JSpinner(tempoModel);
        //
        tempoSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                level.setTempo((Integer) tempoSpinner.getValue());
            }
        });
        tempoSpinner.setSize(numberAreaWidth, bannerHeight);
        tempoSpinner.setLocation(numberAreaWidth, 0);
        // labels
        JLabel difficultyLabel = new JLabel("difficulty");
        difficultyLabel.setForeground(Color.white);
        difficultyLabel.setSize(textAreaWidth, textHeaderHeight);
        difficultyLabel.setLocation(textAreaWidth, 0);
        // radio buttons
        difficultyButtons = new JRadioButton[4];
        for (int i = 0; i < difficultyButtons.length; i ++) {
            difficultyButtons[i] = new JRadioButton();
            //
            difficultyButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // get index of button selected and pass to the update method
                    int index = Arrays.asList(difficultyButtons).indexOf(e.getSource()) + 1;
                    System.out.println("" + index);
                    updateDifficulty(index);
                    // refresh graphics
                    refresh();
                }
            });
            difficultyButtons[i].setSize((int)textAreaWidth/4, textHeaderHeight);
            difficultyButtons[i].setLocation(textAreaWidth + i * ((int) textAreaWidth/4), textHeaderHeight);
            pBanners[2].add(difficultyButtons[i]);
        }
        // update radio buttons
        updateDifficulty(level.getDifficulty());
        // add to banner
        pBanners[2].add(tempoLabel);
        pBanners[2].add(tempoSpinner);
        pBanners[2].add(difficultyLabel);
        // banner 4

        // add header and body panels to this panel
        mainPanel.add(pHeader);
        mainPanel.add(pBody);
        // create back button
        JButton bExit = new JButton("Main Menu");
        //
        bExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                levelInfo.exit();
            }
        });
        bExit.setForeground(Color.white);
        bExit.setBackground(cButton);
        bExit.setBounds(0, 0, headerButtonWidth, headerHeight);
        // create apply button
        JButton bNext = new JButton();
        bNext.setText("edit level");
        //
        bNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // validate
                validateInputs(titleField.getText(), creatorField.getText());
            }
        });
        bNext.setForeground(Color.white);
        bNext.setBackground(cButton);
        bNext.setBounds(width - headerButtonWidth, 0, headerButtonWidth, headerHeight);
        // add buttons to header panel
        pHeader.add(bNext);
        pHeader.add(bExit);
        // refresh graphics
        refresh();
    }

    public void validateInputs(String title, String creator) {
        // boolean
        Boolean pass = true;
        // check text inputs
        // trim spaces
        title = title.trim();
        creator = creator.trim();
        // check for empty strings
        emptyTitle = (title.equals(""));
        if (emptyTitle) applyMessage(titleLabel, emptyField);
        emptyCreator = (creator.equals(""));
        if (emptyCreator) applyMessage(creatorLabel, emptyField);
        // check pass
        pass = songUploaded && !emptyTitle && !emptyCreator;
        if (pass) {
            level.setTitle(title);
            level.setCreator(creator);
            levelInfo.edit();
        } else {
            // refresh graphics
            refresh();
        }
    }

    public void applyMessage(JLabel comp, String msg) {
        comp.setForeground(Color.red);
        comp.setText(comp.getText() + msg);
    }

    public void updateDifficulty(int value) {
        // iterate through list and set radio buttons
        for (int i = 0; i < difficultyButtons.length; i ++) {
            if (i < value) difficultyButtons[i].setSelected(true);
            else difficultyButtons[i].setSelected(false);
        }
        // update level file
        level.setDifficulty(value);
    }

    // method to refresh graphics
    public void refresh() {
        // redraw this JPanel
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void setSceneRunner(SceneRunner s) {}

    @Override
    public void update(long d) {}

    @Override
    public JPanel getPanel() { return this; }
}
