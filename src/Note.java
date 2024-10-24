// class to store information about an active note on the screen
public class Note {
    // position (goes from 0-1 where 0 is beginning of the screen and 1 is the end of the screen)
    public double pos = 0;

    // which of the 4 possible columns the note will appear in(where 0-3 is left-more to right-most column)
    public int col;
    
    public Note(int col){
        this.col = col % 4;
    }

    public double getPos(){
        return pos;
    }

    public int getCol(){
        return col;
    }
}
