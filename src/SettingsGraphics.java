import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ui template class for the creation of settings page and level editor stuff
public class SettingsGraphics {
    // reference to Settings object
    Settings settings;
    // settings JPanel
    private JPanel mainPanel;
    // reference to settings config
    SettingsConfig config;
    // layout info
    private final int width = 800;
    private final int height = 450;
    private final int headerHeight = 50;
    private final int bannerHeight = 100;
    private final int headerButtonWidth = 100;
    // body banner panels
    private JPanel[] pBanners = new JPanel[4];
    // colors
    Color cHeader = new Color(151, 109, 194);
    Color cBody = new Color(138, 121, 156);
    Color cBanner = new Color(71, 49, 94);
    Color cButton = new Color(106, 65, 148);
    // constructor
    public SettingsGraphics(Settings settings) {
        // get settings object
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
        pHeader.setBackground(cHeader);
        pHeader.setLayout(null);
        pHeader.setBounds(0, 0, width, headerHeight);
        // create back button
        JButton bExit = new JButton("Exit");
        bExit.setBackground(cButton);
        bExit.setBounds(0, 0, headerButtonWidth, headerHeight);
        // create apply button
        JButton bNext = new JButton("Next");
        // add action listener to tell settings to save the config
        bNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.saveConfig();
            }
        });
        bNext.setBackground(cButton);
        bNext.setBounds(width - headerButtonWidth, 0, headerButtonWidth, headerHeight);
        // add buttons to header panel
        pHeader.add(bNext);
        pHeader.add(bExit);
        // add text to header panel
        JLabel lHeader = new JLabel("Settings");
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
        // sfx and music volume sliders
        // sfx volume slider properties
        JSlider slider_sfx = new JSlider(JSlider.HORIZONTAL, 0, 100, config.getVolumeSfx());
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
        slider_sfx.setBorder(BorderFactory.createTitledBorder("Sound Effects"));
        slider_sfx.setBounds(0, 0, (int) width / 2, bannerHeight);
        // music volume slider properties
        JSlider slider_music = new JSlider(JSlider.HORIZONTAL, 0, 100, config.getVolumeMusic());
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
        slider_music.setBorder(BorderFactory.createTitledBorder("Music"));
        slider_music.setBounds((int) width / 2, 0, (int) width / 2, bannerHeight);
        pBanners[0].add(slider_sfx);
        pBanners[0].add(slider_music);
        // button mappings and input latency calibration
        SpinnerModel latency_model = new SpinnerNumberModel(config.getLatency(), 0, 100, 1);
        JSpinner spinner_latency = new JSpinner(latency_model);
        spinner_latency.setBounds(0, (int) width / 2, (int) width / 2, bannerHeight);
        pBanners[1].add(spinner_latency);
        // framerate

        // scroll direction (?) and ghost tapping
        
        // add header and body panels to this panel
        mainPanel.add(pHeader);
        mainPanel.add(pBody);
        // refresh graphics
        refresh();
    }

    // method to refresh graphics
    public void refresh() {
        // redraw this JPanel
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // method to add validation warning to a given panel
    public static void warn(JPanel panel, String message) {
        // change styling and add message
    }
}
