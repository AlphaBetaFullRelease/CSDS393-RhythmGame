// 184 lines

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.regex.Pattern;

// ui template class for the creation of settings page and level editor stuff
public class SettingsGraphics extends JPanel {
    // reference to Settings object
    private Settings settings;
    // settings JPanel
    private JPanel mainPanel;
    // reference to settings config
    private SettingsConfig config;
  
    // layout info
    private final int width = 800;
    private final int height = 450;
    private final int headerHeight = 50;
    private final int bannerHeight = 100;
    private final int headerButtonWidth = 100;
    private final int keyBindWidth = 100;
    // body banner panels
    private JPanel[] pBanners = new JPanel[4];
    // colors
    private Color cHeader = new Color(151, 109, 194);
    private Color cBody = new Color(138, 121, 156);
    private Color cBanner = new Color(71, 49, 94);
    private Color cButton = new Color(106, 65, 148);
    // initialize array of key bind buttons
    private JButton[] bindButtons = new JButton[4];
    // bind listener
    private int keyListening = -1;
    
    private final JButton applyButton;
    // constructor
    public SettingsGraphics(Settings settings) {
        // get settings object and config
        this.settings = settings;
        this.config = settings.getSettingsConfig();
        // get settings as panel
        this.mainPanel = settings.getPanel();
        // JPanel properties
        mainPanel.setPreferredSize(new Dimension(width, height));
        mainPanel.setFocusable(true);
        mainPanel.setLayout(null);
        // create header panel
        JPanel pHeader = new JPanel();
        pHeader.setForeground(Color.white);
        pHeader.setBackground(cHeader);
        pHeader.setLayout(null);
        pHeader.setBounds(0, 0, width, headerHeight);
        // create back button
        JButton bExit = new JButton("Main Menu");
        //
        bExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                settings.exitToMenu();
            }
        });
        bExit.setForeground(Color.white);
        bExit.setBackground(cButton);
        bExit.setBounds(0, 0, headerButtonWidth, headerHeight);
        // create apply button
        applyButton = new JButton("apply");
        applyButton.setFocusable(false);
        // add action listener to tell settings to save the config
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.saveConfig();
            }
        });
        applyButton.setForeground(Color.white);
        applyButton.setBackground(cButton);
        applyButton.setBounds(width - headerButtonWidth, 0, headerButtonWidth, headerHeight);
        // add buttons to header panel
        pHeader.add(applyButton);
        pHeader.add(bExit);
        // add text to header panel
        JLabel lHeader = new JLabel("Settings");
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
            // add banner to body panel
            pBody.add(pBanners[i]);
        }
        // 0 sfx and music volume sliders
        // sfx volume slider properties
        JSlider slider_sfx = new JSlider(JSlider.HORIZONTAL, 0, 100, config.getVolumeSfx());
        slider_sfx.setFocusable(false);
        slider_sfx.setMajorTickSpacing(10);
        slider_sfx.setSnapToTicks(true);
        // save changes to the config through this change listener
        slider_sfx.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                config.setVolumeSfx(slider_sfx.getValue());
            }
        });
        // sfx slider styling
        slider_sfx.setOpaque(false);
        TitledBorder sfxBorder = BorderFactory.createTitledBorder("sound effects volume");
        sfxBorder.setTitleColor(Color.white);
        slider_sfx.setBorder(sfxBorder);
        slider_sfx.setBounds(0, 0, (int) width / 2, bannerHeight);
        // music volume slider properties
        JSlider slider_music = new JSlider(JSlider.HORIZONTAL, 0, 100, config.getVolumeMusic());
        slider_music.setFocusable(false);
        slider_music.setMajorTickSpacing(10);
        slider_music.setSnapToTicks(true);
        // save changes to the config w this change listener
        slider_music.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                config.setVolumeMusic(slider_music.getValue());
            }
        });
        // music slider styling
        slider_music.setOpaque(false);
        TitledBorder musicBorder = BorderFactory.createTitledBorder("music volume");
        musicBorder.setTitleColor(Color.white);
        slider_music.setBorder(musicBorder);
        slider_music.setBounds((int) width / 2, 0, (int) width / 2, bannerHeight);
        pBanners[0].add(slider_sfx);
        pBanners[0].add(slider_music);
        // 1 button mappings and input latency calibration
        // button mappings
        // initialize keyBind buttons and set properties
        for (int i = 0; i < bindButtons.length; i++) {
            JButton keyBind = new JButton();
            keyBind.setFocusable(false);
            keyBind.setBackground(cButton);
            keyBind.setForeground(Color.white);
            // position on screen
            keyBind.setBounds(i * keyBindWidth, 0, keyBindWidth, bannerHeight);
            // get config value
            char keyVal = ' ';
            switch (i) {
                case 0:
                    keyVal = config.getKey1();
                    break;
                case 1:
                    keyVal = config.getKey2();
                    break;
                case 2:
                    keyVal = config.getKey3();
                    break;
                case 3:
                    keyVal = config.getKey4();
                    break;
                default:
                    System.out.println("Error with key mapper, invalid index into bindButtons array");
                    break;
            }
            // set value of button to config value
            keyBind.setText(String.valueOf(keyVal));
            // set state to enabled
            keyBind.setEnabled(true);
            // add action listener
            // get index
            final int key_i = i;
            keyBind.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isListening()) {
                        // disable keyBind button
                        keyBind.setEnabled(false);
                        keyBind.setText("...");
                        // disable apply button
                        applyButton.setEnabled(false);
                        // set listening key
                        keyListening = key_i;
                        // initialize bind listener
                        settings.requestFocus();
                    }
                }
            });
            // add button to banner
            bindButtons[i] = keyBind;
            pBanners[2].add(bindButtons[i]);
        }

        // latency spinner properties
        SpinnerModel latency_model = new SpinnerNumberModel(config.getLatency(), 0, 100, 1);
        JSpinner spinner_latency = new JSpinner(latency_model);
        spinner_latency.setFocusable(false);
        ((JSpinner.DefaultEditor)spinner_latency.getEditor()).getTextField().setEnabled(false);
        //
        spinner_latency.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                config.setLatency((int) spinner_latency.getValue());
            }
        });
        // latency spinner styling
        spinner_latency.setOpaque(false);
        TitledBorder latencyBorder = BorderFactory.createTitledBorder("input latency");
        latencyBorder.setTitleColor(Color.white);
        spinner_latency.setBorder(latencyBorder);
        spinner_latency.setBounds(0, 0, (int) width / 2, bannerHeight);
        pBanners[1].add(spinner_latency);
        // ghost tapping
        JToggleButton gTapButton = new JToggleButton();
        gTapButton.setFocusable(false);
        if (config.getGhostTap()) {
            gTapButton.setText("on");
            gTapButton.setSelected(true);
        } else {
            gTapButton.setText("off");
            gTapButton.setSelected(false);
        }

        //
        gTapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gTapButton.isSelected()) {
                    config.setGhostTap(true);
                    gTapButton.setText("on");
                } else {
                    config.setGhostTap(false);
                    gTapButton.setText("off");
                }
            }
        });
        gTapButton.setOpaque(false);
        gTapButton.setBorder(BorderFactory.createTitledBorder("ghost tap"));
        gTapButton.setBounds((int) width / 2, 0, (int) width / 2, bannerHeight);
        pBanners[1].add(gTapButton);
        // add header and body panels to this panel
        mainPanel.add(pHeader);
        mainPanel.add(pBody);
        // refresh graphics
        refresh();
    }
    // listen for key input
    public void setKeyBind(int val) {
        // get current bind string
        String curBinds = "" + (char)config.getKey1() + (char)config.getKey2() + (char)config.getKey3() + (char)config.getKey4();
        // string of valid key inputs, remove current binds
        String validateStr = "1234567890-=QWERTYUIOP\\[]ASDFGHJKL;ZXCVBNM,./";
        for (char l : curBinds.toCharArray()) {
            validateStr = validateStr.replace(String.valueOf(l), "");
        }
        // validate and update button at key index
        char charVal = (char) val;
        if (validateStr.indexOf(charVal) != -1) {
            System.out.println("valid key: " + charVal);
            // set config
            switch (keyListening) {
                case 0:
                    config.setKey1(charVal);
                    break;
                case 1:
                    config.setKey2(charVal);
                    break;
                case 2:
                    config.setKey3(charVal);
                    break;
                case 3:
                    config.setKey4(charVal);
                    break;
                default:
                    System.out.println("Invalid keyListening");
                    break;
            }
            bindButtons[keyListening].setText(String.valueOf(charVal));
        } else {
            System.out.println("Invalid key");
            // restore old value text
            char keyVal = ' ';
            switch (keyListening) {
                case 0:
                    keyVal = config.getKey1();
                    break;
                case 1:
                    keyVal = config.getKey2();
                    break;
                case 2:
                    keyVal = config.getKey3();
                    break;
                case 3:
                    keyVal = config.getKey4();
                    break;
                default:
                    System.out.println("Invalid keyListening");
                    break;
            }
            bindButtons[keyListening].setText(String.valueOf(keyVal));
        }
        bindButtons[keyListening].setEnabled(true);
        applyButton.setEnabled(true);
        keyListening = -1;
    }

    // method to refresh graphics
    public void refresh() {
        // redraw this JPanel
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public boolean isListening() {
        return keyListening != -1;
    }
}
