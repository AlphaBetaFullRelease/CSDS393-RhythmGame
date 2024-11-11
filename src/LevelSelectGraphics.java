import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;

//does all of the graphical work for the level select
//implementation copied from GameGraphics for the purposes of the demo
public class LevelSelectGraphics {
	
	//level card list
	private ArrayList<LevelCard> list;
	
    //these variables control the layout of the level select (all in % of the screen)
    private static final double headerHeight = 0.1; // the height of the level select header
    private static final double listWidth = 0.75; // the width of the level list
    private static final double cardHeight = 0.18; // height of a single card
    private static final double[] cardTextHeight = {cardHeight * 0.33, cardHeight * 0.66}; //height of card (top and bottom) text
    private static final double cardTextMargin = 0.05; //margin between card border and text

    //needs to be passed the displayList
    public LevelSelectGraphics(){
        //populate list
    	updateList();
        //generate all dimensions for graphical objects
        Layout.initialize();
    }

    //draws all the elements to the screen
    public void drawFrame(Graphics2D g){
        //set default line thickness
        g.setStroke(new BasicStroke(2f));
        //draw the level select header
        drawHeader(g);
        //draw the level list
        drawList(g);
    }
    
    //
    private void drawHeader(Graphics2D g) {
    	//fill in header background
    	g.setColor(Colors.headerFill);
    	g.fillRect(0, 0, getWidth(), Layout.headerH);
    	
    	//draw header text
    	g.setColor(Colors.border);
    	g.drawString("Level Select " + Integer.toString(LevelSelect.getPage()) + "/" +
    	Integer.toString(LevelSelect.getNumPages()), 10, (int)Layout.headerH/2);
    	
    	//draw header border
    	g.drawLine(0, Layout.headerH, getWidth(), Layout.headerH);
    }
    
    private void drawCard(Graphics2D g, LevelCard card, int pos) {
    	//calculate y position
    	int cardPos = Layout.headerH + (Layout.cardH * pos);
    	
    	//fill in card background
    	g.setColor(Colors.cardFill);
    	g.fillRect(Layout.listLeft, cardPos, Layout.listW, Layout.cardH);
    	
    	//draw card text
    	g.setColor(Colors.border);
    	g.drawString(card.getHeader(), Layout.listLeft + Layout.textM, cardPos + Layout.textH[0]); //card header
    	g.drawString(card.getDuration(), Layout.listLeft + Layout.textM, cardPos + Layout.textH[1]); //duration
    	g.drawString(card.getDifficulty(), getWidth() - Layout.listLeft - Layout.textM, cardPos + Layout.textH[0]); //difficulty
    	g.drawString("000000", getWidth() - Layout.listLeft - Layout.textM, cardPos + Layout.textH[1]); //score
    	
    	//draw card border
    	g.drawRect(Layout.listLeft, (Layout.cardH * pos) + Layout.headerH, Layout.listW, Layout.cardH);
    	
    }
    //
    private void drawList(Graphics2D g) {
    	for(int i = 0; i < list.size(); i ++) {
    		drawCard(g, list.get(i), i);
    	}
    }
    //update display list
    public void updateList() {
    	list = LevelSelect.getPageCards();
    }

    public static int getWidth(){
        return 800;
    }

    public static int getHeight(){
        return 450;
    }

    //holds all coordinates that only need to be calculated once
    private static class Layout {
    	public static int headerH;
    	public static int listLeft; //margin between left side of the screen and the level list
    	public static int cardH;
    	public static int listW;
    	public static int[] textH = new int[2];
    	public static int textM;
        //uses the variables in GameGraphics to generate coordinates for graphical elements
        public static void initialize(){
        	headerH = (int)(headerHeight * getHeight());
        	listLeft = (int)(((1 - listWidth)/2) * getWidth());
        	cardH = (int)(cardHeight * getHeight());
        	listW = (int)(listWidth * getWidth());
        	textH[0] = (int)(cardTextHeight[0] * getHeight());
        	textH[1] = (int)(cardTextHeight[1] * getHeight());
        	textM = (int)(cardTextMargin * getWidth());
        }
    }

    // holds color information for graphics
    private static class Colors {
        // borders
        public static final Color border = new Color(61, 61, 61);
        // color for the header
        public static final Color headerFill = new Color(145, 145, 145);
        //color for the cards
        public static final Color cardFill = new Color(145, 145, 145);
    }
}
