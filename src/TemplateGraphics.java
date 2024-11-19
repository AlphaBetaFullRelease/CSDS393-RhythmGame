import javax.swing.*;
import java.awt.*;

// ui template class for the creation of settings page and level editor stuff
public class TemplateGraphics extends JPanel implements Scene {
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
    public TemplateGraphics(String headerText) {
        // JPanel properties
        this.setPreferredSize(new Dimension(width, height));
        this.setFocusable(true);
        this.setLayout(null);
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
        bNext.setBackground(cButton);
        bNext.setBounds(width - headerButtonWidth, 0, headerButtonWidth, headerHeight);
        // add buttons to header panel
        pHeader.add(bNext);
        pHeader.add(bExit);
        // add text to header panel
        JLabel lHeader = new JLabel(headerText);
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
            // temp: add text to banner
            JLabel l = new JLabel("banner " + i);
            l.setBounds(0, 0, width, bannerHeight);
            pBanners[i].add(l);
            // add banner to body panel
            pBody.add(pBanners[i]);
        }
        // banner 0

        // banner 1

        // banner 2

        // banner 4

        // add header and body panels to this panel
        this.add(pHeader);
        this.add(pBody);
        // refresh graphics
        refresh();
    }

    // method to refresh graphics
    public void refresh() {
        // redraw this JPanel
        this.revalidate();
        this.repaint();
    }

    @Override
    public void setSceneRunner(SceneRunner s) {}

    @Override
    public void update(long d) {}

    @Override
    public JPanel getPanel() { return this; }
}
